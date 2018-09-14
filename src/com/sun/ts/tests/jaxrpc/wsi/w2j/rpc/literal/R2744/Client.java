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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R2744;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;

/**
 * Tests R2744 in the WSI Basic Profile 1.0: A MESSAGE MUST contain a SOAPAction
 * HTTP header field with a quoted value equal to the value of the soapAction
 * attribute of soapbind:operation, if present in the corresponding WSDL
 * description.
 */
public class Client extends ServiceEETest {
  /**
   * The one string to be echoed.
   */
  private static final String STRING = "R2744";

  /**
   * The one client.
   */
  private W2JRLR2744Client client;

  /**
   * Test entry point.
   * 
   * @param args
   *          the command-line arguments.
   */
  public static void main(String[] args) {
    Client client = new Client();
    Status status = client.run(args, System.out, System.err);
    status.exit();
  }

  /**
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws Fault
   */
  public void setup(String[] args, Properties properties) throws Fault {
    client = (W2JRLR2744Client) ClientFactory.getClient(W2JRLR2744Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testSOAPAction
   *
   * @assertion_ids: JAXRPC:WSI:R2744
   *
   * @test_Strategy: The supplied WSDL, containing a soap:operation with a
   *                 soapAction attribute, has been used by the WSDL-to-Java
   *                 tool to generate an endpoint and client. The endpoint has
   *                 been replaced by a Servlet Filter that verifies the
   *                 presence of the SOAPAction header and its quoted value.
   *
   * @throws Fault
   */
  public void testSOAPAction() throws Fault {
    String result;
    try {
      result = client.echoString(STRING);
      System.out.println("result=" + result);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R2744)", e);
    }
    if (!result.equals("OK")) {
      throw new Fault("Missing or invalid SOAPAction header (BP-R2744)");
    }
  }
}
