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

package com.sun.ts.lib.implementation.sun.javaee.runtime;

import javax.xml.bind.*;
import com.sun.ts.lib.porting.DeploymentInfo;
import com.sun.ts.lib.implementation.sun.javaee.runtime.app.*;
import com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.*;
import com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.*;
import com.sun.ts.lib.implementation.sun.javaee.runtime.web.*;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.deliverable.DeliverableFactory;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.sax.*;
import java.io.*;
import java.util.*;
import com.sun.ts.lib.util.*;

/**
 * Provides all information required to deploy an application on a server. Much
 * of this information is extracted from runtime xml files. The following
 * information is provided:
 * <ul>
 * <li>EJB Jar info</li>
 * <li>Web Resources - Display name, context root, resource references and ejb
 * references for each web resource in this ear.</li>
 * <li>EJB Resources - Name, JNDI name, resource references, ejb references, and
 * CMP information for each ejb resource in this ear.</li>
 * <li>Resource References - For each resource reference, the JNDI name, default
 * resource principal name and password, and any mail configuration information
 * is provided.</li>
 * <li>EJB References - For each EJB reference, the EJB name and its
 * corresponding JNDI name is provided.</li>
 * </ul>
 * </p>
 * See: javaee.home.ri/lib/dtds/sun-application_5_0-0.dtd
 * javaee.home.ri/lib/dtds/sun-application-client_5_0-0.dtd
 * javaee.home.ri/lib/dtds/sun-ejb-jar_3_0-0.dtd
 * javaee.home.ri/lib/dtds/sun-web-app_2_5-0.dtd for more and updated
 * information.
 *
 * @author Mark Roth, Kyle Grucci, Ryan O'Connell
 */
