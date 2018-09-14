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

package com.sun.ts.tests.ejb.ee.deploy.mdb.enventry.scope;

import java.util.Properties;
import javax.jms.Queue;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.Status;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  /*
   * JNDI Names for the beans.
   */
  private static final String prefix = "java:comp/env/jms/";

  private static final String bean1Name_SameJar = prefix + "Bean1_SameJar";

  private static final String bean2Name_SameJar = prefix + "Bean2_SameJar";

  private static final String bean1Name_MultiJar = prefix + "Bean1_MultiJar";

  private static final String bean2Name_MultiJar = prefix + "Bean2_MultiJar";

  /*
   * String env entry name and its expected values.
   */
  private static final String entryName = "Duende";

  private static final String bean1Value_SameJar = "Paco de Lucia";

  private static final String bean2Value_SameJar = "El Camaron";

  private static final String bean1Value_MultiJar = "Vincente Amigo";

  private static final String bean2Value_MultiJar = "Tomatito";

  private Queue mdbQ1;

  private Queue mdbQ2;

  private Queue mdbQ3;

  private Queue mdbQ4;

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

      TestUtil.logTrace("Looking up " + bean1Name_SameJar);
      mdbQ1 = (Queue) context.lookup(bean1Name_SameJar);
      mdbQ2 = (Queue) context.lookup(bean2Name_SameJar);
      mdbQ3 = (Queue) context.lookup(bean1Name_MultiJar);
      mdbQ4 = (Queue) context.lookup(bean2Name_MultiJar);
    } catch (Exception e) {
      TestUtil.logErr("[Client] Setup failed! ", e);
      throw new Fault("Setup Failed!", e);
    }
  }

  /**
   * @testName: testScopeInternal1
   *
   * @assertion_ids: EJB:SPEC:757.1
   *
   * @test_Strategy: Deploy two MDB's (part of a same JAR file) using the same
   *                 env-entry-name but declaring different values. Check that
   *                 we can lookup this env entry from both EJB's, that runtime
   *                 values are distinct, and that they correspond to the ones
   *                 in the DD.
   */
  public void testScopeInternal1() throws Fault {

    String operation = "checkEntry";
    int testNum = 1;

    try {
      qSender = session.createSender(mdbQ1);
      createTestMessage(operation, testNum);
      msg.setStringProperty("entryLookup", entryName);
      msg.setStringProperty("expectedValue", bean1Value_SameJar);
      qSender.send(msg);

      if (!checkOnResponse(operation)) {
        TestUtil.logErr("[Client] " + operation + " failed");
        throw new Exception(operation + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + operation + " failed: ", e);
      throw new Fault(operation + " failed!", e);
    }

  }

  /**
   * @testName: testScopeInternal2
   *
   * @assertion_ids: EJB:SPEC:757.1
   *
   * @test_Strategy: Deploy two MDB's (part of a same JAR file) using the same
   *                 env-entry-name but declaring different values. Check that
   *                 we can lookup this env entry from both EJB's, that runtime
   *                 values are distinct, and that they correspond to the ones
   *                 in the DD.
   */
  public void testScopeInternal2() throws Fault {

    String operation = "checkEntry";
    int testNum = 2;

    try {
      qSender = session.createSender(mdbQ2);
      createTestMessage(operation, testNum);
      msg.setStringProperty("entryLookup", entryName);
      msg.setStringProperty("expectedValue", bean2Value_SameJar);
      qSender.send(msg);

      if (!checkOnResponse(operation)) {
        TestUtil.logErr("[Client] " + operation + " failed");
        throw new Exception(operation + " Failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("[Client] " + operation + " failed: ", e);
      throw new Fault(operation + " failed!", e);
    }

  }

  /**
   * @testName: testScopeExternal1
   *
   * @assertion_ids: EJB:SPEC:757.1
   *
   * @test_Strategy: Deploy two EJB's (in 2 distinct JAR files) using the same
   *                 env-entry-name but declaring different values. Check that
   *                 we can lookup this env entry from both EJB's, that runtime
   *                 values are distinct, and that they correspond to the ones
   *                 in the DD.
   */
  public void testScopeExternal1() throws Fault {

    String operation = "checkEntry";
    int testNum = 3;

    try {
      qSender = session.createSender(mdbQ3);
      createTestMessage(operation, testNum);
      msg.setStringProperty("entryLookup", entryName);
      msg.setStringProperty("expectedValue", bean1Value_MultiJar);
      qSender.send(msg);

      if (!checkOnResponse(operation)) {
        TestUtil.logErr("[Client] " + operation + " failed");
        throw new Exception(operation + " Failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] " + operation + " failed: ", e);
      throw new Fault(operation + " failed!", e);
    }
  }

  /**
   * @testName: testScopeExternal2
   *
   * @assertion_ids: EJB:SPEC:757.1
   *
   * @test_Strategy: Deploy two EJB's (in 2 distinct JAR files) using the same
   *                 env-entry-name but declaring different values. Check that
   *                 we can lookup this env entry from both EJB's, that runtime
   *                 values are distinct, and that they correspond to the ones
   *                 in the DD.
   */
  public void testScopeExternal2() throws Fault {

    String operation = "checkEntry";
    int testNum = 4;

    try {
      qSender = session.createSender(mdbQ4);
      createTestMessage(operation, testNum);
      msg.setStringProperty("entryLookup", entryName);
      msg.setStringProperty("expectedValue", bean2Value_MultiJar);
      qSender.send(msg);

      if (!checkOnResponse(operation)) {
        TestUtil.logErr("[Client] " + operation + " failed");
        throw new Exception(operation + " Failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("[Client] " + operation + " failed: ", e);
      throw new Fault(operation + " failed!", e);
    }
  }

}
