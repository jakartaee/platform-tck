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
package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_soap.MTOMFeature;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.math.*;

import jakarta.xml.ws.soap.MTOMFeature;

import com.sun.javatest.Status;

public class Client extends ServiceEETest {

  private final static Byte myByte = Byte.valueOf(Byte.MAX_VALUE);

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
   * @testName: MTOMFeatureDefaultConstructorTest
   *
   * @assertion_ids: JAXWS:JAVADOC:171; JAXWS:JAVADOC:170;
   *
   * @test_Strategy: Create instance via MTOMFeature() constructor. Verify
   * MTOMFeature object created successfully.
   */
  public void MTOMFeatureDefaultConstructorTest() throws Fault {
    TestUtil.logTrace("MTOMFeatureDefaultConstructorTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via MTOMFeature() ...");
      MTOMFeature n = new MTOMFeature();
      if (n != null) {
        if (!(n.isEnabled())) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but MTOM is not enabled");
          pass = false;
        } else {
          TestUtil.logMsg("MTOMFeature enabled successfully");
        }
        if (n.getThreshold() != 0) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but Threshold is not 0");
          pass = false;
        } else {
          TestUtil.logMsg("MTOMFeature threshold set successfully");
        }

      } else {
        TestUtil.logErr("MTOMFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MTOMFeatureDefaultConstructorTest failed", e);
    }

    if (!pass) {
      throw new Fault("MTOMFeatureDefaultConstructorTest failed");
    }
  }

  /*
   * @testName: MTOMFeatureConstructorTest1
   *
   * @assertion_ids: JAXWS:JAVADOC:172;
   *
   * @test_Strategy: Create instance via MTOMFeature(true) constructor. Verify
   * MTOMFeature object created successfully.
   */
  public void MTOMFeatureConstructorTest1() throws Fault {
    TestUtil.logTrace("MTOMFeatureConstructorTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via MTOMFeature(true) ...");
      MTOMFeature n = new MTOMFeature(true);
      if (n != null) {
        if (!(n.isEnabled())) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but MTOM is not enabled");
          pass = false;
        } else {
          TestUtil.logMsg("MTOMFeature object created successfully");
        }
      } else {
        TestUtil.logErr("MTOMFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MTOMFeatureConstructorTest1 failed", e);
    }

    if (!pass) {
      throw new Fault("MTOMFeatureConstructorTest1 failed");
    }
  }

  /*
   * @testName: MTOMFeatureConstructorTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:172;
   *
   * @test_Strategy: Create instance via MTOMFeature(false) constructor. Verify
   * MTOMFeature object created successfully.
   */
  public void MTOMFeatureConstructorTest2() throws Fault {
    TestUtil.logTrace("MTOMFeatureConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via MTOMFeature(false) ...");
      MTOMFeature n = new MTOMFeature(false);
      if (n != null) {
        if (!(n.isEnabled())) {
          TestUtil.logMsg(
              "MTOMFeature object created successfully, MTOM is correctly not enabled");
        } else {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but MTOM is incorrectly enabled.");
          pass = false;
        }
      } else {
        TestUtil.logErr("MTOMFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MTOMFeatureConstructorTest2 failed", e);
    }

    if (!pass) {
      throw new Fault("MTOMFeatureConstructorTest2 failed");
    }
  }

  /*
   * @testName: MTOMFeatureConstructorTest3
   *
   * @assertion_ids: JAXWS:JAVADOC:174; JAXWS:JAVADOC:170;
   *
   * @test_Strategy: Create instance via MTOMFeature(true, int) constructor.
   * Verify MTOMFeature object created successfully.
   */
  public void MTOMFeatureConstructorTest3() throws Fault {
    TestUtil.logTrace("MTOMFeatureConstructorTest3");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via MTOMFeature(true, int) ...");
      MTOMFeature n = new MTOMFeature(true, 100);
      if (n != null) {
        if (!(n.isEnabled())) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but MTOM is not enabled");
          pass = false;
        } else if (n.getThreshold() != 100) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but threshold is not set correctly");
          pass = false;
        } else {
          TestUtil.logMsg("MTOMFeature object created successfully");
        }
      } else {
        TestUtil.logErr("MTOMFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MTOMFeatureConstructorTest3 failed", e);
    }

    if (!pass) {
      throw new Fault("MTOMFeatureConstructorTest3 failed");
    }
  }

  /*
   * @testName: MTOMFeatureConstructorTest4
   *
   * @assertion_ids: JAXWS:JAVADOC:174; JAXWS:JAVADOC:170;
   *
   * @test_Strategy: Create instance via MTOMFeature(false, int) constructor.
   * Verify MTOMFeature object created successfully.
   */
  public void MTOMFeatureConstructorTest4() throws Fault {
    TestUtil.logTrace("MTOMFeatureConstructorTest4");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via MTOMFeature(false, int) ...");
      MTOMFeature n = new MTOMFeature(false, 100);
      if (n != null) {
        if (n.isEnabled()) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but MTOM is incorrectly enabled");
          pass = false;
        } else if (n.getThreshold() != 100) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but threshold is not set correctly");
          pass = false;
        } else {
          TestUtil.logMsg("MTOMFeature object created successfully");
        }
      } else {
        TestUtil.logErr("MTOMFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MTOMFeatureConstructorTest4 failed", e);
    }

    if (!pass) {
      throw new Fault("MTOMFeatureConstructorTest4 failed");
    }
  }

  /*
   * @testName: MTOMFeatureConstructorTest5
   *
   * @assertion_ids: JAXWS:JAVADOC:173; JAXWS:JAVADOC:170;
   *
   * @test_Strategy: Create instance via MTOMFeature(int) constructor. Verify
   * MTOMFeature object created successfully.
   */
  public void MTOMFeatureConstructorTest5() throws Fault {
    TestUtil.logTrace("MTOMFeatureConstructorTest5");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via MTOMFeature(int) ...");
      MTOMFeature n = new MTOMFeature(100);
      if (n != null) {
        if (!(n.isEnabled())) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, however MTOM should be enabled and is not enabled.");
          pass = false;
        } else if (n.getThreshold() != 100) {
          TestUtil.logErr(
              "MTOMFeature object created successfully, but threshold is not set correctly, expected ["
                  + Integer.valueOf(100) + "] received [" + n.getThreshold()
                  + "]");
          pass = false;
        } else {
          TestUtil.logMsg(
              "MTOMFeature object created successfully with correct threshold, expected ["
                  + Integer.valueOf(100) + "] received [" + n.getThreshold()
                  + "]");
        }
      } else {
        TestUtil.logErr("MTOMFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MTOMFeatureConstructorTest5 failed", e);
    }

    if (!pass) {
      throw new Fault("MTOMFeatureConstructorTest5 failed");
    }
  }

  /*
   * @testName: getIDTest
   *
   * @assertion_ids: JAXWS:JAVADOC:169; JAXWS:JAVADOC:160;
   *
   * @test_Strategy: Test getting ID string of MTOMFeature object. Verify value
   * returned is set correctly.
   */
  public void getIDTest() throws Fault {
    TestUtil.logTrace("getIDTest");
    boolean pass = true;

    try {
      TestUtil.logMsg("Create instance via MTOMFeature(true) ...");
      MTOMFeature n = new MTOMFeature(true);
      if (n != null) {
        if (!(MTOMFeature.ID.equals(n.getID()))) {
          TestUtil
              .logErr("MTOMFeature object created with incorrect ID, expected ["
                  + MTOMFeature.ID + "], received [" + n.getID() + "]");
          pass = false;
        } else {
          TestUtil.logMsg(
              "MTOMFeature object created successfully with correct ID, expected ["
                  + MTOMFeature.ID + "], received [" + n.getID() + "]");
        }
      } else {
        TestUtil.logErr("MTOMFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getIDTest failed", e);
    }

    if (!pass) {
      throw new Fault("getIDTest failed");
    }
  }

  /*
   * @testName: getSetThresholdTest
   *
   * @assertion_ids: JAXWS:JAVADOC:170;
   *
   * @test_Strategy: Test setting threshold of MTOMFeature object. Get value and
   * verify value returned is set correctly.
   */
  public void getSetThresholdTest() throws Fault {
    TestUtil.logTrace("getSetThresholdTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via MTOMFeature(true, 100) ...");
      MTOMFeature n = new MTOMFeature(true, 100);
      if (n != null) {
        int threshold = n.getThreshold();
        if (threshold == 100) {
          // get returned correct value, now try setting it to new value
          TestUtil.logMsg(
              "MTOMFeature object created with correct threshold, received ["
                  + n.getThreshold() + "] now try setting it to new value ["
                  + Integer.valueOf(1000) + "]");
          n = new MTOMFeature(true, 1000);
          threshold = n.getThreshold();
          if (threshold == 1000) {
            // get returned correct value
            TestUtil.logMsg(
                "MTOMFeature object set and retrieved correct threshold, expected ["
                    + Integer.valueOf(1000) + "], received [" + n.getThreshold()
                    + "]");
          } else {
            TestUtil.logMsg(
                "MTOMFeature object created with incorrect threshold, expected ["
                    + Integer.valueOf(1000) + "], received [" + n.getThreshold()
                    + "]");
            pass = false;
          }
        } else {
          TestUtil.logMsg(
              "MTOMFeature object created with incorrect threshold, expected ["
                  + Integer.valueOf(1000) + "], received [" + n.getThreshold()
                  + "]");
          pass = false;
        }
      } else {
        TestUtil.logErr("MTOMFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getSetThresholdTest failed", e);
    }

    if (!pass) {
      throw new Fault("getSetThresholdTest failed");
    }
  }

}
