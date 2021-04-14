# FHIR Profile & Standard for DiGAs that treat nicotine addiction using CBT

This repository contains sources for
a) the FHIR profile that defines how data should be structured (`fhir-profile`)
b) the [implementation guide](https://simplifier.net/guide/self-reported-nicotine-usage-diga/home) written on the Simplify project page (`implementation-guide`)
c) the written Standard (`document`)
d) a Java library which can be used to implement the standard in a DiGA backend (`java-package`)

## Usage
The Standard is a document which can be read and used to implement a valid DiGA data export.

There is a provided Java library which facilitates implementing the Standard. How to use that can be read in its [readme](java-package/README.md).

## Releases

Releases prefixed with `standard-` contain the Standard in PDF format and the FHIR Profile artefacts

Releases prefixed with `java-` contain the latest java library release.

## Contributions

## To the standard
The standard with the FHIR profiles are actively maintained by the team at Alex Therapeutics. However, contributions are welcome from other actors who wish to use the standard. For example, if you have certain extensions you need to make for your use-case, we will happily accept PRs which modify the profiles and add to the text in the Standard, and then coordinate a new release of the Standard. You probably want to open an Issue first on this repository so that we can discuss the change. Note that any changes that include inserting new mandatory attributes (`1..1`) will likely be rejected, as that constitutes a breaking change for all implementers. However, any optional extensions or additions to value code sets can be added to the Standard without too much difficulty.

## To the java library
The library is also maintained by the team at Alex Therapeutics. Contributions are welcome. Open an Issue if you wish to report a bug or request a feature, or submit a Pull Request if you wish to address one of the issues!

## Standard Build Instructions
The Standard PDF is written and generated using LaTEX. You need a TeX environment and something like pdflatex to generate it.
1. generate FHIR output by running `./fhir-profile/._genonce.sh`
2. run `./build-tables.sh`
3. build the `document/standard.tex` file to PDF using your TeX environment

## Implementation guide at Simplify
The guide is written using markdown files in combination with an `xml` file which describes the structure of the IG. These have to be manually uploaded to Simplify when changed.

## FHIR Build Instructions
The FHIR profiles are generated using FHIR Shorthand (link), which is a language for writing FHIR profiles and implementation guides. You can compile the profile using _sushi_ and the `*.sh*` scripts (`_genonce.sh` generates the IG if you are properly set up, see Sushi instructions).

Due to limitations in Simplify and how FSH download packages you will also need to download snapshots for the german base profile manually. Go to [their project](https://simplifier.net/Basisprofil-DE-R4/~packages) and download snapshots for the latest r4 package. Then, you need to unzip the snapshots into your FHIR cache - for example in `~/.fhir/packages/{name-of-project}`. For example, you might end up unzipping the snapshots to `~/.fhir/packages/de.basisprofil.r4#1.0.0-alpha9`. Now compiling with Sushi will work.

## License
The source code contained in this repository is licensed under the Apache 2.0 license, with copyright by Alex Therapeutics AB and individual contributors.

The build artifacts making up the produced Standard- the document and the FHIR Artifacts contained in a _Release_ - are licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA. The Standard is copyright by Alex Therapeutics AB.
