## Representing the DiGA care program

For patient journals and doctors there needs to be a quick way of discovering what the DiGA does or did when the patient was using it. For this, we use `NicotineUsageTreatmentPlan`. Here, the `description` and `purpose` attributes are key, because implementers should use these to describe what the DiGA does as part of its program, and what the purpose of it is. These texts can later go into a patient's journal when describing the treatment received. The plan also contains an optional extension `selfReportedSmokingStatus` to allow for a timeline of how the nicotine usage status of the patient has changed over time, with one record each time the patient has reported a change in the status. You can use a codesystem for this, `SelfReportedSmokingStatusCodeSystem`.

### CarePlan
{{link:treat-nicotine-usage-diga/NicotineUsageTreatmentPlan}}

{{tree:treat-nicotine-usage-diga/NicotineUsageTreatmentPlan}}

### Self Reported Smoking Status extension
{{link:treat-nicotine-usage-diga/SelfReportedSmokingStatus}}

{{tree:treat-nicotine-usage-diga/SelfReportedSmokingStatus}}

## Example
{{link:treat-nicotine-usage-diga/NicotineUsageTreatmentPlanExample}}

{{tree:treat-nicotine-usage-diga/NicotineUsageTreatmentPlanExample}}
