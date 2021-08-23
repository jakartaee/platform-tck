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

package com.sun.ts.tests.jaxws.wsi.w2j.document.literal.R2712;

import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;
import com.sun.javatest.Status;
import java.text.MessageFormat;

import jakarta.xml.ws.*;

import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPElement;
import java.util.Properties;
import java.util.Iterator;
import java.io.InputStream;
import com.sun.ts.tests.jaxws.common.*;

import com.sun.ts.lib.util.*;

public class Client extends ServiceEETest implements SOAPRequests {

  private W2JDLR2712ClientOne client1;

  private W2JDLR2712ClientTwo client2;

  private static String GLOBAL_ELEMENT = "HelloResponseElement";

  static W2JDLR2712TestService service = null;

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
    client1 = (W2JDLR2712ClientOne) ClientFactory
        .getClient(W2JDLR2712ClientOne.class, properties, this, service);
    client2 = (W2JDLR2712ClientTwo) ClientFactory
        .getClient(W2JDLR2712ClientTwo.class, properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: ChildElementInstanceOfGlobalElementInRequest
   *
   * @assertion_ids: WSI:SPEC:R2712
   *
   * @test_Strategy: Send a request and verify the child elements using a
   *                 servlet filter.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void ChildElementInstanceOfGlobalElementInRequest()
      throws EETest.Fault {
    HelloResponse result;
    HelloRequest hr = new HelloRequest();
    hr.setString("ChildElementInstanceOfGlobalElementInRequest");
    try {
      result = client2.hello(hr);
    } catch (Exception e) {
      throw new Fault("Failure with (BP-R2712)", e);
    }
  }

  /**
   * @testName: ChildElementInstanceOfGlobalElementInResponse
   *
   * @assertion_ids: WSI:SPEC:R2712
   *
   * @test_Strategy: Send a request to the endpoint which inturn sends a
   *                 response back. Verify the child elements in that response.
   *
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public void ChildElementInstanceOfGlobalElementInResponse()
      throws EETest.Fault {
    SOAPMessage response;
    try {
      String request = MessageFormat.format(R2712_REQUEST,
          "ChildElementInstanceOfGlobalElementInResponse");
      String expected = "PASSED";
      response = client1.makeSaajRequest(request);
      String result = verifyChildElement(response);
      if (!result.equals(expected)) {
        TestUtil.logErr("ERROR: incorrect result");
        TestUtil.logErr("expected=" + expected);
        TestUtil.logErr("actual=" + result);
        throw new Fault("Failure with (BP-R2712)");
      }
    } catch (Exception e) {
      throw new Fault("Failure with (BP-R2712)", e);
    }

  }

  /**
   * Verifies that the correct child element of the soap:body is returned
   *
   * @param request
   *          the SOAPMessage response.
   *
   * @return "PASSED" if valid; an error message otherwise.
   *
   * @throws Exception
   */
  protected String verifyChildElement(SOAPMessage sm) throws Exception {
    String result = "FAILED";
    System.out.println("Getting children of body element ...");
    Iterator children = sm.getSOAPBody().getChildElements();
    SOAPElement child;
    String localName;
    int count = 0;
    while (children.hasNext()) {
      count++;
      System.out.println("Getting operation name ...");
      child = (SOAPElement) children.next();
      localName = child.getElementName().getLocalName();
      System.out.println("child localname: " + localName);
      if (localName.equals(GLOBAL_ELEMENT)) {
        if (count == 1) {
          result = "PASSED";
        } else {
          result = "Error: The element '" + GLOBAL_ELEMENT + "' was found "
              + count + " time(s) in the soap:body";
        }
      } else {
        result = "Error: Expected element '" + GLOBAL_ELEMENT
            + "' in soap:body, instead got '" + localName + "'";
      }

    }
    if (count == 0) {
      result = "Error: no child elements were found in soap:body";
    }
    System.out.println("result=" + result);
    JAXWS_Util.printSOAPMessage(sm, System.out);
    return result;
  }
}
