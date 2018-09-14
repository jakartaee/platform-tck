/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)mejbClient2.java	1.3   03/05/16
 */

package com.sun.ts.tests.j2eetools.mgmt.api.mejb2;

// Java imports
import java.io.*;
import java.util.*;

// Harness imports
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

// RMI imports
import java.rmi.RemoteException;

// EJB imports
import javax.ejb.*;

// Management imports
import javax.management.*;
import javax.management.j2ee.Management;
import javax.management.j2ee.ManagementHome;

// Our utility classes
import com.sun.ts.tests.j2eetools.mgmt.common.MOUtils;
import com.sun.ts.tests.j2eetools.mgmt.common.BaseMO;
import com.sun.ts.tests.j2eetools.mgmt.common.KeyNotFoundException;

public class mejbClient2 extends ServiceEETest {

  // MEJB fields. Used in this class only to perform lookup of MEJB
  private static final String mejbLookup = "java:comp/env/ejb/MEJB";

  private ManagementHome mejbHome = null;

  private TSNamingContext ctx = null;

  private Management mejb = null;

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    mejbClient2 theTests = new mejbClient2();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: user; password; secured.ejb.vehicle.client;
   */
  /**
   * Setup method. Called by harness for each test in this source.
   *
   * @param args
   *          List of test arguments
   * @param p
   *          Test properties
   * @throws Fault
   *           If there is any error in the setup process
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      // moved login code to EJBVehicleRunner.java

      ctx = new TSNamingContext();
      TestUtil.logTrace("Got TSNamingContext");

      mejbHome = (ManagementHome) ctx.lookup(mejbLookup, ManagementHome.class);
      TestUtil.logTrace("Got MEJB Home interface");

      mejb = mejbHome.create();
      TestUtil.logMsg("Created MEJB instance");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logMsg("Exception during setup: " + e.getMessage());
    }
  }

  /**
   * @testName: testInvoke1
   * @assertion_ids: J2EEMGMT:JAVADOC:36
   * @test_Strategy: Call invoke
   *
   */
  public void testInvoke1() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testInvoke1");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }

  }

  /**
   * @testName: testInvoke2
   * @assertion_ids: J2EEMGMT:JAVADOC:36
   * @test_Strategy: Call invoke, catch InstanceNotFoundException
   *
   */
  public void testInvoke2() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testInvoke2");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }

  }

  /**
   * @testName: testGetListenerRegistry
   * @assertion_ids: J2EEMGMT:JAVADOC:43
   * @test_Strategy: Call getListenerRegistry on an existing MO.
   *
   */
  public void testGetListenerRegistry() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testGetListenerRegistry");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }

  }

  /**
   * @testName: testAddNotificationListener1
   * @assertion_ids: J2EEMGMT:JAVADOC:45
   * @test_Strategy: Call addListenerRegistry on an existing MO.
   *
   */
  public void testAddNotificationListener1() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testAddNotificationListener1");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }

  }

  /**
   * @testName: testAddNotificationListener2
   * @assertion_ids: J2EEMGMT:JAVADOC:46
   * @test_Strategy: Call addListenerRegistry on a nonexistent MO, catch
   *                 InstanceNotFoundException
   *
   */
  public void testAddNotificationListener2() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testAddNotificationListener1");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }

  }

  /**
   * @testName: testRemoveNotificationListener1
   * @assertion_ids: J2EEMGMT:JAVADOC:48
   * @test_Strategy: Call addListenerRegistry on a nonexistent MO, catch
   *                 InstanceNotFoundException
   *
   */
  public void testRemoveNotificationListener1() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testRemoveNotificationListener1");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }

  }

  /**
   * @testName: testRemoveNotificationListener2
   * @assertion_ids: J2EEMGMT:JAVADOC:49
   * @test_Strategy: Call addListenerRegistry on a nonexistent MO, catch
   *                 InstanceNotFoundException
   *
   */
  public void testRemoveNotificationListener2() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testRemoveNotificationListener2");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }

  }

  /**
   * @testName: testRemoveNotificationListener3
   * @assertion_ids: J2EEMGMT:JAVADOC:50
   * @test_Strategy: Call addListenerRegistry on a nonexistent MO, catch
   *                 InstanceNotFoundException
   *
   */
  public void testRemoveNotificationListener3() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testRemoveNotificationListener3");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }

  }

  /**
   * Cleanup method. Called by harness after each test in this source.
   *
   * @throws Fault
   *           If there is any error in the setup process
   *
   */
  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup() invoked");
    try {
      mejb.remove();
    } catch (Exception e) {
      TestUtil.logErr("Error removing MEJB reference: " + e.getMessage(), e);
    }
  }

} // end class mejbClient2
