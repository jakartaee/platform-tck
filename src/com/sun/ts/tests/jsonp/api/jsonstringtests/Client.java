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
package com.sun.ts.tests.jsonp.api.jsonstringtests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.json.*;
import javax.json.stream.*;
import java.io.*;

import com.sun.javatest.Status;
import java.util.Properties;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.BigInteger;

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
   * @testName: jsonStringEqualsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:254;
   * 
   * @test_Strategy: Tests JsonString equals method. Create 2 equal JsonStrings
   * and compare them for equality and expect true. Create 2 non-equal
   * JsonStrings and compare them for equality and expect false.
   */
  public void jsonStringEqualsTest() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create sample JsonString 1 for testing");
      JsonString string1 = (JsonString) JSONP_Util
          .createJsonString("Hello World");
      logMsg("string1=" + JSONP_Util.toStringJsonString(string1));

      logMsg("Create sample JsonString 2 for testing");
      JsonString string2 = JSONP_Util.createJsonString("Hello World");
      logMsg("string2=" + JSONP_Util.toStringJsonString(string2));

      logMsg(
          "Call JsonString.equals() to compare 2 equal JsonStrings and expect true");
      if (string1.equals(string2)) {
        logMsg("JsonStrings are equal - expected.");
      } else {
        pass = false;
        logErr("JsonStrings are not equal - unexpected.");
      }

      logMsg("Create sample JsonString 1 for testing");
      string1 = JSONP_Util.createJsonString("Hello World");
      logMsg("string1=" + JSONP_Util.toStringJsonString(string1));

      logMsg("Create sample JsonString 2 for testing");
      string2 = JSONP_Util.createJsonString("Hello USA");
      logMsg("string2=" + JSONP_Util.toStringJsonString(string2));

      logMsg(
          "Call JsonString.equals() to compare 2 equal JsonStrings and expect false");
      if (!string1.equals(string2)) {
        logMsg("JsonStrings are not equal - expected.");
      } else {
        pass = false;
        logErr("JsonStrings are equal - unexpected.");
      }
    } catch (Exception e) {
      throw new Fault("jsonStringEqualsTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonStringEqualsTest Failed");
  }

  /*
   * @testName: jsonStringHashCodeTest
   * 
   * @assertion_ids: JSONP:JAVADOC:255;
   * 
   * @test_Strategy: Tests JsonString equals method. Create 2 equal JsonStrings
   * and compare them for hashcode and expect true. Create 2 non-equal
   * JsonStrings and compare them for hashcode and expect false.
   */
  public void jsonStringHashCodeTest() throws Fault {
    boolean pass = true;
    try {
      logMsg("Create sample JsonString 1 for testing");
      JsonString string1 = JSONP_Util.createJsonString("Hello World");
      logMsg("string1=" + JSONP_Util.toStringJsonString(string1));
      logMsg("string1.hashCode()=" + string1.hashCode());

      logMsg("Create sample JsonString 2 for testing");
      JsonString string2 = JSONP_Util.createJsonString("Hello World");
      logMsg("string2=" + JSONP_Util.toStringJsonString(string2));
      logMsg("string2.hashCode()=" + string2.hashCode());

      logMsg(
          "Call JsonString.hashCode() to compare 2 equal JsonStrings and expect true");
      if (string1.hashCode() == string2.hashCode()) {
        logMsg("JsonStrings hashCode are equal - expected.");
      } else {
        pass = false;
        logErr("JsonStrings hashCode are not equal - unexpected.");
      }

      logMsg("Create sample JsonString 1 for testing");
      string1 = JSONP_Util.createJsonString("Hello World");
      logMsg("string1=" + JSONP_Util.toStringJsonString(string1));
      logMsg("string1.hashCode()=" + string1.hashCode());

      logMsg("Create sample JsonString 2 for testing");
      string2 = JSONP_Util.createJsonString("Hello USA");
      logMsg("string2=" + JSONP_Util.toStringJsonString(string2));
      logMsg("string2.hashCode()=" + string2.hashCode());

      logMsg(
          "Call JsonString.hashCode() to compare 2 equal JsonStrings and expect false");
      if (string1.hashCode() != string2.hashCode()) {
        logMsg("JsonStrings hashCode are not equal - expected.");
      } else {
        pass = false;
        logErr("JsonStrings hashCode are equal - unexpected.");
      }
    } catch (Exception e) {
      throw new Fault("jsonStringHashCodeTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonStringHashCodeTest Failed");
  }

  /*
   * @testName: jsonStringGetCharsTest
   * 
   * @assertion_ids: JSONP:JAVADOC:383;
   * 
   * @test_Strategy: Tests JsonString getChars method.
   */
  public void jsonStringGetCharsTest() throws Fault {
    boolean pass = true;
    String helloWorld = "Hello World";

    try {
      logMsg("Create sample JsonString for testing");
      JsonString string = JSONP_Util.createJsonString(helloWorld);
      logMsg("string=" + JSONP_Util.toStringJsonString(string));

      logMsg(
          "Call JsonString.getChars() to return the char sequence for the JSON string");
      CharSequence cs = string.getChars();
      logMsg("charSequence=" + cs.toString());

      logMsg("Checking char sequence for equality to expected string contents");
      if (!JSONP_Util.assertEquals(helloWorld, cs.toString()))
        pass = false;

      logMsg("Checking char sequence for expected equality to string length");
      if (!JSONP_Util.assertEquals(helloWorld.length(), cs.length()))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonStringGetCharsTest Failed: ", e);
    }
    if (!pass)
      throw new Fault("jsonStringGetCharsTest Failed");
  }
}
