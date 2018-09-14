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

package com.sun.ts.lib.deliverable;

import java.io.*;
import java.util.*;

import com.sun.javatest.*;
import com.sun.ts.lib.util.*;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 *
 * @created August 22, 2002
 * @author Kyle Grucci
 */
public class AbstractPropertyManager implements PropertyManagerInterface {
  private TestEnvironment env;

  private Properties jteProperties;

  private String tmp = File.separator + "tmp";

  protected static boolean bReversed;

  protected AbstractPropertyManager() {
  }

  /**
   * @exception PropertyNotSetException
   */
  private void checkHarnessTempDir() throws PropertyNotSetException {
    String tmpDir = getProperty("harness.temp.directory", null);
    if (tmpDir == null) {
      tmpDir = getProperty("TS_HOME", null) + this.tmp;
      setProperty("harness.temp.directory", tmpDir);
    } else {
      if (!(tmpDir.endsWith(this.tmp)
          || tmpDir.endsWith(this.tmp + File.separator)
          || tmpDir.endsWith("/tmp") || tmpDir.endsWith("/tmp/"))) {
        tmpDir += this.tmp;
        setProperty("harness.temp.directory", tmpDir);
      }
    }
  }

  /**
   * copies all entries from TestEnvironment and jteProperties to a new
   * Properties, which is used for remote invocation of porting server.
   * jteProperties has precedence over TestEnvironment. We set forward/reverse
   * related properties in jteProperties and the same key in TestEnvironment may
   * have old value.
   *
   * @return a new properties
   */
  private Properties copyEntries() {
    Properties props = new Properties();
    if (this.env != null) {
      String key = null;
      String val = null;
      for (Iterator it = env.keys().iterator(); it.hasNext();) {
        key = (String) it.next();
        val = getFromEnv(key);
        if (key != null && val != null) {
          props.setProperty(key, val);
        } else {
          if (TestUtil.harnessDebug) {
            TestUtil.logHarnessDebug(
                "AbstractPropertyManager.copyEntries():" + key + " is null.");
          }
        }
      }
    }
    if (this.jteProperties != null) {
      props.putAll(this.jteProperties);
    }
    if (TestUtil.harnessDebug) {
      TestUtil.logHarnessDebug(
          "AbstractPropertyManager copied all entries to a properties.");
    }

    props.put("bin.dir", System.getProperty("bin.dir", ""));
    return props;
  }

  /**
   * Sets a property in property manager. If the key already exists in the
   * property manager, the old value is overriden by new value.
   *
   * @param key
   *          key of the property.
   * @param val
   *          value of the property.
   */
  public void setProperty(String sKey, String sVal) {
    if (jteProperties == null) {
      jteProperties = new Properties();
    }
    if (sKey != null && sVal != null) {
      jteProperties.setProperty(sKey, sVal);
    }
  }

  /**
   * This method swaps all of the following interop values in
   * TSPropertyManager...
   *
   * @param sDirection
   */
  public void swapInteropPropertyValues(String sDirection) {
    if (sDirection.equals("reverse")) {
      if (bReversed) {
        return;
      } else {
        reverseValues();
      }
    } else {
      if (!bReversed) {
        return;
      } else {
        forwardValues();
      }
    }
  }

  protected void forwardValues() {
    bReversed = false;
  }

  protected void reverseValues() {
    bReversed = true;
  }

  /**
   * gets a new properties containing all entries in the property manager. Any
   * operation on the returned properties will have no effect on property
   * manager
   *
   * @return The jteProperties value
   */
  public Properties getJteProperties() {
    return copyEntries();
  }

