#!/bin/sh
wget -O pmd.zip https://github.com/pmd/pmd/releases/download/pmd_releases%2F6.10.0/pmd-bin-6.10.0.zip
unzip pmd.zip
cd pmd-*
PMD_RUNNER=$(pwd)
cd ..
${PMD_RUNNER}/bin/run.sh pmd -cache target/pmd.cache -d src/main/java -f codeclimate -R rulesets/rules.xml > target/pmd.json
