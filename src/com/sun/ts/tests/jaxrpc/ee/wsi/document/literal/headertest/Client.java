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

/*
 * @(#)Client.java	1.4	03/05/16
 */

package com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.headertest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.headertest.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

import javax.xml.rpc.*;
import javax.xml.soap.*;

import java.util.Properties;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.*;

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

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.headertest.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsidlheadertest.endpoint.1";

  private static final String WSDLLOC_URL = "wsidlheadertest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  // Get Port and Stub access via porting layer interface
  HeaderTest port = null;

  Stub stub = null;

  private void getStubStandalone() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (HeaderTest) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.ee.wsi.document.literal."
            + "headertest.HeaderTestService", "getHeaderTestPort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Lookup java:comp/env/service/wsidlheadertest");
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/wsidlheadertest");
    TestUtil.logMsg("Get port from Service");
    port = (HeaderTest) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.headertest.HeaderTest.class);
    TestUtil.logMsg("Port obtained");
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
        getStubStandalone();
        TestUtil.logMsg("Setting target endpoint to " + url + " ...");
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
        JAXRPC_Util.setSOAPLogging(stub, System.out);
      } else {
        getStub();
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
   * @testName: GoodOrderTestWithSoapHeaderAndMUFalse
   *
   * @assertion_ids: JAXRPC:SPEC:111; JAXRPC:SPEC:298; JAXRPC:SPEC:280;
   * JAXRPC:SPEC:536; WS4EE:SPEC:179; WS4EE:SPEC:177; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207;
   *
   * @test_Strategy: Call submitOrder() with a valid product code passing a soap
   * header (ConfigHeader) with mustUnderstand=false. The soap header is simply
   * ignored. The RPC request must succeed.
   *
   */
  public void GoodOrderTestWithSoapHeaderAndMUFalse() throws Fault {
    TestUtil.logMsg("GoodOrderTestWithSoapHeaderAndMUFalse");
    boolean pass = true;

    ProductOrderRequest poRequest = new ProductOrderRequest();
    ProductOrderItem[] poiArray = new ProductOrderItem[1];
    ProductOrderItem poi = new ProductOrderItem();
    poi.setProductName("Product-1");
    poi.setProductCode(new BigInteger("100"));
    poi.setQuantity(10);
    poi.setPrice(new BigDecimal(119.00));
    poiArray[0] = poi;
    CustomerInfo ci = new CustomerInfo();
    ci.setCreditcard("1201-4465-1567-9823");
    ci.setName("John Doe");
    ci.setStreet("1 Network Drive");
    ci.setCity("Burlington");
    ci.setState("Ma");
    ci.setZip("01837");
    ci.setCountry("USA");
    poRequest.setItem(poiArray);
    poRequest.setCustomerInfo(ci);
    ConfigHeader ch = new ConfigHeader();
    ch.setMustUnderstand(false);
    ch.setMessage("Config Header");
    ch.setTestName("GoodOrderTestWithSoapHeaderAndMUFalse");

    try {
      TestUtil
          .logMsg("Submit good order with soap header (ConfigHeader:MU=false)");
      TestUtil.logMsg("ConfigHeader must be ignored because MU=false");
      TestUtil.logMsg("The service endpoint simply ignores the soap header");
      TestUtil.logMsg("The RPC request must succeed");
      ProductOrderResponse poResponse = port.submitOrder(poRequest, ch);
      if (!ProductOrdersEqual(poRequest, poResponse))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GoodOrderTestWithSoapHeaderAndMUFalse failed", e);
    }

    if (!pass)
      throw new Fault("GoodOrderTestWithSoapHeaderAndMUFalse failed");
  }

  /*
   * @testName: GoodOrderTestWithSoapHeaderAndMUTrue
   *
   * @assertion_ids: JAXRPC:SPEC:111; JAXRPC:SPEC:298; JAXRPC:SPEC:280;
   * JAXRPC:SPEC:536; WS4EE:SPEC:179; WS4EE:SPEC:177; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207;
   *
   * @test_Strategy: Call submitOrder() with a valid product code passing a soap
   * header (ConfigHeader) with mustUnderstand=true. The soap header is
   * understood by the service endpoint and the soap header is valid. The RPC
   * request must succeed.
   */
  public void GoodOrderTestWithSoapHeaderAndMUTrue() throws Fault {
    TestUtil.logMsg("GoodOrderTestWithSoapHeaderAndMUTrue");
    boolean pass = true;

    ProductOrderRequest poRequest = new ProductOrderRequest();
    ProductOrderItem[] poiArray = new ProductOrderItem[1];
    ProductOrderItem poi = new ProductOrderItem();
    poi.setProductName("Product-1");
    poi.setProductCode(new BigInteger("100"));
    poi.setQuantity(10);
    poi.setPrice(new BigDecimal(119.00));
    poiArray[0] = poi;
    CustomerInfo ci = new CustomerInfo();
    ci.setCreditcard("1201-4465-1567-9823");
    ci.setName("John Doe");
    ci.setStreet("1 Network Drive");
    ci.setCity("Burlington");
    ci.setState("Ma");
    ci.setZip("01837");
    ci.setCountry("USA");
    poRequest.setItem(poiArray);
    poRequest.setCustomerInfo(ci);
    ConfigHeader ch = new ConfigHeader();
    ch.setMustUnderstand(true);
    ch.setMessage("Config Header");
    ch.setTestName("GoodOrderTestWithSoapHeaderAndMUTrue");

    try {
      TestUtil
          .logMsg("Submit good order with soap header (ConfigHeader:MU=true)");
      TestUtil
          .logMsg("ConfigHeader must be understood and valid bacause MU=true");
      TestUtil.logMsg(
          "The service endpoint understands and validates the soap header as ok");
      TestUtil.logMsg("The RPC request must succeed");
      ProductOrderResponse poResponse = port.submitOrder(poRequest, ch);
      TestUtil.logMsg("GoodOrderTestWithMUTrueHeader succeeded (expected)");
      if (!ProductOrdersEqual(poRequest, poResponse))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GoodOrderTestWithSoapHeaderAndMUTrue failed", e);
    }

    if (!pass)
      throw new Fault("GoodOrderTestWithSoapHeaderAndMUTrue failed");
  }

  /*
   * @testName: SoapHeaderFaultTest
   *
   * @assertion_ids: JAXRPC:SPEC:111; JAXRPC:SPEC:303; JAXRPC:SPEC:280;
   * JAXRPC:SPEC:536; WS4EE:SPEC:179; WS4EE:SPEC:177; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207;
   *
   * @test_Strategy: Call submitOrder() passing soap header (ConfigHeader) with
   * mustUnderstand attribute=true and the soap header (ConfigHeader) is not
   * understood. The service endpoint must throw back the SOAP Header Fault
   * (ConfigFault).
   *
   */
  public void SoapHeaderFaultTest() throws Fault {
    TestUtil.logMsg("SoapHeaderFaultTest");
    boolean pass = true;

    ProductOrderRequest poRequest = new ProductOrderRequest();
    ProductOrderItem[] poiArray = new ProductOrderItem[1];
    ProductOrderItem poi = new ProductOrderItem();
    poi.setProductName("Product-1");
    poi.setProductCode(new BigInteger("100"));
    poi.setQuantity(10);
    poi.setPrice(new BigDecimal(119.00));
    poiArray[0] = poi;
    CustomerInfo ci = new CustomerInfo();
    ci.setCreditcard("1201-4465-1567-9823");
    ci.setName("John Doe");
    ci.setStreet("1 Network Drive");
    ci.setCity("Burlington");
    ci.setState("Ma");
    ci.setZip("01837");
    ci.setCountry("USA");
    poRequest.setItem(poiArray);
    poRequest.setCustomerInfo(ci);
    ConfigHeader ch = new ConfigHeader();
    ch.setMustUnderstand(true);
    ch.setMessage("Config Header");
    ch.setTestName("SoapHeaderFaultTest");

    try {
      TestUtil
          .logMsg("Submit good order with soap header (ConfigHeader:MU=true)");
      TestUtil
          .logMsg("ConfigHeader must be understood and valid bacause MU=true");
      TestUtil
          .logMsg("The service endpoint does not understand the soap header");
      TestUtil.logMsg("The RPC request must fail with a ConfigFault");
      ProductOrderResponse poResponse = port.submitOrder(poRequest, ch);
      TestUtil.logErr("Did not throw expected ConfigFault");
      pass = false;
    } catch (ConfigFault e) {
      TestUtil.logMsg("Caught expected ConfigFault");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SoapHeaderFaultTest failed", e);
    }

    if (!pass)
      throw new Fault("SoapHeaderFaultTest failed");
  }

  /*
   * @testName: SoapFaultTest
   *
   * @assertion_ids: JAXRPC:SPEC:111; JAXRPC:SPEC:298; JAXRPC:SPEC:280;
   * JAXRPC:SPEC:536; WS4EE:SPEC:179; WS4EE:SPEC:177; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207;
   *
   * @test_Strategy: Call submitOrder() passing soap header (ConfigHeader) with
   * mustUnderstand attribute=false so the soap header (ConfigHeader) will
   * simply be ignored. The submitOrder() contains an invalid product code so
   * the service endpoint must throw back the SOAP Fault (BadOrderFault).
   *
   */
  public void SoapFaultTest() throws Fault {
    TestUtil.logMsg("SoapFaultTest");
    boolean pass = true;

    ProductOrderRequest poRequest = new ProductOrderRequest();
    ProductOrderItem[] poiArray = new ProductOrderItem[1];
    ProductOrderItem poi = new ProductOrderItem();
    poi.setProductName("Product-1");
    poi.setProductCode(new BigInteger("1234123412341234"));
    poi.setQuantity(10);
    poi.setPrice(new BigDecimal(119.00));
    poiArray[0] = poi;
    CustomerInfo ci = new CustomerInfo();
    ci.setCreditcard("1201-4465-1567-9823");
    ci.setName("John Doe");
    ci.setStreet("1 Network Drive");
    ci.setCity("Burlington");
    ci.setState("Ma");
    ci.setZip("01837");
    ci.setCountry("USA");
    poRequest.setItem(poiArray);
    poRequest.setCustomerInfo(ci);
    ConfigHeader ch = new ConfigHeader();
    ch.setMustUnderstand(false);
    ch.setMessage("Config Header");
    ch.setTestName("SoapFaultTest");

    try {
      TestUtil
          .logMsg("Submit bad order with soap header (ConfigHeader:MU=false)");
      TestUtil.logMsg("ConfigHeader must be ignored because MU=false");
      TestUtil.logMsg("The service endpoint simply ignores the soap header");
      TestUtil
          .logMsg("Order contains bad product code (must throw BadOrderFault)");
      TestUtil.logMsg("The RPC request must fail with a BadOrderFault");
      ProductOrderResponse poResponse = port.submitOrder(poRequest, ch);
      TestUtil.logErr("Did not throw expected BadOrderFault");
      pass = false;
    } catch (BadOrderFault e) {
      TestUtil.logMsg("Caught expected BadOrderFault");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SoapFaultTest failed", e);
    }

    if (!pass)
      throw new Fault("SoapFaultTest failed");
  }

  private boolean ProductOrdersEqual(ProductOrderRequest req,
      ProductOrderResponse resp) {
    boolean equal = true;
    TestUtil.logMsg(
        "Performing data comparison of request/response (should be equal)");
    ProductOrderItem[] reqArray = req.getItem();
    ProductOrderItem[] respArray = resp.getItem();
    ProductOrderItem reqItem = null;
    ProductOrderItem respItem = null;
    if (reqArray == null || respArray == null) {
      TestUtil.logErr("Data comparison error (unexpected)");
      TestUtil.logErr("Got:      Item Array = " + respItem);
      TestUtil.logErr("Expected: Item Array = " + reqItem);
      equal = false;
    } else if (reqArray.length != respArray.length) {
      TestUtil.logErr("Data comparison error (unexpected)");
      TestUtil.logErr("Got:      Item Array length = " + respArray.length);
      TestUtil.logErr("Expected: Item Array length = " + reqArray.length);
      equal = false;
    } else {
      reqItem = reqArray[0];
      respItem = respArray[0];
    }
    if (equal) {
      if (!reqItem.getProductName().equals(respItem.getProductName())
          || !reqItem.getProductCode().equals(respItem.getProductCode())
          || reqItem.getQuantity() != respItem.getQuantity()
          || !reqItem.getPrice().equals(respItem.getPrice())) {
        TestUtil.logErr("Data comparison error (unexpected)");
        TestUtil.logErr("Got:      <" + respItem.getProductName() + ","
            + respItem.getProductCode() + "," + respItem.getQuantity() + ","
            + respItem.getPrice() + ">");
        TestUtil.logErr("Expected: <" + reqItem.getProductName() + ","
            + reqItem.getProductCode() + "," + reqItem.getQuantity() + ","
            + reqItem.getPrice() + ">");
        equal = false;
      } else {
        TestUtil.logMsg("Data comparison ok (expected)");
        TestUtil.logMsg("Got:      <" + respItem.getProductName() + ","
            + respItem.getProductCode() + "," + respItem.getQuantity() + ","
            + respItem.getPrice() + ">");
        TestUtil.logMsg("Expected: <" + reqItem.getProductName() + ","
            + reqItem.getProductCode() + "," + reqItem.getQuantity() + ","
            + reqItem.getPrice() + ">");
      }
    }
    return equal;
  }
}
