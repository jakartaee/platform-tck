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
 * @(#)URLClient.java	1.14 03/05/16
 */

package com.sun.ts.tests.interop.tx.webclient.jsp;

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

  private static final String HOSTNAME = "yacko";

  private static final int PORTNUM = 8000;

  private static final String JSPBEAN = "/interop_tx_jsp_web/jspbean2ejb.jsp";

  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private TSURL ctsurl = new TSURL();

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
   * EJBServer1TxInteropEnabled; EJBServer2TxInteropEnabled; webServerHost, the
   * web server host; webServerPort, the web server port;
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
   * @assertion: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * Test the transaction interoperability from JSP to EJB.
   *
   * Test the three scenarios for EJB Containers supporting transaction
   * interoperability in section 19.5.2.2.1.
   *
   * EJB Specification (Chapter 19.5) Transaction Interoperability
   *
   * EJB Containers supporting transaction interoperability 1) If there is no
   * OTS transaction context in the IIOP message then follow defined behavior in
   * section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in section 17.6. 3) If there is a null
   * transaction context in the IIOP message then follow defined behavior in
   * table 19.5.2.2.1.
   *
   * Test the three scenarios for EJB Containers not supporting transaction
   * interoperability in section 19.5.2.2.2.
   *
   * EJB Containers not supporting transaction interoperability 1) If there is
   * no OTS transaction context in the IIOP message then follow defined behavior
   * in section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in table 19.5.2.2.2. 3) If there is a
   * null transaction context in the IIOP message then follow defined behavior
   * in table 19.5.2.2.1.
   *
   * Test using bean-managed transactions.
   *
   * (Section: 17.6 bean-managed transaction demarcation) (Section: 19.5.2.2.1
   * EJB containers supporting transaction interoperability) (Section:
   * 19.5.2.2.2 EJB containers not supporting transaction interoperability)
   *
   * @test_Strategy: Access a JSP to perform transaction propagation. Obtain the
   * UserTransaction interface. Perform a global transaction using the TxBean
   * (deployed as TX_BEAN_MANAGED) to a single RDBMS table.
   *
   * The TxBean EJB Container is configured to support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * The TxBean EJB Container is configured to not support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   *
   * Insert/Delete followed by a commit to a single table.
   *
   */

  public void test1() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPBEAN);
      props.setProperty("TESTNAME", "test1");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String result = p.getProperty("RESULT");

      if (result.equals("false")) {
        pass = false;
      } else
        pass = true;

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
   * @assertion: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * Test the transaction interoperability from JSP to EJB.
   *
   * Test the three scenarios for EJB Containers supporting transaction
   * interoperability in section 19.5.2.2.1.
   *
   * EJB Specification (Chapter 19.5) Transaction Interoperability
   *
   * EJB Containers supporting transaction interoperability 1) If there is no
   * OTS transaction context in the IIOP message then follow defined behavior in
   * section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in section 17.6. 3) If there is a null
   * transaction context in the IIOP message then follow defined behavior in
   * table 19.5.2.2.1.
   *
   * Test the three scenarios for EJB Containers not supporting transaction
   * interoperability in section 19.5.2.2.2.
   *
   * EJB Containers not supporting transaction interoperability 1) If there is
   * no OTS transaction context in the IIOP message then follow defined behavior
   * in section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in table 19.5.2.2.2. 3) If there is a
   * null transaction context in the IIOP message then follow defined behavior
   * in table 19.5.2.2.1.
   *
   * Test using bean-managed transactions.
   *
   * (Section: 17.6 bean-managed transaction demarcation) (Section: 19.5.2.2.1
   * EJB containers supporting transaction interoperability) (Section:
   * 19.5.2.2.2 EJB containers not supporting transaction interoperability)
   *
   * @test_Strategy: Access a JSP to perform transaction propagation. Obtain the
   * UserTransaction interface. Perform a global transaction using the TxBean
   * (deployed as TX_BEAN_MANAGED) to a single RDBMS table.
   *
   * The TxBean EJB Container is configured to support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * The TxBean EJB Container is configured to not support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   *
   * Insert/Delete followed by a rollback to a single table.
   *
   */

  public void test2() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPBEAN);
      props.setProperty("TESTNAME", "test2");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String result = p.getProperty("RESULT");

      if (result.equals("false")) {
        pass = false;
      } else
        pass = true;

      if (!pass)
        throw new Fault("test2 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test2 failed", e);
    }
  }

  /*
   * @testName: test3
   *
   * @assertion: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * Test the transaction interoperability from JSP to EJB.
   *
   * Test the three scenarios for EJB Containers supporting transaction
   * interoperability in section 19.5.2.2.1.
   *
   * EJB Specification (Chapter 19.5) Transaction Interoperability
   *
   * EJB Containers supporting transaction interoperability 1) If there is no
   * OTS transaction context in the IIOP message then follow defined behavior in
   * section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in section 17.6. 3) If there is a null
   * transaction context in the IIOP message then follow defined behavior in
   * table 19.5.2.2.1.
   *
   * Test the three scenarios for EJB Containers not supporting transaction
   * interoperability in section 19.5.2.2.2.
   *
   * EJB Containers not supporting transaction interoperability 1) If there is
   * no OTS transaction context in the IIOP message then follow defined behavior
   * in section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in table 19.5.2.2.2. 3) If there is a
   * null transaction context in the IIOP message then follow defined behavior
   * in table 19.5.2.2.1.
   *
   * Test using bean-managed transactions.
   *
   * (Section: 17.6 bean-managed transaction demarcation) (Section: 19.5.2.2.1
   * EJB containers supporting transaction interoperability) (Section:
   * 19.5.2.2.2 EJB containers not supporting transaction interoperability)
   *
   * @test_Strategy: Access a JSP to perform transaction propagation. Obtain the
   * UserTransaction interface. Perform a global transaction using the TxBean
   * (deployed as TX_NOT_SUPPORTED) to a single RDBMS table.
   *
   * The TxBean EJB Container is configured to support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * The TxBean EJB Container is configured to not support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   *
   * Insert/Delete followed by a commit to a single table.
   *
   */

  public void test3() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPBEAN);
      props.setProperty("TESTNAME", "test3");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String result = p.getProperty("RESULT");

      if (result.equals("false")) {
        pass = false;
      } else
        pass = true;

      if (!pass)
        throw new Fault("test3 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test3 failed", e);
    }
  }

  /*
   * @testName: test4
   *
   * @assertion: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * Test the transaction interoperability from JSP to EJB.
   *
   * Test the three scenarios for EJB Containers supporting transaction
   * interoperability in section 19.5.2.2.1.
   *
   * EJB Specification (Chapter 19.5) Transaction Interoperability
   *
   * EJB Containers supporting transaction interoperability 1) If there is no
   * OTS transaction context in the IIOP message then follow defined behavior in
   * section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in section 17.6. 3) If there is a null
   * transaction context in the IIOP message then follow defined behavior in
   * table 19.5.2.2.1.
   *
   * Test the three scenarios for EJB Containers not supporting transaction
   * interoperability in section 19.5.2.2.2.
   *
   * EJB Containers not supporting transaction interoperability 1) If there is
   * no OTS transaction context in the IIOP message then follow defined behavior
   * in section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in table 19.5.2.2.2. 3) If there is a
   * null transaction context in the IIOP message then follow defined behavior
   * in table 19.5.2.2.1.
   *
   * Test using bean-managed transactions.
   *
   * (Section: 17.6 bean-managed transaction demarcation) (Section: 19.5.2.2.1
   * EJB containers supporting transaction interoperability) (Section:
   * 19.5.2.2.2 EJB containers not supporting transaction interoperability)
   *
   * @test_Strategy: Access a JSP to perform transaction propagation. Obtain the
   * UserTransaction interface. Perform a global transaction using the TxBean
   * (deployed as TX_NOT_SUPPORTED) to a single RDBMS table.
   *
   * The TxBean EJB Container is configured to support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * The TxBean EJB Container is configured to not support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   *
   * Insert/Delete followed by a rollback to a single table.
   *
   */

  public void test4() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPBEAN);
      props.setProperty("TESTNAME", "test4");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String result = p.getProperty("RESULT");

      if (result.equals("false")) {
        pass = false;
      } else
        pass = true;

      if (!pass)
        throw new Fault("test4 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test4 failed", e);
    }
  }

  /*
   * @testName: test9
   *
   * @assertion: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * Test the transaction interoperability from JSP to EJB.
   *
   * Test the three scenarios for EJB Containers supporting transaction
   * interoperability in section 19.5.2.2.1.
   *
   * EJB Specification (Chapter 19.5) Transaction Interoperability
   *
   * EJB Containers supporting transaction interoperability 1) If there is no
   * OTS transaction context in the IIOP message then follow defined behavior in
   * section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in section 17.6. 3) If there is a null
   * transaction context in the IIOP message then follow defined behavior in
   * table 19.5.2.2.1.
   *
   * Test the three scenarios for EJB Containers not supporting transaction
   * interoperability in section 19.5.2.2.2.
   *
   * EJB Containers not supporting transaction interoperability 1) If there is
   * no OTS transaction context in the IIOP message then follow defined behavior
   * in section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in table 19.5.2.2.2. 3) If there is a
   * null transaction context in the IIOP message then follow defined behavior
   * in table 19.5.2.2.1.
   *
   * Test using bean-managed transactions.
   *
   * (Section: 17.6 bean-managed transaction demarcation) (Section: 19.5.2.2.1
   * EJB containers supporting transaction interoperability) (Section:
   * 19.5.2.2.2 EJB containers not supporting transaction interoperability)
   *
   * @test_Strategy: Access a JSP to perform transaction propagation. Obtain the
   * UserTransaction interface. Perform a global transaction using the TxBean
   * (deployed as TX_REQUIRES_NEW) to a single RDBMS table.
   *
   * The TxBean EJB Container is configured to support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * The TxBean EJB Container is configured to not support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   *
   * Insert/Delete followed by a commit to a single table.
   *
   */

  public void test9() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPBEAN);
      props.setProperty("TESTNAME", "test9");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String result = p.getProperty("RESULT");

      if (result.equals("false")) {
        pass = false;
      } else
        pass = true;

      if (!pass)
        throw new Fault("test9 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test9 failed", e);
    }
  }

  /*
   * @testName: test10
   *
   * @assertion: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * Test the transaction interoperability from JSP to EJB.
   *
   * Test the three scenarios for EJB Containers supporting transaction
   * interoperability in section 19.5.2.2.1.
   *
   * EJB Specification (Chapter 19.5) Transaction Interoperability
   *
   * EJB Containers supporting transaction interoperability 1) If there is no
   * OTS transaction context in the IIOP message then follow defined behavior in
   * section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in section 17.6. 3) If there is a null
   * transaction context in the IIOP message then follow defined behavior in
   * table 19.5.2.2.1.
   *
   * Test the three scenarios for EJB Containers not supporting transaction
   * interoperability in section 19.5.2.2.2.
   *
   * EJB Containers not supporting transaction interoperability 1) If there is
   * no OTS transaction context in the IIOP message then follow defined behavior
   * in section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in table 19.5.2.2.2. 3) If there is a
   * null transaction context in the IIOP message then follow defined behavior
   * in table 19.5.2.2.1.
   *
   * Test using bean-managed transactions.
   *
   * (Section: 17.6 bean-managed transaction demarcation) (Section: 19.5.2.2.1
   * EJB containers supporting transaction interoperability) (Section:
   * 19.5.2.2.2 EJB containers not supporting transaction interoperability)
   *
   * @test_Strategy: Access a JSP to perform transaction propagation. Obtain the
   * UserTransaction interface. Perform a global transaction using the TxBean
   * (deployed as TX_REQUIRES_NEW) to a single RDBMS table.
   *
   * The TxBean EJB Container is configured to support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * The TxBean EJB Container is configured to not support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   *
   * Insert/Delete followed by a rollback to a single table.
   *
   */

  public void test10() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPBEAN);
      props.setProperty("TESTNAME", "test10");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String result = p.getProperty("RESULT");

      if (result.equals("false")) {
        pass = false;
      } else
        pass = true;

      if (!pass)
        throw new Fault("test10 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test10 failed", e);
    }
  }

  /*
   * @testName: test13
   *
   * @assertion: EJB Containers supporting transaction interoperability must
   * behave in a defined manner. EJB Containers not supporting transaction
   * interoperability must also behave in a defined manner.
   *
   * Test the transaction interoperability from JSP to EJB.
   *
   * Test the three scenarios for EJB Containers supporting transaction
   * interoperability in section 19.5.2.2.1.
   *
   * EJB Specification (Chapter 19.5) Transaction Interoperability
   *
   * EJB Containers supporting transaction interoperability 1) If there is no
   * OTS transaction context in the IIOP message then follow defined behavior in
   * section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in section 17.6. 3) If there is a null
   * transaction context in the IIOP message then follow defined behavior in
   * table 19.5.2.2.1.
   *
   * Test the three scenarios for EJB Containers not supporting transaction
   * interoperability in section 19.5.2.2.2.
   *
   * EJB Containers not supporting transaction interoperability 1) If there is
   * no OTS transaction context in the IIOP message then follow defined behavior
   * in section 17.6. 2) If there is a valid OTS transaction context in the IIOP
   * message then follow defined behavior in table 19.5.2.2.2. 3) If there is a
   * null transaction context in the IIOP message then follow defined behavior
   * in table 19.5.2.2.1.
   *
   * Test using bean-managed transactions.
   *
   * (Section: 17.6 bean-managed transaction demarcation) (Section: 19.5.2.2.1
   * EJB containers supporting transaction interoperability) (Section:
   * 19.5.2.2.2 EJB containers not supporting transaction interoperability)
   *
   * @test_Strategy: Access a JSP to perform transaction propagation. Obtain the
   * UserTransaction interface. Perform a global transaction using the TxBean
   * (deployed as TX_NEVER) to a single RDBMS table.
   *
   * The TxBean EJB Container is configured to support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #1: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=true
   *
   * Test Config #2: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=true
   *
   * The TxBean EJB Container is configured to not support transaction
   * interoperability. The following TS properties must be set in the ts.jte
   * file for the following 2 test configurations:
   *
   * Test Config #3: EJBServer1TxInteropEnabled=false
   * EJBServer2TxInteropEnabled=false
   *
   * Test Config #4: EJBServer1TxInteropEnabled=true
   * EJBServer2TxInteropEnabled=false
   *
   *
   */

  public void test13() throws Fault {
    try {
      boolean pass = true;

      url = ctsurl.getURL(PROTOCOL, hostname, portnum, JSPBEAN);
      props.setProperty("TESTNAME", "test13");
      urlConn = TestUtil.sendPostData(props, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());
      TestUtil.logMsg("Response is ................");
      Properties p = TestUtil.getResponseProperties(urlConn);
      TestUtil.list(p);
      String result = p.getProperty("RESULT");

      if (result.equals("false")) {
        pass = false;
      } else
        pass = true;

      if (!pass)
        throw new Fault("test13 failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("test13 failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }
}
