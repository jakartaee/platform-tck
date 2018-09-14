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
 * @(#)JAXRClient.java  1.21 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.RegistryPackage;

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
   * @testName: registryObjectTest
   *
   * @assertion_ids: JAXR:SPEC:10;JAXR:JAVADOC:441;JAXR:JAVADOC:445;
   * 
   * @test_Strategy: create a registry package add a service to the package
   * verify that the service is returned with a getRegistryObjects remove the
   * service and verify the remove with getRegistryObjects Verify that level 0
   * providers throw an UnsupportedCapabilityException.
   *
   */
  public void registryObjectTest() throws Fault {
    String testName = "registryObjectTest";
    String serviceName = "testService_registryObjectTest";
    int providerlevel = 0;
    String name = "TestOrganizationName_registryObjectTest";
    int failcount = 0;
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      Service service = blm.createService(serviceName);
      // level 0 providers do not support the setExpiration method and must
      // throw
      // an UnsupportedCapabilityException.
      RegistryPackage rp = blm
          .createRegistryPackage("registryPackage_registryObjectTest");
      if (providerlevel == 0) {
        throw new Fault(testName
            + "failed to throw expected UnsupportedCapabilityException.");
      }
      Key serviceKey = service.getKey();
      rp.addRegistryObject(service);

      Set ros = rp.getRegistryObjects();
      Iterator iter = ros.iterator();
      Service s = null;
      while (iter.hasNext()) {
        Object o = iter.next();
        if (o instanceof Service) {
          Service myService = (Service) o;
          if (myService.getKey().getId().equals(serviceKey.getId())) {
            s = myService;
            debug.add("All set, I found my service \n");
            break;
          }
        }
      }
      if (s == null) {
        debug.add(
            "Error - my service was not returned from getRegistryObjects\n");
        failcount = 1;
      }
      rp.removeRegistryObject(service);
      Set regos = rp.getRegistryObjects();

      iter = regos.iterator();
      s = null;
      while (iter.hasNext()) {
        Object o = iter.next();
        if (o instanceof Service) {
          Service myService = (Service) o;
          if (myService.getKey().getId().equals(serviceKey.getId())) {
            debug.add(
                "Error - my service was not removed with removeRegistryObject \n");
            failcount = failcount + 1;
          }
        }
      }
      if (failcount > 0)
        throw new Fault(testName
            + " failed - run with trace = true to see specific error.");
    } catch (UnsupportedCapabilityException uce) {
      if (providerlevel != 0) {
        TestUtil.logErr("Cleanup error: " + uce.toString());
        TestUtil.printStackTrace(uce);
        throw new Fault(
            testName + " failed - unexpected UnsupportedCapabilityException");
      }
    } catch (Exception e) {
      if (providerlevel == 0) {
        TestUtil.logErr(
            "Expected level 0 provider to throw UnsupportedCapabilityException");
      }
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of test

  /*
   * @testName: registryObjectsTest
   *
   * @assertion_ids:
   * JAXR:SPEC:11;JAXR:JAVADOC:443;JAXR:JAVADOC:447;JAXR:JAVADOC:449
   *
   * @test_Strategy: create a registry package add a service to the package also
   * a RegistryEntry
   *
   * verify that the service is returned with a getRegistryObjects remove the
   * service and verify the remove with getRegistryObjects Verify that level 0
   * providers throw an UnsupportedCapabilityException.
   *
   */
  public void registryObjectsTest() throws Fault {
    String testName = "registryObjectsTest";
    String serviceName = "testService_registryObjectsTest";
    int providerlevel = 0;
    String name = "TestOrganizationName_registryObjectsTest";
    int failcount = 0;
    try {
      providerlevel = rs.getCapabilityProfile().getCapabilityLevel();
      Organization org = blm
          .createOrganization(blm.createInternationalString(name));
      Service service = blm.createService(serviceName);

      // level 0 providers do not support the setExpiration method and must
      // throw
      // an UnsupportedCapabilityException.
      RegistryPackage rp = blm.createRegistryPackage(
          blm.createInternationalString("registryPackage_registryObjectTest"));
      if (providerlevel == 0) {
        throw new Fault(testName
            + "failed to throw expected UnsupportedCapabilityException.");
      }
      Key serviceKey = service.getKey();
      Key orgKey = org.getKey();
      debug.add("Service key is: " + serviceKey.getId() + "\n");
      debug.add("orgKey is: " + orgKey.getId() + "\n");
      Collection robs = new ArrayList();
      robs.add(service);
      robs.add(org);
      rp.addRegistryObjects(robs);
      Set ros = rp.getRegistryObjects();
      debug.add("Number of registry objects returned is " + ros.size() + "\n");
      Iterator iter = ros.iterator();
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
        debug.add("Error - getRegistryObjects() did not return my service \n");
      }
      if (myOrg == null) {
        failcount = failcount + 1;
        debug.add(
            "Error - getRegistryObjects() did not return my organization \n");
      }
      rp.removeRegistryObjects(robs);
      Set regos = rp.getRegistryObjects();

      iter = regos.iterator();
      while (iter.hasNext()) {
        Object o = iter.next();
        if (o instanceof Service) {
          Service s = (Service) o;
          if (s.getKey().getId().equals(serviceKey.getId())) {
            failcount = failcount + 1;
            debug.add(
                "Error - my service was not removed with removeRegistryObjects \n");
          }
        }
        if (o instanceof Organization) {
          Organization orgn = (Organization) o;
          if (orgn.getKey().getId().equals(orgKey.getId())) {
            failcount = failcount + 1;
            debug.add(
                "Error - my organization was not removed with removeRegistryObjects \n");
          }
        }

      }
      if (failcount > 0)
        throw new Fault(testName + " failed ");
    } catch (UnsupportedCapabilityException uce) {
      if (providerlevel != 0) {
        TestUtil.logErr("Cleanup error: " + uce.toString());
        TestUtil.printStackTrace(uce);
        throw new Fault(
            testName + " failed - unexpected UnsupportedCapabilityException");
      }
    } catch (Exception e) {
      if (providerlevel == 0) {
        TestUtil.logErr(
            "Expected level 0 provider to throw UnsupportedCapabilityException");
      }
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of test

}
