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

import com.sun.javatest.*;
import com.sun.javatest.util.StringArray;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.deliverable.*;
import java.io.*;
import java.util.*;
import java.util.jar.*;
import org.apache.tools.ant.taskdefs.condition.Os;
import org.apache.tools.ant.Project;

public class TSScript extends com.sun.javatest.Script {

  TestResult.Section section;

  Status status;

  TestResult testResult;

  String keywords;

  PrintWriter logOut;

  private static String sClientClassesPath = "";

  private static SuiteSynchronizer ss;

  String testDir = "";

  String sVehicle = "";

  String sIsServiceTest = "";

  java.util.Properties pTestProps;

  PropertyManagerInterface propMgr;

  boolean bAppClientTest;

  int executionMode;

  java.util.Properties pExecProps = new Properties();

  public static boolean bUseSameJVMCommand;

  private String sInteropDirection;

  private boolean bIsRebuildableReverseTest = false;

  static {
    bUseSameJVMCommand = Boolean.getBoolean("same.jvm");
  }

  public Status run(String[] args, TestDescription td, TestEnvironment env) {

    pTestProps = new java.util.Properties();
    String[] sArgs = null;
    String[] sVal = null;
    String testName = td.getParameter("testName");
    String executeClass = td.getParameter("classname");
    String executeArgs = td.getParameter("testArgs");
    String sTestJteFile = null;
    String sSrcDir = TSTestFinder
        .getAbsolutePath(td.getParameter("test_directory"));
    String sClassesVIBuiltDir = TestUtil.replaceLastSrc(sSrcDir,
        "classes_vi_built");
    testDir = TestUtil.srcToDist(sSrcDir);
    // based on the test description, we should construct the set of props
    // that get sent to the test.
    testResult = getTestResult();
    section = testResult.createSection("TestRun");
    logOut = section.createOutput("log");
    sInteropDirection = td.getParameter("direction");
    if (sInteropDirection == null) {
      sInteropDirection = "forward";
    }
    if (testDir.indexOf("interop") == -1
        && sInteropDirection.equals("reverse")) {
      bIsRebuildableReverseTest = true;
      File fClassesVIBuiltDir = new File(sClassesVIBuiltDir);
      String[] sVIBuiltClasses = fClassesVIBuiltDir
          .list(ClassFilter.getInstance());

      if (sVIBuiltClasses == null || sVIBuiltClasses.length == 0) {
        String error = "TEST NOT RUN:  This test cannot be run until you"
            + TestUtil.NEW_LINE
            + "rebuild the test classes and/or archives associated with it."
            + TestUtil.NEW_LINE + "Please see the TCK documentation for more"
            + TestUtil.NEW_LINE + "information on rebuildable tests.";

        // Report an error since the vi built archives do not exist yet
        logOut.println("*********************************************");
        logOut.println(error);
        logOut.println("*********************************************");
        return Status.error("VI classes and/or archives have not been built.");
      }
    }

    try {
      // grab keywords before resetting property manager
      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();
      keywords = propMgr.getProperty("current.keywords");

      // reset property manager with currrent environment
      propMgr = DeliverableFactory.getDeliverableInstance()
          .createPropertyManager(env);

      ss = SuiteSynchronizer.getSuiteSynchronizer(logOut);

      // If we're just deploying/undeploying, return now
      executionMode = ExecutionMode.getExecutionMode(propMgr);
      if (executionMode == ExecutionMode.LIST) {
        logOut.println("TEST:  " + testName);
        return Status.error("LISTED");
      } else if (executionMode == ExecutionMode.DEPLOY) {
        if (ss.getDeploymentStatus().equals("failed")) {
          logOut.println(ss.getDeploymentExceptionTrace());
          return Status.failed(
              "An error occurred during the Deployment phase for tests in this directory.");
        } else {
          return Status.passed("DEPLOYED");
        }
      } else if (executionMode == ExecutionMode.UNDEPLOY) {
        if (ss.getDeploymentStatus().equals("failed")) {
          logOut.println(ss.getDeploymentExceptionTrace());
          return Status.failed(
              "An error occurred during the Deployment phase for tests in this directory.");
        } else {
          return Status.passed("UNDEPLOYED");
        }
      }
      // else continue on

      // We must reset all settings to forward because of the call above to
      // recreate
      // the property manager with the new environment. The call above is needed
      // for every
      // test since a user may run the GUI and change jte file settings prior to
      // running
      // another set of tests. This also fixes bug# 4953862.
      ss.swapSettings("forward");

      if (TestUtil.harnessDebug) {
        TestUtil.logHarnessDebug(
            "TSScript - sInteropDirection:  " + sInteropDirection);
      }
      if (sInteropDirection.equals("reverse")) {
        ss.swapSettings("reverse");
        if (TestUtil.harnessDebug) {
          TestUtil.logHarnessDebug("Reversed settings for test:  " + testName);
        }
      }

      if (TestUtil.harnessDebug) {
        StringBuffer sb = new StringBuffer();
        sb.append("test description contains key/value:");
        for (Iterator e = td.getParameterKeys(); e.hasNext();) {
          String k = (String) (e.next());
          sb.append(k).append('=').append(td.getParameter(k));
        }
        TestUtil.logHarnessDebug(sb.toString());
      }
      // Add the path to any -ap args
      String sTemp = "";
      String sTempArgs = "";
      String defaultBin = System.getProperty("TS_HOME") + File.separator
          + "bin";
      String sTSBinDir = System.getProperty("bin.dir", defaultBin);
      if (executeArgs != null) {
        StringTokenizer st = new StringTokenizer(executeArgs);
        while (st.hasMoreTokens()) {
          sTemp = st.nextToken();
          if (sTemp.equals("-ap")) {
            String next = st.nextToken();
            if (next.equals("tssql.stmt")
                && sInteropDirection.equals("reverse")) {
              sTempArgs += sTemp + " " + sTSBinDir + File.separator
                  + "tssql.stmt.ri" + " ";
            } else {
              sTempArgs += sTemp + " " + sTSBinDir + File.separator + next
                  + " ";
            }
          } else {
            sTempArgs += sTemp + " ";
          }
        }
        executeArgs = sTempArgs.trim();
      }
      // get properties from the test description
      String testPropsInDescp = td.getParameter("testProps");
      String[] sTestPropKeys = TestUtil.EMPTY_STRING_ARRAY;
      if (testPropsInDescp != null) {
        StringTokenizer st = new StringTokenizer(testPropsInDescp.trim());
        sTestPropKeys = new String[st.countTokens()];
        for (int ii = 0; ii < sTestPropKeys.length; ii++) {
          sTestPropKeys[ii] = st.nextToken();
        }
      }
      // construct a properties object with the test specific keys
      // and the values from the jte file
      try {
        pTestProps = propMgr.getTestSpecificProperties(sTestPropKeys);
      } catch (PropertyNotSetException pnse) {
        pnse.printStackTrace();
        return Status.failed(
            "Please supply values for the necessary properties to run these tests.  "
                + pnse.getMessage());
      }

      // set current.keywords so we can pass it to the sig tests
      // get keywords that are set and add them to the prop mgr
      TestUtil
          .logHarness("keywords (to be passed to tests) set to:  " + keywords);
      pTestProps.put("current.keywords", keywords);

      // so we know what kind of test this is
      String finderType = td.getParameter("finder");
      pTestProps.put("finder", (finderType == null ? "cts" : finderType));
      // Added test classname for JCKService tests.
      pTestProps.put("test_classname", executeClass);

      if (TestUtil.harnessDebug) {
        TestUtil.logHarnessDebug("** for JCK::testClassName= " + executeClass);
      }
      // get and set which vehicle this test should run in
      String sID = td.getParameter("id");
      sIsServiceTest = td.getParameter("service_eetest");
      sVehicle = null;
      // do we have a service test?
      if (sIsServiceTest.equals("yes")) {

        if (sID.endsWith("_reverse")) {
          // remove "_reverse" first
          sVehicle = sID.substring(0, sID.lastIndexOf("_"));
          sVehicle = sVehicle.substring(sVehicle.lastIndexOf("_") + 1);
        } else {
          sVehicle = sID.substring(sID.lastIndexOf("_") + 1);
        }

        // set the ExecuteClass to our VehicleClient
        if (sVehicle.equalsIgnoreCase("wsappclient")) {
          executeClass = "com.sun.ts.tests.common.vehicle.wsappclient.WSAppclient";
        } else {
          executeClass = "com.sun.ts.tests.common.vehicle.VehicleClient";
        }

        if (TestUtil.harnessDebug) {
          TestUtil.logHarnessDebug("sVehicle = " + sVehicle);
        }
        if (sVehicle.indexOf("servlet") != -1
            || sVehicle.indexOf("connectorservlet") != -1
            || sVehicle.indexOf("customvehicle") != -1
            || sVehicle.indexOf("web") != -1 || sVehicle.indexOf("jsp") != -1
            || sVehicle.indexOf("jsf") != -1) {
          try {
            pTestProps.put("webServerHost",
                propMgr.getProperty("webServerHost"));
            pTestProps.put("webServerPort",
                propMgr.getProperty("webServerPort"));
          } catch (PropertyNotSetException e) {
            e.printStackTrace();
            return Status.failed(
                "Please supply values for webServerHost & webServerPort.  They are required to run service tests in a jsp or servlet.");
          }
        }

        if (!sVehicle.equals("standalone") && !sVehicle.equals("ejbembed")
            && !sVehicle.equals("customvehicle")) {
          // only use the vehicle ear name in serviceeetest to create
          // unique JNDI names and context roots.
          // This is not used in the standalone TCK case
          String vehicleArchiveName = getVehicleArchiveName();
          if (vehicleArchiveName == null) {
            String msg = "TSScript:  No vehicle ear found in : \"" + testDir
                + "\".  If this is a test that requires test clients"
                + " to be built before executing the tests, please refer to"
                + " the TCK documentation for further instructions.";
            TestUtil.logErr(msg);
            return Status.failed(msg);
          }
          pTestProps.put("vehicle_archive_name", vehicleArchiveName);
        }
      } else {
        sVehicle = "not a service test";
      }

      if (TestUtil.harnessDebug) {
        TestUtil.logHarnessDebug("sVehicle = " + sVehicle);
      }

      // if we are standalone (like JDBC ouside of J2EE, don't deploy)
      // just run!
      if (!sVehicle.equals("standalone")) {
        // check the status of deployment
        // which happened in the observer class
        if (ss.getDeploymentStatus().equals("failed")) {

          System.err.println("*************test args = " + executeArgs);
          // If we have a negative deployment test, and we fail deployment
          // return PASS here.
          if (executeArgs.contains("-expectdeploymentfailure")) {
            logOut.println("Deployment failed as expected");
            return Status.passed("Deployment failed as expected");
          } else {
            logOut.println(ss.getDeploymentExceptionTrace());
            return Status.failed(
                "An error occurred during the Deployment phase for tests in this directory.");
          }
        }
        sClientClassesPath = ss.getClientClassPath();
      }
      // Set up the test-specific props file here and add it to the args
      // that are passed to EETest
      try {
        sTestJteFile = createTestPropertyFile(pTestProps);
      } catch (Exception e) {
        return Status.failed("TSScript:  Failed to create tstest.jte file");
      }

      // create args
      // ADD ARGS FOR APPCLIENTCONTAINER
      // -client $(SRC_DIR)$(SLASH)$(APP_NAME).ear -name $(APP_NAME)_client
      // call SuiteSynchronizer.runAppclient which calls
      // TSDeployment.runAppclient(executeargs
      if (sVehicle.equals("not a service test")) {
        if (executeArgs == null) {
          executeArgs = "-p " + sTestJteFile + " -t " + testName;
        } else {
          executeArgs += " -p " + sTestJteFile + " -t " + testName;
        }
      } else {
        if (executeArgs == null) {
          executeArgs = "-p " + sTestJteFile + " -t " + testName + " -vehicle "
              + sVehicle;
        } else {
          executeArgs = " -p " + sTestJteFile + " -t " + testName + " -vehicle "
              + sVehicle + " " + executeArgs;
        }
      }
      // props needed:
      // args
      // earfile
      // testname
      String sClientEar = getClientEarFile();
      // sClientEar should also come up null in the case that
      // the vehicle is standalone (ktg 6-8-00)

      // In the case where we have special embedded ejb vehicle tests,
      // we need to exec the special commandline from ts.jte
      if (sVehicle.equalsIgnoreCase("ejbembed")) {
        if (TestUtil.harnessDebug) {
          TestUtil.logHarnessDebug("testExecuteejbembed used");
        }

        pExecProps = new Properties();
        pExecProps.put("clientClasspath", TestUtil.srcToDist(testDir)
            + File.separator + "ejbembed_vehicle_ejb.jar");
        status = execute("testExecuteEjbEmbed", executeClass, executeArgs);
      } else if (sClientEar == null || sVehicle.equals("standalone")
          || sVehicle.indexOf("customvehicle") != -1
          || sVehicle.indexOf("connectorservlet") != -1
          || sVehicle.indexOf("servlet") != -1 || sVehicle.indexOf("web") != -1
          || sVehicle.indexOf("jsp") != -1 || sVehicle.indexOf("jsf") != -1) {
        // not an appclient test so run as usual
        bAppClientTest = false;

        // Added to allow execution of standalone clients within the same JVM
        // as the JavaTest harness. This mode can only be used when running
        // the tests for sanity checking.
        if (bUseSameJVMCommand) {
          TestUtil.logHarness("Running test client in SameJVM mode");
          status = execute("testExecuteSameJVM", executeClass, executeArgs);
        } else {
          if (sInteropDirection.equals("reverse")) {
            if (TestUtil.harnessDebug) {
              TestUtil.logHarnessDebug("testExecute2 used");
            }
            status = execute("testExecute2", executeClass, executeArgs);
          } else {
            if (TestUtil.harnessDebug) {
              TestUtil.logHarnessDebug("testExecute used");
            }
            status = execute("testExecute", executeClass, executeArgs);
          }
        }

      } else {
        bAppClientTest = true;
        pExecProps = new Properties();
        pExecProps.put("executeArgs", executeArgs);
        pExecProps.put("ear_file", sClientEar);
        pExecProps.put("clientClasspath", sClientClassesPath);

        if (sClientEar.indexOf("_vehicles") != -1) {
          pExecProps.put("client_name",
              sClientEar.substring(sClientEar.lastIndexOf(File.separator) + 1,
                  sClientEar.indexOf("_vehicles")) + "_" + sVehicle
                  + "_vehicle_client");
        } else {
          pExecProps.put("client_name",
              sClientEar.substring(sClientEar.lastIndexOf(File.separator) + 1,
                  sClientEar.lastIndexOf(".")) + "_client");
        }

        // Added test classname for JCKService Tests
        pExecProps.put("test_classname", executeClass);
        TSDeploymentInterface ctsDeploy = null;
        // Is our appclient contained in an ear that was deployed on
        // server1 or server2?
        if (sClientEar.indexOf("_j2ee2") != -1
            || sClientEar.startsWith("vi_built_")) {
          if (TestUtil.harnessDebug) {
            TestUtil.logHarnessDebug("ss.getTSDeployer = cts2");
          }
          ctsDeploy = ss.getTSDeployer("cts2");
        } else {
          // we should always get in here
          if (TestUtil.harnessDebug) {
            TestUtil.logHarnessDebug("ss.getTSDeployer = cts1");
          }
          ctsDeploy = ss.getTSDeployer("cts1");
        }
        if (sInteropDirection.equals("reverse")) {
          if (TestUtil.harnessDebug) {
            TestUtil.logHarnessDebug("testExecuteAppClient2 used");
          }
          status = execute("testExecuteAppClient2",
              "this.is.specified.in.the.jte.file",
              ctsDeploy.getAppClientArgs(pExecProps));
        } else {
          if (TestUtil.harnessDebug) {
            TestUtil.logHarnessDebug("testExecuteAppClient used");
          }
          status = execute("testExecuteAppClient",
              "this.is.specified.in.the.jte.file",
              ctsDeploy.getAppClientArgs(pExecProps));
        }
      }
    } catch (ClassCastException e) {
      e.printStackTrace();
      status = Status.failed("Can't load test: required interface not found");
    } catch (Exception e) {
      status = Status.failed("Illegal access to test: " + e);
      e.printStackTrace();
    } finally {
      if (logOut != null) {
        logOut.close();
      }
    }
    section.setStatus(status);
    return status;
  }

