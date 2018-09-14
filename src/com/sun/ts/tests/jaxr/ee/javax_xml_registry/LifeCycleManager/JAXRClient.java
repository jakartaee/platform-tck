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

package com.sun.ts.tests.jaxr.ee.javax_xml_registry.LifeCycleManager;

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

  // ================================================
  RegistryService rs2 = null;

  BusinessLifeCycleManager blm2 = null;

  com.sun.ts.tests.jaxr.common.JAXR_Util util = null;

  Properties props;

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
      props = p;
      super.setup(args, p);
      super.cleanUpRegistry(); // clean "TS Default Organization"

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
   * @testName: lifeCycleManager_OrganizationDeleteObjectsTest
   *
   * @assertion_ids:
   * JAXR:JAVADOC:840;JAXR:JAVADOC:197;JAXR:SPEC:184;JAXR:SPEC:182;
   * JAXR:SPEC:192;JAXR:SPEC:203;JAXR:SPEC:228;
   * 
   * @test_Strategy: Create an organization. Before saving make sure that it is
   * not in the registry. Use saveObjects to save the organization. Verify that
   * the organization has been saved. Invoke deleteObjects to remove the
   * organization from the registry. Verify that the organization has been
   * removed.
   *
   */
  public void lifeCycleManager_OrganizationDeleteObjectsTest() throws Fault {
    String testName = "lifeCycleManager_OrganizationDeleteObjectsTest()";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    Key orgKey = null;
    try {
      Collection objects = new ArrayList();
      // Make sure orgName is not in the registry first
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      objects.add(org);
      // BulkResponse br = blm.saveOrganizations(objects);
      // returns a Collection of keys for those objects that were saved
      // successfully
      BulkResponse br = blm.saveObjects(objects);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      // Verify that the organization was created
      Collection keys = br.getCollection();
      Iterator iter = keys.iterator();
      while (iter.hasNext()) {
        orgKey = (Key) iter.next();
      }
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Organization myOrg = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);
      if (!(myOrg.getName().getValue(tsLocale).equals(orgName)))
        throw new Fault(testName + "Error: organization was not created\n");
      else
        debug.add("Success: Organization was created\n");

      br = blm.deleteObjects(keys, LifeCycleManager.ORGANIZATION);
      if (!(JAXR_Util.checkBulkResponse("deleteObjects", br, debug))) {
        debug.add("Error:    deleteObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      // Verify the delete
      br = bqm.getRegistryObjects();
      if (!(JAXR_Util.checkBulkResponse("getRegistryObjects", br, debug))) {
        debug.add("Error:    getRegistryObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }

      Collection objs = br.getCollection();
      iter = objs.iterator();
      while (iter.hasNext()) {
        RegistryObject ro = (RegistryObject) iter.next();
        // Just make sure that the organization was really deleted.
        if (ro instanceof Organization) {
          if (ro.getKey().getId().equals(orgKey.getId())) {
            throw new Fault(testName + "Error: organization was not deleted\n");
          }
        }
      } // while

    } catch (JAXRException je) {
      TestUtil.logErr("Caught jaxr exception: " + je.getMessage());
      TestUtil.printStackTrace(je);
      throw new Fault(testName + "failed", je);

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method

  /*
   * @testName: lifeCycleManager_ServiceSaveDeleteObjectsTest
   *
   * @assertion: saveObjects - Saves one or more Objects to the registry.
   * deleteObjects - Deletes one or more previously submitted objects from the
   * registry
   *
   * @assertion_ids: JAXR:SPEC:178;JAXR:SPEC:180;JAXR:SPEC:191;
   * 
   * @test_Strategy: Create a Service. Before saving make sure that it is not in
   * the registry. Use saveObjects to save the service. Verify that the service
   * has been saved. Invoke deleteObjects to remove the service from the
   * registry. Verify that the service has been removed.
   *
   */
  public void lifeCycleManager_ServiceSaveDeleteObjectsTest() throws Fault {
    String testName = "lifeCycleManager_ServiceSaveDeleteObjectsTest";
    String serviceName = "Service_saveDeleteObjectsTestForService";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    BulkResponse br = null;
    Key orgKey = null;
    Iterator iter = null;
    boolean pass = false;
    try {
      Collection services = new ArrayList();
      BusinessQueryManager bqm = rs.getBusinessQueryManager();

      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      // create and save an organization
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      Collection orgs = new ArrayList();
      orgs.add(org);
      // save the organization
      br = blm.saveOrganizations(orgs);
      if (!(JAXR_Util.checkBulkResponse("saveOrganization", br, debug))) {
        debug.add("Error:    saveOrganization failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      Collection orgKeys = br.getCollection();
      iter = orgKeys.iterator();
      while (iter.hasNext()) {
        orgKey = (Key) iter.next();
      }

      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      if (br.getExceptions() != null) {
        debug.add("Error from saveOrganizations \n");
      }

      Collection myOrgs = br.getCollection();
      iter = myOrgs.iterator();
      Organization myOrg = null;
      while (iter.hasNext()) {
        // iterate thru registryObject collection
        myOrg = (Organization) iter.next();
        myOrg.addService(service);
        services.add(service);
      }
      // save service
      // returns a Collection of keys for those objects that were saved
      // successfully
      // br = blm.saveServices(services);
      br = blm.saveObjects(services);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveServices failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      Collection servicekeys = br.getCollection();
      // verify save
      Collection serviceNames = new ArrayList();
      serviceNames.add(serviceName);
      debug.add("And service count returns: "
          + serviceCount(orgKey, serviceNames) + "\n");

      // delete service
      blm.deleteObjects(servicekeys, LifeCycleManager.SERVICE);
      if (!(JAXR_Util.checkBulkResponse("deleteObjects", br, debug))) {
        debug.add("Error:    deleteObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      debug.add("deleteObject method called.  Service count should be 0\n");
      debug.add("Service count returns: " + serviceCount(orgKey, serviceNames));

      if (serviceCount(orgKey, serviceNames) == 0)
        debug.add("Good service was deleted\n");
      else
        debug.add("Error: service was not deleted\n");
      // delete the organization
      blm.deleteObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      if (!(JAXR_Util.checkBulkResponse("deleteObjects", br, debug))) {
        debug.add("Error:    deleteObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  } // end of test method
  /*
   * @testName: lifeCycleManager_saveObjectsUpdateTest
   *
   * @assertion_ids: JAXR:SPEC:193;JAXR:SPEC:212;JAXR:SPEC:226;JAXR:SPEC:202;
   * 
   * @test_Strategy: Create an organization. Use saveObjects to save the
   * organization. Modify the organaization and resave Save and verify the
   * change.
   *
   */

  public void lifeCycleManager_saveObjectsUpdateTest() throws Fault {
    String testName = "lifeCycleManager_saveObjectsUpdateTest()";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String updOrgName = "MyUpdatedName";
    Collection keys = null;
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Collection objects = new ArrayList();
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      objects.add(org);

      BulkResponse br = blm.saveObjects(objects);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      keys = br.getCollection();
      // Verify that the organization was created
      br = bqm.getRegistryObjects(keys, LifeCycleManager.ORGANIZATION);
      Collection orgs = br.getCollection();
      Iterator iter = orgs.iterator();
      Organization myOrg = null;
      while (iter.hasNext()) {
        myOrg = (Organization) iter.next();
      }
      if (myOrg.getName().getValue(tsLocale).equals(orgName)) {
        debug.add("Good: org was published.  Now do an update.");
      } else {
        debug.add("Oops! org was not published!. Error!!.");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      myOrg.setName(blm.createInternationalString(tsLocale, updOrgName));
      objects.clear();
      objects.add(myOrg);
      br = blm.saveObjects(objects);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      // verify the update to the organization
      br = bqm.getRegistryObjects(keys, LifeCycleManager.ORGANIZATION);
      orgs = br.getCollection();
      iter = orgs.iterator();
      myOrg = null;
      while (iter.hasNext()) {
        myOrg = (Organization) iter.next();
      }
      if (myOrg.getName().getValue(tsLocale).equals(updOrgName)) {
        debug.add("Good: org was updated.  ");
      } else {
        debug.add("Oops! org was not updated!. Error!!.");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    } finally {
      try {
        blm.deleteObjects(keys, LifeCycleManager.ORGANIZATION);
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Warning: could not remove the organization\n");
      }
    }
  } // end of test method
  /*
   * @testName: lifeCycleManager_saveMultiObjectsTest
   *
   * @assertion_ids: JAXR:SPEC:194;
   * 
   * @test_Strategy: Create an organization and a service Use saveObjects to
   * save the them. Verify that both objects are saved to the registry.
   *
   */

  public void lifeCycleManager_saveMultiObjectsTest() throws Fault {
    String testName = "lifeCycleManager_saveMultiObjectsTest()";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String serviceName = "myTestService";
    String updOrgName = "updatedOrgName";
    String updServiceName = "updatedTestService";

    Collection keys = null;
    Collection orgKeys = null;
    Collection serviceKeys = null;
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Collection objects = new ArrayList();
      Collection services = new ArrayList();

      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      objects.add(org);
      BulkResponse br = blm.saveObjects(objects);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      debug.add(
          "Good.  No errors were returned from saveObjects for organization \n");
      // save the org keys
      orgKeys = br.getCollection();
      // ==
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      if (br.getExceptions() != null) {
        debug.add("Error from saveOrganizations \n");
      }
      Collection myOrgs = br.getCollection();
      Iterator iter = myOrgs.iterator();
      Organization myOrg = null;
      while (iter.hasNext()) {
        myOrg = (Organization) iter.next();
        myOrg.addService(service);
        services.add(service);
      }
      br = blm.saveObjects(services);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveServices failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      serviceKeys = br.getCollection();

      // ==
      debug.add(
          "Good.  No errors were returned from saveObjects for service \n");
      // ==
      // get the org from the registry
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      Collection orgs = br.getCollection();
      iter = orgs.iterator();
      myOrg = null;
      while (iter.hasNext()) {
        myOrg = (Organization) iter.next();
      }
      // update the organization - change its name.
      myOrg.setName(blm.createInternationalString(tsLocale, updOrgName));

      // get the service from the registry
      br = bqm.getRegistryObjects(serviceKeys, LifeCycleManager.SERVICE);
      services = br.getCollection();
      iter = services.iterator();
      Service myService = null;
      while (iter.hasNext()) {
        myService = (Service) iter.next();
      }
      // update the service - change its name.
      myService
          .setName(blm.createInternationalString(tsLocale, updServiceName));

      objects.clear();
      objects.add(myOrg);
      objects.add(myService);
      br = blm.saveObjects(objects);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      // verify the updates to the organization and service
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      orgs = br.getCollection();
      iter = orgs.iterator();
      myOrg = null;
      while (iter.hasNext()) {
        myOrg = (Organization) iter.next();
      }
      if (myOrg.getName().getValue(tsLocale).equals(updOrgName)) {
        debug.add("Good: org was updated.  ");
      } else {
        debug.add("Oops! org was not updated!. Error!!.");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      // now do the service
      br = bqm.getRegistryObjects(serviceKeys, LifeCycleManager.SERVICE);
      services = br.getCollection();
      iter = services.iterator();
      myService = null;
      while (iter.hasNext()) {
        myService = (Service) iter.next();
      }
      if (myService.getName().getValue(tsLocale).equals(updServiceName)) {
        debug.add("Good: service was updated.  ");
      } else {
        debug.add("Oops! Service was not updated!. Error!!.");
        throw new Fault(testName + " due to errors, test did not complete!");
      }

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    } finally {
      try {
        // now delete it
        BulkResponse br = blm.deleteObjects(orgKeys,
            LifeCycleManager.ORGANIZATION);
        if (!(JAXR_Util.checkBulkResponse("deleteObjects", br, debug))) {
          debug.add(
              "Cleanup Error:    deleteObjects failed for Organization \n");
        }
        br = blm.deleteObjects(serviceKeys, LifeCycleManager.SERVICE);
        if (!(JAXR_Util.checkBulkResponse("deleteObjects", br, debug))) {
          debug.add("Cleanup Error:    deleteObjects failed for Service \n");
        }
      } catch (Exception ee) {
        TestUtil.logErr("Caught unexpected exception: " + ee.getMessage());
        TestUtil.printStackTrace(ee);
      }
    }

  } // end of test method

  /*
   * @testName: lifeCycleManager_saveObjectChangeCredTest
   *
   * @assertion_ids: JAXR:SPEC:165;JAXR:SPEC:166;JAXR:SPEC:175;
   * JAXR:SPEC:287;JAXR:SPEC:284;JAXR:SPEC:275;
   * 
   * @test_Strategy: Create an organization. Use saveObjects to save the
   * organization. Switch to a different user. Verify that new user cannot
   * update the org. Switch back and delete.
   */

  public void lifeCycleManager_saveObjectChangeCredTest() throws Fault {
    String testName = "lifeCycleManager_saveObjectChangeCredTest()";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String updOrgName = "MyUpdatedName";
    Collection keys = null;
    boolean pass = false;

    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Collection objects = new ArrayList();

      // Create a new organization
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      objects.add(org);

      // Save the organization
      BulkResponse br = blm.saveObjects(objects);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }

      // Verify that the organization was created
      keys = br.getCollection();
      br = bqm.getRegistryObjects(keys, LifeCycleManager.ORGANIZATION);
      Collection orgs = br.getCollection();
      Iterator iter = orgs.iterator();
      Organization myOrg = null;
      while (iter.hasNext()) {
        myOrg = (Organization) iter.next();
      }
      if (myOrg.getName().getValue(tsLocale).equals(orgName)) {
        debug.add("Good: org was published.  Now change the user.");
      } else {
        debug.add("Oops! org was not published!. Error!!.");
        throw new Fault(testName + " due to errors, test did not complete!");
      }

      myOrg.setName(blm.createInternationalString(tsLocale, updOrgName));
      objects.clear();
      objects.add(myOrg);

      // change credentials to a different user and then try to write
      Set credentials = JAXR_Util.doCredentials(jaxrSecurityCredentialType,
          props, "user2");
      // change connection creds
      conn.setCredentials(credentials);
      // get the blm
      rs = conn.getRegistryService();
      BusinessLifeCycleManager blm = rs.getBusinessLifeCycleManager();

      try {
        br = blm.saveObjects(objects);
        if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
          debug.add("Update to org from different user failed as expected");
          pass = true;
        }
      } catch (Exception exx) {
        debug.add("Update to org from different user failed as expected");
        debug.add("Exception thrown:" + exx.getMessage());
      }

      credentials.clear();
      // now delete it with user1
      credentials = JAXR_Util.doCredentials(jaxrSecurityCredentialType, props,
          "user1");
      credentials.add(passwdAuth);
      // change connection creds
      conn.setCredentials(credentials);
      // get the blm
      rs = conn.getRegistryService();
      blm = rs.getBusinessLifeCycleManager();
      br = blm.deleteObjects(keys, LifeCycleManager.ORGANIZATION);
      if (!(JAXR_Util.checkBulkResponse("deleteObjects", br, debug))) {
        debug.add("Cleanup Error:    deleteObjects failed \n");
      }

    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    }
  }

  /*
   * testName: lifeCycleManager_deleteAssociationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:300;
   *
   * @test_Strategy: Create and publish an association. Verify that it was
   * created. Delete it. Verify the delete.
   *
   */
  public void lifeCycleManager_deleteAssociationsTest() throws Fault {
    String testName = "lifeCycleManager_deleteAssociationsTest";

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

      debug.add("Create and publish a source and target organization \n");
      Organization target = blm2.createOrganization(
          blm.createInternationalString(tsLocale, orgTarget));
      Organization source = blm.createOrganization(
          blm.createInternationalString(tsLocale, orgSource));
      // ==
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
        debug.add("Saving the source key ");
        savekey = (Key) iter.next();
      }
      sourceId = savekey.getId();
      debug.add("THe saved source key is " + sourceId);
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
        debug.add("Saving the target key ");
      }
      targetId = savekey.getId();
      debug.add("THe saved target key is " + targetId);
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

      debug.add("create an association \n");
      Association a = blm2.createAssociation(pubTarget, associationType);
      a.setSourceObject(pubSource);

      debug.add("both users confirm the association");
      blm.confirmAssociation(a);
      blm2.confirmAssociation(a);

      // publish the Association
      Collection associations = new ArrayList();
      associations.add(a);
      // user2 saves the association.
      br = blm2.saveAssociations(associations, false);
      associationKeys = br.getCollection();
      iter = associationKeys.iterator();
      Key assocKey = (Key) iter.next();
      debug
          .add("This is the key to the saved association: " + assocKey.getId());

      if (!(JAXR_Util.checkBulkResponse("saveAssociations", br, debug))) {
        debug.add("Error:    saveAssociations failed \n");
        throw new Fault(testName + "did not complete due to errors");
      }
      //
      br = bqm.findCallerAssociations(null, new Boolean(true),
          new Boolean(true), null);

      if (br.getExceptions() == null) {
        Collection results = br.getCollection();
        debug.add("Associations returned from findCallerAssociations is "
            + results.size() + "\n");
        if (results.size() > 0) {
          iter = results.iterator();
          a = null;
          Association a1 = null;
          while (iter.hasNext()) {
            a1 = (Association) iter.next();
            if (a1.getKey().getId().equals(assocKey.getId())) {
              debug.add(" found my key with findCallerAssociatons = "
                  + a1.getKey().getId());
              a = a1;
              break;
            }

          } // end of while
          if (a == null)
            throw new Fault(testName + "did not complete due to errors");
          assockey = a1.getKey();
          Organization o = (Organization) a1.getSourceObject();
          debug.add("Source object " + o.getName().getValue(tsLocale) + "\n");
          if (o.getName().getValue(tsLocale).equals(orgSource)) {
            debug.add("Good - found association with findAssociation \n");
          }
          o = (Organization) a1.getTargetObject();
          debug.add("Target object " + o.getName().getValue(tsLocale) + "\n");
          Concept atype = a1.getAssociationType();
          debug.add("type is " + atype.getName().getValue() + "\n");
          debug.add("==================================\n");

          // } // end while
        } // end of if
      } // end if
      if (assockey != null) {
        Collection keys = new ArrayList();
        keys.add(assockey);
        debug.add("Now delete the association \n");
        blm2.deleteAssociations(keys);
        debug.add("Verify the delete - try to find the deleted associtaion \n");
        br = bqm.findCallerAssociations(null, new Boolean(true),
            new Boolean(true), null);
        if (br.getExceptions() == null) {
          Collection retAssocs = br.getCollection();
          iter = retAssocs.iterator();
          a = null;
          while (iter.hasNext()) {
            Association ass = (Association) iter.next();
            debug.add(" making sure my association is deleted");
            if (ass.getKey().getId().equals(assocKey.getId())) {
              debug.add(" Found the deleted association -  this is unexpected");
              debug.add(" Found this key " + ass.getKey().getId());
              a = ass;
              break;
            }
          }
          if (a == null) {
            debug.add("good - no association was found \n");
            pass = true;
          }
        }
      }
      if (!pass)
        throw new Fault(testName + " failed ");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      // clean up - get rid of published orgs
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

  } // end of method

  /*
   * @testName: lifeCycleManager_deleteObjectsTest
   *
   * @assertion_ids: JAXR:JAVADOC:840
   *
   * @test_Strategy: Create an organization. Before saving make sure that it is
   * not in the registry. Use saveObjects to save the organization. Verify that
   * the organization has been saved. Invoke deleteObjects to remove the
   * organization from the registry. Verify that the organization has been
   * removed.
   *
   */
  public void lifeCycleManager_deleteObjectsTest() throws Fault {
    String testName = "lifeCycleManager_deleteObjectsTest";
    String associationName = "associationSaveDeleteObjectsTest";
    String serviceName = "serviceSaveDeleteObjectsTest";
    Collection keys = null;
    int providerlevel = 0;
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      Collection objects = new ArrayList();
      // Make sure orgName is not in the registry first
      String orgName = "lifeCycleManager_deleteObjectsTest_organization";
      InternationalString iorgName = blm.createInternationalString(tsLocale,
          orgName);
      Organization org = blm.createOrganization(iorgName);
      objects.add(org);
      // returns a Collection of keys for those objects that were saved
      // successfully
      BulkResponse br = blm.saveObjects(objects);
      if (!(JAXR_Util.checkBulkResponse("saveObjects", br, debug))) {
        debug.add("Error:    saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      // Verify that the organization was created
      keys = br.getCollection();
      Iterator iter = keys.iterator();
      Key orgKey = null;
      while (iter.hasNext()) {
        orgKey = (Key) iter.next();
      }
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      Organization myOrg = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);
      if (!(myOrg.getName().getValue(tsLocale).equals(orgName)))
        throw new Fault(testName + "Error: organization was not created\n");
      else
        debug.add("Success: Organization was created\n");

      br = blm.deleteObjects(keys);
      if (providerlevel == 0)
        // deleteObjects(collection) is a level 1 method.
        throw new Fault(
            testName + " expected UnsupportedCapabilityException \n");
      if (!(JAXR_Util.checkBulkResponse("deleteObjects", br, debug))) {
        debug.add("Error:    deleteObjects failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      // Verify the delete
      Organization deletedOrg = (Organization) bqm
          .getRegistryObject(orgKey.getId(), LifeCycleManager.ORGANIZATION);
      if (deletedOrg != null)
        throw new Fault(testName + "Error: organization was not deleted\n");
      else
        debug.add("Success: Organization was deleted \n");

    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        debug.add(
            " Call to deleteObjects(keys) threw UnsupportedCapabilityException as expected\n");
      } else
        throw new Fault(
            testName
                + " unexpected UnsupportedCapabilityException was caught \n",
            uc);
    } catch (Exception ue) {
      TestUtil.logErr("Caught unexpected exception: " + ue.getMessage());
      TestUtil.printStackTrace(ue);
      throw new Fault(testName + "failed", ue);
    } finally {
      debug.add("cleanup registry for level 0 - remove test organization\n");
      try {
        blm.deleteObjects(keys, LifeCycleManager.ORGANIZATION);
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Warning: could not remove the organization\n");
      }
    }

  } // end of test method

  /*
   * @testName: LifeCycleManager_deprecateObjectsTest
   *
   * @assertion_ids:
   * JAXR:JAVADOC:199;JAXR:JAVADOC:201;JAXR:SPEC:12;JAXR:SPEC:13;
   *
   * @test_Strategy: A level 0 provider should throw an
   * UnsupportedCapabilityException error when this method is invoked. Invoke
   * the method and verify that level 0 providers return this error
   *
   */
  public void LifeCycleManager_deprecateObjectsTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "LifeCycleManager_deprecateObjectsTest";
    int failcount = 0;
    Collection orgKeys = null;

    Collection serviceKeys = null;
    Collection myServiceKeys = null;
    Service service = null;
    Service myService = null;
    Organization myOrg = null;
    BusinessQueryManager bqm = null;
    int providerlevel = 0;
    Collection debug = new ArrayList();
    try {
      String serviceName = testName + "_service";
      bqm = rs.getBusinessQueryManager();
      debug.add("Create a service registryObject \n");
      service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      orgKeys = new ArrayList();
      // ==
      InternationalString iName = blm.createInternationalString(tsLocale,
          testName + "_organization");
      myOrg = blm.createOrganization(iName);
      orgKeys = saveMyOrg(myOrg);
      Iterator iter = orgKeys.iterator();
      Key orgKey = null;
      while (iter.hasNext()) {
        orgKey = (javax.xml.registry.infomodel.Key) iter.next();
      }

      myOrg = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);
      myService = blm.createService(
          blm.createInternationalString(tsLocale, testName + "_myService"));
      myOrg.addService(myService);
      myServiceKeys = saveMyService(myService);

      // ==

      if (myServiceKeys.size() == 0)
        throw new Fault(
            testName + " due to save service errors , test did not complete!");
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      try {
        debug.add("Deprecate the service \n");
        BulkResponse br = blm.deprecateObjects(myServiceKeys);
        if (providerlevel == 0) {
          debug.add(
              "level 0 providers must throw UnsupportedCapabilityException \n");
          throw new Fault(testName + " failed");
        }

        serviceKeys = saveMyService(service);
        br = blm.deprecateObjects(serviceKeys);

        // check deprecation
        boolean IllegalStateExceptionThrown = false;
        try {
          myOrg.addService(service);
        } catch (IllegalStateException is) {
          TestUtil.printStackTrace(is);
          IllegalStateExceptionThrown = true;
          debug.add("IllegalStateException was thrown!!\n");
        }
        if (!IllegalStateExceptionThrown) {
          failcount = failcount = 1;
          debug.add("Expected IllegalStateException was not thrown\n");
        }

      } catch (UnsupportedCapabilityException uce) {
        TestUtil.printStackTrace(uce);
        if (providerlevel != 0)
          failcount = failcount = 1;
        debug.add("UnsupportedCapabilityException was thrown!!\n");
      } catch (Exception uk) {
        debug.add("Error: unexpected exception was thrown\n");
        TestUtil.printStackTrace(uk);
        failcount = failcount + 1;
      }
      debug.add("invoke unDeprecateObjects\n");
      try {
        BulkResponse br = blm.unDeprecateObjects(myServiceKeys);
        if (providerlevel == 0) {
          debug.add(
              "level 0 providers must throw UnsupportedCapabilityException \n");
          throw new Fault(testName + " failed");
        }
        br = blm.unDeprecateObjects(serviceKeys);
        // should now be able to add a service.
        myOrg.addService(service);
        saveMyOrg(myOrg);
        Collection services = myOrg.getServices();
        iter = services.iterator();
        while (iter.hasNext()) {
          Service s = (Service) iter.next();
          debug.add("my org has this service: " + s.getName().getValue(tsLocale)
              + "\n");
        }
      } catch (IllegalStateException is) {
        TestUtil.printStackTrace(is);
        failcount = failcount + 1;
        debug.add("IllegalStateException was thrown!!\n");
      } catch (UnsupportedCapabilityException uce) {
        TestUtil.printStackTrace(uce);
        if (providerlevel != 0)
          failcount = failcount + 1;
        debug.add("UnsupportedCapabilityException was thrown!!\n");
      } catch (Exception uk) {
        debug.add("Error: unexpected exception was thrown\n");
        TestUtil.printStackTrace(uk);
        failcount = failcount + 1;
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
      debug.add("cleanup registry - remove test organization\n");
      try {
        // level 0 providers do not need to delete next line.
        if (providerlevel != 0)
          blm.deleteServices(serviceKeys);
        blm.deleteServices(myServiceKeys);
        blm.deleteOrganizations(orgKeys);
        if (debug != null)
          TestUtil.logTrace(debug.toString());
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Warning: could not remove the organization\n");
      }
    }
    if (failcount != 0)
      throw new Fault(testName + "failed ");
  } // end of test method

  /*
   * This method is called to determine how many if any organizations are in the
   * registry with this name.
   *
   * @param orgKey - the organization key.
   * 
   * @param services - name of the services.
   * 
   * @return the number of services found or -1 if an error is encountered.
   *
   */
  private int serviceCount(Key orgKey, Collection services) {
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      BulkResponse br = bqm.findServices(orgKey, null, services, null, null);

      if (!(JAXR_Util.checkBulkResponse("findServices ", br, debug))) {
        debug.add("Error reported - findServices failed \n");
        return -1;
      }
      Collection s = br.getCollection();
      return s.size();
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return -1;
    }
  }

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

  private Collection saveMyService(Service service) {
    Collection services = new ArrayList();
    Collection serviceKeys = null;
    services.add(service);
    try {
      BulkResponse br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("Error:   saveServices failed \n");
        return null;
      }
      serviceKeys = br.getCollection();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return serviceKeys;
  } // end of saveMyService method

  private Collection saveMyOrg(Organization org) {
    Collection orgs = new ArrayList();
    Collection orgKeys = null;
    orgs.add(org);
    try {
      BulkResponse br = blm.saveOrganizations(orgs);
      if (br.getExceptions() != null) {
        debug.add("Error:   saveOrganizations failed \n");
        return null;
      }
      orgKeys = br.getCollection();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return orgKeys;
  } // end of saveMyOrg method
} // end of class
