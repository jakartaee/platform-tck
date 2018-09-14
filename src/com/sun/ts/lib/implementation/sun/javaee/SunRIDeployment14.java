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

package com.sun.ts.lib.implementation.sun.javaee;

import java.util.*;
import java.io.*;
import java.net.*;
import com.sun.javatest.*;
import com.sun.enterprise.deployment.*;
import com.sun.enterprise.tools.deployment.main.*;
import com.sun.enterprise.tools.packager.*;
import com.sun.enterprise.tools.deployment.main.*;
import com.sun.enterprise.tools.deployment.backend.*;
import com.sun.enterprise.resource.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.deliverable.*;
import com.sun.ejb.sqlgen.DBInfo;
import com.sun.ejb.sqlgen.SQLGenerator;

/**
 * This particular implementation uses the deploy/undeploy semantics of Sun's
 * Java EE reference implementation. This is a utility class used by both
 * SunRIDeployment and SunRIPortingServer.
 *
 * @author Kyle Grucci
 */
public class SunRIDeployment14 {
  protected DeployTool deployTool = new DeployTool(false);

  protected ComponentPackager packager = new ComponentPackager();

  // public static TSDeployment deployTool = null;
  protected URL[] urls = new URL[1];

  protected PrintWriter Log = null;

  protected Hashtable htDeployedApps = new Hashtable();

  protected PropertyManagerInterface propMgr = null;

  protected String slastDeploymentHost = "";

  protected String slastDeploymentPort = "";

  protected File fRIEarFile = null;

  protected String sHarnessTempDir = "";

  protected String sRIAppName = "";

  // protected Application standaloneComponent = null;
  protected Hashtable htJNDIRefs = null;

  protected String sImplID = "";

  public void init(PrintWriter out, String id, PropertyManagerInterface pmi) {
    Log = out;
    sImplID = id;
    propMgr = pmi;

    try {
      sHarnessTempDir = propMgr.getProperty("harness.temp.directory");
    } catch (PropertyNotSetException pe) {
      Log.print(pe.getMessage());
      Log.print("error looking up property:  harness.temp.directory");
      pe.printStackTrace();
    }
  }

  protected String[] setRunTimeInfo(String s, String sApp)
      throws TSDeploymentException {
    String[] sRunTimeFiles = null;
    Application toSet = null;
    StringTokenizer st = new StringTokenizer(s);
    int iCount = st.countTokens();
    String sAppName = sApp.substring(sApp.lastIndexOf(File.separator) + 1,
        sApp.lastIndexOf("."));
    String sSweptRuntimeFile = "";
    sRunTimeFiles = new String[iCount];
    // sAppNameOnly = (new File(sApp)).getName();
    File fApp = new File(sApp);
    sRIAppName = sHarnessTempDir + File.separator + sImplID + fApp.getName();

    try {
      // reset
      fRIEarFile = null;
      fRIEarFile = new File(sRIAppName);
    } catch (Exception ioe) {
      Log.print(ioe.getMessage());
      throw new TSDeploymentException(ioe.getMessage());
    }

    for (int ii = 0; ii < iCount; ii++) {
      // set runtime info for each file
      sRunTimeFiles[ii] = st.nextToken().trim();

      try {
        TestUtil.logHarness("fApp = " + fApp.getPath());
        TestUtil.logHarness("fRIFile = " + sRIAppName);
        packager.setRuntimeDeploymentInfo(fApp, new File(sRunTimeFiles[ii]),
            fRIEarFile);
        Log.print("setRuntimeInfo done with:  " + sRunTimeFiles[ii]);

        // After the first time through, make sure we use the tmp ear
        // file
        if (fRIEarFile != null) {
          fApp = fRIEarFile;
          fRIEarFile = null;
        }
      } catch (Throwable t) {
        Log.print("*****************??????");
        Log.print(t.getMessage());
        Log.print("*****************??????");
        System.out.println("*****************??????");
        t.printStackTrace(Log);
        t.printStackTrace();
        System.out.println("Exception = " + t.getMessage());
        System.out.println("*****************??????");
        Log.print("Warning:  Failed to set runtime info in " + sRunTimeFiles[ii]
            + " for application, " + sApp);
        throw new TSDeploymentException(t.getMessage());
      }
    }
    return sRunTimeFiles;
  }

