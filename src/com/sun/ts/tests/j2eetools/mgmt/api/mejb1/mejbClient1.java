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
 * @(#)mejbClient1.java	1.8   03/05/16
 */

package com.sun.ts.tests.j2eetools.mgmt.api.mejb1;

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

public class mejbClient1 extends ServiceEETest {

  // MEJB fields. Used in this class only to perform lookup of MEJB
  private static final String mejbLookup = "java:comp/env/ejb/MEJB";

  private ManagementHome mejbHome = null;

  private TSNamingContext ctx = null;

  private Management mejb = null;

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    mejbClient1 theTests = new mejbClient1();
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
      // login code moved to EJBVehicleRunner.java

      ctx = new TSNamingContext();
      TestUtil.logTrace("Got TSNamingContext");

      mejbHome = (ManagementHome) ctx.lookup(mejbLookup, ManagementHome.class);
      TestUtil.logTrace("Got MEJB Home interface");

      mejb = mejbHome.create();
      TestUtil.logMsg("Created MEJB instance");

    } catch (Exception e) {
      TestUtil.logErr("Here is the SETUP ERROR: ", e);
      TestUtil.logMsg("Exception during setup: " + e.getMessage());
    }
  }

  /**
   * @testName: testQueryNamesSet1
   * @assertion_ids: J2EEMGMT:JAVADOC:4
   * @test_Strategy: Call queryNames with ObjectName and QueryExp set to null.
   *                 Should return all MO's
   *
   */
  public void testQueryNamesSet1() throws Fault {

    Set mos = null;

    TestUtil.logMsg("Starting testQueryNameSet1");

    try {
      mos = mejb.queryNames(null, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage(), e);
    }
  }

  /**
   * @testName: testQueryNamesSet2
   * @assertion_ids: J2EEMGMT:JAVADOC:4
   * @test_Strategy: Call queryNames with ObjectName set to *:* Should return
   *                 all MO's
   *
   */
  public void testQueryNamesSet2() throws Fault {

    Set mos = null;
    ObjectName search = null;

    TestUtil.logMsg("Starting testQueryNameSet2");

    // Construct an ObjectName which will serve as a wildcard for
    // the default domain.
    try {
      search = new ObjectName(":*");
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
   * @testName: testIsRegistered
   * @assertion_ids: J2EEMGMT:JAVADOC:6
   * @test_Strategy: Call isRegistered on all J2EE MO's Should return all MO's
   *
   */
  public void testIsRegistered() throws Fault {

    TestUtil.logMsg("Starting testQueryNameSet2");

    Set mos = null;
    ObjectName search = null;
    Iterator moi = null;
    boolean notregistered = false;
    boolean b = false;

    // Construct an ObjectName which will serve as a wildcard for
    // the default domain
    try {
      search = new ObjectName(":*");
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

    // Loop thru MO's, and verify they are all registered
    TestUtil.logMsg("Scanning objects for isRegistered...");
    moi = mos.iterator();
    while (moi.hasNext()) {
      ObjectName on = (ObjectName) moi.next();
      try {
        b = mejb.isRegistered(on);
      } catch (RemoteException re) {
        throw new Fault("Caught exception calling getMBeanCount", re);
      }
      TestUtil.logMsg("Object: " + on.toString() + ": " + b);
      if (!b) {
        notregistered = true;
      }
    }

    // If ANY objects were not registered, fail
    if (notregistered) {
      throw new Fault("Some objects returned by queryNames are not registered");
    }
  }

  /**
   * @testName: testGetMBeanCount
   * @assertion_ids: J2EEMGMT:JAVADOC:8
   * @test_Strategy: Query for all object names and compare to
   *                 mejb.getMBeanCount
   *
   */
  public void testGetMBeanCount() throws Fault {

    Set mos = null;
    ObjectName search = null;
    Integer mbcount = null;

    TestUtil.logMsg("Starting testQueryNameSet2");

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

    try {
      mbcount = mejb.getMBeanCount();
    } catch (RemoteException re) {
      throw new Fault("Caught exception calling getMBeanCount", re);
    }

    if (mbcount.equals(new Integer(mos.size()))) {
      TestUtil.logMsg("Count is equal: " + mos.size());
    } else {
      throw new Fault("Sizes are not equal!");
    }
  }

  /**
   * @testName: testGetMBeanInfo1
   * @assertion_ids: J2EEMGMT:JAVADOC:10
   * @test_Strategy: Query for all object names. Call getMBeanInfo on the first
   *                 element of the returned set.
   *
   */
  public void testGetMBeanInfo1() throws Fault {

    Set mos = null;
    ObjectName search = null;
    MBeanInfo mbi = null;

    TestUtil.logMsg("Starting testQueryNameSet2");

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

    try {
      Iterator i = mos.iterator();
      ObjectName on = (ObjectName) i.next();
      mbi = mejb.getMBeanInfo(on);
    } catch (Exception e) {
      throw new Fault("Caught exception calling getMBeanCount", e);
    }

  }

  /**
   * @testName: testGetMBeanInfo2
   * @assertion_ids: J2EEMGMT:JAVADOC:12
   * @test_Strategy: Call getMbeanInfo on a non-existent ObjectName. Should
   *                 throw InstanceNotFoundException
   *
   */
  public void testGetMBeanInfo2() throws Fault {

    Set mos = null;
    ObjectName search = null;
    MBeanInfo mbi = null;
    boolean gotexception = false;

    TestUtil.logMsg("Starting testQueryNameSet2");

    // Construct an ObjectName
    try {
      search = new ObjectName(
          "*:j2eeType=J2EEDomain,name=ThisDomainDoesNotExist");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage(),
          e);
    }

    // Try to get MBeanInfo on invalid object
    try {
      mbi = mejb.getMBeanInfo(search);
    } catch (InstanceNotFoundException ie) {
      TestUtil.logMsg("Got expected exception: " + ie.getMessage());
      gotexception = true;
    } catch (Exception e) {
      throw new Fault("Caught unexpected exception: ", e);
    }

    if (!gotexception) {
      throw new Fault("Did not catch expected exception");
    }

  }

  /**
   * @testName: testGetAttribute1
   * @assertion_ids: J2EEMGMT:JAVADOC:17
   * @test_Strategy: Call getAttribute on a non-existent attribute. Should throw
   *                 AttributeNotFoundException
   *
   */
  public void testGetAttribute1() throws Fault {

    Set mos = null;
    ObjectName search = null;
    MBeanInfo mbi = null;
    boolean gotexception = false;

    TestUtil.logMsg("Starting testQueryNameSet2");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage());
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage());
    }

    // Use the first MO from the set for the test
    try {
      Iterator i = mos.iterator();
      ObjectName on = (ObjectName) i.next();
      Object att = mejb.getAttribute(on, "NonExistentAttribute");
    } catch (AttributeNotFoundException anfe) {
      TestUtil.logMsg("Received expected exception: " + anfe.getMessage());
      gotexception = true;
    } catch (Exception e) {
      throw new Fault("Caught exception calling getAttribute", e);
    }

    if (!gotexception) {
      throw new Fault("Did not catch expected exception");
    }

  }

  /**
   * @testName: testGetAttribute2
   * @assertion_ids: J2EEMGMT:JAVADOC:18
   * @test_Strategy: Call getAttribute on a non-existent object. Should throw
   *                 InstanceNotFoundException
   *
   */
  public void testGetAttribute2() throws Fault {

    Set mos = null;
    ObjectName search = null;
    MBeanInfo mbi = null;
    boolean gotexception = false;

    TestUtil.logMsg("Starting testQueryNameSet2");

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

    // Use the first MO from the set for the test
    try {
      Iterator i = mos.iterator();
      ObjectName on = (ObjectName) i.next();
      Object att = mejb.getAttribute(on, "NonExistentAttribute");
    } catch (AttributeNotFoundException anfe) {
      TestUtil.logMsg("Received expected exception: " + anfe.getMessage());
      gotexception = true;
    } catch (Exception e) {
      throw new Fault("Caught exception calling getAttribute", e);
    }

    if (!gotexception) {
      throw new Fault("Did not catch expected exception");
    }

  }

  /**
   * @testName: testGetAttributes1
   * @assertion_ids: J2EEMGMT:JAVADOC:21
   * @test_Strategy: Call getAttributes.
   *
   */
  public void testGetAttributes1() throws Fault {

    Set mos = null;
    ObjectName search = null;
    MBeanInfo mbi = null;
    boolean gotexception = false;

    TestUtil.logMsg("Starting testQueryNameSet2");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage());
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage());
    }

    // Use the first MO from the set for the test
    try {
      Iterator i = mos.iterator();
      ObjectName on = (ObjectName) i.next();
      Object att = mejb.getAttribute(on, "NonExistentAttribute");
    } catch (AttributeNotFoundException anfe) {
      TestUtil.logMsg("Received expected exception: " + anfe.getMessage());
      gotexception = true;
    } catch (Exception e) {
      throw new Fault("Caught exception calling getAttribute", e);
    }

    if (!gotexception) {
      throw new Fault("Did not catch expected exception");
    }

  }

  /**
   * @testName: testGetAttributes2
   * @assertion_ids: J2EEMGMT:JAVADOC:22
   * @test_Strategy: Call getAttributes with a non existent object. Catch
   *                 InstanceNotFoundException.
   *
   */
  public void testGetAttributes2() throws Fault {

    Set mos = null;
    ObjectName search = null;
    MBeanInfo mbi = null;
    boolean gotexception = false;

    TestUtil.logMsg("Starting testQueryNameSet2");

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

    // Use the first MO from the set for the test
    try {
      Iterator i = mos.iterator();
      ObjectName on = (ObjectName) i.next();
      Object att = mejb.getAttribute(on, "NonExistentAttribute");
    } catch (AttributeNotFoundException anfe) {
      TestUtil.logMsg("Received expected exception: " + anfe.getMessage());
      gotexception = true;
    } catch (Exception e) {
      throw new Fault("Caught exception calling getAttribute", e);
    }

    if (!gotexception) {
      throw new Fault("Did not catch expected exception");
    }
  }

  /**
   * @testName: testGetDefaultDomain
   * @assertion_ids: J2EEMGMT:JAVADOC:41
   * @test_Strategy: Call getDefaultDomain
   *
   */
  public void testGetDefaultDomain() throws Fault {

    Set mos = null;
    ObjectName search = null;
    MBeanInfo mbi = null;
    boolean gotexception = false;

    TestUtil.logMsg("Starting testQueryNameSet2");

    // Construct an ObjectName which will serve as a wildcard
    try {
      search = new ObjectName("*:*");
    } catch (MalformedObjectNameException e) {
      throw new Fault("FAILED: Couldn't create ObjectName: " + e.getMessage());
    }

    // Retrieve all MO's using wildcard
    try {
      mos = mejb.queryNames(search, null);
    } catch (RemoteException e) {
      throw new Fault("FAILED: RemoteException on test: " + e.getMessage());
    }

    // Use the first MO from the set for the test
    try {
      Iterator i = mos.iterator();
      ObjectName on = (ObjectName) i.next();
      Object att = mejb.getAttribute(on, "NonExistentAttribute");
    } catch (AttributeNotFoundException anfe) {
      TestUtil.logMsg("Received expected exception: " + anfe.getMessage());
      gotexception = true;
    } catch (Exception e) {
      throw new Fault("Caught exception calling getAttribute", e);
    }

    if (!gotexception) {
      throw new Fault("Did not catch expected exception");
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

} // end class mejbClient1
