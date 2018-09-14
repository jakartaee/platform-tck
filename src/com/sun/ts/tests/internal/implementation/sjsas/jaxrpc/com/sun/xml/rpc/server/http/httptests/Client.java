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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.server.http.httptests;

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
import com.sun.xml.rpc.server.http.*;
import com.sun.xml.rpc.util.localization.*;
import com.sun.xml.rpc.tools.wscompile.*;
import com.sun.xml.rpc.tools.wscompile.CompileTool;
import com.sun.xml.rpc.processor.generator.GeneratorException;

public class Client extends EETest {
  private Properties props = null;

  private final static String FS = System.getProperty("file.separator");

  private final static String CLASSES = "classes";

  private static final ClassLoader CLASSLOADER = Client.class.getClassLoader();

  private static final String BASEDIR = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/server/http/httptests/";

  private static final String TEST_SAMPLE_RT1 = "rt01.xml";

  private static final String TEST_SAMPLE_RT2 = "rt02.xml";

  private static final String TEST_SAMPLE_RT3 = "rt03.xml";

  private static final String TEST_SAMPLE_RT4 = "rt04.xml";

  // Common definitions for Endpoints
  private final static int NUM_ENDPOINTS = 2;

  private static final String WSDL_NAME = "/WEB-INF/HelloTestService.wsdl";

  private static final String SERVICE_STR_NAME = "{http://hellotestservice.org/wsdl}HelloTestService";

  private static final String MODEL_NAME = "hello.model.gz";

  private static final String NAMESPACE = "http://hellotestservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloTestService";

  private static final QName SERVICE_QNAME = new QName(NAMESPACE, SERVICE_NAME);

  private static final Exception EXCEPTION = new Exception("hello");

  // Endpoint 1 definitions
  private static final String ENDPOINT_NAME1 = "HelloTest1";

  private static final String INTERFACE_NAME1 = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.server.http.httptests.HelloTest1";

  private static final String IMPL_NAME1 = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.server.http.httptests.HelloTest1Impl";

  private static final String TIE_NAME1 = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.server.http.httptests.HelloTest1_Tie";

  private static final String PORT_STR_NAME1 = "{http://hellotestservice.org/wsdl}HelloTest1Port";

  private static final String URL_NAME1 = "/jaxrpc/HelloTest1";

  private static final String PORT_NAME1 = "HelloTest1Port";

  private static final QName PORT_QNAME1 = new QName(NAMESPACE, PORT_NAME1);

  // Endpoint 2 definitions
  private static final String ENDPOINT_NAME2 = "HelloTest2";

  private static final String INTERFACE_NAME2 = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.server.http.httptests.HelloTest2";

  private static final String IMPL_NAME2 = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.server.http.httptests.HelloTest2Impl";

  private static final String TIE_NAME2 = "com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.server.http.httptests.HelloTest2_Tie";

  private static final String PORT_STR_NAME2 = "{http://hellotestservice.org/wsdl}HelloTest2Port";

  private static final String URL_NAME2 = "/jaxrpc/HelloTest2";

  private static final String PORT_NAME2 = "HelloTest2Port";

  private static final QName PORT_QNAME2 = new QName(NAMESPACE, PORT_NAME2);

  private String tsHome;

  private String baseDir;

  private String classDir;

  private void assertIt(String s, boolean b) {
    if (!b) {
      TestUtil.logMsg("FAILED ... " + s);
      fail++;
    } else {
      TestUtil.logMsg("PASSED ... " + s);
    }
  }

  private int fail;

  private void fail(String s) {
    TestUtil.logErr(s);
    fail++;
  }

  private JAXRPCRuntimeInfo parse(InputStream is) {
    JAXRPCRuntimeInfoParser parser = new JAXRPCRuntimeInfoParser(CLASSLOADER);
    return parser.parse(is);
  }

  private void parseAndFail(InputStream is) {
    JAXRPCRuntimeInfoParser parser = new JAXRPCRuntimeInfoParser(CLASSLOADER);
    try {
      parser.parse(is);
      fail("should-have-failed");
    } catch (Exception e) {
      TestUtil.logMsg("PASSED - got parse exception as expected: " + e);
    }
  }

  private boolean invokeWsCompileServer(String features) {
    TestUtil.logMsg("Invoke wscompile ...");
    return (new CompileTool(System.out, "wscompile")).run(new String[] {
        "-classpath", classDir, "-d", classDir, "-gen:server", "-f:" + features,
        "-Xprintstacktrace", baseDir + FS + "config.xml" });
  }

  private JAXRPCRuntimeInfo testSampleRT1() throws Exception {
    TestUtil.logMsg("Parsing runtime file: " + TEST_SAMPLE_RT1);
    String fileName = baseDir + TEST_SAMPLE_RT1;
    return parse(new FileInputStream(fileName));
  }

