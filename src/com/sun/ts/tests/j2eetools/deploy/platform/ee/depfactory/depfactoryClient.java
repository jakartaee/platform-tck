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
 * @(#)depfactoryClient.java	1.10   03/05/28
 */

package com.sun.ts.tests.j2eetools.deploy.platform.ee.depfactory;

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

// common test imports
import com.sun.ts.lib.deliverable.cts.deploy.*;

public class depfactoryClient extends ServiceEETest {

  // Member instance variables
  DMProps dmp; // Properties object derived from ts.jte file

  DeployTestUtil dtu; // Utility to obtain DM's

  Properties testProps; // Test properties passed via harness code. Contents of
                        // ts.jte file

  /**
   * Run test in standalone mode
   */
  public static void main(String[] args) {
    depfactoryClient theTests = new depfactoryClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: deployManagerJarFile.1, jarfile with manifest;
   * deployManageruri.1, URI to connect to; deployManageruname.1, username for
   * DeploymentManager; deployManagerpasswd.1, password for DeploymentManager;
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logMsg("Setup");
    this.testProps = p;

    try {

      // Construct properties object for DM
      dmp = new DMProps(p.getProperty("deployManagerJarFile.1"),
          p.getProperty("deployManageruri.1"),
          p.getProperty("deployManageruname.1"),
          p.getProperty("deployManagerpasswd.1"));

      // Utility object for getting dm's
      dtu = new DeployTestUtil(dmp);

    } catch (Exception e) {
      TestUtil.logErr("Exception constructing DeployTestUtil: " + e, e);
    }
  }

  /* cleanup */
  public void cleanup() throws Fault {
    TestUtil.logMsg("Releasing DeploymentManager...");
    TestUtil.logMsg("DeploymentManager released");
  }

  /*
   * @testName: testGetDeploymentManager
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:1; J2EEDEPLOY:JAVADOC:2;
   * J2EEDEPLOY:JAVADOC:3; J2EEDEPLOY:JAVADOC:90; J2EEDeploy:SPEC:1;
   * J2EEDeploy:SPEC:7;
   *
   * @test_Strategy: Obtain a connection to DeploymentManager. Verify by calling
   * a simple method.
   *
   */

  public void testGetDeploymentManager() throws Fault {

    TestUtil.logMsg("Starting testGetDeploymentManager");

    DeploymentManager dm;

    try {
      dm = dtu.getDeploymentManager();
      boolean supports = dm.isRedeploySupported();
      TestUtil.logMsg("Redepoyment: " + supports);
      dm.release();
    } catch (DeploymentManagerCreationException dmce) {
      TestUtil.printStackTrace(dmce);
      throw new Fault("Unable to get DeploymentManager: " + dmce.getMessage());
    }

  }

  /*
   * @testName: testGetDisconnectedDeploymentManager
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:6; J2EEDEPLOY:JAVADOC:7;
   * J2EEDEPLOY:JAVADOC:92; J2EEDeploy:SPEC:9; J2EEDeploy:SPEC:10
   * 
   * @test_Strategy: get a disconnected DeploymentManager. Try to call
   * dm.getTargets. Should throw CreationException.
   *
   */

  public void testGetDisconnectedDeploymentManager() throws Fault {

    TestUtil.logMsg("Starting testGetDisconnectedDeploymentManager");

    DeploymentManager dm;
    boolean pass = false;

    try {
      dm = dtu.getDisconnectedDeploymentManager();
      TestUtil.logMsg("Got disconnected DM");
    } catch (DeploymentManagerCreationException dmce) {
      TestUtil.printStackTrace(dmce);
      throw new Fault("Unable to get DeploymentManager: " + dmce.getMessage());
    }

    try {
      dm.getTargets();
    } catch (IllegalStateException ise) {
      pass = true;
      TestUtil.logMsg("Received expected IllegalStateException");
    }

    // Need a better way to release.
    // dm.release();

    if (!pass) {
      throw new Fault("Did not receive expected IllegalStateException");
    }
  }

  /*
   * @testName: testGetDisplayName
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:94;
   * 
   * @test_Strategy: Load all DeploymentFactory's. Validate that getDisplayName
   * returns a String.
   *
   */

  public void testGetDisplayName() throws Fault {

    TestUtil.logMsg("Starting testGetDisplayName");
    DeploymentFactory[] dFactories = DeploymentFactoryManager.getInstance()
        .getDeploymentFactories();

    for (int i = 0; i < dFactories.length; i++) {

      DeploymentFactory df = dFactories[i];
      String dispname = df.getDisplayName();

      if (dispname instanceof String) {
        TestUtil.logMsg("DisplayName is: " + dispname);
      } else {
        throw new Fault("DisplayName is not a String type");
      }
    }

  }

  /*
   * @testName: testHandlesURI
   *
   * @assertion_ids: J2EEDEPLOY:JAVADOC:89;
   * 
   * @test_Strategy: Obtain a list of DeploymentFactories. At least one of them
   * should handle the URI provided to the test suite.
   *
   */

  public void testHandlesURI() throws Fault {

    TestUtil.logMsg("Starting testHandlesURI");
    DeploymentFactory[] dFactories = DeploymentFactoryManager.getInstance()
        .getDeploymentFactories();
    boolean handlesuri = false;

    for (int i = 0; i < dFactories.length; i++) {
      DeploymentFactory df = dFactories[i];
      if (df.handlesURI(dmp.getURI())) {
        handlesuri = true;
      }
    }

    TestUtil.logMsg("Length is: " + dFactories.length);

    if (!handlesuri) {
      throw new Fault("No DeploymentFactory could handle this uri");
    }

  }

}
