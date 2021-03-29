## Representing app activities

App activities can vary between DiGAs so implementors are free to represent these in various ways including CarePlan's `activity`. One common demoninator for apps treating nicotine usage however is that the patient's are often encouraged to track, _log_, their cigarettes. Often, the patient also answers questionnaires about various things, to gather information about their usage and to allow the patient to gain insight themselves on how their addiction works.

To represent these activities, logging and questionnaires, implementors should use `NicotineTreatmentQuestionnaire` to define how the questionnaire is presented in the application, and `NicotineTreatmentQuestionnaireResponse` to represent an individual patient's response to it. You are required to give a title, description and purpose to the questionnaire to explain what it does.

### Nicotine Treatment Questionnaire
{{link:treat-nicotine-usage-diga/NicotineTreatmentQuestionnaire}}
{{tree:treat-nicotine-usage-diga/NicotineTreatmentQuestionnaire}}

### Nicotine Treatment Questionnaire Response
{{link:treat-nicotine-usage-diga/NicotineTreatmentQuestionnaireResponse}}
{{tree:treat-nicotine-usage-diga/NicotineTreatmentQuestionnaireResponse}}

## Example

### Questionnaire
{{link:treat-nicotine-usage-diga/NicotineTreatmentQuestionnaireExample}}
{{tree:treat-nicotine-usage-diga/NicotineTreatmentQuestionnaireExample}}

### QuestionnaireResponse
{{link:treat-nicotine-usage-diga/NicotineTreatmentQuestionnaireResponseExample}}
{{tree:treat-nicotine-usage-diga/NicotineTreatmentQuestionnaireResponseExample}}
