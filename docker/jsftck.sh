#!/bin/bash -xe

export TCK_HOME=${WORKSPACE}
echo "TCK_HOME in jsftck.sh $TCK_HOME"
echo "ANT_HOME in jsftck.sh $ANT_HOME"
echo "PATH in jsftck.sh $PATH"
echo "ANT_OPTS in jsftck.sh $ANT_OPTS"

cd $TCK_HOME

if [ -f "${WORKSPACE}/standalone-bundles/jsftck-2.3_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
else
  echo "Download and install Servlet TCK Bundle ..."
  mkdir -p ${WORKSPACE}/standalone-bundles
  wget http://blr00akv.in.oracle.com/tck-builds/links/builds/tcks/javaee_cts/8.1/nightly/jsftck-2.3_Latest.zip -O ${WORKSPACE}/standalone-bundles/jsftck-2.3_latest.zip
fi
unzip ${WORKSPACE}/standalone-bundles/jsftck-2.3_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  export GF_BUNDLE_URL="http://download.oracle.com/glassfish/5.0.1/nightly/latest-glassfish.zip"
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
sed -i "s#^jsf.classes=.*#jsf.classes=${webServerHome}/modules/cdi-api.jar;${webServerHome}/modules/javax.servlet.jsp.jstl-api.jar;${webServerHome}/modules/javax.inject.jar;${webServerHome}/modules/javax.faces.jar;${webServerHome}/modules/javax.servlet.jsp-api.jar;${webServerHome}/modules/javax.servlet-api.jar;${webServerHome}/modules/javax.el.jar#g" ts.jte
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

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/
