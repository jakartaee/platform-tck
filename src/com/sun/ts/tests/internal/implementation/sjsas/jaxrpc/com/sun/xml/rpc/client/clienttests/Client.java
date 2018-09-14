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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.clienttests;

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
import com.sun.xml.rpc.client.*;
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
   * @testName: HandlerExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HandlerExceptionTest() throws Fault {
    TestUtil.logTrace("HandlerExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    HandlerException v;
    try {
      TestUtil.logMsg("Call HandlerException(String key) constructor");
      v = new HandlerException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil
          .logMsg("Call HandlerException(String key, String arg) constructor");
      v = new HandlerException(key, arg);
      TestUtil.logMsg(
          "Call HandlerException(String key, Object[] args) constructor");
      v = new HandlerException(key, args);
      TestUtil.logMsg(
          "Call HandlerException(String key, Localizable arg) constructor");
      v = new HandlerException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg("Call HandlerException(Localizable arg) constructor");
      v = new HandlerException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HandlerExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("HandlerExceptionTest failed");
  }

  /*
   * @testName: SenderExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void SenderExceptionTest() throws Fault {
    TestUtil.logTrace("SenderExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    SenderException v;
    try {
      TestUtil.logMsg("Call SenderException(String key) constructor");
      v = new SenderException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil
          .logMsg("Call SenderException(String key, String arg) constructor");
      v = new SenderException(key, arg);
      TestUtil.logMsg(
          "Call SenderException(String key, Object[] args) constructor");
      v = new SenderException(key, args);
      TestUtil.logMsg(
          "Call SenderException(String key, Localizable arg) constructor");
      v = new SenderException(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg("Call SenderException(Localizable arg) constructor");
      v = new SenderException(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SenderExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("SenderExceptionTest failed");
  }

  /*
   * @testName: ServiceExceptionImplTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ServiceExceptionImplTest() throws Fault {
    TestUtil.logTrace("ServiceExceptionImplTest");
    boolean pass = true;
    String bn = "english";
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    String s;
    Object a[];
    Throwable t;
    ServiceExceptionImpl v;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream saveErrStream = System.err;
    try {
      TestUtil.logMsg("Call ServiceExceptionImpl() constructor");
      v = new ServiceExceptionImpl();
      TestUtil.logMsg("Call ServiceExceptionImpl(String key) constructor");
      v = new ServiceExceptionImpl(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      s = v.getResourceBundleName();
      TestUtil.logMsg("Call getKey() method");
      s = v.getKey();
      TestUtil.logMsg("Call getArguments() method");
      a = v.getArguments();
      TestUtil.logMsg("Call toString() method");
      s = v.toString();
      TestUtil.logMsg("Call getMessage() method");
      s = v.getMessage();
      TestUtil.logMsg("Call getLinkedException() method");
      t = v.getLinkedException();
      System.setErr(new PrintStream(baos));
      TestUtil.logMsg("Call printStackTrace() method");
      v.printStackTrace();
      System.setErr(saveErrStream);
      TestUtil.logMsg("Call printStackTrace(PrintStream) method");
      v.printStackTrace(new PrintStream(baos));
      TestUtil.logMsg("Call printStackTrace(PrintWriter) method");
      v.printStackTrace(new PrintWriter(new PrintStream(baos)));
      TestUtil.logMsg(
          "Call ServiceExceptionImpl(String key, String arg) constructor");
      v = new ServiceExceptionImpl(key, arg);
      TestUtil.logMsg(
          "Call ServiceExceptionImpl(String key, Object[] args) constructor");
      v = new ServiceExceptionImpl(key, args);
      TestUtil.logMsg(
          "Call ServiceExceptionImpl(String key, Localizable arg) constructor");
      v = new ServiceExceptionImpl(key, new LocalizableMessage(bn, key));
      TestUtil.logMsg("Call ServiceExceptionImpl(Localizable arg) constructor");
      v = new ServiceExceptionImpl(new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("ServiceExceptionImplTest failed", e);
    }

    if (!pass)
      throw new Fault("ServiceExceptionImplTest failed");
  }

  /*
   * @testName: HandlerChainInfoImplTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void HandlerChainInfoImplTest() throws Fault {
    TestUtil.logTrace("HandlerChainInfoImplTest");
    boolean pass = true;
    HandlerInfo hi1 = new HandlerInfo(
        com.sun.ts.tests.jaxrpc.common.ClientHandler1.class, null, null);
    HandlerInfo hi2 = new HandlerInfo(
        com.sun.ts.tests.jaxrpc.common.ClientHandler2.class, null, null);
    ArrayList al = new ArrayList();
    al.add(hi1);
    al.add(hi2);
    HandlerChainInfoImpl v;
    try {
      TestUtil.logMsg("Call HandlerChainInfoImpl() constructor");
      v = new HandlerChainInfoImpl();
      TestUtil.logMsg("Call HandlerChainInfoImpl(List) constructor");
      v = new HandlerChainInfoImpl(al);
      TestUtil.logMsg("Call getHandlerList() method");
      List l = v.getHandlerList();
      TestUtil.logMsg("Call getHandlers() method");
      Iterator i = v.getHandlers();
      TestUtil.logMsg("Call getRoles() method");
      String roles[] = v.getRoles();
      TestUtil.logMsg("Call setRoles(String[] roles) method");
      v.setRoles(roles);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HandlerChainInfoImplTest failed", e);
    }

    if (!pass)
      throw new Fault("HandlerChainInfoImplTest failed");
  }
}
