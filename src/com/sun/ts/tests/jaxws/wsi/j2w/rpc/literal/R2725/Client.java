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

package com.sun.ts.tests.jaxws.wsi.j2w.rpc.literal.R2725;

import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.sharedclients.simpleclient.*;
import com.sun.ts.tests.jaxws.wsi.utils.SOAPUtils;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import java.util.Properties;

import com.sun.ts.lib.harness.*;

public class Client extends ServiceEETest implements SOAPRequests {

  private SimpleTestClient client;

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
    client = (SimpleTestClient) ClientFactory.getClient(SimpleTestClient.class,
        properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testVersionMismatchFaultcode
   *
   * @assertion_ids: WSI:SPEC:R2725
   *
   * @test_Strategy: Make a request and inspect response to ensure faultcode
   *                 "VersionMismatch" was sent.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testVersionMismatchFaultcode() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(BAD_SOAP_ENVELOPE);
      client.logMessageInHarness(response);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateVersionMismatchFaultcode(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
  }

  /**
   * @testName: testVersionMismatchFaultcodeWithMustUnderstand
   *
   * @assertion_ids: WSI:SPEC:R2725
   *
   * @test_Strategy: Make a request and inspect response to ensure faultcode
   *                 "VersionMismatch" was sent.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testVersionMismatchFaultcodeWithMustUnderstand()
      throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(BAD_SOAP_ENVELOPE_WITH_HEADER);
      client.logMessageInHarness(response);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateVersionMismatchFaultcode(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
  }

  /**
   * @testName: testVersionMismatchFaultcodeWithNonExistantOperation
   *
   * @assertion_ids: WSI:SPEC:R2725
   *
   * @test_Strategy: Make a request and inspect response to ensure faultcode
   *                 "VersionMismatch" was sent.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testVersionMismatchFaultcodeWithNonExistantOperation()
      throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client
          .makeSaajRequest(BAD_SOAP_ENVELOPE_NON_EXISTANT_OPERATION);
      client.logMessageInHarness(response);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateVersionMismatchFaultcode(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
  }

  /**
   * @testName: testMustUnderstandFaultcode
   *
   * @assertion_ids: WSI:SPEC:R2725
   *
   * @test_Strategy: Make a request and inspect response to ensure faultcode
   *                 "MustUnderstand" was sent.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testMustUnderstandFaultcode() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(MUST_UNDERSTAND_HEADER);
      client.logMessageInHarness(response);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateMustUnderstandFaultcode(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
  }

  /**
   * @testName: testMustUnderstandFaultcodeWithNonExistantOperation
   *
   * @assertion_ids: WSI:SPEC:R2725
   *
   * @test_Strategy: Make a request and inspect response to ensure faultcode
   *                 "MustUnderstand" was sent.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testMustUnderstandFaultcodeWithNonExistantOperation()
      throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client
          .makeSaajRequest(MUST_UNDERSTAND_HEADER_NON_EXISTANT_OPERATION);
      client.logMessageInHarness(response);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateMustUnderstandFaultcode(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
  }

  /**
   * @testName: testClientFaultcode
   *
   * @assertion_ids: WSI:SPEC:R2725
   *
   * @test_Strategy: Make a request and inspect response to ensure faultcode
   *                 "Client" was sent.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testClientFaultcode() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(NON_EXISTANT_OPERATION);
      client.logMessageInHarness(response);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateClientFaultcode(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
  }

  private void validateVersionMismatchFaultcode(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    if (!SOAPUtils.isVersionMismatchFaultcode(response)) {
      throw new EETest.Fault(
          "Invalid soap:Fault:  faultcode must be \"VersionMismatch\" (BP-R2725)");
    }
  }

  private void validateClientFaultcode(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    if (!SOAPUtils.isClientFaultcode(response)) {
      throw new EETest.Fault(
          "Invalid soap:Fault:  faultcode must be \"Client\" (BP-R2725)");
    }
  }

  private void validateMustUnderstandFaultcode(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    if (!SOAPUtils.isMustUnderstandFaultcode(response)) {
      throw new EETest.Fault(
          "Invalid soap:Fault:  faultcode must be \"MustUnderstand\" (BP-R2725)");
    }
  }
}
