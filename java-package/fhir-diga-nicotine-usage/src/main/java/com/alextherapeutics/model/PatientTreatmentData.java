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
import lombok.NonNull;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.QuestionnaireResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Object to create a Bundle for exporting one patient's data.
 */
@Builder
@Getter
public class PatientTreatmentData {
    /**
     * The name of the DiGA exporting this data.
     */
    @NonNull
    private String digaName;
    /**
     * The name of the organization exporting this data (optional).
     */
    private String organizationName;
    /**
     * A root url to start FHIR uris with. F.e http://www.my-org.com/fhir
     */
    @NonNull
    private String fhirUrl;
    /**
     * A description of what the DiGA's treatment plan does.
     */
    @NonNull
    private String digaPlanDescription;
    /**
     * Name of the patient. Will be exported as a 'nickname'
     */
    @NonNull
    private String patientName;
    /**
     * The patient's email
     */
    @NonNull
    private String patientEmail;
    /**
     * Gender of the patient
     */
    private Enumerations.AdministrativeGender patientGender;
    /**
     * Common nicotine triggers for the patient, as determined by the DiGA
     */
    @Builder.Default
    private List<TriggerCode> commonNicotineTriggers = new ArrayList<>();
    /**
     * Effective nicotine interventions for the patient, as determined by the DiGA
     */
    @Builder.Default
    private List<String> effectiveNicotineInterventions = new ArrayList<>();
    /**
     * A list of all smoking status updates made by the patient.
     */
    @Builder.Default
    private List<SelfReportedSmokingStatus> smokingStatusList = new ArrayList<>();

    /**
     * When the patient started their treatment using the DiGA. This becomes the CarePlan's {@link CarePlan#getCreated()} value.
     */
    @NonNull
    private Date digaTreatmentStartDate;

    /**
     * Attach patient responses to questionnaires. You only need to add the questionnaire items (with answers etc)
     */
    @Builder.Default
    private List<NicotineTreatmentQuestionnaireResponse> questionnaireResponses = new ArrayList<>();

    /**
     * A smoking status update made by the patient.
     */
    @Builder
    @Getter
    public static class SelfReportedSmokingStatus {
        /**
         * Date the report was made
         */
        @NonNull
        private Date reportedOn;
        /**
         * The status that was reported
         */
        @NonNull
        private SelfReportedSmokingStatusCode status;
    }

    /**
     * A questionnaire response together with the questionnaire that was responded to.
     */
    @Builder
    @Getter
    public static class NicotineTreatmentQuestionnaireResponse {
        /**
         * The questionnaire that was responded to.
         * Make sure to set the canonical 'url' field.
         */
        private NicotineTreatmentQuestionnaire questionnaire;
        /**
         * Items containing answers to the questionnaire.
         * Other fields on the QuestionnaireResponse such as source, questionnaire, etc. are set automatically based
         * on other data.
         */
        private List<QuestionnaireResponse.QuestionnaireResponseItemComponent> items;
    }
}
