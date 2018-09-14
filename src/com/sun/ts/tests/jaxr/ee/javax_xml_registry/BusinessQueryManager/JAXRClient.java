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

package com.sun.ts.tests.jaxr.ee.javax_xml_registry.BusinessQueryManager;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {
  Locale tsLocale = new Locale("en", "US");

  RegistryService rs2 = null;

  BusinessLifeCycleManager blm2 = null;

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
      logMsg("in cleanup");
      if (conn != null) {
        logTrace("Cleanup is closing the connection");
        conn.close();
      }
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    } finally {
      // print out messages
      if (debug != null)
        TestUtil.logTrace(debug.toString());
    }

  }

  private boolean findClassificationSchemeByName(String schemeName,
      String testName) {
    try {
      Collection findQualifiers = new ArrayList();
      findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      ClassificationScheme scheme = bqm
          .findClassificationSchemeByName(findQualifiers, schemeName);
      if (scheme != null) {
        debug.add("name is: " + scheme.getName().getValue());
        if (!(scheme.getName().getValue().equals(schemeName)))
          return false;
      } else {
        debug.add("Error: null returned!!\n");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  /*
   * @testName: businessQueryManager_findServiceCaseSensitiveMatchTest
   *
   * @assertion_ids: JAXR:JAVADOC:218;
   *
   * @test_Strategy: Query for ClassificationScheme name that is a case
   * sensitive match.
   *
   */
  public void businessQueryManager_findServiceCaseSensitiveMatchTest()
      throws Fault {
    String testName = "businessQueryManager_findServiceCaseSensitiveMatchTest";
    boolean pass = false;
    String serviceName[] = {
        "businessQueryManager_findCaseSensitiveMatchTest_SellsApples",
        "businessQueryManager_findCaseSensitiveMatchTest_SELLSAPPLES" };
    String exactMatch = "businessQueryManager_findCaseSensitiveMatchTest_SellsApples";
    Collection serviceKeys = null;
    Collection orgKeys = null;
    javax.xml.registry.infomodel.Key serviceKey = null;
    javax.xml.registry.infomodel.Key orgKey = null;

    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      // create an organization first
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveOrganizations failed \n");
        throw new Fault(testName + " failed ");
      }
      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        orgKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      org = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);

      // now create services
      Service service = null;
      Collection services = new ArrayList();
      for (int i = 0; i < serviceName.length; i++) {
        service = blm.createService(
            blm.createInternationalString(tsLocale, serviceName[i]));
        org.addService(service);
        services.add(service);
      }
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService failed \n");
        throw new Fault(testName + " failed ");
      }
      serviceKeys = br.getCollection();

      // ==
      Collection theOrgs = new ArrayList();
      theOrgs.add(org);
      br = blm.saveOrganizations(theOrgs);

      Collection findQualifiers = new ArrayList();
      Collection namePatterns = new ArrayList();
      namePatterns.add(exactMatch);

      findQualifiers.add(FindQualifier.CASE_SENSITIVE_MATCH);
      debug.add(
          " ====================>   CASE_SENSITIVE_MATCH <==============   \n");

      br = bqm.findServices(orgKey, findQualifiers, namePatterns, null, null);
      // check for an error
      if (br.getExceptions() != null)
        debug.add(" did not complete - findServices error");

      services.clear();
      services = br.getCollection();
      debug.add("Count of services returned: " + services.size() + "\n");

      iter = services.iterator();
      while (iter.hasNext()) {
        Service s = (Service) iter.next();
        debug.add("UUID:  returned " + s.getKey().getId() + "\n");
        debug.add(
            "service name returned: " + s.getName().getValue(tsLocale) + "\n");
        if (s.getName().getValue(tsLocale).equals(exactMatch)
            && services.size() == 1)
          pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      try {
        if (serviceKeys != null)
          blm.deleteServices(serviceKeys);
        if (orgKeys != null)
          blm.deleteOrganizations(orgKeys);

        if (debug != null)
          TestUtil.logTrace(debug.toString());
      } catch (Exception fe) {
        TestUtil.logErr("Caught exception: " + fe.getMessage());
        fe.printStackTrace();

      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  }

  /*
   * @testName: businessQueryManager_findExactNameMatchTest
   *
   * @assertion_ids: JAXR:JAVADOC:217;
   *
   * @test_Strategy: Query for ClassificationScheme name that is an exact name
   * match.
   *
   */
  public void businessQueryManager_findExactNameMatchTest() throws Fault {
    String testName = "businessQueryManager_findExactNameMatchTest";
    String exactMatch = "PhoneType";
    String namePattern = "%Type";

    boolean pass = false;
    try {
      Collection findQualifiers = new ArrayList();
      findQualifiers.add(FindQualifier.EXACT_NAME_MATCH);
      Collection namePatterns = new ArrayList();
      namePatterns.add(exactMatch);
      debug.add(
          " ====================>   EXACT_NAME_MATCH <==============   \n");
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      BulkResponse br = bqm.findClassificationSchemes(findQualifiers,
          namePatterns, null, null);
      // check for an error
      if (br.getExceptions() != null)
        debug.add(" did not complete - findClassificationSchemes error");

      Collection schemes = br.getCollection();
      debug.add("Count of schemes returned: " + schemes.size() + "\n");

      Iterator iter = schemes.iterator();
      while (iter.hasNext()) {
        ClassificationScheme scheme = (ClassificationScheme) iter.next();
        debug.add("UUID:  returned " + scheme.getKey().getId() + "\n");
        debug
            .add("scheme name returned: " + scheme.getName().getValue() + "\n");
        if (scheme.getName().getValue().equals(exactMatch))
          pass = true;
      }

      // ==
      namePatterns.clear();
      namePatterns.add(namePattern);
      findQualifiers.clear();
      findQualifiers.add(FindQualifier.CASE_SENSITIVE_MATCH);
      debug.add(
          " ====================>   CASE_SENSITIVE_MATCH <==============   \n");
      getSchemes(namePatterns, findQualifiers);
      // ==
      findQualifiers.clear();
      findQualifiers.add(FindQualifier.SORT_BY_NAME_ASC);
      debug.add(
          " ====================>   SORT_BY_NAME_ASC <==============   \n");
      getSchemes(namePatterns, findQualifiers);
      // ==
      findQualifiers.clear();
      findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
      debug.add(
          " ====================>   SORT_BY_NAME_DESC <==============   \n");
      getSchemes(namePatterns, findQualifiers);
      // ==
      findQualifiers.clear();
      findQualifiers.add(FindQualifier.SORT_BY_DATE_ASC);
      debug.add(
          " ====================>   SORT_BY_DATE_ASC <==============   \n");
      getSchemes(namePatterns, findQualifiers);

      // ==
      findQualifiers.clear();
      findQualifiers.add(FindQualifier.SORT_BY_DATE_DESC);
      debug.add(
          " ====================>   SORT_BY_DATE_DESC <==============   \n");
      getSchemes(namePatterns, findQualifiers);

      if (!pass)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }

  private void getSchemes(Collection namePatterns, Collection findQualifiers) {
    boolean status = false;
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      BulkResponse br = bqm.findClassificationSchemes(findQualifiers,
          namePatterns, null, null);
      // check for an error
      if (br.getExceptions() != null)
        debug.add(" did not complete - findClassificationSchemes error");

      Collection schemes = br.getCollection();
      debug.add("Count of schemes returned: " + schemes.size() + "\n");

      Iterator iter = schemes.iterator();
      while (iter.hasNext()) {
        ClassificationScheme scheme = (ClassificationScheme) iter.next();
        debug.add("UUID:  returned " + scheme.getKey().getId() + "\n");
        debug
            .add("scheme name returned: " + scheme.getName().getValue() + "\n");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: businessQueryManager_findClassificationSchemeByNameNullTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:261;
   *
   * @test_Strategy: Query for ClassificationScheme name that is invalid. Verify
   * that null is returned.
   *
   */
  public void businessQueryManager_findClassificationSchemeByNameNullTest()
      throws Fault {
    String testName = "businessQueryManager_findClassificationSchemeByNameNullTest";
    Collection findQualifiers = new ArrayList();
    findQualifiers.add(FindQualifier.EXACT_NAME_MATCH);
    String bogusName = "bogusDummyNameNoMatchHereReturnNullPlease";
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      // if no match return null
      Collection names = new ArrayList();
      ClassificationScheme cs = bqm
          .findClassificationSchemeByName(findQualifiers, bogusName);
      if (cs == null) {
        debug.add("Good - Null returned for no match as expected\n");
      } else {
        debug.add(
            "Error - Expected null to be returned for name bogusDummyNameNoMatchHereReturnNullPlease\n");
        throw new Fault(testName + " failed ");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: businessQueryManager_findClassificationSchemeByNameMultipleTest
   *
   * @assertion_ids: JAXR:JAVADOC:262;
   *
   * @test_Strategy: Query for ClassificationScheme name where multiple matches
   * should occur. Verify that an InvalidRequestException is returned.
   *
   */
  public void businessQueryManager_findClassificationSchemeByNameMultipleTest()
      throws Fault {
    String testName = "businessQueryManager_findClassificationSchemeByNameMultipleTest";
    Collection findQualifiers = new ArrayList();
    String multiMatchName = "%";

    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      ClassificationScheme cs = bqm.findClassificationSchemeByName(null,
          multiMatchName);

      debug.add("[CLIENT] Scheme name returned is: " + cs.getName().getValue()
          + "\n");
      debug.add(
          "Error: findClassificationSchemeByName multiple match should have thrown an InvalidRequestException \n");
      throw new Fault(
          testName + " did not return an InvalidRequestException as expected ");

    } catch (InvalidRequestException ir) {
      TestUtil.printStackTrace(ir);
      debug.add("Good - InvalidRequestException thrown expected \n");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  }

  /*
   * testName: businessQueryManager_findAssociationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:252;JAXR:SPEC:135;
   *
   * @test_Strategy: Create and publish an association. Verify that it can be
   * retrieved with findAssociation.
   */

  public void businessQueryManager_findAssociationsTest() throws Fault {

    String testName = "businessQueryManager_findAssociationsTest";
    String orgTarget = "Org Target";
    String orgSource = "Org Source";
    String type = "RelatedTo";
    BulkResponse br = null;
    Key savekey = null;
    javax.xml.registry.infomodel.Key assockey = null;
    String objectType = LifeCycleManager.ORGANIZATION;
    BusinessQueryManager bqm2 = null;
    Collection associationKeys = null;
    Collection sourceKeys = null;
    Collection targetKeys = null;
    String targetId = null;
    String sourceId = null;
    boolean pass = false;

    try {
      // second user.
      secondConnection();
      bqm2 = rs2.getBusinessQueryManager();

      Organization target = blm2.createOrganization(
          blm.createInternationalString(tsLocale, orgTarget));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSource));

      // publish the source organization
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
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Organization pubSource = (Organization) bqm.getRegistryObject(sourceId,
          objectType);
      debug.add("Verify the pub source retrieved from registry \n");
      debug.add("pubSource retrieved: " + pubSource.getName().getValue(tsLocale)
          + "\n");
      // publish the target
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

      debug.add("users confirm the association");
      blm.confirmAssociation(a);
      blm2.confirmAssociation(a);

      // publish the Association
      Collection associations = new ArrayList();
      associations.add(a);
      // user2 saves the association.
      br = blm2.saveAssociations(associations, false);

      if (!(JAXR_Util.checkBulkResponse("saveAssociations", br, debug))) {
        debug.add("Error:    saveAssociations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }
      // ........................
      //
      // get back the association
      associationKeys = br.getCollection();
      iter = associationKeys.iterator();
      Key assocKey = (Key) iter.next(); // FN added
      debug
          .add("Just got this key from saveAssociations - " + assocKey.getId());

      BulkResponse targetBResp = bqm.findCallerAssociations(null,
          new Boolean(true), new Boolean(true), null);

      if (targetBResp.getExceptions() == null) {
        Collection targetResult = targetBResp.getCollection();
        debug.add("Associations returned from findCallerAssociations is "
            + targetResult.size() + "\n");
        if (targetResult.size() > 0) {
          iter = targetResult.iterator();
          Association a1 = null;
          while (iter.hasNext()) {
            debug.add("From Caller Associations \n");
            a = (Association) iter.next();
            if (a.getKey().getId().equals(assocKey.getId())) {
              a1 = a;
              break;
            }
          }
          if (a1 == null)
            new Fault(testName + " did not complete - association is null! ");

          Organization o = (Organization) a1.getSourceObject();
          debug.add("Source object " + o.getName().getValue(tsLocale) + "\n");
          o = (Organization) a1.getTargetObject();
          debug.add("Target object " + o.getName().getValue(tsLocale) + "\n");
          Concept atype = a1.getAssociationType();
          debug.add("type is " + atype.getName().getValue() + "\n");
          debug.add("==================================\n");

        } // end of if
      } // end if
      // ........................

      br = null;
      Collection associationTypes = new ArrayList();
      associationTypes.add(type);
      br = bqm.findAssociations(null, sourceId, targetId, null);
      if (!(JAXR_Util.checkBulkResponse("findAssociations", br, debug))) {
        debug.add("Error:    findAssociations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }
      associations = null;
      associations = br.getCollection();
      if (associations.size() > 0) {
        iter = associations.iterator();
        Association a1 = null;
        while (iter.hasNext()) {
          debug.add("From findAssociations \n");
          a = (Association) iter.next();
          if (a.getKey().getId().equals(assocKey.getId())) {
            a1 = a;
            break;
          }
        }
        if (a1 == null)
          new Fault(testName + " did not complete - association is null! ");

        assockey = a1.getKey();
        Organization o = (Organization) a1.getSourceObject();
        debug.add("Source object " + o.getName().getValue(tsLocale) + "\n");
        if (o.getName().getValue(tsLocale).equals(orgSource)) {
          debug.add("Good - found association with findAssociation \n");
          pass = true;
        }
        o = (Organization) a1.getTargetObject();
        debug.add("Target object " + o.getName().getValue(tsLocale) + "\n");
        Concept atype = a1.getAssociationType();
        debug.add("type is " + atype.getName().getValue(tsLocale) + "\n");
        debug.add("==================================\n");
      } // end if

      if (!pass)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      // clean up - get rid of published orgs
      try {
        if (assockey != null) {
          associationKeys = new ArrayList();
          associationKeys.add(assockey);
          debug.add(" Cleanup: Remove association\n");
          blm2.deleteAssociations(associationKeys);
        }
        debug.add(" Cleanup: Remove source organization\n");
        blm.deleteOrganizations(sourceKeys);
        debug.add(" Cleanup: Remove target organization\n");
        blm2.deleteOrganizations(targetKeys);
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Error: not able to delete registry object\n");
      }
    }

  } // end of method

  /*
   * @testName: businessQueryManager_findConceptByPathTest
   *
   * @assertion_ids: JAXR:JAVADOC:268;JAXR:SPEC:255;
   *
   * @test_Strategy: JAXR has predefined enumerations defined as concept
   * hierarchies. Test findConceptByPath with ObjectType.
   *
   */
  public void businessQueryManager_findConceptByPathTest() throws Fault {
    String testName = "businessQueryManager_findConceptByPathTest";
    boolean pass = true;
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Concept serviceConcept = bqm.findConceptByPath("/ObjectType/Service");
      // Verify that the concept was returned.
      if (serviceConcept.getValue().equals("Service")) {
        debug.add("findConceptByPath returned correct concept\n");
      } else {
        debug.add(
            "findConceptByPath did not return expected Service concept \n");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end

  private Concept getAssociationConcept(String associationType) {
    Collection atypes = new ArrayList();
    atypes.add("AssociationType");

    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
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

  /*
   * @testName: businessQueryManager_findServiceNameASCTest
   *
   * @assertion_ids: JAXR:JAVADOC:219;
   *
   * @test_Strategy: Query for ClassificationScheme name that is a case
   * sensitive match.
   *
   */
  public void businessQueryManager_findServiceNameASCTest() throws Fault {
    String testName = "businessQueryManager_findServiceNameASCTest";
    boolean pass = true;
    String serviceName[] = {
        "businessQueryManager_findServiceNameASCTest_Apples",
        "businessQueryManager_findServiceNameASCTest_Bananas",
        "businessQueryManager_findServiceNameASCTest_Cherries" };
    String namePattern = "businessQueryManager_findServiceNameASCTest%";
    Collection serviceKeys = null;
    Collection orgKeys = null;
    javax.xml.registry.infomodel.Key serviceKey = null;
    javax.xml.registry.infomodel.Key orgKey = null;

    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      // create an organization first
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveOrganizations failed \n");
        throw new Fault(testName + " failed ");
      }
      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        orgKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      org = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);

      // now create services
      Service service = null;
      Collection services = new ArrayList();
      for (int i = 0; i < serviceName.length; i++) {
        service = blm.createService(
            blm.createInternationalString(tsLocale, serviceName[i]));
        org.addService(service);
        services.add(service);
      }
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService failed \n");
        throw new Fault(testName + " failed ");
      }
      serviceKeys = br.getCollection();
      Collection theOrgs = new ArrayList();
      theOrgs.add(org);
      br = blm.saveOrganizations(theOrgs);

      Collection findQualifiers = new ArrayList();
      Collection namePatterns = new ArrayList();
      namePatterns.add(namePattern);

      findQualifiers.add(FindQualifier.SORT_BY_NAME_ASC);
      debug.add(
          " ====================>   SORT_BY_NAME_ASC  <==============   \n");

      br = bqm.findServices(orgKey, findQualifiers, namePatterns, null, null);
      // check for an error
      if (br.getExceptions() != null)
        debug.add(" did not complete - findServices error");

      services.clear();
      services = br.getCollection();
      debug.add("Count of services returned: " + services.size() + "\n");
      debug.add("Number of services expected: " + serviceName.length + "\n");
      if (services.size() != serviceName.length)
        pass = false;
      iter = services.iterator();
      int i = 0;
      while (iter.hasNext()) {
        Service s = (Service) iter.next();
        debug.add("UUID:  returned " + s.getKey().getId() + "\n");
        debug.add(
            "service name returned: " + s.getName().getValue(tsLocale) + "\n");
        if (!(s.getName().getValue(tsLocale).equals(serviceName[i])))
          pass = false;
        i++;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);

    } finally {
      try {
        if (serviceKeys != null)
          blm.deleteServices(serviceKeys);
        if (orgKeys != null)
          blm.deleteOrganizations(orgKeys);

        if (debug != null)
          TestUtil.logTrace(debug.toString());
      } catch (Exception fe) {
      }
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  }

  /*
   * @testName: businessQueryManager_findServiceNameDESCTest
   *
   * @assertion_ids: JAXR:JAVADOC:220;
   *
   * @test_Strategy: Create and publish 3 services. Query for the services and
   * request descending order of return - verify.
   *
   */
  public void businessQueryManager_findServiceNameDESCTest() throws Fault {
    String testName = "businessQueryManager_findServiceNameDESCTest";
    boolean pass = true;
    String serviceName[] = {
        "businessQueryManager_findServiceNameDESCTest_Apples",
        "businessQueryManager_findServiceNameDESCTest_Bananas",
        "businessQueryManager_findServiceNameDESCTest_Cherries" };
    String namePattern = "businessQueryManager_findServiceNameDESCTest%";
    Collection serviceKeys = null;
    Collection orgKeys = null;
    javax.xml.registry.infomodel.Key serviceKey = null;
    javax.xml.registry.infomodel.Key orgKey = null;

    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    try {
      // create an organization first
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveOrganizations failed \n");
        throw new Fault(testName + " failed ");
      }
      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        orgKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      org = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);

      // now create services
      Service service = null;
      Collection services = new ArrayList();
      for (int i = 0; i < serviceName.length; i++) {
        service = blm.createService(
            blm.createInternationalString(tsLocale, serviceName[i]));
        org.addService(service);
        services.add(service);
      }
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService failed \n");
        throw new Fault(testName + " failed ");
      }
      serviceKeys = br.getCollection();
      Collection theOrgs = new ArrayList();
      theOrgs.add(org);
      br = blm.saveOrganizations(theOrgs);

      Collection findQualifiers = new ArrayList();
      Collection namePatterns = new ArrayList();
      namePatterns.add(namePattern);

      findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
      debug.add(
          " ====================>   SORT_BY_NAME_DESC  <==============   \n");

      br = bqm.findServices(orgKey, findQualifiers, namePatterns, null, null);
      // check for an error
      if (br.getExceptions() != null)
        debug.add(" did not complete - findServices error");

      services.clear();
      services = br.getCollection();
      debug.add("Count of services returned: " + services.size() + "\n");
      debug.add("Number of services expected: " + serviceName.length + "\n");
      if (services.size() != serviceName.length)
        pass = false;
      iter = services.iterator();
      int i = serviceName.length - 1;
      while (iter.hasNext()) {
        Service s = (Service) iter.next();
        debug.add("UUID:  returned " + s.getKey().getId() + "\n");
        debug.add(
            "service name returned: " + s.getName().getValue(tsLocale) + "\n");
        if (!(s.getName().getValue(tsLocale).equals(serviceName[i])))
          pass = false;
        i--;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);

    } finally {
      try {
        if (serviceKeys != null)
          blm.deleteServices(serviceKeys);
        if (orgKeys != null)
          blm.deleteOrganizations(orgKeys);

        if (debug != null)
          TestUtil.logTrace(debug.toString());
      } catch (Exception fe) {
      }
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  }
  /*
   * @testName: businessQueryManager_nonPrivilegedUserTest
   *
   * @assertion_ids: JAXR:SPEC:230;JAXR:SPEC:237;JAXR:SPEC:238;
   *
   *
   * @test_Strategy: Have user 1 create an organization and services. User 2
   * with non privilege should be able to query services in the registry. Verify
   * the services returned for name and correct sort order.
   */

  public void businessQueryManager_nonPrivilegedUserTest() throws Fault {
    String testName = "businessQueryManager_nonPrivilegedUserTest";
    boolean pass = false;
    Service service = null;
    String[] serviceName = {
        "businessQueryManager_nonPrivilegedUserTest-testService1",
        "businessQueryManager_nonPrivilegedUserTest-testService2",
        "businessQueryManager_nonPrivilegedUserTest-testService3" };
    String retS[] = { null, null, null }; // will be services returned from the
                                          // registry
    Collection serviceKeys = null;
    Collection orgKeys = null;
    javax.xml.registry.infomodel.Key orgKey = null;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;

    try {
      // need to create an organization first
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveOrganizations failed \n");
        throw new Fault(testName + " failed ");
      }
      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        orgKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      org = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);

      // now create services
      Collection services = new ArrayList();
      for (int i = 0; i < serviceName.length; i++) {
        service = blm.createService(
            blm.createInternationalString(tsLocale, serviceName[i]));
        org.addService(service);
        services.add(service);
      }
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService failed \n");
        throw new Fault(testName + " failed ");
      }
      serviceKeys = br.getCollection();

      Collection theOrgs = new ArrayList();
      theOrgs.add(org);
      br = blm.saveOrganizations(theOrgs);
      // create a connection for the non privileged user
      Connection srcConnection = factory.createConnection();
      RegistryService rs2 = srcConnection.getRegistryService();
      BusinessQueryManager bqm2 = rs2.getBusinessQueryManager();
      Collection findQualifiers = new ArrayList();
      findQualifiers.add(FindQualifier.CASE_SENSITIVE_MATCH);
      findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
      findQualifiers.add(FindQualifier.SORT_BY_DATE_DESC);
      Collection namePatterns = new ArrayList();
      namePatterns.add("%nonPrivilegedUserTest-testService%");
      br = bqm2.findServices(orgKey, findQualifiers, namePatterns, null, null);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:   findOrganization   failed \n");
        throw new Fault(testName + " failed ");
      }
      Collection ss = br.getCollection();
      iter = ss.iterator();
      // Should find all 3 services - check service count returned.
      if (ss.size() != serviceName.length)
        throw new Fault(testName + " failed to return expected service count ");

      debug.add("Expected number of services returned");
      // Verify the 3 services
      int i = 0;
      while (iter.hasNext()) {
        Service s = (Service) iter.next();
        debug.add(" Found service: : " + s.getName().getValue(tsLocale) + "\n");
        retS[i] = s.getName().getValue(tsLocale);
        i++;
      }
      // verify descending ordering of service names
      int match = 0;
      int n = 2;
      for (i = 0; i < serviceName.length; i++) {
        debug.add("Now verify descending order returned" + "\n");
        if (retS[i].equals(serviceName[n--])) {
          debug.add("match on " + retS[i] + "\n");
          match++;
        }
      }
      if ((!(match == serviceName.length)))
        throw new Fault(
            testName + " failed to return expected sort order of services. ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      // clean up
      try {

        if (serviceKeys != null) {
          blm.deleteServices(serviceKeys);
          debug.add("==>>   Cleanup: delete service\n");
        }
        if (orgKeys != null) {
          debug.add("==>>  Cleanup: delete organization\n");
          blm.deleteOrganizations(orgKeys);
        }

      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Error: not able to delete organization \n");
      }
    }

  } // end of method

  /*
   * @testName: businessQueryManager_findRegistryPackagesTest
   *
   * @assertion_ids: JAXR:JAVADOC:270
   *
   * @test_Strategy:
   * 
   *
   */
  public void businessQueryManager_findRegistryPackagesTest() throws Fault {
    String testName = "businessQueryManager_findRegistryPackagesTest";
    int failcount = 0;
    int providerlevel = 0;
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // level 0 providers do not support the setExpiration method and must
      // throw
      // an UnsupportedCapabilityException.
      String rpName = "RegistryPackage_" + testName;

      RegistryPackage rp = blm.createRegistryPackage(
          blm.createInternationalString(tsLocale, rpName));
      if (providerlevel == 0) {
        throw new Fault(testName
            + "failed to throw expected UnsupportedCapabilityException.");
      }
      Key rpKey = rp.getKey();
      debug.add("RegistryPackage key is: " + rpKey.getId() + "\n");
      // create the organization.
      String orgName = "Organization_" + testName;
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));
      // create the service.
      String serviceName = "Service_" + testName;
      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      Key serviceKey = service.getKey();
      Key orgKey = org.getKey();
      debug.add("Service key is: " + serviceKey.getId() + "\n");
      debug.add("orgKey is: " + orgKey.getId() + "\n");
      Collection robs = new ArrayList();
      robs.add(service);
      robs.add(org);
      rp.addRegistryObjects(robs);
      robs.add(rp);
      Collection rps = new ArrayList();
      rps.add(rp);
      // save the registry objects
      BulkResponse br = blm.saveObjects(robs);
      // does it save all of the objects ?
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveObjects failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }
      Collection mySavedObjectKeys = br.getCollection();
      debug.add("got back " + mySavedObjectKeys.size()
          + " objects from save objects\n");
      Collection findQualifiers = new ArrayList();
      findQualifiers.add(FindQualifier.EXACT_NAME_MATCH);
      Collection namePatterns = new ArrayList();
      namePatterns.add(rpName);
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      br = bqm.findRegistryPackages(findQualifiers, namePatterns, null, null);
      if (!(JAXR_Util.checkBulkResponse("findRegistryPackages", br, debug))) {
        debug.add("Error:    findRegistryPackages failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }

      Collection myRegPkg = br.getCollection();
      Iterator iter = myRegPkg.iterator();
      debug.add("got back " + myRegPkg.size()
          + " objects from save findRegistryPackages\n");
      while (iter.hasNext()) {
        Object obj = iter.next();
        debug.add("what is it? " + obj.getClass().getName() + "\n");
        if (obj instanceof RegistryPackage) {
          rp = (RegistryPackage) obj;
          if (rp.getKey().getId().equals(rpKey.getId())) {
            debug.add(
                "Good - findRegistryPackages returned the correct package\n");
          } else {
            debug.add(
                "Not Good - findRegistryPackages did not returned the correct package\n");
            failcount = failcount + 1;
          }
        }
      }
      // ==
      Set ros = rp.getRegistryObjects();
      debug
          .add("Number of registry objects in package is " + ros.size() + "\n");
      iter = ros.iterator();
      Service myService = null;
      Organization myOrg = null;
      int count = 0;
      while (iter.hasNext()) {
        Object o = iter.next();
        debug.add("what is it? " + o.getClass().getName() + "\n");
        if (o instanceof Service) {
          debug.add("getRegistryObjects() returned a service\n");
          Service s = (Service) o;
          if (s.getKey().getId().equals(serviceKey.getId())) {
            debug.add("Found my service ! \n");
            myService = s;
          }
        }
        if (o instanceof Organization) {
          debug.add("getRegistryObjects() returned an organization \n");
          Organization orgn = (Organization) o;
          if (orgn.getKey().getId().equals(orgKey.getId())) {
            myOrg = orgn;
            debug.add("Found my org! \n");
          }
        }
      }
      if (myService == null) {
        failcount = failcount + 1;
        debug.add(
            "Error -  could not find the service in the registry package \n");
      }
      if (myOrg == null) {
        failcount = failcount + 1;
        debug.add(
            "Error -  could not find the organization in the registry package \n");
      }

      Collection keys = new ArrayList();
      keys.add(rpKey);
      keys.add(serviceKey);
      keys.add(orgKey);
      br = blm.deleteObjects(keys);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    deleteObjects failed \n");
      }

      if (failcount > 0)
        throw new Fault(testName + " failed - ");

    } catch (UnsupportedCapabilityException uce) {
      if (providerlevel != 0) {
        TestUtil.logErr("Cleanup error: " + uce.toString());
        TestUtil.printStackTrace(uce);
        throw new Fault(
            testName + " failed - unexpected UnsupportedCapabilityException");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  }

}
