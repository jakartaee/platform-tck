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

package com.sun.ts.lib.porting;

import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.deliverable.DeliverableFactory;
import java.io.*;
import java.util.*;

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
 * <p>
 * See: javaee.home.ri/lib/dtds/sun-application_5_0-0.dtd
 * javaee.home.ri/lib/dtds/sun-application-client_5_0-0.dtd
 * javaee.home.ri/lib/dtds/sun-ejb-jar_3_0-0.dtd
 * javaee.home.ri/lib/dtds/sun-web-app_2_5-0.dtd for more and updated
 * information.
 *
 */

public interface DeploymentInfo extends java.io.Serializable {
  /**
   * Sets the value of the given property. This method should be temporary,
   * until all important information can be provided by the API.
   */
  public void setProperty(String key, String value);

  /**
   * Returns the value of the given property. This method should be temporary,
   * until all important information can be provided by the API.
   */
  public String getProperty(String key);

  /**
   * Sets/gets an array of deploymentInfo objects from previously deployed apps
   * in the currrent directory along with all common apps
   */
  public void setPreviousInfos(DeploymentInfo[] infos);

  public DeploymentInfo[] getPreviousInfos();

  /**
   * Returns the ear file to deploy
   */
  public String getEarFile();

  /**
   * Returns the list of runtime files to be deployed
   */
  public String[] getRuntimeFiles();

  /**
   * Returns a Map that maps runtimne deployment descriptor filename Strings to
   * concrete implementations of the com.sun.ts.lib.porting.ejb.SunEjbJar
   * interface.
   */
  public Map getEjbRuntimeData();

  /**
   * Returns a Map that maps runtimne deployment descriptor filename Strings to
   * concrete implementations of the com.sun.ts.lib.porting.web.SunWebApp
   * interface.
   */
  public Map getWebRuntimeData();

  /**
   * Returns a Map that maps runtimne deployment descriptor filename Strings to
   * concrete implementations of the com.sun.ts.lib.porting.app.SunApplication
   * interface.
   */
  public Map getAppRuntimeData();

  /**
   * Returns a Map that maps runtimne deployment descriptor filename Strings to
   * concrete implementations of the
   * com.sun.ts.lib.porting.appclient.SunApplicationClient interface.
   */
  public Map getAppClientRuntimeData();

  /**
   * Returns a List of concrete implementations of the
   * com.sun.ts.lib.porting.appclient.SunApplicationClient interface.
   */
  public List getAppClientRuntimeDDs();

  /**
   * Returns a List of concrete implementations of the
   * com.sun.ts.lib.porting.app.SunApplication interface.
   */
  public List getAppRuntimeDDs();

  /**
   * Returns a List of concrete implementations of the
   * com.sun.ts.lib.porting.web.SunWebApp interface.
   */
  public List getWebRuntimeDDs();

  /**
   * Returns a List of concrete implementations of the
   * com.sun.ts.lib.porting.ejb.SunEjbJar interface.
   */
  public List getEjbRuntimeDDs();

  /**
   * Returns a String that conatains the contents of all the runtime XML files.
   */
  public String getContentAsXml();

  /**
   * Exception thrown if an error occured parsing the XML
   */
  public class ParseException extends Exception implements Serializable {
    public ParseException(String message) {
      super(message);
    }
  }

}
