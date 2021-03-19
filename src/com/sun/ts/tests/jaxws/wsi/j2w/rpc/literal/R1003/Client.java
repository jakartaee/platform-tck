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

package com.sun.ts.tests.jaxws.wsi.j2w.rpc.literal.R1003;

import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.faultclient.*;
import com.sun.javatest.Status;

import jakarta.xml.ws.soap.SOAPFaultException;
import java.util.Properties;

import com.sun.ts.lib.harness.*;

public class Client extends ServiceEETest {

  private FaultTestClient client;

  static FaultTest service = null;

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
    client = (FaultTestClient) ClientFactory.getClient(FaultTestClient.class,
        properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testSOAPFaultExceptionDetailNoAttributes
   *
   * @assertion_ids: WSI:SPEC:R1003
   *
   * @test_Strategy: Make a request that generates a soap:Fault with a detail
   *                 element with no attributes.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testSOAPFaultExceptionDetailNoAttributes() throws EETest.Fault {
    try {
      client.alwaysThrowsSOAPFaultExceptionDetailNoAttributes();
    } catch (SOAPFaultException sfe) {
      // expected result
    } catch (jakarta.xml.ws.WebServiceException re) {
      // expected result
    } catch (Exception e) {
      throw new EETest.Fault(
          "Error processing received fault: should have accepted a soap:Fault"
              + "with a detail element with no attributes (BP-R1003)");
    }
  }

  /**
   * @testName: testSOAPFaultExceptionDetailQualifiedAttributes
   *
   * @assertion_ids: WSI:SPEC:R1002
   *
   * @test_Strategy: Make a request that generates a soap:Fault with a detail
   *                 element with qualified attributes.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testSOAPFaultExceptionDetailQualifiedAttributes()
      throws EETest.Fault {
    try {
      client.alwaysThrowsSOAPFaultExceptionDetailQualifiedAttributes();
    } catch (SOAPFaultException sfe) {
      // expected result
    } catch (jakarta.xml.ws.WebServiceException re) {
      // expected result
    } catch (Exception e) {
      throw new EETest.Fault(
          "Error processing received fault: should have accepted a soap:Fault"
              + "with a detail element with qualified attributes (BP-R1003)");
    }
  }
}
