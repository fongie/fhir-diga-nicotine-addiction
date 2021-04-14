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

ValueSet: SelfReportedSmokingStatusCode
Id: self-reported-smoking-status-code
Title: "Self Reported Smoking Status Code"
Description: "A self-reported smoking status"
* include codes from system SelfReportedStatusCodeSystem

CodeSystem: SelfReportedStatusCodeSystem
Id: self-reported-status-code-system
Title: "Self Reported Smoking Status Code System"
Description: "Available values for self-reporting smoking status"
* #actively_smoking "Actively Smoking" "Currently actively smoking and hasn't attempted to cut down or quit yet, or has relapsed and started to smoke again."
* #cutting_down "Cutting Down" "Started a process of progressively cutting down on the amount of cigarettes smoked"
* #quit_smoking "Quit Smoking" "Has quit smoking."