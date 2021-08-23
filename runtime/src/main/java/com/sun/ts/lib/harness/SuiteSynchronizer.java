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

package com.sun.ts.lib.harness;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import com.sun.ts.lib.deliverable.DeliverableFactory;
import com.sun.ts.lib.deliverable.DeliverableInterface;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.deliverable.PropertyNotSetException;
import com.sun.ts.lib.porting.DeploymentInfo;
import com.sun.ts.lib.porting.TSDeployment;
import com.sun.ts.lib.porting.TSDeploymentException;
import com.sun.ts.lib.porting.TSDeploymentInterface;
import com.sun.ts.lib.porting.TSJMSAdmin;
import com.sun.ts.lib.porting.TSJMSAdminException;
import com.sun.ts.lib.porting.TSJMSAdminInterface;
import com.sun.ts.lib.util.TestUtil;

public class SuiteSynchronizer {
  PrintWriter logOut;

  private static String sClientClassesPath = "";

  private static String sCommonClientClassesPath = "";

  private static String sLastTestDirectory = "none run yet";

  private static String sInteropDirectionWhenTablesWerePopulated = "forward";

  private static Hashtable htTSDeployers = new Hashtable();

  private static Hashtable htTSJMSAdmins = new Hashtable();

  private static Hashtable htJNDIMapsFromServer1 = new Hashtable();

  private static Hashtable htJNDIMapsFromServer2 = new Hashtable();

  private static TSRuntimeConfiguration runtimeConfig;

  String testDir = "";

  Properties pDeployProps;

  private boolean bSweepRebuildableReverseRuntimeFiles;

  private static SuiteSynchronizer ss;

  private PropertyManagerInterface jteMgr;

  private static Vector vCommonAppsDeployedThisJVM = new Vector();

  private static String sDeployStatus = "passed";

  private static String sDeployStackTrace = "";

  private static String[] sTopicFactories1;

  private static String[] sQueueFactories1;

  private static String[] sTopicFactories2;

  private static String[] sQueueFactories2;

  private static boolean bCreateConnectionFactories1 = true;

  private static boolean bCreateConnectionFactories2 = true;

  private static boolean bReversed;

  private int executionMode;

  private static Vector vCommonDeploymentInfos = new Vector();

  private static Vector vCurrentDeploymentInfos = new Vector();

  private static boolean bDeployingCommonApps;

  // deliverable specific settings
  // All info here is retrieved from the active deliverable
  private static DeliverableInterface deliv;

  private static boolean bSupportsAutoDeploy = true;

  private static boolean bSupportsAutoJMS = true;

  private static boolean bSupportsInterop = true;

  public static final SuiteSynchronizer createSuiteSynchronizer(
      PrintWriter writer) {
    ss = new SuiteSynchronizer(writer);
    return ss;
  }

  public static final SuiteSynchronizer getSuiteSynchronizer(
      PrintWriter writer) {
    if (ss == null)
      ss = new SuiteSynchronizer(writer);
    return ss;
  }

  public SuiteSynchronizer(PrintWriter w) {
    setOutputWriter(w);
    try {
      deliv = DeliverableFactory.getDeliverableInstance();
      bSupportsAutoDeploy = deliv.supportsAutoDeployment();
      bSupportsAutoJMS = deliv.supportsAutoJMSAdmin();
      bSupportsInterop = deliv.supportsInterop();
      jteMgr = deliv.getPropertyManager();
    } catch (Exception e) {
      e.printStackTrace();
    }
    executionMode = ExecutionMode.getExecutionMode(jteMgr);
    init();
  }

