// make separate fagerstrom questionnaire profile - if new onboarding

Profile: NicotineTreatmentQuestionnaire
Id: nicotine-treatment-questionnaire
Title: "Nicotine Treatment Questionnaire"
Description: "A questionnaire belonging to a nicotine reducing care program."
Parent: Questionnaire
* title 1..1
* description 1..1 // explain that this is the primary spot to look for "what was asked" for journals
* purpose 1..1 // a manufacturer should use these fields to explain in a humnan-friendly way what this questionnaire is for and what it does

Instance: NicotineTreatmentQuestionnaireExample
Description: 
"""
An example of a nicotine treatment questionnaire. This questionnaire is a 'Logging' questionnaire, where the patient can answer questions when they feel the need to smoke.
The answers are coded in valuesets containing common triggers and effective interventions.
"""
InstanceOf: NicotineTreatmentQuestionnaire
* status = #active
* title = "Logging"
* description = 
"""
This questionnaire is given to the patient each time they feel an urge to smoke. 
It asks about the current situation and triggers, and optionally asks the patient if they wish to use an intervention. 
At last, the patient answers whether they smoked or not.
"""
* purpose =
"""
This questionnaire finds out information about common triggers for the patient, as well as effective interventions, and helps the patient understand these themselves.
"""
* item[0].linkId = "SITUATION"
* item[0].type = #choice
* item[0].text = "Which situation are you in?"
* item[0].answerValueSet = Canonical(TriggerCode)
* item[1].linkId = "FEELING"
* item[1].type = #choice
* item[1].text = "How are you feeling?"
* item[1].answerValueSet = Canonical(TriggerCode)
* item[2].linkId = "WITHDRAWAL"
* item[2].type = #choice
* item[2].text = "Are you experiencing any physical symptoms?"
* item[2].answerValueSet = Canonical(TriggerCode)
* item[3].linkId = "INTERVENTION"
* item[3].type = #choice
* item[3].text = "Would you like to try an intervention?"
* item[3].answerValueSet = Canonical(EffectiveInterventionCode)
* item[4].linkId = "DECISION"
* item[4].type = #choice
* item[4].text = "Did you smoke?"
* item[4].answerValueSet = Canonical(YesOrNo)

ValueSet: YesOrNo
* include codes from system http://terminology.hl7.org/CodeSystem/v2-0136