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

package com.sun.ts.tests.jaxrpc.wsi.w2j.rpc.literal.R1012;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.tests.jaxrpc.sharedclients.ClientFactory;
import com.sun.ts.tests.jaxrpc.sharedwebservices.faultservice.WSIConstants;
import com.sun.ts.tests.jaxrpc.wsi.constants.SOAPConstants;
import com.sun.ts.tests.jaxrpc.wsi.requests.SOAPRequests;

/**
 * Tests R1012 in the WSI Basic Profile 1.0: A MESSAGE MUST be serialized as
 * either UTF-8 or UTF-16.
 */
public class Client extends ServiceEETest
    implements SOAPConstants, WSIConstants, SOAPRequests {

  /**
   * The string to be echoed for request two.
   */
  private static final String STRING_2 = "R1012-2";

  /**
   * The one client.
   */
  private W2JRLR1012ClientOne client1;

  /**
   * The other client.
   */
  private W2JRLR1012ClientTwo client2;

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
    client1 = (W2JRLR1012ClientOne) ClientFactory
        .getClient(W2JRLR1012ClientOne.class, properties);
    client2 = (W2JRLR1012ClientTwo) ClientFactory
        .getClient(W2JRLR1012ClientTwo.class, properties);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testResponseEncoding
   *
   * @assertion_ids: JAXRPC:WSI:R1012
   *
   * @test_Strategy: A valid request is made to the endpoint and the returned
   *                 response is investigated in order to determine the
   *                 encoding.
   *
   * @throws Fault
   */
  public void testResponseEncoding() throws Fault {
    InputStream is;
    Charset cs = Charset.forName("UTF-8");
    try {
      is = client1.makeHTTPRequest(R1012_REQUEST, cs);
      String contentType = client1.getResponseHeader("Content-Type");
      if (contentType != null) {
        int index = contentType.toLowerCase().indexOf("charset=");
        if (index > 0) {
          String name = contentType.substring(index + 8).trim();
          if (name.charAt(0) == '"')
            name = name.substring(1, name.length() - 1);
          if ((name.equalsIgnoreCase("UTF-8"))
              || name.equalsIgnoreCase("UTF-16")) {
            char c = name.charAt(0);
            if ((c == '\"') || (c == '\'')) {
              name = name.substring(1, name.length() - 1);
            }
            cs = Charset.forName(name);
          } else {
            throw new Fault("Response encoded in '" + name + "' (BP-R1012)");
          }
        }
      }
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R1012)", e);
    }
    InputStreamReader isr = new InputStreamReader(is, cs);
    try {
      char[] buffer = new char[1024];
      int length;
      do {
        length = isr.read(buffer);
      } while (length > 0);
    } catch (IOException e) {
      throw new Fault("Unable to read response from endpoint (BP-R1012)", e);
    }
  }

  /**
   * @testName: testRequestEncoding
   *
   * @assertion_ids: JAXRPC:WSI:R1012
   *
   * @test_Strategy: A request is made from the generated client. The endpoint
   *                 is replaced by a Filter, that verified the encoding. The
   *                 returned string indicates the success or failure.
   *
   * @throws Fault
   */
  public void testRequestEncoding() throws Fault {
    String result;
    try {
      result = client2.echoString(STRING_2);
    } catch (Exception e) {
      throw new Fault("Unable to invoke echoString operation (BP-R1012)", e);
    }
    if (!result.equals("OK")) {
      if (result.equals("EXCEPTION")) {
        throw new Fault("Endpoint unable to process request (BP-R1012)");
      } else {
        throw new Fault(
            "Request encoding neither 'UTF-8' nor 'UTF-16' (BP-R1012)");
      }
    }
  }
}