  /**
   * This method is called by the test harness to deploy an .ear file into Sun
   * 's Java EE reference implementation. We extract such info as the app
   * earfile from the provided deployment information. The following properties
   * are available for this method's use:
   * <p>
   * generateSQL - "true" if SQL is to be generated for CMP beans
   * <p>
   * <p>
   * deployment_host - the host where this app is to be deployed
   * <p>
   *
   * All additional information is queryable from the DeploymentInfo interface.
   *
   * @param info
   *          Object containing necessary deployment info.
   * @return This method should return a string which is formatted such that it
   *         can be appended to the classpath. This implementation returns the
   *         fully qualified path to a jar file, which contains the generated
   *         ejb stub classes, which are used by any appclient tests (tests
   *         whose client directly uses an ejb).
   */
  public String deploy(DeploymentInfo info) throws TSDeploymentException {
    Application toDeploy = null;
    String bGenerateSQL = info.getProperty("generateSQL");
    String sDeployHost = info.getProperty("deployment_host");
    String sApp = info.getEarFile();
    String[] sRunTimeFileArray = info.getRuntimeFiles();
    String sRunTimeFiles = "";
    File clientClassesFile = null;
    // URL[] returnURL = new URL[1];
    String sTSDeploymentDir = sApp.substring(0,
        sApp.lastIndexOf(File.separator) + 1) + "ts_dep";
    File ctsDeployDir = new File(sTSDeploymentDir);
    boolean bDeployFlag = true;
    sRIAppName = sHarnessTempDir + File.separator + sImplID
        + (new File(sApp)).getName();
    // standaloneComponent = null;

    if (!ctsDeployDir.exists()) {
      if (!ctsDeployDir.mkdir()) {
        throw new TSDeploymentException(
            "Failed to create the cts deployment working directory:  "
                + sTSDeploymentDir);
      }
    }

    // Set sRunTimeFiles string with contents of runtime files
    // array:
    if (sRunTimeFileArray != null) {
      for (int i = 0; i < sRunTimeFileArray.length; i++) {
        sRunTimeFiles += sRunTimeFileArray[i] + " ";
      }
    }

    // create client jar based on app name
    String sClientJarName = sApp.substring(sApp.lastIndexOf(File.separator) + 1,
        sApp.lastIndexOf(".") + 1) + "jar";

    // set RI specific runtime info for our app
    if (sRunTimeFiles != null)
      sRunTimeFileArray = setRunTimeInfo(sRunTimeFiles, sApp);

    // create the clientclasses directory (if it doesn't exist)
    clientClassesFile = new File(
        sTSDeploymentDir + File.separator + sClientJarName);

    Log.println("sApp = " + sApp);

    if (bGenerateSQL != null) {
      if (bGenerateSQL.equals("true")) {
        try {
          deployTool.doGenerateSQL(sRIAppName, sDeployHost, false);
        } catch (Exception e) {
          System.err.println("Failed to generate SQL:  " + e.getMessage());
          e.printStackTrace();
          throw new TSDeploymentException(e.getMessage());
        }
      }
    }

    String sApplicationName = "";

    try {

      fRIEarFile = new File(sRIAppName);
      sApplicationName = com.sun.enterprise.deployment.archivist.ApplicationArchivist
          .getApplicationName(fRIEarFile);
      TestUtil.logHarness("Just got app named:  " + sApplicationName);
    } catch (Exception ioe) {
      System.err.println(ioe.getMessage());
      ioe.printStackTrace(Log);
      throw new TSDeploymentException(ioe.getMessage());
    }

    try {
      // TestUtil.logHarness("About to check if app is installed: " +
      // sApplicationName);

      if (deployTool.getServerManager().isInstalled(sApplicationName,
          sDeployHost)) {
        TestUtil.logHarness(
            "App named :  " + sApplicationName + "is currently installed.");

        // check here for developer flag to disable undeploy/redeploy
        bDeployFlag = (Boolean
            .valueOf(propMgr.getProperty("undeploy_redeploy_apps")))
                .booleanValue();

        if (!bDeployFlag) {
          TestUtil.logHarness(
              "Leaving current app deployed - will not redeploy app");
          return clientClassesFile.toString();
        } else {
          System.err.println(sApplicationName
              + " must be uninstalled before it can be re-deployed.");
          throw new TSDeploymentException(sApplicationName
              + " must be uninstalled before it can be re-deployed.");
        }
      }

      // use the old RI api still because we're so close to FCS
      // deployTool.deploy(toDeploy, sDeployHost, null, clientClassesFile);
      // TestUtil.logHarness("About to deploy: " + sApplicationName);

      deployTool.deploy(sApplicationName, fRIEarFile, sDeployHost, null,
          clientClassesFile);
      // TestUtil.logHarness("Completed deploy call: " + sApplicationName);

    } catch (MalformedURLException t) {
      System.err.println(t.getMessage());
      t.printStackTrace(Log);
      throw new TSDeploymentException(t.getMessage());
    } catch (Throwable t) {
      System.err.println(t.getMessage());
      t.printStackTrace(Log);
      t.printStackTrace();
      throw new TSDeploymentException(t.getMessage());
    }

    return clientClassesFile.toString();
  }

