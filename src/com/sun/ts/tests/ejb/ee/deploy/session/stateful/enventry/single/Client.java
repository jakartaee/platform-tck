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

package com.sun.ts.tests.ejb.ee.deploy.session.stateful.enventry.single;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.javatest.Status;

public class Client extends EETest {

  /*
   * Bean names for JNDI lookups
   */
  private static final String envPrefix = "java:comp/env/";

  private static final String charBeanName = envPrefix + "ejb/CharBean";

  private static final String stringBeanName = envPrefix + "ejb/StringBean";

  private static final String boolBeanName = envPrefix + "ejb/BoolBean";

  private static final String byteBeanName = envPrefix + "ejb/ByteBean";

  private static final String shortBeanName = envPrefix + "ejb/ShortBean";

  private static final String intBeanName = envPrefix + "ejb/IntBean";

  private static final String longBeanName = envPrefix + "ejb/LongBean";

  private static final String floatBeanName = envPrefix + "ejb/FloatBean";

  private static final String doubleBeanName = envPrefix + "ejb/DoubleBean";

  private static final String allBeanName = envPrefix + "ejb/AllBean";

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      nctx = new TSNamingContext();
      logMsg("Client: Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testCharacter
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a Character environment entry. Lookup this entry and check
   *                 that its runtime value match the DD value.
   *
   */
  public void testCharacter() throws Fault {

    CharBeanHome home = null;
    CharBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + charBeanName + "'...");
      home = (CharBeanHome) nctx.lookup(charBeanName, CharBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);

      logTrace("Client: Calling bean...");
      pass = bean.testCharacterEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("Character env entry test failed");
      }
    } catch (Exception e) {
      throw new Fault("Character env entry test failed: ", e);
    }
  }

  /**
   * @testName: testString
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a String environment entry. Lookup this entry and check
   *                 that its runtime value match the DD value.
   *
   */
  public void testString() throws Fault {

    StringBeanHome home = null;
    StringBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + stringBeanName + "'...");
      home = (StringBeanHome) nctx.lookup(stringBeanName, StringBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");
      pass = bean.testStringEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("String env entry test failed ");
      }
    } catch (Exception e) {
      throw new Fault("String env entry test failed: ", e);
    }
  }

  /**
   * @testName: testBoolean
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a Boolean environment entry. Lookup this entry and check
   *                 that its runtime value match the DD value.
   *
   */
  public void testBoolean() throws Fault {

    BooleanBeanHome home = null;
    BooleanBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + boolBeanName + "'...");
      home = (BooleanBeanHome) nctx.lookup(boolBeanName, BooleanBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");
      pass = bean.testBooleanEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("Boolean env entry test failed ");
      }
    } catch (Exception e) {
      throw new Fault("Boolean env entry test failed: ", e);
    }
  }

  /**
   * @testName: testByte
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a Byte environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testByte() throws Fault {

    ByteBeanHome home = null;
    ByteBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + byteBeanName + "'...");
      home = (ByteBeanHome) nctx.lookup(byteBeanName, ByteBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");
      pass = bean.testByteEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("Byte env entry test failed ");
      }
    } catch (Exception e) {
      throw new Fault("Byte env entry test failed: ", e);
    }
  }

  /**
   * @testName: testShort
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a Short environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testShort() throws Fault {

    ShortBeanHome home = null;
    ShortBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + shortBeanName + "'...");
      home = (ShortBeanHome) nctx.lookup(shortBeanName, ShortBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");
      pass = bean.testShortEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("Short env entry test failed ");
      }
    } catch (Exception e) {
      throw new Fault("Short env entry test failed: ", e);
    }
  }

  /**
   * @testName: testInteger
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a Integer environment entry. Lookup this entry and check
   *                 that its runtime value match the DD value.
   *
   */
  public void testInteger() throws Fault {

    IntegerBeanHome home = null;
    IntegerBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + intBeanName + "'...");
      home = (IntegerBeanHome) nctx.lookup(intBeanName, IntegerBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");
      pass = bean.testIntegerEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("Integer env entry test failed ");
      }
    } catch (Exception e) {
      throw new Fault("Integer env entry test failed: ", e);
    }
  }

  /**
   * @testName: testLong
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a Long environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testLong() throws Fault {

    LongBeanHome home = null;
    LongBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + longBeanName + "'...");
      home = (LongBeanHome) nctx.lookup(longBeanName, LongBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");
      pass = bean.testLongEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("Long env entry test failed ");
      }
    } catch (Exception e) {
      throw new Fault("Long env entry test failed: ", e);
    }
  }

  /**
   * @testName: testFloat
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a Float environment entry. Lookup this entry and check that
   *                 its runtime value match the DD value.
   *
   */
  public void testFloat() throws Fault {

    FloatBeanHome home = null;
    FloatBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + floatBeanName + "'...");
      home = (FloatBeanHome) nctx.lookup(floatBeanName, FloatBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");
      pass = bean.testFloatEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("Float env entry test failed ");
      }
    } catch (Exception e) {
      throw new Fault("Float env entry test failed: ", e);
    }
  }

  /**
   * @testName: testDouble
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 a Double environment entry. Lookup this entry and check
   *                 that its runtime value match the DD value.
   *
   */
  public void testDouble() throws Fault {

    DoubleBeanHome home = null;
    DoubleBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + doubleBeanName + "'...");
      home = (DoubleBeanHome) nctx.lookup(doubleBeanName, DoubleBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");
      pass = bean.testDoubleEntry();
      logTrace("Client: Removing bean...");
      bean.remove();

      if (!pass) {
        throw new Fault("Double env entry test failed ");
      }
    } catch (Exception e) {
      throw new Fault("Double env entry test failed: ", e);
    }
  }

  /**
   * @testName: testAll
   *
   * @assertion_ids: EJB:SPEC:762
   *
   * @test_Strategy: Deploy and create a Stateful Session Bean whose DD declares
   *                 an environment entry of each type. Lookup these entries and
   *                 check that their runtime value match their DD value.
   *
   */
  public void testAll() throws Fault {

    AllBeanHome home = null;
    AllBean bean = null;
    boolean pass = true;

    try {
      logTrace("Client: looking up '" + allBeanName + "'...");
      home = (AllBeanHome) nctx.lookup(allBeanName, AllBeanHome.class);
      logTrace("Client: creating bean instance...");
      bean = home.create(props);
      logTrace("Client: Calling bean...");

      pass = bean.testCharacterEntry();
      if (!pass) {
        throw new Fault("env entry test failed [Character]");
      }

      pass = bean.testStringEntry();
      if (!pass) {
        throw new Fault("env entry test failed [String]");
      }

      pass = bean.testBooleanEntry();
      if (!pass) {
        throw new Fault("env entry test failed [Boolean]");
      }

      pass = bean.testByteEntry();
      if (!pass) {
        throw new Fault("env entry test failed [Byte]");
      }

      pass = bean.testShortEntry();
      if (!pass) {
        throw new Fault("env entry test failed [Short]");
      }

      pass = bean.testIntegerEntry();
      if (!pass) {
        throw new Fault("env entry test failed [Integer]");
      }

      pass = bean.testLongEntry();
      if (!pass) {
        throw new Fault("env entry test failed [Long]");
      }

      pass = bean.testFloatEntry();
      if (!pass) {
        throw new Fault("env entry test failed [Float]");
      }

      pass = bean.testDoubleEntry();
      if (!pass) {
        throw new Fault("env entry test failed [Double]");
      }

    } catch (Exception e) {
      throw new Fault("env entry test [all types] failed: ", e);
    } finally {
      logTrace("Client: Removing bean...");
      try {
        if (null != bean) {
          bean.remove();
        }
      } catch (Exception e) {
        throw new Fault("Exception while removing bean: ", e);
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("Client: cleanup");
  }

}
