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
 * @(#)Client.java	1.3 03/05/16
 */

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R4001;

import java.nio.charset.Charset;
import java.util.Properties;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

/**
 * Tests R4001 in the WSI Basic Profile 1.0: A RECEIVER MUST accept messages
 * that include the Unicode Byte Order Mark (BOM).
 */
public class Client extends ServiceEETest implements SOAPRequests {

  /**
   * The string to be echoed for request two.
   */
  private static final String STRING_2 = "R4001-2";

  /**
   * The one client.
   */
  private W2JRLR4001ClientOne client1;

  /**
   * The other client.
   */
  private W2JRLR4001ClientTwo client2;

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
    client1 = (W2JRLR4001ClientOne) ClientFactory
        .getClient(W2JRLR4001ClientOne.class, properties);
    client2 = (W2JRLR4001ClientTwo) ClientFactory
        .getClient(W2JRLR4001ClientTwo.class, properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testRequestWithBOM
   *
   * @assertion_ids: JAXRPC:WSI:R4001
   *
   * @test_Strategy: A valid request, encoded in UTF-16 with a BOM, is sent to
   *                 the endpoint. A conformant server must correctly process it
   *                 and return the expected response.
   *
   * @throws Fault
   */
  public void testRequestWithBOM() throws Fault {
    SOAPMessage response;
    try {
      Charset cs = Charset.forName("UTF-16");
      response = client1.makeSaajRequest(R4001_REQUEST, cs);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R4001)", e);
    }
    try {
      SOAPBody body = response.getSOAPPart().getEnvelope().getBody();
      if (body.hasFault()) {
        throw new Fault("Request not processed by endpoint (BP-R4001)");
      }
    } catch (SOAPException e) {
      throw new Fault("Invalid SOAP message returned (BP-R4001)", e);
    }
  }

  /**
   * @testName: testResponseWithBOM
   *
   * @assertion_ids: JAXRPC:WSI:R4001
   *
   * @test_Strategy: A request is made from the generated client. The endpoint
   *                 is replaced by a Filter, that returns a valid response in
   *                 UTF-16 with a BOM. A conformant client must correctly
   *                 accept and process the response.
   *
   * @throws Fault
   */
  public void testResponseWithBOM() throws Fault {
    String result;
    try {
      result = client2.echoString(STRING_2);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R4001)", e);
    }
    if (!STRING_2.equals(result)) {
      throw new Fault("echoString operation returns '" + result
          + "' in stead of '" + STRING_2 + "' (BP-R4001)");
    }
  }
}
