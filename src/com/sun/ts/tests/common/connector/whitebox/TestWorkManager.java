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
 * $Id$
 */

package com.sun.ts.tests.common.connector.whitebox;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.HintsContext;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkCompletedException;
import javax.resource.spi.work.WorkRejectedException;
import com.sun.ts.tests.common.connector.util.*;
import javax.transaction.xa.Xid;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.TransactionContext;
import javax.resource.spi.work.SecurityContext;

public class TestWorkManager {

  private WorkManager wmgr;

  private static Xid myxid;

  private static Xid mynestxid;

  private XATerminator xa;

  private String sicUser = "";

  private String sicPwd = "";

  private String eisUser = "";

  private LocalTxMessageListener ml;

  private BootstrapContext bsc = null;

  // by default assume Case 1 security where identity is flown-in
  // from RA and exists within AppServer security domain
  // the alternative is case 2 which requires mapping EIS identity to
  // an identity which exists within the AppServer security domain.
  private boolean useSecurityMapping = false;

  public TestWorkManager(BootstrapContext val) {
    this.bsc = val;
    this.wmgr = bsc.getWorkManager();
    this.xa = bsc.getXATerminator();

    this.sicUser = System.getProperty("j2eelogin.name");
    this.sicPwd = System.getProperty("j2eelogin.password");
    this.eisUser = System.getProperty("eislogin.name");

    verifyConfigSettings();
  }

  public void runTests() {

    doWork();
    startWork();
    rogueWork();
    distributableWork();
    scheduleWorkListener();
    scheduleWork();
    submitXidWork();
    submitNestedXidWork();

    submitSecurityContextWork();
    submitSICWork();

    testWorkContextProvider();
    testWorkContextLifecycleListener();
    doWorkAndAssoc();

    doAPITests();
  }

  /*
   * There are 2 choices related to establishing Caller identity which need to
   * considered when using TSSecurityContextWithListener/TSSecurityContext:
   * choice 1: RA flows in an identity to the AppServers security domain. (eg no
   * mapping/transalation done the identity is used as-is when passed into the
   * App Servers security domain) choice 2: RA flows in an identity that belongs
   * to the EIS domain only and a mapping of that identity to an AppServer
   * identity then needs to be done to map the EIS id to an id that exists
   * within the AppServers security domain.
   */
  public void submitSecurityContextWork() {

    if (!useSecurityMapping) {
      // case 1: no mappings, identities must exist in security domain.
      // PVC is for case 1 only, so do PVC tests in here.
      cbTestCPCandNullPrin();
      cbTestAllCallbacksAndPrin();
      cbTestAllCallbacksNullPrin();
      cbTestCPCandPVC();
      cbTestCPCandGPC();
      cbTestCPCandPrin();
    } else {
      // case-2: only call this for security mappings tests.
      cbTestGPCandCPCFail();
      cbTestEISCPCandPrin();
    }
  }

  /*
   * Test handling of a CPC - where no principals will be added to the CPC
   * subject and the CPC has a non-null principal. This is testing the CPC being
   * handled in a case-2 scenario with security-mapping on such that the EIS
   * creds need to be used and mapped.
   * 
   */
  public void cbTestEISCPCandPrin() {
    String str = "Enterred cbEISTestCPCandPrin() and Testing that A CPC ";
    str += "that has nothing added to subject and has a non-null principal is handled";
    debug(str);

    debug("cbTestEISCPCandPrin():  useSecurityMapping SHOULD be true");
    debug(
        "cbTestEISCPCandPrin():  useSecurityMapping IS " + useSecurityMapping);

    TSSecurityContext tsic = new TSSecurityContextWithListener(sicUser, sicPwd,
        eisUser, useSecurityMapping);
    tsic.setCallbacks(true, false, false); // CPC=true, GPC=false, PVC=false
    tsic.setAddPrinToExecSubject(false);
    tsic.setLogOutString("cbEISTestCPCandPrin:  callbacks handled.");

    SecurityContext sic = (SecurityContext) tsic;

    testSecurityInflow(sic, true);

    debug("done testing cbEISTestCPCandPrin()");
  }

