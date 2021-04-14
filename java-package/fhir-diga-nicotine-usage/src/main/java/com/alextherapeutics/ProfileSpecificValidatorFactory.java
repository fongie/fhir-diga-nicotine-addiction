/*
 *  Copyright 2021-2021 Alex Therapeutics AB and individual contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.alextherapeutics;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.common.hapi.validation.support.*;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.ValueSet;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * Provides static methods to build validators configured to validate resources from this package
 */
@Slf4j
public class ProfileSpecificValidatorFactory {
    private static final String FHIR_RESOURCES_DIR = "fhir";

    /**
     * Get a {@link FhirValidator} instance configured to validate resources from the set of profiles defined by the standard
     * @param context
     * @return
     */
    @SneakyThrows
    public static FhirValidator getValidator(FhirContext context) {
        var chain = new ValidationSupportChain();
        var defaultSupport = new DefaultProfileValidationSupport(context);
        chain.addValidationSupport(defaultSupport);
        chain.addValidationSupport(new CommonCodeSystemsTerminologyService(context));
        chain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(context));
        chain.addValidationSupport(new SnapshotGeneratingValidationSupport(context));

        var custom = new PrePopulatedValidationSupport(context);
        var parser = context.newJsonParser();
        var files = IOUtils.readLines(
                ProfileSpecificValidatorFactory.class.getClassLoader().getResourceAsStream(FHIR_RESOURCES_DIR),
                StandardCharsets.UTF_8
        );
        files.stream().forEach(
                fileName -> {
                    var content = ProfileSpecificValidatorFactory.class.getClassLoader().getResourceAsStream(Paths.get(FHIR_RESOURCES_DIR, fileName).toString());
                    var resource = parser.parseResource(content);
                    var type = fileName.split("-")[0];
                    switch (type) {
                        case "StructureDefinition":
                            custom.addStructureDefinition(resource);
                            break;
                        case "CodeSystem":
                            custom.addCodeSystem(resource);
                            break;
                        case "ValueSet":
                            custom.addValueSet((ValueSet) resource);
                            break;
                        default:
                            log.warn("Found FHIR definition file with unsupported file name prefix: {}. Ignoring", fileName);
                    }
                }
        );
        chain.addValidationSupport(custom);
        var cache = new CachingValidationSupport(chain);
        var module = new FhirInstanceValidator(cache);
        return context.newValidator().registerValidatorModule(module);
    }
}