  /**
   * This method is called by the test harness to check whether or not an
   * application ear is deployed. This information is used to determine whether
   * or not the harness needs to undeploy it. The following properties are
   * available for this method's use:
   *
   * ear_file - the fully qualified application (.ear file) deployment_host -
   * the host where this app is deployed
   *
   * @param p
   *          Properties specific to the currently running test
   * @return True if the app is deployed. False if not.
   */
  public boolean isDeployed(Properties p) throws TSDeploymentException {
    boolean bIsInstalled = false;
    String sDeployHost = p.getProperty("deployment_host");
    String sApp = p.getProperty("ear_file");
    Application toDeploy = null;
    sRIAppName = sHarnessTempDir + File.separator + sImplID
        + (new File(sApp)).getName();

    TestUtil.logHarnessDebug("Deployhost = " + sDeployHost);
    String sApplicationName = "";

    try {
      TestUtil.logHarnessDebug("isdeploy called with: " + sApp);

      File fApp = new File(sRIAppName);

      // if the ri ear does not exist, use the portable ear
      if (!fApp.exists()) {
        fApp = new File(sApp);
        TestUtil.logHarness("Using app from dist directory");
      }
      TestUtil.logHarnessDebug("isdeploy called with: " + sApp);

      TestUtil.logHarness("Before getting app name");
      sApplicationName = com.sun.enterprise.deployment.archivist.ApplicationArchivist
          .getApplicationName(fApp);
      TestUtil.logHarness(
          "After getting app name from archivist:  " + sApplicationName);

      htDeployedApps.put(fApp.getName(), sApplicationName);
    } catch (Exception ioe) {
      Log.println(
          "Failed while checking if app was already deployed:  " + sRIAppName);
      ioe.printStackTrace(Log);
      ioe.printStackTrace();
      throw new TSDeploymentException(ioe.getMessage());
    }

    try {
      if (deployTool.getServerManager().isInstalled(sApplicationName,
          sDeployHost)) {
        bIsInstalled = true;
      }
    } catch (Exception t) {
      Log.println(
          "Failed while checking if app was already deployed:  " + sApp);
      t.printStackTrace();
      throw new TSDeploymentException(t.getMessage());
    }

    TestUtil.logHarness("isdeploy returning : " + bIsInstalled);

    return bIsInstalled;
  }

