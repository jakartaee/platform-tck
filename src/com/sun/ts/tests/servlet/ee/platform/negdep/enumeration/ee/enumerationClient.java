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

package com.sun.ts.tests.servlet.ee.platform.negdep.enumeration.ee;

// Java imports
import java.io.*;
import java.util.*;

// Harness imports
import com.sun.javatest.Status;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

// common test imports
import com.sun.ts.tests.common.negdep.NegdepUtil;

public class enumerationClient extends ServiceEETest {

  protected NegdepUtil ndu;

  protected String baseAppDir;

  protected Properties testProps;

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    enumerationClient theTests = new enumerationClient();
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
        + "/src/com/sun/ts/tests/servlet/ee/platform/negdep/enumeration/apps/";

    ndu = new NegdepUtil(testProps);
  }

  /* cleanup */
  public void cleanup() throws Fault {
    ndu.cleanup();
  }

  /*
   * @testName: enumerationDispatcherTest
   *
   * @assertion_ids: JavaEE:SPEC:10265; Servlet:SPEC:175; Servlet:SPEC:179
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains a dispatcher tag in which the enumeration value
   * contains lower-case characters, but which is otherwise valid, and verify
   * that deployment fails. Deploy the same war file in which the deployment
   * descriptor has been corrected.
   * 
   */
  public void enumerationDispatcherTest() throws Fault {

    TestUtil.logMsg("Starting enumerationDispatcherTest");
    boolean pass = false;
    String appDir = baseAppDir + "dispatcher/";
    String badModuleName = appDir + "BadDispatcher.war";
    String goodModuleName = appDir + "GoodDispatcher.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_enumeration_apps_baddispatcher_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_enumeration_apps_gooddispatcher_web.war.sun-web.xml";

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
        throw new Fault("Failed: enumerationDispatcherTest");
      else
        logMsg("Passed: enumerationDispatcherTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: enumerationHttpMethodTest
   *
   * @assertion_ids: JavaEE:SPEC:10265; Servlet:SPEC:175; PENDING (Servlet 2.5
   * MR 6, issue 32)
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains a http-method tag in which the enumeration value does
   * not match the pattern value of the http-methodType as specified in Servlet
   * 2.5 MR 6, but which is otherwise valid, and verify that deployment fails.
   * Deploy the same war file in which the deployment descriptor has been
   * corrected.
   * 
   */
  public void enumerationHttpMethodTest() throws Fault {

    TestUtil.logMsg("Starting enumerationHttpMethodTest");
    boolean pass = false;
    String appDir = baseAppDir + "http-method/";
    String badModuleName = appDir + "BadHttpMethod.war";
    String goodModuleName = appDir + "GoodHttpMethod.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_enumeration_apps_badhttp-method_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_enumeration_apps_goodhttp-method_web.war.sun-web.xml";

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
        throw new Fault("Failed: enumerationHttpMethodTest");
      else
        logMsg("Passed: enumerationHttpMethodTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }

  /*
   * @testName: enumerationTransportGuaranteeTest
   *
   * @assertion_ids: JavaEE:SPEC:10265; Servlet:SPEC:175; Servlet:SPEC:179
   *
   * @test_Strategy: Attempt to deploy a war file which whose deployment
   * descriptor contains a transport-guarantee tag in which the enumeration
   * value contains lower-case characters, but which is otherwise valid, and
   * verify that deployment fails. Deploy the same war file in which the
   * deployment descriptor has been corrected.
   * 
   */
  public void enumerationTransportGuaranteeTest() throws Fault {

    TestUtil.logMsg("Starting enumerationTransportGuaranteeTest");
    boolean pass = false;
    String appDir = baseAppDir + "transport-guarantee/";
    String badModuleName = appDir + "BadTransportGuarantee.war";
    String goodModuleName = appDir + "GoodTransportGuarantee.war";
    String badPlanName = appDir
        + "servlet_ee_platform_negdep_enumeration_apps_badtransport-guarantee_web.war.sun-web.xml";
    String goodPlanName = appDir
        + "servlet_ee_platform_negdep_enumeration_apps_goodtransport-guarantee_web.war.sun-web.xml";

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
        throw new Fault("Failed: enumerationTransportGuaranteeTest");
      else
        logMsg("Passed: enumerationTransportGuaranteeTest");
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      throw new Fault("Failed: " + ex.getMessage());
    }
  }
}
