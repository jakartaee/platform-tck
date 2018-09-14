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

package com.sun.ts.tests.ejb.ee.deploy.mdb.enventry.singleT;

import java.util.Properties;
import javax.jms.Topic;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.Status;

public class Client extends com.sun.ts.tests.jms.commonee.Client {

  private Topic stringT;

  private Topic boolT;

  private Topic byteT;

  private Topic shortT;

  private Topic intT;

  private Topic longT;

  private Topic floatT;

  private Topic doubleT;

  private Topic allT;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: jms_timeout; user; password;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      super.setup(args, props);

      TestUtil.logTrace("[Client] Looking up Destinations...");

      stringT = (Topic) context.lookup("java:comp/env/jms/MDBString");
      boolT = (Topic) context.lookup("java:comp/env/jms/MDBBoolean");
      byteT = (Topic) context.lookup("java:comp/env/jms/MDBByte");
      shortT = (Topic) context.lookup("java:comp/env/jms/MDBShort");
      intT = (Topic) context.lookup("java:comp/env/jms/MDBInteger");
      longT = (Topic) context.lookup("java:comp/env/jms/MDBLong");
      floatT = (Topic) context.lookup("java:comp/env/jms/MDBFloat");
      doubleT = (Topic) context.lookup("java:comp/env/jms/MDBDouble");
      allT = (Topic) context.lookup("java:comp/env/jms/MDBAll");

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
   * @test_Strategy: Deploy and create a MDB Topic Bean whose DD declares a
   *                 String environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testString() throws Fault {

    String testCase = "testString";
    int testNum = 1;

    try {
      tPub = tSession.createPublisher(stringT);
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
   * @testName: testBoolean
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a MDB Topic Bean whose DD declares a
   *                 Boolean environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testBoolean() throws Fault {

    String testCase = "testBoolean";
    int testNum = 2;

    try {
      tPub = tSession.createPublisher(boolT);
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
   * @testName: testByte
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a MDB Topic Bean whose DD declares a Byte
   *                 environment entry. Lookup this entry and check that its
   *                 runtime value match the DD value.
   *
   */
  public void testByte() throws Fault {

    String testCase = "testByte";
    int testNum = 3;

    try {
      tPub = tSession.createPublisher(byteT);
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
   * @testName: testShort
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a MDB Topic Bean whose DD declares a
   *                 Short environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testShort() throws Fault {

    String testCase = "testShort";
    int testNum = 4;

    try {
      tPub = tSession.createPublisher(shortT);
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
   * @testName: testInteger
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a MDB Topic Bean whose DD declares a
   *                 Integer environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testInteger() throws Fault {

    String testCase = "testInteger";
    int testNum = 5;

    try {
      tPub = tSession.createPublisher(intT);
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
   * @testName: testLong
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a MDB Topic Bean whose DD declares a Long
   *                 environment entry. Lookup this entry and check that its
   *                 runtime value match the DD value.
   *
   */
  public void testLong() throws Fault {

    String testCase = "testLong";
    int testNum = 6;

    try {
      tPub = tSession.createPublisher(longT);
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
   * @testName: testFloat
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a MDB Topic Bean whose DD declares a
   *                 Float environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testFloat() throws Fault {

    String testCase = "testFloat";
    int testNum = 7;

    try {
      tPub = tSession.createPublisher(floatT);
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
   * @testName: testDouble
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a MDB Topic Bean whose DD declares a
   *                 Double environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testDouble() throws Fault {

    String testCase = "testDouble";
    int testNum = 8;

    try {
      tPub = tSession.createPublisher(doubleT);
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
   * @testName: testAll
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a MDB Topic bean whose DD declares an
   *                 environment entry of each type. Lookup these entries and
   *                 check that their runtime value match their DD value.
   *
   */
  public void testAll() throws Fault {

    String testCase = "testAll";
    int testNum = 9;

    try {
      tPub = tSession.createPublisher(allT);
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
