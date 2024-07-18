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
package com.sun.ts.tests.jaxws.wsi.w2j.rpc.literal.R2010;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.sharedclients.ClientFactory;
import java.math.BigInteger;
import com.sun.ts.lib.harness.*;

public class Client extends ServiceEETest {
  /**
   * The string to be echoed.
   */
  private static final String R = "R";

  private static final String NUMBER = "2010";

  private static final String STRING = R + NUMBER;

  /**
   * The integer to be echoed.
   */
  private static final BigInteger BIGINTEGER = new BigInteger(NUMBER);

  /**
   * The client.
   */
  private W2JRLR2010Client client;

  static W2JRLR2010TestService service = null;

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
   * @class.testArgs: -ap jaxws-url-props.dat
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   *
   * @param args
   * @param properties
   *
   * @throws Fault
   */
  public void setup(String[] args, Properties properties) throws Fault {
    client = (W2JRLR2010Client) ClientFactory.getClient(W2JRLR2010Client.class,
        properties, this, service);
    logMsg("setup ok");
  }

  public void cleanup() {
    logMsg("cleanup");
  }

  /**
   * @testName: testImportDirectlyUTF8Schema
   *
   * @assertion_ids: WSI:SPEC:R2010
   *
   * @test_Strategy: The supplied WSDL, directly imports an XML Schema that uses
   *                 UTF-8 encoding which has been used by the WSDL-to-Java tool
   *                 to generate an end point. If the tool works correctly, the
   *                 end-point has been built and deployed, so it should simply
   *                 be reachable.
   *
   * @throws Fault
   */
  public void testImportDirectlyUTF8Schema() throws Fault {
    String result;
    try {
      result = client.echoImportDirectlyUTF8Test(STRING);
    } catch (Exception e) {
      throw new Fault(
          "Unable to invoke echoImportDirectlyUTF8Test operation (BP-R2010)",
          e);
    }
    if (!STRING.equals(result)) {
      throw new Fault("echoImportDirectlyUTF8Test operation returns '" + result
          + "' in stead of '" + STRING + "' (BP-R2010)");
    }
  }

  /**
   * @testName: testImportDirectlyUTF16Schema
   *
   * @assertion_ids: WSI:SPEC:R2010
   *
   * @test_Strategy: The supplied WSDL, directly imports an XML Schema that uses
   *                 UTF-16 encoding which has been used by the WSDL-to-Java
   *                 tool to generate an end point. If the tool works correctly,
   *                 the end-point has been built and deployed, so it should
   *                 simply be reachable.
   *
   * @throws Fault
   */
  public void testImportDirectlyUTF16Schema() throws Fault {
    BigInteger result;
    try {
      result = client.echoImportDirectlyUTF16Test(BIGINTEGER);
    } catch (Exception e) {
      throw new Fault(
          "Unable to invoke echoImportDirectlyUTF16Test operation (BP-R2010)",
          e);
    }
    if (!BIGINTEGER.equals(result)) {
      throw new Fault("echoImportDirectlyUTF16Test operation returns '" + result
          + "' in stead of '" + BIGINTEGER + "' (BP-R2010)");
    }
  }

  /**
   * @testName: testImportIndirectlyUTF8Schema
   *
   * @assertion_ids: WSI:SPEC:R2010
   *
   * @test_Strategy: The supplied WSDL, directly imports an XML Schema that uses
   *                 UTF-8 encoding which has been used by the WSDL-to-Java tool
   *                 to generate an end point. If the tool works correctly, the
   *                 end-point has been built and deployed, so it should simply
   *                 be reachable.
   *
   * @throws Fault
   */
  public void testImportIndirectlyUTF8Schema() throws Fault {
    String result;
    try {
      result = client.echoImportIndirectlyUTF8Test(STRING);
    } catch (Exception e) {
      throw new Fault(
          "Unable to invoke echoImportIndirectlyUTF8Test operation (BP-R2010)",
          e);
    }
    if (!STRING.equals(result)) {
      throw new Fault("echoImportIndirectlyUTF8Test operation returns '"
          + result + "' in stead of '" + STRING + "' (BP-R2010)");
    }
  }

  /**
   * @testName: testImportIndirectlyUTF16Schema
   *
   * @assertion_ids: WSI:SPEC:R2010
   *
   * @test_Strategy: The supplied WSDL, directly imports an XML Schema that uses
   *                 UTF-16 encoding which has been used by the WSDL-to-Java
   *                 tool to generate an end point. If the tool works correctly,
   *                 the end-point has been built and deployed, so it should
   *                 simply be reachable.
   *
   * @throws Fault
   */
  public void testImportIndirectlyUTF16Schema() throws Fault {
    BigInteger result;
    try {
      result = client.echoImportIndirectlyUTF16Test(BIGINTEGER);
    } catch (Exception e) {
      throw new Fault(
          "Unable to invoke echoImportIndirectlyUTF16Test operation (BP-R2010)",
          e);
    }
    if (!BIGINTEGER.equals(result)) {
      throw new Fault("echoImportIndirectlyUTF16Test operation returns '"
          + result + "' in stead of '" + BIGINTEGER + "' (BP-R2010)");
    }
  }

}
