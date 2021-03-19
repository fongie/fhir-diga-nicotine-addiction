# Plan

basis de IG suggests looking for other derivations before proceeding, one of them is "IPS" (international patient summary) which is a FHIR profile.
we can use that too since it tries to be "global"

rehappy in their standard refrain from "LOINC" codes because they are diagonostical and their app doesnt do diagnosis, only assessment based on the patient's own input
could be a useful excuse

## Person
We should use PERSON as a WRAPPER for the rest of the data (user == person), like Rehappy does
Here we could mandate that no personal details are entered on person, but on patient instead (and link to a patient ,like rehappy does)

## Patient
German doesnt have its own profile but it has extensinos.
They refer to using https://simplifier.net/guide/basisprofil-de-r4/Ressourcen-Extern-Observation-Anamnese for smoking status.
It will have "current every day smoker, former smoker" etc

maybe we should split "name" into something like "given name, last name, user name" (?)

## Condition
https://www.hl7.org/fhir/condition.html

Condition should probably be set to "smoker" or "nicotine addicted" or similar for patients who are not smoke free.
When smoke-free, the condition that was set should have its "abated" date set - which means it has stopped
There is a "clinicalStatus" code here which can be active | recurrence | relapse | inactive | remission | resolved. It should be active when patient is smoking, "remission" when smoke free ("The subject is no longer experiencing the symptoms of the condition, but there is a risk of the symptoms returning.") and "relapse" when relapsed.
There is a "resolved" too for when there is negligble risk of return. Should we set that and when?

_code_ attribute in Condition is set to mandatory by IPS. German profile says that when possible you should use ICD-10 codes here https://simplifier.net/guide/basisprofil-de-r4/Datentypen-ICD-10GM-Coding .
For Eila this is F17.2 . There is a slice in code for IPS which might be trouble since it specifies SNOMED codes, so maybe not use IPS. German says use "coding" as F17.2
There is info on extensions for the coding here https://simplifier.net/guide/basisprofil-de-r4/ICD10Codierung-Extension

## CarePlan
This will probably need to be our deescelation plans

## Goal
This will be "stop smoking" and will be connected to the care plan

## PlanDefinition
We can use this to define a deesc plan and use hat to generate careplans(?)
"Create a CarePlan resource focused on the Patient in context and linked to the PlanDefinition using the instantiates element"

## Communication
We can record things we communicated to the user with this (maybe). Like, notifications and hero messages like "do this, go through your day), etc

## Questionnaires

Our data on conversations like motivational level, triggers etc can probably be created as "Questionnaire"s
see https://simplifier.net/guide/basisprofil-de-r4/Ressourcen-VerweiseaufExterneLeitfaeden for german basis info
the questionnaire will include what was asked - not wht was replied. for that, use QuestionnaireResponse
We should have responses for our cnovos, but perhaps we shall also use questionnaires for our checkin logs. f.e the questionnaire would be 4 questions "what situation are you in, what are you feeling, did you smoke or not". and each checkin would be a response


Next step: start creating profiles for Patient, Condition, Questionnaires, and try to find out how we can use "Observation" (?) for logged checkins
import IPS too and check it


# Mappings

AppUser -> Person
AppUser.email -> Patient.telecom

HabitProgram.isQuit -> Goal/Condition/Observation extension ("hasQuit" or "smokeFree" or..). Should be 0..* so you can relapse etc and have some sort of start and quitdate
<- vi kör rökfri som observationer där man säger att "för 2 veckor sen sa patienten att den var rökfri >

DeescalationPlan -> CarePlan, PlanDefinition(?) - might be difficult and need an extension. man kan säga att logging och aktivitet är npågot man ägnar sig åt under perioden
i careplan så beskriv att vi gör kbt med aktiviteter (logging, aktiviteter) med
nertrappning
en careplan tar inte slut när man är rökfri utan careplanen inkluderar relapse-prevention. den är lika för alla med en beskrivning av vad man gör

"use CBT to treat nicotine dependance"

MyMotivation -> Questionnaire
TriggerIdentify -> Questionnaire
TriggerSort -> Questionnaire (? or leave out)

Questions -> QuestionnaireResponse
Checkins -> Questionnaire & QuestionnaireResponse. We could also maybe "observe" that they smoked
Triggers -> ? maybe an observation extension "commonTriggers" or something. Probably leave out

Milestones -> Communication maybe?

EpisodeOfCare - här kan vi skriva in vår organisation som att vi "tar hand om patienten"


## Frågor:

- Hur medicinska ska vi vara. Får vi t.ex sätta condition = smoking, och sen ändra den till "smoke free" när användaren säger att den är det. Får vi använda de extension patient->smoking status och sätta smoker, former smoker, etc. när de rapporterar i appen
- Kan vi kalla vår deescplan för en "careplan". Eller ska vi lämna nedtrappningen ute helt
- Bör vi prata om "patient"
- Vad kan man kalla 'hacks' för. En observation med en summering kanske? Men hur kodar man dem då ..
- Kan vi använda "episodeofcare" när specen är att man "assume some level of responsibility" för patienten
- Borde vi använda "procedure" för något


i journal: man kunde ha ett standardsätt

patienten startade igång alex rökfri, kort beskrivning av vad det är - den kopieras in i varje journal

sen typ patienten följer behandlingsplan, och så säger man det typ 1g/v
om något avviker om t.ex patienten skriver massa till supporten att den vill talivet av sig

man skulle aldrig skriva exakt vilka hacks etc.


i checkin questionnaire är det värdefullt med hack info

milestones: i journalspråk "positiv förstärkning"
det ska stå i initiala journalanteckningen på journalspråk
det ärinte så intressant att veta när/hur man får dem




...summering

- När man börjar hos Eila så får man en CarePlan. Den innehåller en ganska detaljerad beskrivning av vad vi gör: baserad på KBT, man har aktiviteter som logging och ..., man gör en nedtrappning, man har positiv förstärkning under tiden och efter att man är rökfri. Man får hjälp vid relapses. Patienten självrapporterar. Careplanen fortsätter under tiden man använder Eila - även i fall man relapsar, är rökfri, osv.
- Vi översätter viktiga konversationer till questionnaires
- Vi översätter loggingflödet till en questionnaire. Vi ska ta med hacks-biten (men inte statements) men göra optional. Det är okej att vi har alla alex-specifika typer av triggers,hacks, etc. Vi skriver i standarden att andra företag kan utöka dessa via vårt open source repo.
- Statusuppdateringar av planen som sätta igång plan, bli rökfri, reducera, relapsa osv gör vi som Observation (en extension som säger "changed smoke free status" typ). Vi kan ha en observatuion som är "curretReportedSmokingStatus" också.
- Man kan lägga till någon sorts summering/observation om vilka triggers och hacks som funkar bäst, men det är nog optional.
- Vi sätter inga condition/rökningsstatus direk tpå patienten, eftersom allt är självrapporterat.