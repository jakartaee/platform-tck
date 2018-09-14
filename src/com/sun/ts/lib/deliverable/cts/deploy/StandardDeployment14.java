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

import javax.enterprise.deploy.spi.*;
import javax.enterprise.deploy.shared.*;

import java.io.*;
import java.util.*;
import com.sun.ts.lib.implementation.sun.javaee.runtime.SunRIDeploymentInfo;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.deliverable.*;
import com.sun.ts.lib.harness.ExecutionMode;

/**
 *
 * This class implements the TSDeploymentInterface. It does so by using the 88
 * API's. As of J2EE 1.4, this class should be the standard implementation of
 * TSDeploymentInterface. There will be no need of alternative implentations.
 * All implementation specific code now exists in TSDeploymentInterface2.
 *
 * @author Kyle Grucci
 * @author Anand Dhingra
 *
 */
public class StandardDeployment14 implements TSDeploymentInterface {

  protected static final String DEPLOYED_MODULES_FILE = "ts-deployed-modules";

  protected static String TEMP_DIR;

  protected PrintWriter log; // Log to harness

  protected int iPortingSet = 1; // Porting set using this impl
                                 // of TSDeploymentInterface

  private String url = "deployer:???:???:999"; // URI for DM

  private DeploymentManager depMgr; // for Deployment to manager 1

  private DeployTestUtil dtu; // for Deployment to manager 1

  private DMProps dmProps; // get these from Propmgr

  protected TSDeploymentInterface2 dep2; // our impl of
  // TSDeploymentInterface2

  protected Hashtable htDeployedModules = // Map ear files to IDs so we
      new Hashtable(); // can later undeploy

  protected PropertyManagerInterface propMgr; // Properties from ts.jte

  protected String sDepNumber = null; // Module ID

  protected String deployStateFile;

  /**
   *
   * Static initialization of this class
   *
   */
  static {
    TestUtil.initJavaTest();
    TEMP_DIR = System.getProperty("java.io.tmpdir");
    if (TEMP_DIR != null && TEMP_DIR.endsWith(File.separator)) {
      TEMP_DIR = TEMP_DIR.substring(0, TEMP_DIR.length() - 1);
    }
  }

  /**
   *
   * Initializes logging output, gets the Deliverable instance
   *
   * @param writer - PrintWriter for harness tracing
   *
   */
  public void init(PrintWriter writer) {

    this.log = writer;
    TestUtil.logHarness("StandardDeployment14.init()");

    iPortingSet = TSDeployment.iPortingSet;
    TestUtil
        .logHarness("StandardDeployment14:  Using porting set #" + iPortingSet);

    try {
      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();

      try {
        String portingString = String.valueOf(iPortingSet);
        initDeployTestUtils(portingString);
        deployStateFile = TEMP_DIR + File.separator + DEPLOYED_MODULES_FILE
            + "_" + portingString + ".ser";
      } catch (TSDeploymentException e) {
        TestUtil.logHarness("Unable to initialize the deployment utilities.");
      }
      htDeployedModules = getDeployedModules();
      dumpDeployedMods(htDeployedModules);

      TestUtil.logHarnessDebug("Adding shutdown hook for state serialization");

      // Handle shutdown gracefully.
      Runtime.getRuntime().addShutdownHook(new Thread() {

        // When the VM shuts down this method is invoked to undeploy
        // all currently deployed applications. Note, this method is
        // invoked when a user issues a ctrl-c or the VM shuts down
        // normally. No matter which way the VM shuts down, this
        // method will honor the harness execute mode specified by
        // the user in the ts.jte file (harness.executeMode property).
        public void run() {

          int harnessExecMode = ExecutionMode.getExecutionMode(propMgr);

          // immediately return if we are running in a mode
          // where undeploy is not required.
          if (harnessExecMode == ExecutionMode.DEPLOY
              || harnessExecMode == ExecutionMode.DEPLOY_RUN) {
            try {
              TestUtil.logHarness("IN SHUTDOWN HOOK BEFORE WRITE");
              dumpDeployedMods(htDeployedModules);
              writeMap(htDeployedModules);
            } catch (IOException ioe) {
              ioe.printStackTrace();
            }
            return;
          } else if (harnessExecMode == ExecutionMode.RUN) {
            return;
          }
          TestUtil.logHarness(
              "IN SHUTDOWNHOOK state file is : \"" + deployStateFile + "\"");
          dumpDeployedMods(htDeployedModules);
          Map deployedModules = (Map) htDeployedModules.clone();
          if (deployedModules.size() != 0) {
            TestUtil.logHarness("Shutdown requested during test run."
                + "  Undeploying previously deployed applications...");

            try {
              initDeployTestUtils(sDepNumber);
            } catch (TSDeploymentException e) {
              TestUtil.logHarness("Unable to initialize the deployment"
                  + " utilities.  Applications will not be undeployed.");
            }

            for (Iterator i = deployedModules.keySet().iterator(); i
                .hasNext();) {

              try {
                undeploy((String) i.next());
              } catch (TSDeploymentException tde) {
                TestUtil.logHarness("Unexpected exception while"
                    + " undeploying application during shutdown. " + "Cause: "
                    + tde.getMessage());
              }

            }
            // Release deployment manager in case we get killed
            // We'll also do this with the deploy and undeploy methods
            // since this will only be called if a shutdown is received
            dtu.releaseDeploymentManager();
            deleteDeployedModulesState(); // remove deployed module state
          }
        }
      });
      String portClass = "porting.ts.deploy2.class." + iPortingSet;
      TestUtil.logHarness("Using " + portClass);
      dep2 = TSDeployment2.getDeploymentInstance(log, portClass);
    } catch (Exception e) {
      e.printStackTrace();
      TestUtil.logHarness(
          "Creation of TSDeployment2 implementation instance failed."
              + " Please check the values of 'porting.ts.deploy2.class.1' and"
              + " 'porting.ts.deploy2.class.2'");
    }
  }

