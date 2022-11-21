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

/**
 *
 * @author Raja Perumal
 */

package com.sun.ts.lib.deliverable.jaspic;

import com.sun.ts.lib.deliverable.AbstractPropertyManager;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.deliverable.PropertyNotSetException;
import com.sun.javatest.TestEnvironment;

import java.util.Map;
import java.util.Properties;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 */
public class JaspicPropertyManager extends AbstractPropertyManager {
  // uninitialized singleton instance
  private static JaspicPropertyManager jteMgr = new JaspicPropertyManager();

  private String sLoginClass1;

  private String sLoginClass2;

  private String sURLClass1;

  private String sURLClass2;

  private String sJMSClass1;

  private String sJMSClass2;

  private String sWebServerHost;

  private String sWebServerPort;

  private String sWebServerHost2;

  private String sWebServerPort2;

  private String sSecuredWebServicePort;

  private String sSecuredWebServicePort2;

  private String sHttpsURLConnectionClass1;

  private String sHttpsURLConnectionClass2;

  private String sWSDLRepository1;

  private String sWSDLRepository2;

  private String user1;

  private String password1;

  private String user2;

  private String password2;

  private JaspicPropertyManager() {
  }

  /**
   * This method returns the singleton instance of TSPropertyManager which
   * provides access to all ts.jte properties. This is only called once by the
   * test harness.
   *
   * @param env
   *          - TestEnvironment object from JavaTest
   * @return TSPropertyManager - singleton property manager object
   */
  public final static JaspicPropertyManager getJaspicPropertyManager(
      TestEnvironment env) throws PropertyNotSetException {
    jteMgr.setTestEnvironment(env);
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of JaspicPropertyManager which
   * provides access to all ts.jte properties. This is only called by the init()
   * method in ManualDeployment.java
   *
   * @param p
   *          - Properties object from JavaTest
   * @return JaspicPropertyManager - singleton property manager object
   */
  public final static JaspicPropertyManager getJaspicPropertyManager(
      Properties p) throws PropertyNotSetException {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static JaspicPropertyManager getJaspicPropertyManager()
      throws PropertyNotSetException {
    return jteMgr;
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

    pTestProps.put("javaee.level", getProperty("javaee.level", "component"));

    return pTestProps;
  }
}
