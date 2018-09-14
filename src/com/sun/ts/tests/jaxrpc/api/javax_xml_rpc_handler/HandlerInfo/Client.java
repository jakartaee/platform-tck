/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_handler.HandlerInfo;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.*;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {
  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: HandlerInfoConstructorTest1
   *
   * @assertion_ids: JAXRPC:JAVADOC:129; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via HanderInfo.HandlerInfo() constructor.
   * Verify HandlerInfo object created successfully.
   */
  public void HandlerInfoConstructorTest1() throws Fault {
    TestUtil.logTrace("HandlerInfoConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via HandlerInfo() ...");
      HandlerInfo hi = new HandlerInfo();
      if (hi != null) {
        TestUtil.logMsg("HandlerInfo object created successfully");
      } else {
        TestUtil.logErr("HandlerInfo object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HandlerInfoConstructorTest1 failed", e);
    }

    if (!pass)
      throw new Fault("HandlerInfoConstructorTest1 failed");
  }

  /*
   * @testName: HandlerInfoConstructorTest2a
   *
   * @assertion_ids: JAXRPC:JAVADOC:130; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via HandlerInfo.HandlerInfo(Class, Map,
   * QName[]) constructor. Verify HandlerInfo object created successfully.
   */
  public void HandlerInfoConstructorTest2a() throws Fault {
    TestUtil.logTrace("HandlerInfoConstructorTest2a");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via HandlerInfo(Class, Map," + " QName[]) ...");
      HandlerInfo hi = new HandlerInfo(
          com.sun.ts.tests.jaxrpc.common.ClientHandler1.class, null, null);
      if (hi != null) {
        TestUtil.logMsg("HandlerInfo object created successfully");
      } else {
        TestUtil.logErr("HandlerInfo object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HandlerInfoConstructorTest2a failed", e);
    }

    if (!pass)
      throw new Fault("HandlerInfoConstructorTest2a failed");
  }

  /*
   * @testName: HandlerInfoConstructorTest2b
   *
   * @assertion_ids: JAXRPC:JAVADOC:130; WS4EE:SPEC:70;
   *
   * @test_Strategy: Create instance via HandlerInfo(Class, Map, QName[])
   * constructor. Verify HandlerInfo object created successfully.
   */
  public void HandlerInfoConstructorTest2b() throws Fault {
    TestUtil.logTrace("HandlerInfoConstructorTest2b");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Create instance via HandlerInfo(Class, Map," + " QName[]) ...");
      QName headers[] = new QName[10];
      for (int i = 0; i < headers.length; i++)
        headers[i] = new QName("This is header #" + i);
      Hashtable map = new Hashtable();
      HandlerInfo hi = new HandlerInfo(
          com.sun.ts.tests.jaxrpc.common.ClientHandler1.class, map, headers);
      if (hi != null) {
        TestUtil.logMsg("HandlerInfo object created successfully");
      } else {
        TestUtil.logErr("HandlerInfo object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("HandlerInfoConstructorTest2b failed", e);
    }

    if (!pass)
      throw new Fault("HandlerInfoConstructorTest2b failed");
  }

  /*
   * @testName: SetGetHandlerClassTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:131;JAXRPC:JAVADOC:132; WS4EE:SPEC:70;
   *
   * @test_Strategy: Call HandlerInfo.setHandlerClass() followed by
   * HandlerInfo.getHandlerClass(). Verify behavior.
   */
  public void SetGetHandlerClassTest() throws Fault {
    TestUtil.logTrace("SetGetHandlerClassTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via HandlerInfo() ...");
      HandlerInfo hi = new HandlerInfo();
      if (hi != null) {
        TestUtil.logMsg("HandlerInfo object created successfully");
      } else {
        TestUtil.logErr("HandlerInfo object not created");
        pass = false;
      }
      if (pass) {
        TestUtil.logMsg("Set the Handler class");
        TestUtil.logMsg("Call HandleInfo.setHandlerClass()");
        hi.setHandlerClass(com.sun.ts.tests.jaxrpc.common.ClientHandler1.class);
        TestUtil.logMsg("Get the Handler class");
        TestUtil.logMsg("Call HandleInfo.getHandlerClass()");
        Object o = Class
            .forName("com.sun.ts.tests.jaxrpc.common.ClientHandler1")
            .newInstance();
        Class c = hi.getHandlerClass();
        TestUtil.logMsg("Handler class = " + c);
        if (c.isInstance(o)) {
          TestUtil.logMsg("Returned correct Handler class");
        } else {
          TestUtil.logErr("Returned incorrect Handler class");
          TestUtil.logErr("Expected: " + o);
          TestUtil.logErr("Received: " + c);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetHandlerClassTest failed", e);
    }

    if (!pass)
      throw new Fault("SetGetHandlerClassTest failed");
  }

  /*
   * @testName: GetHandlerClassNegativeTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:132; WS4EE:SPEC:70;
   *
   * @test_Strategy: Call HandlerInfo.getHandlerClass() when no Handler class
   * has been set. Verify behavior.
   */
  public void GetHandlerClassNegativeTest() throws Fault {
    TestUtil.logTrace("GetHandlerClassNegativeTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via HandlerInfo() ...");
      HandlerInfo hi = new HandlerInfo();
      if (hi != null) {
        TestUtil.logMsg("HandlerInfo object created successfully");
      } else {
        TestUtil.logErr("HandlerInfo object not created");
        pass = false;
      }
      if (pass) {
        TestUtil.logMsg("Do not set a Handler class");
        TestUtil.logMsg("Get the Handler class (should return null)");
        TestUtil.logMsg("Call HandleInfo.getHandlerClass()");
        Class c = hi.getHandlerClass();
        if (c != null) {
          TestUtil.logErr("Did not return expected null");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetHandlerClassNegativeTest failed", e);
    }

    if (!pass)
      throw new Fault("GetHandlerClassNegativeTest failed");
  }

  /*
   * @testName: SetGetHeadersTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:135;JAXRPC:JAVADOC:136; WS4EE:SPEC:70;
   *
   * @test_Strategy: Call HandlerInfo.setHeaders() followed by
   * HandlerInfo.getHeaders(). Verify behavior.
   */
  public void SetGetHeadersTest() throws Fault {
    TestUtil.logTrace("SetGetHeadersTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via HandlerInfo() ...");
      HandlerInfo hi = new HandlerInfo();
      if (hi != null) {
        TestUtil.logMsg("HandlerInfo object created successfully");
      } else {
        TestUtil.logErr("HandlerInfo object not created");
        pass = false;
      }
      if (pass) {
        TestUtil.logMsg("Set the Header blocks for this Handler");
        QName[] headers1 = new QName[10];
        for (int i = 0; i < headers1.length; i++)
          headers1[i] = new QName("This is header #" + i);
        TestUtil.logMsg("Call HandleInfo.setHeaders()");
        hi.setHeaders(headers1);
        TestUtil.logMsg("Get the Header blocks for this Handler");
        TestUtil.logMsg("Call HandleInfo.getHeaders()");
        QName[] headers2 = hi.getHeaders();
        if (headers1.length != headers2.length) {
          TestUtil.logErr("Returned incorrect count of headers: " + " expected "
              + headers1.length + ", received " + headers2.length);
          pass = false;
        } else {
          TestUtil.logMsg("headers1.length=" + headers1.length
              + ", headers2.length=" + headers2.length);
          TestUtil.logMsg("Check that contents of headers are equal");
          for (int i = 0; i < headers1.length; i++) {
            if (!headers1[i].equals(headers2[i])) {
              TestUtil.logErr("headers1[" + i + "]=" + headers1[i]
                  + ", headers2[" + i + "]=" + headers2[i]);
              pass = false;
            }
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetHeadersTest failed", e);
    }

    if (!pass)
      throw new Fault("SetGetHeadersTest failed");
  }

  /*
   * @testName: SetGetHandlerConfigTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:133;JAXRPC:JAVADOC:134; WS4EE:SPEC:70;
   *
   * @test_Strategy: Call HandlerInfo.setHandlerConfig() followed by
   * HandlerInfo.getHandlerConfig(). Verify behavior.
   */
  public void SetGetHandlerConfigTest() throws Fault {
    TestUtil.logTrace("SetGetHandlerConfigTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via HandlerInfo() ...");
      HandlerInfo hi = new HandlerInfo();
      if (hi != null) {
        TestUtil.logMsg("HandlerInfo object created successfully");
      } else {
        TestUtil.logErr("HandlerInfo object not created");
        pass = false;
      }
      if (pass) {
        TestUtil.logMsg("Set the Handler configuration");
        Hashtable map1 = new Hashtable();
        map1.put("Key1", "Value1");
        TestUtil.logMsg("Call HandleInfo.setHandlerConfig()");
        hi.setHandlerConfig(map1);
        TestUtil.logMsg("Get the Handler configuration");
        TestUtil.logMsg("Call HandleInfo.getHandlerConfig()");
        Map map2 = hi.getHandlerConfig();
        if (map1.size() != map2.size()) {
          TestUtil.logErr("Returned incorrect map size: " + " expected "
              + map1.size() + ", received " + map2.size());
          pass = false;
        } else {
          if (!map2.containsKey("Key1")) {
            TestUtil.logErr("Map2 does not contain key: Key1");
            pass = false;
          }
          if (!map2.containsValue("Value1")) {
            TestUtil.logErr("Map2 does not contain value: Value1");
            pass = false;
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("SetGetHandlerConfigTest failed", e);
    }

    if (!pass)
      throw new Fault("SetGetHandlerConfigTest failed");
  }

  /*
   * @testName: GetHandlerConfigNegativeTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:134; WS4EE:SPEC:70;
   *
   * @test_Strategy: Call HandlerInfo.getHandlerConfig() when no Handler
   * configuration has been set. Verify behavior.
   */
  public void GetHandlerConfigNegativeTest() throws Fault {
    TestUtil.logTrace("GetHandlerConfigNegativeTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via HandlerInfo() ...");
      HandlerInfo hi = new HandlerInfo();
      if (hi != null) {
        TestUtil.logMsg("HandlerInfo object created successfully");
      } else {
        TestUtil.logErr("HandlerInfo object not created");
        pass = false;
      }
      if (pass) {
        TestUtil.logMsg("Do not set a Handler configuration");
        TestUtil.logMsg("Get the Handler configuration (should return null)");
        TestUtil.logMsg("Call HandleInfo.getHandlerConfig()");
        Map map = hi.getHandlerConfig();
        if (!map.isEmpty()) {
          TestUtil.logErr("Did not return expected empty Map");
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("GetHandlerConfigNegativeTest failed", e);
    }

    if (!pass)
      throw new Fault("GetHandlerConfigNegativeTest failed");
  }
}
