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

package com.sun.ts.tests.jaxr.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

import java.security.cert.*;
import java.security.PrivateKey;
import javax.security.auth.*;
import java.security.KeyStore;

public final class JAXR_Util {
  static Locale tsLocale = new Locale("en", "US");

  public javax.xml.registry.Connection conn = null;

  public javax.xml.registry.ConnectionFactory factory = null;

  public String supr = "JAXRCommonClient";

  protected static final String JAXRPASSWORD = "jaxrPassword";

  protected static final String JAXRUSER = "jaxrUser";

  protected String jaxrPassword;

  protected String jaxrUser;

  protected RegistryService rs = null;

  protected BusinessLifeCycleManager blm = null;

  public static final String TS_DEFAULT_PERSON_NAME_FULLNAME = "TS Test Person";

  public static final String TS_DEFAULT_ORGANIZATION_NAME = "TS Default Organization";

  public static final String TS_DEFAULT_ORGANIZATION_DESCRIPTION = "TS default Organization Data";

  public static final String TS_DEFAULT_SERVICE_NAME = "TS default Service";

  protected static final int USE_USERNAME_PASSWORD = 0;

  protected static final int USE_DIGITAL_CERTIFICATES = 1;

  // ================================================

  public static Organization createDefaultOrganization(
      BusinessLifeCycleManager blm) {
    Organization theOrg = null;
    Service sv = null;
    String name = TS_DEFAULT_ORGANIZATION_NAME;
    String descr = TS_DEFAULT_ORGANIZATION_DESCRIPTION;
    User user = null;

    try {

      Collection users = null;
      users = new ArrayList();

      theOrg = (Organization) blm.createObject(blm.ORGANIZATION);
      theOrg.setName(blm.createInternationalString(tsLocale, name));
      theOrg.setDescription(blm.createInternationalString(tsLocale, descr));

      // create and add a Service object to Organization
      sv = createDefaultService(blm);
      theOrg.addService(sv);

      user = createDefaultUser(blm);
      users.add(user);

      user = createDefaultUsers(blm, "Fred", "Flintstone", "Fred Flintstone");
      users.add(user);

      user = createDefaultUsers(blm, "Wilma", "Flintstone", "Wilma Flintstone");
      users.add(user);

      user = createDefaultUsers(blm, "Betty", "Rubble", "Betty Rubble");
      users.add(user);
      theOrg.addUsers(users);
      theOrg.setPrimaryContact(user);
      TelephoneNumber pNumber = createDefaultTelephoneNumber(blm, "123-1234",
          "cell");

      Collection pNumbers = null;
      pNumbers = new ArrayList();
      pNumbers.add(pNumber);
      theOrg.setTelephoneNumbers(pNumbers);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

    return theOrg;

  }// end of setOrganization

  public static Service createDefaultService(BusinessLifeCycleManager blm) {
    Service sv = null;
    String name = "TS Default Service";
    String descr = "TS default Service Data";
    User user = null;

    try {

      sv = (Service) blm.createService("defaultTSService");
      sv.setName(blm.createInternationalString(tsLocale, name));
      sv.setDescription(blm.createInternationalString(tsLocale, descr));
      // add a service binding to the Service
      // ServiceBinding sb = createDefaultServiceBinding(blm);
      // sv.addServiceBinding(sb);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return sv;
  }

  public static ServiceBinding createDefaultServiceBinding(
      BusinessLifeCycleManager blm) {
    ServiceBinding sb = null;
    // String name = "TS Default ServiceBinding";
    String descr = "TS default ServiceBinding Data";

    try {
      sb = (ServiceBinding) blm.createServiceBinding(); // sb =
                                                        // blm.createServiceBinding();
      // sb.setName(blm.createInternationalString(name));
      sb.setDescription(blm.createInternationalString(tsLocale, descr));
      // need to get a specification link
      SpecificationLink sl = createDefaultSpecificationLink(blm);
      sb.addSpecificationLink(sl);
      sb.setAccessURI("http://www.something.com");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

    return sb;
  }

  public static SpecificationLink createDefaultSpecificationLink(
      BusinessLifeCycleManager blm) {
    SpecificationLink sl = null;
    String usageDescription = "TS Default SpecificationLink Usage Description: test link ";
    String name = "TS Default SpecificationLink";
    String descr = "TS default SpecificationLink Data";

    try {
      sl = (SpecificationLink) blm.createObject("SpecificationLink"); // sl =
                                                                      // blm.createSpecificationLink();
      InternationalString iusageDescription = blm
          .createInternationalString(tsLocale, usageDescription);
      sl.setUsageDescription(iusageDescription);
      sl.setName(blm.createInternationalString(tsLocale, name));
      sl.setDescription(blm.createInternationalString(tsLocale, descr));

      RegistryObject ro = (RegistryObject) createDefaultConcept(blm);
      sl.setSpecificationObject(ro);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

    return sl;
  }

  public static Concept createDefaultConcept(BusinessLifeCycleManager blm) {
    String name = "TS Default Concept";
    String descr = "TS default Concept Data";
    Concept c = null;

    try {

      c = (Concept) blm.createObject("Concept");
      c.setName(blm.createInternationalString(tsLocale, name));
      c.setDescription(blm.createInternationalString(tsLocale, descr));
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return c;

  }

  public static User createDefaultUser(BusinessLifeCycleManager blm) {
    String name = "TS Default PrimaryContact User";
    String descr = "TS default User Primary Contact Data";
    User user = null;

    try {
      user = (User) blm.createObject("User"); // user = blm.createUser();
      // user.setName(blm.createInternationalString(name));
      TelephoneNumber tn = null;
      String phoneNumber = "123-1234";

      user.setDescription(blm.createInternationalString(tsLocale, descr));
      PersonName pn = createDefaultPersonName(blm);
      user.setPersonName(pn);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

    return user;

    // set a phone number for this user.........................
  }

  public static User createDefaultUsers(BusinessLifeCycleManager blm,
      String name, String descr, String fullName) {
    User user = null;
    PersonName pn = null;
    TelephoneNumber tn = null;
    String phoneNumber = "123-1234";
    Collection addresses = null;
    try {
      addresses = new ArrayList();
      user = (User) blm.createObject(blm.USER); // user = blm.createUser();
      // user.setName(blm.createInternationalString(name));
      user.setDescription(blm.createInternationalString(tsLocale, descr));

      pn = createDefaultPersonName(blm); // chk
      pn.setFullName(fullName);
      user.setPersonName(pn);
      String emailaddress = name + "@Bedrock.org";
      String emailType = "whatever";
      EmailAddress ea = createEmailAddress(blm, emailaddress, emailType);
      addresses.add(ea);
      user.setEmailAddresses(addresses);

      // set this users telephone number ............
      tn = createDefaultTelephoneNumber(blm, phoneNumber, null);
      Collection nums = new ArrayList();
      nums.add(tn);
      user.setTelephoneNumbers(nums);

      user.setType("defaultUser");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

    return user;

  }

  public static EmailAddress createEmailAddress(BusinessLifeCycleManager blm,
      String address, String emailType) {
    EmailAddress ea = null;
    try {
      ea = (EmailAddress) blm.createObject(blm.EMAIL_ADDRESS);
      ea.setAddress(address);
      ea.setType(emailType);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return ea;

  }

  public static TelephoneNumber createDefaultTelephoneNumber(
      BusinessLifeCycleManager blm, String phoneNumber, String phoneType) {
    TelephoneNumber tn = null;
    try {
      tn = (TelephoneNumber) blm.createObject(blm.TELEPHONE_NUMBER); // Gtn =
                                                                     // blm.createTelephoneNumber();
      tn.setNumber(phoneNumber);
      tn.setType(phoneType);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

    return tn;

  }

  public static PersonName createDefaultPersonName(
      BusinessLifeCycleManager blm) {
    String fullName = TS_DEFAULT_PERSON_NAME_FULLNAME;
    PersonName pn = null;
    try {
      pn = (PersonName) blm.createObject("PersonName"); // pn =
                                                        // blm.createPersonName(fullName);
      pn.setFullName(fullName);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return pn;
  }

  public static InternationalString getOrgName(Organization org) {
    InternationalString o = null;
    try {
      o = org.getName();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return o;
  }

  // get the data from an Organization object
  public static Collection getOrganizationData(Organization theOrg) {
    Collection o = new ArrayList();
    try {
      o.add(theOrg.getName());
      o.add(theOrg.getDescription());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }

    return o;

  }// end of setOrganization

  // Check bulk response.....
  // Return true if no exceptions found
  public static boolean checkBulkResponse(String whoami, BulkResponse br,
      Collection debug) {
    boolean pass = false;

    debug.add(
        "\n================ Checking BulkResponse =====================\n");

    try {
      // Check on Error....
      debug.add("=====  for " + whoami + "\n");
      if (br.getExceptions() == null) {
        // no error get the keys
        pass = true; // no errors returned!!!
        debug.add("== Got back bulkresponse with no Exceptions!\n");
      } else {
        debug.add("== Exception returned from BulkResponse!!\n");
        Collection ex = br.getExceptions();
        Iterator iter = ex.iterator();
        //
        while (iter.hasNext()) {
          JAXRException je = (JAXRException) iter.next();
          debug.add("== Detail Message for the JAXRException object: "
              + je.getMessage() + "\n");
        }
      }
      debug.add("== Is response available returns: " + br.isAvailable() + "\n");
      debug.add(
          "== IsPartialResponse returns: " + br.isPartialResponse() + "\n");
      debug.add("== The JAXR Response Status: "
          + checkBulkResponseStatus(br.getStatus()) + "\n");
      debug.add(
          "== The Unique Request Id for the request that generated this response is: "
              + br.getRequestId() + "\n");

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return pass;
  } // end of checkBulkResponse

  static String checkBulkResponseStatus(int status) {
    String response;
    switch (status) {
    case JAXRResponse.STATUS_SUCCESS: {
      response = "STATUS_SUCCESS ";
    }
      break;

    case JAXRResponse.STATUS_FAILURE: {
      response = "STATUS_FAILURE ";
    }
      break;

    case JAXRResponse.STATUS_UNAVAILABLE: {
      response = "STATUS_UNAVAILABLE ";
    }
      break;

    case JAXRResponse.STATUS_WARNING: {
      response = "STATUS_WARNING  ";
    }
      break;

    default: {
      response = "Unknown Status! ";

    }
    } // end of switch
    return response;
  }// end of checkBulkResponseStatus

  public static void deleteConcept(Collection conceptKeys,
      BusinessLifeCycleManager blm, Collection debug) {
    try {

      // == Got the key, now we can delete it....
      BulkResponse br = blm.deleteConcepts(conceptKeys);
      if (!(checkBulkResponse("deleteConcepts", br, debug))) {
        debug.add(
            "Error reported in deleteConcept method:  deleteConcepts failed \n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }

  // Returns a collection of keys

  public static Collection findConceptsbyName(String conceptName,
      BusinessQueryManager bqm, Collection debug) {

    javax.xml.registry.infomodel.Key key = null;
    Collection conceptKeys = null;
    try {
      // find the concept by name
      Collection names = new ArrayList();
      conceptKeys = new ArrayList();
      names.add(conceptName);
      BulkResponse br = bqm.findConcepts(null, names, null, null, null);
      if (!(JAXR_Util.checkBulkResponse("findConcepts", br, debug))) {
        debug.add("Error reported:  findConcepts failed \n");
        return null;
      }

      // == Check if there is something found
      Collection concepts = br.getCollection(); // get a collection of concepts
      if (concepts.size() == 0) {
        debug.add(
            "WARNING:   findConceptbyName method found nothing in the registry\n");
        return null;
      }

      // == Get the key for the concept
      Iterator iter = concepts.iterator();
      Concept concept;
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        key = concept.getKey();
        conceptKeys.add(key);
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return conceptKeys;

  } // end of findConcepts

  // returns a concept key
  public static javax.xml.registry.infomodel.Key findConceptbyName(
      String conceptName, BusinessQueryManager bqm, Collection debug) {

    javax.xml.registry.infomodel.Key key = null;
    try {
      // find the concept by name
      Collection names = new ArrayList();
      names.add(conceptName);
      BulkResponse br = bqm.findConcepts(null, names, null, null, null);
      if (!(JAXR_Util.checkBulkResponse("findConcepts", br, debug))) {
        debug.add("Error reported:  findConcepts failed \n");
        return null;
      }

      // == Check if there is something found
      Collection concepts = br.getCollection(); // get a collection of concepts
      if (concepts.size() == 0) {
        debug.add(
            "WARNING:   findConceptbyName method found nothing in the registry\n");
        return null;
      }

      if (concepts.size() > 0) {
        debug.add(
            "WARNING:   findConceptbyName method found more than one concept\n");
        return null;
      }

      // == Get the key for the concept
      Iterator iter = concepts.iterator();
      Concept concept;
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        key = concept.getKey();
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return key;

  } // end of findConcept

  public static Organization getMyOrgByName(RegistryService rs,
      String orgName) {
    BusinessQueryManager bqm = null;
    Organization o = null;
    Collection debug = null;
    try {
      debug = new ArrayList();

      bqm = rs.getBusinessQueryManager();

      Collection names = new ArrayList();
      names.add(orgName);
      BulkResponse br = bqm.findOrganizations(null, names, null, null, null,
          null);
      // Check bulk response
      if (!(JAXR_Util.checkBulkResponse("findOrganizations", br, debug))) {
        debug.add("Error:    findOrganizations failed \n");
        return null;
      }

      Collection myOrgs = br.getCollection();
      if (myOrgs.size() != 1) {
        debug.add("Expected 1 organization, but got: " + myOrgs.size() + "\n");
        return null;
      }
      // iterate the collection - only one
      Iterator iter = myOrgs.iterator();
      o = null;
      while (iter.hasNext()) {
        o = (Organization) iter.next();
      }
      if (o != null) {
        if (o.getName().equals(orgName)) {
          debug.add("======================================\n");
          debug.add("Success: Found published Organization \n");
          debug.add("======================================\n");
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      System.out.print(debug.toString());
    }
    return o;
  } // end of getMyOrgByName()

  public static Set doCredentials(int jaxrSecurityCredentialType,
      Properties creds, String whichUser) {
    Set credentials = null;
    try {
      switch (jaxrSecurityCredentialType) {
      case USE_USERNAME_PASSWORD:
        // ==
        if (whichUser.equals("user1")) {
          credentials = getUsernamePasswordCredentials(
              creds.getProperty("jaxrUser"), creds.getProperty("jaxrPassword"));
          TestUtil.logTrace(
              "Setting credentials for " + creds.getProperty("jaxrUser"));
        } else {
          credentials = getUsernamePasswordCredentials(
              creds.getProperty("jaxrUser2"),
              creds.getProperty("jaxrPassword2"));
          TestUtil.logTrace(
              "Setting credentials for " + creds.getProperty("jaxrUser2"));
        }
        break;

      case USE_DIGITAL_CERTIFICATES:
        // ==
        if (whichUser.equals("user1")) {
          credentials = getDigitalCertificateCredentials(
              creds.getProperty("jaxrAlias"),
              creds.getProperty("jaxrAliasPassword"));
          TestUtil.logTrace(
              "Setting credentials for " + creds.getProperty("jaxrAlias"));
        } else {
          credentials = getDigitalCertificateCredentials(
              creds.getProperty("jaxrAlias2"),
              creds.getProperty("jaxrAlias2Password"));
          TestUtil.logTrace(
              "Setting credentials for " + creds.getProperty("jaxrAlias2"));
        }
        break;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
    return credentials;
  } // end of doCredentials()

  /**
   * This method getUsernamePasswordCredentials is used to get the Credentials
   * for digital certificate and private key
   *
   * @param useralias
   *          - each entry in a keystore is identified by the alias string
   */
  private static Set getUsernamePasswordCredentials(String jaxrUser,
      String jaxrPassword) {
    PasswordAuthentication passwdAuth = null;
    Set credentials = new HashSet();
    passwdAuth = new PasswordAuthentication(jaxrUser,
        jaxrPassword.toCharArray());
    credentials.add(passwdAuth);
    return credentials;
  }

  /**
   * This method getDigitalCertificateCredentials is used to get the Credentials
   * for digital certificate and private key
   *
   * @param useralias
   *          - each entry in a keystore is identified by the alias string
   */
  private static Set getDigitalCertificateCredentials(String useralias,
      String aliasPassword) {
    String keystoreType = "JKS"; // type of keystore implementation provided by
                                 // SUN.
    Set credentials = new HashSet();

    try {

      // Get the keystore file - this is passed in on the command line - see
      // ts.jte
      String keystoreFile = System.getProperty("javax.net.ssl.keyStore");

      // get keystore password
      String keyPass = System.getProperty("javax.net.ssl.keyStorePassword");
      char[] passphrase = null;
      passphrase = keyPass.toCharArray();

      // get keystore instance
      KeyStore ks = KeyStore.getInstance(keystoreType);

      // load keystore
      ks.load(new FileInputStream(keystoreFile), passphrase);

      // useralias tells me which certificate to get from the keystore
      Certificate certificate = ks.getCertificate(useralias);

      // I need to get the private key.
      // ?? does the following return the private key???
      java.security.PrivateKey key = (java.security.PrivateKey) ks
          .getKey(useralias, aliasPassword.toCharArray());
      // java.security.Key key = ks.getKey(useralias, passphrase);

      // ok now I need an X500PrivateCredential
      X509Certificate cert = (X509Certificate) certificate;

      javax.security.auth.x500.X500PrivateCredential privateCredential = null;

      privateCredential = new javax.security.auth.x500.X500PrivateCredential(
          cert, key, useralias);
      credentials.add(privateCredential);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return credentials;
  }

} // end of class JAXR_Util
