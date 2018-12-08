#!/bin/bash -xe

export TCK_HOME=${WORKSPACE}
echo "TCK_HOME in jsptck.sh $TCK_HOME"
echo "ANT_HOME in jsptck.sh $ANT_HOME"
echo "PATH in jsptck.sh $PATH"
echo "ANT_OPTS in jsptck.sh $ANT_OPTS"

cd $TCK_HOME

if [ -f "${WORKSPACE}/standalone-bundles/jsptck-2.3_latest.zip" ];then
  echo "Using stashed bundle created during the build phase"
else
  echo "[ERROR] TCK bundle not found"
  exit 1
fi
unzip ${WORKSPACE}/standalone-bundles/jsptck-2.3_latest.zip -d ${TCK_HOME}
##### installRI.sh starts here #####
echo "Download and install GlassFish 5.0.1 ..."
if [ -z "${GF_BUNDLE_URL}" ]; then
  echo "[ERROR] GF_BUNDLE_URL not set"
  exit 1
fi
wget --progress=bar:force --no-cache $GF_BUNDLE_URL -O latest-glassfish.zip
unzip ${TCK_HOME}/latest-glassfish.zip -d ${TCK_HOME}

TS_HOME=$TCK_HOME/jsptck
echo "TS_HOME $TS_HOME"

chmod -R 777 $TS_HOME

cd $TS_HOME/bin

sed -i "s#webServerHome=.*#webServerHome=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^webServerHost=.*#webServerHost=localhost#g" ts.jte
sed -i "s#^webServerPort=.*#webServerPort=8080#g" ts.jte
sed -i "s#^impl\.vi=.*#impl.vi=glassfish#g" ts.jte
sed -i 's#^impl\.vi\.deploy\.dir=.*#impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy#' ts.jte
sed -i "s#^impl\.deploy\.timeout\.multiplier=.*#impl.deploy.timeout.multiplier=30#g" ts.jte
sed -i 's#^jspservlet\.classes=.*#jspservlet.classes=${webServerHome}/modules/jakarta.servlet-api.jar${pathsep}${webServerHome}/modules/javax.servlet.jsp.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp-api.jar#g' ts.jte
sed -i 's#^jstl\.classes=.*#jstl.classes=${webServerHome}/modules/javax.servlet.jsp.jstl.jar#g' ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/jsptckreport/jsptck#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/jsptckwork/jsptck#g" ts.jte

mkdir -p $TCK_HOME/jsptckreport/jsptck
mkdir -p $TCK_HOME/jsptckwork/jsptck

cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

cd $TS_HOME/src/com/sun/ts/tests/jsp
ant -Dutil.dir=$TS_HOME deploy.all
ant -Dutil.dir=$TS_HOME runclient
echo "Test run complete"

TCK_NAME=jsptck
JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
