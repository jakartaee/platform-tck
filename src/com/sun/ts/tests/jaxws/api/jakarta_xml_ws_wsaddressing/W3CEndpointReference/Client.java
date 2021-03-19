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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_wsaddressing.W3CEndpointReference;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.EprUtil;

public class Client extends ServiceEETest {

  private static String xmlSource = "<EndpointReference xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>http://localhost:8080/WSDLHelloService_web/jaxws/Hello</Address><Metadata><wsam:InterfaceName xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:wsns=\"http://helloservice.org/wsdl\">wsns:Hello</wsam:InterfaceName><wsam:ServiceName xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" xmlns:ns3=\"http://www.w3.org/2005/08/addressing\" xmlns=\"\" xmlns:wsns=\"http://helloservice.org/wsdl\" EndpointName=\"HelloPort\">wsns:HelloService</wsam:ServiceName><wsdl:definitions xmlns:wsdl=\"http://schemas.xmlsoap.org/wsdl/\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\"><wsdl:import xmlns:ns5=\"http://www.w3.org/2005/08/addressing\" xmlns=\"\" location=\"http://localhost:8080/WSDLHelloService_web/jaxws/Hello?wsdl\" namespace=\"http://helloservice.org/wsdl\"/></wsdl:definitions></Metadata></EndpointReference>";

  private static final String URLENDPOINT = "http://localhost:8080/WSDLHelloService_web/jaxws/Hello";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private static final String PORT_TYPE_NAME = "Hello";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private QName PORT_TYPE_QNAME = new QName(NAMESPACEURI, PORT_TYPE_NAME);

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
   * @testName: W3CEndpointReferenceConstructorTest
   *
   * @assertion_ids: JAXWS:JAVADOC:184;
   *
   * @test_Strategy:
   */
  public void W3CEndpointReferenceConstructorTest() throws Fault {
    TestUtil.logTrace("W3CEndpointReferenceConstructorTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via W3CEndpointReference() ...");
      W3CEndpointReference e = new W3CEndpointReference(
          JAXWS_Util.makeSource(xmlSource, "StreamSource"));
      if (e != null) {
        TestUtil.logMsg("W3CEndpointReference object created successfully");
      } else {
        TestUtil.logErr("W3CEndpointReference object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("W3CEndpointReferenceConstructorTest failed", e);
    }

    if (!pass)
      throw new Fault("W3CEndpointReferenceConstructorTest failed");
  }

  /*
   * @testName: writeToTest
   *
   * @assertion_ids: JAXWS:JAVADOC:185;
   *
   * @test_Strategy:
   */
  public void writeToTest() throws Fault {
    TestUtil.logTrace("writeToTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via W3CEndpointReference() ...");
      W3CEndpointReference epr = new W3CEndpointReference(
          JAXWS_Util.makeSource(xmlSource, "StreamSource"));
      if (epr != null) {
        TestUtil.logMsg("W3CEndpointReference object created successfully");
      } else {
        TestUtil.logErr("W3CEndpointReference object not created");
        pass = false;
      }
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg("writeTo(): " + baos.toString());
      TestUtil.logMsg(
          "Now perform an epr.readFrom() of the results from epr.writeTo()");
      epr = new W3CEndpointReference(
          JAXWS_Util.makeSource(baos.toString(), "StreamSource"));
      TestUtil.logMsg("Validate the EPR for correctness (Verify MetaData)");
      if (!EprUtil.validateEPR(epr, URLENDPOINT, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.TRUE)) {
        pass = false;
        TestUtil.logErr("writeTo failed to write out xml source as expected");
      } else
        TestUtil.logMsg("writeTo passed to write out xml source as expected");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("writeToTest failed", e);
    }

    if (!pass)
      throw new Fault("writeToTest failed");
  }
}
