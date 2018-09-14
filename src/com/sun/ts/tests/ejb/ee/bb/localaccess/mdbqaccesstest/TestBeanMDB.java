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
 * @(#)TestBeanMDB.java	1.7 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.localaccess.mdbqaccesstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;
import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.jms.*;

public class TestBeanMDB extends ParentMsgBean {

  private QueueSender qSender = null;

  private MessageDrivenContext mdc = null;

  private Properties harnessProps = null;

  // Entity Bean (CMP) A -> Local Interface Only
  // Session Bean (SF) B -> Local Interface Only
  // Entity Bean (BMP) C -> Local Interface Only
  // Session Bean (SL) D -> Local Interface Only

  // JNDI Names for A, B, C, D Local Home Interface
  private static final String ALocal = "java:comp/env/ejb/AEJBLocal";

  private static final String BLocal = "java:comp/env/ejb/BEJBLocal";

  private static final String CLocal = "java:comp/env/ejb/CEJBLocal";

  private static final String DLocal = "java:comp/env/ejb/DEJBLocal";

  // References to Local Interfaces for Session and Entity Bean A,B,C,D
  private ALocal aLocalRef = null;

  private ALocalHome aLocalHome = null;

  private BLocal bLocalRef = null;

  private BLocalHome bLocalHome = null;

  private CLocal cLocalRef = null;

  private CLocalHome cLocalHome = null;

  private DLocal dLocalRef = null;

  private DLocalHome dLocalHome = null;

  // ===========================================================
  // private methods

  private ALocal createA(int id, String name, int value) throws Exception {
    TestUtil.logTrace("createA");
    aLocalHome = (ALocalHome) context.lookup(ALocal);
    aLocalRef = aLocalHome.createA(id, name, value);
    return aLocalRef;
  }

  private BLocal createB() throws Exception {
    TestUtil.logTrace("createB");
    bLocalHome = (BLocalHome) context.lookup(BLocal);
    bLocalRef = bLocalHome.createB();
    return bLocalRef;
  }

  private CLocal createC(Properties p, int id, String name, int value)
      throws Exception {
    TestUtil.logTrace("createC");
    cLocalHome = (CLocalHome) context.lookup(CLocal);
    cLocalRef = cLocalHome.createC(p, id, name, value);
    return cLocalRef;
  }

  private DLocal createD() throws Exception {
    TestUtil.logTrace("createD");
    dLocalHome = (DLocalHome) context.lookup(DLocal);
    dLocalRef = dLocalHome.create();
    return dLocalRef;
  }

  protected void runTests(Message msg, QueueSession qSession, String testName,
      java.util.Properties p) {

    boolean result = false;
    String testcase;
    String propName;
    try {

      System.out.println(" TestBeanMDB runTests!");
      TestUtil.logTrace("  from TestBeanMDB runTests!");
      // for (Enumeration e = p.propertyNames(); e.hasMoreElements();){
      // propName = (String)e.nextElement();
      // TestUtil.logTrace("@@**!!+==+==+ : " + propName + " " );
      // }
      if (msg.getIntProperty("TestCaseNum") > 0) {

        switch (msg.getIntProperty("TestCaseNum")) {

        case 1:
          result = test1();
          break;
        case 2:
          result = test2();
          break;
        case 3:
          result = test3();
          break;
        case 4:
          result = test4();
          break;
        default:
          TestUtil.logTrace("Error in mdb - ");
          TestUtil.logTrace("No test match for TestCaseNum: "
              + msg.getIntProperty("TestCaseNum"));
          break;
        }
      }
      TestUtil.logTrace("from TestBeanMDB - sending response");
      testcase = msg.getStringProperty("COM_SUN_JMS_TESTNAME");
      if (testName.equals("remove_stateful_bean")) {
        TestUtil.logTrace("Removing stateful session bean");
        cleanUpStatefulBean();
        return;
      }
      TestUtil.logTrace("for testcase: " + testcase);
      TestUtil.logTrace("Result sent to replyQ is " + result);
      sendResponseToQ(testcase, qSession, result);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

  } // runTests

  // ===========================================================
  // EJB Specification Required Methods

  private void sendResponseToQ(String testcase, QueueSession qSession,
      boolean result) {
    TextMessage msg = null;
    try {
      qSender = qSession.createSender(queueR);
      msg = qSession.createTextMessage();
      msg.setStringProperty("TestCase", testcase);
      msg.setText(testcase);
      if (result)
        msg.setStringProperty("Status", "Pass");
      else
        msg.setStringProperty("Status", "Fail");
      qSender.send(msg);
    } catch (JMSException je) {
      TestUtil.printStackTrace(je);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public boolean test1() {
    TestUtil.logTrace("test1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Lookup local home of Entity Bean (CMP) and do create");
      aLocalRef = createA(1, "a1", 1);
      String s = aLocalRef.whoAmI();
      TestUtil.logMsg("Calling local business method: " + s);
      if (!s.equals("entity-cmp")) {
        TestUtil.logErr(
            "Wrong string returned: got: " + s + ", expected: entity-cmp");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        aLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean test2() {
    TestUtil.logTrace("test2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Lookup local home of Session Bean (SF) and do create");
      bLocalRef = createB();
      String s = bLocalRef.whoAmI();
      TestUtil.logMsg("Calling local business method: " + s);
      if (!s.equals("session-stateful")) {
        TestUtil.logErr("Wrong string returned: got: " + s
            + ", expected: session-stateful");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  public boolean test3() {
    TestUtil.logTrace("test3");
    boolean pass = true;
    try {
      TestUtil.logMsg("Lookup local home of Entity Bean (BMP) and do create");
      cLocalRef = createC(p, 1, "c1", 1);
      String s = cLocalRef.whoAmI();
      TestUtil.logMsg("Calling local business method: " + s);
      if (!s.equals("entity-bmp")) {
        TestUtil.logErr(
            "Wrong string returned: got: " + s + ", expected: entity-bmp");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        cLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public boolean test4() {
    TestUtil.logTrace("test4");
    boolean pass = true;
    try {
      TestUtil.logMsg("Lookup local home of Session Bean (SL) and do create");
      dLocalRef = createD();
      String s = dLocalRef.whoAmI();
      TestUtil.logMsg("Calling local business method: " + s);
      if (!s.equals("session-stateless")) {
        TestUtil.logErr("Wrong string returned: got: " + s
            + ", expected: session-stateless");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        dLocalRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return pass;
  }

  public void cleanUpStatefulBean() {
    TestUtil.logTrace("cleanUpStatefulBean");
    try {
      if (bLocalRef != null) {
        bLocalRef.remove();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception caught trying to remove bLocalRef");
    }
  }

  // ===========================================================
}
