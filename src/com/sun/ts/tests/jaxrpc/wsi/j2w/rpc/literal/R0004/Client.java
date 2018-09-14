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

package com.sun.ts.tests.jaxrpc.wsi.j2w.rpc.literal.R0004;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.sharedclients.simpleclient.SimpleTestClient;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;
import java.util.Properties;

/**
 * Tests R0004 in the WSI Basic Profile 1.0: A MESSAGE MAY contain conformance
 * claims, as specified in the conformance claim schema.
 */
public class Client extends ServiceEETest implements SOAPRequests {

  private SimpleTestClient client;

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
    client = (SimpleTestClient) ClientFactory.getClient(SimpleTestClient.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testConformanceClaimInRequestMessage
   *
   * @assertion_ids: JAXRPC:WSI:R0004
   *
   * @test_Strategy: Make a request with a conformance claim, ensure the
   *                 response is normal (not a fault).
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testConformanceClaimInRequestMessage() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(HELLOWORLD_WITH_CONFORMANCE);
    } catch (Exception e) {
      client.logMessageInHarness(response);
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      client.logMessageInHarness(response);
      validateIsExpected(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }

  }

  /**
   * @testName: testConformanceClaimInResponseMessage
   *
   * @assertion_ids: JAXRPC:WSI:R0004
   *
   * @test_Strategy: Make a request, ensure the response with conformance claim
   *                 can be processed by client.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testConformanceClaimInResponseMessage() throws EETest.Fault {
    String response;
    try {
      response = client.helloWorld();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    if (response == null || !response.equals("hello world")) {
      throw new EETest.Fault("Client stub did not properly process response:"
          + " it must accept messages with conformance claims (BP-R0004)");
    }
  }

  private void validateIsExpected(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    if (response.getSOAPPart().getEnvelope().getBody().hasFault()) {
      throw new EETest.Fault("Invalid response: instances must accept messages"
          + "with conformance claims (BP-R0004)");
    }
    if (!getResponseValue(response).equals("hello world")) {
      throw new EETest.Fault("Invalid response: instances must accept messages"
          + "with conformance claims (BP-R0004)");
    }

  }

  private String getResponseValue(SOAPMessage response) throws SOAPException {
    SOAPElement elem = (SOAPElement) response.getSOAPPart().getEnvelope()
        .getBody().getChildElements().next();
    return ((SOAPElement) elem.getChildElements().next()).getValue();
  }
}
