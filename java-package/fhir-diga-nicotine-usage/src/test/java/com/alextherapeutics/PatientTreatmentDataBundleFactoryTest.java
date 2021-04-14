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

package com.alextherapeutics;

import ca.uhn.fhir.context.FhirContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PatientTreatmentDataBundleFactoryTest {
    private static PatientTreatmentDataBundleFactory factory;
    private static FhirContext context;

    @BeforeAll
    static void first() {
        context = FhirContext.forR4();
        factory = new PatientTreatmentDataBundleFactory(context);
    }

    @Test
    void testCreateBundle() {
        var b = factory.createBundle(ExamplePatientTreatmentData.get());
    }

}