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
 * $Id$
 */

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.headertest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.headertest.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

import jakarta.xml.ws.*;
import jakarta.xml.soap.*;

import java.util.Properties;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.headertest.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jrlheadertest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jrlheadertest.wsdlloc.1";

  private String url = null;

  // service and port information
  private static final String NAMESPACEURI = "http://headertestservice.org/HeaderTestService.wsdl";

  private static final String SERVICE_NAME = "HeaderTestService";

  private static final String PORT_NAME = "HeaderTestPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private URL wsdlurl = null;

  HeaderTest port = null;

  static HeaderTestService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    port = (HeaderTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        HeaderTestService.class, PORT_QNAME, HeaderTest.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (HeaderTest) service.getHeaderTestPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
    // JAXWS_Util.setSOAPLogging(port);
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
        service = (HeaderTestService) getSharedObject();
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
   * @testName: GoodOrderTestWithSoapHeaderAndMUFalse
   *
   * @assertion_ids: JAXWS:SPEC:2048; JAXWS:SPEC:2049; JAXWS:SPEC:10008;
   * JAXWS:SPEC:3038; WSI:SPEC:R1013; WSI:SPEC:R1034; WSI:SPEC:R1032;
   * WSI:SPEC:R9802; WSI:SPEC:R2209;
   *
   * @test_Strategy: Call submitOrder() with a valid product code passing a soap
   * header (ConfigHeader) with mustUnderstand=false. The soap header is simply
   * ignored. The RPC request must succeed.
   *
   */
  public void GoodOrderTestWithSoapHeaderAndMUFalse() throws Fault {
    TestUtil.logMsg("GoodOrderTestWithSoapHeaderAndMUFalse");
    boolean pass = true;

    ProductOrderRequest poRequest;
    ConfigHeader ch;
    try {
      poRequest = new ProductOrderRequest();
      ProductOrderItem poi = new ProductOrderItem();
      poi.setProductName("Product-1");
      poi.setProductCode(new BigInteger("100"));
      poi.setQuantity(10);
      poi.setPrice(new BigDecimal(119.00));
      CustomerInfo ci = new CustomerInfo();
      ci.setCreditcard("1201-4465-1567-9823");
      ci.setName("John Doe");
      ci.setStreet("1 Network Drive");
      ci.setCity("Burlington");
      ci.setState("Ma");
      ci.setZip("01837");
      ci.setCountry("USA");
      poRequest.getItem().add(poi);
      poRequest.setCustomerInfo(ci);
      ch = new ConfigHeader();
      ch.setMustUnderstand(false);
      ch.setMessage("Config Header");
      ch.setTestName("GoodOrderTestWithSoapHeaderAndMUFalse");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GoodOrderTestWithSoapHeaderAndMUFalse failed", e);
    }

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
   * @assertion_ids: JAXWS:SPEC:2048; JAXWS:SPEC:2049; JAXWS:SPEC:10008;
   * JAXWS:SPEC:3038; WSI:SPEC:R1013; WSI:SPEC:R1034; WSI:SPEC:R1032;
   * WSI:SPEC:R9802; WSI:SPEC:R2209;
   *
   * @test_Strategy: Call submitOrder() with a valid product code passing a soap
   * header (ConfigHeader) with mustUnderstand=true. The soap header is
   * understood by the service endpoint and the soap header is valid. The RPC
   * request must succeed.
   */
  public void GoodOrderTestWithSoapHeaderAndMUTrue() throws Fault {
    TestUtil.logMsg("GoodOrderTestWithSoapHeaderAndMUTrue");
    boolean pass = true;

    ProductOrderRequest poRequest;
    ConfigHeader ch;
    try {
      poRequest = new ProductOrderRequest();
      ProductOrderItem poi = new ProductOrderItem();
      poi.setProductName("Product-1");
      poi.setProductCode(new BigInteger("100"));
      poi.setQuantity(10);
      poi.setPrice(new BigDecimal(119.00));
      CustomerInfo ci = new CustomerInfo();
      ci.setCreditcard("1201-4465-1567-9823");
      ci.setName("John Doe");
      ci.setStreet("1 Network Drive");
      ci.setCity("Burlington");
      ci.setState("Ma");
      ci.setZip("01837");
      ci.setCountry("USA");
      poRequest.getItem().add(poi);
      poRequest.setCustomerInfo(ci);
      ch = new ConfigHeader();
      ch.setMustUnderstand(true);
      ch.setMessage("Config Header");
      ch.setTestName("GoodOrderTestWithSoapHeaderAndMUTrue");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GoodOrderTestWithSoapHeaderAndMUTrue failed", e);
    }

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
   * @assertion_ids: JAXWS:SPEC:2048; JAXWS:SPEC:2049; JAXWS:SPEC:10008;
   * WSI:SPEC:R1013; WSI:SPEC:R1034; WSI:SPEC:R1032; WSI:SPEC:R9802;
   * WSI:SPEC:R2209;
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

    ProductOrderRequest poRequest;
    ConfigHeader ch;
    try {
      poRequest = new ProductOrderRequest();
      ProductOrderItem poi = new ProductOrderItem();
      poi.setProductName("Product-1");
      poi.setProductCode(new BigInteger("100"));
      poi.setQuantity(10);
      poi.setPrice(new BigDecimal(119.00));
      CustomerInfo ci = new CustomerInfo();
      ci.setCreditcard("1201-4465-1567-9823");
      ci.setName("John Doe");
      ci.setStreet("1 Network Drive");
      ci.setCity("Burlington");
      ci.setState("Ma");
      ci.setZip("01837");
      ci.setCountry("USA");
      poRequest.getItem().add(poi);
      poRequest.setCustomerInfo(ci);
      ch = new ConfigHeader();
      ch.setMustUnderstand(true);
      ch.setMessage("Config Header");
      ch.setTestName("SoapHeaderFaultTest");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SoapHeaderFaultTest failed", e);
    }

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
   * @assertion_ids: JAXWS:SPEC:2048; JAXWS:SPEC:2049; JAXWS:SPEC:10008;
   * JAXWS:SPEC:3028; JAXWS:SPEC:2044; WSI:SPEC:R1013; WSI:SPEC:R1034;
   * WSI:SPEC:R1032; WSI:SPEC:R9802; WSI:SPEC:R2209;
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

    ProductOrderRequest poRequest;
    ConfigHeader ch;
    try {
      poRequest = new ProductOrderRequest();
      ProductOrderItem poi = new ProductOrderItem();
      poi.setProductName("Product-1");
      poi.setProductCode(new BigInteger("1234123412341234"));
      poi.setQuantity(10);
      poi.setPrice(new BigDecimal(119.00));
      CustomerInfo ci = new CustomerInfo();
      ci.setCreditcard("1201-4465-1567-9823");
      ci.setName("John Doe");
      ci.setStreet("1 Network Drive");
      ci.setCity("Burlington");
      ci.setState("Ma");
      ci.setZip("01837");
      ci.setCountry("USA");
      poRequest.getItem().add(poi);
      poRequest.setCustomerInfo(ci);
      ch = new ConfigHeader();
      ch.setMustUnderstand(false);
      ch.setMessage("Config Header");
      ch.setTestName("SoapFaultTest");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SoapFaultTest failed", e);
    }

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
    Object[] reqArray = req.getItem().toArray();
    Object[] respArray = resp.getItem().toArray();
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
      reqItem = (ProductOrderItem) reqArray[0];
      respItem = (ProductOrderItem) respArray[0];
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
