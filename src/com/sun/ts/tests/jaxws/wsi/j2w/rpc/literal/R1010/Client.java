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

package com.sun.ts.tests.jaxws.wsi.j2w.rpc.literal.R1010;

import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.simpleclient.*;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPElement;
import java.util.Properties;

import com.sun.ts.lib.harness.*;

public class Client extends ServiceEETest implements SOAPRequests {

  private SimpleTestClient client;

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
    client = (SimpleTestClient) ClientFactory.getClient(SimpleTestClient.class,
        properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testXMLDeclaration
   *
   * @assertion_ids: WSI:SPEC:R1010
   *
   * @test_Strategy: Make a request with XML declaration, inpsect response to
   *                 make sure it is expected response (not a soap:Fault).
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testXMLDeclaration() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(HELLOWORLD);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateIsExpected(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
    client.logMessageInHarness(response);
  }

  private void validateIsExpected(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    String responseMessage = getResponseValue(response);
    if (responseMessage == null || !responseMessage.equals("hello world")) {
      client.logMessageInHarness(response);
      throw new EETest.Fault(
          "Invalid response: instances must accept messages with an XML declaration"
              + "(BP-R1010)");
    }
  }

  private String getResponseValue(SOAPMessage response) throws SOAPException {
    SOAPElement elem = (SOAPElement) response.getSOAPPart().getEnvelope()
        .getBody().getChildElements().next();
    return ((SOAPElement) elem.getChildElements().next()).getValue();
  }
}
