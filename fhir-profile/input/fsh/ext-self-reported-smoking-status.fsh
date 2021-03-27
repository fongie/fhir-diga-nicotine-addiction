Extension: SelfReportedSmokingStatus
Id: self-reported-smoking-status
Title: "Self Reported Smoking Status"
Description: "A report on the smoking status made by the patient him/herself at a point in time. Used for example when a digital application asks the patient if they are smoke free, or when a patient reports that they have relapsed."
* extension contains
    reportedOn 1..1 and
    status 1..1
* extension[reportedOn] ^short = "The date this report was made"
* extension[reportedOn].value[x] only date
* extension[reportedOn].valueDate 1..1
* extension[status] ^short = "The status that was reported"
* extension[status].value[x] only CodeableConcept
* extension[status].valueCodeableConcept from SelfReportedSmokingStatusCode (preferred)