  /**
   * This method is called by test harness to undeploy an .ear file from Sun's
   * Java EE reference implementation. We extract such info as host and app from
   * these props. The following properties are available for this method 's use:
   *
   * ear_file - the fully qualified application (.ear file) deployment_host -
   * the host to undeploy this app from
   *
   * @param p
   *          Properties specific to the currently running test
   */
  public void undeploy(Properties p) throws TSDeploymentException {
    String sDeployHost = p.getProperty("deployment_host");
    String sApp = p.getProperty("ear_file");
    String sAppName = (new File(sApp)).getName();
    String sTSDeploymentDir = sApp.substring(0,
        sApp.lastIndexOf(File.separator) + 1) + "ts_dep";
    File ctsDeployDir = new File(sTSDeploymentDir);
    boolean bDeployFlag = true;
    sRIAppName = sHarnessTempDir + File.separator + sImplID
        + (new File(sApp)).getName();

    // remove any client jars if they exist
    if (ctsDeployDir.exists()) {
      File[] fclientFiles = ctsDeployDir.listFiles();
      for (int ii = 0; ii < fclientFiles.length; ii++) {
        if (fclientFiles[ii].isFile()) {
          if (fclientFiles[ii].delete())
            Log.println("Old Client Jar file, " + fclientFiles[ii].getName()
                + ", was successfully deleted.");
          else
            Log.println("Failed to delete old Client Jar file, "
                + fclientFiles[ii].getName() + ".");
        }
      }
    }

    sAppName = (String) htDeployedApps.get(sAppName);
    TestUtil.logHarnessDebug("About to try to undeploy Appname:  " + sAppName);

    try {
      // check here for developer flag to disable undeploy/redeploy
      bDeployFlag = (Boolean
          .valueOf(propMgr.getProperty("undeploy_redeploy_apps")))
              .booleanValue();

      if (!bDeployFlag) {
        TestUtil.logHarness(
            "Leaving current app deployed - app will not be undeployed");
        return;
      }

      if (deployTool.getServerManager().isInstalled(sAppName, sDeployHost)) {
        deployTool.getServerManager().undeployApplication(sAppName,
            sDeployHost);
        Log.print(sAppName + " was uninstalled from " + sDeployHost);
        TestUtil.logHarness(sAppName + " was uninstalled from " + sDeployHost);
      } else {
        TestUtil.logHarnessDebug("Undeploy called with jar/war: " + sApp);
        TestUtil
            .logHarnessDebug(sAppName + " is not deployed on " + sDeployHost);
        Log.print(sAppName + " is not deployed on " + sDeployHost);
        throw new TSDeploymentException(
            sAppName + " is not deployed on " + sDeployHost);
      }

      // remove temporary ri .ear file
      // if(sApp.endsWith(".ear"))
      (new File(sRIAppName)).delete();
    } catch (Exception e) {
      Log.print("Undeployment failed!");
      e.printStackTrace(Log);
      e.printStackTrace();
      Log.print(e.getMessage());
      throw new TSDeploymentException(e.getMessage());
    }
    return;
  }

  /**
   * This method is called to deploy a connector (.rar file) to Sun's Java EE
   * reference implementation. We extract such info as deployment_host and
   * rar_file from these props. The following properties are available for this
   * method's use:
   *
   * rar_file - the fully qualified connector file (.rar file) deployment_host -
   * the host name of the machine to deploy it to
   *
   * @param p
   *          Properties specific to the currently running test
   */

