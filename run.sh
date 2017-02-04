#!/bin/sh
mvn package
mvn dependency:copy-dependencies

cp target/voicecontrol-0.0.1-SNAPSHOT.jar target/dependency/
java -jar target/dependency/voicecontrol-0.0.1-SNAPSHOT.jar