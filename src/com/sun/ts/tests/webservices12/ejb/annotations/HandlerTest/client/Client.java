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

/*
 $Id$
 */
package com.sun.ts.tests.webservices12.ejb.annotations.HandlerTest.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.ws.*;
import java.util.Properties;
import com.sun.ts.tests.jaxws.common.*;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  Hello port = null;

  HandlerTestService service = null;

  // Get Port and Stub access via porting layer interface

  private void getPortJavaEE() throws Exception {
    port = (Hello) service.getHelloPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
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
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
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
      TestUtil.logMsg(
          "WebServiceRef is not set in Client (get it from specific vehicle)");
      service = (HandlerTestService) getSharedObject();
      getPortJavaEE();
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
   * @testName: transformBodyTest
   *
   * @assertion_ids: WS4EE:SPEC:6007
   *
   * @test_Strategy: Use a handler to transform a SOAPBody.
   */
  public void transformBodyTest() throws Fault {
    TestUtil.logMsg("transformBodyTest");
    boolean pass = true;
    boolean fault = false;
    String expected1 = "transformBodyTest";
    String expected2 = "InboundClientLogicalHandler";
    String expected3 = "OutboundServerLogicalHandler";
    String expected4 = "InboundServerLogicalHandler";
    String expected5 = "OutboundClientLogicalHandler";
    String expected6 = "OutboundClientSOAPHandler";
    String expected7 = "InboundServerSOAPHandler";
    String expected8 = "OutboundServerSOAPHandler";
    String expected9 = "InboundClientSOAPHandler";

    try {
      String result = port.hello("transformBodyTest");
      TestUtil.logMsg("Return value = " + result);
      if (result.indexOf(expected1) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected1 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected2) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected2 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected3) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected3 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected4) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected4 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected5) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected5 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected6) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected6 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected7) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected7 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected8) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected8 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected9) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected9 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("transformBodyTest failed");
  }

  /*
   * @testName: transformHeaderTest
   *
   * @assertion_ids: WS4EE:SPEC:6006
   *
   * @test_Strategy: Use a handler to transform a SOAPHeader.
   */
  public void transformHeaderTest() throws Fault {
    TestUtil.logMsg("transformHeaderTest");
    boolean pass = true;
    HeaderType ht = null;
    String expected = "theTransformHeaderOutboundClientSOAPHandlerInboundServerSOAPHandlerOutboundServerSOAPHandlerInboundClientSOAPHandler";
    try {
      ht = new HeaderType();
      ht.setMyheader("theTransformHeader");
      Holder<HeaderType> hht = new Holder<HeaderType>();
      hht.value = ht;

      port.hello2("transformHeaderTest", hht);

      String result = hht.value.getMyheader();

      TestUtil.logMsg("Return value = " + result);
      if (!result.equals(expected)) {
        pass = false;
        TestUtil.logErr("Expected =" + expected);
        TestUtil.logErr("Actual=" + result);
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("transformHeaderTest failed");
  }

}
