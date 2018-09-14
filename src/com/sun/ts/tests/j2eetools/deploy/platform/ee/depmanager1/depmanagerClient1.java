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

package com.sun.ts.tests.j2eetools.deploy.platform.ee.depmanager1;

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

public class depmanagerClient1 extends ServiceEETest {

  // Member instance variables
  DMProps dmp; // Properties object derived from ts.jte file

  DeployTestUtil dtu; // TestUtility driver to interact with DeploymentManager

  DeploymentManager dm; // The DeploymentManager we're interacting with

  Properties testProps; // Test properties passed via harness code.
  // Need a better name ...

  Util u;

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    depmanagerClient1 theTests = new depmanagerClient1();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: all.props; all properties;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
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

  /**
   * cleanup
   */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Releasing DeploymentManager...");
    dtu.releaseDeploymentManager();
    TestUtil.logMsg("DeploymentManager released");
  }

  /**
   * @testName: testDistEAR
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:14; J2EEDEPLOY:JAVADOC:16;
   *                 J2EEDEPLOY:JAVADOC:17; J2EEDEPLOY:JAVADOC:29;
   *                 J2EEDEPLOY:JAVADOC:96; J2EEDEPLOY:JAVADOC:97;
   *                 J2EEDEPLOY:JAVADOC:98; J2EEDEPLOY:JAVADOC:99;
   *                 J2EEDEPLOY:JAVADOC:100; J2EEDEPLOY:JAVADOC:108;
   *                 J2EEDEPLOY:JAVADOC:110; J2EEDEPLOY:JAVADOC:111;
   *                 J2EEDEPLOY:JAVADOC:30; J2EEDEPLOY:JAVADOC:32;
   *                 J2EEDeploy:SPEC:11;
   *
   * @test_Strategy: Distribute an EAR file. Verify the moduleID does not exist
   *                 before distribute, and exists after distribute.
   * 
   *
   */

  public void testDistEAR() throws Fault {

    File earFile = u.getEarFile();
    File earPlan = u.getEarPlan();

    TestUtil.logTrace("file: " + earFile.toString());
    TestUtil.logTrace("plan: " + earPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.EAR, earFile, earPlan,
          DeployTestUtil.FILE_ARCHIVE, false);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testDistWAR
   *
   * @assertion: Distribute a WAR
   * 
   * @test_Strategy: Distribute an WAR file. Verify the moduleID does not exist
   * before distribute, and exists after distribute.
   * 
   *
   */

  public void testDistWAR() throws Fault {

    TestUtil.logMsg("Starting testDistWAR");

    File warEar = u.getWarFile();
    File warPlan = u.getWarPlan();
    TestUtil.logMsg("file: " + warEar.toString());
    TestUtil.logMsg("plan: " + warPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.WAR, warEar, warPlan,
          DeployTestUtil.FILE_ARCHIVE, false);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  // ********************* NEW TESTS ***********************

  /*
   * @testName: testDistEJBJar
   *
   * @assertion: Distribute an EJBJar
   * 
   * @test_Strategy: Distribute an EJBJar file. Verify the moduleID does not
   * exist before distribute, and exists after distribute.
   * 
   * 
   *
   */

  public void testDistEJBJar() throws Fault {

    TestUtil.logMsg("Starting testDistEJBJar");

    File ejbFile = u.getEjbFile();
    File ejbPlan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbFile.toString());
    TestUtil.logMsg("plan: " + ejbPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.EJB, ejbFile, ejbPlan,
          DeployTestUtil.FILE_ARCHIVE, false);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testDistRAR
   *
   * @assertion: Distribute a RAR
   * 
   * @test_Strategy: Distribute a RAR file. Verify the moduleID does not exist
   * before distribute, and exists after distribute.
   * 
   *
   */

  public void testDistRAR() throws Fault {

    TestUtil.logMsg("Starting testDistRAR");

    File rarFile = u.getRarFile();
    File rarPlan = u.getRarPlan();
    TestUtil.logMsg("file: " + rarFile.toString());
    if (rarPlan == null)
      TestUtil.logMsg("rarPlan is null");
    else
      TestUtil.logMsg("plan: " + rarPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.RAR, rarFile, rarPlan,
          DeployTestUtil.FILE_ARCHIVE, false);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testDistEARFromStreamDeprecatedAPI
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:14; J2EEDEPLOY:JAVADOC:16;
   * J2EEDEPLOY:JAVADOC:17; J2EEDEPLOY:JAVADOC:31;
   * 
   * 
   * @test_Strategy: Distribute an EAR from a Stream using pre-1.5 distribute()
   * API. Verify the moduleID does not exist before distribute, and exists after
   * distribute.
   */

  public void testDistEARFromStreamDeprecatedAPI() throws Fault {

    TestUtil.logMsg("Starting testDistEARFromStreamDeprecatedAPI");

    File earFile = u.getEarFile();
    File earPlan = u.getEarPlan();
    TestUtil.logMsg("file: " + earFile.toString());
    TestUtil.logMsg("plan: " + earPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.EAR, earFile, earPlan,
          DeployTestUtil.STREAM_ARCHIVE, false);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testDistEARFromStreamOneDotFive
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:14; J2EEDEPLOY:JAVADOC:16;
   * J2EEDEPLOY:JAVADOC:17; J2EEDEPLOY:JAVADOC:31;
   * 
   * 
   * @test_Strategy: Distribute an EAR from a Stream using 1.5 distribute() API.
   * Verify the moduleID does not exist before distribute, and exists after
   * distribute.
   */

  public void testDistEARFromStreamOneDotFive() throws Fault {

    TestUtil.logMsg("Starting testDistEARFromStreamOneDotFive");

    File earFile = u.getEarFile();
    File earPlan = u.getEarPlan();
    TestUtil.logMsg("file: " + earFile.toString());
    TestUtil.logMsg("plan: " + earPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.EAR, earFile, earPlan,
          DeployTestUtil.STREAM_ARCHIVE, true);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testDistEJBJarFromStreamDeprecatedAPI
   *
   * @assertion: Distribute an EJBJar from a Stream
   * 
   * @test_Strategy: Distribute an Ejb jar from a Stream using pre-1.5
   * distribute() API. Verify the moduleID does not exist before distribute, and
   * exists after distribute.
   */

  public void testDistEJBJarFromStreamDeprecatedAPI() throws Fault {

    TestUtil.logMsg("Starting testDistEJBJarFromStreamDeprecatedAPI");

    File ejbFile = u.getEjbFile();
    File ejbPlan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbFile.toString());
    TestUtil.logMsg("plan: " + ejbPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.EJB, ejbFile, ejbPlan,
          DeployTestUtil.STREAM_ARCHIVE, false);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: testDistEJBJarFromStreamOneDotFive
   *
   * @assertion: Distribute an EJBJar from a Stream
   * 
   * @test_Strategy: Distribute an Ejb Jar from a Stream using 1.5 distribute()
   * API. Verify the moduleID does not exist before distribute, and exists after
   * distribute.
   */

  public void testDistEJBJarFromStreamOneDotFive() throws Fault {

    TestUtil.logMsg("Starting testDistEJBJarFromStreamOneDotFive");

    File ejbFile = u.getEjbFile();
    File ejbPlan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbFile.toString());
    TestUtil.logMsg("plan: " + ejbPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.EJB, ejbFile, ejbPlan,
          DeployTestUtil.STREAM_ARCHIVE, true);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: testDistWARFromStreamDeprecatedAPI
   *
   * @assertion: Distribute a WAR from a Stream
   * 
   * @test_Strategy: Distribute a WAR from a Stream using pre-1.5 distribute()
   * API. Verify the moduleID does not exist before distribute, and exists after
   * distribute.
   */

  public void testDistWARFromStreamDeprecatedAPI() throws Fault {

    TestUtil.logMsg("Starting testDistWARFromStreamDeprecatedAPI");

    File warFile = u.getWarFile();
    File warPlan = u.getWarPlan();
    TestUtil.logMsg("file: " + warFile.toString());
    TestUtil.logMsg("plan: " + warPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.WAR, warFile, warPlan,
          DeployTestUtil.STREAM_ARCHIVE, false);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testDistWARFromStreamOneDotFive
   *
   * @assertion: Distribute a WAR from a Stream
   * 
   * @test_Strategy: Distribute a WAR from a Stream using 1.5 distribute() API.
   * Verify the moduleID does not exist before distribute, and exists after
   * distribute.
   */

  public void testDistWARFromStreamOneDotFive() throws Fault {

    TestUtil.logMsg("Starting testDistWARFromStreamOneDotFive");

    File warFile = u.getWarFile();
    File warPlan = u.getWarPlan();
    TestUtil.logMsg("file: " + warFile.toString());
    TestUtil.logMsg("plan: " + warPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.WAR, warFile, warPlan,
          DeployTestUtil.STREAM_ARCHIVE, true);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: testDistRARFromStreamDeprecatedAPI
   *
   * @assertion: Distribute a RAR from a Stream
   * 
   * @test_Strategy: Distribute a RAR from a Stream using pre-1.5 distribute()
   * API. Verify the moduleID does not exist before distribute, and exists after
   * distribute.
   */

  public void testDistRARFromStreamDeprecatedAPI() throws Fault {

    TestUtil.logMsg("Starting testDistRARFromStreamDeprecatedAPI");

    File rarFile = u.getRarFile();
    File rarPlan = u.getRarPlan();
    TestUtil.logMsg("file: " + rarFile.toString());
    if (rarPlan == null)
      TestUtil.logMsg("plan is null");
    else
      TestUtil.logMsg("plan: " + rarPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.RAR, rarFile, rarPlan,
          DeployTestUtil.STREAM_ARCHIVE, false);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testDistRARFromStreamOneDotFive
   *
   * @assertion: Distribute a RAR from a Stream
   * 
   * @test_Strategy: Distribute a RAR from a Stream using 1.5 distribute() API.
   * Verify the moduleID does not exist before distribute, and exists after
   * distribute.
   */

  public void testDistRARFromStreamOneDotFive() throws Fault {

    TestUtil.logMsg("Starting testDistRARFromStreamOneDotFive");

    File rarFile = u.getRarFile();
    File rarPlan = u.getRarPlan();
    TestUtil.logMsg("file: " + rarFile.toString());
    if (rarPlan == null)
      TestUtil.logMsg("plan is null");
    else
      TestUtil.logMsg("plan: " + rarPlan.toString());

    try {
      boolean pass = dtu.testDistributeModule(ModuleType.RAR, rarFile, rarPlan,
          DeployTestUtil.STREAM_ARCHIVE, true);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: testStartEAR
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:33; J2EEDEPLOY:JAVADOC:34;
   * 
   * @test_Strategy: Call start on an EAR module. Check that the started module
   * is the same as the deployed module.
   *
   *
   */

  public void testStartEAR() throws Fault {

    TestUtil.logMsg("Starting testStartEAR");

    File earFile = u.getEarFile();
    File earPlan = u.getEarPlan();

    TestUtil.logTrace("file: " + earFile.toString());
    TestUtil.logTrace("plan: " + earPlan.toString());

    try {
      boolean pass = dtu.testStartModule(ModuleType.EAR, earFile, earPlan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testStartEJBJar
   *
   * @assertion: Start an EJBJar.
   * 
   * @test_Strategy: Call start on an EJBJar module. Check that the started
   * module is the same as the deployed module.
   *
   *
   */

  public void testStartEJBJar() throws Fault {

    TestUtil.logMsg("Starting testStartEJBJar");

    File ejbFile = u.getEjbFile();
    File ejbPlan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbFile.toString());
    TestUtil.logMsg("plan: " + ejbPlan.toString());

    try {
      boolean pass = dtu.testStartModule(ModuleType.EJB, ejbFile, ejbPlan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testStartWAR
   *
   * @assertion: Start a WAR.
   * 
   * @test_Strategy: Call start on a WAR module. Check that the started module
   * is the same as the deployed module.
   *
   *
   */

  public void testStartWAR() throws Fault {

    TestUtil.logMsg("Starting testStartWAR");

    File warEar = u.getWarFile();
    File warPlan = u.getWarPlan();
    TestUtil.logMsg("file: " + warEar.toString());
    TestUtil.logMsg("plan: " + warPlan.toString());

    try {
      boolean pass = dtu.testStartModule(ModuleType.WAR, warEar, warPlan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testStopEAR
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:35; J2EEDEPLOY:JAVADOC:36
   * 
   * @test_Strategy: Call stop on an EAR module. Check that moduleID is not in
   * list of running modules, and is in list of non running modules.
   *
   */

  public void testStopEAR() throws Fault {

    TestUtil.logMsg("Starting testStopEAR");

    File earFile = u.getEarFile();
    File earPlan = u.getEarPlan();

    TestUtil.logTrace("file: " + earFile.toString());
    TestUtil.logTrace("plan: " + earPlan.toString());

    try {
      boolean pass = dtu.testStopModule(ModuleType.EAR, earFile, earPlan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testStopEJBJar
   *
   * @assertion: Stop an EJBJar
   * 
   * @test_Strategy: Call stop on an EJBJar module. Check that moduleID is not
   * in list of running modules, and is in list of non running modules.
   *
   *
   */

  public void testStopEJBJar() throws Fault {

    TestUtil.logMsg("Starting testStopEJBJar");

    File ejbFile = u.getEjbFile();
    File ejbPlan = u.getEjbPlan();
    TestUtil.logMsg("file: " + ejbFile.toString());
    TestUtil.logMsg("plan: " + ejbPlan.toString());

    try {
      boolean pass = dtu.testStopModule(ModuleType.EJB, ejbFile, ejbPlan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

  /*
   * @testName: testStopWAR
   *
   * @assertion: Stop a WAR
   * 
   * @test_Strategy: Call stop on a WAR module. Check that moduleID is not in
   * list of running modules, and is in list of non running modules.
   *
   *
   */

  public void testStopWAR() throws Fault {

    TestUtil.logMsg("Starting testStopWAR");

    File warFile = u.getWarFile();
    File warPlan = u.getWarPlan();
    TestUtil.logMsg("file: " + warFile.toString());
    TestUtil.logMsg("plan: " + warPlan.toString());

    try {
      boolean pass = dtu.testStopModule(ModuleType.WAR, warFile, warPlan,
          DeployTestUtil.FILE_ARCHIVE);
      if (!pass)
        throw new Fault("Failed: Test method returned false.");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }

  }

}
