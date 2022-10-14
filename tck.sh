#!/bin/bash
set -e
on_exit () {
    echo -e "\n\n!!! Build failed or was interrupted !!!"
}
trap on_exit ERR

export WORKSPACE="$(readlink -f $(dirname "$0"))"

# The tck-env.cfg file can contain following exports, required to build the TCK.
# You can also set these variables directly, but they have to be set somehow.
#
# export ANT_HOME=/usr/share/ant
# export JAVA_HOME=/usr/lib/jvm/jdk11
# export HARNESS_DEBUG=true
# export PATH=$JAVA_HOME/bin:$ANT_HOME/bin/:$PATH

if [ -z "${TCK_ENV_CFG}" ]; then
    export TCK_ENV_CFG="${WORKSPACE}/tck-env.cfg"
fi
if [ -f "${TCK_ENV_CFG}" ]; then
    echo "Loading configuration file ${TCK_ENV_CFG}"
    . "${TCK_ENV_CFG}"
fi

echo "Curent timestamp: $(date --iso-8601=seconds)"
echo "Build will take around 60 minutes..."

cd "${WORKSPACE}"
"${WORKSPACE}/docker/build_jakartaeetck.sh" $1 $2 $3 $4 $5

echo "Curent timestamp: $(date --iso-8601=seconds)"
echo "Build successful!"
