/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.mdcomplete;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.WorkCompletedException;
import javax.resource.spi.work.WorkContextErrorCodes;
import javax.resource.spi.work.TransactionContext;
import javax.resource.spi.work.SecurityContext;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.WorkImpl;
import javax.transaction.xa.Xid;
import com.sun.ts.tests.common.connector.whitebox.*;

public class MDCompleteWorkManager {
  private BootstrapContext bsc = null;

  private WorkManager wmgr;

  private String sicUser = "";

  private String sicPwd = "";

  private String eisUser = "";

  private String eisPwd = "";

  public MDCompleteWorkManager(BootstrapContext val) {
    debug("enterred constructor");
    this.bsc = val;
    this.wmgr = bsc.getWorkManager();

    this.sicUser = System.getProperty("j2eelogin.name");
    this.sicPwd = System.getProperty("j2eelogin.password");
    this.eisUser = System.getProperty("eislogin.name");
    this.eisPwd = System.getProperty("eislogin.password");
    debug("leaving constructor");
  }

  public void runTests() {
    debug("enterred runTests");
    doWork();
    testNestedContext();
    testNestedContext2();
    testNestedContext3();
    submitTICWork();
    debug("leaving runTests");
  }

  public void doWork() {
    debug("MDCompleteWorkManager enterred doWork");

    try {
      WorkImpl workimpl = new WorkImpl(wmgr);
      ExecutionContext ec = startTx();
      ContextWork w1 = new ContextWork(wmgr);

      // helps test: Connector:SPEC:72, Connector:SPEC:73, Connector:SPEC:214
      // this better generate an end result of a WorkCompletedException with
      // an error code == WorkContextErrorCodes.UNSUPPORTED_CONTEXT_TYPE
      debug(
          "MDCompleteWorkManager.doWork() submitting UnknownWorkContext generate error code.");

      UnknownWorkContext uwc = new UnknownWorkContext();
      uwc.setXid(ec.getXid());
      w1.addWorkContext(uwc);

      // submitting work inst w/ an unknown work context shoudl throw
      // proper error code...
      wmgr.doWork(w1);

    } catch (WorkCompletedException e) {
      // this must throw exception ==
      // WorkContextErrorCodes.UNSUPPORTED_CONTEXT_TYPE
      debug("MDCompleteWorkManager WorkCompletedException thrown is "
          + e.getMessage());

      // this helps verify assertion Connector:SPEC:214
      // get error code and make sure we get one that makes sense
      String strErrorCode = e.getErrorCode();
      if (WorkContextErrorCodes.UNSUPPORTED_CONTEXT_TYPE.equals(strErrorCode)) {
        // excellant - this is what we expect
        ConnectorStatus.getConnectorStatus().logState(
            "MDCompleteWorkManager threw WorkContextErrorCodes.UNSUPPORTED_CONTEXT_TYPE");
        debug(
            "MDCompleteWorkManager threw WorkContextErrorCodes.UNSUPPORTED_CONTEXT_TYPE");
      } else {
        // doh! we got incorrect error code
        debug("MDCompleteWorkManager threw WorkContextErrorCodes = "
            + strErrorCode);
      }
    } catch (WorkException we) {
      debug("MDCompleteWorkManager WorkException thrown is " + we.getMessage());
      Debug.printDebugStack(we);
    } catch (Exception ex) {
      debug("MDCompleteWorkManager Exception thrown is " + ex.getMessage());
      Debug.printDebugStack(ex);
    }

    debug("MDCompleteWorkManager leaving doWork");
  }

  /*
   * this method is used to facilitate testing assertion Connector:SPEC:305 the
   * idea here is to test as follows: - create parent work obj - add valid SIC
   * to parent work obj - create 2nd work obj to be child and get nested within
   * parent work obj - assign NO SIC to child work obj - add child work obj into
   * parent - execute parent work obj - parent has valid SIC so should be okay
   * but child has NO SIC and should NOT inherit the parents SIC so the child
   * workobj XXXX: how to verify the workInst with no SIC did not inherit!
   */
  public void testNestedContext2() {

    try {
      debug("enterred testNestedWork2()");

      // to properly test assert Connector:SPEC:305 we need to have nested
      // work objects where each work has a context set.
      ContextWork parent = new ContextWork(wmgr);
      NestWork nw = new NestWork();

      // create valid sic / creds for parent work obj
      SecurityContext psic = new TSSecurityContextWithListener(sicUser, sicPwd,
          eisUser, false);

      // lets add SIC to our parent work obj only
      parent.addWorkContext(psic); // add SIC w/ valid creds

      // add our child workobj(ie nw) into our parent work obj
      parent.addNestedWork(nw);

      wmgr.doWork(parent);

    } catch (WorkException e) {
      debug("testNestedWork2() - got WorkException()");
    } catch (Exception e) {
      // flow should not go here
      debug("got exception in testNestedContext2() with user = " + sicUser
          + " pwd = " + sicPwd + "  principal=" + eisUser);
      debug(e.toString());
      Debug.printDebugStack(e);
    }
    debug("leaving testNestedContext2()");
  }

