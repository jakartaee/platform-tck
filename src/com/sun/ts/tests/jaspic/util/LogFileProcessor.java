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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.SequenceInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
 * LogFileProcessor does the following operations
 *
 * 1) Fetches log records from TSSVLog.txt
 *
 * 2) Checks for the existance of search string in the log for example to verify
 * whether server log contains a string "Java EE rocks" use the following code
 *
 * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
 * contains = logProcessor.verifyLogContains("Java EE rocks");
 *
 * where "properties" contains the following key value pair 1) log.file.location
 *
 * 3) Prints the collection of log records.
 *
 */
public class LogFileProcessor {

  private Properties props = null;

  private String logFileLocation = null;

  private Collection recordCollection = null;

  private Collection appIdRecordCollection = null;

  private Collection appSpecificRecordCollection = null;

  public LogFileProcessor() {
  }

  public LogFileProcessor(Properties props) {
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

      TestUtil.logMsg("log.file.location = " + logFileLocation);

      if (!pass) {
        TestUtil.logErr("Setup Failed ");
        TestUtil.logErr("Please verify the following in ts.jte");
        TestUtil.logErr("log.file.location");
      }
      TestUtil.logMsg("Setup ok");

    } catch (Exception e) {
      TestUtil.logErr("Setup Failed ");
      TestUtil.logErr("Please verify the following in ts.jte");
      TestUtil.logErr("log.file.location");
    }

  }

  /**
   * FetchLogs pull logs from the server.
   *
   */
  public void fetchLogs(String accessMethod) {

    File logfile = null;
    URL url = null;

    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
          .newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory
          .newDocumentBuilder();

      String strFilePathAndName = "";
      if (logFileLocation.indexOf("TSSVLog.txt") <= 0) {
        strFilePathAndName = logFileLocation + "/TSSVLog.txt";
      }
      System.out.println("Log File = " + strFilePathAndName);

      if (strFilePathAndName != null)
        logfile = new File(strFilePathAndName);

      if (!logfile.exists()) {
        String header = "Log file does not exists";
        // ostream.writeObject(header);

        System.out
            .println("Log File : " + logfile.getName() + " does not exists");
        System.out.println("Check permissions for log file ");
        System.out
            .println("See User guide for Configuring log file permissions");
      } else {
        // LogRecords will be added to TSSVLog.txt as long as the server is
        // up and running. Since TSSLog.txt is continuously updated with
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
        String endLogTag = "</log>";
        ByteArrayInputStream bais = new ByteArrayInputStream(
            endLogTag.getBytes());
        SequenceInputStream sis = new SequenceInputStream(
            new FileInputStream(strFilePathAndName), bais);

        Document document = documentBuilder.parse(sis);
        Element rootElement = document.getDocumentElement();
        NodeList nodes = rootElement.getChildNodes();

        String queryString = "pullAllRecords";
        String queryParams = "fullLog";

        if (queryString.equals("pullAllRecords")) {
          recordCollection = pullAllLogRecords(queryParams, nodes);
          // printCollection(recordCollection);
        }

      }

    } catch (Exception e) {
      TestUtil.logErr(e.getMessage());
      TestUtil.printStackTrace(e);
      e.printStackTrace();
    }

  }

  /**
   * Fetches all JSR196 SPI logs from TSSVLog.txt
   */
  public static Collection pullAllLogRecords(String queryParams, NodeList nodes)
      throws Exception {
    Node childNode;
    Node recordNode;
    NodeList recordNodeChildren;
    Collection recordCollection = new Vector();

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
   *
   * LogProcessor logProcessor = new LogProcessor(properties); boolean contains
   * = logProcessor.verifyLogContains("Java EE rocks");
   *
   * where "properties" contains the key value pair for 1) log.file.location
   */
  public boolean verifyLogContains(String args[]) {
    return verifyLogContains(args, false);
  }

  /**
   * Checks for the existance of search string in the log. For example to verify
   * whether server log contains a string "Java EE rocks" use the following code
   *
   * LogProcessor logProcessor = new LogProcessor(properties); boolean contains
   * = logProcessor.verifyLogContains("Java EE rocks");
   *
   * where "properties" contains the key value pair for 1) log.file.location
   */
  public boolean verifyLogContains(String args[], boolean validateOrder) {
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
    if (!validateOrder) {
      // we dont care about order, just that all args were found
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

      // if here, our non-order-specific search did not find all matches...
      // Print unmatched Strings(i.e no matches were found for these strings)
      TestUtil.logMsg(
          "No Matching log Record(s) found for the following String(s) :");
      for (int i = 0; i < numberOfArgs; i++) {
        if (argsMatchIndex[i] == false) {
          TestUtil.logMsg(args[i]);
        }
      }

    } else {
      // we care about order, so find args and make sure they are in
      // same order as the arg[] array
      int recordCnt = 0;
      int argCount = 0;

      // first find how last occurance of our FIRST ITEM. Why? because
      // we want to be sure that if we are searching for "a", "b", "c" within
      // the TSSVLog.txt, we better find the very last and most recent entry
      // for "a". For example, if logfile contain "a", "b", "c", "a", "c" due
      // to multiple runs, we will erroneously pass a search for "a", "b", "c"
      // UNLESS we are careful to find the last "a", then do our check.

      int occurances = 0;
      while (iterator.hasNext()) {
        recordCnt++;
        recordEntry = (LogRecordEntry) iterator.next();
        String message = recordEntry.getMessage();
        if ((message != null) && message.equals(args[argCount])) {
          occurances++;
          TestUtil
              .logMsg("verifyLogContains() found " + occurances + "occurances");
        }
      }

      // reset the iterator
      iterator = recordCollection.iterator();
      while (iterator.hasNext()) {
        recordCnt++;
        recordEntry = (LogRecordEntry) iterator.next();
        String message = recordEntry.getMessage();

        if ((message != null) && (message.equals(args[0]))
            && (occurances > 1)) {
          // found a match but not the LAST occurance of it in TSSVLog.txt
          // so keep looking
          occurances--;
          continue;
        }

        if ((message != null) && message.equals(args[argCount])) {
          // we found last occurance of first item in args[]
          TestUtil.logMsg(
              "Found: " + args[argCount] + " in record entry: " + recordCnt);
          argCount++;
          if (argCount >= args.length) {
            // we are done, found all our matches in order
            return true;
          }
        }
      }

      // if here, our order-specific-search did not succeed.
      TestUtil.logMsg(
          "Failed to find order-specific-records search in verifyLogContains()");
      TestUtil.logMsg("   Could not find: " + args[argCount]);
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