  public String deploy(DeploymentInfo info) throws TSDeploymentException {
    try {
      String sArchive = info.getEarFile();
      // we need to pass the deployinfo to the new porting package to get the
      // plan. For now just use the runtime file
      String[] sRunTimeFileArray = info.getRuntimeFiles();

      TestUtil.logHarness("StandardDeployment14.deploy()");
      sDepNumber = info.getProperty("deployment.props.number");
      initDeployTestUtils(sDepNumber);

      // start distribute here
      TestUtil.logHarness("Starting to distribute:  " + sArchive);

      File earfile = new File(sArchive);
      Object plan = dep2.getDeploymentPlan(info);

      TestUtil.logHarness("file: " + earfile.toString());

      if (plan != null)
        TestUtil.logHarness("plan: " + plan.toString());
      else
        TestUtil.logHarness("No deployment plan for this archive.");

      Target[] targets = dep2.getTargetsToUse(dtu.getTargets(), info);
      if (targets == null || targets.length == 0)
        throw new TSDeploymentException("Empty Target List: ");

      OperationStatus status;

      if (plan instanceof InputStream) {
        // distribute the module
        status = dtu.distributeModuleStreams(targets,
            new FileInputStream(earfile), (InputStream) plan);
      } else if ((plan instanceof File) || (plan == null)) {
        // distribute the module
        status = dtu.distributeModuleFiles(targets, earfile, (File) plan);
      } else {
        throw new TSDeploymentException(
            "Object returned from getDeploymentPlan must return either an InputStream or a File."
                + "  May also be null in some cases like connectors.");
      }

      if (status.isFailed()) {
        throw new TSDeploymentException(
            "Distribute to one or more targets failed " + status.errMessage());
      }

      TestUtil.logHarness(
          "$$$$$$$$$$ Deployment SUCCEEDED for \"" + earfile + "\"");

      // Allow the licensee an opportunity to examine the returned
      // ProgressObject and
      // take any vendor specifc actions that may be necessary.
      dep2.postDistribute(status.getProgressObject());

      status = dtu.startModule(status.getDeployedTargetIDs());
      if (status.isFailed()) {
        throw new TSDeploymentException(
            "Starting of module failed on one or more targets "
                + status.errMessage());
      }

      // Allow the licensee an opportunity to examine the returned
      // ProgressObject and
      // take any vendor specifc actions that may be necessary.
      dep2.postStart(status.getProgressObject());

      // keep track of filename to TargetmoduleIDs mapping
      htDeployedModules.put(getAppName(sArchive),
          status.getDeployedTargetIDs());

      // Check to see if we're in a test directory that contains
      // embedded rars
      if (sArchive.indexOf("connector" + File.separator + "deployment"
          + File.separator + "ejb_Deployment.ear") != -1) {

        Properties p = new Properties();
        p.setProperty("rar_file", sArchive);
        dep2.createConnectionFactory(status.getDeployedTargetIDs(), p);
      }

      if (sArchive.indexOf("xa" + File.separator + "ee" + File.separator + "tsr"
          + File.separator + "ejb_Tsr.ear") != -1) {

        Properties p = new Properties();
        p.setProperty("rar_file", sArchive);
        dep2.createConnectionFactory(status.getDeployedTargetIDs(), p);
      }

      // if we have a rar file embedded in the ear, then we need to create
      // connection factories - needsome naming we can rely on - don't want
      // to open every ear file to check
      String javaeeLevel = propMgr.getProperty("javaee.level", "full");

      if (javaeeLevel.contains("full") && !sArchive.endsWith(".rar")) {
        return dep2.getClientClassPath(status.getDeployedTargetIDs(), info,
            dtu.getDeploymentManager());
      } else {
        return "";
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw (TSDeploymentException) new TSDeploymentException(
          "Deployment Failed.").initCause(e);
    }
  }

  public void undeploy(String moduleId) throws TSDeploymentException {
    ModuleType moduleType = getModuleType(moduleId);
    TargetModuleID[] idsToUndeploy = (TargetModuleID[]) htDeployedModules
        .get(getAppName(moduleId));

    TestUtil.logHarnessDebug(
        "$$$$$$$$$$$$$ idsToUndeploy.length = " + idsToUndeploy.length);
    TestUtil.logHarnessDebug("$$$$$$$$$$$$$ Undeploying Module ID \""
        + idsToUndeploy[0].getModuleID() + "\"");

    if (idsToUndeploy == null) {
      TestUtil.logHarness(
          "idToUndeploy is null.  Assuming that the module is not currently deployed");
      return;
    } else {
      TestUtil.logHarness("Undeploying module \"" + moduleId + "\"");
    }

    OperationStatus status = null;
    try {
      status = dtu.stopModule(idsToUndeploy);
      if (status.isFailed()) {
        // log a message but still try to undeploy all the modules
        TestUtil.logHarness(
            "Stop failed for one or more targets while undeploying module \""
                + moduleId + "\"");
      }

      // Allow the licensee an opportunity to examine the returned
      // ProgressObject and
      // take any vendor specifc actions that may be necessary.
      dep2.postStop(status.getProgressObject());

      status = dtu.undeployModule(idsToUndeploy);
      if (status.isFailed()) {
        TestUtil.logHarness("Undeploy failed for one or more targets \""
            + status.errMessage() + "\"");
      }

      // Allow the licensee an opportunity to examine the returned
      // ProgressObject and
      // take any vendor specifc actions that may be necessary.
      dep2.postUndeploy(status.getProgressObject());

      htDeployedModules.remove(getAppName(moduleId));
    } catch (Exception e) {
      e.printStackTrace();
      throw new TSDeploymentException("Error while undeploying");
    }
    if (status.isFailed()) {
      throw new TSDeploymentException(
          "Undeploy failed for one or more targets \"" + status.errMessage()
              + "\"");
    }
  }

  public void undeploy(Properties p) throws TSDeploymentException {
    String sArchive = p.getProperty("ear_file");
    TestUtil.logHarness("StandardDeployment14.undeploy()");
    initDeployTestUtils(p.getProperty("deployment.props.number"));

    // Check to see if we're in a test directory that contains
    // embedded rars
    if (sArchive
        .indexOf("connector" + File.separator + "deployment" + File.separator
            + "ejb_Deployment.ear") != -1
        || sArchive.indexOf("xa" + File.separator + "ee" + File.separator
            + "tsr" + File.separator + "ejb_Tsr.ear") != -1) {
      // we must have an embedded ra file in this ear
      // we need to remove the connection factory
      p.setProperty("rar_file", sArchive);
      // call into new porting package to remove connection factories
      TargetModuleID[] modulesToUndeploy = (TargetModuleID[]) (htDeployedModules
          .get(getAppName(sArchive)));
      if (modulesToUndeploy.length == 0) {
        TestUtil
            .logHarness("undeploy failed for application \"" + sArchive + "\"");
        throw new TSDeploymentException(
            "undeploy failed for application \"" + sArchive + "\"");
      }
      dep2.removeConnectionFactory(modulesToUndeploy, p);
    }
    this.undeploy(sArchive);
  }

  public boolean isDeployed(Properties p) throws TSDeploymentException {
    TestUtil.logHarness("StandardDeployment14.isDeployed()");
    String sArchive = p.getProperty("ear_file");
    String sAppName = getAppName(sArchive);
    // For now just assume that we started with clean servers
    // and check our internal state hashtable
    if (htDeployedModules == null) {
      return false;
    }

    // resync the list of deployed modules with what the targets actually report
    try {
      normalizeMap(getTargets());
    } catch (Exception e) {
      e.printStackTrace();
    }

    TargetModuleID[] id = (TargetModuleID[]) htDeployedModules.get(sAppName);
    if (id != null && id.length > 0) {
      TestUtil.logHarnessDebug("StandardDeployment14.isDeployed():  "
          + "After checking hashtable, id = " + sAppName);
      return true;
    } else {
      TestUtil.logHarnessDebug("StandardDeployment14.isDeployed():  "
          + "After checking hashtable, id = null");
      return false;
    }
  }

  public void deployConnector(Properties p) throws TSDeploymentException {
    TestUtil.logHarness("StandardDeployment14.deployConnector()");
    String sRarFileName = p.getProperty("rar_file");
    DeploymentInfo info = null;
    try {
      info = new SunRIDeploymentInfo(sRarFileName, new String[] {});
      info.setProperty("deployment.props.number",
          p.getProperty("deployment.props.number"));
      deploy(info); // adds deployed targets IDs to htDeployedModules
      TargetModuleID[] targets = (TargetModuleID[]) (htDeployedModules
          .get(getAppName(sRarFileName)));
      dep2.createConnectionFactory(targets, p);
    } catch (Exception e) {
      e.printStackTrace();
      throw new TSDeploymentException("Error while deploying Connector");
    }

    // call into new porting package to create connection factories
    // If rar files now have runtime info, we will have to change the harness
    // code
    // to call deploy if we are using the StandardDeployment class. Our deploy
    // method in here can check for rars and handle the connection factory
    // logic.
    // If old porting impls are used, then the harness wil still call these
    // connector methods.
  }

  public void undeployConnector(Properties p) throws TSDeploymentException {
    TestUtil.logHarness("StandardDeployment14.undeployConnector()");

    if (isConnectorDeployed(p)) {
      String rarFile = p.getProperty("rar_file");
      // targets must have a non-empty list of TargetModuleIDs since
      // isConnectorDeployed just checked it.
      TargetModuleID[] targets = (TargetModuleID[]) (htDeployedModules
          .get(getAppName(rarFile)));
      dep2.removeConnectionFactory(targets, p);
      p.setProperty("ear_file", rarFile);
      undeploy(p);
    } else {
      TestUtil.logHarness(
          "StandardDeployment14.undeployConnector() - " + "not undeploying.");
    }
  }

  public boolean isConnectorDeployed(Properties p)
      throws TSDeploymentException {
    TestUtil.logHarness("StandardDeployment14.isConnectorDeployed()");
    p.setProperty("ear_file", p.getProperty("rar_file"));
    return isDeployed(p);
  }

  protected void initDeployTestUtils(String sPropNum)
      throws TSDeploymentException {
    try {
      if (dtu != null) {
        return;
      }
      String sJar = propMgr.getProperty("deployManagerJarFile." + sPropNum);
      String sUri = propMgr.getProperty("deployManageruri." + sPropNum);
      String sUname = propMgr.getProperty("deployManageruname." + sPropNum);
      String sPassword = propMgr.getProperty("deployManagerpasswd." + sPropNum);

      TestUtil.logHarnessDebug(
          "StandardDeployment14.initDeployTestUtils() + sPropNum = "
              + sPropNum);
      TestUtil.logHarnessDebug("deployManagerJarFile:  " + sJar);
      TestUtil.logHarnessDebug("deployManageruri:  " + sUri);
      TestUtil.logHarnessDebug("deployManageruname:  " + sUname);
      TestUtil.logHarnessDebug("deployManagerpasswd:  " + sPassword);

      // Construct properties object for DM
      dmProps = new DMProps(sJar, sUri, sUname, sPassword);

      // Utility object for getting depMgr's
      // dtu = DeployTestUtil.getDeployTestUtil(dmProps);
      dtu = new DeployTestUtil(dmProps);

    } catch (Exception e) {
      e.printStackTrace();
      throw new TSDeploymentException(
          "Failed to get DeployTestUtil: " + e.getMessage());
    }

    // Construct the DM
    // always initialize these in case props change in same VM of JavaTest
    try {
      depMgr = dtu.getDeploymentManager();

    } catch (Exception e) {
      e.printStackTrace();
      TestUtil.logHarness(
          "Exception loading DeploymentFactoryManager factories: " + e);
      throw new TSDeploymentException(
          "Unable to get DeploymentManager: " + e.getMessage());
    }
  }

  public String getAppClientArgs(Properties p) {
    return dep2.getAppClientArgs(p);
  }

  public Hashtable getInteropJNDINames(DeploymentInfo[] infoArray) {
    return dep2.getDependentValues(infoArray);
  }

  private static ModuleType getModuleType(String sArchive) {
    String sExtension = sArchive.substring(sArchive.lastIndexOf(".") + 1);
    ModuleType mt;

    if (sExtension.equalsIgnoreCase("ear"))
      mt = ModuleType.EAR;
    else if (sExtension.equalsIgnoreCase("rar"))
      mt = ModuleType.RAR;
    else if (sExtension.equalsIgnoreCase("jar")) {
      if (sArchive.indexOf("component") != -1)
        mt = ModuleType.EJB;
      else
        mt = ModuleType.EJB; // Should this be CAR ?
    } else if (sExtension.equalsIgnoreCase("war"))
      mt = ModuleType.WAR;
    else
      mt = ModuleType.EAR;

    return mt;
  }

  protected static String getAppName(String path) {
    return path.substring(path.lastIndexOf(File.separator) + 1);
  }

  protected void dumpDeployedMods(Hashtable map) {
    StringBuffer message = new StringBuffer("Deployed Modules: \n");
    if (map == null || map.keySet() == null) {
      TestUtil.logHarness("There are no deployed modules, returning");
      return;
    }
    Iterator iter = map.keySet().iterator();
    while (iter.hasNext()) {
      String modName = (String) iter.next();
      message.append("\t" + modName + "\n");
    }
    TestUtil.logHarness(message.toString());
  }

  protected boolean isValidTargetID(TargetModuleID targModID,
      SerializableTargetID[] serTargModIDs) {
    boolean result = false;
    SerializableTargetID test = new SerializableTargetID(targModID);
    for (int i = 0; i < serTargModIDs.length; i++) {
      if (serTargModIDs[i].equals(test)) {
        result = true;
        break;
      }
    }
    return result;
  }

  protected boolean isValidTargetID(TargetModuleID targModID,
      TargetModuleID[] serTargModIDs) {
    boolean result = false;
    for (int i = 0; i < serTargModIDs.length; i++) {
      if (serTargModIDs[i].equals(targModID)) {
        result = true;
        break;
      }
    }
    return result;
  }

  protected TargetModuleID[] getRunningTargetModuleIDs(String sModuleName,
      Target[] targets) throws Exception {
    return depMgr.getRunningModules(getModuleType(sModuleName), targets);
  }

  /*
   * This method is used to update our local hashtable (htDeployedModules) of
   * deployed modules with the latest information from the targets that we've
   * deployed them to.
   */
  protected void normalizeMap(Target[] targets) throws Exception {
    List targModIDsList = null;

    TestUtil.logHarnessDebug("Target[]");
    for (int i = 0; i < targets.length; i++) {
      TestUtil.logHarnessDebug("[" + targets[i].getDescription() + ", "
          + targets[i].getName() + "]");
    }

    Hashtable result = new Hashtable();
    Iterator iter = htDeployedModules.keySet().iterator();
    while (iter.hasNext()) {
      String modName = (String) iter.next();
      TargetModuleID[] targModIDs = getRunningTargetModuleIDs(modName, targets);
      if (targModIDs == null || targModIDs.length == 0) {
        continue;
      }
      TestUtil.logHarnessDebug("****** Module name = \"" + modName + "\"");
      TestUtil
          .logHarnessDebug("****** targModIDs.length = " + targModIDs.length);
      TestUtil.logHarnessDebug("TargetModuleID[]");
      for (int i = 0; i < targModIDs.length; i++) {
        TestUtil.logHarnessDebug("[" + targModIDs[i].getModuleID() + ", "
            + targModIDs[i].getTarget().getDescription() + ", "
            + targModIDs[i].getTarget().getName() + "]");
      }

      TargetModuleID[] savedIDs = (TargetModuleID[]) htDeployedModules
          .get(modName);

      TestUtil.logHarnessDebug("****** savedIDs.length = " + savedIDs.length);
      TestUtil.logHarnessDebug("TargetModuleID[]");
      for (int i = 0; i < savedIDs.length; i++) {
        TestUtil.logHarnessDebug("[" + savedIDs[i].getModuleID() + "]");
      }

      targModIDsList = new ArrayList();
      for (int i = 0; i < targModIDs.length; i++) {
        if (isValidTargetID(targModIDs[i], savedIDs)) {
          TestUtil.logHarnessDebug(
              "&&&&&&&& Adding = " + targModIDs[i].getModuleID());
          targModIDsList.add(targModIDs[i]);
          TestUtil.logHarnessDebug(
              "&&&&&&&& targModIDsList.size() is " + targModIDsList.size());
          break;
        }
      }
      if (targModIDsList.size() > 0) {
        TargetModuleID[] validSavedIDs = (TargetModuleID[]) (targModIDsList
            .toArray(new TargetModuleID[targModIDsList.size()]));
        result.put(modName, validSavedIDs);
      } else {
        TestUtil.logHarnessDebug(
            "$$$$$ We did not find any valid IDs :  " + modName);
      }
    }
    TestUtil.logHarnessDebug("DUMP OF RESULT");
    dumpDeployedMods(result);
    TestUtil.logHarnessDebug("END DUMP OF RESULT END");
    htDeployedModules = result;
  }

  // Read
  protected Hashtable normalizeMap(Hashtable map, Target[] targets)
      throws Exception {
    List targModIDsList = null;

    TestUtil.logHarnessDebug("Target[]");
    for (int i = 0; i < targets.length; i++) {
      TestUtil.logHarnessDebug("[" + targets[i].getDescription() + ", "
          + targets[i].getName() + "]");
    }

    Hashtable result = new Hashtable();
    Iterator iter = map.keySet().iterator();
    while (iter.hasNext()) {
      String modName = (String) iter.next();
      TargetModuleID[] targModIDs = getRunningTargetModuleIDs(modName, targets);
      if (targModIDs == null || targModIDs.length == 0) {
        continue;
      }
      TestUtil.logHarnessDebug("****** Module name = \"" + modName + "\"");
      TestUtil
          .logHarnessDebug("****** targModIDs.length = " + targModIDs.length);
      TestUtil.logHarnessDebug("TargetModuleID[]");
      for (int i = 0; i < targModIDs.length; i++) {
        TestUtil.logHarnessDebug("[" + targModIDs[i].getModuleID() + ", "
            + targModIDs[i].getTarget().getDescription() + ", "
            + targModIDs[i].getTarget().getName() + "]");
      }

      SerializableTargetID[] serIDs = (SerializableTargetID[]) map.get(modName);

      TestUtil.logHarnessDebug("****** serIDs.length = " + serIDs.length);
      TestUtil.logHarnessDebug("SerializableTargetModuleID[]");
      for (int i = 0; i < serIDs.length; i++) {
        TestUtil.logHarnessDebug("[" + serIDs[i].getModuleID() + "]");
      }

      targModIDsList = new ArrayList();
      for (int i = 0; i < targModIDs.length; i++) {
        if (isValidTargetID(targModIDs[i], serIDs)) {
          TestUtil.logHarnessDebug(
              "&&&&&&&& Adding = " + targModIDs[i].getModuleID());
          targModIDsList.add(targModIDs[i]);
          TestUtil.logHarnessDebug(
              "&&&&&&&& targModIDsList.size() is " + targModIDsList.size());
          break;
        }
      }
      if (targModIDsList.size() > 0) {
        TargetModuleID[] validSerIDs = (TargetModuleID[]) (targModIDsList
            .toArray(new TargetModuleID[targModIDsList.size()]));
        result.put(modName, validSerIDs);
      }
    }
    TestUtil.logHarnessDebug("DUMP OF RESULT");
    dumpDeployedMods(result);
    TestUtil.logHarnessDebug("END DUMP OF RESULT END");
    return result;
  }

  // Write
  protected Hashtable normalizeMap(Hashtable map) {
    Hashtable result = new Hashtable();
    Iterator iter = map.keySet().iterator();
    while (iter.hasNext()) {
      String key = (String) iter.next();
      TargetModuleID[] tmid = (TargetModuleID[]) map.get(key);
      SerializableTargetID[] serIDs = new SerializableTargetID[tmid.length];
      for (int i = 0; i < tmid.length; i++) {
        serIDs[i] = new SerializableTargetID(tmid[i]);
      }
      result.put(key, serIDs);
    }
    return result;
  }

  protected void writeMap(Hashtable map) throws IOException {
    deleteDeployedModulesState();
    ObjectOutputStream oout = null;
    Hashtable newMap = normalizeMap(map);
    try {
      oout = new ObjectOutputStream(new FileOutputStream(deployStateFile));
      oout.writeObject(newMap);
    } catch (IOException e) {
      throw e;
    } finally {
      try {
        oout.close();
      } catch (Exception ee) {
      }
    }
  }

  protected Hashtable readMap() throws IOException {
    Hashtable result = null;
    File inFile = new File(deployStateFile);
    if (inFile.isFile()) {
      ObjectInputStream oin = null;
      try {
        oin = new ObjectInputStream(new FileInputStream(deployStateFile));
        result = (Hashtable) (oin.readObject());
      } catch (IOException e) {
        throw e;
      } catch (ClassNotFoundException cnfe) {
        cnfe.printStackTrace();
      } finally {
        try {
          oin.close();
        } catch (Exception ee) {
        }
      }
    }
    return result;
  }

  protected Hashtable getDeployedModules() {
    Hashtable result = null;
    Hashtable serMap = null;
    try {
      serMap = readMap();
    } catch (IOException e) {
    }
    if (serMap == null) {
      result = new Hashtable();
    } else {
      try {
        result = normalizeMap(serMap, getTargets());
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  protected void deleteDeployedModulesState() {
    try {
      File removeMe = new File(
          TEMP_DIR + File.separator + DEPLOYED_MODULES_FILE);
      removeMe.delete();
    } catch (Exception e) {
    }
  }

  protected Target[] getTargets() {
    // get the targets
    return depMgr.getTargets();
  }

  protected static class SerializableTarget implements Serializable {
    private String description;

    private String name;

    public SerializableTarget(Target target) {
      this.description = target.getDescription();
      this.name = target.getName();
    }

    public String getDescription() {
      return description;
    }

    public String getName() {
      return name;
    }

    public boolean equals(Object obj) {
      SerializableTarget that = null;
      boolean result = false;
      if (this instanceof SerializableTarget) {
        that = (SerializableTarget) obj;
        result = (description == null) ? that.getDescription() == null
            : description.equals(that.getDescription());
        result = result && ((name == null) ? that.getName() == null
            : name.equals(that.getName()));
      }
      return result;
    }
  }

  protected static class SerializableTargetID implements Serializable {
    private SerializableTarget target;

    private String moduleID;

    public SerializableTargetID(TargetModuleID id) {
      this.moduleID = id.getModuleID();
      this.target = new SerializableTarget(id.getTarget());
    }

    public SerializableTarget getTarget() {
      return target;
    }

    public String getModuleID() {
      return moduleID;
    }

    public boolean equals(Object obj) {
      boolean result = false;
      SerializableTargetID that = null;
      if (obj instanceof SerializableTargetID) {
        that = (SerializableTargetID) obj;
        result = (moduleID == null) ? that.getModuleID() == null
            : moduleID.equals(that.getModuleID());
        result = result && ((target == null) ? that.getTarget() == null
            : target.equals(that.getTarget()));
      }
      return result;
    }
  }

}
