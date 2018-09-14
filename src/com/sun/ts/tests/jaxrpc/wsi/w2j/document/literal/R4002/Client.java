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

package com.sun.ts.tests.jaxrpc.wsi.w2j.document.literal.R4002;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;

/**
 * Tests R4002 in the WSI Basic Profile 1.0: A DESCRIPTION MAY include the
 * Unicode Byte Order Mark (BOM).
 */
public class Client extends ServiceEETest {
  /**
   * The string to be echoed.
   */
  private static final String STRING = "R4002";

  /**
   * The client.
   */
  private W2JDLR4002Client client;

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
    client = (W2JDLR4002Client) ClientFactory.getClient(W2JDLR4002Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testDescriptionWithBOM
   *
   * @assertion_ids: JAXRPC:WSI:R4002
   *
   * @test_Strategy: The supplied WSDL, encoded as UTF-16 and containing a BOM,
   *                 has been used by the WSDL-to-Java tool to generate an end
   *                 point. If the tool works correctly, the end-point has been
   *                 built and deployed so it should simply be reachable.
   *
   * @throws Fault
   */
  public void testDescriptionWithBOM() throws Fault {
    String result;
    try {
      result = client.echoString(STRING);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R4002)", e);
    }
    if (!STRING.equals(result)) {
      throw new Fault("echoString operation returns '" + result
          + "' in stead of '" + STRING + "' (BP-R4002)");
    }
  }
}
