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

package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R273X;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxws.wsi.requests.SOAPRequests;
import jakarta.xml.soap.*;
import java.util.Iterator;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.lib.util.TestUtil;

import com.sun.ts.lib.harness.*;

public class Client extends ServiceEETest implements SOAPRequests {

  private static String OPERATION_NAME = "echoFooBarResponse";

  private static String OPERATION_URI = "http://w2jrlr273Xtestservice.org/W2JRLR273XTestService.wsdl";

  private static String PART_ACCESSOR_NAME = "fooBarResponse";

  private static String PART1_NAME = "foo";

  private static String PART1_URI = "http://w2jrlr273Xtestservice.org/xsd";

  private static String PART2_NAME = "bar";

  private static String PART2_URI = "http://w2jrlr273Xtestservice.org/xsd";

  /**
   * The clients.
   */
  private W2JRLR273XClient1 client1;

  private W2JRLR273XClient2 client2;

  static W2JRLR273XTestService service = null;

  /**
   * Test entry.
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
    client1 = (W2JRLR273XClient1) ClientFactory
        .getClient(W2JRLR273XClient1.class, properties, this, service);
    client2 = (W2JRLR273XClient2) ClientFactory
        .getClient(W2JRLR273XClient2.class, properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testNamespacesForChildrenOfPartAccessorsOnRequest
   *
   * @assertion_ids: WSI:SPEC:R2735; WSI:SPEC:R2737;
   *
   * @test_Strategy: A request is made from the generated client. The endpoint
   *                 is replaced by a Servlet Filter, that verifies the request.
   *                 The returned object is interrogated for success of failure.
   *
   * @throws Fault
   */
  public void testNamespacesForChildrenOfPartAccessorsOnRequest() throws Fault {
    FooBar fb = new FooBar();
    String expected = "I am a foo request";
    fb.setFoo(expected);
    fb.setBar("I am a bar request");
    FooBar fb2;
    try {
      fb2 = client2.echoFooBar(fb);
      if (!fb2.getFoo().equals(expected)) {
        TestUtil.logErr("Expected value=" + expected);
        TestUtil.logErr("Actual value=" + fb2.getFoo());
        throw new Fault(
            "testNamespacesForChildrenOfPartAccessorsOnRequest failed (BP-R273X)");
      }
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoFooBar operation (BP-R273X)", e);
    }
  }

  /**
   * @testName: testNamespacesForChildrenOfPartAccessorsOnResponse
   *
   * @assertion_ids: WSI:SPEC:R2735; WSI:SPEC:R2737;
   *
   * @test_Strategy: A valid request is made to the endpoint and the returned
   *                 response is investigated for the presence of namespaces for
   *                 children of part accessor.
   *
   * @throws Fault
   */
  public void testNamespacesForChildrenOfPartAccessorsOnResponse()
      throws Fault {
    SOAPMessage response;
    try {
      response = client1.makeSaajRequest(R273X_REQUEST);
      String result = verifyNamespacesForChildrenOfPartAccessors(response);
      if (!result.equals("OK")) {
        TestUtil.logErr("Expected value=OK");
        TestUtil.logErr("Actual value=" + result);
        throw new Fault(
            "testNamespacesForChildrenOfPartAccessorsOnResponse failed (BP-R273X)");
      }
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoFooBar operation (BP-R273X)", e);
    }
  }

  /**
   * Verifies that the children of part accessor for rpc-literal SOAP messages
   * parameter and return values are namespace qualified.
   * 
   * @param request
   *          the SOAPMessage response.
   * 
   * @return "OK" if valid; "NOT OK" otherwise.
   * 
   * @throws Exception
   */
  private String verifyNamespacesForChildrenOfPartAccessors(SOAPMessage sm)
      throws Exception {
    String result = "NOT OK";
    System.out.println("Getting children of body element ...");
    Iterator children = sm.getSOAPBody().getChildElements();
    SOAPElement child;
    String localName, uri;
    boolean flg1 = false, flg2 = false, flg3 = false;
    if (children.hasNext()) {
      System.out.println("Getting operation name ...");
      child = (SOAPElement) children.next();
      localName = child.getElementName().getLocalName();
      uri = child.getElementName().getURI();
      System.out.println("child localname: " + localName);
      System.out.println("child uri: " + uri);
      if (localName.equals(OPERATION_NAME) && uri.equals(OPERATION_URI)) {
        children = child.getChildElements();
        if (children.hasNext()) {
          System.out.println("Getting part accessor name ...");
          child = (SOAPElement) children.next();
          localName = child.getElementName().getLocalName();
          uri = child.getElementName().getURI();
          System.out.println("  child localname: " + localName);
          System.out.println("  child uri: " + uri);
          if (localName.equals(PART_ACCESSOR_NAME)
              && (uri == null || uri.equals("")))
            flg1 = true;
          // All children of part accessors MUST be namespace qualified
          if (!children.hasNext()) {
            System.out.println("Getting children of part accessor ...");
            children = child.getChildElements();
            int i = 0;
            System.out.println(
                "Verifying namespaces exist and are correct for children");
            while (children.hasNext()) {
              i++;
              child = (SOAPElement) children.next();
              localName = child.getElementName().getLocalName();
              uri = child.getElementName().getURI();
              System.out.println("    child localname: " + localName);
              System.out.println("    child uri: " + uri);
              switch (i) {
              case 1:
                if (localName.equals(PART1_NAME) && uri.equals(PART1_URI))
                  flg2 = true;
                break;
              case 2:
                if (localName.equals(PART2_NAME) && uri.equals(PART2_URI))
                  flg3 = true;
                break;
              }
            }
          }
        }
      }
    }
    if (flg1 && flg2 && flg3)
      result = "OK";
    System.out.println("result=" + result);
    JAXWS_Util.printSOAPMessage(sm, System.out);
    return result;
  }
}
