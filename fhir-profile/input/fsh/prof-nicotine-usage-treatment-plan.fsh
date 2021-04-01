Profile: NicotineUsageTreatmentPlan
Id: nicotine-usage-treatment-plan
Title: "Nicotine Usage Treatment Plan"
Description: "A treatment plan for nicotine usage."
Parent: CarePlan
* extension contains SelfReportedSmokingStatus named selfReportedSmokingStatus 0..*
* extension[selfReportedSmokingStatus] ^short = "A report made by the patient him/herself on his/her smoking status."
* intent = #plan
* description 1..1 // in standard we should explain that this is the primary point of finding information for how the app works
                    // f.e in a patient journal, this is what would explain what this app's treatment does
* created 1..1 // when it was first created. for eila this is when starting to use app.
* addresses ^slicing.discriminator.type = #profile
* addresses ^slicing.discriminator.path = "addresses"
* addresses ^slicing.rules = #open
* addresses contains selfReportedNicotineUsage 1..1 MS
* addresses[selfReportedNicotineUsage] only Reference(SelfReportedNicotineUsage or Condition)
* addresses[selfReportedNicotineUsage] ^short = "The self-reported condition of nicotine usage treated by this plan"
* subject only Reference(SelfReportedNicotineUsingPatient or Patient)

Instance: NicotineUsageTreatmentPlanExample
Description: """
An example of a nicotine usage treatment plan. In this case, the patient has reported, successively, that they were actively smoking, then cutting down, then quit smoking. 
The description of the plan contains a summary explanation of what the DiGA does and how it works, written in a way to explain it to an external party, like a physician.
"""
InstanceOf: NicotineUsageTreatmentPlan
* extension[SelfReportedSmokingStatus][0].extension[reportedOn].valueDate = "2021-01-01"
* extension[SelfReportedSmokingStatus][0].extension[status].valueCodeableConcept = SelfReportedStatusCodeSystem#actively-smoking
* extension[SelfReportedSmokingStatus][1].extension[reportedOn].valueDate = "2021-02-01"
* extension[SelfReportedSmokingStatus][1].extension[status].valueCodeableConcept = SelfReportedStatusCodeSystem#cutting-down
* extension[SelfReportedSmokingStatus][2].extension[reportedOn].valueDate = "2021-03-01"
* extension[SelfReportedSmokingStatus][2].extension[status].valueCodeableConcept = SelfReportedStatusCodeSystem#quit-smoking
* created = "2021-01-01"
* addresses[selfReportedNicotineUsage] = Reference(SelfReportedNicotineUsageExample)
* status = #active
* intent = #plan
* subject = Reference(SelfReportedNicotineUsingPatientExample)
* description = 
    """
    A treatment plan using the Eila DiGA.
    This is a CBT-based treatment plan for nicotine usage where the patient progress towards quitting smoking by cutting down on cigarettes for a period of time.
    The patient has activities like logging their cigarettes, their triggers, and finding out which interventions are effective for them.
    ... And so on (example)
    """
