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

import java.util.Properties;
import javax.ejb.EJBHome;

import com.sun.ts.tests.jms.common.*;
import com.sun.ts.tests.jms.commonee.*;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import javax.jms.Queue;
import com.sun.javatest.Status;

public class MDBClient extends Client {

  // Naming specific member variables
  private Queue cmtQ;

  private Queue bmtQ;

  private static final String user = "user", password = "password";

  private String user_value, password_value;

  /* Run test in standalone mode */
  public static void main(String[] args) {
    MDBClient theTests = new MDBClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * Test setup:
   * 
   * @class.setup_props: jms_timeout, in milliseconds - how long to wait on
   * synchronous receive; user;password;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    super.setup(args, p);

    try {

      TestUtil.logTrace("look up cmtQ");
      cmtQ = (Queue) context.lookup("java:comp/env/jms/EJB_SEC_MDB_QUEUE_CMT");
      TestUtil.logTrace("look up bmtQ");
      bmtQ = (Queue) context.lookup("java:comp/env/jms/EJB_SEC_MDB_QUEUE_BMT");

    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }
  /* Run tests */

  /*
   * @testName: Test1
   *
   * @assertion_ids: EJB:SPEC:513; EJB:SPEC:528; EJB:SPEC:823; JavaEE:SPEC:130;
   * JavaEE:SPEC:10035
   *
   * @test_Strategy: Invoke an cmt mdb by writing to EJB_SEC_MDB_QUEUE_CMT. The
   * mdb attempts a EJBContext getCallerPrincipal() method. Return verification
   * message that a java.lang.IllegalStateException was not thrown and
   * getCallerPrincipal() does not return null.
   *
   */
  public void Test1() throws Fault {
    String TestCase = "expTest1";
    int TestNum = 1;
    try {
      qSender = session.createSender(cmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test1 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test2
   *
   * @assertion_ids: EJB:SPEC:513; EJB:SPEC:528; EJB:SPEC:823; JavaEE:SPEC:130;
   * JavaEE:SPEC:10035
   *
   * @test_Strategy: Invoke an bmt mdb by writing to EJB_SEC_MDB_QUEUE_BMT. The
   * mdb attempts a EJBContext getCallerPrincipal() method. Return verification
   * message that a java.lang.IllegalStateException was not thrown and
   * getCallerPrincipal() does not return null.
   *
   */
  public void Test2() throws Fault {
    String TestCase = "expTest2";
    int TestNum = 2;
    try {

      qSender = session.createSender(bmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test2 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test3
   *
   * @assertion_ids: EJB:SPEC:513; EJB:SPEC:528; JavaEE:SPEC:130;
   * JavaEE:SPEC:10035
   *
   * @test_Strategy: Invoke an cmt mdb by writing to EJB_SEC_MDB_QUEUE_CMT. The
   * mdb attempts a EJBContext isCallerInRole() method. Return verification
   * message that a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test3() throws Fault {
    String TestCase = "expTest3";
    int TestNum = 3;
    try {
      qSender = session.createSender(cmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test3 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /*
   * @testName: Test4
   *
   * @assertion_ids: EJB:SPEC:513; EJB:SPEC:528; JavaEE:SPEC:130;
   * JavaEE:SPEC:10035
   *
   * @test_Strategy: Invoke an bmt mdb by writing to EJB_SEC_MDB_QUEUE_BMT. The
   * mdb attempts a EJBContext isCallerInRole() method. Return verification
   * message that a java.lang.IllegalStateException was thrown.
   *
   */
  public void Test4() throws Fault {
    String TestCase = "expTest4";
    int TestNum = 4;
    try {

      qSender = session.createSender(bmtQ);
      // create a text message
      createTestMessage(TestCase, TestNum);
      // send the message
      qSender.send(msg);

      // verify that message was requeued and pass
      if (!checkOnResponse(TestCase)) {
        throw new Exception("Test4 - ");
      }
    } catch (Exception e) {
      throw new Fault("Test Failed!", e);
    }
  }

  /* cleanup -- use super cleanup */

}
