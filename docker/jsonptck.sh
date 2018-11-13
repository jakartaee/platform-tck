#!/bin/bash -x

export TCK_HOME=${WORKSPACE}
echo "TCK_HOME in jsonptck.sh $TCK_HOME"
echo "ANT_HOME in jsonptck.sh $ANT_HOME"
echo "PATH in jsonptck.sh $PATH"
echo "ANT_OPTS in jsonptck.sh $ANT_OPTS"

cd $TCK_HOME

if [ -f "${WORKSPACE}/standalone-bundles/jsonptck-1.1_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi
unzip ${WORKSPACE}/standalone-bundles/jsonptck-1.1_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/jsonptck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jsonptckreport/jsonptck/#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jsonptckwork/jsonptck#g" ts.jte
sed -i "s#jsonp\.classes=.*#jsonp.classes=$TCK_HOME/glassfish5/glassfish/modules/javax.json-api.jar:$TCK_HOME/glassfish5/glassfish/modules/javax.json.jar#g" ts.jte

mkdir -p $TCK_HOME/jsonptckreport/jsonptck
mkdir -p $TCK_HOME/jsonptckwork/jsonptck

cd $TS_HOME/bin
#ant config.vi
cd $TS_HOME/src/com/sun/ts/tests/
# ant deploy.all
ant run.all
echo "Test run complete"

TCK_NAME=jsonptck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/