  /*
   * Test handling of a CPC - where no principals will be added to the CPC
   * subject and the CPC has a non-null principal. (This tests it using Case-1
   * security creds)
   */
  public void cbTestCPCandPrin() {
    String str = "Enterred cbTestCPCandPrin() and Testing that A CPC ";
    str += "that has nothing added to subject and has a null principal is handled";
    debug(str);

    debug("cbTestCPCandPrin():  useSecurityMapping SHOULD be false");
    debug("cbTestCPCandPrin():  useSecurityMapping IS " + useSecurityMapping);

    TSSecurityContext tsic = new TSSecurityContextWithListener(sicUser, sicPwd,
        null, useSecurityMapping);
    tsic.setCallbacks(true, false, false); // CPC=true, GPC=false, PVC=false
    tsic.setAddPrinToExecSubject(false);
    tsic.setLogOutString("cbTestCPCandPrin:  callbacks handled.");

    SecurityContext sic = (SecurityContext) tsic;

    testSecurityInflow(sic, true);

    debug("done testing cbTestCPCandPrin()");
  }

  /*
   * Test handling of a CPC - where no principals will be added to the CPC
   * execSubject and the CPC has a null principal. Also, this CPC follows a GPC
   * and PVC.
   */
  public void cbTestAllCallbacksNullPrin() {

    String str = "Enterred cbTestAllCallbacksNullPrin() and Testing a GPC, PVC, and CPC ";
    str += "that have nothing added to subject and a null principal are handled";
    debug(str);

    debug("cbTestAllCallbacksNullPrin():  useSecurityMapping SHOULD be false");
    debug("cbTestAllCallbacksNullPrin():  useSecurityMapping IS "
        + useSecurityMapping);

    TSSecurityContext tsic = new TSSecurityContextWithListener(null, null, null,
        useSecurityMapping);
    tsic.setCallbacks(true, true, true); // CPC=true, GPC=true, PVC=true
    tsic.setAddPrinToExecSubject(false);
    tsic.setLogOutString("cbTestAllCallbacksNullPrin:  callbacks handled.");

    SecurityContext sic = (SecurityContext) tsic;

    testSecurityInflow(sic, true);

    debug("done testing cbTestAllCallbacksNullPrin()");
  }

  /*
   * Test handling of a CPC - where no principals will be added to the CPC
   * execSubject and the CPC has a non-null principal. Also, the CPC is done
   * after a GPC and PVC.
   * 
   */
  public void cbTestAllCallbacksAndPrin() {

    String str = "Enterred cbTestAllCallbacksAndPrin() and Testing a GPC, PVC, and CPC ";
    str += "that have nothing added to subject and a non-null principal are handled";
    debug(str);

    debug("cbTestAllCallbacksAndPrin():  useSecurityMapping SHOULD be false");
    debug("cbTestAllCallbacksAndPrin():  useSecurityMapping IS "
        + useSecurityMapping);

    TSSecurityContext tsic = new TSSecurityContextWithListener(sicUser, sicPwd,
        eisUser, useSecurityMapping);
    tsic.setCallbacks(true, true, true); // CPC=true, GPC=true, PVC=true
    tsic.setAddPrinToExecSubject(false);
    tsic.setLogOutString("cbTestAllCallbacksAndPrin:  callbacks handled.");

    debug(
        "TestWorkManager.cbTestAllCallbacksAndPrin - done calling tsic.setLogOutString()");

    SecurityContext sic = (SecurityContext) tsic;

    testSecurityInflow(sic, true);

    debug("done testing cbTestAllCallbacksAndPrin()");
  }

  /*
   * Test handling of a CPC - where no principals will be added to the CPC
   * subject and the CPC has a null principal.
   * 
   */
  public void cbTestCPCandNullPrin() {
    String str = "Enterred cbTestCPCandNullPrin() and Testing that A CPC ";
    str += "that has nothing added to subject and has a null principal is handled";
    debug(str);

    debug("cbTestCPCandNullPrin():  useSecurityMapping SHOULD be false");
    debug(
        "cbTestCPCandNullPrin():  useSecurityMapping IS " + useSecurityMapping);

    TSSecurityContext tsic = new TSSecurityContextWithListener(null, null, null,
        useSecurityMapping);
    tsic.setCallbacks(true, false, false); // CPC=true, GPC=false, PVC=false
    tsic.setAddPrinToExecSubject(false);
    tsic.setLogOutString(
        "cbTestCPCandNullPrin:  Case-1 security callbacks handled.");
    SecurityContext sic = (SecurityContext) tsic;

    testSecurityInflow(sic, true);

    debug("done testing cbTestCPCandNullPrin()");
  }

