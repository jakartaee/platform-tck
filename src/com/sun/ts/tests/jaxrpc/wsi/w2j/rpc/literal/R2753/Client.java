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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R2753;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.constants.WSIConstants;
import com.sun.javatest.Status;

import java.util.Properties;

/**
 * Tests R2753 in the WSI Basic Profile 1.0: A MESSAGE containing SOAP header
 * blocks that are not described in the appropriate wsdl:binding MAY have the
 * mustUnderstand attribute on such SOAP header blocks set to "1".
 */
public class Client extends ServiceEETest implements WSIConstants {

  private W2JRLR2753Client client;

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
    client = (W2JRLR2753Client) ClientFactory.getClient(W2JRLR2753Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testExtraMustUnderstandHeader
   *
   * @assertion_ids: JAXRPC:WSI:R12753
   *
   * @test_Strategy: Make a request and add a header not contained in the wsdl,
   *                 and set it to 'mustUnderstand=1'. Ensure the request and
   *                 response are normal.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testExtraMustUnderstandHeader() throws EETest.Fault {
    String response = "";
    try {
      response = client.helloWorld();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    if (!response.equals("hello world")) {
      throw new EETest.Fault("Error creating response object:" + response);
    }
  }
}