  private String getVehicleArchiveName() {
    String[] sAppJarsArray = ProfileHelper
        .getArchives(TestUtil.srcToDist(testDir), sInteropDirection);
    if (sAppJarsArray == null) {
      sAppJarsArray = TestUtil.EMPTY_STRING_ARRAY;
    }
    String vehicleName = null;

    if (TestUtil.harnessDebug) {
      TestUtil.logHarnessDebug("testdir = " + testDir);
    }
    if (sIsServiceTest.equals("yes")) {
      vehicleName = getVehicleArchiveName0(sAppJarsArray, ".ear");
      if (vehicleName == null) {
        vehicleName = getVehicleArchiveName0(sAppJarsArray, ".war");
      }
      if (vehicleName == null) {
        vehicleName = getVehicleArchiveName0(sAppJarsArray, ".jar");
      }
    }
    return vehicleName;
  }

  private String getVehicleArchiveName0(String[] archiveNames,
      String archiveExt) {
    String vehicleArchiveName = null;
    for (int ii = 0; ii < archiveNames.length; ii++) {
      if (TestUtil.harnessDebug) {
        TestUtil.logHarnessDebug("archiveNames[ii] = " + archiveNames[ii]);
      }
      if ((archiveNames[ii].indexOf("_vehicles") != -1
          || archiveNames[ii].indexOf(sVehicle + "_vehicle") != -1)
          && archiveNames[ii].endsWith(archiveExt)) {

        if (archiveNames[ii].startsWith("vi_built_")) {
          // strip off the vi_built_ string since this is used by vehicle
          // tests as the context root or jndi name and should
          // not have the vi_built_ string on it
          vehicleArchiveName = archiveNames[ii].substring(9,
              archiveNames[ii].lastIndexOf(archiveExt));

        } else {
          vehicleArchiveName = archiveNames[ii].substring(0,
              archiveNames[ii].lastIndexOf(archiveExt));
        }

        if (TestUtil.harnessDebug) {
          TestUtil
              .logHarnessDebug("vehicleArchiveName = " + vehicleArchiveName);
        }
        break;
      }
    }
    return vehicleArchiveName;
  }

