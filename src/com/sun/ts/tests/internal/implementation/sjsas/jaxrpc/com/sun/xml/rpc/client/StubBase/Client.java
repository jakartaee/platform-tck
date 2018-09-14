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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.StubBase;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.lang.reflect.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.client.*;
import com.sun.xml.rpc.util.localization.*;

public class Client extends EETest {
  private Properties props = null;

  private static String srcDir = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/client/StubBase";

  private static final String NS_URI = "http://hellotestservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloTestService";

  private static final String PORT_NAME = "HelloPort";

  private static final String WSDL_FILE = "HelloTestService.wsdl";

  private String WSDL_URL;

  private static final QName SERVICE_QNAME = new QName(NS_URI, SERVICE_NAME);

  private static final QName PORT_QNAME = new QName(NS_URI, PORT_NAME);

  private ServiceFactoryImpl sf = new ServiceFactoryImpl();

  private URL wsdlURL;

  private Service service;

  private Hello hello;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: ts_home;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      String tsHome = p.getProperty("ts_home");
      WSDL_URL = "file:" + tsHome + "/" + srcDir + "/HelloTestService.wsdl";
      logMsg("WSDL_URL=" + WSDL_URL);
      wsdlURL = new URL(WSDL_URL);
      TestUtil.logMsg("Call createService(URL wsdl, QName name) method");
      service = sf.createService(wsdlURL, SERVICE_QNAME);
      TestUtil.logMsg("service=" + service);
      hello = (Hello) service.getPort(PORT_QNAME, Hello.class);
      TestUtil.logMsg("stub=" + hello);
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void _setTransportFactory(javax.xml.rpc.Stub o,
      java.io.OutputStream s) throws Exception {
    TestUtil.logMsg("_setTransportFactory");
    Class c = o.getClass();
    try {
      Object testArgs[] = {
          new com.sun.xml.rpc.client.http.HttpClientTransportFactory(s) };
      Method methods[] = c.getMethods();
      Method m = null;
      for (int i = 0; i < methods.length; i++) {
        if (methods[i].getName().equals("_setTransportFactory")) {
          m = methods[i];
          break;
        }
      }
      if (m != null)
        m.invoke(o, testArgs);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      e.printStackTrace(System.out);
    }
  }

  private void _getHandlerChain(javax.xml.rpc.Stub o) throws Exception {
    TestUtil.logMsg("_getHandlerChain");
    Class c = o.getClass();
    try {
      Object testArgs[] = null;
      Method methods[] = c.getMethods();
      Method m = null;
      for (int i = 0; i < methods.length; i++) {
        if (methods[i].getName().equals("_getHandlerChain")) {
          m = methods[i];
          break;
        }
      }
      if (m != null)
        m.invoke(o, testArgs);
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      e.printStackTrace(System.out);
    }
  }

  /*
   * @testName: setTransportFactory
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void setTransportFactory() throws Fault {
    TestUtil.logTrace("setTransportFactory");
    boolean pass = true;
    try {
      _setTransportFactory((Stub) hello, System.out);
      TestUtil.logMsg("Success invoking _setTransportFactory method");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("setTransportFactory failed", e);
    }

    if (!pass)
      throw new Fault("setTransportFactory failed");
  }

  /*
   * @testName: getHandlerChain
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getHandlerChain() throws Fault {
    TestUtil.logTrace("getHandlerChain");
    boolean pass = true;
    try {
      _getHandlerChain((Stub) hello);
      TestUtil.logMsg("Success invoking _getHandlerChain method");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getHandlerChain failed", e);
    }

    if (!pass)
      throw new Fault("getHandlerChain failed");
  }
}
