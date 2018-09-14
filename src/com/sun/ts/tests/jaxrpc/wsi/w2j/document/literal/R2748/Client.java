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

package com.sun.ts.tests.jaxrpc.wsi.w2j.document.literal.R2748;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;

/**
 * Tests R2748 in the WSI Basic Profile 1.0: A CONSUMER MUST NOT interpret the
 * presence of the wsdl:required attribute on a soapbind extension element with
 * a value of "false" to mean the extension element is optional in the messages
 * generated from the WSDL description.
 */
public class Client extends ServiceEETest {
  /**
   * The one string to be echoed.
   */
  private static final String STRING_1 = "R2748-1";

  /**
   * The other string to be echoed.
   */
  private static final String STRING_2 = "R2748-2";

  /**
   * The one client.
   */
  private W2JDLR2748ClientOne client1;

  /**
   * The other client.
   */
  private W2JDLR2748ClientTwo client2;

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
    client1 = (W2JDLR2748ClientOne) ClientFactory
        .getClient(W2JDLR2748ClientOne.class, properties);
    client2 = (W2JDLR2748ClientTwo) ClientFactory
        .getClient(W2JDLR2748ClientTwo.class, properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testFalseWSDLRequireds
   *
   * @assertion_ids: JAXRPC:WSI:R2748
   *
   * @test_Strategy: The supplied WSDL, containing two port types, which are
   *                 identical except for the use of the wsdl:required attribute
   *                 on the soap binding extension elements, has been used by
   *                 the WSDL-to-Java tool to generate an end point. If the tool
   *                 works correctly, the end-point has been built and deployed
   *                 so it should simply be reachable.
   *
   * @throws Fault
   */
  public void testFalseWSDLRequireds() throws Fault {
    String result;
    try {
      result = client1.echoString(STRING_1);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R2748)", e);
    }
    if (!STRING_1.equals(result)) {
      throw new Fault("echoString operation returns '" + result
          + "' in stead of '" + STRING_1 + "' (BP-R2748)");
    }
    try {
      result = client2.echoString(STRING_2);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R2748)", e);
    }
    if (!STRING_2.equals(result)) {
      throw new Fault("echoString operation returns '" + result
          + "' in stead of '" + STRING_2 + "' (BP-R2748)");
    }
  }
}
