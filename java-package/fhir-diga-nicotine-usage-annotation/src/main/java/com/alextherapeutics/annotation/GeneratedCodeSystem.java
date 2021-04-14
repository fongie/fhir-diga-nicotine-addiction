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

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hl7.fhir.r4.model.CodeSystem;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * A class being generated to define a {@link CodeSystem}
 */
@AllArgsConstructor
class GeneratedCodeSystem extends FhirJavaFileGenerator {
    private Element element;
    private CodeSystem resource;

    @Override
    JavaFile toFile() {
        var codeEnumBuilder = addCommon(TypeSpec.enumBuilder(element.getSimpleName().toString()))
                .addAnnotation(AllArgsConstructor.class)
                .addAnnotation(Getter.class)
                .addField(String.class, "code", Modifier.PRIVATE)
                .addField(String.class, "display", Modifier.PRIVATE);
        for (var concept : resource.getConcept()) {
            codeEnumBuilder.addEnumConstant(
                    concept.getCode(),
                    TypeSpec.anonymousClassBuilder(
                            "$S, $S",
                            concept.getDisplay(),
                            concept.getDefinition()
                    ).build()
            );
        }
        return JavaFile.builder(packageName, codeEnumBuilder.build())
                .build();
    }
}
