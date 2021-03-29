## Representing the condition

The profile on Condition allows the DiGA to make it clear what is being treated while at the same time making it clear that the DiGA is not making diagnosis and that the status of the condition is reported by the patient him/herself, and not otherwise monitored by the DiGA. To achieve this, the profile disallows clinical attributes like `evidence`, `clinicalStatus` etc, and adds an extension called `currentSelfReportedSmokingStatus` to allow for a readily accessible way to find out what the current status of the condition is. The condition coding points to the german base profile's ICD-10 code for nicotine addiction, F17.2.

### Condition
{{link:treat-nicotine-usage-diga/SelfReportedNicotineUsage}}
{{tree:treat-nicotine-usage-diga/SelfReportedNicotineUsage}}

### Current Self-Reported Smoking Status extension
{{link:treat-nicotine-usage-diga/SelfReportedSmokingStatus}}
{{tree:treat-nicotine-usage-diga/SelfReportedSmokingStatus}}

## Example
{{link:treat-nicotine-usage-diga/SelfReportedNicotineUsageExample}}
{{tree:treat-nicotine-usage-diga/SelfReportedNicotineUsageExample}}
