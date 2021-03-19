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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.WebServicePermission;

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
   * @testName: WebServicePermissionConstructorTest1
   *
   * @assertion_ids: JAXWS:JAVADOC:76;
   *
   * @test_Strategy: Create instance via WebServicePermission(String)
   * constructor. Verify WebServicePermission object created successfully.
   */
  public void WebServicePermissionConstructorTest1() throws Fault {
    TestUtil.logTrace("WebServicePermissionConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via WebServicePermission(String) ...");
      WebServicePermission e = new WebServicePermission("thename");
      if (e != null) {
        TestUtil.logMsg("WebServicePermission object created successfully");
      } else {
        TestUtil.logErr("WebServicePermission object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServicePermissionConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("WebServicePermissionConstructorTest1 failed");
  }

  /*
   * @testName: WebServicePermissionConstructorTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:77;
   *
   * @test_Strategy: Create instance via WebServicePermission(String, String)
   * constructor. Verify WebServicePermission object created successfully.
   */
  public void WebServicePermissionConstructorTest2() throws Fault {
    TestUtil.logTrace("WebServicePermissionConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via WebServicePermission(String, String) ...");
      WebServicePermission e = new WebServicePermission("thename", null);
      if (e != null) {
        TestUtil.logMsg("WebServicePermission object created successfully");
      } else {
        TestUtil.logErr("WebServicePermission object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServicePermissionConstructorTest2 failed", e);
    }

    if (!pass)
      throw new Fault("WebServicePermissionConstructorTest2 failed");
  }

  /*
   * @testName: WebServicePermissionConstructorTest2a
   *
   * @assertion_ids: JAXWS:JAVADOC:77;
   *
   * @test_Strategy: Create instance via WebServicePermission(String, String)
   * constructor. Verify WebServicePermission object created successfully.
   */
  public void WebServicePermissionConstructorTest2a() throws Fault {
    TestUtil.logTrace("WebServicePermissionConstructorTest2a");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via WebServicePermission(String, String) ...");
      WebServicePermission e = new WebServicePermission("thename",
          "someaction");
      if (e != null) {
        TestUtil.logMsg("WebServicePermission object created successfully");
      } else {
        TestUtil.logErr("WebServicePermission object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("WebServicePermissionConstructorTest2a failed", e);
    }

    if (!pass)
      throw new Fault("WebServicePermissionConstructorTest2a failed");
  }

}
