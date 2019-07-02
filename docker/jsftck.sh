#!/bin/bash -xe

export TCK_HOME=${WORKSPACE}
echo "TCK_HOME in jsftck.sh $TCK_HOME"
echo "ANT_HOME in jsftck.sh $ANT_HOME"
echo "PATH in jsftck.sh $PATH"
echo "ANT_OPTS in jsftck.sh $ANT_OPTS"

cd $TCK_HOME

if [ -f "${WORKSPACE}/standalone-bundles/jsftck-2.3_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
  unzip ${WORKSPACE}/standalone-bundles/jsftck-2.3_latest.zip -d ${TCK_HOME}
elif [ -f "${WORKSPACE}/standalone-bundles/eclipse-jsftck-2.3_latest.zip" ];then
  echo "Using stashed eclipse bundle created during the build phase"
  unzip ${WORKSPACE}/standalone-bundles/eclipse-jsftck-2.3_latest.zip -d ${TCK_HOME}
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
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/jsftck
echo "TS_HOME $TS_HOME"

cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

cd $TS_HOME/bin
webServerHome=$TCK_HOME/glassfish5/glassfish

sed -i "s#webServerHome=.*#webServerHome=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^webServerHost=.*#webServerHost=localhost#g" ts.jte
sed -i "s#^webServerPort=.*#webServerPort=8080#g" ts.jte
sed -i "s#^impl.vi=.*#impl.vi=glassfish#g" ts.jte
sed -i "s#^impl.vi.deploy.dir=.*#impl.vi.deploy.dir=$TCK_HOME/glassfish5/glassfish/domains/domain1/autodeploy#g" ts.jte
sed -i "s#^jsf.classes=.*#jsf.classes=${webServerHome}/modules/cdi-api.jar;${webServerHome}/modules/jakarta.servlet.jsp.jstl-api.jar;${webServerHome}/modules/jakarta.inject.jar;${webServerHome}/modules/jakarta.faces.jar;${webServerHome}/modules/jakarta.servlet.jsp-api.jar;${webServerHome}/modules/jakarta.servlet-api.jar;${webServerHome}/modules/jakarta.el.jar#g" ts.jte
sed -i 's/^impl\.deploy\.timeout\.multiplier=.*/impl\.deploy\.timeout\.multiplier=960/g' ts.jte

sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jsftckreport/jsftck#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jsftckwork/jsftck#g" ts.jte

mkdir -p $TCK_HOME/jsftckreport/jsftck/
mkdir -p $TCK_HOME/jsftckwork/jsftck/

cd $TS_HOME/src/com/sun/ts/tests/jsf
ant -Dutil.dir=$TS_HOME deploy.all
ant -Dutil.dir=$TS_HOME runclient
echo "Test run complete"


TCK_NAME=jsftck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
