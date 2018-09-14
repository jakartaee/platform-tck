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
 * $Id$
 */

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.PersonName;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

public class JAXRClient extends JAXRCommonClient {
  Locale tsLocale = new Locale("en", "US");

  com.sun.ts.tests.jaxr.common.JAXR_Util util = null;

  // PersonName level 1 methods - level 0 providers must throw an
  // UnsupportedCapabilityException
  private static final int GETLASTNAME = 0;

  private static final int SETLASTNAME = 1;

  private static final int GETFIRSTNAME = 2;

  private static final int SETFIRSTNAME = 3;

  private static final int GETMIDDLENAME = 4;

  private static final int SETMIDDLENAME = 5;

  private static final int NUM_METHODS = 6;

  public static void main(String[] args) {
    JAXRClient theTests = new JAXRClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */
  /*
   * @class.setup_props: jaxrPassword; jaxrUser; jaxrPassword2; jaxrUser2;
   * registryURL; queryManagerURL; authenticationMethod; providerCapability;
   * jaxrConnectionFactoryLookup; jaxrSecurityCredentialType; jaxrJNDIResource;
   * jaxrAlias; jaxrAlias2; jaxrAliasPassword; jaxrAlias2Password;
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      super.setup(args, p);
      // super.cleanUpRegistry(); //
      debug.clear();

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      // super.cleanUpRegistry();
      super.cleanup();
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    } finally {
      // print out messages
      if (debug != null)
        TestUtil.logTrace(debug.toString());
    }

  }

  /*
   * @testName: PersonName_getFullName_Test
   *
   * @assertion_ids: JAXR:JAVADOC:586;
   *
   * @test_Strategy: create a default organization and publish to the registry.
   * Verify the published fullName with getFullName.
   *
   */
  public void PersonName_getFullName_Test() throws Fault {
    // --
    // --
    String testName = "PersonName_getFullName_Test";
    boolean pass = false;
    String fullName = JAXR_Util.TS_DEFAULT_PERSON_NAME_FULLNAME;
    Organization org = null;
    Collection orgs = null;
    BulkResponse br = null;
    debug.clear();
    Collection orgKeys = null;
    try {
      // create a default organization
      org = JAXR_Util.createDefaultOrganization(blm);
      BusinessQueryManager bqm = rs.getBusinessQueryManager();

      // publish the default organization
      orgs = new ArrayList();
      orgs.add(org);
      br = blm.saveOrganizations(orgs); // publish to registry
      // Verify published with no errors.
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");

      }
      orgKeys = br.getCollection();
      // Query for the published organization
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      // Check bulk response
      if (!(JAXR_Util.checkBulkResponse("getRegistryObject", br, debug))) {
        debug.add("Error:    getRegistryObject failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");

      }
      orgs.clear();
      orgs = br.getCollection();
      Iterator iter = orgs.iterator();
      org = null;
      if (orgs.size() != 1) { // we only published one org
        debug.add("Problem: too many orgs : " + orgs.size() + "\n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      while (iter.hasNext()) {
        org = (Organization) iter.next();
      }
      // Get the User object to get the PersonName
      // returns a collection of users for this organization
      Collection users = org.getUsers();
      debug.add("==\n");
      debug.add("The number of users: " + users.size() + "\n");
      debug.add("==\n");
      User user = null;
      iter = users.iterator();
      while (iter.hasNext()) {
        user = (User) iter.next();
        // find default user
        debug.add("getFullName returned: " + user.getPersonName().getFullName()
            + "\n");
        if (user.getPersonName().getFullName().equals(fullName)) {
          debug.add("Found default user! \n");
          pass = true;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      // print out messages
      debug.add("in finally  - do cleanup\n");
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }
    if (!pass)
      throw new Fault(testName + " failed ");

  } // end of Concept_setValue_Test

  /*
   * @testName: PersonName_getFullName_DefaultTest
   *
   * @assertion_ids: JAXR:JAVADOC:870
   *
   * @test_Strategy: create a default organization and publish to the registry.
   * Create a PersonName object, but don't set the name. Verify the default.
   *
   */
  public void PersonName_getFullName_DefaultTest() throws Fault {
    // --
    // --
    String testName = "PersonName_getFullName_DefaultTest";
    boolean pass = false;
    String fullName = JAXR_Util.TS_DEFAULT_PERSON_NAME_FULLNAME;
    Organization org = null;
    Collection orgs = null;
    BulkResponse br = null;
    String emptyString = "";
    debug.clear();
    Collection orgKeys = null;
    try {
      // create an organization
      org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm.createInternationalString(tsLocale,
          JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));

      PersonName pn = (PersonName) blm
          .createObject(LifeCycleManager.PERSON_NAME);
      debug.add("Just created PersonName\n");
      debug.add("FullName is: " + pn.getFullName() + "\n");

      User user = (User) blm.createObject(LifeCycleManager.USER);
      user.setPersonName(pn);

      Collection users = new ArrayList();
      users.add(user);
      org.addUsers(users);

      BusinessQueryManager bqm = rs.getBusinessQueryManager();

      // publish the organization
      orgs = new ArrayList();
      orgs.add(org);
      br = blm.saveOrganizations(orgs); // publish to registry
      // Verify published with no errors.
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");

      }
      orgKeys = br.getCollection();
      Collection names = new ArrayList();
      names.add(org.getName().getValue(tsLocale));
      // Query for the published organization
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      // Check bulk response
      if (!(JAXR_Util.checkBulkResponse("getRegistryObjects", br, debug))) {
        debug.add("Error:    getRegistryObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      orgs.clear();
      orgs = br.getCollection();
      Iterator iter = orgs.iterator();
      org = null;
      if (orgs.size() != 1) { // we only published one org
        debug.add("Problem: too many orgs : " + orgs.size() + "\n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      while (iter.hasNext()) {
        org = (Organization) iter.next();
      }
      // Get the User object to get the PersonName
      // returns a collection of users for this organization
      users = org.getUsers();
      user = null;
      iter = users.iterator();
      while (iter.hasNext()) {
        user = (User) iter.next();
        // find default user
        debug.add("getFullName returned: " + user.getPersonName().getFullName()
            + "\n");
        if (user.getPersonName().getFullName().equals(emptyString)) {
          debug.add("Default is correct! \n");
          pass = true;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      // print out messages
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

  /*
   * @testName: PersonName_setFullName_Test
   *
   * @assertion_ids: JAXR:JAVADOC:588;
   *
   *
   * @test_Strategy: create a default organization and publish to the registry.
   * set the name and verify.
   *
   */
  public void PersonName_setFullName_Test() throws Fault {
    // --
    // --
    String testName = "PersonName_setFullName_Test";
    boolean pass = false;
    Organization org = null;
    Collection orgs = null;
    BulkResponse br = null;
    String fullName = "PersonName_setFullName_Test";
    debug.clear();
    Collection orgKeys = null;
    try {
      // create an organization
      org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm.createInternationalString(tsLocale,
          JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));

      PersonName pn = (PersonName) blm
          .createObject(LifeCycleManager.PERSON_NAME);
      pn.setFullName(fullName);
      debug.add("Just created PersonName\n");
      debug.add("FullName is: " + pn.getFullName() + "\n");

      User user = (User) blm.createObject(LifeCycleManager.USER);
      user.setPersonName(pn);

      Collection users = new ArrayList();
      users.add(user);
      org.addUsers(users);

      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      // publish the organization
      orgs = new ArrayList();
      orgs.add(org);

      br = blm.saveOrganizations(orgs); // publish to registry
      // Verify published with no errors.
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");

      }
      orgKeys = br.getCollection();
      Collection names = new ArrayList();
      names.add(org.getName().getValue(tsLocale));
      // Query for the published organization
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      // Check bulk response
      if (!(JAXR_Util.checkBulkResponse("getRegistryObjects", br, debug))) {
        debug.add("Error:    getRegistryObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      orgs.clear();
      orgs = br.getCollection();
      Iterator iter = orgs.iterator();
      org = null;
      if (orgs.size() != 1) { // we only published one org
        debug.add("Problem: too many orgs : " + orgs.size() + "\n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      while (iter.hasNext()) {
        org = (Organization) iter.next();
      }
      // Get the User object to get the PersonName
      // returns a collection of users for this organization
      users = org.getUsers();
      user = null;
      iter = users.iterator();
      while (iter.hasNext()) {
        // it never get here - it does not get any users from the organization.
        user = (User) iter.next();
        // find default user
        debug.add("getFullName returned: " + user.getPersonName().getFullName()
            + "\n");
        if (user.getPersonName().getFullName().equals(fullName)) {
          debug.add("Default is correct! \n");
          pass = true;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      // print out messages
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

  /*
   * @testName: PersonName_UnsupportedCapabilityException_Test
   *
   * @assertion_ids: JAXR:SPEC:42;JAXR:SPEC:43; JAXR:SPEC:44; JAXR:SPEC:45;
   * JAXR:SPEC:46; JAXR:SPEC:47;
   *
   * @test_Strategy: This is a level 1 method. Level 0 providers should throw an
   * UnsupportedCapabilityException Verify.
   *
   */
  public void PersonName_UnsupportedCapabilityException_Test() throws Fault {
    String testName = "PersonName_UnsupportedCapabilityException_Test";
    boolean pass = false;
    Organization org = null;
    Collection orgs = null;
    BulkResponse br = null;
    String fullName = "PersonName_UnsupportedCapabilityException_Test";
    String lastName = "Smith";
    String firstName = "Joey";
    String middleName = "Sebastian";
    debug.clear();
    try {
      // create an organization
      org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm
          .createInternationalString(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));

      PersonName pn = (PersonName) blm
          .createObject(LifeCycleManager.PERSON_NAME);
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add(
          "This provider reports capability level = " + providerlevel + "\n");

      for (int method = 0; method < NUM_METHODS; method++) {

        try {
          switch (method) {
          case GETLASTNAME:
            debug.add("Calling getLastName \n");
            String last = pn.getLastName();
            break;
          case SETLASTNAME:
            debug.add("Calling setLastName\n");
            pn.setLastName(lastName);
            break;
          case GETFIRSTNAME:
            debug.add("Calling getFirstName\n");
            String first = pn.getFirstName();
            break;
          case SETFIRSTNAME:
            debug.add("Calling setFirstName\n");
            pn.setFirstName(firstName);
            break;
          case GETMIDDLENAME:
            debug.add("Calling getMiddleName\n");
            String middle = pn.getMiddleName();
            break;
          case SETMIDDLENAME:
            debug.add("Calling setMiddleName\n");
            pn.setMiddleName(middleName);
            break;
          default:
            throw new Fault("Test Error - Unknown method: " + method);

          } // end of switch....
          if (providerlevel == 0) {
            debug.add(
                "Error: UnsupportedCapabilityException expected for capability level 0\n");
          }
        } catch (UnsupportedCapabilityException ue) {
          // Provider level must be 0 to get here
          if (providerlevel == 0) {
            pass = true;
            debug.add("Good it threw an UnsupportedCapabilityException\n");
            TestUtil.printStackTrace(ue);
          } else
            debug.add(
                "Error: UnsupportedCapabilityException not expected for this capability level\n");

        } catch (Exception ee) {
          // Should not have gotten here!
          debug.add("Error threw an unexpected exception \n");
          TestUtil.printStackTrace(ee);
        }
      } // end of for loop.................

      if (providerlevel > 0) {
        int count = 0;
        if (!(pn.getLastName().equals(lastName))) {
          TestUtil.logErr(" unexpected results from getLastName");
        } else
          count = count + 1;
        if (!(pn.getFirstName().equals(firstName))) {
          TestUtil.logErr(" unexpected results from getFirstName");
        } else
          count = count + 1;
        if (!(pn.getMiddleName().equals(middleName))) {
          TestUtil.logErr(" unexpected results from getMiddleName");
        } else
          count = count + 1;
        if (count == 3)
          pass = true;
      } // end of if provider level > 0

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

} // end of class
