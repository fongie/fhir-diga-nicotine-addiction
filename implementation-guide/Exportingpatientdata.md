## Exporting patient data

DiGAs are required to be able to export a single patient's data. To do this, implementers must export them as a `Bundle`. In the bundle, you must include the `ExportedNicotineUsageTreatmentData` profile on `Composition` which has details on how the bundle is composed. The composition's `section` is sliced to include pointers to the resources which must be included in the bundle: `patientData`, `selfReportedCondition`, `nicotineUsageTreatmentPlan`, `questionnaires`, `questionnaireResponses`.

### Exported Nicotine Usage Treatment Data
{{link:treat-nicotine-usage-diga/ExportedNicotineUsageTreatmentData}}
{{tree:treat-nicotine-usage-diga/ExportedNicotineUsageTreatmentData}}

## Example Bundle
{{link:treat-nicotine-usage-diga/PatientExportBundleExample}}
{{tree:treat-nicotine-usage-diga/PatientExportBundleExample}}
