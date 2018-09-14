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

package com.sun.ts.tests.common.negdep;

// Java imports
import java.io.*;
import java.util.*;

// Harness imports
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.deliverable.cts.deploy.*;
import com.sun.ts.lib.deliverable.DeliverableFactory;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;

// J2EE Deployment API's
import javax.enterprise.deploy.spi.*;

public class NegdepUtil {

  // Member instance variables
  DMProps dmp; // Properties object derived from ts.jte file

  DeployTestUtil dtu; // Utility to obtain DM's

  DeploymentManager dm; // The DeploymentManager we're interacting with

  private Properties testProps;

  public NegdepUtil(Properties p) {

    testProps = p;

    try {

      // Construct properties object for DM
      dmp = new DMProps(p.getProperty("deployManagerJarFile.1"),
          p.getProperty("deployManageruri.1"),
          p.getProperty("deployManageruname.1"),
          p.getProperty("deployManagerpasswd.1"));

      // Utility object for getting dm's
      dtu = new DeployTestUtil(dmp);

    } catch (Exception e) {
      TestUtil.logErr("Exception construction NegdepUtil" + e, e);
    }
  }

  public void cleanup() {
    TestUtil.logMsg("Releasing DeploymentManager...");
    dtu.releaseDeploymentManager();
    TestUtil.logMsg("DeploymentManager released");
  }

  public InputStream getDeploymentPlan(String earfile, String[] runtimefiles) {

    TestUtil.logTrace("Calling getDeploymentPlan ...");
    File deploymentPlan = null;
    InputStream is = null;
    PropertyManagerInterface propMgr;

    try {
      TestUtil.logTrace("About to get DeliverableFactory ...");
      propMgr = DeliverableFactory.getDeliverableInstance()
          .createPropertyManager(testProps);
      TestUtil.logTrace("Got DeliverableFactory");
    } catch (Exception ex) {
      TestUtil.logErr("Failed to create PropertyManager in TS constructor.",
          ex);
      return null;
    }
    try {

      // Deployment Interface
      TestUtil.logTrace("About to get DeploymentInterface ...");
      TSDeploymentInterface2 dep2 = TSDeployment2.getDeploymentInstance(
          new PrintWriter(System.out), "porting.ts.deploy2.class.1");
      TestUtil.logTrace("GotDeploymentInterface");

      // DeploymentInfo
      TestUtil.logTrace("About to get DeploymentInfo ...");
      DeploymentInfo info = DeliverableFactory.getDeliverableInstance()
          .getDeploymentInfo(earfile, runtimefiles);
      TestUtil.logTrace("Got DeploymentInfo");

      // Deployment plan, as InputStream returned from harness
      TestUtil.logTrace("Getting File from harness ...");
      deploymentPlan = (File) (dep2.getDeploymentPlan(info));
      TestUtil.logTrace("Got File");

      is = new FileInputStream(deploymentPlan);

    } catch (Exception e) {
      TestUtil.logErr("Couldn't get Deployment Plan File.", e);
    }
    return is;
  }

  public boolean negativeTestDistributeModule(FileInputStream moduleArchive,
      InputStream deploymentPlan) throws Exception {

    TestUtil.logTrace("Beginning negativeTestDistributeModule()");
    Target[] targets = dtu.getTargets();
    TestUtil.logTrace("got targets");
    OperationStatus status = dtu.distributeModuleStreams(targets, moduleArchive,
        deploymentPlan);
    TestUtil.logTrace("status message is " + status.getProgressMessage());
    if (!status.isFailed()) {
      TestUtil.logErr("Deployment did not fail as expected");
      cleanupModules(status);
      return false;
    }
    TestUtil.logTrace("Deployment failed as expected");
    return true;
  }

  public boolean positiveTestDistributeModule(FileInputStream moduleArchive,
      InputStream deploymentPlan) throws Exception {
    TestUtil.logTrace("Beginning positiveTestDistributeModule()");
    Target[] targets = dtu.getTargets();
    TestUtil.logTrace("got targets");
    OperationStatus status = dtu.distributeModuleStreams(targets, moduleArchive,
        deploymentPlan);
    TestUtil.logTrace("status message is " + status.getProgressMessage());
    if (status.isFailed()) {
      TestUtil.logErr("Deployment failed - not expected");
      return false;
    }
    TestUtil.logTrace("Deployment succeeded as expected");
    cleanupModules(status);
    return true;
  }

  private void cleanupModules(OperationStatus status) {

    boolean success = false;
    TestUtil.logTrace("undeploying modules");

    try {
      TargetModuleID[] deployedTargetIDs = status.getDeployedTargetIDs();
      if (deployedTargetIDs != null) {
        status = dtu.undeployModule(deployedTargetIDs);
        if (!status.isFailed())
          success = true;
      }
      if (!success)
        TestUtil.logErr("Failed to undeploy modules");
    } catch (Exception e) {
      TestUtil.logErr("Exception while undeploying modules", e);
    }
  }
}