  public void deployConnector(Properties p) throws TSDeploymentException {
    String sDeployHost = p.getProperty("deployment_host");
    String sRarFileName = p.getProperty("rar_file");

    try {

      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();
      String sRarPropName = sRarFileName.substring(
          sRarFileName.lastIndexOf(File.separator) + 1,
          sRarFileName.lastIndexOf(".rar"));
      String sRarName = sRarFileName
          .substring(sRarFileName.lastIndexOf(File.separator) + 1);
      String username = propMgr.getProperty("user1");
      String password = propMgr.getProperty("password1");

      // Following to be used by JDBC RA
      String connectionURL = propMgr.getProperty("connector_connectionURL");
      String xaDataSource = propMgr
          .getProperty("porting.ts.connector.xaDataSource.class");
      String serverName = propMgr.getProperty("connector_serverName");
      String portNumber = propMgr.getProperty("connector_portNumber");
      String sid = propMgr.getProperty("connector_sid");
      String dbName = propMgr.getProperty("connector_dbName");

      // First, deploy the adapter using the deploytool
      String args[] = new String[3];
      args[0] = "-deployConnector";
      args[1] = sRarFileName;
      args[2] = sDeployHost;

      TestUtil.logHarnessDebug("deployConnector: calling deploytool: " + args[0]
          + " " + args[1] + " " + args[2]);
      com.sun.enterprise.tools.deployment.main.Main.internalMain(args);
      TestUtil.logHarness("Connector:  " + sRarFileName + " has been deployed");

      // Differentiate betweenn the JDBC Resource Adapter and TS EIS Resource
      // ADapter. Initialise properties accordingly.
      // Create connection factories using admintool
      // XA rar files have XADataSource, ServerName, PortNumber overridden, and
      // tx files
      // have ConnectionURL overridden.

      if (sRarPropName.startsWith("JDBC")) {
        String sJNDIName = propMgr.getProperty(sRarPropName);
        sJNDIName = sJNDIName.substring(("java:comp/").length());

        String ra_args[] = new String[7];
        ra_args[0] = "-addConnectorFactory";
        ra_args[1] = sJNDIName;
        ra_args[2] = sRarName;
        ra_args[3] = username;
        ra_args[4] = password;
        ra_args[5] = "-props";
        ra_args[6] = "ConnectionURL=" + connectionURL;

        if (sRarName.indexOf("xa") > 0) {

          // String xara_args[] = new String[5];
          String xara_args[] = new String[11];
          xara_args[0] = "-addConnectorFactory";
          xara_args[1] = sJNDIName;
          xara_args[2] = sRarName;
          xara_args[3] = username;
          xara_args[4] = password;
          xara_args[5] = "-props";
          xara_args[6] = "XADataSourceName=" + xaDataSource;
          xara_args[7] = "ServerName=" + serverName;
          xara_args[8] = "PortNumber=" + portNumber;
          xara_args[9] = "SID=" + sid;
          xara_args[10] = "DbName=" + dbName;

          TestUtil.logHarnessDebug("deployConnector: calling admintool: "
              + xara_args[0] + " " + xara_args[1] + " " + xara_args[2] + " "
              + xara_args[3] + " " + xara_args[4] + " " + xara_args[5] + " "
              + xara_args[6] + " " + xara_args[7] + " " + xara_args[8] + " "
              + xara_args[9] + " " + xara_args[10]);
          com.sun.enterprise.tools.admin.AdminTool.main(xara_args);
          TestUtil.logHarness(
              "XA Connection Factory created with JNDI name:  " + xara_args[1]);

        } else {

          TestUtil.logHarnessDebug(
              "deployConnector: calling admintool: " + ra_args[0] + " "
                  + ra_args[1] + " " + ra_args[2] + " " + ra_args[3] + " "
                  + ra_args[4] + " " + ra_args[5] + " " + ra_args[6]);
          com.sun.enterprise.tools.admin.AdminTool.main(ra_args);
          TestUtil.logHarness(
              "Connection Factory created with JNDI name:  " + ra_args[1]);
        }

      } else { // start of TSEIS

        String sJNDIName = propMgr.getProperty(sRarPropName);
        sJNDIName = sJNDIName.substring(("java:comp/").length());
        String ra_args[] = new String[5];
        ra_args[0] = "-addConnectorFactory";
        ra_args[1] = sJNDIName;
        ra_args[2] = sRarName;
        ra_args[3] = username;
        ra_args[4] = password;

        if (sRarName.indexOf("xa") > 0) {
          String xara_args[] = new String[5];
          xara_args[0] = "-addConnectorFactory";
          xara_args[1] = sJNDIName;
          xara_args[2] = sRarName;
          xara_args[3] = username;
          xara_args[4] = password;

          TestUtil.logHarnessDebug("deployConnector: calling admintool: "
              + xara_args[0] + " " + xara_args[1] + " " + xara_args[2] + " "
              + xara_args[3] + " " + xara_args[4]);
          com.sun.enterprise.tools.admin.AdminTool.main(xara_args);
          TestUtil.logHarness(
              "XA Connection Factory created with JNDI name:  " + xara_args[1]);

        } else {
          TestUtil.logHarnessDebug("deployConnector: calling admintool: "
              + ra_args[0] + " " + ra_args[1] + " " + ra_args[2] + " "
              + ra_args[3] + " " + ra_args[4]);
          com.sun.enterprise.tools.admin.AdminTool.main(ra_args);
          TestUtil.logHarness(
              "Connection Factory created with JNDI name:  " + ra_args[1]);

        }
      } // end of TSEIS
    } catch (Exception e) {
      TestUtil
          .logHarness("Deployment of Connector:  " + sRarFileName + " failed");
      e.printStackTrace();
      throw new TSDeploymentException(e.getMessage());
    }
  }

