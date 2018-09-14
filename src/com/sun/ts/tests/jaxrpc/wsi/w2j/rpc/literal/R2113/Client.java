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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R2113;

import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.utils.SOAPUtils;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.Name;
import java.util.Properties;
import java.util.Iterator;

/**
 * Tests R2113 in the WSI Basic Profile 1.0: A MESSAGE containing serialized
 * arrays MUST NOT include the soapenc:arrayType attribute.
 */
public class Client extends ServiceEETest implements SOAPRequests {

  private W2JRLR2113Client client;

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
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void setup(String[] args, Properties properties) throws EETest.Fault {
    client = (W2JRLR2113Client) ClientFactory.getClient(W2JRLR2113Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testNoArrayTypeInResponse
   *
   * @assertion_ids: JAXRPC:WSI:R2113
   *
   * @test_Strategy: Make a request and inspect response to ensure
   *                 soap-enc:arrayType attribute is not included.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoArrayTypeInResponse() throws EETest.Fault {
    SOAPMessage response = null;
    try {
      response = client.makeSaajRequest(ARRAY_OPERATION);
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    try {
      validateNoArrayType(response);
    } catch (SOAPException se) {
      throw new EETest.Fault("Error creating response object", se);
    }
    client.logMessageInHarness(response);
  }

  /**
   * @testName: testNoArrayTypeInRequest
   *
   * @assertion_ids: JAXRPC:WSI:R2113
   *
   * @test_Strategy: Make a request and inspect response to ensure
   *                 soap-enc:arrayType attribute is not included.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void testNoArrayTypeInRequest() throws EETest.Fault {
    String response = "";
    try {
      response = client
          .arrayOperationFromClient(new String[] { "one", "two", "three" });
    } catch (Exception e) {
      throw new EETest.Fault("Test didn't complete properly: ", e);
    }
    if (response.startsWith("failed")) {
      throw new EETest.Fault(response);
    }
  }

  private void validateNoArrayType(SOAPMessage response)
      throws EETest.Fault, SOAPException {
    String prefix = getSOAPEncodingNamespacePrefix(
        response.getSOAPPart().getEnvelope().getBody());
    if (prefix == null) {
      prefix = getSOAPEncodingNamespacePrefix(
          response.getSOAPPart().getEnvelope());
    }
    if (prefix != null) {
      Name soapEncArrayType = response.getSOAPPart().getEnvelope()
          .createName("arrayType", prefix, SOAPUtils.URI_NS_SOAP_ENCODING);
      String soapEncArrayTypeValue = response.getSOAPPart().getEnvelope()
          .getBody().getAttributeValue(soapEncArrayType);
      if (soapEncArrayTypeValue == null || soapEncArrayTypeValue.equals("")) {
        soapEncArrayTypeValue = response.getSOAPPart().getEnvelope()
            .getAttributeValue(soapEncArrayType);
      }
      if (!(soapEncArrayTypeValue == null
          || soapEncArrayTypeValue.equals(""))) {
        client.logMessageInHarness(response);
        throw new EETest.Fault(
            "Invalid element: messages may not use a soapEnc:arrayType attribute (BP-R2113)");
      }
    }
  }

  private String getSOAPEncodingNamespacePrefix(SOAPElement element) {
    Iterator attributes = element.getNamespacePrefixes();
    String prefix = null;
    boolean done = false;
    while (attributes.hasNext() && !done) {
      prefix = (String) attributes.next();
      if (element.getNamespaceURI(prefix)
          .equals(SOAPUtils.URI_NS_SOAP_ENCODING)) {
        done = true;
      }
    }
    return prefix;
  }
}
