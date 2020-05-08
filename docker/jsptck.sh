#!/bin/bash -xe

export TCK_HOME=${WORKSPACE}
echo "TCK_HOME in jsptck.sh $TCK_HOME"
echo "ANT_HOME in jsptck.sh $ANT_HOME"
echo "PATH in jsptck.sh $PATH"
echo "ANT_OPTS in jsptck.sh $ANT_OPTS"

cd $TCK_HOME

if ls ${WORKSPACE}/standalone-bundles/*jsptck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for jsptck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*jsptck*.zip -d ${TCK_HOME}
  TCK_NAME=jsptck
elif ls ${WORKSPACE}/standalone-bundles/*pages-tck*.zip 1> /dev/null 2>&1; then
  echo "Using stashed bundle for pages-tck created during the build phase"
  unzip -q ${WORKSPACE}/standalone-bundles/*pages-tck*.zip -d ${TCK_HOME}
  TCK_NAME=pages-tck
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

sed -i "s#webServerHome=.*#webServerHome=$TCK_HOME/glassfish5/glassfish#g" ts.jte
sed -i "s#^webServerHost=.*#webServerHost=localhost#g" ts.jte
sed -i "s#^webServerPort=.*#webServerPort=8080#g" ts.jte
sed -i "s#^impl\.vi=.*#impl.vi=glassfish#g" ts.jte
sed -i 's#^impl\.vi\.deploy\.dir=.*#impl.vi.deploy.dir=${webServerHome}/domains/domain1/autodeploy#' ts.jte
sed -i "s#^impl\.deploy\.timeout\.multiplier=.*#impl.deploy.timeout.multiplier=30#g" ts.jte
sed -i 's#sigTestClasspath=.*#sigTestClasspath=\$\{ts.home\}/classes\$\{pathsep\}\$\{jstl.classes\}\$\{pathsep\}\$\{jspservlet.classes\}\$\{pathsep\}\$\{el.classes\}\$\{pathsep\}\$\{JAVA_HOME\}/lib/rt.jar#g' ts.jte
sed -i 's#^jspservlet\.classes=.*#jspservlet.classes=${webServerHome}/modules/jakarta.servlet-api.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp.jar${pathsep}${webServerHome}/modules/jakarta.servlet.jsp-api.jar#g' ts.jte
sed -i 's#^jstl\.classes=.*#jstl.classes=${webServerHome}/modules/jakarta.servlet.jsp.jstl.jar#g' ts.jte
sed -i 's#^el\.classes=.*#el.classes=${webServerHome}/modules/jakarta.el.jar#g' ts.jte
sed -i "s#^report.dir=.*#report.dir=$TCK_HOME/${TCK_NAME}report/${TCK_NAME}#g" ts.jte
sed -i "s#^work.dir=.*#work.dir=$TCK_HOME/${TCK_NAME}work/${TCK_NAME}#g" ts.jte

mkdir -p $TCK_HOME/${TCK_NAME}report/${TCK_NAME}
mkdir -p $TCK_HOME/${TCK_NAME}work/${TCK_NAME}

cd $TCK_HOME/glassfish5/bin
./asadmin start-domain

cd $TS_HOME/src/com/sun/ts/tests
ant -Dutil.dir=$TS_HOME deploy.all
ant -Dutil.dir=$TS_HOME run.all
echo "Test run complete"

JT_REPORT_DIR=$TCK_HOME/${TCK_NAME}report
export HOST=`hostname -f`
echo "1 ${TCK_NAME} ${HOST}" > ${WORKSPACE}/args.txt
mkdir -p ${WORKSPACE}/results/junitreports/
${JAVA_HOME}/bin/java -Djunit.embed.sysout=true -jar ${WORKSPACE}/docker/JTReportParser/JTReportParser.jar ${WORKSPACE}/args.txt ${JT_REPORT_DIR} ${WORKSPACE}/results/junitreports/

tar zcvf ${WORKSPACE}/${TCK_NAME}-results.tar.gz ${TCK_HOME}/${TCK_NAME}report ${TCK_HOME}/${TCK_NAME}work ${WORKSPACE}/results/junitreports/ ${TCK_HOME}/glassfish5/glassfish/domains/domain1 $TCK_HOME/$TCK_NAME/bin/ts.*
