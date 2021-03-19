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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R1016;

import com.sun.ts.lib.harness.*;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.ws.WebServiceException;
import java.util.Properties;
import java.util.Iterator;

public class Client extends ServiceEETest implements SOAPRequests {

  private W2JRLR1016Client client;

  static SimpleTest service = null;

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
    client = (W2JRLR1016Client) ClientFactory.getClient(W2JRLR1016Client.class,
        properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testCanAcceptXMLLangAttribute
   *
   * @assertion_ids: WSI:SPEC:R1016
   *
   * @test_Strategy: Make a request that generates a fault with an xml:lang
   *                 attribute on the faultstring element, ensure the client can
   *                 accept the fault
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testCanAcceptXMLLangAttribute() throws EETest.Fault {
    try {
      client.alwaysThrowsWebServiceException();
    } catch (WebServiceException e) {
      // expected result
      TestUtil.logMsg("Received WebServiceException");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
  }
}