  private String getClientEarFile() {
    File fTestDir = new File(TestUtil.srcToDist(testDir));
    if (!fTestDir.exists()) {
      return null;
    }
    String[] sAppJarsArray = ProfileHelper
        .getArchives(TestUtil.srcToDist(testDir), sInteropDirection);
    Enumeration entries = null;
    String sEntry = null;
    String sAppClientEar = null;
    JarFile earFile = null;
    String sRuntimeFileName = null;

    if (TestUtil.harnessDebug) {
      TestUtil.logHarnessDebug("testdir = " + testDir);
    }
    try {
      for (int ii = 0; ii < sAppJarsArray.length
          && sAppClientEar == null; ii++) {

        if ((bIsRebuildableReverseTest
            && sAppJarsArray[ii].indexOf("vi_built_") == -1)
            || (!bIsRebuildableReverseTest
                && sAppJarsArray[ii].indexOf("vi_built_") != -1)) {
          continue;
        }

        if (TestUtil.harnessDebug) {
          TestUtil.logHarnessDebug(
              "earFile = " + testDir + File.separator + sAppJarsArray[ii]);
        }

        // if a service test, then first check that the ear
        // ends in _<vehicle>
        if (sIsServiceTest.equals("yes")) {
          if (sAppJarsArray[ii].indexOf("_vehicles") != -1
              || sAppJarsArray[ii].indexOf(sVehicle + "_vehicle") != -1) {
            sAppClientEar = testDir + File.separator + sAppJarsArray[ii];
            break;
          }
        } else {
          if (sAppJarsArray[ii].endsWith(".ear")) {
            earFile = new JarFile(testDir + File.separator + sAppJarsArray[ii]);
            entries = earFile.entries();
            while (entries.hasMoreElements()) {
              sEntry = ((JarEntry) entries.nextElement()).getName();
              if (sEntry.indexOf("_client.jar") != -1) {
                sAppClientEar = testDir + File.separator + sAppJarsArray[ii];
                break;
              }
            }
            try {
              if (earFile != null) {
                earFile.close();
              }
              earFile = null;
            } catch (IOException ioe) {
              ioe.printStackTrace(logOut);
            }
          } else {
            if (sAppJarsArray[ii].endsWith("_client.jar")) {
              sAppClientEar = testDir + File.separator + sAppJarsArray[ii];
              break;
            }
          }
        }
      }
    } catch (IOException e) {
      logOut.println("Error trying to read ear files");
      e.printStackTrace();
    }
    return sAppClientEar;
  }

