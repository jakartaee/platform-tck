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

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sun.ts.tests.jaspic.util.LogRecordEntry;
import com.sun.ts.lib.util.TestUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.SequenceInputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Enumeration;

/**
 *
 * @author Raja Perumal
 */

/**
 * FetchLogs servlet is used for collecting logs from the server.
 *
 * FetchLogs servlet gets the logfile location from its input parameters and
 * locates the log file in the server, parses it and extracts a subset of logs
 * based on various parameters such as "classname" or "sequenceNumber" and
 * builds Collection of logs of type (LogRecordEntry) and returns the Collection
 *
 */
public class FetchLogs extends HttpServlet {

  Collection recordCollection = new Vector();

  Collection appIdRecordCollection = new Vector();

  Collection linkRecordCollection = new Vector();

  Collection appSpecificRecordCollection = new Vector();

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    File logfile = null;

    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
          .newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory
          .newDocumentBuilder();

      OutputStream outStream = response.getOutputStream();
      ObjectOutputStream ostream = new ObjectOutputStream(outStream);
      if (ostream == null) {
        throw new Exception(
            "ServletException in doGet - could not get ObjectOutputStream");
      }

      String logFileLocation = request.getParameter("LogFilePath");

      if (logFileLocation != null)
        logfile = new File(logFileLocation);

      if ((logfile != null) && !logfile.exists()) {
        String header = "Log file does not exists";
        ostream.writeObject(header);

        TestUtil.logErr("Log File : " + logFileLocation + " does not exists");
        TestUtil.logErr("Check permissions for log file ");
        TestUtil.logErr("See User guide for Configuring log file permissions");
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
            new FileInputStream(logFileLocation), bais);

        Document document = documentBuilder.parse(sis);
        Element rootElement = document.getDocumentElement();
        NodeList nodes = rootElement.getChildNodes();

        String queryString = request.getParameter("LogQueryString");
        String queryParams = request.getParameter("LogQueryParams");
        // TestUtil.logMsg("LogFilePath =" + logFileLocation);
        // TestUtil.logMsg("LogQueryString =" + queryString);
        // TestUtil.logMsg("LogQueryParams =" + queryParams);

        // Add a header to inform the client that the following
        // data is a recordCollection Object.
        ostream.writeObject("RecordCollection header");