  /*
   * Test case where GPC is followed by CPC - no principals are added to the
   * execSubject, the CPC (with a non-null principal), and one or more GPC's
   * (each non-null group) are handled.
   */
  public void cbTestCPCandGPC() {
    String str = "Enterred cbTestCPCandGPC() and Testing that ";
    str += "GPC (w non-null group) followed by a CPC that has non-null princiapl) is handled";
    debug(str);

    debug("cbTestCPCandPrin():  useSecurityMapping SHOULD be false");
    debug("cbTestCPCandPrin():  useSecurityMapping IS " + useSecurityMapping);

    TSSecurityContext tsic = new TSSecurityContextWithListener(sicUser, sicPwd,
        eisUser, useSecurityMapping);
    tsic.setCallbacks(true, true, false); // CPC=true, GPC=true, PVC=false
    tsic.setAddPrinToExecSubject(false);
    tsic.setLogOutString("cbTestCPCandGPC:  callbacks handled.");

    SecurityContext sic = (SecurityContext) tsic;

    testSecurityInflow(sic, true);

    debug("done testing cbTestCPCandGPC()");
  }

  /*
   * Test case where PVC is followed by CPC - no principals are added to the
   * execSubject.
   */
  public void cbTestCPCandPVC() {
    String str = "Enterred cbTestCPCandPVC() and Testing that ";
    str += "PVC followed by a CPC that has non-null principal) is handled";
    debug(str);

    debug("cbTestCPCandPVC():  useSecurityMapping SHOULD be false");
    debug("cbTestCPCandPVC():  useSecurityMapping IS " + useSecurityMapping);

    TSSecurityContext tsic = new TSSecurityContextWithListener(sicUser, sicPwd,
        eisUser, useSecurityMapping);
    tsic.setCallbacks(true, false, true); // CPC=true, GPC=false, PVC=true
    tsic.setLogOutString("cbTestCPCandPVC:  callbacks handled.");
    tsic.setAddPrinToExecSubject(false);
    SecurityContext sic = (SecurityContext) tsic;

    testSecurityInflow(sic, true);

    debug("done testing cbTestCPCandPVC()");
  }

  /*
   * Test that a single (presumably non-group) principal was added to the
   * execSubject and the CPC is NOT handled. This uses invalid creds that are
   * used in a case-2 scenario (with security mappings). The assumption is that
   * this is only called for case-2 security.
   */
  public void cbTestGPCandCPCFail() {
    String str = "Enterred cbTestGPCandCPCFail() and Testing a GPC, and CPC fail";
    str += "where principal is added to execSubject and CPC is not handled";
    debug(str);

    debug("cbTestGPCandCPCFail():  useSecurityMapping SHOULD be true");
    debug(
        "cbTestGPCandCPCFail():  useSecurityMapping IS " + useSecurityMapping);

    TSSecurityContext tsic = new TSSecurityContextWithListener("fakeusr",
        "fakepwd", "fakepwd", true);
    tsic.setCallbacks(true, true, false); // CPC=true, GPC=true, PVC=false
    tsic.setAddPrinToExecSubject(true);
    tsic.setExpectFailure(true);
    tsic.setLogOutString(
        "cbTestGPCandCPCFail:  callbacks are NOT handled - as expected.");

    SecurityContext sic = (SecurityContext) tsic;

    testSecurityInflow(sic, true);

    debug("done testing cbTestGPCandCPCFail()");
  }

  /*
   * This will need to submit a work object that has SIC set on it and then we
   * will need to verify that the work object was recieved by the AppServer (via
   * MDB) and we will want to verify the values that got set within the SIC
   * setting to confirm the subject info is correct. (note that invoking
   * MDB.isCallerInRole() is one thing we can use to assist with credential
   * validation.
   * 
   */
  public void submitSICWork() {

    try {
      ConnectorStatus.getConnectorStatus().logState("enterred submitSICWork()");
      debug("enterred submitSICWork()");

      ContextWork w1 = new ContextWork(wmgr);

      debug("creating SIC with user=" + sicUser + "  pwd=" + sicPwd
          + "    eisUser = " + eisUser);

      SecurityContext sic = new TSSecurityContextWithListener(sicUser, sicPwd,
          eisUser, this.useSecurityMapping);

      w1.addWorkContext(sic);

      wmgr.doWork(w1, WorkManager.INDEFINITE, null, null);

      ConnectorStatus.getConnectorStatus()
          .logState("submitted work with SIC Listener");

    } catch (WorkCompletedException e) {
      // everything seemed to work okay
      debug("WorkCompleted calling submitSICWork()");

    } catch (Exception e) {
      debug("got exception in submitSICWork() with user = " + sicUser
          + " pwd = " + sicPwd + "  principal=" + eisUser);
      debug(e.toString());
      Debug.printDebugStack(e);
    }

  }

