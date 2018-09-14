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

package com.sun.ts.tests.jaxr.ee.javax_xml_registry.BusinessLifeCycleManager;

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
   * jaxrWebContext; webServerHost; webServerPort; jaxrAlias; jaxrAlias2;
   * jaxrAliasPassword; jaxrAlias2Password;
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
  /*
   * @testName: businessLifeCycleManager_saveServiceBindingsTest
   *
   * @assertion_ids: JAXR:JAVADOC:278;
   *
   * @test_Strategy:
   * 
   *
   */

  public void businessLifeCycleManager_saveServiceBindingsTest() throws Fault {
    String testName = "businessLifeCycleManager_saveServiceBindingsTest";
    boolean pass = false;
    String serviceName = "testService";
    String sbDescription = "my sb description";

    String conceptName = "my concept";
    Collection sbKeys = null;
    Collection serviceKeys = null;
    Collection orgKeys = null;
    Collection conceptKeys = null;
    javax.xml.registry.infomodel.Key conceptKey = null;
    javax.xml.registry.infomodel.Key serviceKey = null;
    javax.xml.registry.infomodel.Key serviceBindingKey = null;
    javax.xml.registry.infomodel.Key orgKey = null;
    javax.xml.registry.infomodel.Key sbKey = null;

    String accessURI = baseuri + "jaxrTestPage1.html";
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

      // now must create a service for a reference for the service binding.
      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      org.addService(service);
      Collection services = new ArrayList();
      services.add(service);
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService failed \n");
        throw new Fault(testName + " failed ");
      }
      serviceKeys = br.getCollection();

      iter = serviceKeys.iterator();
      while (iter.hasNext()) {
        serviceKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      service = (Service) bqm.getRegistryObject(serviceKey.getId(),
          LifeCycleManager.SERVICE);
      // create a concept
      Concept myConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      myConcept.setName(blm.createInternationalString(tsLocale, conceptName));
      Collection concepts = new ArrayList();
      concepts.add(myConcept);
      br = blm.saveConcepts(concepts);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveConcepts failed \n");
        throw new Fault(testName + " failed ");
      }
      conceptKeys = br.getCollection();
      iter = conceptKeys.iterator();
      while (iter.hasNext()) {
        conceptKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      myConcept = (Concept) bqm.getRegistryObject(conceptKey.getId(),
          LifeCycleManager.CONCEPT);
      // create a specification link
      SpecificationLink sl = blm.createSpecificationLink();
      sl.setSpecificationObject(myConcept);
      // create the service binding
      ServiceBinding sb = blm.createServiceBinding();
      sb.setDescription(blm.createInternationalString(tsLocale, sbDescription));
      sb.setAccessURI(accessURI);
      sb.addSpecificationLink(sl);
      service.addServiceBinding(sb);
      debug.add("== Check sb before save \n");
      debug.add("==>> access uri is " + sb.getAccessURI() + "\n");
      debug.add("==>> sb description is "
          + sb.getDescription().getValue(tsLocale) + "\n");
      debug.add(" ================================================  \n");
      Collection sbs = new ArrayList();
      sbs.add(sb);
      br = blm.saveServiceBindings(sbs);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveServiceBinding failed \n");
        throw new Fault(testName + " failed ");
      }

      sbKeys = br.getCollection();
      iter = sbKeys.iterator();
      while (iter.hasNext()) {
        sbKey = (javax.xml.registry.infomodel.Key) iter.next();
      }
      Collection specifications = new ArrayList();
      specifications.add(myConcept);
      br = bqm.findServiceBindings(serviceKey, null, null, specifications);
      sbs = br.getCollection();
      debug.add("==>>  Count of sbs found is : " + sbs.size() + "\n");
      iter = sbs.iterator();
      while (iter.hasNext()) {
        sb = (ServiceBinding) iter.next();
        Service myService = sb.getService();
        debug.add("==>> service name is : "
            + myService.getName().getValue(tsLocale) + "\n");
        debug.add("==>> access uri is " + sb.getAccessURI() + "\n");
        debug.add("==>> sb description is "
            + sb.getDescription().getValue(tsLocale) + "\n");
        boolean failures = false;
        if (!(myService.getName().getValue(tsLocale).equals(serviceName))) {
          debug.add("Error: unexpected service name \n");
          failures = true;
        }
        if (!(sb.getDescription().getValue(tsLocale).equals(sbDescription))) {
          debug.add("Error: unexpected servicebinding description \n");
          failures = true;
        }
        if (!(sb.getAccessURI().equals(accessURI))) {
          debug.add("Error: unexpected accessURI name \n");
          failures = true;
        }

        if (!failures)
          pass = true;
      }

      if (!pass)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      // clean up
      try {
        if (conceptKeys != null) {
          debug.add("==>>  Cleanup: delete concept\n");
          blm.deleteConcepts(conceptKeys);
        }
        if (sbKeys != null) {
          debug.add("==>>  Cleanup: delete serviceBinding\n");
          blm.deleteServiceBindings(sbKeys);
        }
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
   * @testName: businessLifeCycleManager_saveServiceBindingsUpdateTest
   *
   * @assertion_ids: JAXR:JAVADOC:279;JAXR:SPEC:87;JAXR:SPEC:89;
   *
   * @test_Strategy: Create a service binding for a service. Add a specification
   * link for a WSDL concept. Save the service binding, retrieve it from from
   * the registry update and then verify the update.
   *
   */

  public void businessLifeCycleManager_saveServiceBindingsUpdateTest()
      throws Fault {
    String testName = "businessLifeCycleManager_saveServiceBindingsUpdateTest";
    boolean pass = false;
    String serviceName = "testService";
    String conceptName = "myWSDLFile";
    Collection sbKeys = null;
    Collection serviceKeys = null;
    Collection orgKeys = null;
    Collection conceptKeys = null;
    javax.xml.registry.infomodel.Key conceptKey = null;
    javax.xml.registry.infomodel.Key serviceKey = null;
    javax.xml.registry.infomodel.Key serviceBindingKey = null;
    javax.xml.registry.infomodel.Key orgKey = null;
    javax.xml.registry.infomodel.Key sbKey = null;

    String accessURI = baseuri + "jaxrTestPage1.html";
    String accessURIUpd = "http://www.sun.com";
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
        orgKey = (Key) iter.next();
      }

      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      org = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);

      // now must create a service for a reference for the service binding.
      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      org.addService(service);
      Collection services = new ArrayList();
      services.add(service);
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService failed \n");
        throw new Fault(testName + " failed ");
      }
      serviceKeys = br.getCollection();

      iter = serviceKeys.iterator();
      while (iter.hasNext()) {
        serviceKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      service = (Service) bqm.getRegistryObject(serviceKey.getId(),
          LifeCycleManager.SERVICE);
      // create a concept
      Concept myConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      myConcept.setName(blm.createInternationalString(tsLocale, conceptName));
      Collection concepts = new ArrayList();
      concepts.add(myConcept);
      br = blm.saveConcepts(concepts);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveConcepts failed \n");
        throw new Fault(testName + " failed ");
      }
      conceptKeys = br.getCollection();
      iter = conceptKeys.iterator();
      while (iter.hasNext()) {
        conceptKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      myConcept = (Concept) bqm.getRegistryObject(conceptKey.getId(),
          LifeCycleManager.CONCEPT);
      // create a specification link
      // a specification link provides linkage between a service binding and one
      // of its
      // technical specifications.

      SpecificationLink sl = blm.createSpecificationLink();
      sl.setSpecificationObject(myConcept);
      // create the service binding
      // ServiceBinding instances are RegistryObjects that represent technical
      // information on a specific way to access a specific interface offered by
      // a Service
      // instance.
      ServiceBinding sb = blm.createServiceBinding();

      sb.setAccessURI(accessURI);
      sb.addSpecificationLink(sl);
      service.addServiceBinding(sb);
      Collection sbs = new ArrayList();
      sbs.add(sb);
      br = blm.saveServiceBindings(sbs);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveServiceBinding failed \n");
        throw new Fault(testName + " failed ");
      }
      sbKeys = br.getCollection();
      iter = sbKeys.iterator();
      while (iter.hasNext()) {
        sbKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      Collection specifications = new ArrayList();
      specifications.add(myConcept);
      br = bqm.findServiceBindings(serviceKey, null, null, specifications);
      sbs = br.getCollection();
      debug.add("==>>  Count of sbs found is : " + sbs.size() + "\n");
      iter = sbs.iterator();
      while (iter.hasNext()) {
        sb = (ServiceBinding) iter.next();
        Service myService = sb.getService();
        Collection links = sb.getSpecificationLinks();

      }

      debug.add("===> Update SB  \n");
      sb.setAccessURI(accessURIUpd);
      sbs.clear();
      sbs.add(sb);
      br = blm.saveServiceBindings(sbs);
      if (br.getExceptions() != null) {
        debug.add("==>> \n");
        debug.add("==>>  Error:    saveServiceBinding failed \n");
        debug.add("==>> \n");
        throw new Fault(testName + " failed ");

      }
      debug.add(
          " ===================Verify the Update of SB =============================  \n");
      br = bqm.findServiceBindings(serviceKey, null, null, specifications);
      sbs = br.getCollection();
      debug.add("==>> \n");
      debug.add("==>>  Count of sbs found is : " + sbs.size() + "\n");
      debug.add("==>> \n");
      iter = sbs.iterator();
      while (iter.hasNext()) {
        sb = (ServiceBinding) iter.next();
      }
      Service myService = sb.getService();
      debug.add("==>> service name is : "
          + myService.getName().getValue(tsLocale) + "\n");
      debug.add("==>> access uri is " + sb.getAccessURI() + "\n");

      boolean failures = false;
      if (!(myService.getName().getValue(tsLocale).equals(serviceName))) {
        debug.add("Error: unexpected service name \n");
        failures = true;
      }
      if (!(sb.getAccessURI().equals(accessURIUpd))) {
        debug.add("Error: unexpected accessURI name \n");
        failures = true;
      }

      if (!failures)
        pass = true;

      if (!pass)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      // clean up
      try {
        if (conceptKeys != null) {
          debug.add("==>>  Cleanup: delete concept\n");
          blm.deleteConcepts(conceptKeys);
        }
        if (sbKeys != null) {
          debug.add("==>>  Cleanup: delete serviceBinding\n");
          blm.deleteServiceBindings(sbKeys);
        }
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
   * @testName: businessLifeCycleManager_saveOrganizationTest
   *
   * @assertion_ids: JAXR:JAVADOC:273;
   *
   * @test_Strategy: Create and save an organization. Verify the org was
   * published. Update it. Verify the update
   *
   */
  public void businessLifeCycleManager_saveOrganizationTest() throws Fault {
    String testName = "businessLifeCycleManager_saveOrganizationTest";
    boolean pass = false;
    String orgName = "SaveOrganization organization";
    String orgDescription = "this is a test organization from businessLifeCycleManager_saveOrganizationTest";

    String orgNameUpd = "UpdatedSaveOrganization organization";
    String orgDescriptionUpd = "Updated-this is a test organization from businessLifeCycleManager_saveOrganizationTest";
    Collection orgKeys = null;
    Key savekey = null;
    String objectType = LifeCycleManager.ORGANIZATION;
    try {

      debug.add("Publish the organization \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));
      org.setDescription(
          blm.createInternationalString(tsLocale, orgDescription));
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      if (br.getExceptions() != null) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " failed ");
      }
      orgKeys = br.getCollection();

      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        savekey = (Key) iter.next();
      }
      String orgId = savekey.getId();
      debug.add("Retrieve the published organization from the registry \n");
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Organization retOrg = (Organization) bqm.getRegistryObject(orgId,
          objectType);
      if (retOrg.getName().getValue(tsLocale).equals(orgName) && retOrg
          .getDescription().getValue(tsLocale).equals(orgDescription)) {
        debug.add("Good - retrieved published org successfully! \n");
        debug.add("Now update the organization \n");
      } else {
        debug.add("Failed to verify retrieved published org. \n");
        throw new Fault(testName + " test failed to complete \n");
      }
      retOrg.setName(blm.createInternationalString(tsLocale, orgNameUpd));
      retOrg.setDescription(
          blm.createInternationalString(tsLocale, orgDescriptionUpd));

      orgs.clear();
      orgs.add(retOrg);
      // ==
      br = blm.saveOrganizations(orgs); // publish to registry
      if (br.getExceptions() != null) {
        debug.add("Error:    update saveOrganizations failed \n");
        throw new Fault(testName + " failed ");
      }
      debug.add(
          "Retrieve the updated published organization from the registry \n");
      Organization updOrg = (Organization) bqm.getRegistryObject(orgId,
          objectType);
      if (updOrg.getName().getValue(tsLocale).equals(orgNameUpd) && updOrg
          .getDescription().getValue(tsLocale).equals(orgDescriptionUpd)) {
        debug.add("Good - retrieved updated published org successfully! \n");
        pass = true;
      } else {
        debug.add("Failed to verify retrieved updated published org. \n");
        throw new Fault(testName + " test failed to complete \n");
      }

      if (!pass)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      // clean up
      try {
        debug.add(" Cleanup: delete organization\n");
        blm.deleteOrganizations(orgKeys);
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Error: not able to delete organization \n");
      }
    }

  } // end of method

  /*
   * testName: businessLifeCycleManager_saveAssociationsUpdateTest
   *
   * @assertion_ids: JAXR:JAVADOC:288;
   *
   * @test_Strategy: Create and save an association. Update it. Verify the
   * update with findCallerAssociations.
   *
   */
  public void businessLifeCycleManager_saveAssociationsUpdateTest()
      throws Fault {
    String testName = "businessLifeCycleManager_saveAssociationsUpdateTest";
    boolean pass = false;

    String orgTarget = "Org Target";
    String orgSource = "Org Source";
    String type = "EquivalentTo";
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

    try {
      // create a second user for second organiazation
      secondConnection();
      bqm2 = rs2.getBusinessQueryManager();

      Organization target = blm2.createOrganization(
          blm.createInternationalString(tsLocale, orgTarget));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSource));
      // ==
      // publish the source organization
      Collection orgs = new ArrayList();
      orgs.add(source);
      br = blm.saveOrganizations(orgs); // publish to registry
      if (br.getExceptions() != null) {
        debug.add("Error:    source  saveOrganizations failed \n");
        throw new Fault(testName + " failed ");
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
      if (br.getExceptions() != null) {
        debug.add("Error:    target saveOrganizations failed \n");
        throw new Fault(testName + " did not complete due to errors");
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

      Association a = blm.createAssociation(pubTarget, associationType);
      a.setSourceObject(pubSource);

      debug.add("users confirm the association");
      blm.confirmAssociation(a);
      blm2.confirmAssociation(a);

      // publish the Association
      Collection associations = new ArrayList();
      associations.add(a);
      // user saves the association.
      br = blm.saveAssociations(associations, false);

      // save the key
      associationKeys = br.getCollection();
      iter = associationKeys.iterator();
      Key assocKey = (Key) iter.next();

      if (br.getExceptions() != null) {
        debug.add("Error:    saveAssociations failed \n");
        throw new Fault(testName + " did not complete due to errors");
      }
      // ==
      BulkResponse targetBResp = bqm.findCallerAssociations(null,
          new Boolean(true), new Boolean(true), null);
      Association myAssoc = null;
      if (targetBResp.getExceptions() == null) {
        Collection targetResult = targetBResp.getCollection();
        debug.add("Associations returned from findCallerAssociations is "
            + targetResult.size() + "\n");
        if (targetResult.size() > 0) {
          iter = targetResult.iterator();
          while (iter.hasNext()) {
            Association aa = (Association) iter.next();
            if (aa.getKey().getId().equals(assocKey.getId())) {
              myAssoc = aa;
              break;
            }
          }
          assockey = myAssoc.getKey();
          Concept atype = myAssoc.getAssociationType();
          debug.add("type is " + atype.getName().getValue(tsLocale) + "\n");

        } // end of if
      } // end if

      // ==
      //
      if (assockey == null)
        throw new Fault(testName + " failed to find association ");

      String updateType = "RelatedTo";
      Concept myType = getAssociationConcept(updateType);
      debug.add(" changed association type to RelatedTo \n");
      // update Association
      myAssoc.setAssociationType(myType);
      associations.clear();
      associations.add(myAssoc);
      debug.add("Save updated association \n");
      br = blm.saveAssociations(associations, false);
      // save the key
      associationKeys = br.getCollection();
      iter = associationKeys.iterator();
      assocKey = (Key) iter.next();

      if (br.getExceptions() != null) {
        debug.add("Error:    saveAssociations failed \n");
        throw new Fault(testName + " did not complete due to errors");
      }
      // ==
      debug
          .add("use findCallerAssociations to retrieve updated association \n");
      br = bqm.findCallerAssociations(null, new Boolean(true),
          new Boolean(false), null);

      if (br.getExceptions() == null) {
        Collection targetResult = br.getCollection();
        debug.add("Associations returned from findCallerAssociations is "
            + targetResult.size() + "\n");
        Association a1 = null;
        if (targetResult.size() > 0) {
          iter = targetResult.iterator();
          while (iter.hasNext()) {
            Association aa = (Association) iter.next();
            if (aa.getKey().getId().equals(assocKey.getId())) {
              a1 = aa;
              break;
            }
          } // end while
          if (a1 == null)
            new Fault(testName + " did not complete - association is null! ");

          Concept atype = a1.getAssociationType();
          debug.add("type is " + atype.getValue() + "\n");
          Organization o = (Organization) a1.getSourceObject();
          debug.add("Source object " + o.getName().getValue(tsLocale) + "\n");
          if (o.getName().getValue(tsLocale).equals(orgSource)) {
            debug.add("Good - org source name matched! \n");
            if (a1.getAssociationType().getValue()
                .equalsIgnoreCase(updateType)) {
              debug.add("And the concept type was updated too! \n");
              pass = true;
            }
          }
        } // end of if
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
          blm.deleteAssociations(associationKeys);
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
   * testName: businessLifeCycleManager_saveAssociationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:287;
   *
   * @test_Strategy: Create and save an association. Verify the save with
   * findCallerAssociations.
   *
   */
  public void businessLifeCycleManager_saveAssociationsTest() throws Fault {
    String testName = "businessLifeCycleManager_saveAssociationsTest";
    boolean pass = false;

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

    try {
      // create a second user for second organiazation
      secondConnection();
      bqm2 = rs2.getBusinessQueryManager();

      Organization target = blm2.createOrganization(
          blm.createInternationalString(tsLocale, orgTarget));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSource));
      // ==
      // publish the source organization
      Collection orgs = new ArrayList();
      orgs.add(source);
      br = blm.saveOrganizations(orgs); // publish to registry
      if (br.getExceptions() != null) {
        debug.add("Error:    source  saveOrganizations failed \n");
        throw new Fault(testName + " failed ");
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
      if (br.getExceptions() != null) {
        debug.add("Error:    target saveOrganizations failed \n");
        throw new Fault(testName + " did not complete due to errors");
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

      Association a = blm.createAssociation(pubTarget, associationType);
      a.setSourceObject(pubSource);

      debug.add("users confirm the association");
      blm.confirmAssociation(a);
      // blm2.confirmAssociation(a);

      // publish the Association
      Collection associations = new ArrayList();
      associations.add(a);
      // user saves the association.
      br = blm.saveAssociations(associations, false);

      // save the key
      associationKeys = br.getCollection();
      iter = associationKeys.iterator();
      Key assocKey = (Key) iter.next();
      if (br.getExceptions() != null) {
        debug.add("Error:    saveAssociations failed \n");
        throw new Fault(testName + " did not complete due to errors");
      }
      // ==
      BulkResponse targetBResp = bqm.findCallerAssociations(null,
          new Boolean(true), new Boolean(false), null);
      if (targetBResp.getExceptions() == null) {
        Collection targetResult = targetBResp.getCollection();
        debug.add("Associations returned from findCallerAssociations is "
            + targetResult.size() + "\n");
        Association a1 = null;
        if (targetResult.size() > 0) {
          iter = targetResult.iterator();
          while (iter.hasNext()) {
            Association aa = (Association) iter.next();
            if (aa.getKey().getId().equals(assocKey.getId())) {
              a1 = aa;
              break;
            }
          } // end while
          if (a1 == null)
            new Fault(testName + " did not complete - association is null! ");
          assockey = a1.getKey();
          Organization o = (Organization) a1.getSourceObject();
          debug.add("Source object " + o.getName().getValue(tsLocale) + "\n");
          if (o.getName().getValue(tsLocale).equals(orgSource)) {
            debug.add("Good - verified save association \n");
            pass = true;
          }
          o = (Organization) a1.getTargetObject();
          debug.add("Target object " + o.getName().getValue(tsLocale) + "\n");
          Concept atype = a1.getAssociationType();
          debug.add("type is " + atype.getName().getValue(tsLocale) + "\n");
          debug.add("==================================\n");

        } // end of if
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
          blm.deleteAssociations(associationKeys);
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
   * @testName: businessLifeCycleManager_deleteServiceBindingsTest
   *
   * @assertion_ids: JAXR:JAVADOC:294;
   *
   * @test_Strategy: Publish a service binding. Verify it was published. delete
   * it and verify the delete.
   */

  public void businessLifeCycleManager_deleteServiceBindingsTest()
      throws Fault {
    String testName = "businessLifeCycleManager_deleteServiceBindingsTest";
    String serviceName = "testService";
    String sbDescription = "my sb description";

    String conceptName = "my concept";
    Collection sbKeys = null;
    Collection serviceKeys = null;
    Collection orgKeys = null;
    Collection conceptKeys = null;
    javax.xml.registry.infomodel.Key conceptKey = null;
    javax.xml.registry.infomodel.Key serviceKey = null;
    javax.xml.registry.infomodel.Key serviceBindingKey = null;
    javax.xml.registry.infomodel.Key orgKey = null;
    javax.xml.registry.infomodel.Key sbKey = null;

    String accessURI = baseuri + "jaxrTestPage1.html";
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

      // now must create a service for a reference for the service binding.
      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      org.addService(service);
      Collection services = new ArrayList();
      services.add(service);
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService failed \n");
        throw new Fault(testName + " failed ");
      }
      serviceKeys = br.getCollection();
      iter = serviceKeys.iterator();
      while (iter.hasNext()) {
        serviceKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      service = (Service) bqm.getRegistryObject(serviceKey.getId(),
          LifeCycleManager.SERVICE);
      // create a concept
      Concept myConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      myConcept.setName(blm.createInternationalString(tsLocale, conceptName));
      Collection concepts = new ArrayList();
      concepts.add(myConcept);
      br = blm.saveConcepts(concepts);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveConcepts failed \n");
        throw new Fault(testName + " failed ");
      }
      conceptKeys = br.getCollection();
      iter = conceptKeys.iterator();
      while (iter.hasNext()) {
        conceptKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      myConcept = (Concept) bqm.getRegistryObject(conceptKey.getId(),
          LifeCycleManager.CONCEPT);
      // create a specification link
      SpecificationLink sl = blm.createSpecificationLink();
      sl.setSpecificationObject(myConcept);
      // create the service binding
      ServiceBinding sb = blm.createServiceBinding();
      sb.setDescription(blm.createInternationalString(tsLocale, sbDescription));
      sb.setAccessURI(accessURI);
      sb.addSpecificationLink(sl);
      service.addServiceBinding(sb);
      Collection sbs = new ArrayList();
      sbs.add(sb);
      br = blm.saveServiceBindings(sbs);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveServiceBinding failed \n");
        throw new Fault(testName + " failed ");
      }

      sbKeys = br.getCollection();
      iter = sbKeys.iterator();
      while (iter.hasNext()) {
        sbKey = (javax.xml.registry.infomodel.Key) iter.next();
      }
      Collection specifications = new ArrayList();
      specifications.add(myConcept);
      br = bqm.findServiceBindings(serviceKey, null, null, specifications);
      sbs = br.getCollection();
      debug.add("==>>  Published a service binding, Count found is : "
          + sbs.size() + "\n");
      iter = sbs.iterator();
      // Make sure that the service binding was published.
      boolean found = false;
      while (iter.hasNext()) {
        sb = (ServiceBinding) iter.next();
        Service myService = sb.getService();

        // look for my service
        if (myService.getName().getValue(tsLocale).equals(serviceName)) {
          debug.add("==>> access uri is " + sb.getAccessURI() + "\n");
          if (!(sb.getAccessURI().equals(accessURI))) {
            debug.add("Error: unexpected accessURI name \n");
            throw new Fault(testName
                + " did not complete due to errors saveing service binding ");
          }
        }

        if (found)
          continue;
      }

      // Now delete the service binding
      blm.deleteServiceBindings(sbKeys);
      // Now verify the delete
      br = bqm.findServiceBindings(serviceKey, null, null, specifications);
      sbs = br.getCollection();

      if (sbs.contains(sb)) {
        debug.add("Failed! - sb was not removed \n");
        throw new Fault(testName + " Error: did not delete service binding ");
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      // clean up
      try {
        if (conceptKeys != null) {
          debug.add("==>>  Cleanup: delete concept\n");
          blm.deleteConcepts(conceptKeys);
        }
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
   * @testName: businessLifeCycleManager_saveServicesTest
   *
   * @assertion_ids: JAXR:JAVADOC:276;
   *
   * @test_Strategy: Publish a service. Verify it was published. Change the name
   * and publish again. Verify that the service was updated.
   */

  public void businessLifeCycleManager_saveServicesTest() throws Fault {
    String testName = "businessLifeCycleManager_saveServicesTest";
    String serviceName = "testService";
    String updServiceName = "UpdatedtestService";
    int passCount = 0;
    Collection serviceKeys = null;
    Collection orgKeys = null;
    javax.xml.registry.infomodel.Key serviceKey = null;
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

      // now must create a service
      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      org.addService(service);
      Collection services = new ArrayList();
      services.add(service);
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService failed \n");
        throw new Fault(testName + " failed ");
      }
      serviceKeys = br.getCollection();

      iter = serviceKeys.iterator();
      while (iter.hasNext()) {
        serviceKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      service = (Service) bqm.getRegistryObject(serviceKey.getId(),
          LifeCycleManager.SERVICE);
      if (service != null) {
        if (service.getName().getValue(tsLocale).equals(serviceName)) {
          debug.add("Good - service was published\n");
          passCount = 1;
        } else
          debug.add("Error: failed to get name of service \n");
      }
      // update service
      service.setName(blm.createInternationalString(tsLocale, updServiceName));
      services.clear();
      services.add(service);
      br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("==>>  Error:    saveService Update failed \n");
        throw new Fault(testName + " failed ");
      }
      // retrieve and verify the update
      service = (Service) bqm.getRegistryObject(serviceKey.getId(),
          LifeCycleManager.SERVICE);
      if (service != null) {
        if (service.getName().getValue(tsLocale).equals(updServiceName)) {
          debug.add("Good - service was update\n");
          passCount = 2;
        } else
          debug.add("Error: failed to get updated name of service \n");
      }
      if (passCount != 2)
        throw new Fault(testName + " failed ");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      // clean up
      try {
        if (serviceKeys != null) {
          blm.deleteServices(serviceKeys);
          debug.add("Cleanup: delete service\n");
        }
        if (orgKeys != null) {
          debug.add("Cleanup: delete organization\n");
          blm.deleteOrganizations(orgKeys);
        }
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Error: not able to delete organization \n");
      }
    }

  } // end of method

  /*
   * @testName: BusinessLifeCycleManager_SaveClassificationSchemes
   *
   * @assertion_ids: JAXR:JAVADOC:284;
   *
   * @test_Strategy: Create a ClassificationScheme object that is not already in
   * the registry and verify. Verify that it was published.
   *
   */
  public void BusinessLifeCycleManager_SaveClassificationSchemes()
      throws Fault {
    // get the Connection object conn from the super class
    String testName = "BusinessLifeCycleManager_SaveClassificationSchemes";
    boolean pass = true;
    BusinessQueryManager bqm = null;
    String name = "TS TEST Name:Test BusinessLifeCycleManager_SaveClassificationSchemes";
    String descr = "Description: Test BusinessLifeCycleManager_SaveClassificationSchemes";
    ClassificationScheme classificationscheme = null;
    BulkResponse br = null;
    Collection keys = null;
    Collection classificationschemes = null;
    javax.xml.registry.infomodel.Key ClassificationschemeKey = null;

    try {
      bqm = rs.getBusinessQueryManager();
      classificationscheme = blm.createClassificationScheme(name, descr);

      debug.add("classificationscheme name is: "
          + classificationscheme.getName().getValue());
      Collection schemes = new ArrayList();
      schemes.add(classificationscheme);

      br = blm.saveClassificationSchemes(schemes);
      debug.add("check br for classificationscheme save\n");

      if (!(JAXR_Util.checkBulkResponse("saveClassificationschemes", br,
          debug))) {
        debug.add("Error reported:  saveClassificationschemes\n");
        throw new Fault(testName
            + " Error from saveClassificationschemes - test did not complete!");
      }

      keys = br.getCollection(); // get a collection of keys
      if (keys.size() == 0) {
        throw new Fault(testName + "Error: no key was returned \n");
      }
      if (keys.size() > 1) {
        throw new Fault(testName + "Error: too many keys were returned \n");
      }

      Iterator iter = keys.iterator();
      while (iter.hasNext()) {
        ClassificationschemeKey = (Key) iter.next();
      }

      String keyId = ClassificationschemeKey.getId();
      classificationscheme = (ClassificationScheme) bqm.getRegistryObject(keyId,
          LifeCycleManager.CLASSIFICATION_SCHEME);

      if (classificationscheme.getName().getValue().equals(name)) {
        debug.add("Got back expected classificationscheme Name!\n");
      } else {
        pass = false;
        debug.add("Error: returned name not as expected! \n");
        debug.add("Expected name: " + name + "\n");
        debug.add("Returned name: " + classificationscheme.getName().getValue()
            + "\n");
      }
      if (classificationscheme.getDescription().getValue().equals(descr)) {
        debug.add("Got back expected classificationscheme Description! \n");
      } else {
        pass = false;
        debug.add("Error: returned description not as expected! \n");
        debug.add("Expected description: " + descr + "\n");
        debug.add("Returned description: "
            + classificationscheme.getDescription().getValue() + "\n");
      }
      // cleanup - remove the classificationscheme.
      br = blm.deleteClassificationSchemes(keys);

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

  private Concept getAssociationConcept(String associationType) {
    Collection atypes = new ArrayList();
    atypes.add("AssociationType");

    try {
      Locale en_US = new Locale("en", "US");
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

}
