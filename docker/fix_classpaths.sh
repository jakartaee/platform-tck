#!/bin/bash -x
sed -i 's/javax.annotation.jar/jakarta.annotation-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/jaxb-api.jar/jakarta.xml.bind-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.annotation-api.jar/jakarta.annotation-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.ejb-api.jar/jakarta.ejb-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.el.jar/jakarta.el.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.enterprise.concurrent-api.jar/jakarta.enterprise.concurrent-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.enterprise.concurrent.jar/jakarta.enterprise.concurrent.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.enterprise.deploy-api.jar/jakarta.enterprise.deploy-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.faces.jar/jakarta.faces.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.inject.jar/jakarta.inject.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.interceptor-api.jar/jakarta.interceptor-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.jms-api.jar/jakarta.jms-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.json.bind-api.jar/jakarta.json.bind-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.json.jar/jakarta.json.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.json-api.jar/jakarta.json.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.mail.jar/jakarta.mail.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.management.j2ee-api.jar/jakarta.management.j2ee-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.persistence.jar/jakarta.persistence.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.resource-api.jar/jakarta.resource-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.security.auth.message-api.jar/jakarta.security.auth.message-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.security.enterprise-api.jar/jakarta.security.enterprise-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.security.enterprise.jar/jakarta.security.enterprise.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.security.jacc-api.jar/jakarta.security.jacc-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.servlet-api.jar/jakarta.servlet-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.servlet.jsp-api.jar/jakarta.servlet.jsp-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.servlet.jsp.jstl-api.jar/jakarta.servlet.jsp.jstl-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.transaction-api.jar/jakarta.transaction-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.websocket-api.jar/jakarta.websocket-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.xml.registry-api.jar/jakarta.xml.registry-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.xml.rpc-api.jar/jakarta.xml.rpc-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte
sed -i 's/javax.ws.rs-api.jar/jakarta.ws.rs-api.jar/g' ${CTS_HOME}/jakartaeetck/bin/ts.jte

echo "Fixed classpath properties in ts.jte successfully."
