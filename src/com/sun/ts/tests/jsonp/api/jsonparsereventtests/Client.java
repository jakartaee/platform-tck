/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsonp.api.jsonparsereventtests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.json.stream.*;
import java.io.*;
import java.util.*;

import com.sun.javatest.Status;

import com.sun.ts.tests.jsonp.common.*;

public class Client extends ServiceEETest {

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
   * @testName: jsonValueOfTest
   * 
   * @assertion_ids: JSONP:JAVADOC:128;
   * 
   * @test_Strategy: Test JsonParser.Event.valueOf() API method call with all
   * JsonParser.Event types.
   *
   */
  public void jsonValueOfTest() throws Fault {
    boolean pass = true;

    String eventTypeStrings[] = { "END_ARRAY", "END_OBJECT", "KEY_NAME",
        "START_ARRAY", "START_OBJECT", "VALUE_FALSE", "VALUE_NULL",
        "VALUE_NUMBER", "VALUE_STRING", "VALUE_TRUE" };

    for (String eventTypeString : eventTypeStrings) {
      JsonParser.Event eventType;
      try {
        logMsg(
            "Testing enum value for string constant name " + eventTypeString);
        eventType = JsonParser.Event.valueOf(eventTypeString);
        logMsg("Got enum type " + eventType + " for enum string constant named "
            + eventTypeString);
      } catch (Exception e) {
        logErr("Caught unexpected exception: " + e);
        pass = false;
      }

    }

    logMsg("Testing negative test case for NullPointerException");
    try {
      JsonParser.Event.valueOf(null);
      logErr("did not get expected NullPointerException");
      pass = false;
    } catch (NullPointerException e) {
      logMsg("Got expected NullPointerException");
    } catch (Exception e) {
      logErr("Got unexpected exception " + e);
      pass = false;
    }

    logMsg("Testing negative test case for IllegalArgumentException");
    try {
      JsonParser.Event.valueOf("INVALID");
      logErr("did not get expected IllegalArgumentException");
      pass = false;
    } catch (IllegalArgumentException e) {
      logMsg("Got expected IllegalArgumentException");
    } catch (Exception e) {
      logErr("Got unexpected exception " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("jsonValueOfTest Failed");
  }

  /*
   * @testName: jsonValuesTest
   * 
   * @assertion_ids: JSONP:JAVADOC:129;
   * 
   * @test_Strategy: Test JsonParser.Event.values() API method call and verify
   * enums returned.
   *
   */
  public void jsonValuesTest() throws Fault {
    boolean pass = true;

    logMsg(
        "Testing API method JsonParser.Event.values() to return array of enums.");
    JsonParser.Event[] values = JsonParser.Event.values();

    for (JsonParser.Event eventType : values) {
      String eventString = JSONP_Util.getEventTypeString(eventType);
      if (eventString == null) {
        logErr("Got no value for enum " + eventType);
        pass = false;
      } else
        logMsg("Got " + eventString + " for enum " + eventType);
    }

    if (!pass)
      throw new Fault("jsonValuesTest Failed");
  }
}
