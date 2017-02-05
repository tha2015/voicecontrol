#!/bin/sh
mvn package
mvn dependency:copy-dependencies

cp target/voicecontrol-0.0.1-SNAPSHOT.jar target/dependency/

export GOOGLE_APPLICATION_CREDENTIALS=$HOME/voicecontrol.json

java -jar target/dependency/voicecontrol-0.0.1-SNAPSHOT.jar