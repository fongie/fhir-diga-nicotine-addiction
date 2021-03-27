#!/bin/bash

### you need to populate EMAIL and PASSWORD with your simplify login

AUTH_ENDPOINT=https://api.simplifier.net/token
DE_R4_ENDPOINT=https://api.simplifier.net/Basisprofil-DE-R4/zip
DE_PACKAGE_PATH=~/.fhir/packages/de.basisprofil.r4\#1.0.0-alpha9/package

DATA="{\"Email\": \"$EMAIL\", \"Password\": \"$PASSWORD\"}"
TOKEN=$(curl -X POST -H 'Content-Type: application/json' --data "$DATA" $AUTH_ENDPOINT | jq -r .token)
BEARER="Authorization: Bearer $TOKEN"
ZIP_PATH=$DE_PACKAGE_PATH/package.zip
mkdir -p $DE_PACKAGE_PATH
curl -v -H "$BEARER" $DE_R4_ENDPOINT --output $ZIP_PATH
cd $DE_PACKAGE_PATH
unzip -n package.zip
