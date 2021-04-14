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
import com.google.auto.service.AutoService;
import org.hl7.fhir.r4.model.StructureDefinition;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Annotation processor for the {@link GeneratedFromFhir} annotation.
 * This processor will generate java source files based on fields tagged with {@link GeneratedFromFhir}.
 */
@AutoService(Processor.class)
public class GeneratedFromFhirProcessor extends AbstractProcessor {
    private Messager messager;
    private Filer filer;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        var annotations = new LinkedHashSet<String>();
        annotations.add(GeneratedFromFhir.class.getCanonicalName());
        return annotations;
    }
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        var elements = roundEnv.getElementsAnnotatedWith(GeneratedFromFhir.class);
        for (var element: elements) {
            if (element.getKind() != ElementKind.FIELD) {
                error(element, "The @%s annotation only works for fields");
                return true;
            }
            var fhirType = element.getAnnotation(GeneratedFromFhir.class).type();
            var fhirId = element.getAnnotation(GeneratedFromFhir.class).id();
            var extensions = element.getAnnotation(GeneratedFromFhir.class).extensions();
            var compositeExtensions = element.getAnnotation(GeneratedFromFhir.class).compositeExtensions();
            Path path;
            if (fhirType == FhirType.CODE_SYSTEM) {
                path = Paths.get("fhir", "CodeSystem-" + fhirId + ".json");
            } else {
                path = Paths.get("fhir", "StructureDefinition-" + fhirId + ".json");
            }
            var file = GeneratedFromFhirProcessor.class.getClassLoader().getResourceAsStream(path.toString());
            var ctxt = FhirContext.forR4();
            var resource = ctxt.newJsonParser().parseResource(file);
            FhirJavaFileGenerator fileGenerator;
            if (fhirType == FhirType.CODE_SYSTEM) {
                fileGenerator = new GeneratedCodeSystem(element, (org.hl7.fhir.r4.model.CodeSystem) resource);
            } else {
                fileGenerator = GeneratedProfile.builder()
                        .element(element)
                        .resource((StructureDefinition) resource)
                        .extensions(extensions)
                        .compositeExtensions(compositeExtensions)
                        .messager(messager)
                        .processingEnvironment(processingEnv)
                        .fhirContext(ctxt)
                        .build();
            }
            try {
                fileGenerator.toFile().writeTo(filer);
            } catch (IOException e) {
                error(element, e.getMessage());
            }
        }
        return true;
    }

    private void error(Element e, String msg, Object ...args) {
        messager.printMessage(
                Diagnostic.Kind.ERROR,
                String.format(msg, args),
                e
        );
    }
}
