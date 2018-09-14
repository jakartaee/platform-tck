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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.Organization;

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
      debug.clear();

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
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
   * @testName: organization_addChildOrganization_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:33;
   *
   * @assertion: addChildOrganization - Add a child Organization All are at
   * Capability Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void organization_addChildOrganization_Test() throws Fault {
    int index = 0;
    String testName = "organization_addChildOrganization_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String childOrgName = "TS Test Child Organization";
    Organization childOrg = null;
    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        // try to add a child organization to it.
        InternationalString ichildOrgName = blm
            .createInternationalString(tsLocale, childOrgName);

        childOrg = blm.createOrganization(ichildOrgName);
        org.addChildOrganization(childOrg);
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected addChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // level 1 provider should get here.
        debug
            .add("Added one child organization,  verify the collection size\n");
        debug.add("count of child orgs is " + org.getChildOrganizations().size()
            + "\n");
        if (org.getChildOrganizations().size() != 1) {
          failcount = failcount + 1;
          debug.add("count of child orgs is not valid \n");
        }
      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! addChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: addChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_add_removeChildOrganizations_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:34;JAXR:SPEC:36;
   *
   * @assertion: addChildOrganizations - Add a Collection of Organization
   * children removeChildOrganizations - Remove a Collection of children
   * Organizations Capability Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   *
   *
   */

  public void organization_add_removeChildOrganizations_Test() throws Fault {

    String testName = "organization_add_removeChildOrganizations_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String childOrgName1 = "TS Test Child Organization1";
    String childOrgName2 = "TS Test Child Organization2";
    Collection myChildOrgs = null;

    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();

      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        // try to add a child organization to it.
        InternationalString ichildOrgName1 = blm
            .createInternationalString(tsLocale, childOrgName1);
        InternationalString ichildOrgName2 = blm
            .createInternationalString(tsLocale, childOrgName2);

        Organization childOrg1 = blm.createOrganization(ichildOrgName1);
        Organization childOrg2 = blm.createOrganization(ichildOrgName2);
        myChildOrgs = new ArrayList();
        myChildOrgs.add(childOrg1);
        myChildOrgs.add(childOrg2);
        org.addChildOrganizations(myChildOrgs);
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected addChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // level 1 provider should get here.
        debug.add("Added 2 child organizations,  verify the collection size\n");
        debug.add("count of child orgs is " + org.getChildOrganizations().size()
            + "\n");
        if (org.getChildOrganizations().size() != 2) {
          failcount = failcount + 1;
          debug.add("count of child orgs is not valid \n");
        }

      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! addChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: addChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

      try {
        // try to remove a child organizations
        org.removeChildOrganizations(myChildOrgs);
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganizations to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // level 1 provider should get here.
        debug.add("Removed child organizations,  verify the collection size\n");
        debug.add("count of child orgs is " + org.getChildOrganizations().size()
            + "\n");
        if (org.getChildOrganizations().size() != 0) {
          failcount = failcount + 1;
          debug.add("count of child orgs is not valid \n");
        }
      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganizations threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganizations threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_add_getService_Test
   *
   * @assertion_ids: JAXR:JAVADOC:614; JAXR:JAVADOC:622;JAXR:SPEC:79;
   *
   * @assertion: addService - Add a child Service getServices - Get all children
   * Services Capability Level:0 JAXR javadoc
   *
   * @test_Strategy: Create a service and an organization. use addService to add
   * the service to the organization use getServices and verify the service was
   * added.
   * 
   *
   */

  public void organization_add_getService_Test() throws Fault {
    String testName = "organization_add_getService_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String serviceName = "TS Test Service organization_addService_Test";
    try {
      debug.add(
          "Adding service name: " + serviceName + " to test organization \n");
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      // Create an Service instance
      Service myService = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));

      org.addService(myService);
      Service retService = null;
      Collection services = org.getServices();
      Iterator iter = services.iterator();
      while (iter.hasNext()) {
        retService = (Service) iter.next();
        debug.add("Retrieved service name: "
            + retService.getName().getValue(tsLocale) + " from organization\n");

        if (retService.getName().getValue(tsLocale).equals(serviceName)) {
          pass = true;
          break;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_addServices_Test
   *
   *
   * @assertion_ids: JAXR:JAVADOC:616;JAXR:SPEC:9;
   *
   * @assertion: addServices - Add a Collection of Service children Capability
   * Level:0 JAXR javadoc
   *
   * @test_Strategy: Create a couple of services and an organization. use
   * addServices to add the service to the organization use getServices and
   * verify the services were added.
   */

  public void organization_addServices_Test() throws Fault {
    String testName = "organization_addServices_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String serviceName1 = "TS Test Service1 organization_addService_Test";
    String serviceName2 = "TS Test Service2 organization_addService_Test";

    try {
      debug.add(
          "Adding service name: " + serviceName1 + " to test organization \n");
      debug.add(
          "Adding service name: " + serviceName2 + " to test organization \n");
      Collection myServiceNames = new ArrayList();
      myServiceNames.add(serviceName1);
      myServiceNames.add(serviceName2);

      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      // Create Services
      Collection services = new ArrayList();
      services.add(blm.createService(
          blm.createInternationalString(tsLocale, serviceName1)));
      services.add(blm.createService(
          blm.createInternationalString(tsLocale, serviceName2)));

      org.addServices(services);
      Service retService = null;
      Collection retServices = org.getServices();
      Iterator iter = retServices.iterator();
      int passcount = 0;
      while (iter.hasNext()) {
        retService = (Service) iter.next();
        debug.add("Retrieved service name: "
            + retService.getName().getValue(tsLocale) + " from organization\n");
        // see if the returned service matches one of the ones that were just
        // set
        if (myServiceNames.contains(retService.getName().getValue(tsLocale))) {
          passcount = passcount + 1;
          debug.add("Found expected service, name is: "
              + retService.getName().getValue(tsLocale) + "\n");
        } else {
          debug.add("Found service, name is: "
              + retService.getName().getValue(tsLocale) + "\n");
        }
      }
      if (passcount == retServices.size())
        pass = true;
      else {
        debug.add("Something is not right: ");
        debug.add("Found " + retServices.size() + " services");
        debug.add("Found " + passcount + " services passed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_set_getPrimaryContact
   *
   *
   * @assertion_ids: JAXR:JAVADOC:594;JAXR:JAVADOC:596;
   *
   * @assertion: setPrimaryContact - Sets the primary contact getPrimaryContact
   * - Gets the primary Contact for this Organization. Contact at organization.
   * Capability Level:0 JAXR javadoc
   *
   * @test_Strategy: user create an organization. Create a user and use
   * setPrimaryContact to add it to the organization. Use getPrimaryContact to
   * verify it.
   * 
   *
   */

  public void organization_set_getPrimaryContact() throws Fault {
    String testName = "organization_set_getPrimaryContact";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String userName1 = "TS Test Primary Contact";

    try {

      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      // Create a couple of Users
      User primaryContactUser = blm.createUser();

      PersonName primaryPersonName = blm.createPersonName(userName1);
      debug.add(
          "Created PersonName for primary contact.  Name: " + userName1 + "\n");
      User user = blm.createUser();
      user.setPersonName(primaryPersonName);

      debug.add("Added person name to user. Name: "
          + user.getPersonName().getFullName() + "\n");

      org.setPrimaryContact(user);
      debug.add("getPrimaryContact returned "
          + org.getPrimaryContact().getPersonName().getFullName() + "\n");
      if (org.getPrimaryContact().getPersonName().getFullName()
          .equals(userName1))
        pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_set_getTelephoneNumbers
   *
   *
   * @assertion_ids: JAXR:JAVADOC:610;JAXR:JAVADOC:612;
   *
   * @assertion: setTelephoneNumbers - Set the various telephone numbers for
   * this user getTelephoneNumbers - Gets the telephone numbers for this User
   * that match the specified telephone number type. If phoneType is null return
   * all telephoneNumbers. JAXR javadoc
   *
   * @test_Strategy: Create 3 telephone numbers and add them to an organization.
   * Verify that getTelephoneNumbers(null) returns all numbers.
   *
   */

  public void organization_set_getTelephoneNumbers() throws Fault {
    String testName = "organization_set_getTelephoneNumbers";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String number1 = "111-1234";
    String number2 = "222-1234";
    String number3 = "333-1234";
    try {
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      Collection numbers = new ArrayList();
      numbers.add(number1);
      numbers.add(number2);
      numbers.add(number3);

      // create a few phone numbers
      TelephoneNumber tn1 = blm.createTelephoneNumber();
      TelephoneNumber tn2 = blm.createTelephoneNumber();
      TelephoneNumber tn3 = blm.createTelephoneNumber();
      tn1.setNumber(number1);
      tn2.setNumber(number2);
      tn3.setNumber(number3);
      Collection phoneNumbers = new ArrayList();
      phoneNumbers.add(tn1);
      phoneNumbers.add(tn2);
      phoneNumbers.add(tn3);
      org.setTelephoneNumbers(phoneNumbers);
      debug.add("get all telephone numbers \n");
      Collection retNumbers = org.getTelephoneNumbers(null);
      // verify the numbers
      Iterator iter = retNumbers.iterator();
      TelephoneNumber number = null;
      while (iter.hasNext()) {
        number = (TelephoneNumber) iter.next();
        debug.add("Retrieved this number: " + number.getNumber() + "\n");
        if (!(numbers.contains(number.getNumber()))) {
          throw new Fault(testName + " failed ");
        } else
          debug.add("Found number in my number list!\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      super.cleanUpRegistry(orgName);
    }
  } // end of method

  /*
   * @testName: organization_set_getUsers
   *
   *
   * @assertion_ids: JAXR:JAVADOC:607;JAXR:JAVADOC:601;
   *
   * @assertion: getUsers - Gets the Collection of Users affiliated with this
   * Organization. addUsers - Sets the Users Capability Level:0 JAXR javadoc
   *
   * @test_Strategy:
   *
   *
   */

  public void organization_set_getUsers() throws Fault {
    String testName = "organization_set_getUsers";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String userName1 = "TS Test Primary Contact";
    User user = null;
    PersonName person = null;
    int count = 6;
    try {

      Collection userNames = new ArrayList();
      for (int i = 0; i < count; i++) {
        userNames.add("TS Test Person" + i);
      }
      Collection users = new ArrayList();
      debug.add("Setting up collection of users \n");
      for (Iterator iter = userNames.iterator(); iter.hasNext();) {
        user = blm.createUser();
        person = blm.createPersonName((String) iter.next());
        user.setPersonName(person);
        users.add(user);
        debug.add("Adding this person to user collection: "
            + user.getPersonName().getFullName() + "\n");
      }

      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      debug.add("Invoke addUsers on organization \n");
      org.addUsers(users);

      debug.add("getUsers and verify users returned from the organization \n");
      users.clear();
      users = org.getUsers();
      int passcount = 0;
      Iterator iter = users.iterator();
      while (iter.hasNext()) {
        user = (User) iter.next();
        debug.add("Found user: " + user.getPersonName().getFullName() + "\n");
        if (userNames.contains(user.getPersonName().getFullName())) {
          passcount = passcount + 1;
        }
      }
      if (passcount == userNames.size())
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_removeService_Test
   *
   *
   * @assertion_ids: JAXR:JAVADOC:618;JAXR:JAVADOC:622;
   *
   * @assertion: removeService - Remove a child Service Capability Level:0 JAXR
   * javadoc
   *
   * @test_Strategy: Create and add 2 services to an organization. Use
   * removeService to remove one of them. Verify the correct service was
   * removed.
   * 
   *
   *
   */

  public void organization_removeService_Test() throws Fault {
    String testName = "organization_removeService_Test";
    boolean pass = false;
    boolean found = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String serviceName = "TS Test Service organization_removeService_Test";
    String serviceName1 = "TS Test Service organization_removeService_Test1";

    try {
      debug.add("\nAdding following services: \n");
      debug.add("                " + serviceName + "  \n");
      debug.add("                " + serviceName1 + " \n");

      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      // Create an Service instance
      Service myService = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      Service anotherService = blm
          .createService(blm.createInternationalString(tsLocale, serviceName1));

      org.addService(myService);
      org.addService(anotherService);

      Service retService = null;
      Collection services = org.getServices();
      Iterator iter = services.iterator();
      while (iter.hasNext()) {
        retService = (Service) iter.next();
        debug.add("Retrieved service name: "
            + retService.getName().getValue(tsLocale) + " from organization\n");
        if (retService.getName().getValue(tsLocale).equals(serviceName)) {
          found = true;
        }
      }
      if (!found)
        throw new Fault(testName
            + "Error: failed to find added service - test did not complete!");

      debug.add("Added and verified service.  Will now remove the service");
      org.removeService(myService);

      debug.add("Verify that one service has been removed\n");
      services.clear();
      retService = null;
      services = org.getServices();
      found = false;
      if (services.size() > 0) {
        iter = services.iterator();
        while (iter.hasNext()) {
          retService = (Service) iter.next();
          debug.add("Retrieved service name: "
              + retService.getName().getValue(tsLocale)
              + " from organization\n");
          if (retService.getName().getValue(tsLocale).equals(serviceName)) {
            found = true;
            break;
          }
        }
      }
      if (!found)
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_removeServices_Test
   *
   *
   * @assertion_ids: JAXR:JAVADOC:620;
   *
   * @assertion: removeServices - Remove a child Service Capability Level:0 JAXR
   * javadoc
   *
   * @test_Strategy: Create and add 2 services to an organization. Use
   * removeServices to remove them. Verify the services were removed.
   *
   *
   */

  public void organization_removeServices_Test() throws Fault {
    String testName = "organization_removeServices_Test";
    boolean pass = false;
    boolean found = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String serviceName = "TS Test Service organization_removeService_Test";
    String serviceName1 = "TS Test Service organization_removeService_Test1";

    try {
      debug.add("\nAdding following services: \n");
      debug.add("                " + serviceName + "  \n");
      debug.add("                " + serviceName1 + " \n");

      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      // Create two Service instances
      Service myService = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      Service anotherService = blm
          .createService(blm.createInternationalString(tsLocale, serviceName1));

      Collection myServiceNames = new ArrayList();
      myServiceNames.add(serviceName);
      myServiceNames.add(serviceName1);

      Collection ctsServices = new ArrayList();
      ctsServices.add(myService);
      ctsServices.add(anotherService);

      org.addServices(ctsServices);
      Service retService = null;

      Collection services = org.getServices();
      Iterator iter = services.iterator();
      int passcount = 0;
      while (iter.hasNext()) {
        retService = (Service) iter.next();
        debug.add("Retrieved service name: "
            + retService.getName().getValue(tsLocale) + " from organization\n");
        if ((myServiceNames
            .contains(retService.getName().getValue(tsLocale)))) {
          passcount = passcount + 1;
        }
      }
      if (passcount != myServiceNames.size())
        throw new Fault(testName + "Error test did not complete !");

      debug.add("Added and verified services   Will now remove the services");
      org.removeServices(ctsServices);
      debug.add("Verify that both services has been removed\n");
      services.clear();
      retService = null;
      services = org.getServices();
      found = false;
      if (services.size() > 0) {
        iter = services.iterator();
        while (iter.hasNext()) {
          retService = (Service) iter.next();
          debug.add("Retrieved service name: "
              + retService.getName().getValue(tsLocale)
              + " from organization\n");
          if (myServiceNames
              .contains(retService.getName().getValue(tsLocale))) {
            found = true;
          }
        }
      }
      if (!found)
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_getChildOrganizationCount_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:37;
   *
   * @assertion: getChildOrganizationCount - Get number of children Capability
   * Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void organization_getChildOrganizationCount_Test() throws Fault {
    String testName = "organization_getChildOrganizationCount_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        int childCount = org.getChildOrganizationCount();
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // level 1 providers will get here.
        debug.add("No children were added \n");
        debug.add("Child count is: " + childCount + "\n");

        if (childCount != 0)
          failcount = failcount + 1;
        String childOrgName1 = "TS Test Child Organization1";
        String childOrgName2 = "TS Test Child Organization2";
        InternationalString ichildOrgName1 = blm
            .createInternationalString(tsLocale, childOrgName1);
        InternationalString ichildOrgName2 = blm
            .createInternationalString(tsLocale, childOrgName2);
        Organization childOrg1 = blm.createOrganization(ichildOrgName1);
        Organization childOrg2 = blm.createOrganization(ichildOrgName2);
        Collection myChildOrgs = new ArrayList();
        myChildOrgs.add(childOrg1);
        myChildOrgs.add(childOrg2);
        org.addChildOrganizations(myChildOrgs);

        debug.add("Two children were added \n");
        debug.add("Child count is: " + org.getChildOrganizationCount() + "\n");
        if (org.getChildOrganizationCount() != 2) {
          failcount = failcount + 1;
          debug.add("count of child orgs is not valid \n");
        }

      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_getChildOrganizations_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:38;
   *
   * @assertion: getChildOrganizations - Get all immediate children
   * Organizations Capability Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void organization_getChildOrganizations_Test() throws Fault {
    String testName = "organization_getChildOrganizations_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        Collection children = org.getChildOrganizations();
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // level 1 provider should get here.
        debug.add("count of child orgs is " + org.getChildOrganizations().size()
            + "\n");
        if (org.getChildOrganizations().size() != 0) {
          failcount = failcount + 1;
          debug.add("count of child orgs is not valid \n");
        }
      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: removeChildOrganization_Test
   *
   * @assertion_ids: JAXR:SPEC:35;
   * 
   * @assertion: removeChildOrganization - Remove a child Organization
   * Capability Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void removeChildOrganization_Test() throws Fault {
    String testName = "removeChildOrganization_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String childOrgName = "TS Test Child Organization";
    Organization childOrg = null;
    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        // try to add a child organization to it.
        InternationalString ichildOrgName = blm
            .createInternationalString(tsLocale, childOrgName);
        childOrg = blm.createOrganization(ichildOrgName);
        org.removeChildOrganization(childOrg);
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // level 1 providers will get here
        childOrg = blm.createOrganization(ichildOrgName);
        org.addChildOrganization(childOrg);
        // check the size
        debug
            .add("Added one child organization,  verify the collection size\n");
        debug.add("count of child orgs is " + org.getChildOrganizations().size()
            + "\n");
        if (org.getChildOrganizations().size() != 1) {
          failcount = failcount + 1;
          debug.add("count of child orgs is not valid \n");
        }
        // remove the child
        org.removeChildOrganization(childOrg);
        // check the size
        debug.add(
            "Removed the one child organization,  verify the collection size\n");
        debug.add("count of child orgs is " + org.getChildOrganizations().size()
            + "\n");
        if (org.getChildOrganizations().size() != 0) {
          failcount = failcount + 1;
          debug.add("count of child orgs is not valid \n");
        }
      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_getDescendantOrganizations_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:39;
   *
   * @assertion: getDescendantOrganizations - Get all descendant Organizations
   * Capability Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void organization_getDescendantOrganizations_Test() throws Fault {
    String testName = "organization_getDescendantOrganizations_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        Collection descendants = org.getDescendantOrganizations();
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // level 1 providers will get here.
        debug.add(
            "no descendants yet - getDescendantOrganizations size should be 0 \n");
        debug.add("descendants.size() is: " + descendants.size() + "\n");
        if (descendants.size() != 0) {
          failcount = failcount + 1;
          debug.add("count of descendant orgs is not valid \n");
        }
        // give org some descendants.
        String childOrgName = testName + "_childOrganization";
        InternationalString ichildOrgName = blm
            .createInternationalString(tsLocale, childOrgName);
        Organization childOrg = blm.createOrganization(ichildOrgName);
        org.addChildOrganization(childOrg);
        debug.add(
            "Added a child organization, call org.getDescendantOrganizations\n");
        Collection mydescendants = org.getDescendantOrganizations();

        debug.add(
            "now there is a descendant - getDescendantOrganizations size should be 1  \n");
        debug.add("descendants.size() is: " + mydescendants.size() + "\n");
        if (mydescendants.size() != 1) {
          failcount = failcount + 1;
          debug.add("count of descendant orgs is not valid \n");
        }

      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_getParentOrganization_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:40;
   *
   * @assertion: getParentOrganization - Get the parent (container) organization
   * Capability Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void organization_getParentOrganization_Test() throws Fault {
    String testName = "organization_getParentOrganization_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        Organization parent = org.getParentOrganization();
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // level 1 providers
        // getParentOrganization returns the parent Organization or null if
        // object has no parent Organization
        if (parent != null) {
          debug.add(
              "Error - there is no parent, so parent org returned should be null \n");
          failcount = failcount + 1;
        } else {
          debug.add("org.getParentOrganization() returned null as expected \n");
        }

        // create a parent
        String parentName = testName + "_ParentOrganization";
        InternationalString iname = blm.createInternationalString(tsLocale,
            parentName);
        Organization theParentOrg = blm.createOrganization(iname);
        theParentOrg.addChildOrganization(org);
        Organization myParentOrg = org.getParentOrganization();

        if (myParentOrg == null) {
          debug.add(
              "Error - there is a parent, so parent org returned should not be null \n");
          failcount = failcount + 1;
        } else {
          if (myParentOrg.getKey().getId()
              .equals(theParentOrg.getKey().getId())) {
            debug.add("Verified the parent org returned - success \n");
          } else {
            debug.add(
                "Error - parent key does not match returned parent key \n");
            failcount = failcount + 1;
          }
        }

      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_getPostalAddress_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:31;
   *
   * @assertion: getPostalAddress - Gets the Address for this Organization
   * Capability Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void organization_getPostalAddress_Test() throws Fault {
    String testName = "organization_getPostalAddress_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        PostalAddress pa = org.getPostalAddress();
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_setPostalAddress_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:32;
   *
   * @assertion: setPostalAddress - Sets the address Capability Level: 1 JAXR
   * javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void organization_setPostalAddress_Test() throws Fault {
    String testName = "organization_setPostalAddress_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String streetNumber = "1";
    String street = "Amsden";
    String city = "Arlington";
    String stateOrProvince = "MA";
    String country = "US";
    String postalCode = "02174";
    String type = "homebase";

    int failcount = 0;
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
            stateOrProvince, country, postalCode, type);
        org.setPostalAddress(pa);
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // verify postal address - level 1 provider
        PostalAddress retPa = org.getPostalAddress();
        debug.add("Verify postal address \n");
        debug.add("StreetNumber: " + retPa.getStreetNumber() + "\n");
        if (!retPa.getStreetNumber().equals(streetNumber)) {
          debug.add("incorrect street number \n");
          failcount = failcount + 1;
        }
        debug.add("Street : " + retPa.getStreet() + "\n");
        if (!retPa.getStreet().equals(street)) {
          debug.add("incorrect street \n");
          failcount = failcount + 1;
        }
        debug.add("city: " + retPa.getCity() + "\n");
        if (!retPa.getCity().equals(city)) {
          debug.add("incorrect city\n");
          failcount = failcount + 1;
        }
        debug.add("stateorprovince: " + retPa.getStateOrProvince() + "\n");
        if (!retPa.getStateOrProvince().equals(stateOrProvince)) {
          debug.add("incorrect stateOrProvince\n");
          failcount = failcount + 1;
        }
        debug.add("country: " + retPa.getCountry() + "\n");
        if (!retPa.getCountry().equals(country)) {
          debug.add("incorrect country\n");
          failcount = failcount + 1;
        }
        debug.add("postalCode: " + retPa.getPostalCode() + "\n");
        if (!retPa.getPostalCode().equals(postalCode)) {
          debug.add("incorrect postal code\n");
          failcount = failcount + 1;
        }
        debug.add("type: " + retPa.getType() + "\n");
        if (!retPa.getType().equals(type)) {
          debug.add("incorrect type code\n");
          failcount = failcount + 1;
        }

      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_getRootOrganization_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:41;
   *
   * @assertion: getRootOrganization - Get the root (container) organization
   * Capability Level: 1 JAXR javadoc
   *
   * A JAXR provider must implement all methods that are assigned a capability
   * level that is greater than the capability level declared by the JAXR
   * provider, to throw an UnsupportedCapabilityException. JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   */
  public void organization_getRootOrganization_Test() throws Fault {
    String testName = "organization_getRootOrganization_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    int failcount = 0;
    String childOrgName = testName + "_ChildOrganization";
    try {
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      try {
        Organization root = org.getRootOrganization();
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected removeChildOrganization to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
        // Note:
        // The api javadoc has a an error. It states that the
        // getRootOrganization method
        // should return the root Organization
        // or null if object has no parent Organization
        // There is a jaxr bug entered on this.
        // Root by definition should be the highest level node
        // and should be the org itself if it has no parent.

        if (root != null) {
          if (org.getKey().getId().equals(root.getKey().getId())) {
            debug.add(" Good - root id was verified \n");
          }
        } else {
          failcount = failcount + 1;
          debug.add(" root should be the highest level node. In this case, \n");
          debug.add(
              " it should be the org itself,  since there is no parent \n");
        }
        InternationalString ichildOrgName = blm
            .createInternationalString(tsLocale, childOrgName);

        Organization childOrg = blm.createOrganization(ichildOrgName);
        org.addChildOrganization(childOrg);
        debug.add(
            "Added a child organization, call getRootOrganization on the child \n");
        Organization myRoot = childOrg.getRootOrganization();

        if (root != null) {
          debug.add(
              " root is not null.  There is a parent.  This is expected  \n");
          if (org.getKey().getId().equals(myRoot.getKey().getId())) {
            debug.add(" Good - root id was verified \n");
          } else {
            debug.add(" Error - root id was not verifier \n");
            failcount = failcount + 1;
          }
        } else {
          failcount = failcount + 1;
          debug.add(" root is null - this is an error\n");
        }

      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! removeChildOrganization threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add(
            "Error: removeChildOrganization threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_Test
   *
   *
   * @assertion_ids: JAXR:JAVADOC:618;JAXR:SPEC:82;JAXR:SPEC:84;JAXR:SPEC:85;
   *
   * @Assertion: Organization instances provide information on organizations
   * such as a Submitting Organization. JAXR specification 1.0
   *
   * @test_Strategy: Create an Organization. Fully populate it. Publish it. Find
   * the newly published organization in the registry. Verify the organaization
   * data.
   * 
   *
   */
  public void organization_Test() throws Fault {
    int index = 0;
    String testName = "organization_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    int failcount = 0;
    Service ctsService = null;
    User ctsUser = null;
    PersonName ctsPersonName = null;
    TelephoneNumber ctsTelephoneNumber = null;
    int serviceCount = 3;
    String serviceName = "TS Test Service";
    String serviceNames = "TS Test Services";
    String primaryContactPersonFullName = "TS Test PrimaryContactPerson";
    Collection orgKeys = null;

    try {
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      // addService(service)
      debug.add("\n==========================addservice(service) \n");
      ctsService = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      org.addService(ctsService);

      // addServices(services)
      debug.add("==========================addservices(services) \n");
      // Create a list of names for the services
      Collection myServiceNames = new ArrayList();
      for (int i = 0; i < serviceCount; i++) {
        myServiceNames.add(serviceNames + i);
        debug.add("Adding service name: " + serviceNames + i
            + " to test organization \n");
      }
      // Create Services
      Collection ctsServices = new ArrayList();
      for (int i = 0; i < serviceCount; i++) {
        ctsServices.add(blm.createService(
            blm.createInternationalString(tsLocale, serviceNames + i)));
      }
      org.addServices(ctsServices);

      // Set the primary contact
      debug.add("================================================\n");
      User primaryContactUser = blm.createUser();
      PersonName primaryContactPerson = blm
          .createPersonName(primaryContactPersonFullName);
      primaryContactUser.setPersonName(primaryContactPerson);
      org.setPrimaryContact(primaryContactUser);

      // setUsers
      debug.add("================================================\n");

      int count = 10;
      User user = null;
      PersonName person = null;
      Collection userNames = new ArrayList();
      for (int i = 0; i < count; i++) {
        userNames.add("TS Test Person" + i);
      }
      Collection users = new ArrayList();
      debug.add("Setting up collection of users \n");
      for (Iterator iter = userNames.iterator(); iter.hasNext();) {
        user = blm.createUser();
        person = blm.createPersonName((String) iter.next());
        user.setPersonName(person);
        users.add(user);
        debug.add("Adding this person to user collection: "
            + user.getPersonName().getFullName() + "\n");
      }
      org.addUsers(users);

      // Publish the organization....
      orgKeys = publishOrg(org);
      if (orgKeys == null) {
        debug.add("Error: could not publish the  organization\n");
        throw new Fault(testName + "test did not complete!");
      }
      // get the id
      Iterator iter = orgKeys.iterator();
      Key orgKey = null;
      while (iter.hasNext()) {
        orgKey = (Key) iter.next();
      }
      // Retrieve the Organization from the registry
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      org = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);
      if (org == null) {
        debug.add("Error: could not find the just published organization\n");
        throw new Fault(testName + "test did not complete!");
      }
      // verify the organization has the services that were added to it
      debug.add("================================================\n");
      debug.add("Find all the services that were added to the organization\n");

      Collection allServiceNames = new ArrayList();
      allServiceNames.addAll(myServiceNames);
      allServiceNames.add(serviceName);
      Collection retServices = org.getServices();
      Service retService = null;
      iter = retServices.iterator();
      int servicesFound = 0;
      ctsService = null;
      ctsServices.clear();
      while (iter.hasNext()) {
        retService = (Service) iter.next();
        debug.add("Retrieved service name: "
            + retService.getName().getValue(tsLocale) + " from organization\n");
        // see if the returned service matches one of the ones that were just
        // set
        if (allServiceNames.contains(retService.getName().getValue(tsLocale))) {
          servicesFound = servicesFound + 1;
        }
        if (retService.getName().getValue(tsLocale).equals(serviceName)) {
          debug.add("Setting ctsService for removeService \n");
          ctsService = retService;
        }
      }
      if (servicesFound == allServiceNames.size()) {
        debug.add("Success: found all my services! \n");

      } else {
        failcount = failcount + 1;
        debug.add("Error: failed to find added services\n");
      }

      // remove a single service
      org.removeService(ctsService);
      // verify the service has been removed.from the organization.
      retServices = org.getServices();
      retService = null;
      iter = retServices.iterator();
      servicesFound = 0;
      debug.add("==============================================\n");
      debug.add("Verify all added services have been removed: \n");
      while (iter.hasNext()) {
        retService = (Service) iter.next();
        debug.add("Retrieve service name to verify removal: "
            + retService.getName().getValue(tsLocale) + " from organization\n");
        // see if the service name matches the removed one.
        if (retService.getName().getValue(tsLocale).equals(serviceName)) {
          failcount = failcount + 1;
          debug.add("Error: found removed Service: " + serviceName + "\n");
        } else {
          debug.add("Adding retService to ctsServices for removeServices \n");
          ctsServices.add(retService);
        }

      }

      // remove all of services.
      org.removeServices(ctsServices);

      // verify that these were removed.
      retServices = org.getServices();
      retService = null;
      debug.add("==============================================\n");
      debug.add("removing  services:  \n");

      iter = retServices.iterator();
      servicesFound = 0;
      while (iter.hasNext()) {
        retService = (Service) iter.next();
        debug.add("Retrieved service name: "
            + retService.getName().getValue(tsLocale) + " from organization\n");
        // see if the returned service matches one of the ones that were just
        // set
        if (myServiceNames.contains(retService.getName().getValue(tsLocale))) {
          failcount = failcount = 1;
          debug.add("Error: found a removed service!\n");
        }
      }

      // Get and verify the primary contact
      User pc = org.getPrimaryContact();
      debug.add("==============================================\n");

      debug.add("The primary contact returned from the organization was: "
          + pc.getPersonName().getFullName() + "\n");
      if (!(pc.getPersonName().getFullName()
          .equals(primaryContactPersonFullName))) {
        debug.add(
            "Error: PrimaryContact returned from organization is not valid \n");
        failcount = failcount + 1;
      }

      // getUsers
      users.clear();
      debug.add("==============================================\n");

      users = org.getUsers();
      iter = users.iterator();
      int found = 0;
      debug.add(
          "Adding the primary contact to the list of users to be returned\n");
      userNames.add(primaryContactPersonFullName);

      while (iter.hasNext()) {
        user = (User) iter.next();
        debug.add("Found user: " + user.getPersonName().getFullName() + "\n");
        if (userNames.contains(user.getPersonName().getFullName())) {
          found = found + 1;
        } else if (user.getPersonName().getFullName()
            .equals(primaryContactPersonFullName)) {
          found = found + 1;
        }
      }

      if (found == userNames.size()) {
        debug.add("Success: found all my Users! \n");
      } else {
        failcount = failcount + 1;
        debug.add("Failed to find all my Users! Bug# 4532179 \n");
      }

      if (failcount == 0) {
        pass = true;
      } else {
        debug.add("Failure count for this test: " + failcount + "\n");
      }

      if (failcount == 0)
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      debug.add("cleanup registry - remove test organization\n");
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  private Collection publishOrg(Organization org) {
    Collection keys = null;
    try {
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug)))
        return keys;
      keys = br.getCollection();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return keys;
  }

  private int checkServices(ArrayList myServiceNames, Organization org) {
    int failcount = 0;
    try {
      // verify the organization has the services that were added to it
      // myServiceNames.add(serviceName);
      Collection retServices = org.getServices();
      Service retService = null;
      Iterator iter = retServices.iterator();
      int servicesFound = 0;
      while (iter.hasNext()) {
        retService = (Service) iter.next();
        debug.add("Retrieved service name: "
            + retService.getName().getValue(tsLocale) + " from organization\n");
        // see if the returned service matches
        if (myServiceNames.contains(retService.getName().getValue(tsLocale))) {
          servicesFound = servicesFound + 1;
          debug.add("Found expected service, name is: "
              + retService.getName().getValue(tsLocale) + "\n");
        }
      }
      if (servicesFound == myServiceNames.size()) {
        debug.add("Success: found all my services! \n");

      } else {
        failcount = failcount + 1;
        debug.add("Failed to find added services\n");
      }
    } catch (Exception e) {
    }
    return failcount;
  } // end of method

  /*
   * @testName: organization_addUserTest
   *
   * @assertion_ids: JAXR:JAVADOC:599;
   *
   * @test_Strategy: Use the addUser method to add multiple users to an
   * organization. Call getUsers and verify that all the users were added.
   *
   */
  public void organization_addUserTest() throws Fault {
    String testName = "organization_addUserTest";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    User user = null;
    PersonName person = null;
    int count = 20;
    try {
      Collection userNames = new ArrayList();
      for (int i = 0; i < count; i++) {
        userNames.add("TS Test Person" + i);
      }
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      debug.add("Adding users one at a time with addUser method.\n");
      for (Iterator iter = userNames.iterator(); iter.hasNext();) {
        user = blm.createUser();
        person = blm.createPersonName((String) iter.next());
        user.setPersonName(person);
        org.addUser(user);
      }

      debug.add(
          "getUsers and verify all of the users were returned from the organization \n");
      Collection users = org.getUsers();
      int passcount = 0;
      Iterator iter = users.iterator();
      while (iter.hasNext()) {
        user = (User) iter.next();
        debug.add("Found user: " + user.getPersonName().getFullName() + "\n");
        if (userNames.contains(user.getPersonName().getFullName())) {
          passcount = passcount + 1;
        }
      }
      if (passcount == userNames.size())
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: organization_removeUsersTest
   *
   * @assertion_ids: JAXR:JAVADOC:605;
   *
   * @test_Strategy: Use the addUser method to add users to an organization.
   * Then verify that removeUsers removes all of the users.
   *
   */
  public void organization_removeUsersTest() throws Fault {
    String testName = "organization_removeUsersTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    User user = null;
    PersonName person = null;
    int count = 20;
    try {

      Collection userNames = new ArrayList();
      for (int i = 0; i < count; i++) {
        userNames.add("TS Test Person" + i);
      }
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      debug.add("Invoke addUser on organization \n");

      for (Iterator iter = userNames.iterator(); iter.hasNext();) {
        user = blm.createUser();
        person = blm.createPersonName((String) iter.next());
        user.setPersonName(person);
        org.addUser(user);
      }

      Collection users = org.getUsers();
      int passcount = 0;
      Iterator iter = users.iterator();
      while (iter.hasNext()) {
        user = (User) iter.next();
        if (userNames.contains(user.getPersonName().getFullName())) {
          passcount = passcount + 1;
        }
      }
      if (passcount != userNames.size())
        throw new Fault(testName + "Error: test didnot complete");
      // Remove the users
      org.removeUsers(users);
      users.clear();
      users = org.getUsers();
      if (users.size() == 0) {
        debug.add("good! all users were removed\n");
      } else {
        debug.add("Error: all users not removed!\n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }

  } // end of method

  /*
   * @testName: organization_removeUserTest
   *
   * @assertion_ids: JAXR:JAVADOC:603;
   *
   * @test_Strategy: Use the addUser method to a add user Then verify that
   * removeUser removes the user.
   *
   */
  public void organization_removeUserTest() throws Fault {
    String testName = "organization_removeUserTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    User user = null;
    PersonName person = null;
    String personName = "TS User 1";
    try {
      // Create an organization.
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      debug.add("Add one user to an organization \n");

      user = blm.createUser();
      person = blm.createPersonName(personName);
      user.setPersonName(person);
      org.addUser(user);

      Collection users = org.getUsers();
      if (!(users.contains(user))) {
        debug.add("Error: expected organization to contain my user \n");
        debug.add("Test did not complete \n");
        throw new Fault(testName + " failed ");
      }
      debug.add("Remove the user");
      org.removeUser(user);
      users = org.getUsers();
      if (users.contains(user)) {
        debug.add("Error: expected user to be removed from organization\n");
        throw new Fault(testName + " failed ");
      } else
        debug.add("Good - user was removed");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      super.cleanUpRegistry(orgName);
    }

  } // end of method
} // end of class
