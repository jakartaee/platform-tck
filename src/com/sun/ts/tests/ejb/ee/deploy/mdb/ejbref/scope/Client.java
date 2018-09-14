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
 * @(#)Client.java	1.13 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.mdb.ejbref.scope;

import java.util.Properties;
import javax.jms.Queue;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.Status;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  private Queue mdb1;

  private Queue mdb2;

  private Queue mdb3;

  /* Expected values for partners */
  private static final String bean1RefPartner = "Juliette";

  private static final String bean2RefPartner = "Iseult";

  private static final String bean3RefPartner = "Roxane";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: jms_timeout; user; password;
   *
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      super.setup(args, props);

      mdb1 = (Queue) context.lookup("java:comp/env/jms/MDB1");
      mdb2 = (Queue) context.lookup("java:comp/env/jms/MDB2");
      mdb3 = (Queue) context.lookup("java:comp/env/jms/MDB3");
    } catch (Exception e) {
      TestUtil.logErr("[Client] Setup failed! ", e);
      throw new Fault("Setup Failed!", e);
    }
  }

  /**
   * @testName: testEjbRefScopeInternal1
   *
   * @assertion_ids: EJB:SPEC:757.2
   *
   * @test_Strategy:
   *
   *                 We package in the same jar:
   *
   *                 - Two ReferencingBean's using the same ejb-ref-name
   *                 ('ejb/Partner') to reference two distinct ReferencedBean's.
   *                 - Two ReferencedBean's, whose identity is defined by a
   *                 String environment entry ('myName').
   *
   *                 We check that:
   *
   *                 - We can deploy the application. - We can lookup each of
   *                 the referencing beans. - Each ReferencingBean can lookup
   *                 its ReferencedBean and get the ReferencedBean's identity. -
   *                 Check this runtime identity against the one specified in
   *                 the DD (in an attempt to check the correct resolution of
   *                 the reference).
   *
   */
  public void testEjbRefScopeInternal1() throws Fault {

    String bean1Partner;

    String testCase = "whoIsYourPartner";
    int testNum = 1;

    try {
      qSender = session.createSender(mdb1);
      createTestMessage(testCase, testNum);
      msg.setStringProperty("beanPartner", bean1RefPartner);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Exception(testCase + " failed !");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("[Client] " + testCase + " failed!", e);
    }
  }

  /**
   * @testName: testEjbRefScopeInternal2
   *
   * @assertion_ids: EJB:SPEC:757.2
   *
   * @test_Strategy:
   *
   *                 We package in the same jar:
   *
   *                 - Two ReferencingBean's using the same ejb-ref-name
   *                 ('ejb/Partner') to reference two distinct ReferencedBean's.
   *                 - Two ReferencedBean's, whose identity is defined by a
   *                 String environment entry ('myName').
   *
   *                 We check that:
   *
   *                 - We can deploy the application. - We can lookup each of
   *                 the referencing beans. - Each ReferencingBean can lookup
   *                 its ReferencedBean and get the ReferencedBean's identity. -
   *                 Check this runtime identity against the one specified in
   *                 the DD (in an attempt to check the correct resolution of
   *                 the reference).
   *
   */
  public void testEjbRefScopeInternal2() throws Fault {

    String bean2Partner;

    String testCase = "whoIsYourPartner";
    int testNum = 2;

    try {
      qSender = session.createSender(mdb2);
      createTestMessage(testCase, testNum);
      msg.setStringProperty("beanPartner", bean2RefPartner);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Exception(testCase + " failed !");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: ", e);
      throw new Fault("[Client] " + testCase + " failed!", e);
    }
  }

  /**
   * @testName: testEjbRefScopeExternal1
   *
   * @assertion_ids: EJB:SPEC:757.2
   *
   * @test_Strategy:
   *
   *                 We package in the one jar:
   *
   *                 - ReferencingBean1 using ejb-ref-name ('ejb/Partner') to
   *                 reference ReferencedBean1. - ReferencedBean1 and
   *                 ReferencedBean3, whose identity is defined by a String
   *                 environment entry ('myName').
   *
   *                 We package in another jar (part of the same EAR file):
   *
   *                 - ReferencingBean3 using ejb-ref-name ('ejb/Partner') to
   *                 reference ReferencedBean3 (packaged in previous jar).
   * 
   *                 We check that:
   *
   *                 - We can deploy the application. - We can lookup each of
   *                 the referencing beans. - Each ReferencingBean can lookup
   *                 its ReferencedBean and get the ReferencedBean's identity. -
   *                 Check this runtime identity against the one specified in
   *                 the DD (in an attempt to check the correct resolution of
   *                 the reference).
   *
   */
  public void testEjbRefScopeExternal1() throws Fault {

    String bean1Partner;

    String testCase = "whoIsYourPartner";
    int testNum = 3;

    try {
      qSender = session.createSender(mdb1);
      createTestMessage(testCase, testNum);
      msg.setStringProperty("beanPartner", bean1RefPartner);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Exception(testCase + " failed !");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: " + e);
      throw new Fault("[Client] " + testCase + " failed!" + e, e);
    }
  }

  /**
   * @testName: testEjbRefScopeExternal2
   *
   * @assertion_ids: EJB:SPEC:757.2
   *
   * @test_Strategy:
   *
   *                 We package in the one jar:
   *
   *                 - ReferencingBean1 using ejb-ref-name ('ejb/Partner') to
   *                 reference ReferencedBean1. - ReferencedBean1 and
   *                 ReferencedBean3, whose identity is defined by a String
   *                 environment entry ('myName').
   *
   *                 We package in another jar (part of the same EAR file):
   *
   *                 - ReferencingBean3 using ejb-ref-name ('ejb/Partner') to
   *                 reference ReferencedBean3 (packaged in previous jar).
   * 
   *                 We check that:
   *
   *                 - We can deploy the application. - We can lookup each of
   *                 the referencing beans. - Each ReferencingBean can lookup
   *                 its ReferencedBean and get the ReferencedBean's identity. -
   *                 Check this runtime identity against the one specified in
   *                 the DD (in an attempt to check the correct resolution of
   *                 the reference).
   *
   */
  public void testEjbRefScopeExternal2() throws Fault {

    String bean3Partner;

    String testCase = "whoIsYourPartner";
    int testNum = 4;

    try {
      qSender = session.createSender(mdb3);
      createTestMessage(testCase, testNum);
      msg.setStringProperty("beanPartner", bean3RefPartner);
      qSender.send(msg);

      if (!checkOnResponse(testCase)) {
        throw new Exception(testCase + " failed !");
      }
    } catch (Exception e) {
      logErr("[Client] Caught exception: " + e);
      throw new Fault("[Client] " + testCase + " failed!" + e, e);
    }
  }

}
