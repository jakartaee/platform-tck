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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.Service;

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
  Locale tsLocale = new Locale("en", "US");

  com.sun.ts.tests.jaxr.common.JAXR_Util util = null;

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
      // super.cleanup();

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
   * @testName: service_setProvidingOrganization_Test
   *
   * @assertion: setProvidingOrganization() - Sets the Organization that
   * provides this service. JAXR javadoc
   *
   * @assertion_ids: JAXR:JAVADOC:428; JAXR:JAVADOC:426;
   *
   * @test_Strategy: create an organization. Create a Service. set the providing
   * organizaton and then verify with getProvidingOrganization
   *
   */
  public void service_setProvidingOrganization_Test() throws Fault {
    String testName = "service_setProvidingOrganization_Test";
    String name = "setProvidingOrganization_TestService";
    String serviceName = "testService";

    try {
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, name));
      Service service = blm.createService(serviceName);

      service.setProvidingOrganization(org);
      if (!(service.getProvidingOrganization().getName().getValue(tsLocale)
          .equals(name)))
        throw new Fault(testName + "Error: organization name does not match");
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of test

  /*
   * @testName: service_addGetServiceBindingTest
   *
   * @assertion: addServiceBinding - JAXR javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:430;JAXR:JAVADOC:438;JAXR:SPEC:86;
   *
   * @test_Strategy: Create and add a service binding to a service. Verify the
   * add with a getServiceBindings call
   *
   *
   */
  public void service_addGetServiceBindingTest() throws Fault {
    // ---
    String testName = "service_addGetServiceBindingTest";
    boolean pass = true;
    String serviceName = "testService";
    try {
      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      ServiceBinding sb = blm.createServiceBinding();
      service.addServiceBinding(sb);
      Collection c = service.getServiceBindings();
      Iterator iter = c.iterator();
      while (iter.hasNext()) {
        ServiceBinding retBinding = (ServiceBinding) iter.next();
        if (!(retBinding.getService().getName().getValue(tsLocale)
            .equals(serviceName)))
          throw new Fault(
              testName + "Error: ServiceBinding name does not match");
      }
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + "Error: ServiceBinding name does not match");
    }

  } // end of test

  /*
   * @testName: service_addServiceBindingsTest
   *
   * @assertion: addServiceBindings - Add a Collection of ServiceBinding
   * children JAXR javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:432;
   *
   * @test_Strategy: Create a collection of ServiceBindings and add them to a
   * service. Verify the addition by calling getServiceBindings
   *
   *
   */
  public void service_addServiceBindingsTest() throws Fault {
    // ---
    String testName = "service_addServiceBindingsTest";
    boolean pass = true;
    String serviceName = "testService";
    ServiceBinding sb = null;
    String sbAccessURI = "test service binding";
    int count = 20;
    try {
      Service service = blm.createService(serviceName);
      Collection bindings = new ArrayList();
      for (int i = 0; i < count; i++) {
        sb = blm.createServiceBinding();
        sb.setValidateURI(false);
        sb.setAccessURI(sbAccessURI + i);
        bindings.add(sb);
      }
      // add a collection of service bindings
      service.addServiceBindings(bindings);

      // Verify with a call to getServiceBindings
      Collection c = service.getServiceBindings();
      if (c.size() != count)
        throw new Fault(
            testName + "Error: ServiceBinding count does not match");

      // get the list of service binding names and verify
      ArrayList uris = new ArrayList();
      ArrayList a = new ArrayList(c);
      for (int i = 0; i < count; i++) {
        sb = (ServiceBinding) a.get(i);
        uris.add(sb.getAccessURI());
      }
      for (int i = 0; i < count; i++) {
        // check that each added binding has been returned.
        if (!(uris.contains(sbAccessURI + i)))
          throw new Fault(
              testName + "Error: ServiceBinding access uri does not match");
      }
    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test

  /*
   * @testName: service_removeServiceBindingTest
   *
   * @assertion: removeServiceBinding - Remove a child ServiceBinding JAXR
   * javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:434;
   *
   * @test_Strategy: Create and add a collection of servicebindings to a
   * service. Remove a single servicebinding. Call getServicebindings and get
   * the current collection. Verify that the removed binding is not returned.
   *
   */
  public void service_removeServiceBindingTest() throws Fault {
    // ---
    String testName = "service_removeServiceBindingTest";
    boolean pass = true;
    String serviceName = "testService";
    int count = 20;
    String sburi = "test service binding";
    ServiceBinding sb = null;
    ServiceBinding serviceBinding0 = null;
    try {
      Service service = blm.createService(serviceName);
      Collection bindings = new ArrayList();

      serviceBinding0 = blm.createServiceBinding();
      TestUtil
          .logTrace("validate is set to : " + serviceBinding0.getValidateURI());

      serviceBinding0.setValidateURI(false);
      TestUtil
          .logTrace("validate is set to : " + serviceBinding0.getValidateURI());

      serviceBinding0.setAccessURI(sburi + "0");
      bindings.add(serviceBinding0);

      for (int i = 1; i < count; i++) {
        sb = blm.createServiceBinding();
        sb.setValidateURI(false);
        sb.setAccessURI(sburi + i);
        bindings.add(sb);
      }
      // add a collection of service bindings
      service.addServiceBindings(bindings);

      service.removeServiceBinding(serviceBinding0);
      Collection c = service.getServiceBindings();

      // get the list of service binding names and verify
      ArrayList uris = new ArrayList();
      ArrayList a = new ArrayList(c);
      for (int i = 0; i < c.size(); i++) {
        sb = (ServiceBinding) a.get(i);
        uris.add(sb.getAccessURI());
        TestUtil.logTrace("found: " + sb.getAccessURI());
      }
      if (uris.contains(serviceBinding0.getAccessURI()))
        throw new Fault(testName + "Error: removed ServiceBinding was found");

    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test

  /*
   * @testName: service_removeServiceBindingsTest
   *
   * @assertion: removeServiceBindings - Remove a Collection of children
   * ServiceBindings JAXR javadoc
   *
   *
   * @assertion_ids: JAXR:JAVADOC:436;
   *
   * @test_Strategy: Create and add a collection of servicebindings to a
   * service. Call removeServiceBindings to delete all servicebindings. Verify
   * with a call to getServiceBindings that all of them have been deleted.
   *
   */
  public void service_removeServiceBindingsTest() throws Fault {
    // ---
    String testName = "service_removeServiceBindingsTest";
    boolean pass = true;
    String serviceName = "testService";
    int count = 20;
    String sbURI = "test service binding";
    ServiceBinding sb = null;
    try {
      Service service = blm.createService(serviceName);
      Collection bindings = new ArrayList();

      for (int i = 0; i < count; i++) {
        sb = blm.createServiceBinding();
        sb.setValidateURI(false);
        sb.setAccessURI(sbURI + i);
        bindings.add(sb);
      }
      // add a collection of service bindings
      service.addServiceBindings(bindings);

      service.removeServiceBindings(bindings);
      Collection c = service.getServiceBindings();
      if (c.size() != 0)
        throw new Fault(
            testName + "Error: deleted ServiceBindings were returned");

    } catch (Exception e) {
      TestUtil.logErr("Cleanup error: " + e.toString());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault(testName + " failed ");
  } // end of test

} // end of class
