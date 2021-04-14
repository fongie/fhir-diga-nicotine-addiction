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
import ca.uhn.fhir.util.BundleBuilder;
import com.alextherapeutics.model.*;
import lombok.AllArgsConstructor;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Creates bundles of patient treatment data using entered patient data
 */
@AllArgsConstructor
public class PatientTreatmentDataBundleFactory {
    private FhirContext context;

    /**
     * Create a Bundle FHIR resources containing a document of patient treatment data for a single patient.
     * This method enforces a structure based on the data object to create the bundle. If you wish to have more control
     * over how the bundle and the underlying reosurces are created, you can use this method as inspiration and create
     * your own by using the profile package's resource model f.e {@link SelfReportedNicotineUsingPatient},
     * {@link NicotineUsageTreatmentPlan} etc.
     * @param data - DiGA treatment data on the patient
     * @return A FHIR {@link Bundle} containing DiGA treatment data for a single patient.
     */
    public IBaseBundle createBundle(PatientTreatmentData data) {
        var builder = new BundleBuilder(context);
        builder.setBundleField("type", Bundle.BundleType.DOCUMENT.toCode());
        builder.setBundleField("timestamp", new DateTimeType(new Date()).getValueAsString());

        var fhirTreatmentData = createFhirTreatmentData(data);

        var composition = createComposition(fhirTreatmentData, data);
        addEntry(builder, composition);

        if (fhirTreatmentData.getOrganization() != null) {
            addEntry(builder, fhirTreatmentData.getOrganization());
        }
        addEntry(builder, fhirTreatmentData.getPatient());
        addEntry(builder, fhirTreatmentData.getCondition());
        addEntry(builder, fhirTreatmentData.getPlan());
        fhirTreatmentData.getQuestionnaires().forEach(questionnaire -> addEntry(builder, questionnaire));
        fhirTreatmentData.getQuestionnairesResponses().forEach(response -> addEntry(builder, response));
        var bundle = (Bundle) builder.getBundle();
        bundle.setIdentifier(new Identifier()
                .setValue(UUID.randomUUID().toString())
                .setSystem(data.getFhirUrl() + "/exportId"));
        return builder.getBundle();
    }
    private void addEntry(BundleBuilder builder, IAnyResource resource) {
        var entry = (Bundle.BundleEntryComponent) builder.addEntry();
        entry.setFullUrl("urn:uuid:" + resource.getId().replace(" ", "-"));

        builder.addToEntry(
                entry,
                "resource",
                resource
        );
    }
    private PatientTreatmentDataFhir createFhirTreatmentData(PatientTreatmentData data) {
        var patient = createPatient(data);
        patient.setId(UUID.randomUUID().toString());
        var condition = createCondition(data, patient);
        condition.setId(UUID.randomUUID().toString());
        var plan = createCarePlan(data, patient, condition);
        plan.setId(UUID.randomUUID().toString());
        return PatientTreatmentDataFhir.builder()
                .organization(
                        (Organization) new Organization()
                                .setName(data.getOrganizationName())
                                .setId(data.getOrganizationName())
                )
                .patient(patient)
                .condition(condition)
                .plan(plan)
                .questionnaires(getNonDuplicateQuestionnaires(data.getQuestionnaireResponses()))
                .questionnairesResponses(
                        data.getQuestionnaireResponses()
                                .stream().map(response -> createResponse(response, patient)).collect(Collectors.toList())
                )
                .build();
    }
    private List<NicotineTreatmentQuestionnaire> getNonDuplicateQuestionnaires(List<PatientTreatmentData.NicotineTreatmentQuestionnaireResponse> responses) {
        return responses.stream().map(PatientTreatmentData.NicotineTreatmentQuestionnaireResponse::getQuestionnaire)
                .map(Questionnaire::getUrl)
                .distinct()
                .map(
                        url -> responses.stream().map(PatientTreatmentData.NicotineTreatmentQuestionnaireResponse::getQuestionnaire)
                                .filter(questionnaire -> url.equals(questionnaire.getUrl()))
                                .map(questionnaire -> (NicotineTreatmentQuestionnaire) questionnaire.setId(UUID.randomUUID().toString()))
                                .findAny()
                                .orElse(null)
                )
                .collect(Collectors.toList());
    }
    private NicotineTreatmentQuestionnaireResponse createResponse(PatientTreatmentData.NicotineTreatmentQuestionnaireResponse from, SelfReportedNicotineUsingPatient patient) {
        var response = new NicotineTreatmentQuestionnaireResponse();
        response.setId(UUID.randomUUID().toString());
        response.setQuestionnaire(from.getQuestionnaire().getUrl());
        response.setSource(new Reference(patient));
        response.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
        response.setItem(from.getItems());
        return response;
    }
    private NicotineUsageTreatmentPlan createCarePlan(PatientTreatmentData data, SelfReportedNicotineUsingPatient patient, SelfReportedNicotineUsage condition) {
        var plan = new NicotineUsageTreatmentPlan();
        plan.setCreated(data.getDigaTreatmentStartDate());
        plan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
        plan.setIntent(CarePlan.CarePlanIntent.PLAN);
        plan.setDescription(data.getDigaPlanDescription());
        plan.setSelfReportedSmokingStatus(
                data.getSmokingStatusList().stream()
                        .map(selfReportedSmokingStatus -> {
                            var extension = new NicotineUsageTreatmentPlan.SelfReportedSmokingStatusExtension();
                            extension.setReportedOn(new DateType(selfReportedSmokingStatus.getReportedOn()));
                            extension.setStatus(new CodeableConcept(new Coding().setCode(selfReportedSmokingStatus.getStatus().toString())));
                            return extension;
                        })
                        .collect(Collectors.toList())
        );
        plan.setSubject(new Reference(patient));
        plan.setAddresses(Collections.singletonList(new Reference(condition)));
        return plan;
    }

