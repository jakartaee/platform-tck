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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R2714;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.constants.WSIConstants;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

/**
 * Tests R2714 in the WSI Basic Profile 1.0: For one-way operations, an INSTANCE
 * MUST NOT return a HTTP response that contains a SOAP envelope. Specifically,
 * the HTTP response entity-body of the must be empty.
 */
public class Client extends ServiceEETest
    implements WSIConstants, SOAPRequests {

  private W2JRLR2714Client client;

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
    client = (W2JRLR2714Client) ClientFactory.getClient(W2JRLR2714Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testNoResponseBodyForOneWay
   *
   * @assertion_ids: JAXRPC:WSI:R2714
   *
   * @test_Strategy: Make a request and inspect response to ensure there is no
   *                 HTTP response body
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoResponseBodyForOneWay() throws EETest.Fault {
    InputStream response = null;
    try {
      response = client.makeHTTPRequest(ONE_WAY_OPERATION);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateNoResponseBody(response);
    } catch (IOException ioe) {
      throw new EETest.Fault("Error creating response object", ioe);
    }
    client.logMessageInHarness(response);
  }

  private void validateNoResponseBody(InputStream is)
      throws IOException, EETest.Fault {
    if (is.available() > 0) {
      throw new EETest.Fault(
          "Invalid HTTP response: response body must be empty for "
              + "one way operations (BP-R2714).");
    }
  }
}
