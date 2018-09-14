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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1014;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;
import java.util.Properties;
import java.util.Iterator;

/**
 * Tests R1014 in the WSI Basic Profile 1.0: The children of the soap:Body
 * element in a MESSAGE MUST be namespace qualified.
 */
public class Client extends ServiceEETest implements SOAPRequests {

  private W2JRLR1014Client client;

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
    client = (W2JRLR1014Client) ClientFactory.getClient(W2JRLR1014Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testBodyChildrenAreQualifiedOnResponse
   *
   * @assertion_ids: JAXRPC:WSI:R1014
   *
   * @test_Strategy: Make a request and inspect response soap:Body children to
   *                 ensure they are namespace qualified.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testBodyChildrenAreQualifiedOnResponse() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(HELLOWORLD_WITH_HANDLER);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateBodyChildrenAreQualified(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
    client.logMessageInHarness(response);
  }

  /**
   * @testName: testBodyChildrenAreQualifiedOnRequest
   *
   * @assertion_ids: JAXRPC:WSI:R1014
   *
   * @test_Strategy: Make a request and inspect response to see if request was
   *                 conformant, as determined by server side handler.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testBodyChildrenAreQualifiedOnRequest() throws EETest.Fault {
    String response = null;
    try {
      response = client.helloWorld();
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    if (response.startsWith("failed")) {
      throw new EETest.Fault(response);
    }
  }

  private void validateBodyChildrenAreQualified(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    Iterator bodyChildren = response.getSOAPPart().getEnvelope().getBody()
        .getChildElements();
    SOAPElement child;
    String uri;
    while (bodyChildren.hasNext()) {
      child = (SOAPElement) bodyChildren.next();
      uri = child.getElementName().getURI();
      if (uri == null || uri.equals("")) {
        client.logMessageInHarness(response);
        throw new EETest.Fault("Invalid element: child elements of soap:Body"
            + " must be qualified (BP-R1014):  "
            + child.getElementName().getQualifiedName());
      }
    }
  }
}
