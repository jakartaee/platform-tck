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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.ExternalLink;

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
   * @testName: externalLink_getExternalURITest
   *
   * @assertion_ids: JAXR:JAVADOC:692;JAXR:SPEC:100;
   *
   * @assertion: getExternalURI - Gets URI to the an external resource Default
   * is a NULL String.
   *
   * JAXR javadoc
   *
   * @test_Strategy: Create an External Link with externalURI set to
   * java.sun.com. Verify that getExternalURI returns java.sun.com.
   *
   */
  public void externalLink_getExternalURITest() throws Fault {
    String testName = "externalLink_getExternalURITest";
    String externalURI = baseuri + "jaxrTestPage1.html";
    String description = "JAXR test page";
    boolean pass = false;
    try {
      ExternalLink el = blm.createExternalLink(externalURI, description);
      String ret = el.getExternalURI();
      debug.add("getExternalURI returned " + ret + "\n");
      if (ret.equals(externalURI))
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
   * @testName: externalLink_setExternalURITest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:694;
   *
   * @assertion: setExternalURI - Sets URI for an external resource
   *
   * JAXR javadoc
   *
   * @test_Strategy: Create an External Link with externalURI set to
   * java.sun.com. Use the setExternalURI method to change to www.sun.com.
   * Verify that getExternalURI returns www.sun.com.
   *
   */
  public void externalLink_setExternalURITest() throws Fault {
    String testName = "externalLink_setExternalURITest";
    String createURI = baseuri + "jaxrTestPage1.html";
    String externalURI = baseuri + "jaxrTestPage2.html";
    String description = "JAXR test page";
    boolean pass = false;
    try {
      ExternalLink el = blm.createExternalLink(createURI, description);
      el.setExternalURI(externalURI);
      String ret = el.getExternalURI();
      debug.add("getExternalURI returned " + ret + "\n");
      if (ret.equals(externalURI))
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
   * @testName: externalLink_getLinkedObjectsEmptyTest
   *
   * @assertion: getLinkedObjects - Gets the collection of RegistryObjects that
   * are annotated by this ExternalLink. returns Collection of RegistryObjects.
   * Return an empty Collection if no RegistryObjects are annotated by this
   * object.
   *
   * JAXR javadoc
   *
   * @test_Strategy: Call getLinkedObjects with no RegistryObjects annotated
   * Verify that an empty collection is returned.
   *
   */
  public void externalLink_getLinkedObjectsEmptyTest() throws Fault {
    String testName = "externalLink_getLinkedObjectsEmptyTest";
    String externalURI = baseuri + "jaxrTestPage1.html";
    String description = "JAXR test page";

    boolean pass = false;
    try {
      ExternalLink el = blm.createExternalLink(externalURI, description);
      Collection c = el.getLinkedObjects();
      if (c.size() == 0)
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
   * @testName: externalLink_getLinkedObjectsTest
   *
   *
   * @assertion_ids: JAXR:JAVADOC:690;
   *
   * @assertion: getLinkedObjects - Gets the collection of RegistryObjects that
   * are annotated by this ExternalLink. returns Collection of RegistryObjects.
   * Return an empty Collection if no RegistryObjects are annotated by this
   * object.
   *
   * @test_Strategy: Call getLinkedObjects
   *
   */
  public void externalLink_getLinkedObjectsTest() throws Fault {
    String testName = "externalLink_getLinkedObjectsTest";
    String externalURI = baseuri + "jaxrTestPage1.html";
    String description = baseuri + "jaxrTestPage2.html";
    String testOrg = "TestOrganization";

    boolean pass = false;
    try {
      ExternalLink el = blm.createExternalLink(externalURI, description);
      InternationalString itestOrg = blm.createInternationalString(tsLocale,
          testOrg);
      Organization org = blm.createOrganization(itestOrg);
      org.addExternalLink(el);
      Collection c = el.getLinkedObjects();
      Iterator iter = c.iterator();

      if (c.size() == 1) {
        while (iter.hasNext()) {
          Organization o = (Organization) iter.next();
          if (o.getName().getValue(tsLocale).equals(testOrg))
            pass = true;
        }
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
   * TestName: externalLink_invalidURITest
   *
   * @assertion_ids: JAXR:SPEC:109;JAXR:SPEC:102;
   *
   * @test_Strategy: Create an external link with an invalid URI. Verify an
   * InvalidRequestException is thrown.
   *
   */
  public void externalLink_invalidURITest() throws Fault {
    String testName = "externalLink_invalidURITest";
    boolean pass = true;
    String uriDescription = "pass an invalid URI";
    String invalidURI = "this is not a valid uri";

    try {
      // an invalid URI must throw an InvalidRequestException
      ExternalLink el = blm.createExternalLink(invalidURI, uriDescription);
      debug.add("Error: InvalidRequestException not thrown as expected\n");
      pass = false;

    } catch (InvalidRequestException ire) {
      TestUtil.printStackTrace(ire);
      debug.add("Good: InvalidRequestException was thrown as expected\n");
    } catch (Exception ue) {
      debug.add("Error: InvalidRequestException not thrown as expected\n");
      TestUtil.logErr("Caught exception: " + ue.getMessage());
      pass = false;
      TestUtil.printStackTrace(ue);
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

} // end of test class
