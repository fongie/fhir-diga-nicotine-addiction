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

/**
 * A definition of an extension contained in a resource.
 */
public @interface ExtensionFromFhir {
    /**
     * The name of the extension. This must be the name of the slice on 'extension' on the parent resource.
     * For example, if you defined an extension called "effectiveNicotineIntervention", then that should be the name.
     * @return the name
     */
    String name();

    /**
     * The HAPI type of the extension type. For example, if your extension is of the value type {@link org.hl7.fhir.r4.model.StringType},
     * then that class should be this value. This is the reason we cannot generate the extensions from the parent profile definition - the
     * extension value type is not present there.
     * @return the value type class
     */
    Class<?> valueType();
}
