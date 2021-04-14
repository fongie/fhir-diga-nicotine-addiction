/*
 *  Copyright 2021-2021 Alex Therapeutics AB and individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.alextherapeutics.annotation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.util.ElementUtil;
import com.squareup.javapoet.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.BackboneElement;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.StructureDefinition;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A class being generated to define a profile on a FHIR resource.
 */
@Builder
public class GeneratedProfile extends FhirJavaFileGenerator {
    private Element element;
    private StructureDefinition resource;
    private ExtensionFromFhir[] extensions;
    private CompositeExtensionFromFhir[] compositeExtensions;
    private Messager messager;
    private ProcessingEnvironment processingEnvironment;
    private FhirContext fhirContext;
    @Override
    JavaFile toFile() {
        var superClass = ClassName.get("org.hl7.fhir.r4.model", resource.getType());
        var profilePojoBuilder = addCommon(TypeSpec.classBuilder(element.getSimpleName().toString()))
                .addAnnotation(
                        AnnotationSpec.builder(ResourceDef.class)
                                .addMember("name", "$S", resource.getType())
                                .addMember("profile", "$S", resource.getUrl())
                                .build()
                )
                .addAnnotation(Data.class)
                .superclass(superClass);
        if (hasExtensions()) {
            profilePojoBuilder
                    .addFields(buildExtensionFields(extensions, resource))
                    .addMethod(buildEmptyMethodForExtensions(extensions));
        }
        if (hasCompositeExtensions()) {
            addCompositeExtensions(profilePojoBuilder);
        }
        return JavaFile.builder(packageName, profilePojoBuilder.build()).build();
    }
    private void addCompositeExtensions(TypeSpec.Builder typeSpec) {
        for (var comppositeExtension: compositeExtensions) {
            var filePath = Paths.get("fhir", "StructureDefinition-" + comppositeExtension.id() + ".json").toString();
            var extensionDefinition = (StructureDefinition) fhirContext.newJsonParser().parseResource(GeneratedProfile.class.getClassLoader().getResourceAsStream(filePath));
            var compositeName = Arrays.stream(comppositeExtension.id().split("-")).map(StringUtils::capitalize).collect(Collectors.joining(""));
            var innerClassName = compositeName + "Extension";
            var innerClass = ClassName.get("com.alextherapeutics.model", element.getSimpleName().toString(), innerClassName);

            var extensionFields = buildExtensionFields(comppositeExtension.extensions(), extensionDefinition);
            var innerClassType = TypeSpec.classBuilder(innerClassName)
                    .addAnnotation(Block.class)
                    .addAnnotation(Data.class)
                    .addModifiers(Modifier.STATIC, Modifier.PUBLIC)
                    .addFields(extensionFields)
                    .superclass(BackboneElement.class);
            var copyMethod = MethodSpec.methodBuilder("copy")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(innerClass)
                    .addStatement("$N copy = new $N()", innerClassType.build(), innerClassType.build());

            for (var field : extensionFields) {
                copyMethod.addStatement("copy.$N = $N.copy()", field, field);
            }

            copyMethod.addStatement("return copy");
            innerClassType.addMethod(copyMethod.build());
            innerClassType.addMethod(buildEmptyMethodForExtensions(comppositeExtension.extensions()));

            var definition = findExtensionDefinition(comppositeExtension.name(), resource);
            if (definition.isEmpty()) {
                messager.printMessage(Diagnostic.Kind.ERROR, String.format("Couldnt find extension with name %s in structure definition", comppositeExtension.name()), element);
                return;
            }
            typeSpec.addField(buildExtensionField(comppositeExtension.name(), innerClass, definition.get()));
            typeSpec.addType(innerClassType.build());
        }
    }
    private Optional<ElementDefinition> findExtensionDefinition(String extensionName, StructureDefinition resource) {
        return resource
                .getDifferential()
                .getElement()
                .stream()
                .filter(Objects::nonNull)
                .filter(elementDefinition -> elementDefinition.getSliceName() != null)
                .filter(elementDefinition -> elementDefinition.getSliceName().equals(extensionName))
                .findFirst();
    }
    private List<FieldSpec> buildExtensionFields(ExtensionFromFhir[] extensions, StructureDefinition resource) {
        return Arrays.stream(extensions)
                .map(
                        extensionFromFhir -> {
                            var structureElement = findExtensionDefinition(extensionFromFhir.name(), resource);
                            if (structureElement.isEmpty()) {
                                messager.printMessage(Diagnostic.Kind.ERROR, String.format("Couldnt find extension with name %s in structure definition", extensionFromFhir.name()), element);
                                return null;
                            }
                            return buildExtensionField(extensionFromFhir, structureElement.get());
                        }
                ).collect(Collectors.toList());
    }
    private FieldSpec buildExtensionField(String name, ClassName typeName, ElementDefinition definition) {
        var min = definition.getMin();
        var max = definition.getMax() == null
                ? 1
                : definition.getMax().equals("*")
                ? Child.MAX_UNLIMITED
                : Integer.parseInt(definition.getMax());
        var list = ClassName.get("java.util", "List");
        var arrayList = ClassName.get("java.util", "ArrayList");
        var listOfType = ParameterizedTypeName.get(list, typeName);
        var finalType = max == 1 ? typeName : listOfType;
        var url = definition.getType() == null || definition.getType().isEmpty() ? name : definition.getType().get(0).getProfile().get(0).asStringValue(); // for composite extensions theres no url here, but fhir accepts just the name then
        var field = FieldSpec.builder(finalType, name, Modifier.PRIVATE)
                .addAnnotation(
                        AnnotationSpec.builder(Description.class)
                                .addMember("shortDefinition", "$S", definition.getShort())
                                .build()
                )
                .addAnnotation(
                        AnnotationSpec.builder(Extension.class)
                                .addMember("definedLocally", "$L", true)
                                .addMember("url", "$S", url)
                                .addMember("isModifier", "$L", false)
                                .build()
                )
                .addAnnotation(
                        AnnotationSpec.builder(Child.class)
                                .addMember("name", "$S", name)
                                .addMember("min", "$L", min)
                                .addMember("max", "$L", max)
                                .build()
                );
        if (max == 1) {
            field = field.initializer("new $T()", finalType);
        } else {
            field = field.initializer("new $T<>()", arrayList);
        }
        return field.build();

    }
    private FieldSpec buildExtensionField(ExtensionFromFhir extension, ElementDefinition definition) {
        if (definition == null) {
            return null;
        }
        var type = ClassName.get(getType(extension));
        return buildExtensionField(extension.name(), type, definition);
    }

    // https://stackoverflow.com/questions/7687829/java-6-annotation-processing-getting-a-class-from-an-annotation
    @SneakyThrows
    private Class getType(ExtensionFromFhir extension) {
        try {
            extension.valueType();
        } catch (MirroredTypeException e) {
            var utils = processingEnvironment.getTypeUtils();
            var type = (TypeElement) utils.asElement(e.getTypeMirror());
            return Class.forName(type.getQualifiedName().toString());
        }
        return null;
    }
    private MethodSpec buildEmptyMethodForExtensions(ExtensionFromFhir[] extensions) {
        var params = Arrays.stream(extensions)
                .map(ExtensionFromFhir::name)
                .map(CodeBlock::of)
                .collect(Collectors.toList());
        var paramsBlock = CodeBlock.join(params, ", ");

        return MethodSpec.methodBuilder("isEmpty")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(boolean.class)
                .addStatement("return super.isEmpty() && $T.isEmpty($L)", ElementUtil.class, paramsBlock)
                .build();
    }
    private boolean hasExtensions() {
        return extensions != null && extensions.length > 0;
    }
    private boolean hasCompositeExtensions() {
        return compositeExtensions != null && compositeExtensions.length > 0;
    }
}
