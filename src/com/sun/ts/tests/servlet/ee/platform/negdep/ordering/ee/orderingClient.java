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

package com.sun.ts.tests.servlet.ee.platform.negdep.ordering.ee;

// Java imports
import java.io.*;
import java.util.*;

// Harness imports
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

// common test imports
import com.sun.ts.tests.common.negdep.NegdepUtil;

public class orderingClient extends ServiceEETest {

  protected NegdepUtil ndu;

  protected String baseAppDir;

  protected Properties testProps;

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    orderingClient theTests = new orderingClient();
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
        + "/src/com/sun/ts/tests/servlet/ee/platform/negdep/ordering/apps/";

    ndu = new NegdepUtil(testProps);
  }

  /* cleanup */
  public void cleanup() throws Fault {
    ndu.cleanup();
  }

  /*
   * @testName: orderingFilterTest
   *
   * @assertion_ids: JavaEE:SPEC:10265; Servlet:SPEC:175
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains a filter tag in which at least one subelement is out of
   * order, but which is otherwise valid, and verify that deployment fails.
   * Deploy the same war file in which the deployment descriptor has been
   * corrected.
   * 
   */
  public void orderingFilterTest() throws Fault {

    TestUtil.logMsg("Starting orderingFilterTest");
    boolean pass = false;
    String appDir = baseAppDir + "filter/";
    String badModuleName = appDir + "BadFilter.war";
    String goodModuleName = appDir + "GoodFilter.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_ordering_apps_badfilter_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_ordering_apps_goodfilter_web.war.sun-web.xml";

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
        throw new Fault("Failed: orderingFilterTest");
      else
        logMsg("Passed: orderingFilterTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: orderingFilterMappingTest
   *
   * @assertion_ids: JavaEE:SPEC:10265; Servlet:SPEC:175
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains a filter-mapping tag in which at least one subelement
   * is out of order, but which is otherwise valid, and verify that deployment
   * fails. Deploy the same war file in which the deployment descriptor has been
   * corrected.
   * 
   */
  public void orderingFilterMappingTest() throws Fault {

    TestUtil.logMsg("Starting orderingFilterMappingTest");
    boolean pass = false;
    String appDir = baseAppDir + "filter-mapping/";
    String badModuleName = appDir + "BadFilterMapping.war";
    String goodModuleName = appDir + "GoodFilterMapping.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_ordering_apps_badfilter-mapping_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_ordering_apps_goodfilter-mapping_web.war.sun-web.xml";

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
        throw new Fault("Failed: orderingFilterMappingTest");
      else
        logMsg("Passed: orderingFilterMappingTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: orderingSecurityConstraintTest
   *
   * @assertion_ids: JavaEE:SPEC:10265; Servlet:SPEC:175
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains a security-constraint tag in which at least one
   * subelement is out of order, but which is otherwise valid, and verify that
   * deployment fails. Deploy the same war file in which the deployment
   * descriptor has been corrected.
   * 
   */
  public void orderingSecurityConstraintTest() throws Fault {

    TestUtil.logMsg("Starting orderingSecurityConstraintTest");
    boolean pass = false;
    String appDir = baseAppDir + "security-constraint/";
    String badModuleName = appDir + "BadSecurityConstraint.war";
    String goodModuleName = appDir + "GoodSecurityConstraint.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_ordering_apps_badsecurity-constraint_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_ordering_apps_goodsecurity-constraint_web.war.sun-web.xml";

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
        throw new Fault("Failed: orderingSecurityConstraintTest");
      else
        logMsg("Passed: orderingSecurityConstraintTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: orderingServletTest
   *
   * @assertion_ids: JavaEE:SPEC:10265; Servlet:SPEC:175
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains a servlet tag in which at least one subelement is out
   * of order, but which is otherwise valid, and verify that deployment fails.
   * Deploy the same war file in which the deployment descriptor has been
   * corrected.
   * 
   */
  public void orderingServletTest() throws Fault {

    TestUtil.logMsg("Starting orderingServletTest");
    boolean pass = false;
    String appDir = baseAppDir + "servlet/";
    String badModuleName = appDir + "BadServlet.war";
    String goodModuleName = appDir + "GoodServlet.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_ordering_apps_badservlet_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_ordering_apps_goodservlet_web.war.sun-web.xml";

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
        throw new Fault("Failed: orderingServletTest");
      else
        logMsg("Passed: orderingServletTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }
}
