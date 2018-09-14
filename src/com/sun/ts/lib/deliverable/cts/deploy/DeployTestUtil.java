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

package com.sun.ts.lib.deliverable.cts.deploy;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;

import java.io.*;
import java.util.jar.Manifest;
import java.net.URL;
import java.net.URLClassLoader;

// Harness imports
import com.sun.ts.lib.util.TestUtil;

// J2EE Deployment imports
import javax.enterprise.deploy.spi.*;
import javax.enterprise.deploy.spi.status.*;
import javax.enterprise.deploy.spi.exceptions.*;
import javax.enterprise.deploy.model.*;
import javax.enterprise.deploy.shared.*;
import javax.enterprise.deploy.model.*;

import java.io.*;
import java.util.*;

import javax.enterprise.deploy.shared.factories.*;
import javax.enterprise.deploy.spi.*;
import javax.enterprise.deploy.spi.exceptions.*;
import javax.enterprise.deploy.spi.factories.*;

public class DeployTestUtil {

  public static final String DEPLOYMENT_MANIFEST_ENTRY_NAME = "J2EE-DeploymentFactory-Implementation-Class";

  public static final String FILE_ARCHIVE = "FILE";

  public static final String STREAM_ARCHIVE = "STREAM";

  public static final String APPLICATION_ARCHIVE = "APPLICATION";

  public static final String STAND_ALONE_ARCHIVE = "STAND_ALONE";

  private DeploymentManager dm = null;

  private DMProps dmp;

  private DeploymentManager disconnectedDM;

  /**
   *
   * Construct a DeployTestUtil object. Load the DeploymentFactories from the
   * provided jarfile, and get a DeploymentManager instance.
   *
   */
  public DeployTestUtil(DMProps dmprops) throws Exception {
    dmp = dmprops;
    loadDMFactories();
  }

  public DeploymentManager getDeploymentManager()
      throws DeploymentManagerCreationException {
    if (dm == null) {
      DeploymentFactoryManager dfmanager = DeploymentFactoryManager
          .getInstance();
      dm = dfmanager.getDeploymentManager(dmp.getURI(), dmp.getUname(),
          dmp.getPasswd());
      TestUtil.logHarnessDebug(
          "$$$$$$$$$$$$$$$[" + dmp.getUname() + ", " + dmp.getPasswd() + "]");
      TestUtil.logHarness("amd Obtained FRESH DeploymentManager");
    } else {
      TestUtil.logHarness("amd Obtained CACHED DeploymentManager");
    }
    System.out.println(dm);
    return dm;
  }

  public DeploymentManager getDisconnectedDeploymentManager()
      throws DeploymentManagerCreationException {
    if (disconnectedDM == null) {

      TestUtil.logHarness("DeployTestUtil.getDeploymentManager called");
      DeploymentFactoryManager dfmanager = DeploymentFactoryManager
          .getInstance();
      disconnectedDM = dfmanager.getDisconnectedDeploymentManager(dmp.getURI());
    }
    return (disconnectedDM);
  }

  public Vector getDeploymentFactoryClassNames() throws Exception {
    File deployPlatformJarFile;

    try {
      deployPlatformJarFile = new File(dmp.getJarFile());
    } catch (Exception e) {
      throw new Exception(
          "Could not contruct file object: " + dmp.getJarFile());
    }

    // Obtain DeploymentFactory names from manifest and load those entries
    TestUtil.logHarness("loadDeploymentFactories loading factories from file:"
        + deployPlatformJarFile.getName());

    Manifest mf = new java.util.jar.JarFile(deployPlatformJarFile)
        .getManifest();

    // This should be rewritten to get multiple class names from the manifest
    String dfClassNames = mf.getMainAttributes()
        .getValue(DEPLOYMENT_MANIFEST_ENTRY_NAME);
    if (dfClassNames == null) {
      throw new Exception(
          "Invalid Jar File: Jar file does not have entry for Deployment factories");
    }

    // Parse class names, assuming that class names are separated by space
    java.util.StringTokenizer st = new java.util.StringTokenizer(dfClassNames,
        " ");
    Vector classNames = new Vector();
    while (st.hasMoreTokens()) {
      classNames.add(st.nextToken());
    }

    return (classNames);

  }

  private void loadDMFactories() throws Exception {
    TestUtil.logHarness("DeployTestUtil.loadDMFactories called");
    TestUtil.logHarness(
        "DeployTestUtil.loadDMFactories using jarfile: " + dmp.getJarFile());
    loadDeploymentFactories();
  }

