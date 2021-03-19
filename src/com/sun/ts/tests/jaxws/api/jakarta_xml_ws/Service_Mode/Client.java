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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Service_Mode;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {

  // Expected Enum Constant Summary
  private final static jakarta.xml.ws.Service.Mode expectedEnums[] = {
      jakarta.xml.ws.Service.Mode.MESSAGE, jakarta.xml.ws.Service.Mode.PAYLOAD, };

  private boolean findEnums(jakarta.xml.ws.Service.Mode[] args) {
    boolean pass = true;
    boolean found;
    for (jakarta.xml.ws.Service.Mode a : args) {
      found = false;
      TestUtil.logMsg("Searching expected list of enums for " + a);
      for (jakarta.xml.ws.Service.Mode b : expectedEnums) {
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

  private void printEnums(jakarta.xml.ws.Service.Mode[] args) {
    TestUtil.logMsg("Print Enums");
    TestUtil.logMsg("-----------");
    for (jakarta.xml.ws.Service.Mode c : args)
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
   * @assertion_ids: JAXWS:JAVADOC:60; JAXWS:JAVADOC:61;
   *
   * @test_Strategy: Verify jakarta.xml.ws.Service.Mode.values() returns array
   * containing the constants of this enum type.
   */
  public void valuesTest() throws Fault {
    TestUtil.logTrace("valuesTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call jakarta.xml.ws.Service.Mode.values() ...");
      jakarta.xml.ws.Service.Mode[] methods = jakarta.xml.ws.Service.Mode.values();
      printEnums(methods);
      pass = findEnums(methods);
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
   * @assertion_ids: JAXWS:JAVADOC:59;
   *
   * @test_Strategy: Verify jakarta.xml.ws.Service.Mode.valueOf(String name)
   * returns the enum constant of this type with specified name.
   */
  public void valueOfTest() throws Fault {
    TestUtil.logTrace("valuesTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call jakarta.xml.ws.Service.Mode.valueOf(MESSAGE) ...");
      jakarta.xml.ws.Service.Mode method = jakarta.xml.ws.Service.Mode
          .valueOf("MESSAGE");
      if (method != jakarta.xml.ws.Service.Mode.MESSAGE) {
        TestUtil.logErr(
            "jakarta.xml.ws.Service.Mode.valueOf(MESSAGE) failed:" + " expected: "
                + jakarta.xml.ws.Service.Mode.MESSAGE + ", received: " + method);
        pass = false;
      } else {
        TestUtil.logMsg("jakarta.xml.ws.Service.Mode.valueOf(MESSAGE) passed");
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
