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

package com.sun.ts.lib.implementation.sun.javaee.glassfish;

import org.apache.tools.ant.*;

import java.io.*;
import java.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.deliverable.*;
import org.glassfish.deployment.client.*;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;
import com.sun.ts.lib.implementation.sun.javaee.RuntimeInfo;

/**
 *
 * This class implements the TSDeploymentInterface. It does so by copying
 * to/from the V3 autodeploy directory and delegates out to ant build files
 * under bin/xml/glassfish/deploy.xml
 *
 * @author Kyle Grucci
 *
 */
public class AutoDeployment implements TSDeploymentInterface {

  protected static final String DEPLOYED_MODULES_FILE = "ts-deployed-modules";

  protected static String TEMP_DIR;

  protected static String SERVER_LOG = "server.log";

  private static String newLine = System.getProperty("line.separator", "\n"); // used
                                                                              // for
                                                                              // log
                                                                              // messages

  protected PrintWriter log; // Log to harness

  protected int iPortingSet = 1; // Porting set using this impl
                                 // of TSDeploymentInterface

  private String url = "deployer:???:???:999"; // URI for DM

  protected Hashtable htJNDIRefs = null;

  // TSDeploymentInterface2
  protected Hashtable htDeployedModules = // Map ear files to IDs so we
      new Hashtable(); // can later undeploy

  protected PropertyManagerInterface propMgr; // Properties from ts.jte

  protected String deployStateFile;

  private String sDepNumber = "1";
  /*
   * 1. Need to handle common apps, we can do this programatically
   * 
   * 
   * 
   * /
   * 
   * /**
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
   * @param writer
   *          PrintWriter for harness tracing
   *
   * @return void
   *
   */
  @Override
  public void init(PrintWriter writer) {

    this.log = writer;
    TestUtil.logHarness("AutoDeployment.init()");

    iPortingSet = TSDeployment.iPortingSet;
    TestUtil.logHarness("AutoDeployment:  Using porting set #" + iPortingSet);

    try {
      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();

      String portingString = String.valueOf(iPortingSet);
      // initDeployTestUtils(portingString);

      String portClass = "porting.ts.deploy2.class." + iPortingSet;
      TestUtil.logHarness("Using " + portClass);

    } catch (Exception e) {
      e.printStackTrace();
      TestUtil.logHarness(
          "Creation of TSDeployment2 implementation instance failed."
              + " Please check the values of 'porting.ts.deploy2.class.1' and"
              + " 'porting.ts.deploy2.class.2'");
    }
  }

  private void antSetup(String archive, Project p, String propsNumber) {
    String tsHome = propMgr.getProperty("ts.home", "/");
    String pkgNameMinusArchive = archive.substring(
        archive.indexOf(File.separator + "dist" + File.separator) + 6,
        archive.lastIndexOf(File.separator));
    // String sBuildFile = tsHome + File.separator + "src" + File.separator +
    // pkgNameMinusArchive + File.separator + "build.xml";

    String deployHandlerBuildFile;

    if (iPortingSet == 1) {
      p.setProperty("deploy.dir",
          propMgr.getProperty("impl.vi.deploy.dir", ""));
      deployHandlerBuildFile = tsHome + File.separator + "bin" + File.separator
          + "xml" + File.separator + "impl" + File.separator
          + propMgr.getProperty("impl.vi", "glassfish") + File.separator
          + "deploy.xml";
    } else {
      p.setProperty("deploy.dir",
          propMgr.getProperty("impl.ri.deploy.dir", ""));
      deployHandlerBuildFile = tsHome + File.separator + "bin" + File.separator
          + "xml" + File.separator + "impl" + File.separator
          + propMgr.getProperty("impl.ri", "glassfish") + File.separator
          + "deploy.xml";
    }

    TestUtil.logHarnessDebug(
        "antsetup:  deploy.dir = " + p.getProperty("deploy.dir"));

    File buildFile = new File(deployHandlerBuildFile);
    p.setUserProperty("ant.file", buildFile.getAbsolutePath());
    p.init();
    ProjectHelper helper = ProjectHelper.getProjectHelper();
    p.addReference("ant.projectHelper", helper);
    helper.parse(p, buildFile);

    DefaultLogger consoleLogger = new DefaultLogger();
    consoleLogger.setErrorPrintStream(System.err);
    consoleLogger.setOutputPrintStream(System.out);
    consoleLogger.setMessageOutputLevel(Project.MSG_INFO);
    p.addBuildListener(consoleLogger);
  }

