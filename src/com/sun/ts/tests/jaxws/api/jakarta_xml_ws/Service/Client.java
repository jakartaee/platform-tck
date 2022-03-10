/*
 * Copyright (c) 2007, 2022 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Service;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import java.math.BigInteger;
import java.math.BigDecimal;

import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import javax.xml.namespace.QName;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;

import javax.xml.transform.Source;

import javax.naming.InitialContext;
import jakarta.xml.bind.JAXBContext;

public class Client extends ServiceEETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Service.";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private QName PORT2_QNAME;

  private QName NONEXISTANT_PORT_QNAME;

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private static final String PORT2_NAME = "Hello2Port";

  private static final String NONEXISTANT_PORT_NAME = "BadPort";

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.HelloService.class;

  private static final Class SEI_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.Hello.class;

  private static final Class SEI2_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.Hello2.class;

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "dlhelloservice.endpoint.1";

  private static final String ENDPOINT2_URL = "dlhelloservice.endpoint.2";

  private static final String WSDLLOC_URL = "dlhelloservice.wsdlloc.1";

  private static final String WSDLLOC2_URL = "dlhelloservice.wsdlloc.2";

  private String url = null;

  private String url2 = null;

  private URL wsdlurl = null;

  private URL wsdlurl2 = null;

  private EndpointReference epr = null;

  private WebServiceFeature[] wsfmtomtrue = { new MTOMFeature(true) };

  private WebServiceFeature[] wsfmtomfalse = { new MTOMFeature(false) };

  private WebServiceFeature[] wsftcktrue = { new TCKFeature(true) };

  private WebServiceFeature[] wsftckfalse = { new TCKFeature(false) };

  private WebServiceFeature[] wsftrue = { new AddressingFeature(true) };

  private WebServiceFeature[] wsffalse = { new AddressingFeature(false) };

  static HelloService service = null;

  private String SERVICE_NAME_WITH_WSDL = "wsw2jdlhelloservice";

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT2_URL);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC2_URL);
    wsdlurl2 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("Service Endpoint URL2: " + url2);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
    TestUtil.logMsg("WSDL Location URL2:    " + wsdlurl2);
  }

  private static final Class JAXB_OBJECT_FACTORY = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.ObjectFactory.class;

  private JAXBContext createJAXBContext() {
    try {
      return JAXBContext.newInstance(JAXB_OBJECT_FACTORY);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  private Dispatch<Object> createDispatchJAXB(jakarta.xml.ws.Service service)
      throws Exception {
    return service.createDispatch(PORT_QNAME, createJAXBContext(),
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  private Dispatch<Object> createDispatchJAXB(jakarta.xml.ws.Service service,
      WebServiceFeature[] wsf) throws Exception {
    return service.createDispatch(PORT_QNAME, createJAXBContext(),
        jakarta.xml.ws.Service.Mode.PAYLOAD, wsf);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */
  public void setup(String[] args, Properties p) throws Fault {
    boolean pass = true;

    // Initialize QNAMES used in the test
    SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);
    PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);
    PORT2_QNAME = new QName(NAMESPACEURI, PORT2_NAME);
    NONEXISTANT_PORT_QNAME = new QName(NAMESPACEURI, NONEXISTANT_PORT_NAME);

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
      modeProperty = p.getProperty(MODEPROP);
      getTestURLs();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: createTest1
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4003; JAXWS:SPEC:4009;
   * JAXWS:SPEC:4010; JAXWS:JAVADOC:48; JAXWS:SPEC:6003;
   *
   * @test_Strategy: Call jakarta.xml.ws.Service.create(QName) to return a service
   * instance.
   */
  public void createTest1() throws Fault {
    TestUtil.logTrace("createTest1");
    boolean pass = true;
    jakarta.xml.ws.Service service = null;
    TestUtil.logMsg("Call jakarta.xml.ws.Service.create(QName) ...");
    try {
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      if (service == null) {
        TestUtil.logErr("service is null");
        pass = false;
      } else
        TestUtil.logMsg("service is " + service);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createTest1 failed");
  }

  /*
   * @testName: createTest2
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4003; JAXWS:SPEC:4009;
   * JAXWS:SPEC:4010; JAXWS:JAVADOC:47; JAXWS:SPEC:6003;
   *
   * @test_Strategy: Call jakarta.xml.ws.Service.create(URL, QName) to return a
   * service instance.
   */
  public void createTest2() throws Fault {
    TestUtil.logTrace("createTest2");
    boolean pass = true;
    jakarta.xml.ws.Service service = null;
    TestUtil.logMsg("Call jakarta.xml.ws.Service.create(URL, QName) ...");
    try {
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      if (service == null) {
        TestUtil.logErr("service is null");
        pass = false;
      } else
        TestUtil.logMsg("service is " + service);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createTest2 failed");
  }

  /*
   * @testName: createWithWSFTest1
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4003; JAXWS:SPEC:4009;
   * JAXWS:SPEC:4010; JAXWS:JAVADOC:217; JAXWS:SPEC:6003; JAXWS:SPEC:4000;
   * JAXWS:SPEC:4031;
   *
   * @test_Strategy: Call jakarta.xml.ws.Service.create(QName,
   * WebServiceFeature[]) to return a service instance. Pass in a
   * WebServiceFeature that the endpoint supports. A valid service object should
   * be returned with that WebServiceFeature enabled.
   */
  public void createWithWSFTest1() throws Fault {
    TestUtil.logTrace("createWithWSFTest1");
    boolean pass = true;
    jakarta.xml.ws.Service service = null;
    TestUtil.logMsg(
        "Call jakarta.xml.ws.Service.create(QName, WebServiceFeature[]) ...");
    TestUtil.logMsg("Pass in AddressingFeature(true) as WebServiceFeature ...");
    try {
      if (modeProperty.equals("standalone")) {
        service = getService(SERVICE_QNAME, wsftrue);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      if (service == null) {
        TestUtil.logErr("service is null");
        pass = false;
      } else
        TestUtil.logMsg("service is " + service);
    } catch (WebServiceException e) {
      TestUtil.logMsg(
          "Caught WebServiceException (Service based features must not be supported)");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createWithWSFTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createWithWSFTest1 failed");
  }

  /*
   * @testName: createWithWSFTest2
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4003; JAXWS:SPEC:4009;
   * JAXWS:SPEC:4010; JAXWS:JAVADOC:216; JAXWS:SPEC:6003; JAXWS:SPEC:4031;
   *
   * @test_Strategy: Call jakarta.xml.ws.Service.create(URL, QName,
   * WebServiceFeature[]) to return a service instance. Pass in a
   * WebServiceFeature that the endpoint supports. A valid service object should
   * be returned with that WebServiceFeature enabled.
   */
  public void createWithWSFTest2() throws Fault {
    TestUtil.logTrace("createWithWSFTest2");
    boolean pass = true;
    jakarta.xml.ws.Service service = null;
    TestUtil.logMsg(
        "Call jakarta.xml.ws.Service.create(URL, QName, WebServiceFeature[]) ...");
    TestUtil.logMsg("Pass in AddressingFeature(true) as WebServiceFeature ...");
    try {
      if (modeProperty.equals("standalone")) {
        service = getService(wsdlurl, SERVICE_QNAME, wsftrue);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      if (service == null) {
        TestUtil.logErr("service is null");
        pass = false;
      } else
        TestUtil.logMsg("service is " + service);
    } catch (WebServiceException e) {
      TestUtil.logMsg(
          "Caught WebServiceException (Service based features must not be supported)");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createWithWSFTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createWithWSFTest2 failed");
  }

  /*
   * @testName: createWithWSFNegativeTest1
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4003; JAXWS:SPEC:4009;
   * JAXWS:SPEC:4010; JAXWS:JAVADOC:217; JAXWS:SPEC:6003;
   *
   * @test_Strategy: Call jakarta.xml.ws.Service.create(QName,
   * WebServiceFeature[]) to return a service instance. Pass in a
   * WebServiceFeature that the endpoint doesn't support. API should throw back
   * a WebServiceException.
   */
  public void createWithWSFNegativeTest1() throws Fault {
    TestUtil.logTrace("createWithWSFNegativeTest1");
    boolean pass = true;
    jakarta.xml.ws.Service service = null;
    TestUtil.logMsg(
        "Call jakarta.xml.ws.Service.create(QName, WebServiceFeature[]) ...");
    TestUtil.logMsg("Pass in TCKFeature(true) as WebServiceFeature ...");
    TestUtil.logMsg(
        "API must throw WebServiceException as endpoint does not know TCKFeature(true) ...");
    try {
      if (modeProperty.equals("standalone")) {
        service = getService(SERVICE_QNAME, wsftcktrue);
        TestUtil.logErr("Did not throw expected WebServiceException");
        pass = false;
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      if (!pass) {
        if (service == null) {
          TestUtil.logErr("service is null");
          pass = false;
        } else
          TestUtil.logMsg("service is " + service);
      }
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createWithWSFNegativeTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createWithWSFNegativeTest1 failed");
  }

  /*
   * @testName: createWithWSFNegativeTest2
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4003; JAXWS:SPEC:4009;
   * JAXWS:SPEC:4010; JAXWS:JAVADOC:216; JAXWS:SPEC:6003;
   *
   * @test_Strategy: Call jakarta.xml.ws.Service.create(URL, QName,
   * WebServiceFeature[]) to return a service instance. Pass in a
   * WebServiceFeature that the endpoint doesn't support. API should throw back
   * a WebServiceException.
   */
  public void createWithWSFNegativeTest2() throws Fault {
    TestUtil.logTrace("createWithWSFNegativeTest2");
    boolean pass = true;
    jakarta.xml.ws.Service service = null;
    TestUtil.logMsg(
        "Call jakarta.xml.ws.Service.create(URL, QName, WebServiceFeature[]) ...");
    TestUtil.logMsg("Pass in TCKFeature(true) as WebServiceFeature ...");
    TestUtil.logMsg(
        "API must throw WebServiceException as endpoint does not know TCKFeature(true) ...");
    try {
      if (modeProperty.equals("standalone")) {
        service = getService(wsdlurl, SERVICE_QNAME, wsftcktrue);
        TestUtil.logErr("Did not throw expected WebServiceException");
        pass = false;
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      if (!pass) {
        if (service == null) {
          TestUtil.logErr("service is null");
          pass = false;
        } else
          TestUtil.logMsg("service is " + service);
      }
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createWithWSFNegativeTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createWithWSFNegativeTest2 failed");
  }

  /*
   * @testName: GetPort1PosTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:SPEC:4010;
   * JAXWS:JAVADOC:52; JAXWS:JAVADOC:122; JAXWS:JAVADOC:116;
   *
   * @test_Strategy: Call Service.getPort(QName, Class) to return a dynamic
   * proxy for the service port. Pass a valid port name with WSDL access. Verify
   * that the method returns a dynamic proxy.
   */
  public void GetPort1PosTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPort1PosTest1WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getPort (valid port name/WSDL access) - positive test 1");
      TestUtil.logMsg("Call Service.getPort(QName, Class)");
      TestUtil.logMsg("Should find port");
      Hello tests = (Hello) service.getPort(PORT_QNAME, Hello.class);
      if (tests == null) {
        TestUtil.logErr("getPort(QName, Class) returned null");
        pass = false;
      } else if (!(tests instanceof Hello)) {
        TestUtil.logErr(
            "getPort(QName, Class) did not" + " return instance of Hello");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPort1PosTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPort1PosTest1WithWsdl failed");
  }

  /*
   * @testName: GetPortTest1WithFeatures
   *
   * @assertion_ids: JAXWS:JAVADOC:155; JAXWS:JAVADOC:181; JAXWS:SPEC:4026
   *
   * @test_Strategy: Call Service.getPort(QName, Class, WebServiceFeature ...)
   * to return a dynamic proxy for the service port. Pass a valid port name with
   * WSDL access. Verify that the method returns a dynamic proxy.
   */
  public void GetPortTest1WithFeatures() throws Fault {
    TestUtil.logTrace("GetPortTest1WithFeatures");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getPort (valid port name/WSDL access) AddressingFeature(true)");
      TestUtil
          .logMsg("Call Service.getPort(QName, Class, WebServiceFeature ...)");
      TestUtil.logMsg("Should find port");
      Hello2 tests = (Hello2) service.getPort(PORT2_QNAME, Hello2.class,
          wsftrue);
      if (tests == null) {
        TestUtil.logErr(
            "getPort(QName, Class, WebServiceFeature ...) returned null");
        pass = false;
      } else if (!(tests instanceof Hello2)) {
        TestUtil.logErr("getPort(QName, Class, WebServiceFeature ...) did not"
            + " return instance of Hello2");
        pass = false;
      }
      TestUtil.logMsg(
          "Test getPort (valid port name/WSDL access) AddressingFeature(false)");
      TestUtil
          .logMsg("Call Service.getPort(QName, Class, WebServiceFeature ...)");
      TestUtil.logMsg("Should find port");
      tests = (Hello2) service.getPort(PORT2_QNAME, Hello2.class, wsffalse);
      if (tests == null) {
        TestUtil.logErr(
            "getPort(QName, Class, WebServiceFeature ...) returned null");
        pass = false;
      } else if (!(tests instanceof Hello2)) {
        TestUtil.logErr("getPort(QName, Class, WebServiceFeature ...) did not"
            + " return instance of Hello2");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortTest1WithFeatures failed", e);
    }

    if (!pass)
      throw new Fault("GetPortTest1WithFeatures failed");
  }

  /*
   * @testName: GetPortTest2WithFeatures
   *
   * @assertion_ids: JAXWS:JAVADOC:156; JAXWS:JAVADOC:183; JAXWS:SPEC:4025
   *
   * @test_Strategy: Call Service.getPort(Class, WebServiceFeature ...) to
   * return a dynamic proxy for the service port. Pass a valid port name with
   * WSDL access. Verify that the method returns a dynamic proxy.
   */
  public void GetPortTest2WithFeatures() throws Fault {
    TestUtil.logTrace("GetPortTest2WithFeatures");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getPort (valid port name/WSDL access) AddressingFeature(true)");
      TestUtil.logMsg("Call Service.getPort(Class, WebServiceFeature ...)");
      TestUtil.logMsg("Should find port");
      Hello2 tests = (Hello2) service.getPort(Hello2.class, wsftrue);
      if (tests == null) {
        TestUtil.logErr("getPort(Class, WebServiceFeature ...) returned null");
        pass = false;
      } else if (!(tests instanceof Hello2)) {
        TestUtil.logErr("getPort(Class, WebServiceFeature ...) did not"
            + " return instance of Hello2");
        pass = false;
      }
      TestUtil.logMsg(
          "Test getPort (valid port name/WSDL access) AddressingFeature(false)");
      TestUtil.logMsg("Call Service.getPort(Class, WebServiceFeature ...)");
      TestUtil.logMsg("Should find port");
      tests = (Hello2) service.getPort(Hello2.class, wsffalse);
      if (tests == null) {
        TestUtil.logErr("getPort(Class, WebServiceFeature ...) returned null");
        pass = false;
      } else if (!(tests instanceof Hello2)) {
        TestUtil.logErr("getPort(Class, WebServiceFeature ...) did not"
            + " return instance of Hello2");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortTest2WithFeatures failed", e);
    }

    if (!pass)
      throw new Fault("GetPortTest2WithFeatures failed");
  }

  /*
   * @testName: GetPortTest3WithFeatures
   *
   * @assertion_ids: JAXWS:JAVADOC:157; JAXWS:JAVADOC:182; JAXWS:SPEC:4027;
   * JAXWS:JAVADOC:177;
   *
   * @test_Strategy: Call Service.getPort(EndpointReference, Class,
   * WebServiceFeature ...) to return a dynamic proxy for the service port. Pass
   * a valid port name with WSDL access. Verify that the method returns a
   * dynamic proxy.
   */
  public void GetPortTest3WithFeatures() throws Fault {
    TestUtil.logTrace("GetPortTest3WithFeatures");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }

      Hello2 tests2 = (Hello2) service.getPort(PORT2_QNAME, Hello2.class);
      epr = ((BindingProvider) tests2).getEndpointReference();
      TestUtil
          .logMsg("EndpointReference for port2 Hello2 is: " + epr.toString());

      TestUtil.logMsg(
          "Test getPort (valid port name/WSDL access) AddressingFeature(true)");
      TestUtil.logMsg(
          "Call Service.getPort(EndpointReference, Class, WebServiceFeature ...)");
      TestUtil.logMsg("Should find port");
      Hello2 tests = (Hello2) service.getPort(epr, Hello2.class, wsftrue);
      if (tests == null) {
        TestUtil
            .logErr("getPort(epr, Class, WebServiceFeature ...) returned null");
        pass = false;
      } else if (!(tests instanceof Hello2)) {
        TestUtil.logErr(
            "getPort(EndpointReference, Class, WebServiceFeature ...) did not"
                + " return instance of Hello2");
        pass = false;
      }
      TestUtil.logMsg(
          "Test getPort (valid port name/WSDL access) AddressingFeature(false)");
      TestUtil.logMsg(
          "Call Service.getPort(EndpointReference, Class, WebServiceFeature ...)");
      TestUtil.logMsg("Should find port");
      tests = (Hello2) service.getPort(epr, Hello2.class, wsffalse);
      if (tests == null) {
        TestUtil.logErr(
            "getPort(EndpointReference, Class, WebServiceFeature ...) returned null");
        pass = false;
      } else if (!(tests instanceof Hello2)) {
        TestUtil.logErr(
            "getPort(EndpointReference, Class,  WebServiceFeature ...) did not"
                + " return instance of Hello2");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortTest3WithFeatures failed", e);
    }

    if (!pass)
      throw new Fault("GetPortTest3WithFeatures failed");
  }

  /*
   * @testName: GetPort1NegTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4001; JAXWS:SPEC:4011; JAXWS:SPEC:4013;
   * JAXWS:JAVADOC:52; JAXWS:JAVADOC:122; JAXWS:JAVADOC:116;
   *
   * @test_Strategy: Call Service.getPort(QName, Class) to return a dynamic
   * proxy for the service port. Pass a valid port name with WSDL access. Verify
   * that the method returns a dynamic proxy. Verify that an exception occurs
   * due to port not found. Expect a WebServiceException.
   */
  public void GetPort1NegTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPort1NegTest1WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getPort (invalid port name/WSDL access) - negative test 1");
      TestUtil.logMsg("INVALID_PORT_QNAME=" + NONEXISTANT_PORT_QNAME);
      TestUtil.logMsg("Call Service.getPort(QName, Class)");
      TestUtil.logMsg("Should not find port (expect WebServiceException)");
      Hello tests = (Hello) service.getPort(NONEXISTANT_PORT_QNAME,
          Hello.class);
      TestUtil.logErr("Did not throw expected WebServiceException");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPort1NegTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPort1NegTest1WithWsdl failed");
  }

  /*
   * @testName: GetPort2PosTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:SPEC:4010;
   * JAXWS:JAVADOC:53; JAXWS:JAVADOC:123; JAXWS:JAVADOC:116;
   *
   * @test_Strategy: Call Service.getPort(Class) to return a dynamic proxy for
   * the service port. Pass a valid SEI class. Access to WSDL metadata. Verify
   * behavior.
   */
  public void GetPort2PosTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPort2PosTest1WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getPort (valid SEI class/WSDL access) - positive test 1");
      TestUtil.logMsg("Call Service.getPort(Class)");
      TestUtil.logMsg("Should find port");
      Hello tests = (Hello) service.getPort(Hello.class);
      if (tests == null) {
        TestUtil.logErr("getPort(Class) returned null");
        pass = false;
      } else if (!(tests instanceof Hello)) {
        TestUtil.logErr("getPort(Class) did not" + " return instance of Hello");
        pass = false;
      }
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPort2PosTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPort2PosTest1WithWsdl failed");
  }

  /*
   * @testName: GetPort2NegTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4001; JAXWS:SPEC:4011; JAXWS:SPEC:4013;
   * JAXWS:JAVADOC:53; JAXWS:JAVADOC:123; JAXWS:JAVADOC:116;
   *
   * @test_Strategy: Call Service.getPort(Class) to return a dynamic proxy for
   * the service port. Pass an invalid SEI class. WSDL metadata access. Verify
   * behavior. Expect WebServiceException.
   */
  public void GetPort2NegTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPort2NegTest1WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Test getPort (invalid SEI class/WSDL access) - negative test 1");
      TestUtil.logMsg("Call Service.getPort(Class)");
      TestUtil.logMsg("Should not find port (expect Exception)");
      Hello tests = (Hello) service
          .getPort((Class) java.util.regex.Pattern.class);
      TestUtil.logErr("Did not throw expected Exception");
      TestUtil.logErr("hello port=" + tests);
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException");
    } catch (Exception e) {
      TestUtil.logMsg("Caught expected Exception");
    }

    if (!pass)
      throw new Fault("GetPort2NegTest1WithWsdl failed");
  }

  /*
   * @testName: GetPortsTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:JAVADOC:54;
   * JAXWS:JAVADOC:124; JAXWS:JAVADOC:116;
   *
   * @test_Strategy: Call Service.getPorts() to return a list of qualified names
   * of the ports grouped by this service. Verify that the method returns a list
   * of qualified names of the ports grouped by this service. Create a Service
   * object with access to WSDL metadata.
   */
  public void GetPortsTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetPortsTest1WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Test getPorts with (WSDL access) - positive test 1");
      TestUtil.logMsg("Call Service.getPorts()");
      TestUtil.logMsg("Expect a non empty iterator of ports");
      Iterator i = service.getPorts();
      if (!i.hasNext()) {
        TestUtil.logErr("getPorts() returned empty iterator (unexpected)");
        pass = false;
      } else {
        int count = 0;
        while (i.hasNext()) {
          i.next();
          count++;
        }
        TestUtil.logMsg("getPorts() returned count of " + count);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortsTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetPortsTest1WithWsdl failed");
  }

  /*
   * @testName: GetServiceNameTest1
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:JAVADOC:55;
   * JAXWS:JAVADOC:125; JAXWS:JAVADOC:116;
   *
   * @test_Strategy: Call Service.getServiceName() to return the name of this
   * service.
   */
  public void GetServiceNameTest1() throws Fault {
    TestUtil.logTrace("GetServiceNameTest1");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Get service name via Service.getServiceName()");
      QName sname = service.getServiceName();
      TestUtil.logMsg(
          "ServiceName = " + SERVICE_QNAME + "\ngetServiceName() = " + sname);
      if (!sname.equals(SERVICE_QNAME)) {
        TestUtil.logErr("getServiceName() returned wrong QName");
        TestUtil.logErr("Expected " + SERVICE_QNAME + "\nGot " + sname);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetServiceNameTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetServiceNameTest1 failed");
  }

  /*
   * @testName: GetWSDLDocumentLocationTest1
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:JAVADOC:56;
   * JAXWS:JAVADOC:126; JAXWS:JAVADOC:116;
   *
   * @test_Strategy: Call Service.getWSDLDocumentLocation() to return the
   * location of the WSDL document for this service.
   */
  public void GetWSDLDocumentLocationTest1() throws Fault {
    TestUtil.logTrace("GetWSDLDocumentLocationTest1");
    boolean pass = false;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Call Service.getWSDLDocumentLocation() to get WSDL url");
      URL url = service.getWSDLDocumentLocation();
      TestUtil.logMsg("WSDLURL=" + url);
      if (url != null) {
        TestUtil.logMsg("WSDLURL is not null (expected)");
        pass = true;
      } else
        TestUtil.logErr("WSDLURL is null (unexpected)");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetWSDLDocumentLocationTest1 failed", e);
    }

    if (!pass)
      throw new Fault("GetWSDLDocumentLocationTest1 failed");
  }

  /*
   * @testName: GetHandlerResolverTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:JAVADOC:51;
   * JAXWS:JAVADOC:121; JAXWS:JAVADOC:116;
   *
   * @test_Strategy: Call Service.getHandlerResolver() to get the configured
   * HandlerResolver. Access to WSDL metadata. Verify behavior.
   */
  public void GetHandlerResolverTest1WithWsdl() throws Fault {
    TestUtil.logTrace("GetHandlerResolverTest1WithWsdl");
    boolean pass = false;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Call Service.getHandlerResolver() - should pass");
      HandlerResolver hr = service.getHandlerResolver();
      pass = true;
    } catch (WebServiceException e) {
      TestUtil.logErr("Caught unexpected WebServiceException");
      pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetHandlerResolverTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("GetHandlerResolverTest1WithWsdl failed");
  }

  /*
   * @testName: SetHandlerResolverTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:SPEC:4010;
   * JAXWS:JAVADOC:58; JAXWS:JAVADOC:128;
   *
   * @test_Strategy: Call Service.getHandlerResolver() to get the configured
   * HandlerResolver. Access to WSDL metadata. Verify behavior.
   */
  public void SetHandlerResolverTest1WithWsdl() throws Fault {
    TestUtil.logTrace("SetHandlerResolverTest1WithWsdl");
    boolean pass = false;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME, SERVICE_CLASS);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg("Call Service.getHandlerResolver() - should pass");
      service.setHandlerResolver(new HandlerResolver() {
        public List<Handler> getHandlerChain(PortInfo info) {
          return new ArrayList<Handler>();
        }
      });
      pass = true;
    } catch (WebServiceException e) {
      TestUtil.logErr("Caught unexpected WebServiceException");
      pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetHandlerResolverTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("SetHandlerResolverTest1WithWsdl failed");
  }

  /*
   * @testName: CreateDispatchTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:SPEC:4014;
   * JAXWS:JAVADOC:49; JAXWS:JAVADOC:119;
   *
   * @test_Strategy: Create a Dispatch object using the constructor via
   * Service.createDispatch(QName, Source, Mode). Verify that the Dispatch
   * object was successfully created.
   */
  public void CreateDispatchTest1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateDispatchTest1WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Create Dispatch object via Service.createDispatch(QName, Source, Mode)");
      Dispatch dispatch = service.createDispatch(PORT_QNAME, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD);
      if (dispatch == null) {
        TestUtil.logErr("Service.createDispatch() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateDispatchTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateDispatchTest1WithWsdl failed");
  }

  /*
   * @testName: CreateDispatchTest2WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:SPEC:4010;
   * JAXWS:SPEC:4014; JAXWS:JAVADOC:49; JAXWS:JAVADOC:119; JAXWS:JAVADOC:218;
   * JAXWS:JAVADOC:241;
   *
   * @test_Strategy: Create a Dispatch object using the constructor via
   * Service.createDispatch(QName, JAXBContext, Mode). Verify that the Dispatch
   * object was successfully created.
   */
  public void CreateDispatchTest2WithWsdl() throws Fault {
    TestUtil.logTrace("CreateDispatchTest2WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Create Dispatch object via Service.createDispatch(QName, JAXBContext, Mode)");
      Dispatch<Object> dispatch = createDispatchJAXB(service);
      if (dispatch == null) {
        TestUtil.logErr("Service.createDispatch() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateDispatchTest2WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateDispatchTest2WithWsdl failed");
  }

  /*
   * @testName: CreateDispatchTest1WithFeatures
   *
   * @assertion_ids: JAXWS:JAVADOC:153; JAXWS:JAVADOC:179;
   *
   * @test_Strategy: Create a Dispatch object using the constructor via
   * Service.createDispatch(QName, Source, Mode, WebsServiceFeature ...). Verify
   * that the Dispatch object was successfully created.
   */
  public void CreateDispatchTest1WithFeatures() throws Fault {
    TestUtil.logTrace("CreateDispatchTest1WithFeatures");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Create Dispatch via Service.createDispatch(QName, Source, Mode, WebServiceFeature ...) - Addressing(true)");
      Dispatch<Source> dispatch = service.createDispatch(PORT2_QNAME,
          Source.class, jakarta.xml.ws.Service.Mode.PAYLOAD, wsftrue);
      if (dispatch == null) {
        TestUtil.logErr("Service.createDispatch() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
      TestUtil.logMsg(
          "Create Dispatch via Service.createDispatch(QName, Source, Mode, WebServiceFeature ...) - Addressing(false)");
      dispatch = service.createDispatch(PORT2_QNAME, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD, wsffalse);
      if (dispatch == null) {
        TestUtil.logErr("Service.createDispatch() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateDispatchTest1WithFeatures failed", e);
    }

    if (!pass)
      throw new Fault("CreateDispatchTest1WithFeatures failed");
  }

  /*
   * @testName: CreateDispatchTest2WithFeatures
   *
   * @assertion_ids: JAXWS:JAVADOC:219; JAXWS:JAVADOC:242;
   *
   * @test_Strategy: Create a Dispatch object using the constructor via
   * Service.createDispatch(QName, JAXBContext, Mode, WebsServiceFeature ...).
   * Verify that the Dispatch object was successfully created.
   */
  public void CreateDispatchTest2WithFeatures() throws Fault {
    TestUtil.logTrace("CreateDispatchTest2WithFeatures");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Create Dispatch via Service.createDispatch(QName, JAXBContext, Mode, WebServiceFeature ...) - Addressing(true)");
      Dispatch<Object> dispatch = createDispatchJAXB(service, wsftrue);
      if (dispatch == null) {
        TestUtil.logErr(
            "Service.createDispatch(QName, JAXBContext, Mode, WebServiceFeature ...) returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
      TestUtil.logMsg(
          "Create Dispatch via Service.createDispatch(QName, JAXBContext, Mode, WebServiceFeature ...) - Addressing(false)");
      dispatch = createDispatchJAXB(service, wsffalse);
      if (dispatch == null) {
        TestUtil.logErr(
            "Service.createDispatch(QName, JAXBContext, Mode, WebServiceFeature ...) returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateDispatchTest2WithFeatures failed", e);
    }

    if (!pass)
      throw new Fault("CreateDispatchTest2WithFeatures failed");
  }

  /*
   * @testName: CreateDispatchObjectUsingEPRWithWFTrueTest
   *
   * @assertion_ids: JAXWS:JAVADOC:154; JAXWS:JAVADOC:180;
   *
   * @test_Strategy: Create a Dispatch object using the constructor via Service.
   * createDispatch(EndpointReference, Source, Mode, WebServiceFeature ...).
   * Verify that the Dispatch object was successfully created using EPR.
   */
  public void CreateDispatchObjectUsingEPRWithWFTrueTest() throws Fault {
    TestUtil.logTrace("CreateDispatchObjectUsingEPRWithWFTrueTest");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }

      Hello2 tests2 = (Hello2) service.getPort(PORT2_QNAME, Hello2.class);
      epr = ((BindingProvider) tests2).getEndpointReference();
      TestUtil
          .logMsg("EndpointReference for port2 Hello2 is: " + epr.toString());

      TestUtil.logMsg(
          "Create Dispatch via Service.createDispatch(EndpointReference, Source, Mode, WebServiceFeature ...) - Addressing(true)");
      String bindingID = new String(SOAPBinding.SOAP11HTTP_BINDING);
      Dispatch<Source> dispatch = service.createDispatch(epr, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD, wsftrue);
      if (dispatch == null) {
        TestUtil.logErr("Service.createDispatch() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
      TestUtil.logMsg(
          "Create Dispatch via Service.createDispatch(EndpointReference, Source, Mode, WebServiceFeature ...) - Addressing(false)");
      dispatch = service.createDispatch(epr, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD, wsffalse);
      if (dispatch == null) {
        TestUtil.logErr("Service.createDispatch() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateDispatchObjectUsingEPRWithWFTrueTest failed", e);
    }

    if (!pass)
      throw new Fault("CreateDispatchObjectUsingEPRWithWFTrueTest failed");
  }

  /*
   * @testName: CreateDispatchObjectUsingEPRWithWFTrueTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:220;
   *
   * @test_Strategy: Create a Dispatch object using the constructor via Service.
   * createDispatch(EndpointReference, JAXBContext, Mode, WebServiceFeature
   * ...). Verify that the Dispatch object was successfully created using EPR.
   */
  public void CreateDispatchObjectUsingEPRWithWFTrueTest2() throws Fault {
    TestUtil.logTrace("CreateDispatchObjectUsingEPRWithWFTrueTest2");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }

      Hello2 tests2 = (Hello2) service.getPort(PORT2_QNAME, Hello2.class);
      epr = ((BindingProvider) tests2).getEndpointReference();
      TestUtil
          .logMsg("EndpointReference for port2 Hello2 is: " + epr.toString());

      TestUtil.logMsg(
          "Create Dispatch via Service.createDispatch(EndpointReference, JAXBContext, Mode, WebServiceFeature ...) - Addressing(true)");
      String bindingID = new String(SOAPBinding.SOAP11HTTP_BINDING);
      Dispatch<Object> dispatch = service.createDispatch(epr,
          createJAXBContext(), jakarta.xml.ws.Service.Mode.PAYLOAD, wsftrue);
      if (dispatch == null) {
        TestUtil.logErr("Service.createDispatch() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
      TestUtil.logMsg(
          "Create Dispatch via Service.createDispatch(EndpointReference, JAXBContext, Mode, WebServiceFeature ...) - Addressing(false)");
      dispatch = service.createDispatch(epr, createJAXBContext(),
          jakarta.xml.ws.Service.Mode.PAYLOAD, wsffalse);
      if (dispatch == null) {
        TestUtil.logErr("Service.createDispatch() returned null");
        pass = false;
      } else
        TestUtil.logMsg("Dispatch object was successfully created");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateDispatchObjectUsingEPRWithWFTrueTest2 failed", e);
    }

    if (!pass)
      throw new Fault("CreateDispatchObjectUsingEPRWithWFTrueTest2 failed");
  }

  /*
   * @testName: CreateDispatchExceptionTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4001; JAXWS:SPEC:4014; JAXWS:SPEC:4016;
   * JAXWS:SPEC:4015; JAXWS:JAVADOC:49;
   *
   * @test_Strategy: Create a Dispatch object using the constructor via
   * Service.createDispatch(QName, Object, Mode). Pass an invalid port for the
   * QName. Verify that the call to createDispatch() throws a
   * WebServiceException.
   */
  public void CreateDispatchExceptionTest1WithWsdl() throws Fault {
    TestUtil.logTrace("CreateDispatchExceptionTest1WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil.logMsg(
          "Call Service.createDispatch(QName, Object, Mode) with invalid QName");
      TestUtil.logMsg("INVALID_PORT_QNAME=" + NONEXISTANT_PORT_QNAME);
      String bindingID = new String(SOAPBinding.SOAP11HTTP_BINDING);
      service.addPort(PORT_QNAME, bindingID, url);
      Dispatch dispatch = service.createDispatch(NONEXISTANT_PORT_QNAME,
          Source.class, jakarta.xml.ws.Service.Mode.PAYLOAD);
      TestUtil.logErr(
          "Service.createDispatch(QName. Object, Mode) did not throw expected WebServiceException");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreateDispatchExceptionTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreateDispatchExceptionTest1WithWsdl failed");
  }

  /*
   * @testName: CreatePortTest1WithWsdl
   *
   * @assertion_ids: JAXWS:SPEC:4000; JAXWS:SPEC:4009; JAXWS:SPEC:4010;
   * JAXWS:JAVADOC:46; JAXWS:JAVADOC:118;
   *
   * @test_Strategy: Create a port for service using Service.addPort(
   * javax.xml.namespace.QName, java.net.String, java.lang.String).
   */
  public void CreatePortTest1WithWsdl() throws Fault {
    TestUtil.logTrace("CreatePortTest1WithWsdl");
    boolean pass = true;
    try {
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      TestUtil
          .logMsg("Create a port via Service.addPort(QName, String, String)");
      String bindingID = new String(SOAPBinding.SOAP11HTTP_BINDING);
      service.addPort(NONEXISTANT_PORT_QNAME, bindingID, url);
      TestUtil.logMsg("Service.addPort() call was successfull");
    } catch (Exception e) {
      TestUtil.logErr("Service.addPort() call was unsuccessfull");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("CreatePortTest1WithWsdl failed", e);
    }

    if (!pass)
      throw new Fault("CreatePortTest1WithWsdl failed");
  }

  /*
   * @testName: getExecutorTest
   *
   * @assertion_ids: JAXWS:SPEC:4002; JAXWS:SPEC:4003; JAXWS:SPEC:4009;
   * JAXWS:SPEC:4005; JAXWS:JAVADOC:50; JAXWS:JAVADOC:120;
   *
   * @test_Strategy: Get the Executor for this instance.
   */
  public void getExecutorTest() throws Fault {
    TestUtil.logTrace("getExecutorTest");
    boolean pass = true;
    try {
      java.util.concurrent.Executor executor = new java.util.concurrent.ScheduledThreadPoolExecutor(
          1);
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      service.setExecutor(executor);
      java.util.concurrent.Executor executor2 = service.getExecutor();
      if (executor == null) {
        TestUtil.logErr("executor is null");
        pass = false;
      } else {
        TestUtil.logTrace("executor is not null");
        if (executor != executor2) {
          TestUtil.logErr(
              "The executor that was set was not the same one returned by the get");
          pass = false;
        } else {
          TestUtil.logTrace("The executors are the same for the set and get");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Service.getExecutor() call was unsuccessfull");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getExecutorTest failed", e);
    }

    if (!pass)
      throw new Fault("getExecutorTest failed");
  }

  /*
   * @testName: setExecutorTest
   *
   * @assertion_ids: JAXWS:SPEC:4002; JAXWS:SPEC:4003; JAXWS:SPEC:4009;
   * JAXWS:SPEC:4005; JAXWS:JAVADOC:57; JAXWS:JAVADOC:127;
   *
   * @test_Strategy: Set the Executor for this instance.
   */
  public void setExecutorTest() throws Fault {
    TestUtil.logTrace("setExecutorTest");
    boolean pass = true;
    try {
      java.util.concurrent.Executor executor = new java.util.concurrent.ScheduledThreadPoolExecutor(
          1);
      jakarta.xml.ws.Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXWS_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        TestUtil.logMsg("Get Initial Context");
        InitialContext ctx = new InitialContext();
        TestUtil.logMsg("Get JAXWS service instance with WSDL");
        service = (jakarta.xml.ws.Service) ctx
            .lookup("java:comp/env/service/" + SERVICE_NAME_WITH_WSDL);
      }
      service.setExecutor(executor);
      if (executor == null) {
        TestUtil.logErr("executor is null");
        pass = false;
      } else {
        TestUtil.logTrace("executor is not null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Service.setExecutor() call was unsuccessfull");
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("setExecutorTest failed", e);
    }

    if (!pass)
      throw new Fault("setExecutorTest failed");
  }

  private jakarta.xml.ws.Service getService(QName sname,
      WebServiceFeature[] wsfeatures) throws Exception {
    TestUtil.logMsg("JAXWS_Util:getService(QName, WebServiceFeature[])");
    jakarta.xml.ws.Service service = null;
    TestUtil.logMsg("QNAME=" + sname);
    TestUtil.logMsg(
        "Creating Service via jakarta.xml.ws.Service.create(QName, WebServiceFeature[])");
    service = jakarta.xml.ws.Service.create(sname, wsfeatures);
    if (service == null)
      TestUtil.logErr(
          "FATAL: jakarta.xml.ws.Service.create(QName, WebServiceFeature[]) returned a null");
    return service;
  }

  private jakarta.xml.ws.Service getService(URL wsdlurl, QName sname,
      WebServiceFeature[] wsfeatures) throws Exception {
    TestUtil.logMsg("JAXWS_Util:getService(URL, QName, WebServiceFeature[])");
    jakarta.xml.ws.Service service = null;
    if (wsdlurl != null)
      TestUtil.logMsg("URL=" + wsdlurl.toString());
    TestUtil.logMsg("QName=" + sname);
    TestUtil.logMsg(
        "Creating Service via jakarta.xml.ws.Service.create(URL, QName, WebServiceFeature[])");
    service = jakarta.xml.ws.Service.create(wsdlurl, sname, wsfeatures);
    if (service == null)
      TestUtil.logErr(
          "FATAL: jakarta.xml.ws.Service.create(URL, QName, WebServiceFeature[]) returned a null");
    return service;
  }
}
