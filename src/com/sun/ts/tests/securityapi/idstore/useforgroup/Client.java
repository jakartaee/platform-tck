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

package com.sun.ts.tests.securityapi.idstore.useforgroup;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;

import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.util.Properties;
import java.io.PrintWriter;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_useforgroup_web";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    Client theTests = new Client();

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   *
   */
  // Note:Based on the input argument setup will intialize JSP or servlet pages

  public void setup(String[] args, Properties p) throws Fault {
    super.setup(args, p);
    props = p;
  }

  /*
   * @testName: testIdentityStore_validationType_useforgroup
   *
   * @assertion_ids: Security:SPEC:3.2.3-2
   *
   * @test_Strategy:
   * 
   * Implement a simple IdentityStore and adding one additional group
   * "getCallerGroups" if getCallerGroups() method is called.
   * 
   */
  public void testIdentityStore_validationType_useforgroup() throws Fault {
    String testName = "idstore/useforgroup/testIdentityStore_validationType_useforgroup";
    String pageSec = pageServletBase + "/ServletForIDStoreGroupOnly";
    String username = "tom";
    String password = "secret1";

    pageSec += "?user=" + username;
    pageSec += "&pwd=" + password;

    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[").append("|");
    sb.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(UNORDERED_SEARCH_STRING, sb.toString());
    invoke();

    checkGroupsSet("ValidateResultGroups",
        "useforgroup:getCallerGroups, Oracle, Oracle_HQ");
    dumpResponse();
  }

}
