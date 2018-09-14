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
 * @(#)MDBClient.java	1.9 03/05/16
 */

package com.sun.ts.tests.connector.localTx.msginflow;

import java.io.*;
import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;
import com.sun.ts.tests.common.connector.util.TSMessageListenerInterface;
import javax.resource.spi.endpoint.MessageEndpointFactory;

public class MDBClient extends ServiceEETest implements Serializable {

  // Harness requirements
  private TSNamingContext nctx = null;

  private TSConnection con = null;

  private String whitebox_tx = null;

  private String whitebox_tx_param = null;

  private String whitebox_ibanno_no_md = null;

  private TSDataSource ds1 = null;

  private TSDataSource ds2 = null;

  private TSDataSource ds3 = null;

  private String uname = null;

  private String password = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    MDBClient theTests = new MDBClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-tx, JNDI name of TS WhiteBox;
   * whitebox-tx-param, conn w/ params; whitebox-ibanno_no_md; rauser1, user
   * name; rapassword1, password for rauser1;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_tx = p.getProperty("whitebox-tx");
    whitebox_tx_param = p.getProperty("whitebox-tx-param");
    whitebox_ibanno_no_md = p.getProperty("whitebox-ibanno_no_md");

    logMsg("Using: " + whitebox_tx);
    logMsg("Using: " + whitebox_tx_param);
    logMsg("Using: " + whitebox_ibanno_no_md);

    // For application level sign on
    uname = p.getProperty("rauser1");
    password = p.getProperty("rapassword1");

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage());
    }

    // Obtain our TSDataSources for interacting with our TS whitebox
    try {
      nctx = new TSNamingContext();
      ds1 = (TSDataSource) nctx.lookup(whitebox_tx);
      ds2 = (TSDataSource) nctx.lookup(whitebox_tx_param);
      ds3 = (TSDataSource) nctx.lookup(whitebox_ibanno_no_md);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testReceiveMessage
   *
   * @assertion_ids: Connector:SPEC:82; Connector:SPEC:285; Connector:SPEC:286;
   * Connector:SPEC:302; Connector:JAVADOC:58; Connector:JAVADOC:314;
   * Connector:JAVADOC:316;
   * 
   * @test_Strategy: Deploy the MDB and whitebox, whitebox will narrows the
   * endpoint and will deliver the message to the endpoint instance. Check
   * whether the endpoint preferences still stays the same.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testReceiveMessage() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTx Message To MDB";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + toCheck1);
    }

  }

  /*
   * @testName: testProxyInterfaceImp
   *
   * @assertion_ids: Connector:SPEC:96; Connector:SPEC:285; Connector:SPEC:286;
   * Connector:SPEC:302;
   * 
   * @test_Strategy: get the application proxy endpoint instance and check with
   * through reflection package that MessageEndpoint interface and
   * MessageListener interface is implemented.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testProxyInterfaceImp() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTx MessageEndpoint interface implemented";
    String toCheck2 = "LocalTx TSMessageListener interface implemented";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    String errmsg = "";
    if (!b1) {
      errmsg = toCheck1;
    }

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (!b2) {
      if (!b1) {
        errmsg += "   AND  ";
      }
      errmsg += toCheck2;
    }

    if (b1 && b2) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + errmsg);
    }

  }

  /*
   * @testName: testUniqueMessageEndpoint
   *
   * @assertion_ids: Connector:SPEC:114; Connector:SPEC:285; Connector:SPEC:286;
   * Connector:SPEC:302; Connector:JAVADOC:320;
   * 
   * @test_Strategy: create two messageEndpoint in the whitebox and compare
   * them. If they are hashcode is not equal then tests passes.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testUniqueMessageEndpoint() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTx Unique MessageEndpoint returned";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + toCheck1);
    }

  }

  /*
   * @testName: testMessageEndpointFactoryForEquals
   *
   * @assertion_ids: Connector:SPEC:93; Connector:SPEC:285; Connector:SPEC:286;
   * Connector:SPEC:302;
   * 
   * 
   * @test_Strategy: Deploy the whitebox and both the mdb and compare both the
   * MessageEndpointfactory if they are not equal then the test passes.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testMessageEndpointFactoryForEquals() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTx MessageEndpointFactory equals implemented correctly";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + toCheck1);
    }

  }

  /*
   * @testName: testUniqueMessageEndpointFactory
   *
   * @assertion_ids: Connector:SPEC:92; Connector:SPEC:285; Connector:SPEC:286;
   * Connector:SPEC:302;
   * 
   * 
   * @test_Strategy: Deploy the whitebox and mdb and check through the equals if
   * they are different or not MessageEndpointFactory.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testUniqueMessageEndpointFactory() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTx MessageEndpointFactory is Unique";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + toCheck1);
    }

  }

  /*
   * @testName: testEndpointActivationName
   *
   * @assertion_ids: Connector:SPEC:319;
   * 
   * 
   * @test_Strategy: Deploy the whitebox_ibanno_no_md and mdb and validate that
   * the MEF is able to call getActivationName() and get a valid (non-null)
   * activation name for our MEF.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejb and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testEndpointActivationName() throws Fault {

    Vector log = null;
    boolean b1 = false;

    TestUtil.logMsg("Enterred testEndpointActivationName()");

    try {
      log = ds3.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "IBAnnotatedResourceAdapterImpl.endpointActivation() getActivationName() returned nonNull name";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        TestUtil.logTrace(str);
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("getActivationName() called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + toCheck1);
    }

  }

  /*
   * @testName: testGetEndpoinClass
   *
   * @assertion_ids: Connector:SPEC:318;
   * 
   * @test_Strategy: Deploy the whitebox_ibanno_no_md and mdb and validate that
   * the MEF is able to call getEndpointClass() and get a valid (non-null)
   * classname for our MEF.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testGetEndpoinClass() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Enterred testGetEndpoinClass()");
    try {
      log = ds3.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "IBAnnotatedResourceAdapterImpl.endpointActivation() getEndpointClass() returned: com.sun.ts.tests.connector.mdb.JCAMessageBean";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        TestUtil.logTrace(str);
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("getEndpointClass() called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + toCheck1);
    }
  }

  /*
   * @testName: testMessageDeliveryTransacted
   *
   * @assertion_ids: Connector:SPEC:117; Connector:SPEC:111; Connector:SPEC:99;
   * Connector:SPEC:285; Connector:SPEC:84; Connector:SPEC:286;
   * Connector:SPEC:302; Connector:JAVADOC:324; Connector:JAVADOC:258;
   * 
   * @test_Strategy: Deploy the whitebox and transaction required mdb provide an
   * XAResource while creating the endpoint and see if the message delivery is
   * transacted and XAResource was called.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testMessageDeliveryTransacted() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTx MDB2 Transacted Message To MDB";
    String toCheck2 = "LocalTxMessageXAResource1.end";
    String toCheck3 = "LocalTx MDB2 delivery is transacted";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    String errmsg = "";

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (!b1) {
      errmsg += toCheck1;
    }

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (!b2) {
      errmsg += "   AND   " + toCheck2;
    }

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
    }

    if (!b3) {
      errmsg += "   AND   " + toCheck3;
    }

    if (b1 && b2 && b3) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + errmsg);
    }

  }

  /*
   * @testName: testMessageDeliveryNonTransacted
   *
   * @assertion_ids: Connector:SPEC:118; Connector:SPEC:285; Connector:SPEC:286;
   * Connector:SPEC:302; Connector:JAVADOC:402;
   * 
   * 
   * @test_Strategy: Deploy the whitebox and no transaction mdb provide an
   * XAResource while creating the endpoint and see if the message delivery is
   * non transacted and XAResource is not called.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testMessageDeliveryNonTransacted() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTx Non Transacted Message To MDB1";
    String toCheck2 = "LocalTxMessageXAResource.start";
    String toCheck3 = "LocalTx MDB1 delivery is not transacted";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.equals(toCheck1)) {
        b1 = true;
      }
    }

    String errmsg = "";
    if (!b1) {
      errmsg += toCheck1;
    }

    // We dont want the toCheck2 to be in the Log.
    // Since this is a non transacted mdb.

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (!str.equals(toCheck2)) {
        b2 = true;
      } else {
        b2 = false;
        break;
      }
    }

    if (!b2) {
      errmsg += "   AND   " + toCheck2;
    }

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.equals(toCheck3)) {
        b3 = true;
      }
    }

    if (!b3) {
      errmsg += "   AND   " + toCheck3;
    }

    if (b1 && b2 && b3) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Failed: couldn't find..." + errmsg);
    }

  }

  /*
   * @testName: testMessageDeliveryTransactedUsingXid
   *
   * @assertion_ids: Connector:SPEC:115; Connector:SPEC:285; Connector:SPEC:286;
   * Connector:SPEC:302; Connector:JAVADOC:132; Connector:JAVADOC:344;
   * Connector:JAVADOC:367;
   * 
   * @test_Strategy: Deploy the whitebox and transaction required mdb provide an
   * XAResource while creating the endpoint associate the work object with and
   * xid and see if the message delivery is transacted and uses Xid to commit
   * the transaction.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejb and web containers which implies the validation of
   * assertion Connector:SPEC:285. This satisfies javadoc assertions by virtue
   * of validation of "committed Xid" string.
   *
   */
  public void testMessageDeliveryTransactedUsingXid() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTx MDB2 Transacted Message1";
    String toCheck2 = "LocalTxMessageXAResource2.start";
    String toCheck3 = "LocalTxMessageListener committed Xid";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    // We dont want the toCheck2 to be in the log
    // Since it is using imported transaction and
    // should not use the XAResouce impl we have provided.

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (!str.startsWith(toCheck2)) {
        b2 = true;
      } else {
        b2 = false;
        break;
      }
    }

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
    }

    if (b1 && b2 && b3) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      String errStr = null;

      if (!b1) {
        errStr = toCheck1;
      }
      if (!b2) {
        errStr += "   AND  " + toCheck2;
      }
      if (!b3) {
        errStr += "   AND  " + toCheck3;
      }

      throw new Fault("Failed: couldn't find..." + errStr);
    }

  }

  /*
   * @testName: testActivationSpeccalledOnce
   *
   * @assertion_ids: Connector:SPEC:7; Connector:SPEC:8; Connector:SPEC:285;
   * Connector:SPEC:286; Connector:SPEC:302;
   *
   * @test_Strategy: Check to see if LocalTxActivationSpec.setResourceAdapter
   * was called exactly once. If it has then the test passes.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end-to-end verification.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testActivationSpeccalledOnce() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {

      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "LocalTxActivationSpec setResourceAdapter 1";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("LocalTxActivationSpec.setResourceAdapter not called .");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      throw new Fault(sqle.getMessage(), sqle);
    }
    TestUtil.logMsg("Exception dropping table.");
  }

  /*
   * @testName: testEJBExceptionNotSupported
   *
   * @assertion_ids: Connector:SPEC:107; EJB:SPEC:639; EJB:SPEC:639.3;
   * Connector:SPEC:285; Connector:SPEC:286; Connector:SPEC:302;
   *
   * @test_Strategy: Check to see if SysException thrown by Not Supported
   * transaction demarcation MDB get re-thrown back to the Resource adapter as
   * an EJBException
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end-to-end verification.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testEJBExceptionNotSupported() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {

      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "EJBException thrown by NotSupported";
    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("EJBException not thrown by Not Supported MDB.");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      throw new Fault(sqle.getMessage(), sqle);
    }
    TestUtil.logMsg("Exception dropping table.");
  }

  /*
   * @testName: testEJBExceptionRequired
   *
   * @assertion_ids: Connector:SPEC:107; EJB:SPEC:637; Connector:SPEC:285;
   * Connector:SPEC:286; Connector:SPEC:302;
   *
   * @test_Strategy: Check to see if SysException thrown by Required transaction
   * demarcation MDB get re-thrown back to the Resource adapter as an
   * EJBException.
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end-to-end verification.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testEJBExceptionRequired() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {

      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "EJBException thrown by Required";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("EJBException not thrown by Required MDB.");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      throw new Fault(sqle.getMessage(), sqle);
    }
    TestUtil.logMsg("Exception dropping table.");
  }

  /*
   * @testName: testAppExceptionNotSupported
   *
   * @assertion_ids: Connector:SPEC:107; EJB:SPEC:638; Connector:SPEC:285;
   * Connector:SPEC:286; Connector:SPEC:302;
   *
   * @test_Strategy: Check to see if AppException thrown by Not Supported
   * transaction demarcation MDB get re-thrown back to the Resource adapter as
   * an AppException
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end-to-end verification.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testAppExceptionNotSupported() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {

      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "AppException thrown by NotSupported";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("AppException not thrown by NotSupported MDB.");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      throw new Fault(sqle.getMessage(), sqle);
    }
    TestUtil.logMsg("Exception dropping table.");
  }

  /*
   * @testName: testAppExceptionRequired
   *
   * @assertion_ids: Connector:SPEC:107; EJB:SPEC:636; EJB:SPEC:636.2;
   * Connector:SPEC:285; Connector:SPEC:286; Connector:SPEC:302;
   *
   * @test_Strategy: Check to see if AppException thrown by Required transaction
   * demarcation MDB get re-thrown back to the Resource adapter as an
   * AppException
   * 
   * Check for the validity of the connection which is returned by performing
   * some queries to the TSeis for end-to-end verification.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testAppExceptionRequired() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {

      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "AppException thrown by Required";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("AppException not thrown by Required MDB.");
    }

    // Verify connection object works by doing some end to end tests.
    TestUtil.logMsg("Performing end to end verification...");

    // Insert into table
    try {
      dbutil.insertIntoTable(con);
      TestUtil.logMsg("Values inserted into table!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception inserting into table.");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Drop the table
    try {
      dbutil.dropTable(con);
      TestUtil.logMsg("Table has been dropped!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception dropping table.");
      throw new Fault(sqle.getMessage(), sqle);
    }
    TestUtil.logMsg("Exception dropping table.");
  }

  /*
   * @testName: testSICMsgPrincipal
   *
   * @assertion_ids: Connector:SPEC:232; Connector:SPEC:233; Connector:SPEC:285;
   * Connector:SPEC:286; Connector:SPEC:302;
   *
   * @test_Strategy: We want to set the SIC on a work object that causes a a msg
   * to be sent to an MDB. We expect the msg to have the principal set according
   * to the SIC values. This test verifies that when MDB's are the msg
   * endpoints, then calls to getCallerPrincipal must return the principal
   * corresponding to the established security identity, and isCallerInRole()
   * must return the result of testing the established security identity for
   * role membership.
   * 
   * Also, these tests should only get run in fullEE mode which means they will
   * be run in both ejg and web containers which implies the validation of
   * assertion Connector:SPEC:285.
   *
   */
  public void testSICMsgPrincipal() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil
        .logMsg("connecting to the connector server and the internal log...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {

      throw new Fault(sqle.getMessage(), sqle);
    }

    // we SHOULD see toCheck1 we should NOT see toCheck2
    String toCheck1 = "mdb executed with proper SIC principal";
    String toCheck2 = "mdb not executed with proper SIC principal";
    TestUtil.logTrace("search string 1 = " + toCheck1);
    TestUtil.logTrace("search string 2 = " + toCheck2);

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        // good that we found this
        b1 = true;
      }
      if (str.startsWith(toCheck2)) {
        // bad that we found this
        b2 = true;
      }
    }

    if (b1 && !b2) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault(
          "Setting SIC on msg into MDB did not set principal correctly.");
    }

  }

  /*
   * @testName: testIBAnnoMsgTransactedUsingXid
   *
   * @assertion_ids: Connector:SPEC:115; Connector:JAVADOC:257;
   * 
   * 
   * @test_Strategy: Deploy the whitebox and transaction required mdb provide an
   * XAResource while creating the endpoint associate the work object with and
   * xid and see if the message delivery is transacted and uses Xid to commit
   * the transaction.
   *
   */
  public void testIBAnnoMsgTransactedUsingXid() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds3.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "IBAnnotatedResourceAdapterImpl Required transaction";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Inbound Annotation Transaction failure.");
    }

  }

  /*
   * @testName: testActivationSpecImplRAA
   *
   * @assertion_ids: Connector:SPEC:282; Connector:JAVADOC:257;
   * Connector:JAVADOC:264; Connector:JAVADOC:265; Connector:JAVADOC:114;
   * 
   * @test_Strategy: Verify that an ActivationSpec that implements
   * ResourceAdapterAssociation will be associated with the RA instance. Verify
   * by getting an Activation Spec instance and ensuring that the
   * setResourceAdapter() method (from inherited testActivationSpecImplRAA
   * class) was called with a valid RA passed in.
   *
   */
  public void testActivationSpecImplRAA() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds3.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "IBAnnoActivationSpecChild.setResourceAdatper called";
    String toCheck2 = "IBAnnoActivationSpecChild.getResourceAdapter() not null.";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        // good that we found this
        b1 = true;
      }
      if (str.startsWith(toCheck2)) {
        // good that we found this
        b2 = true;
      }
    }

    if (b1 && b2) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Inbound Annotation Transaction failure.");
    }

  }

  /*
   * @testName: testIBAnnoASConfigProp
   *
   * @assertion_ids: Connector:SPEC:309; Connector:SPEC:315;
   * Connector:JAVADOC:256;
   * 
   * 
   * @test_Strategy: Deploy the whitebox and transaction required mdb thus
   * creating the endpointActivation, then verify that the childActivationSpec
   * inherits the ConfigProperty anno from the parentActivationSpec.
   *
   */
  public void testIBAnnoASConfigProp() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds3.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "IBAnnoActivationSpecChild.propName = IBAnnoConfigPropVal";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      throw new Fault("Inbound Annotation ConfigProperty inheritance failure.");
    }

  }

  /*
   * @testName: testContextSetupCompleted
   *
   * @assertion_ids: Connector:SPEC:223;
   * 
   * 
   * @test_Strategy: This will check that a workContext impl can implement the
   * WorkContextLifecycleListener iface to get fine grained notifications that
   * will result in the contextSetupCompleted() being invoked by the
   * workmanager. This verifies that contextSetupComplete() is called when the
   * WorkContext instance was successfully set as the execution context for the
   * work instance. None of the connector test code explicitly calls
   * TSSecurityContextWithListener.contextSetupComplete() so we assume that if
   * this was invoked, the workmanager must have done it.
   * 
   */
  public void testContextSetupCompleted() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      log = ds1.getStateLog();
      TestUtil.logTrace("Got State LOG!");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "Context setup completed";
    String toCheck2 = "WorkListenerImpl.workAccepted for:notifications test";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (b1 & b2) {
      TestUtil.logMsg("workmanager called contextSetupCompleted() properly");
    } else {
      throw new Fault(
          "failure - workmanager didn't call WorkContextLifecycleListener.contextSetupCompleted() correctly.");
    }

  }

  /* cleanup -- none in this case */
  public void cleanup() throws Fault {
    try {
      TestUtil.logTrace("Inside cleanup");
      if (con != null) {
        con.close();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }
}
