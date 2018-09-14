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

package com.sun.ts.tests.securityapi.idstore.ldap.binddn;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_ldap_binddn_web";

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
   * @testName: testAnnotationLdapIDStore_Binddn
   *
   * @assertion_ids: Security:JAVADOC:119;
   * Security:JAVADOC:121;Security:JAVADOC:123;Security:JAVADOC:124;Security:
   * JAVADOC:125;Security:JAVADOC:127;Security:JAVADOC:128
   *
   * @test_Strategy: Test bindDn and related properties for ldap IDStore. Set
   * below properties in LdapIdentityStoreDefination annotation: 1. bindDn 2.
   * passw?ord 3. sear?chBase 4. callerSearchFilter, then request with corret
   * user/pwd, authentication result should be VALID
   * 
   * 
   * request with tomx/secret1: return VALID with groups [Administratorx,
   * Managerx] request with tom/secret1: return with INVALID request with
   * bobx/secret1: return with VALID with groups [Administratorx, Managerx,
   * Employeex] -->bobx is memberof three groups: Administratorx, Managerx,
   * Employeex
   *
   */
  public void testAnnotationLdapIDStore_Binddn() throws Fault {
    String testName = "idstore/ldap/testAnnotationLdapIDStore_Binddn";
    String pageSecBase = pageServletBase + "/ServletForLdapIDStore";
    String username = "tomx";
    String password = "secret1";

    String pageSec = pageSecBase + "?user=" + username;
    pageSec += "&pwd=" + password;
    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[Administratorx, Managerx]").append("|");
    sb.append("web username: ").append(username);
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    dumpResponse();

    username = "tom";
    pageSec = pageSecBase + "?user=" + username;
    pageSec += "&pwd=" + password;
    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=INVALID").append("|");
    sb1.append("ValidateResultGroups=[]").append("|");
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, sb1.toString());
    invoke();
    dumpResponse();

    username = "bobx";
    pageSec = pageSecBase + "?user=" + username;
    pageSec += "&pwd=secret3";
    StringBuffer sb2 = new StringBuffer(100);
    sb2.append("ValidateResultStatus=VALID").append("|");
    sb2.append("ValidateResultGroups=[Administratorx]").append("|");
    sb2.append("web username: ").append(username);
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, sb2.toString());
    invoke();
    dumpResponse();
  }

}