  // Set up the test-specific props file here and add it to the args
  // that are passed to EETest
  private String createTestPropertyFile(java.util.Properties p)
      throws Exception {
    String sTestDir = p.getProperty("harness.temp.directory");
    File ctsTempDir = new File(sTestDir);
    if (!ctsTempDir.exists()) {
      if (!ctsTempDir.mkdirs()) {
        throw new Exception(
            "Failed to create the testsuite's temporary working directory:  "
                + sTestDir);
      }
    }
    FileOutputStream propsOut = null;
    String sJteFile = sTestDir + File.separator + "tstest.jte";
    try {
      propsOut = new FileOutputStream(sJteFile);
      p.store(propsOut, null);
      return sJteFile;
    } catch (Exception e) {
      if (TestUtil.harnessDebug) {
        TestUtil.logHarnessDebug(
            "createTestPropertyFile:  An exception was thrown while trying to create the test property file");
      }
      e.printStackTrace(logOut);
      throw e;
    } finally {
      if (propsOut != null) {
        propsOut.close();
      }
    }
  }

  private boolean isWindows() {
    return Os.isFamily("windows");
  }

  private String formatPath(String classpath) {

    if (bIsRebuildableReverseTest) {
      // replace classes with classes_vi_built
      classpath = classpath.replace("classes", "classes_vi_built");
    }

    try {
      String added = DeliverableFactory.getDeliverableInstance()
          .getAdditionalClasspath(testDir);

      if (added != null) {
        if (!added.startsWith(File.pathSeparator)) {
          classpath += File.pathSeparator;
        }
        classpath += added;
      }
    } catch (Exception e) {
      TestUtil.logHarness(
          "Failed to get additional classpath for the following dist directory:  "
              + testDir);
    }

    if (isWindows()) {
      return org.apache.tools.ant.Project.translatePath(classpath);
    }
    return classpath;
  }

