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

package com.sun.ts.tests.jaspic.util;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.porting.TSURL;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.util.Properties;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.net.URL;

/**
 *
 * @author Raja Perumal
 */

/**
 * LogProcessor does the following operations
 *
 * 1) Fetches the server log using "FetchLogs" servlet.
 *
 * 2) Checks for the existance of search string in the log for example to verify
 * whether server log contains a string "Java EE rocks" use the following code
 *
 * LogProcessor logProcessor = new LogProcessor(properties); boolean contains =
 * logProcessor.verifyLogContains("Java EE rocks");
 *
 * where "properties" contains the following key value pair 1) log.file.location
 * 2) webServerHost 3) webServerPort
 *
 * 3) Prints the collection of log records.
 *
 */
public class LogProcessor {

  private Properties props = null;

  private TSURL tsURL = new TSURL();

  private String webServerHost = "unknown";

  private int webServerPort = 8000;

  private String SERVLET = "/jaspic_util_web/FetchLogs";

  private String logFileLocation = null;

  private Collection recordCollection = null;

  private Collection appIdRecordCollection = null;

  private Collection appSpecificRecordCollection = null;

  public LogProcessor() {
  }

  public LogProcessor(Properties props) {
    setup(props);
  }

  /**
   * setup method
   */
  public void setup(Properties p) {
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
        pass = false;
      }

      TestUtil.logMsg("webServerHost = " + webServerHost);
      TestUtil.logMsg("webServerPort = " + webServerPort);
      TestUtil.logMsg("log.file.location = " + logFileLocation);

