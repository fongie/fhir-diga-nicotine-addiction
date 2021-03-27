Instance: PatientExportBundleExample
Description: "A full example of an export containing data for a single self-reported nicotine-using patient together with all relevant resources like his/her condition, careplan, questionnaires he/she answered, as well as the replies."
InstanceOf: Bundle
* type = #document
* timestamp = "2021-03-25T13:28:17.239+02:00"
* entry[0].resource = ExportedDataCompositionExample
* entry[1].resource = InlinePatient
* entry[2].resource = AlexTherapeutics
* entry[3].resource = SelfReportedNicotineUsageExample
* entry[4].resource = NicotineTreatmentQuestionnaireExample
* entry[5].resource = NicotineTreatmentQuestionnaireResponseExample
* entry[6].resource = NicotineUsageTreatmentPlanExample