public class SunRIDeploymentInfo
    implements DeploymentInfo, java.io.Serializable {

  private static final String EJB_PARSE_PKG = "com.sun.ts.lib.implementation.sun.javaee.runtime.ejb";

  private static final String WEB_PARSE_PKG = "com.sun.ts.lib.implementation.sun.javaee.runtime.web";

  private static final String APP_PARSE_PKG = "com.sun.ts.lib.implementation.sun.javaee.runtime.app";

  private static final String APC_PARSE_PKG = "com.sun.ts.lib.implementation.sun.javaee.runtime.appclient";

  private static final String EJB_RUNTIME_FILE = "sun-ejb-jar.xml";

  private static final String WEB_RUNTIME_FILE = "sun-web.xml";

  private static final String APC_RUNTIME_FILE = "sun-application-client.xml";

  private static final String APP_RUNTIME_FILE = "sun-application.xml";

  // Maps DD filename (String) ->
  // com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.SunEjbJar interfaces
  private Map ejbRuntimeData = new HashMap();

  // Maps DD filename (String) ->
  // com.sun.ts.lib.implementation.sun.javaee.runtime.web.SunWebApp interfaces
  private Map webRuntimeData = new HashMap();

  // Maps DD filename (String) ->
  // com.sun.ts.lib.implementation.sun.javaee.runtime.app.SunApplication
  // interfaces
  private Map appRuntimeData = new HashMap();

  // Maps DD filename (String) ->
  // com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.SunApplicationClient
  // interfaces
  private Map appClientRuntimeData = new HashMap();

  /** The ear file to deploy to the server */
  private String earFile;

  /** The runtime files being analyzed */
  private String[] runtimeFiles;

  protected DeploymentInfo[] prevDeployInfos = null;

  /**
   * Creates a new deployment information object.
   *
   * @param earFile
   *          Full path to the ear file being deployed
   * @param runtimeFiles
   *          Array of full paths to runtime.xml files to analyze.
   *
   * @exception IOException
   *              Thrown if an IO error occured while analyzing runtime
   *              information.
   * @exception DeploymentInfo.ParseException
   *              Thrown if a parsing error occured while analyzing runtime
   *              information.
   */
  public SunRIDeploymentInfo(String earFile, String[] runtimeFiles)
      throws IOException, DeploymentInfo.ParseException {
    this.earFile = earFile;
    this.runtimeFiles = runtimeFiles;

    // default is to not generate sql for this ear
    setProperty("generateSQL", "false");

    unmarshalFiles(runtimeFiles);
  }

  /**
   * A temporary properties list, until all information can be provided by this
   * API.
   */
  protected Properties properties = new Properties();

  /**
   * Sets the value of the given property. This method should be temporary,
   * until all important information can be provided by the API.
   */
  public void setProperty(String key, String value) {
    properties.setProperty(key, value);
  }

  /**
   * Returns the value of the given property. This method should be temporary,
   * until all important information can be provided by the API.
   */
  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  /**
   * Sets/gets an array of deploymentInfo objects from previously deployed apps
   * in the currrent directory along with all common apps
   */
  public void setPreviousInfos(DeploymentInfo[] infos) {
    prevDeployInfos = infos;
  }

  public DeploymentInfo[] getPreviousInfos() {
    return prevDeployInfos;
  }

  /**
   * Returns the ear file to deploy
   */
  public String getEarFile() {
    return earFile;
  }

  /**
   * Returns the list of runtime files to be deployed
   */
  public String[] getRuntimeFiles() {
    return runtimeFiles;
  }

  /**
   * Returns a Map that maps runtimne deployment descriptor filename Strings to
   * concrete implementations of the
   * com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.SunEjbJar interface.
   */
  public Map getEjbRuntimeData() {
    return ejbRuntimeData;
  }

  /**
   * Returns a Map that maps runtimne deployment descriptor filename Strings to
   * concrete implementations of the
   * com.sun.ts.lib.implementation.sun.javaee.runtime.web.SunWebApp interface.
   */
  public Map getWebRuntimeData() {
    return webRuntimeData;
  }

  /**
   * Returns a Map that maps runtimne deployment descriptor filename Strings to
   * concrete implementations of the
   * com.sun.ts.lib.implementation.sun.javaee.runtime.app.SunApplication
   * interface.
   */
  public Map getAppRuntimeData() {
    return appRuntimeData;
  }

  /**
   * Returns a Map that maps runtimne deployment descriptor filename Strings to
   * concrete implementations of the
   * com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.SunApplicationClient
   * interface.
   */
  public Map getAppClientRuntimeData() {
    return appClientRuntimeData;
  }

  private List makeList(Map aMap) {
    List result = new ArrayList();
    if (aMap == null) {
      return result;
    }
    Iterator i = aMap.values().iterator();
    while (i.hasNext()) {
      result.add(i.next());
    }
    return result;
  }

  /**
   * Returns a List of concrete implementations of the
   * com.sun.ts.lib.implementation.sun.javaee.runtime.appclient.SunApplicationClient
   * interface.
   */
  public List getAppClientRuntimeDDs() {
    return makeList(appClientRuntimeData);
  }

  /**
   * Returns a List of concrete implementations of the
   * com.sun.ts.lib.implementation.sun.javaee.runtime.app.SunApplication
   * interface.
   */
  public List getAppRuntimeDDs() {
    return makeList(appRuntimeData);
  }

  /**
   * Returns a List of concrete implementations of the
   * com.sun.ts.lib.implementation.sun.javaee.runtime.web.SunWebApp interface.
   */
  public List getWebRuntimeDDs() {
    return makeList(webRuntimeData);
  }

  /**
   * Returns a List of concrete implementations of the
   * com.sun.ts.lib.implementation.sun.javaee.runtime.ejb.SunEjbJar interface.
   */
  public List getEjbRuntimeDDs() {
    return makeList(ejbRuntimeData);
  }

  private String normalizeFile(String fileName) {
    int index = fileName.lastIndexOf(File.separator);
    index++;
    int index2 = fileName.indexOf(".sun-");
    if (index2 == -1) {
      index2 = fileName.length();
    }
    String result = fileName.substring(index, index2);
    return result;
  }

  private void unmarshalFiles(String[] files)
      throws DeploymentInfo.ParseException {
    String file = "";
    try {
      int numFiles = (files == null) ? 0 : files.length;
      for (int i = 0; i < numFiles; i++) {
        file = files[i];
        if (file.endsWith(EJB_RUNTIME_FILE)) {
          this.ejbRuntimeData.put(normalizeFile(file),
              (SunEjbJar) (unmarshalFile(EJB_PARSE_PKG, file)));
        } else if (file.endsWith(WEB_RUNTIME_FILE)) {
          this.webRuntimeData.put(normalizeFile(file),
              (SunWebApp) (unmarshalFile(WEB_PARSE_PKG, file)));
        } else if (file.endsWith(APP_RUNTIME_FILE)) {
          this.appRuntimeData.put(normalizeFile(file),
              (SunApplication) (unmarshalFile(APP_PARSE_PKG, file)));
        } else if (file.endsWith(APC_RUNTIME_FILE)) {
          this.appClientRuntimeData.put(normalizeFile(file),
              (SunApplicationClient) (unmarshalFile(APC_PARSE_PKG, file)));
        } else {
          TestUtil.logHarnessDebug(
              "Warning unknown file type found in unmarshalFiles (String[])");
          TestUtil.logHarnessDebug("\tfile name is \"" + file
              + "\", Ignoring file and continuing...");
        }
        if (TestUtil.harnessDebug) {
          TestUtil.logHarnessDebug("###################");
          TestUtil.logHarnessDebug("### DD file key was \"" + file + "\"");
          TestUtil.logHarnessDebug(
              "### DD file key is  \"" + normalizeFile(file) + "\"");
          TestUtil.logHarnessDebug("###################");
        }
      }
    } catch (Exception e) {
      TestUtil.logHarness("Error unmarshalling file \"" + file + "\"");
      e.printStackTrace();
      throw new DeploymentInfo.ParseException(
          "Error unmarshalling file \"" + file + "\"");
    }
  }

  private String getAppserverLibDir() {
    String libDir = File.separator + "j2eetck" + File.separator + "lib";
    try {
      PropertyManagerInterface propMgr = DeliverableFactory
          .getDeliverableInstance().getPropertyManager();
      String ctsDtdDirectory = propMgr.getProperty("ts.home", libDir);
      libDir = ctsDtdDirectory + File.separator + "lib";
    } catch (Exception e) {
      e.printStackTrace();
    }
    return libDir;
  }

  public class MyResolver implements EntityResolver {
    public InputSource resolveEntity(String publicId, String systemId) {
      int index = systemId.lastIndexOf('/');
      if (index == -1) { // should never happen
        index = systemId.lastIndexOf('\\');
      }
      String fileName = systemId.substring(index + 1);
      String libDir = getAppserverLibDir();
      String result = systemId;
      if (fileName.endsWith(".xsd")) {
        result = libDir + File.separator + "schemas" + File.separator
            + fileName;
      } else if (fileName.endsWith(".dtd")) {
        result = libDir + File.separator + "dtds" + File.separator + fileName;
      } else {
        TestUtil.logHarnessDebug("No match for \"" + systemId + "\"");
      }
      if (new File(result).exists()) {
        TestUtil
            .logHarnessDebug("Entity Resolver Returning \"" + result + "\"");
        return new InputSource(result);
      }
      TestUtil.logHarnessDebug(
          "Entity Resolver could not find \"" + result + "\", returning null");
      return null;
    }
  }

  private Object unmarshalFile(String pkg, String aFile) throws Exception {
    SAXParserFactory spf = SAXParserFactory.newInstance();
    // TestUtil.logHarnessDebug("********************************************************************************");
    // TestUtil.logHarnessDebug("**** Factory is namespace aware \"" +
    // spf.isNamespaceAware() + "\"");
    spf.setNamespaceAware(true);
    // TestUtil.logHarnessDebug("**** Factory is namespace aware \"" +
    // spf.isNamespaceAware() + "\"");
    SAXParser saxParser = spf.newSAXParser();
    // TestUtil.logHarnessDebug("**** Parser is namespace aware \"" +
    // saxParser.isNamespaceAware() + "\"");
    // TestUtil.logHarnessDebug("********************************************************************************");
    XMLReader xmlReader = saxParser.getXMLReader();
    xmlReader.setEntityResolver(new MyResolver());
    SAXSource source = new SAXSource(xmlReader, new InputSource(aFile));
    JAXBContext jc = JAXBContext.newInstance(pkg);
    Unmarshaller um = jc.createUnmarshaller();
    TestUtil.logHarnessDebug("Unmarshalling file \"" + aFile + "\"");
    Object tree = um.unmarshal(source);
    return tree;
  }

  /**
   * Returns a String that conatains the contents of all the runtime XML files.
   */
  public String getContentAsXml() {
    String NL = System.getProperty("line.separator", "\n");
    int numFiles = (runtimeFiles == null) ? 0 : runtimeFiles.length;
    org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder(false);
    builder.setEntityResolver(new MyResolver());
    File runtimeFile = null;
    Writer writer = new StringWriter();
    org.jdom.output.XMLOutputter out = new org.jdom.output.XMLOutputter(
        org.jdom.output.Format.getPrettyFormat());
    for (int i = 0; i < numFiles; i++) {
      try {
        writer.write(
            "*******************************************************************"
                + NL);
        writer.write("File: \"" + runtimeFiles[i] + "\"" + NL);
        runtimeFile = new File(runtimeFiles[i]);
        org.jdom.Document doc = builder.build(runtimeFile);
        out.output(doc, writer);
        writer.write(NL);
      } catch (Exception e) {
        e.printStackTrace();
        try {
          writer.write(
              "*******************************************************************"
                  + NL);
          writer.write(NL + NL + "Error processing file \"" + runtimeFiles[i]
              + "\"" + NL + NL);
          writer.write(
              "*******************************************************************"
                  + NL);
        } catch (Exception ee) {
        } // keep processing runtime files
      }
    }
    try {
      writer.close();
    } catch (Exception e) {
    }
    return writer.toString();
  }

}
