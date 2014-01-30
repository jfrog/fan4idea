#/bin/bash

pluginVersion=0.0.5
pluginDir="/work/fan/fan4idea/plugin"
ideaDir="/Users/freds/Library/Application Support/IntelliJIDEA90"

cd "$pluginDir" && \
mvn clean install && \
cd "$ideaDir" && \
rm -rf "fan4idea-$pluginVersion" && \
unzip "$pluginDir/target/fan4idea-$pluginVersion.zip"
