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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2745;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;

import com.sun.ts.lib.harness.*;

public class Client extends ServiceEETest {
  /**
   * The one string to be echoed.
   */
  private static final String STRING_1 = "R2745-1";

  /**
   * The other string to be echoed.
   */
  private static final String STRING_2 = "R2745-2";

  /**
   * The one client.
   */
  private W2JRLR2745ClientOne client1;

  /**
   * The other client.
   */
  private W2JRLR2745ClientTwo client2;

  static W2JRLR2745TestService service = null;

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
   * @class.testArgs: -ap jaxws-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws Fault
   */
  public void setup(String[] args, Properties properties) throws Fault {
    client1 = (W2JRLR2745ClientOne) ClientFactory
        .getClient(W2JRLR2745ClientOne.class, properties, this, service);
    client2 = (W2JRLR2745ClientTwo) ClientFactory
        .getClient(W2JRLR2745ClientTwo.class, properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testSOAPAction
   *
   * @assertion_ids: WSI:SPEC:R2745; WSASB:SPEC:4001; WSASB:SPEC:4001.1;
   *                 WSASB:SPEC:4001.2;
   *
   * @test_Strategy: The supplied WSDL, containing a soap:operations with a
   *                 missing and empty soapAction attribute respectively, has
   *                 been used by the WSDL-to-Java tool to generate an endpoint
   *                 and client. A handler verifies the presence of the
   *                 SOAPAction header and its quoted value.
   *
   * @throws Fault
   */
  public void testSOAPAction() throws Fault {
    String result;
    try {
      result = client1.echoString(STRING_1);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R2745)", e);
    }
    if (!result.equals(STRING_1)) {
      throw new Fault("Missing or invalid soapAction header (BP-R2745)");
    }
    try {
      result = client2.echoString(STRING_2);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R2745)", e);
    }
    if (!result.equals(STRING_2)) {
      throw new Fault("Missing or invalid soapAction header (BP-R2745)");
    }
  }
}
