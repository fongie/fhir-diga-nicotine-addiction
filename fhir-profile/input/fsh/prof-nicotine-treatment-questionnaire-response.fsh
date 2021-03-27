Profile: NicotineTreatmentQuestionnaireResponse
Id: nicotine-treatment-questionnaire-response
Title: "Nicotine Treatment Questionnaire Response"
Description: "A questionnaire response dealing with nicotine as part of a treatment program"
Parent: QuestionnaireResponse
* questionnaire 1..1 // responses SHOULD link to a defined questionnaire, always, so the other party can understand what the standard forms are. and a practicioner can take a look at what the DiGA does
* source 1..1 
* source only Reference(SelfReportedNicotineUsingPatient) // SHOULD be the patient
* item.answer.value[x] from TriggerCode (example) // indicate in standard that these codes can be used for logging or other questions involved 'triggers' which is a CBT concept

Instance: NicotineTreatmentQuestionnaireResponseExample
Description: 
"""
An example of a response to a nicotine treatment questionnaire. 
In this case, the reply must be from the TriggerCode valueset. The patient answered this time that he/she was 'waiting' for something, and that triggered them to smoke.
"""
InstanceOf: NicotineTreatmentQuestionnaireResponse
* questionnaire = Canonical(InlineQuestionnaire)
* source = Reference(InlinePatient)
* status = #completed
* item[0].linkId = "TRIGGER"
* item[0].answer[0].valueCoding = TriggerCodeSystem#waiting

Instance: InlineQuestionnaire
InstanceOf: NicotineTreatmentQuestionnaire
Usage: #inline
* title = "Logging a trigger"
* description = "This questionnaire asks the patient what trigger they feel right now"
* purpose = "This enables the patient to understand their own triggers, as well as for the DiGA to gain information on which triggers are common for this patient"
* status = #active
* item[0].linkId = "TRIGGER"
* item[0].type = #choice
* item[0].text = "In what situation are you right now?"
* item[0].answerValueSet = Canonical(TriggerCode)

Instance: InlinePatient
InstanceOf: SelfReportedNicotineUsingPatient
Usage: #inline
