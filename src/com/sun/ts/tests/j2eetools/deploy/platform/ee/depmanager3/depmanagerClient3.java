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

package com.sun.ts.tests.j2eetools.deploy.platform.ee.depmanager3;

// Java imports
import java.io.*;
import java.util.*;
import java.lang.System;

// Harness imports
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;

// RMI imports
import java.rmi.RemoteException;

// EJB imports
import javax.ejb.*;

// J2EE Deployment API's
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.shared.factories.DeploymentFactoryManager;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;
import javax.enterprise.deploy.shared.ModuleType;

// common test imports
import com.sun.ts.lib.deliverable.cts.deploy.*;
import com.sun.ts.lib.porting.DeploymentInfo.*;
import com.sun.ts.lib.deliverable.*;
import com.sun.ts.tests.j2eetools.deploy.common.Util;

public class depmanagerClient3 extends ServiceEETest {

  // Member instance variables
  DMProps dmp; // Properties object derived from ts.jte file

  DeployTestUtil dtu; // TestUtility driver to interact with DeploymentManager

  DeploymentManager dm; // The DeploymentManager we're interacting with

  Properties testProps; // Test properties passed via harness code. Contents of
                        // ts.jte file

  Util u;

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    depmanagerClient3 theTests = new depmanagerClient3();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: all.props; all properties;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("Setup");
    this.testProps = p;
    testProps.setProperty("ts.home", testProps.getProperty("ts_home"));

    try {

      // Construct properties object for DM
      dmp = new DMProps(p.getProperty("deployManagerJarFile.1"),
          p.getProperty("deployManageruri.1"),
          p.getProperty("deployManageruname.1"),
          p.getProperty("deployManagerpasswd.1"));

      // Utility object for getting dm's
      dtu = new DeployTestUtil(dmp);
      u = new Util(testProps);

      // Construct the DM
      try {
        dm = dtu.getDeploymentManager();
      } catch (DeploymentManagerCreationException dmce) {
        TestUtil.logErr("Caught DMCException!!!", dmce);
        throw new Fault("Unable to get DeploymentManager: " + dmce.getMessage(),
            dmce);
      }

    } catch (Exception e) {
      TestUtil.logErr(
          "Exception loading DeploymentFactoryManager factories: " + e, e);
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Releasing DeploymentManager...");
    if (dtu != null)
      dtu.releaseDeploymentManager();
    TestUtil.logMsg("DeploymentManager released");
  }

  /*
   * @testName: testDefaultLocale
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:47; J2EEDEPLOY:JAVADOC:51;
   * J2EEDEPLOY:JAVADOC:52; J2EEDEPLOY:SPEC:12
   * 
   * @test_Strategy: Verify that the default locale exists, that it is included
   * in the list of supported locales, and that the Deployment Manager shows
   * that it is supported.
   */

  public void testDefaultLocale() throws Fault {

    TestUtil.logMsg("Starting testDefaultLocale");

    Locale[] locales = dm.getSupportedLocales();
    Locale defaultLocale = dm.getDefaultLocale();
    boolean localePresent = false;
    boolean supported = false;

    for (int i = 0; i < locales.length; i++) {
      TestUtil.logMsg("Supported locale: " + locales[i].getDisplayName());
      if (locales[i].equals(defaultLocale)) {
        TestUtil.logMsg("Default locale present in supported locale list");
        localePresent = true;
      }
    }

    if (dm.isLocaleSupported(defaultLocale)) {
      TestUtil.logMsg("isSupported returns true for default locale");
      supported = true;
    }

    if (localePresent && supported) {
      TestUtil.logMsg("Locale is default, and isSupported");
    } else {
      throw new Fault("Locale information is incorrect");
    }

  }

  /*
   * @testName: testCurrentLocale
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:48
   * 
   * @test_Strategy: Verify that the current locale exists, that it is included
   * in the list of supported locales, and that the Deployment Manager shows
   * that it is supported.
   */

  public void testCurrentLocale() throws Fault {

    TestUtil.logMsg("Starting testCurrentLocale");

    Locale[] locales = dm.getSupportedLocales();
    Locale currentLocale = dm.getCurrentLocale();
    boolean localePresent = false;
    boolean supported = false;

    for (int i = 0; i < locales.length; i++) {
      TestUtil.logMsg("Supported locale: " + locales[i].getDisplayName());
      if (locales[i].equals(currentLocale)) {
        TestUtil.logMsg("Current locale present in supported locale list");
        localePresent = true;
      }
    }

    if (dm.isLocaleSupported(currentLocale)) {
      TestUtil.logMsg("isSupported returns true for current locale");
      supported = true;
    }

    if (localePresent && supported) {
      TestUtil.logMsg("Locale is current, and isSupported");
    } else {
      throw new Fault("Locale information is incorrect");
    }

  }

  /*
   * @testName: testRelease
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:17; J2EEDEPLOY:JAVADOC:46
   * 
   * @test_Strategy: Release the Deployment Manager, then verify that the
   * Deployment Manager is no longer available by calling its getTargets()
   * method and catching an IllegalStateException.
   */

  public void testRelease() throws Fault {

    TestUtil.logMsg("Starting testRelease");

    DeploymentManager dm;
    boolean b = false;

    try {
      dm = dtu.getDeploymentManager();
      dm.release();
      try {
        dm.getTargets();
      } catch (IllegalStateException ie) {
        TestUtil.printStackTrace(ie);
        b = true;
      }

    } catch (DeploymentManagerCreationException dmce) {
      TestUtil.printStackTrace(dmce);
      throw new Fault("Unable to get DeploymentManager: " + dmce.getMessage());
    }

    dtu = null;

    if (!b) {
      throw new Fault("Release did not release");
    }

  }
}
