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

package com.sun.ts.tests.ejb.ee.deploy.mdb.ejbref.singleT;

import java.util.Properties;
import javax.jms.Topic;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.javatest.Status;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  private Topic mdbT;

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

      TestUtil.logTrace("[Client] Initializing BMP table...");
      DAOFactory.getInstance().getCoffeeDAO().cleanup();

      mdbT = (Topic) context.lookup("java:comp/env/jms/MDBTest");
    } catch (Exception e) {
      throw new Fault("Setup Failed!", e);
    }
  }

  /**
   * @testName: testStatelessInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven Topic bean referencing another
   *                 stateless session bean (StatelessInternal) part of the same
   *                 JAR file. The EJB reference is declared without using the
   *                 optional ejb-link element in the DD.
   *
   *                 Check at runtime that Topic MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessInternal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testStatelessInternal() throws Fault {

    String testCase = "testStatelessInternal";
    int testNum = 1;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testStatelessExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven bean referencing a stateless
   *                 session bean (StatelessExternal) part of another JAR file.
   *                 The EJB reference is declared without using the optional
   *                 ejb-link element in the DD.
   *
   *                 Check at runtime that Topic MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessExternal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testStatelessExternal() throws Fault {

    String testCase = "testStatelessExternal";
    int testNum = 2;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testStatefulInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven Topic bean referencing a stateful
   *                 session bean (StatefulInternal) part of the same JAR file.
   *                 The EJB reference is declared without using the optional
   *                 ejb-link element in the DD.
   *
   *                 Check at runtime that Topic MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulInternal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testStatefulInternal() throws Fault {

    String testCase = "testStatefulInternal";
    int testNum = 3;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    } finally {
      try {
        createTestMessage("cleanUpBean", 0);
        tPub.publish(msg);
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
   * @test_Strategy: Deploy a message-driven Topic bean referencing a stateful
   *                 session bean (StatefulExternal) part of another JAR file.
   *                 The EJB reference is declared without using the optional
   *                 ejb-link element in the DD.
   *
   *                 Check at runtime that Topic MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulExternal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testStatefulExternal() throws Fault {

    String testCase = "testStatefulExternal";
    int testNum = 4;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    } finally {
      try {
        createTestMessage("cleanUpBean", 0);
        tPub.publish(msg);
      } catch (Exception ee) {
        TestUtil.logErr("Exception caught removing SSF bean", ee);
      }
    }
  }

  /**
   * @testName: testBMPInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a Topic message-driven bean referencing a BMP Entity
   *                 bean (BMPInternal) part of the same JAR file. The EJB
   *                 reference is declared without using the optional ejb-link
   *                 element in the DD.
   *
   *                 Check at runtime that Topic MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in BMPInternal
   *                 beans in an attempt to check that the EJB reference was
   *                 resolved consistently with the DD.
   */
  public void testBMPInternal() throws Fault {

    String testCase = "testBMPInternal";
    int testNum = 5;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testBMPExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven Topic bean referencing a BMP emtity
   *                 bean (BMPExternal) part of another JAR file. The EJB
   *                 reference is declared without using the optional ejb-link
   *                 element in the DD.
   *
   *                 Check at runtime that Topic MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in BMPExternal
   *                 beans in an attempt to check that the EJB reference was
   *                 resolved consistently with the DD.
   */
  public void testBMPExternal() throws Fault {

    String testCase = "testBMPExternal";
    int testNum = 6;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testCMP11Internal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven bean referencing a CMP 1.1 Entity
   *                 bean (CMP11Internal) part of the same JAR file. The EJB
   *                 reference is declared without using the optional ejb-link
   *                 element in the DD.
   *
   *                 Check at runtime that a Topic MDB can do a lookup for the
   *                 ejb reference and use it to create a bean. Then invoke on
   *                 that instance a business method to be found only in
   *                 CMP20Internal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testCMP11Internal() throws Fault {

    String testCase = "testCMP11Internal";
    int testNum = 7;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testCMP11External
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a (Topic) message-driven bean referencing a CMP 1.1
   *                 entity bean (CMP11External) part of another JAR file. The
   *                 EJB reference is declared without using the optional
   *                 ejb-link element in the DD.
   *
   *                 Check at runtime that a Topic MDB can do a lookup for the
   *                 ejb reference and use it to create a bean. Then invoke on
   *                 that instance a business method to be found only in
   *                 CMP11External beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testCMP11External() throws Fault {

    String testCase = "testCMP11External";
    int testNum = 8;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testCMP20Internal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a message-driven bean referencing a CMP 2.0 Entity
   *                 bean (CMP20Internal) part of the same JAR file. The EJB
   *                 reference is declared without using the optional ejb-link
   *                 element in the DD.
   *
   *                 Check at runtime that Topic MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP20Internal beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testCMP20Internal() throws Fault {

    String testCase = "testCMP20Internal";
    int testNum = 9;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

  /**
   * @testName: testCMP20External
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a Topic Message-driven bean referencing a CMP 2.0
   *                 entity bean (CMP20External) part of another JAR file. The
   *                 EJB reference is declared without using the optional
   *                 ejb-link element in the DD.
   *
   *                 Check at runtime that Topic MDB can do a lookup for the ejb
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 CMP20External beans in an attempt to check that the EJB
   *                 reference was resolved consistently with the DD.
   */
  public void testCMP20External() throws Fault {

    String testCase = "testCMP20External";
    int testNum = 10;

    try {
      tPub = tSession.createPublisher(mdbT);
      createTestMessage(testCase, testNum);
      tPub.publish(msg);

      if (!checkOnResponse(testCase)) {
        TestUtil.logErr("[Client] " + testCase + " failed");
        throw new Exception(testCase + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + testCase + " failed: ", e);
      throw new Fault(testCase + " failed!", e);
    }
  }

}
