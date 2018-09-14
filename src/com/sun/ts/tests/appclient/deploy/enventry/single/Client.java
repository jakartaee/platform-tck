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
 * @(#)Client.java	1.15 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.enventry.single;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.shared.enventry.single.TestCode;
import com.sun.javatest.Status;

public class Client extends EETest {

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      nctx = new TSNamingContext();
      logMsg("[Client] Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Fault("Setup failed: ", e);
    }
  }

  /**
   * @testName: testString
   *
   * @assertion_ids: JavaEE:SPEC:103.1
   *
   * @test_Strategy: Deploy and create an application client whose DD declares a
   *                 String environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testString() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testStringEntry(nctx);
      if (!pass) {
        throw new Fault("String env entry test failed!");
      }
    } catch (Exception e) {
      throw new Fault("String env entry test failed: " + e, e);
    }
  }

  /**
   * @testName: testBoolean
   *
   * @assertion_ids: JavaEE:SPEC:103.7
   *
   * @test_Strategy: Deploy and create an application client whose DD declares a
   *                 Boolean environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testBoolean() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testBooleanEntry(nctx);
      if (!pass) {
        throw new Fault("Boolean env entry test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Boolean env entry test failed: " + e, e);
    }
  }

  /**
   * @testName: testByte
   *
   * @assertion_ids: JavaEE:SPEC:103.3
   *
   * @test_Strategy: Deploy and create an application client whose DD declares a
   *                 Byte environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testByte() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testByteEntry(nctx);
      if (!pass) {
        throw new Fault("Byte env entry test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Byte env entry test failed: " + e, e);
    }
  }

  /**
   * @testName: testShort
   *
   * @assertion_ids: JavaEE:SPEC:103.4
   *
   * @test_Strategy: Deploy and create an application client whose DD declares a
   *                 Short environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testShort() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testShortEntry(nctx);
      if (!pass) {
        throw new Fault("Short env entry test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Short env entry test failed: " + e, e);
    }
  }

  /**
   * @testName: testInteger
   *
   * @assertion_ids: JavaEE:SPEC:103.5
   *
   * @test_Strategy: Deploy and create an application client whose DD declares a
   *                 Integer environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testInteger() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testIntegerEntry(nctx);
      if (!pass) {
        throw new Fault("Integer env entry test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Integer env entry test failed: " + e, e);
    }
  }

  /**
   * @testName: testLong
   *
   * @assertion_ids: JavaEE:SPEC:103.8
   *
   * @test_Strategy: Deploy and create an application client whose DD declares a
   *                 Long environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testLong() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testLongEntry(nctx);
      if (!pass) {
        throw new Fault("Long env entry test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Long env entry test failed: " + e, e);
    }
  }

  /**
   * @testName: testFloat
   *
   * @assertion_ids: JavaEE:SPEC:103.9
   *
   * @test_Strategy: Deploy and create an application client whose DD declares a
   *                 Float environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testFloat() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testFloatEntry(nctx);
      if (!pass) {
        throw new Fault("Float env entry test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Float env entry test failed: " + e, e);
    }
  }

  /**
   * @testName: testDouble
   *
   * @assertion_ids: JavaEE:SPEC:103.6
   *
   * @test_Strategy: Deploy and create an application client whose DD declares a
   *                 Double environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testDouble() throws Fault {
    boolean pass;
    Double value;

    try {
      pass = TestCode.testDoubleEntry(nctx);
      if (!pass) {
        throw new Fault("Double env entry test failed!");
      }
    } catch (Exception e) {
      throw new Fault("Double env entry test failed: ", e);
    }
  }

  /**
   * @testName: testAll
   *
   * @assertion_ids: JavaEE:SPEC:103
   *
   * @test_Strategy: Deploy and create an application client whose DD declares
   *                 an environment entry of each type. Lookup these entries and
   *                 check that their runtime value match their DD value.
   *
   */
  public void testAll() throws Fault {
    try {
      logTrace("[Client] testAll() : starting...");
      testString();
      testBoolean();
      testByte();
      testShort();
      testInteger();
      testLong();
      testFloat();
      testDouble();
      logTrace("[Client] testAll() : done!");
    } catch (Exception e) {
      throw new Fault("env entry test [all types] failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }
}
