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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R97XX;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;

import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.ts.lib.harness.*;

public class Client extends ServiceEETest
    implements SOAPConstants, SOAPRequests {

  /**
   * The string to be echoed for request two.
   */
  private static final String STRING_2 = "R97XX-2";

  /**
   * The one client.
   */
  private W2JRLR97XXClientOne client1;

  /**
   * The other client.
   */
  private W2JRLR97XXClientTwo client2;

  static W2JRLR97XXTestService service = null;

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
   * @class.testArgs: -ap jaxws-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws Fault
   */
  public void setup(String[] args, Properties properties) throws Fault {
    client1 = (W2JRLR97XXClientOne) ClientFactory
        .getClient(W2JRLR97XXClientOne.class, properties, this, service);
    client2 = (W2JRLR97XXClientTwo) ClientFactory
        .getClient(W2JRLR97XXClientTwo.class, properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  private boolean isElement(Element element, String namespaceURI,
      String localName) {
    if (!namespaceURI.equals(element.getNamespaceURI())) {
      return false;
    }
    return localName.equals(element.getLocalName());
  }

  /**
   * @testName: testResponseEncoding
   *
   * @assertion_ids: WSI:SPEC:R9700; WSI:SPEC:R9701; WSI:SPEC:R9702;
   *                 WSI:SPEC:R9703; WSI:SPEC:R1018; WSI:SPEC:R1140;
   *                 WSI:SPEC:R1132;
   *
   * @test_Strategy: A valid request is made to the endpoint and the returned
   *                 response is investigated in order to determine the
   *                 encoding.
   *
   * @throws Fault
   */
  public void testResponseEncoding() throws Fault {
    InputStream is;
    Charset cs = Charset.forName("UTF-8");
    try {
      is = client1.makeHTTPRequest(R97XX_REQUEST, cs);

      // Testing for correct Content-Type header info
      String contentType = client1.getResponseHeader("Content-Type");
      TestUtil.logMsg("Content-Type:" + contentType);
      if (contentType == null)
        throw new Fault(
            "R9700,R9701,R9702,R9703,R1018 assertions failed - missing Content-Type header");
      else if (contentType != null) {
        if (contentType.indexOf("text/xml") == -1)
          throw new Fault(
              "R9703 assertion failed - Content-Type header not text/xml");
        int index = contentType.toLowerCase().indexOf("charset=");
        if (index == -1)
          throw new Fault(
              "R1018 assertion failed - Content-Type header missing charset attribute");
        if (index > 0) {
          String name = contentType.substring(index + 8).trim();
          char c = name.charAt(0);
          if ((c == '\"') || (c == '\'')) {
            name = name.substring(1, name.length() - 1);
          }
          if ((name.equalsIgnoreCase("UTF-8"))
              || name.equalsIgnoreCase("UTF-16")) {
            cs = Charset.forName(name);
          } else {
            throw new Fault(
                "R9700,R9701,R1018 assertions failed - Content-Type header attribute charset not UTF-8 or UTF-16 but "
                    + name);
          }
        }
      }

    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (R97XX)", e);
    }
    try {
      // Testing for serialization of envelope as XML 1.0
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(is);
      Element envelope = document.getDocumentElement();
      if (!isElement(envelope, "http://schemas.xmlsoap.org/soap/envelope/",
          "Envelope")) {
        throw new Fault("Expected 'env:Envelope' element not received (R9701)");
      }
    } catch (Exception e) {
      com.sun.ts.lib.util.TestUtil.printStackTrace(e);
      throw new Fault("Failed to parse XML document information");
    }
  }

  /**
   * @testName: testRequestEncoding
   *
   * @assertion_ids: WSI:SPEC:R97XX
   *
   * @test_Strategy: A request is made from the generated client. A handler
   *                 verifies the encoding. An exception is thrown if ther eis a
   *                 failure
   *
   * @throws Fault
   */
  public void testRequestEncoding() throws Fault {
    String result;
    try {
      result = client2.echoString(STRING_2);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (R97XX)", e);
    }
    TestUtil.logMsg("result=" + result);
    if (!result.equals(STRING_2)) {
      if (result.equals("EXCEPTION")) {
        throw new Fault("Endpoint unable to process request (R97XX)");
      } else {
        throw new Fault(
            "Request encoding neither 'UTF-8' nor 'UTF-16' (R97XX)");
      }
    }
  }
}