    private SelfReportedNicotineUsage createCondition(PatientTreatmentData data, SelfReportedNicotineUsingPatient patient) {
        var condition = new SelfReportedNicotineUsage();
        var lastReportedSmokingStatus = data.getSmokingStatusList().stream()
                .max(Comparator.comparing(PatientTreatmentData.SelfReportedSmokingStatus::getReportedOn))
                .orElse(null);
        if (lastReportedSmokingStatus != null) {
            var currentSelfReportedSmokingStatusExtension = new SelfReportedNicotineUsage.SelfReportedSmokingStatusExtension();
            currentSelfReportedSmokingStatusExtension.setReportedOn(new DateType(lastReportedSmokingStatus.getReportedOn()));
            currentSelfReportedSmokingStatusExtension.setStatus(
                    new CodeableConcept().addCoding(new Coding().setCode(lastReportedSmokingStatus.getStatus().toString()))
            );
            condition.setCurrentSelfReportedSmokingStatus(currentSelfReportedSmokingStatusExtension);
        }
        condition.setCode(
                new CodeableConcept().addCoding(
                        new Coding()
                                .setCode("F17.2")
                                .setSystem("http://fhir.de/CodeSystem/dimdi/icd-10-gm")
                                .setVersion("2021")
                )
        );
        condition.setSubject(new Reference(patient));
        return condition;
    }

    private SelfReportedNicotineUsingPatient createPatient(PatientTreatmentData data) {
        var patient = new SelfReportedNicotineUsingPatient();
        patient.setActive(true);
        patient.setName(
                Arrays.asList(
                        new HumanName().setUse(HumanName.NameUse.NICKNAME).setGiven(Arrays.asList(new StringType(data.getPatientName())))
                )
        );
        patient.setTelecom(
                Arrays.asList(
                        new ContactPoint().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue(data.getPatientEmail())
                )
        );
        patient.setGender(data.getPatientGender());
        patient.setCommonNicotineTrigger(
                data.getCommonNicotineTriggers().stream().map(trigger -> new CodeableConcept(new Coding().setCode(trigger.toString())))
                        .collect(Collectors.toList())
        );
        patient.setEffectiveNicotineIntervention(
                data.getEffectiveNicotineInterventions().stream().map(StringType::new).collect(Collectors.toList())
        );
        return patient;
    }
    private ExportedNicotineUsageTreatmentData createComposition(PatientTreatmentDataFhir fhirData, PatientTreatmentData data) {
        var composition = new ExportedNicotineUsageTreatmentData();
        composition.setId(UUID.randomUUID().toString());
        composition.setStatus(Composition.CompositionStatus.FINAL);
        composition.setType(new CodeableConcept().setTextElement(new StringType("DiGA Data Export")));
        composition.setSubject(new Reference(fhirData.getPatient()));
        composition.setDate(new Date());
        composition.setTitle("Treatment data export from DiGA \"" + data.getDigaName() + "\" for patient " + data.getPatientName());
        if (fhirData.getOrganization() != null) {
            composition.setAuthor(
                    Collections.singletonList(
                            new Reference(
                                    fhirData.getOrganization()
                            )
                    )
            );
        }
        composition.setSection(createCompositionSections(fhirData));
        return composition;
    }
    private List<Composition.SectionComponent> createCompositionSections(PatientTreatmentDataFhir fhirData) {
        var patientData = new Composition.SectionComponent()
                .setTitle("Patient data for a self-reporting nicotine-using patient")
                .addEntry(new Reference(fhirData.getPatient()));

        var conditionData = new Composition.SectionComponent()
                .setTitle("Self-reported nicotine-usage condition")
                .addEntry(new Reference(fhirData.getCondition()));

        var planData = new Composition.SectionComponent()
                .setTitle("Nicotine usage treatment plan")
                .addEntry(new Reference(fhirData.getPlan()));

        var questionnaireData = new Composition.SectionComponent().setTitle("Questionnaire definitions");
        fhirData.getQuestionnaires().stream().map(Reference::new).forEach(questionnaireData::addEntry);
        var responseData = new Composition.SectionComponent().setTitle("Questionnaire responses");
        fhirData.getQuestionnairesResponses().stream().map(Reference::new).forEach(responseData::addEntry);

        return Arrays.asList(
                patientData,
                conditionData,
                planData,
                questionnaireData,
                responseData
        );
    }
}
