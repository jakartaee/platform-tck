/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaspic.spi.baseline;

import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.Status;
import com.sun.ts.tests.jaspic.spi.common.CommonTests;
import java.util.Properties;
import java.io.Serializable;

/**
 * These tests are to be run for the case of no Profile - where there is NO
 * Servlet and NO SOAP Profile support. When there is no profile, we call that
 * the baseline requirements. Executing these tests assumes the implementation
 * of Custom Vehicle Porting Package. By default, the jaspicservlet is used
 * (where jaspicserver requires a web server).
 *
 * Again, these tests only need to be run when there is no profile being tested
 * (ie No SOAP and No Servlet Profile being tested)
 *
 * @author Oracle
 */
public class Client extends ServiceEETest {

  private Properties props = null;

  private String logFileLocation;

  private String providerConfigFilePath;

  private String vendorACFClass;

  private transient CommonTests commonTests;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: log.file.location; provider.configuration.file;
   * vendor.authconfig.factory; webServerHost; webServerPort; user; password;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    logFileLocation = props.getProperty("log.file.location");
    TestUtil.logMsg("Log file location = " + logFileLocation);

    providerConfigFilePath = props.getProperty("provider.configuration.file");
    TestUtil
        .logMsg("TestSuite Provider ConfigFile = " + providerConfigFilePath);

    vendorACFClass = props.getProperty("vendor.authconfig.factory");
    TestUtil.logMsg("Vendor AuthConfigFactory class = " + vendorACFClass);

    commonTests = new CommonTests();

