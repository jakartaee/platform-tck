/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbWebServiceRefWithNoDDsTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import javax.xml.ws.*;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.net.URL;
import java.util.Properties;
import java.util.Iterator;

public class Client extends EETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsejbwebservicerefwithnoddstest.endpoint";

  private static final String WSDLLOC_URL = "wsejbwebservicerefwithnoddstest.wsdlloc";

  private String url = null;

  private static URL wsdlurl = null;

  private WSEjbWSRefWithNoDDsTestHelloEJB port;

  @WebServiceRef
  static WSEjbWSRefWithNoDDsTestHelloEJBService service;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPort() throws Exception {
    TestUtil.logMsg(
        "Get wsejbwebservicerefwithnoddstest Service via @WebServiceRef annotation");
    TestUtil.logMsg("Uses no attribute @WebServiceRef");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Get port from service");
    port = (WSEjbWSRefWithNoDDsTestHelloEJB) service
        .getWSEjbWSRefWithNoDDsTestHelloEJBPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Port obtained");
    TestUtil.logMsg(
        "Set the target endpoint address for this webservice since there are no DD's");
    JAXWS_Util.dumpTargetEndpointAddress(port);
    JAXWS_Util.setTargetEndpointAddress(port, url);
    JAXWS_Util.dumpTargetEndpointAddress(port);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap webservices-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;
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
      if (pass) {
        getTestURLs();
        getPort();
      }
    } catch (Exception e) {
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

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: WSEjbWebServiceRefWithNoDDsTestCallHello
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:115; WS4EE:SPEC:213;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * class and SEI class are packaged in the ear file. The @WebServiceRef uses
   * no field attributes. No deployment descriptors are uses in the packaging.
   */
  public void WSEjbWebServiceRefWithNoDDsTestCallHello() throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefWithNoDDsTestCallHello");
    try {
      String txt = port.hello("Hi there");
      if (txt.equals("Hi there to you too!"))
        TestUtil.logMsg("WSEjbWebServiceRefWithNoDDsTestCallHello passed");
      else
        throw new RuntimeException("Msg returned from hello() incorrect");
    } catch (Throwable t) {
      throw new Fault("WSEjbWebServiceRefWithNoDDsTestCallHello failed");
    }
    return;
  }

  /*
   * @testName: WSEjbWebServiceRefWithNoDDsTestCallBye
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:115; WS4EE:SPEC:213;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef and @WebService annotations. The EJBBean
   * class and SEI class are packaged in the ear file. The @WebServiceRef uses
   * no field attributes. No deployment descriptors are uses in the packaging.
   */
  public void WSEjbWebServiceRefWithNoDDsTestCallBye() throws Fault {
    TestUtil.logMsg("WSEjbWebServiceRefWithNoDDsTestCallBye");
    try {
      String txt = port.bye("Bye-bye");
      if (txt.equals("Bye-bye and take care"))
        TestUtil.logMsg("WSEjbWebServiceRefWithNoDDsTestCallBye passed");
      else
        throw new RuntimeException("Msg returned from bye() incorrect");
    } catch (Throwable t) {
      throw new Fault("WSEjbWebServiceRefWithNoDDsTestCallBye failed");
    }
    return;
  }
}