  private void antSetupFromInfo(DeploymentInfo info, Project p) {
    String archive = info.getEarFile();
    sDepNumber = info.getProperty("deployment.props.number");

    p.setProperty("archive.file", archive);
    antSetup(archive, p, sDepNumber);
  }

  private void antSetupFromProps(Properties props, Project p) {
    String archive = props.getProperty("ear_file");
    sDepNumber = props.getProperty("deployment.props.number");

    p.setProperty("archive.file", archive);
    antSetup(archive, p, sDepNumber);
  }

  @Override
  public String deploy(DeploymentInfo info) throws TSDeploymentException {

    // what about interop - do we need to autodeploy directories
    // we need to determine if the archives are interop or rebuildable and set
    // the
    // correct props
    Project p = new Project();
    antSetupFromInfo(info, p);

    try {

      p.executeTarget("-deploy");
    } catch (Throwable t) {
      log("Deployment failed for: " + info.getEarFile());
      t.printStackTrace();
      throw new TSDeploymentException(t.getMessage()
          + System.getProperty("line.separator") + getServerLogContents());
    }

    // will I need to make a call here to get the location of the client jar, if
    // any?

    // check if we have an appclient jar, and get stups
    // check if we have an ear with appclient jar
    // if either are not ture, then don't get stubs from server

    String javaeeLevel = propMgr.getProperty("javaee.level", "full");

    if (javaeeLevel.contains("full")) {
      getClientClassPath(info);
    }

    return "";

  }

  @Override
  public void undeploy(Properties p) throws TSDeploymentException {
    // String sArchive = p.getProperty("ear_file");
    TestUtil.logHarness("AutoDeployment.undeploy()");
    // initDeployTestUtils(p.getProperty("deployment.props.number"));

    Project pr = new Project();
    antSetupFromProps(p, pr);

    try {
      pr.executeTarget("-undeploy");
    } catch (Throwable t) {
      log("Undeployment failed for: " + p.getProperty("ear_file"));
      t.printStackTrace();
      throw new TSDeploymentException(t.getMessage()
          + System.getProperty("line.separator") + getServerLogContents());
    }
  }

  @Override
  public boolean isDeployed(Properties p) throws TSDeploymentException {

    sDepNumber = p.getProperty("deployment.props.number");
    String sArchive = p.getProperty("ear_file");
    TestUtil.logHarness("AutoDeployment.isDeployed()");
    String archiveNameOnly = sArchive
        .substring(sArchive.lastIndexOf(File.separator) + 1);
    TestUtil.logHarnessDebug("$$$$$$$$$$$$$ " + archiveNameOnly);
    String autoDeployDir;

    if (archiveNameOnly.startsWith("vi_built_")) {
      archiveNameOnly = archiveNameOnly.substring(9);
    }

    if (iPortingSet == 1) {
      autoDeployDir = propMgr.getProperty("impl.vi.deploy.dir",
          "UNSET glassfish.deploy.dir");
    } else {
      autoDeployDir = propMgr.getProperty("impl.ri.deploy.dir",
          "UNSET glassfish.deploy.dir");
    }

    TestUtil.logHarnessDebug("isdeployed:  deploy.dir = " + autoDeployDir);

    if ((new File(
        autoDeployDir + File.separator + archiveNameOnly + "_deployed"))
            .exists()) {
      TestUtil.logHarnessDebug("isdeployed:  returning true");
      return true;
    } else {
      TestUtil.logHarnessDebug("isdeployed:  returning false");
      return false;
    }
  }

  @Override
  public void deployConnector(Properties p) throws TSDeploymentException {

    TestUtil.logHarness("AutoDeployment.deployConnector()");

    p.setProperty("ear_file", p.getProperty("rar_file"));

    Project pr = new Project();
    antSetupFromProps(p, pr);

    try {
      pr.executeTarget("-deploy");
    } catch (Throwable t) {
      t.printStackTrace();
      throw new TSDeploymentException(t.getMessage());
    }

  }

  @Override
  public void undeployConnector(Properties p) throws TSDeploymentException {
    TestUtil.logHarness("AutoDeployment.undeployConnector()");
    p.setProperty("ear_file", p.getProperty("rar_file"));
    undeploy(p);
  }

