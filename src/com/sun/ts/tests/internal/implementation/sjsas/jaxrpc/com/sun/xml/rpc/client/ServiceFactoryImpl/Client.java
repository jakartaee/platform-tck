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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.ServiceFactoryImpl;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.client.*;
import com.sun.xml.rpc.util.localization.*;

public class Client extends EETest {
  private Properties props = null;

  private static String srcDir = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/client/ServiceFactoryImpl";

  private static final String NS_URI = "http://hellotestservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloTestService";

  private static final String ENDPOINT_URL = "http://localhost:8000/HS/hello";

  private static final String WSDL_FILE = "HelloTestService.wsdl";

  private String WSDL_URL;

  private static final Class SERVICE_CLASS = com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.ServiceFactoryImpl.HelloTestService.class;

  private static final String SERVICE_NAME_IMPL = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.ServiceFactoryImpl.HelloTestService_Impl";

  private static final QName SERVICE_QNAME = new QName(NS_URI, SERVICE_NAME);

  private ServiceFactoryImpl sf = new ServiceFactoryImpl();

  private URL wsdlURL;

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
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: createServiceTest1
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void createServiceTest1() throws Fault {
    TestUtil.logTrace("createServiceTest1");
    boolean pass = true;
    Service v;
    try {
      TestUtil.logMsg("Call createService(QName name) method");
      v = sf.createService(SERVICE_QNAME);
      TestUtil.logMsg("service=" + v);
    } catch (ServiceException e) {
      TestUtil.logMsg("service implementation class is not available ...");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createServiceTest1 failed", e);
    }

    if (!pass)
      throw new Fault("createServiceTest1 failed");
  }

  /*
   * @testName: createServiceTest2
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void createServiceTest2() throws Fault {
    TestUtil.logTrace("createServiceTest2");
    boolean pass = true;
    Service v;
    try {
      TestUtil.logMsg("Call createService(Class si, QName name) method");
      v = sf.createService(SERVICE_CLASS, SERVICE_QNAME);
      TestUtil.logMsg("service=" + v);
    } catch (ServiceException e) {
      TestUtil.logMsg("ServiceException ... " + e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createServiceTest2 failed", e);
    }

    if (!pass)
      throw new Fault("createServiceTest2 failed");
  }

  /*
   * @testName: createServiceTest3
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void createServiceTest3() throws Fault {
    TestUtil.logTrace("createServiceTest3");
    boolean pass = true;
    Service v;
    try {
      TestUtil.logMsg("Call createService(URL wsdl, QName name) method");
      v = sf.createService(wsdlURL, SERVICE_QNAME);
      TestUtil.logMsg("service=" + v);
    } catch (ServiceException e) {
      TestUtil.logMsg("ServiceException ... " + e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("createServiceTest3 failed", e);
    }

    if (!pass)
      throw new Fault("createServiceTest3 failed");
  }

  /*
   * @testName: loadServiceTest1
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void loadServiceTest1() throws Fault {
    TestUtil.logTrace("loadServiceTest1");
    boolean pass = true;
    Service v;
    try {
      TestUtil.logMsg("Call loadService(Class serviceInterface) method");
      v = sf.loadService(SERVICE_CLASS);
      TestUtil.logMsg("service=" + v);
    } catch (ServiceException e) {
      TestUtil.logMsg("ServiceException ... " + e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("loadServiceTest1 failed", e);
    }

    if (!pass)
      throw new Fault("loadServiceTest1 failed");
  }

  /*
   * @testName: loadServiceTest2
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void loadServiceTest2() throws Fault {
    TestUtil.logTrace("loadServiceTest2");
    boolean pass = true;
    Service v;
    Properties props = new Properties();
    props.setProperty("serviceImplementationName", SERVICE_NAME_IMPL);
    try {
      TestUtil.logMsg(
          "Call loadService(URL wsdl, Class serviceInterface, Properties props) method");
      v = sf.loadService(wsdlURL, SERVICE_CLASS, props);
      TestUtil.logMsg("service=" + v);
    } catch (ServiceException e) {
      TestUtil.logMsg("ServiceException ... " + e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("loadServiceTest2 failed", e);
    }

    if (!pass)
      throw new Fault("loadServiceTest2 failed");
  }

  /*
   * @testName: loadServiceTest3
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void loadServiceTest3() throws Fault {
    TestUtil.logTrace("loadServiceTest3");
    boolean pass = true;
    Service v;
    Properties props = new Properties();
    props.setProperty("serviceImplementationName", SERVICE_NAME_IMPL);
    try {
      TestUtil.logMsg(
          "Call loadService(URL wsdl, QName serviceName, Properties props) method");
      v = sf.loadService(wsdlURL, SERVICE_QNAME, props);
      TestUtil.logMsg("service=" + v);
    } catch (ServiceException e) {
      TestUtil.logMsg("ServiceException ... " + e);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("loadServiceTest3 failed", e);
    }

    if (!pass)
      throw new Fault("loadServiceTest3 failed");
  }
}
