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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.BasicService;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import javax.naming.*;

import javax.xml.rpc.*;
import javax.xml.rpc.encoding.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.client.*;
import com.sun.xml.rpc.naming.ServiceReferenceResolver;

public class Client extends EETest {
  private Properties props = null;

  private static final String NS_URI = "http://hellotestservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloTestService";

  private static final String PORT_NAME = "HelloPort";

  private static final String ENDPOINT_URL = "http://localhost:8000/BS/hello";

  private static final String WSDL_URL = "http://localhost:8000/BS/hello?WSDL";

  private static final Class SERVICE_CLASS = com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.BasicService.HelloTestService.class;

  private static final String SERVICE_NAME_IMPL = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.BasicService.HelloTestService_Impl";

  private static final QName SERVICE_QNAME = new QName(NS_URI, SERVICE_NAME);

  private static final QName PORT_QNAME = new QName(NS_URI, PORT_NAME);

  private static final QName[] PORTS = { new QName(NS_URI, PORT_NAME) };

  private ServiceFactoryImpl sf = new ServiceFactoryImpl();

  private URL wsdlURL;

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
    try {
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
   * @testName: basicServiceTest1
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void basicServiceTest1() throws Fault {
    TestUtil.logTrace("basicServiceTest1");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil.logMsg("Call BasicService(QName name) constructor");
      v = new BasicService(SERVICE_QNAME);
      TestUtil.logMsg("service=" + v);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("basicServiceTest1 failed", e);
    }

    if (!pass)
      throw new Fault("basicServiceTest1 failed");
  }

  /*
   * @testName: basicServiceTest2
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void basicServiceTest2() throws Fault {
    TestUtil.logTrace("basicServiceTest2");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil
          .logMsg("Call BasicService(QName name, QName[] ports) constructor");
      v = new BasicService(SERVICE_QNAME, PORTS);
      TestUtil.logMsg("service=" + v);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("basicServiceTest2 failed", e);
    }

    if (!pass)
      throw new Fault("basicServiceTest2 failed");
  }

  /*
   * @testName: basicServiceTest3
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void basicServiceTest3() throws Fault {
    TestUtil.logTrace("basicServiceTest3");
    boolean pass = true;
    ArrayList arrayPorts = new ArrayList();
    arrayPorts.add(new QName(NS_URI, PORT_NAME));
    Iterator iterator = arrayPorts.iterator();
    BasicService v;
    try {
      TestUtil
          .logMsg("Call BasicService(QName name, Iterator ports) constructor");
      v = new BasicService(SERVICE_QNAME, iterator);
      TestUtil.logMsg("service=" + v);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("basicServiceTest3 failed", e);
    }

    if (!pass)
      throw new Fault("basicServiceTest3 failed");
  }

  /*
   * @testName: basicServiceTest4
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void basicServiceTest4() throws Fault {
    TestUtil.logTrace("basicServiceTest4");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil.logMsg(
          "Call BasicService(QName name, TypeMappingRegistry registry) constructor");
      v = new BasicService(SERVICE_QNAME,
          BasicService.createStandardTypeMappingRegistry());
      TestUtil.logMsg("service=" + v);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("basicServiceTest4 failed", e);
    }

    if (!pass)
      throw new Fault("basicServiceTest4 failed");
  }

  /*
   * @testName: getCallsTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getCallsTest() throws Fault {
    TestUtil.logTrace("getCallsTest");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil
          .logMsg("Call BasicService(QName name, QName[] ports) constructor");
      v = new BasicService(SERVICE_QNAME, PORTS);
      TestUtil.logMsg("service=" + v);
      TestUtil.logMsg("Call BasicService.getCalls(QName port)");
      Call[] calls = v.getCalls(PORT_QNAME);
      TestUtil.logMsg("calls=" + calls.length);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
    }

    if (!pass)
      throw new Fault("getCallsTest failed");
  }

  /*
   * @testName: getPortsTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getPortsTest() throws Fault {
    TestUtil.logTrace("getPortsTest");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil
          .logMsg("Call BasicService(QName name, QName[] ports) constructor");
      v = new BasicService(SERVICE_QNAME, PORTS);
      TestUtil.logMsg("service=" + v);
      TestUtil.logMsg("Call BasicService.getPorts()");
      Iterator iterator = v.getPorts();
      TestUtil.logMsg("iterator=" + iterator);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
    }

    if (!pass)
      throw new Fault("getPortsTest failed");
  }

  /*
   * @testName: getWSDLDocumentLocationTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getWSDLDocumentLocationTest() throws Fault {
    TestUtil.logTrace("getWSDLDocumentLocationTest");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil
          .logMsg("Call BasicService(QName name, QName[] ports) constructor");
      v = new BasicService(SERVICE_QNAME, PORTS);
      TestUtil.logMsg("service=" + v);
      TestUtil.logMsg("Call BasicService.getWSDLDocumentLocation()");
      URL wsdlURL = v.getWSDLDocumentLocation();
      TestUtil.logMsg("wsdlURL=" + wsdlURL);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getWSDLDocumentLocationTest failed", e);
    }

    if (!pass)
      throw new Fault("getWSDLDocumentLocationTest failed");
  }

  /*
   * @testName: getTypeMappingRegistryTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getTypeMappingRegistryTest() throws Fault {
    TestUtil.logTrace("getTypeMappingRegistryTest");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil
          .logMsg("Call BasicService(QName name, QName[] ports) constructor");
      v = new BasicService(SERVICE_QNAME, PORTS);
      TestUtil.logMsg("service=" + v);
      TestUtil.logMsg("Call BasicService.getTypeMappingRegistry()");
      TypeMappingRegistry typeRegistry = v.getTypeMappingRegistry();
      TestUtil.logMsg("typeRegistry=" + typeRegistry);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getTypeMappingRegistryTest failed", e);
    }

    if (!pass)
      throw new Fault("getTypeMappingRegistryTest failed");
  }

  /*
   * @testName: getReferenceTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getReferenceTest() throws Fault {
    TestUtil.logTrace("getReferenceTest");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil
          .logMsg("Call BasicService(QName name, QName[] ports) constructor");
      v = new BasicService(SERVICE_QNAME, PORTS);
      TestUtil.logMsg("service=" + v);
      TestUtil.logMsg("Call BasicService.getReference()");
      Reference reference = v.getReference();
      TestUtil.logMsg("reference=" + reference);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getReferenceTest failed", e);
    }

    if (!pass)
      throw new Fault("getReferenceTest failed");
  }

  /*
   * @testName: getPortTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void getPortTest() throws Fault {
    TestUtil.logTrace("getPortTest");
    boolean pass = true;
    BasicService v;
    try {
      TestUtil
          .logMsg("Call BasicService(QName name, QName[] ports) constructor");
      v = new BasicService(SERVICE_QNAME, PORTS);
      TestUtil.logMsg("service=" + v);
      TestUtil.logMsg("Call BasicService.getPort(QName, Class)");
      Remote remote = v.getPort(PORT_QNAME, Hello.class);
      TestUtil.logMsg("remote=" + remote);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
    }

    if (!pass)
      throw new Fault("getPortTest failed");
  }
}
