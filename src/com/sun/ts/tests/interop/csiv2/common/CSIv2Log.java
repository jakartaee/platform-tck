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
 * @(#)CSIv2Log.java	1.25 03/09/13
 */

package com.sun.ts.tests.interop.csiv2.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.TSURL;

import java.util.*;

import javax.ejb.*;

import java.rmi.*;
import java.io.*;
import java.net.*;

import javax.naming.*;
import javax.rmi.*;
import javax.net.ssl.*;
import javax.security.auth.x500.X500Principal;

import java.security.cert.*;

/**
 * Singleton CSIv2 Log. Keeps track of all CSIv2-related information during
 * request/reply sequences. Encapsulates interaction with the Logging servlet.
 * <p>
 * This Singleton actually has two instances - one is for remote logging via the
 * logger servlet stored on the RI, and one is for local logging directly via
 * the LogStore. The latter is useful for code we know is running on the RI.
 *
 * @author Mark Roth
 */
public class CSIv2Log {

  /**
   * A vector of Strings. This is the "watch list." The next time a method with
   * a name in the watch list is called, the operation will be logged. The name
   * will be removed from the watch list as soon as the method call returns.
   */
  static Hashtable watchList = new Hashtable();

  /** The instance of the remote log bean. */
  private static CSIv2Log theRemoteLog; // remote from where we are calling from

  /** The instance of the local log bean. */
  private static CSIv2Log theLocalLog; // local to RI

  /**
   * If true, log remotely via LoggerServlet. If false, log locally via local
   * logging
   */
  private boolean logRemotely;

  /** Stores the most recent assertion name encountered. */
  public static String assertion;

  private static String logFileLocation = System
      .getProperty("ri.log.file.location");

  private static String logFileAction = "getLogContent"; // default behavior

  private static String theServerInterceptorLog = logFileLocation
      + File.separator + "serverInterceptorLog.txt";

  private static CSIv2FileLoggerImpl serverInterceptorLogger;

  private static String LOG_SERVLET_PATH = "/logger_servlet_web/LoggerServlet";

  private static String ri_webhostname = System.getProperty("webServerHost.2");

  private static String ri_webportnum = System.getProperty("webServerPort.2");

  private static boolean isInterceptorLoggingRequired = true;

  /**
   * Creates a new CSIv2 log. Writes logs to the given instance.
   */
  private CSIv2Log(boolean remote) {
    this.logRemotely = remote;

    // lets do some log validation first
    String errMsg = "ERROR - CSIv2Log constructor, could not get value for jvm option:  ";

    if (logFileLocation == null) {
      System.err.println(errMsg + "ri.log.file.location");
    } else {
      debug("CSIv2Log constructor - System.getProperty(ri.log.file.location) = "
          + logFileLocation);
    }

    if (ri_webhostname == null) {
      System.err.println(errMsg + "webServerHost.2");
      ri_webhostname = "localhost"; // lets try setting a default
    } else {
      debug("CSIv2Log constructor - System.getProperty(webServerHost.2) = "
          + ri_webhostname);
    }

    if (ri_webportnum == null) {
      System.err.println(errMsg + "webServerPort.2");
      ri_webportnum = "8080"; // lets try setting a default
    } else {
      debug("CSIv2Log constructor - System.getProperty(webServerPort.2) = "
          + ri_webportnum);
    }

    isInterceptorLoggingRequired = getSysPropInterceptorLoggingRequired();

    if (isInterceptorLoggingRequired) {

      debug("creating CSIv2FileLoggerImpl for " + theServerInterceptorLog);
      serverInterceptorLogger = CSIv2FileLoggerImpl
          .getFileLogger(theServerInterceptorLog);
      if (serverInterceptorLogger == null) {
        System.err.println(
            "ERROR in CSIv2Log constructor invoking CSIv2FileLoggerImpl.getFileLogger()");
      }
    }

  }

  /**
   * Adds the given operation name to the watch list. Only operations that match
   * the names in the filter list or the watch list return an active status of
   * true when isActive is called. Starts the watch counter at 0. When it goes
   * up and then back to zero, it is removed from the watch list.
   */
  public static void addWatch(String operationName) {
    if (isInterceptorLoggingRequired) {
      System.out.println("CSIv2log.addWatch():  Watching for call to "
          + operationName + "...");
      if (!watchList.containsKey(operationName)) {
        watchList.put(operationName, new Integer(0));
      }
    }
  }

  public static void clearWatchList() {
    if (isInterceptorLoggingRequired) {
      TestUtil.logTrace("CSIv2log.clearWatchList(): Clearing watch list...");
      watchList = new Hashtable();
    }
  }

  /**
   * Increments the watch counter for the given operation, if it is on the watch
   * list (otherwise, ignores this call).
   */
  public static void incrementWatch(String operationName) {
    if (isInterceptorLoggingRequired) {
      TestUtil.logTrace(
          "CSIv2log.incrementWatch(): Pending Incrementing watch count for "
              + operationName + ".");
      if (watchList.containsKey(operationName)) {
        int i = ((Integer) watchList.get(operationName)).intValue() + 1;
        TestUtil.logTrace(
            "Incrementing watch count for " + operationName + ": " + i);
        watchList.put(operationName, new Integer(i));
      } else {
        TestUtil.logTrace(
            "CSIv2log.incrementWatch(): Did not find " + operationName);
      }
    }
  }

  /**
   * Decrements the watch counter for the given operation, if it is on the watch
   * list (otherwise, ignores this call). When the watch counter reaches zero or
   * below, the method is removed from the watch list.
   */
  public static void decrementWatch(String operationName) {
    if (isInterceptorLoggingRequired) {
      if (watchList.containsKey(operationName)) {
        int i = ((Integer) watchList.get(operationName)).intValue() - 1;
        if (i == -1) {
          // If we are invoking when i == 0, we have not yet reached
          // a starting point, so don't yet remove from the list.
          // Instead, just ignore this call.
          TestUtil.logTrace(
              "CSIv2Log.decrementWatch(): Ignoring first call to decrement "
                  + "watch for " + operationName + "...");
        } else {
          if (i <= 0) {
            TestUtil.logTrace("CSIv2Log.decrementWatch(): Removing "
                + operationName + " from watch list...");
            // remove from watch list.
            watchList.remove(operationName);
          } else {
            TestUtil.logTrace(
                "CSIv2Log.decrementWatch(): Decrementing watch count for "
                    + operationName + ": " + i);
            // decrement counter
            watchList.put(operationName, new Integer(i));
          }
        }
      }
    }
  }

  /**
   * Get an instance of the CSIv2 Log that logs remotely, via the Log Bean.
   */
  public static CSIv2Log getLog() {
    return getLogImpl(true);
  }

  /**
   * Get an instance of the CSIv2 Log that logs locally, via the LogStore.
   */
  public static CSIv2Log getLocalLog() {
    return getLogImpl(false);
  }

  /**
   * Returns the appropriate instance of the CSIv2 Log, depending on the remote
   * parameter.
   * 
   * @param remote
   *          If true, return remote log. If false, return local log.
   */
  private static CSIv2Log getLogImpl(boolean remote) {
    CSIv2Log result;

    if (remote && (theRemoteLog == null)) {
      theRemoteLog = new CSIv2Log(remote);
    } else if (!remote && (theLocalLog == null)) {
      theLocalLog = new CSIv2Log(remote);
    }

    if (remote) {
      result = theRemoteLog;
    } else {
      result = theLocalLog;
    }

    return result;
  }

  /**
   * Starts the log
   */
  public void startLog() {
    if (isInterceptorLoggingRequired) {

      log(1, "<csiv2-log>");
    }
  }

  /**
   * Logs the start of an assertion
   */
  public void logStartAssertion(String name) {
    if (isInterceptorLoggingRequired) {
      log(2, "<assertion name=\"" + name + "\">");
    }
  }

  /**
   * Logs the start of an invocation. This invocation will include both the
   * EJBHome lookup and the EJBRemote invocation.
   */
  public void logStartInvocation() {
    if (isInterceptorLoggingRequired) {
      log(3, "<invocation>");
    }
  }

  /**
   * Logs the start of the ejb-home invocation
   */
  public void logStartEJBHome() {
    if (isInterceptorLoggingRequired) {
      log(4, "<ejb-home>");
    }
  }

  /**
   * Logs the start of a client invocation to either the EJBHome or EJBRemote
   * interface.
   */
  public void logStartClient() {
    if (isInterceptorLoggingRequired) {
      log(5, "<client>");
    }
  }

  /**
   * Logs the start of a client ineterception
   */
  public void logStartClientInterceptor() {
    if (isInterceptorLoggingRequired) {
      log(6, "<client-interceptor>");
    }
  }

  /**
   * Logs an operation name
   */
  public void logOperationName(String operation) {
    if (isInterceptorLoggingRequired) {
      log(7, "<operation>" + operation + "</operation>");
    }
  }

  /**
   * Logs the request service context
   * 
   * @param logEntry
   *          XML representation of this service context, or null if service
   *          context was not present.
   */
  public void logRequestServiceContext(String logEntry) {
    if (isInterceptorLoggingRequired) {
      if (logEntry == null) {
        log(7, "<req-svc-context present=\"false\"/>");
      } else {
        log(7, "<req-svc-context present=\"true\">\n" + logEntry);
        log(7, "</req-svc-context>");
      }
    }
  }

  /**
   * Logs whether SSL is used
   */
  public void logSSLUsed(boolean sslUsed) {
    if (isInterceptorLoggingRequired) {
      log(7, "<ssl-used>" + sslUsed + "</ssl-used>");
    }
  }

  /**
   * Logs a reply
   * 
   * @param t
   *          A throwable or null
   */
  public void logReply(Throwable t) {
    if (isInterceptorLoggingRequired) {
      log(6, "<reply>");
      if (t == null)
        log(7, "<no-exception/>");
      else if (t instanceof javax.ejb.CreateException) {
        log(7, "<create-exception>");
        log(8, t.getMessage().trim());
        log(7, "</create-exception>");
      } else if (t instanceof java.rmi.AccessException) {
        log(7, "<access-exception>");
        log(8, t.getMessage().trim());
        log(7, "</access-exception>");
      } else {
        log(7, "<other-exception>");
        log(8, t.getMessage().trim());
        log(7, "</other-exception>");
      }
      log(6, "</reply>");
    }
  }

  /**
   * Logs whether reply was a location forward
   */
  public void logLocationForward(boolean locationForward) {
    if (isInterceptorLoggingRequired) {
      log(7, "<location-forward>" + locationForward + "</location-forward>");
    }
  }

  /**
   * Logs the reply service context
   * 
   * @param logEntry
   *          XML representation of this service context, or null if service
   *          context was not present.
   */
  public void logReplyServiceContext(String logEntry) {
    if (isInterceptorLoggingRequired) {
      if (logEntry == null) {
        log(7, "<reply-svc-context present=\"false\"/>");
      } else {
        log(7, "<reply-svc-context present=\"true\">\n" + logEntry);
        log(7, "</reply-svc-context>");
      }
    }
  }

  /**
   * Logs a stringified IOR
   */
  public void logIOR(String ior) {
    if (isInterceptorLoggingRequired) {
      log(7, "<ior>" + ior);
      log(7, "</ior>");
    }
  }

  /**
   * Logs the end of the client interception
   */
  public void logEndClientInterceptor() {
    if (isInterceptorLoggingRequired) {
      log(6, "</client-interceptor>");
    }
  }

  /**
   * Logs the start of a server interception
   */
  public void logStartServerInterceptor() {
    if (isInterceptorLoggingRequired) {
      log(6, "<server-interceptor>");
    }
  }

  /**
   * Logs transport client principals from a socket. If the socket is not an SSL
   * socket, there will be 0 principals logged. Otherwise, each of the
   * principals in the certificate chain for the SSLSocket are logged.
   */
  public void logTransportClientPrincipals(Socket socket) {
    if (isInterceptorLoggingRequired) {
      if (socket instanceof SSLSocket) {
        log(7, "<transport-client-principals>");
        try {
          SSLSocket sslSocket = (SSLSocket) socket;
          SSLSession session = sslSocket.getSession();
          Certificate[] certificates = session.getPeerCertificates();
          for (int i = 0; i < certificates.length; i++) {
            String principalName = ((X509Certificate) certificates[i])
                .getSubjectX500Principal().getName(X500Principal.RFC1779);
            log(8, "<principal>" + principalName + "</principal>");
          }
        } catch (SSLPeerUnverifiedException e) {
          // TestUtil.printStackTrace(e);
          // No transport client principal to log. Fall through
          // to finally block with no <principal> elements.
        } finally {
          log(7, "</transport-client-principals>");
        }
      } else {
        // Not an SSL socket.
        log(7, "<transport-client-principals/>");
      }
    } else {
      if (socket instanceof SSLSocket) {
        try {
          SSLSocket sslSocket = (SSLSocket) socket;
          SSLSession session = sslSocket.getSession();
          Certificate[] certificates = session.getPeerCertificates();
          log("<transport-client-principals>");
          for (int i = 0; i < certificates.length; i++) {
            String principalName = ((X509Certificate) certificates[i])
                .getSubjectX500Principal().getName(X500Principal.RFC1779);
            log("<peer_principal>" + principalName + "</peer_principal>");
          }
          Certificate[] certificates2 = session.getLocalCertificates();
          for (int i = 0; i < certificates2.length; i++) {
            String principalName = ((X509Certificate) certificates2[i])
                .getSubjectX500Principal().getName(X500Principal.RFC1779);
            log("<local_principal>" + principalName + "</local_principal>");
          }
        } catch (SSLPeerUnverifiedException e) {
          // TestUtil.printStackTrace(e);
          // No transport client principal to log. Fall through
          // to finally block with no <principal> elements.
        } finally {
          log("</transport-client-principals>");
        }
      } else {
        // Not an SSL socket.
        log("Not an SSL socket");
        log("<transport-client-principals/>");
      }

    }
  }

  /**
   * Logs the start of a server invocation
   */
  public void logStartServer() {
    if (isInterceptorLoggingRequired) {
      log(7, "<server>");
    }
  }

  /**
   * Logs an invocation principal
   */
  public void logInvocationPrincipal(String principal) {
    if (isInterceptorLoggingRequired) {
      log(8, "<invocation-principal>" + principal + "</invocation-principal>");
    } else {
      log("<invocation-principal>" + principal + "</invocation-principal>");
    }
  }

  /**
   * Logs the end of a server invocation
   */
  public void logEndServer() {
    if (isInterceptorLoggingRequired) {
      log(7, "</server>");
    }
  }

  /**
   * Logs the end of a server interception
   */
  public void logEndServerInterceptor() {
    if (isInterceptorLoggingRequired) {
      log(6, "</server-interceptor>");
    }
  }

  /**
   * Logs the end of a client invocation.
   */
  public void logEndClient() {
    if (isInterceptorLoggingRequired) {
      log(5, "</client>");
    }
  }

  /**
   * Logs the start of the ejb-home invocation
   */
  public void logEndEJBHome() {
    if (isInterceptorLoggingRequired) {
      log(4, "</ejb-home>");
    }
  }

  /**
   * Logs the start of the ejb-remote invocation
   */
  public void logStartEJBRemote() {
    if (isInterceptorLoggingRequired) {
      log(4, "<ejb-remote>");
    }
  }

  /**
   * Logs the start of the ejb-home invocation
   */
  public void logEndEJBRemote() {
    if (isInterceptorLoggingRequired) {
      log(4, "</ejb-remote>");
    }
  }

  /**
   * Logs the end of an invocation
   */
  public void logEndInvocation() {
    if (isInterceptorLoggingRequired) {
      log(3, "</invocation>");
    }
  }

  /**
   * Logs the end of an assertion
   */
  public void logEndAssertion() {
    if (isInterceptorLoggingRequired) {
      log(2, "</assertion>");
    }
    CSIv2Log.assertion = null;
  }

  /**
   * Ends the log
   */
  public void endLog() {
    if (isInterceptorLoggingRequired) {
      log(1, "</csiv2-log>");
    }
  }

  /**
   * Gets a copy of the log in its current form.
   */
  public String getLogContents() {

    String result = null;
    if (logRemotely) {
      try {
        result = invokeServletAndGetLogContent(ri_webhostname, ri_webportnum);
      } catch (Exception e) {
        TestUtil.logErr("COULD NOT GET LOG INFO FROM LOGGER SERVLET, RESULT: ",
            e);
        System.err.println(
            "COULD NOT GET LOG INFO FROM LOGGER SERVLET, RESULT: " + e);
      }
    } else {
      // log access is local (likely from in the interceptor) so dont
      // make servlet call

      try {
        result = serverInterceptorLogger.get();
      } catch (Exception e) {
        TestUtil.logErr("ERROR: Could not read log info from file: "
            + theServerInterceptorLog + "   due to exception:  ", e);
        System.err.println("ERROR: Could not read log info from file: "
            + theServerInterceptorLog + "   due to exception:  " + e);
      }
    }

    return result;
  }

  /**
   * Purges the log
   */
  public void purge() {
    if (isInterceptorLoggingRequired) {
      if (logRemotely) {
        try {
          invokeServletAndPurgeLog(ri_webhostname, ri_webportnum);
        } catch (Exception e) {
          TestUtil.logErr("COULD NOT PURGE LOG FILE: ", e);
          System.err.println("COULD NOT PURGE LOG FILE: " + e);
        }
      } else {
        // purge is local, likely from in the interceptor - so dont do
        // servlet call

        try {
          serverInterceptorLogger.purge();
        } catch (Exception e) {
          TestUtil.logErr("ERROR: Could not invoke localLogger.purge(): ", e);
          System.err
              .println("ERROR: Could not invoke localLogger.purge(): " + e);
        }
      }
    }

  }

  private void log(String message) {
    if (isInterceptorLoggingRequired) {

      if (logRemotely) {
        try {
          String result = invokeServletAndLogMessage(ri_webhostname,
              ri_webportnum, message);
        } catch (Exception e) {
          TestUtil.logErr("ERROR: Could not write message to log: ", e);
          System.err.println("ERROR: Could not write message to log: " + e);
        }
      } else {
        // local logging (likely from in the interceptor) so dont do
        // servlet call

        try {
          serverInterceptorLogger.log(message);
        } catch (Exception e) {
          TestUtil.logErr("ERROR: Could not log message to localLogger: ", e);
          System.err
              .println("ERROR: Could not log message to localLogger: " + e);
        }

        // Snoop the log messages, get assertion name and store it, if
        // found.
        if (message.indexOf("<assertion name") > 0) {
          int start = message.indexOf("\"") + 1;
          int end = message.indexOf("\"", start);
          CSIv2Log.assertion = message.substring(start, end);
          debug("Assertion name: " + CSIv2Log.assertion);

        }
      }
    }
  }

  private void log(int index, String message) {

    if (isInterceptorLoggingRequired) {

      // Based on the index the message is spaced(aligned) accordingly
      //
      // For example
      // if message="<element>";
      //
      // log( 1 , message) will prefix zero spaces in front of the message
      // The output will be "<element>"
      //
      // log( 2 , message) will prefix 2 spaces in front of the message
      // The output will be " <element>"
      //
      // log( 3 , message) will prefix 4 spaces in front of the message
      // The output will be " <element>"
      //
      // log( 4 , message) will prefix 6 spaces in front of the message
      // The output will be " <element>"
      //
      // i.e insert 2*(index - 1) spaces in front of the message

      StringBuffer strbuf = new StringBuffer(message);

      // i.e insert 2*(index-1) spaces in front of the buffer
      for (int i = 0; i < (2 * (index - 1)); i++)
        strbuf.insert(i, ' ');

      message = strbuf.toString();

      if (logRemotely) {
        // Temporarily suspend CSIv2 Logging interceptor so we do not
        // log
        // create calls to the log bean itself:

        try {
          String rval = invokeServletAndLogMessage(ri_webhostname,
              ri_webportnum, message);
        } catch (Exception e) {
          TestUtil.logErr("ERROR: Could not log message to log: ", e);
          System.err.println("ERROR: Could not log message to log: " + e);
        }
      } else {
        // local logging (likely from in the interceptor) so dont make
        // servlet call
        serverInterceptorLogger.log(message);
      }
    }
  }

  public static String binHex(byte[] bytes) {
    StringBuffer result = new StringBuffer("");
    char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
        'B', 'C', 'D', 'E', 'F' };

    for (int i = 0; i < bytes.length; i++) {
      int b = (int) bytes[i];
      result.append(digits[(b & 0xF0) >> 4]);
      result.append(digits[b & 0x0F]);
    }

    return result.toString();
  }

  /**
   * Converts the given byte array to binhex and logs it one line at a time,
   * with 32 bytes-worth per line. This is required because of a limitation of a
   * log message of 255 bytes.
   */
  private void logHex(byte[] bytes) {
    String hex = binHex(bytes);
    for (int i = 0; i < hex.length(); i += 220) {
      String line = hex.substring(i, Math.min(i + 220, hex.length()));
      TestUtil.logMsg(line);
      // System.out.println( line );
      log(line);
    }
  }

  /*
   * NOTE: This was used when we had a logBean and controlBean but this method
   * is not needed to enable logging anymore. It is still used to manipulate the
   * watchList. Because this is referenced in so many places, we are going to
   * leave this method here but no longer do anything with the passed in args
   * since we no longer have a logBean nor controlBean.
   */
  public void enableLoggingInterceptor(boolean enableLocal, boolean enableRI) {
    if (isInterceptorLoggingRequired) {
      CSIv2Log.clearWatchList();
    }
  }

  private String getLogAssertion() {

    String rval = null;

    try {
      rval = invokeServletAndGetAssertionName(ri_webhostname, ri_webportnum);
      debug("CSIv2Log.getLogAssertion():  Assertion name = " + rval);
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Could not getAssertionName from log. ", e);
      System.err.println("ERROR: Could not getAssertionName from log. " + e);
    }

    return rval;
  }

  /*
   * This is a convenience method used to post a url to a servlet so that our
   * servlet can get the assertion name from in the current server side log.
   * This passes some params onto the request/context so that the servlet will
   * have info it needs in order to properly perform its serverside ACF and ACP
   * tests.
   *
   */
  private String invokeServletAndGetAssertionName(String hostname,
      String portnum) {

    return invokeServletAndGetResponse("POST", "getAssertion", hostname,
        portnum, null);
  }

  /*
   * This is a convenience method used to post a url to a servlet so that our
   * servlet can do some tests and send back status about success or failure.
   * This passes some params onto the request/context so that the servlet will
   * have info it needs in order to properly perform its serverside ACF and ACP
   * tests.
   *
   */
  private String invokeServletAndGetLogContent(String hostname,
      String portnum) {

    return invokeServletAndGetResponse("POST", "getLogContent", hostname,
        portnum, null);
  }

  /*
   * This is used to remove the current log file on the server side. (Removal
   * can include deletion of the log file or saving it off to an other name.
   * There is a ri/server side setting which specifies if we delete or rename
   * the log file.
   */
  private String invokeServletAndPurgeLog(String hostname, String portnum) {

    debug("in CSIv2Log.invokeServletAndPurgeLog():  hostname = " + hostname);
    debug("in CSIv2Log.invokeServletAndPurgeLog():  portnum = " + portnum);
    return invokeServletAndGetResponse("POST", "Purge", hostname, portnum,
        null);
  }

  /*
   * this is used to send a message to the ri/server side where it will be
   * logged.
   */
  private String invokeServletAndLogMessage(String hostname, String portnum,
      String logMsg) {

    debug("in CSIv2Log.invokeServletAndLogMessage():  hostname = " + hostname);
    debug("in CSIv2Log.invokeServletAndLogMessage():  portnum = " + portnum);
    debug("in CSIv2Log.invokeServletAndLogMessage():  logMsg = " + logMsg);
    return invokeServletAndGetResponse("POST", "Log", hostname, portnum,
        logMsg);
  }

  /*
   * Invokes the servlet at passed in context (e.g. sContext) and specifies the
   * hostname and portnum that the servlet can be accessed at. Additionally, the
   * passed in logFileAction indicates what action we want to take on the
   * servlet.
   */
  private String invokeServletAndGetResponse(String requestMethod, // GET or
                                                                   // POST
      String logFileAction, String hostname, String sPortnum,
      String logMessage) {
    String sContext = LOG_SERVLET_PATH;
    String retVal = null;
    if (isInterceptorLoggingRequired) {

      int portnum = 8080;
      if (sPortnum != null) {
        // port was specified so use it
        portnum = Integer.parseInt(sPortnum);
      } else {
        String msg = "ERROR - CSIv2Log.invokeServletAndGetResponse():  invalid portnum passed in. Check JVM settings.";
        TestUtil.logErr(msg);
        System.err.println(msg);
      }

      if (hostname == null) {
        String msg = "ERROR - CSIv2Log.invokeServletAndGetResponse():  invalid hostname passed in. Check JVM settings.";
        TestUtil.logErr(msg);
        System.err.println(msg);
        hostname = "localhost"; // default since none specified
      }

      if (!sContext.startsWith("/")) {
        sContext = "/" + sContext;
      }

      HttpURLConnection conn = null;
      DataOutputStream wr = null;

      try {
        String urlParameters = null;
        urlParameters = "ri.log.file.location="
            + URLEncoder.encode(logFileLocation, "UTF-8") + "&log.file.action="
            + URLEncoder.encode(logFileAction, "UTF-8") + "&log.file.name="
            + URLEncoder.encode("serverInterceptorLog.txt", "UTF-8");

        if (logMessage != null) {
          urlParameters = urlParameters + "&log.message="
              + URLEncoder.encode(logMessage, "UTF-8");
        }

        String url = "http://" + hostname + ":" + portnum + "/" + sContext;
        URL newURL = new URL(url);

        // open URLConnection
        conn = (HttpURLConnection) newURL.openConnection();

        // set request property
        if (requestMethod.equalsIgnoreCase("POST")) {
          conn.setDoInput(true);
          conn.setDoOutput(true);
        }
        conn.setRequestMethod(requestMethod); // use POST or GET etc

        conn.setRequestProperty("Content-Type",
            "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length",
            "" + Integer.toString(urlParameters.getBytes().length));
        conn.setRequestProperty("Content-Language", "en-US");
        conn.setUseCaches(false);

        // Send request
        wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        // Get Response
        int code = conn.getResponseCode();

        StringBuilder sbResponse = new StringBuilder();
        InputStream content = (InputStream) conn.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(content));
        try {
          String line;
          while ((line = in.readLine()) != null) {
            sbResponse.append(line + "\n");
            // TestUtil.logTrace(line);
          }
        } catch (Exception ex) {
          TestUtil.logMsg(
              "Exception reading response in CSIv2Log.invokeServletAndGetResponse().");
          TestUtil.logTrace(
              "Exception in CSIv2Log.invokeServletAndGetResponse(): ", ex);
        } finally {
          in.close();
        }

        retVal = sbResponse.toString();

        return retVal;
      } catch (Exception e) {
        TestUtil.logErr(
            "Abnormal return status and Exception encountered while invoking "
                + sContext);
        TestUtil.logTrace(
            "Exception in CSIv2Log.invokeServletAndGetResponse() is ", e);
      } finally {
        try {
          wr.close();
        } catch (Exception e) {
        }
      }

    }
    return "No CSIv2 logs. Nothing was recorded";
  } // invokeServletAndGetResponse()

  private void debug(String str) {
    TestUtil.logTrace(str);
  }

  private static boolean getSysPropInterceptorLoggingRequired() {
    String sysPropText = System.getProperty("interceptor.logging.required");

    if (sysPropText != null) {
      isInterceptorLoggingRequired = Boolean.parseBoolean(sysPropText);
      TestUtil.logTrace("System property interceptor.logging.required = "
          + isInterceptorLoggingRequired);
    }

    return isInterceptorLoggingRequired;
  }

}
