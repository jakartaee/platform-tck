/*
 * Copyright (c)  2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.interop.csiv2.rionly;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.annotation.security.PermitAll;

import com.sun.ts.tests.interop.csiv2.common.CSIv2FileLoggerImpl;

public class LoggerServlet extends HttpServlet {
  private static String saveLogFile = System.getProperty("csiv2.save.log.file");

  private static String logFileLocation;

  private static String logFilename;

  private static String logFileAction; // action to be performed against log
                                       // file (eg. getLogContent, etc)

  private static String fullPathAndFilename;

  private static String logMessage;;

  // private static String CONTENT_TYPE = "text/xml;charset=ISO-8859-1";
  // private static String CONTENT_TYPE = "application/x-www-form-urlencoded";
  // private static String CONTENT_TYPE = "text/plain; charset=UTF-8";
  private static String CONTENT_TYPE = "text/xml; charset=UTF-8";

  @PermitAll
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    debug("in LoggerServlet.doGet()");
    debug("request.getQueryString() = " + request.getQueryString());
    if (request.getQueryString().indexOf("ping") >= 0) {
      ping(response);
    } else {
      handleGet(request, response);
    }
  }

  @PermitAll
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    debug("in LoggerServlet.doPost() ....." + "<BR>");
    handlePost(request, response);
  }

  private void handleGet(HttpServletRequest request,
      HttpServletResponse response) {

    debug("in LoggerServlet.handleGet()");

    // get some common props
    getPropsAndParams(request, response);

    debug("LoggerServlet.handleGet():  logFileAction = " + logFileAction);

    if (logFileAction == null) {
      debug("logFileAction not passed into LoggerServlet");
    } else if (logFileAction.equals("getLogContent")) {
      getLogContent(response);
    } else if (logFileAction.equals("getAssertion")) {
      getAssertion(response); // get assertion name associated w/ current log
    }
  }

  private void handlePost(HttpServletRequest request,
      HttpServletResponse response) {

    debug("in LoggerServlet.handlePost()");

    // get some common props
    getPropsAndParams(request, response);

    debug("LoggerServlet.handlePost():  logFileAction = " + logFileAction);

    if (logFileAction == null) {
      debug("logFileAction not passed into LoggerServlet");
    } else if (logFileAction.equals("Purge")) {
      purgeLogFile(response); // purge content
    } else if (logFileAction.equals("Log")) {
      doLog(response, logMessage);
    } else if (logFileAction.equals("getLogContent")) {
      getLogContent(response);
    } else if (logFileAction.equals("getAssertion")) {
      getAssertion(response); // get assertion name associated w/ current log
    }
  }

  private void getPropsAndParams(HttpServletRequest req,
      HttpServletResponse response) {

    // set logfile location
    logFileLocation = req.getParameter("ri.log.file.location");
    if (logFileLocation == null) {
      String str = "ERROR:	LoggerServlet.getPropsAndParams(): logFileLocation NOT specified.";
      str += "  ensure that you are setting ri.log.file.location correct in ts.jte";
      System.err.println(str);
    } else {
      // if here, we have logfile location value which contains our csiv2 log
      // info
      debug("LoggerServlet.logFileLocation specified to exist at: "
          + logFileLocation);
    }

    logFilename = req.getParameter("log.file.name");
    if (logFilename == null) {
      System.err.println(
          "ERROR:  LoggerServlet.getPropsAndParams(): logFilename NOT specified.");
    } else {
      debug("LoggerServlet.getPropsAndParams(): logFilename = " + logFilename);
    }

    // set vendor class
    logFileAction = req.getParameter("log.file.action");
    if (logFileAction == null) {
      System.err.println(
          "ERROR:  LoggerServlet.getPropsAndParams(): logFileAction NOT specified.");
    } else {
      debug("LoggerServlet.getPropsAndParams(): logFileAction = "
          + logFileAction);
    }

    if ((logFileLocation == null) || (logFilename == null)) {
      // error
      fullPathAndFilename = null;
      System.err.println(
          "ERROR:  LoggerServlet.getPropsAndParams(): fullPathAndFilename = null!");
    } else {
      fullPathAndFilename = logFileLocation + File.separator + logFilename;
      debug("LoggerServlet.getPropsAndParams(): fullPathAndFilename = "
          + fullPathAndFilename);
    }

    logMessage = req.getParameter("log.message");
    debug("LoggerServlet.getPropsAndParams(): logMessage = " + logMessage);

    return;
  }

  private void purgeLogFile(HttpServletResponse response) {
    try {
      debug("in LoggerServlet.purgeLogFile()....");

      if (fullPathAndFilename != null) {
        CSIv2FileLoggerImpl logger = CSIv2FileLoggerImpl
            .getFileLogger(fullPathAndFilename);
        debug("saveLogFile = " + Boolean.valueOf(saveLogFile));
        boolean bval = logger.purge(Boolean.valueOf(saveLogFile));
        if (!bval) {
          System.err.println(
              "ERROR occurred  trying to delete file: " + fullPathAndFilename);
        }
      } else {
        System.err.println(
            "ERROR:  LoggerServlet.purgeLogFile():  fullPathAndFilename = null");
      }

    } catch (Exception ex) {
      System.err.println("got exception in LoggerServlet.purgeLogFile()");
      ex.printStackTrace();
    }
  }

  private void getLogContent(HttpServletResponse response) {
    PrintWriter out = null;
    debug("in LoggerServlet.getLogContent()");
    BufferedReader br = null;

    try {
      response.setContentType(CONTENT_TYPE);
      out = response.getWriter();

      if (fullPathAndFilename != null) {
        // open log file, read content, and send it back
        debug("in getLogContent():  fullPathAndFilename != null");

        br = new BufferedReader(new FileReader(fullPathAndFilename));
        String strLine;
        while ((strLine = br.readLine()) != null) {
          out.println(strLine); // send it back
          debug("    --> LoggerServlet.getLogContent() sending back:  "
              + strLine);
        }
        out.flush();
      } else {
        System.err.println(
            "ERROR:  LoggerServlet.getLogContent():  fullPathAndFilename = null");
      }
    } catch (Exception ex) {
      System.err.println("got exception in LoggerServlet.getLogContent()");
      ex.printStackTrace();
    } finally {
      try {
        if (br != null) {
          br.close();
        }
        if (out != null) {
          out.close();
        }
      } catch (Exception ex) {
        System.err.println(
            "got (finally) exception in LoggerServlet.getLogContent()");
        ex.printStackTrace();
      }
    }
  }

  /*
   * this will open the logfile, find the assertion name in it (if there is an
   * 'assertion' element. Then this will send the name for that assertion back
   * to the caller.
   */
  private void getAssertion(HttpServletResponse response) {
    PrintWriter out = null;
    debug("in LoggerServlet.getAssertion");
    try {
      debug("in getAssertion()");
      response.setContentType(CONTENT_TYPE);
      out = response.getWriter();

      String assertionName = getAssertionName();
      out.println(assertionName);
      out.flush();
    } catch (Exception ex) {
      System.err.println("got exception in LoggerServlet.getAssertion()");
      ex.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (Exception ex) {
      }
    }

  }

  private String getAssertionName() {
    String rval = null;
    try {
      debug("in getAssertionName()");
      if (fullPathAndFilename != null) {
        CSIv2FileLoggerImpl logger = CSIv2FileLoggerImpl
            .getFileLogger(fullPathAndFilename);
        rval = logger.getAssertionName();
      } else {
        System.err.println(
            "ERROR:  LoggerServlet.getAssertionName():  fullPathAndFilename = null");
      }
    } catch (Exception ex) {
      System.err.println("got exception in LoggerServlet.getAssertionName()");
      ex.printStackTrace();
    }

    return rval;
  }

  private void ping(HttpServletResponse response) {
    debug("in ping()");
    PrintWriter out = null;
    try {
      out = response.getWriter();
      out.println("in LoggerServlet.ping()");
      out.flush();
    } catch (Exception ex) {
      System.err.println("got exception in LoggerServlet.ping()");
      ex.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (Exception ex) {
      }
    }
  }

  private void doLog(HttpServletResponse response, String msg) {
    try {
      debug("in LoggerServlet.log()");

      if (fullPathAndFilename != null) {
        // open log file, read content, and send it back
        debug("LoggerServlet.log():  fullPathAndFilename != null");
        CSIv2FileLoggerImpl logger = CSIv2FileLoggerImpl
            .getFileLogger(fullPathAndFilename);
        logger.log(msg);
      } else {
        System.err.println(
            "ERROR:  LoggerServlet.log():  fullPathAndFilename = null");
      }
    } catch (Exception ex) {
      System.err.println("got exception in LoggerServlet.log()");
      ex.printStackTrace();
    }

  }

  private void debug(String str) {
    System.out.println(str);
  }

}
