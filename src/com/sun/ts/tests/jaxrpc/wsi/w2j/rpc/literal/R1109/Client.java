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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1109;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.sharedclients.SOAPClient;
import com.sun.ts.tests.jaxrpc.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import java.io.InputStream;
import java.util.Properties;

public class Client extends ServiceEETest implements SOAPRequests {

  private W2JRLR1109ClientOne client1;

  private W2JRLR1109ClientTwo client2;

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
    client1 = (W2JRLR1109ClientOne) ClientFactory
        .getClient(W2JRLR1109ClientOne.class, properties);
    client2 = (W2JRLR1109ClientTwo) ClientFactory
        .getClient(W2JRLR1109ClientTwo.class, properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testSoapActionHeaderIsQuotedInRequest
   *
   * @assertion_ids: JAXRPC:WSI:R1109
   *
   * @test_Strategy: Make a request and inspect request soapAction HTTP header
   *                 to ensure value is quoted
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testSoapActionHeaderIsQuotedInRequest() throws EETest.Fault {
    String response = "";
    try {
      response = client2.echoString("hello string");
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    if (response.startsWith("failed")) {
      throw new EETest.Fault(response);
    }
  }
}
