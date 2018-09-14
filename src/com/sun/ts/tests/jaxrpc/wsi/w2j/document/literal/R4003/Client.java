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

package com.sun.ts.tests.jaxrpc.wsi.w2j.document.literal.R4003;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;

/**
 * Tests R4003 in the WSI Basic Profile 1.0: A DESCRIPTION MUST use either UTF-8
 * or UTF-16 encoding.
 */
public class Client extends ServiceEETest {
  /**
   * The client.
   */
  private W2JDLR4003Client client;

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
    client = (W2JDLR4003Client) ClientFactory.getClient(W2JDLR4003Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testDescriptionEncoding
   *
   * @assertion_ids: JAXRPC:WSI:R4003
   *
   * @test_Strategy: The supplied WSDL, importing UTF-8 and UTF-16 descriptions,
   *                 has been used by the WSDL-to-Java tool to generate an end
   *                 point. If the tool works correctly, the end-point has been
   *                 built and deployed so it should simply be reachable.
   *
   * @throws Fault
   */
  public void testDescriptionEncoding() throws Fault {
    try {
      String result = client.echoString("R4003");
      if (!"R4003".equals(result)) {
        throw new Fault("echoString operation returns '" + result
            + "' in stead of 'R4003' (BP-R4003)");
      }
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R4003)", e);
    }
  }
}
