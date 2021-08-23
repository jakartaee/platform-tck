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
 * $Id$
 */

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1027;

import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.wsi.utils.SOAPUtils;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.ws.soap.SOAPFaultException;
import java.util.Properties;
import java.rmi.ServerException;

public class Client extends ServiceEETest implements SOAPRequests {

  private W2JRLR1027Client client;

  static SimpleTest service;

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
    client = (W2JRLR1027Client) ClientFactory.getClient(W2JRLR1027Client.class,
        properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testNotUnderstoodHeaderInRequest
   *
   * @assertion_ids: WSI:SPEC:R1027
   *
   * @test_Strategy: Make a request with envelope with soap header with
   *                 mustUnderstnad = 1, that the server doesn't understand.
   *                 Inspect repsonse to ensure it is a soap:Fault with
   *                 faultcode of "MustUnderstand"
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNotUnderstoodHeaderInRequest() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(MUST_UNDERSTAND_HEADER);
    } catch (Exception e) {
      client.logMessageInHarness(response);
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      client.logMessageInHarness(response);
      validateIsMustUnderstandFault(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }

  }

  private void validateIsMustUnderstandFault(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    if (!SOAPUtils.isMustUnderstandFaultcode(response)) {
      throw new EETest.Fault(
          "Invalid response: instances must generate a \"MustUnderstand\" "
              + "soap:Fault when a request contains a soap header with \"mustUnderstand=1\" "
              + "that is not understood (BP-R1027)");
    }

  }

  private void validateIsMustUnderstandFault(SOAPFaultException se)
      throws EETest.Fault {
    if (!SOAPUtils.isMustUnderstandFaultcode(se)) {
      throw new EETest.Fault(
          "Invalid response: instances must generate a \"MustUnderstand\" "
              + "soap:Fault when a request contains a soap header with \"mustUnderstand=1\" "
              + "that is not understood (BP-R1027)");
    }

  }
}
