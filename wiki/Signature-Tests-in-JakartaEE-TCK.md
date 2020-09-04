## How to generate the signature files for technologies in Jakartaee-TCK using the API jars in Glassfish.

## JDK8

### Setup:

*      git clone https://github.com/eclipse-ee4j/jakartaee-tck.git as /tmp/jakartaee-tck
*      Download Glassfish6 (to use the latest api jars) : https://ci.eclipse.org/jakartaee-tck/job/build-glassfish/lastSuccessfulBuild/artifact/appserver/distributions/glassfish/target/glassfish.zip , extract as /tmp/glassfish6


### Environment variables:
*     JAVA_HOME to Jdk8, ANT_HOME, PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH
*     TS_HOME=/tmp/jakartaee-tck
*     sigtest_classes="${TS_HOME}/lib/sigtest.jar"
*     s1as_modules=/tmp/glassfish6/glassfish/modules
*     pathsep=:
*     sigtest_classes="${TS_HOME}/lib/sigtest.jar"
*     tckjarname=<api jar name in Glassfish modules, eg: jakarta.jms-api.jar>
*     sigTestClasspath=${pathsep}${s1as_modules}/${tckjarname}${pathsep}${JAVA_HOME}/jre/lib/rt.jar${pathsep}


### Commands :

*     cd ${TS_HOME}/src/com/sun/ts/tests/signaturetest
*     vi ${TS_HOME}/install/{tckname}/bin/sig-test_se8.map : edit the map file accordingly with latest version of api
*     ant -find record-build.xml -Dsig.source="${sigtest_classes}${pathsep}${sigTestClasspath}" -Dmap.file=${TS_HOME}install/{tckname}/bin/sig-test_se8.map -Ddeliverabledir="<tckname>"   -Drecorder.type=sigtest record.sig.batch

If any errors with packages missing, the corresponding api jar should be added to the sigTestClasspath

The ${TS_HOME}/src/com/sun/ts/tests/signaturetest/signature-repository will have the new signature files with _se8 suffix.

Alternatively, below command can be used to generate signature file if we want to avoid using ant commands :

java -jar ${TS_HOME}/lib/sigtestdev.jar Setup -classpath ${sigTestClasspath}  -FileName <signature-file-name>  -apiVersion <packageversion_se8> -package <packagename>

eg for jakarta.faces : java -jar ${TS_HOME}/lib/sigtestdev.jar Setup -classpath ${sigTestClasspath}  -FileName jakarta.faces.3.0_se8  -apiVersion 3.0_se8 -package jakarta.faces


## JDK11

### Setup:

*      git clone https://github.com/eclipse-ee4j/jakartaee-tck.git as /tmp/jakartaee-tck
*      Download Glassfish6 (to use the latest api jars) : https://download.eclipse.org/ee4j/glassfish/glassfish-6.0.0-M1.zip , extract as /tmp/glassfish6
*      set JAVA_HOME to Jdk11
*      jimage extract $JAVA_HOME/lib/modules to JDK11_CLASSES
       Commands : mkdir -p $JAVA_HOME/extract/classes
                  jimage extract --dir $JAVA_HOME/extract/classes $JAVA_HOME/lib/modules

### Environment variables:

*     JAVA_HOME to Jdk11, ANT_HOME, PATH=$JAVA_HOME/bin:$ANT_HOME/bin:$PATH
*     TS_HOME=/tmp/jakartaee-tck
*     s1as_modules=/tmp/glassfish6/glassfish/modules
*     pathsep=:
*     JDK11_CLASSES=$JAVA_HOME/extract/classes
*     sigtest_classes="${TS_HOME}/lib/sigtest.jar:${JDK11_CLASSES}"
*     tckjarname=<api jar name in Glassfish modules, eg: jakarta.jms-api.jar>
*     sigTestClasspath=${pathsep}${s1as_modules}/${tckjarname}${pathsep}${JDK11_CLASSES}/java.base${pathsep}${JDK11_CLASSES}/java.logging${pathsep}${JDK11_CLASSES}/java.xml${pathsep}${JDK11_CLASSES}/java.desktop${pathsep}${JDK11_CLASSES}/java.instrument${pathsep}${JDK11_CLASSES}/java.transaction.xa${pathsep}${JDK11_CLASSES}/java.naming${pathsep}${JDK11_CLASSES}/java.sql${pathsep}${JDK11_CLASSES}/java.rmi${pathsep}${JDK11_CLASSES}/java.management


### Commands :

*     cd ${TS_HOME}/src/com/sun/ts/tests/signaturetest
*     vi ${TS_HOME}/install/{tckname}/bin/sig-test_se11.map : edit the map file accordingly with latest version of api
*     ant -find record-build.xml -Dsig.source="${sigtest_classes}${pathsep}${sigTestClasspath}" -Dmap.file=${TS_HOME}install/{tckname}/bin/sig-test_se11.map -Ddeliverabledir="<tckname>"  -Drecorder.type=sigtest record.sig.batch


If any errors with packages missing, the corresponding api jar should be added to the sigTestClasspath

The ${TS_HOME}/src/com/sun/ts/tests/signaturetest/signature-repository will have the new signature files with _se11 suffix.
