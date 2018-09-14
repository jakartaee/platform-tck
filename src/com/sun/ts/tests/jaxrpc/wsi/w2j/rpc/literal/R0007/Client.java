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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R0007;

import java.util.Properties;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.sharedwebservices.faultservice.WSIConstants;
import com.sun.ts.tests.jaxrpc.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

/**
 * Tests R0007 in the WSI Basic Profile 1.0: A CONSUMER MUST NOT interpret the
 * presence of the wsdl:required attribute on a soapbind extension element with
 * a value of "false" to mean the extension element is optional in the messages
 * generated from the WSDL description.
 */
public class Client extends ServiceEETest
    implements SOAPConstants, WSIConstants, SOAPRequests {

  /**
   * The string to be echoed for request two.
   */
  private static final String STRING_2 = "R0007-2";

  /**
   * The one client.
   */
  private W2JRLR0007ClientOne client1;

  /**
   * The other client.
   */
  private W2JRLR0007ClientTwo client2;

  /**
   * Test entry.
   * 
   * @param args
   *          the command-line arguments.
   */
  public static void main(String[] args) {
    Client client = new Client();
    Status status = client.run(args, System.out, System.err);
    status.exit();
  }

  /**
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws Fault
   */
  public void setup(String[] args, Properties properties) throws Fault {
    client1 = (W2JRLR0007ClientOne) ClientFactory
        .getClient(W2JRLR0007ClientOne.class, properties);
    client2 = (W2JRLR0007ClientTwo) ClientFactory
        .getClient(W2JRLR0007ClientTwo.class, properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testResponseClaims
   *
   * @assertion_ids: JAXRPC:WSI:R0007
   *
   * @test_Strategy: A valid request is made to the endpoint and the returned
   *                 response is investigated for the presence of wsi:Claim
   *                 elements with soap:mustUnderstand="1" attriutes.
   *
   * @throws Fault
   */
  public void testResponseClaims() throws Fault {
    SOAPMessage response;
    try {
      response = client1.makeSaajRequest(R0007_REQUEST);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R0007):", e);
    }
    SOAPBody body;
    try {
      body = response.getSOAPPart().getEnvelope().getBody();
    } catch (SOAPException e) {
      throw new Fault("Invalid SOAP message returned (BP-R0007)", e);
    }
    NodeList list = body.getElementsByTagNameNS(WSI_CLAIM_NAMESPACE_URI,
        WSI_CLAIM_LOCAL_NAME);
    for (int i = 0; i < list.getLength(); i++) {
      Element element = (Element) list.item(i);
      Attr attr = element.getAttributeNodeNS(SOAP_NAMESPACE_URI,
          SOAP_MUST_UNDERSTAND_ATTR);
      if (attr != null) {
        String value = attr.getValue().trim();
        if (value.equals("1")) {
          throw new Fault(
              "wsi:Claim element with soap:mustUnderstand=\"1\" encountered (BP-R0007)");
        }
      }
    }
  }

  /**
   * @testName: testRequestClaims
   *
   * @assertion_ids: JAXRPC:WSI:R0007
   *
   * @test_Strategy: A request is made from the generated client. The endpoint
   *                 is replaced by a Servlet Filter, that verifies the request.
   *                 The returned string indicates success or failure.
   *
   * @throws Fault
   */
  public void testRequestClaims() throws Fault {
    String result;
    try {
      result = client2.echoString(STRING_2);
      System.out.println("result=" + result);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R0007):", e);
    }
    if (!result.equals("OK")) {
      if (result.equals("EXCEPTION")) {
        throw new Fault("Endpoint unable to process request (BP-R0007)");
      } else {
        throw new Fault(
            "wsi:Claim element with soap:mustUnderstand=\"1\" encountered (BP-R007)");
      }
    }
  }
}
