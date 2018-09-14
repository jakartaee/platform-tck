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

package com.sun.ts.lib.deliverable.jaspic;

import com.sun.javatest.TestEnvironment;
import com.sun.ts.lib.deliverable.AbstractPropertyManager;
import com.sun.ts.lib.deliverable.PropertyNotSetException;
import java.util.Properties;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 */
public class JaspicJavaEEPropertyManager extends AbstractPropertyManager {

  private static JaspicJavaEEPropertyManager jteMgr = new JaspicJavaEEPropertyManager();

  private String sDeployClass1;

  private String sDeployClass2;

  private String sLoginClass1;

  private String sURLClass1;

  private String sURLClass2;

  private String sJMSClass1;

  private String sDeployHost1;

  private String sDeployHost2;

  private String sWebServerHost;

  private String sWebServerPort;

  private String sWebServerHost2;

  private String sWebServerPort2;

  private String sSecuredWebServicePort;

  private String sSecuredWebServicePort2;

  private String sHttpsURLConnectionClass1;

  private String sWSDLRepository1;

  private String sWSDLRepository2;

  private String user1;

  private String password1;

  private String user2;

  private String password2;

  // JSR-88
  private String deployManagerJarFile1;

  private String deployManageruri1;

  private String deployManageruname1;

  private String deployManagerpasswd1;

  private String deployManagerJarFile2;

  private String deployManageruri2;

  private String deployManageruname2;

  private String deployManagerpasswd2;

  private JaspicJavaEEPropertyManager() {
  }

  /**
   * This method returns the singleton instance of JaspicJavaEEPropertyManager
   * which provides access to all ts.jte properties. This is only called once by
   * the test harness.
   *
   * @param env
   *          - TestEnvironment object from JavaTest
   * @return JaspicJavaEEPropertyManager - singleton property manager object
   */
  public final static JaspicJavaEEPropertyManager getJaspicJavaEEPropertyManager(
      TestEnvironment env) throws PropertyNotSetException {
    jteMgr.setTestEnvironment(env);
    jteMgr.initInteropProperties(); // TODO: why init interop only here?
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of JaspicJavaEEPropertyManager
   * which provides access to all ts.jte properties. This is only called by the
   * init() method in ManualDeployment.java
   *
   * @param p
   *          - Properties object from JavaTest
   * @return JaspicJavaEEPropertyManager - singleton property manager object
   */
  public final static JaspicJavaEEPropertyManager getJaspicJavaEEPropertyManager(
      Properties p) throws PropertyNotSetException {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static JaspicJavaEEPropertyManager getJaspicJavaEEPropertyManager()
      throws PropertyNotSetException {
    return jteMgr;
  }

  private void initInteropProperties() {
  }

  public void forwardValues() {
    // reverse all interop props
    setProperty("user1", user1);
    setProperty("password1", password1);
    setProperty("user2", user2);
    setProperty("password2", password2);

    setProperty("porting.ts.deploy.class.1", sDeployClass1);
    setProperty("porting.ts.deploy.class.2", sDeployClass2);
    setProperty("porting.ts.login.class.1", sLoginClass1);
    setProperty("porting.ts.url.class.1", sURLClass1);
    setProperty("porting.ts.jms.class.1", sJMSClass1);
    setProperty("deployment_host.1", sDeployHost1);
    setProperty("deployment_host.2", sDeployHost2);
    setProperty("webServerHost", sWebServerHost);
    setProperty("webServerHost.2", sWebServerHost2);
    setProperty("webServerPort", sWebServerPort);
    setProperty("webServerPort.2", sWebServerPort2);
    setProperty("securedWebServicePort", sSecuredWebServicePort);
    setProperty("securedWebServicePort.2", sSecuredWebServicePort2);
    setProperty("porting.ts.HttpsURLConnection.class.1",
        sHttpsURLConnectionClass1);
    setProperty("wsdlRepository1", sWSDLRepository1);
    setProperty("wsdlRepository2", sWSDLRepository2);

    // JSR-88
    setProperty("deployManagerJarFile.1", deployManagerJarFile1);
    setProperty("deployManageruri.1", deployManageruri1);
    setProperty("deployManageruname.1", deployManageruname1);
    setProperty("deployManagerpasswd.1", deployManagerpasswd1);

    super.forwardValues();
  }

  public void reverseValues() {
    // reverse all interop props
  }

  /**
   * This method is called by the test harness to retrieve all properties needed
   * by a particular test.
   *
   * @param sPropKeys
   *          - Properties to retrieve
   * @return Properties - property/value pairs
   */
  public Properties getTestSpecificProperties(String[] sPropKeys)
      throws PropertyNotSetException {
    Properties pTestProps = super.getTestSpecificProperties(sPropKeys);

    // if the abstract propertymanager already loaded all props, just return
    if (pTestProps.getProperty("all.props").equalsIgnoreCase("true")) {
      return pTestProps;
    }

    String sJtePropVal = "";
    // add all porting class props so the factories can work in the server
    pTestProps.put("porting.ts.deploy.class.1",
        getProperty("porting.ts.deploy.class.1"));
    pTestProps.put("porting.ts.deploy.class.2",
        getProperty("porting.ts.deploy.class.2"));
    pTestProps.put("porting.ts.login.class.1",
        getProperty("porting.ts.login.class.1"));
    pTestProps.put("porting.ts.HttpsURLConnection.class.1",
        getProperty("porting.ts.HttpsURLConnection.class.1"));
    pTestProps.put("porting.ts.url.class.1",
        getProperty("porting.ts.url.class.1"));
    pTestProps.put("porting.ts.jms.class.1",
        getProperty("porting.ts.jms.class.1"));
    pTestProps.put("wsdlRepository1", getProperty("wsdlRepository1"));
    pTestProps.put("wsdlRepository2", getProperty("wsdlRepository2"));

    // props needed when using the JSR-88 APIs
    pTestProps.put("deployManagerJarFile.1",
        getProperty("deployManagerJarFile.1"));
    pTestProps.put("deployManageruri.1", getProperty("deployManageruri.1"));

    pTestProps.put("deployManageruname.1", getProperty("deployManageruname.1"));
    pTestProps.put("deployManagerpasswd.1",
        getProperty("deployManagerpasswd.1"));

    pTestProps.put("porting.ts.deploy2.class.1",
        getProperty("porting.ts.deploy2.class.1"));

    return pTestProps;
  }
}
