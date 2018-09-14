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

package com.sun.ts.tests.ejb.ee.deploy.mdb.enventry.single;

import java.util.Properties;
import javax.jms.Queue;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.Status;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  private Queue stringQ;

  private Queue boolQ;

  private Queue byteQ;

  private Queue shortQ;

  private Queue intQ;

  private Queue longQ;

  private Queue floatQ;

  private Queue doubleQ;

  private Queue allQ;

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

      TestUtil.logTrace("[Client] Looking up Destinations...");

      allQ = (Queue) context.lookup("java:comp/env/jms/MDBAll");
      stringQ = (Queue) context.lookup("java:comp/env/jms/MDBString");
      boolQ = (Queue) context.lookup("java:comp/env/jms/MDBBoolean");
      byteQ = (Queue) context.lookup("java:comp/env/jms/MDBByte");
      shortQ = (Queue) context.lookup("java:comp/env/jms/MDBShort");
      intQ = (Queue) context.lookup("java:comp/env/jms/MDBInteger");
      longQ = (Queue) context.lookup("java:comp/env/jms/MDBLong");
      floatQ = (Queue) context.lookup("java:comp/env/jms/MDBFloat");
      doubleQ = (Queue) context.lookup("java:comp/env/jms/MDBDouble");

    } catch (Exception e) {
      TestUtil.logErr("[Client] Setup failed! ", e);
      throw new Fault("Setup Failed!", e);
    }
  }

  /**
   * @testName: testString
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven Queue Bean whose DD
   *                 declares a String environment entry. Lookup this entry and
   *                 check that its runtime value match the DD value.
   *
   */
  public void testString() throws Fault {

    String testCase = "testString";
    int testNum = 1;

    try {
      qSender = session.createSender(stringQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
   * @testName: testBoolean
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven queue Bean whose DD
   *                 declares a Boolean environment entry. Lookup this entry and
   *                 check that its runtime value match the DD value.
   *
   */
  public void testBoolean() throws Fault {

    String testCase = "testBoolean";
    int testNum = 2;

    try {
      qSender = session.createSender(boolQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
   * @testName: testByte
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven Queue Bean whose DD
   *                 declares a Byte environment entry. Lookup this entry and
   *                 check that its runtime value match the DD value.
   *
   */
  public void testByte() throws Fault {

    String testCase = "testByte";
    int testNum = 3;

    try {
      qSender = session.createSender(byteQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
   * @testName: testShort
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven Bean whose DD declares a
   *                 Short environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testShort() throws Fault {

    String testCase = "testShort";
    int testNum = 4;

    try {
      qSender = session.createSender(shortQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
   * @testName: testInteger
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven queue Bean whose DD
   *                 declares a Integer environment entry. Lookup this entry and
   *                 check that its runtime value match the DD value.
   *
   */
  public void testInteger() throws Fault {

    String testCase = "testInteger";
    int testNum = 5;

    try {
      qSender = session.createSender(intQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
   * @testName: testLong
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven Bean whose DD declares a
   *                 Long environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testLong() throws Fault {

    String testCase = "testLong";
    int testNum = 6;

    try {
      qSender = session.createSender(longQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
   * @testName: testFloat
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven Bean whose DD declares a
   *                 Float environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testFloat() throws Fault {

    String testCase = "testFloat";
    int testNum = 7;

    try {
      qSender = session.createSender(floatQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
   * @testName: testDouble
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven Bean whose DD declares a
   *                 Double environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testDouble() throws Fault {

    String testCase = "testDouble";
    int testNum = 8;

    try {
      qSender = session.createSender(doubleQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
   * @testName: testAll
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a message-driven queue Bean whose DD
   *                 declares an environment entry of each type. Lookup these
   *                 entries and check that their runtime value match their DD
   *                 value.
   *
   */
  public void testAll() throws Fault {

    String testCase = "testAll";
    int testNum = 9;

    try {
      qSender = session.createSender(allQ);
      createTestMessage(testCase, testNum);
      qSender.send(msg);

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
