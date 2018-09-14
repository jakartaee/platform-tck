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
package com.sun.ts.tests.jsonp.api.exceptiontests;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import javax.json.*;
import javax.json.stream.*;
import java.io.*;

import com.sun.javatest.Status;
import java.util.Properties;

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
   * @testName: jsonExceptionConstructorTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:37;
   * 
   * @test_Strategy: Test API: JsonException ret = new JsonException(String)
   */
  public void jsonExceptionConstructorTest1() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect.";

      logMsg("Test JsonException(String)");
      JsonException exception = new JsonException(message);
      try {
        throw exception;
      } catch (JsonException e) {
        if (!e.getMessage().equals(message)) {
          logErr("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jsonExceptionConstructorTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonExceptionConstructorTest1 Failed:");
  }

  /*
   * @testName: jsonExceptionConstructorTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:38;
   * 
   * @test_Strategy: Test API: JsonException ret = new JsonException(String,
   * Throwable)
   */
  public void jsonExceptionConstructorTest2() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");

      logMsg("Test JsonException(String, Throwable)");
      JsonException exception = new JsonException(message, foo);

      try {
        throw exception;
      } catch (JsonException e) {
        if (!e.getCause().equals(foo)) {
          logErr("Incorrect cause: expected " + foo + ", received "
              + e.getCause());
          pass = false;
        }
        if (!e.getMessage().equals(message)) {
          logErr("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonExceptionConstructorTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonExceptionConstructorTest2 Failed:");
  }

  /*
   * @testName: jsonParsingExceptionConstructorTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:285; JSONP:JAVADOC:478; JSONP:JAVADOC:474;
   * JSONP:JAVADOC:475; JSONP:JAVADOC:476;
   * 
   * @test_Strategy: Test API: JsonParsingException ret = new
   * JsonParsingException(String, JsonLocation)
   */
  public void jsonParsingExceptionConstructorTest1() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect.";
      MyJsonLocation expLoc = new MyJsonLocation(10, 20, 30);
      logMsg("MyJsonLocation");
      JSONP_Util.dumpLocation(expLoc);

      logMsg("Test JsonParsingException(String, JsonLocation)");
      JsonParsingException exception = new JsonParsingException(message,
          expLoc);
      try {
        throw exception;
      } catch (JsonParsingException e) {
        if (!e.getMessage().equals(message)) {
          logErr("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
      JsonLocation actLoc = exception.getLocation();
      logMsg("JsonParsingException.getLocation()");
      JSONP_Util.dumpLocation(actLoc);
      if (!JSONP_Util.assertEquals(expLoc, actLoc))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParsingExceptionConstructorTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonParsingExceptionConstructorTest1 Failed:");
  }

  /*
   * @testName: jsonParsingExceptionConstructorTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:286; JSONP:JAVADOC:478; JSONP:JAVADOC:474;
   * JSONP:JAVADOC:475; JSONP:JAVADOC:476;
   * 
   * @test_Strategy: Test API: JsonParsingException ret = new
   * JsonParsingException(String, Throwable, JsonLocation)
   */
  public void jsonParsingExceptionConstructorTest2() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");
      MyJsonLocation expLoc = new MyJsonLocation(10, 20, 30);
      logMsg("MyJsonLocation");
      JSONP_Util.dumpLocation(expLoc);

      logMsg("Test JsonParsingException(String, Throwable)");
      JsonParsingException exception = new JsonParsingException(message, foo,
          expLoc);

      try {
        throw exception;
      } catch (JsonParsingException e) {
        if (!e.getCause().equals(foo)) {
          logErr("Incorrect cause: expected " + foo + ", received "
              + e.getCause());
          pass = false;
        }
        if (!e.getMessage().equals(message)) {
          logErr("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
      JsonLocation actLoc = exception.getLocation();
      logMsg("JsonParsingException.getLocation()");
      JSONP_Util.dumpLocation(actLoc);
      if (!JSONP_Util.assertEquals(expLoc, actLoc))
        pass = false;
    } catch (Exception e) {
      throw new Fault("jsonParsingExceptionConstructorTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonParsingExceptionConstructorTest2 Failed:");
  }

  /*
   * @testName: jsonGenerationExceptionConstructorTest1
   * 
   * @assertion_ids: JSONP:JAVADOC:278;
   * 
   * @test_Strategy: Test API: JsonGenerationException ret = new
   * JsonGenerationException(String)
   */
  public void jsonGenerationExceptionConstructorTest1() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect.";

      logMsg("Test JsonGenerationException(String)");
      JsonGenerationException exception = new JsonGenerationException(message);
      try {
        throw exception;
      } catch (JsonGenerationException e) {
        if (!e.getMessage().equals(message)) {
          logErr("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }
    } catch (Exception e) {
      throw new Fault("jsonGenerationExceptionConstructorTest1 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonGenerationExceptionConstructorTest1 Failed:");
  }

  /*
   * @testName: jsonGenerationExceptionConstructorTest2
   * 
   * @assertion_ids: JSONP:JAVADOC:279;
   * 
   * @test_Strategy: Test API: JsonGenerationException ret = new
   * JsonGenerationException(String, Throwable)
   */
  public void jsonGenerationExceptionConstructorTest2() throws Fault {
    boolean pass = true;

    try {
      String message = "This JSON is incorrect due to foo.";
      Exception foo = new Exception("This is a foo exception");

      logMsg("Test JsonGenerationException(String, Throwable)");
      JsonGenerationException exception = new JsonGenerationException(message,
          foo);

      try {
        throw exception;
      } catch (JsonGenerationException e) {
        if (!e.getCause().equals(foo)) {
          logErr("Incorrect cause: expected " + foo + ", received "
              + e.getCause());
          pass = false;
        }
        if (!e.getMessage().equals(message)) {
          logErr("Incorrect message: expected " + message + ", received "
              + e.getMessage());
          pass = false;
        }
      }

    } catch (Exception e) {
      throw new Fault("jsonGenerationExceptionConstructorTest2 Failed: ", e);
    }

    if (!pass)
      throw new Fault("jsonGenerationExceptionConstructorTest2 Failed:");
  }
}
