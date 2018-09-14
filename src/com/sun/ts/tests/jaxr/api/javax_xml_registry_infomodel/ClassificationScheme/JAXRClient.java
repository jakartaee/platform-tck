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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.ClassificationScheme;

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

  String classificationName = "Book Publishers";

  String classificationValue = "51113";

  Classification classification = null;

  ClassificationScheme scheme = null;

  // String schemeName = "Geography";
  String schemeName = "CTSxxx";

  String schemeDescription = "North American Regions";

  String name = "classificationScheme";

  Concept concept = null;

  String conceptNameAK = "Alaska";

  String conceptValueAK = "US-AK";

  String conceptNameCA = "California";

  String conceptValueCA = "US-CA";

  String conceptNameUS = "United States";

  String conceptValueUS = "US";

  String conceptName = "North America";

  String conceptValue = "NA";

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
   * @testName: classificationScheme_isExternalTrueTest
   *
   * @assertion: isExternal - Returns true if this is an external
   * classificationScheme.
   *
   * @assertion_ids: JAXR:JAVADOC:769
   *
   * @test_Strategy: create an external classificationScheme. Verify isExternal
   * returns true.
   *
   *
   */
  public void classificationScheme_isExternalTrueTest() throws Fault {
    String testName = "classificationScheme_isExternalTrueTest";
    boolean pass = false;
    try {
      createExternalClassificationObject();
      debug.add("Create an external Classification \n");
      if (scheme.isExternal()) {
        pass = true;
        debug.add("Success! isExternal returned: " + scheme.isExternal());
      } else
        debug.add("FAIL: isExternal returned " + scheme.isExternal());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classificationScheme_isExternalTrueTest

  /*
   * @testName: classificationScheme_isExternalFalseTest
   *
   * @assertion: isExternal - Returns true if this is an external
   * classificationScheme.
   *
   *
   * @assertion_ids: JAXR:JAVADOC:770
   *
   *
   * @test_Strategy: create an internal classificationScheme. Verify isExternal
   * returns false.
   *
   *
   */
  public void classificationScheme_isExternalFalseTest() throws Fault {
    String testName = "classificationScheme_isExternalFalseTest";

    boolean pass = false;
    try {
      // create a Registry Object......................
      createInternalClassificationObject();
      debug.add("Created an internal Classification \n");
      if (!(scheme.isExternal())) {
        pass = true;
        debug.add("Success! isExternal returned: " + scheme.isExternal());
      } else
        debug.add("FAIL: isExternal returned " + scheme.isExternal());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classificationScheme_isExternalFalseTest

  /*
   * @testName: classificationScheme_addChildConceptTest
   *
   * @assertion: addChildConcept - Adds a child concept
   *
   * @assertion_ids: JAXR:JAVADOC:755
   *
   * @test_Strategy: add a child concept to classification scheme. Verify it
   * with getChildrenConcepts call.
   * 
   */
  public void classificationScheme_addChildConceptTest() throws Fault {
    String testName = "classificationScheme_addChildConceptTest";
    boolean pass = false;
    Concept childConcept = null;
    try {
      // create a Registry Object......................
      createInternalClassificationObject();
      // create a child concept
      childConcept = blm.createConcept(scheme, conceptNameAK, conceptValueAK);
      debug.add("Child concept count before adding a child is: "
          + scheme.getChildConceptCount() + "\n");
      scheme.addChildConcept(childConcept);
      // Verify the child was added.
      debug.add("Child concept count before adding a child is: "
          + scheme.getChildConceptCount() + "\n");
      Collection concepts = scheme.getChildrenConcepts();
      if (concepts == null)
        throw new Fault(testName + "Error- no concepts for getChildren!!");
      debug.add("Concept count is: " + concepts.size());
      Iterator iter = concepts.iterator();
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        // verify the child - there should only be one.
        if (concept.getValue().equals(conceptValueAK)) {
          pass = true;
        } else {
          debug.add("Error: unexpected child returned\n");
          debug.add("Childs value is " + concept.getValue() + "\n");
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        scheme.removeChildConcept(childConcept);
      } catch (JAXRException ex) {
        TestUtil.logErr("failed to remove ChildConcept: " + ex.getMessage());
        TestUtil.printStackTrace(ex);
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classificationScheme_addChildConceptTest

  /*
   * @testName: classificationScheme_removeChildConceptTest
   *
   * @assertion: removeChildConcept - removes a child concept
   *
   *
   * @assertion_ids: JAXR:JAVADOC:759
   *
   * @test_Strategy: create 2 child concepts. Remove one of them and verify.
   * 
   *
   */
  public void classificationScheme_removeChildConceptTest() throws Fault {
    String testName = "classificationScheme_removeChildConceptTest";
    boolean pass = false;
    Concept caConcept = null;
    try {
      // create a Registry Object......................
      createInternalClassificationObject();
      // create a child concept
      Concept akConcept = blm.createConcept(scheme, conceptNameAK,
          conceptValueAK);
      caConcept = blm.createConcept(scheme, conceptNameCA, conceptValueCA);
      Collection concepts = new ArrayList();
      concepts.add(akConcept);
      concepts.add(caConcept);

      debug.add("Child concept count before adding a child is: "
          + scheme.getChildConceptCount() + "\n");

      scheme.addChildConcepts(concepts);
      // Verify the child was added.

      debug.add("Child concept count after adding a 2 child concepts is: "
          + scheme.getChildConceptCount() + "\n");
      debug.add("remove 1 - Alaska , 1 should be left - California \n");
      scheme.removeChildConcept(akConcept);
      // verify that California is left.
      concepts = scheme.getChildrenConcepts();
      if (concepts == null)
        throw new Fault(testName + "Error- no concepts for getChildren!!");
      debug.add("Concept count is: " + concepts.size());
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
    } finally {
      try {
        scheme.removeChildConcept(caConcept);
      } catch (JAXRException ex) {
        TestUtil.logErr("failed to remove ChildConcept: " + ex.getMessage());
        TestUtil.printStackTrace(ex);
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classificationScheme_removeChildConceptTest

  /*
   * @testName: classificationScheme_removeChildConceptsTest
   *
   * @assertion: removeChildConcepts - remove a collection ofchildren concepts
   *
   * @assertion_ids: JAXR:JAVADOC:761
   *
   * @test_Strategy: Create an internal Classification. Add 2 child concepts.
   * Remove the 2 concepts. Verify that they have been removed.
   * 
   *
   */
  public void classificationScheme_removeChildConceptsTest() throws Fault {
    String testName = "classificationScheme_removeChildConceptsTest";
    boolean pass = true;
    Collection concepts = new ArrayList();

    try {
      // create a Registry Object......................
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      debug.add("The child count after createClassificationScheme is: "
          + scheme.getChildConceptCount() + "\n");

      // create a child concepts
      Concept akConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      akConcept.setName(blm.createInternationalString(conceptNameAK));
      akConcept.setValue(conceptValueAK);

      Concept caConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      caConcept.setName(blm.createInternationalString(conceptNameCA));
      caConcept.setValue(conceptValueCA);

      concepts.add(akConcept);
      concepts.add(caConcept);
      debug.add("The count before child concepts added to scheme is: "
          + scheme.getChildConceptCount() + "\n");
      scheme.addChildConcepts(concepts);
      // Verify the child was added.
      debug.add("Added 2 child concepts -for alaska and california \n");
      debug.add(" verify the count. The count is : "
          + scheme.getChildConceptCount() + "\n");
      // remove both
      scheme.removeChildConcepts(concepts);
      // verify that nothing is left.
      concepts = scheme.getChildrenConcepts();
      debug.add("Removed the 2 child concepts for alaska and california \n");
      debug.add("Concept count is: " + concepts.size());
      if (concepts.size() != 0)
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classificationScheme_removeChildConceptsTest

  /*
   * @testName: classificationScheme_addGetChildConceptsTest
   *
   * @assertion: addChildConcepts - Adds a collection of concept children
   * getChildrenConcepts - Get all immediate children Concepts.
   *
   *
   * @assertion_ids: JAXR:JAVADOC:757; JAXR:JAVADOC:765
   *
   *
   * @test_Strategy: Create child concepts for Alaska and California. Use
   * addChildConcepts method to add them to a scheme. Use the
   * getChildrenConcepts method to retrieve them back from scheme Verify that 2
   * child concepts for Alaska and California were retrieved.
   *
   */
  public void classificationScheme_addGetChildConceptsTest() throws Fault {
    String testName = "classificationScheme_addGetChildConceptsTest";
    boolean pass = false;
    Concept childConcept = null;
    Collection childConcepts = new ArrayList();

    try {
      // create a Registry Object......................
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);

      // create a children concepts
      // ==
      // create a child concepts
      Concept akConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      akConcept.setName(blm.createInternationalString(conceptNameAK));
      akConcept.setValue(conceptValueAK);
      childConcepts.add(akConcept);

      Concept caConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      caConcept.setName(blm.createInternationalString(conceptNameCA));
      caConcept.setValue(conceptValueCA);
      childConcepts.add(caConcept);
      // --
      debug.add("Children concept count before adding a child is: "
          + scheme.getChildConceptCount() + "\n");
      scheme.addChildConcepts(childConcepts);
      // Verify the children were added.
      debug.add("Add child concepts for Alaska and California to scheme \n");
      debug.add(
          "Verify the concept count. Scheme.getChildConceptCount() returns: "
              + scheme.getChildConceptCount() + "\n");
      Collection concepts = scheme.getChildrenConcepts();
      if (concepts == null)
        throw new Fault(testName + "Error- no concepts for getChildren!!");
      debug.add("Concept count returned from getChildrenConcepts is: "
          + concepts.size());
      Iterator iter = concepts.iterator();
      int childCount = 0;
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        // verify the child - there should two
        if (concept.getValue().equals(conceptValueAK)
            || concept.getValue().equals(conceptValueCA)) {
          childCount = childCount + 1;
        }
      }
      debug.add("Child concept count is: " + childCount + "\n");
      if (childCount == 2)
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        scheme.removeChildConcepts(childConcepts);
      } catch (JAXRException ex) {
        TestUtil.logErr("failed to remove ChildConcepts: " + ex.getMessage());
        TestUtil.printStackTrace(ex);
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classificationScheme_addGetChildConceptsTest

  /*
   * @testName: classificationScheme_getChildConceptCountTest
   *
   * @assertion: getChildConceptCount - Get number of children
   *
   *
   * @assertion_ids: JAXR:JAVADOC:763
   *
   * @test_Strategy: add to child concepts. Verify correct count is returned.
   *
   */
  public void classificationScheme_getChildConceptCountTest() throws Fault {
    String testName = "classificationScheme_getChildConceptCountTest";
    boolean pass = false;
    Collection childConcepts = new ArrayList();
    try {
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);

      // create a children concepts
      // ==
      Concept akConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      akConcept.setName(blm.createInternationalString(conceptNameAK));
      akConcept.setValue(conceptValueAK);
      childConcepts.add(akConcept);

      Concept caConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      caConcept.setName(blm.createInternationalString(conceptNameCA));
      caConcept.setValue(conceptValueCA);
      childConcepts.add(caConcept);
      // ==
      debug.add("Children concept count before adding a children is: "
          + scheme.getChildConceptCount() + "\n");
      scheme.addChildConcepts(childConcepts);
      // Verify the children were added.
      debug.add("getChildConceptCount after adding 2 children is "
          + scheme.getChildConceptCount() + "\n");
      debug.add("Child concept after adding a children is: "
          + scheme.getChildConceptCount() + "\n");
      if (scheme.getChildConceptCount() == 2)
        pass = true;
      else {
        debug.add("ChildConceptCount got " + scheme.getChildConceptCount());
        debug.add("ChildConceptCount expected " + 2);
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        scheme.removeChildConcepts(childConcepts);
      } catch (JAXRException ex) {
        TestUtil.logErr("failed to remove ChildConcepts: " + ex.getMessage());
        TestUtil.printStackTrace(ex);
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classificationScheme_getChildConceptCountTest

  /*
   * @testName: classificationScheme_getDescendantConceptsTest
   *
   * @assertion: addChildConcepts - Adds a collection of concept children
   * getChildrenConcepts - Get all immediate children Concepts.
   *
   * @assertion_ids: JAXR:JAVADOC:767;
   *
   *
   * @test_Strategy: Create a child - US. Create children AK and CA. Add AK and
   * CA child concepts to US. Add US to scheme. Verify that
   * scheme.getDescendantConcepts returns concepts for US, Ak and CA.
   *
   */
  public void classificationScheme_getDescendantConceptsTest() throws Fault {
    String testName = "classificationScheme_getDescendantConceptsTest";
    boolean pass = false;
    int testCount = 3;
    Collection childConcepts = new ArrayList();
    Concept usConcept = null;

    try {
      // create a Registry Object......................
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      // ==
      //
      // create a child
      usConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      usConcept.setName(blm.createInternationalString(conceptNameUS));
      usConcept.setValue(conceptValueUS);

      // create children
      Concept akConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      akConcept.setName(blm.createInternationalString(conceptNameAK));
      akConcept.setValue(conceptValueAK);

      Concept caConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      caConcept.setName(blm.createInternationalString(conceptNameCA));
      caConcept.setValue(conceptValueCA);

      // add children to US Concept so we will have descendents
      childConcepts.add(caConcept);
      childConcepts.add(akConcept);
      usConcept.addChildConcepts(childConcepts);
      scheme.addChildConcept(usConcept);
      // ==

      // Verify the descendents
      debug.add("Child concept after adding a children is: "
          + scheme.getChildConceptCount() + "\n");
      Collection concepts = scheme.getDescendantConcepts();
      if (concepts == null)
        throw new Fault(testName
            + "Error- no concepts returned from getDescendantConcepts!!");
      Iterator iter = concepts.iterator();
      int descendantCount = 0;
      while (iter.hasNext()) {
        concept = (Concept) iter.next();
        debug.add("Concept value is " + concept.getValue() + "\n");
        if (concept.getValue().equals(conceptValueAK)
            || concept.getValue().equals(conceptValueCA)
            || concept.getValue().equals(conceptValueUS)) {
          descendantCount = descendantCount + 1;
        }
      }
      if (descendantCount == testCount) {
        pass = true;
      } else {
        debug.add("Descendant count is " + descendantCount + "\n");
        debug.add("Expected Descendant count is " + testCount + "\n");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        scheme.removeChildConcepts(childConcepts);
        scheme.removeChildConcept(usConcept);
      } catch (JAXRException ex) {
        TestUtil.logErr("failed to remove ChildConcepts: " + ex.getMessage());
        TestUtil.printStackTrace(ex);
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classificationScheme_getDescendantConceptsTest

  /*
   * @testName: classificationScheme_getValueTypeTest
   *
   * @assertion_ids: JAXR:SPEC:23;
   *
   * @test_Strategy: Level 0 providers must throw an UnsupportedCapability
   * exception when this method is called.
   *
   */
  public void classificationScheme_getValueTypeTest() throws Fault {
    String testName = "classificationScheme_getValueTypeTest";
    boolean pass = false;
    Concept childConcept = null;
    int providerlevel = 0;
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add(
          "Level 0 Providers must throw an UnsupportedCapabilityException for getValueType\n");
      debug.add(
          "This provider report a Capability Level of " + providerlevel + "\n");

      // create a Registry Object......................
      createInternalClassificationObject();
      // create a child concept
      childConcept = blm.createConcept(scheme, conceptNameAK, conceptValueAK);
      scheme.addChildConcept(childConcept);
      int value = scheme.getValueType();
      if (providerlevel > 0)
        pass = true;
    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        debug.add(
            " Call to getValueType threw UnsupportedCapabilityException as expected\n");
        pass = true;
      } else
        throw new Fault(
            testName
                + " unexpected UnsupportedCapabilityException was caught \n",
            uc);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        scheme.removeChildConcept(childConcept);
      } catch (JAXRException ex) {
        TestUtil.logErr("failed to remove ChildConcepts: " + ex.getMessage());
        TestUtil.printStackTrace(ex);
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

  /*
   * @testName: classificationScheme_setValueTypeTest
   *
   * @assertion_ids: JAXR:SPEC:24;
   *
   * @test_Strategy: Level 0 providers must throw an UnsupportedCapability
   * exception when this method is called.
   *
   */
  public void classificationScheme_setValueTypeTest() throws Fault {
    String testName = "classificationScheme_setValueTypeTest";
    Concept childConcept = null;
    int providerlevel = 0;
    boolean pass = true;
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add(
          "Level 0 Providers must throw an UnsupportedCapabilityException for setValueType\n");
      debug.add(
          "This provider report a Capability Level of " + providerlevel + "\n");

      // create a Registry Object......................
      createInternalClassificationObject();
      // create a child concept
      childConcept = blm.createConcept(scheme, conceptNameAK, conceptValueAK);
      scheme.addChildConcept(childConcept);
      scheme.setValueType(ClassificationScheme.VALUE_TYPE_UNIQUE);
      // if provider is > 0, no exception should be thrown.
      if (providerlevel == 0)
        throw new Fault(testName
            + " UnsupportedCapabilityException was not thrown by level 0 provider");

      if (scheme.getValueType() != ClassificationScheme.VALUE_TYPE_UNIQUE) {
        debug.add("Did not returned expected value \n");
        pass = false;
      } else
        debug.add(
            "Returned expected value - ClassificationScheme.VALUE_TYPE_UNIQUE\n");

      scheme.setValueType(ClassificationScheme.VALUE_TYPE_EMBEDDED_PATH);
      if (scheme
          .getValueType() != ClassificationScheme.VALUE_TYPE_EMBEDDED_PATH) {
        debug.add("Did not returned expected value \n");
        pass = false;
      } else
        debug.add(
            "Returned expected value - ClassificationScheme.VALUE_TYPE_EMBEDDED_PATH\n");

      scheme.setValueType(ClassificationScheme.VALUE_TYPE_NON_UNIQUE);
      if (scheme.getValueType() != ClassificationScheme.VALUE_TYPE_NON_UNIQUE) {
        debug.add("Did not returned expected value \n");
        pass = false;
      } else
        debug.add(
            "Returned expected value - ClassificationScheme.VALUE_TYPE_NON_UNIQUE\n");

    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        debug.add(
            " Call to getValueType threw UnsupportedCapabilityException as expected\n");
        pass = true;
      } else
        throw new Fault(
            testName
                + " unexpected UnsupportedCapabilityException was caught \n",
            uc);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    } finally {
      try {
        scheme.removeChildConcept(childConcept);
      } catch (JAXRException ex) {
        TestUtil.logErr("failed to remove ChildConcepts: " + ex.getMessage());
        TestUtil.printStackTrace(ex);
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

  //
  // helper method

  private void createExternalClassificationObject() throws JAXRException {
    scheme = null;
    classification = null;
    // create a ClassificationScheme
    scheme = blm.createClassificationScheme(schemeName, schemeDescription);
    // create an external Classification
    classification = blm.createClassification(scheme, classificationName,
        classificationValue);
    classification.setClassificationScheme(scheme);
  } // end of createExternalClassificationObject

  //
  private void createInternalClassificationObject() throws JAXRException {
    scheme = blm.createClassificationScheme(schemeName, schemeDescription);
    // create a concept for the internal classification
    concept = blm.createConcept(scheme, conceptName, conceptValue);
    // create an internal Classification
    classification = blm.createClassification(concept);
  } // end of createExternalClassificationObject

}
