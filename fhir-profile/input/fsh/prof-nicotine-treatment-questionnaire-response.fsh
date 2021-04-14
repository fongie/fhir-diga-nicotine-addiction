/*
 * Copyright 2021-2021 Alex Therapeutics AB and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

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
* questionnaire = Canonical(QuestionnaireExample)
* source = Reference(SelfReportedNicotineUsingPatientExample)
* status = #completed
* item[0].linkId = "TRIGGER"
* item[0].answer[0].valueCoding = TriggerCodeSystem#waiting

Instance: QuestionnaireExample
InstanceOf: NicotineTreatmentQuestionnaire
Usage: #inline
* url = "http://my-url/Questionnaire/logging"
* title = "Logging a trigger"
* description = "This questionnaire asks the patient what they are feeling or which situation they are in right now"
* purpose = "This enables the patient to understand their own triggers, as well as for the DiGA to gain information on which triggers are common for this patient"
* status = #active
* item[0].linkId = "TRIGGER"
* item[0].type = #choice
* item[0].text = "In what situation are you right now?"
* item[0].answerValueSet = Canonical(TriggerCode)
