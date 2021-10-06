/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2751;

import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPElement;
import java.util.Properties;
import java.util.Iterator;
import java.io.InputStream;

public class Client extends ServiceEETest implements SOAPRequests {

  private W2JRLR2751Client client;

  static W2JRLR2751TestService service = null;

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
   * @class.testArgs: -ap jaxws-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void setup(String[] args, Properties properties) throws EETest.Fault {
    client = (W2JRLR2751Client) ClientFactory.getClient(W2JRLR2751Client.class,
        properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: IndependentOrderOfHeadersTest
   *
   * @assertion_ids: WSI:SPEC:R2751
   *
   * @test_Strategy: Send a SOAP request that has the headers in a different
   *                 order than they are defined in the wsdl. The endpoint
   *                 checks for their correctness.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void IndependentOrderOfHeadersTest() throws EETest.Fault {
    SOAPMessage response;
    try {
      response = client.makeSaajRequest(R2751_REQUEST);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
  }
}
