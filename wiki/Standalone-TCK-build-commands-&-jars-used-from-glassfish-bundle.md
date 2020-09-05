
# Major commands executed to build a Standalone TCK bundle 

## 1. Build the TCK jar and required library files
        ant -f $BASEDIR/install/<technology>/bin/build.xml -Ddeliverabledir=<technology> -Dbasedir=$BASEDIR/install/<technology>/bin $TCK_SPECIFIC_PROPS clean.all build.all.jars 

where  technology can be any of of the list:  

        jaxr, jsonp, jsonb, jaxrs, websocket, el, concurrency, connector, jacc, jaspic, caj, jms, jsp, jstl, jaxws, saaj, servlet, jsf, securityapi, jaxrpc, jpa, jta 

$TCK_SPECIFIC_PROPS varies for tcks as below:

       jaxr : 
       $TCK_SPECIFIC_PROPS="-Djwsdp.home=$GF_HOME/glassfish5/glassfish/ -Dts.classpath=$GF_HOME/glassfish5/glassfish/modules/endorsed/jakarta.xml.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/endorsed/webservices-api-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.xml.registry-api.jar:$GF_HOME/glassfish5/glassfish/modules/webservices-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jaxb-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$BASEDIR/lib/javatest.jar:$BASEDIR/lib/tsharness.jar -Dall.test.dir=com/sun/ts/tests/jaxr,com/sun/ts/tests/signaturetest/jaxr"

       jaxrpc:
       $TCK_SPECIFIC_PROPS="-Dendorsed.dirs=$GF_HOME/glassfish5/glassfish/modules/endorsed -Dall.test.dir=com/sun/ts/tests/jaxrpc,com/sun/ts/tests/signaturetest/jaxrpc -Dlocal.classes=$GF_HOME/glassfish5/glassfish/modules/webservices-osgi.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.xml.rpc-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.mail.jar:$GF_HOME/glassfish5/glassfish/modules/jaxb-osgi.jar:$GF_HOME/modules/jakarta.ejb-api.jar"

       jta:
       $TCK_SPECIFIC_PROPS="-Djta.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.transaction-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar"

       jsf:
       $TCK_SPECIFIC_PROPS="-Djsf.classes=$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet.jsp.jstl-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.faces.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet.jsp-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.el.jar"

       jsonp:
       $TCK_SPECIFIC_PROPS="-Djsonp.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar"

       jsonb:
       $TCK_SPECIFIC_PROPS="-Djsonb.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar"

       jaxrs:
       $TCK_SPECIFIC_PROPS="-Djaxrs.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.bind-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jsonp-jaxrs.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ws.rs-api.jar:$GF_HOME/glassfish5/glassfish/modules/jsonp-jaxrs.jar:$GF_HOME/glassfish5/glassfish/modules/endorsed/jakarta.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.json.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.interceptor-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/validation-api.jar:$GF_HOME/glassfish5/glassfish/modules/bean-validator.jar"

       websocket:
       $TCK_SPECIFIC_PROPS="-Dwebsocket.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.websocket-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar"

       securityapi:
       $TCK_SPECIFIC_PROPS="-Dsecurityapi.classes=$GF_HOME/glassfish5/glassfish/modules/endorsed/jakarta.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.inject.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.security.enterprise-api.jar:$GF_HOME/glassfish5/glassfish/modules/cdi-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.faces.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.security.auth.message-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.interceptor-api.jar"

       el:
       $TCK_SPECIFIC_PROPS="-Del.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.el.jar"

       concurrency:
       $TCK_SPECIFIC_PROPS="-Dconcurrency.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.enterprise.concurrent-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jta.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.enterprise.deploy-api.jar"

       connector:
       $TCK_SPECIFIC_PROPS="-Dconnector.home=$GF_HOME/glassfish5/glassfish/"

       jacc:
       $TCK_SPECIFIC_PROPS="-Djacc.home=$GF_HOME/glassfish5/glassfish/ -Djacc.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.jms-api.jar:$GF_HOME/glassfish5/glassfish/modules/security.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.security.jacc-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.persistence.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.interceptor-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.mail.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.transaction-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet.jsp-api.jar"

       jms:
       $TCK_SPECIFIC_PROPS="-Djms.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.jms-api.jar"

       jsp, jstl, jaspic:
       $TCK_SPECIFIC_PROPS="-DwebServerHome=$GF_HOME/glassfish5/glassfish/"

       jpa: 
       $TCK_SPECIFIC_PROPS="-Djpa.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.persistence.jar"

       saaj:
       $TCK_SPECIFIC_PROPS="-Dlocal.classes=$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.ejb-api.jar -Dwebcontainer.home=$GF_HOME/glassfish5/glassfish -Dendorsed.dirs=$GF_HOME/glassfish5/glassfish/modules/endorsed"

       servlet:
       $TCK_SPECIFIC_PROPS="-Dservlet.classes=$GF_HOME/glassfish5/glassfish/modules/endorsed/jakarta.annotation-api.jar:$GF_HOME/glassfish5/glassfish/modules/jakarta.servlet-api.jar"

       jaxws:
       $TCK_SPECIFIC_PROPS="-Dwebcontainer.home=$BASEDIR/glassfish5/glassfish -Dwebcontainer.home.ri=$BASEDIR/glassfish5/glassfish -Ddeliverable.version=2.3"


