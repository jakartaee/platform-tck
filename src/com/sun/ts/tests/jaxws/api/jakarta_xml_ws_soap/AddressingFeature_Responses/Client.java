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
 * $Id: Client.java 53493 2007-05-22 17:06:35Z adf $
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_soap.AddressingFeature_Responses;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.soap.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {

  // Expected Enum Constant Summary
  private final static jakarta.xml.ws.soap.AddressingFeature.Responses expectedEnums[] = {
      jakarta.xml.ws.soap.AddressingFeature.Responses.ALL,
      jakarta.xml.ws.soap.AddressingFeature.Responses.ANONYMOUS,
      jakarta.xml.ws.soap.AddressingFeature.Responses.NON_ANONYMOUS, };

  private boolean findEnums(
      jakarta.xml.ws.soap.AddressingFeature.Responses[] args) {
    boolean pass = true;
    boolean found;
    for (jakarta.xml.ws.soap.AddressingFeature.Responses a : args) {
      found = false;
      TestUtil.logMsg("Searching expected list of enums for " + a);
      for (jakarta.xml.ws.soap.AddressingFeature.Responses b : expectedEnums) {
        if (a == b) {
          found = true;
          break;
        }
      }
      if (!found) {
        pass = false;
        TestUtil.logErr("No enum found for " + a);
      } else {
        TestUtil.logMsg("Enum found for " + a);
      }
    }
    return pass;
  }

  private void printEnums(
      jakarta.xml.ws.soap.AddressingFeature.Responses[] args) {
    TestUtil.logMsg("Print Enums");
    TestUtil.logMsg("-----------");
    for (jakarta.xml.ws.soap.AddressingFeature.Responses c : args)
      TestUtil.logMsg("" + c);
  }

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

  /*
   * @testName: valuesTest
   *
   * @assertion_ids: JAXWS:JAVADOC:226;
   *
   * @test_Strategy: Verify
   * jakarta.xml.ws.soap.AddressingFeature.Responses.values() returns array
   * containing the constants of this enum type.
   */
  public void valuesTest() throws Fault {
    TestUtil.logTrace("valuesTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Call jakarta.xml.ws.soap.AddressingFeature.Responses.values() ...");
      jakarta.xml.ws.soap.AddressingFeature.Responses[] responses = jakarta.xml.ws.soap.AddressingFeature.Responses
          .values();
      printEnums(responses);
      pass = findEnums(responses);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("valuesTest failed", e);
    }

    if (!pass)
      throw new Fault("valuesTest failed");
  }

  /*
   * @testName: valueOfTest
   *
   * @assertion_ids: JAXWS:JAVADOC:225;
   *
   * @test_Strategy: Verify
   * jakarta.xml.ws.soap.AddressingFeature.Responses.valueOf(String name) returns
   * the enum constant of this type with specified name.
   */
  public void valueOfTest() throws Fault {
    TestUtil.logTrace("valuesTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Call jakarta.xml.ws.soap.AddressingFeature.Responses.valueOf(ALL) ...");
      jakarta.xml.ws.soap.AddressingFeature.Responses responses = jakarta.xml.ws.soap.AddressingFeature.Responses
          .valueOf("ALL");
      if (responses != jakarta.xml.ws.soap.AddressingFeature.Responses.ALL) {
        TestUtil.logErr(
            "jakarta.xml.ws.soap.AddressingFeature.Responses.valueOf(ALL) failed:"
                + " expected: "
                + jakarta.xml.ws.soap.AddressingFeature.Responses.ALL
                + ", received: " + responses);
        pass = false;
      } else {
        TestUtil.logMsg(
            "jakarta.xml.ws.soap.AddressingFeature.Responses.valueOf(ALL) passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("valuesTest failed", e);
    }

    if (!pass)
      throw new Fault("valuesTest failed");
  }
}
