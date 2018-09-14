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

package com.sun.ts.tests.ejb.ee.sec.mdb;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;
import java.io.Serializable;
import java.util.*;
import javax.ejb.EJBException;
import javax.ejb.EJBHome;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.naming.*;
import javax.jms.*;
import java.security.*;
import java.sql.*;
import javax.sql.*;
import javax.transaction.UserTransaction;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

public class MsgBean extends ParentMsgBeanNoTx {

  private boolean result = false;

  protected void runTests(Message msg, QueueSession qSession, String testName,
      java.util.Properties p) {
    try {
      // test to see if this is the first message
      if (msg.getIntProperty("TestCaseNum") > 0) {

        switch (msg.getIntProperty("TestCaseNum")) {

        case 1: // MDB Queue w/CMT
        case 2: // MDB Queue w/BMT
          runGetCallerPrincipal(msg, qSession, testName);
          break;

        case 3: // MDB Queue w/CMT
        case 4: // MDB Queue w/BMT
          runIsCallerInRole(msg, qSession, testName);
          break;

        default:
          TestUtil.logTrace("Error in mdb - ");
          TestUtil.logTrace("No test match for TestCaseNum: "
              + msg.getIntProperty("TestCaseNum"));
          break;
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  // runTests

  public void runGetCallerPrincipal(Message msg, QueueSession qSession,
      String testName) {
    result = false;
    // this should not throw an exception
    try {
      Principal cp = mdc.getCallerPrincipal();
      if (cp != null) {
        TestUtil.logMsg(
            "getCallerPrincipal() returned" + " non-null principal: " + cp);
        result = true;
      } else {
        TestUtil.logErr("getCallerPrincipal() returned null reference");
        result = false;
      }
    } catch (java.lang.IllegalStateException e) {
      TestUtil.printStackTrace(e);
      TestUtil.logTrace("getCallerPrincipal() - Not Allowed");
      result = false;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("Unexpected Exception caught in runGetCallerPrincipal",
          e);
    }
    // send a message to MDB_QUEUE_REPLY.
    JmsUtil.sendTestResults(testName, result, qSession, queueR);
  }

  public void runIsCallerInRole(Message msg, QueueSession qSession,
      String testName) {
    String role = "Administrator";
    result = false;

    // this should throw an exception
    try {
      boolean cr = mdc.isCallerInRole(role);

      TestUtil.logTrace("isCallerInRole() Test Succeeded!");
      result = true;
    } catch (java.lang.IllegalStateException e) {
      // this used to be proper behavior befor ejb 3.1 but now
      // invoking isCallerinRole() should not generate exception
      TestUtil.logTrace("isCallerInRole() threw exception with message of:  "
          + e.getMessage());
      TestUtil.logTrace(
          "isCallerInRole() Test Failed!  IllegalStateException caught");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("Unexpected Exception caught in runIsCallerInRole", e);
    }
    // send a message to MDB_QUEUE_REPLY.
    JmsUtil.sendTestResults(testName, result, qSession, queueR);
  }

}
