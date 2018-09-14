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

package com.sun.ts.tests.jaxr.api.javax_xml_registry.BusinessLifeCycleManager;

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
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {

  // ================================================

  String[] className = { LifeCycleManager.ASSOCIATION,
      LifeCycleManager.AUDITABLE_EVENT, LifeCycleManager.CLASSIFICATION,
      LifeCycleManager.CLASSIFICATION_SCHEME, LifeCycleManager.CONCEPT,
      LifeCycleManager.EMAIL_ADDRESS, LifeCycleManager.EXTERNAL_IDENTIFIER,
      LifeCycleManager.EXTERNAL_LINK, LifeCycleManager.EXTRINSIC_OBJECT,
      LifeCycleManager.INTERNATIONAL_STRING, LifeCycleManager.KEY,
      LifeCycleManager.LOCALIZED_STRING, LifeCycleManager.ORGANIZATION,
      LifeCycleManager.PERSON_NAME, LifeCycleManager.POSTAL_ADDRESS,
      // LifeCycleManager.REGISTRY_ENTRY,
      LifeCycleManager.REGISTRY_PACKAGE, LifeCycleManager.SERVICE,
      LifeCycleManager.SERVICE_BINDING, LifeCycleManager.SLOT,
      LifeCycleManager.SPECIFICATION_LINK, LifeCycleManager.TELEPHONE_NUMBER,
      LifeCycleManager.USER
      // LifeCycleManager.VERSIONABLE
  };

  /*
   * int [] classLevel = { 0,1,0,0,0,0,0,0,1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1 };
   */
  int[] classLevel = { 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0,
      0, 0, 0 };

  com.sun.ts.tests.jaxr.common.JAXR_Util util = null;

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

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      if (debug != null) {
        TestUtil.logTrace(debug.toString());
        debug.clear();
      }
      super.cleanUpRegistry(); // clean "TS Default Organization"
      super.cleanup(); // close connection
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    }
  }

  // helper method
  boolean verifyObject(Object o, int index) {
    boolean pass = true;
    index = index + 1;

    switch (index) {
    case 1:
      if (!(o instanceof Association)) {
        pass = false;
      }
      break;
    case 2:
      if (!(o instanceof AuditableEvent)) {
        pass = false;
      }
      break;
    case 3:
      if (!(o instanceof Classification)) {
        pass = false;
      }
      break;
    case 4:
      if (!(o instanceof ClassificationScheme)) {
        pass = false;
      }
      break;
    case 5:
      if (!(o instanceof Concept)) {
        pass = false;
      }
      break;
    case 6:
      if (!(o instanceof EmailAddress)) {
        pass = false;
      }
      break;
    /*
     * case 7: if (!(o instanceof ExtensibleObject)) { pass = false; } break;
     */
    case 7:
      if (!(o instanceof ExternalIdentifier)) {
        pass = false;
      }
      break;
    case 8:
      if (!(o instanceof ExternalLink)) {
        pass = false;
      }
      break;
    case 9:
      if (!(o instanceof ExtrinsicObject)) {
        pass = false;
      }
      break;
    case 10:
      if (!(o instanceof InternationalString)) {
        pass = false;
      }
      break;
    case 11:
      if (!(o instanceof Key)) {
        pass = false;
      }
      break;
    case 12:
      if (!(o instanceof LocalizedString)) {
        pass = false;
      }
      break;
    case 13:
      if (!(o instanceof Organization)) {
        pass = false;
      }
      break;
    case 14:
      if (!(o instanceof PersonName)) {
        pass = false;
      }
      break;
    case 15:
      if (!(o instanceof PostalAddress)) {
        pass = false;
      }
      break;
    /*
     * case 16: if (!(o instanceof RegistryEntry) ) { pass = false; } break;
     */
    case 16:
      if (!(o instanceof RegistryPackage)) {
        pass = false;
      }
      break;
    case 17:
      if (!(o instanceof Service)) {
        pass = false;
      }
      break;
    case 18:
      if (!(o instanceof ServiceBinding)) {
        pass = false;
      }
      break;
    case 19:
      if (!(o instanceof Slot)) {
        pass = false;
      }
      break;
    case 20:
      if (!(o instanceof SpecificationLink)) {
        pass = false;
      }
      break;
    case 21:
      if (!(o instanceof TelephoneNumber)) {
        pass = false;
      }
      break;
    case 22:
      if (!(o instanceof User)) {
        pass = false;
      }
      break;
    /*
     * case 23: if (!(o instanceof Versionable) ) { pass = false; } break;
     */

    default: {
      TestUtil
          .logMsg("Error: createObjectTest did not find infomodel object\n");
      pass = false;
    }
    } // end of switch
    return pass;
  } // end of verifyObject

  /*
   * @testName: BusinessLifeCycleManagerInvalid_createObjectTest
   *
   * @assertion_ids: JAXR:JAVADOC:117;
   *
   * @test_Strategy: Pass createObject and invalid class. Verify that an
   * InvalidRequestException is thrown
   *
   */
  public void BusinessLifeCycleManagerInvalid_createObjectTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManagerInvalid_createObjectTest";
    boolean pass = true;
    try {
      RegistryService rs = conn.getRegistryService();
      if (!(rs instanceof RegistryService)) {
        pass = false;
        throw new Fault(
            testName + "Error failed to get a RegistryService object");
      }
      BusinessLifeCycleManager blcm = rs.getBusinessLifeCycleManager();
      if (!(blcm instanceof BusinessLifeCycleManager)) {
        pass = false;
        throw new Fault(
            testName + "Error failed to get a BusinessLifeCycleManager object");
      }
      // if createObject is passed a class that is not from
      // javax.xml.registry.infomodel
      // - should throw InvalidRequestException
      Object o = blcm.createObject("String");
      pass = false; // its an error to get here
      TestUtil.logTrace("Error: InvalidRequestException was not thrown!");
    } catch (InvalidRequestException ir) {
      TestUtil.printStackTrace(ir);
      TestUtil.logTrace("Caught InvalidRequestException exception: "
          + ir.getMessage() + " as expected");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.logTrace("Was expecting an InvalidRequestException: ");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  }

  /*
   * @testName: BusinessLifeCycleManagerInvalid2_createObjectTest
   *
   * @assertion_ids: JAXR:JAVADOC:118;
   *
   * @test_Strategy: For each class check capability level against the providers
   * capability level. A level 0 provider should throw an
   * UnsupportedCapabilityException error when an attempt is made to instantiate
   * a level 1 class.
   */
  public void BusinessLifeCycleManagerInvalid2_createObjectTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManagerInvalid2_createObjectTest";
    boolean pass = true;
    Collection debug = null;
    int providerlevel;
    int i;
    int failcount = 0;
    try {
      RegistryService rs = conn.getRegistryService();
      debug = new ArrayList();
      if (!(rs instanceof RegistryService)) {
        throw new Fault(
            testName + "Error failed to get a RegistryService object");
      }

      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add("     The capability level for this JAXR provider is: "
          + providerlevel + "\n");

      BusinessLifeCycleManager blcm = rs.getBusinessLifeCycleManager();
      if (!(blcm instanceof BusinessLifeCycleManager)) {
        throw new Fault(
            testName + "Error failed to get a BusinessLifeCycleManager object");
      }
      Object o = null;
      //
      for (i = 0; i < className.length; i++) {
        if (classLevel[i] > providerlevel) {
          debug.add("\n=== Attempt to create: " + className[i] + "\n");
          debug.add(className[i] + " exceeds provider capability level\n");

          try {
            o = blcm.createObject(className[i]);
            debug.add("Error: create did not throw an error as expected!\n");
            pass = false;
            failcount = failcount + 1;
          } catch (UnsupportedCapabilityException uce) {
            TestUtil.printStackTrace(uce);
            debug.add("Pass: UnsupportedCapabilityException was thrown!!\n");
          }
        }

      }
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    } finally {
      debug.add("Total Failures for this test:  " + failcount + "\n");
    }

    if (!pass)
      throw new Fault(testName + "failed ");
  }
  // end of test class

  /*
   * @testName: BusinessLifeCycleManager_createObjectTest
   *
   * @assertion_ids:
   * JAXR:JAVADOC:90;JAXR:JAVADOC:91;JAXR:JAVADOC:92;JAXR:JAVADOC:93;JAXR:
   * JAVADOC:94;JAXR:JAVADOC:95;
   * JAXR:JAVADOC:97;JAXR:JAVADOC:98;JAXR:JAVADOC:99;
   * JAXR:JAVADOC:100;JAXR:JAVADOC:101;JAXR:JAVADOC:102;JAXR:JAVADOC:103;
   * JAXR:JAVADOC:104;JAXR:JAVADOC:105;JAXR:JAVADOC:106;JAXR:JAVADOC:107;
   * JAXR:JAVADOC:108;JAXR:JAVADOC:109;JAXR:JAVADOC:110;JAXR:JAVADOC:111;
   * JAXR:JAVADOC:112;JAXR:JAVADOC:113;JAXR:JAVADOC:114;JAXR:JAVADOC:115;
   * JAXR:SPEC:190;
   * 
   * @test_Strategy: Use createObject method. Create each object from the
   * infomodel Verify the the object returned is valid.
   *
   * Also, check capability level. A level 0 provider should throw an
   * UnsupportedCapabilityException error when an attempt is made to instantiate
   * a level 1 class.
   */
  public void BusinessLifeCycleManager_createObjectTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManager_createObjectTest";
    boolean pass = true;
    Collection debug = null;
    int providerlevel;
    int i;
    int failcount = 0;

    try {
      RegistryService rs = conn.getRegistryService();
      debug = new ArrayList();
      if (!(rs instanceof RegistryService)) {
        throw new Fault(
            testName + "Error failed to get a RegistryService object");
      }

      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add("     The capability level for this JAXR provider is: "
          + providerlevel + "\n");
      BusinessLifeCycleManager blcm = rs.getBusinessLifeCycleManager();
      if (!(blcm instanceof BusinessLifeCycleManager)) {
        throw new Fault(
            testName + "Error failed to get a BusinessLifeCycleManager object");
      }
      Object o = null;
      //
      for (i = 0; i < className.length; i++) {
        if (classLevel[i] > providerlevel) {
          debug.add("\n============================\n");
          debug.add(className[i] + " exceeds provider capability level\n");
          continue;
        }

        try {
          debug.add("\n=== Creating: " + className[i] + "\n");
          o = blcm.createObject(className[i]);
          // We need to make sure that createObject is valid for this class
          // capability level ;
          // * ========================
          debug.add("providerlevel is: " + providerlevel + " |  classLevel is: "
              + classLevel[i] + "\n");

        } catch (Exception ue) {
          debug.add("Error: caught an unexpected exception\n");
          debug.add(ue.getMessage() + "\n");
          TestUtil.printStackTrace(ue);
          failcount = failcount + 1;
          debug.add("Fail!: \n");
          continue;
        }
        if (o != null) {
          debug.add("=============>  createObject returned: "
              + o.getClass().getName() + "\n");

        }
        //
        // Verify that the created object is what was requested.
        //
        debug.add(" o.toString() is:  " + o.toString() + "\n");
        pass = verifyObject(o, i);
        if (pass)
          debug.add("Pass: \n");
        else {
          failcount = failcount + 1;
          debug.add("Fail!: \n");
        }

      }
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    } finally {
      debug.add("Total Failures for this test:  " + failcount + "\n");
    }
    if (failcount > 1) {
      if (debug != null) {
        TestUtil.logTrace(debug.toString());
        debug.clear();
      }
      throw new Fault(testName + "failed ");
    }

  }
  // end of test class

  /*
   * @testName: BusinessLifeCycleManager_SaveConceptsTest1
   *
   * @assertion_ids: JAXR:JAVADOC:281;
   * 
   * @assertion: saveConcepts -Saves specified Concepts. If the object is not in
   * the registry, then it is created in the registry. If it already exists in
   * the registry and has been modified, registry and has been modified, then
   * its state is updated (replaced) in the registry
   *
   * @test_Strategy: Create an object that is not already in the registry and
   * verify. Verify that it was published.
   *
   */
  public void BusinessLifeCycleManager_SaveConceptsTest1() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManager_SaveConceptsTest1";
    debug.add("JAXRClient.BusinessLifeCycleManager_SaveConceptsTest1\n");
    boolean pass = false;
    BusinessQueryManager bqm = null;
    Organization org = null;
    Concept concept = null;
    Collection concepts = null;
    Collection orgs = null;
    String name = "TS TEST Name:Test BusinessLifeCycleManager_SaveConceptsTest1";
    String descr = "Description: Test BusinessLifeCycleManager_SaveConceptsTest1";
    BulkResponse br = null;
    Key savedOrgKey = null;
    try {
      bqm = rs.getBusinessQueryManager();
      // Create a concept, add it to a Collection
      concept = (Concept) blm.createObject(blm.CONCEPT);
      concepts = new ArrayList();
      concept.setName(blm.createInternationalString(tsLocale, name));
      concept.setDescription(blm.createInternationalString(tsLocale, descr));
      concepts.add(concept);
      debug.add("Save concepts to registry\n");
      // Save the concepts collection to the registry and check the bulk
      // response
      br = blm.saveConcepts(concepts);

      if (!(JAXR_Util.checkBulkResponse("saveConcepts", br, debug)))
        throw new Fault(
            testName + " Error from saveServices - test did not complete!");
      debug.add(
          "Validate the concepts was actually published to the registry\n");

      // Validate the concepts was actually published to the registry.
      // --
      Collection names = new ArrayList();
      names.add(name);
      br = bqm.findConcepts(null, names, null, null, null);
      if (!(JAXR_Util.checkBulkResponse("findConcepts", br, debug))) {
        debug.add("Error reported:  findConcepts failed \n");
        throw new Fault(testName
            + " Error from getRegistryObjects- test did not complete!");
      }

      // Check what was returned from the registry
      concepts = br.getCollection();
      debug.add("Number of concepts found is: " + concepts.size() + "\n");
      if (concepts.size() == 0) {
        debug.add("Error: no concepts found in the registry\n");
        throw new Fault(testName + " due to error test did not complete");
      }

      Collection myKeys = new ArrayList();
      Iterator iter = concepts.iterator();
      concept = null;
      Collection orgKeys = new ArrayList();
      javax.xml.registry.infomodel.Key key = null;
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        if (concept.getName().getValue(tsLocale).equals(name)) {
          debug.add("Got back expected Concept Name!\n");
          pass = true;
        } else {
          debug.add("Error: returned name not as expected! \n");
          debug.add("Expected name: " + name + "\n");
          debug.add(
              "Returned name: " + concept.getName().getValue(tsLocale) + "\n");
        }
        key = concept.getKey();
        myKeys.add(key);

        JAXR_Util.deleteConcept(myKeys, blm, debug);
      } // end of while
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

  /*
   * @testName: BusinessLifeCycleManager_SaveConceptsTest2
   *
   * @assertion: saveConcepts -Saves specified Concepts. If the object is not in
   * the registry, then it is created in the registry. If it already exists in
   * the registry and has been modified, then its state is updated (replaced) in
   * the registry JavaDoc
   *
   * @assertion_ids: JAXR:JAVADOC:282;JAXR:SPEC:172;
   *
   * @test_Strategy: Create an object that is not already in the registry and
   * publish. Verify that what was just published can be replaced in the
   * registry.
   *
   */
  public void BusinessLifeCycleManager_SaveConceptsTest2() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManager_SaveConceptsTest2";
    boolean pass = true;
    BusinessQueryManager bqm = null;
    Organization org = null;
    Concept concept = null;
    Collection concepts = null;
    Collection keys = null;
    Collection names = null;
    javax.xml.registry.infomodel.Key key = null;
    javax.xml.registry.infomodel.Key conceptKey = null;
    javax.xml.registry.infomodel.Key conceptKey1 = null;

    String name = "TS TEST Name:Test BusinessLifeCycleManager_SaveConceptsTest2";
    String descr = "Description: Test BusinessLifeCycleManager_SaveConceptsTest2";
    String replacementDescr = "Description: Test Replaced BusinessLifeCycleManager_SaveConceptsTest2";

    BulkResponse br = null;

    try {
      bqm = rs.getBusinessQueryManager();

      // Create a concept object......
      concept = (Concept) blm.createObject(blm.CONCEPT);
      concepts = new ArrayList();

      // create a collection of concepts and save to the registry
      concept.setName(blm.createInternationalString(tsLocale, name));
      concept.setDescription(blm.createInternationalString(descr));
      concepts.add(concept);

      br = blm.saveConcepts(concepts);
      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("saveConcepts", br, debug)))
        throw new Fault(
            testName + " Error from saveConcepts - test did not complete!");

      keys = br.getCollection(); // get a collection of concept keys
      if (keys.size() == 0) {
        throw new Fault(
            testName + "Error: no key was returned from saveConcept\n");
      }
      if (keys.size() > 1) {
        throw new Fault(
            testName + "Error: too many keys were returned from saveConcept\n");
      }

      Iterator iter = keys.iterator();
      while (iter.hasNext()) {
        conceptKey = (Key) iter.next();
      }

      // Validate the concept was actually published to the registry.
      String keyId = conceptKey.getId();
      debug
          .add("Get it back from the registry - its keyid is: " + keyId + "\n");
      concept = (Concept) bqm.getRegistryObject(keyId,
          LifeCycleManager.CONCEPT);

      if (concept.getName().getValue(tsLocale).equals(name)) {
        debug.add("Got back expected Concept Name!\n");
      } else {
        pass = false;
        debug.add("Error: returned name not as expected! \n");
        debug.add("Expected name: " + name + "\n");
        debug.add(
            "Returned name: " + concept.getName().getValue(tsLocale) + "\n");
      }

      if (concept.getDescription().getValue().equals(descr)) {
        debug.add("Got back expected Concept Description! \n");
      } else {
        pass = false;
        debug.add("Error: returned description not as expected! \n");
        debug.add("Expected description: " + descr + "\n");
        debug.add("Returned description: " + concept.getDescription().getValue()
            + "\n");
      }

      // Create the replacement concept object......
      concepts.clear();
      concept.setDescription(blm.createInternationalString(replacementDescr));
      concepts.add(concept);

      br = blm.saveConcepts(concepts);

      // Verify the replacement
      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("saveConcepts", br, debug)))
        throw new Fault(
            testName + " Error from saveServices - test did not complete!");

      keys = br.getCollection(); // get a collection of concept keys
      if (keys.size() == 0) {
        throw new Fault(
            testName + "Error: no key was returned from saveConcept\n");
      }
      if (keys.size() > 1) {
        throw new Fault(
            testName + "Error: too many keys were returned from saveConcept\n");
      }
      iter = keys.iterator();
      while (iter.hasNext()) {
        conceptKey1 = (Key) iter.next();
      }

      // ================================================
      // Validate the concepts was actually published to the registry.
      keyId = conceptKey1.getId();
      debug
          .add("Get it back from the registry - its keyid is: " + keyId + "\n");
      concept = (Concept) bqm.getRegistryObject(keyId,
          LifeCycleManager.CONCEPT);

      if (concept.getName().getValue(tsLocale).equals(name)) {
        debug.add("Got back expected Concept Name!\n");
      } else {
        pass = false;
        debug.add("Error: returned name not as expected! \n");
        debug.add("Expected name: " + name + "\n");
        debug.add(
            "Returned name: " + concept.getName().getValue(tsLocale) + "\n");
      }

      if (concept.getDescription().getValue().equals(replacementDescr)) {
        debug.add("Got back expected Concept Description! \n");
      } else {
        pass = false;
        debug.add("Error: returned description not as expected! \n");
        debug.add("Expected description: " + replacementDescr + "\n");
        debug.add("Returned description: " + concept.getDescription().getValue()
            + "\n");
      }

      // cleanup - logically remove the concept from the registry.....
      keys.clear();
      keys.add(conceptKey);
      keys.add(conceptKey1);
      br = blm.deleteConcepts(keys);

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      debug.add("In finally loop ===== Test is OVER!!!!! \n");
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

  /*
   * @testName: BusinessLifeCycleManager_DeleteConcepts
   *
   * @assertion_ids: JAXR:JAVADOC:296
   *
   * @test_Strategy: Verify the deleteConcepts method. Create and publish a
   * concept. Delete the concept from the registry with the deleteConcepts
   * method. Verify with a findConcepts that the concept is hidden.
   *
   */
  public void BusinessLifeCycleManager_DeleteConcepts() throws Fault {
    String testName = "BusinessLifeCycleManager_DeleteConcepts";
    BusinessQueryManager bqm = null;
    Concept concept = null;
    Collection concepts = null;
    String name = "TS TEST Name:DeleteConcepts";
    String descr = "Description: Test DeleteConcepts";
    BulkResponse br = null;
    javax.xml.registry.infomodel.Key conceptKey = null;

    try {
      bqm = rs.getBusinessQueryManager();

      // Create a concept object......
      concept = (Concept) blm.createObject(blm.CONCEPT);
      concepts = new ArrayList();

      // create a collection of concepts and save to the registry
      concept.setName(blm.createInternationalString(tsLocale, name));
      concept.setDescription(blm.createInternationalString(tsLocale, descr));
      concepts.add(concept);

      br = blm.saveConcepts(concepts);

      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("saveConcepts", br, debug)))
        throw new Fault(
            testName + " Error from saveServices - test did not complete!");
      // get the collection of concept keys from saveConcepts - will need this
      // later for delete
      Collection conceptKeys = br.getCollection();
      if (conceptKeys.size() != 1)
        throw new Fault(testName + "Error: did not get back 1 concept key!\n");

      Iterator iter = conceptKeys.iterator();
      while (iter.hasNext()) {
        conceptKey = (Key) iter.next();
      }

      // Verify the concept was published to the registry
      String keyId = conceptKey.getId();
      concept = (Concept) bqm.getRegistryObject(keyId,
          LifeCycleManager.CONCEPT);

      if (concept.getName().getValue(tsLocale).equals(name)) {
        debug.add("Got back expected Concept Name!\n");
      } else
        throw new Fault(testName
            + " Error verifying concept saved - test did not complete!");

      if (concept.getDescription().getValue().equals(descr)) {
        debug.add("Got back expected Concept Description! \n");
      } else
        throw new Fault(testName
            + " Error verifying concept saved - test did not complete!");

      // now delete the concept
      br = blm.deleteConcepts(conceptKeys);
      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("deleteConcepts", br, debug)))
        throw new Fault(
            testName + " Error from deleteConcepts - test did not complete!");
      // Verify that the concept is "hidden"
      Collection names = new ArrayList();
      names.add(name);
      br = bqm.findConcepts(null, names, null, null, null);
      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("findConcepts", br, debug)))
        throw new Fault(
            testName + " Error from findConcepts - test did not complete!");
      concepts = br.getCollection();
      Concept c = null;
      iter = concepts.iterator();
      while (iter.hasNext()) {
        c = (Concept) iter.next();
        if (c.getKey().getId().equals(keyId)) {
          debug.add(
              "Error: Deleted Concept was found ( should have been hidden) \n");
          throw new Fault(testName + " failed ");
        } else
          debug.add("Did not find matching concept - as expected\n");
      }
      if (concepts.size() > 0) {
        debug.add(
            "Warning: unexpectedly found some concepts - though not the deleted one \n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of test class

  /*
   * @testName: BusinessLifeCycleManager_DeleteServicesTest
   *
   * @assertion_ids: JAXR:JAVADOC:292;
   *
   * @test_Strategy: Create and save a collection of services Verify the
   * services have been added. Then delete. Verify the delete
   *
   */
  public void BusinessLifeCycleManager_DeleteServicesTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManager_DeleteServicesTest";
    Organization org;
    Collection keys = null;
    Key key = null;
    int numOrgs = 3;
    Collection orgs = null;
    BulkResponse br = null;
    Iterator iter = null;
    BusinessQueryManager bqm = null;
    Collection services = null;
    Collection orgKeys = null;
    String myServiceName = "Name:Test Service";

    try {
      org = JAXR_Util.createDefaultOrganization(blm);
      // Collection services = org.getServices();
      bqm = rs.getBusinessQueryManager();

      orgs = new ArrayList();
      orgs.add(org);

      debug.add("Publish the default organization to the registry  - "
          + org.getName().getValue() + "\n");
      br = blm.saveOrganizations(orgs);

      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug)))
        throw new Fault(testName
            + " Error from saveOrganizations - test did not complete!");

      // BulkResponse containing the Collection of keys for those objects that
      // were saved successfully
      orgKeys = br.getCollection();
      debug.add("Expect 1 key returned from saveOrganizations br - got: "
          + orgKeys.size() + "\n");

      // BulkResponse containing a Collection of RegistryObjects - in this case
      // orgs
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("getRegistryObjects", br, debug)))
        throw new Fault(testName
            + " Error from getRegistryObjects - test did not complete!");
      debug.add("Get the organizations owned by this user \n");
      orgs.clear();

      orgs = br.getCollection();
      debug.add(
          "Call to getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION) returned count of "
              + orgs.size() + "\n");

      services = new ArrayList();
      Service theService = (Service) blm.createObject(blm.SERVICE);

      theService
          .setName(blm.createInternationalString(tsLocale, myServiceName));
      theService.setDescription(
          blm.createInternationalString("Description: Testservice"));

      debug.add("Created the service object = "
          + theService.getName().getValue(tsLocale) + "\n");
      iter = orgs.iterator();
      Organization org2 = null;
      Key orgkey = null;
      while (iter.hasNext()) {
        // iterate thru registryObject collection
        org2 = (Organization) iter.next();
        orgkey = org2.getKey();
        debug.add("got an org from the reg: "
            + org2.getName().getValue(tsLocale) + "\n");
        org2.addService(theService);
        services.add(theService);
      }
      debug.add("saving services = " + services.size() + "\n");

      br = blm.saveServices(services);
      // Check for error
      if (!(JAXR_Util.checkBulkResponse("saveServices", br, debug)))
        throw new Fault(
            testName + " Error from saveServices - test did not complete!");
      // --
      debug.add("Verify that the service was saved \n");
      Collection serviceKeys = br.getCollection();
      debug.add(
          "SaveServices returned count of " + serviceKeys.size() + "keys \n");
      Key servicekey = null;
      iter = serviceKeys.iterator();
      while (iter.hasNext()) {
        servicekey = (Key) iter.next();
      }
      String serviceKeyId = servicekey.getId();
      debug.add("Get it back from the registry - its keyid is: " + serviceKeyId
          + "\n");

      debug.add(
          "request this object from the registry - getRegistryObject(serviceKeyId, LifeCycleManager.SERVICE) \n");
      Service myServ = (Service) bqm.getRegistryObject(serviceKeyId,
          LifeCycleManager.SERVICE);
      if (myServ == null)
        throw new Fault(
            testName + " Error: getRegistryObjects returned a null service \n");

      debug.add("Service returned from getRegistryObjects is "
          + myServ.getName().getValue(tsLocale) + "\n");
      if (!(myServ.getName().getValue(tsLocale).equals(myServiceName)))
        throw new Fault(
            testName + " Error: Name of service returned did not match!! \n");
      debug.add(
          "Verified service was saved to registry - now will delete it. \n");
      debug.add("Verify serviceKeys has 1 key:  " + serviceKeys.size() + "\n");
      br = blm.deleteServices(serviceKeys);
      // Check for error
      if (!(JAXR_Util.checkBulkResponse("deleteServices", br, debug)))
        throw new Fault(
            testName + " Error from deleteServices - test did not complete!");
      Collection retKeys = br.getCollection();
      debug.add(
          "Deleted the service.  Check count of keys returned, should be 1: "
              + retKeys.size() + "\n");
      if (retKeys.size() == 0)
        throw new Fault(testName
            + " Error from deleteServices - should have returned deleted key!");
      br = blm.deleteOrganizations(orgKeys);
      if (br.getExceptions() != null)
        TestUtil.logErr("Warning:  error returned on organization cleanup ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  } // end of test class

  // =
  /*
   * @testName: BusinessLifeCycleManager_SaveServicesTest
   *
   * @assertion_ids: JAXR:JAVADOC:275;
   *
   * @assertion: saveServices - Saves specified Services. If the object is not
   * in the registry, then it is created in the registry. If it already exists
   * in the registry and has been modified, then its state is updated (replaced)
   * in the registry. Partial commits are allowed.
   *
   * @test_Strategy: Create and save a collection of services Verify the
   * services have been added. Then do a cleanup
   *
   * Capability Level: 0 This interface is required to be implemented by JAXR
   * Providers at or above capability level 0.
   *
   *
   */
  public void BusinessLifeCycleManager_SaveServicesTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManager_SaveServicesTest";
    boolean pass = false;
    Organization org;
    Collection keys = null;
    Key key = null;
    Key savedOrgKey = null; // save organization key for findServices call
    int numOrgs = 3;
    Collection orgs = null;
    BulkResponse br = null;
    Iterator iter = null;
    BusinessQueryManager bqm = null;
    Collection services = null;
    String serviceName = "Name:Test Service";
    Collection orgKeys = null;
    try {
      org = JAXR_Util.createDefaultOrganization(blm);
      // Collection services = org.getServices();
      bqm = rs.getBusinessQueryManager();

      orgs = new ArrayList();
      orgs.add(org);
      debug.add("Saving this organization: " + org.getName().getValue(tsLocale)
          + "\n");
      br = blm.saveOrganizations(orgs);
      if (!(JAXR_Util.checkBulkResponse("saveServices", br, debug)))
        throw new Fault(testName
            + " Error from saveOrganizations - test did not complete!");

      if (br.getExceptions() != null) {
        debug.add("Error from saveOrganizations \n");
      }
      // BulkResponse containing the Collection of keys for those objects that
      // were saved successfully
      orgKeys = br.getCollection();
      debug.add("OrgKey count returned from saveOrganization(should be 1) is : "
          + orgKeys.size() + "\n");
      // get the key for this organization - should be just one
      iter = orgKeys.iterator();
      while (iter.hasNext()) {
        savedOrgKey = (Key) iter.next();
      }

      // BulkResponse containing a hetrogeneous Collection of RegistryObjects -
      // in this case orgs
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      if (br.getExceptions() != null) {
        debug.add("Error from saveOrganizations \n");
      }
      Collection myOrgs = br.getCollection();
      services = new ArrayList();
      Service theService = (Service) blm.createObject(blm.SERVICE);
      // iterate thru the collection of orgs returned from getRegistryObjects -
      // we only have 1
      iter = myOrgs.iterator();
      Organization myOrg = null;
      theService.setName(blm.createInternationalString(tsLocale, serviceName));
      theService.setDescription(
          blm.createInternationalString("Description: Testservice"));
      while (iter.hasNext()) {
        myOrg = (Organization) iter.next();
        myOrg.addService(theService);
        services.add(theService);
      }

      ///// ======
      // save the new service back to the registry
      br = blm.saveServices(services);
      // Check for error

      if (!(JAXR_Util.checkBulkResponse("saveServices", br, debug)))
        throw new Fault(
            testName + " Error from saveServices - test did not complete!");

      // update the registry ...
      Collection updatedOrgs = new ArrayList();
      updatedOrgs.add(myOrg);
      br = blm.saveOrganizations(updatedOrgs);
      if (!(JAXR_Util.checkBulkResponse("saveServices", br, debug)))
        throw new Fault(testName
            + " Error from saveOrganizations - test did not complete!");

      if (br.getExceptions() != null) {
        debug.add("Error from saveOrganizations \n");
      }
      // --
      // Now we must verify that the service was saved successfully....
      // --
      Collection names = new ArrayList();
      names.add(serviceName);

      br = bqm.findServices(savedOrgKey, null, names, null, null);
      // check for error
      if (!(JAXR_Util.checkBulkResponse("findServices", br, debug)))
        throw new Fault(
            testName + " Error from findServices - test did not complete!");

      // get the collection of services returned from findServices - should be
      // one
      Service s = null;
      Collection ss = br.getCollection();
      debug.add("Count returned from findServices should be one \n");
      debug.add("Count returned : " + ss.size() + "\n");

      iter = ss.iterator();
      while (iter.hasNext()) {
        s = (Service) iter.next();
        debug.add("And the service returned is: "
            + s.getName().getValue(tsLocale) + "\n");

      }

      if (s.getName().getValue(tsLocale).equals(serviceName)) {
        pass = true;
        debug.add(testName + "Added Service successfully!!!\n");
      } else
        debug.add(testName + "Failed to add Service successfully!!\n");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

  /*
   * @testName: BusinessLifeCycleManager_DeleteOrganizationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:290;
   *
   * @assertion: deleteOrganizations - Deletes the organizations corresponding
   * to the specified Keys.
   *
   * @test_Strategy: Create and save a collection of organizations Delete the
   * organizations. Verify the delete.
   * 
   * Capability Level: 0 This interface is required to be implemented by JAXR
   * Providers at or above capability level 0.
   *
   *
   */
  public void BusinessLifeCycleManager_DeleteOrganizationsTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManager_DeleteOrganizationsTest";
    boolean pass = true;
    Organization org;
    String name = "TS TEST Name: DeleteOrganizationsTest";
    String descr = "Description: Test for DeleteOrganizationsTest";
    Collection orgs = null;
    Collection myKeys = null;
    try {
      // Create numOrgs organizations
      orgs = new ArrayList();
      org = JAXR_Util.createDefaultOrganization(blm);
      // Change organization name
      org.setName(blm.createInternationalString(name));
      org.setDescription(blm.createInternationalString(descr));
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs);
      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug)))
        throw new Fault(testName
            + " Error from saveOrganizations - test did not complete!");
      myKeys = br.getCollection();
      debug.add("Find the Organizations by Name \n");
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Iterator iter = null;
      Organization o = null;
      Collection orgKeys = new ArrayList();
      Collection names = new ArrayList();
      Collection myOrgs = null;
      Key key = null;

      names.add(name);
      br = bqm.getRegistryObjects(myKeys, LifeCycleManager.ORGANIZATION);

      if (!(JAXR_Util.checkBulkResponse("findOrganizations", br, debug)))
        throw new Fault(testName
            + " Error from findOrganizations - test did not complete!");

      myOrgs = br.getCollection();
      // iterate the collection
      iter = myOrgs.iterator();
      o = null;

      // get key for each organization..............
      while (iter.hasNext()) {
        o = (Organization) iter.next();
        debug.add("This is my orgs key: " + o.getKey().getId() + " \n");
        // delete service
        Collection services = o.getServices();
        o.removeServices(services);
        // deletes users from this organization
        Collection users = o.getUsers();
        o.removeUsers(users);
        key = (Key) o.getKey();
        // put keyID in Collection for getOrganizations
        orgKeys.add(key);
      }
      debug.add(" delete the organization \n");

      br = blm.deleteOrganizations(myKeys);
      if (!(JAXR_Util.checkBulkResponse("deleteOrganizations", br, debug)))
        throw new Fault(testName
            + " Error from findOrganizations - test did not complete!");
      names.clear();
      names.add(name);
      debug.add("Verify the organizations have been deleted \n");
      // attempt to find the deleted organizations

      RegistryObject ro = null;
      try {
        String orgKeyId = key.getId();
        ro = bqm.getRegistryObject(orgKeyId, LifeCycleManager.ORGANIZATION);
      } catch (Exception e) {
        debug.add(
            "Good - caught exception trying to get a deleted organization \n");
        TestUtil.printStackTrace(e);
        pass = true;
      }
      if (ro == null)
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

  /*
   * @testName: BusinessLifeCycleManager_SaveOrganizationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:272;
   *
   * @assertion: saveOrganizations - Saves specified Organizations. If the
   * object is not in the registry, then it is created in the registry. If it
   * already exists in the registry and has been modified, in the registry and
   * has been modified, then its state is updated (replaced) in the registry.
   * Partial commits are allowed. Processing stops on first SaveException
   * encountered
   *
   * @test_Strategy: Create and save an organization that is not in the registry
   * Verify the save
   *
   * Capability Level: 0 This interface is required to be implemented by JAXR
   * Providers at or above capability level 0.
   *
   *
   */
  public void BusinessLifeCycleManager_SaveOrganizationsTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManager_SaveOrganizationsTest";
    boolean pass = true;
    Organization org;
    String name = "TS Test Name: BusinessLifeCycleManager_SaveOrganizationsTest";
    String descr = "Description: Test for BusinessLifeCycleManager_SaveOrganizationsTest";
    Collection orgs = null;
    Collection myKeys = null;
    try {
      orgs = new ArrayList();
      org = JAXR_Util.createDefaultOrganization(blm);

      // Change organization name
      org.setName(blm.createInternationalString(tsLocale, name));
      org.setDescription(blm.createInternationalString(tsLocale, descr));
      orgs.add(org);

      BulkResponse br = blm.saveOrganizations(orgs);
      // check bulk response before proceeding....
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug)))
        throw new Fault(testName
            + " Error from saveOrganizations - test did not complete!");
      debug.add("Find the Organizations by Name \n");
      myKeys = br.getCollection();
      BusinessQueryManager bqm = rs.getBusinessQueryManager();

      Iterator iter = null;
      Organization o = null;
      Collection orgKeys = new ArrayList();
      Collection names = new ArrayList();
      Collection myOrgs = null;
      Key key = null;
      names.add(blm.createInternationalString(tsLocale, name));
      br = bqm.getRegistryObjects(myKeys, LifeCycleManager.ORGANIZATION);

      if (!(JAXR_Util.checkBulkResponse("findOrganizations", br, debug)))
        throw new Fault(testName
            + " Error from findOrganizations - test did not complete!");

      myOrgs = br.getCollection();
      // iterate the collection
      iter = myOrgs.iterator();
      o = null;

      // get key for each organization..............
      while (iter.hasNext()) {
        o = (Organization) iter.next();
      }

      if (o.getName().getValue(tsLocale).equals(name)) {
        debug.add("Got back expected Organization Name!\n");
      } else {
        pass = false;
        debug.add("Error: returned name not as expected! \n");
        debug.add("Expected name: " + name + "\n");
        debug.add("Returned name: " + o.getName().getValue(tsLocale) + "\n");
      }
      if (o.getDescription().getValue(tsLocale).equals(descr)) {
        debug.add("Got back expected Organization Description!\n");
      } else {
        pass = false;
        debug.add("Error: returned description not as expected! \n");
        debug.add("Expected description: " + descr + "\n");
        debug.add(
            "Returned description: " + o.getDescription().getValue() + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      super.cleanUpRegistry(myKeys, LifeCycleManager.ORGANIZATION);
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

  /*
   * @testName: saveOrganizationsUnexpectedObjectExceptionTest
   *
   * @assertion_ids: JAXR:SPEC:225;
   *
   * @assertion: saveOrganizations method accepts a Collection of Organization
   * instances. If the Collection contains an object whose type does not match
   * the save method, the implementation must throw an
   * UnexpectedObjectException. JAXR Specification version 1.0, section 8.3.1
   *
   * @test_Strategy: Invoke the saveOrganizations method. In the Collection
   * passed in as the argument, add a String object. Verify that the
   * UnexpectedObjectException is thrown.
   *
   */

  public void saveOrganizationsUnexpectedObjectExceptionTest() throws Fault {
    String testName = "saveOrganizationsUnexpectedObjectExceptionTest";
    boolean pass = false;
    Organization org;
    String notAnOrg = "This is a string object";
    Collection debug = null;
    debug = new ArrayList();
    try {

      org = JAXR_Util.createDefaultOrganization(blm);
      Collection orgs = new ArrayList();
      orgs.add(org);
      orgs.add(notAnOrg);
      try {
        BulkResponse br = blm.saveOrganizations(orgs);
        pass = false;
        debug.add(
            "Error! UnexpectedObjectException was NOT thrown as expected\n");
      } catch (UnexpectedObjectException uo) {
        TestUtil.printStackTrace(uo);
        debug.add("Good! UnexpectedObjectException was thrown as expected\n");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

  /*
   * @testName: saveOrganizationsInitialTest
   *
   * @assertion: saveOrganizations - Saves specified Organizations. If the
   * object is not in the registry, then it is created in the registry. If it
   * already exists in the registry and has been modified, in the registry and
   * has been modified, then its state is updated (replaced) in the registry.
   * Partial commits are allowed. Processing stops on first SaveException
   * encountered
   *
   * @test_Strategy: Create and save an organization that is not in the registry
   * Verify the save with a find. Delete the organization at the end.
   *
   * Capability Level: 0 This interface is required to be implemented by JAXR
   * Providers at or above capability level 0.
   *
   *
   */
  public void saveOrganizationsInitialTest() throws Fault {
    String testName = "saveOrganizationsInitialTest";
    boolean pass = true;
    Organization org;
    Collection myKeys = null;
    try {
      org = JAXR_Util.createDefaultOrganization(blm);
      Collection orgs = new ArrayList();
      orgs.add(org);

      BulkResponse br = blm.saveOrganizations(orgs);
      // Check for error
      debug.add("Status from saveOrganizations is: " + br.getStatus() + " \n");
      if (br.getExceptions() == null) {
        // no error get the keys
        debug.add(
            "Got back bulkresponse from saveOrganization with no errors!\n");
      } else {
        debug.add("Exception returned from saveOrganization!!\n");
        debug.add("IsPartialResponse returns: " + br.isPartialResponse());
        debug.add("Exception returned is: " + br.getExceptions());
      }

      myKeys = br.getCollection();
      debug.add("Now do a find on Organization\n");
      BusinessQueryManager bqm = rs.getBusinessQueryManager();

      Collection names = new ArrayList();
      names.add(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME);
      // Ok now get it back
      br = bqm.getRegistryObjects(myKeys, LifeCycleManager.ORGANIZATION);

      debug.add("Check for errors \n");
      Collection myOrgs = null;

      if (br.getExceptions() == null) {
        debug.add(
            "Got back bulkresponse from findOrganization with no errors!\n");
        myOrgs = br.getCollection();
        // iterate the collection
        Iterator iter = myOrgs.iterator();
        Organization o = null;
        Collection orgKeys = new ArrayList();
        while (iter.hasNext()) {
          o = (Organization) iter.next();
          // delete service
          Collection services = o.getServices();
          o.removeServices(services);
          // deletes users from this organization
          Collection users = o.getUsers();
          o.removeUsers(users);

          // getKey
          Key key = (Key) o.getKey();
          // put keyID in Collection for getOrganizations
          orgKeys.add(key);
          debug.add("Got an organization:  == " + o.getName() + "\n");
        }

        debug.add("Now delete the organization:  == " + o.getName() + "\n");
        br = blm.deleteOrganizations(orgKeys);
        debug.add("Now iterate thru again \n");
        while (iter.hasNext()) {
          o = (Organization) iter.next();
          debug.add("Got an organization:  == " + o.getName() + "\n");
        }

      } else {
        debug.add("Exception returned from findOrganization!!\n");

        debug.add("IsPartialResponse returns: " + br.isPartialResponse());
        debug.add("Exception returned is: " + br.getExceptions());
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      super.cleanUpRegistry(myKeys, LifeCycleManager.ORGANIZATION);
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

}
