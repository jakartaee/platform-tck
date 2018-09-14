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

package com.sun.ts.tests.jaxr.ee.javax_xml_registry_infomodel.AuditableEvent;

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
  java.sql.Timestamp createTS = null;

  java.sql.Timestamp updateTS = null;

  java.sql.Timestamp deleteTS = null;

  java.sql.Timestamp versionedTS = null;

  java.sql.Timestamp unDeprecateTS = null;

  java.sql.Timestamp deprecateTS = null;

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
      if (debug != null)
        TestUtil.logTrace(debug.toString());
    }
  }

  /*
   * @testName: auditableEvent_Test
   *
   * @assertion_ids: JAXR:JAVADOC:476;JAXR:JAVADOC:807;JAXR:JAVADOC:803;
   * JAXR:JAVADOC:805;JAXR:JAVADOC:795;JAXR:JAVADOC:798;
   * JAXR:JAVADOC:797;JAXR:JAVADOC:800;JAXR:JAVADOC:796;
   *
   * @test_Strategy: level 0 providers must throw UnsupportedCapabilityException
   * For level 1 providers, create/update/deprecate/undeprecate and delete a
   * service verify the events are in the audit trail verify the timestamp and
   * user verify the registry object from the auditable event matches the id for
   * the created service
   */

  public void auditableEvent_Test() throws Fault {
    String testName = "auditableEvent_Test";
    int failcount = 0;
    Collection serviceKeys = null;
    Service service = null;
    String serviceId = null;
    BusinessQueryManager bqm = null;
    Key serviceKey = null;
    int providerlevel = 0;
    String createUser = null;
    String deleteUser = null;
    Collection orgKeys = null;
    javax.xml.registry.infomodel.Key orgKey = null;

    try {
      // create an organization
      Organization org = blm.createOrganization(
          blm.createInternationalString(testName + "_organization_"));
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
      bqm = rs.getBusinessQueryManager();
      org = (Organization) bqm.getRegistryObject(orgKey.getId(),
          LifeCycleManager.ORGANIZATION);

      // test for level 0 provider
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      // ==

      Collection audits = org.getAuditTrail();
      if (providerlevel == 0)
        throw new Fault(testName
            + " UnsupportedCapabilityException expected for level 0 providers!");

      try {
        // getRegistryObject -------------------
        debug.add("Test AuditableEvent.getRegistryObject \n");
        Iterator itr = audits.iterator();
        debug.add(
            " Number of auditable events returned is: " + audits.size() + "\n");
        while (itr.hasNext()) {
          AuditableEvent myAE = (AuditableEvent) itr.next();
          RegistryObject ro = myAE.getRegistryObject();
          if (ro != null) {
            if (ro.getKey().getId().equals(orgKey.getId())) {
              debug.add("good - org key is a match! \n");
            } else {
              debug.add(
                  "Failed: getRegistryObject did not return the organization as  expected\n");
              debug.add("myAE.getRegistryObject() returned a: "
                  + ro.getClass().getName() + "\n");
              failcount = failcount + 1;
            }
          } else if (ro == null) {
            failcount = failcount + 1;
            debug
                .add(" Failure on getRegistryObject! - method returns null!\n");
          }
        }
      } catch (Exception e) {
        failcount = failcount + 1;
        debug.add("Error, myAE.getRegistryObject threw an error \n");
        TestUtil.printStackTrace(e);
      }
      // ==
      String serviceName = testName + "_service";
      bqm = rs.getBusinessQueryManager();
      debug.add("Create an service registryObject \n");
      service = blm.createService(serviceName);
      org.addService(service);
      serviceKeys = new ArrayList();
      serviceKey = saveMyService(service);
      if (serviceKey == null)
        throw new Fault(testName
            + " due to create service errors , test did not complete!");

      serviceId = serviceKey.getId();
      serviceKeys.add(serviceKey);
      if (serviceKey == null)
        throw new Fault(
            testName + " due to save service errors , test did not complete!");

      // ===============
      // Retrieve the newly published service from the registry
      service = retrieveMyObject(serviceKey);
      // ==

      // Verify create event..
      AuditableEvent ae = getAE(service);
      // verify create event.
      if (verifyEvent(service, AuditableEvent.EVENT_TYPE_CREATED)) {
        debug.add(" created event returned as expected \n");
        createTS = ae.getTimestamp();
        createUser = ae.getUser().getPersonName().getLastName();
      } else {
        failcount = failcount + 1;
        debug.add(" verify create event failed");
      }

      // update event ...
      debug.add("Update the service \n");
      String description = "We want to test the update event";
      InternationalString is = blm.createInternationalString(description);
      service.setDescription(is);
      serviceKey = saveMyService(service);
      if (serviceKey == null)
        throw new Fault(
            testName + " due to save service errors , test did not complete!");

      // verify update event
      // Retrieve the newly published service from the registry
      service = retrieveMyObject(serviceKey);
      debug.add("what is the version number after the update? "
          + service.getMinorVersion() + "\n");
      ae = getAE(service);
      if (verifyEvent(service, AuditableEvent.EVENT_TYPE_UPDATED)) {
        debug.add(" update event returned as expected \n");
        updateTS = ae.getTimestamp();
      } else {
        failcount = failcount + 1;
        debug.add(" update event failed");
      }

      // deprecate the object
      br = blm.deprecateObjects(serviceKeys);
      debug.add("Deprecate the service \n");

      if (br.getExceptions() != null) {
        debug.add(" error on deprecateObjects \n");
        Collection ex = br.getExceptions();
        iter = ex.iterator();
        while (iter.hasNext()) {
          JAXRException je = (JAXRException) iter.next();
          debug.add("== Detail Message for the JAXRException object: "
              + je.getMessage() + "\n");
        }
        failcount = failcount + 1;
      } else {
        service = retrieveMyObject(serviceKey);
        ae = getAE(service);
        if (verifyEvent(service, AuditableEvent.EVENT_TYPE_DEPRECATED)) {
          debug.add(" deprecate event returned as expected \n");
          deprecateTS = ae.getTimestamp();
        } else {
          debug.add(" deprecate event not returned as expected \n");
          failcount = failcount + 1;
        }
      }
      // undeprecate the object

      br = blm.unDeprecateObjects(serviceKeys);
      if (br.getExceptions() != null) {
        debug.add(" error on unDeprecateObjects \n");
        Collection ex = br.getExceptions();
        iter = ex.iterator();
        while (iter.hasNext()) {
          JAXRException je = (JAXRException) iter.next();
          debug.add("== Detail Message for the JAXRException object: "
              + je.getMessage() + "\n");
        }
        failcount = failcount + 1;
      } else {
        service = retrieveMyObject(serviceKey);
        ae = getAE(service);
        if (verifyEvent(service, AuditableEvent.EVENT_TYPE_UNDEPRECATED)) {
          debug.add(" deprecate event returned as expected \n");
          unDeprecateTS = ae.getTimestamp();
        } else {
          debug.add(" unDeprecate event not returned as expected \n");
          failcount = failcount + 1;
        }
      }

      //
      // delete event....
      try {
        debug.add(
            "delete event time - service key is " + serviceKey.getId() + "\n");
        br = blm.deleteServices(serviceKeys);
        if (br.getExceptions() != null) {
          debug.add(
              "WARNING:  cleanup encountered an error while trying to delete the service.\n");
        } else
          serviceKeys = null; //
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        debug.add("Caught Exception while trying to delete the service \n");
      }
      // == after delete
      debug.add("Just deleted service: " + serviceId + "\n");
      debug.add("use query to get the audit trail for the delete event\n");
      DeclarativeQueryManager dqm = rs.getDeclarativeQueryManager();
      Query query = dqm.createQuery(Query.QUERY_TYPE_SQL,
          "SELECT * FROM AuditableEvent ae, AffectedObject o WHERE o.id = '"
              + serviceId
              + "' AND o.eventId = ae.id AND ae.eventType = 'urn:oasis:names:tc:ebxml-regrep:EventType:Deleted'");
      // urn:uuid:35ee4757-be1a-4200-bc03-d6b2992ddd27'");

      br = dqm.executeQuery(query);
      if (br.getExceptions() != null) {
        failcount = failcount + 1;
        debug.add("Expected Delete event not found \n");
      } else
        debug.add("No exceptions from executeQuery\n");

      // results of query after delete.
      Collection events = br.getCollection();
      iter = events.iterator();
      debug.add(
          "size returned from query after delete is: " + events.size() + "\n");
      while (iter.hasNext()) {
        ae = (AuditableEvent) iter.next();
        deleteTS = ae.getTimestamp();
        deleteUser = ae.getUser().getPersonName().getLastName();
        printEventType(ae);
        if (ae.getEventType() == AuditableEvent.EVENT_TYPE_DELETED) {
          debug.add("Expected Delete event found \n");
        } else
          failcount = failcount + 1;
      } // end of while

      // user should be the same for create and delete events
      if (!(createUser.equals(deleteUser))) {
        failcount = failcount + 1;
        debug.add("create and delete user should match \n");
      }
      debug.add(" good - create and delete users match \n");

      // test create and delete timestamp
      if (createTS == null)
        debug.add("create timestamp was not created  - test failure\n");
      if (deleteTS == null)
        debug.add("deletetimestamp was not created  - test failure\n");
      // see if we can check create and delete time.
      if ((createTS != null) & (deleteTS != null)) {
        if (!(createTS.before(deleteTS))) {
          failcount = failcount + 1;
          debug.add("create timestamp should be before delete timestamp \n");
        }
        debug.add(
            " good - create timestamp is earlier than delete timestamp \n");
      } else
        failcount = failcount + 1; // we could not check it - count as a failure

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
      debug.add("End of test method - delete the Service \n");
      // cleanup...
      try {
        debug.add("Cleanup time -service key is " + serviceKey.getId() + "\n");
        if (serviceKey != null) {
          BulkResponse br = blm.deleteServices(serviceKeys);
          if (br.getExceptions() != null) {
            debug.add(
                "WARNING:  cleanup encountered an error while trying to delete the service.\n");
          }
        }
        if (orgKey != null) {
          BulkResponse br = blm.deleteOrganizations(orgKeys);
          if (br.getExceptions() != null) {
            debug.add(
                "WARNING:  cleanup encountered an error while trying to delete an organization.\n");
          }
        }

      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        debug.add("Caught Exception while trying to delete the service \n");
      }
    }
    debug.add("Number of failures: " + failcount + " \n");
    if (failcount > 0)
      throw new Fault(testName + " had test failures");

  } // end of method.

  private boolean verifyEvent(Service service, int expectedEvent) {
    boolean status = false;
    try {
      Collection audits = service.getAuditTrail();
      Iterator iter = audits.iterator();
      debug.add("VerifyEvent - Number of auditable events returned is: "
          + audits.size() + "\n");
      while (iter.hasNext()) {
        AuditableEvent ae = (AuditableEvent) iter.next();
        printEventType(ae);
        if (ae.getEventType() == expectedEvent) {
          return true;
        }
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return status;

  } // end of verifyEvent method

  private AuditableEvent getAE(Service service) {
    AuditableEvent ae = null;
    try {
      Collection audits = service.getAuditTrail();
      Iterator iter = audits.iterator();
      debug.add(" getAE - Number of auditable events returned is: "
          + audits.size() + "\n");
      while (iter.hasNext()) {
        ae = (AuditableEvent) iter.next();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return ae;
  }

  private Key saveMyService(Service service) {
    Key key = null;
    Collection services = new ArrayList();
    services.add(service);
    try {
      BulkResponse br = blm.saveServices(services);
      if (br.getExceptions() != null) {
        debug.add("Error:   saveServices failed \n");
        debug.add("== The JAXR Response Status: "
            + checkBulkResponseStatus(br.getStatus()) + "\n");
        Collection ex = br.getExceptions();
        Iterator iter = ex.iterator();
        //
        while (iter.hasNext()) {
          JAXRException je = (JAXRException) iter.next();
          debug.add("== Detail Message for the JAXRException object: "
              + je.getMessage() + "\n");
        }

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

  private Service retrieveMyObject(Key key) {
    Service service = null;
    Collection serviceKeys = new ArrayList();
    serviceKeys.add(key);
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      BulkResponse br = bqm.getRegistryObjects(serviceKeys,
          LifeCycleManager.SERVICE);
      if (br.getExceptions() != null) {
        debug.add(
            "Error:  encountered an error while trying to retrieve the SERVICE.\n");
        return null;
      }
      Collection retServices = br.getCollection();
      // Verify that we got back 1 service
      if (retServices.size() != 1)
        return null;
      Iterator iter = retServices.iterator();
      while (iter.hasNext()) {
        service = (Service) iter.next();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
    return service;
  } // end of retrieveMyObject method

  private void printEventType(AuditableEvent ae) {
    try {
      switch (ae.getEventType()) {
      case AuditableEvent.EVENT_TYPE_CREATED:
        debug.add("EVENT_TYPE_CREATED \n");
        break;
      case AuditableEvent.EVENT_TYPE_DELETED:
        debug.add("EVENT_TYPE_DELETED\n");
        break;
      case AuditableEvent.EVENT_TYPE_DEPRECATED:
        debug.add("EVENT_TYPE_DEPRECATED \n");
        break;
      case AuditableEvent.EVENT_TYPE_UNDEPRECATED:
        debug.add("EVENT_TYPE_UNDEPRECATED\n");
        break;
      case AuditableEvent.EVENT_TYPE_UPDATED:
        debug.add("EVENT_TYPE_UPDATED\n");
        break;
      case AuditableEvent.EVENT_TYPE_VERSIONED:
        debug.add("EVENT_TYPE_VERSIONED\n");
        break;
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  } // end of printEventType method....

  static String checkBulkResponseStatus(int status) {
    String response;
    switch (status) {
    case JAXRResponse.STATUS_SUCCESS: {
      response = "STATUS_SUCCESS ";
    }
      break;

    case JAXRResponse.STATUS_FAILURE: {
      response = "STATUS_FAILURE ";
    }
      break;

    case JAXRResponse.STATUS_UNAVAILABLE: {
      response = "STATUS_UNAVAILABLE ";
    }
      break;

    case JAXRResponse.STATUS_WARNING: {
      response = "STATUS_WARNING  ";
    }
      break;

    default: {
      response = "Unknown Status! ";

    }
    } // end of switch
    return response;
  }// end of checkBulkResponseStatus

} // end of class
