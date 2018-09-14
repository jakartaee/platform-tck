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

package com.sun.ts.tests.j2eetools.deploy.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.Properties;

// Harness imports
import com.sun.ts.lib.deliverable.DeliverableFactory;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.porting.DeploymentInfo;
import com.sun.ts.lib.porting.TSDeployment2;
import com.sun.ts.lib.porting.TSDeploymentInterface2;

import com.sun.ts.lib.util.TestUtil;

/**
 * Utility class to retrieve deployment plans through the TSDeploymentInterface.
 * 
 * @author Anand Dhingra, Sun Microsystems
 *
 */
public class Util {

  Properties testProps; // Test properties from TS harness

  String appDir; // Directory of apps to be deployed

  String earDir; // Ears live here

  String earRt[]; // Contains runtime files for creating deployment plan

  File earFile; // EAR to deploy

  File earPlan; // Deployment plan

  String warDir; // Wars live here

  String warRt[]; // Contains runtime files for creating deployment plan

  File warFile; // WAR to deploy

  File warPlan; // Deployment plan

  String ejbDir; // Ejbs live here

  String ejbRt[]; // Contains runtime files for creating deployment plan

  File ejbFile; // EJB to deploy

  File ejbPlan; // Deployment plan

  String carDir; // Cars live here

  String carRt[]; // Contains runtime files for creating deployment plan

  File carFile; // CAR to deploy

  File carPlan; // Deployment plan

  String rarDir; // Rars live here

  String rarRt[]; // Contains runtime files for creating deployment plan

  File rarFile; // RAR to deploy

  File rarPlan; // Deployment plan

  /**
   * Default constructor
   */
  public Util(Properties p) {

    testProps = p;

    // Get application home
    appDir = testProps.getProperty("ts.home")
        + "/src/com/sun/ts/tests/j2eetools/deploy/apps" + "/";

    // Ear files
    earDir = appDir + "ear/";
    earFile = new File(earDir + "ejb_sam_Hello.ear");
    earRt = new String[] { earDir + "ejb_sam_Hello_ejb.jar.sun-ejb-jar.xml",
        earDir + "ejb_sam_Hello_client.jar.sun-application-client.xml" };

    // War files
    warDir = appDir + "war/";
    warFile = new File(warDir + "assembly_standalone_war_component_web.war");
    warRt = new String[] {
        warDir + "assembly_standalone_war_component_web.war.sun-web.xml" };

    // Car files
    carDir = appDir + "car/";
    carFile = new File(carDir + "appclient_dep_compat_client.jar");
    carRt = new String[] {
        carDir + "appclient_dep_compat_ejb.jar.sun-ejb-jar.xml",
        carDir + "appclient_dep_compat_client.jar.sun-application-client.xml" };

    // Ejb files
    ejbDir = appDir + "ejb/";
    ejbFile = new File(ejbDir + "assembly_standalone_jar_component_ejb.jar");
    ejbRt = new String[] {
        ejbDir + "assembly_standalone_jar_component_ejb.jar.sun-ejb-jar.xml" };

    // Rar files
    rarDir = appDir + "rar/";
    rarFile = new File(rarDir + "whitebox-tx-88.rar");
    rarRt = new String[] { rarDir + "nullrarplan.xml" };

  }

  public File getEarFile() {
    return earFile;
  }

  public File getEarPlan() {
    earPlan = getDeploymentPlan(earFile.toString(), earRt);
    return earPlan;
  }

  public File getCarFile() {
    return carFile;
  }

  public File getCarPlan() {
    carPlan = getDeploymentPlan(carFile.toString(), carRt);
    return carPlan;
  }

  public File getWarFile() {
    return warFile;
  }

  public File getWarPlan() {
    warPlan = getDeploymentPlan(warFile.toString(), warRt);
    return warPlan;
  }

  public File getEjbFile() {
    return ejbFile;
  }

  public File getEjbPlan() {
    ejbPlan = getDeploymentPlan(ejbFile.toString(), ejbRt);
    return ejbPlan;
  }

  public File getRarFile() {
    return rarFile;
  }

  public File getRarPlan() {
    rarPlan = getDeploymentPlan(rarFile.toString(), rarRt);
    return rarPlan;
  }

  private File getDeploymentPlan(String earfile, String[] runtimefiles) {

    TestUtil.logTrace("Calling getDeploymentPlan ...");
    File f = null;
    PropertyManagerInterface propMgr;

    try {
      TestUtil.logTrace("About to get DeliverableFactory ...");
      propMgr = DeliverableFactory.getDeliverableInstance()
          .createPropertyManager(testProps);
      TestUtil.logTrace("Got DeliverableFactory");
    } catch (Exception ex) {
      TestUtil.logErr("Failed to create PropertyManager in TS constructor.",
          ex);
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
      f = (File) (dep2.getDeploymentPlan(info));
      TestUtil.logTrace("Got File");

    } catch (Exception e) {
      TestUtil.logErr("Couldn't get Deployment Plan.", e);
    }

    return f;
  }

}
