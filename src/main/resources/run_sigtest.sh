
# Sample shell script to setup and run the sigtest generation
M2=~/.m2/repository
CDI_API_JAR=${M2}/jakarta/enterprise/jakarta.enterprise.cdi-api/3.0.0-M3/jakarta.enterprise.cdi-api-3.0.0-M3.jar
ATINJECT_API_JAR=${M2}/jakarta/inject/jakarta.inject-api/2.0.0-RC4/jakarta.inject-api-2.0.0-RC4.jar
EL_API_JAR=${M2}/jakarta/el/jakarta.el-api/4.0.0-RC1/jakarta.el-api-4.0.0-RC1.jar
INTERCEPTOR_API_JAR=${M2}/jakarta/interceptor/jakarta.interceptor-api/2.0.0-RC2/jakarta.interceptor-api-2.0.0-RC2.jar
CLASSPATH="${JAVA_HOME}/jre/lib/rt.jar"
CLASSPATH="${CLASSPATH}:${CDI_API_JAR}"
CLASSPATH="${CLASSPATH}:${ATINJECT_API_JAR}"
CLASSPATH="${CLASSPATH}:${EL_API_JAR}"
CLASSPATH="${CLASSPATH}:${INTERCEPTOR_API_JAR}"

java -jar  "/tmp/sigtestdev.jar" Setup -classpath "${CLASSPATH}" -Package jakarta.decorator -Package jakarta.enterprise -FileName cdi-api.sig -static