  protected Status invokeCommand(String key) {
    Status s = null;
    try {
      String[] command = env.lookup("command." + key);
      String sClassPathFromExecProps = pExecProps.getProperty("clientClasspath",
          sClientClassesPath);
      TestUtil
          .logHarness("sClassPathFromExecProps = " + sClassPathFromExecProps);

      for (int ii = 0; ii < command.length; ii++) {
        if (isWindows() && command[ii].toUpperCase().startsWith("PATH=")) {
          int iEqualsIndex = command[ii].indexOf("=");
          String existingPath = command[ii].substring(iEqualsIndex + 1);
          command[ii] = "PATH=" + Project.translatePath(existingPath);
        }

        if (command[ii].startsWith("CLASSPATH=")) {
          int iEqualsIndex = command[ii].indexOf("=");
          String existingClassPath = command[ii].substring(iEqualsIndex + 1);
          if (sVehicle.equals("ejbembed")) {
            command[ii] = "CLASSPATH=" + formatPath(existingClassPath)
                + File.pathSeparator + sClassPathFromExecProps;
          } else {
            command[ii] = "CLASSPATH=" + sClassPathFromExecProps
                + File.pathSeparator + formatPath(existingClassPath);
          }

          if (TestUtil.harnessDebug) {
            TestUtil.logHarnessDebug("new classpath = " + command[ii]);
          }
        }
        if (command[ii].equals("-classpath")) {
          command[ii + 1] = formatPath(command[ii + 1]) + File.pathSeparator
              + sClassPathFromExecProps;
          if (TestUtil.harnessDebug) {
            TestUtil.logHarnessDebug("new -classpath = " + command[ii]);
          }
        }
        if (command[ii]
            .startsWith("-Dcom.sun.enterprise.iiop.security.interceptorFactory")
            && !testDir.contains("csiv2")) {
          command[ii] = "-DNotInCSIV2=true";
        }
      }
      if (command.length == 0) {
        return Status.error("environment `" + env.getName()
            + "' does not define a command `" + key + "'");
      }
      String className = command[0];
      String[] args = new String[command.length - 1];
      System.arraycopy(command, 1, args, 0, args.length);
      String basic = "command: " + className + " " + StringArray.join(args);
      section.getMessageWriter().println(basic);
      s = invokeClass(className, args, logOut, logOut);
      return s;
    } catch (TestEnvironment.Fault e) {
      return Status.error("problem getting info in environment `"
          + env.getName() + "' for `command." + key + "'");
    } catch (Throwable t) {
      t.printStackTrace();
      return Status.error(
          "An unexpected error occured in TSScript's invokeCommand method"
              + t.getMessage());
    }
  }