  /**
   * This method is called by the test harness to retrieve all properties needed
   * by a particular test.
   *
   * @param sPropKeys
   *          - Properties to retrieve
   * @return Properties - property/value pairs
   * @exception PropertyNotSetException
   */
  public Properties getTestSpecificProperties(String[] sPropKeys)
      throws PropertyNotSetException {
    Properties pTestProps = new Properties();
    String tmpKey = null;

    for (int ii = 0; ii < sPropKeys.length; ii++) {
      tmpKey = sPropKeys[ii];
      if (tmpKey.equalsIgnoreCase("generateSQL")) {
        pTestProps.put("generateSQL", "true");
      } else if (tmpKey.equalsIgnoreCase("all.props")) {
        pTestProps = getJteProperties();
        pTestProps.put("generateSQL", "true");
        pTestProps.put("all.props", "true");
        return pTestProps;
      } else {
        pTestProps.put(tmpKey, getProperty(tmpKey));
      }
    }
    // set all props that all tests need
    pTestProps.put("harness.log.port", getProperty("harness.log.port"));
    pTestProps.put("harness.log.traceflag",
        getProperty("harness.log.traceflag"));
    pTestProps.put("harness.log.delayseconds",
        getProperty("harness.log.delayseconds"));
    pTestProps.put("harness.temp.directory",
        getProperty("harness.temp.directory"));
    pTestProps.put("harness.socket.retry.count",
        getProperty("harness.socket.retry.count"));
    pTestProps.put("bin.dir", System.getProperty("bin.dir", ""));

    pTestProps.put("all.props", "false");
    return pTestProps;
  }

  /**
   * This method is called to get a property value
   *
   * @param sKey
   *          - Property to retrieve
   * @return String - property value
   * @exception PropertyNotSetException
   */
  public String getProperty(String sKey) throws PropertyNotSetException {
    String sVal = getProperty0(sKey);
    if (sVal == null) {
      throw new PropertyNotSetException("No value found for " + sKey
          + ".  Please check your jte file for an appropiate value");
    }
    return sVal;
  }

  /**
   * gets property value with default
   *
   * @param sKey
   *          - Property to retrieve
   * @param def
   * @return String - property value
   */
  public String getProperty(String sKey, String def) {
    String result = getProperty0(sKey);
    if (result == null) {
      result = def;
    }
    return result;
  }

  /**
   * Gets the defaultValue attribute of the AbstractPropertyManager object
   *
   * @param sKey
   * @return The defaultValue value
   */
  private String getDefaultValue(String sKey) {
    String result = null;
    if (sKey.startsWith("deployment_host.")) {
      result = "local";
      if (TestUtil.harnessDebug) {
        TestUtil.logHarnessDebug(
            "WARNING:  No value found for " + sKey + ", use default local");
      }
    } else if (sKey.equals("undeploy_redeploy_apps")) {
      result = "true";
    }
    return result;
  }

  /**
   * retrieves property value from TestEnvironment, and converts it to a single
   * string. If lookup returns null or empty string array, returns null.
   *
   * @param key
   * @return The fromEnv value
   */
  private String getFromEnv(String key) {
    String result = null;
    String[] values = null;
    try {
      values = env.lookup(key);
    } catch (TestEnvironment.Fault f) {
      f.printStackTrace();
    }
    if (values != null) {
      switch (values.length) {
      case 0:
        if (key.equals("s1as.admin.passwd") || key.equals("ri.admin.passwd")
            || key.equals("deployManagerpasswd.1")
            || key.equals("deployManagerpasswd.2")) {
          result = "";
        }
        break;
      case 1:
        result = values[0].trim();
        break;
      default:
        result = "";
        for (int i = 0; i < values.length; i++) {
          result += values[i] + " ";
        }
        result = result.trim();
      }
    }
    return result;
  }

  /**
   * first tries properties, then env, and finally default values
   *
   * @param key
   * @return The property0 value
   */
  private String getProperty0(String key) {
    String result = null;
    if (jteProperties != null) {
      result = jteProperties.getProperty(key);
    }
    if (result == null && env != null) {
      result = getFromEnv(key);
    }
    if (result == null) {
      result = getDefaultValue(key);
    }
    return result;
  }

  /**
   * Sets the jteProperties attribute of the AbstractPropertyManager object
   *
   * @param p
   *          The new jteProperties value
   * @exception PropertyNotSetException
   */
  protected final void setJteProperties(Properties p)
      throws PropertyNotSetException {
    jteProperties = p;
    jteProperties.put("bin.dir", System.getProperty("bin.dir", ""));
    env = null;
    checkHarnessTempDir();
  }

  /**
   * Sets the testEnvironment attribute of the AbstractPropertyManager object
   *
   * @param env
   *          The new testEnvironment value
   * @exception PropertyNotSetException
   */
  protected final void setTestEnvironment(TestEnvironment env)
      throws PropertyNotSetException {
    this.env = env;
    jteProperties = null;
    checkHarnessTempDir();
  }
}
