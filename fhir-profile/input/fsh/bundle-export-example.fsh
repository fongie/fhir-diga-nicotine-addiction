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
Instance: PatientExportBundleExample
Description: "A full example of an export containing data for a single self-reported nicotine-using patient together with all relevant resources like his/her condition, careplan, questionnaires he/she answered, as well as the replies."
InstanceOf: Bundle
* type = #document
* timestamp = "2021-03-25T13:28:17.239+02:00"
* entry[0].resource = ExportedDataCompositionExample
* entry[1].resource = SelfReportedNicotineUsingPatientExample
* entry[2].resource = AlexTherapeutics
* entry[3].resource = SelfReportedNicotineUsageExample
* entry[4].resource = QuestionnaireExample
* entry[5].resource = NicotineTreatmentQuestionnaireResponseExample
* entry[6].resource = NicotineUsageTreatmentPlanExample