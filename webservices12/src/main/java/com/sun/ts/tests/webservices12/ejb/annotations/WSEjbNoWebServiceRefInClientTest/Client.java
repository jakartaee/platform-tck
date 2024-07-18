/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbNoWebServiceRefInClientTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import jakarta.xml.ws.*;
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

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private Hello port;

  HelloService service;

  private void getPort() throws Exception {
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg(
        "JNDI lookup java:comp/env/service/wsejbnowebservicerefinclienttest");
    HelloService service = (HelloService) ctx
        .lookup("java:comp/env/service/wsejbnowebservicerefinclienttest");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Get port from service");
    port = (Hello) service.getHello();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Port obtained");
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

  public void setup(String[] args, Properties p) throws Exception {
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
      if (pass)
        getPort();
    } catch (Exception e) {
      throw new Exception("setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: WSEjbNoWebServiceRefInClientTestVerifyJNDILookupOfService
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. There is no @WebServiceRef annotation in client. The EJBBean
   * Implementation class is packaged in the ear file. There is no EJBHome or
   * EJBRemote implementation class packaged. Since no @WebServiceRef exists in
   * the client verify that the webservice can be obtained via a JNDI lookup.
   * Verify that the container correctly publishes the web service
   * implementation under "java:comp/env/service-ref-name".
   */
  public void WSEjbNoWebServiceRefInClientTestVerifyJNDILookupOfService()
      throws Exception {
    TestUtil
        .logMsg("WSEjbNoWebServiceRefInClientTestVerifyJNDILookupOfService");
    try {
      TestUtil.logMsg(
          "Verify JNDI lookup of \"java:comp/env/service/wsejbnowebservicerefinclienttest\"");
      InitialContext ctx = new InitialContext();
      TestUtil.logMsg(
          "JNDI lookup java:comp/env/service/wsejbnowebservicerefinclienttest");
      HelloService service = (HelloService) ctx
          .lookup("java:comp/env/service/wsejbnowebservicerefinclienttest");
      TestUtil.logMsg("service=" + service);
      TestUtil.logMsg(
          "WSEjbNoWebServiceRefInClientTestVerifyJNDILookupOfService passed");
    } catch (Throwable t) {
      throw new Exception(
          "WSEjbNoWebServiceRefInClientTestVerifyJNDILookupOfService failed");
    }
    return;
  }

  /*
   * @testName: WSEjbNoWebServiceRefInClientTestInvokeHelloBye
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: Test that invocation works. Call methods hello and bye.
   */
  public void WSEjbNoWebServiceRefInClientTestInvokeHelloBye() throws Exception {
    boolean pass = true;

    TestUtil.logMsg("WSEjbNoWebServiceRefInClientTestInvokeHelloBye");
    try {
      TestUtil.logMsg("Test invocation .....");
      TestUtil.logMsg("Invoke hello method");
      String txt = port.hello("Hello there");
      if (txt.equals("Hello there to you too!"))
        TestUtil.logMsg("Invoke of hello passed");
      else {
        pass = false;
        TestUtil.logErr("Invoke of hello failed");
      }
      TestUtil.logMsg("Invoke bye method");
      txt = port.bye("Bye");
      if (txt.equals("Bye and take care!"))
        TestUtil.logMsg("Invoke of bye passed");
      else {
        pass = false;
        TestUtil.logErr("Invoke of bye failed");
      }
    } catch (Throwable t) {
      throw new Exception("WSEjbNoWebServiceRefInClientTestInvokeHelloBye failed");
    }
    if (!pass)
      throw new Exception("WSEjbNoWebServiceRefInClientTestInvokeHelloBye failed");
  }
}
