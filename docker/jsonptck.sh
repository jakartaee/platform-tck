#!/bin/bash -x

export TCK_HOME=${WORKSPACE}
echo "TCK_HOME in jsonptck.sh $TCK_HOME"
echo "ANT_HOME in jsonptck.sh $ANT_HOME"
echo "PATH in jsonptck.sh $PATH"
echo "ANT_OPTS in jsonptck.sh $ANT_OPTS"

cd $TCK_HOME

if ls ${WORKSPACE}/standalone-bundles/*jsonptck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jsonptck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*jsonptck*.zip -d ${TCK_HOME}
  TCK_NAME=jsonptck
elif ls ${WORKSPACE}/standalone-bundles/*jsonp-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jsonp-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*jsonp-tck*.zip -d ${TCK_HOME}
  TCK_NAME=jsonp-tck
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi

##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip -q ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/$TCK_NAME
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME
cd $TS_HOME/bin

sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}/#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte
sed -i "s#jsonp\.classes=.*#jsonp.classes=$TCK_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$TCK_HOME/glassfish5/glassfish/modules/jakarta.json.jar#g" ts.jte

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

cd $TS_HOME/bin
#ant config.vi
cd $TS_HOME/src/com/sun/ts/tests/
# ant deploy.all
ant run.all
echo "Test run complete"

JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
