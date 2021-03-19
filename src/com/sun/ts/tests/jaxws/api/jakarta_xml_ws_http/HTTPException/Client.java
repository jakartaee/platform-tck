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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_http.HTTPException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.http.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {

  private final static int MY_HTTP_STATUS_CODE = 100;

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
   * @testName: HTTPExceptionConstructorTest
   *
   * @assertion_ids: JAXWS:JAVADOC:105;
   *
   * @test_Strategy: Create instance via HTTPException(int statusCode)
   * constructor. Verify HTTPException object created successfully.
   */
  public void HTTPExceptionConstructorTest() throws Fault {
    TestUtil.logTrace("HTTPExceptionConstructorTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via HTTPException() ...");
      HTTPException e = new HTTPException(MY_HTTP_STATUS_CODE);
      if (e != null) {
        TestUtil.logMsg("HTTPException object created successfully");
      } else {
        TestUtil.logErr("HTTPException object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HTTPExceptionConstructorTest failed", e);
    }

    if (!pass)
      throw new Fault("HTTPExceptionConstructorTest failed");
  }

  /*
   * @testName: getStatusCodeTest
   *
   * @assertion_ids: JAXWS:JAVADOC:104;
   *
   * @test_Strategy: Create instance via HTTPException(int statusCode)
   * constructor. Verify HTTPException.getStatusCode() returns expected code.
   */
  public void getStatusCodeTest() throws Fault {
    TestUtil.logTrace("getStatusCodeTest");
    boolean pass = true;
    int code;
    try {
      TestUtil.logMsg("Create instance via HTTPException() ...");
      HTTPException e = new HTTPException(MY_HTTP_STATUS_CODE);
      if (e != null) {
        TestUtil.logMsg("HTTPException object created successfully");
      } else {
        TestUtil.logErr("HTTPException object not created");
        pass = false;
      }
      code = e.getStatusCode();
      if (code == MY_HTTP_STATUS_CODE)
        TestUtil.logMsg(
            "getStatusCode returned expected code " + MY_HTTP_STATUS_CODE);
      else {
        TestUtil.logErr("getStatusCode returned unexpected code, expected "
            + MY_HTTP_STATUS_CODE + ", received " + code);
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getStatusCodeTest failed", e);
    }

    if (!pass)
      throw new Fault("getStatusCodeTest failed");
  }
}
