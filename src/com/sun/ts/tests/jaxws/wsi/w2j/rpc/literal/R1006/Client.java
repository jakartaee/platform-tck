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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1006;

import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPElement;
import java.util.Properties;
import java.util.Iterator;

public class Client extends ServiceEETest implements SOAPRequests {

  private W2JRLR1006Client client;

  static SimpleTest service = null;

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
   * @class.testArgs: -ap jaxws-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void setup(String[] args, Properties properties) throws EETest.Fault {
    client = (W2JRLR1006Client) ClientFactory.getClient(W2JRLR1006Client.class,
        properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testNoEncodingStyleOnResponseBodyChildren
   *
   * @assertion_ids: WSI:SPEC:R1006
   *
   * @test_Strategy: Make a request and inspect response soap:Body children to
   *                 ensure they don't have soap:encodingStyle attribute.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoEncodingStyleOnResponseBodyChildren() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(HELLOWORLD_WITH_HANDLER);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateNoEncodingStyleOnBodyChildren(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
    client.logMessageInHarness(response);
  }

  /**
   * @testName: testNoEncodingStyleOnRequestBodyChildren
   *
   * @assertion_ids: WSI:SPEC:R1006
   *
   * @test_Strategy: Make a request and inspect request soap:Body children to
   *                 ensure they don't have soap:encodingStyle attribute.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoEncodingStyleOnRequestBodyChildren() throws EETest.Fault {
    String response = "";
    try {
      response = client.helloWorld();
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    if (response.startsWith("failed")) {
      throw new EETest.Fault(response);
    }
  }

  private void validateNoEncodingStyleOnBodyChildren(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    Iterator bodyChildren = response.getSOAPPart().getEnvelope().getBody()
        .getChildElements();
    SOAPElement child;
    String encodingStyle;
    while (bodyChildren.hasNext()) {
      child = (SOAPElement) bodyChildren.next();
      encodingStyle = child.getEncodingStyle();
      if (!(encodingStyle == null || encodingStyle.equals(""))) {
        client.logMessageInHarness(response);
        throw new EETest.Fault("Invalid element: child elements of soap:Body"
            + " cannot have soap:encodingStyle attribute (BP-R1006):  "
            + child.getElementName().getQualifiedName());
      }
    }
  }
}
