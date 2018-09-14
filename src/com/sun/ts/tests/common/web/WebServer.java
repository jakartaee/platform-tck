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

package com.sun.ts.tests.common.web;

import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.porting.TSURL;
import com.sun.javatest.Status;

/**
 * Facade class to ease client access to a TS test deployed in the Web
 * container. Shield clients from underlying TS and HTPP indiosyncrasies.
 */
public class WebServer {

  protected static final String PROTOCOL = "http";

  protected static final String WEB_HOST_PROP = "webServerHost";

  protected static final String WEB_PORT_PROP = "webServerPort";

  protected static final String RESULT_PROP = "ctsWebTestResult";

  protected static final String TEST_NAME_PROP = "ctsWebTestName";

  protected String host;

  protected int port;

  protected URL url;

  protected URLConnection conn;

  protected Properties props;

  /** Constructor */
  private WebServer(String hostName, int portNumber, Properties p)
      throws IllegalArgumentException {

    if (null == hostName) {
      throw new IllegalArgumentException("null host name!");
    }

    this.host = hostName;
    this.port = portNumber;
    this.props = p;
  }

  /**
   * Factory method to build a WebServer object from TS props parsing relevant
   * properties.
   */
  public static WebServer newInstance(Properties props)
      throws IllegalArgumentException {

    String host;
    String portValue;
    String msg;
    int port;

    if (null == props) {
      msg = "null properties!";
      TestUtil.logErr("[TSTestURL] " + msg);
      throw new IllegalArgumentException(msg);
    }

    host = props.getProperty(WEB_HOST_PROP);
    if (null == host || host.equals("")) {
      msg = "Empty " + WEB_HOST_PROP + " property: " + host;
      printConfigError(msg);
      throw new IllegalArgumentException(msg);
    }

    portValue = props.getProperty(WEB_PORT_PROP);
    if (null == portValue || portValue.equals("")) {
      msg = "Empty " + WEB_PORT_PROP + " property: " + portValue;
      printConfigError(msg);
      throw new IllegalArgumentException(msg);
    }

    try {
      port = Integer.parseInt(portValue);
    } catch (Exception e) {
      msg = "Invalid " + WEB_PORT_PROP + " property: '" + portValue + "' : "
          + e.getMessage();
      printConfigError(msg);
      throw new IllegalArgumentException(msg);
    }

    return new WebServer(host, port, props);
  }

  /**
   * Call test 'testName' in web component deployed as 'webFile'.
   *
   * @param webFile
   *          file component (see java.net.URL) of the url the web component
   *          mapped to.
   *
   * @param testName
   *          Name of the test to run in this web component.
   * 
   * @return true if test pass. false otherwise.
   */
  public boolean test(String webFile, String testName) {
    return (null != call(webFile, testName, this.props));
  }

  /**
   * Call test 'testName' in web component deployed as 'webFile', passing 'args'
   * as input parameters.
   *
   * @param webFile
   *          file component (see java.net.URL) of the url the web component
   *          mapped to.
   *
   * @param testName
   *          Name of the test to run in this web component.
   * 
   * @param args
   *          input parameters in a Properties object. This object need at least
   *          to have valid TS harness logging properties;
   *
   * @return output parameters in a Properties object. Return null if a problem
   *         occured or test failed.
   */
  public Properties call(String webFile, String testName, Properties args)
      throws IllegalArgumentException {

    URL url;
    String status;
    Properties results;

    if (null == args) {
      throw new IllegalArgumentException("null args!");
    }

    try {
      args.setProperty(TEST_NAME_PROP, testName);

      url = (new TSURL()).getURL(PROTOCOL, host, port, webFile);
      conn = TestUtil.sendPostData(args, url);
      TestUtil
          .logMsg("Getting response from url connection: " + url.toString());

      results = TestUtil.getResponseProperties(conn);
      if (null == results) {
        TestUtil.logErr("[TSTestURL] null response props!");
        return null;
      }

      // Too verbose - TestUtil.list(results);

      /* Check whether test passed, return null if test failed */
      status = results.getProperty(RESULT_PROP);
      if (!(Boolean.valueOf(status).booleanValue())) {
        return null;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      e.printStackTrace();
      return null;
    }

    return results;
  }

  /** Convenience method to print TS configuration error messages */
  protected static void printConfigError(String msg) {
    TestUtil.logErr("[TSTestURL] " + msg);
    TestUtil.logErr(
        "Please check you that " + WEB_HOST_PROP + " and " + WEB_PORT_PROP
            + " properties are set correctly " + "in you ts.jte file");
  }

}
