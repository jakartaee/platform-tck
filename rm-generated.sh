#!/bin/bash

rm -rf ./modules
rm -rf ./tmp

rm -rf ./jakartaeetck.version
rm -rf ./src/testsuite.jtt
rm -rf ./src/vehicle.properties
rm -rf ./weblib

rm -rf lib/cts.jar
rm -rf lib/dbprocedures.jar
rm -rf lib/jsonb_alternate_provider.jar
rm -rf lib/jsonp_alternate_provider.jar
rm -rf lib/jstltck-common.jar
rm -rf lib/tsharness.jar
rm -rf lib/tsprovider.jar
rm -rf lib/tssv.jar

rm -rf ./install/jakartaee/bin/ts.jte.bak
rm -rf ./install/jakartaee/bin/tssql.stmt
git checkout HEAD -- ./install/jakartaee/bin/ts.jte
rm -rf ./internal/docs/jakartaee/javadoc_assertions.dtd
rm -rf ./internal/docs/jakartaee/spec_assertions.dtd

rm -rf ./src/web/servlet/api/\$\{pkg.name\}

rm -rf ./src/com/sun/ts/lib/implementation/sun/javaee/runtime/app
rm -rf ./src/com/sun/ts/lib/implementation/sun/javaee/runtime/appclient
rm -rf ./src/com/sun/ts/lib/implementation/sun/javaee/runtime/ejb
rm -rf ./src/com/sun/ts/lib/implementation/sun/javaee/runtime/web

git checkout HEAD -- ./src/com/sun/ts/tests/appclient/deploy/metadatacomplete/testapp/HelloService.wsdl
git checkout HEAD -- ./src/com/sun/ts/tests/appclient/deploy/metadatacomplete/testapp/HelloService_schema1.xsd
git checkout HEAD -- ./src/com/sun/ts/tests/appclient/deploy/metadatacomplete/testapp/TestAppClient.java
git checkout HEAD -- ./src/com/sun/ts/tests/appclient/deploy/metadatacomplete/testapp/TestBean.java

