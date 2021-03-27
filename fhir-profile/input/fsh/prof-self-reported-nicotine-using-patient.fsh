Profile: SelfReportedNicotineUsingPatient
Id: self-reported-nicotine-using-patient
Title: "Self Reported Nicotine Using Patient"
Description: "A patient who has reported they are using nicotine and are being treated for it."
Parent: Patient
* extension contains NicotineTrigger named commonNicotineTrigger 0..*
* extension[commonNicotineTrigger] ^short = "Common trigger(s) for a patient to use nicotine"
* extension contains EffectiveNicotineIntervention named effectiveNicotineIntervention 0..*
* extension[effectiveNicotineIntervention] ^short = "Intervention(s) that have been effective when patient has wanted to use nicotine"

Instance: SelfReportedNicotineUsingPatientExample
Description: "An example of a self reporting nicotine using patient, who has been found to have a common nicotine trigger when 'waiting', and that an effective intervention has been to 'drink water'."
InstanceOf: SelfReportedNicotineUsingPatient
* extension[NicotineTrigger][0].valueCodeableConcept = TriggerCodeSystem#waiting
* extension[EffectiveNicotineIntervention][0].valueCodeableConcept = EffectiveInterventionCodeSystem#drink-water
* active = true
* name.use = #nickname
* name.given = "Max"
* telecom.system = #email
* telecom.value = "max.mustermann@diga.de"
* gender = #male