  /*
   * This method is testing the order in which notifications occur. (see JCA
   * spec assertions 224, 225, 262) This method makes use of a counter and some
   * dedicated classes that will use the counter to record the calling order
   * order of notifications for workAccepted, workStarted,
   * contextSetupCompleted, and workAccepted.
   *
   */
  public void testWorkContextLifecycleListener() {

    try {
      debug("enterred testWorkContextLifecycleListener()");

      ContextWork workimpl = new ContextWork(wmgr);

      // we want to ensure that WorkListenerImpl notifications
      // occur BEFOR the workcontext setup related notifications
      // so create new counter obj, and ensure it is set to zero
      Counter.resetCount(); // set counter to 0
      WorkListenerImpl2 wl = new WorkListenerImpl2();

      debug("Submitting Work Object testWorkContextLifecycleListener().");

      // as part of assert 261, set hint context - which should be unknown
      // to the appserver and thus ignored...if its not ignored and any
      // contextSetupFailed notification occurs - assert 261 will be flagged
      // and detected as failing later on
      HintsContext hic = new HintsContext();
      hic.setName("someReallyLongAndUncommonName");
      hic.setHint(HintsContext.NAME_HINT, "someReallyLongAndUncommonHintValue");
      workimpl.addWorkContext(hic);

      SecurityContext sic = new TSSICWithListener(eisUser, eisUser, sicUser,
          true);
      workimpl.addWorkContext(sic);
      wmgr.doWork(workimpl, WorkManager.INDEFINITE, null, wl);
      debug("done submitting work obj in testWorkContextLifecycleListener()");

    } catch (WorkRejectedException ex) {
      // we should not get here
      debug(
          "Problem:  testWorkContextLifecycleListener WorkException thrown is "
              + ex.getMessage());
      ex.printStackTrace();

    } catch (WorkException ex) {
      // we should not get here (unless WorkCompleted Exception)
      debug("testWorkContextLifecycleListener WorkException thrown is "
          + ex.getMessage());
      ex.printStackTrace();

    } catch (Exception ex) {
      // we should not get here
      debug("Problem:  testWorkContextLifecycleListener Exception thrown is "
          + ex.getMessage());
      ex.printStackTrace();
    }

  }

  public void testWorkContextProvider() {

    try {
      debug("enterred testWorkContextProvider()");

      // this helps test assertion 221
      ContextWork workimpl = new ContextWork(wmgr);
      ExecutionContext ec = new ExecutionContext();
      WorkListenerImpl wl = new WorkListenerImpl();

      debug(
          "Submitting Work Object - which should genetate WorkRejectedException.");
      wmgr.doWork(workimpl, 5000, ec, wl);

    } catch (WorkRejectedException we) {
      // we expect to get here!!!
      String str = "SUCCESS:  WorkContextProvider causes expected WorkRejectedException";
      debug(str);
      ConnectorStatus.getConnectorStatus().logState(str);

    } catch (WorkException we) {
      // we should not get here
      debug("FAILURE:  testWorkContextProvider() WorkException thrown is "
          + we.getMessage());

    } catch (Exception ex) {
      // we should not get here
      debug("FAILURE:  testWorkContextProvider() Exception thrown is "
          + ex.getMessage());
    }

    debug("leaving testWorkContextProvider()");
  }