  private void loadDeploymentFactories() throws Exception {

    File deployPlatformJarFile;

    try {
      deployPlatformJarFile = new File(dmp.getJarFile());
    } catch (Exception e) {
      throw new Exception(
          "Could not contruct file object: " + dmp.getJarFile());
    }

    // Obtain DeploymentFactory names from manifest and load those entries
    TestUtil.logHarness("loadDeploymentFactories loading factories from file:"
        + deployPlatformJarFile.getName());

    Manifest mf = new java.util.jar.JarFile(deployPlatformJarFile)
        .getManifest();

    // This should be rewritten to get multiple class names from the manifest
    TestUtil.logHarness(
        "Looking for manifest entry: " + DEPLOYMENT_MANIFEST_ENTRY_NAME);
    String dfClassNames = mf.getMainAttributes()
        .getValue(DEPLOYMENT_MANIFEST_ENTRY_NAME);

    if (dfClassNames == null) {
      throw new Exception("Invalid Jar File: No manifest entry.");
    }

    // Parse class names, assuming that class names are separated by space
    java.util.StringTokenizer st = new java.util.StringTokenizer(dfClassNames,
        " ");
    Vector classNames = new Vector();
    while (st.hasMoreTokens())
      classNames.add(st.nextToken());

    // Load classes
    try {
      URL[] urls = new URL[] { deployPlatformJarFile.toURL() };
      URLClassLoader urlClassLoader = new URLClassLoader(urls,
          this.getClass().getClassLoader());

      // Do i need to set the contect class loader ?
      TestUtil.logHarness(
          "loadDeploymentFactories about to load classes :" + dfClassNames);
      Thread.currentThread().setContextClassLoader(urlClassLoader);

      // Load all classes
      for (int classNameIndex = 0; classNameIndex < classNames
          .size(); classNameIndex++) {

        Class factory = null;
        DeploymentFactory df = null;

        try {
          factory = urlClassLoader
              .loadClass((String) classNames.elementAt(classNameIndex));
          df = (DeploymentFactory) factory.newInstance();
        } catch (Exception e) {
          e.printStackTrace();
        }

        if (df instanceof DeploymentFactory) {
          DeploymentFactoryManager.getInstance()
              .registerDeploymentFactory((DeploymentFactory) df);
          TestUtil
              .logMsg("Registered DeploymentFactory: " + df.getDisplayName());
        } else {
          throw new Exception("Does not implement DeploymentFactory");
        }

        // Class.forName((String)classNames.elementAt(classNameIndex),
        // true, urlClassLoader);

        // Register DeploymentFactory with DeploymentFactoryManager
        // DeploymentFactory df = (DeploymentFactory)classNames.elementAt
        // (classNameIndex);
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Exception(
          "Could not load DeploymentFactory class: " + ex.getMessage());
    }

  }

  public void releaseDeploymentManager() {

    if (dm != null) {
      TestUtil.logHarness("amd Releasing DeploymentManager amd");
      this.dm.release();
    }
    if (disconnectedDM != null) {
      TestUtil.logHarness("amd Releasing DISCONNECTED DeploymentManager amd");
      this.disconnectedDM.release();
    }

    TestUtil.logHarness(" amd Released DeploymentManager amd ");
  }

  public boolean checkIfModuleExists(TargetModuleID moduleToCheck,
      TargetModuleID[] modulesList) {
    boolean exists = false;
    if (moduleToCheck == null) {
      return exists;
    }
    int numMods = (modulesList == null) ? 0 : modulesList.length;
    for (int modIndex = 0; modIndex < numMods; modIndex++) {
      if (moduleToCheck.getModuleID()
          .equals(modulesList[modIndex].getModuleID())) {
        // Also check for same targets.
        if (moduleToCheck.getTarget().getName()
            .equals(modulesList[modIndex].getTarget().getName())) {
          exists = true;
          break;
        }
      }
    }
    return exists;
  }

  /*
   * If the operation is successful then TargetModuleID is a valid module id.
   * Else it will be null
   **/
  public TargetModuleID distributeModuleArchive(Target[] targets,
      File moduleArchive, File deploymentPlan) throws Exception {
    DeploymentManager dm = getDeploymentManager();
    ProgressHandler progressHandler = new ProgressHandler();
    ProgressObject progress = dm.distribute(targets, moduleArchive,
        deploymentPlan);
    progress.addProgressListener(progressHandler);
    progressHandler.start();
    // Wait for the progress handler to complete its job
    progressHandler.join();
    StateType completionState = progressHandler.getCompletionState();
    if (completionState.getValue() != StateType.COMPLETED.getValue()) {
      // The state must be either FAILED, or RELEASED
      return null;
    }
    TargetModuleID[] resultModuleIDs = progress.getResultTargetModuleIDs();
    if (resultModuleIDs.length < 1) {
      // There should be atleast one target module id if progress is
      // successfully complete
      return null;
    }
    return resultModuleIDs[0];
  }

  /*
   * If the operation is successful then TargetModuleID is a valid module id.
   * Else it will be null
   **/
  public TargetModuleID distributeModuleStream(Target[] targets,
      ModuleType moduleType, InputStream moduleArchive,
      InputStream deploymentPlan, boolean oneDotFiveApi) throws Exception {
    String apiType = (oneDotFiveApi == true) ? "1.5 API" : "deprecated API";
    TestUtil.logMsg("Calling distribute(): Using " + apiType);
    DeploymentManager dm = getDeploymentManager();
    ProgressHandler progressHandler = new ProgressHandler();
    ProgressObject progress = (oneDotFiveApi == true)
        ? dm.distribute(targets, moduleType, moduleArchive, deploymentPlan)
        : dm.distribute(targets, moduleArchive, deploymentPlan);
    progress.addProgressListener(progressHandler);
    progressHandler.start();
    // Wait for the progress handler to complete its job
    progressHandler.join();
    StateType completionState = progressHandler.getCompletionState();
    if (completionState.getValue() != StateType.COMPLETED.getValue()) {
      // The state must be either FAILED, or RELEASED
      return null;
    }
    TargetModuleID[] resultModuleIDs = progress.getResultTargetModuleIDs();
    if (resultModuleIDs.length < 1) {
      // There should be atleast one target module id if progress is
      // successfully complete
      return null;
    }
    return resultModuleIDs[0];
  }

  /*
   * If the operation is successful then TargetModuleID is a valid module id.
   * Else it will be null
   **/
  public TargetModuleID startModule(TargetModuleID moduleToStart)
      throws Exception {
    DeploymentManager dm = getDeploymentManager();
    ProgressHandler progressHandler = new ProgressHandler();
    ProgressObject progress = dm.start(new TargetModuleID[] { moduleToStart });
    progress.addProgressListener(progressHandler);
    progressHandler.start();
    // Wait for the progress handler to complete its job
    progressHandler.join();
    StateType completionState = progressHandler.getCompletionState();
    if (completionState.getValue() != StateType.COMPLETED.getValue()) {
      // The state must be either FAILED, or RELEASED
      return null;
    }
    TargetModuleID[] resultModuleIDs = progress.getResultTargetModuleIDs();
    if (resultModuleIDs.length < 1) {
      // There should be atleast one target module id if progress is
      // successfully complete
      return null;
    }
    return resultModuleIDs[0];
  }

  /*
   * If the operation is successful then TargetModuleID is a valid module id.
   * Else it will be null
   **/
  public TargetModuleID stopModule(TargetModuleID moduleToStop)
      throws Exception {
    DeploymentManager dm = getDeploymentManager();
    ProgressHandler progressHandler = new ProgressHandler();
    ProgressObject progress = dm.stop(new TargetModuleID[] { moduleToStop });
    progress.addProgressListener(progressHandler);
    progressHandler.start();
    // Wait for the progress handler to complete its job
    progressHandler.join();
    StateType completionState = progressHandler.getCompletionState();
    if (completionState.getValue() != StateType.COMPLETED.getValue()) {
      // The state must be either FAILED, or RELEASED
      return null;
    }
    TargetModuleID[] resultModuleIDs = progress.getResultTargetModuleIDs();
    if (resultModuleIDs.length < 1) {
      // There should be atleast one target module id if progress is
      // successfully complete
      return null;
    }
    return resultModuleIDs[0];
  }

  /*
   * If the operation is successful then TargetModuleID is a valid module id.
   * Else it will be null
   **/
  public TargetModuleID undeployModule(TargetModuleID moduleToUndeploy)
      throws Exception {
    DeploymentManager dm = getDeploymentManager();
    ProgressHandler progressHandler = new ProgressHandler();
    ProgressObject progress = dm
        .undeploy(new TargetModuleID[] { moduleToUndeploy });
    progress.addProgressListener(progressHandler);
    progressHandler.start();
    // Wait for the progress handler to complete its job
    progressHandler.join();
    StateType completionState = progressHandler.getCompletionState();
    if (completionState.getValue() != StateType.COMPLETED.getValue()) {
      // The state must be either FAILED, or RELEASED
      return null;
    }
    TargetModuleID[] resultModuleIDs = progress.getResultTargetModuleIDs();
    if (resultModuleIDs.length < 1) {
      // There should be atleast one target module id if progress is
      // successfully complete
      return null;
    }
    return resultModuleIDs[0];
  }

  /*
   * If the operation is successful then TargetModuleID is a valid module id.
   * Else it will be null
   **/
  public TargetModuleID redeployModuleArchive(TargetModuleID[] targetModuleIds,
      File moduleArchive, File deploymentPlan) throws Exception {
    DeploymentManager dm = getDeploymentManager();
    ProgressHandler progressHandler = new ProgressHandler();
    ProgressObject progress = dm.redeploy(targetModuleIds, moduleArchive,
        deploymentPlan);
    progress.addProgressListener(progressHandler);
    progressHandler.start();
    // Wait for the progress handler to complete its job
    progressHandler.join();
    StateType completionState = progressHandler.getCompletionState();
    if (completionState.getValue() != StateType.COMPLETED.getValue()) {
      // The state must be either FAILED, or RELEASED
      return null;
    }
    TargetModuleID[] resultModuleIDs = progress.getResultTargetModuleIDs();
    if (resultModuleIDs.length < 1) {
      // There should be atleast one target module id if progress is
      // successfully complete
      return null;
    }
    return resultModuleIDs[0];
  }

  /*
   * If the operation is successful then TargetModuleID is a valid module id.
   * Else it will be null
   **/
  public TargetModuleID redeployModuleStream(TargetModuleID[] targetModuleIds,
      InputStream moduleArchive, InputStream deploymentPlan) throws Exception {
    DeploymentManager dm = getDeploymentManager();
    ProgressHandler progressHandler = new ProgressHandler();
    ProgressObject progress = dm.redeploy(targetModuleIds, moduleArchive,
        deploymentPlan);
    progress.addProgressListener(progressHandler);
    progressHandler.start();
    // Wait for the progress handler to complete its job
    progressHandler.join();
    StateType completionState = progressHandler.getCompletionState();
    if (completionState.getValue() != StateType.COMPLETED.getValue()) {
      // The state must be either FAILED, or RELEASED
      return null;
    }
    TargetModuleID[] resultModuleIDs = progress.getResultTargetModuleIDs();
    if (resultModuleIDs.length < 1) {
      // There should be atleast one target module id if progress is
      // successfully complete
      return null;
    }
    return resultModuleIDs[0];
  }

  public boolean testDistributeModule(ModuleType moduleType, File moduleArchive,
      File deploymentPlan, String archiveType, boolean oneDotFiveApi)
      throws Exception {
    DeploymentManager dm = getDeploymentManager();
    Target[] targets = dm.getTargets();
    if (targets.length == 0)
      return false;
    Target targetToDeploy = targets[0];
    TargetModuleID distributedModuleID = null;
    TargetModuleID[] moduleIDsBeforeDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    if (archiveType.equals(FILE_ARCHIVE)) {
      distributedModuleID = distributeModuleArchive(
          new Target[] { targetToDeploy }, moduleArchive, deploymentPlan);
    } else {
      FileInputStream moduleStream = (moduleArchive == null) ? null
          : new FileInputStream(moduleArchive);
      FileInputStream planStream = (deploymentPlan == null) ? null
          : new FileInputStream(deploymentPlan);
      distributedModuleID = distributeModuleStream(
          new Target[] { targetToDeploy }, moduleType, moduleStream, planStream,
          oneDotFiveApi);
    }
    // If the distributedModuleID == null, the test is failed
    if (distributedModuleID == null)
      return false;
    // Check if distriburtedModuleID has the same target as deployed target
    if (!distributedModuleID.getTarget().getName()
        .equals(targetToDeploy.getName()))
      return false;
    TargetModuleID[] moduleIDsAfterDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    boolean moduleExistsBeforeDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsBeforeDistribute);
    boolean moduleExistsAfterDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsAfterDistribute);

