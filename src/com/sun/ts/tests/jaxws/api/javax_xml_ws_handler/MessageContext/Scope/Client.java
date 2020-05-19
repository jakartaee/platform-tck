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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_handler.MessageContext.Scope;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.handler.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  // Expected Enum Constant Summary
  private final static MessageContext.Scope expectedEnums[] = {
      MessageContext.Scope.APPLICATION, MessageContext.Scope.HANDLER, };

  private boolean findEnums(MessageContext.Scope[] args) {
    boolean pass = true;
    boolean found;
    for (MessageContext.Scope a : args) {
      found = false;
      TestUtil.logMsg("Searching expected list of enums for " + a);
      for (MessageContext.Scope b : expectedEnums) {
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

  private void printEnums(MessageContext.Scope[] args) {
    TestUtil.logMsg("Print Enums");
    TestUtil.logMsg("-----------");
    for (MessageContext.Scope c : args)
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
   * @assertion_ids: JAXWS:JAVADOC:95; WS4EE:SPEC:6012;
   *
   * @test_Strategy: Verify MessageContext.Scope.values() returns array
   * containing the constants of this enum type.
   */
  public void valuesTest() throws Fault {
    TestUtil.logTrace("valuesTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call MessageContext.Scope.values() ...");
      MessageContext.Scope[] methods = MessageContext.Scope.values();
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
   * @assertion_ids: JAXWS:JAVADOC:94; WS4EE:SPEC:6012;
   *
   * @test_Strategy: Verify MessageContext.Scope.valueOf(String name) returns
   * the enum constant of this type with specified name.
   */
  public void valueOfTest() throws Fault {
    TestUtil.logTrace("valuesTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Call MessageContext.Scope.valueOf(APPLICATION) ...");
      MessageContext.Scope method = MessageContext.Scope.valueOf("APPLICATION");
      if (method != MessageContext.Scope.APPLICATION) {
        TestUtil.logErr(
            "MessageContext.Scope.valueOf(APPLICATION) failed:" + " expected: "
                + MessageContext.Scope.APPLICATION + ", received: " + method);
        pass = false;
      } else {
        TestUtil.logMsg("MessageContext.Scope.valueOf(APPLICATION) passed");
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
