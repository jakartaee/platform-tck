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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.User;

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
   * @testName: user_getEmailAddresses_Test
   *
   * @assertion_ids: JAXR:JAVADOC:338;JAXR:JAVADOC:340;
   *
   * @test_Strategy: Create email addresses. add them to user with
   * setEmailAddresses. Verify the addition with getEmailAddresses().
   *
   */

  public void user_getEmailAddresses_Test() throws Fault {

    String testName = "user_getEmailAddresses_Test";
    boolean pass = false;
    Collection email = null;
    try {
      Vector emailAddresses = new Vector();
      emailAddresses.add("Barney@Bedrock.org");
      emailAddresses.add("Betty@Bedrock.org");

      // create a User
      User user = blm.createUser();
      Collection emails = new ArrayList();
      // create an email address
      EmailAddress e = null;
      for (int i = 0; i < emailAddresses.size(); i++) {
        e = blm.createEmailAddress((String) emailAddresses.get(i));
        emails.add(e);
        debug.add("Adding this email address to the list: "
            + emailAddresses.get(i) + "\n");
      }
      // give the user the email address
      user.setEmailAddresses(emails);
      EmailAddress ea = null;
      // now get and verify
      Collection retEmails = user.getEmailAddresses();

      Iterator iter = retEmails.iterator();
      while (iter.hasNext()) {
        // get each email address in the collection and verify it is as set
        ea = (EmailAddress) iter.next();
        debug.add("Got Back this address: " + ea.getAddress() + " \n");
        if (!(emailAddresses.contains(ea.getAddress()))) {
          debug.add(
              "Error: It does not match any of the addresses that were set\n");
          throw new Fault(testName + " failed ");
        }
      }
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
   * @testName: user_getPersonName_Test
   *
   *
   * @assertion: getPersonName() - Name of contact person JAXR javadoc
   *
   * @assertion_ids: JAXR:JAVADOC:322;JAXR:JAVADOC:324;
   *
   * @test_Strategy: Create a person. Add it to a user with setPersonName.
   * Verify it with getPersonName.
   *
   */
  public void user_getPersonName_Test() throws Fault {
    String testName = "user_getPersonName_Test";
    boolean pass = false;
    String person = "Mickey Mouse";
    try {
      // create a User
      User user = blm.createUser();
      debug.add("Create a PersonName instance for " + person + "\n");
      PersonName myPerson = blm.createPersonName(person);
      debug.add("Set Name of contact person on User object \n");
      user.setPersonName(myPerson);
      debug.add("Call get method to get back person\n");
      PersonName retPersonName = user.getPersonName();
      String thePerson = retPersonName.getFullName();
      debug.add(
          "The person returned from getPersonName was: " + thePerson + "\n");
      if (!(thePerson.equals(person))) {
        debug.add("Error: unexpected person returned \n");
        throw new Fault(testName + " failed ");
      }
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
   * @testName: user_getOrganization_Test
   *
   * @assertion: getOrganization() - Gets the Submitting Organization that sent
   * the request that effected this change. JAXR javadoc
   *
   * @assertion_ids: JAXR:JAVADOC:320
   *
   * @test_Strategy: Create an organization. Add a user. Submit the
   * organization. Query the registry for the organization. Call getOrganization
   * on User. And verify it matches the submitting organization
   *
   *
   */
  public void user_getOrganization_Test() throws Fault {

    String testName = "user_getOrganization_Test";
    boolean pass = false;
    String person = "Mickey Mouse";
    Organization org;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    BusinessQueryManager bqm = null;
    Key orgKey = null;

    try {

      // create a User
      bqm = rs.getBusinessQueryManager();
      User user = blm.createUser();
      debug.add("Create a PersonName instance for " + person + "\n");
      PersonName myPerson = blm.createPersonName(person);
      debug.add("Set Name of contact person on User object \n");
      user.setPersonName(myPerson);
      // create the default organization
      org = JAXR_Util.createDefaultOrganization(blm);
      debug.add("Add this User to the default organization\n");
      Collection users = new ArrayList();
      users.add(user);
      org.addUsers(users);
      debug.add("publish the organization to the registry \n");
      // --
      // Publish the organization
      orgKey = saveMyOrganization(org);

      // Find the published organization
      Organization pubOrg = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);

      if (pubOrg == null) {
        debug.add("Error - org not found\n");
        throw new Fault(testName
            + " Error from saveOrganizations - test did not complete!");
      }

      debug.add("Get the Users from this organization \n");
      users.clear();
      users = pubOrg.getUsers();
      debug.add("Find the User \n");
      Iterator itr = users.iterator();
      while (itr.hasNext()) {
        user = (User) itr.next();
        debug.add("Searching for the correct User\n");
        myPerson = user.getPersonName();
        debug.add("Found " + myPerson.getFullName() + " \n");
        if (myPerson.getFullName().equals(person)) {
          debug.add("Found the User, got the right organization !! \n");
          debug.add("And what about getOrg?? " + user.getOrganization() + "\n");
          break;
        }
      }
      debug.add("Now get the organization from the User \n");

      // Test getOrganization method here...
      Organization testOrg = user.getOrganization();
      debug.add("testOrg is what? " + testOrg + "\n");

      users = testOrg.getUsers();
      debug.add("Find my User \n");
      itr = users.iterator();
      while (itr.hasNext()) {
        user = (User) itr.next();
        debug.add("Searching for the correct User\n");
        myPerson = user.getPersonName();
        debug.add("Found " + myPerson.getFullName() + " \n");
        if (myPerson.getFullName().equals(person)) {
          debug.add("Found the User, getOrganization was a success!! \n");
          pass = true;
          break;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      debug.add("cleanup registry - remove test organization\n");
      Collection keys = new ArrayList();
      keys.add(orgKey);
      cleanUpRegistry(keys, LifeCycleManager.ORGANIZATION);
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end test method

  /*
   * @testName: user_getType_Test
   *
   * @assertion_ids: JAXR:JAVADOC:342;JAXR:JAVADOC:344;
   *
   * @assertion: getType() - The type for this object. Default is a NULL String.
   *
   * @test_Strategy:
   *
   */

  public void user_getType_Test() throws Fault {

    String testName = "user_getType_Test";
    boolean pass = false;
    String myUseType = "Use Type: Technical Contact";
    try {

      // create a User
      User user = blm.createUser();
      debug.add("\nSet the type for this User to " + myUseType + "\n");
      user.setType(myUseType);
      debug.add("Use the getType method to retrieve the type just set\n");
      String retType = user.getType();
      debug.add("getType returned: " + retType + "\n");
      if (retType.equals(myUseType)) {
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

  /*
   * @testName: user_getUrl_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:70; JAXR:SPEC:71;
   *
   * @assertion: getUrl() - The URL to the web page for this contact. Level 1
   * functionality. JAXR javadoc A JAXR provider must implement all methods that
   * are assigned a capability level that is greater than the capability level
   * declared by the JAXR provider, to throw an UnsupportedCapabilityException.
   * JAXR Specification
   *
   * @test_Strategy: Level 0 providers should throw
   * UnsupportedCapabilityException Level 1 providers should not throw and
   * exception
   *
   *
   */

  public void user_getUrl_Test() throws Fault {

    String testName = "user_getUrl_Test";
    boolean pass = false;
    int failcount = 0;
    java.net.URL url = null;
    String resource = "http://java.sun.com/FAQ.html";
    try {
      url = new URL(resource);
      debug.add("\nCreate a url for this test => " + url.toString() + "\n");
      // create a User
      User user = blm.createUser();
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add("\n The capability level for this provider is: " + providerlevel
          + "\n");
      // test setUrl
      try {
        user.setUrl(url);
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected setUrl to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! setUrl threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e0) {
        TestUtil.printStackTrace(e0);
        failcount = failcount + 1;
        debug.add("Error: setUrl threw an unexpected exception \n");
        debug.add("Exception: " + e0.toString() + "\n");
      }
      // test getUrl
      try {
        url = user.getUrl();
        debug.add("Level 0 providers should not get here!\n");
        if (!(providerlevel > 0)) {
          debug.add(
              "Error: expected getUrl to throw UnsupportedCapabilityException \n");
          failcount = failcount + 1;
        }
      } catch (UnsupportedCapabilityException uc) {
        TestUtil.printStackTrace(uc);
        if (providerlevel == 0) {
          debug.add(
              "Success! getUrl threw UnsupportedCapabilityException as expected\n");
        } else {
          debug.add(
              "Error: UnsupportedCapabilityException not expected for this provider\n");
          failcount = failcount + 1;
        }
      } catch (Exception e1) {
        TestUtil.printStackTrace(e1);
        failcount = failcount + 1;
        debug.add("Error: getUrl threw an unexpected exception \n");
        debug.add("Exception: " + e1.toString() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      failcount = failcount + 1;
    }
    if (failcount == 0)
      pass = true;

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

  /*
   * @testName: user_getTelephoneNumbers_Test
   *
   * @assertion_ids: JAXR:JAVADOC:334;JAXR:JAVADOC:336;
   *
   * @assertion: getTelephoneNumbers() - Gets the telephone numbers for this
   * User that match the specified telephone number type. If phoneType is null
   * return all telephoneNumbers JAXR javadoc
   *
   * @test_Strategy: create a new user. Set a telephone number. Get and verify
   * its the correct one.
   *
   */

  public void user_getTelephoneNumbers_Test() throws Fault {

    String testName = "user_getTelephoneNumbers_Test";
    String telNum = "781-442-1234";
    boolean pass = false;
    try {
      TelephoneNumber number = blm.createTelephoneNumber();
      number.setNumber(telNum);
      Collection numbers = new ArrayList();
      numbers.add(number);
      TelephoneNumber retNum = null;
      // create a User
      User user = blm.createUser();
      user.setTelephoneNumbers(numbers);
      debug.add("\nSet the phone number for this user to: " + telNum + "\n");
      numbers = user.getTelephoneNumbers(null);
      // get the number from the collection
      Iterator iter = numbers.iterator();
      while (iter.hasNext()) {
        retNum = (TelephoneNumber) iter.next();
        debug.add("Got this telephone number:  " + retNum.getNumber() + "\n");
        if (retNum.getNumber().equals(telNum)) {
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
  } // end

  /*
   * @testName: user_getPostalAddress_Test
   *
   * @assertion_ids: JAXR:JAVADOC:328;JAXR:JAVADOC:326;
   *
   * @assertion: getPostalAddress() - The postal address for this Contact. JAXR
   * javadoc
   *
   * @test_Strategy: create a new user. Set a PostalAddress. Get and verify its
   * the correct one. Use slots.
   */
  public void user_getPostalAddress_Test() throws Fault {

    String testName = "user_getPostalAddress_Test";
    boolean pass = false;
    String streetNumber = "1";
    String street = "Amsden";
    String city = "Arlington";
    String stateOrProvince = "MA";
    String country = "US";
    String postalCode = "02174";
    String type = "homebase";
    int failcount = 0;
    try {
      debug.add("Issue:  what is slot type supposed to be ????\n");
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);

      Collection addressValues = new ArrayList();
      addressValues.add(streetNumber);
      addressValues.add(street);
      addressValues.add(city);
      addressValues.add(stateOrProvince);
      addressValues.add(country);
      addressValues.add(postalCode);
      addressValues.add(type);

      Slot mySlot = blm.createSlot(Slot.ADDRESS_LINES_SLOT, addressValues,
          "WhatIsThisSupposedToBe?");
      pa.addSlot(mySlot);

      debug.add("\nSetting Postal Address as follows:\n");
      debug.add("streetNumber is " + streetNumber + "\n");
      debug.add("street" + street + "\n");
      debug.add("city is " + city + "\n");
      debug.add("stateOrProvince is " + stateOrProvince + "\n");
      debug.add("country is " + country + "\n");
      debug.add("postalCode is " + postalCode + "\n");
      debug.add("type is " + type + "\n");

      // create a User
      User user = blm.createUser();
      Collection pas = new ArrayList();
      pas.add(pa);
      user.setPostalAddresses(pas);
      // PostalAddress retPa = user.getPostalAddress();

      Collection retPas = user.getPostalAddresses();
      // retrieve the postal address
      PostalAddress retPa = null;
      Iterator iter = retPas.iterator();
      while (iter.hasNext()) {
        retPa = (PostalAddress) iter.next();
      }
      // verify the address

      // Collection retSlots = retPa.getSlot();
      // Test is being developed -- not complete yet ho ho ho

      debug.add("User.getPostalAddress returned the following: \n");
      debug.add("streetNumber: " + retPa.getStreetNumber() + "\n");
      debug.add("street: " + retPa.getStreet() + "\n");
      debug.add("city: " + retPa.getCity() + "\n");
      debug.add("stateOrProvince: " + retPa.getStateOrProvince() + "\n");
      debug.add("country: " + retPa.getCountry() + "\n");
      debug.add("postalCode: " + retPa.getPostalCode() + "\n");
      debug.add("type: " + retPa.getType() + "\n");
      if (!(retPa.getStreetNumber().equals(streetNumber))) {
        debug.add("Error: getStreetNumber returned: " + retPa.getStreetNumber()
            + "\n");
        failcount = failcount + 1;
      }
      if (!(retPa.getStreet().equals(street))) {
        debug.add("Error: getStreet returned: " + retPa.getStreet() + "\n");
        failcount = failcount + 1;
      }
      if (!(retPa.getCity().equals(city))) {
        debug.add("Error: getCity returned: " + retPa.getCity() + "\n");
        failcount = failcount + 1;
      }
      if (!(retPa.getStateOrProvince().equals(stateOrProvince))) {
        debug.add("Error: getStateOrProvince returned: "
            + retPa.getStateOrProvince() + "\n");
        failcount = failcount + 1;
      }
      if (!(retPa.getCountry().equals(country))) {
        debug.add("Error: getCountry returned: " + retPa.getCountry() + "\n");
        failcount = failcount + 1;
      }
      if (!(retPa.getPostalCode().equals(postalCode))) {
        debug.add(
            "Error: getPostalCode returned: " + retPa.getPostalCode() + "\n");
        failcount = failcount + 1;
      }
      if (!(retPa.getType().equals(type))) {
        debug.add("Error: getType returned: " + retPa.getType() + "\n");
        failcount = failcount + 1;
      }
      if (failcount == 0)
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

  /*
   * @testName: user_Test
   *
   *
   * @assertion_ids: JAXR:SPEC:106;
   *
   * @assertion: User instances are RegistryObjects that are used to provide
   * information about registered users within the registry
   *
   *
   * @test_Strategy: create default organization and add a fully populated user
   * object. Publish the organization to the registry Query the registy and
   * verify the user object.
   *
   *
   *
   */
  public void user_Test() throws Fault {

    String testName = "user_Test";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    Collection email = null;
    // person name
    String person = "Minnie Mouse";
    // use type
    String myUseType = "Use Type: Technical Contact";
    // telephone number
    String telNum = "781-442-1234";
    // Postal Address
    String streetNumber = "1";
    String street = "Amsden";
    String city = "Arlington";
    String stateOrProvince = "MA";
    String country = "US";
    String postalCode = "02174";
    String type = "homebase";
    Key orgKey = null;
    try {
      // create a User
      User user = blm.createUser();
      // add emailaddresses
      Vector emailAddresses = new Vector();
      emailAddresses.add("Barney@Bedrock.org");
      emailAddresses.add("Betty@Bedrock.org");

      Collection emails = new ArrayList();
      // create an email address
      EmailAddress e = null;
      for (int i = 0; i < emailAddresses.size(); i++) {
        e = blm.createEmailAddress((String) emailAddresses.get(i));
        emails.add(e);
        debug.add("Adding this email address to the list: "
            + emailAddresses.get(i) + "\n");
      }
      // give the user the email address
      user.setEmailAddresses(emails);

      // add person
      PersonName myPerson = blm.createPersonName(person);
      debug.add("Set Name of contact person on User object \n");
      user.setPersonName(myPerson);

      // add Use Type
      debug.add("\nSet the type for this User to " + myUseType + "\n");
      user.setType(myUseType);

      // add Telephone number
      debug.add("\nSet the telephone number for this User to " + telNum + "\n");
      TelephoneNumber number = blm.createTelephoneNumber();
      number.setNumber(telNum);
      Collection numbers = new ArrayList();
      numbers.add(number);
      user.setTelephoneNumbers(numbers);

      // -
      // PostalAddress
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      debug.add("\nSetting Postal Address as follows:\n");
      debug.add("streetNumber is " + streetNumber + "\n");
      debug.add("street" + street + "\n");
      debug.add("city is " + city + "\n");
      debug.add("stateOrProvince is " + stateOrProvince + "\n");
      debug.add("country is " + country + "\n");
      debug.add("postalCode is " + postalCode + "\n");
      debug.add("type is " + type + "\n");
      Collection pas = new ArrayList();
      pas.add(pa);
      user.setPostalAddresses(pas);

      // create the default organization
      Organization org = JAXR_Util.createDefaultOrganization(blm);
      debug.add("Add this User to the default organization\n");
      Collection users = new ArrayList();
      users.add(user);
      org.addUsers(users);
      debug.add("publish the organization to the registry \n");

      // --
      // Publish the organization
      orgKey = saveMyOrganization(org);
      // Find the published organization
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Organization pubOrg = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);
      if (pubOrg == null) {
        debug.add("Error - org not found\n");
        throw new Fault(testName
            + " Error from saveOrganizations - test did not complete!");
      }

      // find my User in the organization...

      debug.add("Find my User in the Users from this organization \n");
      users.clear();
      users = pubOrg.getUsers();
      Iterator itr = users.iterator();
      boolean found = false;
      while (itr.hasNext()) {
        user = (User) itr.next();
        debug.add("Searching for the correct User\n");
        myPerson = user.getPersonName();
        debug.add("Found " + myPerson.getFullName() + " \n");
        if (myPerson.getFullName().equals(person)) {
          debug.add("Found the User, got the right organization !! \n");
          found = true;
          break;
        }
      }
      if (!(found))
        throw new Fault(testName
            + " Error: did not find correct User from retrieved organization ");

      // get emailAddresses from User
      Collection retEmails = user.getEmailAddresses();
      EmailAddress ea = null;
      Iterator iter = retEmails.iterator();
      while (iter.hasNext()) {
        // get each email address in the collection and verify it is as set
        ea = (EmailAddress) iter.next();
        debug.add("Got Back this address: " + ea.getAddress() + " \n");
        if (!(emailAddresses.contains(ea.getAddress()))) {
          debug.add(
              "Error: It does not match any of the addresses that were set\n");
          throw new Fault(testName + " getEmailAddresses failed ");
        }
      }

      // get PersonName from User
      PersonName retPersonName = user.getPersonName();
      String thePerson = retPersonName.getFullName();
      debug.add(
          "The person returned from getPersonName was: " + thePerson + "\n");
      if (!(thePerson.equals(person))) {
        debug.add("Error: unexpected person returned \n");
        throw new Fault(testName + " getPersonName failed ");
      }

      // get the Organization
      Organization testOrg = user.getOrganization();
      if (!(testOrg.getName().getValue(tsLocale).equals(orgName)))
        throw new Fault(testName + " getOrganization failed ");

      // get the user type
      String retType = user.getType();
      debug.add("getType returned: " + retType + "\n");
      if (!(retType.equals(myUseType)))
        throw new Fault(testName + " getType");

      // get telephone number
      numbers = user.getTelephoneNumbers(null);
      // get the number from the collection
      iter = numbers.iterator();
      TelephoneNumber retNum = null;
      while (iter.hasNext()) {
        retNum = (TelephoneNumber) iter.next();
        debug.add("Got this telephone number:  " + retNum.getNumber() + "\n");
        if (!(retNum.getNumber().equals(telNum)))
          throw new Fault(testName + " getTelephoneNumbers failed ");
      }

      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      debug.add("cleanup registry - remove test organization\n");
      Collection keys = new ArrayList();
      keys.add(orgKey);
      cleanUpRegistry(keys, LifeCycleManager.ORGANIZATION);
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

} // end of class