    // Clean up....
    // undeploy the module
    undeployModule(distributedModuleID);

    if ((!moduleExistsBeforeDistribute) && moduleExistsAfterDistribute)
      return true;
    else
      return false;
  }

  public boolean testStartModule(ModuleType moduleType, File moduleArchive,
      File deploymentPlan, String archiveType) throws Exception {
    DeploymentManager dm = getDeploymentManager();
    Target[] targets = dm.getTargets();
    if (targets.length == 0)
      return false;
    Target targetToDeploy = targets[0];
    TargetModuleID distributedModuleID = null;
    TargetModuleID[] moduleIDsBeforeDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    if (archiveType.equals(FILE_ARCHIVE)) {
      distributedModuleID = distributeModuleArchive(
          new Target[] { targetToDeploy }, moduleArchive, deploymentPlan);
    } else {
      distributedModuleID = distributeModuleStream(
          new Target[] { targetToDeploy }, moduleType,
          new FileInputStream(moduleArchive),
          new FileInputStream(deploymentPlan), true);
    }
    // If the distributedModuleID == null, the test is failed
    if (distributedModuleID == null)
      return false;
    // Check if distriburtedModuleID has the same target as deployed target
    if (!distributedModuleID.getTarget().getName()
        .equals(targetToDeploy.getName()))
      return false;
    TargetModuleID[] moduleIDsAfterDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    boolean moduleExistsBeforeDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsBeforeDistribute);
    boolean moduleExistsAfterDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsAfterDistribute);
    if (!((!moduleExistsBeforeDistribute) && moduleExistsAfterDistribute))
      return false;

    // start the module
    TargetModuleID startedModuleID = null;
    startedModuleID = startModule(distributedModuleID);
    if (startedModuleID == null)
      return false;
    // Check that the startedModuleID is same as deployed module id
    if (!startedModuleID.getModuleID()
        .equals(distributedModuleID.getModuleID()))
      return false;
    TargetModuleID[] runningModules = dm.getRunningModules(moduleType,
        new Target[] { targetToDeploy });
    boolean result = false;
    if (checkIfModuleExists(startedModuleID, runningModules))
      result = true;
    // Clean up....
    // undeploy the module
    stopModule(startedModuleID);
    undeployModule(startedModuleID);

    return result;

  }

  public boolean testStopModule(ModuleType moduleType, File moduleArchive,
      File deploymentPlan, String archiveType) throws Exception {
    DeploymentManager dm = getDeploymentManager();
    Target[] targets = dm.getTargets();
    if (targets.length == 0)
      return false;
    Target targetToDeploy = targets[0];
    TargetModuleID distributedModuleID = null;
    TargetModuleID[] moduleIDsBeforeDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    if (archiveType.equals(FILE_ARCHIVE)) {
      distributedModuleID = distributeModuleArchive(
          new Target[] { targetToDeploy }, moduleArchive, deploymentPlan);
    } else {
      distributedModuleID = distributeModuleStream(
          new Target[] { targetToDeploy }, moduleType,
          new FileInputStream(moduleArchive),
          new FileInputStream(deploymentPlan), true);
    }
    // If the distributedModuleID == null, the test is failed
    if (distributedModuleID == null)
      return false;
    // Check if distriburtedModuleID has the same target as deployed target
    if (!distributedModuleID.getTarget().getName()
        .equals(targetToDeploy.getName()))
      return false;
    TargetModuleID[] moduleIDsAfterDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    boolean moduleExistsBeforeDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsBeforeDistribute);
    boolean moduleExistsAfterDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsAfterDistribute);
    if (!((!moduleExistsBeforeDistribute) && moduleExistsAfterDistribute))
      return false;

    // start the module
    TargetModuleID startedModuleID = null;
    startedModuleID = startModule(distributedModuleID);
    if (startedModuleID == null)
      return false;
    // Check that the startedModuleID is same as deployed module id
    if (!startedModuleID.getModuleID()
        .equals(distributedModuleID.getModuleID()))
      return false;
    TargetModuleID[] runningModules = dm.getRunningModules(moduleType,
        new Target[] { targetToDeploy });
    if (!checkIfModuleExists(startedModuleID, runningModules))
      return false;

    // stop the module
    TargetModuleID stoppedModuleID = null;
    stoppedModuleID = stopModule(startedModuleID);
    if (stoppedModuleID == null)
      return false;
    // Check that the startedModuleID is same as redeployed module id
    if (!startedModuleID.getModuleID().equals(stoppedModuleID.getModuleID()))
      return false;
    boolean modExistsInRunningMods = checkIfModuleExists(stoppedModuleID,
        dm.getRunningModules(moduleType, new Target[] { targetToDeploy }));
    boolean modExistsInNonRunningMods = checkIfModuleExists(stoppedModuleID,
        dm.getNonRunningModules(moduleType, new Target[] { targetToDeploy }));

    // Clean up....
    // undeploy the module
    undeployModule(stoppedModuleID);

    if ((!modExistsInRunningMods) && modExistsInNonRunningMods)
      return true;
    else
      return false;
  }

  public boolean testRedeployModule(ModuleType moduleType, File moduleArchive,
      File deploymentPlan, String archiveType) throws Exception {
    DeploymentManager dm = getDeploymentManager();
    Target[] targets = dm.getTargets();
    if (targets.length == 0)
      return false;
    Target targetToDeploy = targets[0];
    TargetModuleID distributedModuleID = null;
    TargetModuleID[] moduleIDsBeforeDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    if (archiveType.equals(FILE_ARCHIVE)) {
      distributedModuleID = distributeModuleArchive(
          new Target[] { targetToDeploy }, moduleArchive, deploymentPlan);
    } else {
      distributedModuleID = distributeModuleStream(
          new Target[] { targetToDeploy }, moduleType,
          new FileInputStream(moduleArchive),
          new FileInputStream(deploymentPlan), true);
    }
    // If the distributedModuleID == null, the test is failed
    if (distributedModuleID == null)
      return false;
    // Check if distriburtedModuleID has the same target as deployed target
    if (!distributedModuleID.getTarget().getName()
        .equals(targetToDeploy.getName()))
      return false;
    TargetModuleID[] moduleIDsAfterDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    boolean moduleExistsBeforeDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsBeforeDistribute);
    boolean moduleExistsAfterDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsAfterDistribute);
    if (!((!moduleExistsBeforeDistribute) && moduleExistsAfterDistribute))
      return false;

    // start the module
    TargetModuleID startedModuleID = null;
    startedModuleID = startModule(distributedModuleID);
    if (startedModuleID == null)
      return false;
    // Check that the startedModuleID is same as deployed module id
    if (!startedModuleID.getModuleID()
        .equals(distributedModuleID.getModuleID()))
      return false;
    TargetModuleID[] runningModules = dm.getRunningModules(moduleType,
        new Target[] { targetToDeploy });
    if (!checkIfModuleExists(startedModuleID, runningModules))
      return false;

    // redeploy the module
    TargetModuleID redeployedModuleID = null;
    if (archiveType.equals(FILE_ARCHIVE)) {
      redeployedModuleID = redeployModuleArchive(
          new TargetModuleID[] { startedModuleID }, moduleArchive,
          deploymentPlan);
    } else {
      redeployedModuleID = redeployModuleStream(
          new TargetModuleID[] { startedModuleID },
          new FileInputStream(moduleArchive),
          new FileInputStream(deploymentPlan));
    }
    if (redeployedModuleID == null)
      return false;
    // Check that the startedModuleID is same as redeployed module id
    if (!startedModuleID.getModuleID().equals(redeployedModuleID.getModuleID()))
      return false;
    TargetModuleID[] runningModulesAfterRedeploy = dm
        .getRunningModules(moduleType, new Target[] { targetToDeploy });
    boolean result = false;
    if (checkIfModuleExists(redeployedModuleID, runningModulesAfterRedeploy))
      result = true;

    // Clean up....
    // undeploy the module
    stopModule(redeployedModuleID);
    undeployModule(redeployedModuleID);

    return result;
  }

  public boolean testUndeployModule(ModuleType moduleType, File moduleArchive,
      File deploymentPlan, String archiveType) throws Exception {
    DeploymentManager dm = getDeploymentManager();
    Target[] targets = dm.getTargets();
    if (targets.length == 0)
      return false;
    Target targetToDeploy = targets[0];
    TargetModuleID distributedModuleID = null;
    TargetModuleID[] moduleIDsBeforeDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    if (archiveType.equals(FILE_ARCHIVE)) {
      distributedModuleID = distributeModuleArchive(
          new Target[] { targetToDeploy }, moduleArchive, deploymentPlan);
    } else {
      distributedModuleID = distributeModuleStream(
          new Target[] { targetToDeploy }, moduleType,
          new FileInputStream(moduleArchive),
          new FileInputStream(deploymentPlan), true);
    }
    // If the distributedModuleID == null, the test is failed
    if (distributedModuleID == null)
      return false;
    // Check if distriburtedModuleID has the same target as deployed target
    if (!distributedModuleID.getTarget().getName()
        .equals(targetToDeploy.getName()))
      return false;
    TargetModuleID[] moduleIDsAfterDistribute = dm
        .getAvailableModules(moduleType, new Target[] { targetToDeploy });
    boolean moduleExistsBeforeDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsBeforeDistribute);
    boolean moduleExistsAfterDistribute = checkIfModuleExists(
        distributedModuleID, moduleIDsAfterDistribute);
    if (!((!moduleExistsBeforeDistribute) && moduleExistsAfterDistribute))
      return false;

    // start the module
    TargetModuleID undeployedModuleID = null;
    undeployedModuleID = undeployModule(distributedModuleID);
    if (undeployedModuleID == null)
      return false;
    // Check that the startedModuleID is same as deployed module id
    if (!undeployedModuleID.getModuleID()
        .equals(distributedModuleID.getModuleID()))
      return false;
    if (checkIfModuleExists(undeployedModuleID,
        dm.getAvailableModules(moduleType, new Target[] { targetToDeploy })))
      return false;
    else
      return true;
  }

  // To test TargetModuleID
  // Test TargetModuleID
  public boolean testTargetModuleID(File moduleArchive, File deploymentPlan,
      String archiveType) {
    try {
      DeploymentManager dm = getDeploymentManager();
      Target[] targets = dm.getTargets();
      if (targets.length == 0)
        return false;
      Target targetToDeploy = targets[0];
      TargetModuleID distributedModuleID = null;
      distributedModuleID = distributeModuleArchive(
          new Target[] { targetToDeploy }, moduleArchive, deploymentPlan);
      // If the distributedModuleID == null, the test is failed
      if (distributedModuleID == null)
        return false;
      // Check if distriburtedModuleID has the same target as deployed target
      if (!distributedModuleID.getTarget().getName()
          .equals(targetToDeploy.getName()))
        return false;

      // Clean up....
      // undeploy the module
      undeployModule(distributedModuleID);

      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
  }

  //////
  // Porting package 1.4 additions
  //////

  /*
   * Why are we polling on the progress object's state to determine when the
   * executed command is actually finished? First, Prakash mentioned that there
   * is a case where it is possible for an executed command to finish before the
   * user is even returned the progress object. In this case if we register a
   * ProgressListener with the ProgressObject and handle events within the
   * ProgressListener's handleProgressEvent() we may miss the event that told us
   * the executed command had finished. Second, again according to Prakash, the
   * spec does not specify how many events are generated when executing a
   * command. For instance, if we call stop and pass 4 TargetModuleID objects,
   * there is no place in the spec that tells us how many events we should
   * expect. There could be a set of one or more events for each target or a
   * single event for all the targets. Since the spec doesn't define this, we
   * shouldn't assume.
   *
   * Having said that we decided to simply poll the progress object and ask it
   * when it thinks it is finished. This will be denoted by a COMPLETED or
   * FAILED state. If the state is FAILED we can use the
   * getResultTargetModuleIds method to determine the failed targets. We can
   * determine the failed targets by comparing the targets we issued the command
   * for against the targets returned by getResultTargetModuleIds. The doc for
   * getResultTargetModuleIds says it only returns the IDs of the targets that
   * successfully completed the specified command. See the OperationStatus class
   * for details on determining the failed targets.
   */
  private OperationStatus commandStatus(Target[] targets,
      ProgressObject progress) {
    OperationStatus status = null;
    int sleepCount = 0;
    int deployWaitMinutes = getDeployDelay();
    final int MAX_SLEEP_COUNT = deployWaitMinutes * 60; // minutes to delay
    while (!(progress.getDeploymentStatus().isCompleted()
        || progress.getDeploymentStatus().isFailed())) {
      try {
        Thread.sleep(1 * 1000); // 1 second
        if (++sleepCount >= MAX_SLEEP_COUNT) {
          TestUtil.logErr("Error: DeployTestUtil.commandStatus() timed out"
              + " waiting for operation to complete");
          status = new OperationStatus(progress, targets, true);
          break;
        }
      } catch (InterruptedException ie) {
        break;
      }
    }
    if (status == null) {
      status = new OperationStatus(progress, targets);
    }

    return status;
  }

  private int getDeployDelay() {
    int result = 5;
    String delayStr = System.getProperty("DEPLOY_DELAY_IN_MINUTES", "5");
    try {
      result = Integer.parseInt(delayStr);
    } catch (NumberFormatException nfe) {
    }
    TestUtil.logTrace(
        "Maximum wait time for deployment is " + result + " minutes.");
    return result;
  }

  private void writeFile(File aFile) {
    FileInputStream in = null;
    FileOutputStream out = null;
    try {
      in = new FileInputStream(aFile);
      out = new FileOutputStream("/tmp/" + aFile.getName());
      byte[] buf = new byte[2048];
      int start = 0;
      int i = 0;
      while ((i = in.read(buf)) != -1) {
        out.write(buf, 0, i);
        start += i;
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        in.close();
      } catch (Exception e) {
      }
      try {
        out.close();
      } catch (Exception e) {
      }
    }
  }

  public OperationStatus distributeModuleStreams(Target[] targets,
      InputStream moduleArchive, InputStream deploymentPlan) throws Exception {
    ProgressObject progress = null;
    try {
      progress = getDeploymentManager().distribute(targets, moduleArchive,
          deploymentPlan);
    } catch (Exception e) {
      e.printStackTrace();
      if (progress.isCancelSupported()) {
        progress.cancel();
      }
      throw e;
    }
    OperationStatus result = commandStatus(targets, progress);
    delay();
    return result;
  }

  public OperationStatus distributeModuleFiles(Target[] targets,
      File moduleArchive, File deploymentPlan) throws Exception {
    ProgressObject progress = null;
    try {
      progress = getDeploymentManager().distribute(targets, moduleArchive,
          deploymentPlan);
    } catch (Exception e) {
      e.printStackTrace();
      if (progress.isCancelSupported()) {
        progress.cancel();
      }
      throw e;
    }
    OperationStatus result = commandStatus(targets, progress);
    delay();
    return result;
  }

  public Target[] getTargets() throws Exception {
    Target[] result = null;
    result = getDeploymentManager().getTargets();
    return result;
  }

  public OperationStatus stopModule(TargetModuleID[] modulesIDsToStop)
      throws Exception {
    ProgressObject progress = null;
    try {
      progress = getDeploymentManager().stop(modulesIDsToStop);
    } catch (Exception e) {
      e.printStackTrace();
      // throw e;
    }
    return commandStatus(convertTargets(modulesIDsToStop), progress);
  }

  public OperationStatus startModule(TargetModuleID[] modulesIDsToStart)
      throws Exception {
    ProgressObject progress = null;
    try {
      progress = getDeploymentManager().start(modulesIDsToStart);
    } catch (Exception e) {
      e.printStackTrace();
      // throw e;
    }
    return commandStatus(convertTargets(modulesIDsToStart), progress);
  }

  public OperationStatus undeployModule(TargetModuleID[] modulesIDsToUndeploy)
      throws Exception {
    ProgressObject progress = null;
    try {
      progress = getDeploymentManager().undeploy(modulesIDsToUndeploy);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    OperationStatus result = commandStatus(convertTargets(modulesIDsToUndeploy),
        progress);
    delay();
    return result;
  }

  private void delay() {
    try {
      int delayTime = Integer
          .parseInt(System.getProperty("delay.after.deploy", "0"));
      if (delayTime != 0) {
        System.err.println("%%%%%%%%% SLEEPING FOR " + delayTime + " SECONDS");
        Thread.sleep(delayTime * 1000);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Target[] convertTargets(TargetModuleID[] targets) {
    if (targets == null) {
      return null;
    }
    Target[] result = new Target[targets.length];
    for (int i = 0; i < targets.length; i++) {
      result[i] = targets[i].getTarget();
    }
    return result;
  }

  //////
  // Porting package 1.4 additions
  //////

}

class ProgressHandler extends Thread implements ProgressListener {
  volatile StateType finalState = null;

  public void run() {
    while (finalState == null) {
      Thread.currentThread().yield();
    }
  }

  public void handleProgressEvent(ProgressEvent event) {
    DeploymentStatus ds = event.getDeploymentStatus();
    System.out.println(ds.getMessage() + "\n");
    if (ds.getState().getValue() != StateType.RUNNING.getValue()) {
      finalState = ds.getState();
    }
  }

  public StateType getCompletionState() {
    return finalState;
  }
}
