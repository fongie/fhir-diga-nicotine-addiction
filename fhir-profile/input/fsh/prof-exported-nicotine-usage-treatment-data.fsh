Profile: ExportedNicotineUsageTreatmentData
Id: exported-nicotine-usage-treatment-data
Title: "Exported Nicotine Usage Treatment Data"
Description: "A bulk export containing all data pertaining to a single patient being treated for nicotine usage"
Parent: Composition
* type.text = "DiGA Data Export"
* subject 1..1 
* subject only Reference(SelfReportedNicotineUsingPatient or Patient)
* status = #final
* title 1..1
* section ^slicing.discriminator.type = #value
* section ^slicing.discriminator.path = "section"
* section ^slicing.rules = #closed
* section contains patientData 1..1 MS
* section[patientData] ^short = "Data on the self-reported nicotine-using patient"
* section[patientData].title = "Self-reported nicotine-using patientdata"
* section[patientData].entry 1..1 MS
* section[patientData].entry only Reference(SelfReportedNicotineUsingPatient or Patient) 
* section contains selfReportedCondition 1..1 MS
* section[selfReportedCondition] ^short = "Data on the self-reported condition of nicotine usage"
* section[selfReportedCondition].title = "Self-reported nicotine-usage condition"
* section[selfReportedCondition].entry 1..1 MS
* section[selfReportedCondition].entry only Reference(SelfReportedNicotineUsage or Condition)
* section contains nicotineUsageTreatmentPlan 1..1 MS
* section[nicotineUsageTreatmentPlan] ^short = "The treatment plan for the self-reported nicotine usage"
* section[nicotineUsageTreatmentPlan].title = "Nicotine usage treatment plan"
* section[nicotineUsageTreatmentPlan].entry only Reference(NicotineUsageTreatmentPlan or CarePlan)
* section contains questionnaires 1..1 MS
* section[questionnaires] ^short = "Definitions for questionnaires during the nicotine usage treatment"
* section[questionnaires].title = "Questionnaire definitions"
* section[questionnaires].entry only Reference(NicotineTreatmentQuestionnaire or Questionnaire)
* section contains questionnaireResponses 1..1 MS
* section[questionnaireResponses] ^short = "Questionnaire responses by the patient during the nicotine treatment"
* section[questionnaireResponses].title = "Questionnaire responses"
* section[questionnaireResponses].entry only Reference(NicotineTreatmentQuestionnaireResponse or QuestionnaireResponse)

Instance: ExportedDataCompositionExample
InstanceOf: ExportedNicotineUsageTreatmentData
Description: "An example of a composition of exported nicotine usage treatment data"
* date = "2021-03-25"
* author = Reference(AlexTherapeutics)
* type.text = "DiGA Data Export"
* subject = Reference(InlinePatient)
* title = "Eila data export for patient 'Max'"
* section[patientData].entry = Reference(InlinePatient)
* section[selfReportedCondition].entry = Reference(SelfReportedNicotineUsageExample)
* section[nicotineUsageTreatmentPlan].entry = Reference(NicotineUsageTreatmentPlanExample)
* section[questionnaires].entry[0] = Reference(NicotineTreatmentQuestionnaireExample)
* section[questionnaireResponses].entry[0] = Reference(NicotineTreatmentQuestionnaireResponseExample)

Instance: AlexTherapeutics
InstanceOf: Organization
Usage: #inline
* name = "Alex Therapeutics"