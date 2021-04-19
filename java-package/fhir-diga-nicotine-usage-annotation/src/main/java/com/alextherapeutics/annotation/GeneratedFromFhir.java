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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate that this field should generate a class from a FHIR structure definition located in resources/fhir/
 * Fields annotated with this will generate a class with the same name as the field.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.FIELD)
public @interface GeneratedFromFhir {
    /**
     * The FHIR resource ID. This must correspond to the filename of the FHIR resource, for example:
     * StructureDefinition-exported-nicotine-usage-treatment-data.json gives an id of 'exported-nicotine-usage-treatment-data'
     * @return the ID
     */
    String id();

    /**
     * The type of resource this generates.
     * @return the enum fhir type
     */
    FhirType type() default FhirType.PROFILE;

    /**
     * Extension definitions contained in this resource. Unfortunately it is not possible to generate these from the
     * FHIR files in all cases, so they are defined separately here.
     * @return the extensions as a list
     */
    ExtensionFromFhir[] extensions() default {};

    /**
     * Composite extension definitions defined in this resource.
     * @return the composite extensions as a list
     */
    CompositeExtensionFromFhir[] compositeExtensions() default {};
}
