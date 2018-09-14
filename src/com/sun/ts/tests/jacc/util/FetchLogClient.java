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
 * @(#)FetchLogClient.java	1.4 03/05/16
 */

package com.sun.ts.tests.jacc.util;

import javax.ejb.*;
import javax.rmi.PortableRemoteObject;
import java.rmi.*;
import javax.naming.*;
import java.net.*;
import java.io.*;
import java.util.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.porting.*;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import com.sun.ts.tests.jacc.provider.TSLogger;
import com.sun.ts.tests.jacc.provider.TSLogRecord;
import com.sun.ts.tests.jacc.provider.TSXMLFormatter;

public class FetchLogClient extends EETest {
  private Properties props = null;

  private TSURL tsURL = new TSURL();

  private String webServerHost = "unknown";

  private int webServerPort = 8000;

  private String SERVLET = "/jaccRoot/FetchLogs";

  private String logFileLocation = null;

  public static void main(String args[]) {
    FetchLogClient theTests = new FetchLogClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: log.file.location; webServerHost; webServerPort;
   * 
   * 
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;
    try {
      logFileLocation = p.getProperty("log.file.location");
      if (logFileLocation == null)
        pass = false;

      webServerHost = p.getProperty("webServerHost");
      if (webServerHost == null)
        pass = false;
      else if (webServerHost.equals(""))
        pass = false;

      try {
        webServerPort = Integer.parseInt(p.getProperty("webServerPort"));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);

        pass = false;
      }

      TestUtil.logMsg("webServerHost = " + webServerHost);
      TestUtil.logMsg("webServerPort = " + webServerPort);
      TestUtil.logMsg("log.file.location = " + logFileLocation);

      if (!pass) {
        TestUtil
            .logErr("Please specify web server host & port" + "in ts.jte file");
        throw new Fault("Setup Failed: WebServer host/port not set");
      }
      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      throw new Fault("Setup Failed:", e);
    }

  }

  public void cleanup() throws Fault {
  }

  /**
   * testName: fetchLogClientTest
   * 
   * @assertion: This is a sample test used for fetching log messages created by
   *             TSlogger.
   */
  public void fetchLogClientTest() throws Fault {

    long seqNum = 0L;
    String contextId = null;
    Collection recordCollection = null;
    Properties props = null;
    URL url = null;

    try {

      // Create a tsLogger
      TSLogger tsLogger = TSLogger.getTSLogger("jacc");
      FileHandler tsFileHandler = new FileHandler(
          logFileLocation + "/jacc_log.txt");
      tsFileHandler.setFormatter(new TSXMLFormatter());
      tsLogger.addHandler(tsFileHandler);

      // create some log messages
      tsLogger.log(Level.INFO, "message-1", "jacc_ctx");
      tsLogger.log(Level.INFO, "message-2", "jacc_ctx");
      tsLogger.log(Level.INFO, "PolicyConfigurationFactory instantiated",
          "jacc_ctx");
      tsLogger.log(Level.INFO, "message-4", "new_ctx");
      tsLogger.log(Level.INFO, "message-5", "new_ctx");

      tsFileHandler.close();

      url = tsURL.getURL("http", webServerHost, webServerPort, SERVLET);

      props = new Properties();
      props.put("LogFilePath", logFileLocation + "/jacc_log.txt");
      props.put("LogQueryString", "findLogsByContextId");
      props.put("LogQueryParams", "jacc_ctx");

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("Content-Type",
          "java-internal/" + props.getClass().getName());

      ObjectOutputStream oos = new ObjectOutputStream(conn.getOutputStream());
      oos.writeObject(props);
      oos.flush();
      oos.close();

      InputStream is = conn.getInputStream();
      ObjectInputStream ois = new ObjectInputStream(is);

      String header = (String) ois.readObject();

      // read the header information,
      //
      // if the header is equal to "RecordCollection header"
      // then the remaining data is a recordCollection object
      // else report log file processing failure.
      //
      if (header.equals("RecordCollection header")) {
        recordCollection = (Collection) ois.readObject();

        printCollection(recordCollection);

      } else {
        TestUtil.logErr("Log File processing failed");
        TestUtil.logErr("Log file does not exists in the server"
            + " or you don't have read permission for log file");
        TestUtil.logErr("Check permissions for log file ");
        TestUtil.logErr("See User guide for Configuring log file permissions");
      }

      ois.close();

    } catch (Exception e) {
      TestUtil.logErr(e.getMessage());
      TestUtil.printStackTrace(e);
    }

  }

  public void printCollection(Collection recordCollection) {
    RecordEntry recordEntry = null;
    Iterator iterator = recordCollection.iterator();

    while (iterator.hasNext()) {
      recordEntry = (RecordEntry) iterator.next();
      printRecordEntry(recordEntry);
    }
  }

  public void printRecordEntry(RecordEntry rec) {
    TestUtil.logMsg("*******Log Content*******");
    TestUtil.logMsg("seqence no  =" + rec.getSequenceNumber());
    TestUtil.logMsg("ContextId   =" + rec.getContextId());
    TestUtil.logMsg("Message     =" + rec.getMessage());
    if (rec.getClassName() != null)
      TestUtil.logMsg("Class name  =" + rec.getClassName());
    if (rec.getMethodName() != null)
      TestUtil.logMsg("Method name =" + rec.getMethodName());
    if (rec.getLevel() != null)
      TestUtil.logMsg("Level        =" + rec.getLevel());
    if (rec.getThrown() != null)
      TestUtil.logMsg("Thrown       =" + rec.getThrown());
    TestUtil.logMsg("");

  }

}
