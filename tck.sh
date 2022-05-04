#!/bin/bash
export WORKSPACE="$(readlink -f $(dirname "$0"))"
export ANT_HOME=/usr/share/ant
export JAVA_HOME=/usr/lib/jvm/jdk11
export HARNESS_DEBUG=true
export PATH=$JAVA_HOME/bin:$ANT_HOME/bin/:$PATH

echo "Curent timestamp: $(date --iso-8601=seconds)"
echo "Build will take around 45 minutes..."

cd "${WORKSPACE}"
"${WORKSPACE}/docker/build_jakartaeetck.sh" $1 $2 $3 $4 $5

