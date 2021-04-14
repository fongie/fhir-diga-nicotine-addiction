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

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Generated;
import javax.lang.model.element.Modifier;
import java.util.Date;

/**
 * A class that generates java files extending a FHIR resource
 */
abstract class FhirJavaFileGenerator {
    protected static final String packageName = "com.alextherapeutics.model";

    /**
     * Generate a java source file.
     * @return
     */
    abstract JavaFile toFile();

    protected TypeSpec.Builder addCommon(TypeSpec.Builder builder) {
        return builder.addAnnotation(
                AnnotationSpec.builder(Generated.class)
                        .addMember("value", "$S", this.getClass().getCanonicalName())
                        .addMember("date", "$S", new Date().toString())
                        .build()
        )
                .addModifiers(Modifier.PUBLIC);
    }
}
