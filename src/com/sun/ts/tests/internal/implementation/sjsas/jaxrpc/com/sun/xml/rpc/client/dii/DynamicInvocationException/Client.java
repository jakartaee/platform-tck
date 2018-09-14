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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.dii.DynamicInvocationException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.rpc.handler.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.client.dii.*;
import com.sun.xml.rpc.util.localization.*;

public class Client extends EETest {
  private Properties props = null;

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
   * @testName: DynamicInvocationExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void DynamicInvocationExceptionTest() throws Fault {
    TestUtil.logTrace("DynamicInvocationExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    DynamicInvocationException v;
    try {
      TestUtil
          .logMsg("Call DynamicInvocationException(String key) constructor");
      v = new DynamicInvocationException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call DynamicInvocationException(String key, String arg) constructor");
      v = new DynamicInvocationException(key, arg);
      TestUtil.logMsg(
          "Call DynamicInvocationException(String key, Object[] args) constructor");
      v = new DynamicInvocationException(key, args);
      TestUtil.logMsg(
          "Call DynamicInvocationException(String key, Localizable arg) constructor");
      v = new DynamicInvocationException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg(
          "Call DynamicInvocationException(Localizable arg) constructor");
      v = new DynamicInvocationException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("DynamicInvocationExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("DynamicInvocationExceptionTest failed");
  }
}
