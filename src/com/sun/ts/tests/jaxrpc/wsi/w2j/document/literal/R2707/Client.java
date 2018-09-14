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

package com.sun.ts.tests.jaxrpc.wsi.w2j.document.literal.R2707;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;

/**
 * Tests R2707 in the WSI Basic Profile 1.0: A wsdl:binding in a DESCRIPTION
 * that contains one or more soapbind:body, soapbind:header, or
 * soapbind:headerfault elements that do not specify the use attribute MUST be
 * interpreted as though the value "literal" had been specified in each case.
 */
public class Client extends ServiceEETest {
  /**
   * The string to be echoed.
   */
  private static final String STRING = "R2707";

  /**
   * The client.
   */
  private W2JDLR2707Client client;

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
    client = (W2JDLR2707Client) ClientFactory.getClient(W2JDLR2707Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testUseAttributeDefaulting
   *
   * @assertion_ids: JAXRPC:WSI:R2707
   *
   * @test_Strategy: The supplied WSDL, containg a soap:body element without the
   *                 use="literal" attribute has been used by the WSDL-to-Java
   *                 tool to generate an end point. If the tool works correctly,
   *                 the end-point has been built and deployed so it should
   *                 simply be reachable.
   *
   * @throws Fault
   */
  public void testUseAttributeDefaulting() throws Fault {
    String result;
    try {
      result = client.echoString(STRING);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R2707)", e);
    }
    if (!STRING.equals(result)) {
      throw new Fault("echoString operation returns '" + result
          + "' in stead of '" + STRING + "' (BP-R2707)");
    }
  }
}
