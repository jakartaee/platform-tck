/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.common.vehicle.ejbliteshare;

import static com.sun.ts.tests.common.vehicle.ejbliteshare.EJBLiteClientIF.TEST_PASSED;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.vehicle.VehicleRunnable;

public class EJBLiteWebVehicleRunner implements VehicleRunnable {
  private final static Logger logger = Logger
      .getLogger(EJBLiteWebVehicleRunner.class.getName());

  protected String getServletPath(String vehicle) {
    return "/" + vehicle + "_vehicle.jsp";
  }

  protected String getQueryString(Properties p) {
    return "?testName=" + TestUtil.getProperty(p, "testName");
  }

  public Status run(String[] argv, Properties p) {
    String vehicle = TestUtil.getProperty(p, "vehicle");
    String contextRoot = TestUtil.getProperty(p, "vehicle_archive_name");
    String requestUrl = "/" + contextRoot + getServletPath(vehicle)
        + getQueryString(p);

    TSURL ctsURL = new TSURL();
    URL url = null;
    HttpURLConnection connection = null;
    int statusCode = Status.NOT_RUN;
    String response = null;
    try {
      url = ctsURL.getURL("http", TestUtil.getProperty(p, "webServerHost"),
          Integer.parseInt(TestUtil.getProperty(p, "webServerPort")), requestUrl);

      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setUseCaches(false);
      logger.info("Connecting " + url.toExternalForm());
      connection.connect();

      response = TestUtil.getResponse(connection).trim();
      if (response.indexOf(TEST_PASSED) >= 0) {
        statusCode = Status.PASSED;
      } else {
        statusCode = Status.FAILED;
      }
    } catch (IOException e) {
      statusCode = Status.FAILED;
      response = "Failed to connect to the test webapp."
          + TestUtil.printStackTraceToString(e);
    }
    return new ReasonableStatus(statusCode, response);
  }
}