  /*
   * This method is used to facilitate testing assertion Connector:SPEC:210 The
   * following steps are needed to validate assertion 210. - create parent work
   * obj - add valid SIC to parent work obj - create 2nd work obj to be child
   * and get nested within parent work obj - assign another valid
   * (authenticatable) SIC to child work obj - add child work obj into parent
   * work obj - execute parent work obj (which, in turn, attempts to execute
   * child ) - parent has valid SIC so should be okay - but child does not
   *
   * The goal here is to show that the AS supports nested contexts by supplying
   * 2 different SIC contexts (one valid and one invalid).
   *
   */
  public void testNestedContext3() {

    try {
      debug("enterred testNestedWork3()");

      // to properly test assert Connector:SPEC:305 we need to have nested
      // work objects where each work has a context set.
      ContextWork parent = new ContextWork(wmgr);
      NestWork nw = new NestWork();

      // create valid sic / creds for parent work obj
      TSSecurityContext psic = new TSSecurityContext(sicUser, sicPwd, eisUser,
          false);

      // create invalid sic / creds for child work obj
      // note we pass 'false' as we expect it should fail to authenticate
      // with teh bogus creds we are passing in...if it fails to authenticate,
      // it should log appropriate msg stating so which menas that our
      // child/nested work context did not inherit security from parent.
      TSNestedSecurityContext csic = new TSNestedSecurityContext("phakeUsr",
          "phakePwd", "phakeEis", false, false);
      nw.addWorkContext(csic); // add SIC w/ invalid creds

      // lets add SIC to our parent work obj only
      parent.addWorkContext(psic); // add SIC w/ valid creds

      // add our child workobj(ie nw) into our parent work obj
      parent.addNestedWork(nw);

      wmgr.doWork(parent);

    } catch (WorkException e) {
      debug("testNestedWork3() - got WorkException()");
    } catch (Exception e) {
      // flow should not go here
      debug("got exception in testSecurityInflow() with user = " + sicUser
          + " pwd = " + sicPwd + "  principal=" + eisUser);
      debug(e.toString());
      Debug.printDebugStack(e);
    }

    debug("leaving testNestedContext3()");
  }

  /*
   * This method is used to facilitate testing assertion Connector:SPEC:210 The
   * following steps are needed to validate assertion 210. - create parent work
   * obj - add valid SIC to parent work obj - create 2nd work obj to be child
   * and get nested within parent work obj - assign another valid
   * (authenticatable) SIC to child work obj - add child work obj into parent
   * work obj - execute parent work obj (which, in turn, attempts to execute
   * child ) - parent has valid SIC so should be okay - same with child
   *
   * The goal here is to show that the AS supports nested contexts by supplying
   * 2 different SIC contexts (both valid).
   *
   */
  public void testNestedContext() {

    String strPass = "Nested Work and Nested Security Context worked.";

    try {
      debug("enterred testNestedContext()");

      // to properly test assert Connector:SPEC:210 we need to have nested
      // work objects where each work has a context set.
      ContextWork parent = new ContextWork(wmgr);
      NestWork nw = new NestWork();
      SecurityContext sic = new TSSecurityContextWithListener(sicUser, sicPwd,
          eisUser, false);

      // add SIC that should not be able to authenticate
      SecurityContext sic2 = new TSSecurityContextWithListener(sicUser, sicPwd,
          eisUser, false);

      // lets add two different SICs to our work objs
      parent.addWorkContext(sic); // valid creds
      nw.addWorkContext(sic2); // valid creds

      // add our child workobj(ie nw) into our parent work obj
      parent.addNestedWork(nw);

      // this may or may not throw WorkCompletedException upon
      // successful execution of work.
      wmgr.doWork(parent);

      // note: flow should make it here and NOT yeild any exceptions.
      ConnectorStatus.getConnectorStatus().logState(strPass);
      debug(strPass);

    } catch (WorkCompletedException e) {
      // could get here upon success work completion
      ConnectorStatus.getConnectorStatus().logState(strPass);
      debug(strPass);
    } catch (Exception e) {
      // should not get here
      debug("got exception in testSecurityInflow() with user = " + sicUser
          + " pwd = " + sicPwd + "  principal=" + eisUser);
      debug(e.toString());
      Debug.printDebugStack(e);
    }
    debug("leaving testNestedContext()");
  }

  public void submitTICWork() {

    try {
      debug("enterred submitTICWork()");

      ExecutionContext ec = startTx();
      ContextWork w1 = new ContextWork(wmgr);
      TransactionContext tic = new TransactionContext();
      tic.setXid(ec.getXid());

      // add same tic twice and AS should throw
      // WorkContextErrorCodes.DUPLICATE_CONTEXTS
      debug(
          "adding Duplicate WorkContext (with dup TIC Listener) should throw WorkContextErrorCodes.DUPLICATE_CONTEXTS.");
      w1.addWorkContext(tic);
      w1.addWorkContext(tic);
      wmgr.doWork(w1);

      debug("submitted Duplicate WorkContext with dup TIC Listener");

    } catch (WorkCompletedException e) {
      String strErrorCode = e.getErrorCode();
      if (WorkContextErrorCodes.DUPLICATE_CONTEXTS.equals(strErrorCode)) {
        // excellant - this is what we expect
        ConnectorStatus.getConnectorStatus().logState(
            "MDCompleteWorkManager threw WorkContextErrorCodes.DUPLICATE_CONTEXTS");
        debug(
            "MDCompleteWorkManager correctly threw WorkContextErrorCodes.DUPLICATE_CONTEXTS");
      } else {
        // doh! we got incorrect error code
        debug("MDCompleteWorkManager threw improper WorkContextErrorCodes = "
            + strErrorCode);
      }
      debug("MDCompleteWorkManager threw WorkContextErrorCodes = "
          + strErrorCode);
    } catch (Exception e) {
      debug(
          "got bad exception when testing for WorkContextErrorCodes.DUPLICATE_CONTEXTS");
      Debug.printDebugStack(e);
    }
    debug("leaving submitTICWork()");
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

  public void debug(String out) {
    Debug.trace("MDCompleteWorkManager:  " + out);
  }

}