        // Get appId records(appId records are records which identifies
        // the records with application name and time stamp ).
        //
        // for extracting appId records, process each record and
        // search whether the record starts with "appId" string.
        //
        // if the log records starts with "appId"
        // i.e if (log records starts with "appId") then
        // add the records to appIdRecordCollection
        // else if (log records starts with "link") then
        // add the records to linkRecordCollection
        // else
        // add the records to recordCollection
        //
        // Note: In the process of locating appId records
        // the remaining records are also processed and
        // stored as "linkRecordCollection" and
        // "recordCollection" based on the content of the records
        if (queryString.equals("getAppSpecificRecordCollection")) {
          // This call populates both appIdRecordCollection and
          // the rest of record collection
          appIdRecordCollection = getAppIdRecordCollection("appId", nodes);
          // printCollection(appIdRecordCollection);

          // write recordCollection
          ostream.writeObject(recordCollection);
          // printCollection(recordCollection);

          // Parse through all appId records and
          // find the current application name
          String applicationName = getCurrentApplicationName();

          // Parse all link records and find all the
          // applications that are linked to the current application
          Vector linkedApplicationNames = getLinkedApplicationNames();

          // Using the application names, isolate the
          // application specific logs from the rest of the logs
          Collection newAppSpecificRecordCollection = getAppSpecificRecordCollection(
              applicationName, linkedApplicationNames);
          // printCollection(newAppSpecificRecordCollection);

          // write applicaiton specific record Collection
          ostream.writeObject(newAppSpecificRecordCollection);
        } else {
          recordCollection = processLogs(queryString, queryParams, nodes);
          // write recordCollection
          ostream.writeObject(recordCollection);
        }
        // printCollection(appIdRecordCollection);
      }
      ostream.flush();
      ostream.close();
    } catch (ParserConfigurationException pce) {
      TestUtil.logErr("PaserConfigurationException :" + pce.getMessage());
      TestUtil.printStackTrace(pce);
      throw new ServletException(pce.getMessage(), pce);
    } catch (SAXException se) {
      TestUtil.logErr("SAXException :" + se.getMessage());
      TestUtil.printStackTrace(se);
      throw new ServletException(se.getMessage(), se);
    } catch (IOException ioe) {
      TestUtil.logErr("IOException :" + ioe.getMessage());
      TestUtil.printStackTrace(ioe);
      ioe.printStackTrace();
      throw new ServletException("log file not found :" + ioe.getMessage(),
          ioe);
    } catch (SecurityException se) {
      TestUtil.logErr("SecurityException :" + se.getMessage());
      TestUtil.printStackTrace(se);
      se.printStackTrace();
      throw new ServletException(se.getMessage(), se);
    } catch (Exception e) {
      TestUtil.logErr("Exception :" + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new ServletException(e.getMessage(), e);
    }
  }

  public Collection processLogs(String queryString, String queryParams,
      NodeList nodes) throws Exception {
    Collection recordCollection = null;

    if (queryString.equals("findLogsBySequenceNumber")) {
      recordCollection = findLogsBySequenceNumber(queryParams, nodes);
    } else if (queryString.equals("findLogsBySubStringMatch")) {
      recordCollection = findLogsBySubStringMatch(queryParams, nodes);
    } else if (queryString.equals("findLogsByPrefix")) {
      recordCollection = findLogsByPrefix(queryParams, nodes);
    } else if (queryString.equals("pullAllLogRecords")) {
      recordCollection = pullAllLogRecords(queryParams, nodes);
    } else {
      TestUtil.logErr("InCorrect query string :+queryString");
      throw new Exception("InCorrect query string :" + queryString);
    }
    return recordCollection;
  }

  /**
   * Fetches all JSR196 SPI logs from TSSVLog.txt
   */
  public Collection pullAllLogRecords(String queryParams, NodeList nodes)
      throws Exception {
    Collection recordCollection = new Vector();
    Node childNode;
    Node recordNode;
    NodeList recordNodeChildren;

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

  /**
   * Locates the logs based on SubStringMatch
   */
  public Collection findLogsBySubStringMatch(String queryParams, NodeList nodes)
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
          if (nodeValue.indexOf(queryParams) > 0) {
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

  /**
   * Locates the logs based on the given prefix string
   */
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
            // create a new record entry that matches the
            // query criteria
            LogRecordEntry matchingRecordEntry = new LogRecordEntry(recordNode);
            recordCollection.add(matchingRecordEntry);
          }
        }
      }
    }
    return recordCollection;
  }

  /**
   * This method retrieves the appId records.
   *
   * i.e if (log records starts with "appId") then add the records to
   * appIdRecordCollection else add the records to recordCollection
   *
   * Note: In the process of locating appId records the remaining records are
   * also isolated and stored in a collection called "recordCollection"
   */
  public Collection getAppIdRecordCollection(String queryParams, NodeList nodes)
      throws Exception {
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
            // create a new record entry that matches the
            // query criteria i.e "appId"
            LogRecordEntry matchingRecordEntry = new LogRecordEntry(recordNode);
            this.appIdRecordCollection.add(matchingRecordEntry);
          } else if (nodeValue.startsWith("link")) {
            // create a new record entry for link records
            LogRecordEntry linkRecordEntry = new LogRecordEntry(recordNode);
            this.linkRecordCollection.add(linkRecordEntry);
          } else {
            // create a new record entry that do not
            // match the query criteria
            LogRecordEntry nonMatchingRecordEntry = new LogRecordEntry(
                recordNode);
            this.recordCollection.add(nonMatchingRecordEntry);
          }
        }
      }
    }
    return appIdRecordCollection;
  }

  /**
   * Locates logs based on the given sequenceNumber
   */
  public Collection findLogsBySequenceNumber(String queryParams, NodeList nodes)
      throws Exception {
    Collection recordCollection = new Vector();
    String nodeName;
    String nodeValue;
    Node childNode;
    Node recordNode;
    NodeList recordNodeChildren;
    int sequenceNumber = (new Integer(queryParams)).intValue();
    for (int i = 0; i < nodes.getLength(); i++) {
      // Take the first record
      recordNode = nodes.item(i);
      // get all the child nodes for the first record
      recordNodeChildren = recordNode.getChildNodes();
      for (int j = 0; j < recordNodeChildren.getLength(); j++) {
        childNode = recordNodeChildren.item(j);
        nodeName = childNode.getNodeName();
        if (nodeName.equals("sequence")) {
          nodeValue = getText(childNode);
          int nodeValueInInt = (new Integer(nodeValue)).intValue();
          if (nodeValueInInt == sequenceNumber) {
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

  public void printCollection(Collection recordCollection) {
    LogRecordEntry recordEntry = null;
    Iterator iterator = recordCollection.iterator();
    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      printRecordEntry(recordEntry);
    }
  }

  public void printRecordEntry(LogRecordEntry rec) {
    System.out.println("*******Log Content*******");
    System.out.println("MilliSeconds  =" + rec.getMilliSeconds());
    System.out.println("seqence no  =" + rec.getSequenceNumber());
    System.out.println("Message     =" + rec.getMessage());
    if (rec.getClassName() != null)
      System.out.println("Class name  =" + rec.getClassName());
    if (rec.getMethodName() != null)
      System.out.println("Method name =" + rec.getMethodName());
    if (rec.getLevel() != null)
      System.out.println("Level        =" + rec.getLevel());
    if (rec.getThrown() != null)
      System.out.println("Thrown       =" + rec.getThrown());
    System.out.println();

  }

  // This method returns the current application name by analysing
  // all the appId log records.
  //
  // The appId log record contains the Application name, the timeStamp in the
  // appId log record is also used fo this calculation
  //
  // For example, let us examine the following 3 appId log records.
  //
  // appId :: MyApp recordTimeStamp = 1058312446598
  // appId :: App recordTimeStamp = 1058312463706
  // appId :: adminapp recordTimeStamp = 1058312480593
  //
  // In the above 3 appId records
  // a) the first field "appId :: " identifies this as a appId record
  // b) the second field indicates the application name
  // c) recordTimeStamp is the "millis" element of the log record which
  // specifies the timestamp
  //
  // By comparing the timestamps we can locate the latest of all appId records
  // and return the application name associated with it.
  public String getCurrentApplicationName() {
    LogRecordEntry recordEntry = null;
    String temp = null;
    String appName = null;
    String prevAppName = null;
    long prevRecordTimeStamp = 0;
    long recordTimeStamp = 0;
    String[] tokenArray = new String[2];

    if (appIdRecordCollection == null) {
      TestUtil.logMsg("Record collection empty : No appId records found");
      return null;
    }

    Iterator iterator = appIdRecordCollection.iterator();
    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      String message = recordEntry.getMessage();

      // Remove the string "appId :: " from the message
      temp = message.substring(9, message.length());

      // Get appName and timeStampString
      tokenArray = temp.split(" , ");
      appName = tokenArray[0];
      // timeStampString = tokenArray[1];

      // Get timeStampString
      recordTimeStamp = recordEntry.getMilliSeconds();

      // now unstuff application name
      appName = unStuffData(appName);

      // TestUtil.logMsg("appName ="+appName);
      // TestUtil.logMsg("timeStampString ="+timeStampString);

      if (recordTimeStamp < prevRecordTimeStamp) {
        recordTimeStamp = prevRecordTimeStamp;
        appName = prevAppName;
      }
    }
    return appName;
  }

  // This method returns the linked application names by analysing
  // all the link log records.
  //
  // The link log record contains the list of Application names,
  // the record timeStamp is also used for this calculation
  //
  // For example, let us examine the following 2 link log records.
  //
  // link :: MyApp,SecondApp recordTimeStamp = 1058312446598
  // link :: App,SecondApp,ThirdApp recordTimeStamp = 1058312463706
  //
  // In the above 3 link records
  // a) the first field "link :: " identifies this as a link record
  // b) the second field indicates the application names that are
  // linked to.
  // Note: Linked application names are idenified using
  // the comma separator.
  // c) the recordTimeStamp is the "millis" element in the log record
  // which specifies the time at which the record was created.
  //
  // By comparing the timestamps we can locate the latest link records
  // and return the application names linked to it.
  public Vector getLinkedApplicationNames() {
    LogRecordEntry recordEntry = null;
    String temp = null;
    String timeStampString = null;
    String appNames = null;
    String prevAppName = null;
    long prevRecordTimeStamp = 0;
    long recordTimeStamp = 0;
    String[] tokenArray = new String[2];
    Vector applicationNames = new Vector();

    if (linkRecordCollection == null) {
      TestUtil.logMsg("Record collection empty : No link records found");
      return null;
    }

    Iterator iterator = linkRecordCollection.iterator();
    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      String message = recordEntry.getMessage();

      // Remove the string "link :: " from the message
      temp = message.substring(8, message.length());

      // Get appName and timeStampString
      tokenArray = temp.split(" : ");
      appNames = tokenArray[0];
      // timeStampString = tokenArray[1];

      // get recordTimeStamp
      recordTimeStamp = recordEntry.getMilliSeconds();

      // now unstuff application name
      appNames = unStuffData(appNames);

      if (recordTimeStamp < prevRecordTimeStamp) {
        recordTimeStamp = prevRecordTimeStamp;
        appNames = prevAppName;
      }
    }

    StringTokenizer strtoken;

    if (appNames != null) {
      // create a vector with applicationNames
      strtoken = new StringTokenizer(appNames, ",");
      if (appNames.indexOf(",") > 0) {
        // if the appNames string contains multiple applications
        // add all of them to the vector applicationNames
        while (strtoken.hasMoreTokens()) {
          applicationNames.add(strtoken.nextToken());
        }
      } else if (appNames != null) {
        // if the appNames string contains only one application
        // add it to the vector applicationNames
        applicationNames.add(appNames);
      }
    } else {
      // else return null
      return null;
    }

    return applicationNames;
  }

  /*
   * This method reads all non-appId records from the record collection and
   * isolates current appSpecific records from the rest using the given
   * applicationName and the linkedApplicationNames.
   */
  public Collection getAppSpecificRecordCollection(String applicationName,
      Vector linkedApplicationNames) {
    LogRecordEntry recordEntry = null;

    if (recordCollection == null) {
      TestUtil.logMsg("Record collection empty : No records found");
      return null;
    }
    Iterator iterator = this.recordCollection.iterator();
    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      String message = recordEntry.getMessage();

      // if recordEntry contains the specified applicationName
      // Add the record to appSpecificRecordCollection
      if (message.indexOf(applicationName) > 0) {
        appSpecificRecordCollection.add(recordEntry);
      }
    }

    if (linkedApplicationNames != null) {
      // retrieve all the records associated with
      // linked applications.
      for (Enumeration appEnum = linkedApplicationNames.elements(); appEnum
          .hasMoreElements();) {
        applicationName = (String) appEnum.nextElement();

        iterator = this.recordCollection.iterator();
        while (iterator.hasNext()) {
          recordEntry = (LogRecordEntry) iterator.next();
          String message = recordEntry.getMessage();

          // if recordEntry contains the specified applicationName
          // Add the record to appSpecificRecordCollection
          if (message.indexOf(applicationName) > 0) {
            appSpecificRecordCollection.add(recordEntry);
          }
        }

      }
    }
    return appSpecificRecordCollection;
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

  // This will remove the stuffed characters in the input string
  // Note: The non-alphabets in the input string was already stuffed by
  // the same character, this method unstuff those characters
  public static String unStuffData(String inputStr) {
    char[] outStr = new char[2048];
    char[] str = inputStr.toCharArray();
    for (int i = 0, j = 0; i < str.length;) {

      int a = (new Character(str[i])).getNumericValue(str[i]);

      // Don't stuff extra character if the character is an alphabet
      //
      // Numeric values for alphabets falls in 10 to 35, this includes
      // both upper and lower cases
      if ((a > 9) && (a < 36)) {
        outStr[j++] = str[i++];
      } else { // unstuff the character
        outStr[j++] = str[i++]; // just skip the next character
        i++;
      }
    }

    return ((new String(outStr)).trim());
  }

}