  private void testSampleRT2() throws Exception {
    TestUtil.logMsg("Parsing runtime file: " + TEST_SAMPLE_RT2);
    String fileName = baseDir + TEST_SAMPLE_RT2;
    parseAndFail(new FileInputStream(fileName));
  }

  private void testSampleRT3() throws Exception {
    TestUtil.logMsg("Parsing runtime file: " + TEST_SAMPLE_RT3);
    String fileName = baseDir + TEST_SAMPLE_RT3;
    parseAndFail(new FileInputStream(fileName));
  }

  private void testSampleRT4() throws Exception {
    TestUtil.logMsg("Parsing runtime file: " + TEST_SAMPLE_RT4);
    String fileName = baseDir + TEST_SAMPLE_RT4;
    parseAndFail(new FileInputStream(fileName));
  }

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
      tsHome = p.getProperty("ts_home").replaceAll("/", FS);
      logMsg("tsHome=" + tsHome);
      baseDir = tsHome + FS + BASEDIR.replaceAll("/", FS);
      logMsg("baseDir=" + baseDir);
      classDir = tsHome + FS + CLASSES;
      logMsg("classDir=" + classDir);
      fail = 0;
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: JAXRPCServletExceptionTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void JAXRPCServletExceptionTest() throws Fault {
    TestUtil.logTrace("JAXRPCServletExceptionTest");
    boolean pass = true;
    String bn = "english";
    String rb;
    String key = "My Key";
    String arg = "My Arg";
    Object args[] = { "My Arg1", "My Arg2" };
    JAXRPCServletException v;
    try {
      TestUtil.logMsg("Call JAXRPCServletException(String key) constructor");
      v = new JAXRPCServletException(key);
      TestUtil.logMsg("Call getResourceBundleName() method");
      rb = v.getResourceBundleName();
      TestUtil.logMsg(
          "Call JAXRPCServletException(String key, String arg) constructor");
      v = new JAXRPCServletException(key, arg);
      TestUtil.logMsg(
          "Call JAXRPCServletException(String key, Object[] args) constructor");
      v = new JAXRPCServletException(key, args);
      TestUtil.logMsg(
          "Call JAXRPCServletException(String key, Localizable arg) constructor");
      v = new JAXRPCServletException(key, new LocalizableMessage(bn, key));
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("JAXRPCServletExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("JAXRPCServletExceptionTest failed");
  }

  /*
   * @testName: rttest1
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void rttest1() throws Fault {
    TestUtil.logTrace("rttest1()");
    boolean pass = true;
    try {
      invokeWsCompileServer("strict");
      JAXRPCRuntimeInfo jrti = testSampleRT1();
      TestUtil.logMsg("get Endpoints");
      List list = jrti.getEndpoints();
      TestUtil.logMsg("#endpoints=" + list.size());
      assertIt("#Endpoints", list.size() == NUM_ENDPOINTS);
      TestUtil.logMsg("set Endpoints");
      jrti.setEndpoints(list);
      list = jrti.getEndpoints();
      assertIt("#Endpoints", list.size() == NUM_ENDPOINTS);
      Iterator iter = list.iterator();
      TestUtil.logMsg("Traverse RuntimeEndpointInfo ...");
      while (iter.hasNext()) {
        RuntimeEndpointInfo rei = (RuntimeEndpointInfo) iter.next();
        if (rei.getName().equals(ENDPOINT_NAME1)) {
          assertIt("RemoteInterface", rei.getRemoteInterface()
              .isAssignableFrom(Class.forName(INTERFACE_NAME1)));
          assertIt("Implemenation", rei.getImplementationClass()
              .isAssignableFrom(Class.forName(IMPL_NAME1)));
          assertIt("Tie",
              rei.getTieClass().isAssignableFrom(Class.forName(TIE_NAME1)));
          TestUtil.logMsg("Endpoint Name=" + rei.getName());
          assertIt("EndpointName", rei.getName().equals(ENDPOINT_NAME1));
          TestUtil.logMsg("WSDL File Name=" + rei.getWSDLFileName());
          assertIt("WSDLFileName", rei.getWSDLFileName().equals(WSDL_NAME));
          TestUtil.logMsg("Model File Name=" + rei.getModelFileName());
          assertIt("ModelFileName", rei.getModelFileName().equals(MODEL_NAME));
          TestUtil.logMsg("URL Pattern=" + rei.getUrlPattern());
          assertIt("URLPattern", rei.getUrlPattern().equals(URL_NAME1));
          TestUtil.logMsg("portName=" + rei.getPortName());
          assertIt("PortName", rei.getPortName().equals(PORT_QNAME1));
          TestUtil.logMsg("serviceName=" + rei.getServiceName());
          assertIt("ServiceName", rei.getServiceName().equals(SERVICE_QNAME));
        } else if (rei.getName().equals(ENDPOINT_NAME2)) {
          assertIt("RemoteInterface", rei.getRemoteInterface()
              .isAssignableFrom(Class.forName(INTERFACE_NAME2)));
          assertIt("Implemenation", rei.getImplementationClass()
              .isAssignableFrom(Class.forName(IMPL_NAME2)));
          assertIt("Tie",
              rei.getTieClass().isAssignableFrom(Class.forName(TIE_NAME2)));
          TestUtil.logMsg("Endpoint Name=" + rei.getName());
          assertIt("EndpointName", rei.getName().equals(ENDPOINT_NAME2));
          TestUtil.logMsg("WSDL File Name=" + rei.getWSDLFileName());
          assertIt("WSDLFileName", rei.getWSDLFileName().equals(WSDL_NAME));
          TestUtil.logMsg("Model File Name=" + rei.getModelFileName());
          assertIt("ModelFileName", rei.getModelFileName().equals(MODEL_NAME));
          TestUtil.logMsg("URL Pattern=" + rei.getUrlPattern());
          assertIt("URLPattern", rei.getUrlPattern().equals(URL_NAME2));
          TestUtil.logMsg("portName=" + rei.getPortName());
          assertIt("PortName", rei.getPortName().equals(PORT_QNAME2));
          TestUtil.logMsg("serviceName=" + rei.getServiceName());
          assertIt("ServiceName", rei.getServiceName().equals(SERVICE_QNAME));
        }
      }
    } catch (Exception e) {
      throw new Fault("rttest1() failed", e);
    }

    if (fail > 0)
      pass = false;

    if (!pass)
      throw new Fault("rttest1() failed");
  }

  /*
   * @testName: rttest2
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void rttest2() throws Fault {
    TestUtil.logTrace("rttest2()");
    boolean pass = true;
    try {
      invokeWsCompileServer("strict");
      TestUtil.logMsg("Create RuntimeEndpointInfo ...");
      RuntimeEndpointInfo rei = new RuntimeEndpointInfo();
      rei.setRemoteInterface(Class.forName(INTERFACE_NAME1));
      rei.setImplementationClass(Class.forName(IMPL_NAME1));
      rei.setTieClass(Class.forName(TIE_NAME1));
      rei.setName(ENDPOINT_NAME1);
      rei.setUrlPattern(URL_NAME1);
      rei.setWSDLFileName(WSDL_NAME);
      rei.setModelFileName(MODEL_NAME);
      rei.setPortName(PORT_QNAME1);
      rei.setServiceName(SERVICE_QNAME);
      rei.setException(EXCEPTION);
      rei.setDeployed(false);
      assertIt("RemoteInterface", rei.getRemoteInterface()
          .isAssignableFrom(Class.forName(INTERFACE_NAME1)));
      assertIt("Implemenation", rei.getImplementationClass()
          .isAssignableFrom(Class.forName(IMPL_NAME1)));
      assertIt("Tie",
          rei.getTieClass().isAssignableFrom(Class.forName(TIE_NAME1)));
      TestUtil.logMsg("Endpoint Name=" + rei.getName());
      assertIt("EndpointName", rei.getName().equals(ENDPOINT_NAME1));
      TestUtil.logMsg("WSDL File Name=" + rei.getWSDLFileName());
      assertIt("WSDLFileName", rei.getWSDLFileName().equals(WSDL_NAME));
      TestUtil.logMsg("Model File Name=" + rei.getModelFileName());
      assertIt("ModelFileName", rei.getModelFileName().equals(MODEL_NAME));
      TestUtil.logMsg("URL Pattern=" + rei.getUrlPattern());
      assertIt("URLPattern", rei.getUrlPattern().equals(URL_NAME1));
      TestUtil.logMsg("portName=" + rei.getPortName());
      assertIt("PortName", rei.getPortName().equals(PORT_QNAME1));
      TestUtil.logMsg("serviceName=" + rei.getServiceName());
      assertIt("ServiceName", rei.getServiceName().equals(SERVICE_QNAME));
      TestUtil.logMsg("isDeployed=" + rei.isDeployed());
      assertIt("isDeployed", !rei.isDeployed());
      TestUtil.logMsg("Exceptionn=" + rei.getException());
      assertIt("Exception", rei.getException().equals(EXCEPTION));
    } catch (Exception e) {
      throw new Fault("rttest2() failed", e);
    }

    if (fail > 0)
      pass = false;

    if (!pass)
      throw new Fault("rttest2() failed");
  }

  /*
   * @testName: negrttests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void negrttests() throws Fault {
    TestUtil.logTrace("negrttests()");
    boolean pass = true;
    try {
      invokeWsCompileServer("strict");
      testSampleRT2();
      testSampleRT3();
      testSampleRT4();
    } catch (Exception e) {
      throw new Fault("negrttests() failed", e);
    }

    if (fail > 0)
      pass = false;

    if (!pass)
      throw new Fault("negrttests() failed");
  }
}
