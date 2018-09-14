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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1007;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;
import java.util.Properties;
import java.util.Iterator;

/**
 * Tests R1007 in the WSI Basic Profile 1.0: A MESSAGE MUST NOT contain
 * soap:encodingStyle attributes on any element that is a grandchild of
 * soap:Body .
 */
public class Client extends ServiceEETest implements SOAPRequests {

  private W2JRLR1007Client client;

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
    client = (W2JRLR1007Client) ClientFactory.getClient(W2JRLR1007Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testNoEncodingStyleOnResponseBodyGrandchildren
   *
   * @assertion_ids: JAXRPC:WSI:R1007
   *
   * @test_Strategy: Make a request and inspect response soap:Body grandchildren
   *                 to ensure they don't have soap:encodingStyle attribute.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoEncodingStyleOnResponseBodyGrandchildren()
      throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(HELLOWORLD_WITH_HANDLER);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateNoEncodingStyleOnBodyGrandchildren(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
    client.logMessageInHarness(response);
  }

  /**
   * @testName: testNoEncodingStyleOnRequestBodyGrandchildren
   *
   * @assertion_ids: JAXRPC:WSI:R1007
   *
   * @test_Strategy: Make a request and inspect request soap:Body grandchildren
   *                 to ensure they don't have soap:encodingStyle attribute.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoEncodingStyleOnRequestBodyGrandchildren()
      throws EETest.Fault {
    String response = "";
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

  private void validateNoEncodingStyleOnBodyGrandchildren(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    Iterator bodyChildren = response.getSOAPPart().getEnvelope().getBody()
        .getChildElements();
    SOAPElement child;
    while (bodyChildren.hasNext()) {
      child = (SOAPElement) bodyChildren.next();
      validateNoEncodingStyleOnChildren(child, response);
    }
  }

  private void validateNoEncodingStyleOnChildren(SOAPElement element,
      SOAPMessage response) throws EETest.Fault {
    Iterator children = element.getChildElements();
    SOAPElement child;
    String encodingStyle;
    while (children.hasNext()) {
      child = (SOAPElement) children.next();
      encodingStyle = child.getEncodingStyle();
      if (!(encodingStyle == null || encodingStyle.equals(""))) {
        client.logMessageInHarness(response);
        throw new EETest.Fault(
            "Invalid element: grandchild elements of soap:Body"
                + " cannot have soap:encodingStyle attribute (BP-R1007):  "
                + child.getElementName().getQualifiedName());
      }
    }
  }
}
