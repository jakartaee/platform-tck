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
 * @(#)JAXRClient.java	1.28 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.jaxr.api.javax_xml_registry.LifeCycleManager;

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
import javax.activation.DataHandler;
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {

  // ================================================
  com.sun.ts.tests.jaxr.common.JAXR_Util util = null;

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
   * jaxrWebContext; webServerHost; webServerPort;
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
      super.cleanup(); // close connection
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
   * @testName: LifeCycleManager_createExtrinsicObjectTest
   *
   * 
   * @assertion_ids: JAXR:JAVADOC:153;JAXR:SPEC:8;
   *
   * @test_Strategy: A level 0 provider should throw an
   * UnsupportedCapabilityException error when this method is invoked. Invoke
   * the method and verify that level 0 providers return this error
   * 
   */
  public void LifeCycleManager_createExtrinsicObjectTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "LifeCycleManager_createExtrinsicObjectTest";
    boolean pass = false;
    Collection debug = null;
    int providerlevel;
    javax.activation.DataHandler repositoryItem;
    java.net.URL myUrl = null;
    String url = "http://www.ncsa.uiuc.edu/demoweb/url-primer.html";
    try {
      myUrl = new URL(url);

      repositoryItem = new DataHandler(myUrl);
      RegistryService rs = conn.getRegistryService();
      debug = new ArrayList();
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add("     The capability level for this JAXR provider is: "
          + providerlevel + "\n");
      debug.add("\n getting the BusinessLifeCycleManager\n");
      BusinessLifeCycleManager blm = rs.getBusinessLifeCycleManager();
      try {
        ExtrinsicObject eo = blm.createExtrinsicObject(repositoryItem);
        if (providerlevel > 0) {
          if (eo instanceof ExtrinsicObject) {
            pass = true;
          }
        } else
          pass = false;

      } catch (UnsupportedCapabilityException uce) {
        TestUtil.printStackTrace(uce);
        if (providerlevel == 0)
          pass = true;
        debug.add("UnsupportedCapabilityException was thrown!!\n");
      } catch (Exception uk) {
        debug.add("Error: unexpected exception was thrown\n");
        TestUtil.printStackTrace(uk);
        pass = false;
      }

    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: lifeCycleManager_createPersonNameL1Test
   *
   *
   * @assertion_ids: JAXR:JAVADOC:171;JAXR:SPEC:9;
   *
   * @test_Strategy: A level 0 provider should throw an
   * UnsupportedCapabilityException error when this method is invoked. Invoke
   * the method and verify that level 0 providers return this error
   *
   */
  public void lifeCycleManager_createPersonNameL1Test() throws Fault {
    // get the Connection object conn from the super class
    String testName = "lifeCycleManager_createPersonNameL1Test";
    boolean pass = false;
    Collection debug = null;
    int providerlevel;

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
      debug.add("\n getting the BusinessLifeCycleManager\n");
      BusinessLifeCycleManager blm = rs.getBusinessLifeCycleManager();
      try {
        PersonName pn = blm.createPersonName("First", "Middle", "Last");
        if (providerlevel > 0) {
          if (pn instanceof PersonName) {
            pass = true;
          }
        } else
          pass = false;

      } catch (UnsupportedCapabilityException uce) {
        TestUtil.printStackTrace(uce);
        if (providerlevel == 0)
          pass = true;
        debug.add("UnsupportedCapabilityException was thrown!!\n");
      } catch (Exception uk) {
        debug.add("Error: unexpected exception was thrown\n");
        TestUtil.printStackTrace(uk);
        pass = false;
      }
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");

  } // end of test method

  /*
   * @testName: LifeCycleManager_createIntRegistryPackageTest
   *
   *
   * @assertion_ids: JAXR:SPEC:11;
   *
   * @test_Strategy: A level 0 provider should throw an
   * UnsupportedCapabilityException error when this method is invoked. Invoke
   * the method and verify that level 0 providers return this error
   *
   */
  public void LifeCycleManager_createIntRegistryPackageTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "LifeCycleManager_createIntRegistryPackageTest";
    boolean pass = false;
    Collection debug = null;
    int providerlevel;
    try {
      RegistryService rs = conn.getRegistryService();
      debug = new ArrayList();
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add("     The capability level for this JAXR provider is: "
          + providerlevel + "\n");
      BusinessLifeCycleManager blm = rs.getBusinessLifeCycleManager();
      try {
        InternationalString name = blm.createInternationalString("TestPackage");
        RegistryPackage rp = blm.createRegistryPackage(name);
        if (providerlevel > 0) {
          if (rp instanceof RegistryPackage) {
            pass = true;
          }
        } else
          pass = false;

      } catch (UnsupportedCapabilityException uce) {
        TestUtil.printStackTrace(uce);
        if (providerlevel == 0)
          pass = true;
        debug.add("UnsupportedCapabilityException was thrown!!\n");
      } catch (Exception uk) {
        debug.add("Error: unexpected exception was thrown\n");
        TestUtil.printStackTrace(uk);
        pass = false;
      }
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");

  } // end of test method

  /*
   * @testName: LifeCycleManager_createRegistryPackageTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:177;JAXR:SPEC:10;
   *
   * @test_Strategy: A level 0 provider should throw an
   * UnsupportedCapabilityException error when this method is invoked. Invoke
   * the method and verify that level 0 providers return this error
   *
   */
  public void LifeCycleManager_createRegistryPackageTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "LifeCycleManager_createRegistryPackageTest";
    boolean pass = false;
    Collection debug = null;
    int providerlevel;
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
      debug.add("\n getting the BusinessLifeCycleManager\n");
      BusinessLifeCycleManager blm = rs.getBusinessLifeCycleManager();
      try {
        RegistryPackage rp = blm.createRegistryPackage("TestPackage");
        if (providerlevel > 0) {
          if (rp instanceof RegistryPackage) {
            pass = true;
          }
        } else
          pass = false;

      } catch (UnsupportedCapabilityException uce) {
        TestUtil.printStackTrace(uce);
        if (providerlevel == 0)
          pass = true;
        debug.add("UnsupportedCapabilityException was thrown!!\n");
      } catch (Exception uk) {
        debug.add("Error: unexpected exception was thrown\n");
        TestUtil.printStackTrace(uk);
        pass = false;
      }
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");

  } // end of test method
  /*
   * @testName: lifeCycleManager_createAssociationTest
   *
   * @assertion_ids: JAXR:JAVADOC:119;
   *
   * @test_Strategy: use createAssociation to create an Association. Verify an
   * Association is returned.
   *
   */

  public void lifeCycleManager_createAssociationTest() throws Fault {
    String testName = "lifeCycleManager_createAssociationTest";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      InternationalString iName = blm.createInternationalString(orgName);
      Organization targetObject = blm.createOrganization(iName);
      Concept concept = null;
      Association assoc = blm.createAssociation(targetObject, concept);
      if (assoc == null)
        throw new Fault(testName + "Error: createAssociation returned null!");
      if (assoc instanceof Association)
        pass = true;

    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");

  } // end of test method

  /*
   * @testName: lifeCycleManager_createExternalClassificationTest
   *
   * @assertion_ids: JAXR:JAVADOC:121;
   *
   * @test_Strategy: use createClassification to create an Classification.
   * Verify a Classification is returned.
   *
   */
  public void lifeCycleManager_createExternalClassificationTest() throws Fault {
    String testName = "lifeCycleManager_createExternalClassificationTest";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;

    String schemeName = "NAICS";
    String classificationName = "Book Publishers";
    String classificationValue = "51113";
    String schemeDescription = "North American Industry Classification System";
    try {
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      // create an external Classification
      Classification classification = blm.createClassification(scheme,
          classificationName, classificationValue);
      if (classification == null)
        throw new Fault(
            testName + "Error: createClassification returned null!");
      if (classification instanceof Classification)
        pass = true;
      else
        throw new Fault(
            testName + "Error: createClassification returned null!");
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");

  } // end of test method

  /*
   * @testName: lifeCycleManager_createExternalClassificationInternationalTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:123;
   *
   * @test_Strategy: use createClassification to create an Classification.
   * Verify a Classification is returned.
   *
   */
  public void lifeCycleManager_createExternalClassificationInternationalTest()
      throws Fault {
    String testName = "lifeCycleManager_createExternalClassificationInternationalTest";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;

    String schemeName = "NAICS";
    String classificationName = "Book Publishers";
    String classificationValue = "51113";
    String schemeDescription = "North American Industry Classification System";
    Classification classification = null;
    try {
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      // create an external Classification
      InternationalString name = blm
          .createInternationalString(classificationName);
      classification = blm.createClassification(scheme, name,
          classificationValue);
      if (classification == null)
        throw new Fault(
            testName + "Error: createClassification returned null!");
      if (classification instanceof Classification)
        pass = true;
      else
        throw new Fault(
            testName + "Error: createClassification returned null!");
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");

  } // end of test method

  /*
   * @testName: lifeCycleManager_createInternalClassificationTest
   *
   * @assertion_ids: JAXR:JAVADOC:125;
   * 
   * @test_Strategy: use createClassification to create an Classification.
   * Verify a Classification is returned.
   *
   */
  public void lifeCycleManager_createInternalClassificationTest() throws Fault {
    String testName = "lifeCycleManager_createInternalClassificationTest";
    boolean pass = false;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;

    String schemeName = "NAICS";
    String schemeDescription = "North American Industry Classification System";
    String conceptName = "NAICS";
    String conceptValue = "51";
    try {

      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      // create a concept for the internal classification
      Concept concept = blm.createConcept(scheme, conceptName, conceptValue);
      // create an internal Classification
      Classification classification = blm.createClassification(concept);
      if (classification == null)
        throw new Fault(
            testName + "Error: createClassification returned null!");
      if (classification instanceof Classification)
        pass = true;
      else
        throw new Fault(
            testName + "Error: createClassification returned null!");
    } catch (Exception ue) {
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");

  } // end of test method

  /*
   * @testName: lifeCycleManager_createInvalidInternalClassificationTest
   *
   * @assertion_ids: JAXR:JAVADOC:127;
   *
   * @test_Strategy: use createClassification to create an Classification. Use a
   * Concept that is not under a ClassificationScheme. Verify a
   * InvalidRequestException thrown.
   *
   */
  public void lifeCycleManager_createInvalidInternalClassificationTest()
      throws Fault {
    String testName = "lifeCycleManager_createInvalidInternalClassificationTest";
    try {
      Concept concept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      // create an internal Classification
      Classification classification = blm.createClassification(concept);
      // should not have gotten here!
      throw new Fault(
          testName + "Error: InvalidRequestException not thrown as expected!");
    } catch (InvalidRequestException ir) {
      TestUtil.printStackTrace(ir);
      debug.add("InvalidRequestException thrown as expected!");
      debug.add(ir.getMessage() + "\n");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createClassificationSchemeTest
   *
   * @assertion_ids: JAXR:JAVADOC:128;
   *
   * @test_Strategy: Verify that createClassificationScheme returns a
   * ClassificationScheme
   *
   */
  public void lifeCycleManager_createClassificationSchemeTest() throws Fault {
    String testName = "lifeCycleManager_createClassificationSchemeTest";
    String schemeName = "NAICS";
    String schemeDescription = "North American Industry Classification System";
    try {
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      if (!(scheme instanceof ClassificationScheme))
        throw new Fault(testName
            + "Error: ClassificationScheme not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createClassificationSchemeInvalidTest
   *
   * @assertion_ids: JAXR:JAVADOC:136;
   *
   * @test_Strategy: invoke createClassificationScheme with a concept that is
   * under a classification scheme. Verify that it throws an
   * InvalidRequestException
   *
   */
  public void lifeCycleManager_createClassificationSchemeInvalidTest()
      throws Fault {
    String testName = "lifeCycleManager_createClassificationSchemeInvalidTest";
    String schemeName = "Geography";
    String schemeDescription = "North American Regions";
    String conceptName = "North America";
    String conceptValue = "NA";
    String conceptNameCA = "California";
    String conceptValueCA = "US-CA";
    String conceptNameUS = "United States";
    String conceptValueUS = "US";

    try {
      debug.add("Create a concept and then a child concept\n");
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      // Parent
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);
      // Child
      Concept caConcept = blm.createConcept(scheme, conceptNameCA,
          conceptValueCA);
      usConcept.addChildConcept(caConcept);

      ClassificationScheme testScheme = blm
          .createClassificationScheme(caConcept);
      debug.add("Error: failed to throw an InvalidRequestException!\n");
      throw new Fault(
          testName + "concept has parent and should have thrown an error!");
    } catch (InvalidRequestException ir) {
      debug.add("Threw exception as expected!!\n");
      TestUtil.printStackTrace(ir);
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }

  } // end of test method

  /*
   * @testName: lifeCycleManager_createConceptTest
   *
   * @assertion_ids: JAXR:JAVADOC:137;
   *
   * @test_Strategy: use createConcept to create a concept. Verify that a
   * concept is returned.
   *
   */
  public void lifeCycleManager_createConceptTest() throws Fault {
    String testName = "lifeCycleManager_createConceptTest";
    String schemeName = "Geography";
    String schemeDescription = "North American Regions";
    String conceptName = "North America";
    String conceptValue = "NA";
    try {
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      // Parent
      Concept usConcept = blm.createConcept(scheme, conceptName, conceptValue);
      if (!(usConcept instanceof Concept))
        throw new Fault(
            testName + "concept has parent and should have thrown an error!");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }

  } // end of test method

  /*
   * @testName: lifeCycleManager_createConceptInternationalTest
   *
   * @assertion_ids: JAXR:JAVADOC:139;
   *
   * @test_Strategy: use createConcept to create a concept. Verify that a
   * concept is returned.
   *
   */
  public void lifeCycleManager_createConceptInternationalTest() throws Fault {
    String testName = "lifeCycleManager_createConceptInternationalTest";
    String schemeName = "Geography";
    String schemeDescription = "North American Regions";
    String conceptName = "North America";
    String conceptValue = "NA";
    try {
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      // Parent
      InternationalString name = blm.createInternationalString(conceptName);

      Concept usConcept = blm.createConcept(scheme, name, conceptValue);
      if (!(usConcept instanceof Concept))
        throw new Fault(
            testName + "concept has parent and should have thrown an error!");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }

  } // end of test method

  /*
   * @testName: lifeCycleManager_createExternalIdentifierTest
   *
   * @assertion_ids: JAXR:JAVADOC:145;
   *
   * @test_Strategy: Use createExternalIdentifier to create an
   * ExternalIdentifier. Verify ExternalIdentifier is returned.
   *
   */
  public void lifeCycleManager_createExternalIdentifierTest() throws Fault {
    String testName = "lifeCycleManager_createExternalIdentifierTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";

    try {
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, name, value);
      if (!(ei instanceof ExternalIdentifier))
        throw new Fault(testName + "Error: ExternalIdentifier not returned");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }

  } // end of test method
  /*
   * @testName: lifeCycleManager_createExternalIdentifierInternationalTest
   *
   * @assertion_ids: JAXR:JAVADOC:147;
   *
   * @test_Strategy: Use createExternalIdentifier to create an
   * ExternalIdentifier. Verify ExternalIdentifier is returned.
   *
   */

  public void lifeCycleManager_createExternalIdentifierInternationalTest()
      throws Fault {
    String testName = "lifeCycleManager_createExternalIdentifierInternationalTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";

    try {
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(name);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, iName,
          value);
      if (!(ei instanceof ExternalIdentifier))
        throw new Fault(testName + "Error: ExternalIdentifier not returned");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }

  } // end of test method

  /*
   * @testName: lifeCycleManager_createInternationalClassificationSchemeTest
   *
   * @assertion_ids: JAXR:JAVADOC:131;JAXR:SPEC:189;
   *
   * @test_Strategy: Verify that createClassificationScheme returns a
   * ClassificationScheme
   *
   */
  public void lifeCycleManager_createInternationalClassificationSchemeTest()
      throws Fault {
    String testName = "lifeCycleManager_createInternationalClassificationSchemeTest";
    String schemeName = "NAICS";
    String schemeDescription = "North American Industry Classification System";
    ClassificationScheme scheme = null;
    try {
      InternationalString ischemeDescription = blm
          .createInternationalString(schemeDescription);
      InternationalString ischemeName = blm
          .createInternationalString(schemeName);
      scheme = blm.createClassificationScheme(ischemeName, ischemeDescription);
      if (!(scheme instanceof ClassificationScheme))
        throw new Fault(testName
            + "Error: ClassificationScheme not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createClassificationSchemeConceptTest
   *
   * @assertion_ids: JAXR:JAVADOC:134;
   *
   * @test_Strategy: Verify that createClassificationScheme returns a
   * ClassificationScheme
   *
   */
  public void lifeCycleManager_createClassificationSchemeConceptTest()
      throws Fault {
    String testName = "lifeCycleManager_createClassificationSchemeConceptTest";
    try {
      Concept concept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      concept.setValue("conceptValue");
      if (concept == null)
        throw new Fault(
            testName + "Error: createObject returned a null concept!");

      ClassificationScheme scheme = blm.createClassificationScheme(concept);
      if (!(scheme instanceof ClassificationScheme))
        throw new Fault(testName
            + "Error: ClassificationScheme not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createEmailAddressTest
   *
   * @assertion_ids: JAXR:JAVADOC:141;
   *
   * @test_Strategy: Use createEmailAddress method to create an email address.
   * Verify it returns an EmailAddress
   *
   */
  public void lifeCycleManager_createEmailAddressTest() throws Fault {
    String testName = "lifeCycleManager_createEmailAddressTest";
    String address = "abc@myCo.com";
    try {
      EmailAddress ea = blm.createEmailAddress(address);
      if (!(ea instanceof EmailAddress))
        throw new Fault(
            testName + "Error: EmailAddress not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createEmailAddress2Test
   *
   *
   * @assertion_ids: JAXR:JAVADOC:143;
   *
   * @test_Strategy: Use createEmailAddress method to create an email address.
   * Verify it returns an EmailAddress
   *
   */
  public void lifeCycleManager_createEmailAddress2Test() throws Fault {
    String testName = "lifeCycleManager_createEmailAddress2Test";
    String address = "abc@myCo.com";
    String type = "headquarters";
    try {
      EmailAddress ea = blm.createEmailAddress(address, type);
      if (!(ea instanceof EmailAddress))
        throw new Fault(
            testName + "Error: EmailAddress not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createExternalLinkTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:149;
   *
   * @test_Strategy: Use createExternalLink method to create an external link
   * Verify it returns an ExternalLink
   *
   */
  public void lifeCycleManager_createExternalLinkTest() throws Fault {
    String testName = "lifeCycleManager_createExternalLinkTest";
    String externalURI = baseuri + "jaxrTestPage1.html";
    String description = "web portal";
    try {
      ExternalLink el = blm.createExternalLink(externalURI, description);
      if (!(el instanceof ExternalLink))
        throw new Fault(
            testName + "Error: ExternalLink not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method
  /*
   * @testName: lifeCycleManager_createExternalLinkTestInternational
   *
   *
   * @assertion_ids: JAXR:JAVADOC:151;
   *
   * @test_Strategy: Use createExternalLink method to create an external link
   * Verify it returns an ExternalLink
   *
   */

  public void lifeCycleManager_createExternalLinkTestInternational()
      throws Fault {
    String testName = "lifeCycleManager_createExternalLinkTestInternational";
    String externalURI = baseuri + "jaxrTestPage1.html";
    String description = "web portal";
    try {
      InternationalString idescription = blm
          .createInternationalString(description);
      ExternalLink el = blm.createExternalLink(externalURI, idescription);
      if (!(el instanceof ExternalLink))
        throw new Fault(
            testName + "Error: ExternalLink not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createKey
   *
   * @assertion_ids: JAXR:JAVADOC:161;
   *
   * @test_Strategy: Use createKey method to create a key. Verify it returns a
   * Key.
   *
   */
  public void lifeCycleManager_createKey() throws Fault {
    String testName = "lifeCycleManager_createKey";
    String theKey = "32D8CEB0-D8D9-11D5-AE91-000629DC0A2B";
    try {
      Key key = blm.createKey(theKey);
      if (!(key instanceof Key))
        throw new Fault(
            testName + "Error: Key not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: LifeCycleManager_createInternationalStringTest
   *
   * @assertion_ids: JAXR:JAVADOC:155;
   *
   * @test_Strategy: Create an International String and verify it.
   *
   */
  public void LifeCycleManager_createInternationalStringTest() throws Fault {
    String testName = "LifeCycleManager_createInternationalStringTest";
    try {
      InternationalString is = blm.createInternationalString();
      if (!(is instanceof InternationalString))
        throw new Fault(testName
            + "createInternationalString did not return InternationalString instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

  /*
   * @testName: LifeCycleManager_createInternationalString2Test
   *
   * @assertion_ids: JAXR:JAVADOC:159;
   *
   * @test_Strategy: Create an International String and verify it.
   *
   */
  public void LifeCycleManager_createInternationalString2Test() throws Fault {
    String testName = "LifeCycleManager_createInternationalString2Test";
    String str = "MyInternationalString";
    try {
      Locale l = Locale.getDefault();
      debug.add("The locale is set to: " + l.getCountry() + "\n");
      InternationalString is = blm.createInternationalString(l, str);
      if (!(is instanceof InternationalString))
        throw new Fault(testName
            + "createInternationalString did not return InternationalString instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method
  /*
   * @testName: LifeCycleManager_createInternationalString3Test
   *
   * @assertion_ids: JAXR:JAVADOC:157;
   *
   * @test_Strategy: Create an International String and verify it.
   *
   */

  public void LifeCycleManager_createInternationalString3Test() throws Fault {
    String testName = "LifeCycleManager_createInternationalString3Test";
    String str = "MyInternationalString";
    try {
      InternationalString is = blm.createInternationalString(str);
      if (!(is instanceof InternationalString))
        throw new Fault(testName
            + "createInternationalString did not return InternationalString instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method
  /*
   * @testName: LifeCycleManager_createLocalizedStringTest
   *
   * @assertion_ids: JAXR:JAVADOC:163;
   *
   * @test_Strategy: Create a LocalizedString and verify it.
   */

  public void LifeCycleManager_createLocalizedStringTest() throws Fault {
    String testName = "LifeCycleManager_createLocalizedStringTest";
    String str = "MyString";
    try {
      Locale l = Locale.getDefault();
      debug.add("The locale is set to: " + l.getCountry() + "\n");
      LocalizedString ls = blm.createLocalizedString(l, str);
      if (!(ls instanceof LocalizedString))
        throw new Fault(testName
            + "createLocalizedString did not return a LocalizedString instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

  /*
   * @testName: LifeCycleManager_createLocalizedStringTest2
   *
   * @assertion_ids: JAXR:JAVADOC:165;
   *
   * @test_Strategy: Create a LocalizedString(locale,string, string) and verify
   * it.
   */
  public void LifeCycleManager_createLocalizedStringTest2() throws Fault {
    String testName = "LifeCycleManager_createLocalizedStringTest2";
    String str = "MyString";
    String charset = "UTF-8";

    try {
      Locale l = Locale.getDefault();
      LocalizedString ls = blm.createLocalizedString(l, str, charset);
      if (!(ls instanceof LocalizedString))
        throw new Fault(testName
            + "createLocalizedString(locale, string, string)  did not return a LocalizedString instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

  /*
   * @testName: LifeCycleManager_createOrganizationTest
   *
   * @assertion_ids: JAXR:JAVADOC:167;
   *
   * @test_Strategy: Create an organization and verify it.
   */
  public void LifeCycleManager_createOrganizationTest() throws Fault {
    String testName = "LifeCycleManager_createOrganizationTest";
    String str = "MyOrganization";
    try {
      Locale l = Locale.getDefault();
      InternationalString iName = blm.createInternationalString(str);
      Organization org = blm.createOrganization(iName);
      if (!(org instanceof Organization))
        throw new Fault(testName
            + "createOrganization did not return an Organization instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

  /*
   * @testName: LifeCycleManager_createOrganizationInternationalTest
   *
   * @assertion_ids: JAXR:JAVADOC:169;
   *
   * @test_Strategy: Create an organization and verify it.
   */
  public void LifeCycleManager_createOrganizationInternationalTest()
      throws Fault {
    String testName = "LifeCycleManager_createOrganizationInternationalTest";
    String str = "MyOrganization";
    try {
      Locale l = Locale.getDefault();
      InternationalString istr = blm.createInternationalString();
      Organization org = blm.createOrganization(istr);
      if (!(org instanceof Organization))
        throw new Fault(testName
            + "createOrganization(internationalString)  did not return an Organization instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

  /*
   * @testName: LifeCycleManager_createPersonNameTest
   *
   * @assertion_ids: JAXR:JAVADOC:173;
   *
   * @test_Strategy: Create a PersonName and verify it.
   */
  public void LifeCycleManager_createPersonNameTest() throws Fault {
    String testName = "LifeCycleManager_createPersonNameTest";
    String name = "Minnie Mouse";
    try {
      PersonName pn = blm.createPersonName(name);
      if (!(pn instanceof PersonName))
        throw new Fault(testName
            + "createPersonName did not return a PersonName instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

  /*
   * @testName: LifeCycleManager_createPostalAddressTest
   *
   * @assertion_ids: JAXR:JAVADOC:175;
   *
   * @test_Strategy: Create a PostalAddress and verify it.
   */
  public void LifeCycleManager_createPostalAddressTest() throws Fault {
    String testName = "LifeCycleManager_createPostalAddressTest";
    String name = "Minnie Mouse";
    String streetNumber = "6";
    String street = "Ivaloo";
    String city = "Somerville";
    String stateOrProvince = "MA";
    String country = "USA";
    String postalCode = "01950";
    String type = "snailmail";
    try {
      PostalAddress pa = blm.createPostalAddress(streetNumber, street, city,
          stateOrProvince, country, postalCode, type);
      if (!(pa instanceof PostalAddress))
        throw new Fault(testName
            + "createPostalAddress did not return a PostalAddress instance!");
    } catch (JAXRException e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "failed", e);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createServiceTest
   *
   * @assertion_ids: JAXR:JAVADOC:181;
   *
   * @test_Strategy: use createService to create a service and verify it.
   *
   */
  public void lifeCycleManager_createServiceTest() throws Fault {
    String testName = "lifeCycleManager_createServiceTest";
    String serviceName = "MyTestService";
    try {
      Service s = blm.createService(serviceName);
      if (!(s instanceof Service))
        throw new Fault(
            testName + "Error: Service not returned from create method");

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createServiceInternationalTest
   *
   * @assertion_ids: JAXR:JAVADOC:183;
   *
   * @test_Strategy: use createService to create a service and verify it.
   *
   */
  public void lifeCycleManager_createServiceInternationalTest() throws Fault {
    String testName = "lifeCycleManager_createServiceInternationalTest";
    String name = "MyTestService";
    try {
      InternationalString serviceName = blm.createInternationalString(name);
      Service s = blm.createService(serviceName);
      if (!(s instanceof Service))
        throw new Fault(
            testName + "Error: Service not returned from create method");

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createServiceBindingTest
   *
   * @assertion_ids: JAXR:JAVADOC:185;
   *
   * @test_Strategy: use createServiceBinding to create a service and verify it.
   *
   */
  public void lifeCycleManager_createServiceBindingTest() throws Fault {
    String testName = "lifeCycleManager_createServiceBindingTest";
    try {
      ServiceBinding sb = blm.createServiceBinding();
      if (!(sb instanceof ServiceBinding))
        throw new Fault(
            testName + "Error: ServiceBinding not returned from create method");

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createSlotTest
   *
   * @assertion_ids: JAXR:JAVADOC:187;
   *
   * @test_Strategy: use createslot to create a slot and verify it.
   *
   */
  public void lifeCycleManager_createSlotTest() throws Fault {
    String testName = "lifeCycleManager_createSlotTest()";
    String name = "streetAddress";
    String value = "Main Street";
    String slotType = "type";
    try {
      Slot slot = blm.createSlot(name, value, slotType);
      if (!(slot instanceof Slot))
        throw new Fault(
            testName + "Error: Slot not returned from create method");

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method
  /*
   * @testName: lifeCycleManager_createSlot2Test
   *
   * @assertion_ids: JAXR:JAVADOC:189;
   *
   * @test_Strategy: use createslot to create a slot and verify it.
   *
   */

  public void lifeCycleManager_createSlot2Test() throws Fault {
    String testName = "lifeCycleManager_createSlot2Test()";
    String name = "streetAddress";
    String value = "Main Street";
    String slotType = "type";
    try {
      Collection values = new ArrayList();
      values.add(value);
      Slot slot = blm.createSlot(name, values, slotType);
      if (!(slot instanceof Slot))
        throw new Fault(
            testName + "Error: Slot not returned from create method");

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createSpecificationLinkTest
   *
   * @assertion_ids: JAXR:JAVADOC:191;
   *
   * @test_Strategy: use createSpecificationLink and verify it.
   *
   */
  public void lifeCycleManager_createSpecificationLinkTest() throws Fault {
    String testName = "lifeCycleManager_createSpecificationLinkTest()";
    try {
      SpecificationLink sl = blm.createSpecificationLink();
      if (!(sl instanceof SpecificationLink))
        throw new Fault(testName
            + "Error: SpecificationLink not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createTelephoneNumberTest
   *
   * @assertion_ids: JAXR:JAVADOC:193;
   *
   * @test_Strategy: use TelephoneNumber and verify it.
   *
   */
  public void lifeCycleManager_createTelephoneNumberTest() throws Fault {
    String testName = "lifeCycleManager_createTelephoneNumberTest()";
    try {
      TelephoneNumber telno = blm.createTelephoneNumber();
      if (!(telno instanceof TelephoneNumber))
        throw new Fault(testName
            + "Error: TelephoneNumber not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_createUserTest
   *
   * @assertion_ids: JAXR:JAVADOC:195;
   *
   * @test_Strategy: create a User and verify it.
   *
   */
  public void lifeCycleManager_createUserTest() throws Fault {
    String testName = "lifeCycleManager_createUserTest()";
    try {
      User user = blm.createUser();
      if (!(user instanceof User))
        throw new Fault(
            testName + "Error: User not returned from create method");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_getRegistryService
   *
   * @assertion_ids: JAXR:JAVADOC:205;
   *
   * @test_Strategy: invoke getRegistryService and verify RegistryService was
   * returned.
   *
   */
  public void lifeCycleManager_getRegistryService() throws Fault {
    String testName = "lifeCycleManager_getRegistryService()";
    try {
      RegistryService rs = blm.getRegistryService();
      if (!(rs instanceof RegistryService))
        throw new Fault(testName
            + "Error:  RegistryService was not returned from getRegistryService");
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

} // end of class