## 2. Compile all test files related to the technology

     ant -f $BASEDIR/install/<technology>/bin/build.xml -Ddeliverabledir=<technology> -Dbasedir=$BASEDIR/<technology>/bin/ $TCK_SPECIFIC_PROPS -Dbasedir=$BASEDIR/install/$tck/bin -Djava.endorsed.dirs=$GF_HOME/glassfish5/glassfish/modules/endorsed build.all


For JAXRPC TCK copy ant-props library 

    cp $BASEDIR/lib/ant-props.jar $ANT_HOME/lib

    ant -f $BASEDIR/install/<technology>/bin/build.jaxrpc.xml -Ddeliverabledir=<technology> -Dbasedir=$BASEDIR/install/<technology>/bin $TCK_SPECIFIC_PROPS -Djava.endorsed.dirs=$BASEDIR/glassfish5/glassfish/modules/endorsed buildall


## 3. Build the TCK bundle.

      copy the dtds to the tck doc directory
      cp $BASEDIR/internal/docs/dtd/*.dtd $BASEDIR/internal/docs/<technology>/

      ant -f $BASEDIR/release/tools/build.xml -Ddeliverabledir=<technology> -Dbasedir=$BASEDIR/release/tools $DOC_SPECIFIC_PROPS -Dskip.createbom="true" -Dskip.build="true" $TCK_SPECIFIC_PROPS <technology>

For jaxrpc tck, 

      $DOC_SPECIFIC_PROPS="-propertyfile $BASEDIR/install/jaxrpc/bin/build.properties -Dts.home=$BASEDIR -Ddeliverable.version=1.1 -Ddeliverable.class=com.sun.ts.lib.deliverable.jaxrpc.JAXRPCDeliverable" 


where GF_HOME is the path with glassfish bundle is available(=WORKSPACE by default)
BASEDIR is the local directory where Jakarta EE TCK bundle is checked out (=WORKSPACE by default)

# TCKs and the Glassfish Jars required to Build them


| TCK            | GlassFish Jars    | 
| ---------------|:-----------------:|
| JAXR     | jakarta.xml.bind-api.jar, endorsed/webservices-api-osgi.jar, jakarta.xml.registry-api.jar, webservices-osgi.jar, jaxb-osgi.jar, jakarta.servlet-api.jar |
| JAXRPC     | webservices-osgi.jar, jakarta.xml.rpc-api.jar, jakarta.servlet-api.jar, jakarta.mail.jar, jaxb-osgi.jar, jakarta.ejb-api.jar |
| JTA     | jakarta.transaction-api.jar, jakarta.servlet-api.jar |
| JSF     | cdi-api.jar, jakarta.servlet.jsp.jstl-api.jar, jakarta.inject.jar, jakarta.faces.jar, jakarta.servlet.jsp-api.jar, jakarta.servlet-api.jar, jakarta.el.jar |
| JSONP     | jakarta.json.jar, jakarta.json.bind-api.jar, jakarta.json.jar |
| JSONB     | jakarta.json.jar, jakarta.json.bind-api.jar, jakarta.json.jar, jakarta.json.jar, jakarta.inject.jar, jakarta.servlet-api.jar |
| JAXRS     | jakarta.json.jar, jakarta.json.bind-api.jar, jakarta.json.jar, jsonp-jaxrs.jar, jakarta.ws.rs-api.jar, jsonp-jaxrs.jar, endorsed/jakarta.annotation-api.jar, jakarta.json.jar, jakarta.ejb-api.jar, jakarta.interceptor-api.jar, jakarta.servlet-api.jar, cdi-api.jar, jakarta.inject.jar, validation-api.jar, bean-validator.jar |
| WEBSOCKET     | jakarta.websocket-api.jar, jakarta.servlet-api.jar, jakarta.inject.jar, cdi-api.jar |
| SECURITYAPI     | endorsed/jakarta.annotation-api.jar, jakarta.servlet-api.jar, jakarta.inject.jar, jakarta.security.enterprise-api.jar, cdi-api.jar, jakarta.faces.jar, jakarta.security.auth.message-api.jar, jakarta.ejb-api.jar, jakarta.interceptor-api.jar |
| EL     | jakarta.el.jar |
| CONCURRENCY     | jakarta.enterprise.concurrent-api.jar, jakarta.servlet-api.jar, jakarta.ejb-api.jar, jta.jar, jakarta.enterprise.deploy-api.jar |
| JACC     | jakarta.jms-api.jar, security.jar, jakarta.servlet-api.jar, jakarta.security.jacc-api.jar, jakarta.ejb-api.jar, jakarta.persistence.jar, jakarta.interceptor-api.jar, jakarta.mail.jar, jakarta.transaction-api.jar, jakarta.servlet.jsp-api.jar |
| JMS     | jakarta.jms-api.jar |
| JPA     | jakarta.persistence.jar |
| SAAJ     | jakarta.servlet-api.jar, jakarta.ejb-api.jar,  |
| SERVLET     | jakarta.annotation-api.jar, jakarta.servlet-api.jar |



