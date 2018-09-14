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

package com.sun.ts.tests.jacc.util;

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

import java.util.Properties;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import java.security.Permission;
import java.security.Permissions;
import java.security.PermissionCollection;
import javax.security.jacc.WebResourcePermission;
import javax.security.jacc.WebRoleRefPermission;
import javax.security.jacc.WebUserDataPermission;
import javax.security.jacc.EJBMethodPermission;
import javax.security.jacc.EJBRoleRefPermission;

/**
 *
 * @author Raja Perumal
 */
/**
 * LogFileProcessor does the following operations
 *
 * 1) Fetches log records from JACCLog.txt
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

  private String logFileLocation = null;

  private Collection recordCollection = new Vector();

  private Collection appIdRecordCollection = new Vector();

  private Collection linkRecordCollection = new Vector();

  private Collection appSpecificRecordCollection = new Vector();

  private Permissions appSpecificUnCheckedPermissions = new Permissions();

  private Permissions appSpecificExcludedPermissions = new Permissions();

  private Permissions appSpecificAddToRolePermissions = new Permissions();

  public LogFileProcessor() {
  }

  public LogFileProcessor(Properties props) {
    setup(props);
  }

  /**
   * setup method
   */
  public void setup(Properties p) {
    boolean pass = true;
    try {
      if (p != null) {
        TestUtil
            .logMsg("setting logFileLocation based on passed in Properties");
        logFileLocation = p.getProperty("log.file.location");
      } else {
        TestUtil.logMsg(
            "setting logFileLocation based on passed in System.getProperty()");
        logFileLocation = System.getProperty("log.file.location");
      }
      if (logFileLocation == null) {
        pass = false;
      }
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

  public Permissions getAppSpecificUnCheckedPermissions() {
    return appSpecificUnCheckedPermissions;
  }

  public Permissions getAppSpecificExcludedPermissions() {
    return appSpecificExcludedPermissions;
  }

  public Permissions getAppSpecificAddToRolePermissions() {
    return appSpecificAddToRolePermissions;
  }

  /*
   * This is convenience method for pulling out a list of permissions from
   * records that are identified with passed in that match all of the following:
   * - permission category (e.g. "excluded", "unchecked", "addToRole") -
   * permission type ("WebResourcePermission", etc) - with an appcontext that
   * contains the passed in appContext value. If you want a complete matching
   * appContext, then pass the whole thing in.
   */
  public Permissions getAppSpecificPermissions(String permCat, String permType,
      String appContext) {
    Permissions permsToReturn = new Permissions();
    LogRecordEntry recordEntry = null;
    Permission p = null;

    if (appSpecificRecordCollection == null) {
      return null;
    }

    Iterator iterator = appSpecificRecordCollection.iterator();

    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      p = getPermissionFromRecordEntry(recordEntry, permCat, permType,
          appContext);
      if (p != null) {
        permsToReturn.add(p);
      }
    }

    // printPermissionCollection(permsToReturn);
    return permsToReturn;
  }

  public void fetchLogs(String accessMethod) {
    fetchLogs(accessMethod, null);
  }

  /**
   * FetchLogs pull logs from the server.
   *
   */
  public void fetchLogs(String accessMethod, String appName) {

    File logfile = null;

    try {
      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
          .newInstance();
      DocumentBuilder documentBuilder = documentBuilderFactory
          .newDocumentBuilder();

      logFileLocation += "/JACCLog.txt";

      if (logFileLocation != null) {
        logfile = new File(logFileLocation);
      }
      if ((logfile == null) || (!logfile.exists())) {
        // String header = "Log file does not exists";
        // ostream.writeObject(header);

        System.out
            .println("Log File : " + logFileLocation + " does not exists");
        System.out.println("Check permissions for log file ");
        System.out
            .println("See User guide for Configuring log file permissions");
      } else {
        // LogRecords will be added to JACCLog.txt as long as the server is
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

        String queryString = "pullAllRecords";
        String queryParams = "fullLog";

        StringTokenizer strtoken = new StringTokenizer(accessMethod, "|");

        if (accessMethod.indexOf("|") > 0) {
          queryString = strtoken.nextToken();
          queryParams = strtoken.nextToken();
        }

        if (queryString.equals("pullAllRecords")) {
          recordCollection = pullAllLogRecords(queryParams, nodes);
          // printCollection(recordCollection);
        } else if (queryString.equals("getAppSpecificRecordCollection")) {

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

          // This call populates both appIdRecordCollection and
          // the rest of record collection
          appIdRecordCollection = getAppIdRecordCollection("appId", nodes);
          // printCollection(appIdRecordCollection);
          // printCollection(recordCollection);

          // Parse through all appId records and
          // find the current application name
          String applicationName = null;
          if (appName == null) {
            applicationName = getCurrentApplicationName();
          } else {
            applicationName = getCurrentApplicationName(appName);
          }

          // Parse all link records and find all the
          // applications that are linked to the current application
          Vector linkedApplicationNames = getLinkedApplicationNames();

          // Using the application names, isolate the
          // application specific logs from the rest of the logs
          Collection newAppSpecificRecordCollection = getAppSpecificRecordCollection(
              applicationName, linkedApplicationNames);
          // printCollection(newAppSpecificRecordCollection);

        }

        // From the record collection read all the records
        // and construct list of Permissions such as
        //
        // 1) appSpecificUnCheckedPermissions
        // 2) appSpecificExcludedPermissions
        // 3) appSpecificAddToRolePermissions
        //
        // Where "appSpecificUnCheckedPermissions" contains all the
        // application specific unchecked permission collection.
        //
        // "appSpecificExcludedPermissions" contains all the
        // application specific excluded permission collection.
        //
        // "appSpecificAddToRolePermissions" contains all the
        // application specific add to role permission collection.
        //
        getPermissionCollection();
      }

    } catch (Exception e) {
      TestUtil.logErr(e.getMessage());
      TestUtil.printStackTrace(e);
      e.printStackTrace();
    }

  }

  public PermissionCollection getPermissionCollection() {
    PermissionCollection pColl = new Permissions();
    LogRecordEntry recordEntry = null;
    Permission p = null;

    if (appSpecificRecordCollection == null) {
      return null;
    }
    Iterator iterator = appSpecificRecordCollection.iterator();

    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      p = getPermissionFromRecordEntry(recordEntry);
      if (p != null) {
        pColl.add(p);
      }
    }
    // printPermissionCollection(pColl);
    return pColl;
  }

  // Parse record entry and extract permissions from the following
  // type of log records "unchecked", "excluded" and "addToRole"
  //
  // unchecked :: appName , 1058323788497 , EJBMethodPermission , MEJBBean ,
  // getMBeanCount,Remote
  // addToRole :: appName3 , 1058323977373 , WebResourcePermission ,
  // /secured.jsp , GET,POST
  // excluded :: appName2 , 1058323977373 , WebUserDataPermission ,
  // /excluded.jsp , GET,POST
  //
  // In the above records
  //
  // 1) First field identifies the category of record
  // 2) second field identifies the application name
  // 3) thrid field identifies the timestamp in which
  // the record was created.
  // 4) fourth field identifies the Permission type such as
  // a) WebResourcePermission b) WebRoleRefPermission
  // c) WebUserDataPermission d) EJBMehodPermission
  // e) EJBRoleRefPermission
  // 5) fifth field identifies the permission name
  // 6) sixth field identifies the permission action.
  //
  public Permission getPermissionFromRecordEntry(LogRecordEntry recordEntry) {

    String permissionCategory = null;
    String applicationContext = null;
    String temp = null;
    String message = null;
    String permissionType = null;
    String permissionName = null;
    String permissionAction = null;
    String permissionNameAndAction = null;
    String applicationTimeStamp = null;
    String[] tokenArray = new String[2];
    StringTokenizer permCategoryToken = null;
    StringTokenizer strtok = null;
    boolean isUnChecked = false;
    boolean isExcluded = false;
    boolean isAddToRole = false;

    Permission p = null;

    if (recordEntry != null) {
      message = recordEntry.getMessage();
      // Get permission category
      // i.e "unchecked, excluded or addTorole"
      permCategoryToken = new StringTokenizer(message, " :: ");
      if (message.indexOf(" :: ") > 0) {
        permissionCategory = permCategoryToken.nextToken();
        temp = message.substring(permissionCategory.length() + 4,
            message.length());
      }
      // TestUtil.logMsg("PermissionCategory ="+permissionCategory);

      if (permissionCategory != null) {
        if (permissionCategory.equals("unchecked")) {
          isUnChecked = true;
        } else if (permissionCategory.equals("excluded")) {
          isExcluded = true;
        } else if (permissionCategory.equals("addToRole")) {
          isAddToRole = true;
        }
      }

      // i.e Proceed only if the permission Category is one of the following
      // a) unchecked
      // b) excluded
      // c) addToRole
      // else return null
      if (isUnChecked || isExcluded || isAddToRole) {
        // Get ApplicationContext
        tokenArray = getTokens(temp, " , ");
        applicationContext = tokenArray[0];
        temp = tokenArray[1];
        TestUtil.logTrace("Application Context  =" + applicationContext);

        // Get Application time stamp
        tokenArray = getTokens(temp, " , ");
        applicationTimeStamp = tokenArray[0];
        temp = tokenArray[1];
        TestUtil.logTrace("Application Time stamp =" + applicationTimeStamp);

        // Get permission Type
        tokenArray = getTokens(temp, " , ");
        permissionType = tokenArray[0];
        permissionNameAndAction = tokenArray[1];
        TestUtil.logTrace("PermissionType =" + permissionType);

        // extract permission name and action
        tokenArray = getTokens(permissionNameAndAction, " , ");
        permissionName = tokenArray[0];
        permissionAction = tokenArray[1];
        TestUtil.logTrace("permissionName    = " + permissionName);
        TestUtil.logTrace("permissionAction  = " + permissionAction);

        // Change string "null" to just null
        // i.e "null" --> null
        if (permissionAction.equals("null")) {
          permissionAction = null; // Construct permissions based on their
                                   // permission type
        }
        if (permissionType.equals("WebResourcePermission")) {
          p = new WebResourcePermission(permissionName, permissionAction);
        } else if (permissionType.equals("WebRoleRefPermission")) {
          p = new WebRoleRefPermission(permissionName, permissionAction);
        } else if (permissionType.equals("WebUserDataPermission")) {
          p = new WebUserDataPermission(permissionName, permissionAction);
        } else if (permissionType.equals("EJBMethodPermission")) {
          p = new EJBMethodPermission(permissionName, permissionAction);
        } else if (permissionType.equals("EJBRoleRefPermission")) {
          p = new EJBRoleRefPermission(permissionName, permissionAction); // Add
                                                                          // permissions
                                                                          // to
                                                                          // their
                                                                          // corresponding
                                                                          // permission
                                                                          // collection
          // based on their permission category type
        }
        if (isUnChecked) {
          appSpecificUnCheckedPermissions.add(p);
        } else if (isExcluded) {
          appSpecificExcludedPermissions.add(p);
        } else if (isAddToRole) {
          appSpecificAddToRolePermissions.add(p);

        }
      }
    }

    return p;
  }

  public Permission getPermissionFromRecordEntry(LogRecordEntry recordEntry,
      String permCat, String permType, String appContext) {
    String permissionCategory = null;
    String applicationContext = null;
    String temp = null;
    String message = null;
    String permissionType = null;
    String permissionName = null;
    String permissionAction = null;
    String permissionNameAndAction = null;
    String applicationTimeStamp = null;
    String[] tokenArray = new String[2];
    StringTokenizer permCategoryToken = null;

    Permission p = null;

    if (recordEntry != null) {
      message = recordEntry.getMessage();
      // Get permission category
      // i.e "unchecked, excluded or addToRole"
      permCategoryToken = new StringTokenizer(message, " :: ");
      if (message.indexOf(" :: ") > 0) {
        permissionCategory = permCategoryToken.nextToken();
        temp = message.substring(permissionCategory.length() + 4,
            message.length());
      }

      if ((permissionCategory == null)
          || (!permissionCategory.equals(permCat))) {
        TestUtil.logTrace(permissionCategory + " != " + permCat);
        return null;
      }
      TestUtil.logTrace(permissionCategory + " == " + permCat);

      // Get ApplicationContext
      tokenArray = getTokens(temp, " , ");
      applicationContext = tokenArray[0];
      temp = tokenArray[1];
      if (!applicationContext.contains(appContext)) {
        TestUtil.logTrace(applicationContext + " != " + appContext);
        return null;
      }
      TestUtil.logTrace("applicationContext =" + applicationContext);

      // Get Application time stamp
      tokenArray = getTokens(temp, " , ");
      applicationTimeStamp = tokenArray[0];
      temp = tokenArray[1];
      TestUtil.logTrace("Application Time stamp =" + applicationTimeStamp);

      // Get permission Type
      tokenArray = getTokens(temp, " , ");
      permissionType = tokenArray[0];
      permissionNameAndAction = tokenArray[1];
      TestUtil.logTrace("PermissionType =" + permissionType);

      // extract permission name and action
      tokenArray = getTokens(permissionNameAndAction, " , ");
      permissionName = tokenArray[0];
      permissionAction = tokenArray[1];
      TestUtil.logTrace("permissionName    = " + permissionName);
      TestUtil.logTrace("permissionAction  = " + permissionAction);

      if (permissionAction.equals("null")) {
        permissionAction = null; // Construct permissions based on their
                                 // permission type
      }

      if (!permissionType.equals(permType)) {
        return null;
      }

      if (permissionType.equals("WebResourcePermission")) {
        p = new WebResourcePermission(permissionName, permissionAction);
      } else if (permissionType.equals("WebRoleRefPermission")) {
        p = new WebRoleRefPermission(permissionName, permissionAction);
      } else if (permissionType.equals("WebUserDataPermission")) {
        p = new WebUserDataPermission(permissionName, permissionAction);
      } else if (permissionType.equals("EJBMethodPermission")) {
        p = new EJBMethodPermission(permissionName, permissionAction);
      } else if (permissionType.equals("EJBRoleRefPermission")) {
        p = new EJBRoleRefPermission(permissionName, permissionAction);
      }
    }

    return p;
  }

  public Permissions getSpecificPermissions(Permissions suppliedPermCollection,
      String permissionType) {
    Permission p = null;
    Permissions expectedPermissionCollection = new Permissions();

    if (permissionType.equals("WebResourcePermission")) {
      for (Enumeration en = suppliedPermCollection.elements(); en
          .hasMoreElements();) {
        p = (Permission) en.nextElement();
        if (p instanceof WebResourcePermission) {
          expectedPermissionCollection.add(p);
        }
      }
    } else if (permissionType.equals("WebUserDataPermission")) {
      for (Enumeration en = suppliedPermCollection.elements(); en
          .hasMoreElements();) {
        p = (Permission) en.nextElement();

        if (p instanceof WebUserDataPermission) {
          expectedPermissionCollection.add(p);
        }
      }
    } else if (permissionType.equals("WebRoleRefPermission")) {
      for (Enumeration en = suppliedPermCollection.elements(); en
          .hasMoreElements();) {
        p = (Permission) en.nextElement();
        if (p instanceof WebRoleRefPermission) {
          expectedPermissionCollection.add(p);
        }
      }
    } else if (permissionType.equals("EJBMethodPermission")) {
      for (Enumeration en = suppliedPermCollection.elements(); en
          .hasMoreElements();) {
        p = (Permission) en.nextElement();
        if (p instanceof EJBMethodPermission) {
          expectedPermissionCollection.add(p);
        }
      }
    } else if (permissionType.equals("EJBRoleRefPermission")) {
      for (Enumeration en = suppliedPermCollection.elements(); en
          .hasMoreElements();) {
        p = (Permission) en.nextElement();
        if (p instanceof EJBRoleRefPermission) {
          expectedPermissionCollection.add(p);
        }
      }
    }
    return expectedPermissionCollection;
  }

  /**
   * Fetches all JSR196 SPI logs from JACCLog.txt
   */
  public static Collection pullAllLogRecords(String queryParams, NodeList nodes)
      throws Exception {
    Collection recordCollection = new Vector();
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
   *
   * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
   * contains = logProcessor.verifyLogContains("Java EE rocks");
   *
   * where "properties" contains the key value pair for 1) log.file.location
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
      if (numberOfMatches == numberOfArgs) {
        return true;
      }
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
   * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
   * contains = logProcessor.verifyLogContainsOneOf(arr);
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
   * LogFileProcessor logProcessor = new LogFileProcessor(properties); boolean
   * contains = logProcessor.verifyLogContainsOneOf(arr);
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

  /**
   * verifyLogImplies() takes the individual expectedPermissions and and checks
   * whether the generatedPermissions.implies() is true.
   */
  public boolean verifyLogImplies(Permissions expectedPermissions,
      Permissions generatedPermissions) {
    boolean verified = false;
    Permission p;

    for (Enumeration en = expectedPermissions.elements(); en
        .hasMoreElements();) {
      p = (Permission) en.nextElement();

      verified = generatedPermissions.implies(p);
      if (!verified) {
        TestUtil.logErr(
            "The following permission doesn't match with server generated Permissions");
        TestUtil.logErr("permissionName   = " + p.getName());
        TestUtil.logErr("permissionAction = " + p.getActions());

        TestUtil.logErr("\n\n");
        TestUtil.logErr("Print Expected Permissions :");
        printPermissions(expectedPermissions);
        TestUtil.logErr("\n\n");
        TestUtil.logErr("Print Generated Permissions :");
        printPermissions(generatedPermissions);
        return false;
      }
    }

    // following code compares each generatedPermission with
    // expectedPermissionCollection and lists the extra permissions
    for (Enumeration en = generatedPermissions.elements(); en
        .hasMoreElements();) {
      p = (Permission) en.nextElement();
      verified = expectedPermissions.implies(p);
      if (!verified) {
        TestUtil.logMsg(
            "The following server generated permission doesn't match with the expected Permissions");
        TestUtil.logMsg("permissionName   = " + p.getName());
        TestUtil.logMsg("permissionAction = " + p.getActions());
      }
    }

    return true;
  }

  public void printCollection(Collection recordCollection) {
    LogRecordEntry recordEntry = null;
    Iterator iterator = recordCollection.iterator();

    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      printRecordEntry(recordEntry);
    }
  }

  // Print heterogeneous collection of permissions
  public void printPermissions(Permissions perms) {
    int count = 0;
    for (Enumeration en = perms.elements(); en.hasMoreElements();) {
      count++;
      Permission p = (Permission) en.nextElement();
      TestUtil.logMsg("-------------");
      TestUtil.logMsg(count + ") permissionType   = " + p.getClass().getName());
      TestUtil.logMsg(count + ") permissionName   = " + p.getName());
      TestUtil.logMsg(count + ") permissionAction = " + p.getActions());
    }

  }

  public void printPermissionCollection(PermissionCollection permCollection) {
    String permissionType = null;
    int count = 0;

    for (Enumeration en = permCollection.elements(); en.hasMoreElements();) {
      count++;

      Permission p = (Permission) en.nextElement();
      if (p instanceof WebResourcePermission) {
        permissionType = "WebResourcePermission";
      } else if (p instanceof WebUserDataPermission) {
        permissionType = "WebUserDataPermission";
      } else if (p instanceof WebRoleRefPermission) {
        permissionType = "WebRoleRefPermission";
      } else if (p instanceof EJBMethodPermission) {
        permissionType = "EJBMethodPermission";
      } else if (p instanceof EJBRoleRefPermission) {
        permissionType = "EJBRoleRefPermission";
      }
      TestUtil.logMsg("-------------");
      TestUtil.logMsg(count + ") permissionType   = " + permissionType);
      TestUtil.logMsg(count + ") permissionName   = " + p.getName());
      TestUtil.logMsg(count + ") permissionAction = " + p.getActions());
    }
  }

  public void printRecordEntry(LogRecordEntry rec) {
    TestUtil.logMsg("*******Log Content*******");

    TestUtil.logMsg("Milli Seconds  =" + rec.getMilliSeconds());
    TestUtil.logMsg("Seqence no  =" + rec.getSequenceNumber());
    TestUtil.logMsg("Message     =" + rec.getMessage());
    if (rec.getClassName() != null) {
      TestUtil.logMsg("Class name  =" + rec.getClassName());
    }
    if (rec.getMethodName() != null) {
      TestUtil.logMsg("Method name =" + rec.getMethodName());
    }
    if (rec.getLevel() != null) {
      TestUtil.logMsg("Level        =" + rec.getLevel());
    }
    if (rec.getThrown() != null) {
      TestUtil.logMsg("Thrown       =" + rec.getThrown());
    }
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
      if (strtok.hasMoreTokens()) {
        qparams = strtok.nextToken();
      }
    }

    // return query string or query params based on the content
    // of the string str
    if (str.equals("LogQueryString")) {
      return qstring;
    } else {
      return qparams;
    }
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
    } else {
      // With JSR115 Maintenance review change the permission name
      // for WebRoleRefPermission can be an empty string.
      // this results in permissionName=""
      // i.e the input string will have a value such as
      // str=" , <RoleName>"
      array[0] = "";
      array[1] = strtoken.nextToken();
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

  public String getCurrentApplicationName() {
    return getCurrentAppName(null);
  }

  public String getCurrentApplicationName(String appName) {
    return getCurrentAppName(appName);
  }

  // This method returns the current application name by analysing
  // all the appId log records.
  //
  // The appId log record contains the Application name and the timeStamp
  // For example, let us examine the following 3 appId log records.
  //
  // appId :: MyApp , 1058312446598
  // appId :: App , 1058312463706
  // appId :: adminapp , 1058312480593
  //
  // In the above 3 appId records
  // a) the first field "appId :: " identifies this as a appId record
  // b) the second field indicates the application name
  // c) the third field refers to the timestamp at which the record was
  // created.
  //
  // By comparing the timestamps we can locate the latest of all appId records
  // and return the application name associated with it.
  private String getCurrentAppName(String matchAppName) {
    LogRecordEntry recordEntry = null;
    String temp = null;
    String timeStampString = null;
    String appName = null;
    String prevAppName = null;
    long prevRecordTimeStamp = 0;
    long recordTimeStamp = 0;
    String[] tokenArray = new String[2];

    if ((appIdRecordCollection == null) || (appIdRecordCollection.isEmpty())) {
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
      timeStampString = tokenArray[1];

      // now unstuff application name
      appName = unStuffData(appName);

      TestUtil.logMsg("appName =" + appName);
      TestUtil.logMsg("timeStampString =" + timeStampString);

      // Get long value from the string
      if (matchAppName == null) {
        // warning: what this does is look for the newest record
        // and use that as the appname. If multiple apps
        // are deployed at same time - this could return
        // a record that belongs to a different app.
        recordTimeStamp = (new Long(timeStampString)).longValue();
        if (recordTimeStamp < prevRecordTimeStamp) {
          recordTimeStamp = prevRecordTimeStamp;
          appName = prevAppName;
        }
      } else {
        // we want an appname that matches/contains the passed in appName
        if (appName.contains(matchAppName)) {
          break;
        }
      }
    }
    return appName;
  }

  // This method returns the linked application names by analysing
  // all the link log records.
  //
  // The link log record contains the list of Application names
  // and the timeStamp.
  // For example, let us examine the following 2 link log records.
  //
  // link :: MyApp,SecondApp : 1058312446598
  // link :: App,SecondApp,ThirdApp : 1058312463706
  //
  // In the above 3 link records
  // a) the first field "link :: " identifies this as a link record
  // b) the second field indicates the application names that are
  // linked to.
  // Note: Linked application names are idenified using
  // the comma separator.
  // c) the last field indicates the timestamp at which the record was
  // created.
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
      timeStampString = tokenArray[1];

      // now unstuff application name
      appNames = unStuffData(appNames);

      // Get long value from the string
      recordTimeStamp = (new Long(timeStampString)).longValue();
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
   * This returns the collection of records that have a message field that
   * contains the keyword "MSG_TAG". This is a generic flag used to put a phrase
   * or info into a records message field so that we can easily search on those
   * MSG_TAG fields later on.
   */
  public Collection<LogRecordEntry> getMsgTagRecordCollection() {
    LogRecordEntry recordEntry = null;
    Collection<LogRecordEntry> msgTagRecordCollection = new Vector();

    TestUtil.logTrace("getMsgTagRecordCollection():  Record collection size : "
        + recordCollection.size());
    if (recordCollection == null) {
      TestUtil.logTrace("Record collection empty : No records found");
      return null;
    }
    Iterator iterator = this.recordCollection.iterator();
    while (iterator.hasNext()) {
      recordEntry = (LogRecordEntry) iterator.next();
      String message = recordEntry.getMessage();

      if (message.indexOf("MSG_TAG") > -1) {
        TestUtil.logTrace("getMsgTagRecordCollection():  message = " + message);
        msgTagRecordCollection.add(recordEntry);
      }
    }
    TestUtil
        .logTrace("getMsgTagRecordCollection():  returning collection size of: "
            + msgTagRecordCollection.size());
    return msgTagRecordCollection;
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

  // This will remove the stuffed characters in the input string
  // Note: The non-alphabets in the input string was already stuffed by
  // the same character, this method unstuff those characters
  public static String unStuffData(String inputStr) {
    char[] outStr = new char[2048];
    char[] str = inputStr.toCharArray();

    TestUtil.logMsg("unStuffData called with:  " + inputStr);

    for (int i = 0, j = 0; i < str.length;) {

      int a = Character.getNumericValue(str[i]);

      // Don't stuff extra character if the character is an alphabet
      //
      // Numeric values for alphabets falls in 10 to 35, this includes
      // both upper and lower cases
      if ((a > 9) && (a < 36)) {
        outStr[j++] = str[i++];
      } else { // unstuff the character
        outStr[j] = str[i]; // just skip the next character
        // Remove only the stuffed characters not data separators
        if (((i + 1) < str.length) && (str[i + 1] == str[i])) {
          // just skip the next character
          i++;
        }
        i++;
        j++;
      }
    }

    TestUtil.logMsg("unStuffData returning:  " + (new String(outStr)).trim());
    return ((new String(outStr)).trim());
  }
}
