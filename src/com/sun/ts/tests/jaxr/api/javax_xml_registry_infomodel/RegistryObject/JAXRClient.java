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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.RegistryObject;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.*;

import com.sun.javatest.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

public class JAXRClient extends JAXRCommonClient {
  Locale tsLocale = new Locale("en", "US");

  BusinessQueryManager bqm = null;

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
      super.cleanUpRegistry(); //
      debug.clear();
      bqm = rs.getBusinessQueryManager();

    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    try {
      super.cleanup();
      // super.cleanUpRegistry();
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
   * @testName: registryObject_getSetKeyTest
   *
   * @assertion_ids: JAXR:JAVADOC:450;JAXR:JAVADOC:460;
   *
   * @test_Strategy: Get a key from PhoneType. Set the key Call getKey and
   * verify it matches what was set.
   *
   */
  public void registryObject_getSetKeyTest() throws Fault {
    String testName = "registryObject_getSetKeyTest";
    Key schemeKey = null;
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      ClassificationScheme scheme = bqm.findClassificationSchemeByName(null,
          "PhoneType");
      if (scheme == null) {
        debug.add("Error: unable to find PhoneType \n");
        throw new Fault(testName
            + "failed - test did not complete - unable to find classification scheme ");
      }
      schemeKey = scheme.getKey();
      String newKey = "urn:uuid:de111111-a1e1-11a1-abcc-ee6d11111111";
      Key newOne = blm.createKey(newKey);
      scheme.setKey(newOne);
      if (scheme.getKey().getId().equals(newKey))
        debug.add("good! ");
      else
        throw new Fault(
            testName + " Error: get returned did not match get that was set ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_DescriptionTest
   *
   * @assertion_ids: JAXR:JAVADOC:452; JAXR:JAVADOC:454
   *
   * @test_Strategy: getDescription - Gets the textual description for this
   * object. setDescription - Sets the context independent textual description
   * for this object. Create a RegistryObject - Organization - set the
   * description of the organization by calling RegistryObject.setDescription.
   * Verify the description by calling RegistryObject.getDescription.
   *
   */
  public void registryObject_DescriptionTest() throws Fault {
    String testName = "registryObject_DescriptionTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String orgDescription = "Organization for RegistryObject.getDescription Test";
    try {
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      InternationalString iOrgDescription = blm
          .createInternationalString(tsLocale, orgDescription);
      org.setDescription(iOrgDescription);
      if (org.getDescription().getValue(tsLocale).equals(orgDescription)) {
        debug.add("getDescription returned correct description: "
            + org.getDescription().getValue(tsLocale) + "\n");
      } else {
        debug.add(
            "Unexpected description returned from org.getDescription() \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_getNameTest
   *
   * @assertion_ids: JAXR:JAVADOC:456;
   *
   * @test_Strategy: getName - Gets user friendly name of this object. Create an
   * organization. Call getName on the organization. Verify that the name
   * returned matches the name passed to createOrganization.
   *
   */
  public void registryObject_getNameTest() throws Fault {
    String testName = "registryObject_getNameTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      if (org.getName().getValue(tsLocale).equals(orgName)) {
        debug.add("getName returned the correct name: "
            + org.getName().getValue(tsLocale) + "\n");
      } else {
        debug.add("Unexpected name returned from org.getName() \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_setNameTest
   *
   * @assertion_ids: JAXR:JAVADOC:458;
   *
   * @test_Strategy: setName - Sets user friendly name of object in repository.
   * Create an organization. Call setName to set a name. Verify that the name
   * returned by getName matches the set name.
   *
   */
  public void registryObject_setNameTest() throws Fault {
    String testName = "registryObject_setNameTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String oldName = "This is the old Name";
    try {
      InternationalString ioldName = blm.createInternationalString(tsLocale,
          oldName);
      Organization org = blm.createOrganization(ioldName);

      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      org.setName(iorgName);

      if (org.getName().getValue(tsLocale).equals(orgName)) {
        debug.add("getName returned the correct name: "
            + org.getName().getValue(tsLocale) + "\n");
      } else {
        debug.add("Unexpected name returned from org.getName() \n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_addClassificationTest
   *
   * @assertion_ids: JAXR:JAVADOC:464;
   *
   * @test_Strategy: addClassification - Adds specified Classification to this
   * object. Create an Organization RegistryObject. Create a classification for
   *
   */
  public void registryObject_addClassificationTest() throws Fault {
    String schemeName = "Geography";
    String schemeDescription = "North American Regions";
    String conceptNameUS = "United States";
    String conceptValueUS = "US";

    String testName = "registryObject_addClassificationTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);

      // create a ClassificationScheme
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      Concept concept = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameUS),
          conceptValueUS);

      Classification classification = blm.createClassification(concept);
      org.addClassification(classification);
      Collection cls = org.getClassifications();
      debug.add(
          "Expect number of Classifications returned from getClassifications to be 1\n");
      debug.add("getClassifications returned: " + cls.size() + "\n");
      if (cls.size() != 1)
        throw new Fault(testName + " failed ");
      Iterator iter = cls.iterator();
      Classification cl = null;
      while (iter.hasNext()) {
        cl = (Classification) iter.next();
      }
      Concept con = cl.getConcept();
      debug.add("Verify classification concept name and value\n");
      debug.add("expected name is " + conceptNameUS + "\n");
      debug.add("name is " + con.getName().getValue(tsLocale) + "\n");
      debug.add("expected value is " + conceptValueUS + "\n");
      debug.add("value is " + con.getValue() + "\n");

      if (!(con.getName().getValue(tsLocale).equals(conceptNameUS)))
        throw new Fault(testName + " failed  - Concept Name did not match");

      if (!(con.getValue().equals(conceptValueUS)))
        throw new Fault(testName + " failed  - Concept Value did not match");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_addClassificationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:466; JAXR:JAVADOC:474;
   * JAXR:SPEC:117;JAXR:SPEC:118; JAXR:SPEC:127;
   *
   * @test_Strategy: addClassifications - Adds specified Classification to this
   * object. Create an Organization RegistryObject. Create a classification for
   *
   */
  public void registryObject_addClassificationsTest() throws Fault {
    String schemeName = "Geography";
    String schemeDescription = "North American Regions";

    String conceptNameAZ = "Arizona";
    String conceptValueAZ = "US-AZ";
    String conceptNameCO = "Colorado";
    String conceptValueCO = "US-CO";
    String testName = "registryObject_addClassificationsTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);

      // create a ClassificationScheme
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      Concept conceptAZ = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameAZ),
          conceptValueAZ);
      Concept conceptCO = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameCO),
          conceptValueCO);

      Classification clAZ = blm.createClassification(conceptAZ);
      Classification clCO = blm.createClassification(conceptCO);
      Collection classifications = new ArrayList();
      classifications.add(clAZ);
      classifications.add(clCO);

      org.addClassifications(classifications);
      Collection cls = org.getClassifications();
      debug.add(
          "Expect number of Classifications returned from getClassifications to be 2 \n");
      debug.add("getClassifications returned: " + cls.size() + "\n");
      if (cls.size() != 2)
        throw new Fault(testName + " failed ");
      Iterator iter = cls.iterator();
      Classification cl = null;
      Concept con = null;
      int count = 0;
      while (iter.hasNext()) {
        cl = (Classification) iter.next();
        con = cl.getConcept();
        if ((con.getName().getValue(tsLocale).equals(conceptNameAZ))
            && (con.getValue().equals(conceptValueAZ))) {
          debug.add("Found: " + con.getName().getValue(tsLocale) + " and "
              + con.getValue() + "\n");
          count = count + 1;
        }
        if ((con.getName().getValue(tsLocale).equals(conceptNameCO))
            && (con.getValue().equals(conceptValueCO))) {
          debug.add("Found: " + con.getName().getValue(tsLocale) + " and "
              + con.getValue() + "\n");
          count = count + 1;
        }
      }

      if (count != 2)
        throw new Fault(testName + " failed ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_removeClassificationTest
   *
   * @assertion_ids: JAXR:JAVADOC:468
   *
   * @test_Strategy: removeClassification -
   * 
   *
   */
  public void registryObject_removeClassificationTest() throws Fault {
    String schemeName = "Geography";
    String schemeDescription = "North American Regions";
    String conceptNameUS = "United States";
    String conceptValueUS = "US";

    String testName = "registryObject_removeClassificationTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);

      // create a ClassificationScheme
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      Concept concept = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameUS),
          conceptValueUS);

      Classification classification = blm.createClassification(concept);
      org.addClassification(classification);
      Collection cls = org.getClassifications();
      debug.add(
          "Expect number of Classifications returned from getClassifications to be 1\n");
      debug.add("getClassifications returned: " + cls.size() + "\n");
      if (cls.size() != 1)
        throw new Fault(testName + " failed ");
      Iterator iter = cls.iterator();
      Classification cl = null;
      while (iter.hasNext()) {
        cl = (Classification) iter.next();
      }
      Concept con = cl.getConcept();
      debug.add("Verify classification concept name and value\n");
      debug.add("expected name is " + conceptNameUS + "\n");
      debug.add("name is " + con.getName().getValue(tsLocale) + "\n");
      debug.add("expected value is " + conceptValueUS + "\n");
      debug.add("value is " + con.getValue() + "\n");
      // Verify the addition
      if (!(con.getName().getValue(tsLocale).equals(conceptNameUS)))
        throw new Fault(testName + " failed  - Concept Name did not match");

      if (!(con.getValue().equals(conceptValueUS)))
        throw new Fault(testName + " failed  - Concept Value did not match");
      debug.add("Verified classification was added.  Now remove it\n");

      org.removeClassification(classification);
      debug.add(
          "Removed the classification. getClassifications should return 0\n");
      cls = org.getClassifications();

      debug.add("getClassifications returned " + cls.size() + "\n");
      if (cls.size() != 0)
        throw new Fault(testName + " failed ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_removeClassificationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:470
   *
   * @test_Strategy: removeClassifications -
   * 
   *
   */
  public void registryObject_removeClassificationsTest() throws Fault {
    String schemeName = "Geography";
    String schemeDescription = "North American Regions";
    String conceptNameAZ = "Arizona";
    String conceptValueAZ = "US-AZ";
    String conceptNameCO = "Colorado";
    String conceptValueCO = "US-CO";
    String testName = "registryObject_removeClassificationsTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);

      // create a ClassificationScheme
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);
      Concept conceptAZ = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameAZ),
          conceptValueAZ);
      Concept conceptCO = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameCO),
          conceptValueCO);

      Classification clAZ = blm.createClassification(conceptAZ);
      Classification clCO = blm.createClassification(conceptCO);
      Collection classifications = new ArrayList();
      classifications.add(clAZ);
      classifications.add(clCO);

      org.addClassifications(classifications);
      Collection cls = org.getClassifications();
      debug.add(
          "Expect number of Classifications returned from getClassifications to be 2 \n");
      debug.add("getClassifications returned: " + cls.size() + "\n");
      if (cls.size() != 2)
        throw new Fault(testName + " failed ");
      Iterator iter = cls.iterator();
      Classification cl = null;
      Concept con = null;
      int count = 0;
      while (iter.hasNext()) {
        cl = (Classification) iter.next();
        con = cl.getConcept();
        if ((con.getName().getValue(tsLocale).equals(conceptNameAZ))
            && (con.getValue().equals(conceptValueAZ))) {
          debug.add("Found: " + con.getName().getValue(tsLocale) + " and "
              + con.getValue() + "\n");
          count = count + 1;
        }
        if ((con.getName().getValue(tsLocale).equals(conceptNameCO))
            && (con.getValue().equals(conceptValueCO))) {
          debug.add("Found: " + con.getName().getValue(tsLocale) + " and "
              + con.getValue() + "\n");
          count = count + 1;
        }
      }
      if (count != 2)
        throw new Fault(testName + " failed ");

      debug.add("Added Classifications and verified - now remove them \n");
      org.removeClassifications(cls);

      cls = org.getClassifications();
      debug.add(
          "Removed classifications. getClassifications should return 0 classifications \n");
      debug.add("getClassifications returned: " + cls.size() + "\n");

      if (cls.size() != 0)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_setClassificationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:472
   *
   * @test_Strategy: Add classifications for Arizona and Colorado to an
   * Organization Call setClassifications with classifications for Alaska and
   * California. Verify that Alaska and California replaced Arizona and
   * Colorado. Create an Organization RegistryObject. Create a classification
   * for
   *
   */
  public void registryObject_setClassificationsTest() throws Fault {
    String schemeName = "Geography";
    String schemeDescription = "North American Regions";

    String conceptNameAZ = "Arizona";
    String conceptValueAZ = "US-AZ";
    String conceptNameCO = "Colorado";
    String conceptValueCO = "US-CO";
    String conceptNameCA = "California";
    String conceptValueCA = "US-CA";
    String conceptNameAK = "Alaska";
    String conceptValueAK = "US-AK";

    String testName = "registryObject_setClassificationsTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);

      // create a ClassificationScheme
      ClassificationScheme scheme = blm.createClassificationScheme(schemeName,
          schemeDescription);

      // Create concepts for Arizona and Colorado Alaska and California
      Concept conceptAZ = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameAZ),
          conceptValueAZ);
      Concept conceptCO = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameCO),
          conceptValueCO);
      Concept conceptAK = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameAK),
          conceptValueAK);
      Concept conceptCA = blm.createConcept(scheme,
          blm.createInternationalString(tsLocale, conceptNameCA),
          conceptValueCA);

      // Create classifications for for Arizona and Colorado Alaska and
      // California concepts
      Classification clAZ = blm.createClassification(conceptAZ);
      Classification clCO = blm.createClassification(conceptCO);
      Classification clAK = blm.createClassification(conceptAK);
      Classification clCA = blm.createClassification(conceptCA);

      // Collection for original classifications
      Collection classifications = new ArrayList();
      classifications.add(clAZ);
      classifications.add(clCO);

      // Collection for classifications that will replace the origanals
      Collection newClassifications = new ArrayList();
      newClassifications.add(clAK);
      newClassifications.add(clCA);

      org.addClassifications(classifications);
      Collection cls = org.getClassifications();
      debug.add(
          "Expect number of Classifications returned from getClassifications to be 2 \n");
      debug.add("getClassifications returned: " + cls.size() + "\n");
      if (cls.size() != 2)
        throw new Fault(testName + " failed ");
      Iterator iter = cls.iterator();
      Classification cl = null;
      Concept con = null;
      int count = 0;
      while (iter.hasNext()) {
        cl = (Classification) iter.next();
        con = cl.getConcept();
        if ((con.getName().getValue(tsLocale).equals(conceptNameAZ))
            && (con.getValue().equals(conceptValueAZ))) {
          debug.add("Found: " + con.getName().getValue(tsLocale) + " and "
              + con.getValue() + "\n");
          count = count + 1;
        }
        if ((con.getName().getValue(tsLocale).equals(conceptNameCO))
            && (con.getValue().equals(conceptValueCO))) {
          debug.add("Found: " + con.getName().getValue(tsLocale) + " and "
              + con.getValue() + "\n");
          count = count + 1;
        }
      }
      if (count != 2)
        throw new Fault(testName + " failed - test did not complete!");

      debug
          .add("Added and verified classifications for Arizona and Colorado\n");
      debug.add(
          "Now replace them with classifications for Alaska and California \n");
      // ==
      org.setClassifications(newClassifications);
      // ==
      debug.add("Verify the replacement\n");

      // ==
      cls = org.getClassifications();
      debug.add(
          "Expect number of Classifications returned from getClassifications to be 2 \n");
      debug.add("getClassifications returned: " + cls.size() + "\n");
      if (cls.size() != 2)
        throw new Fault(testName + " failed ");
      iter = cls.iterator();
      cl = null;
      con = null;
      count = 0;
      while (iter.hasNext()) {
        cl = (Classification) iter.next();
        con = cl.getConcept();
        if ((con.getName().getValue(tsLocale).equals(conceptNameAK))
            && (con.getValue().equals(conceptValueAK))) {
          debug.add("Found: " + con.getName().getValue(tsLocale) + " and "
              + con.getValue() + "\n");
          count = count + 1;
        }
        if ((con.getName().getValue(tsLocale).equals(conceptNameCA))
            && (con.getValue().equals(conceptValueCA))) {
          debug.add("Found: " + con.getName().getValue(tsLocale) + " and "
              + con.getValue() + "\n");
          count = count + 1;
        }
      }
      if (count != 2)
        throw new Fault(testName + " failed ");
      // ==

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_addExternalIdentifierTest
   *
   * @assertion_ids: JAXR:JAVADOC:492;
   *
   * @test_Strategy: addExternalIdentifier
   *
   */
  public void registryObject_addExternalIdentifierTest() throws Fault {
    String testName = "registryObject_addExternalIdentifierTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";
    String orgName = "Test";
    try {
      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("Create an ExternalIdentifier for the registryobject \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(tsLocale, name);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, iName,
          value);
      debug.add("add the ExternalIdentifier to the organization \n");
      org.addExternalIdentifier(ei);
      debug.add("Verify the addition of the ExternalIdentifier \n");
      Collection eis = org.getExternalIdentifiers();
      debug.add(
          "getExternalIdentifiers method should return 1 ExternalIdentifier \n");
      debug.add("getExternalIdentifiers returned: " + eis.size()
          + " ExternalIdentifier \n");
      if (eis.size() != 1)
        throw new Fault(testName + " failed ");

      debug.add("Value expected from external identifier: " + value + "\n");

      Iterator iter = eis.iterator();
      while (iter.hasNext()) {
        ExternalIdentifier e = (ExternalIdentifier) iter.next();
        debug.add(
            "Value returned from external identifier: " + e.getValue() + "\n");
        if (!(e.getValue().equals(value)))
          throw new Fault(testName + " failed ");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_addExternalIdentifiersTest
   *
   * @assertion_ids: JAXR:JAVADOC:494; JAXR:JAVADOC:502
   *
   * @test_Strategy: Create an organization. Create externalIdentifiers for
   * multiple social security numbers and add them to the organization with the
   * addExternalIdentifiers method. Retrieve the added external identifiers with
   * the getExternalIdentifiers method and verify expected number returned and
   * values are correct.
   *
   *
   */
  public void registryObject_addExternalIdentifiersTest() throws Fault {
    String testName = "registryObject_addExternalIdentifiersTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value[] = { "026-10-5738", "032-09-3030", "025-15-2511" };
    String orgName = "Test";
    try {
      ArrayList values = new ArrayList();
      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("Create an ExternalIdentifiers for the registryobject \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(tsLocale, name);
      Collection eis = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        eis.add(blm.createExternalIdentifier(scheme, iName, value[i]));
        values.add(value[i]);
      }

      debug.add("add the ExternalIdentifieris to the organization \n");
      // =
      org.addExternalIdentifiers(eis);
      // =
      debug.add("Verify the addition of the ExternalIdentifiers \n");
      eis = null;
      // =
      eis = org.getExternalIdentifiers();
      // =

      debug.add("getExternalIdentifiers method should return " + value.length
          + " ExternalIdentifiers \n");
      debug.add("getExternalIdentifiers returned: " + eis.size()
          + " ExternalIdentifier \n");
      if (eis.size() != value.length)
        throw new Fault(testName + " failed ");

      debug.add("Values expected from external identifier: \n");
      for (int i = 0; i < value.length; i++) {
        debug.add("          value = " + value[i] + " \n");
      }

      debug.add("Values returned from external identifier: \n");
      Iterator iter = eis.iterator();
      int count = 0;
      while (iter.hasNext()) {
        ExternalIdentifier e = (ExternalIdentifier) iter.next();
        debug.add("          value = " + e.getValue() + "\n");
        if (values.contains(e.getValue()))
          count = count + 1;
      }
      if (count != value.length)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_removeExternalIdentifierTest
   *
   * @assertion_ids: JAXR:JAVADOC:496;
   *
   * @test_Strategy: removeExternalIdentifier
   *
   */
  public void registryObject_removeExternalIdentifierTest() throws Fault {
    String testName = "registryObject_removeExternalIdentifierTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";
    String orgName = "Test";
    try {
      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("Create an ExternalIdentifier for the registryobject \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(tsLocale, name);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, iName,
          value);
      debug.add("add the ExternalIdentifier to the organization \n");
      org.addExternalIdentifier(ei);
      debug.add("Verify the addition of the ExternalIdentifier \n");
      Collection eis = org.getExternalIdentifiers();
      debug.add(
          "getExternalIdentifiers method should return 1 ExternalIdentifier \n");
      debug.add("getExternalIdentifiers returned: " + eis.size()
          + " ExternalIdentifier \n");
      if (eis.size() != 1)
        throw new Fault(testName + " failed ");

      debug.add("Value expected from external identifier: " + value + "\n");
      ExternalIdentifier e = null;
      Iterator iter = eis.iterator();
      while (iter.hasNext()) {
        e = (ExternalIdentifier) iter.next();
        debug.add(
            "Value returned from external identifier: " + e.getValue() + "\n");
        if (!(e.getValue().equals(value)))
          throw new Fault(testName + " failed - test did not complete!");
      }
      debug.add("remove the external identifier from the registryobject\n");
      org.removeExternalIdentifier(e);
      eis = org.getExternalIdentifiers();
      debug.add(
          "getExternalIdentifiers method should return no ExternalIdentifiers \n");
      debug.add("getExternalIdentifiers returned: " + eis.size()
          + " ExternalIdentifiers \n");
      if (eis.size() != 0)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_removeExternalIdentifiersTest
   *
   * @assertion_ids: JAXR:JAVADOC:498
   *
   * @test_Strategy:
   * 
   */
  public void registryObject_removeExternalIdentifiersTest() throws Fault {
    String testName = "registryObject_removeExternalIdentifiersTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value[] = { "026-10-5738", "032-09-3030", "025-15-2511" };
    String orgName = "Test";
    try {
      ArrayList values = new ArrayList();
      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("Create an ExternalIdentifiers for the registryobject \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(tsLocale, name);
      Collection eis = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        eis.add(blm.createExternalIdentifier(scheme, iName, value[i]));
        values.add(value[i]);
      }

      debug.add("add the ExternalIdentifieris to the organization \n");
      // =
      org.addExternalIdentifiers(eis);
      // =
      debug.add("Verify the addition of the ExternalIdentifiers \n");
      eis = null;
      // =
      eis = org.getExternalIdentifiers();
      // =

      debug.add("getExternalIdentifiers method should return " + value.length
          + " ExternalIdentifiers \n");
      debug.add("getExternalIdentifiers returned: " + eis.size()
          + " ExternalIdentifier \n");
      if (eis.size() != value.length)
        throw new Fault(testName + " failed ");

      debug.add("Values expected from external identifier: \n");
      for (int i = 0; i < value.length; i++) {
        debug.add("          value = " + value[i] + " \n");
      }

      debug.add("Values returned from external identifier: \n");
      Iterator iter = eis.iterator();
      int count = 0;
      while (iter.hasNext()) {
        ExternalIdentifier e = (ExternalIdentifier) iter.next();
        debug.add("          value = " + e.getValue() + "\n");
        if (values.contains(e.getValue()))
          count = count + 1;
      }
      if (count != value.length)
        throw new Fault(testName + " failed ");

      debug.add("Added and verified external identifiers - now remove them\n");
      org.removeExternalIdentifiers(eis);
      eis = org.getExternalIdentifiers();
      debug.add(
          "getExternalIdentifiers method should return no ExternalIdentifiers \n");
      debug.add("getExternalIdentifiers returned: " + eis.size()
          + " ExternalIdentifiers \n");
      if (eis.size() != 0)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_setExternalIdentifiersTest
   *
   * @assertion_ids: JAXR:JAVADOC:500
   *
   * @test_Strategy:
   *
   */
  public void registryObject_setExternalIdentifiersTest() throws Fault {
    String testName = "registryObject_setExternalIdentifiersTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value[] = { "026-10-5738", "032-09-3030", "025-15-2511" };
    String newValue[] = { "029-10-5744", "030-09-3930", "028-15-2211",
        "020-19-2299" };

    String orgName = "Test";
    try {
      ArrayList values = new ArrayList();
      ArrayList newValues = new ArrayList();

      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("Create ExternalIdentifiers for the registryobject \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(tsLocale, name);
      Collection eis = new ArrayList();
      for (int i = 0; i < value.length; i++) {
        eis.add(blm.createExternalIdentifier(scheme, iName, value[i]));
        values.add(value[i]);
      }
      Collection newEis = new ArrayList();
      for (int i = 0; i < newValue.length; i++) {
        newEis.add(blm.createExternalIdentifier(scheme, iName, newValue[i]));
        newValues.add(newValue[i]);
      }

      debug.add("add the ExternalIdentifieris to the organization \n");
      // =
      org.addExternalIdentifiers(eis);
      // =
      debug.add("Verify the addition of the ExternalIdentifiers \n");
      eis = null;
      // =
      eis = org.getExternalIdentifiers();
      // =

      debug.add("getExternalIdentifiers method should return " + value.length
          + " ExternalIdentifiers \n");
      debug.add("getExternalIdentifiers returned: " + eis.size()
          + " ExternalIdentifier \n");
      if (eis.size() != value.length)
        throw new Fault(testName + " failed ");

      debug.add("Values expected from external identifier: \n");
      for (int i = 0; i < value.length; i++) {
        debug.add("          value = " + value[i] + " \n");
      }

      debug.add("Values returned from external identifier: \n");
      Iterator iter = eis.iterator();
      int count = 0;
      while (iter.hasNext()) {
        ExternalIdentifier e = (ExternalIdentifier) iter.next();
        debug.add("          value = " + e.getValue() + "\n");
        if (values.contains(e.getValue()))
          count = count + 1;
      }
      if (count != value.length)
        throw new Fault(testName + " failed ");
      // ==
      debug.add("Added and verified external identifiers - now replace them\n");
      org.setExternalIdentifiers(newEis);
      eis = org.getExternalIdentifiers();
      debug.add("getExternalIdentifiers method should now return "
          + newValue.length + " ExternalIdentifiers \n");
      debug.add("getExternalIdentifiers returned: " + eis.size()
          + " ExternalIdentifiers \n");
      if (eis.size() != newValue.length)
        throw new Fault(testName + " failed ");

      debug.add("New external identifier values are:  \n");
      for (int i = 0; i < newValue.length; i++) {
        debug.add("          new value = " + newValue[i] + " \n");
      }
      debug.add("Verify the replacement\n");

      iter = eis.iterator();
      count = 0;
      while (iter.hasNext()) {
        ExternalIdentifier e = (ExternalIdentifier) iter.next();
        debug.add("          value = " + e.getValue() + "\n");
        if (newValues.contains(e.getValue()))
          count = count + 1;
      }
      if (count != newValue.length)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_addExternalLinkTest
   *
   * @assertion_ids: JAXR:JAVADOC:504
   *
   * @test_Strategy: addExternalLink
   *
   */
  public void registryObject_addExternalLinkTest() throws Fault {
    String testName = "registryObject_addExternalLinkTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String externalURI = baseuri + "jaxrTestPage1.html";
    String description = "JAXR Test Page";

    try {
      debug.add("Create an external link \n");
      ExternalLink el = blm.createExternalLink(externalURI, description);

      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("add the external link to the organization \n");
      org.addExternalLink(el);
      debug.add("Verify the addition of the external link \n");
      Collection els = org.getExternalLinks();
      debug.add("getExternalLinks method should return 1 ExternalLink \n");
      debug
          .add("getExternalLinks returned: " + els.size() + " ExternalLink \n");
      if (els.size() != 1)
        throw new Fault(testName + " failed ");

      debug.add("URI expected from external link: " + externalURI + "\n");

      Iterator iter = els.iterator();
      while (iter.hasNext()) {
        ExternalLink e = (ExternalLink) iter.next();
        debug.add(
            "URI returned from external link: " + e.getExternalURI() + "\n");
        if (!(e.getExternalURI().equals(externalURI)))
          throw new Fault(testName + " failed ");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_addExternalLinksTest
   *
   * @assertion_ids: JAXR:JAVADOC:506; JAXR:JAVADOC:514
   *
   * @test_Strategy: addExternalLinks
   *
   */
  public void registryObject_addExternalLinksTest() throws Fault {
    String testName = "registryObject_addExternalLinksTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String description = "home page";
    String[] link = { baseuri + "jaxrTestPage1.html",
        baseuri + "jaxrTestPage2.html", baseuri + "jaxrTestPage3.html" };
    try {
      ArrayList links = new ArrayList();
      Collection els = new ArrayList();
      debug.add("Create an external link \n");
      for (int i = 0; i < link.length; i++) {
        els.add(blm.createExternalLink(link[i], description));
        links.add(link[i]);
      }
      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("add the external links to the organization \n");
      org.addExternalLinks(els);
      debug.add("Verify the addition of the external links \n");
      els = org.getExternalLinks();
      debug.add("getExternalLinks method should return " + link.length
          + " ExternalLinks \n");
      debug.add(
          "getExternalLinks returned: " + els.size() + " ExternalLinks \n");
      if (els.size() != link.length)
        throw new Fault(testName + " failed ");

      debug.add("Expecting the following links from the registryobject\n");
      for (int i = 0; i < link.length; i++) {
        debug.add("          link: " + link[i] + "\n");
      }
      Iterator iter = els.iterator();
      int count = 0;
      while (iter.hasNext()) {
        ExternalLink e = (ExternalLink) iter.next();
        debug.add("found link: " + e.getExternalURI() + "\n");
        if (links.contains(e.getExternalURI()))
          count = count + 1;
      }
      if (count != link.length)
        throw new Fault(testName + " failed ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_removeExternalLinkTest
   *
   * @assertion_ids: JAXR:JAVADOC:508
   *
   * @test_Strategy: removeExternalLink
   *
   */
  public void registryObject_removeExternalLinkTest() throws Fault {
    String testName = "registryObject_removeExternalLinkTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String externalURI = baseuri + "jaxrTestPage1.html";
    String description = "JavaSoft home page";

    try {
      debug.add("Create an external link \n");
      ExternalLink el = blm.createExternalLink(externalURI, description);

      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("add the external link to the organization \n");
      org.addExternalLink(el);
      debug.add("Verify the addition of the external link \n");
      Collection els = org.getExternalLinks();
      debug.add("getExternalLinks method should return 1 ExternalLink \n");
      debug
          .add("getExternalLinks returned: " + els.size() + " ExternalLink \n");
      if (els.size() != 1)
        throw new Fault(testName + " failed ");

      debug.add("URI expected from external link: " + externalURI + "\n");

      Iterator iter = els.iterator();
      ExternalLink e = null;
      while (iter.hasNext()) {
        e = (ExternalLink) iter.next();
        debug.add(
            "URI returned from external link: " + e.getExternalURI() + "\n");
        if (!(e.getExternalURI().equals(externalURI)))
          throw new Fault(testName + " failed ");
      }
      debug.add("Added and verified external link, now remove it \n");
      org.removeExternalLink(e);
      els = org.getExternalLinks();
      debug.add("Link removed.  Link count should be 0 \n");
      debug.add("Link count is " + els.size() + "\n");
      if (els.size() != 0)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_removeExternalLinksTest
   *
   * @assertion_ids: JAXR:JAVADOC:510
   *
   * @test_Strategy: removeExternalLinks
   *
   */
  public void registryObject_removeExternalLinksTest() throws Fault {
    String testName = "registryObject_removeExternalLinksTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String description = "home page";
    String[] link = { baseuri + "jaxrTestPage1.html",
        baseuri + "jaxrTestPage2.html", baseuri + "jaxrTestPage3.html" };

    try {
      ArrayList links = new ArrayList();
      Collection els = new ArrayList();
      debug.add("Create an external link \n");
      for (int i = 0; i < link.length; i++) {
        els.add(blm.createExternalLink(link[i], description));
        links.add(link[i]);
      }
      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("add the external links to the organization \n");
      org.addExternalLinks(els);
      debug.add("Verify the addition of the external links \n");
      els = org.getExternalLinks();
      debug.add("getExternalLinks method should return " + link.length
          + " ExternalLinks \n");
      debug.add(
          "getExternalLinks returned: " + els.size() + " ExternalLinks \n");
      if (els.size() != link.length)
        throw new Fault(testName + " failed ");

      debug.add("Expecting the following links from the registryobject\n");
      for (int i = 0; i < link.length; i++) {
        debug.add("          link: " + link[i] + "\n");
      }
      Iterator iter = els.iterator();
      int count = 0;
      while (iter.hasNext()) {
        ExternalLink e = (ExternalLink) iter.next();
        debug.add("found link: " + e.getExternalURI() + "\n");
        if (links.contains(e.getExternalURI()))
          count = count + 1;
      }
      if (count != link.length)
        throw new Fault(testName + " failed ");
      debug.add("Added and verified links.  Now remove them \n");
      org.removeExternalLinks(els);
      debug.add("Removed the links.  Number of links should be 0 \n");
      els = org.getExternalLinks();
      debug.add("getExternal links returns: " + els.size() + "\n");
      if (els.size() != 0)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_setExternalLinksTest
   *
   * @assertion_ids: JAXR:JAVADOC:512
   *
   * @test_Strategy: Create 2 collections of external links. Add the first
   * collection to to a test organization and verify by checking the count and
   * the URI strings. Invoke the setExternalLinks method to replace the first
   * collection with a new one. Verify by checking the link count and the URI
   * strings.
   *
   */
  public void registryObject_setExternalLinksTest() throws Fault {
    String testName = "registryObject_setExternalLinksTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String description = "home page";
    String[] link = { baseuri + "jaxrTestPage1.html" };
    String[] newLink = { baseuri + "jaxrTestPage2.html",
        baseuri + "jaxrTestPage3.html" };

    try {
      ArrayList links = new ArrayList();
      ArrayList newLinks = new ArrayList();

      Collection els = new ArrayList();
      Collection newEls = new ArrayList();

      debug.add("Create a external links \n");
      for (int i = 0; i < link.length; i++) {
        // these are the original links
        els.add(blm.createExternalLink(link[i], description));
        links.add(link[i]);
      }
      for (int i = 0; i < newLink.length; i++) {
        // these will be the replacement links.
        newEls.add(blm.createExternalLink(newLink[i], description));
        newLinks.add(newLink[i]);
      }

      debug.add("Create an organization registryObject \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(orgName));

      debug.add("add the external links to the organization \n");
      org.addExternalLinks(els);
      debug.add("Verify the addition of the external links \n");
      els = org.getExternalLinks();
      debug.add("getExternalLinks method should return " + link.length
          + " ExternalLinks \n");
      debug.add(
          "getExternalLinks returned: " + els.size() + " ExternalLinks \n");
      if (els.size() != link.length)
        throw new Fault(testName + " failed ");

      debug.add("Expecting the following links from the registryobject\n");
      for (int i = 0; i < link.length; i++) {
        debug.add("          link: " + link[i] + "\n");
      }
      Iterator iter = els.iterator();
      int count = 0;
      while (iter.hasNext()) {
        ExternalLink e = (ExternalLink) iter.next();
        debug.add("found link: " + e.getExternalURI() + "\n");
        if (links.contains(e.getExternalURI()))
          count = count + 1;
      }
      if (count != link.length)
        throw new Fault(testName + " failed ");
      debug.add(
          "Added and verified links.  Now replace them with the setExternalLinksTest method.\n");

      org.setExternalLinks(newEls);
      debug.add("Replaced the links.  Number of links should now be "
          + newLink.length + "\n");
      els = org.getExternalLinks();
      debug.add("getExternal links returns: " + els.size() + "\n");
      // if (els.size() != newLink.length ) throw new Fault(testName + " failed
      // ");

      debug.add("Now check the replacements \n");
      debug.add("Expecting the following links from the registryobject\n");
      for (int i = 0; i < newLink.length; i++) {
        debug.add("          link: " + newLink[i] + "\n");
      }
      iter = els.iterator();
      count = 0;
      while (iter.hasNext()) {
        ExternalLink e = (ExternalLink) iter.next();
        debug.add("found link: " + e.getExternalURI() + "\n");
        if (newLinks.contains(e.getExternalURI()))
          count = count + 1;
      }
      if (count != newLink.length)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_addAssociationTest
   *
   * @assertion_ids: JAXR:JAVADOC:478;
   *
   * @test_Strategy: Add an association to an organization. Verify it replaces
   * the source object with Association.getSourceObject
   *
   *
   */
  public void registryObject_addAssociationTest() throws Fault {
    String testName = "registryObject_addAssociationTest";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String orgSourceName = "ThisIsTheSourceOrganization";
    try {
      Organization target = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSourceName));

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/Implements");
      Association a = blm.createAssociation(target, associationType);

      // adds specified Association to use this object as source.
      // Silently replaces the sourceObject in Association with
      // reference to this object.
      source.addAssociation(a);

      // Gets the Object that is the source of this Association.
      Organization o = (Organization) a.getSourceObject();
      debug.add("source object returned is " + o.getName().getValue(tsLocale));
      if (!(o.getName().getValue(tsLocale)
          .equals(source.getName().getValue(tsLocale))))
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_addAssociationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:480;
   *
   * @test_Strategy: Create an organization - the source org. Create multiple
   * associations and associate them with multiple target organizations. Use
   * addAssociations method to add the collection of associations to the source
   * organization. Verify each association in the collection has the correct
   * soure organization
   *
   *
   */
  public void registryObject_addAssociationsTest() throws Fault {
    String testName = "registryObject_addAssociationsTest";
    String orgSourceName = "ThisIsTheSourceOrganization";
    String orgTargetName = "ThisIsTargetOrganization_0";
    int count = 5;
    try {
      // Create multiple target organizations
      Collection targets = new ArrayList();
      for (int i = 0; i < count; i++) {
        targets.add(blm.createOrganization(
            blm.createInternationalString(tsLocale, orgTargetName + i)));
      }
      // create one source organization
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSourceName));

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/EquivalentTo");
      // Create a collection of Associations with the organization targets
      Collection as = new ArrayList();
      Iterator iter = targets.iterator();
      while (iter.hasNext()) {
        as.add(
            blm.createAssociation((Organization) iter.next(), associationType));
      }

      // addAssociations adds specified Associations to use this object as
      // source.
      // Silently replaces the sourceObject in Associations with
      // reference to this object.
      source.addAssociations(as);

      // Gets the Object that is the source of this Association.
      iter = as.iterator();

      Organization o = null;
      Association a = null;
      while (iter.hasNext()) {
        a = (Association) iter.next();
        o = (Organization) a.getSourceObject();
        debug.add("Found " + o.getName().getValue(tsLocale) + "\n");
        if (!(o.getName().getValue(tsLocale)
            .equals(source.getName().getValue(tsLocale))))
          throw new Fault(testName + " failed ");
        debug.add("And the target is: "
            + ((Organization) a.getTargetObject()).getName().getValue(tsLocale)
            + "\n");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_removeAssociationTest
   *
   * @assertion_ids: JAXR:JAVADOC:482;
   *
   * @test_Strategy: Add an association to an organization. Remove it. Verify
   * the remove.
   *
   */
  public void registryObject_removeAssociationTest() throws Fault {
    String testName = "registryObject_removeAssociationTest";
    String orgTargetName = "ThisIsTheTargetOrganization";
    String orgSourceName = "ThisIsTheSourceOrganization";
    try {
      Organization target = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgTargetName));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSourceName));

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/SubmitterOf");
      Association a = blm.createAssociation(target, associationType);
      source.addAssociation(a);

      // Gets the Object that is the source of this Association.
      Organization o = (Organization) a.getSourceObject();
      debug.add(
          "source object returned is " + o.getName().getValue(tsLocale) + "\n");
      if (!(o.getName().getValue(tsLocale)
          .equals(source.getName().getValue(tsLocale)))) {
        debug.add("Error: source organization not returned as expected \n");
        throw new Fault(testName + " test did not complete!");
      } else
        debug.add("good source object returned \n");
      // Removes the specified Association from this object.
      source.removeAssociation(a);
      // Verify the removal.
      Collection associations = source.getAssociations();
      if (!(associations.contains(a))) {
        debug.add("Association was removed - good!\n");
      } else {
        debug.add("Error: expected my association to be removed \n");
        throw new Fault(testName + " failed ");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_removeAssociationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:484;
   *
   * @test_Strategy: Create an organization - the source org. Create multiple
   * associations and associate them with multiple target organizations. Use
   * addAssociations method to add the collection of associations to the source
   * organization. Verify each association in the collection has the correct
   * soure organization Remove all the associations from the source object and
   * verify
   *
   */
  public void registryObject_removeAssociationsTest() throws Fault {
    String testName = "registryObject_removeAssociationsTest";
    String orgSourceName = "ThisIsTheSourceOrganization";
    String orgTargetName = "ThisIsTargetOrganization_0";
    int count = 10;
    try {
      // Create multiple target organizations
      Collection targets = new ArrayList();
      for (int i = 0; i < count; i++) {
        targets.add(blm.createOrganization(
            blm.createInternationalString(tsLocale, orgTargetName + i)));
      }
      // create one source organization
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSourceName));
      Concept associationType = bqm
          .findConceptByPath("/AssociationType/ResponsibleFor");
      // Create a collection of Associations with the organization targets
      Collection as = new ArrayList();
      Iterator iter = targets.iterator();
      while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Organization) {
          Organization o = (Organization) obj;
          as.add(blm.createAssociation(o, associationType));
        }
      }

      // addAssociations adds specified Associations to use this object as
      // source.
      // Silently replaces the sourceObject in Associations with
      // reference to this object.
      source.addAssociations(as);
      // Gets the Object that is the source of this Association.
      iter = as.iterator();
      Organization o = null;
      Association a = null;
      while (iter.hasNext()) {
        a = (Association) iter.next();
        o = (Organization) a.getSourceObject();
        debug.add("Found " + o.getName().getValue(tsLocale) + "\n");
        if (!(o.getName().getValue(tsLocale)
            .equals(source.getName().getValue(tsLocale))))
          throw new Fault(testName
              + "Error: source not as expected.  test did not complete!");
        debug.add("And the target is: "
            + ((Organization) a.getTargetObject()).getName().getValue(tsLocale)
            + "\n");
      }
      // remove the associations
      source.removeAssociations(as);
      // Verify the removal.
      Collection associations = source.getAssociations();
      // all of my association objects should have been removed
      iter = associations.iterator();
      while (iter.hasNext()) {
        Object obj = iter.next();
        // check that each association is removed.
        if (obj instanceof Association) {
          a = (Association) obj;
          if (as.contains(a)) {
            debug.add("Found one of the removed associations");
            throw new Fault(testName
                + " Error: removeAssociations did not remove all of the assocations !");
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: registryObject_getSetAssociationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:486;JAXR:JAVADOC:488;
   *
   * @test_Strategy: Create 2 collections of associations. Add the first group
   * of associations to the source organization. Use the setAssociation method
   * to replace the first collection of associations with the second collection.
   * Use the getAssociation method to verify the replacement.
   *
   */
  public void registryObject_getSetAssociationsTest() throws Fault {
    String testName = "registryObject_getSetAssociationsTest";
    String orgSourceName = "ThisIsTheSourceOrganization";
    String orgTargetName = "ThisIsTargetOrganization_0";
    int count = 10;
    boolean pass = true;
    try {
      // Create multiple target organizations
      Collection targets = new ArrayList();
      for (int i = 0; i < count; i++) {
        targets.add(blm.createOrganization(
            blm.createInternationalString(tsLocale, orgTargetName + i)));
      }
      // Create another set of targets organizations for replacement
      // associations
      Collection replacementTargets = new ArrayList();
      for (int i = count + 1; i < count + 11; i++) {
        replacementTargets.add(blm.createOrganization(
            blm.createInternationalString(tsLocale, orgTargetName + i)));
      }

      // create one source organization
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSourceName));

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/Supersedes");
      // Create a collection of Associations with the organization targets
      Collection as = new ArrayList();
      Iterator iter = targets.iterator();
      while (iter.hasNext()) {
        as.add(
            blm.createAssociation((Organization) iter.next(), associationType));
      }

      // addAssociations adds specified Associations to use this object as
      // source.
      // Add the first collection of associations to the source organization
      source.addAssociations(as);

      // Use setAssocaiotn method to replace the first collection associations
      Collection replacementAssociations = new ArrayList();
      iter = replacementTargets.iterator();
      while (iter.hasNext()) {
        replacementAssociations.add(
            blm.createAssociation((Organization) iter.next(), associationType));
      }
      source.setAssociations(replacementAssociations);
      // Verify the replacement with the getAssociation method.
      Collection currentAssociations = source.getAssociations();

      if (currentAssociations.size() != count)
        pass = false;
      debug.add("Expecting " + count + " associations to be returned  \n");
      debug.add("Number of associations returned is "
          + currentAssociations.size() + "\n");
      if (currentAssociations.containsAll(replacementAssociations)) {
        debug.add(
            "good! getAssociations returned all of the expected associations!\n");
      } else {
        debug.add(
            "Error! getAssociations did not return all of the expected associations!\n");
        iter = currentAssociations.iterator();
        Organization o = null;
        Association a = null;
        debug.add("Replacement Associations returned.....................\n");
        while (iter.hasNext()) {
          a = (Association) iter.next();
          debug.add("Target: "
              + ((Organization) a.getTargetObject()).getName().getValue()
              + "\n");
        }
        debug.add("Replacement Associations expected.....................\n");
        iter = replacementAssociations.iterator();
        while (iter.hasNext()) {
          a = (Association) iter.next();
          debug.add("Target: " + ((Organization) a.getTargetObject()).getName()
              .getValue(tsLocale) + "\n");
        }
        pass = false;

      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
    if (pass != true)
      throw new Fault(testName + " failed ");
  } // end of method

} // end of class