      if (!pass) {
        TestUtil.logErr("Setup Failed ");
        TestUtil.logErr("Please verify the following in build.properties");
        TestUtil.logErr("webServerHost, webServerPort");
        TestUtil.logErr("Please verify the following in ts.jte");
        TestUtil.logErr("log.file.location");
      }
      TestUtil.logMsg("Setup ok");

    } catch (Exception e) {
      TestUtil.logErr("Setup Failed ");
      TestUtil.logErr("Please verify the following in build.properties");
      TestUtil.logErr("webServerHost, webServerPort");
      TestUtil.logErr("Please verify the following in ts.jte");
      TestUtil.logErr("log.file.location");
    }

  }

  /**
   * FetchLogs does the following operations
   *
   * 1) Pulls all the logs from the server
   *
   * 2) It retrieves all the records specific to the current application.
   *
   *
   */
  public void fetchLogs(String accessMethod) {

    long seqNum = 0L;
    String contextId = null;
    Collection recordCollection = null;
    Properties props = null;
    URL url = null;
    String qString = null;
    String qParams = null;

    try {

      url = tsURL.getURL("http", webServerHost, webServerPort, SERVLET);

      props = new Properties();
      props.put("LogFilePath", logFileLocation + "/TSSVLog.txt");

      // obtain the QueryString and Query params from the accessMethod string
      qString = extractQueryToken("LogQueryString", accessMethod);
      qParams = extractQueryToken("LogQueryParams", accessMethod);
      props.put("LogQueryString", qString);
      props.put("LogQueryParams", qParams);

      String argString = TestUtil.toEncodedString(props);

      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setRequestProperty("CONTENT_LENGTH", "" + argString.length());

      // get Output stream
      OutputStream os = conn.getOutputStream();
      OutputStreamWriter osw = new OutputStreamWriter(os);

      TestUtil.logMsg("Posting Data to :" + url.toString());

      // write the data to the output stream
      osw.write(argString);
      osw.flush();
      osw.close();

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
        TestUtil.logMsg("Reading records collection from the server ...");

        recordCollection = (Collection) ois.readObject();
        if ((recordCollection == null) || (recordCollection.isEmpty())) {
          TestUtil.logErr("Logfile is empty and contains No records.");
        }
        setRecordCollection(recordCollection);
        // printCollection(recordCollection);

        if (qString.equals("getAppSpecificRecordCollection")) {
          appSpecificRecordCollection = (Collection) ois.readObject();
          setAppSpecificRecordCollection(appSpecificRecordCollection);
          // printCollection(appSpecificRecordCollection);
        }
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
      e.printStackTrace();
    }
  }

  public void setAppIdRecordCollection(Collection recordCollection) {
    this.appIdRecordCollection = recordCollection;
  }

  public Collection getAppIdRecordCollection() {
    return this.appIdRecordCollection;
  }

  public void setRecordCollection(Collection recordCollection) {
    this.recordCollection = recordCollection;
  }

  public Collection getRecordCollection() {
    return this.recordCollection;
  }

  public void setAppSpecificRecordCollection(Collection recordCollection) {
    this.appSpecificRecordCollection = recordCollection;
  }

  public Collection getAppSpecificRecordCollection() {
    return this.appSpecificRecordCollection;
  }

  /**
   * Checks for the existance of search string in the log. For example to verify
   * whether server log contains a string "Java EE rocks" use the following code
   *
   * LogProcessor logProcessor = new LogProcessor(properties); boolean contains
   * = logProcessor.verifyLogContains("Java EE rocks");
   *
   * where "properties" contains the key value pair for 1) log.file.location 2)
   * webServerHost 3) webServerPort
   */
  public boolean verifyLogContains(String args[]) {
    LogRecordEntry recordEntry = null;
    TestUtil.logMsg("Searching log records for record :" + args[0]);
    if (recordCollection == null) {
      TestUtil.logMsg("Record collection empty : No log records found");
      return false;
    } else {
      TestUtil.logMsg(
          "Record collection has:  " + recordCollection.size() + " records.");
    }

    int numberOfArgs = args.length;
    int numberOfMatches = 0;

    boolean argsMatchIndex[] = new boolean[args.length];
    for (int i = 0; i < args.length; i++) {
      // initialize all argsMatchIndex to "false" (i.e no match)
      argsMatchIndex[i] = false;

      // From the given string array(args) if there is a record match
      // for the search string, then the corresponding argsMatchIndex[i]
      // will be set to true(to indicate a match)
      // i.e argsMatchIndex[i] = true;
      //
      // For example if the string array contains
      // String args[]={"JK", "EMERSON", "J.B.Shaw};
      //
      // And if the string "JK" and "J.B.Shaw" are found in the records
      // then the argsMatchIndex will be set as shown below
      // argsMatchIndex[] ={true, false, true};
      //
    }

    Iterator iterator = recordCollection.iterator();
    while (iterator.hasNext()) {
      // loop thru all message tag/entries in the log file
      recordEntry = (LogRecordEntry) iterator.next();
      String message = recordEntry.getMessage();
      // loop through all arguments to search for a match
      for (int i = 0; i < numberOfArgs; i++) {

        // Search only unique record matches ignore repeat occurances
        if (argsMatchIndex[i] != true) {
          // see if one of the search argument matches with
          // the logfile message entry and if so return true
          if ((message != null) && message.equals(args[i])) {
            TestUtil.logMsg("Matching Record :");
            TestUtil.logMsg(recordEntry.getMessage());

            // Increment match count
            numberOfMatches++;

            // Mark the matches in argsMatchIndex
            argsMatchIndex[i] = true;

            continue;
          }
        }

      }

      // Return true if, we found matches for all strings
      // in the given string array
      if (numberOfMatches == numberOfArgs)
        return true;
    }

    // Print unmatched Strings(i.e no matches were found for these strings)
    TestUtil.logMsg(
        "No Matching log Record(s) found for the following String(s) :");
    for (int i = 0; i < numberOfArgs; i++) {
      if (argsMatchIndex[i] == false) {
        TestUtil.logMsg(args[i]);
      }
    }

    return false;
  }

  /**
   * Checks for the existance of one of the search string(from a given String
   * array.
   *
   * For example to verify whether server log contains one of the following
   * String String[] arr ={"aaa", "bbb", "ccc"};
   *
   * LogProcessor logProcessor = new LogProcessor(properties); boolean contains
   * = logProcessor.verifyLogContainsOneOf(arr);
   *
   * This method will return true if the log file contains one of the specified
   * String (say "aaa" )
   *
   * where "properties" contains the key value pair for 1) log.file.location
   */
  public boolean verifyLogContainsOneOf(String args[]) {
    LogRecordEntry recordEntry = null;
    boolean result = false;

    TestUtil
        .logMsg("Searching log records for the presence of one of the String"
            + " from a given string array");
    if (recordCollection == null) {
      TestUtil.logMsg("Record collection empty : No log records found");
      return false;
    } else {
      TestUtil.logMsg(
          "Record collection has:  " + recordCollection.size() + " records.");
    }

    int numberOfArgs = args.length;

    Iterator iterator = recordCollection.iterator();
    searchLabel: while (iterator.hasNext()) {
      // loop thru all message tag/entries in the log file
      recordEntry = (LogRecordEntry) iterator.next();
      String message = recordEntry.getMessage();
      // loop through all arguments to search for a match
      for (int i = 0; i < numberOfArgs; i++) {

        // see if one of the search argument matches with
        // the logfile message entry and if so return true
        if ((message != null) && message.equals(args[i])) {
          TestUtil.logMsg("Matching Record :");
          TestUtil.logMsg(recordEntry.getMessage());
          result = true;

          // If a match is found no need to search further
          break searchLabel;
        }
      }

    }

    if (!result) {
      // Print unmatched Strings(i.e no matches were found for these strings)
      TestUtil.logMsg(
          "No Matching log Record(s) found for the following String(s) :");
      for (int i = 0; i < numberOfArgs; i++) {
        TestUtil.logMsg(args[i]);
      }
    }

    return result;
  }

  /**
   * This method looks for the presence of the given substring (from the array
   * of strings "args") in the serverlog, which starts with the given
   * "srchStrPrefix" search-string-prefix.
   *
   *
   * For example to verify whether server log contains one of the following
   * Strings in a server log with appContextId as the message prefix we can
   * issue the following command
   *
   * String[] arr ={"aaa", "bbb", "ccc"}; String srchStrPrefix ="appContextId";
   *
   * LogProcessor logProcessor = new LogProcessor(properties); boolean contains
   * = logProcessor.verifyLogContainsOneOf(arr);
   *
   * "appContextId= xxxx aaa yyyyyyyyyyyyyyyyy" "appContextId= yyyy bbb
   * xxxxxxxxxxxxxxxxx"
   *
   * This method will return true if the log file contains one of the specified
   * String (say "aaa" ) in the message log with "appContextId" as its message
   * prefix.
   *
   * where "properties" contains the key value pair for 1) log.file.location
   */
  public boolean verifyLogContainsOneOfSubString(String args[],
      String srchStrPrefix) {
    LogRecordEntry recordEntry = null;
    boolean result = false;

    TestUtil
        .logMsg("Searching log records for the presence of one of the String"
            + " from a given string array");
    if (recordCollection == null) {
      TestUtil.logMsg("Record collection empty : No log records found");
      return false;
    } else {
      TestUtil.logMsg(
          "Record collection has:  " + recordCollection.size() + " records.");
    }

    int numberOfArgs = args.length;

    Iterator iterator = recordCollection.iterator();
    searchLabel: while (iterator.hasNext()) {
      // loop thru all message tag/entries in the log file
      recordEntry = (LogRecordEntry) iterator.next();
      String message = recordEntry.getMessage();
      // loop through all arguments to search for a match
      for (int i = 0; i < numberOfArgs; i++) {

        // see if one of the search argument matches with
        // the logfile message entry and if so return true
        if ((message != null) && (message.startsWith(srchStrPrefix, 0))
            && (message.indexOf(args[i]) > 0)) {
          TestUtil.logMsg("Matching Record :");
          TestUtil.logMsg(recordEntry.getMessage());
          result = true;

          // If a match is found no need to search further
          break searchLabel;
        }
      }

    }

    if (!result) {
      // Print unmatched Strings(i.e no matches were found for these strings)
      TestUtil.logMsg(
          "No Matching log Record(s) found for the following String(s) :");
      for (int i = 0; i < numberOfArgs; i++) {
        TestUtil.logMsg(args[i]);
      }
    }

    return result;
  }

  public void printCollection(Collection recordCollection) {
    LogRecordEntry recordEntry = null;
    Iterator iterator = recordCollection.iterator();

    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      printRecordEntry(recordEntry);
    }
  }

  public void printRecordEntry(LogRecordEntry rec) {
    TestUtil.logMsg("*******Log Content*******");

    TestUtil.logMsg("Milli Seconds  =" + rec.getMilliSeconds());
    TestUtil.logMsg("Seqence no  =" + rec.getSequenceNumber());
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

  public String extractQueryToken(String str, String ContextId) {
    StringTokenizer strtok;
    String DELIMETER = "|";
    String qstring = null;
    String qparams = null;

    strtok = new StringTokenizer(ContextId, DELIMETER);
    if (ContextId.indexOf(DELIMETER) > 0) {
      qstring = strtok.nextToken();
      if (strtok.hasMoreTokens())
        qparams = strtok.nextToken();
    }

    // return query string or query params based on the content
    // of the string str
    if (str.equals("LogQueryString"))
      return qstring;
    else
      return qparams;
  }

  // This method tokenize the given string and
  // return first token and the remaining
  // string a string array based on the given delimeter
  public static String[] getTokens(String str, String delimeter) {
    String[] array = new String[2];
    StringTokenizer strtoken;

    // Get first token and the remaining string
    strtoken = new StringTokenizer(str, delimeter);
    if (str.indexOf(delimeter) > 0) {
      array[0] = strtoken.nextToken();
      array[1] = str.substring(array[0].length() + 3, str.length());
    }

    // TestUtil.logMsg("Input String ="+str);
    // TestUtil.logMsg("array[0] ="+array[0]);
    // TestUtil.logMsg("array[1] ="+array[1]);
    return array;
  }

  //
  // Locates the logs based on the given prefix string
  //
  // For example to locate all commit records i.e records such as
  //
  // commit :: MyApp1058312446320 , recordTimeStamp=1058312446598
  //
  // Use the following method to pull all the commit records
  //
  // fingLogsByPrefix("commit", nodes);
  public Collection findLogsByPrefix(String queryParams, NodeList nodes)
      throws Exception {
    Collection recordCollection = new Vector();
    String nodeName;
    String nodeValue;
    Node childNode;
    Node recordNode;
    NodeList recordNodeChildren;

    for (int i = 0; i < nodes.getLength(); i++) {
      // Take the first record
      recordNode = nodes.item(i);

      // get all the child nodes for the first record
      recordNodeChildren = recordNode.getChildNodes();

      for (int j = 0; j < recordNodeChildren.getLength(); j++) {
        childNode = recordNodeChildren.item(j);
        nodeName = childNode.getNodeName();
        if (nodeName.equals("message")) {
          nodeValue = getText(childNode);
          if (nodeValue.startsWith(queryParams)) {
            // create a new record entry and
            // add it to the collection
            LogRecordEntry recordEntry = new LogRecordEntry(recordNode);

            recordCollection.add(recordEntry);
          }
        }
      }
    }
    return recordCollection;
  }

  public String getText(Node textNode) {
    String result = "";
    NodeList nodes = textNode.getChildNodes();

    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);

      if (node.getNodeType() == Node.TEXT_NODE) {
        result = node.getNodeValue();
        break;
      }
    }
    return result;
  }

}
