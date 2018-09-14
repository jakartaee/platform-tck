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
 * @(#)JAXRClient.java	1.13 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.jaxr.ee.javax_xml_registry_infomodel.RegistryObject;

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
   * @testName: registryObject_Test
   *
   * @assertion_ids: JAXR:SPEC:92;JAXR:SPEC:106;JAXR:SPEC:107;JAXR:SPEC:108;
   *
   * @test_Strategy: Create an organization with ExternalLinks and
   * ExternalIdentifier. Publish the organization. Retrieve it and verify
   * ExternalLinks and ExternalIdentifier.
   *
   */
  public void registryObject_Test() throws Fault {
    String testName = "registryObject_Test";
    boolean pass = true;
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;
    // Specification Links
    String slUsageDescription = "detail of specification link usage";
    Collection slUsageParameters = new ArrayList();
    slUsageParameters.add("usage=test");
    // Service
    String myServiceName = "TheNameOfMyService";
    // External Links
    String[] externalURI = { baseuri + "jaxrTestPage1.html",
        baseuri + "jaxrTestPage2.html" };
    String[] description = { "CTS Test Page1", "CTS Test Page2" };

    String name = "United States";
    String eivalue = "US";
    ExternalLink el = null;
    Collection orgKeys = null;
    Collection conceptKeys = null;
    BulkResponse br = null;
    Collection keys = null;
    Collection services = null;
    Organization org = null;
    try {
      Collection orgs = new ArrayList();
      Collection externalURIs = new ArrayList();
      Collection els = new ArrayList();

      BusinessQueryManager bqm = rs.getBusinessQueryManager();

      debug.add("Create an external link \n");
      // = Registry Objects may be associated with any number of External Links

      // = ExternalLink instances provide a link to content that is managed
      // outside the
      // = Registry using a URI to the external content.
      for (int i = 0; i < externalURI.length; i++) {
        els.add(blm.createExternalLink(externalURI[i], description[i]));
        externalURIs.add(externalURI[i]);
      }

      debug.add("Create an organization registryObject \n");
      org = blm.createOrganization(blm.createInternationalString(orgName));
      org.addExternalLinks(els);
      els = null;

      // = ExternalIdentifier instances provide identification information to a
      // RegistryObject
      // --
      String schemeName = "Geography";
      String schemeDescription = "North American Regions";

      ClassificationScheme cs = blm.createClassificationScheme(schemeName,
          schemeDescription);
      Collection schemes = new ArrayList();
      schemes.add(cs);
      br = blm.saveClassificationSchemes(schemes);
      if (!(JAXR_Util.checkBulkResponse("saveClassificationschemes", br,
          debug))) {
        debug.add("Error reported:  saveClassificationschemes\n");
        throw new Fault(testName
            + " Error from saveClassificationschemes - test did not complete!");
      }
      keys = new ArrayList();
      keys = br.getCollection(); // get a collection of keys
      Key ClassificationschemeKey = null;
      Iterator iterate = keys.iterator();
      while (iterate.hasNext()) {
        ClassificationschemeKey = (Key) iterate.next();
      }
      String keyId = ClassificationschemeKey.getId();
      debug.add("ClassificationschemeKey keyId is: " + keyId + "\n");

      ClassificationScheme scheme = (ClassificationScheme) bqm
          .getRegistryObject(keyId, LifeCycleManager.CLASSIFICATION_SCHEME);
      // --
      InternationalString iName = blm.createInternationalString(name);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, iName,
          eivalue);
      org.addExternalIdentifier(ei);
      ei = null;

      // = Service instances are RegistryObjects that provide information on
      // services
      // (e.g. web services) offered by an Organization.

      // create a specification link.
      SpecificationLink specificationlink = blm.createSpecificationLink();

      specificationlink.setUsageDescription(
          blm.createInternationalString(slUsageDescription));
      specificationlink.setUsageParameters(slUsageParameters);

      Concept specificationConcept = (Concept) blm.createObject(blm.CONCEPT);
      // specificationConcept.setKey(scheme.getKey());

      specificationConcept
          .setName(blm.createInternationalString("Conceptname"));
      specificationConcept.setValue("Concept value");

      // == Save the concept to the registry
      Collection concepts = new ArrayList();
      concepts.add(specificationConcept);
      br = blm.saveConcepts(concepts);
      if (!(JAXR_Util.checkBulkResponse("saveConcepts", br, debug))) {
        debug.add("Error:   saveConcepts failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      conceptKeys = br.getCollection();
      Iterator ii = conceptKeys.iterator();
      Key conKey = null;
      while (ii.hasNext()) {
        conKey = (Key) ii.next();
      }
      Concept concept = (Concept) bqm.getRegistryObject(conKey.getId(),
          LifeCycleManager.CONCEPT);
      specificationlink.setSpecificationObject(concept);
      // ==

      // create a service binding.
      ServiceBinding serviceBinding = blm.createServiceBinding();
      // set accessURI
      serviceBinding.setValidateURI(false);
      serviceBinding.setAccessURI(externalURI[0]);
      // add specificationlink to service binding
      serviceBinding.addSpecificationLink(specificationlink);
      // create a service
      Service service = blm.createService(myServiceName);
      // attach bindings to the service
      service.addServiceBinding(serviceBinding);
      // add the service to the organization
      org.addService(service);

      orgs.add(org);
      org = null;
      // Publish the organization to the registry. Check for errors.
      br = blm.saveOrganizations(orgs);
      if (!(JAXR_Util.checkBulkResponse("saveOrganizations", br, debug))) {
        debug.add("Error:   saveOrganizations failed \n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }

      // save the keys
      orgKeys = br.getCollection();

      // Retrieve the newly published organization from the registry
      br = bqm.getRegistryObjects(orgKeys, LifeCycleManager.ORGANIZATION);
      if (!(JAXR_Util.checkBulkResponse("getRegistryObjects", br, debug))) {
        debug.add(
            "Error:  encountered an error while trying to retrieve the organization.\n");
        throw new Fault(testName + " due to errors, test did not complete!");
      }
      Collection retOrgs = br.getCollection();
      // Verify that we got back 1 organization
      if (retOrgs.size() != 1)
        throw new Fault(testName
            + " failed to retrieve 1 organization as expected. Test did not complete!");
      // get our organization so we can verify it.
      Iterator iter = retOrgs.iterator();
      while (iter.hasNext()) {
        org = (Organization) iter.next();
      }
      // successfully retrieved newly published organization from the registry -
      // now verify it.

      // Verify Service
      service = null;
      services = org.getServices();
      iter = services.iterator();
      while (iter.hasNext()) {
        service = (Service) iter.next();
        debug.add("Service Name: " + service.getName().getValue() + "\n");
        debug.add("Get the service binding from this service \n");
        Collection serviceBindings = service.getServiceBindings();
        serviceBinding = null;
        Iterator itr = serviceBindings.iterator();
        while (itr.hasNext()) {
          serviceBinding = (ServiceBinding) itr.next();
          Collection specificationlinks = serviceBinding
              .getSpecificationLinks();
          Iterator it = specificationlinks.iterator();
          while (it.hasNext()) {
            specificationlink = (SpecificationLink) it.next();

          } // end of while specificationLinks

        } // end of while servicebinding.

      } // end of while service

      // Verify ExternalIdentifier.
      // get Organizations External Identifier
      Collection eis = org.getExternalIdentifiers();
      iter = eis.iterator();
      while (iter.hasNext()) {
        ei = (ExternalIdentifier) iter.next();
        if (ei.getValue().equals(eivalue)) {
          debug.add(
              "String returned from ExternalIdentifier.getValue matched value \n");
        } else {
          pass = false;
          debug.add("Error: ExternalIdentifier value returned: " + ei.getValue()
              + "expected value was " + eivalue + "\n");
        }
      }
      // Verify ExternalLinks.
      debug.add("Expecting the following ExternalLinkURIs: \n");
      for (int i = 0; i < externalURI.length; i++) {
        debug.add("          " + externalURI[i] + "\n");
      }
      els = org.getExternalLinks();
      iter = els.iterator();
      int count = 0;
      while (iter.hasNext()) {
        el = (ExternalLink) iter.next();
        debug.add("found link: " + el.getExternalURI() + "\n");
        if (externalURIs.contains(el.getExternalURI()))
          count = count + 1;
      }
      if (count != externalURI.length) {
        pass = false;
        debug.add(
            "Error: retrieved unexpected ExternalLink from organization\n");
      } else
        debug.add("ExternalLinks pass\n");

    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      debug.add("End of test method - delete the Organization \n");
      // cleanup...
      try {
        org.removeServices(services);
        br = blm.deleteClassificationSchemes(keys);
        br = blm.deleteConcepts(conceptKeys);
        br = blm.deleteOrganizations(orgKeys);
        if (!(JAXR_Util.checkBulkResponse("deleteOrganzations", br, debug))) {
          debug.add(
              "WARNING:  cleanup encountered an error while trying to delete the organization.\n");
        }
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        debug
            .add("Caught Exception while trying to delete the organization \n");
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");

  } // end of method.

  /*
   * @testName: registryObject_getAssociatedObjects
   *
   * @assertion_ids: JAXR:SPEC:54
   *
   * @test_Strategy: getAssociatedObjects - Returns the collection of
   * RegistryObject instances associated with this object. Capability Level: 1
   * This method must throw UnsupportedCapabilityException in lower capability
   * levels. Create an organization. Invoke getAssociatedObjects method. Verify
   * that Level 0 providers throw an UnsupportedCapabilityException.
   *
   */
  public void registryObject_getAssociatedObjects() throws Fault {
    String testName = "registryObject_getAssociatedObjects";
    int failcount = 0;
    BusinessQueryManager bqm = null;
    int providerlevel = 0;
    String type = "RelatedTo";
    Collection orgKeys = null;
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      String orgName1 = testName + "_myTestOrganization1";
      String orgName2 = testName + "_myTestOrganization2";
      bqm = rs.getBusinessQueryManager();
      // create 2 organizations
      Organization org1 = blm
          .createOrganization(blm.createInternationalString(orgName1));
      Organization org2 = blm
          .createOrganization(blm.createInternationalString(orgName2));
      // save 2 organizations
      Key org1Key = saveMyOrganization(org1);
      Key org2Key = saveMyOrganization(org2);
      String orgId2 = org2Key.getId();
      Concept relatedTo = bqm.findConceptByPath("/AssociationType/RelatedTo");
      Association a = (Association) blm.createAssociation(org2, relatedTo);
      org1.addAssociation(a);
      saveMyOrganization(org1);

      // test getAssociated objects
      Collection o1AssObjs = org1.getAssociatedObjects();
      if (providerlevel == 0) {
        debug.add(
            " Error - Expected UnsupportedCapabilityException Not Thrown \n");
        throw new Fault(testName + " failed ");
      }
      debug.add("size of org1 object collection returned is " + o1AssObjs.size()
          + "\n");
      if (o1AssObjs.size() == 0) {
        debug.add("Error - getAssociatedObjects returned empty collection\n");
        failcount = failcount + 1;
      }
      Iterator iter = o1AssObjs.iterator();
      while (iter.hasNext()) {
        Organization o = (Organization) iter.next();
        if (o.getKey().getId().equals(orgId2))
          debug.add(" good got back org2 for assocated object \n");
        else {
          debug.add(" not good - did not get back org2 as expected\n");
          failcount = failcount + 1;
        }
      }

    } catch (UnsupportedCapabilityException ue) {
      if (providerlevel == 0)
        debug.add(" UnsupportedCapabilityException thrown as expected\n");
      else
        throw new Fault(testName
            + " UnsupportedCapabilityException not expected for level 1 providers!");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      debug.add("End of test method - delete the organizations \n");
      // cleanup...
      try {
        BulkResponse br = bqm.getRegistryObjects(LifeCycleManager.ORGANIZATION);
        Collection myOrgs = br.getCollection();
        Iterator iter = myOrgs.iterator();
        Collection myOrgKeys = new ArrayList();
        while (iter.hasNext()) {
          Organization o = (Organization) iter.next();
          myOrgKeys.add(o.getKey());
        }
        br = blm.deleteOrganizations(myOrgKeys);

        if (br.getExceptions() != null) {
          debug.add(
              "WARNING:  cleanup encountered an error while trying to delete the organizations.\n");
        }
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        debug.add("Caught Exception while trying to delete the service \n");
      }

    }
    if (failcount > 0)
      throw new Fault(testName + " failed ");

  } // end of method.

  /*
   * @testName: registryObject_getObjectTypeTest
   *
   * @assertion_ids: JAXR:SPEC:55
   *
   * @test_Strategy: getObjectType - Gets the pre-defined object type that best
   * describes the RegistryObject. Capability Level: 1 This method must throw
   * UnsupportedCapabilityException in lower capability levels. Create an
   * organization. Invoke getObjectType method. Verify that Level 0 providers
   * throw an UnsupportedCapabilityException.
   *
   */
  public void registryObject_getObjectTypeTest() throws Fault {
    String testName = "registryObject_getObjectTypeTest";
    int providerlevel = 0;
    Collection orgKeys = null;
    // refer to ebXML Registry Information Model specification,
    // section 1.6 Canonical ClassificationSchemes
    // http://www.oasis-open.org/committees/regrep/documents/3.0/canonical/SubmitObjectsRequest_ObjectTypeScheme.xml
    // --

    String orgClassificationId = "urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Organization";
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      String orgName = testName + "_myTestOrganization";
      Organization org = blm
          .createOrganization(blm.createInternationalString(orgName));
      debug.add("get the object type for the RegistryObject - Organization\n");
      Concept concept = org.getObjectType();
      if (providerlevel == 0) {
        debug.add(
            " Error - Expected UnsupportedCapabilityException Not Thrown \n");
        throw new Fault(testName + " failed ");
      }
      debug.add("concept value is: " + concept.getValue() + "\n");
      debug.add("concept key is: " + concept.getKey().getId() + "\n");
      if (concept.getKey().getId().equals(orgClassificationId))
        debug.add("ObjectType verified \n");
      else
        throw new Fault(testName + " key returned from concept is not correct");
    } catch (UnsupportedCapabilityException ue) {
      if (providerlevel == 0)
        debug.add(" UnsupportedCapabilityException thrown as expected\n");
      else
        throw new Fault(testName
            + " UnsupportedCapabilityException not expected for level 1 providers!");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }

  } // end of method.

  /*
   * @testName: registryObject_getRegistryPackagesTest
   *
   * @assertion_ids: JAXR:SPEC:56
   *
   * @test_Strategy: getRegistryPackages - Returns the Package associated with
   * this object. Capability Level: 1 This method must throw
   * UnsupportedCapabilityException in lower capability levels. Create a service
   * and a registry package. Invoke getRegistryPackages method. Verify that
   * Level 0 providers throw an UnsupportedCapabilityException.
   *
   */
  public void registryObject_getRegistryPackagesTest() throws Fault {
    String testName = "registryObject_getRegistryPackagesTest";
    int providerlevel = 0;
    boolean pass = false;
    Collection serviceKeys = null;
    try {
      String serviceName = testName + "_my-test-service";
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      Service service = blm.createService(serviceName);
      // level 0 providers do not support the setExpiration method and must
      // throw
      // an UnsupportedCapabilityException.
      // create a registry package
      String registryPackage = testName + "_my-test-registry-package";
      RegistryPackage rP = blm.createRegistryPackage(registryPackage);
      if (providerlevel == 0) {
        throw new Fault(testName
            + "failed to throw expected UnsupportedCapabilityException.");
      }
      serviceKeys = new ArrayList();
      Key serviceKey = saveMyService(service);
      serviceKeys.add(serviceKey); // save for cleanup at end of test
      rP.addRegistryObject(service);
      Collection packages = new ArrayList();
      packages.add(rP);
      blm.saveObjects(packages);
      Key rpKey = rP.getKey();

      Collection rpkgs = service.getRegistryPackages();
      if (rpkgs.size() == 0)
        throw new Fault(testName
            + "failed - getRegistryPackages returned an empty collection.");
      Iterator iter = rpkgs.iterator();
      while (iter.hasNext()) {
        Object o = iter.next();
        if (o instanceof RegistryPackage) {
          RegistryPackage rp = (RegistryPackage) o;
          if (rp.getKey().getId().equals(rpKey.getId())) {
            debug.add("All set, I found my registryPackage\n");
            pass = true;
            break;
          }
        }
      }
    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        pass = true;
        debug.add(
            " Call to getRegistryPackages threw UnsupportedCapabilityException as expected\n");
      } else
        throw new Fault(
            testName
                + " unexpected UnsupportedCapabilityException was caught \n",
            uc);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      debug.add("End of test method - delete the service\n");
      // cleanup...
      try {
        if (serviceKeys != null) { // if this is not a level 0 provider.
          BulkResponse br = blm.deleteServices(serviceKeys);
          if (br.getExceptions() != null) {
            debug.add(
                "WARNING:  cleanup encountered an error while trying to delete the service.\n");
          }
        }
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        debug.add("Caught Exception while trying to delete the service \n");
      }
    }

    if (pass != true)
      throw new Fault(testName + " failed ");
  } // end of method

  /*
   * @testName: registryObject_getAuditTrailTest
   *
   * @assertion_ids: JAXR:SPEC:53;JAXR:SPEC:4;
   *
   * @test_Strategy: getAuditTrail - Returns the complete audit trail of all
   * requests that effected a state change in this object as an ordered
   * Collection of AuditableEvent objects. Verify that level 0 providers throw
   * an UnsupportedCapabilityException. Verify level 1 functionality - create an
   * organization and verify the event.
   *
   */
  public void registryObject_getAuditTrailTest() throws Fault {
    String testName = "registryObject_getAuditTrailTest";
    int providerlevel = 0;
    boolean pass = false;
    Collection orgKeys = null;
    Key orgKey = null;
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      debug.add(
          "Level 0 Providers must throw an UnsupportedCapabilityException for getAuditTrail\n");
      debug.add(
          "This provider report a Capability Level of " + providerlevel + "\n");
      String orgName = testName + " _organization";
      Organization org = blm
          .createOrganization(blm.createInternationalString(orgName));
      Collection orgs = new ArrayList();
      orgs.add(org);
      BulkResponse br = blm.saveOrganizations(orgs);
      if (br.getExceptions() != null)
        throw new Fault(testName + " unexpected error ");
      orgKeys = br.getCollection();
      Iterator itr = orgKeys.iterator();
      while (itr.hasNext()) {
        orgKey = (Key) itr.next();
      }
      org = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);

      Collection audits = org.getAuditTrail();
      if (providerlevel == 0)
        throw new Fault(testName
            + " failed to throw expected UnsupportedCapabilityException.");
      Iterator iter = audits.iterator();
      while (iter.hasNext()) {
        AuditableEvent ae = (AuditableEvent) iter.next();
        if (ae.getEventType() == AuditableEvent.EVENT_TYPE_CREATED) {
          pass = true;
        }
      }
    } catch (UnsupportedCapabilityException uc) {
      if (providerlevel == 0) {
        debug.add(
            " Call to getAuditTrail threw UnsupportedCapabilityException as expected\n");
        pass = true;
      } else
        throw new Fault(
            testName
                + " unexpected UnsupportedCapabilityException was caught \n",
            uc);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    } finally {
      debug.add("End of test method - delete the org \n");
      // cleanup...
      try {
        BulkResponse br = blm.deleteOrganizations(orgKeys);
        if (br.getExceptions() != null) {
          debug.add(
              "WARNING:  cleanup encountered an error while trying to delete the org.\n");
        }
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        debug.add(
            "Caught Exception while trying to delete the organization. \n");
      }
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of method

  private Key saveMyService(Service service) {
    Key key = null;
    Collection services = new ArrayList();
    services.add(service);
    try {
      BulkResponse br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("Error:   saveServices failed \n");
        return null;
      }
      Collection serviceKeys = br.getCollection();
      Iterator iter = serviceKeys.iterator();
      while (iter.hasNext()) {
        key = (Key) iter.next();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return key;
  } // end of saveMyService method

} // end of class
