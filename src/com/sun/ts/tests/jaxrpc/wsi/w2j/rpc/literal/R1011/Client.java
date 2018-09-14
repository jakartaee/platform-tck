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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1011;

import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

/**
 * Tests R1011 in the WSI Basic Profile 1.0: A MESSAGE MUST NOT have any element
 * children of soap:Envelope following the soap:Body element.
 */
public class Client extends ServiceEETest implements SOAPRequests {

  /**
   * The string to be echoed for request two.
   */
  private static final String STRING_2 = "R1011-2";

  /**
   * The one client.
   */
  private W2JRLR1011ClientOne client1;

  /**
   * The other client.
   */
  private W2JRLR1011ClientTwo client2;

  /**
   * Test entry point.
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
    client1 = (W2JRLR1011ClientOne) ClientFactory
        .getClient(W2JRLR1011ClientOne.class, properties);
    client2 = (W2JRLR1011ClientTwo) ClientFactory
        .getClient(W2JRLR1011ClientTwo.class, properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testResponseChildren
   *
   * @assertion_ids: JAXRPC:WSI:R1011
   *
   * @test_Strategy: A valid request is made to the endpoint and the returned
   *                 response is investigated in order to determine the document
   *                 composition.
   *
   * @throws Fault
   */
  public void testResponseChildren() throws Fault {
    Document document;
    try {
      InputStream is = client1.makeHTTPRequest(R1011_REQUEST);
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.parse(is);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R1011)", e);
    }
    Element envelope = document.getDocumentElement();
    if (!isElement(envelope, "http://schemas.xmlsoap.org/soap/envelope/",
        "Envelope")) {
      throw new Fault(
          "Expected 'env:Envelope' element not received (BP-R1011)");
    }
    NodeList list = envelope.getChildNodes();
    boolean hasBody = false;
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() != Node.ELEMENT_NODE) {
        continue;
      }
      if (hasBody) {
        throw new Fault(
            "Child of 'env:Envelope' following 'env:Body' (BP-R1011)");
      } else {
        hasBody = isElement((Element) node,
            "http://schemas.xmlsoap.org/soap/envelope/", "Body");
      }
    }
  }

  protected boolean isElement(Element element, String namespaceURI,
      String localName) {
    if (!namespaceURI.equals(element.getNamespaceURI())) {
      return false;
    }
    return localName.equals(element.getLocalName());
  }

  /**
   * @testName: testRequestChildren
   *
   * @assertion_ids: JAXRPC:WSI:R1011
   *
   * @test_Strategy: A request is made from the generated client. The endpoint
   *                 is replaced by a Filter, that verified the encoding. The
   *                 returned string indicates the success or failure.
   *
   * @throws Fault
   */
  public void testRequestChildren() throws Fault {
    String result;
    try {
      result = client2.echoString(STRING_2);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R1011)", e);
    }
    if (!result.equals("OK")) {
      if (result.equals("EXCEPTION")) {
        throw new Fault("Endpoint unable to process request (BP-R1011)");
      } else {
        throw new Fault(
            "Request contains invalid 'env:Envelope' children (BP-R1011)");
      }
    }
  }
}
