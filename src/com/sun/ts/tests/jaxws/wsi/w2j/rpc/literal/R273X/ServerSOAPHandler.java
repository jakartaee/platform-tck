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

import com.sun.ts.tests.jaxws.common.HTTPSOAPHandler;
import com.sun.ts.tests.jaxws.common.JAXWS_Util;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceException;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

public class ServerSOAPHandler extends HTTPSOAPHandler {

  private String PASSED = "PASSED";

  private String FAILED = "FAILED";

  private static String OPERATION_NAME = "echoFooBar";

  private static String OPERATION_URI = "http://w2jrlr273Xtestservice.org/W2JRLR273XTestService.wsdl";

  private static String PART_ACCESSOR_NAME = "fooBarRequest";

  private static String PART1_NAME = "foo";

  private static String PART1_URI = "http://w2jrlr273Xtestservice.org/xsd";

  private static String PART2_NAME = "bar";

  private static String PART2_URI = "http://w2jrlr273Xtestservice.org/xsd";

  protected void processInboundMessage(SOAPMessageContext context) {
    System.out.println("in ServerSOAPHandler:processInboundMessage");
    String result = null;
    try {
      result = verifyNamespacesForChildrenOfPartAccessors(context);
    } catch (Exception e) {
      throw new RuntimeException(
          "Exception occurred in ServerSOAPHandler:verifyNamespacesForChildrenOfPartAccessors: "
              + e);
    }
    if (!result.equals(PASSED)) {
      throw new RuntimeException(
          "In ServerSOAPHandler:processInboundMessage: " + result);
    }
  }

  /**
   * Verifies that the part accessor for rpc-literal SOAP messages parameter and
   * return values have no namespace.
   */

  protected String verifyNamespacesForChildrenOfPartAccessors(
      SOAPMessageContext context) throws Exception {
    System.out.println(
        "in ServerSOAPHandler:verifyNamespacesForChildrenOfPartAccessors");
    String result = FAILED;
    SOAPMessage sm = context.getMessage();
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
      result = "PASSED";
    System.out.println("result=" + result);
    JAXWS_Util.printSOAPMessage(sm, System.out);
    return result;
  }
}
