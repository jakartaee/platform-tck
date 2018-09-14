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

package com.sun.ts.tests.jpa.common.pluggability.util;

import com.sun.ts.lib.util.TestUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

/**
 *
 * @author Raja Perumal
 */

/**
 * LogFileProcessor does the following operations
 * <p/>
 * 1) Fetches log records from JPALog.txt
 * <p/>
 * 2) Checks for the existance of search string in the log for example to verify
 * whether server log contains a string "Java EE rocks" use the following code
 * <p/>
 * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
 * contains = logProcessor.verifyLogContains("Java EE rocks");
 * <p/>
 * where "properties" contains the following key value pair 1) log.file.location
 * <p/>
 * 3) Prints the collection of log records.
 */
public class LogFileProcessor {

  private String logFileLocation = null;

  private String logFileName = "JPALog.xml";

  private Collection recordCollection = null;

  private Collection appIdRecordCollection = null;

  private Collection appSpecificRecordCollection = null;

  public LogFileProcessor(Properties props) {
    setup(props);
  }

  /**
   * setup method
   */
  public void setup(Properties p) {
    logFileLocation = p.getProperty("log.file.location");

    if (logFileLocation == null) {
      TestUtil.logErr("LogFileProcessor setup failed ");
      TestUtil.logErr(
          "Please verify that the property log.file.location exists in ts.jte");
    } else {
      TestUtil.logMsg("log.file.location = " + logFileLocation);
    }
    logFileLocation = getLogFileName(logFileLocation);
    TestUtil.logTrace("LogFileProcessor setup finished");
  }

  /**
   *
   */

  public long getCurrentSequenceNumber() {
    LogRecordEntry recordEntry = null;
    long seqNum = 0L;
    TestUtil.logTrace("Searching for current Sequence Number");
    if (recordCollection != null) {
      TestUtil.logTrace(
          "Record collection has:  " + recordCollection.size() + " records.");
      for (Iterator iterator = recordCollection.iterator(); iterator
          .hasNext();) {
        // loop thru all message tag/entries in the log file searching for last
        // sequence number
        recordEntry = (LogRecordEntry) iterator.next();
        seqNum = recordEntry.getSequenceNumber();
        // TestUtil.logTrace("seq:" + seqNum);
      }
    } else {
      TestUtil.logErr("Record collection empty : No log records found");
    }
    // System.out.println("final number:" + seqNum);
    return seqNum;

  }

