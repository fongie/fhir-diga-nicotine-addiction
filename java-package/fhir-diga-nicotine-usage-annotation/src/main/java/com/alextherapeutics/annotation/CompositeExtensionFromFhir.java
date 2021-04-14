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
 * Defines a composite extension contained in a resource. Composite extensions are extensions that contain
 * several fields, not just a value. For example, the "selfReportedSmokingStatus" extension has two fields, "status" and "reportedOn".
 */
public @interface CompositeExtensionFromFhir {
    /**
     * The id of the extension. A 'fhir/StructureDefinition-{id}.json' must exist in the classpath.
     * @return
     */
    String id();

    /**
     * The common name of the extension in the parent resource. For example, if you had a composite extension and you name it
     * "myCompositeExtension", that should be this value.
     * @return
     */
    String name();

    /**
     * Definitions for the fields contained in the composite extension.
     * @return
     */
    ExtensionFromFhir[] extensions();
}