  private void init() {
    if (bSupportsAutoDeploy) {
      try {
        // get porting class 1 impls for deployment and JMSAdmin
        if ((htTSDeployers.get("cts1") == null)) {
          htTSDeployers.put("cts1", TSDeployment.getDeploymentInstance(logOut,
              "porting.ts.deploy.class.1"));
        }
        if (bSupportsAutoJMS && (htTSJMSAdmins.get("cts1") == null)) {
          htTSJMSAdmins.put("cts1", TSJMSAdmin.getTSJMSAdminInstance(logOut,
              "porting.ts.jms.class.1"));
        }
        if (bSupportsInterop) {
          // get porting class 2 impls for deployment and JMSAdmin
          if (htTSDeployers.get("cts2") == null) {
            htTSDeployers.put("cts2", TSDeployment.getDeploymentInstance(logOut,
                "porting.ts.deploy.class.2"));
          }
          if (bSupportsAutoJMS && (htTSJMSAdmins.get("cts2") == null)) {
            htTSJMSAdmins.put("cts2", TSJMSAdmin.getTSJMSAdminInstance(logOut,
                "porting.ts.jms.class.2"));
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
        TestUtil.logHarness(
            "Creation of TSDeployment or TSJMSAdmin implementation instances failed.  Please check the values of 'porting.ts.deploy.class.1', 'porting.ts.deploy.class.2', 'porting.ts.jms.class.1' and 'porting.ts.jms.class.2'");
      }
      try {
        runtimeConfig = new TSRuntimeConfiguration(logOut);
        TestUtil.logHarness("ss:  modified runtime.xml");
      } catch (Exception e) {
        e.printStackTrace();
        TestUtil.logHarness(
            "Failed to modify xml files with correct settings.  Please check the values of 'mailHost, mailFrom, webServerHost, and webServerPort'");
      }
    }
  }

  public void setOutputWriter(PrintWriter w) {
    logOut = w;
  }

  public TSDeploymentInterface getTSDeployer(String sDeployerToGet)
      throws TSDeploymentException {
    return (TSDeploymentInterface) htTSDeployers.get(sDeployerToGet);
  }

  public String getDeploymentStatus() {
    return sDeployStatus;
  }

  public String getDeploymentExceptionTrace() {
    return sDeployStackTrace;
  }

  public String getClientClassPath() {
    return sCommonClientClassesPath + sClientClassesPath;
  }

  public void doCommonUnDeployment() throws TSDeploymentException {
    String sVal = null;
    pDeployProps = new Properties();
    // reset the array of DeploymentInfos from the last test run
    vCommonDeploymentInfos.clear();
    if (!vCommonAppsDeployedThisJVM.isEmpty()) {
      for (int ii = 0; ii < vCommonAppsDeployedThisJVM.size(); ii++) {
        TestUtil.logHarness("*******************************");
        TestUtil.logHarness(
            "Beginning one-time Undeployment of any TS common applications...");
        TestUtil.logHarness("*******************************");
        // undeploy apps here
        testDir = (String) vCommonAppsDeployedThisJVM.elementAt(ii);
        try {
          undeployApps(testDir);
          TestUtil.logHarness("Undeployed common apps in:  " + testDir);
        } catch (TSDeploymentException de) {
          TestUtil.logHarness("*******************************");
          TestUtil.logHarness(
              "Failed to complete TS common application deployment.");
          TestUtil.logHarness("*******************************");
          throw de;
        } catch (Exception e) {
          TestUtil.logHarness("*******************************");
          TestUtil.logHarness(
              "Failed to complete TS common application deployment.");
          TestUtil.logHarness("*******************************");
          throw new TSDeploymentException(
              "Exception:  Failed to Undeploy common apps in " + testDir, e);
        }
      }
      vCommonAppsDeployedThisJVM.clear();
      sCommonClientClassesPath = "";
      TestUtil.logHarness("*******************************");
      TestUtil.logHarness(
          "Successfully completed TS common application Undeployment.");
      TestUtil.logHarness("*******************************");
    }
  }

  public void doDeployment(String testDirectory, Properties props)
      throws TSDeploymentException, TSJMSAdminException {
    pDeployProps = props;
    String sCommonTestDir = "";
    testDir = TestUtil.srcToDist(testDirectory);
    // testDir =
    // TestUtil.srcToDist(TSTestFinder.getAbsolutePath(td.getParameter("test_directory")));
    // just return if we're in the same test directory
    if (sLastTestDirectory.equals(testDir)) {
      sDeployStatus = "passed";
      return;
    }
    // reset current test directory's info object to only include previously
    // deployed common apps
    vCurrentDeploymentInfos.clear();
    vCurrentDeploymentInfos.addAll(vCommonDeploymentInfos);
    String sDeployCommon = jteMgr.getProperty("deploy_undeploy_common_apps",
        "true");
    if (TestUtil.harnessDebug)
      TestUtil
          .logHarnessDebug("deploy_undeploy_common_apps = " + sDeployCommon);
    if (sDeployCommon.equalsIgnoreCase("true")) {
      // check to see if any common apps are required by this test
      String[] sCommonApps = CommonAppVerifier.getInstance(new File(testDir))
          .getCommonApps();
      // set the state so we can tell later if we are deploying common apps
      bDeployingCommonApps = true;
      // if there are, then see if any are already deployed
      for (int ii = 0; sCommonApps != null && ii < sCommonApps.length; ii++) {
        sCommonTestDir = sCommonApps[ii];
        if (TestUtil.harnessDebug)
          TestUtil.logHarnessDebug("sCommonTestDir = " + sCommonTestDir);
        // if this app has not been deployed yet in this JVM, and it is
        // already deployed, undeploy it and redeploy it
        if (!vCommonAppsDeployedThisJVM.contains(sCommonTestDir)) {
          try {
            undeployApps(sCommonTestDir);
            if (TestUtil.harnessDebug)
              TestUtil.logHarnessDebug(
                  "Undeployed common apps from - " + sCommonTestDir);
          } catch (TSDeploymentException de) {
            sDeployStatus = "failed";
            logOut
                .println("Failed to undeploy common apps in " + sCommonTestDir);
            throw de;
          } catch (Exception e) {
            sDeployStatus = "failed";
            logOut.println(
                "Unexpected Exception:  Failed to undeploy common apps in "
                    + sCommonTestDir);
            throw new TSDeploymentException(
                "Exception:  Failed to undeploy common apps in "
                    + sCommonTestDir,
                e);
          }
          // Hack to add sql Tag to TxECMPbean by adding it to the
          // pDeployProps here if the bean to be deployed is TxECMPBean.
          if (sCommonTestDir.indexOf("txECMPbean") != -1
              || sCommonTestDir.indexOf("txEPMbean") != -1 || sCommonTestDir
                  .indexOf("ejbql" + File.separator + "schema") != -1) {
            if (TestUtil.harnessDebug)
              TestUtil.logHarnessDebug(
                  "Found the TxECMPbean, TxEPMbean, or ejbql/schema common bean.  We must generate SQL ");
            pDeployProps.put("generateSQL", "true");
          }
          try {
            sCommonClientClassesPath += deployApps(sCommonTestDir)
                + File.pathSeparator;
            if (TestUtil.harnessDebug)
              TestUtil.logHarnessDebug(
                  "Deployed common apps from - " + sCommonTestDir);
            // save off which common apps we actually deployed during this JVM
            vCommonAppsDeployedThisJVM.addElement(sCommonTestDir);
          } catch (TSDeploymentException de) {
            sDeployStatus = "failed";
            TestUtil.logHarness("Deployment of common app(s) from:  "
                + sCommonTestDir + " failed!");
            de.printStackTrace();
            sDeployStackTrace = TestUtil.printStackTraceToString(de);
            throw de;
          } catch (TSJMSAdminException je) {
            sDeployStatus = "failed";
            TestUtil.logHarness("JMS Admin topic/queue creation from:  "
                + sCommonTestDir + " failed!");
            je.printStackTrace();
            sDeployStackTrace = TestUtil.printStackTraceToString(je);
            throw je;
          }
        } else {
          if (TestUtil.harnessDebug)
            TestUtil.logHarnessDebug(
                "Common apps from this dir are already deployed during this JVM- "
                    + sCommonTestDir);
        }
      }
    } else {
      if (TestUtil.harnessDebug)
        TestUtil
            .logHarnessDebug("Skipping common app deployment " + sDeployCommon);
    }
    // Now take care of the apps in the current test dir
    if (!Boolean.getBoolean("common.apps.only")
        && !sLastTestDirectory.equals(testDir)) {
      // set the state so we can tell later if we are deploying common apps
      bDeployingCommonApps = false;
      try {
        if (!sLastTestDirectory.equals("none run yet")) {
          // undeploy the last one
          undeployApps(sLastTestDirectory);
          logOut.println("Undeployed apps from - " + sLastTestDirectory);
        }
      } catch (TSDeploymentException de) {
        sDeployStatus = "failed";
        logOut.println("Undeployment of previous apps failed.");
        sDeployStackTrace = TestUtil.printStackTraceToString(de);
        throw de;
      } catch (Exception e) {
        sDeployStatus = "failed";
        logOut.println("Exception:  undeployment of previous apps failed.");
        sDeployStackTrace = TestUtil.printStackTraceToString(e);
        throw new TSDeploymentException(
            "Exception:  Undeployment of previous apps failed", e);
      }
      try {
        // try to undeploy the current one, just in case
        if (!sLastTestDirectory.equals(testDir)) {
          undeployApps(testDir);
          logOut.println("Undeployed apps from - " + testDir);
        }
      } catch (TSDeploymentException de) {
        sDeployStatus = "failed";
        logOut.println("Undeployment of current apps in " + testDir + "failed");
        sDeployStackTrace = TestUtil.printStackTraceToString(de);
        throw de;
      } catch (Exception e) {
        sDeployStatus = "failed";
        logOut.println("Exception:  Undeployment of current apps in " + testDir
            + "failed");
        sDeployStackTrace = TestUtil.printStackTraceToString(e);
        throw new TSDeploymentException(
            "Exception:  Undeployment of current apps in " + testDir + "failed",
            e);
      }
      try {
        sClientClassesPath = deployApps(testDir) + File.pathSeparator;
        logOut.println("Deployed apps from - " + testDir);
        sLastTestDirectory = testDir;
      } catch (TSDeploymentException de) {
        sDeployStatus = "failed";
        TestUtil
            .logHarness("Deployment of app(s) from:  " + testDir + " failed!");
        sDeployStackTrace = TestUtil.printStackTraceToString(de);
        throw de;
      } catch (Throwable e) {
        sDeployStatus = "failed";
        TestUtil.logHarness(
            "Exception:  Deployment of app(s) from:  " + testDir + " failed!");
        e.printStackTrace(logOut);
        sDeployStackTrace = TestUtil.printStackTraceToString(e);
        throw new TSDeploymentException(
            "Deployment of app(s) from:  " + testDir + " failed!", e);
      }
    }
    sDeployStatus = "passed";
  }

  public void removeJmsConnectionFactories() throws TSJMSAdminException {
    // remove JMS connectionFactories
    try {
      if (bSupportsAutoJMS) {
        TSJMSAdminInterface ctsJMS1 = (TSJMSAdminInterface) htTSJMSAdmins
            .get("cts1");
        TSJMSAdminInterface ctsJMS2 = (TSJMSAdminInterface) htTSJMSAdmins
            .get("cts2");
        if (sTopicFactories1 != null) {
          ctsJMS1.removeJmsConnectionFactories(sTopicFactories1);
          sTopicFactories1 = null;
        }
        if (sQueueFactories1 != null) {
          ctsJMS1.removeJmsConnectionFactories(sQueueFactories1);
          sQueueFactories1 = null;
        }
        bCreateConnectionFactories1 = true;
        logOut.println(
            "Successfully removed JMS connection factories from server 1.");
        if (sTopicFactories2 != null) {
          ctsJMS2.removeJmsConnectionFactories(sTopicFactories2);
          sTopicFactories2 = null;
        }
        if (sQueueFactories2 != null) {
          ctsJMS2.removeJmsConnectionFactories(sQueueFactories2);
          sQueueFactories2 = null;
        }
        bCreateConnectionFactories2 = true;
        logOut.println(
            "Successfully removed JMS connection factories from server 2.");
      }
    } catch (Exception e) {
      logOut.println("Exception:  removal of JMS connection factories failed.");
      e.printStackTrace();
      throw new TSJMSAdminException(
          "Exception:  removal of JMS connection factories failed.", e);
    }
  }

  public void undeployLastApp()
      throws TSDeploymentException, TSJMSAdminException {
    try {
      if (!sLastTestDirectory.equals("none run yet")) {
        // undeploy the last app in the test run
        undeployApps(sLastTestDirectory);
        logOut.println("Undeployed apps from - " + sLastTestDirectory);
        // reset this string in case we're running the GUI and we rerun the same
        // tests
        sLastTestDirectory = "none run yet";
      }
    } catch (TSDeploymentException de) {
      logOut.println("Undeployment of last apps failed.");
      throw de;
    } catch (Exception e) {
      logOut.println("Exception:  undeployment of last apps failed.");
      throw new TSDeploymentException(
          "Exception:  Undeployment of last apps failed", e);
    }
  }

  private boolean isInteropDir(String sDir) {
    return (sDir.indexOf("interop") != -1);
  }

  /**
   * Uses different approaches to search for runtime files for j2sdkee RI and
   * for s1as. For j2sdkee RI, iterates through *.runtime.xml, and for each
   * runtime.xml file, checks if it matches a jar entry. For s1as, search for a
   * list of required runtime files for the current deployable component, and
   * tries to find it in the collection of all runtime files in the current dist
   * dir. If not found, tries to get the generic forms from
   * $TS_HOME/src/com/sun/ts/tests/common/sunxml. Available generic forms of
   * s1as runtime files: sun-application.xml sun-web.xml
   * sun-application-client.xml
   * 
   * @param sAppEar
   *          full path of a deployable component, e.g., ear, standalone war/jar
   * @param sRuntimeInfoArray
   *          a collection of runtime files, equals or greater than the valid
   *          runtime files
   * @param sDir
   *          dist dir for the current leaf dir
   * @param oppositeServerJNDITable
   * @param bSweep
   * @return String[] array of file names of valid runtime files
   * @throws TSDeploymentException
   *           TODO: use File/File[] to represent a file or files, instead of
   *           strings. With strings we don't know if they are file name or full
   *           path. TODO: use collection instead of arrays. TODO: remove
   *           sRuntimeInfoArray from param list. Since we already have sDir
   *           (dist dir) in param list, we can include runtime files as needed,
   *           instead of calling File.list() before every invocation of
   *           getValidRuntimeInfoFiles.
   */
  private String[] getValidRuntimeInfoFiles(String sAppEar,
      String[] sRuntimeInfoArray, String sDir,
      Hashtable oppositeServerJNDITable, boolean bSweep)
      throws TSDeploymentException {

    String[] sValidRuntimeInfo = null;
    sValidRuntimeInfo = getS1ASRuntimeInfoFiles(sAppEar, sRuntimeInfoArray,
        sDir);

    // sweep runtime info for both cases: using merged one single runtime, or
    // multiple
    // runtime per deployable component.
    TestUtil.logHarnessDebug("Valid runtime files before sweep:");
    for (int k = 0; k < sValidRuntimeInfo.length; k++) {
      TestUtil.logHarnessDebug(sValidRuntimeInfo[k]);
    }
    int i = 0;
    try {
      for (; i < sValidRuntimeInfo.length; i++) {
        if (bSweep) {
          runtimeConfig.setTable(oppositeServerJNDITable);
          String sTemp = runtimeConfig
              .sweepRuntimeFile(new File(sValidRuntimeInfo[i]));
          if (sTemp != null) {
            sValidRuntimeInfo[i] = sTemp;
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logHarnessDebug(
          "Error while sweeping valid runtime file: " + sValidRuntimeInfo[i]);
      throw new TSDeploymentException(
          "Error while sweeping valid runtime files!", e);
    }
    TestUtil.logHarness("Valid runtime files after sweep:");
    for (int k = 0; k < sValidRuntimeInfo.length; k++) {
      TestUtil.logHarness(sValidRuntimeInfo[k]);
    }

    return sValidRuntimeInfo;
  }

  /**
   * Gets the basename from a path. For example, c:\foo\foo.ear would return
   * foo.ear, c:\foo\foo.bar.ear would return foo.ear, foo.ear returns foo.ear.
   * We could have used File.getName if we have a file instead of a string path.
   * 
   * @param fullPath
   * @return String
   */
  private String getBaseName(String path) {
    int slash = path.lastIndexOf(File.separator);
    // int dot = path.indexOf(".", slash);
    return path.substring(slash + 1);
  }

  /**
   * gets the valid s1as runtime files from the list of all available runtime
   * files in the current test dir. If it uses generic forms of runtime files,
   * like sun-application.xml, sun-web.xml, they will NOT appear in the result.
   * 
   * @param sAppEar
   *          full path of a deployable component, e.g., ear, standalone war/jar
   * @param sRuntimeInfoArray
   *          a collection of runtime files, equals or greater than the valid
   *          runtime files
   * @param sDir
   *          dist dir for the current leaf dir
   * @return String[] a collection of valid s1as runtime files that are really
   *         needed
   */
  private String[] getS1ASRuntimeInfoFiles(String sAppEar,
      String[] sRuntimeInfoArray, String sDir) {
    List al = new ArrayList(7);
    String[] validRuntimeFiles = null;
    String baseName = getBaseName(sAppEar);

    TestUtil
        .logHarness("Search for s1as runtime files match:`" + baseName + "`");

    try {
      if (sAppEar.endsWith(".ear")) { // need sun-application.xml
        boolean isVIBuiltEar = sAppEar.contains("vi_built");
        addIfPresent(baseName, al, sRuntimeInfoArray, isVIBuiltEar);

        JarFile earFile = null;
        try {
          earFile = new JarFile(sAppEar);
          for (Enumeration enum1 = earFile.entries(); enum1
              .hasMoreElements();) {
            ZipEntry infile = (ZipEntry) enum1.nextElement();
            if (!infile.isDirectory()) {
              String name = infile.getName();
              if (name.endsWith(".war") || name.endsWith("_ejb.jar")
                  || name.endsWith("_client.jar")) {
                addIfPresent(name, al, sRuntimeInfoArray, isVIBuiltEar);
              }
            }
          } // for
        } catch (IOException ioe) {
          TestUtil.logHarness(
              "Exception in checking entries in ear file, use all available s1as runtime files.");
          ioe.printStackTrace();
        } finally {
          if (earFile != null)
            earFile.close();
        }
      } else if (sAppEar.endsWith(".jar") || sAppEar.endsWith(".war")) {
        addIfPresent(baseName, al, sRuntimeInfoArray, false);
      } else {
        TestUtil
            .logHarness("Unrecognized standalone component:`" + sAppEar + "`");
      }
    } catch (IOException ex) {
      TestUtil.logHarness(
          "Exception in checking entries in ear file, use all available s1as runtime files.");
      ex.printStackTrace();
      al = Arrays.asList(sRuntimeInfoArray);
    }
    validRuntimeFiles = new String[al.size()];
    for (int i = 0; i < validRuntimeFiles.length; i++) {
      validRuntimeFiles[i] = sDir + File.separator + (String) al.get(i);
    }

    return validRuntimeFiles;
  }

  /**
   * adds fileName to list if the runtimeFileNames[i] stats with moduleName
   * 
   * @param string
   *          moduleName
   * @param al
   * @param runtimeFileNames
   */
  private void addIfPresent(String moduleName, List al,
      String[] runtimeFileNames, boolean isVIBuiltEar) {
    for (int i = 0; i < runtimeFileNames.length; i++) {
      if (runtimeFileNames[i].startsWith(moduleName) || (isVIBuiltEar
          && runtimeFileNames[i].startsWith("vi_built_" + moduleName))) {
        al.add(runtimeFileNames[i]);
        if (TestUtil.harnessDebug) {
          TestUtil.logHarnessDebug("valid runtime file for:" + moduleName + ": "
              + runtimeFileNames[i]);
        }
      }
    }
  }

  // not sure if we should use this
  public void cleanupTempDirectory() {
    try {
      jteMgr = deliv.getPropertyManager();
    } catch (Exception e) {
      e.printStackTrace();
    }
    String sHarnessTempDir = "";
    try {
      sHarnessTempDir = jteMgr.getProperty("harness.temp.directory");
    } catch (PropertyNotSetException pe) {
      TestUtil.logHarness(pe.getMessage());
      TestUtil.logHarness("error looking up property:  harness.temp.directory");
      pe.printStackTrace();
    }
    File fTestDir = new File(sHarnessTempDir);
    File[] fAppJarsArray = fTestDir
        .listFiles(ProfileHelper.ArchiveFilter.getInstance());
    // remove temporary ri .ear files
    for (int ii = 0; ii < fAppJarsArray.length; ii++) {
      fAppJarsArray[ii].delete();
    }
  }

  private String deployApps(String sDir)
      throws TSDeploymentException, TSJMSAdminException {
    String sClientClassPaths = "";
    if (executionMode == ExecutionMode.DEPLOY_RUN_UNDEPLOY
        || executionMode == ExecutionMode.DEPLOY
        || executionMode == ExecutionMode.DEPLOY_RUN) {
      String sInteropDirections = InteropTestManager
          .getInteropDirectionsFromKeywords(sDir);
      File fTestDir = new File(sDir);

      if (TestUtil.harnessDebug)
        TestUtil.logHarnessDebug(
            "deployApps - InteropDirections:  " + sInteropDirections);

      String[] sAppJarsArray = ProfileHelper.getArchives(sDir,
          sInteropDirections);
      String[] sAppJarsForward = ProfileHelper.getArchives(sDir, "forward");
      String[] sAppJarsReverse = ProfileHelper.getArchives(sDir, "reverse");

      if (sInteropDirections.equals("forward")
          || sInteropDirections.equals("both")) {
        swapSettings("forward");

        if (isInteropDir(sDir)) {
          // comment the interop check so that replacement will occur
          // for non-interop also. This is done in the case that we have
          // web service tests which need to find out WSDL location
          // if (isInteropDir(sDir))
          populatePortingReplacementTables(sDir, sAppJarsArray);
          TestUtil.logHarness("Deploying apps...");
          sClientClassPaths += continueToDeployApps(sDir, sAppJarsArray);
        } else {
          // This is only for the case that we are a rebuildable test and the
          // direction
          // is forward or both. In this case, we must swap settings prior to
          // runtime file substitution which happens in
          // populatePortingReplacementTables
          // and then swap back to forward before we actually deploy
          populatePortingReplacementTables(sDir, sAppJarsForward);
          TestUtil.logHarness("Deploying apps for forward rebuildable...");
          sClientClassPaths += continueToDeployApps(sDir, sAppJarsForward);
          if (sInteropDirections.equals("both")) {
            populatePortingReplacementTables(sDir, sAppJarsReverse);
            TestUtil.logHarness("Deploying apps for reverse rebuildable...");
            bSweepRebuildableReverseRuntimeFiles = true;
            sClientClassPaths += continueToDeployApps(sDir, sAppJarsReverse);
            bSweepRebuildableReverseRuntimeFiles = false;
          }
        }
      }

      if (sInteropDirections.equals("reverse")
          || sInteropDirections.equals("both")) {
        if (isInteropDir(sDir)) {
          swapSettings("reverse");
          populatePortingReplacementTables(sDir, sAppJarsArray);
          TestUtil.logHarness("Deploying apps for reverse run...");
          sClientClassPaths += continueToDeployApps(sDir, sAppJarsArray);
          TestUtil.logHarness(
              "Swapped settings & deployed apps to opposite servers for directory:  "
                  + sDir);
        } else {
          // This is only for the case that we are a rebuildable test and the
          // direction
          // has been overridden to reverse. We don't swap settings and deploy
          // in the
          // reverse direction for rebuiuldable tests - only true interop tests
          if (sInteropDirections.equals("reverse")) {
            populatePortingReplacementTables(sDir, sAppJarsReverse);
            TestUtil.logHarness("Deploying apps for reverse rebuildable...");
            bSweepRebuildableReverseRuntimeFiles = true;
            sClientClassPaths += continueToDeployApps(sDir, sAppJarsReverse);
            bSweepRebuildableReverseRuntimeFiles = false;
          }
        }
      }
    } else {
      if (TestUtil.harnessDebug)
        TestUtil.logHarnessDebug(
            "Deployment of apps disabled due to execute mode being set to:"
                + String.valueOf(executionMode));
    }
    return sClientClassPaths;
  }

  private DeploymentInfo getDeploymentInfo(String earFile, String sAppJar,
      String sFileNameMinusExtension, String[] sValidRuntimeInfoFilesArray)
      throws TSDeploymentException {
    String sRuntimeInfoFiles = "";
    if (sValidRuntimeInfoFilesArray != null) {
      for (int j = 0; j < sValidRuntimeInfoFilesArray.length; j++) {
        sRuntimeInfoFiles += sValidRuntimeInfoFilesArray[j] + " ";
      }
    }
    if (TestUtil.harnessDebug)
      TestUtil.logHarnessDebug(sFileNameMinusExtension + " ear_file = "
          + earFile + "\n" + sFileNameMinusExtension + " runtime files = "
          + sRuntimeInfoFiles);
    try {
      DeploymentInfo deploymentInfo = deliv.getDeploymentInfo(earFile,
          sValidRuntimeInfoFilesArray);
      return deploymentInfo;
    } catch (Exception e) {
      e.printStackTrace();
      throw new TSDeploymentException(
          "Exception reading runtime information from Deliverable instance: "
              + e.getMessage());
    }
  }

  private void populatePortingReplacementTables(String sDir,
      String[] sAppJarsArray) throws TSDeploymentException {
    String sFileNameMinusExtension = "";
    String[] sValidRuntimeInfoFilesArray = null;
    String earFile = "";
    File fTestDir = new File(sDir);
    // String[] sAppJarsArray = fTestDir.list(ArchiveFilter.getInstance());
    String[] sRuntimeInfoFilesArray = fTestDir
        .list(RuntimeInfoFilter.getInstance());

    // if there are no archives to deploy, just warn and return
    if (sAppJarsArray == null || sAppJarsArray.length < 1) {
      return;
    }

    DeploymentInfo[] infoArray = new DeploymentInfo[sAppJarsArray.length];
    for (int ii = 0; ii < sAppJarsArray.length; ii++) {
      earFile = sDir + File.separator + sAppJarsArray[ii];
      if (TestUtil.harnessDebug)
        TestUtil.logHarnessDebug("earFile = " + earFile);
      sFileNameMinusExtension = sAppJarsArray[ii].substring(0,
          sAppJarsArray[ii].lastIndexOf("."));
      // sClientClassPaths = "";
      // get only those runtime files which go with this
      // .ear file
      // also rewrites runtime.xml
      sValidRuntimeInfoFilesArray = getValidRuntimeInfoFiles(
          sDir + File.separator + sAppJarsArray[ii], sRuntimeInfoFilesArray,
          sDir, null, false);
      infoArray[ii] = getDeploymentInfo(earFile, sAppJarsArray[ii],
          sFileNameMinusExtension, sValidRuntimeInfoFilesArray);
    }
    TSDeploymentInterface ctsDeploy1 = (TSDeploymentInterface) htTSDeployers
        .get("cts1");

    // if this is not an interop test, don't use cts2
    TSDeploymentInterface ctsDeploy2;
    if (isInteropDir(sDir))
      ctsDeploy2 = (TSDeploymentInterface) htTSDeployers.get("cts2");
    else
      ctsDeploy2 = ctsDeploy1;

    // never reset these in case we have common apps in a diff dir
    // should we tack on "notcommon" to the beginning of each entry that
    // is not from a common bean so that we can remove those when we get into
    // a different dir?
    // Make sure that htJNDIMapsFromServer1 always matches the porting class.1
    // and the same for 2
    if (bReversed) {
      // maps from server whose porting package is 1
      htJNDIMapsFromServer1.putAll(ctsDeploy2.getInteropJNDINames(infoArray));
      // maps from server whose porting package is 2
      htJNDIMapsFromServer2.putAll(ctsDeploy1.getInteropJNDINames(infoArray));
      sInteropDirectionWhenTablesWerePopulated = "reverse";
    } else {
      // maps from server whose porting package is 1
      htJNDIMapsFromServer1.putAll(ctsDeploy1.getInteropJNDINames(infoArray));
      // maps from server whose porting package is 2
      htJNDIMapsFromServer2.putAll(ctsDeploy2.getInteropJNDINames(infoArray));
      sInteropDirectionWhenTablesWerePopulated = "forward";
    }

    if (TestUtil.harnessDebug)
      printAllMappings();
  }

  private void printAllMappings() {
    String sKey;

    TestUtil.logHarness("RuntimeInfo mappings from Porting impl 1");

    // dump all mappings from the hashtables
    for (Enumeration e = htJNDIMapsFromServer1.keys(); e.hasMoreElements();) {
      sKey = (String) e.nextElement();

      TestUtil.logHarness("----------------------------------------");
      TestUtil.logHarness("RuntimeInfo key:  " + sKey);
      TestUtil.logHarness(
          "To be replaced with:  " + (String) htJNDIMapsFromServer1.get(sKey));
      TestUtil.logHarness("----------------------------------------");
    }

    TestUtil.logHarness("RuntimeInfo mappings from Porting impl 2");

    for (Enumeration e = htJNDIMapsFromServer2.keys(); e.hasMoreElements();) {
      sKey = (String) e.nextElement();

      TestUtil.logHarness("----------------------------------------");
      TestUtil.logHarness("RuntimeInfo key:  " + sKey);
      TestUtil.logHarness(
          "To be replaced with:  " + (String) htJNDIMapsFromServer2.get(sKey));
      TestUtil.logHarness("----------------------------------------");
    }
  }

  private String continueToDeployApps(String sDir, String[] sAppJarsArray)
      throws TSDeploymentException, TSJMSAdminException {
    DeploymentInfo deploymentInfo = null;
    String sClientClassPaths = "";
    File fTestDir = new File(sDir);
    // String[] sAppJarsArray = fTestDir.list(ArchiveFilter.getInstance());
    String[] sRuntimeInfoFilesArray = fTestDir
        .list(RuntimeInfoFilter.getInstance());
    String[] sValidRuntimeInfoFilesArray = null;
    String sFileNameMinusExtension = "";
    String sRuntimeFileMinusExtension = "";
    String sRuntimeInfoFiles = ""; // for debug output
    TSDeploymentInterface ctsDeploy = null;
    TSJMSAdminInterface ctsJMS = null;
    String[] sQueues = null;
    String[] sTopics = null;
    String earFile = "";

    // if there are no archives to deploy, just warn and return
    if (sAppJarsArray == null || sAppJarsArray.length < 1) {
      TestUtil.logHarness(
          "There are no archives to deploy in this directory:  " + sDir);
      return "";
    }
    Arrays.sort(sAppJarsArray);
    // need to figure out the exact build rules for this
    for (int ii = 0; ii < sAppJarsArray.length; ii++) {
      earFile = sDir + File.separator + sAppJarsArray[ii];
      if (TestUtil.harnessDebug)
        TestUtil.logHarnessDebug("earFile = " + earFile);
      sFileNameMinusExtension = sAppJarsArray[ii].substring(0,
          sAppJarsArray[ii].lastIndexOf("."));
      // sClientClassPaths = "";
      // if (isInteropDir(sDir)) {
      Hashtable oppositeServerJNDITable = null;
      if (sInteropDirectionWhenTablesWerePopulated.equals("forward")) {
        if (sFileNameMinusExtension.indexOf("_j2ee2") != -1
            || sFileNameMinusExtension.startsWith("vi_built_"))
          oppositeServerJNDITable = htJNDIMapsFromServer1;
        else
          oppositeServerJNDITable = htJNDIMapsFromServer2;
      } else // reverse
      {
        if (sFileNameMinusExtension.indexOf("_j2ee2") != -1
            || sFileNameMinusExtension.startsWith("vi_built_"))
          oppositeServerJNDITable = htJNDIMapsFromServer2;
        else
          oppositeServerJNDITable = htJNDIMapsFromServer1;
      }

      if (bSweepRebuildableReverseRuntimeFiles) {
        swapSettings("reverse");
      }
      // get only those runtime files which go with this
      // .ear file
      sValidRuntimeInfoFilesArray = getValidRuntimeInfoFiles(
          sDir + File.separator + sAppJarsArray[ii], sRuntimeInfoFilesArray,
          sDir, oppositeServerJNDITable, true);

      if (bSweepRebuildableReverseRuntimeFiles) {
        swapSettings("forward");
      }

      /*
       * } else { // get only those runtime files which go with this // .ear
       * file sValidRuntimeInfoFilesArray = getValidRuntimeInfoFiles(sDir +
       * File.separator + sAppJarsArray[ii], sRuntimeInfoFilesArray, sDir, null,
       * true); }
       */
      deploymentInfo = getDeploymentInfo(earFile, sAppJarsArray[ii],
          sFileNameMinusExtension, sValidRuntimeInfoFilesArray);
      // save off all common app infos so that they can be passed to new test
      // directory deployments
      if (bDeployingCommonApps)
        vCommonDeploymentInfos.addElement(deploymentInfo);
      // always add to the current deploymentinfos
      vCurrentDeploymentInfos.addElement(deploymentInfo);
      // give the current ear's deployment info access to previosly deployed
      // info objects
      deploymentInfo.setPreviousInfos((DeploymentInfo[]) vCurrentDeploymentInfos
          .toArray(new DeploymentInfo[vCurrentDeploymentInfos.size()]));
      // deploy
      if (!sClientClassPaths.equals(""))
        sClientClassPaths += File.pathSeparator;
      if (TestUtil.harnessDebug)
        TestUtil.logHarnessDebug("generateSQL is set to:  "
            + deploymentInfo.getProperty("generateSQL"));
      try {
        if (sFileNameMinusExtension.indexOf("_j2ee2") != -1
            || sFileNameMinusExtension.startsWith("vi_built_")) {
          deploymentInfo.setProperty("deployment_host",
              jteMgr.getProperty("deployment_host.2"));
          deploymentInfo.setProperty("deployment_port",
              jteMgr.getProperty("deployment_port.2"));
          deploymentInfo.setProperty("deployment.props.number", "2");

          ctsDeploy = (TSDeploymentInterface) htTSDeployers.get("cts2");
          if (bSupportsAutoJMS) {
            ctsJMS = (TSJMSAdminInterface) htTSJMSAdmins.get("cts2");
            sQueues = TSJMSAdmin.getQueues(sDir, 2);
            sTopics = TSJMSAdmin.getTopics(sDir, 2);
            // one time creation of connection factories on server 2
            if (bCreateConnectionFactories2
                && TSJMSAdmin.requiresJmsFactories(sDir)) {
              Hashtable htTopicFactories = TSJMSAdmin
                  .getTopicConnectionFactories(2);
              int iSize = htTopicFactories.size();
              sTopicFactories2 = new String[iSize];
              String[] sTopicFactoryProps = new String[iSize];
              int jj = 0;
              for (Enumeration e = htTopicFactories.keys(); e
                  .hasMoreElements();) {
                String sElement = (String) e.nextElement();
                sTopicFactories2[jj] = sElement;
                sTopicFactoryProps[jj] = (String) htTopicFactories
                    .get(sElement);
                jj++;
              }
              ctsJMS.createTopicConnectionFactories(sTopicFactories2,
                  sTopicFactoryProps);
              Hashtable htQueueFactories = TSJMSAdmin
                  .getQueueConnectionFactories(2);
              iSize = htQueueFactories.size();
              sQueueFactories2 = new String[iSize];
              String[] sQueueFactoryProps = new String[iSize];
              jj = 0;
              for (Enumeration e = htQueueFactories.keys(); e
                  .hasMoreElements();) {
                String sElement = (String) e.nextElement();
                sQueueFactories2[jj] = sElement;
                sQueueFactoryProps[jj] = (String) htQueueFactories
                    .get(sElement);
                jj++;
              }
              ctsJMS.createQueueConnectionFactories(sQueueFactories2,
                  sQueueFactoryProps);
              bCreateConnectionFactories2 = false;
            }
          }
        } else {
          deploymentInfo.setProperty("deployment_host",
              jteMgr.getProperty("deployment_host.1"));
          deploymentInfo.setProperty("deployment_port",
              jteMgr.getProperty("deployment_port.1"));
          deploymentInfo.setProperty("deployment.props.number", "1");

          ctsDeploy = (TSDeploymentInterface) htTSDeployers.get("cts1");
          if (bSupportsAutoJMS) {
            ctsJMS = (TSJMSAdminInterface) htTSJMSAdmins.get("cts1");
            sQueues = TSJMSAdmin.getQueues(sDir, 1);
            sTopics = TSJMSAdmin.getTopics(sDir, 1);
            // one time creation of connection factories on server 1
            if (bCreateConnectionFactories1
                && TSJMSAdmin.requiresJmsFactories(sDir)) {
              Hashtable htTopicFactories = TSJMSAdmin
                  .getTopicConnectionFactories(1);
              int iSize = htTopicFactories.size();
              sTopicFactories1 = new String[iSize];
              String[] sTopicFactoryProps = new String[iSize];
              int jj = 0;
              for (Enumeration e = htTopicFactories.keys(); e
                  .hasMoreElements();) {
                String sElement = (String) e.nextElement();
                sTopicFactories1[jj] = sElement;
                sTopicFactoryProps[jj] = (String) htTopicFactories
                    .get(sElement);
                jj++;
              }
              ctsJMS.createTopicConnectionFactories(sTopicFactories1,
                  sTopicFactoryProps);
              Hashtable htQueueFactories = TSJMSAdmin
                  .getQueueConnectionFactories(1);
              iSize = htQueueFactories.size();
              sQueueFactories1 = new String[iSize];
              String[] sQueueFactoryProps = new String[iSize];
              jj = 0;
              for (Enumeration e = htQueueFactories.keys(); e
                  .hasMoreElements();) {
                String sElement = (String) e.nextElement();
                sQueueFactories1[jj] = sElement;
                sQueueFactoryProps[jj] = (String) htQueueFactories
                    .get(sElement);
                jj++;
              }
              ctsJMS.createQueueConnectionFactories(sQueueFactories1,
                  sQueueFactoryProps);
              bCreateConnectionFactories1 = false;
            }
          }
        }
      } catch (PropertyNotSetException e) {
        e.printStackTrace();
        throw new TSDeploymentException(
            "Failed to get values for deployment_host properties.");
      }
      if (bSupportsAutoJMS) {
        // create topics/queues here
        if (sQueues != null) {
          ctsJMS.createQueues(sQueues);
        }
        if (sTopics != null) {
          ctsJMS.createTopics(sTopics);
        }
      }
      // check to see if we have a connector
      if (earFile.endsWith(".rar")) {
        try {
          Properties connectorProps = new Properties();
          connectorProps.setProperty("rar_file", earFile);
          connectorProps.setProperty("deployment_host",
              deploymentInfo.getProperty("deployment_host"));
          connectorProps.setProperty("deployment_port",
              deploymentInfo.getProperty("deployment_port"));
          connectorProps.setProperty("deployment.props.number",
              deploymentInfo.getProperty("deployment.props.number"));

          ctsDeploy.deployConnector(connectorProps);
        } catch (TSDeploymentException e) {
          logOut.println("Failed to deploy Connector:  " + earFile);
          e.printStackTrace();
          throw e;
        }
      } else {
        sClientClassPaths += ctsDeploy.deploy(deploymentInfo);
        if (!sClientClassPaths.endsWith(File.pathSeparator))
          sClientClassPaths += File.pathSeparator;
      }
    }
    return sClientClassPaths;
  }

  private void undeployApps(String sDir)
      throws TSDeploymentException, TSJMSAdminException {
    if (executionMode == ExecutionMode.DEPLOY_RUN_UNDEPLOY
        || executionMode == ExecutionMode.UNDEPLOY) {
      String sInteropDirections = InteropTestManager
          .getInteropDirectionsFromKeywords(sDir);
      String[] sAppJarsArray;

      if (TestUtil.harnessDebug)
        TestUtil.logHarnessDebug(
            "undeployApps - InteropDirections:  " + sInteropDirections);

      sAppJarsArray = ProfileHelper.getArchives(sDir, sInteropDirections);

      if (sInteropDirections.equals("forward")
          || sInteropDirections.equals("both")) {
        swapSettings("forward");
        TestUtil.logHarness("Undeploying apps...");
        continueToUndeployApps(sDir, sAppJarsArray);
      }
      if (sInteropDirections.equals("reverse")
          || sInteropDirections.equals("both")) {

        if (isInteropDir(sDir)) {
          swapSettings("reverse");
          TestUtil.logHarness("Deploying apps for reverse run...");
          continueToUndeployApps(sDir, sAppJarsArray);

        } else {
          // This is only for the case that we are a rebuildable test and the
          // direction
          // has been overridden to reverse. We don't swap settings to reverse
          // and deploy in the
          // reverse direction for rebuiuldable tests - only true interop tests

          // Adding explicit call to be sure that settings are in the forward
          // direction since when
          // these tests run, the settings are swapped to reverse
          swapSettings("forward");

          if (sInteropDirections.equals("reverse")) {
            TestUtil.logHarness("Undeploying apps for reverse rebuildable...");
            continueToUndeployApps(sDir, sAppJarsArray);
          }
        }

      }
    } else {
      if (TestUtil.harnessDebug)
        TestUtil.logHarnessDebug(
            "Undeployment of apps disabled due to execute mode being set to:"
                + String.valueOf(executionMode));
    }
  }

  private void continueToUndeployApps(String sDir, String[] sAppJarsArray)
      throws TSDeploymentException, TSJMSAdminException {
    File fTestDir = new File(sDir);
    // String[] sAppJarsArray = fTestDir.list(ArchiveFilter.getInstance());
    String sFileNameMinusExtension = "";
    TSDeploymentInterface ctsDeploy = null;
    TSJMSAdminInterface ctsJMS = null;
    String[] sQueues = null;
    String[] sTopics = null;
    // if there are no archives to deploy, just warn and return
    if (sAppJarsArray == null) {
      TestUtil.logHarness(
          "There are no archives to undeploy in this directory:  " + sDir);
      return;
    }
    // set the property, sFileNameMinusExtension.ear_file
    // set the prop, sFileNameMinusExtension.runtime_files
    // need to figure out the exact build rules for this
    for (int ii = 0; ii < sAppJarsArray.length; ii++) {
      sFileNameMinusExtension = sAppJarsArray[ii].substring(0,
          sAppJarsArray[ii].lastIndexOf("."));
      pDeployProps.put("ear_file", sDir + File.separator + sAppJarsArray[ii]);
      if (TestUtil.harnessDebug)
        TestUtil.logHarnessDebug(sFileNameMinusExtension + " ear_file = " + sDir
            + File.separator + sAppJarsArray[ii]);
      try {
        if (sFileNameMinusExtension.indexOf("_j2ee2") != -1
            || sFileNameMinusExtension.startsWith("vi_built_")) {
          pDeployProps.setProperty("deployment_host",
              jteMgr.getProperty("deployment_host.2"));
          pDeployProps.setProperty("deployment_port",
              jteMgr.getProperty("deployment_port.2"));
          pDeployProps.setProperty("deployment.props.number", "2");

          ctsDeploy = (TSDeploymentInterface) htTSDeployers.get("cts2");
          if (bSupportsAutoJMS) {
            ctsJMS = (TSJMSAdminInterface) htTSJMSAdmins.get("cts2");
            sQueues = TSJMSAdmin.getQueues(sDir, 2);
            sTopics = TSJMSAdmin.getTopics(sDir, 2);
          }
        } else {
          pDeployProps.setProperty("deployment_host",
              jteMgr.getProperty("deployment_host.1"));
          pDeployProps.setProperty("deployment_port",
              jteMgr.getProperty("deployment_port.1"));
          pDeployProps.setProperty("deployment.props.number", "1");

          ctsDeploy = (TSDeploymentInterface) htTSDeployers.get("cts1");
          if (bSupportsAutoJMS) {
            ctsJMS = (TSJMSAdminInterface) htTSJMSAdmins.get("cts1");
            sQueues = TSJMSAdmin.getQueues(sDir, 1);
            sTopics = TSJMSAdmin.getTopics(sDir, 1);
          }
        }
      } catch (PropertyNotSetException e) {
        e.printStackTrace();
        throw new TSDeploymentException(
            "Failed to get values for deployment_host properties.");
      }
      String earFile = pDeployProps.getProperty("ear_file");
      // check to see if we have a connector
      if (earFile.endsWith(".rar")) {
        // undeploy connector here
        try {
          Properties connectorProps = new Properties();
          connectorProps.setProperty("rar_file", earFile);
          connectorProps.setProperty("deployment_host",
              pDeployProps.getProperty("deployment_host"));
          connectorProps.setProperty("deployment_port",
              pDeployProps.getProperty("deployment_port"));
          connectorProps.setProperty("deployment.props.number",
              pDeployProps.getProperty("deployment.props.number"));

          if (ctsDeploy.isConnectorDeployed(connectorProps)) {
            ctsDeploy.undeployConnector(connectorProps);
          }
        } catch (TSDeploymentException e) {
          logOut.println("Failed to undeploy Connector:  " + earFile);
          e.printStackTrace();
          throw e;
        }
      } else {
        if (ctsDeploy.isDeployed(pDeployProps)) {
          ctsDeploy.undeploy(pDeployProps);
          logOut.println("Successfully undeployed app:  " + sDir
              + File.separator + sAppJarsArray[ii]);
          if (bSupportsAutoJMS) {
            // remove JMS topics/queues here
            // create topics/queues here
            if (sQueues != null) {
              ctsJMS.removeQueues(sQueues);
            }
            if (sTopics != null) {
              ctsJMS.removeTopics(sTopics);
            }
          }
        }
      }
    }
  }

  private void swap() {
    if (bSupportsAutoDeploy) {

      Object cts1 = htTSDeployers.get("cts1");
      Object jms1 = htTSJMSAdmins.get("cts1");
      Object cts2 = htTSDeployers.get("cts2");
      Object jms2 = htTSJMSAdmins.get("cts2");
      htTSDeployers.put("cts1", cts2);
      htTSDeployers.put("cts2", cts1);
      htTSJMSAdmins.put("cts1", jms2);
      htTSJMSAdmins.put("cts2", jms1);
    }
    try {
      runtimeConfig = new TSRuntimeConfiguration(logOut);
      TestUtil.logHarness("ss:  modified runtime.xml");
    } catch (Exception e) {
      e.printStackTrace();
      TestUtil.logHarness(
          "Failed to modify xml files with correct settings.  Please check the values of 'mailHost, mailFrom, webServerHost, and webServerPort'");
    }
  }

  public void swapSettings(String sDirection) {
    TestUtil.logHarnessDebug(
        "*******swapSettings CALLED with direction:  " + sDirection);

    if (sDirection.equals("reverse")) {
      if (bReversed)
        return;
      else {
        try {
          jteMgr = deliv.getPropertyManager();
          jteMgr.swapInteropPropertyValues(sDirection);
        } catch (Exception e) {
          e.printStackTrace();
        }
        bReversed = true;
        // deployers and runtime info
        swap();
      }
    } else {
      if (!bReversed)
        return;
      else {
        try {
          jteMgr = deliv.getPropertyManager();
          jteMgr.swapInteropPropertyValues(sDirection);
        } catch (Exception e) {
          e.printStackTrace();
        }
        bReversed = false;
        // deployers and runtime info
        swap();
      }
    }
  }

  public static class RuntimeInfoFilter implements FilenameFilter {
    private static RuntimeInfoFilter instance = new RuntimeInfoFilter();

    private RuntimeInfoFilter() {
    }

    public static RuntimeInfoFilter getInstance() {
      return instance;
    }

    public boolean accept(File dir, String name) {
      boolean bReturn = false;

      if ((name.endsWith(".xml")
          && (name.indexOf(".sun-") != -1 || name.startsWith("sun-")))
          || name.endsWith("dbschema")) {
        bReturn = true;
      }
      return bReturn;
    }
  }
}
