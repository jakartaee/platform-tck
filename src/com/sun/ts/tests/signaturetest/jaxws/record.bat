WEBCONTAINER_HOME=%CATALINA_HOME%
JAXWS_CLASSES=%JAXWS_HOME%/lib/FastInfoset.jar;%JAXWS_HOME%/lib/activation.jar;%JAXWS_HOME%/libgmbal-api-only.jar;%JAXWS_HOME%/lib/http.jar;%JAXWS_HOME%/lib/jaxb-api.jar;%JAXWS_HOME%/lib/jaxb-impl.jar;%JAXWS_HOME%/lib/jaxb-xjc.jar;%JAXWS_HOME%/lib/jaxws-api.jar;%JAXWS_HOME%/lib/jaxws-rt.jar;%JAXWS_HOME%/lib/jaxws-tools.jar;%JAXWS_HOME%/lib/jsr173_api.jar;%JAXWS_HOME%/lib/jsr181-api.jar;%JAXWS_HOME%/lib/jsr250-api.jar;%JAXWS_HOME%/lib/management-api.jar;%JAXWS_HOME%/lib/mimepull.jar;%JAXWS_HOME%/lib/policy.jar;%JAXWS_HOME%/lib/resolver.jar;%JAXWS_HOME%/lib/saaj-api.jar;%JAXWS_HOME%/lib/saaj-impl.jar;%JAXWS_HOME%/lib/woodstox.jar;%JAXWS_HOME%/lib/stax-ex.jar;%JAXWS_HOME%/lib/streambuffer.jar;%WEBCONTAINER_HOME%/common/lib/servlet-api.jar
cd ../signature-repository
%TS_HOME%\bin\ant -f ..\record-build.xml -Drecorder.type=sigtest -Dsig.source=%JAXWS_CLASSES%;%JAVA_HOME%\jre\lib\rt.jar -Dmap.file=%TS_HOME%\bin\sig-test.map record.sig.batch
cd ../jaxws
