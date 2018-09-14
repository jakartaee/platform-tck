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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.tools.wsdeploy.deployment_test;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.namespace.QName;
import javax.xml.rpc.handler.*;

import com.sun.javatest.Status;

// Internal implementation specific classes to tes
import com.sun.xml.rpc.tools.wsdeploy.*;
import com.sun.xml.rpc.processor.config.HandlerChainInfo;
import com.sun.xml.rpc.processor.config.HandlerInfo;

public class Client extends EETest {
  private final static String FS = System.getProperty("file.separator");

  private String baseDir;

  private String tempDir;

  private static final String BASEDIR = "src/com/sun/ts/tests/internal/implementation/sjsas/jaxrpc/com/sun/xml/rpc/tools/wsdeploy/deployment_test/";

  private static final String TEST_SAMPLE_APP_1 = "hello.war";

  private static final String TEST_SAMPLE_APP_2 = "hello_ws_app.war";

  private static final String TEST_SAMPLE_APP_3 = "nameSpaceMapping.war";

  private static final String TEST_SAMPLE_APP_4 = "testJaxrpcRIXMLValues.war";

  private static final String TEST_SAMPLE_DD1 = "dd01.xml";

  private static final String TEST_SAMPLE_DD2 = "dd02.xml";

  private static final String TEST_SAMPLE_DD3 = "dd03.xml";

  private static final String TEST_SAMPLE_DD4 = "dd04.xml";

  private int fail;

  private void fail(String s) {
    TestUtil.logErr(s);
    fail++;
  }

  public void testSampleApp1() throws Exception {
    TestUtil
        .logMsg("Invoking wsdeploy to deploy app -> : " + TEST_SAMPLE_APP_1);
    deploy(baseDir + TEST_SAMPLE_APP_1);
  }

  public void testSampleApp2() throws Exception {
    TestUtil
        .logMsg("Invoking wsdeploy to deploy app -> : " + TEST_SAMPLE_APP_2);
    deploy(baseDir + TEST_SAMPLE_APP_2);
  }

  public void testSampleApp3() throws Exception {
    TestUtil
        .logMsg("Invoking wsdeploy to deploy app -> : " + TEST_SAMPLE_APP_3);
    deploy(baseDir + TEST_SAMPLE_APP_3);
  }

  public void testSampleApp4() throws Exception {
    TestUtil
        .logMsg("Invoking wsdeploy to deploy app -> : " + TEST_SAMPLE_APP_4);
    deploy(baseDir + TEST_SAMPLE_APP_4);
  }

  protected void deploy(String filename) throws Exception {
    DeployTool tool = new DeployTool(System.out, "wsdeploy");
    File tmpfile = File.createTempFile("jaxrpc-test", "war", new File(tempDir));
    String[] args = new String[4];
    args[0] = filename;
    args[1] = "-o";
    args[2] = tmpfile.getAbsolutePath();
    args[3] = "-verbose";
    assertTrue(tool.run(args));
    tmpfile.delete();
  }

  private void assertTrue(boolean b) {
    if (b)
      TestUtil.logMsg("PASSED ...");
    else {
      TestUtil.logMsg("FAILED ...");
      fail++;
    }
  }

  private void assertEquals(String s1, String s2) {
    if (s1.equals(s2))
      TestUtil.logMsg("PASSED ...");
    else {
      TestUtil.logMsg("FAILED ...");
      fail++;
    }
  }

  private void assertEquals(int s1, int s2) {
    if (s1 == s2)
      TestUtil.logMsg("PASSED ...");
    else {
      TestUtil.logMsg("FAILED ...");
      fail++;
    }
  }

