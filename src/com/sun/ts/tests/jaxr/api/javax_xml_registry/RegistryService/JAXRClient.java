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
 * @(#)JAXRClient.java	1.8 03/05/16
 */

/*
 * @(#)JAXRClient.java  1.6     01/08/30
 */
package com.sun.ts.tests.jaxr.api.javax_xml_registry.RegistryService;

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
  public static void main(String[] args) {
    JAXRClient theTests = new JAXRClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */
  /*
   * @class.setup_props: jaxrPassword; jaxrUser; registryURL; jaxrPassword2;
   * jaxrUser2; queryManagerURL; authenticationMethod; providerCapability;
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
   * @testName: registryService_getCapabilityProfile
   *
   * @assertion_ids: JAXR:JAVADOC:56;JAXR:SPEC:15;
   *
   * @test_Strategy: Invoke getCapabilityProfile on the RegistryService and
   * verify it returns a CapabilityProfile
   * 
   *
   */
  public void registryService_getCapabilityProfile() throws Fault {
    String testName = "registryService_getCapabilityProfile";
    boolean pass = true;
    String usage = "Testing get/set UsageDescription";

    try {
      CapabilityProfile cp = rs.getCapabilityProfile();

      if (!(cp instanceof CapabilityProfile))
        throw new Fault(
            testName + " RegistryService failed to return a CapabilityProfile");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: registryService_getBusinessLifeCycleManager
   *
   * @assertion_ids: JAXR:JAVADOC:58;
   *
   * @test_Strategy: Invoke registryService_getBusinessLifeCycleManager on the
   * RegistryService and verify it returns a BusinessLifeCycleManager
   *
   *
   */
  public void registryService_getBusinessLifeCycleManager() throws Fault {
    String testName = "registryService_getBusinessLifeCycleManager";
    boolean pass = true;
    String usage = "Testing get/set UsageDescription";

    try {
      BusinessLifeCycleManager blm = rs.getBusinessLifeCycleManager();
      if (!(blm instanceof BusinessLifeCycleManager))
        throw new Fault(testName
            + " RegistryService failed to return a BusinessLifeCycleManager ");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: registryService_getBusinessQueryManager
   *
   * @assertion_ids: JAXR:JAVADOC:60
   *
   * @test_Strategy: Invoke getBusinessQueryManager on the RegistryService and
   * verify it returns a BusinessQueryManager
   *
   *
   */
  public void registryService_getBusinessQueryManager() throws Fault {
    String testName = "registryService_getBusinessQueryManager";
    boolean pass = true;
    try {
      BusinessQueryManager bqm = rs.getBusinessQueryManager();
      if (!(bqm instanceof BusinessQueryManager))
        throw new Fault(testName
            + " RegistryService failed to return a BusinessQueryManager");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault(testName + "failed ");
  }

  /*
   * @testName: registryService_getDeclarativeQueryManager
   *
   * @assertion_ids: JAXR:JAVADOC:64;JAXR:SPEC:18;
   *
   * @test_Strategy: Invoke getDeclarativeQueryManager on the RegistryService
   * and verify it returns an UnsupportedCapabilityException for level 0
   * providers. Verify it returns DeclarativeQueryManager for >0 level providers
   *
   *
   */
  public void registryService_getDeclarativeQueryManager() throws Fault {
    String testName = "registryService_getDeclarativeQueryManager ";
    try {
      debug.add("Provider capablility level is :" + capabilityLevel + "\n");
      debug.add("getDeclarativeQueryManager is a level 1 method \n");
      debug.add(
          "UnsupportedCapabilityException exception should be thrown for level 0 providers \n");
      DeclarativeQueryManager dqm = rs.getDeclarativeQueryManager();
      if (capabilityLevel == 0)
        throw new Fault(testName
            + " failed - should have thrown  UnsupportedCapabilityException");

      if (!(dqm instanceof DeclarativeQueryManager))
        throw new Fault(testName
            + " RegistryService failed to return a DeclarativeQueryManager");

    } catch (UnsupportedCapabilityException uce) {
      debug.add("UnsupportedCapabilityException was thrown \n");
      if (!(capabilityLevel == 0))
        throw new Fault(testName + " failed ", uce);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " FAIled - unexpected exception ");
    }
  }

  /*
   * @testName: registryService_getBulkResponseInvalid
   *
   * @assertion_ids: JAXR:JAVADOC:66
   *
   * @test_Strategy: Pass an invalid requestid to getBulkResonse. Verify that an
   * InvalidRequestException is thrown
   * 
   * 
   */
  public void registryService_getBulkResponseInvalid() throws Fault {
    String testName = "registryService_getBulkResponseInvalid";
    String emptyString = "";
    try {
      debug.add("Pass an invalid requestid to getBulkResonse\n");
      debug.add("InvalidRequestException should be thrown \n");
      BulkResponse br = rs.getBulkResponse(emptyString);
      throw new Fault(
          testName + " failed - should have thrown  InvalidRequestException ");

    } catch (InvalidRequestException ir) {
      TestUtil.printStackTrace(ir);
      debug.add("InvalidRequestException was thrown as expected");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testName + " failed - unexpected exception ");
    }
  }

}
