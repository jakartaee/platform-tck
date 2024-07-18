/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices13.ejb.annotations.WSEjbPkgInWarUnderWebInfTest;

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

  @WebServiceRef(name = "service/wsejbpkginwarunderwebinftest")
  static HelloService service;

  private void getPort() throws Exception {
    TestUtil.logMsg(
        "Get wsejbpkginwarunderwebinftest Service via @WebServiceRef annotation");
    TestUtil.logMsg(
        "Uses name attribute @WebServiceRef(name=\"service/wsejbpkginwarunderwebinftest\")");
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
      if (pass)
        getPort();
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
   * @testName: CallHello
   *
   * @assertion_ids: WS4EE:SPEC:5012; WS4EE:SPEC:5013; WS4EE:SPEC:5014;
   *
   * @test_Strategy: Verify packaging the class files of an ejb webservice
   * endpoint within a WAR archive under WEB-INF/classes. This verifies the
   * packaging requirement of an ejb webservice endpoint within a WAR archive.
   */
  public void CallHello() throws Fault {
    TestUtil.logMsg("CallHello");
    try {
      String txt = port.hello("Hi there");
      if (txt.equals("Hi there to you too!"))
        TestUtil.logMsg("CallHello passed");
      else
        throw new RuntimeException("Msg returned from hello() incorrect");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("CallHello failed");
    }
    return;
  }

  /*
   * @testName: CallBye
   *
   * @assertion_ids: WS4EE:SPEC:5012; WS4EE:SPEC:5013; WS4EE:SPEC:5014;
   *
   * @test_Strategy: Verify packaging the class files of an ejb webservice
   * endpoint within a WAR archive under WEB-INF/classes. This verifies the
   * packaging requirement of an ejb webservice endpoint within a WAR archive.
   */
  public void CallBye() throws Fault {
    TestUtil.logMsg("CallBye");
    try {
      String txt = port.bye("Bye-bye");
      if (txt.equals("Bye-bye and take care"))
        TestUtil.logMsg("CallBye passed");
      else
        throw new RuntimeException("Msg returned from bye() incorrect");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("CallBye failed");
    }
    return;
  }
}