  /*
   * This is used to help verify assertion Connector:SPEC:209
   * 
   * ARGS:
   *
   * SecurityContext: this is the sic that is passed in. It must be constructed
   * befor being passed in. writeToDB: this is used to indicate if we want to
   * commit/write our transaction to the db or roll it back.
   * 
   * There are 2 choices related to establishing Caller identity: choice 1: RA
   * flows in an identify to the AppServers security domain. (eg no transalation
   * is done as the identity is used as is) choice 2: RA flows in an identity
   * that belongs to the EIS domain only and a mapping of that identity to an
   * AppServer identity then needs to be done to map the EIS id to an id that
   * exists within the AppServers security domain.
   *
   */
  public void testSecurityInflow(SecurityContext sic, boolean writeToDB) {

    try {
      ConnectorStatus.getConnectorStatus()
          .logState("enterred testSecurityInflow()");
      debug("enterred testSecurityInflow()");

      ExecutionContext ec = startTx();

      ContextWork w1 = new ContextWork(wmgr);

      // adding the worklistener along with securityContextWithListener allows
      // us to test assertion 223
      WorkListenerImpl wl = new WorkListenerImpl();
      wl.setUidStr("notifications test"); // assists w/ assertion
                                          // Connector:SPEC:223

      TransactionContext tic = new TransactionContext();
      tic.setXid(ec.getXid());
      w1.addWorkContext(tic);

      w1.addWorkContext(sic);

      wmgr.doWork(w1, WorkManager.INDEFINITE, null, wl);

      if (writeToDB) {
        // commit write to DB - with TIC and SIC with Listener. For this, a
        // Translation is Required
        xa.commit(ec.getXid(), true);
      } else {
        // rollback a write to DB - with TIC and SIC
        xa.rollback(tic.getXid());
      }

    } catch (WorkCompletedException e) {
      // everything seemed to work okay
      debug("WorkCompleted calling testSecurityInflow()");

    } catch (Exception e) {
      debug(
          "got exception in testSecurityInflow() with user = " + sic.getName());
      debug(e.toString());
      Debug.printDebugStack(e);
    }

  }

  private ExecutionContext startTx() {
    ExecutionContext ec = new ExecutionContext();
    try {
      Xid xid = new XidImpl();
      ec.setXid(xid);
      ec.setTransactionTimeout(5 * 1000); // 5 seconds
    } catch (Exception ex) {
      Debug.printDebugStack(ex);
    }
    return ec;
  }

  public void scheduleWork() {
    try {
      ScheduleWork sw = new ScheduleWork();
      wmgr.scheduleWork(sw);
      debug("Schedule work called");
      ConnectorStatus.getConnectorStatus().logState("Schedule Work Called");
    } catch (WorkException we) {
      debug("TestWorkManager Exception thrown is " + we.getMessage());
    }
  }

  public void scheduleWorkListener() {
    try {
      ScheduleWork sw = new ScheduleWork();
      ExecutionContext ec = new ExecutionContext();
      wmgr.scheduleWork(sw, 5000, ec, null);
      debug("Schedule work listener called");
      ConnectorStatus.getConnectorStatus()
          .logState("Schedule Work Listener Called");
    } catch (WorkException we) {
      debug("TestWorkManager Exception thrown is " + we.getMessage());
    }
  }

  /*
   * this will try to test API assertions which basically means we are testing
   * the API.
   */
  public void doAPITests() {
    APIAssertionTest apiTest = new APIAssertionTest();
    apiTest.runTests();
  }

  public void doWork() {
    try {
      WorkImpl workimpl = new WorkImpl(wmgr);
      ExecutionContext ec = new ExecutionContext();
      debug("doWork():  Creating WorkListener");
      WorkListenerImpl wl = new WorkListenerImpl();
      ConnectorStatus.getConnectorStatus().logState("Work Object Submitted");
      wmgr.doWork(workimpl, WorkManager.INDEFINITE, ec, wl);
    } catch (WorkException we) {
      debug("TestWorkManager WorkException thrown is " + we.getMessage());
    }
  }

  // this is used to test Connector:spec:245
  public void doWorkAndAssoc() {
    try {
      // using WorkAndAssocImpl allows us to test Connector:spec:245
      WorkAndAssocImpl workimpl = new WorkAndAssocImpl(wmgr);
      ExecutionContext ec = new ExecutionContext();
      debug("doWorkAndAssoc():  Creating WorkListener");
      WorkListenerImpl wl = new WorkListenerImpl();
      ConnectorStatus.getConnectorStatus().logState("Work Object Submitted");
      wmgr.doWork(workimpl, wmgr.INDEFINITE, ec, wl);

    } catch (WorkException we) {
      debug("TestWorkManager WorkException thrown is " + we.getMessage());
      we.printStackTrace();
    }
  }

  public void startWork() {
    try {
      WorkImpl workimpl = new WorkImpl(wmgr);
      ExecutionContext ec = new ExecutionContext();
      debug("startWork: Creating WorkListener");
      WorkListenerImpl wl = new WorkListenerImpl();
      long value = wmgr.startWork(workimpl, 1000, ec, wl);

      debug("startWork: WorkManager value = " + value);

      ConnectorStatus.getConnectorStatus()
          .logState("WorkManager value returned " + value);
      debug("WorkManager value returned " + value);

    } catch (WorkException we) {
      debug(
          "TestWorkManager.startWork() Exception thrown is " + we.getMessage());
    }
  }

