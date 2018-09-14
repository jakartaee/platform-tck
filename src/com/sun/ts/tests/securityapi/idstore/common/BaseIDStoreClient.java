/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.idstore.common;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.webclient.BaseUrlClient;
import com.sun.ts.lib.harness.EETest.Fault;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class BaseIDStoreClient extends BaseUrlClient {

  // Shared test variables:
  protected Properties props = null;

  // Constants:
  protected static final String CLASS_TRACE_HEADER = "[IDStoreClient]: ";

  protected void checkGroupsSet(String groupSetName, String groups)
      throws Fault {
    String resp = "";
    try {
      resp = _testCase.getResponse().getResponseBodyAsString();
    } catch (Exception ex) {
      throw new Fault("Fail to get response:  " + ex.getMessage());
    }

    int start = resp.indexOf(groupSetName);
    if (start < 0) {
      throw new Fault("No GroupSet Name in response");
    }

    int end = resp.indexOf("]", start);
    String actualGroupString = resp.substring(start + groupSetName.length() + 2,
        end);
    logMessage("Actual Groups in Response is " + actualGroupString);
    logMessage("Expect Groups is " + groups);
    String[] actualGroups = actualGroupString.split(",");
    String[] expectGroups = groups.split(",");

    TestUtil.logMsg("Check the group set...");
    if (actualGroups.length != expectGroups.length) {
      throw new Fault("The GroupSet Number is not expected. Actual/Expected="
          + actualGroups.length + "/" + expectGroups.length);
    }

    boolean exist = false;
    for (String group : actualGroups) {
      exist = false;
      group = group.trim();
      for (int i = 0; i < expectGroups.length; i++) {
        // logMessage("Expect Group is " + expectGroups[i]);
        // logMessage("Actual Group is " + group);
        if (expectGroups[i].trim().equals(group)) {
          exist = true;
          break;
        }
      }

      if (!exist)
        throw new Fault("Group " + group + " is not the expected group");
    }

  }

  protected void sendRequestAndVerify(String testName, String url,
      String username, String password, String searchStr) throws Fault {
    sendRequestAndVerify(testName, url, username, password, searchStr, 200);
  }

  protected void sendRequestAndVerify(String testName, String url,
      String username, String password, String searchStr, String groups)
      throws Fault {
    sendRequestAndVerify(testName, url, username, password, searchStr, groups,
        200);
  }

  protected void sendRequestAndVerify(String testName, String url,
      String username, String password, String searchStr, int statusCode)
      throws Fault {
    sendRequestAndVerify(testName, url, username, password, searchStr, null,
        200);
  }

  protected void sendRequestAndVerify(String testName, String url,
      String username, String password, String searchStr, String groups,
      int statusCode) throws Fault {
    String pageSec = url;

    pageSec += "?user=" + username;
    pageSec += "&pwd=" + password;

    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, searchStr);
    TEST_PROPS.setProperty(STATUS_CODE, String.valueOf(statusCode));
    invoke();
    if (groups != null && groups.length() > 0)
      checkGroupsSet("ValidateResultGroups", groups);
    dumpResponse();
  }

  /**
   * Returns a valid HTTP/1.1 request line.
   * 
   * @param method
   *          the request method
   * @param path
   *          the request path
   * @return a valid HTTP/1.1 request line
   */
  protected static String getRequestLine(String method, String path) {
    return method + " " + path + " HTTP/1.1";
  }

  /**
   * Simple wrapper around TestUtil.logMessage().
   * 
   * @param message
   *          - the message to log
   */
  protected static void logMessage(String message) {
    TestUtil.logMsg(CLASS_TRACE_HEADER + message);
  }

  protected void dumpResponse() {
    try {
      if ((_testCase != null) && (_testCase.getResponse() != null)) {
        trace(_testCase.getResponse().getResponseBodyAsString());
      }
    } catch (Exception ex) {
      // must've had problem getting response so dump exception but continue on
      ex.printStackTrace();
    }
  }

  /**
   * Simple wrapper around TestUtil.logTrace().
   * 
   * @param message
   *          - the message to log
   */
  protected static void trace(String message) {
    TestUtil.logTrace(CLASS_TRACE_HEADER + message);
  }
}