    TestUtil.logMsg("setup ok");
  }

  /**
   * @testName: ACF_getFactory
   *
   * @assertion_ids: JASPIC:SPEC:329
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. 1st use SetFactory, then test GetFactory to make sure it
   *                 gets set.
   *
   *
   *                 Description Test getFactory() that if a non-null
   *                 system-wide factory instance is defined at the time of the
   *                 call, for example, with setFactory, it will be returned.
   *
   */
  public void ACF_getFactory() throws Fault {
    try {
      commonTests._ACF_getFactory();
      TestUtil.logMsg("ACF_getFactory : PASSED");
    } catch (Exception e) {
      throw new Fault("ACF_getFactory : FAILED");
    }
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: ACFSwitchFactorys
   *
   * @assertion_ids:
   *
   *
   * @test_Strategy: This test does the following: - gets current (CTS) factory
   *                 - sets the vendors ACF thus replacing the CTS ACF - verify
   *                 ACF's were correctly set - reset factory back to the
   *                 original CTS factory
   *
   *                 1. Use the static setFactory method to set an ACF and this
   *                 should always work. and use the getFactory to verify it
   *                 worked.
   *
   *                 Description
   *
   */
  public void ACFSwitchFactorys() throws Fault {
    try {
      commonTests._ACFSwitchFactorys(vendorACFClass);
      TestUtil.logMsg("ACFSwitchFactorys : PASSED");
    } catch (Exception e) {
      throw new Fault("ACFSwitchFactorys : FAILED");
    }
  }

  /**
   * @testName: testACFComesFromSecFile
   *
   * @assertion_ids: JASPIC:SPEC:330
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. use getFactory and very the name of the JASPIC ACF class
   *                 and if it matches our known ACF class, that means the
   *                 runtime must have picked it up from the security property.
   *
   *                 Description test getFactory() that the fully qualified
   *                 class name of the default factory impl class is obtained
   *                 from the value of the authconfigprovider.factory security
   *                 property.
   *
   */
  public void testACFComesFromSecFile() throws Fault {

    try {
      commonTests._testACFComesFromSecFile();
      TestUtil.logMsg("testACFComesFromSecFile : PASSED");
    } catch (Exception e) {
      throw new Fault("testACFComesFromSecFile : FAILED");
    }
  }

  /**
   * @testName: ACFPersistentRegisterOnlyOneACP
   *
   * @assertion_ids: JASPIC:SPEC:331; JASPIC:SPEC:332;
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. test the vendors ACF to verify that we can register our
   *                 providers within their ACF AND that only one registration
   *                 may exist within their ACF for a given msg layer and
   *                 appContext combnation.
   *
   *                 Description registerConfigProvider(class, props, layer,
   *                 appContext, description): At most one registration may
   *                 exist within the factory for a given combination of message
   *                 layer and appContext. Any pre-existing registration with
   *                 identical values for layer and appContext is replaced by a
   *                 subsequent registration. (this is for persisitent
   *                 registration)
   */
  public void ACFPersistentRegisterOnlyOneACP() throws Fault {

    try {
      commonTests._ACFRegisterOnlyOneACP(logFileLocation,
          providerConfigFilePath, vendorACFClass, true);
      TestUtil.logMsg("ACFPersistentRegisterOnlyOneACP : PASSED");
    } catch (Exception e) {
      throw new Fault("ACFPersistentRegisterOnlyOneACP : FAILED");
    }
  }

  /**
   * @testName: ACFInMemoryRegisterOnlyOneACP
   *
   * @assertion_ids: JASPIC:SPEC:331; JASPIC:SPEC:332;
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. test the vendors ACF to verify that we can register our
   *                 providers within their ACF AND that only one registration
   *                 may exist within their ACF for a given msg layer and
   *                 appContext combnation.
   *
   *                 Description registerConfigProvider(ACP, layer, appContext,
   *                 description): At most one registration may exist within the
   *                 factory for a given combination of message layer and
   *                 appContext. Any pre-existing registration with identical
   *                 values for layer and appContext is replaced by a subsequent
   *                 registration. (this is for in-memory registration)
   */
  public void ACFInMemoryRegisterOnlyOneACP() throws Fault {

    try {
      commonTests._ACFRegisterOnlyOneACP(logFileLocation,
          providerConfigFilePath, vendorACFClass, false);
      TestUtil.logMsg("ACFInMemoryRegisterOnlyOneACP : PASSED");
    } catch (Exception e) {
      throw new Fault("ACFInMemoryRegisterOnlyOneACP : FAILED");
    }
  }

  /**
   * @testName: ACFUnregisterACP
   *
   * @assertion_ids: JASPIC:SPEC:334; JASPIC:SPEC:335; JASPIC:SPEC:344;
   *
   * @test_Strategy: 1. Get System properties log.file.location,
   *                 provider.configuration.file and vendor.authconfig.factory
   *
   *                 2. test the vendors ACF to verify that we can register our
   *                 providers within their ACF AND then unregister it.
   *
   *                 Description registerConfigProvider(ACP, layer, appContext,
   *                 description): At most one registration may exist within the
   *                 factory for a given combination of message layer and
   *                 appContext. After registering a non-persistent ACP, we want
   *                 to validate the unregistering of that ACP.
   *
   */
  public void ACFUnregisterACP() throws Fault {

    try {
      commonTests._ACFUnregisterACP(logFileLocation, providerConfigFilePath,
          vendorACFClass);
      TestUtil.logMsg("ACFUnregisterACP : PASSED");
    } catch (Exception e) {
      throw new Fault("ACFUnregisterACP : FAILED");
    }
  }

  /**
   *
   * @keywords: jaspic_servlet
   *
   * @testName: ACFRemoveRegistrationWithBadId
   *
   * @assertion_ids: JASPIC:SPEC:345;
   *
   *
   * @test_Strategy: This test verifies we get a return value of False when
   *                 invoking ACF.removeRegistration(some_bad_id);
   *
   *                 1. Use the static setFactory method to get an ACF and then
   *                 attempt to invoke removeRegistration() with an invalid (ie
   *                 non-existant) regId. This should return False (per
   *                 javadoc).
   *
   *                 Description
   *
   */
  public void ACFRemoveRegistrationWithBadId() throws Fault {

    try {
      commonTests._ACFRemoveRegistrationWithBadId();
      TestUtil.logMsg("ACFRemoveRegistrationWithBadId : PASSED");
    } catch (Exception e) {
      throw new Fault("ACFRemoveRegistrationWithBadId : FAILED");
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

}
