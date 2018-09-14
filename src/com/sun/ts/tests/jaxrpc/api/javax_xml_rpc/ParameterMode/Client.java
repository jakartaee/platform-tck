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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc.ParameterMode;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

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
   * @testName: ToStringTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:18; JAXRPC:JAVADOC:19; JAXRPC:JAVADOC:20;
   * JAXRPC:JAVADOC:21; WS4EE:SPEC:70
   *
   * @test_Strategy: Verify ParameterMode.toString() method.
   */
  public void ToStringTest() throws Fault {
    TestUtil.logTrace("ToStringTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Verify toString method");
      TestUtil.logMsg(
          "ParameterMode.IN.toString() = " + ParameterMode.IN.toString());
      TestUtil.logMsg(
          "ParameterMode.OUT.toString() = " + ParameterMode.OUT.toString());
      TestUtil.logMsg(
          "ParameterMode.INOUT.toString() = " + ParameterMode.INOUT.toString());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ToStringTest failed", e);
    }

    if (!pass)
      throw new Fault("ToStringTest failed");
  }
}
