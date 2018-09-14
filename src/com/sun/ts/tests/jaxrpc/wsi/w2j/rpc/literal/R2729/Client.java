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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R2729;

import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

/**
 * Tests R2728 in the WSI Basic Profile 1.0: A MESSAGE described with an
 * rpc-literal binding that is a response message MUST have a wrapper element
 * whose name is the corresponding wsdl:operation name suffixed with the string
 * "Response".
 */
public class Client extends ServiceEETest implements SOAPRequests {
  /**
   * The string to be echoed.
   */
  private static final String STRING = "R2729";

  /**
   * The client.
   */
  private W2JRLR2729Client client;

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
    client = (W2JRLR2729Client) ClientFactory.getClient(W2JRLR2729Client.class,
        properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testResponseWrapperElement
   *
   * @assertion_ids: JAXRPC:WSI:R2729
   *
   * @test_Strategy: A request to the echoString operation is made and the
   *                 returned wrapper element must be "echoStringResponse".
   *
   * @throws Fault
   */
  public void testResponseWrapperElement() throws Fault {
    Document document;
    try {
      InputStream is = client.makeHTTPRequest(R2729_REQUEST);
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      document = builder.parse(is);
    } catch (Exception e) {
      throw new Fault("Unable to invoke 'echoString' operation (BP-R2729)", e);
    }
    Element envelope = document.getDocumentElement();
    System.out.println(
        "got " + envelope.getNamespaceURI() + ":" + envelope.getLocalName());
    NodeList list = envelope.getElementsByTagNameNS(
        "http://w2jrlr2729testservice.org/W2JRLR2729TestService.wsdl",
        "echoStringResponse");
    if (list.getLength() == 0) {
      throw new Fault(
          "Required 'echoStringResponse' element not present in message (BP-R2729)");
    }
  }
}
