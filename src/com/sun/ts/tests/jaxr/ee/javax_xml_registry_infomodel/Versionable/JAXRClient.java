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
package com.sun.ts.tests.jaxr.ee.javax_xml_registry_infomodel.Versionable;

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
      // print out messages
      if (debug != null)
        TestUtil.logTrace(debug.toString());
    }
  }

  /*
   * @testName: versionable_Test
   *
   * @assertion_ids: JAXR:JAVADOC:318;JAXR:JAVADOC:316;
   *
   * @test_Strategy: level 0 providers must throw UnsupportedCapabilityException
   * For level 1 providers, create a versionable object - Service Set the user
   * version. Verify it.
   * 
   *
   */
  public void versionable_Test() throws Fault {
    String testName = "versionable_Test";
    int failcount = 0;
    Key serviceKey = null;
    String serviceId = null;
    Collection serviceKeys = null;
    int providerlevel = 0;

    try {
      String serviceName = testName + "_service";
      debug.add("Create an service registryObject \n");
      Service service = blm.createService(serviceName);
      String userVersion = testName + "_User_Revision";

      service.setUserVersion(userVersion);
      // test for level 0 provider
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      if (providerlevel == 0)
        throw new Fault(testName
            + " UnsupportedCapabilityException expected for level 0 providers!");
      serviceKey = saveMyService(service);
      if (serviceKey == null)
        throw new Fault(
            testName + " due to save service errors , test did not complete!");
      serviceId = serviceKey.getId();
      serviceKeys = new ArrayList();
      serviceKeys.add(serviceKey);

      // ===============
      // Retrieve the newly published service from the registry
      service = retrieveMyObject(serviceKey);
      // ==
      debug.add("user version should be " + userVersion + "\n");
      if (service.getUserVersion().equals(userVersion))
        debug.add("getUserVersion returned expected result \n");
      else {
        debug.add("Unexpected result from getUserVersion, it returned: "
            + service.getUserVersion() + "\n");
        failcount = failcount + 1;
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
      debug.add("End of test method - delete the Service \n");
      // cleanup...
      try {
        BulkResponse br = blm.deleteServices(serviceKeys);
        debug.add("do cleanup - delete service \n");
        if (br.getExceptions() != null) {
          debug.add(
              "WARNING:  cleanup encountered an error while trying to delete the service.\n");
        }
      } catch (Exception ee) {
        TestUtil.printStackTrace(ee);
        debug.add("Caught Exception while trying to delete the service \n");
      }
    }
    if (failcount > 0)
      throw new Fault(testName + " failed ");

  } // end of method.

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

} // end of class
