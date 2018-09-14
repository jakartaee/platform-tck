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

package com.sun.ts.tests.jms.common;

import javax.jms.*;
import java.util.*;
import com.sun.ts.lib.util.*;

/**
 * JmsUtil is a final tool class that will provide support for common code for
 * jms tests.
 * 
 * @author Irene Caruso
 * 
 */
public final class JmsUtil {
  public static boolean test1SecondTime = false;

  public static boolean test2SecondTime = false;

  public static boolean test2Results = false;

  public static boolean test7SecondTime = false;

  // used by initHarnessProps() to cache last value
  private static String cHost = null;

  private static String cTrace = null;

  private static String cPort = null;

  // used by addPropsToMessage() to cache last value
  private static String hHost = null;

  private static String hTrace = null;

  private static String hPort = null;

  /**
   * used by jms tests to pass cts properties object to mdb to initialize cts
   * logging mechanism
   *
   * @param p
   *          the Properties object
   * @param msg
   *          the JMS Message object
   * 
   */

  public static void addPropsToMessage(Message msg, java.util.Properties p) {
    String hostname = null;
    String traceFlag = null;
    String logPort = null;
    Enumeration e = null;
    String key = null;
    String notValid = ".";

    try {

      // get the properties that have "." in them
      // can't put "." in JMS message, rename them.

      // cache last value for these props -
      // sometimes they are null when passed
      // ??Harness issue??

      hostname = p.getProperty("harness.host");
      TestUtil.logTrace("Hostname " + hostname);
      if (hostname == null) {
        if (hHost != null)
          msg.setStringProperty("harnesshost", hHost);
        else {
          TestUtil.logTrace("addPropsToMsg: Hostname is null");
          throw new Exception("Error getting hostname");
        }
      } else {
        msg.setStringProperty("harnesshost", hostname);
        hHost = hostname;
      }

      traceFlag = p.getProperty("harness.log.traceflag");
      TestUtil.logTrace("testFlag  " + traceFlag);
      if (traceFlag == null) {
        if (hTrace != null)
          msg.setStringProperty("harnesslogtraceflag", hTrace);
        else {
          TestUtil.logTrace("addProps:traceflag is null");
          throw new Exception("Error getting traceflag");
        }
      } else {
        msg.setStringProperty("harnesslogtraceflag", traceFlag);
        hTrace = traceFlag;
      }

      logPort = p.getProperty("harness.log.port");
      TestUtil.logTrace("logPort  " + logPort);
      if (logPort == null) {
        if (hPort != null)
          msg.setStringProperty("harnesslogport", hPort);
        else {
          TestUtil.logTrace("addProps: logport is null");
          throw new Exception("Error getting port");
        }
      } else {
        msg.setStringProperty("harnesslogport", logPort);
        hPort = logPort;
      }

      // get the rest of the props to put in JMS message.
      // Sql queries are currently passed as props, may need
      // them in the message for testing w/ DB's

      e = p.propertyNames();
      key = null;
      while (e.hasMoreElements()) {
        key = (String) e.nextElement();
        TestUtil.logTrace("addProps: " + key);
        if ((key.indexOf(notValid) == -1) && (key.indexOf("***") == -1)
            && !(key.startsWith("JMS"))) {
          TestUtil.logTrace("addProps: add property " + key);
          msg.setStringProperty(key, p.getProperty(key));
        }
      }
    } catch (Exception ex) {
      TestUtil.printStackTrace(ex);
      TestUtil.logMsg("Error setting harness Properties in jms msg");
    }
  }

  /**
   * used by MDB onMessage() to extract cts properties from JMS Message to
   * initialize cts logging mechanism
   *
   * @param p
   *          the Properties object
   * @param msg
   *          the JMS Message object
   * 
   */
  public static void initHarnessProps(Message msg, java.util.Properties p) {

    String hostname = null;
    String traceflag = null;
    String logport = null;

    try {
      hostname = msg.getStringProperty("harnesshost");
      TestUtil.logTrace("initHarn: Hostname " + hostname);
      if (hostname == null) {
        TestUtil.logTrace("intiHarn:Hostname is null");
        if (cHost != null)
          p.put("harness.host", cHost);
        else
          throw new Exception("Error getting hostname");
      } else {
        p.put("harness.host", hostname);
        cHost = hostname;
      }

      traceflag = msg.getStringProperty("harnesslogtraceflag");
      TestUtil.logTrace("initHarn:traceflag " + traceflag);
      if (traceflag == null) {
        TestUtil.logTrace("initHarn: is null");
        if (cTrace != null)
          p.put("harness.log.traceflag", cTrace);
        else
          throw new Exception("Error getting traceflag");
      } else {
        p.put("harness.log.traceflag", traceflag);
        cTrace = traceflag;
      }

      logport = msg.getStringProperty("harnesslogport");
      TestUtil.logTrace("initHarn:logport " + logport);
      if (logport == null) {
        TestUtil.logTrace("initHarn:logport is null");
        if (cPort != null)
          p.put("harness.log.port", cPort);
        else
          throw new Exception("Error getting port");
      } else {
        p.put("harness.log.port", logport);
        cPort = logport;
      }

      // now pull out the rest of the properties from the message
      Enumeration e = msg.getPropertyNames();
      String key = null;
      while (e.hasMoreElements()) {
        key = (String) e.nextElement();
        if (!key.startsWith("JMS"))
          p.put(key, msg.getStringProperty(key));
      }

      // now initialize the props
      TestUtil.init(p);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
    }
  }

  public static void sendTestResults(String testCase, boolean results,
      QueueSession qSession, javax.jms.Queue queueR) {
    TextMessage msg = null;
    QueueSender mSender = null;

    TestUtil.logTrace("*@$#)@(@#$ --- - -   sendTestResults ");

    try {
      // create a msg sender for the response queue
      mSender = qSession.createSender(queueR);
      // and we'll send a text msg
      msg = qSession.createTextMessage();
      msg.setStringProperty("TestCase", testCase);
      msg.setText(testCase);
      if (results)
        msg.setStringProperty("Status", "Pass");
      else
        msg.setStringProperty("Status", "Fail");
      TestUtil.logTrace("*@$#)@(@$#@($----Sending response message ");
      TestUtil.logTrace(
          "*@$#)@(@ ----- status: " + msg.getStringProperty("Status"));
      TestUtil.logTrace(
          "*@$#)@(@# -----test: " + msg.getStringProperty("TestCase"));
      mSender.send(msg);

    } catch (JMSException je) {
      TestUtil.printStackTrace(je);
    } catch (Exception ee) {
      TestUtil.printStackTrace(ee);
    }
  }
}
