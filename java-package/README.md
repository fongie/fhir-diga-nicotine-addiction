# FHIR Diga Nicotine Usage

This library facilitates implementing the standard for exporting patient data for Digital Health Applications treating nicotine addiction using cognitive behavioural therapy.

## Installation
Import it using Maven in your `pom.xml`:

```xml
<dependency>
    <groupId>com.alextherapeutics</groupId>
    <artifactId>fhir-diga-nicotine-usage</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

```java

var exporter = new PatientDataExporter();

var patientData = PatientTreatmentData.builder()
        ... etc
        ).build();

var jsonExport = exporter.getJsonExport(patientData); // as json string
var xmlExport = exporter.getXmlExport(patientData); // as xml string
```

You can find example `PatientData` and `NicotineQuestionnaire` builds in the [tests](fhir-diga-nicotine-usage/src/test/java/com/alextherapeutics/ExamplePatientTreatmentData.java).

## Contributing

Contributions are very welcome! You can raise an Issue if you want to get something done, or you can submit a Pull Request to fix another issue, or do both!

## Build Instructions

The package contains two modules along with a parent. The `annotation` module handles converting FHIR resources to java classes.
The `fhir-diga-nicotine-usage` module is the main library for consumers of the library.

To build the project, run `mvn clean install` in the `java-package` root directory.

If you changed some FHIR resources, you need to build those according to those instructions and run the [script to add them to the java resources](../add-java-resources.sh).