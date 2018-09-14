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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.ServiceBinding;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxr.common.JAXRCommonClient;

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
   * jaxrWebContext; webServerHost; webServerPort; jaxrAlias; jaxrAlias2;
   * jaxrAliasPassword; jaxrAlias2Password;
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
   * @testName: serviceBinding_addGetSpecificationLinkTest
   *
   * @assertion_ids: JAXR:JAVADOC:416; JAXR:JAVADOC:424;JAXR:SPEC:88;
   *
   * @assertion: addSpecificationLink- Add a child SpecificationLink
   * getSpecificationLinks - Get all children SpecificationLinks
   *
   * @test_Strategy: Add a SpecificationLink to serviceBinding and verify with
   * getSpecificationLinks
   *
   *
   */
  public void serviceBinding_addGetSpecificationLinkTest() throws Fault {
    String testName = "serviceBinding_addGetSpecificationLinkTest";
    boolean pass = true;
    SpecificationLink lnk = null;
    String usageDescription = "To test addSpecificationLink";
    try {
      ServiceBinding sb = blm.createServiceBinding();
      // add a child specification link
      SpecificationLink sl = blm.createSpecificationLink();
      InternationalString iusageDescription = blm
          .createInternationalString(usageDescription);
      sl.setUsageDescription(iusageDescription);
      sb.addSpecificationLink(sl);
      // Verify the child specification link
      Collection c = sb.getSpecificationLinks();
      Iterator iter = c.iterator();
      while (iter.hasNext()) {
        lnk = (SpecificationLink) iter.next();
        if (!(lnk.getUsageDescription().getValue().equals(usageDescription)))
          throw new Fault(testName
              + "Error: getSpecificationLinks - can't find added SpecificationLink");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_addGetSpecificationLinksTest
   *
   * @assertion_ids: JAXR:JAVADOC:418;
   *
   * @assertion: addSpecificationLinks - Add a Collection of SpecificationLink
   * children getSpecificationLinks - Get all children SpecificationLinks
   *
   *
   * @test_Strategy: Add a collection of SpecificationLinks to serviceBinding
   * and verify with getSpecificationLinks
   *
   *
   */
  public void serviceBinding_addGetSpecificationLinksTest() throws Fault {
    String testName = "serviceBinding_addGetSpecificationLinksTest";
    int count = 3;
    SpecificationLink lnk = null;
    boolean pass = true;
    try {
      ServiceBinding sb = blm.createServiceBinding();
      Collection lnks = new ArrayList();
      // add children
      for (int i = 0; i < count; i++) {
        lnk = blm.createSpecificationLink();
        lnks.add(lnk);
      }
      sb.addSpecificationLinks(lnks);
      // Verify the child specification link
      Collection c = sb.getSpecificationLinks();
      if (!(c.size() == count))
        throw new Fault(testName + "Error: should have 3 SpecificationLinks");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_setGetAccessURI
   *
   *
   * @assertion_ids: JAXR:JAVADOC:406;JAXR:JAVADOC:404;
   *
   * @assertion: setAccessURI - Sets the URI that gives access to the service
   * via this binding. getAccessURI - Gets the URI that gives access to the
   * service via this binding
   *
   *
   * @test_Strategy: Create a serviceBinding. Set the access URI then verify
   * with getAccessURI
   *
   *
   */
  public void serviceBinding_setGetAccessURI() throws Fault {
    String testName = "serviceBinding_setGetAccessURI";
    String url = baseuri + "jaxrTestPage1.html";
    boolean pass = true;
    try {
      ServiceBinding sb = blm.createServiceBinding();
      sb.setAccessURI(url);
      if (!(sb.getAccessURI().equals(url)))
        throw new Fault(testName + "Error: did not get expected URI back");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_getAccessURINullTest
   *
   * @assertion_ids: JAXR:JAVADOC:404;
   * 
   * @assertion: getAccessURI - Gets the URI that gives access to the service
   * via this binding Default is a NULL String.
   *
   * @test_Strategy: Create a serviceBinding. Call getAccessURI() and verify
   * that null is returned.
   *
   *
   */
  public void serviceBinding_getAccessURINullTest() throws Fault {
    String testName = "serviceBinding_getAccessURINullTest";
    boolean pass = true;
    try {
      ServiceBinding sb = blm.createServiceBinding();
      if (!(sb.getAccessURI() == null))
        throw new Fault(testName + "Error: AccessURI expected to be null");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_setAccessURIInvalidTest
   *
   * @assertion_ids: JAXR:JAVADOC:407;
   *
   * @assertion: setAccessURI - Sets the URI that gives access to the service
   * via this binding.
   *
   *
   * @test_Strategy: Create a serviceBinding. Set a target binding. Verify that
   * attempting to set access uri now will throw an InvalidRequestException
   *
   */
  public void serviceBinding_setAccessURIInvalidTest() throws Fault {
    String testName = "serviceBinding_setAccessURIInvalidTest";
    String url = baseuri + "jaxrTestPage1.html";
    boolean pass = false;
    try {
      ServiceBinding sb = blm.createServiceBinding();
      ServiceBinding target = blm.createServiceBinding();
      sb.setTargetBinding(target);
      sb.setAccessURI(url);
      // we should not have gotten this far!
      throw new Fault(
          testName + "Error: InvalidRequestException was not thrown");
    } catch (InvalidRequestException ir) {
      TestUtil.printStackTrace(ir);
      pass = true;
      debug.add("InvalidRequestException was thrown as expected\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_getService
   *
   *
   * @assertion_ids: JAXR:JAVADOC:414;
   * 
   * @assertion: getService - Gets the parent service for which this is a
   * binding
   *
   * @test_Strategy:
   *
   *
   */
  public void serviceBinding_getService() throws Fault {
    String testName = "serviceBinding_getService";
    String serviceName = "test service for serviceBinding_getService";
    boolean pass = true;
    try {
      ServiceBinding sb = blm.createServiceBinding();
      // create a Service
      Service service = blm
          .createService(blm.createInternationalString(tsLocale, serviceName));
      service.addServiceBinding(sb);
      Service testService = sb.getService();
      if (testService == null)
        throw new Fault(
            testName + "Error: serviceBinding returned a null service");
      if (!(testService.getName().getValue(tsLocale).equals(serviceName)))
        throw new Fault(testName + "Error: did not get expected Service");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_setGetTargetBinding
   *
   *
   * @assertion_ids: JAXR:JAVADOC:409; JAXR:JAVADOC:411;
   *
   * @assertion: setTargetBinding - Sets the next ServiceBinding in case there
   * is a redirection getTargetBinding - Gets the next ServiceBinding in case
   * there is a redirection JAXR javadoc
   *
   * @test_Strategy: create a service binding. Set the target binding. Verify
   * with a call to getTargetBinding
   *
   *
   */
  public void serviceBinding_setGetTargetBinding() throws Fault {
    String testName = "serviceBinding_setGetTargetBinding";
    String uri = "target binding uri";
    boolean pass = true;
    try {
      ServiceBinding sb = blm.createServiceBinding();
      ServiceBinding target = blm.createServiceBinding();
      target.setValidateURI(false);
      // set the access uri so we can verify the set
      target.setAccessURI(uri);
      sb.setTargetBinding(target);
      ServiceBinding retTarget = sb.getTargetBinding();
      if (!(retTarget.getAccessURI().equals(uri)))
        throw new Fault(testName + "Error: did not get expected target");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_setTargetBindingInvalidTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:409; JAXR:JAVADOC:412;
   *
   * @assertion: setTargetBinding - Sets the next ServiceBinding in case there
   * is a redirection The targetBinding is mutually exclusive from accessURI.
   * JAXR Provider must throw an InvalidRequestExcpetion if a targetBinding is
   * set when there is already a non-null accessURI defined.
   *
   *
   * @test_Strategy: Create a serviceBinding. Set an AccessURI. Verify that
   * attempting to set a target bindgin now will throw an
   * InvalidRequestException
   *
   *
   */
  public void serviceBinding_setTargetBindingInvalidTest() throws Fault {
    String testName = "serviceBinding_setTargetBindingInvalidTest";
    String url = baseuri + "jaxrTestPage1.html";
    boolean pass = false;
    try {
      ServiceBinding sb = blm.createServiceBinding();
      sb.setAccessURI(url);
      ServiceBinding target = blm.createServiceBinding();
      sb.setTargetBinding(target);
      // we should not have gotten this far!
      throw new Fault(
          testName + "Error: InvalidRequestException was not thrown");
    } catch (InvalidRequestException ir) {
      TestUtil.printStackTrace(ir);
      pass = true;
      debug.add("InvalidRequestException was thrown as expected\n");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_removeSpecificationLink
   *
   *
   * @assertion_ids: JAXR:JAVADOC:420;
   *
   * @assertion: removeSpecificationLink - Remove a child SpecificationLink
   * 
   * JAXR javadoc
   *
   * @test_Strategy: create a service binding. Add a child SpecificationLink
   * Call removeSpecificationLink to remove it. Verify it was removed by calling
   * getSpecificationLinks
   *
   *
   */
  public void serviceBinding_removeSpecificationLink() throws Fault {
    String testName = "serviceBinding_removeSpecificationLink";
    boolean pass = true;
    SpecificationLink lnk = null;
    String usageDescription = "To test serviceBinding_removeSpecificationLink";
    try {
      ServiceBinding sb = blm.createServiceBinding();
      // add a child specification link
      SpecificationLink sl = blm.createSpecificationLink();
      InternationalString iusageDescription = blm
          .createInternationalString(usageDescription);
      sl.setUsageDescription(iusageDescription);
      sb.addSpecificationLink(sl);
      // Verify the child specification link
      Collection c = sb.getSpecificationLinks();
      Iterator iter = c.iterator();
      while (iter.hasNext()) {
        lnk = (SpecificationLink) iter.next();
        if (!(lnk.getUsageDescription().getValue().equals(usageDescription)))
          throw new Fault(testName
              + "Error: test did not complete.  Cannot find SpecificationLink");
      }
      // specification was added and verified. now remove and verify.
      sb.removeSpecificationLink(sl);
      c = sb.getSpecificationLinks();
      if (!(c.size() == 0))
        throw new Fault(testName + "Error: SpecificationLink was not removed");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: serviceBinding_removeSpecificationLinksTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:422;
   *
   * @assertion: removeSpecificationLinks - Remove a Collection of children
   * SpecificationLinks
   * 
   *
   * @test_Strategy: create a service binding. Add children SpecificationLinks
   * Call removeSpecificationLinks to remove them. Verify it was removed by
   * calling getSpecificationLinks
   *
   *
   */
  public void serviceBinding_removeSpecificationLinksTest() throws Fault {
    String testName = "serviceBinding_removeSpecificationLinksTest";
    boolean pass = true;
    SpecificationLink lnk = null;
    int count = 5;
    String usageDescription = "To test serviceBinding_removeSpecificationLink";
    try {
      ServiceBinding sb = blm.createServiceBinding();
      Collection lnks = new ArrayList();
      // add children
      for (int i = 0; i < count; i++) {
        lnk = blm.createSpecificationLink();
        lnks.add(lnk);
      }
      sb.addSpecificationLinks(lnks);
      // Verify the child specification link
      Collection c = sb.getSpecificationLinks();
      if (!(c.size() == count))
        throw new Fault(testName
            + "Error: test did not complete - incorrect count of SpecificationLinks");

      // specification was added and verified. now remove and verify.
      sb.removeSpecificationLinks(lnks);
      c = sb.getSpecificationLinks();
      if (!(c.size() == 0))
        throw new Fault(
            testName + "Error: SpecificationLinks were not removed");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

} // end of test class
