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
import ca.uhn.fhir.validation.ResultSeverityEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PatientDataExporterTest {
    private static PatientDataExporter exporter;

    @BeforeAll
    static void first() {
        exporter = new PatientDataExporter();
    }
    @Test
    void testExportsValidBundle() {
        var validator = ProfileSpecificValidatorFactory.getValidator(FhirContext.forR4());
        var export = exporter.getXmlExport(ExamplePatientTreatmentData.get());
        var res = validator.validateWithResult(export);
        var errors = res.getMessages().stream().filter(message -> message.getSeverity().equals(ResultSeverityEnum.ERROR)).collect(Collectors.toList());
        var errorsNotContainingSlicingEvaluation = errors.stream().filter(error -> !error.getMessage().contains("Slicing cannot be evaluated:")).collect(Collectors.toList());
        assertTrue(errorsNotContainingSlicingEvaluation.size() == 0);
    }

}