  @Override
  public boolean isConnectorDeployed(Properties p)
      throws TSDeploymentException {
    TestUtil.logHarness("AutoDeployment.isConnectorDeployed()");
    p.setProperty("ear_file", p.getProperty("rar_file"));
    return isDeployed(p);
  }

  @Override
  public String getAppClientArgs(Properties p) {

    String sAppName = null;
    String executeArgs = p.getProperty("executeArgs");
    String sApp = p.getProperty("ear_file");
    String sClientname = p.getProperty("client_name");

    if (sApp.endsWith(".ear")) { // Check for an application-name in the
                                 // application.xml
      sAppName = getAppNameFromApplicationXML(sApp);
    }

    String sTSDeploymentDir = sApp.substring(0,
        sApp.lastIndexOf(File.separator) + 1) + "ts_dep";

    if (sAppName == null) { // if we didn't have an ear or there was no
                            // application-name use the old scheme
      sAppName = sApp.substring(sApp.lastIndexOf(File.separator) + 1,
          sApp.lastIndexOf("."));
    }

    if (sAppName.startsWith("vi_built_")) {
      // strip off the vi_built_ string
      sAppName = sAppName.substring(9);
      sTSDeploymentDir = sApp.substring(0, sApp.lastIndexOf(File.separator) + 1)
          + "ts_dep_vi_built";
      sClientname = sClientname.substring(9);
    }

    sApp = sTSDeploymentDir + File.separator + sAppName + "Client.jar";

    if (executeArgs == null) {
      executeArgs = sApp + ",arg=-name,arg=" + sClientname + " -jar " + sApp; // +
                                                                              // "
                                                                              // -stubs
                                                                              // "
                                                                              // +
                                                                              // sClientClasspath;
    } else {
      executeArgs = sApp + ",arg=-name,arg=" + sClientname + " -jar " + sApp
          + " " + executeArgs; // + " -name " + p.getProperty("client_name"); //
                               // + " -stubs " + sClientClasspath;
    }
    // reset the property to nothing so it isn't added to the classpath
    // p.put("clientClasspath", "");
    return executeArgs;

  }