  /**
   * This method is called to undeploy a connector (.rar file) from Sun's Java
   * EE reference implementation. We extract such info as deployment_host and
   * rar_file from these props. The following properties are available for this
   * method's use:
   *
   * rar_file - the fully qualified connector file (.rar file) deployment_host -
   * the host name of the machine to undeploy it from
   *
   * @param p
   *          Properties specific to the currently running test
   */
  public void undeployConnector(Properties p) throws TSDeploymentException {
    String sDeployHost = p.getProperty("deployment_host");
    String sRarFileName = p.getProperty("rar_file");

    try {
      String sRarName = sRarFileName
          .substring(sRarFileName.lastIndexOf(File.separator) + 1);
      String sRarPropName = sRarFileName.substring(
          sRarFileName.lastIndexOf(File.separator) + 1,
          sRarFileName.lastIndexOf(".rar"));

      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();

      // remove connection factory for the resource adatper

      String sJNDIName = propMgr.getProperty(sRarPropName);
      sJNDIName = sJNDIName.substring(("java:comp/").length());

      TestUtil.logHarness("sJNDIName is :  " + sJNDIName);

      String args2[] = new String[2];
      args2[0] = "-removeConnectorFactory";
      args2[1] = sJNDIName;
      TestUtil.logHarnessDebug(
          "In SunRIDeploy: undeployConnector: calling with args: " + args2[0]
              + " " + args2[1]);
      com.sun.enterprise.tools.admin.AdminTool.main(args2);
      TestUtil.logHarness(
          "Connection Factory with JNDI name:  " + args2[1] + " was removed.");

      TestUtil.logHarnessDebug("In SunRIDeploy: undeployConnector");
      String args[] = new String[3];
      args[0] = "-undeployConnector";
      args[1] = sRarName;
      args[2] = sDeployHost;
      TestUtil.logHarnessDebug(
          "In SunRIDeploy: undeployConnector: calling with args: " + args[0]
              + " " + args[1] + " " + args[2]);
      com.sun.enterprise.tools.deployment.main.Main.internalMain(args);
      TestUtil.logHarnessDebug(
          "Connector:  " + sRarFileName + " has been undeployed");

    } catch (Exception e) {
      TestUtil.logHarness(
          "Undeployment of Connector:  " + sRarFileName + " failed");
      e.printStackTrace();
      throw new TSDeploymentException(e.getMessage());
    }
  }

