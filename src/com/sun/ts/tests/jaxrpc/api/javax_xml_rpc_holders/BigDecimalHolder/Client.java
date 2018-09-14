/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_holders.BigDecimalHolder;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.holders.BigDecimalHolder;

import java.math.BigDecimal;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

  private final static BigDecimal myBigDecimal = new BigDecimal("3512359.1456");

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: BigDecimalHolderConstructorTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:216; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via BigDecimalHolder() constructor. Verify
   * BigDecimalHolder object created successfully.
   */
  public void BigDecimalHolderConstructorTest1() throws Fault {
    TestUtil.logTrace("BigDecimalHolderConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via BigDecimalHolder() ...");
      BigDecimalHolder n = new BigDecimalHolder();
      if (n != null) {
        TestUtil.logMsg("BigDecimalHolder object created successfully");
      } else {
        TestUtil.logErr("BigDecimalHolder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BigDecimalHolderConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("BigDecimalHolderConstructorTest1 failed");
  }

  /*
   * @testName: BigDecimalHolderConstructorTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:217; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via BigDecimalHolder(BigDecimal)
   * constructor. Verify BigDecimalHolder object created successfully.
   */
  public void BigDecimalHolderConstructorTest2() throws Fault {
    TestUtil.logTrace("BigDecimalHolderConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via BigDecimalHolder(BigDecimal) ...");
      BigDecimalHolder n = new BigDecimalHolder(myBigDecimal);
      if (n != null) {
        TestUtil.logMsg("BigDecimalHolder object created successfully");
      } else {
        TestUtil.logErr("BigDecimalHolder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BigDecimalHolderConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("BigDecimalHolderConstructorTest2 failed");
  }

  /*
   * @testName: getValueTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:215; WS4EE:SPEC:70;
   *
   * @test_Strategy: Test using both constructors. Verify value is set correct
   * in each case.
   */
  public void getValueTest() throws Fault {
    TestUtil.logTrace("getValueTest");
    boolean pass = true;

    if (!getValueTest1())
      pass = false;
    if (!getValueTest2())
      pass = false;

    if (!pass)
      throw new Fault("getValueTest failed");
  }

  /*
   * Create instance via BigDecimalHolder(). Verify value is set to default.
   */
  private boolean getValueTest1() throws Fault {
    TestUtil.logTrace("getValueTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via BigDecimalHolder() ...");
      BigDecimalHolder n = new BigDecimalHolder();
      if (n != null) {
        BigDecimal v = n.value;
        if (v == null)
          TestUtil.logMsg("Value set to null as expected");
        else {
          TestUtil.logErr("Value: expected - null" + ", received - " + v);
          pass = false;
        }
      } else {
        TestUtil.logErr("BigDecimalHolder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    return pass;

  }

  /*
   * Create instance via BigDecimalHolder(BigDecimal). Verify value is equal to
   * what was set.
   */
  private boolean getValueTest2() throws Fault {
    TestUtil.logTrace("getValueTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via BigDecimalHolder(BigDecimal) ...");
      BigDecimalHolder n = new BigDecimalHolder(myBigDecimal);
      if (n != null) {
        BigDecimal v = n.value;
        if (v.equals(myBigDecimal))
          TestUtil.logMsg("Value set as expected to " + myBigDecimal);
        else {
          TestUtil.logErr(
              "Value: expected - " + myBigDecimal + ", received - " + v);
          pass = false;
        }
      } else {
        TestUtil.logErr("BigDecimalHolder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    return pass;
  }
}
