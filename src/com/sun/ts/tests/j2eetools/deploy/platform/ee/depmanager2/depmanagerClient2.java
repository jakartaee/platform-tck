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

package com.sun.ts.tests.j2eetools.deploy.platform.ee.depmanager2;

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
//import javax.enterprise.deploy.spi.*;
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

public class depmanagerClient2 extends ServiceEETest {

  // Member instance variables
  DMProps dmp; // Properties object derived from ts.jte file

  DeployTestUtil dtu; // TestUtility driver to interact with DeploymentManager

  DeploymentManager dm; // The DeploymentManager we're interacting with

  Properties testProps; // Test properties passed via harness code. Contents of
                        // ts.jte file
  // Need a better name ...

  Util u;

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    depmanagerClient2 theTests = new depmanagerClient2();
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
   * @testName: testRedeployEAR
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:39;
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testRedeployEAR() throws Fault {

    TestUtil.logMsg("Starting testRedeployEAR");

    File earfile = u.getEarFile();
    File plan = u.getEarPlan();
    TestUtil.logMsg("file: " + earfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      if (dm.isRedeploySupported()) {
        boolean pass = dtu.testRedeployModule(ModuleType.EAR, earfile, plan,
            DeployTestUtil.FILE_ARCHIVE);
        if (!pass)
          throw new Fault("Failed: Test method returned false.");
      } else {
        TestUtil.logMsg("Redeploy is not supported.  Not running test.");
      }
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testRedeployEJBJar
   *
   * @assertion: Obtain a DeploymenManager object
   * 
   * @test_Strategy: Call DeploymentManager.distribute. Verify:
   * 
   *
   */

  public void testRedeployEJBJar() throws Fault {

    TestUtil.logMsg("Starting testRedeployEJBJar");

    File ejbfile = u.getEjbFile();
    File plan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      if (dm.isRedeploySupported()) {
        boolean pass = dtu.testRedeployModule(ModuleType.EJB, ejbfile, plan,
            DeployTestUtil.FILE_ARCHIVE);
        if (!pass)
          throw new Fault("Failed: Test method returned false.");
      } else {
        TestUtil.logMsg("Redeploy is not supported.  Not running test.");
      }
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testRedeployWAR
   *
   * @assertion: Obtain a DeploymenManager object
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testRedeployWAR() throws Fault {

    TestUtil.logMsg("Starting testRedeployWAR");

    File warfile = u.getWarFile();
    File plan = u.getWarPlan();
    TestUtil.logMsg("file: " + warfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      if (dm.isRedeploySupported()) {
        boolean pass = dtu.testRedeployModule(ModuleType.WAR, warfile, plan,
            DeployTestUtil.FILE_ARCHIVE);
        if (!pass)
          throw new Fault("Failed: Test method returned false.");
      } else {
        TestUtil.logMsg("Redeploy is not supported.  Not running test.");
      }
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testRedeployEARFromStream
   *
   * @assertion: Obtain a DeploymenManager object
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testRedeployEARFromStream() throws Fault {

    TestUtil.logMsg("Starting testRedeployEARFromStream");

    File earfile = u.getEarFile();
    File plan = u.getEarPlan();
    TestUtil.logMsg("file: " + earfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      if (dm.isRedeploySupported()) {
        boolean pass = dtu.testRedeployModule(ModuleType.EAR, earfile, plan,
            DeployTestUtil.STREAM_ARCHIVE);
        if (!pass)
          throw new Fault("Failed: Test method returned false.");
      } else {
        TestUtil.logMsg("Redeploy is not supported.  Not running test.");
      }
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testRedeployEJBJarFromStream
   *
   * @assertion: Obtain a DeploymenManager object
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testRedeployEJBJarFromStream() throws Fault {

    TestUtil.logMsg("Starting testRedeployEJBJarFromStream");

    File ejbfile = u.getEjbFile();
    File plan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      if (dm.isRedeploySupported()) {
        boolean pass = dtu.testRedeployModule(ModuleType.EJB, ejbfile, plan,
            DeployTestUtil.STREAM_ARCHIVE);
        if (!pass)
          throw new Fault("Failed: Test method returned false.");
      } else {
        TestUtil.logMsg("Redeploy is not supported.  Not running test.");
      }
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testRedeployWARFromStream
   *
   * @assertion: Obtain a DeploymenManager object
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testRedeployWARFromStream() throws Fault {

    TestUtil.logMsg("Starting testRedeployWARFromStream");

    File warfile = u.getWarFile();
    File plan = u.getWarPlan();
    TestUtil.logMsg("file: " + warfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      if (dm.isRedeploySupported()) {
        boolean pass = dtu.testRedeployModule(ModuleType.WAR, warfile, plan,
            DeployTestUtil.STREAM_ARCHIVE);
        if (!pass)
          throw new Fault("Failed: Test method returned false.");
      } else {
        TestUtil.logMsg("Redeploy is not supported.  Not running test.");
      }
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testUndeployEAR
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:37;
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testUndeployEAR() throws Fault {

    TestUtil.logMsg("Starting testUndeployEAR");

    File earfile = u.getEarFile();
    File plan = u.getEarPlan();
    TestUtil.logMsg("file: " + earfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      boolean pass = dtu.testUndeployModule(ModuleType.EAR, earfile, plan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testUndeployEJBJar
   *
   * @assertion: Obtain a DeploymenManager object
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testUndeployEJBJar() throws Fault {

    TestUtil.logMsg("Starting testUndeployEJBJar");

    File ejbfile = u.getEjbFile();
    File plan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      boolean pass = dtu.testUndeployModule(ModuleType.EJB, ejbfile, plan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testUndeployWAR
   *
   * @assertion: Obtain a DeploymenManager object
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testUndeployWAR() throws Fault {

    TestUtil.logMsg("Starting testUndeployWAR");

    File warfile = u.getWarFile();
    File plan = u.getWarPlan();
    TestUtil.logMsg("file: " + warfile.toString());
    TestUtil.logMsg("plan: " + plan.toString());

    try {
      boolean pass = dtu.testUndeployModule(ModuleType.WAR, warfile, plan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testUndeployRAR
   *
   * @assertion: Obtain a DeploymenManager object
   * 
   * @test_Strategy: get a DM
   *
   */

  public void testUndeployRAR() throws Fault {

    TestUtil.logMsg("Starting testUndeployRAR");

    File rarfile = u.getRarFile();
    File plan = u.getRarPlan();
    TestUtil.logMsg("file: " + rarfile.toString());
    if (plan == null)
      TestUtil.logMsg("plan is null");
    else
      TestUtil.logMsg("plan: " + plan.toString());

    try {
      boolean pass = dtu.testUndeployModule(ModuleType.RAR, rarfile, plan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

}
