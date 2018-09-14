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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.SpecificationLink;

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
   * @testName: specificationLink_setGetUsageDescriptionTest
   *
   * @assertion: setUsageDescription - Sets the description of usage paramaters.
   *
   * @assertion_ids: JAXR:JAVADOC:378;JAXR:JAVADOC:380
   *
   * @test_Strategy: Create a specification link. set the usage parameters.
   * Verify with a call to getUsageDescription
   *
   *
   */
  public void specificationLink_setGetUsageDescriptionTest() throws Fault {
    String testName = "specificationLink_setGetUsageDescriptionTest";
    boolean pass = true;
    String usage = "Testing get/set UsageDescription";
    try {
      SpecificationLink sl = blm.createSpecificationLink();
      InternationalString iusage = blm.createInternationalString(usage);
      sl.setUsageDescription(iusage);
      if (!(sl.getUsageDescription().getValue().equals(usage)))
        throw new Fault(
            testName + "Error: did not get expected usage description");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: specificationLink_setGetUsageParametersTest
   *
   * @assertion: setUsageParameters - Sets any usage parameters. Each parameter
   * is a String getUsageParameters - Gets any usage parameters.
   *
   * @assertion_ids: JAXR:JAVADOC:382;JAXR:JAVADOC:384
   *
   * @test_Strategy: Create a specification link. set the usage parameters.
   * Verify with a call to getUsageDescription
   *
   *
   */
  public void specificationLink_setGetUsageParametersTest() throws Fault {
    String testName = "specificationLink_setGetUsageParametersTest";
    boolean pass = false;
    String usage = "Testing get/set UsageParameters";
    try {
      Collection params = new ArrayList();
      params.add(usage);
      SpecificationLink sl = blm.createSpecificationLink();
      sl.setUsageParameters(params);
      Collection list = sl.getUsageParameters();
      if (list.size() != 1)
        throw new Fault(
            testName + "Error: invalid parameter count: " + list.size());
      TestUtil.logMsg(" Usage parameter is set to: " + usage);

      if ((list.contains((String) usage)))
        pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: specificationLink_setGetSpecificationObjectTest
   *
   * @assertion: getSpecificationObject - Gets the specification object
   * setSpecificationObject - Sets the Concept for this object.
   *
   * @assertion_ids: JAXR:JAVADOC:374; JAXR:JAVADOC:376
   *
   * @test_Strategy: Create a specification link. Create a Concept.
   *
   *
   */
  public void specificationLink_setGetSpecificationObjectTest() throws Fault {
    String testName = "specificationLink_setGetSpecificationObjectTest";
    boolean pass = true;
    String conceptName = "concept for test specificationLink_setGetSpecificationObjectTest";
    try {
      SpecificationLink sl = blm.createSpecificationLink();
      Concept concept = (Concept) blm.createObject(LifeCycleManager.CONCEPT);
      concept.setName(blm.createInternationalString(tsLocale, conceptName));
      sl.setSpecificationObject(concept);
      Concept retConcept = (Concept) sl.getSpecificationObject();
      if (retConcept == null)
        throw new Fault(
            testName + "Error: null returned from getSpecificationObject");

      String retName = retConcept.getName().getValue(tsLocale);
      if (!(retName.equals(conceptName)))
        throw new Fault(testName + "Error: concept name does not match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: specificationLink_getServiceBinding
   *
   * @assertion_ids: JAXR:JAVADOC:386
   *
   * @test_Strategy: Create a specification link and a servicebinding. Add the
   * specification link to the servicebinding. Call getServicebinding
   *
   *
   */
  public void specificationLink_getServiceBinding() throws Fault {
    String testName = "specificationLink_getServiceBinding";
    // String serviceName = "myTestServiceBinding";
    String uri = "binding uri";
    SpecificationLink lnk = null;
    boolean pass = true;
    try {
      SpecificationLink sl = blm.createSpecificationLink();
      ServiceBinding sb = blm.createServiceBinding();
      sb.setValidateURI(false);
      sb.setAccessURI(uri);
      sb.addSpecificationLink(sl);
      ServiceBinding retSb = sl.getServiceBinding();

      if (!(retSb.getAccessURI().equals(uri)))
        throw new Fault(testName
            + "Error: Did not get expected name for servicebinding.  ");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

} // end of test