  /**
   * This method is called to check to see if a given connector (.rar file) is
   * deployed on Sun's Java EE reference implementation. We extract such info as
   * deployment_host and rar_file from these props. The following properties are
   * available for this method's use:
   *
   * rar_file - the fully qualified connector file (.rar file) deployment_host -
   * the host name of the machine to deploy it to
   *
   * @param p
   *          Properties specific to the currently running test
   * @return True if the app is deployed. False if not.
   */
  public boolean isConnectorDeployed(Properties p)
      throws TSDeploymentException {
    String sDeployHost = p.getProperty("deployment_host");
    String sRarFileName = p.getProperty("rar_file");
    boolean bIsInstalled = false;

    try {
      TestUtil.logHarnessDebug("In SunRIDeploy: isConnectorDeployed");
      JarInstaller backend = deployTool.getServerManager()
          .getServerForName(sDeployHost);
      ConnectorInfo v = backend.listConnectors();

      int size = v.connectors.length;

      for (int ii = 0; ii < size; ii++) {
        String msg = "connector.info:  " + (String) v.connectors[ii];
        TestUtil.logHarnessDebug(msg);

        String sRarName = sRarFileName
            .substring(sRarFileName.lastIndexOf(File.separator) + 1);
        TestUtil.logHarnessDebug(
            "In SunRIDeploy: isConnectorDeployed :  sRarName = " + sRarName);
        if (((String) v.connectors[ii]).equals(sRarName)) {
          bIsInstalled = true;
          TestUtil.logHarnessDebug("Found connector!");
          break;
        }
      }
      TestUtil.logHarnessDebug(
          "In SunRIDeploy: isConnectorDeployed:  returning" + bIsInstalled);
      return bIsInstalled;
    } catch (Exception e) {
      TestUtil.logHarness(
          "Checking if connector:  " + sRarFileName + " is deployed - failed");
      e.printStackTrace();
      throw new TSDeploymentException(e.getMessage());
    }
  }

  /**
   * This method is called by the test harness to get any additional test
   * specific arguments that must be passed to the application client container
   * class, which is specified in the ts.jte file in the given environment
   * (command.testExecuteAppClient property). The additional args should be
   * appended to the value of (p.getProperty("executeArgs");), and returned. The
   * following properties are available for this method's use:
   *
   * executeArgs - the current executeArgs as specified in the jte file
   *
   * @param p
   *          Properties specific to the currently running test
   * @return This method should return a string which represents all executeArgs
   *         to be used.
   */
  public String getAppClientArgs(Properties p) {
    String executeArgs = p.getProperty("executeArgs");
    String sApp = p.getProperty("ear_file");
    String sEarName = sApp.substring(sApp.lastIndexOf(File.separator) + 1);
    sRIAppName = sHarnessTempDir + File.separator + sImplID + sEarName;
    String sClientClasspath = chooseValidStub(sEarName,
        p.getProperty("clientClasspath"));

    if (executeArgs == null)
      executeArgs = "-client " + sRIAppName + " -name "
          + p.getProperty("client_name") + " -stubs " + sClientClasspath;
    else
      executeArgs += " -client " + sRIAppName + " -name "
          + p.getProperty("client_name") + " -stubs " + sClientClasspath;

    // reset the property to nothing so it isn't added to the classpath
    p.put("clientClasspath", "");

    return executeArgs;
  }

  public Hashtable getInteropJNDINames(DeploymentInfo[] infoArray) {
    htJNDIRefs = new Hashtable();

    // if((System.getProperty("cts.harness.debug", "false")).equals("true"))
    if (TestUtil.harnessDebug) {
      for (int ii = 0; ii < infoArray.length; ii++) {
        TestUtil.logHarnessDebug("**** ii=" + ii
            + " ***********************************************************************************");
        TestUtil
            .logHarnessDebug(new RuntimeInfo().getRuntimeInfo(infoArray[ii]));
        TestUtil.logHarnessDebug(
            "***************************************************************************************");

        Vector resources = infoArray[ii].getAppClientResources();
        for (int i = 0; i < resources.size(); i++) {
          DeploymentInfo.AppClient appResource = (DeploymentInfo.AppClient) resources
              .elementAt(i);

          Hashtable jndiChanges = checkJNDINames(appResource.ejbs);
          if (!jndiChanges.isEmpty()) {
            htJNDIRefs.putAll(jndiChanges);
          }
        }

        /* Web Container */
        resources = infoArray[ii].getWebResources();
        for (int i = 0; i < resources.size(); i++) {
          DeploymentInfo.WebResource webResource = (DeploymentInfo.WebResource) resources
              .elementAt(i);

          Hashtable jndiChanges = checkJNDINames(webResource.ejbs);
          if (!jndiChanges.isEmpty()) {
            htJNDIRefs.putAll(jndiChanges);
          }
        }

        /* EJB Container */
        Vector ejbJars = infoArray[ii].getEjbJars();
        for (int y = 0; y < ejbJars.size(); y++) {
          DeploymentInfo.EJBJar ejbJar = (DeploymentInfo.EJBJar) ejbJars
              .elementAt(y);
          resources = ejbJar.getEjbResources();
          for (int i = 0; i < resources.size(); i++) {
            DeploymentInfo.Ejb ejbResource = (DeploymentInfo.Ejb) resources
                .elementAt(i);

            Hashtable jndiChanges = checkJNDINames(ejbResource.ejbs);
            if (!jndiChanges.isEmpty()) {
              htJNDIRefs.putAll(jndiChanges);
            }
          }
        }

      }
    }

    // TestUtil.logHarnessDebug("*****Returning the following Hashtable from
    // SunRIDeployment.getInteropJNDINames*****");
    // TestUtil.logHarnessDebug("***************************************************************************************");

    for (Enumeration e = htJNDIRefs.keys(); e.hasMoreElements();) {
      String sKey = (String) e.nextElement();

      TestUtil.logHarnessDebug("Original value:  " + sKey);
      TestUtil
          .logHarnessDebug("Modified value:  " + (String) htJNDIRefs.get(sKey));
    }
    // TestUtil.logHarnessDebug("***************************************************************************************");

    return htJNDIRefs;
  }

