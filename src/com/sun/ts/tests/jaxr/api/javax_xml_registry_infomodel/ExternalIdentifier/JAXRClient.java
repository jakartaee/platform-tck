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

package com.sun.ts.tests.jaxr.api.javax_xml_registry_infomodel.ExternalIdentifier;

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
      super.cleanUpRegistry(); //
      debug.clear();

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
   * @testName: externalIdentifier_getRegistryObjectTest
   * 
   * @assertion_ids: JAXR:JAVADOC:696
   *
   * @test_Strategy: getRegistryObject - verify that this returns the parent
   * RegistryObject for this ExternalIdentifier. Create an ExternalIdentifier.
   * Create a parent Organization and add the ExternalIdentifier to the org
   * parent. Get the parent RegistryObject from the ExternalIdentifier Verify
   * the parent by checking the orgName
   */
  public void externalIdentifier_getRegistryObjectTest() throws Fault {
    String testName = "externalIdentifier_getRegistryObjectTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";
    String orgName = JAXR_Util.TS_DEFAULT_ORGANIZATION_NAME;

    try {
      debug.add("Create a parent organization \n");
      Organization org = blm
          .createOrganization(blm.createInternationalString(tsLocale, orgName));

      debug.add("Create an ExternalIdentifier \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(tsLocale, name);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, iName,
          value);
      debug.add("add the ExternalIdentifier to the org parent \n");
      org.addExternalIdentifier(ei);

      debug
          .add("Get the parent RegistryObject  from the ExternalIdentifier \n");
      Organization ro = (Organization) ei.getRegistryObject();
      if (ro == null)
        throw new Fault(
            testName + " failed - getRegistryObject returned null!");

      debug.add("Verify the RegistryObject returned by checking the orgName= "
          + orgName + "\n");

      if (ro.getName().getValue(tsLocale).equals(orgName)) {
        debug.add("Got the parent Organization: "
            + ro.getName().getValue(tsLocale) + "\n");
      } else
        throw new Fault(
            testName + " failed - unexpected name returned for RegistryObject");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: externalIdentifier_getValueTest
   *
   * @assertion_ids: JAXR:JAVADOC:698;JAXR:SPEC:99;
   *
   * @test_Strategy: getValue -Gets the value of an ExternalIdentifier. Create
   * an ExternalIdentifier. Call the ExternalIdentifier.getValue method and
   * verify the returned String matches the value passed in the
   * createExternalIdentifier method.
   * 
   *
   *
   */
  public void externalIdentifier_getValueTest() throws Fault {
    String testName = "externalIdentifier_getValueTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";
    try {
      debug.add("Create an ExternalIdentifier \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(name);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, iName,
          value);
      debug.add("Verify that getValue returns  : " + value + "\n");
      if (ei.getValue().equals(value)) {
        debug.add(
            "String returned from ExternalIdentifier.getValue matched value \n");
      } else {
        throw new Fault(testName
            + " failed - did not get back expected String from getValuE");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: externalIdentifier_setValueTest
   *
   * @assertion_ids: JAXR:JAVADOC:700
   *
   * @test_Strategy: setValue - Sets the value of an ExternalIdentifier. Create
   * an ExternalIdentifier. Call ExternalIdentifier.setValue to set the value.
   * Call the ExternalIdentifier.getValue method and verify the returned String
   * matches the value from ExternalIdentifier.setValue
   */
  public void externalIdentifier_setValueTest() throws Fault {
    String testName = "externalIdentifier_setValueTest";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";
    String newValue = "032-11-1201";
    try {
      debug.add("Create an ExternalIdentifier \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      InternationalString iName = blm.createInternationalString(name);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, iName,
          value);
      debug.add(
          "Set value with ExternalIdentifier.setValue to: " + newValue + "\n");
      ei.setValue(newValue);
      debug.add("Verify getValue returns : " + newValue + "\n");
      if (ei.getValue().equals(newValue)) {
        debug.add(
            "String returned from ExternalIdentifier.getValue matched value \n");
      } else {
        throw new Fault(testName
            + " failed - did not get back expected String from getValue");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: externalIdentifier_getIdentificationScheme
   *
   * @assertion_ids: JAXR:JAVADOC:702
   *
   * @test_Strategy: getIdentificationScheme - Gets the ClassificationScheme
   * that is used as the identification scheme for identifying this object.
   * Create an ExternalIdentifier with a scheme. Verify that
   * getIdentificationScheme returns the scheme passed into the create method.
   *
   */
  public void externalIdentifier_getIdentificationScheme() throws Fault {
    String testName = "externalIdentifier_getIdentificationScheme";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";
    try {
      debug.add("Create an ExternalIdentifier \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);
      ExternalIdentifier ei = blm.createExternalIdentifier(scheme, name, value);
      debug.add(
          "Verify that ExternalIdentifier.getIdentificationScheme() returns scheme from create method\n");
      ClassificationScheme retScheme = ei.getIdentificationScheme();
      if (retScheme.getDescription().getValue().equals(cDescription)) {
        debug.add("Found expected description from scheme: "
            + retScheme.getDescription().getValue() + "\n");
      } else
        throw new Fault(testName
            + " failed - expected description not returned from scheme");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

  /*
   * @testName: externalIdentifier_setIdentificationScheme
   *
   * @assertion_ids: JAXR:JAVADOC:704
   *
   * @test_Strategy: setIdentificationScheme - Sets the ClassificationScheme
   * that is used as the identification scheme for identifying this object. Use
   * createObject to create an ExternalIdentifier. Set the scheme with
   * setIdentificationScheme method. Verify with getIdentificationScheme
   */
  public void externalIdentifier_setIdentificationScheme() throws Fault {
    String testName = "externalIdentifier_setIdentificationScheme";
    String cName = "Social Security";
    String cDescription = "Social Security Numbers";
    String name = "Social Security Number";
    String value = "026-10-5738";
    try {
      debug.add("Create an ExternalIdentifier \n");
      ClassificationScheme scheme = blm.createClassificationScheme(cName,
          cDescription);

      ExternalIdentifier ei = (ExternalIdentifier) blm
          .createObject(LifeCycleManager.EXTERNAL_IDENTIFIER);
      ei.setIdentificationScheme(scheme);
      debug.add(
          "call ei.setIdentificationScheme(scheme) with this scheme description: "
              + cDescription + "\n");
      ClassificationScheme retScheme = ei.getIdentificationScheme();
      debug.add(
          "Verify scheme returned from  ei.getIdentificationScheme by checking the scheme description \n");
      if (retScheme.getDescription().getValue().equals(cDescription)) {
        debug.add("Found expected description from scheme: "
            + retScheme.getDescription().getValue() + "\n");
      } else
        throw new Fault(testName
            + " failed - expected description not returned from scheme");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed ");
    }
  } // end of method

} // end of class
