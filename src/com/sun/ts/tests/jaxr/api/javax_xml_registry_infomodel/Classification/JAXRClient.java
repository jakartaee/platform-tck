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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.Classification;

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

  Organization org = null;

  String classificationName = "Book Publishers";

  String classificationValue = "51113";

  ClassificationScheme scheme = null;

  Classification classification = null;

  String schemeName = "NAICS";

  String schemeDescription = "North American Industry Classification System";

  String name = "classificationScheme";

  Concept concept = null;

  String conceptName = "NAICS";

  String conceptValue = "51";

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
   * @testName: classification_setgetValueTest
   *
   * @assertion_ids: JAXR:JAVADOC:786;JAXR:JAVADOC:784;JAXR:SPEC:123;
   *
   * @test_Strategy: set the taxonomy element value with the
   * Classification.setValue method. Then verify with getValue.
   * 
   *
   */
  public void classification_setgetValueTest() throws Fault {
    String testName = "classification_setgetValueTest";
    boolean pass = false;
    try {
      // create a Registry Object......................
      createExternalClassificationObject();

      // set the taxonomy for this classification
      classification.setValue(classificationValue);
      // verify correct value is returned.

      if (classification.getValue().equals(classificationValue)) {
        pass = true;
        debug.add(
            "Success! setValue was returned: " + classification.getValue());
      } else
        debug.add("FAIL: getValue returned " + classification.getValue());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classification_setgetValueTest

  /*
   * @testName: classification_getValueTest
   *
   * @assertion_ids: JAXR:JAVADOC:871
   * 
   * @assertion: getValue - This should return the value of the Concept
   * representing the taxonomy element when the Classification is an internal
   * Classification
   *
   *
   * @test_Strategy: create an internal Classification Call the getValue method
   * and verify it represents the taxonomy element.
   *
   *
   */
  public void classification_getValueTest() throws Fault {
    String testName = "classification_getValueTest";
    boolean pass = false;
    try {
      createInternalClassificationObject();

      debug.add("Expecting value = " + conceptValue + "\n");
      debug.add("returned value = " + classification.getValue() + "\n");
      if (classification.getValue().equals(conceptValue))
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classification_getValueTest

  /*
   * @testName: classification_isExternalTrueTest
   *
   * @assertion_ids: JAXR:JAVADOC:792
   *
   *
   * @test_Strategy: create an external classification. Verify isExternal
   * returns true.
   *
   *
   */
  public void classification_isExternalTrueTest() throws Fault {
    String testName = "classification_isExternalTrueTest";
    boolean pass = false;
    try {
      // create a Registry Object......................
      createExternalClassificationObject();
      classification.setValue(classificationValue);
      debug.add("Create an external Classification \n");
      if (classification.isExternal()) {
        pass = true;
        debug.add(
            "Success! isExternal returned: " + classification.isExternal());
      } else
        debug.add("FAIL: isExternal returned " + classification.isExternal());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classification_isExternalTrueTest

  /*
   * @testName: classification_isExternalFalseTest
   *
   * @assertion_ids: JAXR:JAVADOC:793
   *
   * @test_Strategy: create an internal classification. Verify isExternal
   * returns false
   *
   */
  public void classification_isExternalFalseTest() throws Fault {
    String testName = "classification_isExternalFalseTest";
    boolean pass = false;
    try {
      // create a Registry Object......................
      createInternalClassificationObject();
      debug.add("Created an internal Classification \n");
      if (!(classification.isExternal())) {
        pass = true;
        debug.add(
            "Success! isExternal returned: " + classification.isExternal());
      } else
        debug.add("FAIL: isExternal returned " + classification.isExternal());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classification_isExternalFalseTest

  /*
   * @testName: classification_getClassificationSchemeTest
   *
   *
   * @assertion_ids:
   * JAXR:JAVADOC:780;JAXR:JAVADOC:782;JAXR:SPEC:119;JAXR:SPEC:124;
   *
   * @test_Strategy: create an external classification. Set the classification
   * scheme with setClassificationScheme. Verify the ClassificationScheme set is
   * returned by checking the name.
   *
   *
   */
  public void classification_getClassificationSchemeTest() throws Fault {
    String testName = "classification_getClassificationSchemeTest";
    boolean pass = false;
    ClassificationScheme scheme = null;
    try {
      // create a Registry Object......................
      createExternalClassificationObject();
      scheme = classification.getClassificationScheme();
      // verify the scheme returned.
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
  } // end of classification_getClassificationSchemeTest

  /*
   * @testName: classification_setgetClassifiedObjectTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:788;JAXR:JAVADOC:790
   *
   *
   * @test_Strategy: create an external classification. set the classification
   * to the default organization. Verify with getClassifiedObject
   *
   *
   */
  public void classification_setgetClassifiedObjectTest() throws Fault {
    String testName = "classification_setgetClassifiedObjectTest";
    boolean pass = false;
    try {
      // create a Registry Object......................
      createExternalClassificationObject();
      classification.setClassifiedObject(org);
      RegistryObject ro = classification.getClassifiedObject();
      if (!(ro instanceof Organization)) {
        pass = false;
        throw new Fault("Did not get back an Organization as expected!");
      }
      if (ro.getName().getValue(tsLocale)
          .equals(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME)) {
        pass = true;
        debug.add("Correct Classified object returned: "
            + ro.getName().getValue(tsLocale) + "\n");
      } else
        debug.add("Error: did not get back correct Classified object\n");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classification_setgetClassifiedObjectTest

  /*
   * @testName: classification_getConcept
   *
   * @assertion_ids: JAXR:JAVADOC:776
   *
   * @test_Strategy: create an internal classification call getConcept method an
   * verify the value matches the value set with the createClassification call
   *
   */
  public void classification_getConcept() throws Fault {
    String testName = "classification_getConcept";
    boolean pass = false;
    concept = null;
    try {
      createInternalClassificationObject();
      concept = classification.getConcept();
      debug.add("concept returned value = " + concept.getValue());
      if (concept.getValue().equals(conceptValue))
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classification_getConcept

  /*
   * @testName: classification_setConcept
   *
   * @assertion: setConcept - Sets the concept for this classification. This
   * method is called when defining a Classification using a
   * ClassificationScheme whose structure is internal to the registry.
   *
   * @assertion_ids: JAXR:JAVADOC:778;JAXR:SPEC:122;
   *
   * @test_Strategy: Use the setConcept method to set a concept Verify with the
   * getConcept method
   *
   *
   */
  public void classification_setConcept() throws Fault {
    String testName = "classification_setConcept";
    boolean pass = false;
    concept = null;
    String conceptName = "NAICS";
    String conceptValue = "51";
    try {
      createInternalClassificationObject();
      // create a Classification scheme
      scheme = blm.createClassificationScheme(schemeName, schemeDescription);
      // Create a new concept
      concept = blm.createConcept(scheme, conceptName, conceptValue);
      classification.setConcept(concept);

      concept = classification.getConcept();
      debug.add("Expecting concept value = " + conceptValue + "\n");

      debug.add("concept returned value = " + concept.getValue());
      if (concept.getValue().equals(conceptValue))
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of classification_setConcept

  //
  // helper method

  private void createExternalClassificationObject() throws JAXRException {

    // create a Registry Object......................
    org = JAXR_Util.createDefaultOrganization(blm);
    // create a ClassificationScheme
    scheme = blm.createClassificationScheme(schemeName, schemeDescription);
    scheme.setName(blm.createInternationalString(tsLocale, name));
    // create an external Classification
    classification = blm.createClassification(scheme, classificationName,
        classificationValue);
    classification.setClassificationScheme(scheme);
  } // end of createExternalClassificationObject

  //
  //
  //
  private void createInternalClassificationObject() throws JAXRException {
    // create a Registry Object......................
    org = JAXR_Util.createDefaultOrganization(blm);
    // create a ClassificationScheme
    scheme = blm.createClassificationScheme(schemeName, schemeDescription);
    scheme.setName(blm.createInternationalString(tsLocale, name));

    // create a concept for the internal classification
    concept = blm.createConcept(scheme,
        blm.createInternationalString(tsLocale, conceptName), conceptValue);

    // create an internal Classification
    classification = blm.createClassification(concept);
  } // end of createExternalClassificationObject

}