  public void testSampleDD1() throws FileNotFoundException {
    TestUtil.logMsg("Parsing WebServicesInfo from DD -> : " + TEST_SAMPLE_DD1);
    WebServicesInfo webInfo = parse(
        new FileInputStream(baseDir + TEST_SAMPLE_DD1));
    TestUtil.logMsg("Validating WebServicesInfo settings after parse ...");
    assertTrue(webInfo != null);
    assertEquals("http://com.acme/wsdl", webInfo.getTargetNamespaceBase());
    assertEquals("http://com.acme/types", webInfo.getTypeNamespaceBase());
    assertEquals("/ws", webInfo.getUrlPatternBase());
    Map map = webInfo.getEndpoints();
    assertEquals(4, map.size());
    EndpointInfo endpointInfo = (EndpointInfo) map.get("Hello");
    assertTrue(endpointInfo != null);
    assertEquals("Hello", endpointInfo.getName());
    assertEquals("Hello Service", endpointInfo.getDisplayName());
    assertEquals("A simple web service", endpointInfo.getDescription());
    assertEquals("com.acme.hello.Hello", endpointInfo.getInterface());
    assertEquals("com.acme.hello.HelloImpl", endpointInfo.getImplementation());
    assertTrue(endpointInfo.getModel() == null);
    endpointInfo = (EndpointInfo) map.get("Interop");
    assertTrue(endpointInfo != null);
    assertEquals("interop.xml", endpointInfo.getModel());
    endpointInfo = (EndpointInfo) map.get("Hello2");
    assertTrue(endpointInfo != null);
    assertTrue(endpointInfo.getClientHandlerChainInfo() == null);
    HandlerChainInfo chainInfo = endpointInfo.getServerHandlerChainInfo();
    assertTrue(chainInfo != null);
    Iterator iter = chainInfo.getHandlers();
    assertTrue(iter.hasNext());
    HandlerInfo handlerInfo = (HandlerInfo) iter.next();
    assertEquals("com.acme.MyHandler", handlerInfo.getHandlerClassName());
    Set headerNames = handlerInfo.getHeaderNames();
    assertEquals(2, headerNames.size());
    assertTrue(headerNames.contains(new QName("urn:foo", "foo")));
    assertTrue(headerNames.contains(new QName("urn:bar", "bar")));
    assertTrue(iter.hasNext());
    handlerInfo = (HandlerInfo) iter.next();
    assertEquals("com.acme.AnotherHandler", handlerInfo.getHandlerClassName());
    assertTrue(handlerInfo.getHeaderNames().isEmpty());
    assertEquals(2, handlerInfo.getProperties().size());
    assertTrue(!iter.hasNext());
    Set roles = chainInfo.getRoles();
    assertTrue(roles.isEmpty());
    assertEquals(2, chainInfo.getHandlersCount());
    map = webInfo.getEndpointMappings();
    assertEquals(3, map.size());
    EndpointMappingInfo endpointMappingInfo = (EndpointMappingInfo) map
        .get("Hello");
    assertEquals("Hello", endpointMappingInfo.getName());
    assertEquals("/hello", endpointMappingInfo.getUrlPattern());
  }

  public void testSampleDD2() throws FileNotFoundException {
    TestUtil.logMsg("Parsing WebServicesInfo from DD -> : " + TEST_SAMPLE_DD2);
    parseAndFail(new FileInputStream(baseDir + TEST_SAMPLE_DD2));
  }

  public void testSampleDD3() throws FileNotFoundException {
    TestUtil.logMsg("Parsing WebServicesInfo from DD -> : " + TEST_SAMPLE_DD3);
    parseAndFail(new FileInputStream(baseDir + TEST_SAMPLE_DD3));
  }

  public void testSampleDD4() throws FileNotFoundException {
    TestUtil.logMsg("Parsing WebServicesInfo from DD -> : " + TEST_SAMPLE_DD4);
    parseAndFail(new FileInputStream(baseDir + TEST_SAMPLE_DD4));
  }

  protected WebServicesInfo parse(InputStream is) {
    DeploymentDescriptorParser parser = new DeploymentDescriptorParser();
    return parser.parse(is);
  }

  protected void parseAndFail(InputStream is) {
    DeploymentDescriptorParser parser = new DeploymentDescriptorParser();
    try {
      parser.parse(is);
      fail("should-have-failed");
    } catch (DeploymentException e) {
      TestUtil.logMsg("PASSED - got exception as expected: " + e);
    }
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
    String tsHome;
    try {
      tsHome = p.getProperty("ts_home").replaceAll("/", FS);
      logMsg("tsHome=" + tsHome);
      baseDir = tsHome + FS + BASEDIR.replaceAll("/", FS);
      logMsg("baseDir=" + baseDir);
      tempDir = baseDir + "tmp";
      logMsg("tempDir=" + tempDir);
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
   * @testName: wsdeploytests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void wsdeploytests() throws Fault {
    TestUtil.logTrace("wsdeploytests()");
    boolean pass = true;
    try {
      testSampleApp1();
      testSampleApp2();
      testSampleApp3();
      testSampleApp4();
    } catch (Exception e) {
      throw new Fault("wsdeploytests() failed", e);
    }

    if (fail > 0)
      pass = false;

    if (!pass)
      throw new Fault("wsdeploytests() failed");
  }

  /*
   * @testName: ddtests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void ddtests() throws Fault {
    TestUtil.logTrace("ddtests()");
    boolean pass = true;
    try {
      // Postive DD tests
      testSampleDD1();
    } catch (Exception e) {
      throw new Fault("ddtests() failed", e);
    }

    if (fail > 0)
      pass = false;

    if (!pass)
      throw new Fault("ddtests() failed");
  }

  /*
   * @testName: negddtests
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void negddtests() throws Fault {
    TestUtil.logTrace("negddtests()");
    boolean pass = true;
    try {
      // Negative DD tests
      testSampleDD2();
      testSampleDD3();
      testSampleDD4();
    } catch (Exception e) {
      throw new Fault("negddtests() failed", e);
    }

    if (fail > 0)
      pass = false;

    if (!pass)
      throw new Fault("negddtests() failed");
  }
}
