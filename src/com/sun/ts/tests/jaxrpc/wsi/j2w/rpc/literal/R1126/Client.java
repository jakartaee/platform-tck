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

package com.sun.ts.tests.jaxrpc.wsi.j2w.rpc.literal.R1126;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.sharedclients.faultclient.FaultTestClient;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Tests R1126 in the WSI Basic Profile 1.0: An INSTANCE MUST use a "500
 * Internal Server Error" HTTP status code if the response message contains a
 * SOAP Fault.
 */
public class Client extends ServiceEETest implements SOAPRequests {

  private FaultTestClient client;

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
    client = (FaultTestClient) ClientFactory.getClient(FaultTestClient.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testFaultResponseStatusCode
   *
   * @assertion_ids: JAXRPC:WSI:R1126
   *
   * @test_Strategy: Make a request that generates a soap:Fault, inpsect HTTP
   *                 response to make sure the status code is 500.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testFaultResponseStatusCode() throws EETest.Fault {
    InputStream response;
    try {
      response = client.makeHTTPRequest(ALWAYS_THROWS_EXCEPTION);
      client.logMessageInHarness(response);
      if (client.getStatusCode() != 500) {
        throw new EETest.Fault(
            "Invalid response: instances must return HTTP status code 500"
                + "when a soap:Fault is returned (BP-R1126).");
      }
    } catch (IOException e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
  }
}