rm -rf ./src/com/sun/ts/tests/common/vehicle/wsappclient/WSAppclient.java
rm -rf ./src/com/sun/ts/tests/common/vehicle/wsejb/WSEJBVehicle.java
rm -rf ./src/com/sun/ts/tests/common/vehicle/wsejb/wsejb_vehicle_client.xml
rm -rf ./src/com/sun/ts/tests/common/vehicle/wsservlet/WSServletVehicle.java
rm -rf ./src/com/sun/ts/tests/ejb30/assembly/appres/appclientejb/application.xml
rm -rf ./src/com/sun/ts/tests/ejb30/assembly/appres/warejb/application.xml
rm -rf ./src/com/sun/ts/tests/ejb30/assembly/appres/warmbean/application.xml
rm -rf ./src/com/sun/ts/tests/ejb30/lite/packaging/war/datasource/global/Client.java
rm -rf ./src/com/sun/ts/tests/ejb30/lite/packaging/war/datasource/global/DataSourceBean.java
rm -rf ./src/com/sun/ts/tests/ejb30/lite/packaging/war/datasource/singleton/Client.java
rm -rf ./src/com/sun/ts/tests/ejb30/lite/packaging/war/datasource/singleton/DataSourceBean.java
rm -rf ./src/com/sun/ts/tests/ejb30/lite/packaging/war/datasource/singleton/DataSourceMBean.java
rm -rf ./src/com/sun/ts/tests/ejb30/lite/packaging/war/datasource/singleton/DataSourceRepeatableBean.java
rm -rf ./src/com/sun/ts/tests/ejb30/lite/packaging/war/datasource/stateful/ejb-jar.xml
rm -rf ./src/com/sun/ts/tests/ejb30/misc/datasource/appclientejb/Client.java
rm -rf ./src/com/sun/ts/tests/ejb30/misc/datasource/appclientejb/DataSourceBean.java
rm -rf ./src/com/sun/ts/tests/ejb30/misc/datasource/twojars/ejb3_misc_datasource_twojars_client.xml
rm -rf ./src/com/sun/ts/tests/ejb30/misc/datasource/twojars/two_standalone_component_ejb.xml
rm -rf ./src/com/sun/ts/tests/ejb30/misc/datasource/twowars/DataSourceBean.java
rm -rf ./src/com/sun/ts/tests/ejb30/misc/datasource/twowars/TestServlet.java
rm -rf ./src/com/sun/ts/tests/ejb30/misc/datasource/twowars/TestServlet2.java
rm -rf ./src/com/sun/ts/tests/ejb30/webservice/clientview/HelloService.wsdl
rm -rf ./src/com/sun/ts/tests/ejb30/webservice/clientview/HelloService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/ejb30/webservice/interceptor/HelloService.wsdl
rm -rf ./src/com/sun/ts/tests/ejb30/webservice/interceptor/HelloService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/ejb30/webservice/wscontext/HelloService.wsdl
rm -rf ./src/com/sun/ts/tests/ejb30/webservice/wscontext/HelloService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/jms/ee20/resourcedefs/descriptor/appclient_vehicle_client.xml
rm -rf ./src/com/sun/ts/tests/jms/ee20/resourcedefs/descriptor/ejb_vehicle_ejb.xml
rm -rf ./src/com/sun/ts/tests/jms/ee20/resourcedefs/descriptor/jsp_vehicle_web.xml
rm -rf ./src/com/sun/ts/tests/jms/ee20/resourcedefs/descriptor/servlet_vehicle_web.xml
rm -rf ./src/com/sun/ts/tests/webservices12/ejb/annotations/WSEjbWebServiceRefWithNoDDsTest/WSEjbWSRefWithNoDDsTestHelloEJBService.wsdl
rm -rf ./src/com/sun/ts/tests/webservices12/ejb/annotations/WSEjbWebServiceRefWithNoDDsTest/WSEjbWSRefWithNoDDsTestHelloEJBService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/webservices12/servlet/WebServiceRefsTest/server1/WSHello1Service.wsdl
rm -rf ./src/com/sun/ts/tests/webservices12/servlet/WebServiceRefsTest/server1/WSHello1Service_schema1.xsd
rm -rf ./src/com/sun/ts/tests/webservices12/servlet/WebServiceRefsTest/server2/WSHello2Service.wsdl
rm -rf ./src/com/sun/ts/tests/webservices12/servlet/WebServiceRefsTest/server2/WSHello2Service_schema1.xsd
rm -rf ./src/com/sun/ts/tests/webservices12/specialcases/services/j2w/doclit/noname/EchoService.wsdl
rm -rf ./src/com/sun/ts/tests/webservices12/specialcases/services/j2w/doclit/noname/EchoService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/webservices12/specialcases/services/j2w/doclit/noname2/MyEchoWebService.wsdl
rm -rf ./src/com/sun/ts/tests/webservices12/specialcases/services/j2w/doclit/noname2/MyEchoWebService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/file/twin1/client/svc1/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/file/twin1/client/svc2/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/file/twin2/client/svc1/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/file/twin2/client/svc2/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/file/twin3/client/svc1/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/file/twin3/client/svc2/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/file/twin4/client/svc1/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/file/twin4/client/svc2/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/http/twin1/client/svc1/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/http/twin1/client/svc2/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/http/twin2/client/svc1/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/http/twin2/client/svc2/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/http/twin3/client/svc1/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/http/twin3/client/svc2/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/http/twin4/client/svc1/
rm -rf ./src/com/sun/ts/tests/webservices12/wsdlImport/http/twin4/client/svc2/
rm -rf ./src/com/sun/ts/tests/webservices13/servlet/WSWebServiceRefLookup/client/EchoService.wsdl
rm -rf ./src/com/sun/ts/tests/webservices13/servlet/WSWebServiceRefLookup/client/EchoService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/webservices13/servlet/WSWebServiceRefLookup/server/EchoService.wsdl
rm -rf ./src/com/sun/ts/tests/webservices13/servlet/WSWebServiceRefLookup/server/EchoService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/webservices13/servlet/WSWebServiceRefLookupDDs/client/EchoService.wsdl
rm -rf ./src/com/sun/ts/tests/webservices13/servlet/WSWebServiceRefLookupDDs/client/EchoService_schema1.xsd
rm -rf ./src/com/sun/ts/tests/webservices13/servlet/WSWebServiceRefLookupDDs/server/EchoService.wsdl
rm -rf ./src/com/sun/ts/tests/webservices13/servlet/WSWebServiceRefLookupDDs/server/EchoService_schema1.xsd

git status

