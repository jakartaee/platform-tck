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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_handler.HandlerRegistry;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.jaxrpc.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.rpc.handler.*;
import javax.xml.rpc.handler.soap.*;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;

public class Client extends ServiceEETest {

  Hello port = null;

  Service svc = null;

  private void getStub() throws Exception {
    /* Lookup service then obtain port */
    try {
      InitialContext ic = new InitialContext();
      TestUtil.logMsg("Obtained InitialContext");
      TestUtil.logMsg("Lookup java:comp/env/service/handlerregistry");
      svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/handlerregistry");
      TestUtil.logMsg("Obtained service");
      port = (Hello) svc.getPort(Hello.class);
      TestUtil.logMsg("Obtained port");
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      getStub();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: GetHandlerRegistryTest
   *
   * @assertion_ids: WS4EE:SPEC:28
   *
   * @test_Strategy: Call the getHandlerRegistry() method. A container provider
   * must throw a java.lang.UnsupportedOperationException.
   */
  public void GetHandlerRegistryTest() throws Fault {
    TestUtil.logTrace("GetHandlerRegistryTest");
    boolean pass = false;
    try {
      TestUtil.logMsg("Get HandlerRegistry object for this Service");
      HandlerRegistry hr = svc.getHandlerRegistry();
      TestUtil.logErr("getHandlerRegistry() failed to throw exception.");
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught expected UnsupportedOperationException");
      pass = true;
    } catch (JAXRPCException e) {
      TestUtil.logErr("Caught unexpected JAXRPCException", e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetHandlerRegistryTest failed", e);
    }
    if (!pass)
      throw new Fault("GetHandlerRegistryTest failed");
  }
}
