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

package com.sun.ts.tests.servlet.spec.servletresponse;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.TimeZone;
import java.util.Calendar;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.*;

public class URLClient extends EETest {

  String host = null;

  int port;

  private static final String contextPath = "/servlet_spec_servletresponse_web";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {
    // setContextRoot(CONTEXT_ROOT);
    // setServletName("HttpTestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      // get props
      port = Integer.parseInt(p.getProperty("webServerPort"));
      host = p.getProperty("webServerHost");

      // check props for errors
      if (port < 1) {
        throw new Exception("'port' in ts.jte must be > 0");
      }
      if (host == null) {
        throw new Exception("'host' in ts.jte must not be null ");
      }

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed!", e);
    }
  }

  /* Run test */
  /*
   * @testName: testFlushBufferHttp
   * 
   * @assertion_ids: Servlet:JAVADOC:603
   * 
   * @test_Strategy: Servlet writes data in the buffer and flushes it; Verify
   * data is sent back to client due to the flush, not by exiting service method
   * This is done by sleeping a long time between flush and exit in servlet;
   * Then verify time gap on client side.
   */

  public void testFlushBufferHttp() throws Fault {
    TestUtil.logTrace("testFlushBufferHttp");
    try {
      URL u = new URL(
          "http://" + host + ":" + port + contextPath + "/HttpTestServlet");
      TestUtil.logTrace("URL: http://" + host + ":" + port + contextPath
          + "/HttpTestServlet");

      InputStream is = u.openStream();
      BufferedReader bis = new BufferedReader(new InputStreamReader(is));
      String line = null;
      long time1 = 0L;
      long time2 = 0L;

      while ((line = bis.readLine()) != null) {
        TestUtil.logTrace(line);
        if (line.contains("flushBufferTest for compatibility")
            && (time1 == 0L)) {
          Calendar cal1 = Calendar.getInstance();
          time1 = cal1.getTimeInMillis();
          TestUtil.logTrace("Buffer flush clocked at time " + time1);
        }

        if (line.contains("Test Failed") && (time2 == 0L)) {
          Calendar cal2 = Calendar.getInstance();
          time2 = cal2.getTimeInMillis();
          TestUtil.logTrace("service method exit clocked at time " + time2);
        }
      }
      bis.close();

      if (((time2 - time1) > 5000) && (time1 != 0L) && (time2 != 0L)) {
        TestUtil.logTrace(
            "Test passed.  There is decent time difference between two clocked time.");
      } else {
        throw new Fault(
            "Test failed: there is not enough time between two clocked time");
      }

    } catch (java.net.UnknownHostException exuh) {
      exuh.printStackTrace();
      throw new Fault("Test failed with the above exception");
    } catch (java.io.IOException exio) {
      exio.printStackTrace();
      throw new Fault("Test failed with the above exception");
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("Test failed with the above exception");
    }
  }

  /*
   * @testName: testFlushBuffer
   * 
   * @assertion_ids: Servlet:JAVADOC:153
   * 
   * @test_Strategy: Servlet writes data in the buffer and flushes it; Verify
   * data is sent back to client due to the flush, not by exiting service method
   * This is done by sleeping a long time between flush and exit in servlet;
   * Then verify time gap on client side.
   */

  public void testFlushBuffer() throws Fault {
    TestUtil.logTrace("testFlushBuffer");

    try {
      URL u = new URL(
          "http://" + host + ":" + port + contextPath + "/TestServlet");
      TestUtil.logTrace(
          "URL: http://" + host + ":" + port + contextPath + "/TestServlet");

      InputStream is = u.openStream();
      BufferedReader bis = new BufferedReader(new InputStreamReader(is));
      String line = null;
      long time1 = 0L;
      long time2 = 0L;

      while ((line = bis.readLine()) != null) {
        TestUtil.logTrace(line);
        if (line.contains("flushBufferTest for compatibility")
            && (time1 == 0L)) {
          Calendar cal1 = Calendar.getInstance();
          time1 = cal1.getTimeInMillis();
          TestUtil.logTrace("Buffer flush clocked at time " + time1);
        }

        if (line.contains("Test Failed") && (time2 == 0L)) {
          Calendar cal2 = Calendar.getInstance();
          time2 = cal2.getTimeInMillis();
          TestUtil.logTrace("service method exit clocked at time " + time2);
        }
      }

      bis.close();

      if (((time2 - time1) > 5000) && (time1 != 0L) && (time2 != 0L)) {
        TestUtil.logTrace(
            "Test passed.  There is decent time difference between two clocked time.");
      } else {
        throw new Fault(
            "Test failed: there is not enough time between two clocked time");
      }

    } catch (java.net.UnknownHostException exuh) {
      exuh.printStackTrace();
      throw new Fault("testFlushBuffer failed with the above exception");
    } catch (java.io.IOException exio) {
      exio.printStackTrace();
      throw new Fault("testFlushBuffer failed with the above IOException");
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new Fault("testFlushBuffer failed with the above exception");
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("[BaseUrlClient] Test cleanup OK");
  }

}
