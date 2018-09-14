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

package com.sun.ts.tests.j2eetools.deploy.platform.ee.tmoduleid;

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
import com.sun.ts.lib.deliverable.cts.deploy.DeployTestUtil;

// RMI imports
import java.rmi.RemoteException;

// EJB imports
import javax.ejb.*;

// J2EE Deployment API's
//import javax.enterprise.deploy.spi.*;
import javax.enterprise.deploy.spi.DeploymentManager;
import javax.enterprise.deploy.spi.exceptions.DeploymentManagerCreationException;
import javax.enterprise.deploy.shared.factories.DeploymentFactoryManager;
import javax.enterprise.deploy.spi.factories.DeploymentFactory;

// common test imports
import com.sun.ts.lib.deliverable.cts.deploy.*;
import com.sun.ts.lib.porting.DeploymentInfo.*;
import com.sun.ts.lib.deliverable.*;
import com.sun.ts.tests.j2eetools.deploy.common.Util;

public class tmoduleidClient extends ServiceEETest {

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
    tmoduleidClient theTests = new tmoduleidClient();
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
    dtu.releaseDeploymentManager();
    TestUtil.logMsg("DeploymentManager released");
  }

  /*
   * @testName: testEARModuleId
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:8; J2EEDEPLOY:JAVADOC:11;
   * J2EEDEPLOY:JAVADOC:12
   * 
   * @test_Strategy: Verify that the target identified by the target module ID
   * of a deployed application is identical to the target prior to deployment.
   */

  public void testEARModuleId() throws Fault {

    TestUtil.logMsg("Starting testEARModuleId");

    File earfile = u.getEarFile();
    File plan = u.getEarPlan();
    TestUtil.logMsg("file: " + earfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      boolean pass = dtu.testTargetModuleID(earfile, plan,
          DeployTestUtil.APPLICATION_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testStandaloneModuleId
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:8; J2EEDEPLOY:JAVADOC:11;
   * J2EEDEPLOY:JAVADOC:12
   *
   * @test_Strategy: Verify that the target identified by the target module ID
   * of a deployed standalone jar is identical to the target prior to
   * deployment.
   */

  public void testStandaloneModuleId() throws Fault {

    TestUtil.logMsg("Starting testStandaloneModuleId");

    File ejbfile = u.getEjbFile();
    File plan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      boolean pass = dtu.testTargetModuleID(ejbfile, plan,
          DeployTestUtil.STAND_ALONE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }
}
