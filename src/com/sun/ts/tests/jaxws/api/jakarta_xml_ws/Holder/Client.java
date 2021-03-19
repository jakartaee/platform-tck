/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Holder;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.math.*;

import jakarta.xml.ws.Holder;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {

  private final static Byte myByte = Byte.valueOf(Byte.MAX_VALUE);

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: HolderConstructorTest1
   *
   * @assertion_ids: JAXWS:JAVADOC:28;
   *
   * @test_Strategy: Create instance via Holder() constructor. Verify Holder
   * object created successfully.
   */
  public void HolderConstructorTest1() throws Fault {
    TestUtil.logTrace("HolderConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via Holder() ...");
      Holder n = new Holder();
      if (n != null) {
        TestUtil.logMsg("Holder object created successfully");
      } else {
        TestUtil.logErr("Holder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HolderConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("HolderConstructorTest1 failed");
  }

  /*
   * @testName: HolderConstructorTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:29;
   *
   * @test_Strategy: Create instance via Holder(byte) constructor. Verify Holder
   * object created successfully.
   */
  public void HolderConstructorTest2() throws Fault {
    TestUtil.logTrace("HolderConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via Holder(byte) ...");
      Holder n = new Holder(myByte);
      if (n != null) {
        TestUtil.logMsg("Holder object created successfully");
      } else {
        TestUtil.logErr("Holder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HolderConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("HolderConstructorTest2 failed");
  }

  /*
   * @testName: getValueTest
   *
   * @assertion_ids: JAXWS:JAVADOC:28; JAXWS:JAVADOC:29;
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
   * Create instance via Holder(). Verify value is set to default.
   */
  private boolean getValueTest1() throws Fault {
    TestUtil.logTrace("getValueTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via Holder() ...");
      Holder n = new Holder();
      if (n != null) {
        if (n.value == null)
          TestUtil.logMsg("value set as expected to null");
        else {
          TestUtil.logMsg("value set unexpected to non-null");
          pass = false;
        }
      } else {
        TestUtil.logErr("Holder object not created");
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
   * Create instance via Holder(byte). Verify value is set to default.
   */
  private boolean getValueTest2() throws Fault {
    TestUtil.logTrace("getValueTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via Holder(byte) ...");
      Holder n = new Holder(myByte);
      if (n != null) {
        Byte v = (Byte) n.value;
        if (myByte.equals(v))
          TestUtil.logMsg("value set as expected: " + myByte);
        else {
          TestUtil.logErr("value: expected - " + myByte + ", received - " + v);
          pass = false;
        }
      } else {
        TestUtil.logErr("Holder object not created");
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