  private Hashtable checkJNDINames(Vector ejbRefs) {
    Hashtable jndiChanges = new Hashtable();
    for (int j = 0; j < ejbRefs.size(); j++) {
      DeploymentInfo.EjbReference ref = (DeploymentInfo.EjbReference) ejbRefs
          .elementAt(j);
      if (ref.jndi.startsWith("corbaname")) {
        int endPos = ref.jndi.lastIndexOf("#");
        // String newJNDI = ref.jndi.substring(0,endPos) + "#myfoo/" +
        String newJNDI = ref.jndi.substring(0, endPos) + "#"
            + ref.jndi.substring(endPos + 1);
        jndiChanges.put(ref.jndi, newJNDI);
        TestUtil.logHarnessDebug(
            "********\nold JNDI name" + ref.jndi + ", new JNDI name" + newJNDI);
      }
    }
    return jndiChanges;
  }

  /**
   * chooses a stub jar that belongs to the current vehicle test ear from a list
   * of stub jars separated by file.pathSeparator. This step is necessary for
   * vehicle tests only where multiple stub jars have been appended to the
   * clientClasspath. RI appclient container takes runtime info from the first
   * element in the stubs argument.
   * 
   * @param earName
   *          name of the ear file, not the full path. For example,
   *          testConn_ejb_vehicle.ear
   * @param clientcp
   *          client classpath (a list of stub jars)
   * @return full path to a single stub jar that belongs to the current vehicle
   *         test ear
   *
   */
  protected String chooseValidStub(String earName, String clientcp) {
    if (clientcp == null) {
      return "";
    }
    // do nothing if not vehicle test 8/7/2002
    // other non-vehicle test directories may also have multiple appclients
    // so we check all types. 8/8/2002
    // if(earName.lastIndexOf("_vehicle") == -1) {
    // return clientcp;
    // }
    int pos = earName.lastIndexOf(".ear");
    String earBaseName = null;
    if (pos != -1) {
      earBaseName = earName.substring(0, pos);
    } else {
      earBaseName = earName;
    }
    StringTokenizer st = new StringTokenizer(clientcp, File.pathSeparator);
    String token = null;
    while (st.hasMoreTokens()) {
      token = st.nextToken();
      if (token.lastIndexOf(earBaseName) != -1) {
        return token;
      }
    }
    StringBuffer msgBuffer = new StringBuffer(600);
    msgBuffer.append("Could not choose a valid stub jar for ear: ")
        .append(earName);
    msgBuffer.append(" from existing clientClasspath: ").append(clientcp);
    msgBuffer.append(". Will use the existing one.");
    TestUtil.logHarness(msgBuffer.toString());
    return clientcp;
  }

}
