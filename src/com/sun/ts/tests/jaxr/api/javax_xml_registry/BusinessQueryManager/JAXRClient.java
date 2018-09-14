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

package com.sun.ts.tests.jaxr.api.javax_xml_registry.BusinessQueryManager;

import javax.xml.registry.*;
import javax.xml.registry.infomodel.*;
import java.net.PasswordAuthentication;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.JAXRCommonClient;
import com.sun.ts.tests.jaxr.common.JAXR_Util;

import com.sun.javatest.Status;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Iterator;
import java.util.Properties;

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
   * @testName: businessQueryManager_findClassificationSchemeByNameTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:846;
   * 
   * @assertion: Find a ClassificationScheme by name based on the specified name
   * pattern.
   * 
   * namePattern - Is a String that is a partial or full name pattern with
   * wildcard searching as specified by the SQL-92 LIKE specification
   *
   * Returns: The ClassificationScheme matching the namePattern. If none match
   * return null. If multiple match then throw an InvalidRequestException.
   *
   * @test_Strategy: Query for ClassificationScheme by full name pattern. Use
   * PhoneType which must be supported by the JAXR provider.
   */
  public void businessQueryManager_findClassificationSchemeByNameTest()
      throws Fault {
    String testName = "businessQueryManager_findClassificationSchemeByNameTest";
    boolean pass = false;
    String schemeName = "PhoneType";

    try {
      pass = findClassificationSchemeByName(schemeName, testName);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName:
   * businessQueryManager_findClassificationSchemeByNamePartialNameTest
   *
   * @assertion: Find a ClassificationScheme by name based on the specified name
   * pattern.
   *
   * namePattern - Is a String that is a partial or full name pattern with
   * wildcard searching as specified by the SQL-92 LIKE specification
   *
   * Returns: The ClassificationScheme matching the namePattern. If none match
   * return null. If multiple match then throw an InvalidRequestException.
   *
   * JAXR javadoc
   *
   * @test_Strategy: Query for ClassificationScheme by partial name pattern. Use
   * "%honeTyp%" for PhoneType which must be supported by the JAXR UDDI
   * provider.
   *
   */
  public void businessQueryManager_findClassificationSchemeByNamePartialNameTest()
      throws Fault {
    String testName = "businessQueryManager_findClassificationSchemeByNamePartialNameTest";
    String schemeName = "%honeTyp%";
    String fullSchemeName = "PhoneType";

    try {
      Collection findQualifiers = new ArrayList();
      findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      ClassificationScheme scheme = bqm
          .findClassificationSchemeByName(findQualifiers, schemeName);
      if (scheme != null) {
        debug.add("name is: " + scheme.getName().getValue());
        if (!(scheme.getName().getValue().equals(fullSchemeName)))
          throw new Fault(testName + " Error: unexpected scheme name returned");
      } else {
        throw new Fault(testName + " Error: returned null scheme");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }

  /*
   * @testName: businessQueryManager_findClassificationSchemeByNameNullTest
   *
   * @assertion: Find a ClassificationScheme by name based on the specified name
   * pattern.
   *
   * namePattern - Is a String that is a partial or full name pattern with
   * wildcard searching as specified by the SQL-92 LIKE specification
   *
   * Returns: The ClassificationScheme matching the namePattern. If none match
   * return null. If multiple match then throw an InvalidRequestException.
   *
   * JAXR javadoc
   *
   * @test_Strategy: Query for ClassificationScheme by full name pattern. Use an
   * invalid scheme name which should return null
   *
   */
  public void businessQueryManager_findClassificationSchemeByNameNullTest()
      throws Fault {
    String testName = "businessQueryManager_findClassificationSchemeByNameNullTest";
    boolean pass = false;
    String schemeName = "invalidinvalidinvalidinvalidinvalid";
    try {
      Collection findQualifiers = new ArrayList();
      findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      ClassificationScheme scheme = bqm
          .findClassificationSchemeByName(findQualifiers, schemeName);
      if (scheme != null) {
        debug.add("name is: " + scheme.getName().getValue());
      } else {
        debug.add("Pass: null returned!!\n");
        pass = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  private boolean findClassificationSchemeByName(String schemeName,
      String testName) {
    try {
      Locale en_US = new Locale("en", "US");
      Collection findQualifiers = new ArrayList();
      findQualifiers.add(FindQualifier.SORT_BY_NAME_DESC);
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      ClassificationScheme scheme = bqm
          .findClassificationSchemeByName(findQualifiers, schemeName);
      if (scheme != null) {
        debug.add("name is: " + scheme.getName().getValue(en_US));
        if (!(scheme.getName().getValue(en_US).equals(schemeName)))
          return false;
      } else {
        debug.add("Error: null returned!!\n");
        return false;
      }
    } catch (javax.xml.registry.InvalidRequestException ex) {
      TestUtil.logTrace(
          "Caught exception InvalidRequestException. Multiple match found");
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
    return true;
  }

  /*
   * @testName: businessQueryManager_findOrganizationsTest
   *
   * @assertion_ids: JAXR:JAVADOC:254;JAXR:SPEC:173;
   *
   * @test_Strategy: Create and publish a test organization. Verify that
   * findOrganization retrieves test organization.
   * 
   */
  public void businessQueryManager_findOrganizationsTest() throws Fault {
    String testName = "businessQueryManager_findOrganizationsTest";
    String orgName = testName + "_organization";
    Collection orgKeys = null;
    Collection retKeys;
    Key orgKey = null;
    boolean pass = false;
    try {
      Organization org = blm
          .createOrganization(blm.createInternationalString(orgName));

      // publish the organizations
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs); // publish to registry
      orgKeys = br.getCollection();
      if (orgKeys.size() != 1)
        throw new Fault(
            "Error: expected 1 organization to be saved.  exiting test");

      Iterator iter = orgKeys.iterator();
      // get the key for this saved organization
      while (iter.hasNext()) {
        orgKey = (Key) iter.next();
      }

      // published the organization - now find it.
      Collection findQualifiers = new ArrayList();
      findQualifiers.add(FindQualifier.EXACT_NAME_MATCH);
      Collection namePatterns = new ArrayList();
      namePatterns.add(orgName);

      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      br = bqm.findOrganizations(findQualifiers, namePatterns, null, null, null,
          null);
      // verify that expected org was returned.
      Collection collectionOrgsRet = br.getCollection();
      iter = collectionOrgsRet.iterator();
      while (iter.hasNext()) {
        Organization o = (Organization) iter.next();
        if (o.getKey().getId().equals(orgKey.getId())) {
          debug.add("good.  Got back the correct organization!! \n");
          pass = true;
        } else
          pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);

    } finally {
      // clean up - get rid of published orgs
      try {
        debug.add(" Cleanup: Remove source organization\n");
        blm.deleteOrganizations(orgKeys);

      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Error: not able to delete registry object\n");
      }
    }
    if (!pass)
      throw new Fault(testName + " failed!");
  }

  // =
  /*
   * @testName: businessQueryManager_findServiceTest
   *
   * @assertion_ids: JAXR:JAVADOC:256;
   *
   * @test_Strategy: Create and save an organization. Add a service to it.
   * Verify the service can be retrieved from the registry with findService.
   */
  public void businessQueryManager_findServiceTest() throws Fault {
    // get the Connection object conn from the super class
    String testName = "businessQueryManager_findServiceTest";
    boolean pass = false;
    Collection keys = null;
    Key key = null;
    Key orgKey = null; // save organization key for findServices call
    int numOrgs = 3;
    Collection orgs = null;
    Collection services = null;
    String serviceName = "Name:Test Service";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;

    try {
      Organization org = blm
          .createOrganization(blm.createInternationalString(orgName));
      // Collection services = org.getServices();
      BusinessQueryManager bqm = rs.getBusinessQueryManager();

      orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs);
      if (br.getExceptions() != null) {
        Collection ex = br.getExceptions();
        Iterator iter = ex.iterator();
        //
        while (iter.hasNext()) {
          JAXRException je = (JAXRException) iter.next();
          debug.add("== Detail Message for the JAXRException object: "
              + je.getMessage() + "\n");
        }
        throw new Fault(testName + " failed ");
      }
      Collection orgKeys = br.getCollection();
      if (orgKeys.size() != 1)
        throw new Fault(
            "Error: expected 1 organization to be saved.  exiting test");
      // get the key for this organization - should be just one
      Iterator iter = orgKeys.iterator();
      while (iter.hasNext()) {
        orgKey = (Key) iter.next();
      }

      // get the org from the registry
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      if (br.getExceptions() != null) {
        Collection ex = br.getExceptions();
        iter = ex.iterator();
        //
        while (iter.hasNext()) {
          JAXRException je = (JAXRException) iter.next();
          debug.add("== Detail Message for the JAXRException object: "
              + je.getMessage() + "\n");
        }
        throw new Fault(
            "Error: retrieving organization  from registry.  exiting test");
      }
      Collection myOrgs = br.getCollection();
      services = new ArrayList();
      Service theService = (Service) blm.createObject(blm.SERVICE);
      // iterate thru the collection of orgs returned from getRegistryObjects -
      // we only have 1
      iter = myOrgs.iterator();
      Organization myOrg = null;
      theService.setName(blm.createInternationalString(tsLocale, serviceName));
      theService.setDescription(
          blm.createInternationalString(tsLocale, "Description: Testservice"));
      while (iter.hasNext()) {
        myOrg = (Organization) iter.next();
        myOrg.addService(theService);
        services.add(theService);
      }
      ///// ======
      // save the new service back to the registry
      br = blm.saveServices(services);
      Collection serviceKeys = br.getCollection();
      // save the updated organization to the registry
      Collection updatedOrg = new ArrayList();
      updatedOrg.add(myOrg);
      br = blm.saveOrganizations(updatedOrg);

      // --
      // Now we must verify that the service was saved successfully....
      // --
      Collection names = new ArrayList();
      names.add(serviceName);

      br = bqm.findServices(orgKey, null, names, null, null);

      // get the collection of services returned from findServices - should be
      // one
      Service s = null;
      Collection ss = br.getCollection();
      debug.add("Count returned from findServices should be one \n");
      debug.add("Count returned : " + ss.size() + "\n");

      iter = ss.iterator();
      while (iter.hasNext()) {
        s = (Service) iter.next();
        debug.add("And the service returned is: "
            + s.getName().getValue(tsLocale) + "\n");

      }
      if (s.getName().getValue(tsLocale).equals(serviceName)) {
        pass = true;
        debug.add(testName + "Found Service successfully!!!\n");
      } else
        debug.add(testName + "Failed to add Service successfully!!\n");
      br = blm.deleteServices(serviceKeys);
      br = bqm.findServices(orgKey, null, names, null, null);

      // get the collection of services returned from findServices - should be
      // one
      s = null;
      ss = br.getCollection();
      debug.add("Count returned from findServices should be none \n");
      debug.add("Count returned : " + ss.size() + "\n");
      br = blm.deleteOrganizations(orgKeys);

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test class

  /*
   * @testName: businessQueryManager_findServiceBindingsTest
   *
   * @assertion_ids: JAXR:JAVADOC:258;
   *
   * @test_Strategy: Create a service add a service binding to it. Save the
   * service. Call findServiceBinding to retrieve the binding. Verify the
   * binding was returned.
   */
  public void businessQueryManager_findServiceBindingsTest() throws Fault {
    String testName = "businessQueryManager_findServiceBindingsTest";
    String serviceName = "test service for businessQueryManager_findServiceBindingsTest";
    String url = "http://java.sun.com";
    Collection serviceBindingKeys = null;
    Collection serviceKeys = null;
    Collection orgKeys = null;
    Collection conceptKeys = null;
    BulkResponse br = null;
    BusinessQueryManager bqm = null;
    Key serviceKey = null;
    ServiceBinding serviceBindingRet = null;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    String externalURI = "http://java.sun.com";
    String description = "link for my service";
    Iterator iter;
    try {
      bqm = rs.getBusinessQueryManager();
      // create an org
      Organization org = blm.createOrganization(orgName);
      // Create a service binding.
      ServiceBinding sb = blm.createServiceBinding();
      sb.setAccessURI(url);
      // create a Service
      Service service = blm.createService(serviceName);
      // create a concept for the specification link
      Concept myConcept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      myConcept.setName(blm.createInternationalString("myWSDLFile"));
      // save the concept
      Collection cons = new ArrayList();
      cons.add(myConcept);
      br = blm.saveConcepts(cons);
      conceptKeys = br.getCollection();
      if (conceptKeys.size() != 1) {
        debug.add("Error: expected one concept key to be returned \n");
        throw new Fault(testName + " test did not complete ");
      }
      br = bqm.getRegistryObjects(conceptKeys, LifeCycleManager.CONCEPT);
      Collection cc = br.getCollection();

      if (cc.size() != 1) {
        debug.add("Error: expected one concept to be returned \n");
        throw new Fault(testName + " test did not complete ");
      }

      iter = cc.iterator();
      while (iter.hasNext()) {
        myConcept = (Concept) iter.next();
      }

      // create an external link
      ExternalLink el = blm.createExternalLink(externalURI, description);
      // create a specification link
      Collection els = new ArrayList();
      els.add(el);
      myConcept.setExternalLinks(els);
      SpecificationLink sl = blm.createSpecificationLink();
      sl.setUsageDescription(blm.createInternationalString("yada yada"));

      sl.setSpecificationObject(myConcept);

      sb.addSpecificationLink(sl);
      service.addServiceBinding(sb);
      org.addService(service);

      Collection orgs = new ArrayList();
      orgs.add(org);
      br = blm.saveOrganizations(orgs);
      orgKeys = br.getCollection();
      Key orgKey = null;
      debug.add("\nOrg key count is " + orgKeys.size() + "\n");
      if (orgKeys.size() != 1) {
        debug.add("Error: expected one org key to be returned \n");
        throw new Fault(testName + " test did not complete ");
      }
      iter = orgKeys.iterator();
      while (iter.hasNext()) {
        orgKey = (Key) iter.next();
      }

      Collection namePatterns = new ArrayList();
      namePatterns.add(serviceName);
      br = bqm.findServices(orgKey, null, namePatterns, null, null);
      Collection ss = br.getCollection();
      if (ss.size() != 1) {
        debug.add("Error: expected one service to be returned \n");
        throw new Fault(testName + " test did not complete ");
      }
      Service s = null;
      iter = ss.iterator();
      while (iter.hasNext()) {
        s = (Service) iter.next();
        serviceKey = s.getKey();
      }
      // =========
      Collection ros = new ArrayList();
      ros.add(myConcept);
      br = bqm.findServiceBindings(s.getKey(), null, null, ros);
      Collection serviceBindings = br.getCollection();

      if (serviceBindings.size() != 1) {
        debug.add("Error: expected one serviceBinding to be returned \n");
        throw new Fault(testName + " test did not complete ");
      }
      iter = serviceBindings.iterator();
      while (iter.hasNext()) {
        serviceBindingRet = (ServiceBinding) iter.next();
      }
      // now verify the service binding
      debug.add("Service name is: "
          + serviceBindingRet.getService().getName().getValue(tsLocale) + "\n");
      if (serviceName.equals(
          serviceBindingRet.getService().getName().getValue(tsLocale))) {
        debug.add("found the binding\n");
      } else
        throw new Fault(testName + " didn't get back binding ");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      try {
        serviceKeys = new ArrayList();
        serviceKeys.add(serviceKey);
        blm.deleteServices(serviceKeys);
        blm.deleteConcepts(conceptKeys);
        blm.deleteOrganizations(orgKeys);
      } catch (JAXRException je) {
        TestUtil.printStackTrace(je);
        debug.add("Error: not able to delete registry object\n");
      }
    }

  }

  /*
   * @testName: businessQueryManager_findRegistryPackagesTest
   *
   * @assertion_ids: JAXR:SPEC:5;
   *
   * @test_Strategy: Verify that level 0 providers throw an
   * UnsupportedCapabilityException.
   *
   */
  public void businessQueryManager_findRegistryPackagesTest() throws Fault {
    String testName = "businessQueryManager_findRegistryPackagesTest";
    boolean pass = true;
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      int providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add(
          "Level 0 Providers must throw an UnsupportedCapabilityException for getRegistryObject(String)\n");
      debug.add("This provider reported a Capability Level of " + providerlevel
          + "\n");
      if (providerlevel == 0) {
        Collection namePatterns = new ArrayList();
        namePatterns.add("myRegistryPackage");
        BulkResponse br = bqm.findRegistryPackages(null, namePatterns, null,
            null);
        // should not get here
        debug.add(
            " Call to findRegistryPackages(id) DID NOT throw an UnsupportedCapabilityException as expected\n");
        pass = false;
      }
    } catch (UnsupportedCapabilityException uc) {
      TestUtil.printStackTrace(uc);
      debug.add(
          " Call to getRegistryObject(id) threw UnsupportedCapabilityException as expected\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  // end of test class
}
