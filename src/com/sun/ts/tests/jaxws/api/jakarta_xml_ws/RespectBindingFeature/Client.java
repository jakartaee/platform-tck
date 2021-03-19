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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.RespectBindingFeature;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.math.*;

import jakarta.xml.ws.RespectBindingFeature;

import com.sun.javatest.Status;

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

  /*
   * @testName: RespectBindingFeatureConstructorTest
   *
   * @assertion_ids: JAXWS:JAVADOC:151;
   *
   * @test_Strategy: Create instance via RespectBindingFeature() constructor.
   * Verify RespectBindingFeature object created successfully.
   */
  public void RespectBindingFeatureConstructorTest() throws Fault {
    TestUtil.logTrace("RespectBindingFeatureConstructorTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via RespectBindingFeature() ...");
      RespectBindingFeature n = new RespectBindingFeature();
      if (n != null) {
        if (!(n.isEnabled())) {
          TestUtil.logErr(
              "RespectBindingFeature object created successfully, but RespectBindingFeature is not enabled");
          pass = false;
        } else {
          TestUtil.logMsg("RespectBindingFeature object created successfully");
        }
      } else {
        TestUtil.logErr("RespectBindingFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("RespectBindingFeatureConstructorTest failed", e);
    }

    if (!pass) {
      throw new Fault("RespectBindingFeatureConstructorTest failed");
    }
  }

  /*
   * @testName: RespectBindingFeatureConstructorTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:152;
   *
   * @test_Strategy: Create instance via RespectBindingFeature(true)
   * constructor. Verify RespectBindingFeature object created successfully.
   */
  public void RespectBindingFeatureConstructorTest2() throws Fault {
    TestUtil.logTrace("RespectBindingFeatureConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via RespectBindingFeature(true) ...");
      RespectBindingFeature n = new RespectBindingFeature(true);
      if (n != null) {
        if (!(n.isEnabled())) {
          TestUtil.logErr(
              "RespectBindingFeature object created successfully, but RespectBinding is not enabled");
          pass = false;
        } else {
          TestUtil.logMsg("RespectBindingFeature object created successfully");
        }
      } else {
        TestUtil.logErr("RespectBindingFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("RespectBindingFeatureConstructorTest2 failed", e);
    }

    if (!pass) {
      throw new Fault("RespectBindingFeatureConstructorTest2 failed");
    }
  }

  /*
   * @testName: RespectBindingFeatureConstructorTest3
   *
   * @assertion_ids: JAXWS:JAVADOC:152;
   *
   * @test_Strategy: Create instance via RespectBindingFeature(false)
   * constructor. Verify RespectBindingFeature object created successfully.
   */
  public void RespectBindingFeatureConstructorTest3() throws Fault {
    TestUtil.logTrace("RespectBindingFeatureConstructorTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via RespectBindingFeature(false) ...");
      RespectBindingFeature n = new RespectBindingFeature(false);
      if (n != null) {
        if (!(n.isEnabled())) {
          TestUtil.logMsg(
              "RespectBindingFeature object created successfully, RespectBinding is correctly not enabled");
        } else {
          TestUtil.logErr(
              "RespectBindingFeature object created successfully, but RespectBinding is incorrectly enabled.");
          pass = false;
        }
      } else {
        TestUtil.logErr("RespectBindingFeature object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("RespectBindingFeatureConstructorTest3 failed", e);
    }

    if (!pass) {
      throw new Fault("RespectBindingFeatureConstructorTest3 failed");
    }
  }

  /*
   * @testName: getIDTest
   *
   * @assertion_ids: JAXWS:JAVADOC:150;
   *
   * @test_Strategy: Test getting the unique identifier for this
   * RespectBindingFeature object. Verify value returned is set correctly.
   */
  public void getIDTest() throws Fault {
    TestUtil.logTrace("getIDTest");
    boolean pass = true;

    try {
      TestUtil.logMsg("Create instance via RespectBindingFeature(true) ...");
      RespectBindingFeature n = new RespectBindingFeature(true);
      if (n != null) {
        if (!(RespectBindingFeature.ID.equals(n.getID()))) {
          TestUtil.logErr(
              "RespectBindingFeature object created with incorrect ID, expected ["
                  + RespectBindingFeature.ID + "], received [" + n.getID()
                  + "]");
          pass = false;
        } else {
          TestUtil.logMsg(
              "RespectBindingFeature object created successfully with correct ID, expected ["
                  + RespectBindingFeature.ID + "], received [" + n.getID()
                  + "]");
        }
      } else {
        TestUtil.logErr("RespectBindingFeature object not created");
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
   * @testName: isEnabledTest
   *
   * @assertion_ids: JAXWS:JAVADOC:161;
   *
   * @test_Strategy: Test setting isEnabled of RespectBindingFeature object. Get
   * enabled value and verify value returned is set correctly.
   */
  public void isEnabledTest() throws Fault {
    TestUtil.logTrace("isEnabledTest");
    boolean pass = true;

    try {
      TestUtil.logMsg("Create instance via RespectBindingFeature() ...");
      RespectBindingFeature n = new RespectBindingFeature();
      if (n != null) {
        boolean isEnabled = n.isEnabled();
        if (isEnabled == true) {
          // got returned correct value, now try setting it to new value
          TestUtil.logMsg("Returned correct isEnabled value, received ["
              + n.isEnabled() + "]");
        } else {
          TestUtil.logMsg("Returned incorrect isEnabled value, expected ["
              + new Boolean(true) + "], received [" + n.isEnabled() + "]");
          pass = false;
        }
        // now create with enabled set to false, verify isEnabled correctly
        // returns false
        TestUtil.logMsg("Create instance via RespectBindingFeature(false) ...");
        RespectBindingFeature n1 = new RespectBindingFeature(false);
        if (n1 != null) {
          isEnabled = n1.isEnabled();
          if (isEnabled == false) {
            // got returned correct value, now try setting it to new value
            TestUtil.logMsg("Returned correct isEnabled value, received ["
                + n1.isEnabled() + "]");
          } else {
            TestUtil.logMsg("Returned incorrect isEnabled value, expected ["
                + new Boolean(false) + "], received [" + n1.isEnabled() + "]");
            pass = false;
          }
        } else {
          TestUtil.logErr(
              "RespectBindingFeature object not created for constructor RespectBindingFeature(false)");
          pass = false;
        }
      } else {
        TestUtil.logErr(
            "RespectBindingFeature object not created for default constructor RespectBindingFeature()");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("isEnabledTest failed", e);
    }

    if (!pass) {
      throw new Fault("isEnabledTest failed");
    }
  }

}
