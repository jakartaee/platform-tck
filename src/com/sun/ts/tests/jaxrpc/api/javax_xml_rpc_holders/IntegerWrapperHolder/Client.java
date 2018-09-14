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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_holders.IntegerWrapperHolder;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.math.*;

import javax.xml.rpc.holders.IntegerWrapperHolder;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

  private final static Integer myInteger1 = new Integer(Integer.MIN_VALUE);

  private final static Integer myInteger2 = new Integer(Integer.MAX_VALUE);

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
   * @testName: IntegerWrapperHolderConstructorTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:168; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via IntegerWrapperHolder() constructor.
   * Verify IntegerWrapperHolder object created successfully.
   */
  public void IntegerWrapperHolderConstructorTest1() throws Fault {
    TestUtil.logTrace("IntegerWrapperHolderConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via IntegerWrapperHolder() ...");
      IntegerWrapperHolder n = new IntegerWrapperHolder();
      if (n != null) {
        TestUtil.logMsg("IntegerWrapperHolder object created successfully");
      } else {
        TestUtil.logErr("IntegerWrapperHolder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("IntegerWrapperHolderConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("IntegerWrapperHolderConstructorTest1 failed");
  }

  /*
   * @testName: IntegerWrapperHolderConstructorTest2
   *
   * @assertion_ids: JAXRPC:JAVADOC:169; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via IntegerWrapperHolder(Integer)
   * constructor. Verify IntegerWrapperHolder object created successfully.
   */
  public void IntegerWrapperHolderConstructorTest2() throws Fault {
    TestUtil.logTrace("IntegerWrapperHolderConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via IntegerWrapperHolder(Integer) ...");
      IntegerWrapperHolder n = new IntegerWrapperHolder(myInteger1);
      if (n != null) {
        TestUtil.logMsg("IntegerWrapperHolder object created successfully");
      } else {
        TestUtil.logErr("IntegerWrapperHolder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("IntegerWrapperHolderConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("IntegerWrapperHolderConstructorTest2 failed");
  }

  /*
   * @testName: getValueTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:167; WS4EE:SPEC:70;
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
   * Create instance via IntegerWrapperHolder(). Verify value is set to default.
   */
  private boolean getValueTest1() throws Fault {
    TestUtil.logTrace("getValueTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via IntegerWrapperHolder() ...");
      IntegerWrapperHolder n = new IntegerWrapperHolder();
      if (n != null) {
        Integer v = n.value;
        if (v == null)
          TestUtil.logMsg("Value set to null as expected");
        else {
          TestUtil.logErr("Value: expected - null" + ", received - " + v);
          pass = false;
        }
      } else {
        TestUtil.logErr("IntegerWrapperHolder object not created");
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
   * Create instance via IntegerWrapperHolder(Integer). Verify value is equal to
   * what was set.
   */
  private boolean getValueTest2() throws Fault {
    TestUtil.logTrace("getValueTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via IntegerWrapperHolder(Integer) ...");
      IntegerWrapperHolder n = new IntegerWrapperHolder(myInteger1);
      if (n != null) {
        Integer v = n.value;
        if (v.equals(myInteger1))
          TestUtil.logMsg("Value set as expected to " + myInteger1);
        else {
          TestUtil
              .logErr("Value: expected - " + myInteger1 + ", received - " + v);
          pass = false;
        }
      } else {
        TestUtil.logErr("IntegerWrapperHolder object not created");
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