  public Hashtable getInteropJNDINames(DeploymentInfo[] infoArray) {
    htJNDIRefs = new Hashtable();
    StringBuffer buf = null;
    if (TestUtil.harnessDebug) {
      for (int ii = 0; ii < infoArray.length; ii++) {
        buf = new StringBuffer("**** ii=" + ii
            + " ********************************************" + newLine);
        buf.append(
            "***************************************************************************************"
                + newLine);
        buf.append(
            "***************************************************************************************"
                + newLine);
        buf.append((new RuntimeInfo().getRuntimeInfo(infoArray[ii])) + newLine);
        buf.append(
            "***************************************************************************************"
                + newLine);
        buf.append(
            "***************************************************************************************"
                + newLine);
        buf.append(
            "***************************************************************************************"
                + newLine);
        TestUtil.logHarnessDebug(buf.toString());

        // 1.3 version of runtime paring code
        // Vector resources =
        // infoArray[ii].getAppClientResources();
        // for(int i = 0; i< resources.size(); i++) {
        // DeploymentInfo.AppClient appResource =
        // (DeploymentInfo.AppClient)
        // resources.elementAt(i);
        // Hashtable jndiChanges =
        // checkJNDINames(appResource.ejbs);
        // if(!jndiChanges.isEmpty()) {
        // htJNDIRefs.putAll(jndiChanges);
        // }
        // }
        List resources = infoArray[ii].getAppClientRuntimeDDs();
        for (int i = 0; i < resources.size(); i++) {
          com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.SunApplicationClient appResource = (com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.SunApplicationClient) resources
              .get(i);
          Hashtable jndiChanges = checkJNDINames(appResource.getEjbRef());
          if (!jndiChanges.isEmpty()) {
            htJNDIRefs.putAll(jndiChanges);
          }
        }

        // 1.3 version of runtime paring code
        // /* Web Container */
        // resources = infoArray[ii].getWebResources();
        // for(int i = 0; i< resources.size(); i++) {
        // DeploymentInfo.WebResource webResource =
        // (DeploymentInfo.WebResource)
        // resources.elementAt(i);
        // Hashtable jndiChanges =
        // checkJNDINames(webResource.ejbs);
        // if(!jndiChanges.isEmpty()) {
        // htJNDIRefs.putAll(jndiChanges);
        // }
        // }
        resources = infoArray[ii].getWebRuntimeDDs();
        for (int i = 0; i < resources.size(); i++) {
          com.sun.ts.lib.implementation.sun.javaee.runtime.web.SunWebApp webResource = (com.sun.ts.lib.implementation.sun.javaee.runtime.web.SunWebApp) resources
              .get(i);
          Hashtable jndiChanges = checkJNDINames(webResource.getEjbRef());
          if (!jndiChanges.isEmpty()) {
            htJNDIRefs.putAll(jndiChanges);
          }
        }

        // 1.3 version of runtime paring code
        // /* EJB Container */
        // Vector ejbJars = infoArray[ii].getEjbJars();
        // for(int y= 0; y< ejbJars.size(); y++) {
        // DeploymentInfo.EJBJar ejbJar =
        // (DeploymentInfo.EJBJar)ejbJars.elementAt(y);
        // resources = ejbJar.getEjbResources();
        // for(int i = 0; i< resources.size(); i++) {
        // DeploymentInfo.Ejb ejbResource =
        // (DeploymentInfo.Ejb) resources.elementAt(i);
        // Hashtable jndiChanges =
        // checkJNDINames(ejbResource.ejbs);
        // if(!jndiChanges.isEmpty()) {
        // htJNDIRefs.putAll(jndiChanges);
        // }
        // }
        // }
        // }
        List ejbJars = infoArray[ii].getEjbRuntimeDDs();
        for (int y = 0; y < ejbJars.size(); y++) {
          com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.SunEjbJar ejbJar = (com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.SunEjbJar) ejbJars
              .get(y);
          resources = ejbJar.getEnterpriseBeans().getEjb();

          for (int i = 0; i < resources.size(); i++) {
            com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.Ejb ejbResource = (com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.Ejb) resources
                .get(i);
            Hashtable jndiChanges = checkJNDINames(ejbResource.getEjbRef());
            if (!jndiChanges.isEmpty()) {
              htJNDIRefs.putAll(jndiChanges);
            }
          }
        }

      } // end for loop
    } // end if debug
    buf = new StringBuffer(
        "***** Returning the following Hashtable from SunRIDeployment.getInteropJNDINames *****"
            + newLine);
    buf.append(
        "***************************************************************************************"
            + newLine);
    for (Enumeration e = htJNDIRefs.keys(); e.hasMoreElements();) {
      String sKey = (String) e.nextElement();
      buf.append("Original value:  " + sKey + newLine);
      buf.append("Modified value:  " + (String) htJNDIRefs.get(sKey) + newLine);
    }
    buf.append(
        "***************************************************************************************"
            + newLine);
    TestUtil.logHarnessDebug(buf.toString());

    return htJNDIRefs;
  }

  private void addJNDIName(String jndiName, Hashtable jndiChanges) {
    if (jndiName.startsWith("corbaname")) {
      int endPos = jndiName.lastIndexOf("#");
      // Example JNDI Name:
      // String newJNDI = ref.jndi.substring(0, endPos) + "#myfoo/" +
      // ref.jndi.substring(endPos + 1);
      String newJNDI = jndiName.substring(0, endPos) + "#"
          + jndiName.substring(endPos + 1);
      jndiChanges.put(jndiName, newJNDI);
      TestUtil.logHarnessDebug("******** old JNDI name \"" + jndiName
          + "\", new JNDI name \"" + newJNDI + "\"");
    }
  }

  private Hashtable checkJNDINames(List ejbRefs) {
    Hashtable jndiChanges = new Hashtable();
    if (ejbRefs == null || ejbRefs.size() == 0) {
      return jndiChanges;
    }
    Object o = ejbRefs.get(0);
    if (o instanceof com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.EjbRef) {
      for (int j = 0; j < ejbRefs.size(); j++) {
        com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.EjbRef ref = (com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.EjbRef) ejbRefs
            .get(j);
        String jndiName = ref.getJndiName();
        addJNDIName(jndiName, jndiChanges);
      }
    } else if (o instanceof com.sun.ts.lib.implementation.sun.javaee.runtime.web.EjbRef) {
      for (int j = 0; j < ejbRefs.size(); j++) {
        com.sun.ts.lib.implementation.sun.javaee.runtime.web.EjbRef ref = (com.sun.ts.lib.implementation.sun.javaee.runtime.web.EjbRef) ejbRefs
            .get(j);
        String jndiName = ref.getJndiName();
        addJNDIName(jndiName, jndiChanges);
      }
    } else if (o instanceof com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.EjbRef) {
      for (int j = 0; j < ejbRefs.size(); j++) {
        com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.EjbRef ref = (com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.EjbRef) ejbRefs
            .get(j);
        String jndiName = ref.getJndiName();
        addJNDIName(jndiName, jndiChanges);
      }
    }
    return jndiChanges;
  }