  private Status invokeClass(String className, String[] args,
      PrintWriter logOut, PrintWriter refOut) {
    // this is the central place where we get to run what the user
    // says in the environment file:
    Command testCommand;
    try {
      Class c = Class.forName(className);
      testCommand = (Command) (c.newInstance());
    } catch (ClassCastException e) {
      return Status.error("Can't run class `" + className
          + "': it does not implement interface " + Command.class.getName());
    } catch (ClassNotFoundException ex) {
      return Status.error("Can't find class `" + className + "', used in `"
          + env.getName() + "'");
    } catch (IllegalAccessException ex) {
      return Status.error("Illegal access to class `" + className
          + "', used in `" + env.getName() + "'");
    } catch (IllegalArgumentException ex) {
      return Status.error("Bad class name `" + className + "', used in `"
          + env.getName() + "'");
    } catch (InstantiationException ex) {
      return Status.error("Can't instantiate class `" + className
          + "', used in `" + env.getName() + "'");
    } catch (ThreadDeath e) {
      throw (ThreadDeath) (e.fillInStackTrace());
    } catch (Exception e) {
      e.printStackTrace(logOut);
      return Status.error("Unexpected exception trying to load command `"
          + className + "': " + e);
    } catch (Error e) {
      e.printStackTrace(logOut);
      return Status.error(
          "Unexpected error trying to load command `" + className + "': " + e);
    } catch (Throwable e) {
      e.printStackTrace(logOut);
      return Status.error("Unexpected throwable trying to load command `"
          + className + "': " + e);
    }
    try {
      return testCommand.run(args, logOut, refOut);
    } catch (ThreadDeath e) {
      throw (ThreadDeath) (e.fillInStackTrace());
    } catch (Exception e) {
      e.printStackTrace(logOut);
      // error reduced to failed in following line for benefit of
      // negative tests
      return Status.failed("Unexpected exception while executing command "
          + className + ": " + e);
    } catch (Error e) {
      e.printStackTrace(logOut);
      // error reduced to failed in following line for benefit of
      // negative tests
      return Status.failed(
          "Unexpected error while executing command " + className + ": " + e);
    } catch (Throwable e) {
      e.printStackTrace(logOut);
      // error *NOT* reduced to failed in following line for benefit of
      // negative tests: test should never throw something which is not
      // an Exception or Error
      return Status.error("Unexpected throwable while executing command "
          + className + ": " + e);
    }
  }

  public static class ClassFilter implements FilenameFilter {

    private static ClassFilter instance = new ClassFilter();

    private ClassFilter() {
    }

    public static ClassFilter getInstance() {
      return instance;
    }

    public boolean accept(File dir, String name) {
      return name.endsWith(".class");
    }
  }
}
