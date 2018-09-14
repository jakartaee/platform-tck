/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsonp.api.jsoncoding;

import java.util.Properties;

import javax.json.Json;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;

public class Client extends ServiceEETest {
  private static final long serialVersionUID = 11L;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /* Tests */

  /*
   * @testName: jsonEncodeTest
   * 
   * @assertion_ids: JSONP:JAVADOC:681; JSONP:JAVADOC:682;
   * 
   * @test_Strategy: Encode and decode Json Pointer as defined by RFC 6901
   */
  public void jsonEncodeTest() throws Fault {
    String DECODED = "/a/~b/c";
    String ENCODED = "~1a~1~0b~1c";
    StringBuilder error = new StringBuilder();
    logMsg("----------------------------------------------");
    logMsg("Test encode " + DECODED);
    logMsg("----------------------------------------------");
    String encoded = Json.encodePointer(DECODED);
    if (!ENCODED.equals(encoded))
      error.append("The pointer ").append(DECODED)
          .append(" has been encoded as ").append(encoded).append('\n');

    logMsg("----------------------------------------------");
    logMsg("Test decode " + ENCODED);
    String decoded = Json.decodePointer(ENCODED);
    if (!DECODED.equals(decoded))
      error.append("The pointer ").append(ENCODED)
          .append(" has been decoded as ").append(decoded).append('\n');
    if (error.length() != 0)
      throw new Fault(error.toString());
    logMsg("----------------------------------------------");
  }
}
