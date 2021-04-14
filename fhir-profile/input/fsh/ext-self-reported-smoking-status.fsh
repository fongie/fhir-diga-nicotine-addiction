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