  private String getAppNameFromApplicationXML(String archivePathAndName) {
    /*
     * Read the application.xml jar entry and look for the
     * /application/application-name element. If it exists return its textual
     * content else return null. Allowing users the ability to specify an
     * application name is new feature in JavaEE 6.0.
     */
    String appName = null;
    JarFile jarFile = null;
    try {
      jarFile = new JarFile(archivePathAndName);
      JarEntry appEntry = jarFile.getJarEntry("META-INF/application.xml");
      if (appEntry != null) {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(jarFile.getInputStream(appEntry));
        Element root = doc.getRootElement();
        appName = root.getChildTextNormalize("application-name",
            root.getNamespace());
      }
    } catch (Exception e) {
      // if any errors occur just log a message and return null
      TestUtil.logHarness(
          "Error parsing application.xml looking for application-name, returning null");
    } finally {
      if (jarFile != null) {
        try {
          jarFile.close();
        } catch (Exception e) {
        }
      }
    }
    TestUtil.logHarness(
        "getAppNameFromApplicationXML() returning \"" + appName + "\"");
    return appName;
  }

  /**
   * This method is called by the test harness to deploy an .ear file into Sun's
   * Java EE reference implementation. We extract such info as the app earfile
   * from the provided deployment information. The following properties are
   * available for this method's use:
   * <p>
   * generateSQL - "true" if SQL is to be generated for CMP beans
   * <p>
   * <p>
   * deployment_host - the host where this app is to be deployed
   * <p>
   *
   * All additional information is queryable from the DeploymentInfo interface.
   *
   * @param targetIDs
   *          The list of deployment target IDs.
   * @param info
   *          Object containing necessary deployment info.
   * @return This method should return a string which is formatted such that it
   *         can be appended to the classpath. This implementation returns the
   *         fully qualified path to a jar file, which contains the generated
   *         ejb stub classes, which are used by any appclient tests (tests
   *         whose client directly uses an ejb).
   */
  private String getClientClassPath(DeploymentInfo info)
      throws TSDeploymentException {
    String sAppName = null;
    String sEarFile = info.getEarFile();
    String sTSDeploymentDir = sEarFile.substring(0,
        sEarFile.lastIndexOf(File.separator) + 1) + "ts_dep";

    if (sEarFile.endsWith(".ear")) { // Check for an application-name in the
                                     // application.xml
      sAppName = getAppNameFromApplicationXML(sEarFile);
    }

    if (sAppName == null) { // if we didn't have an ear or there was no
                            // application-name use the old scheme
      sAppName = sEarFile.substring(sEarFile.lastIndexOf(File.separator) + 1,
          sEarFile.lastIndexOf("."));
    }

    if (sAppName.startsWith("vi_built_")) {
      sAppName = sAppName.substring(9);
      sTSDeploymentDir = sEarFile.substring(0,
          sEarFile.lastIndexOf(File.separator) + 1) + "ts_dep_vi_built";
    }

    File ctsDeployDir = new File(sTSDeploymentDir);

    // we should only be calling this method with archives that contain an
    // appclient

    if (!ctsDeployDir.exists()) {
      if (!ctsDeployDir.mkdir()) {
        throw new TSDeploymentException(
            "Failed to create the RI deployment working directory:  "
                + sTSDeploymentDir);
      }
    }

    String sStubJar = sTSDeploymentDir;
    TestUtil.logHarnessDebug(
        "$$$$$$$$$$$ getClientClassPath() sStubJar = \"" + sStubJar + "\"");

    try {
      String sDeploymentHost = info.getProperty("deployment_host");
      String sPropNum = info.getProperty("deployment.props.number");
      String sUname = propMgr.getProperty("deployManageruname." + sPropNum);
      String sPassword = propMgr.getProperty("deployManagerpasswd." + sPropNum,
          "");
      int iPort = Integer.parseInt(info.getProperty("deployment_port"));

      DeploymentFacility df = DeploymentFacilityFactory.getDeploymentFacility();
      ServerConnectionIdentifier sci = new ServerConnectionIdentifier();
      sci.setHostName(sDeploymentHost);
      sci.setHostPort(iPort);
      sci.setUserName(sUname);
      sci.setPassword(sPassword);

      TestUtil.logHarness("V3Deployment sPropNum = " + sPropNum);
      TestUtil.logHarness("V3Deployment uname:  " + sUname);
      TestUtil.logHarness("V3Deployment passwd:  " + sPassword);
      TestUtil.logHarness("V3Deployment host:  " + sDeploymentHost);
      TestUtil.logHarness("V3Deployment port:  " + iPort);

      // sci.setHostName("localhost");
      // sci.setHostPort(4848); // 8080 for the REST client
      // sci.setUserName("admin");
      // sci.setPassword("adminadmin");

      df.connect(sci);

      df.getClientStubs(sTSDeploymentDir, sAppName);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // TestUtil.logHarnessDebug("$$$$$$$$$$$ getClientClassPath() returning \""
    // + returnString + "\"");
    // We no longer need to return the jar file with the EJB stubs since the
    // S1AS
    // now passes the generated jar file (containing the stubs and other
    // necessary classes)
    // to the appclient Main class.
    return "";
  }

  private void log(String s) {
    log.println("GF Deployment: " + s);
  }

  /**
   * Fetch the entire contents of a text file, and return it in a String. This
   * style of implementation does not throw Exceptions to the caller.
   *
   * @param aFile
   *          is a file which already exists and can be read.
   */
  public String getServerLogContents() {
    // ...checks on aFile are elided
    StringBuilder contents = new StringBuilder();
    File serverLog;
    String aFile = "";
    int logLineCount;

    try {
      if (iPortingSet == 1) {
        serverLog = new File(
            new File(propMgr.getProperty("impl.vi.deploy.dir", "")).getParent()
                + File.separator + "logs" + File.separator + SERVER_LOG);
      } else {
        serverLog = new File(
            new File(propMgr.getProperty("impl.ri.deploy.dir", "")).getParent()
                + File.separator + "logs" + File.separator + SERVER_LOG);
      }

      logLineCount = countLines(serverLog);

      contents.append("************************************************");
      contents.append(System.getProperty("line.separator"));
      contents.append("GLASSFISH SERVER LOG CONTENTS:  Last 200 lines");
      contents.append(serverLog.getPath());
      contents.append(System.getProperty("line.separator"));
      contents.append("************************************************");
      contents.append(System.getProperty("line.separator"));

      // use buffering, reading one line at a time
      // FileReader always assumes default encoding is OK!
      BufferedReader input = new BufferedReader(new FileReader(serverLog));
      try {
        String line = null; // not declared within while loop
        /*
         * readLine is a bit quirky : it returns the content of a line MINUS the
         * newline. it returns null only for the END of the stream. it returns
         * an empty String if two newlines appear in a row.
         */
        int iCurrentLine = 1;
        System.err.println("line count = " + logLineCount);
        // only print the last 200 lines
        // start at linecount -(linecount-200)
        while ((line = input.readLine()) != null) {

          if (iCurrentLine > logLineCount - 200) {
            // log.println(line);
            contents.append(iCurrentLine + ":  " + line);
            contents.append(System.getProperty("line.separator"));
          }
          iCurrentLine++;
        }

        contents.append("************************************************");
        contents.append(System.getProperty("line.separator"));
        contents.append("END OF GLASSFISH SERVER LOG CONTENTS");
        contents.append(serverLog.getPath());
        contents.append(System.getProperty("line.separator"));
        contents.append("************************************************");
        contents.append(System.getProperty("line.separator"));

      } finally {
        input.close();
      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return contents.toString();

  }

  private int countLines(File file) throws IOException {
    Reader reader = new InputStreamReader(new FileInputStream(file));

    int lineCount = 0;
    char[] buffer = new char[4096];
    for (int charsRead = reader.read(buffer); charsRead >= 0; charsRead = reader
        .read(buffer)) {
      for (int charIndex = 0; charIndex < charsRead; charIndex++) {
        if (buffer[charIndex] == '\n')
          lineCount++;
      }
    }
    reader.close();
    return lineCount;
  }
}
