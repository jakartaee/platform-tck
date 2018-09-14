/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)WorkContextClient.java	
 */

package com.sun.ts.tests.connector.localTx.workcontext;

import java.io.*;
import java.util.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.connector.util.DBSupport;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

/*
 * This will test the inflow context related assertions (eg SecurityContext,
 * TransactionContext, etc..)
 *
 */
public class WorkContextClient extends ServiceEETest implements Serializable {

  private TSNamingContext nctx = null;

  private TSConnection con = null;

  private String whitebox_tx = null;

  private String whitebox_mdcomplete = null;

  private String whitebox_anno = null;

  private TSDataSource ds1 = null;

  private TSDataSource ds2 = null;

  private TSDataSource ds3 = null;

  private DBSupport dbutil = null;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    WorkContextClient theTests = new WorkContextClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: whitebox-tx, JNDI name of TS WhiteBox;
   * whitebox-mdcomplete; whitebox-anno_no_md;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties p) throws Fault {

    // Get JNDI lookups for both adapters. The harness will throw if these
    // properties can not be retrieved, so there is no need for error checking
    // here.
    whitebox_tx = p.getProperty("whitebox-tx");
    whitebox_mdcomplete = p.getProperty("whitebox-mdcomplete");
    whitebox_anno = p.getProperty("whitebox-anno_no_md");

    logMsg("Using: " + whitebox_tx);
    logMsg("Using: " + whitebox_mdcomplete);

    // Construct our DBSupport object. This object performs interactions
    // on a table, based on the properties object supplied.
    try {
      dbutil = new DBSupport();
    } catch (Exception e) {
      throw new Fault(
          "Exception constructing DBSupport object: " + e.getMessage(), e);
    }

    // Obtain our TSDataSources for interacting with our TS whitebox
    try {
      nctx = new TSNamingContext();
      ds1 = (TSDataSource) nctx.lookup(whitebox_tx);
      ds2 = (TSDataSource) nctx.lookup(whitebox_mdcomplete);
      ds3 = (TSDataSource) nctx.lookup(whitebox_anno);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during JNDI lookup: " + e.getMessage());
    }
  }

  /*
   * @testName: testIsContextSupported
   *
   * @assertion_ids: Connector:SPEC:208; Connector:SPEC:209; Connector:SPEC:241;
   * Connector:JAVADOC:133;
   *
   * @test_Strategy: This is used to verify that the Transaction, Security, and
   * Hints inflow contexts are standardised via the TransactionContext,
   * SecurityContext, and HintsContext interfaces. (ie that the AppServer
   * supports them). This test validates some key strings were added to the log
   * file by the RA's calls to BootstrapContext.isContextSupported().
   *
   */
  public void testIsContextSupported() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "TransactionContext supported by Server.";
    String toCheck2 = "SecurityContext supported by Server.";
    String toCheck3 = "HintsContext supported by Server";

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
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
    }

    if (b1 && b2 && b3) {
      TestUtil.logMsg("Methods called correctly");
    } else {
      if (!b1) {
        throw new Fault("TransactionContext not supported by server.");
      }
      if (!b2) {
        throw new Fault("SecurityContext not supported by server.");
      }
      if (!b3) {
        throw new Fault("HintsContext not supported by server.");
      }
    }

  }

  /*
   * @testName: testSecurityContextExecSubject
   *
   * @assertion_ids: Connector:SPEC:230; Connector:JAVADOC:360;
   * Connector:JAVADOC:363;
   *
   * @test_Strategy: This verifies that the AppServer called the
   * setupSecurityContext method of the SecurityContext implementation provided
   * by the RA. And specifically, this is checking that the executionSubject arg
   * passed into setupSecurityContext() must be non-null and it must not be
   * readonly.
   * 
   */
  public void testSecurityContextExecSubject() throws Fault {

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
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "setupSecurityContext() called with valid executionSubject";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testSecurityContextExecSubject() passed");
    } else {
      throw new Fault("testSecurityContextExecSubject() failed.");
    }

  }

  /*
   * @testName: testSecurityContextServiceSubject
   *
   * @assertion_ids: Connector:SPEC:231;
   *
   * @test_Strategy: This verifies that the AppServer called the
   * setupSecurityContext method of the SecurityContext implementation provided
   * by the RA. And specifically, this is verifying that if the serviceSubject
   * passed into setupSecurityContext() is either (a) null of (b) non-null and
   * NOT set readonly.
   * 
   */
  public void testSecurityContextServiceSubject() throws Fault {

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
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to link these strings to assertion
    String toCheck1 = "setupSecurityContext() called with valid serviceSubject";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
        break;
      }
    }

    if (b1) {
      TestUtil.logMsg("testSecurityContextServiceSubject() passed");
    } else {
      throw new Fault("testSecurityContextServiceSubject() failed.");
    }

  }

  /*
   * @testName: testSecurityContextCBH
   *
   * @assertion_ids: Connector:SPEC:229;
   *
   * @test_Strategy: This is verifying that the AS calls setupSecurityContext()
   * and that the passed in CBH is not null. It also verifies that the passed in
   * CBH supports the 3 JSR-196 CBH's of: CallerPrincipalCallback,
   * GroupPrincipalCallback, PasswordValidationCallback
   */
  public void testSecurityContextCBH() throws Fault {

    Vector log = null;
    boolean b1 = false;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "setupSecurityContext() called with non-null callbackHandler";
    String toCheck2 = "setupSecurityContext callbackhandler supports required callback types.";

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

    if (b1 && b2) {
      TestUtil.logMsg("testSecurityContextCBH() passed");
    } else {
      throw new Fault("testSecurityContextCBH() failed.");
    }

  }

  /*
   * @testName: testWorkContextErrorCode
   *
   * @assertion_ids: Connector:SPEC:214;
   *
   * @test_Strategy: This is verifying that the AS throws a proper
   * WorkContextErrorCodes for the case of an unsupported exception.
   * 
   */
  public void testWorkContextErrorCode() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds2.setLogFlag(true);
      con = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "MDCompleteWorkManager threw WorkContextErrorCodes.UNSUPPORTED_CONTEXT_TYPE";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("testWorkContextErrorCode() passed");
    } else {
      throw new Fault("testWorkContextErrorCode() failed.");
    }

  }

  /*
   * @testName: testWorkContextErrorCode2
   *
   * @assertion_ids: Connector:SPEC:216;
   *
   * @test_Strategy: This is verifying that the AS throws a proper
   * WorkContextErrorCodes for the case of a duplicate workcontext submission.
   * In such a case, we would expect to see
   * WorkContextErrorCode.DUPLICATE_CONTEXTS
   * 
   */
  public void testWorkContextErrorCode2() throws Fault {

    Vector log = null;
    boolean b1 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds2.setLogFlag(true);
      con = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "MDCompleteWorkManager threw WorkContextErrorCodes.DUPLICATE_CONTEXTS";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    if (b1) {
      TestUtil.logMsg("testWorkContextErrorCode2() passed");
    } else {
      throw new Fault("testWorkContextErrorCode2() failed.");
    }

  }

  /*
   * @testName: testNestedWorkContexts
   *
   * @assertion_ids: Connector:SPEC:210; Connector:JAVADOC:312;
   * Connector:JAVADOC:342; Connector:JAVADOC:341;
   *
   * @test_Strategy: This is verifying that the AS allows and supports the
   * nesting of work instances AND contexts (eg securityInflowContext, etc). To
   * test this, we nest work instances such that one work instance ends up
   * executing another work instance. In both of these cases, each work instance
   * has a *different* securityInflowContext (SIC) and each different SIC must
   * be honored and NO inheriting of context should be done by either work
   * instance. As for the 2nd part of the test, we are to test transaction
   * context assigned to a nested work object.
   * 
   */
  public void testNestedWorkContexts() throws Fault {

    Vector log = null;
    Vector log3 = null;
    boolean b1 = false;
    boolean b2 = false;
    boolean b3 = false;
    TSConnection con3 = null;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds2.setLogFlag(true);
      con = ds2.getConnection();
      log = ds2.getStateLog();

      con3 = ds3.getConnection();
      log3 = ds3.getStateLog();
      TestUtil.logTrace("Got connections.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    } finally {
      try {
        if (con3 != null) {
          con3.close();
        }
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    // Need to verify the follow strings exists within the log...
    // we want to test w/ both security context and transaction context
    String toCheck1 = "Nested Work and Nested Security Context worked.";
    String toCheck2 = "anno based NestedWorkXid1 parent context submitted";
    String toCheck3 = "anno based NestedWorkXid1 child context submitted";

    // part 1 of test: verify nested work contexts (using SIC)
    TestUtil.logTrace(log.toString());
    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        b1 = true;
      }
    }

    // part 2 of test: verify nested work contexts (using TranscationContext)
    TestUtil.logTrace(log3.toString());
    for (int i = 0; i < log3.size(); i++) {
      String str = (String) log3.elementAt(i);
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
      if (str.startsWith(toCheck3)) {
        b3 = true;
      }
    }

    if (b1 && b2 && b3) {
      TestUtil.logMsg("testNestedWorkContexts() passed");
    } else {
      if (!b1) {
        TestUtil.logMsg(
            "testNestedWorkContexts() - could not find string: " + toCheck1);
      }
      if (!b2) {
        TestUtil.logMsg(
            "testNestedWorkContexts() - could not find string: " + toCheck2);
      }
      if (!b3) {
        TestUtil.logMsg(
            "testNestedWorkContexts() - could not find string: " + toCheck3);
      }
      throw new Fault("testNestedWorkContexts() failed.");
    }

  }

  /*
   * @testName: testNestedWorkContexts2
   *
   * @assertion_ids: Connector:SPEC:305;
   *
   * @test_Strategy: This is verifying that the AS allows and supports the
   * nesting of work instances AND contexts (eg securityInflowContext, etc). To
   * test this, we nest work instances such that one work instance ends up
   * executing another work instance with NO SIC. The parent work inst should
   * get done but the child should have no work context and should NOT inherit
   * the work inst context from the parent. Thus the parent authenticates but
   * the child does not.
   * 
   * 
   */
  public void testNestedWorkContexts2() throws Fault {

    Vector log = null;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds2.setLogFlag(true);
      con = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck2 = "TSNestedSecurityContext expected PVC failure and got it.";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (b2) {
      TestUtil.logMsg("testNestedWorkContexts2() passed");
    } else {
      TestUtil
          .logMsg("testNestedWorkContexts2() failed - could not find string: "
              + toCheck2);
      throw new Fault("testNestedWorkContexts2() failed.");
    }

  }

  /*
   * @testName: testNestedWorkContexts3
   *
   * @assertion_ids: Connector:SPEC:210;
   *
   * @test_Strategy: This is verifying that the AS allows and supports the
   * nesting of work instances AND contexts (eg securityInflowContext, etc). To
   * test this, we nest work instances such that one work instance with a valid
   * SIC ends up executing another work instance with an invalid SIC. We expect
   * the child work instance (with invalid SIC) should NOT inherit the parents
   * SIC and thus shoudl not show a successful Password Validation Callback
   * (PVC) check.
   * 
   * 
   */
  public void testNestedWorkContexts3() throws Fault {

    Vector log = null;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("Performing callback verification...");
    try {
      TestUtil.logMsg("Got RA log.");
      ds2.setLogFlag(true);
      con = ds2.getConnection();
      log = ds2.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck2 = "TSNestedSecurityContext expected PVC failure and got it.";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck2)) {
        b2 = true;
      }
    }

    if (b2) {
      TestUtil.logMsg("testNestedWorkContexts3() passed");
    } else {
      TestUtil
          .logMsg("testNestedWorkContexts3() failed - could not find string: "
              + toCheck2);
      throw new Fault("testNestedWorkContexts3() failed.");
    }

  }

  /*
   * @testName: testWorkContextProvider
   *
   * @assertion_ids: Connector:SPEC:221;
   *
   * @test_Strategy: This is verifying that a resource adapter must not submit a
   * Work that implements WorkContextProvider along with a valid
   * ExecutionContext to a Connector WorkManager
   * 
   */
  public void testWorkContextProvider() throws Fault {

    Vector log = null;
    boolean b2 = false;

    // Obtain connection, perform API verification
    TestUtil.logMsg("testing testWorkContextProvider()");
    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Need to verify the follow strings exists within the log...
    String toCheck = "SUCCESS:  WorkContextProvider causes expected WorkRejectedException";

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck)) {
        b2 = true;
      }
    }

    if (b2) {
      TestUtil.logMsg("testWorkContextProvider() passed");
    } else {
      TestUtil
          .logMsg("testWorkContextProvider() failed - could not find string: "
              + toCheck);
      throw new Fault("testWorkContextProvider() failed.");
    }

  }

  /*
   * @testName: testWorkContextNotifications
   *
   * @assertion_ids: Connector:SPEC:224; Connector:SPEC:225; Connector:SPEC:262;
   *
   * @test_Strategy: This is verifying that the notifications related to Work
   * accepted and work started events are called prior to the calling of the
   * WorkContext setup related notifications. Additionally,
   * contextSetupComplete() must get called/notified befor workComplete().
   *
   * To test this, we get log entries that start with "notifications test:" and
   * parse those log entries to make sure the count of "workAccepted" and
   * "workStarted" came befor the count for the WorkContext setup related
   * notifications (e.g. "contextSetupComplete"). If contextSetupComplete() has
   * a count thats < workAccepted() and/or workStarted() then fail. else if
   * contextSetupComplete() has a count thats > then pass. Then, as part of
   * assertion 262, we want to verify that contextSetupComplete() is
   * called/notified befor workComplete() notifications occur.
   * 
   */
  public void testWorkContextNotifications() throws Fault {

    Vector log = null;
    int iWorkAcceptedCount = -1;
    int iWorkStartedCount = -1;
    int iWorkCompletedCount = -1;
    int iContextSetupCompleteCount = -1;

    // Need to verify the follow strings exists within the log...
    String toCheck1 = "notifications test: workAccepted(): count=";
    String toCheck2 = "notifications test: workStarted(): count=";
    String toCheck3 = "notifications test: contextSetupComplete(): count=";
    String toCheck4 = "notifications test: workCompleted(): count=";

    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      // get the actual count value for each of the three
      // main notifications we are checking the call order of
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        iWorkAcceptedCount = Integer.parseInt(str.substring(toCheck1.length()));
      }
      if (str.startsWith(toCheck2)) {
        iWorkStartedCount = Integer.parseInt(str.substring(toCheck2.length()));
      }
      if (str.startsWith(toCheck3)) {
        iContextSetupCompleteCount = Integer
            .parseInt(str.substring(toCheck3.length()));
      }
      if (str.startsWith(toCheck4)) {
        iWorkCompletedCount = Integer
            .parseInt(str.substring(toCheck4.length()));
      }
    }

    // some debugging output
    String msg = "testWorkContextNotifications() failed with following values: ";
    msg += "iWorkAcceptedCount = " + iWorkAcceptedCount + "    ";
    msg += "iWorkStartedCount = " + iWorkStartedCount + "    ";
    msg += "iWorkCompletedCount = " + iWorkCompletedCount + "    ";
    msg += "iContextSetupCompleteCount = " + iContextSetupCompleteCount;
    TestUtil.logMsg(msg);

    // first, verify we got all our notifications
    if ((iWorkAcceptedCount == -1) || (iWorkStartedCount == -1)
        || (iWorkCompletedCount == -1) || (iContextSetupCompleteCount == -1)) {
      // problem - some notifications never got sent!
      throw new Fault(
          "FAILURE:  testWorkContextNotifications() detected that some notifications never got recorded");
    }

    // next, verify contextSetupCompleted called befor workCompleted
    if (iWorkCompletedCount < iContextSetupCompleteCount) {
      // workCompleted must be called *after* contextSetupCompleted else fail
      throw new Fault(
          "FAILURE:  contextSetupCompleted must be called befor workCompleted - but was not!");
    } else {
      // good so far as contextSetupCompleted called befor workCompleted
      TestUtil
          .logMsg("contextSetupCompleted correctly called befor workCompleted");
    }

    // finally, verify contextSetupComplete called after WorkAccepted &
    // WorkStarted
    if ((iWorkAcceptedCount < iContextSetupCompleteCount)
        && (iWorkStartedCount < iContextSetupCompleteCount)) {
      TestUtil.logMsg("testWorkContextNotifications() passed");
    } else {
      // our contextSetupComplete must've been called befor one of the
      // other two (eg workAccepted() and workStarted()) so this is failure
      throw new Fault("testWorkContextNotifications() failed.");
    }

  }

  /*
   * @testName: testHICNotifications
   *
   * @assertion_ids: Connector:SPEC:261;
   *
   * @test_Strategy: This is verifying that the notifications related to Work
   * context that have unknown hint name-value pairs submitted. If they don't
   * ignore and throw a contextSetupFailed, thent his test fails.
   *
   * To test this, we get log entries that start with "notifications test:" and
   * parse those log entries to make sure no contextSetupFailed notifications
   * occurred.
   * 
   */
  public void testHICNotifications() throws Fault {

    Vector log = null;
    int iContextSetupFailedCount = -1;
    String toCheck1 = "notifications test: contextSetupFailed(): count=";

    try {
      TestUtil.logMsg("Got RA log.");
      ds1.setLogFlag(true);
      con = ds1.getConnection();
      log = ds1.getStateLog();
      TestUtil.logTrace("Got connection.");
    } catch (Exception sqle) {
      TestUtil.logMsg("Exception caught on creating connection:");
      throw new Fault(sqle.getMessage(), sqle);
    }

    // Turn tracing on if you want to see the log contents
    TestUtil.logTrace(log.toString());

    for (int i = 0; i < log.size(); i++) {
      // get the actual count value for each of the three
      // main notifications we are checking the call order of
      String str = (String) log.elementAt(i);
      if (str.startsWith(toCheck1)) {
        iContextSetupFailedCount = Integer
            .parseInt(str.substring(toCheck1.length()));
      }
    }

    // some debugging output
    String msg = "testWorkContextNotifications() failed with following values: ";
    msg += "iContextSetupFailedCount = " + iContextSetupFailedCount + "    ";
    TestUtil.logMsg(msg);

    // verify we got NO contextSetupFailed notifications
    // which indicates that the hints we set (which used a name-value that was
    // unknown
    // to the server) were ignored by the appserver
    if (iContextSetupFailedCount != -1) {
      // problem - we should NOT have gotten any contextSetupFailed
      // notifications
      throw new Fault(
          "FAILURE:  testWorkContextNotifications() detected contextSetupFailed notification.");
    }

  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("Cleanup");
    ds1.clearLog();
    try {
      con.close();
    } catch (Exception sqle) {
      TestUtil.logErr("Exception on cleanup: " + sqle.getMessage(), sqle);
    }
  }

}
