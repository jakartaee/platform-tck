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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1005;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.constants.WSIConstants;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import javax.xml.soap.*;
import java.util.Properties;
import java.util.Iterator;

/**
 * Tests R1005 in the WSI Basic Profile 1.0: A MESSAGE MUST NOT contain
 * soap:encodingStyle attributes on any of the elements whose (namespace name)
 * is "http://schemas.xmlsoap.org/soap/envelope/".
 */
public class Client extends ServiceEETest
    implements WSIConstants, SOAPRequests {

  private W2JRLR1005Client client;

  /**
   * Test entry point.
   *
   * @param args
   *          the command-line arguments.
   */
  public static void main(String[] args) {
    Client tests = new Client();
    Status status = tests.run(args, System.out, System.err);
    status.exit();
  }

  /**
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void setup(String[] args, Properties properties) throws EETest.Fault {
    client = (W2JRLR1005Client) ClientFactory.getClient(W2JRLR1005Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testNoEncodingStyleOnResponseEnvelopeElements
   *
   * @assertion_ids: JAXRPC:WSI:R1005
   *
   * @test_Strategy: Make a request and inspect response elements with a
   *                 namespace of "http://schemas.xmlsoap.org/soap/envelope/" to
   *                 ensure they don't have soap:encodingStyle attribute.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoEncodingStyleOnResponseEnvelopeElements()
      throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(HELLOWORLD_WITH_HANDLER);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateNoEncodingStyleOnEnvelopeElements(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
    client.logMessageInHarness(response);
  }

  /**
   * @testName: testNoEncodingStyleOnRequestEnvelopeElements
   *
   * @assertion_ids: JAXRPC:WSI:R1005
   *
   * @test_Strategy: Make a request and inspect its elements on the server with
   *                 a namespace of "http://schemas.xmlsoap.org/soap/envelope/"
   *                 to ensure they don't have soap:encodingStyle attribute.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoEncodingStyleOnRequestEnvelopeElements()
      throws EETest.Fault {
    String response = null;
    try {
      response = client.helloWorld();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    if (response.startsWith("failed")) {
      throw new EETest.Fault(response);
    }
  }

  private void validateNoEncodingStyleOnEnvelopeElements(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    validateNoEncodingStyleOnEnvelopeElements(
        response.getSOAPPart().getEnvelope(), response);
  }

  private void validateNoEncodingStyleOnEnvelopeElements(Iterator soapElements,
      SOAPMessage response) throws EETest.Fault {
    Node n;
    while (soapElements.hasNext()) {
      n = (Node) soapElements.next();
      if (n instanceof SOAPElement) {
        validateNoEncodingStyleOnEnvelopeElements((SOAPElement) n, response);
      }
    }
  }

  private void validateNoEncodingStyleOnEnvelopeElements(SOAPElement element,
      SOAPMessage response) throws EETest.Fault {
    boolean fails = hasEncodingStyleAttr(element);
    if (fails) {
      client.logMessageInHarness(response);
      throw new EETest.Fault(
          "Invalid element: elements with namespace http://schemas.xmlsoap.org/soap/envelope/"
              + " cannot have soap:encodingStyle attribute (BP-R1005):  "
              + element.getElementName().getQualifiedName());
    }
    validateNoEncodingStyleOnEnvelopeElements(element.getChildElements(),
        response);
  }

  private boolean hasEncodingStyleAttr(SOAPElement elem) {
    Iterator attrs = elem.getAllAttributes();
    Name name;
    String uri;
    while (attrs.hasNext()) {
      name = (Name) attrs.next();
      uri = name.getURI();
      if (uri == null) {
        uri = "";
      }
      if (name.getLocalName().equals(SOAP_ENC_STYLE)
          && uri.equals(SOAP_ENV_NS)) {
        return true;
      }
    }
    return false;
  }
}
