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

package com.sun.ts.tests.jaxr.ee.javax_xml_registry.QueryManager;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.JAXRCommonClient;
import com.sun.ts.tests.jaxr.common.JAXR_Util;
import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import com.sun.javatest.Status;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import java.net.PasswordAuthentication;

public class JAXRClient extends JAXRCommonClient {
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
   *
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
      // super.cleanUpRegistry();
      if (conn != null) {
        logTrace("Cleanup is closing the connection");
        conn.close();
      }
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
    } finally {
      if (debug != null)
        TestUtil.logTrace(debug.toString());
    }

  }

  /*
   * @testName: queryManager_getRegistryObject
   *
   * @assertion_ids: JAXR:JAVADOC:72;
   *
   * @test_Strategy: Create and save an organization. Get the key for the saved
   * organization. From the key, get the String key id. Use it to get an
   * organization from getRegistryObject(id, objectType) Verify that the
   * organization returned is a match to the saved organization.
   * 
   *
   */
  public void queryManager_getRegistryObject() throws Fault {
    String testName = "queryManager_getRegistryObject";
    String objectType = LifeCycleManager.ORGANIZATION;
    Key savekey = null;
    Collection orgKeys = null;
    BusinessQueryManager bqm = null;
    try {
      bqm = rs.getBusinessQueryManager();
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);

      org.setName(blm.createInternationalString(tsLocale,
          JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry

      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        savekey = (Key) iter.next();
      }
      String id = savekey.getId();
      Organization retOrg = (Organization) bqm.getRegistryObject(id,
          objectType);

      if (retOrg == null)
        throw new Fault(testName + " failed - retOrg is null!");

      debug.add(
          "Verify that the organization returned by getCollection matches \n");
      debug.add("name of returned org: " + retOrg.getName().getValue(tsLocale)
          + "\n");
      debug.add(
          "name of save org: " + JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME + "\n");
      if (!(retOrg.getName().getValue(tsLocale)
          .equals(JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME)))
        throw new Fault(testName + " failed - did not expected organization ");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }
  }

  /*
   * @testName: queryManager_getRegistryObjects
   *
   * @assertion_ids: JAXR:JAVADOC:78;
   *
   * @test_Strategy: Create and save an organization. With the key from the
   * saved organization, save the String key id. Call getRegistryObjects to get
   * the objects owned by the caller. Verify with the key id that the saved
   * Organization was returned.
   *
   */
  public void queryManager_getRegistryObjects() throws Fault {
    String testName = "queryManager_getRegistryObjects";
    Key savekey = null;
    boolean pass = false;
    BusinessQueryManager bqm = null;
    Collection orgKeys = null;
    try {
      bqm = rs.getBusinessQueryManager();
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);
      org.setName(blm.createInternationalString(tsLocale,
          JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry

      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        savekey = (Key) iter.next();
      }
      String myKeyId = savekey.getId();
      debug.add("Saved Organization key id is: " + myKeyId + "\n");

      br = bqm.getRegistryObjects();
      Collection myObjects = br.getCollection();
      iter = myObjects.iterator();
      RegistryObject ro = null;
      debug.add("Number of objects returned: " + myObjects.size() + "\n");
      while (iter.hasNext()) {
        ro = (RegistryObject) iter.next();
        debug.add("Object to string is " + ro.toString() + "\n");
        debug.add("Object keyId is " + ro.getKey().getId() + "\n");
        if (ro.getKey().getId().equals(myKeyId)) {
          pass = true;
          break;
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }
    if (!pass)
      throw new Fault(testName + " failed");
  }

  /*
   * @testName: queryManager_getRegistryObjectsSpecifiedType
   *
   * @assertion_ids: JAXR:JAVADOC:80;
   *
   * @test_Strategy: Create and save an organization. Create and save a service.
   * Request only service be returned. Verify that only the service is returned.
   *
   */
  public void queryManager_getRegistryObjectsSpecifiedType() throws Fault {
    String testName = "queryManager_getRegistryObjectsSpecifiedType";
    Key orgkey = null;
    BusinessQueryManager bqm = null;
    String myServiceName = JAXR_Util.TS_DEFAULT_SERVICE_NAME;
    Collection orgKeys = null;
    boolean pass = false;
    try {
      bqm = rs.getBusinessQueryManager();
      debug.add("Create a service and an organization \n");
      // Create a service.
      Service service = blm.createService(myServiceName);
      Collection myServices = new ArrayList();

      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);

      org.setName(blm.createInternationalString(tsLocale,
          JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      debug
          .add("Save the organization and get the key id from getCollection\n");
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry

      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        orgkey = (Key) iter.next();
      }
      String orgKeyId = orgkey.getId();
      debug.add("Saved Organization key id is: " + orgKeyId + "\n");
      debug.add("Call getRegistryObjects to get all owned objects\n");
      br = bqm.getRegistryObjects();
      debug.add("Find the saved organization and add the service to it \n");
      // get the org back
      Collection ros = br.getCollection();
      Organization o = null;
      iter = ros.iterator();
      String regKeyId = null;
      while (iter.hasNext()) {
        Object obj = iter.next();
        if (obj instanceof Organization) {
          o = (Organization) obj;
          if (o.getKey().getId().equals(orgKeyId)) {
            debug.add("Found the organization\n");
            regKeyId = o.getKey().getId();
            o.addService(service);
            myServices.add(service);
            break;
          }
        }
      }
      if (o == null) {
        debug.add(
            "Error: failed to get the Organization with getRegistryObjects \n");
        throw new Fault(testName + " failed");
      }
      // ==
      debug.add("save the service to the registry \n");
      br = blm.saveServices(myServices);
      Key servicekey = null;
      Collection serviceKeys = br.getCollection();
      debug.add("The number of service keys returned from getCollection is: "
          + serviceKeys.size() + "\n");
      iter = serviceKeys.iterator();
      while (iter.hasNext()) {
        servicekey = (Key) iter.next();
      }
      debug.add("Save the service key returned from saveServices\n");
      String serviceKeyId = servicekey.getId();
      debug.add("Saved Service key id is: " + serviceKeyId + "\n");

      debug.add(
          "request service objects with getRegistryObjects(LifeCycleManager.SERVICE) \n");
      br = bqm.getRegistryObjects(LifeCycleManager.SERVICE);
      // br = bqm.getRegistryObjects();

      Collection myObjects = br.getCollection();
      debug.add("Count of objects returned from service request is: "
          + myObjects.size() + "\n");
      if (myObjects.size() == 0)
        throw new Fault(
            testName + " failed - nothing returned from getRegistryObjects");

      iter = myObjects.iterator();
      RegistryObject ro = null;

      while (iter.hasNext()) {
        ro = (RegistryObject) iter.next();
        if (ro instanceof Service) {
          debug.add(" ro is a Service \n");
          if (ro.getKey().getId().equals(serviceKeyId)) {
            debug.add("Got back my service - Good! \n");
            pass = true;
          }
        } else if (!(ro instanceof Service)) {
          debug.add(" returned ro not a service! " + ro.toString() + "\n");
          throw new Fault(testName + " failed");
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      debug.add("cleanup at test end \n");
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }

    if (!pass)
      new Fault(testName + " failed - did not find service.");
  }

  /*
   * @testName: queryManager_getRegistryObjectStringId
   *
   * @assertion_ids: JAXR:JAVADOC:74;JAXR:SPEC:16;
   *
   * @test_Strategy: Create and save an organization. Get the key for the saved
   * organization. From the key, get the String key id. Use it to get an
   * organization from getRegistryObject(id) Verify that the organization
   * returned is a match to the saved organization.
   *
   * Level 0 Providers must throw UnsupportedCapabilityException for
   * QueryManager getRegistryObject(java.lang.String id)
   *
   *
   */
  public void queryManager_getRegistryObjectStringId() throws Fault {
    String testName = "queryManager_getRegistryObjectStringId";
    String objectType = LifeCycleManager.ORGANIZATION;
    Key savekey = null;
    Collection orgKeys = null;
    BusinessQueryManager bqm = null;
    int providerlevel = 0;
    try {
      bqm = rs.getBusinessQueryManager();
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);
      String orgName = testName + "_testOrganization";

      org.setName(blm.createInternationalString(tsLocale, orgName));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry

      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        savekey = (Key) iter.next();
      }
      String id = savekey.getId();
      Organization retOrg = (Organization) bqm.getRegistryObject(id);
      if (providerlevel == 0) {
        throw new Fault(testName
            + " failed - level 0 providers must throw UnsupportedCapabilityException");
      }
      if (retOrg == null)
        throw new Fault(testName + " failed - retOrg is null!");

      debug.add(
          "Verify that the organization returned by getCollection matches \n");
      debug.add("name of returned org: " + retOrg.getName().getValue(tsLocale)
          + "\n");
      debug.add("name of save org: " + orgName + "\n");
      if (!(retOrg.getName().getValue(tsLocale).equals(orgName)))
        throw new Fault(testName + " failed - did not expected organization ");

    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        debug.add(
            " Call to getRegistryObject(id) threw UnsupportedCapabilityException as expected\n");
      } else
        throw new Fault(
            testName
                + " unexpected UnsupportedCapabilityException was caught \n",
            uc);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
    }
  }

  /*
   * @testName: queryManager_getRegistryObjectsLevel1
   *
   * @assertion_ids: JAXR:JAVADOC:78;JAXR:SPEC:17;
   *
   * @test_Strategy: Create and save an organization. With the key from the
   * saved organization, save the String key id. Call getRegistryObjects(keys)
   * to get the saved objects Verify with the key id that the saved Organization
   * was returned.
   * 
   * Level 0 Providers must throw UnsupportedCapabilityException for
   * QueryManager getRegistryObjects(java.util.Collection objectKeys)
   *
   */
  public void queryManager_getRegistryObjectsLevel1() throws Fault {
    String testName = "queryManager_getRegistryObjectsLevel1";
    Key savekey = null;
    int passCount = 0;
    BusinessQueryManager bqm = null;
    Collection orgKeys = null;
    Collection servicekeys = null;
    int providerlevel = 0;
    try {
      bqm = rs.getBusinessQueryManager();
      // create an organization
      Organization org = (Organization) blm.createObject(blm.ORGANIZATION);
      String orgName = testName + "_testOrganization";
      org.setName(blm.createInternationalString(tsLocale, orgName));
      // publish the organization
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry

      orgKeys = br.getCollection();
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        savekey = (Key) iter.next();
      }
      String myKeyId = savekey.getId();
      debug.add("Saved Organization key id is: " + myKeyId + "\n");
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      br = bqm.getRegistryObjects(orgKeys);
      if (providerlevel == 0) {
        throw new Fault(testName
            + " failed - level 0 providers must throw UnsupportedCapabilityException");
      }
      Collection myObjects = br.getCollection();
      iter = myObjects.iterator();
      RegistryObject ro = null;
      debug.add("Number of objects returned: " + myObjects.size() + "\n");
      while (iter.hasNext()) {
        ro = (RegistryObject) iter.next();
        debug.add("Object to string is " + ro.toString() + "\n");
        debug.add("Object keyId is " + ro.getKey().getId() + "\n");
        if (ro.getKey().getId().equals(myKeyId)) {
          debug.add("good - retrieved my organization \n");
          passCount = passCount + 1;
          break;
        }
      }
      String serviceName = testName + "_testService";
      Service service = blm.createService(serviceName);
      Collection services = new ArrayList();
      services.add(service);
      Key serviceKey = service.getKey();
      br = blm.saveObjects(services);
      servicekeys = br.getCollection();
      Collection regKeys = new ArrayList();
      regKeys.addAll(orgKeys);
      regKeys.addAll(servicekeys);
      // retrieve heterogeneous objects
      br = bqm.getRegistryObjects(regKeys);
      myObjects.clear();
      myObjects = br.getCollection();
      iter = myObjects.iterator();
      ro = null;
      debug.add("Number of objects returned: " + myObjects.size() + "\n");
      while (iter.hasNext()) {
        ro = (RegistryObject) iter.next();
        debug.add("Object to string is " + ro.toString() + "\n");
        debug.add("Object keyId is " + ro.getKey().getId() + "\n");
        if (ro.getKey().getId().equals(myKeyId)) {
          debug.add("good - retrieved my organization \n");
          passCount = passCount + 1;
        }
        if (ro.getKey().getId().equals(serviceKey.getId())) {
          debug.add("good - retrieved my service \n");
          passCount = passCount + 1;
        }
      }

      if (passCount != 3)
        throw new Fault(testName + " failed");
    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        debug.add(
            " Call to getRegistryObject(id) threw UnsupportedCapabilityException as expected\n");
      } else
        throw new Fault(
            testName
                + " unexpected UnsupportedCapabilityException was caught \n",
            uc);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    } finally {
      debug.add("finally - delete organization \n");
      super.cleanUpRegistry(orgKeys, LifeCycleManager.ORGANIZATION);
      try {
        if (servicekeys != null) {
          debug.add("finally - delete service \n");
          blm.deleteObjects(servicekeys, LifeCycleManager.SERVICE);
        }
      } catch (javax.xml.registry.JAXRException je) {
        debug.add(testName + " unexpected error during cleanup was caught \n");
      }
    }
  }

}
