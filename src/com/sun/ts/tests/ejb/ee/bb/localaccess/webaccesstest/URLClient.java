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
 * @(#)URLClient.java	1.30 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.localaccess.webaccesstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class URLClient extends EETest {

  private static final String PROTOCOL = "http";

  private static final String JSPA = "/bb_localaccess_webaccesstest_web/jsp2aejb.jsp";

  private static final String JSPB = "/bb_localaccess_webaccesstest_web/jsp2bejb.jsp";

  private static final String JSPC = "/bb_localaccess_webaccesstest_web/jsp2cejb.jsp";

  private static final String JSPD = "/bb_localaccess_webaccesstest_web/jsp2dejb.jsp";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL ctsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String hostname = null;

  private int portnum = 0;

  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * webServerHost, the web server host; webServerPort, the web server port;
   * 
   * @class.testArgs: -ap tssql.stmt
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
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("Setup failed:");
    }
  }

  /*
   * @testName: webLocalAccessTest1
   * 
   * @assertion_ids: EJB:SPEC:2.4; EJB:SPEC:147.1; EJB:SPEC:147.2;
   * EJB:SPEC:147.5
   * 
   * @test_Strategy: A web component has access to the LocalHome and Local
   * Interfaces of a Local Entity Bean (CMP20). Verify local access from JSP to
   * a local Entity CMP20 Bean.
   */

  public void webLocalAccessTest1() throws Fault {
    try {
      String expectedResult1 = "entity-cmp";
      boolean pass = true;
      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPA);
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String s = p.getProperty("whoAmI");

      if (!expectedResult1.equals(s)) {
        TestUtil.logErr("Incorrect Results: Expected: " + expectedResult1
            + " Received: " + s);
        pass = false;
      }

      if (!pass)
        throw new Fault("webLocalAccessTest1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("webLocalAccessTest1 failed", e);
    }
  }

  /*
   * @testName: webLocalAccessTest2
   * 
   * @assertion_ids: EJB:SPEC:2.4; EJB:SPEC:45.1; EJB:SPEC:45.2; EJB:SPEC:45.4
   * 
   * @test_Strategy: A web component has access to the LocalHome and Local
   * Interfaces of a Local Stateful Session Bean. Verify local access from JSP
   * to a local Stateful Session Bean.
   *
   *
   */
  public void webLocalAccessTest2() throws Fault {
    try {
      String expectedResult2 = "session-stateful";
      boolean pass = true;
      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPB);
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String s = p.getProperty("whoAmI");

      if (!expectedResult2.equals(s)) {
        TestUtil.logErr("Incorrect Results: Expected: " + expectedResult2
            + " Received: " + s);
        pass = false;
      }

      if (!pass)
        throw new Fault("webLocalAccessTest2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("webLocalAccessTest2 failed", e);
    }
  }

  /*
   * @testName: webLocalAccessTest3
   * 
   * @assertion_ids: EJB:SPEC:2.4; EJB:SPEC:147.1; EJB:SPEC:147.2;
   * EJB:SPEC:147.5
   * 
   * @test_Strategy: A web component has access to the LocalHome and Local
   * Interfaces of a Local Entity BMP Bean. Verify local access from JSP to a
   * local Entity BMP Bean.
   *
   *
   */
  public void webLocalAccessTest3() throws Fault {
    try {
      String expectedResult3 = "entity-bmp";
      boolean pass = true;
      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPC);
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String s = p.getProperty("whoAmI");

      if (!expectedResult3.equals(s)) {
        TestUtil.logErr("Incorrect Results: Expected: " + expectedResult3
            + " Received: " + s);
        pass = false;
      }

      if (!pass)
        throw new Fault("webLocalAccessTest3 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("webLocalAccessTest3 failed", e);
    }
  }

  /*
   * @testName: webLocalAccessTest4
   * 
   * @assertion_ids: EJB:SPEC:2.4; EJB:SPEC:45.1; EJB:SPEC:45.2; EJB:SPEC:45.4
   * 
   * @test_Strategy: A web component has access to the LocalHome and Local
   * Interfaces of a Local Stateless Session Bean. Verify local access from JSP
   * to a local Stateless Session Bean.
   */

  public void webLocalAccessTest4() throws Fault {
    try {
      String expectedResult4 = "session-stateless";
      boolean pass = true;
      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPD);
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String s = p.getProperty("whoAmI");

      if (!expectedResult4.equals(s)) {
        TestUtil.logErr("Incorrect Results: Expected: " + expectedResult4
            + " Received: " + s);
        pass = false;
      }

      if (!pass)
        throw new Fault("webLocalAccessTest4 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("webLocalAccessTest4 failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
