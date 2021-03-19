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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.WebServiceException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
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
   * @testName: WebServiceExceptionConstructorTest1
   *
   * @assertion_ids: JAXWS:JAVADOC:72;
   *
   * @test_Strategy: Create instance via WebServiceException() constructor.
   * Verify WebServiceException object created successfully.
   */
  public void WebServiceExceptionConstructorTest1() throws Fault {
    TestUtil.logTrace("WebServiceExceptionConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via WebServiceException() ...");
      WebServiceException e = new WebServiceException();
      if (e != null) {
        TestUtil.logMsg("WebServiceException object created successfully");
      } else {
        TestUtil.logErr("WebServiceException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServiceExceptionConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("WebServiceExceptionConstructorTest1 failed");
  }

  /*
   * @testName: WebServiceExceptionConstructorTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:74;
   *
   * @test_Strategy: Create instance via WebServiceException(String, Throwable).
   * Verify WebServiceException object created successfully.
   */
  public void WebServiceExceptionConstructorTest2() throws Fault {
    TestUtil.logTrace("WebServiceExceptionConstructorTest2");
    boolean pass = true;
    String detailMsg = "a detail message";
    Exception foo = new Exception("foo");
    try {
      TestUtil.logMsg("Create instance via "
          + " WebServiceException(String, Throwable) ...");
      WebServiceException e = new WebServiceException(detailMsg, foo);
      if (e != null) {
        TestUtil.logMsg("WebServiceException object created successfully");
        String msg = e.getMessage();
        if (msg.equals(detailMsg))
          TestUtil.logMsg("detail message match: " + detailMsg);
        else {
          TestUtil.logErr("detail message mismatch - expected: " + detailMsg
              + ", received: " + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("WebServiceException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServiceExceptionConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("WebServiceExceptionConstructorTest2 failed");
  }

  /*
   * @testName: WebServiceExceptionConstructorTest3
   *
   * @assertion_ids: JAXWS:JAVADOC:73;
   *
   * @test_Strategy: Create instance via WebServiceException(String). Verify
   * WebServiceException object created successfully.
   */
  public void WebServiceExceptionConstructorTest3() throws Fault {
    TestUtil.logTrace("WebServiceExceptionConstructorTest3");
    boolean pass = true;
    String detailMsg = "a detail message";
    try {
      TestUtil
          .logMsg("Create instance via " + " WebServiceException(String) ...");
      WebServiceException e = new WebServiceException(detailMsg);
      if (e != null) {
        TestUtil.logMsg("WebServiceException object created successfully");
        String msg = e.getMessage();
        if (msg.equals(detailMsg))
          TestUtil.logMsg("detail message match: " + detailMsg);
        else {
          TestUtil.logErr("detail message mismatch - expected: " + detailMsg
              + ", received: " + msg);
          pass = false;
        }
      } else {
        TestUtil.logErr("WebServiceException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServiceExceptionConstructorTest3 failed", e);
    }

    if (!pass)
      throw new Fault("WebServiceExceptionConstructorTest3 failed");
  }

  /*
   * @testName: WebServiceExceptionConstructorTest4
   *
   * @assertion_ids: JAXWS:JAVADOC:75;
   *
   * @test_Strategy: Create instance via WebServiceException(Throwable). Verify
   * WebServiceException object created successfully.
   */
  public void WebServiceExceptionConstructorTest4() throws Fault {
    TestUtil.logTrace("WebServiceExceptionConstructorTest4");
    boolean pass = true;
    Exception foo = new Exception("foo");
    try {
      TestUtil.logMsg(
          "Create instance via " + " WebServiceException(Throwable) ...");
      WebServiceException e = new WebServiceException(foo);
      if (e != null) {
        TestUtil.logMsg("WebServiceException object created successfully");
      } else {
        TestUtil.logErr("WebServiceException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServiceExceptionConstructorTest4 failed", e);
    }

    if (!pass)
      throw new Fault("WebServiceExceptionConstructorTest4 failed");
  }

}
