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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.Association;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.HashSet;
import java.util.Set;

import java.net.PasswordAuthentication;

import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;

public class JAXRClient extends JAXRCommonClient {
  Locale tsLocale = new Locale("en", "US");

  // for extramural Associations
  RegistryService rs2 = null;

  BusinessLifeCycleManager blm2 = null;

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
   */
  public void setup(String[] args, Properties p) throws Fault {
    try {
      super.setup(args, p);
      debug.clear();
      bqm = rs.getBusinessQueryManager();

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
   * @testName: association_getAssociationType
   *
   * @assertion_ids: JAXR:JAVADOC:817;JAXR:SPEC:130;
   *
   * @test_Strategy: Create an Association with type = Uses. Verify that
   * getAssociationType returns the correct value.
   *
   */
  public void association_getAssociationType() throws Fault {
    String testName = "association_getAssociationType";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String type = "RelatedTo";
    try {
      Organization target = blm
          .createOrganization(blm.createInternationalString(orgName));

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/RelatedTo");

      Association a = blm.createAssociation(target, associationType);
      Concept retType = a.getAssociationType();
      debug.add("getAssociationType returned : " + retType.getValue() + "\n");
      if (retType.getValue().equals(type))
        debug.add("value returned as expected \n");
      else
        throw new Fault(testName + " unexpected value returned ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: association_setAssociationType
   *
   * @assertion_ids: JAXR:JAVADOC:819;
   *
   * @test_Strategy: Use Association method setAssociationType to set the type.
   * Verify that getAssociationType returns the correct value.
   *
   */
  public void association_setAssociationType() throws Fault {
    String testName = "association_setAssociationType";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String type = "HasMember";
    try {

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/HasMember");

      Association a = (Association) blm.createObject(blm.ASSOCIATION);
      a.setAssociationType(associationType);
      Concept retType = a.getAssociationType();
      debug.add("getAssociationType returned : " + retType.getValue() + "\n");
      if (retType.getValue().equals(type))
        debug.add("value returned as expected \n");
      else
        throw new Fault(testName + " unexpected value returned ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: association_setSourceObject
   *
   * @assertion_ids: JAXR:JAVADOC:811; JAXR:JAVADOC:809;
   *
   * @test_Strategy: Create an organization and an Association Object. Use
   * setSourceObject to set the source . Verify with getSourceObject
   *
   */
  public void association_setSourceObject() throws Fault {
    String testName = "association_setSourceObject";
    String orgTarget = "Target Org";
    String orgSource = "Source Org";
    String type = "RelatedTo";
    try {

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/RelatedTo");
      if (associationType == null)
        throw new Fault(
            testName + " failed to find an Association type - RelatedTo ");

      // this is source blm
      Organization target = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgTarget));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSource));

      Association association = blm.createAssociation(target, associationType);
      debug.add("target is"
          + association.getTargetObject().getName().getValue(tsLocale) + "\n");

      association.setSourceObject(source);

      debug.add("Organization name from getSourceObject is "
          + association.getSourceObject().getName().getValue(tsLocale) + "\n");
      if (!(association.getSourceObject().getName().getValue(tsLocale)
          .equals(orgSource))) {
        throw new Fault(testName + " failed ");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: association_getTargetObject
   *
   * @assertion_ids: JAXR:JAVADOC:813;
   *
   * @test_Strategy: Create an organization and an Association Object. Call
   * getTargetObject and verify that the target matches what was passed to
   * createAssociation
   *
   */
  public void association_getTargetObject() throws Fault {
    String testName = "association_getTargetObject";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String type = "RelatedTo";
    try {

      Organization target = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/RelatedTo");
      if (associationType == null)
        throw new Fault(testName
            + " findConceptByPath failed to return /AssociationType/RelatedTo");

      Association a = blm.createAssociation(target, associationType);
      debug.add("Expected this Organization name for the target object "
          + orgName + "\n");
      debug.add("Organization name from getTargetObject is "
          + a.getTargetObject().getName().getValue(tsLocale) + "\n");
      if (!(a.getTargetObject().getName().getValue(tsLocale).equals(orgName))) {
        throw new Fault(testName + " failed ");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: association_setTargetObject
   *
   * @assertion_ids: JAXR:JAVADOC:815;
   *
   * @test_Strategy: Create an organization and an Association Object. Invoke
   * setTargetObject on the Association. Verify with a call to getTargetObject
   *
   */
  public void association_setTargetObject() throws Fault {
    String testName = "association_setTargetObject";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String type = "RelatedTo";
    try {

      Organization target = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      Concept associationType = bqm
          .findConceptByPath("/AssociationType/RelatedTo");

      if (associationType == null)
        throw new Fault(testName
            + " findConceptByPath failed to return /AssociationType/RelatedTo");
      Association a = (Association) blm
          .createObject(LifeCycleManager.ASSOCIATION);
      a.setAssociationType(associationType);
      a.setTargetObject(target);
      debug.add("Expected this Organization name for the target object "
          + orgName + "\n");
      debug.add("Organization name from getTargetObject is "
          + a.getTargetObject().getName().getValue(tsLocale) + "\n");
      if (!(a.getTargetObject().getName().getValue(tsLocale).equals(orgName))) {
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: association_isConfirmed
   *
   * @assertion_ids: JAXR:JAVADOC:829;JAXR:SPEC:131;JAXR:SPEC:139;
   *
   * @test_Strategy: isConfirmed Determines whether an Association has been
   * confirmed by the owner of the target object. This is a positive test.
   * Create an Association, a source and a target with the same owner. This is
   * an intramural association - confirmed. Verify that isConfirmed returns
   * true.
   *
   */
  public void association_isConfirmed() throws Fault {
    String testName = "association_isConfirmed";
    String orgTarget = "Target Org";
    String orgSource = "Source Org";
    String aType = "/AssociationType/RelatedTo";
    try {
      Organization target = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgTarget));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSource));
      debug.add("Created target and source organizations \n");

      Concept associationConcept = bqm.findConceptByPath(aType);
      if (associationConcept == null)
        throw new Fault(testName
            + " findConceptByPath failed to return /AssociationType/RelatedTo");

      Association a = blm.createAssociation(target, associationConcept);
      a.setSourceObject(source);
      debug.add("This is an intramural association and does not require\n");
      debug.add("explicit confirmation step. \n");
      debug.add("Expect isConfirmed to be true\n");
      debug.add("isConfirmed is " + a.isConfirmed());
      if (!(a.isConfirmed()))
        throw new Fault(testName + " failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(" failed ");
    }
  } // end of method

  /*
   * @testName: association_isConfirmedBySourceOwner
   *
   * @assertion_ids: JAXR:JAVADOC:823;
   *
   * @test_Strategy: For intramural Associations always return Create an
   * Association, a source and a target with the same owner. This is an
   * intramural association - confirmed. Verify that isConfirmedBySourceOwner
   * returns true.
   *
   */
  public void association_isConfirmedBySourceOwner() throws Fault {
    String testName = "association_isConfirmedBySourceOwner";
    String orgTarget = "Target Org";
    String orgSource = "Source Org";
    String aType = "/AssociationType/RelatedTo";
    try {
      Organization target = blm
          .createOrganization(blm.createInternationalString(orgTarget));
      Organization source = blm
          .createOrganization(blm.createInternationalString(orgSource));
      debug.add("Created target and source organizations \n");

      Concept associationConcept = bqm.findConceptByPath(aType);
      if (associationConcept == null)
        throw new Fault(testName
            + " findConceptByPath failed to return /AssociationType/RelatedTo");

      Association a = blm.createAssociation(target, associationConcept);
      a.setSourceObject(source);
      debug.add("This is an intramural association and does not require\n");
      debug.add("explicit confirmation step. \n");
      debug.add("Expect isConfirmedBySourceOwner to be true\n");
      debug.add("isConfirmedBySourceOwner is " + a.isConfirmedBySourceOwner());
      if (!(a.isConfirmedBySourceOwner()))
        throw new Fault(testName + " failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(" failed ");
    }
  } // end of method

  /*
   * @testName: association_isConfirmedByTargetOwner
   *
   * @assertion_ids: JAXR:JAVADOC:826;
   *
   * @test_Strategy: For intramural Associations always returns true Create an
   * Association, a source and a target with the same owner. This is an
   * intramural association - confirmed. Verify that isConfirmedByTargetOwner
   * returns true.
   *
   */
  public void association_isConfirmedByTargetOwner() throws Fault {
    String testName = "association_isConfirmedByTargetOwner";
    String orgTarget = "Target Org";
    String orgSource = "Source Org";
    String aType = "/AssociationType/RelatedTo";
    try {
      Organization target = blm
          .createOrganization(blm.createInternationalString(orgTarget));
      Organization source = blm
          .createOrganization(blm.createInternationalString(orgSource));
      debug.add("Created target and source organizations \n");

      Concept associationConcept = bqm.findConceptByPath(aType);
      if (associationConcept == null)
        throw new Fault(testName
            + " findConceptByPath failed to return /AssociationType/RelatedTo");

      Association a = blm.createAssociation(target, associationConcept);
      a.setSourceObject(source);
      debug.add("This is an intramural association and does not require\n");
      debug.add("explicit confirmation step. \n");
      debug.add("Expect isConfirmedByTargetOwner to be true\n");
      debug.add("isConfirmedByTargetOwner is " + a.isConfirmedByTargetOwner());
      if (!(a.isConfirmedByTargetOwner()))
        throw new Fault(testName + " failed");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(" failed ");
    }
  } // end of method

  /*
   * @testName: association_isExtramural
   *
   * @assertion_ids: JAXR:JAVADOC:821;JAXR:SPEC:140;JAXR:SPEC:136;
   *
   * @test_Strategy: Create an Extramural Association. Verify isExtramural
   * returns true.
   *
   */
  public void association_isExtramural() throws Fault {
    String testName = "association_isExtramural";
    String orgTarget = "Org Target";
    String orgSource = "Org Source";
    String type = "Replaces";
    try {
      // second user.
      secondConnection();
      Organization target = blm2
          .createOrganization(blm.createInternationalString(orgTarget));
      Organization source = blm
          .createOrganization(blm.createInternationalString(orgSource));
      Concept associationType = getAssociationConcept(type);
      if (associationType == null)
        throw new Fault(
            testName + " getAssociationConcept returned null associationType");
      Association a = blm.createAssociation(target, associationType);
      a.setSourceObject(source);
      debug.add("isExtramural should be true\n");
      debug.add("Association.isExtramural returns: " + a.isExtramural() + "\n");
      if (!(a.isExtramural()))
        throw new Fault(testName + " failed");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }

  } // end of method

  /*
   * testName: association_isConfirmedExtramuralPubl
   *
   * @assertion_ids: JAXR:JAVADOC:830;JAXR:JAVADOC:824;JAXR:JAVADOC:828;
   * JAXR:SPEC:169; JAXR:SPEC:138;JAXR:SPEC:134;JAXR:SPEC:129;
   *
   * @test_Strategy: Create an Extramural Association and don't confirm it.
   * isConfirmed should return false; isConfirmedBySourceOwner should be false;
   * isConfirmedByTargetOwner should be false; Confirm target and source
   * associations. Verify that isConfirmed is true.
   *
   */
  public void association_isConfirmedExtramuralPubl() throws Fault {
    String testName = "association_isConfirmedExtramuralPubl";
    String orgTarget = "Org Target";
    String orgSource = "Org Source";
    String type = "HasChild";
    BulkResponse br = null;
    Key savekey = null;
    String objectType = LifeCycleManager.ORGANIZATION;
    BusinessQueryManager bqm2 = null;
    Collection associationKeys = null;
    Collection sourceKeys = null;
    Collection targetKeys = null;
    boolean pass = true;
    String targetId = null;
    String sourceId = null;
    try {
      // second user.
      secondConnection();
      bqm2 = rs2.getBusinessQueryManager();

      Organization target = blm2.createOrganization(
          blm.createInternationalString(tsLocale, orgTarget));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSource));
      // ==
      // publish the organizations
      Collection orgs = new ArrayList();
      orgs.add(source);
      br = blm.saveOrganizations(orgs); // publish to registry
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }

      sourceKeys = br.getCollection();
      Iterator iter = sourceKeys.iterator();
      while (iter.hasNext()) {
        savekey = (Key) iter.next();
      }
      sourceId = savekey.getId();
      Organization pubSource = (Organization) bqm.getRegistryObject(sourceId,
          objectType);
      debug.add("Verify the pub source retrieved from registry \n");
      // debug.add("pubSource retrieved: " +
      // pubSource.getName().getValue(tsLocale) + "\n");

      orgs.clear();
      orgs.add(target);
      br = blm2.saveOrganizations(orgs); // publish to registry
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }
      targetKeys = br.getCollection();
      iter = targetKeys.iterator();
      while (iter.hasNext()) {
        savekey = (Key) iter.next();
      }
      targetId = savekey.getId();

      Organization pubTarget = (Organization) bqm2.getRegistryObject(targetId,
          objectType);
      debug.add("Verify the pub target retrieved from registry \n");
      debug.add("pubTarget retrieved: " + pubTarget.getName().getValue(tsLocale)
          + "\n");
      // ==
      Concept associationType = getAssociationConcept(type);
      if (associationType == null)
        throw new Fault(
            testName + " getAssociationConcept returned null associationType");

      Association a = blm2.createAssociation(pubTarget, associationType);
      a.setSourceObject(pubSource);

      debug.add("user2 confirm the target association");
      blm2.confirmAssociation(a);

      // publish the Association
      Collection associations = new ArrayList();
      associations.add(a);
      // user 2 saves the association.
      br = blm2.saveAssociations(associations, false);
      if (!(JAXR_Util.checkBulkResponse("saveAssociations", br, debug))) {
        debug.add("Error:    saveAssociations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }

      // get back the association
      associationKeys = br.getCollection();
      iter = associationKeys.iterator();
      Key assocKey = (Key) iter.next();
      debug.add(
          "expected 1 association key, got " + associationKeys.size() + "\n");

      Collection associationTypes = new ArrayList();
      associationTypes.add(associationType);
      // confirmedByCaller = false, confirmedByOtherParty = true.
      br = bqm.findCallerAssociations(null, Boolean.FALSE, Boolean.TRUE,
          associationTypes);
      if (!(JAXR_Util.checkBulkResponse("findCallerAssociations", br, debug))) {
        debug.add("Error:    findCallerAssociations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }
      associations = br.getCollection();
      if (associations.size() == 0) {
        debug.add(
            "Failure: findCallerAssociations did not return an association as expected!\n");
        throw new Fault(testName + " failed");
      }
      a = null;
      iter = associations.iterator();
      while (iter.hasNext()) {
        Association ass = (Association) iter.next();
        if (ass.getKey().getId().equals(assocKey.getId())) {
          a = ass;
          break;
        }
      }
      if (a == null)
        new Fault(testName + " did not complete - association is null! ");

      debug.add("Verify the published association retrieved from registry \n");
      debug.add("retrieved association with association type = : "
          + a.getAssociationType().getValue() + "\n");

      debug.add(
          "isConfirmed should be false, not confirmed by both source and target owner\n");
      debug.add("Association.isConfirmed returns: " + a.isConfirmed() + "\n");
      if ((a.isConfirmed())) {
        debug.add("FAIL: isConfirmed incorrectly returned true \n");
        pass = false;
      }
      debug.add("isConfirmedBySourceOwner should be false\n");
      debug.add("Association.isConfirmedBySourceOwner returns: "
          + a.isConfirmedBySourceOwner() + "\n");
      if ((a.isConfirmedBySourceOwner())) {
        debug
            .add("FAIL: isConfirmedBySourceOwner incorrectly returned true \n");
        pass = false;
      }
      debug.add("The source owner confirms the association\n");
      // now confirm the association
      // blm2.confirmAssociation(a);
      blm.confirmAssociation(a);

      associations.clear();
      associations.add(a);
      br = blm.saveAssociations(associations, false);
      if (!(JAXR_Util.checkBulkResponse("saveAssociations", br, debug))) {
        debug.add("Error:    saveAssociations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }

      br = bqm.findCallerAssociations(null, Boolean.TRUE, Boolean.TRUE,
          associationTypes);

      if (!(JAXR_Util.checkBulkResponse("findCallerAssociations", br, debug))) {
        debug.add("Error:    findCallerAssociations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }
      //
      associations = br.getCollection();
      iter = associations.iterator();
      a = null;
      while (iter.hasNext()) {
        Association ass = (Association) iter.next();
        if (ass.getKey().getId().equals(assocKey.getId())) {
          a = ass;
          break;
        }
      }
      if (a == null)
        new Fault(testName + " did not complete - association is null! ");

      debug.add("isConfirmed should be true\n");
      debug.add("Association.isConfirmed returns: " + a.isConfirmed() + "\n");
      if (!(a.isConfirmed())) {
        debug.add("FAIL: isConfirmed incorrectly returned false\n");
        pass = false;
      }

      debug.add("isConfirmedBySourceOwner should be true\n");
      debug.add("Association.isConfirmedBySourceOwner returns: "
          + a.isConfirmedBySourceOwner() + "\n");
      if (!(a.isConfirmedBySourceOwner())) {
        debug
            .add("FAIL: isConfirmedBySourceOwner incorrectly returned false\n");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      try {
        debug.add(" Cleanup: Remove source organization\n");
        blm.deleteOrganizations(sourceKeys);
        debug.add(" Cleanup: Remove target organization\n");
        blm2.deleteOrganizations(targetKeys);

      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Error: not able to delete registry object\n");
      }
    }
    if (!pass)
      throw new Fault(testName + "failed ");

  } // end of method

  private Concept getAssociationConcept(String associationType) {
    Collection atypes = new ArrayList();
    atypes.add("AssociationType");
    try {
      Collection associationTypes = bqm
          .findClassificationSchemes(null, atypes, null, null).getCollection();
      Iterator iter = associationTypes.iterator();
      while (iter.hasNext()) {
        ClassificationScheme cs = (ClassificationScheme) iter.next();
        Collection types = cs.getChildrenConcepts();
        Iterator iter1 = types.iterator();
        Concept concept = null;
        while (iter1.hasNext()) {
          concept = (Concept) iter1.next();
          if (concept.getName().getValue(tsLocale).equals(associationType)) {
            return concept;
          }
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      debug.add("getAssociationConcept caught an error!\n");
      return null;
    }

    return null;

  }// end of method
  /*
   * used for extramural association tests were more than 1 user is required.
   *
   */

  private void secondConnection() throws Fault {
    // need a second user for an extramural connection.
    javax.xml.registry.Connection srcConnection = null;
    try {
      srcConnection = factory.createConnection();
      Set credentials = null;
      switch (jaxrSecurityCredentialType) {
      case USE_USERNAME_PASSWORD:
        credentials = new HashSet();
        PasswordAuthentication passwdAuth = new PasswordAuthentication(
            jaxrUser2, jaxrPassword2.toCharArray());
        credentials.add(passwdAuth);
        break;
      case USE_DIGITAL_CERTIFICATES:
        // ==
        credentials = super.getDigitalCertificateCredentials(jaxrAlias2,
            jaxrAlias2Password);
        break;
      default:
        throw new Fault(
            "second connection failed: jaxrSecurityCredentialType is invalid");
      } // end of switch

      srcConnection.setCredentials(credentials);
      rs2 = srcConnection.getRegistryService();
      blm2 = rs2.getBusinessLifeCycleManager();
    } catch (Exception e) {
      throw new Fault("Exception in secondConnection method", e);
    }
  }

}
