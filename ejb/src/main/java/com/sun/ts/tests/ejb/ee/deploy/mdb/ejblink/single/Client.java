/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb.ee.deploy.mdb.ejblink.single;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;

import jakarta.jms.Queue;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  private Queue mdbQ;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: jms_timeout; user; password; generateSQL;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      super.setup(args, props);

      mdbQ = (Queue) context.lookup("java:comp/env/jms/MDBTest");
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /**
   * @testName: testStatelessInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven queue bean referencing a stateless
   *                 session bean (StatelessInternal) part of the same JAR file.
   *
   *                 Check at runtime that the MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessInternal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testStatelessInternal() throws Fault {

    String testCase = "testStatelessInternal";
    int testNum = 1;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed!");
        throw new Exception(testCase + " failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed!", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testStatelessExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven queue bean referencing a stateless
   *                 session bean (StatelessExternal) part of another JAR file.
   *
   *                 Check at runtime that the MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessExternal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testStatelessExternal() throws Fault {

    String testCase = "testStatelessExternal";
    int testNum = 2;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed!");
        throw new Exception(testCase + " failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed!", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testStatefulInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven bean referencing a stateful session
   *                 bean (StatefulInternal) part of the same JAR file.
   *
   *                 Check at runtime that the MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulInternal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testStatefulInternal() throws Fault {

    String testCase = "testStatefulInternal";
    int testNum = 3;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed!");
        throw new Exception(testCase + " failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed!", e);
      throw new Fault(testCase + " failed!", e);
    } finally {
      try {
        createTestMessage("cleanUpBean", 0);
        qSender.send(msg);
      } catch (Exception ee) {
        TestUtil.logErr("Exception caught removing SSF bean", ee);
      }
    }
  }

  /**
   * @testName: testStatefulExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven queue bean referencing a stateful
   *                 session bean (StatefulExternal) part of another JAR file.
   *
   *                 Check at runtime that a MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulExternal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testStatefulExternal() throws Fault {

    String testCase = "testStatefulExternal";
    int testNum = 4;

    try {
      qSender = session.createSender(mdbQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed!");
        throw new Exception(testCase + " failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed!", e);
      throw new Fault(testCase + " failed!", e);
    } finally {
      try {
        createTestMessage("cleanUpBean", 0);
        qSender.send(msg);
      } catch (Exception ee) {
        TestUtil.logErr("Exception caught removing SSF bean", ee);
      }
    }
  }

}
