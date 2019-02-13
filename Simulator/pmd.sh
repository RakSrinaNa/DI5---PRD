#!/bin/sh
wget -O pmd.zip https://github.com/pmd/pmd/releases/download/pmd_releases%2F6.11.0/pmd-bin-6.11.0.zip
unzip pmd.zip
cd pmd-*
PMD_RUNNER=$(pwd)
cd ..
${PMD_RUNNER}/bin/run.sh pmd -failOnViolation false -cache target/pmd.cache -d src/main/java -f codeclimate -R rulesets/rules.xml > target/pmd.json
