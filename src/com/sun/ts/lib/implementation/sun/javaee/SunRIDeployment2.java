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
import java.util.jar.*;
import java.util.zip.*;
import java.io.*;
import java.net.*;
import com.sun.javatest.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.deliverable.*;
import javax.enterprise.deploy.spi.*;
import javax.enterprise.deploy.spi.status.*;
import com.sun.ts.lib.implementation.sun.javaee.runtime.app.*;
import com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.*;
import com.sun.ts.lib.implementation.sun.javaee.runtime.web.*;
import com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.*;

import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import org.jdom.Element;

import org.glassfish.deployment.client.*;

/**
 * This is an implementation of the TSDeploymentInterface2. An implementation of
 * this class must be supplied by any Java EE implementation wishing to have
 * JavaTest (the harness which drives the TS tests) automatically deploy and
 * undeploy test applications through the standard Deployment API defined by
 * JSR-88. The TSDeploymentInterface2 interface allows for implementation
 * specific functionality for those operations not defined by JSR-88. This
 * particular implementation uses functionality present in Sun's Java EE 5
 * reference Impl.
 *
 * @author Kyle Grucci
 */
public class SunRIDeployment2
    implements TSDeploymentInterface2, org.xml.sax.EntityResolver {

  private static final String RESOURCE_ADAPTER_POOL_NAME = "cts-connector-pool-";

  private static final String DB_SCHEMA_FILENAME_EXT = "dbschema";

  private static final String DB_SCHEMA_FILENAME = "CtsSchema" + "."
      + DB_SCHEMA_FILENAME_EXT;

  private static final String SUN_APP_NAME = "sun-application.xml";

  private static final String SUN_CMP_MAPPING_FILE = "sun-cmp-mappings.xml";

  private static final String SUN_EJB_NAME = "sun-ejb-jar.xml";

  private static final String WINDOWS_ASADMIN_CMD = "cts-asadmin";

  private static String newLine = System.getProperty("line.separator", "\n"); // used
                                                                              // for
                                                                              // log
                                                                              // messages

  private static final String TARGET_PLACE_HOLDER = "---TARGET_PLACE_HOLDER---";

  protected PrintWriter Log = null;

  protected PropertyManagerInterface propMgr = null;

  protected Hashtable htJNDIRefs = null;

  // protected DeployTool deployTool = new DeployTool(false);
  private String sHarnessTempDir = null;

  private File dbSchemaFile;

  private static final int THREAD_TIMEOUT = 60 * 1000 * 5; // 5 minutes

  // filled in the init method, used by the createCommand routine
  private String asadminCmd;

  private String s1asUser;

  private String s1asPassword;

  private String s1asHost;

  private String s1asPort;

  private String s1asServer;

  private String[] s1asTargets;

  private String replaceTarget(String command, String target) {
    String result = command;
    int index = command.indexOf(TARGET_PLACE_HOLDER);
    if (index != -1) {
      result = command.replaceAll(TARGET_PLACE_HOLDER, target);
    }
    return result;
  }

  private boolean isWindows() {
    return System.getProperty("os.name", "").toUpperCase().startsWith("WIN");
  }

  private void runCommand(String cmd) throws Exception {
    runCommand(cmd, "S1AS Admin Command => ", false);
  }

  private void runCommand(String cmd, String display) throws Exception {
    runCommand(cmd, display, false);
  }

  private void runCommand(String cmd, String display, boolean returnResults)
      throws Exception {
    for (int i = 0; i < s1asTargets.length; i++) {
      runCommand0(replaceTarget(cmd, s1asTargets[i]), display, returnResults);
    }
  }

  private StringBuffer runCommand0(String cmd, String display,
      boolean returnResults) throws Exception {
    StringBuffer buf = new StringBuffer();
    try {
      Runtime rt = Runtime.getRuntime();
      TestUtil.logHarnessDebug(display + cmd);
      if (isWindows()) {
        cmd = "cmd /c " + cmd; // invoke a windows shell to interpret the
                               // command
      }
      Process proc = rt.exec(cmd);
      StreamWatcher errThread = new StreamWatcher(proc.getErrorStream());
      errThread.start();
      StreamWatcher outThread = new StreamWatcher(proc.getInputStream());
      outThread.start();
      int pstat = proc.waitFor();
      TestUtil.logHarnessDebug("=>Proc status:" + pstat);
      errThread.join(THREAD_TIMEOUT);
      outThread.join(THREAD_TIMEOUT);
      buf = outThread.getBuffer();

      if (outThread.getBuffer().length() > 0) {
        TestUtil.logHarnessDebug("Exec'ed Process \"" + cmd
            + "\" returned on stdout " + outThread.getBuffer());
      }
      if (errThread.getBuffer().length() > 0) {
        TestUtil.logHarnessDebug("Exec'ed Process \"" + cmd
            + "\" returned on stderr " + errThread.getBuffer());
      }
      if (pstat != 0) {
        throw new Exception("runCommand failed, command was \"" + cmd + "\"");
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      throw ex;
    }
    return buf;
  }

  class StreamWatcher extends Thread {
    private StringBuffer buf = new StringBuffer();

    private InputStream in;

    public StreamWatcher(InputStream in) {
      this.in = in;
    }

    public StringBuffer getBuffer() {
      return buf;
    }

    public void run() {
      BufferedReader breader = null;
      try {
        breader = new BufferedReader(new InputStreamReader(in));
        String line = breader.readLine();
        while (line != null) {
          buf.append(line);
          line = breader.readLine();
        }
      } catch (Exception e) {
        TestUtil.logHarness("StreamWatcher error is " + e);
      } finally {
        try {
          in.close();
        } catch (Exception e) {
        }
      }
    }
  }// end class StreamWatcher

  private String buildCommand(String command, String[] args) throws Exception {
    // could probably move these getProperty calls to init and store
    // them in instance variables.
    String ctsHome = propMgr.getProperty("ts.home");
    String binDir = propMgr.getProperty("bin.dir");
    String passwordFile = binDir + File.separator + "password.txt";
    StringBuffer cmd = new StringBuffer(100);
    cmd.append(asadminCmd).append(" ").append(command).append(" --user ")
        .append(s1asUser).append(" --passwordfile ").append(passwordFile)
        .append(" --host ").append(s1asHost).append(" --port ")
        .append(s1asPort);
    if (!command.equals("reconfig")) {
      cmd.append(" --target ").append(TARGET_PLACE_HOLDER);
    }
    int numArgs = (args == null) ? 0 : args.length;
    for (int i = 0; i < numArgs; i++) {
      cmd.append(" ").append(args[i]);
    }
    return cmd.toString();
  }

  private void reconfigAppServer() throws Exception {
    String server = propMgr.getProperty("s1as.server");
    String command = buildCommand("reconfig", new String[] { server });
    runCommand(command);
  }

  private String removeColonFromJNDI(String value) {
    String val = null;
    if (value.startsWith("java:")) {
      val = value.replaceFirst("java:", "");
    }
    return val;
  }

  private String getJndiName(String rarFilename, Properties p)
      throws Exception {
    // String rarModuleID = p.getProperty("rar_module_id");
    String rarPropName = null;
    String jndiName = "";
    if (rarFilename.endsWith(".rar")) {
      rarPropName = rarFilename.substring(0, rarFilename.lastIndexOf(".rar"));
      jndiName = propMgr.getProperty(rarPropName);
      if (jndiName.indexOf("java:comp/env/") != -1) {
        jndiName = jndiName.substring(("java:comp/env/").length());
      }
    } else if (rarFilename.equals("ejb_Deployment.ear")) {
      rarPropName = "whitebox-embed";
      // rarModuleID = rarModuleID +"_whitebox-tx.rar";
      jndiName = propMgr.getProperty(rarPropName);
      if (jndiName.indexOf("java:comp/env/") != -1) {
        jndiName = jndiName.substring(("java:comp/env/").length());
      }
    }

    return jndiName;
  }

  /**
   * Initializes a new TSDeployment instance. All output should be printed to
   * this PrintWriter. All properties in the ts.jte file are accessible to this
   * porting implementation class only via the PropertyManagerInterface. Please
   * see Sun's implementation of this method (below) for an example.
   *
   * @param writer
   *          The PrintWriter that should be used to log output.
   */
  public void init(PrintWriter out) {
    try {
      Log = out;
      TestUtil.logHarnessDebug("SunRIDeployment2:  in init");
      propMgr = DeliverableFactory.getDeliverableInstance()
          .getPropertyManager();
      String ctsHome = propMgr.getProperty("ts.home");
      dbSchemaFile = new File(ctsHome + File.separator + "sql" + File.separator
          + DB_SCHEMA_FILENAME);

      asadminCmd = propMgr.getProperty("s1as.admin");
      if (isWindows()) {
        int index = asadminCmd.lastIndexOf('/');
        if (index == -1) {
          index = asadminCmd.lastIndexOf('\\');
        }
        String path = asadminCmd.substring(0, index);
        asadminCmd = path + File.separator + WINDOWS_ASADMIN_CMD;
      }
      s1asUser = propMgr.getProperty("s1as.admin.user");
      s1asPassword = propMgr.getProperty("s1as.admin.passwd");
      s1asHost = propMgr.getProperty("s1as.admin.host");
      s1asPort = propMgr.getProperty("s1as.admin.port");
      s1asServer = propMgr.getProperty("s1as.server");
      String targets = propMgr.getProperty("s1as.targets", s1asServer);
      if (targets.equals(s1asServer)) {
        s1asTargets = new String[1];
        s1asTargets[0] = s1asServer;
      } else {
        StringTokenizer tokens = new StringTokenizer(targets, " \t\n\r\f,");
        int index = 0;
        s1asTargets = new String[tokens.countTokens()];
        while (tokens.hasMoreTokens()) {
          s1asTargets[index++] = tokens.nextToken();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    try {
      sHarnessTempDir = propMgr.getProperty("harness.temp.directory");
    } catch (PropertyNotSetException pe) {
      Log.print(pe.getMessage());
      Log.print("error looking up property:  harness.temp.directory");
      pe.printStackTrace();
    }
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
  public String getClientClassPath(TargetModuleID[] targetIDs,
      DeploymentInfo info, DeploymentManager manager)
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
      String sPassword = propMgr.getProperty("deployManagerpasswd." + sPropNum);
      int iPort = Integer.parseInt(propMgr.getProperty("webServerPort"));

      if (sPropNum.equals("2"))
        iPort = Integer.parseInt(propMgr.getProperty("webServerPort.2"));

      iPort = 4848;

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

  /**
   * This method allows vendors to choose which targets, of the available
   * targets, they would like to deploy on. For CTS we will only deploy on the
   * first available target.
   *
   * @param targets
   *          Available deployment targets
   * @param info
   *          The deployment info object containing all the S1AS runtime info
   * @return Target[] List of targets to deploy the application on.
   */
  public Target[] getTargetsToUse(Target[] targets, DeploymentInfo info) {
    List filteredTargets = new ArrayList();
    ;
    for (int jj = 0; jj < s1asTargets.length; jj++) {
      boolean foundit = false;
      for (int ii = 0; ii < targets.length; ii++) {
        if (s1asTargets[jj].equals(targets[ii].getName())) {
          filteredTargets.add(targets[ii]);
          TestUtil.logHarnessDebug(
              "$$$ Target Found \"" + targets[ii].getName() + "\"");
          foundit = true;
        }
      }
      if (!foundit) {
        TestUtil.logHarness(
            "**** ERROR, Target \"" + s1asTargets[jj] + "\" NOT Found ****");
      }
    }
    return (Target[]) (filteredTargets
        .toArray(new Target[filteredTargets.size()]));
  }

  private int getInsertPoint(org.jdom.Element elm) {
    int index = 0;
    org.jdom.Element jndiName = elm.getChild("jndi-name");
    if (jndiName != null) {
      index++;
    }
    org.jdom.Element resourcePrincipal = elm
        .getChild("default-resource-principal");
    if (resourcePrincipal != null) {
      index++;
    }
    List props = elm.getChildren("property");
    index += props.size();
    return index;
  }

  private void createElement(List elms, org.jdom.Element parent, String name,
      String value, int index) {
    org.jdom.Element child = parent.getChild(name);
    if (child == null) {
      child = new org.jdom.Element(name).setText(value);
      elms.add(index, child);
    } else {
      child.setText(value);
    }
  }

  private File writeTempXMLFile(org.jdom.Document doc, String filename)
      throws Exception {
    String path = propMgr.getProperty("harness.temp.directory") + File.separator
        + filename;
    File pathFile = new File(path);
    FileOutputStream out = new FileOutputStream(pathFile);
    org.jdom.output.XMLOutputter outputter = new org.jdom.output.XMLOutputter(
        org.jdom.output.Format.getPrettyFormat());
    outputter.output(doc, out);
    return pathFile;
  }

  private String getDBResource(org.jdom.Element elm)
      throws TSDeploymentException {
    try {
      String resName;
      String resResult = elm.getChildText("jndi-name");
      if (resResult.equals("jdbc/DBTimer")) {
        resName = propMgr.getProperty("sjsas.cts.timer.resource");
      } else {
        resName = propMgr.getProperty("sjsas.cmp.backend");
      }
      return resName;
    } catch (Exception e) {
      TestUtil.logErr("Error in getDBResource", e);
      throw new TSDeploymentException("Exception caught in getDBResource");
    }
  }

  private File addCMPCreationElements(File aFile) throws TSDeploymentException {
    try {
      org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder(false);
      builder.setEntityResolver(this);
      org.jdom.Document doc = builder.build(aFile);
      org.jdom.Element cmpResource = doc.getRootElement()
          .getChild("enterprise-beans").getChild("cmp-resource");

      if (cmpResource == null) {
        return writeTempXMLFile(doc, aFile.getName());
      }
      String dbName = getDBResource(cmpResource);
      int insertPoint = getInsertPoint(cmpResource);
      List children = cmpResource.getChildren();
      createElement(children, cmpResource, "create-tables-at-deploy", "true",
          insertPoint++);
      createElement(children, cmpResource, "drop-tables-at-undeploy", "true",
          insertPoint++);
      createElement(children, cmpResource, "database-vendor-name", dbName,
          insertPoint++);

      // If we are testing Java2DB with unique tables we want to add the
      // schema-generator-properties element to the cmp-resource element.
      if (Boolean.getBoolean("USE_UNIQUE_TABLES")) {
        org.jdom.Element schemaResource = cmpResource
            .getChild("schema-generator-properties");
        if (schemaResource == null) {
          schemaResource = new org.jdom.Element("schema-generator-properties");
          children.add(insertPoint++, schemaResource);
        }
        org.jdom.Element propResource = new org.jdom.Element("property");
        propResource.addContent(
            new org.jdom.Element("name").setText("use-unique-table-names"));
        propResource.addContent(new org.jdom.Element("value").setText("true"));
        schemaResource.addContent(propResource);
      }

      return writeTempXMLFile(doc, aFile.getName());
    } catch (Exception e) {
      TestUtil.logErr("Error in addCMPCreationElements", e);
      throw new TSDeploymentException(
          "Error in addCMPCreationElements processing file \"" + aFile + "\"");
    }
  }

  public org.xml.sax.InputSource resolveEntity(String publicId,
      String systemId) {
    String s1asHome = propMgr.getProperty("javaee.home.ri", "/sun/appserver8");
    String schemaDir = s1asHome + File.separator + "lib";
    String dtds = schemaDir + File.separator + "dtds";
    String schemas = schemaDir + File.separator + "schemas";

    TestUtil.logHarnessDebug("systemId \"" + systemId + "\"");

    String schemaName = systemId.substring(systemId.lastIndexOf("/") + 1);
    String resultFile = null;
    if (schemaName.endsWith(".xsd")) {
      resultFile = schemas + File.separator + schemaName;
    } else if (schemaName.endsWith(".dtd")) {
      resultFile = dtds + File.separator + schemaName;
    } else {
      TestUtil.logHarness(
          "Error, system ID not a valid schema \"" + schemaName + "\"");
    }
    TestUtil
        .logHarnessDebug("Entity Resolver returning \"" + resultFile + "\"");
    return new org.xml.sax.InputSource(resultFile);
  }

  /**
   * Gets a Deployment Plan for a given Java EE 5 implementation. The Deployment
   * Plan is constructed from information passed to it via the DeploymentInfo
   * object.
   *
   * Note that the returned Deployment Plan may have additional data which is
   * not present in the DeploymentInfo object.
   *
   * @return A Deployment Plan as an InputStream or a File
   */
  public Object getDeploymentPlan(DeploymentInfo info)
      throws TSDeploymentException {
    String sArchive = info.getEarFile();
    if (sArchive.endsWith(".rar")) {
      return null;
    }

    String[] sRunTimeFileArray = info.getRuntimeFiles();
    int numFiles = (sRunTimeFileArray == null) ? 0 : sRunTimeFileArray.length;
    if (numFiles == 0) {
      return null;
    }

    boolean standAlone = sArchive.endsWith(".war") || sArchive.endsWith(".jar");

    File tempFile = null;
    JarOutputStream out = null; // create the jar file that we'll return the
                                // stream from
    try {
      String userName = System.getProperty("user.name");
      tempFile = File.createTempFile(userName + "-ts-deployment-plan", ".jar");
      out = new JarOutputStream(new FileOutputStream(tempFile));
    } catch (Exception e) {
      throw new TSDeploymentException(
          "ERROR creating jar output stream for archive \"" + sArchive + "\"");
    }

    TestUtil.logHarnessDebug(
        "&&&& RUNTIME FILES " + Arrays.asList(sRunTimeFileArray) + "&&&&&");
    Object fInput = null; // returned result
    File aFile = null;
    try {
      boolean cmpAlreadyCreated = (Boolean
          .valueOf(propMgr.getProperty("create.cmp.tables", "true")))
              .booleanValue();
      for (int i = 0; i < numFiles; i++) {
        aFile = new File(sRunTimeFileArray[i]);
        String jarEntryName = aFile.getName();
        // if we have a sun-application runtime file with an ear name prefix
        // we need to remove the ear name prefix and package the runtime file
        // in the deployment plan jar file as sun-application.xml
        if (jarEntryName.endsWith(".ear." + SUN_APP_NAME)) {
          jarEntryName = SUN_APP_NAME;
        }

        /*
         * If this is an EJB runtime file and we haven't pre-created the CMP
         * tables we check the runtime file for the cmp-resource element. If we
         * don't see a cmp-resource element we assume we are dealing with a
         * non-CMP bean and simply return the aFile unmodified else we modify
         * the cmp-resource to create the table and delete the table at deploy
         * time.
         */
        if (jarEntryName.endsWith(".jar." + SUN_EJB_NAME)
            && !cmpAlreadyCreated) {
          aFile = addCMPCreationElements(aFile);
          addFileToJar(aFile, out, jarEntryName, standAlone);
          try {
            aFile.delete();
          } catch (Exception e) {
            /* keep processing the other files */ }
        } else if (jarEntryName.endsWith(SUN_CMP_MAPPING_FILE)) {
          if (cmpAlreadyCreated) {
            String jarName = jarEntryName.substring(0,
                jarEntryName.indexOf(SUN_CMP_MAPPING_FILE));
            String entryName = jarName + DB_SCHEMA_FILENAME;
            addFileToJar(aFile, out, jarEntryName, standAlone);
            addFileToJar(dbSchemaFile, out, entryName, standAlone);
            TestUtil.logHarness("$$$$$$ Found CMP mapping file for EJB \""
                + sArchive + "\" adding DB schema file \"" + entryName + "\"");
          } else {
            // In the else case we do nothing since the CMP tables don't exist
            // and the
            // sun-ejb-jar.xml has already been modified to create the tables at
            // deploy time.
          }
        } else { // simply add the xml file to the deployment plan
          addFileToJar(aFile, out, jarEntryName, standAlone);
        }
      } // end for loop
    } catch (Exception e) {
      e.printStackTrace();
      throw new TSDeploymentException("ERROR: reading runtime file \"" + aFile
          + "\" while processing archive \"" + sArchive + "\"");
    } finally {
      try {
        out.close();
      } catch (Exception e) {
      }
    }

    try {
      fInput = tempFile;
    } catch (Exception e) {
      e.printStackTrace();
      throw new TSDeploymentException(
          "FileNotFoundException trying to open DeploymentPlan: \"" + tempFile
              + "\"");
    }
    return fInput;
  }

  private void addFileToJar(File aFile, JarOutputStream out,
      String jarEntryName) throws Exception {
    addFileToJar(aFile, out, jarEntryName, false);
  }

  private void addFileToJar(File aFile, JarOutputStream out,
      String jarEntryName, boolean standAlone) throws Exception {
    // if we have a stand alone EJB or WAR we need to modify the names of the
    // runtime deployment files
    // based on the S1AS deployment plan. We need to remove the leading archive
    // name plus the file extension
    // (.jar or .war) from the runtime file.
    if (standAlone) {
      int index = jarEntryName.indexOf(".jar.");
      if (index == -1) {
        index = jarEntryName.indexOf(".war.");
      }
      // shouldn't happen but we'll set this to -5 to keep the jar entry the
      // same as it is
      if (index == -1) {
        index = -5;
      }
      // +5 add length of .war. or .jar. to get the filename without the leading
      // jar or war name
      jarEntryName = jarEntryName.substring(index + 5);
    }
    FileInputStream in = null;
    byte[] buf = new byte[1024];
    try {
      in = new FileInputStream(aFile);
      out.putNextEntry(new JarEntry(jarEntryName));
      int len;
      while ((len = in.read(buf)) > 0) {
        out.write(buf, 0, len);
      }
    } finally {
      try {
        in.close();
      } catch (Exception e) {
      }
    }
  }

  private String removeRarExt(String rarName) {
    String result = rarName;
    int index = rarName.indexOf(".");
    if (index != -1) {
      result = rarName.substring(0, index);
    }
    TestUtil.logHarness(
        "######## Adjusted RAR Filename: [" + rarName + " -> " + result + "]");
    return result;
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
   * @param targetIDs
   *          List of target module IDs where the connector will be deployed to.
   * @param p
   *          Properties specific to the currently running test
   */
  public void createConnectionFactory(TargetModuleID[] targetIDs, Properties p)
      throws TSDeploymentException {

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
   * @param targetIDs
   *          List of target module IDs where the connector will be deployed to.
   * @param p
   *          Properties specific to the currently running test
   */
  public void removeConnectionFactory(TargetModuleID[] targetIDs, Properties p)
      throws TSDeploymentException {

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
      if (sAppName.startsWith("vi_built_")) {
        // strip off the vi_built_ string
        sAppName = sAppName.substring(9);
        sTSDeploymentDir = sApp.substring(0,
            sApp.lastIndexOf(File.separator) + 1) + "ts_dep_vi_built";
        sClientname = sClientname.substring(9);
      }
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

  public Hashtable getDependentValues(DeploymentInfo[] infoArray) {
    Hashtable htWsdlLocations = getInteropJNDINames(infoArray);

    // Here is where a licensee would grab the wsdl locations from the
    // DeploymentInfo object. A hashtable mapping the original values
    // to the new values is returned and a substitution is done on the
    // real deploymentinfo that is passed to getDeploymentPlan() prior
    // to actual deployment of the modules. The original values are in RI
    // format. The code below is just to demonstrate which fields may need
    // to be modified for another impl.

    if (TestUtil.harnessDebug) {
      for (int ii = 0; ii < infoArray.length; ii++) {

        // 1.3 version of runtime paring code
        // Vector resources =
        // infoArray[ii].getAppClientResources();
        // for(int i = 0; i< resources.size(); i++)
        // {
        // DeploymentInfo.AppClient appResource =
        // (DeploymentInfo.AppClient)
        // resources.elementAt(i);
        // checkLocations(appResource.serviceRefs, htWsdlLocations);
        // }
        List resources = infoArray[ii].getAppClientRuntimeDDs();
        for (int i = 0; i < resources.size(); i++) {
          com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.SunApplicationClient appResource = (com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.SunApplicationClient) resources
              .get(i);
          checkLocations(appResource.getServiceRef(), htWsdlLocations);
        }

        // 1.3 version of runtime paring code
        // /* Web Container */
        // resources = infoArray[ii].getWebResources();
        // for(int i = 0; i< resources.size(); i++)
        // {
        // DeploymentInfo.WebResource webResource =
        // (DeploymentInfo.WebResource)
        // resources.elementAt(i);
        // checkLocations(webResource.serviceRefs, htWsdlLocations);
        // checkLocations(webResource.webServiceDescriptions, htWsdlLocations);
        // }
        resources = infoArray[ii].getWebRuntimeDDs();
        for (int i = 0; i < resources.size(); i++) {
          com.sun.ts.lib.implementation.sun.javaee.runtime.web.SunWebApp webResource = (com.sun.ts.lib.implementation.sun.javaee.runtime.web.SunWebApp) resources
              .get(i);
          checkLocations(webResource.getServiceRef(), htWsdlLocations);
          checkLocations(webResource.getWebserviceDescription(),
              htWsdlLocations);
        }

        // 1.3 version of runtime paring code
        // /* EJB Container */
        // Vector ejbJars = infoArray[ii].getEjbJars();
        // for(int y= 0; y< ejbJars.size(); y++)
        // {
        // DeploymentInfo.EJBJar ejbJar =
        // (DeploymentInfo.EJBJar)ejbJars.elementAt(y);
        // checkLocations(ejbJar.webServiceDescriptions, htWsdlLocations);
        // resources = ejbJar.getEjbResources();
        // for(int i = 0; i< resources.size(); i++)
        // {
        // DeploymentInfo.Ejb ejbResource =
        // (DeploymentInfo.Ejb) resources.elementAt(i);
        // checkLocations(ejbResource.serviceRefs, htWsdlLocations);
        // }
        // }
        // }
        List ejbJars = infoArray[ii].getEjbRuntimeDDs();
        for (int y = 0; y < ejbJars.size(); y++) {
          com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.SunEjbJar ejbJar = (com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.SunEjbJar) ejbJars
              .get(y);
          checkLocations(ejbJar.getEnterpriseBeans().getWebserviceDescription(),
              htWsdlLocations);

          resources = ejbJar.getEnterpriseBeans().getEjb();
          for (int i = 0; i < resources.size(); i++) {
            com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.Ejb ejbResource = (com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.Ejb) resources
                .get(i);
            checkLocations(ejbResource.getServiceRef(), htWsdlLocations);
          }
        }

        if (!htWsdlLocations.isEmpty()) {
          StringBuffer buf = new StringBuffer(
              "***** Returning the following Hashtable from SunRIDeployment2.getDependentValues *****"
                  + newLine);
          buf.append("***********************************" + newLine);
          for (Enumeration e = htWsdlLocations.keys(); e.hasMoreElements();) {
            String sKey = (String) e.nextElement();
            buf.append("Original value:  " + sKey + newLine);
            buf.append("Modified value:  " + (String) htWsdlLocations.get(sKey)
                + newLine);
          }
          buf.append("***********************************");
          TestUtil.logHarness(buf.toString());
        } else {
          TestUtil.logHarness(
              "***** Returning the following Hashtable from SunRIDeployment2.getDependentValues *****"
                  + newLine + "Hashtable is empty.");
        }
      }
    }
    return htWsdlLocations;
  }

  private void checkLocations(List resources, Hashtable htChanges) {
    if (resources == null || resources.isEmpty()) {
      return;
    }
    Object o = (Object) resources.get(0);

    if (o instanceof com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.ServiceRef) {
      for (int j = 0; j < resources.size(); j++) {
        com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.ServiceRef ref = (com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.ServiceRef) resources
            .get(j);
        String newLocation = ref.getWsdlOverride();
        htChanges.put(ref.getWsdlOverride(), newLocation); // identity mapping
        TestUtil.logHarnessDebug(
            "******** original override \"" + ref.getWsdlOverride()
                + "\"   new override \"" + newLocation + "\"");
      }
    } else if (o instanceof com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.ServiceRef) {
      for (int j = 0; j < resources.size(); j++) {
        com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.ServiceRef ref = (com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.ServiceRef) resources
            .get(j);
        String newLocation = ref.getWsdlOverride();
        htChanges.put(ref.getWsdlOverride(), newLocation); // identity mapping
        TestUtil.logHarnessDebug(
            "******** original override \"" + ref.getWsdlOverride()
                + "\"   new override \"" + newLocation + "\"");
      }
    } else if (o instanceof com.sun.ts.lib.implementation.sun.javaee.runtime.web.ServiceRef) {
      for (int j = 0; j < resources.size(); j++) {
        com.sun.ts.lib.implementation.sun.javaee.runtime.web.ServiceRef ref = (com.sun.ts.lib.implementation.sun.javaee.runtime.web.ServiceRef) resources
            .get(j);
        String newLocation = ref.getWsdlOverride();
        htChanges.put(ref.getWsdlOverride(), newLocation); // identity mapping
        TestUtil.logHarnessDebug(
            "******** original override \"" + ref.getWsdlOverride()
                + "\"   new override \"" + newLocation + "\"");
      }
    } else if (o instanceof com.sun.ts.lib.implementation.sun.javaee.runtime.web.WebserviceDescription) {
      for (int j = 0; j < resources.size(); j++) {
        com.sun.ts.lib.implementation.sun.javaee.runtime.web.WebserviceDescription desc = (com.sun.ts.lib.implementation.sun.javaee.runtime.web.WebserviceDescription) resources
            .get(j);
        String newLocation = desc.getWsdlPublishLocation();
        htChanges.put(desc.getWsdlPublishLocation(), newLocation); // identity
                                                                   // mapping
        TestUtil.logHarnessDebug("********\noriginal override"
            + desc.getWsdlPublishLocation() + ", new override" + newLocation);
      }
    } else if (o instanceof com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.WebserviceDescription) {
      for (int j = 0; j < resources.size(); j++) {
        com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.WebserviceDescription desc = (com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.WebserviceDescription) resources
            .get(j);
        String newLocation = desc.getWsdlPublishLocation();
        htChanges.put(desc.getWsdlPublishLocation(), newLocation); // identity
                                                                   // mapping
        TestUtil.logHarnessDebug("********\noriginal override"
            + desc.getWsdlPublishLocation() + ", new override" + newLocation);
      }
    }
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

  /**
   * Vendors may choose to implement this method if they want to examine the
   * progress object returned from the JSR 88 API call to distribute. They may
   * also wish to invoke some vendor specific code based on the state of the
   * specified progress object. We choose to implement this as a no-op method.
   *
   * @param po
   *          The progress object returned from the distribute call.
   */
  public void postDistribute(ProgressObject po) {
    TestUtil.logHarnessDebug("***** SunRIDeployment2.postDistribute()");
  }

  /**
   * Vendors may choose to implement this method if they want to examine the
   * progress object returned from the JSR 88 API call to start. They may also
   * wish to invoke some vendor specific code based on the state of the
   * specified progress object. We choose to implement this as a no-op method.
   *
   * @param po
   *          The progress object returned from the start call.
   */
  public void postStart(ProgressObject po) {
    TestUtil.logHarnessDebug("***** SunRIDeployment2.postStart()");
  }

  /**
   * Vendors may choose to implement this method if they want to examine the
   * progress object returned from the JSR 88 API call to stop. They may also
   * wish to invoke some vendor specific code based on the state of the
   * specified progress object. We choose to implement this as a no-op method.
   *
   * @param po
   *          The progress object returned from the stop call.
   */
  public void postStop(ProgressObject po) {
    TestUtil.logHarnessDebug("***** SunRIDeployment2.postStop()");
  }

  /**
   * Vendors may choose to implement this method if they want to examine the
   * progress object returned from the JSR 88 API call to undeploy. They may
   * also wish to invoke some vendor specific code based on the state of the
   * specified progress object. We choose to implement this as a no-op method.
   *
   * @param po
   *          The progress object returned from the undeploy call.
   */
  public void postUndeploy(ProgressObject po) {
    TestUtil.logHarnessDebug("***** SunRIDeployment2.postUndeploy()");
  }

} // end class SunRIDeployment2
