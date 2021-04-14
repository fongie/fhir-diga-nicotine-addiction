#!/bin/bash

###########
# Add FHIR resource differentials to the java package's resource folder
# to be used to create these resources in java
##########

RES_PATH=java-package/fhir-diga-nicotine-usage/src/main/resources/fhir
FHIR_PATH=fhir-profile/fsh-generated/resources
cd fhir-profile \
    && sushi . \
    && cd - \
    && mkdir -p $RES_PATH \
    && \cp $FHIR_PATH/StructureDefinition-*.json $RES_PATH \
    && \cp $FHIR_PATH/CodeSystem-*.json $RES_PATH \
    && \cp $FHIR_PATH/ValueSet-*.json $RES_PATH
