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
 * @(#)URLClient.java	1.17 03/05/16
 */

package com.sun.ts.tests.interop.integration.jspejbjdbc;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import com.sun.javatest.Status;

//***************************************************************************
// An N-Tier end-to-end test scenario to demonstrate/validate J2EE technology
// using JSP technology across N-Tiers. Data validation is done at the client.
//
// Test Detail:
//
// Two scenarios tested
//
// Scenario1
//
// Client -> JSP -> EJB -> DataBase (jsp2ejb.jsp)
//
// Scenario2
//
// Client -> JSP -> JavaBean -> EJB -> DataBase (jspbean2ejb.jsp)
//***************************************************************************

public class URLClient extends EETest {
  private static final String TESTNAME = "Teller";

  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "yacko";

  private static final int PORTNUM = 8000;

  private static final String JSP = "/interop_integration_jspejbjdbc_web/jsp2ejb.jsp";

  private static final String JSPBEAN = "/interop_integration_jspejbjdbc_web/jspbean2ejb.jsp";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String EXPECTED_BALANCE = "10490.75";

  private static final String EXPECTED_DEPOSIT = "10590.75";

  private static final String EXPECTED_WITHDRAW = "10540.75";

  private TSURL ctsurl = new TSURL();

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

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
   * @testName: test1
   * 
   * @assertion_ids: JavaEE:SPEC:40; JavaEE:SPEC:40.1; JavaEE:SPEC:40.2;
   * JavaEE:SPEC:47; JavaEE:SPEC:65; JavaEE:SPEC:68; JavaEE:SPEC:203;
   * JavaEE:SPEC:204
   * 
   * @test_Strategy: Functional test to demonstrate an N-Tier client which
   * performs database transactions via accessing web server component, ejb
   * server component and database server component using the Application
   * Programming Model as described in the J2EE Platform Specification. The test
   * is a complete end-to-end tests and is modeled as follows: Demonstrates
   * URLClient to JSP to EJB interoperability.
   *
   * URLClient -> JSP -> EJB -> DB Create an N-Tier Application Test involving
   * jsp and ejb. Deploy jsp on one J2EE server. Deploy ejb on second J2EE
   * server. Verify correct operations.
   *
   */
  public void test1() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSP);
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String balance = p.getProperty("Balance");
      String deposit = p.getProperty("Deposit");
      String withdraw = p.getProperty("Withdraw");

      if (!balance.equals(EXPECTED_BALANCE)) {
        TestUtil.logErr("Balance of account incorrect: expected: "
            + EXPECTED_BALANCE + " Received: " + balance);
        pass = false;
      } else
        TestUtil.logMsg("Balance of account is correct: " + balance);

      if (!deposit.equals(EXPECTED_DEPOSIT)) {
        TestUtil.logErr("Deposit of account incorrect: expected: "
            + EXPECTED_DEPOSIT + " Received: " + deposit);
        pass = false;
      } else
        TestUtil.logMsg("Deposit of account is correct: " + deposit);

      if (!withdraw.equals(EXPECTED_WITHDRAW)) {
        TestUtil.logErr("Withdraw of account incorrect: expected: "
            + EXPECTED_WITHDRAW + " Received: " + withdraw);
        pass = false;
      } else
        TestUtil.logMsg("Withdraw of account is correct: " + withdraw);

      if (!pass)
        throw new Fault("test1 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test1 failed", e);
    }
  }

  /*
   * @testName: test2
   * 
   * @assertion_ids: JavaEE:SPEC:40; JavaEE:SPEC:40.1; JavaEE:SPEC:40.2;
   * JavaEE:SPEC:47; JavaEE:SPEC:65; JavaEE:SPEC:68; JavaEE:SPEC:203;
   * JavaEE:SPEC:204
   * 
   * @test_Strategy: Functional test to demonstrate an N-Tier client which
   * performs database transactions via accessing web server component, ejb
   * server component and database server component using the Application
   * Programming Model as described in the J2EE Platform Specification. The test
   * is a complete end-to-end tests and is modeled as follows: Demonstrates
   * URLClient to JSP to EJB interoperability.
   *
   * URLClient -> JSP -> JAVABEAN -> EJB -> DB Create an N-Tier Application Test
   * involving jsp and ejb. Deploy jsp on one J2EE server. Deploy ejb on second
   * J2EE server. Verify correct operations.
   *
   */
  public void test2() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPBEAN);
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String balance = p.getProperty("Balance");
      String deposit = p.getProperty("Deposit");
      String withdraw = p.getProperty("Withdraw");

      if (!balance.equals(EXPECTED_BALANCE)) {
        TestUtil.logErr("Balance of account incorrect: expected: "
            + EXPECTED_BALANCE + " Received: " + balance);
        pass = false;
      } else
        TestUtil.logMsg("Balance of account is correct: " + balance);

      if (!deposit.equals(EXPECTED_DEPOSIT)) {
        TestUtil.logErr("Deposit of account incorrect: expected: "
            + EXPECTED_DEPOSIT + " Received: " + deposit);
        pass = false;
      } else
        TestUtil.logMsg("Deposit of account is correct: " + deposit);

      if (!withdraw.equals(EXPECTED_WITHDRAW)) {
        TestUtil.logErr("Withdraw of account incorrect: expected: "
            + EXPECTED_WITHDRAW + " Received: " + withdraw);
        pass = false;
      } else
        TestUtil.logMsg("Withdraw of account is correct: " + withdraw);

      if (!pass)
        throw new Fault("test2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test2 failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
