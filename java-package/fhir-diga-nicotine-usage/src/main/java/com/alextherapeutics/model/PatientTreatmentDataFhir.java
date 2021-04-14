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

package com.alextherapeutics.model;

import lombok.Builder;
import lombok.Getter;
import org.hl7.fhir.r4.model.Organization;

import java.util.ArrayList;
import java.util.List;

/**
 * The treatment data as package-specific FHIR resources
 * Excluding the composition, because it requires access to the other resources.
 */
@Builder
@Getter
public class PatientTreatmentDataFhir {
    private SelfReportedNicotineUsingPatient patient;
    private SelfReportedNicotineUsage condition;
    private NicotineUsageTreatmentPlan plan;
    @Builder.Default
    private List<NicotineTreatmentQuestionnaire> questionnaires = new ArrayList<>();
    @Builder.Default
    private List<NicotineTreatmentQuestionnaireResponse> questionnairesResponses = new ArrayList<>();
    private Organization organization;
}
