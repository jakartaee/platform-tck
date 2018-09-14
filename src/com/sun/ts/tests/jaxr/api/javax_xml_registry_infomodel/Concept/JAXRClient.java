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
 * @(#)JAXRClient.java	1.19 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.Concept;

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
  com.sun.ts.tests.jaxr.common.JAXR_Util util = null;

  ClassificationScheme scheme = null;

  String schemeName = "Geography";

  String schemeDescription = "North American Regions";

  String[] childConceptName = { "California", "Alaska", "Alabama", "Arkansas",
      "Arizona", "Colorado", "Florida" };

  String[] childConceptValue = { "US-CA", "US-AK", "US-AL", "US-AR", "US-AZ",
      "US-CO", "US-FL" };

  String conceptName = "North America";

  String conceptValue = "NA";

  String conceptNameCA = "California";

  String conceptValueCA = "US-CA";

  String conceptNameAK = "Alaska";

  String conceptValueAK = "US-AK";

  String conceptNameAL = "Alabama";

  String conceptValueAL = "US-AL";

  String conceptNameAR = "Arkansas";

  String conceptValueAR = "US-AR";

  String conceptNameAZ = "Arizona";

  String conceptValueAZ = "US-AZ";

  String conceptNameCO = "Colorado";

  String conceptValueCO = "US-CO";

  String conceptNameFL = "Florida";

  String conceptValueFL = "US-FL";

  String conceptNameUS = "United States";

  String conceptValueUS = "US";

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
   * @testName: concept_get_setValue_Test
   *
   *
   * @assertion_ids: JAXR:JAVADOC:726; JAXR:JAVADOC:728
   *
   * @test_Strategy: setValue to a string the use getValue to verify.
   * 
   *
   */
  public void concept_get_setValue_Test() throws Fault {
    // ---
    String testName = "concept_get_setValue_Test";
    boolean pass = false;
    String code = "51";
    try {
      // create a Concept object.......................
      Concept concept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      debug.add("Setting the string value to " + code + "\n");
      concept.setValue(code);
      if (concept.getValue().equals(code)) {
        pass = true;
        debug.add("Success! setValue was returned.");
      } else
        debug.add("FAIL: getValue returned " + concept.getValue());

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of concept_setValue_Test

  /*
   * @testName: concept_addChildConceptTest
   *
   * @assertion: addChildConcept - Add a child concept
   *
   * @assertion_ids: JAXR:JAVADOC:730;JAXR:SPEC:91;JAXR:SPEC:90;JAXR:SPEC:120;
   *
   * @test_Strategy: Create a concept parent - US and then a child CA. Use
   * addChildConcept method to add child (CA) to parent(US). Verify addition by
   * getting the children concepts from US and checking for the concept value
   * for CA
   *
   */
  public void concept_addChildConceptTest() throws Fault {
    String testName = "concept_addChildConceptTest";
    boolean pass = false;
    Concept concept = null;
    try {
      debug.add("Create a concept and then a child concept\n");
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      // Parent
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);
      // Child
      Concept caConcept = blm.createConcept(scheme, conceptNameCA,
          conceptValueCA);

      debug.add("Add a child to the concept\n");
      usConcept.addChildConcept(caConcept);
      Collection concepts = usConcept.getChildrenConcepts();
      debug.add("Child count returned from getChildrenConcepts is "
          + concepts.size() + "\n");

      Iterator iter = concepts.iterator();
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        // verify the child - there should only be one.
        if (concept.getValue().equals(conceptValueCA)) {
          pass = true;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of concept_addChildConceptTest

  /*
   * @testName: concept_addGetChildConceptsTest
   *
   * @assertion_ids: JAXR:JAVADOC:732; JAXR:JAVADOC:740
   *
   * @test_Strategy: Create a US parent. Create CA and AK child concepts. Add
   * the children to the parent US with addChildConcepts. Use
   * getChildrenConcepts to retrieve children from US. Verify that CA and AK
   * were returned as children.
   */
  public void concept_addGetChildConceptsTest() throws Fault {
    String testName = "concept_addGetChildConceptsTest";
    boolean pass = false;
    Concept concept = null;
    int childCount = 2;
    try {
      debug.add("Create a concept and then a child concept\n");
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      // Parent
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);
      // Children
      Concept caConcept = blm.createConcept(scheme, conceptNameCA,
          conceptValueCA);
      Concept akConcept = blm.createConcept(scheme, conceptNameAK,
          conceptValueAK);

      Collection concepts = new ArrayList();
      concepts.add(caConcept);
      concepts.add(akConcept);

      debug.add("Add children \n");
      usConcept.addChildConcepts(concepts);
      concepts = usConcept.getChildrenConcepts();
      debug.add("Child count returned from getChildrenConcepts is "
          + concepts.size() + "\n");

      Iterator iter = concepts.iterator();
      int count = 0;
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        // verify the children
        if (concept.getValue().equals(conceptValueCA)
            || concept.getValue().equals(conceptValueAK)) {
          count = count + 1;
        }
      }
      if (count == childCount)
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of concept_addGetChildConceptsTest

  /*
   * @testName: concept_getChildConceptCount
   *
   * @assertion_ids: JAXR:JAVADOC:738
   *
   * @test_Strategy: Create a parent concept - US. Create child concepts for
   * "California","Alaska", "Alabama","Arkansas","Arizona","Colorado", and
   * "Florida" Add the children to the parent - US. Verify the count returned
   * from getChildConceptCount matches the number of children added.
   * 
   *
   */
  public void concept_getChildConceptCount() throws Fault {
    String testName = "concept_getChildConceptCount";
    boolean pass = false;
    try {
      Concept childConcept[] = new Concept[childConceptName.length];
      debug.add("Create a concept and then a child concept\n");

      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      Collection concepts = new ArrayList();
      // Parent
      debug.add("Creating Parent Concept: " + conceptNameUS + "\n");
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);

      // Children
      for (int i = 0; i < childConceptName.length; i++) {
        debug.add("Creating child Concept for : " + childConceptName[i] + "\n");
        childConcept[i] = blm.createConcept(scheme, childConceptName[i],
            childConceptValue[i]);
        concepts.add(childConcept[i]);
      }

      debug.add("Add children to Parent \n");
      usConcept.addChildConcepts(concepts);
      debug.add("Child count returned from getChildrenConcepts is "
          + usConcept.getChildConceptCount() + "\n");
      if (childConceptName.length == usConcept.getChildConceptCount())
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of concept_getChildConceptCount

  /*
   * @testName: concept_getClassificationScheme
   *
   * @assertion: getClassificationScheme - Get the ClassificationScheme that
   * this Concept is a descendent of
   *
   * @assertion_ids: JAXR:JAVADOC:746
   *
   * @test_Strategy: Create a classification scheme and give it a name. Create a
   * concept with a reference to a parent ClassificationScheme. Use
   * getClassificationScheme to get the scheme. Verify the name.
   *
   */
  public void concept_getClassificationScheme() throws Fault {
    String testName = "concept_getClassificationScheme";
    boolean pass = false;
    String name = "conceptDescendent";
    try {
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      scheme.setName(blm.createInternationalString(name));
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);
      scheme = usConcept.getClassificationScheme();
      if (scheme.getName().getValue().equals(name)) {
        pass = true;
        debug.add("Success! returned: " + scheme.getName().getValue());
      } else
        debug.add("FAIL: isExternal returned " + scheme.getName().getValue());

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of concept_getClassificationScheme

  /*
   * @testName: concept_getDescendantConcepts
   *
   * @assertion_ids: JAXR:JAVADOC:742
   *
   * 
   * @test_Strategy: Create parent, child and grandChildren concepts. Verify
   * that getDescendants returns all children and grandchildren
   * 
   */
  public void concept_getDescendantConcepts() throws Fault {
    String testName = "concept_getDescendantConcepts";
    int passCount = childConceptName.length;
    int count = 0;
    boolean pass1 = false;
    boolean pass2 = false;
    try {
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      // North America concept
      Concept naConcept = blm.createConcept(scheme, conceptName, conceptValue);
      // United States Concept
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);
      naConcept.addChildConcept(usConcept);
      Concept grandChildConcept[] = new Concept[childConceptName.length];
      Collection concepts = new ArrayList();
      // State concepts
      for (int i = 0; i < childConceptName.length; i++) {
        debug.add(
            "Creating grandChild Concept for : " + childConceptName[i] + "\n");
        grandChildConcept[i] = blm.createConcept(scheme, childConceptName[i],
            childConceptValue[i]);
        concepts.add(grandChildConcept[i]);
      }
      usConcept.addChildConcepts(concepts);
      debug.add("Descendants of naConcept\n");
      Collection d1Concepts = naConcept.getDescendantConcepts();
      Iterator iter = d1Concepts.iterator();
      count = 0;
      while (iter.hasNext()) {
        Concept c = (Concept) iter.next();
        debug.add("Concept : " + c.getValue() + "\n");
        count = count + 1;
      }
      // check on North America expected number of descendents
      if (count == childConceptValue.length + 1) {
        pass1 = true;
      } else
        debug.add("Error: Did not find all descendents of North America\n");
      debug.add("Descendants of usConcept\n");

      Collection d2Concepts = usConcept.getDescendantConcepts();
      iter = d2Concepts.iterator();
      count = 0;
      while (iter.hasNext()) {
        Concept c = (Concept) iter.next();
        debug.add("Concept : " + c.getValue() + "\n");
        for (int i = 0; i < childConceptValue.length; i++) {
          if (c.getValue().equals(childConceptValue[i])) {
            count = count + 1;
            debug.add("Found a match " + childConceptValue[i] + "\n");
            break;
          }
        }
      }
      if (count == passCount) {
        pass2 = true;
      } else
        debug.add("Error: Did not find all descendents of the United States\n");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass2 = false;
    }

    if (!(pass1 && pass2))
      throw new Fault(testName + " failed ");
  } // end of concept_getDescendantConcepts

  /*
   * @testName: concept_getParentConceptScheme
   *
   * @assertion_ids: JAXR:JAVADOC:869
   *
   * @test_Strategy: Create a classification scheme Create a concept with a
   * reference to the parent ClassificationScheme. Verify that getParentConcept
   * returns null
   */
  public void concept_getParentConceptScheme() throws Fault {
    String testName = "concept_getParentConceptScheme";
    Concept concept = null;
    boolean pass = false;
    try {
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      // North America concept
      Concept naConcept = blm.createConcept(scheme, conceptName, conceptValue);
      if (naConcept.getParentConcept() == null) {
        pass = true;
      } else
        debug.add("Error: expected null parent\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of concept_getParentConceptScheme

  /*
   * @testName: concept_getParentConcept
   *
   * @assertion_ids: JAXR:JAVADOC:744
   *
   * @test_Strategy: Create a classification scheme Create a concept with a
   * reference to the parent ClassificationScheme. Create a child - US- for that
   * concept - NA Verify that getParentConcept returns the parent concept NA
   *
   */
  public void concept_getParentConcept() throws Fault {
    String testName = "concept_getParentConcept";
    Concept concept = null;
    boolean pass = false;
    try {
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      // North America concept
      Concept naConcept = blm.createConcept(scheme, conceptName, conceptValue);

      // United States Concept
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);
      naConcept.addChildConcept(usConcept);

      concept = usConcept.getParentConcept();
      debug.add("Expect NA concept value\n");
      if (concept.getValue().equals(conceptValue))
        pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of concept_getParentConcept

  /*
   * @testName: concept_removeChildConcept
   *
   * @assertion_ids: JAXR:JAVADOC:734
   *
   *
   * @test_Strategy: Create a North America concept, create a US concept. Add
   * the US concept to the North America concept. Verify the NA child concept
   * count is 1. Call removeChildConcept. Verify that the NA child concept count
   * is now 0.
   */
  public void concept_removeChildConcept() throws Fault {
    String testName = "concept_removeChildConcept";
    Concept concept = null;
    boolean pass = false;
    try {
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      // North America concept
      Concept naConcept = blm.createConcept(scheme, conceptName, conceptValue);

      // United States Concept
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);
      naConcept.addChildConcept(usConcept);
      // should have 1 child
      if (!(naConcept.getChildConceptCount() == 1))
        throw new Fault(
            testName + " did not complete.  Child count not valid\n");
      naConcept.removeChildConcept(usConcept);
      if (!(naConcept.getChildConceptCount() == 0))
        debug.add("Error: test failed to remove child concept\n");
      else
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of concept_removeChildConcept

  /*
   * @testName: concept_preDefinedEnumerationsObjectTypeTest
   *
   * @assertion_ids: JAXR:SPEC:289;
   *
   * @test_Strategy: Get the classification scheme for ObjectType. From the
   * scheme, verify the enumeration.
   *
   */
  public void concept_preDefinedEnumerationsObjectTypeTest() throws Fault {
    String testName = "concept_preDefinedEnumerationsObjectTypeTest";
    boolean pass = true;
    Concept concept = null;
    String[] ObjectTypes = { "CPP", "CPA", "Process", "WSDL", "Association",
        "AuditableEvent", "Classification", "Concept", "ExternalIdentifier",
        "ExternalLink", "ExtrinsicObject", "Organization", "Package", "Service",
        "ServiceBinding", "User" };
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      scheme = bqm.findClassificationSchemeByName(null, "ObjectType");
      // Parent
      debug.add(
          "Child concept count is " + scheme.getChildConceptCount() + "\n");
      // Collection concepts = scheme.getDescendantConcepts();
      Collection concepts = scheme.getChildrenConcepts();
      Iterator iter = concepts.iterator();
      debug.add("\n");
      debug.add("---------------------------------------------    \n");
      Collection values = new ArrayList();
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        values.add(concept.getValue());
      }
      debug.add("---------------------------------------------    \n");
      int findcount = 0;
      for (int i = 0; i < ObjectTypes.length; i++) {
        if (!(values.contains(ObjectTypes[i]))) {
          debug.add("Error: did not find this --> " + ObjectTypes[i] + "\n");
          pass = false;
        } else {
          debug.add("Found " + ObjectTypes[i] + "\n");
          findcount = findcount + 1;
        }
      }
      debug.add("Find count is " + findcount + "\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test
  /*
   * @testName: concept_preDefinedEnumerationsPhoneTypeTest
   *
   *
   * @assertion_ids: JAXR:SPEC:291;
   *
   * @test_Strategy: Get the classification scheme for PhoneType. From the
   * scheme, verify the enumeration.
   * 
   *
   */

  public void concept_preDefinedEnumerationsPhoneTypeTest() throws Fault {
    String testName = "concept_preDefinedEnumerationsPhoneTypeTest";
    boolean pass = true;
    Concept concept = null;
    String[] PhoneTypes = { "OfficePhone", "HomePhone", "MobilePhone", "Beeper",
        "FAX" };
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      scheme = bqm.findClassificationSchemeByName(null, "PhoneType");
      // Parent
      debug.add(
          "Child concept count is " + scheme.getChildConceptCount() + "\n");
      // Collection concepts = scheme.getDescendantConcepts();
      Collection concepts = scheme.getChildrenConcepts();
      Iterator iter = concepts.iterator();
      debug.add("\n");
      debug.add("---------------------------------------------    \n");
      Collection values = new ArrayList();
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        values.add(concept.getValue());
      }
      debug.add("---------------------------------------------    \n");
      int findcount = 0;
      for (int i = 0; i < PhoneTypes.length; i++) {
        if (!(values.contains(PhoneTypes[i]))) {
          debug.add("Error: did not find this --> " + PhoneTypes[i] + "\n");
          pass = false;
        } else
          findcount = findcount + 1;
      }
      debug.add("Find count is " + findcount + "\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test
  /*
   * @testName: concept_preDefinedEnumerationsAssociationTypeTest
   *
   *
   * @assertion_ids: JAXR:SPEC:292;
   *
   * @test_Strategy: Get the classification scheme for AssociationType. From the
   * scheme, verify the enumeration.
   * 
   * 
   *
   */

  public void concept_preDefinedEnumerationsAssociationTypeTest() throws Fault {
    String testName = "concept_preDefinedEnumerationsAssociationTypeTest";
    boolean pass = true;
    Concept concept = null;
    String[] AssociationTypes = { "RelatedTo", "HasChild", "HasMember",
        "HasParent", "ExternallyLinks", "Contains", "EquivalentTo", "Extends",
        "Implements", "InstanceOf", "Supersedes", "Uses", "Replaces",
        "ResponsibleFor", "SubmitterOf" };

    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      scheme = bqm.findClassificationSchemeByName(null, "AssociationType");
      // Parent
      debug.add(
          "Child concept count is " + scheme.getChildConceptCount() + "\n");
      Collection concepts = scheme.getChildrenConcepts();
      Iterator iter = concepts.iterator();
      debug.add("\n");
      debug.add("---------------------------------------------    \n");
      Collection values = new ArrayList();
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        values.add(concept.getValue());
      }
      debug.add("---------------------------------------------    \n");
      int findcount = 0;
      for (int i = 0; i < AssociationTypes.length; i++) {
        if (!(values.contains(AssociationTypes[i]))) {
          debug.add(
              "Error: did not find this --> " + AssociationTypes[i] + "\n");
          pass = false;
        } else
          findcount = findcount + 1;
      }
      debug.add("Find count is " + findcount + "\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test
  /*
   * @testName: concept_preDefinedEnumerationsURLTypeTest
   *
   *
   * @assertion_ids: JAXR:SPEC:293;
   *
   * @test_Strategy: Get the classification scheme for URLType. From the scheme,
   * verify the enumeration.
   * 
   * 
   *
   */

  public void concept_preDefinedEnumerationsURLTypeTest() throws Fault {
    String testName = "concept_preDefinedEnumerationsURLTypeTest";
    boolean pass = true;
    Concept concept = null;
    String[] URLTypes = { "HTTP", "HTTPS", "SMTP", "PHONE", "FAX", "OTHER" };
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      scheme = bqm.findClassificationSchemeByName(null, "URLType");
      // Parent
      debug.add(
          "Child concept count is " + scheme.getChildConceptCount() + "\n");
      Collection concepts = scheme.getChildrenConcepts();
      Iterator iter = concepts.iterator();
      debug.add("\n");
      debug.add("---------------------------------------------    \n");
      Collection values = new ArrayList();
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        values.add(concept.getValue());
      }
      debug.add("---------------------------------------------    \n");
      int findcount = 0;
      for (int i = 0; i < URLTypes.length; i++) {
        if (!(values.contains(URLTypes[i]))) {
          debug.add("Error: did not find this --> " + URLTypes[i] + "\n");
          pass = false;
        } else
          findcount = findcount + 1;
      }
      debug.add("Find count is " + findcount + "\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test
  /*
   * @testName: concept_preDefinedEnumerationsPostalAddressAttributesTest
   *
   *
   * @assertion_ids: JAXR:SPEC:294;
   *
   * @test_Strategy: Get the classification scheme for PostalAddressAttributes.
   * From the scheme, verify the enumeration.
   * 
   * 
   *
   */

  public void concept_preDefinedEnumerationsPostalAddressAttributesTest()
      throws Fault {
    String testName = "concept_preDefinedEnumerationsPostalAddressAttributesTest";
    boolean pass = true;
    Concept concept = null;
    String[] PostalAddressAttributes = { "StreetNumber", "Street", "City",
        "State", "PostalCode", "Country" };
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      scheme = bqm.findClassificationSchemeByName(null,
          "PostalAddressAttributes");
      // Parent
      debug.add(
          "Child concept count is " + scheme.getChildConceptCount() + "\n");
      // Collection concepts = scheme.getDescendantConcepts();
      Collection concepts = scheme.getChildrenConcepts();
      Iterator iter = concepts.iterator();
      debug.add("\n");
      debug.add("---------------------------------------------    \n");
      Collection values = new ArrayList();
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        values.add(concept.getValue());
      }
      debug.add("---------------------------------------------    \n");
      int findcount = 0;
      for (int i = 0; i < PostalAddressAttributes.length; i++) {
        if (!(values.contains(PostalAddressAttributes[i]))) {
          debug.add("Error: did not find this --> " + PostalAddressAttributes[i]
              + "\n");
          pass = false;
        } else
          findcount = findcount + 1;
      }
      debug.add("Find count is " + findcount + "\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test
  /*
   * @testName: concept_removeChildConcepts
   *
   * @assertion: removeChildConcepts
   *
   * @assertion_ids: JAXR:JAVADOC:736
   *
   * @test_Strategy: Create child concepts for "California","Alaska",
   * "Alabama","Arkansas","Arizona","Colorado", and "Florida" Verify the count.
   * Remove them. Verify there are none.
   *
   */

  public void concept_removeChildConcepts() throws Fault {
    String testName = "concept_removeChildConcepts";
    boolean pass = false;
    try {
      Concept childConcept[] = new Concept[childConceptName.length];
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      Collection concepts = new ArrayList();
      // Parent
      debug.add("Creating Parent Concept: " + conceptNameUS + "\n");
      Concept usConcept = blm.createConcept(scheme, conceptNameUS,
          conceptValueUS);

      // Children
      for (int i = 0; i < childConceptName.length; i++) {
        debug.add("Creating child Concept for : " + childConceptName[i] + "\n");
        childConcept[i] = blm.createConcept(scheme, childConceptName[i],
            childConceptValue[i]);
        concepts.add(childConcept[i]);
      }

      debug.add("Add children to Parent \n");
      usConcept.addChildConcepts(concepts);
      debug.add("Child count returned from getChildrenConcepts is "
          + usConcept.getChildConceptCount() + "\n");

      if (!(childConceptName.length == usConcept.getChildConceptCount()))
        throw new Fault(testName + "did not complete. Invalid child count\n");

      // remove all of the children
      debug.add("Removing all of the children\n");
      usConcept.removeChildConcepts(concepts);

      if (usConcept.getChildConceptCount() != 0) {
        debug.add("Error: child count is: " + usConcept.getChildConceptCount()
            + "\n");
        throw new Fault(testName + "failure to remove all children");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
  } // end of concept_removeChildConcepts

}