  /**
   * purgeLogs clears the log.
   */
  public void purgeLog() {
    File logfile = null;
    try {
      if (logFileLocation != null)
        logfile = new File(logFileLocation);

      if (logFileLocation == null || !logfile.exists()) {
        TestUtil.logErr("Log File : " + logFileLocation + " does not exists");
      } else {
        TestUtil.logTrace("Purging log file : " + logFileLocation);

        BufferedWriter writer = new BufferedWriter(
            new FileWriter(logFileLocation));
        writer.newLine();
        writer.close();
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred while purging log:" + e);
    }

  }

  /**
   * FetchLogs pull logs from the server.
   */
  public boolean fetchLog() {
    boolean logRetrieved = false;
    File logfile = null;
    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
          .newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory
          .newDocumentBuilder();

      if (logFileLocation != null)
        logfile = new File(logFileLocation);

      if (logFileLocation == null || !logfile.exists()) {
        TestUtil.logErr("Log File : " + logFileLocation + " does not exists");
        TestUtil.logErr("Check permissions for log file ");
        TestUtil.logErr("See User guide for Configuring log file permissions");
      } else {
        // LogRecords will be added to JPALog.txt as long as the server is
        // up and running. Since JPALog.txt is continuously updated with
        // more record there will not be any end tag </log> at the end of the
        // log file.
        //
        // This will cause the SAXParser to throw fatal error message
        // "XML Document structures must start and end with the
        // same entity"
        //
        // In order to avoid this error message the FileInputStream
        // should have the end tag </log>, this can be achieved by
        // creating a SequenceInputStream which includes a
        // FileInputStream and a ByteArrayInputStream, where the
        // ByteArrayInputStream contains the bytes for </log>
        //
        TestUtil.logTrace("Log File : " + logfile.getAbsolutePath());

        String endLogTag = "</log>";
        ByteArrayInputStream bais = new ByteArrayInputStream(
            endLogTag.getBytes());
        SequenceInputStream sis = new SequenceInputStream(
            new FileInputStream(logFileLocation), bais);

        Document document = documentBuilder.parse(sis);
        Element rootElement = document.getDocumentElement();
        NodeList nodes = rootElement.getChildNodes();

        recordCollection = pullAllLogRecords(nodes);
        // printCollection(recordCollection);
        logRetrieved = true;
        sis.close();
        bais.close();

      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred while fetching log:" + e);
    }
    return logRetrieved;
  }

  /**
   * Fetches all logs from JPALog.txt
   */
  public static Collection pullAllLogRecords(NodeList nodes) throws Exception {
    Collection<LogRecordEntry> recordCollection = new Vector<LogRecordEntry>();
    Node recordNode;

    for (int i = 0; i < nodes.getLength(); i++) {
      // Take the first record
      recordNode = nodes.item(i);

      if (recordNode.getNodeName().equals("record")) {
        LogRecordEntry recordEntry = new LogRecordEntry(recordNode);
        recordCollection.add(recordEntry);

      }
    }
    return recordCollection;
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
   * <p/>
   * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
   * contains = logProcessor.verifyLogContains("Java EE rocks");
   * <p/>
   * where "properties" contains the key value pair for 1) log.file.location
   */
  public boolean verifyLogContains(String args[]) {
    return verifyLogContains(args, 0);
  }

  /**
   * Checks for the existance of search string in the log. For example to verify
   * whether server log contains a string "Java EE rocks" use the following code
   * <p/>
   * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
   * contains = logProcessor.verifyLogContains("Java EE rocks");
   * <p/>
   * where "properties" contains the key value pair for 1) log.file.location
   * <p/>
   * This method takes a sequence number which is used to find the place in the
   * log to start search from, 0 being the top of the file and another value to
   * start from that location.
   */
  public boolean verifyLogContains(String args[], long sequenceNum) {
    LogRecordEntry recordEntry = null;
    TestUtil.logTrace("Searching log records for the following:");
    for (String s : args) {
      TestUtil.logTrace("item:" + s);
    }
    if (recordCollection == null) {
      TestUtil.logTrace("Record collection empty : No log records found");
      return false;
    } else {
      TestUtil.logTrace(
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

    for (Iterator iterator = recordCollection.iterator(); iterator.hasNext();) {

      // loop thru all message tag/entries in the log file
      recordEntry = (LogRecordEntry) iterator.next();
      long seqNum = recordEntry.getSequenceNumber();
      if (seqNum >= sequenceNum) {
        String message = recordEntry.getMessage();
        // TestUtil.logTrace("record:"+message);

        // loop through all arguments to search for a match
        for (int i = 0; i < numberOfArgs; i++) {

          // Search only unique record matches ignore repeat occurances
          if (argsMatchIndex[i] != true) {
            // see if one of the search argument matches with
            // the logfile message entry and if so return true
            if ((message != null) && message.equals(args[i])) {
              TestUtil.logTrace("Matching Record :");
              TestUtil.logTrace(recordEntry.getSequenceNumber() + ":"
                  + recordEntry.getMessage());

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
      } else {
        // TestUtil.logTrace("bypassing record sequence number too low,
        // min:"+sequenceNum+", actual:"+seqNum);
      }
    }

    // Print unmatched Strings(i.e no matches were found for these strings)
    TestUtil.logTrace(
        "No Matching log Record(s) found for the following String(s) :");
    for (int i = 0; i < numberOfArgs; i++) {
      if (argsMatchIndex[i] == false) {
        TestUtil.logTrace(args[i]);
      }
    }

    return false;
  }

  /**
   * Checks for the existance of one of the search string(from a given String
   * array.
   * <p/>
   * For example to verify whether server log contains one of the following
   * String String[] arr ={"aaa", "bbb", "ccc"};
   * <p/>
   * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
   * contains = logProcessor.verifyLogContainsOneOf(arr);
   * <p/>
   * This method will return true if the log file contains one of the specified
   * String (say "aaa" )
   * <p/>
   * where "properties" contains the key value pair for 1) log.file.location
   */
  public boolean verifyLogContainsOneOf(String args[]) {
    LogRecordEntry recordEntry = null;
    boolean result = false;

    TestUtil.logTrace("Searching log records for one of the following:");
    for (String s : args) {
      TestUtil.logTrace("item:" + s);
    }
    if (recordCollection == null) {
      TestUtil.logTrace("Record collection empty : No log records found");
      return false;
    } else {
      TestUtil.logTrace(
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
          TestUtil.logTrace("Matching Record :");
          TestUtil.logTrace(
              recordEntry.getSequenceNumber() + ":" + recordEntry.getMessage());
          result = true;

          // If a match is found no need to search further
          break searchLabel;
        }
      }

    }

    if (!result) {
      // Print unmatched Strings(i.e no matches were found for these strings)
      TestUtil.logTrace(
          "No Matching log Record(s) found for the following String(s) :");
      for (int i = 0; i < numberOfArgs; i++) {
        TestUtil.logTrace(args[i]);
      }
    }

    return result;
  }

  /**
   * This method looks for the presence of the given substring (from the array
   * of strings "args") in the serverlog, which starts with the given
   * "srchStrPrefix" search-string-prefix.
   * <p/>
   * <p/>
   * For example to verify whether server log contains one of the following
   * Strings in a server log with appContextId as the message prefix we can
   * issue the following command
   * <p/>
   * String[] arr ={"aaa", "bbb", "ccc"}; String srchStrPrefix ="appContextId";
   * <p/>
   * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
   * contains = logProcessor.verifyLogContainsOneOf(arr);
   * <p/>
   * "appContextId= xxxx aaa yyyyyyyyyyyyyyyyy" "appContextId= yyyy bbb
   * xxxxxxxxxxxxxxxxx"
   * <p/>
   * This method will return true if the log file contains one of the specified
   * String (say "aaa" ) in the message log with "appContextId" as its message
   * prefix.
   * <p/>
   * where "properties" contains the key value pair for 1) log.file.location
   */
  public boolean verifyLogContainsOneOfSubString(String args[],
      String srchStrPrefix) {
    LogRecordEntry recordEntry = null;
    boolean result = false;

    TestUtil
        .logTrace("Searching log records for the presence of one of the String"
            + " from a given string array");
    if (recordCollection == null) {
      TestUtil.logTrace("Record collection empty : No log records found");
      return false;
    } else {
      TestUtil.logTrace(
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
          TestUtil.logTrace("Matching Record :");
          TestUtil.logTrace(recordEntry.getMessage());
          result = true;

          // If a match is found no need to search further
          break searchLabel;
        }
      }

    }

    if (!result) {
      // Print unmatched Strings(i.e no matches were found for these strings)
      TestUtil.logTrace(
          "No Matching log Record(s) found for the following String(s) :");
      for (int i = 0; i < numberOfArgs; i++) {
        TestUtil.logTrace(args[i]);
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
    TestUtil.logTrace("*******Log Content*******");

    TestUtil.logTrace("Milli Seconds  =" + rec.getMilliSeconds());
    TestUtil.logTrace("Seqence no  =" + rec.getSequenceNumber());
    TestUtil.logTrace("Message     =" + rec.getMessage());
    if (rec.getClassName() != null)
      TestUtil.logTrace("Class name  =" + rec.getClassName());
    if (rec.getMethodName() != null)
      TestUtil.logTrace("Method name =" + rec.getMethodName());
    if (rec.getLevel() != null)
      TestUtil.logTrace("Level        =" + rec.getLevel());
    if (rec.getThrown() != null)
      TestUtil.logTrace("Thrown       =" + rec.getThrown());
    TestUtil.logTrace("");
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

    // TestUtil.logTrace("Input String ="+str);
    // TestUtil.logTrace("array[0] ="+array[0]);
    // TestUtil.logTrace("array[1] ="+array[1]);
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
    Collection<LogRecordEntry> recordCollection = new Vector<LogRecordEntry>();
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

  public String getLogFileName(String logFileLocation) {
    String logName = logFileLocation + File.separator + logFileName;
    String lastFileName = logFileName;
    int lastFileIndex = 0;
    File dir = new File(logFileLocation);
    if (dir.exists()) {
      TestUtil.logTrace("log file location exists:" + logFileLocation);
      String[] chld = dir.list();
      if (chld == null) {
        TestUtil.logErr(
            "Appserver log directory does not exist or is not a directory, using default log file location.");
      } else {
        boolean found = false;
        for (int i = 0; i < chld.length; i++) {
          String fName = chld[i];
          // look for file that doesn't contain ".lck"
          if ((fName.indexOf(logFileName) >= 0)
              && (fName.indexOf(".lck") < 0)) {
            String fileNameArr[] = fName.split("\\.");
            if (fileNameArr.length == 3) {
              int lastFileIndexTemp = Integer.parseInt(fileNameArr[2]);
              if (lastFileIndex < lastFileIndexTemp) {
                lastFileIndex = lastFileIndexTemp;
                lastFileName = fName;
              }
            }
            logName = logFileLocation + File.separator + lastFileName;
            TestUtil.logMsg("Found log file:" + logName);
            found = true;
            // break;
          } else {
            TestUtil.logTrace("Ignoring file:" + fName);
          }
        }
        if (!found) {
          TestUtil
              .logMsg("Log file not found, using default location:" + logName);
        }
      }
    } else {
      TestUtil
          .logErr("Log file location DOES NOT exist, using default location:"
              + logName);
    }
    return logName;

  }

}
