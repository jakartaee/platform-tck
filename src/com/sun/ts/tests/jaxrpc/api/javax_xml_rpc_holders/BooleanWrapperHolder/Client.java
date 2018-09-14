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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_holders.BooleanWrapperHolder;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.math.*;

import javax.xml.rpc.holders.BooleanWrapperHolder;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

  private final static Boolean myBoolean1 = new Boolean(true);

  private final static Boolean myBoolean2 = new Boolean(false);

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
   * @testName: BooleanWrapperHolderConstructorTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:219; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via BooleanWrapperHolder() constructor.
   * Verify BooleanWrapperHolder object created successfully.
   */
  public void BooleanWrapperHolderConstructorTest1() throws Fault {
    TestUtil.logTrace("BooleanWrapperHolderConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via BooleanWrapperHolder() ...");
      BooleanWrapperHolder n = new BooleanWrapperHolder();
      if (n != null) {
        TestUtil.logMsg("BooleanWrapperHolder object created successfully");
      } else {
        TestUtil.logErr("BooleanWrapperHolder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BooleanWrapperHolderConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("BooleanWrapperHolderConstructorTest1 failed");
  }

  /*
   * @testName: BooleanWrapperHolderConstructorTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:220; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via BooleanWrapperHolder(Boolean)
   * constructor. Verify BooleanWrapperHolder object created successfully.
   */
  public void BooleanWrapperHolderConstructorTest2() throws Fault {
    TestUtil.logTrace("BooleanWrapperHolderConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via BooleanWrapperHolder(Boolean) ...");
      BooleanWrapperHolder n = new BooleanWrapperHolder(myBoolean1);
      if (n != null) {
        TestUtil.logMsg("BooleanWrapperHolder object created successfully");
      } else {
        TestUtil.logErr("BooleanWrapperHolder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("BooleanWrapperHolderConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("BooleanWrapperHolderConstructorTest2 failed");
  }

  /*
   * @testName: getValueTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:218; WS4EE:SPEC:70;
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
   * Create instance via BooleanWrapperHolder(). Verify value is set to default.
   */
  private boolean getValueTest1() throws Fault {
    TestUtil.logTrace("getValueTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via BooleanWrapperHolder() ...");
      BooleanWrapperHolder n = new BooleanWrapperHolder();
      if (n != null) {
        Boolean v = n.value;
        if (v == null)
          TestUtil.logMsg("Value set to null as expected");
        else {
          TestUtil.logErr("Value: expected - null" + ", received - " + v);
          pass = false;
        }
      } else {
        TestUtil.logErr("BooleanWrapperHolder object not created");
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
   * Create instance via BooleanWrapperHolder(Boolean). Verify value is equal to
   * what was set.
   */
  private boolean getValueTest2() throws Fault {
    TestUtil.logTrace("getValueTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via BooleanWrapperHolder(Boolean) ...");
      BooleanWrapperHolder n = new BooleanWrapperHolder(myBoolean1);
      if (n != null) {
        Boolean v = n.value;
        if (v.equals(myBoolean1))
          TestUtil.logMsg("Value set as expected to " + myBoolean1);
        else {
          TestUtil
              .logErr("Value: expected - " + myBoolean1 + ", received - " + v);
          pass = false;
        }
      } else {
        TestUtil.logErr("BooleanWrapperHolder object not created");
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
