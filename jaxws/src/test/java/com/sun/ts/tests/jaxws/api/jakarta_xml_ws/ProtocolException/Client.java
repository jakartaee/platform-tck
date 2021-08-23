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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.ProtocolException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
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
   * @testName: ProtocolExceptionConstructorTest1
   *
   * @assertion_ids: JAXWS:JAVADOC:34;
   *
   * @test_Strategy: Create instance via ProtocolException() constructor. Verify
   * ProtocolException object created successfully.
   */
  public void ProtocolExceptionConstructorTest1() throws Fault {
    TestUtil.logTrace("ProtocolExceptionConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via ProtocolException() ...");
      ProtocolException e = new ProtocolException();
      if (e != null) {
        TestUtil.logMsg("ProtocolException object created successfully");
      } else {
        TestUtil.logErr("ProtocolException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ProtocolExceptionConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("ProtocolExceptionConstructorTest1 failed");
  }

  /*
   * @testName: ProtocolExceptionConstructorTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:36;
   *
   * @test_Strategy: Create instance via ProtocolException(String, Throwable).
   * Verify ProtocolException object created successfully.
   */
  public void ProtocolExceptionConstructorTest2() throws Fault {
    TestUtil.logTrace("ProtocolExceptionConstructorTest2");
    boolean pass = true;
    String detailMsg = "a detail message";
    Exception foo = new Exception("foo");
    try {
      TestUtil.logMsg(
          "Create instance via " + " ProtocolException(String, Throwable) ...");
      ProtocolException e = new ProtocolException(detailMsg, foo);
      if (e != null) {
        TestUtil.logMsg("ProtocolException object created successfully");
        String msg = e.getMessage();
        if (msg.equals(detailMsg))
          TestUtil.logMsg("detail message match: " + detailMsg);
        else {
          TestUtil.logErr("detail message mismatch - expected: " + detailMsg
              + ", received: " + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("ProtocolException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ProtocolExceptionConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("ProtocolExceptionConstructorTest2 failed");
  }

  /*
   * @testName: ProtocolExceptionConstructorTest3
   *
   * @assertion_ids: JAXWS:JAVADOC:35;
   *
   * @test_Strategy: Create instance via ProtocolException(String). Verify
   * ProtocolException object created successfully.
   */
  public void ProtocolExceptionConstructorTest3() throws Fault {
    TestUtil.logTrace("ProtocolExceptionConstructorTest3");
    boolean pass = true;
    String detailMsg = "a detail message";
    try {
      TestUtil
          .logMsg("Create instance via " + " ProtocolException(String) ...");
      ProtocolException e = new ProtocolException(detailMsg);
      if (e != null) {
        TestUtil.logMsg("ProtocolException object created successfully");
        String msg = e.getMessage();
        if (msg.equals(detailMsg))
          TestUtil.logMsg("detail message match: " + detailMsg);
        else {
          TestUtil.logErr("detail message mismatch - expected: " + detailMsg
              + ", received: " + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("ProtocolException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ProtocolExceptionConstructorTest3 failed", e);
    }

    if (!pass)
      throw new Fault("ProtocolExceptionConstructorTest3 failed");
  }

  /*
   * @testName: ProtocolExceptionConstructorTest4
   *
   * @assertion_ids: JAXWS:JAVADOC:37;
   *
   * @test_Strategy: Create instance via ProtocolException(Throwable). Verify
   * ProtocolException object created successfully.
   */
  public void ProtocolExceptionConstructorTest4() throws Fault {
    TestUtil.logTrace("ProtocolExceptionConstructorTest4");
    boolean pass = true;
    Exception foo = new Exception("foo");
    try {
      TestUtil
          .logMsg("Create instance via " + " ProtocolException(Throwable) ...");
      ProtocolException e = new ProtocolException(foo);
      if (e != null) {
        TestUtil.logMsg("ProtocolException object created successfully");
      } else {
        TestUtil.logErr("ProtocolException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ProtocolExceptionConstructorTest4 failed", e);
    }

    if (!pass)
      throw new Fault("ProtocolExceptionConstructorTest4 failed");
  }
}