  public void rogueWork() {
    try {
      RogueWorkImpl rwi = new RogueWorkImpl();
      wmgr.doWork(rwi);
    } catch (WorkCompletedException wx) {
      ConnectorStatus.getConnectorStatus()
          .logState("Rogue work throws WorkCompletedException");
    } catch (WorkException we) {
      debug("TestWorkManager Exception thrown is " + we.getMessage());
    }
  }

  public void distributableWork() {
    try {
      DistributedWorkImpl dwi = new DistributedWorkImpl(wmgr);
      // wmgr.doWork(dwi);
      ExecutionContext ec = new ExecutionContext();
      debug("distributableWork(): Creating WorkListener");
      WorkListenerImpl wl = new WorkListenerImpl();
      long value = wmgr.startWork(dwi, 1000, ec, wl);
      ConnectorStatus.getConnectorStatus()
          .logState("DistributedWork Object Submitted");

      if (value >= -1) {
        ConnectorStatus.getConnectorStatus()
            .logState("WorkManagers DistributedWork value returned " + value);
        debug("WorkManagers DistributedWork value returned " + value);
      }
    } catch (WorkCompletedException wx) {
      ConnectorStatus.getConnectorStatus()
          .logState("Rogue work throws WorkCompletedException");
    } catch (WorkException we) {
      debug("TestWorkManager Exception thrown is " + we.getMessage());
    }
  }

  public void setXid(Xid xid) {
    this.myxid = xid;
  }

  public Xid getXid() {
    return this.myxid;
  }

  public void setNestXid(Xid xid) {
    this.mynestxid = xid;
  }

  public Xid getNestXid() {
    return this.mynestxid;
  }

  /*
   * JCA 1.6 spec 9section 16.3 and 16.4 state there are two types of security a
   * RA can use: "Case 1" and "Case 2". Case 1 is when the RA passes in an
   * identity and that identity is assumed to exist within the app server
   * security domain. "Case 2" takes and EIS identity, which will not exist in
   * AppServer domain, so this case requires a mapping/translation from and
   * EIS-Identity to an i AppServer-Identity.
   *
   * set useSecurityMapping to true to specify we want Case 2 security mapping
   * use. set useSecurityMapping to false to indicate Case 1 security.
   */
  public void setUseSecurityMapping(boolean val) {
    this.useSecurityMapping = val;
  }

  public boolean getUseSecurityMapping() {
    return this.useSecurityMapping;
  }

  public void submitXidWork() {
    try {
      XidImpl myid = new XidImpl();

      // setting up the xid so that it can be retrieved by the TestBootstrap to
      // commit the xid work object.
      setXid(myid);

      WorkXid workid = new WorkXid();
      ExecutionContext ec = new ExecutionContext();
      ec.setXid(myid);
      debug("TestWorkManager.submitXidWork XID IS " + myid.getFormatId());
      wmgr.doWork(workid, wmgr.INDEFINITE, ec, null);
      debug("WorkXid Submitted");
      ConnectorStatus.getConnectorStatus().logState("WorkXid Submitted");
    } catch (WorkException we) {
      Debug.printDebugStack(we);
    }
  }

  public void submitNestedXidWork() {
    try {
      XidImpl myid = new XidImpl();
      WorkXid1 workid = new WorkXid1(wmgr, myid);
      // setting up the xid so it can be commited later.
      setNestXid(myid);

      debug("TestWorkManager.submitNestedXidWork XID IS " + myid.getFormatId());

      ExecutionContext ec = new ExecutionContext();
      ec.setXid(myid);
      wmgr.doWork(workid, wmgr.INDEFINITE, ec, null);
      debug("WorkXid1 submitted");

    } catch (WorkException we) {
      Debug.printDebugStack(we);
    }
  }

  /*
   * verify we can find config properties that should be set as part of our
   * configuration process. (See ts.jte prop for values.)
   */
  private void verifyConfigSettings() {
    String err = "WARNING - TestWorkManager could not find required system property: ";

    if (sicUser == null) {
      err += "j2eelogin.name";
      debug(err);
      sicUser = "j2ee"; // try to set to a default
    }

    if (sicPwd == null) {
      err += "j2eelogin.password";
      debug(err);
      sicUser = "j2ee"; // try to set to a default
    }

    if (eisUser == null) {
      err += "j2eelogin.password";
      debug(err);
      sicUser = "cts1"; // try to set to a default
    }
  }

  private void debug(String str) {
    Debug.trace(str);
  }
}
