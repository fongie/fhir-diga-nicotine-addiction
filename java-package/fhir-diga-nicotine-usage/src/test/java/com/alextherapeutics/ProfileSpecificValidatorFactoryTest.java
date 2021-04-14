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
import ca.uhn.fhir.validation.FhirValidator;
import com.alextherapeutics.model.SelfReportedNicotineUsingPatient;
import com.alextherapeutics.model.TriggerCode;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ProfileSpecificValidatorFactoryTest {
    private static FhirValidator validator;

    @BeforeAll
    static void first() {
        validator = ProfileSpecificValidatorFactory.getValidator(FhirContext.forR4());
    }

    @Test
    void validatorValidatesExamplePatient() {
        var ctxt = FhirContext.forR4();
        var patient = new SelfReportedNicotineUsingPatient();
        patient.setActive(true);
        var codeable = new CodeableConcept();
        codeable.setCoding(Arrays.asList(new Coding().setCode(TriggerCode.coffee.getCode())));
        patient.getEffectiveNicotineIntervention().add(
                new StringType("drink-water")
        );
        var r = ctxt.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
        var s = ctxt.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);

        Assertions.assertTrue(
                validator.validateWithResult(r).isSuccessful()
        );
        Assertions.assertTrue(
                validator.validateWithResult(s).isSuccessful()
        );
        Assertions.assertTrue(
                validator.validateWithResult(patient).isSuccessful()
        );
    }
}