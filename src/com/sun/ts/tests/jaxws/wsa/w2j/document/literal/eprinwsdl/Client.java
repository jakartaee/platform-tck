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
 * $Id: Client.java 52501 2007-01-24 02:29:49Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.eprinwsdl;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

import java.awt.Image;
import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.SOAPBinding;
import jakarta.xml.ws.soap.AddressingFeature;
import javax.xml.namespace.QName;

import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Element;

import com.sun.ts.tests.jaxws.wsa.common.EprUtil;

import java.util.Properties;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.w2j.document.literal.eprinwsdl.";

  // service and port information
  private static final String NAMESPACEURI = "http://eprinwsdltestservice.org/wsdl";

  private static final String SERVICE_NAME = "EPRInWsdlTestService";

  private static final String PORT_NAME1 = "Test1Port";

  private static final String PORT_TYPE_NAME1 = "Test1";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private QName PORT_TYPE_QNAME1 = new QName(NAMESPACEURI, PORT_TYPE_NAME1);

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL1 = "eprinwsdl.endpoint.1";

  private static final String WSDLLOC_URL = "eprinwsdl.wsdlloc.1";

  private String url1 = null;

  private URL wsdlurl = null;

  private Test1 port1 = null;

  private WebServiceFeature[] addressingenabled = {
      new AddressingFeature(true) };

  static EPRInWsdlTestService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL1);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL1: " + url1);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    service = (EPRInWsdlTestService) JAXWS_Util.getService(wsdlurl,
        SERVICE_QNAME, EPRInWsdlTestService.class);
    port1 = (Test1) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        EPRInWsdlTestService.class, PORT_QNAME1, Test1.class,
        addressingenabled);
    JAXWS_Util.setTargetEndpointAddress(port1, url1);

  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port1 = (Test1) service.getPort(Test1.class, addressingenabled);
    TestUtil.logMsg("port=" + port1);
    TestUtil.logMsg("Obtained port1");
    JAXWS_Util.dumpTargetEndpointAddress(port1);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);

      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;

      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }

      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (EPRInWsdlTestService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: GetEPRAndVerifyMetaDataAndRefParamsTest1
   *
   * @assertion_ids: WSAMD:SPEC:4002; WSAMD:SPEC:2000; WSAMD:SPEC:2000.1;
   * WSAMD:SPEC:2000.2; WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2;
   * WSAMD:SPEC:2001.3; WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2;
   * WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4; WSACORE:SPEC:2003;
   *
   * @test_Strategy: Retrieve EPR via BinderProvider.getEndpointReference().
   * Verify that the returned EPR matches the EPR defined in the WSDL. Perform
   * invocation and verify the reference parameters defined in the EPR in the
   * WSDL are sent across.
   */
  public void GetEPRAndVerifyMetaDataAndRefParamsTest1() throws Fault {
    TestUtil.logMsg("GetEPRAndVerifyMetaDataAndRefParamsTest1");
    boolean pass = true;

    try {
      BindingProvider bp = (BindingProvider) port1;
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      TestUtil.logMsg("---------------------------");
      TestUtil.logMsg("DUMP OF ENDPOINT REFERENCE");
      TestUtil.logMsg("---------------------------");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      DOMResult dr = new DOMResult();
      epr.writeTo(dr);
      TestUtil.logMsg("Validate the EPR for correctness (Verify MetaData)");
      if (!EprUtil.validateEPR(epr, url1, SERVICE_QNAME, PORT_QNAME1,
          PORT_TYPE_QNAME1, Boolean.TRUE))
        pass = false;
      TestUtil.logMsg("Validate the EPR reference parameters for correctness");
      if (dr != null) {
        String name = "MyParam1";
        String value = "Hello";
        TestUtil.logTrace("name=" + name);
        TestUtil.logTrace("value=" + value);
        boolean success = EprUtil.validateReferenceParameter(dr.getNode(), name,
            value);
        if (!success)
          pass = false;

        name = "MyParam2";
        value = "There";
        TestUtil.logTrace("name=" + name);
        TestUtil.logTrace("value=" + value);
        success = EprUtil.validateReferenceParameter(dr.getNode(), name, value);
        if (!success)
          pass = false;
        DataType data = new DataType();
        data.setParam("GetEPRAndVerifyMetaDataAndRefParamsTest1");
        Holder<DataType> hdt = new Holder<DataType>();
        hdt.value = data;
        TestUtil
            .logMsg("Verify EPR reference parameters are sent on invocation");
        port1.testOperation(hdt);
        TestUtil.logMsg("Invocation succeeded (expected)");
      } else {
        pass = false;
        TestUtil.logErr("No Reference Parameters were found");
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("GetEPRAndVerifyMetaDataAndRefParamsTest1 failed");
  }

  /*
   * @testName: GetPortAndVerifyRefParamsAreSentTest1
   *
   * @assertion_ids: WSAMD:SPEC:4002;
   *
   * @test_Strategy: Call Service.getPort(Class, AddressingFeature(true)).
   * Perform invocation and verify that the reference parameters of the inlined
   * EPR in wsdl are sent across.
   */
  public void GetPortAndVerifyRefParamsAreSentTest1() throws Fault {
    TestUtil.logMsg("GetPortAndVerifyRefParamsAreSentTest1");
    boolean pass = true;
    try {
      DataType datatype = new DataType();
      datatype.setParam("GetPortAndVerifyRefParamsAreSentTest1");
      Holder<DataType> data = new Holder<DataType>();
      data.value = datatype;
      TestUtil.logMsg(
          "Get port via Service.getPort(Class, AddressingFeature(true))");
      Test1 retport = (Test1) service.getPort(Test1.class, addressingenabled);
      if (retport == null) {
        TestUtil.logErr("Service.getPort() returned null (unexpected)");
        pass = false;
      } else {
        TestUtil
            .logMsg("Verify EPR reference parameters are sent on invocation");
        retport.testOperation(data);
        TestUtil.logMsg("Invocation succeeded (expected)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortAndVerifyRefParamsAreSentTest1 failed", e);
    }
    if (!pass)
      throw new Fault("GetPortAndVerifyRefParamsAreSentTest1 failed");
  }

  /*
   * @testName: GetPortAndVerifyRefParamsAreSentTest2
   *
   * @assertion_ids: WSAMD:SPEC:4002;
   *
   * @test_Strategy: Call Service.getPort(EPR, Class, AddressingFeature(true)).
   * Perform invocation and verify that the reference parameters of the inlined
   * EPR in wsdl are sent across.
   */
  public void GetPortAndVerifyRefParamsAreSentTest2() throws Fault {
    TestUtil.logMsg("GetPortAndVerifyRefParamsAreSentTest2");
    boolean pass = true;
    try {
      DataType datatype = new DataType();
      datatype.setParam("GetPortAndVerifyRefParamsAreSentTest2");
      Holder<DataType> data = new Holder<DataType>();
      data.value = datatype;
      BindingProvider bp = (BindingProvider) port1;
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      TestUtil.logMsg(
          "Get port via Service.getPort(EPR, Class, AddressingFeature(true))");
      Test1 retport = (Test1) service.getPort(epr, Test1.class,
          addressingenabled);
      if (retport == null) {
        TestUtil.logErr("Service.getPort() returned null (unexpected)");
        pass = false;
      } else {
        TestUtil
            .logMsg("Verify EPR reference parameters are sent on invocation");
        retport.testOperation(data);
        TestUtil.logMsg("Invocation succeeded (expected)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortAndVerifyRefParamsAreSentTest2 failed", e);
    }
    if (!pass)
      throw new Fault("GetPortAndVerifyRefParamsAreSentTest2 failed");
  }

  /*
   * @testName: GetPortAndVerifyModifiedRefParamsAreSentTest3
   *
   * @assertion_ids: WSAMD:SPEC:4002;
   *
   * @test_Strategy: Retrieve EPR via BinderProvider.getEndpointReference().
   * Modify the EPR to change the reference parameters. Call
   * Service.getPort(EPR, Class, AddressingFeature(true)). Perform invocation
   * and verify that the modified reference parameters are sent across.
   */
  public void GetPortAndVerifyModifiedRefParamsAreSentTest3() throws Fault {
    TestUtil.logMsg("GetPortAndVerifyModifiedRefParamsAreSentTest3");
    boolean pass = true;
    try {
      DataType datatype = new DataType();
      datatype.setParam("GetPortAndVerifyModifiedRefParamsAreSentTest3");
      Holder<DataType> data = new Holder<DataType>();
      data.value = datatype;
      BindingProvider bp = (BindingProvider) port1;
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      TestUtil.logMsg("Modify EPR by changing the reference parameters");
      DOMResult dr = new DOMResult();
      epr.writeTo(dr);
      XMLUtils.changeNodeValue_(dr.getNode(), "MyParam1", "MyValue1");
      XMLUtils.changeNodeValue_(dr.getNode(), "MyParam2", "MyValue2");
      epr = (W3CEndpointReference) EndpointReference
          .readFrom(new DOMSource(dr.getNode()));
      TestUtil.logMsg(
          "Get port via Service.getPort(EPR, Class, AddressingFeature(true))");
      Test1 retport = (Test1) service.getPort(epr, Test1.class,
          addressingenabled);
      if (retport == null) {
        TestUtil.logErr("Service.getPort() returned null (unexpected)");
        pass = false;
      } else {
        TestUtil
            .logMsg("Verify EPR reference parameters are sent on invocation");
        retport.testOperation(data);
        TestUtil.logMsg("Invocation succeeded (expected)");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetPortAndVerifyModifiedRefParamsAreSentTest3 failed",
          e);
    }
    if (!pass)
      throw new Fault("GetPortAndVerifyModifiedRefParamsAreSentTest3 failed");
  }
}
