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

package com.sun.ts.tests.servlet.ee.platform.negdep.multi.ee;

// Java imports
import java.io.*;
import java.util.*;

// Harness imports
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

// common test imports
import com.sun.ts.tests.common.negdep.NegdepUtil;

public class multiClient extends ServiceEETest {

  protected NegdepUtil ndu;

  protected String baseAppDir;

  protected Properties testProps;

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    multiClient theTests = new multiClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: all.props; all properties;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("Setup");
    testProps = p;
    baseAppDir = testProps.getProperty("ts_home")
        + "/src/com/sun/ts/tests/servlet/ee/platform/negdep/multi/apps/";

    ndu = new NegdepUtil(testProps);
  }

  /* cleanup */
  public void cleanup() throws Fault {
    ndu.cleanup();
  }

  /*
   * @testName: multiSessionConfigTest
   *
   * @assertion_ids: Servlet:SPEC:11276.2
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains more than one session-config tag but which is otherwise
   * valid, and verify that deployment fails. Deploy the same war file in which
   * the deployment descriptor has been corrected.
   * 
   */
  public void multiSessionConfigTest() throws Fault {

    TestUtil.logMsg("Starting multiSessionConfigTest");
    boolean pass = false;
    String appDir = baseAppDir + "session-config/";
    String badModuleName = appDir + "BadSessionConfig.war";
    String goodModuleName = appDir + "GoodSessionConfig.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_multi_apps_badsession-config_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_multi_apps_goodsession-config_web.war.sun-web.xml";

    try {
      FileInputStream badModuleStream = new FileInputStream(
          new File(badModuleName));
      FileInputStream goodModuleStream = new FileInputStream(
          new File(goodModuleName));
      InputStream badPlanStream = ndu.getDeploymentPlan(badModuleName,
          new String[] { badPlanName });
      InputStream goodPlanStream = ndu.getDeploymentPlan(goodModuleName,
          new String[] { goodPlanName });
      if (goodPlanStream == null || badPlanStream == null)
        throw new Fault("Failed: Null deployment plan received");
      pass = ndu.negativeTestDistributeModule(badModuleStream, badPlanStream);
      logTrace("pass after bad module is " + pass);
      if (pass) {
        pass = ndu.positiveTestDistributeModule(goodModuleStream,
            goodPlanStream);
        logTrace("pass after good module is " + pass);
      }
      if (!pass)
        throw new Fault("Failed: multiSessionConfigTest");
      else
        logMsg("Passed: multiSessionConfigTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: multiLoginConfigTest
   *
   * @assertion_ids: Servlet:SPEC:11276.2
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains more than one login-config tag but which is otherwise
   * valid, and verify that deployment fails. Deploy the same war file in which
   * the deployment descriptor has been corrected.
   * 
   */
  public void multiLoginConfigTest() throws Fault {

    TestUtil.logMsg("Starting multiLoginConfigTest");
    boolean pass = false;
    String appDir = baseAppDir + "login-config/";
    String badModuleName = appDir + "BadLoginConfig.war";
    String goodModuleName = appDir + "GoodLoginConfig.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_multi_apps_badlogin-config_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_multi_apps_goodlogin-config_web.war.sun-web.xml";

    try {
      FileInputStream badModuleStream = new FileInputStream(
          new File(badModuleName));
      FileInputStream goodModuleStream = new FileInputStream(
          new File(goodModuleName));
      InputStream badPlanStream = ndu.getDeploymentPlan(badModuleName,
          new String[] { badPlanName });
      InputStream goodPlanStream = ndu.getDeploymentPlan(goodModuleName,
          new String[] { goodPlanName });
      if (goodPlanStream == null || badPlanStream == null)
        throw new Fault("Failed: Null deployment plan received");
      pass = ndu.negativeTestDistributeModule(badModuleStream, badPlanStream);
      logTrace("pass after bad module is " + pass);
      if (pass) {
        pass = ndu.positiveTestDistributeModule(goodModuleStream,
            goodPlanStream);
        logTrace("pass after good module is " + pass);
      }
      if (!pass)
        throw new Fault("Failed: multiLoginConfigTest");
      else
        logMsg("Passed: multiLoginConfigTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: multiJspConfigTest
   *
   * @assertion_ids: Servlet:SPEC:11276.2
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains more than one jsp-config tag but which is otherwise
   * valid, and verify that deployment fails. Deploy the same war file in which
   * the deployment descriptor has been corrected.
   * 
   */
  public void multiJspConfigTest() throws Fault {

    TestUtil.logMsg("Starting multiJspConfigTest");
    boolean pass = false;
    String appDir = baseAppDir + "jsp-config/";
    String badModuleName = appDir + "BadJspConfig.war";
    String goodModuleName = appDir + "GoodJspConfig.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_multi_apps_badjsp-config_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_multi_apps_goodjsp-config_web.war.sun-web.xml";

    try {
      FileInputStream badModuleStream = new FileInputStream(
          new File(badModuleName));
      FileInputStream goodModuleStream = new FileInputStream(
          new File(goodModuleName));
      InputStream badPlanStream = ndu.getDeploymentPlan(badModuleName,
          new String[] { badPlanName });
      InputStream goodPlanStream = ndu.getDeploymentPlan(goodModuleName,
          new String[] { goodPlanName });
      if (goodPlanStream == null || badPlanStream == null)
        throw new Fault("Failed: Null deployment plan received");
      pass = ndu.negativeTestDistributeModule(badModuleStream, badPlanStream);
      logTrace("pass after bad module is " + pass);
      if (pass) {
        pass = ndu.positiveTestDistributeModule(goodModuleStream,
            goodPlanStream);
        logTrace("pass after good module is " + pass);
      }
      if (!pass)
        throw new Fault("Failed: multiJspConfigTest");
      else
        logMsg("Passed: multiJspConfigTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }
}
