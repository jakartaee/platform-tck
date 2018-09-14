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

/*
 * @(#)Client.java	1.4	03/08/14
 */

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R4003;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;

/**
 * Tests R4003 in the WSI Basic Profile 1.0:
 */
public class Client extends ServiceEETest {
  /**
   * The string to be echoed.
   */
  private static final String STRING = "R4003";

  /**
   * The client.
   */
  private W2JRLR4003Client client;

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
    client = (W2JRLR4003Client) ClientFactory.getClient(W2JRLR4003Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testWSDLImportUTF8UTF16
   *
   * @assertion_ids: JAXRPC:WSI:R4003
   *
   * @test_Strategy: The supplied WSDL imports both a UTF8 and UTF16 wsdl.
   *
   * @throws Fault
   */
  public void testWSDLImportUTF8UTF16() throws Fault {
    testImportUTF8WSDL();
    testImportUTF16WSDL();
  }

  private void testImportUTF8WSDL() throws Fault {
    String result;
    try {
      result = client.echoStringUTF8(STRING);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoStringUTF8 operation (BP-R4003)",
          e);
    }
    if (!STRING.equals(result)) {
      throw new Fault("echoStringUTF8 operation returns '" + result
          + "' in stead of '" + STRING + "' (BP-R4003)");
    }
  }

  private void testImportUTF16WSDL() throws Fault {
    String result;
    try {
      result = client.echoStringUTF16(STRING);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoStringUTF16 operation (BP-R4003)",
          e);
    }
    if (!STRING.equals(result)) {
      throw new Fault("echoStringUTF16 operation returns '" + result
          + "' in stead of '" + STRING + "' (BP-R4003)");
    }
  }

}
