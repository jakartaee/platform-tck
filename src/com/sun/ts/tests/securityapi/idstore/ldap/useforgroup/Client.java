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

package com.sun.ts.tests.securityapi.idstore.ldap.useforgroup;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_ldap_useforgroup_web";

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
   * @testName: testIdentityStore_ldap_useforgroup
   *
   * @assertion_ids: Security:JAVADOC:118;Security:SPEC:3.2-1
   * 
   *
   * @test_Strategy:
   * 
   * @LdapIdentityStoreDefinition( url = "ldap://localhost:11389/", callerBaseDn
   * = "ou=caller,dc=securityapi,dc=net", groupSearchBase =
   * "ou=group,dc=securityapi,dc=net", useFor={ValidationType.PROVIDE_GROUPS},
   * priority=100 )
   *
   * Two IdentityStore exist: LdapIdentityStore: (useFor getGroup only),
   * priority=100 use/pwd/group: tom/secret1/Administrator, Manager,
   * SubAdministrator, SubManager use/pwd/group: emma/secret2/Administrator,
   * Employee, SubAdministrator use/pwd/group: bob/secret3/Administrator
   * IdentityStore1: (useFor both validation and getgroup); priority=1000
   * use/pwd/group: tom/secret1/Administrator1, Manager1 use/pwd/group:
   * emma/secret12/Administrator1, Employee1 use/pwd/group:
   * bob/secret13/Administrator1 IdentityStore2: (useFor getgroup only);
   * priority=200 use/pwd/group: tom/secret1/Administrator2, Manager2
   * use/pwd/group: emma/secret12/Administrator2, Employee2 use/pwd/group:
   * bob/secret13/Administrator2
   * 
   * Step1. request with valid tom/secret1. return VALID with groups in
   * IdentityStore1 & LdapIdentityStore & IdentityStore2 Step2. request with
   * valid emma/secret12. return VALID with groups in IdentityStore1 &
   * LdapIdentityStore & IdentityStore2
   * 
   * 
   */
  public void testIdentityStore_ldap_useforgroup() throws Fault {
    String testName = "idstore/ldap/testIdentityStore_validationType_ldap";
    String pageSec = pageServletBase + "/ServletForLdapIDStore";
    String username = "tom";
    String password = "secret1";

    // Step1. request with valid tom/secret1. VALID returns in IdentityStore1
    // Groups IdentityStore1 & LdapIdentityStore & IdentityStore2
    String url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[").append("|");
    sb.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName + "_valid");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    checkGroupsSet("ValidateResultGroups",
        "Administrator1, Manager1, Administrator, Manager, SubAdministrator, SubManager,IDStore2:getCallerGroups, Administrator2, Manager2");
    dumpResponse();

    // Step2. request with valid emma/secret12. return VALID with groups in
    // IdentityStore1 & LdapIdentityStore & IdentityStore2
    username = "emma";
    password = "secret12";
    url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=VALID").append("|");
    sb1.append("ValidateResultGroups=[").append("|");
    sb1.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName + "_valid");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb1.toString());
    invoke();
    checkGroupsSet("ValidateResultGroups",
        "Administrator1, Employee1, Administrator, Employee, SubAdministrator, IDStore2:getCallerGroups, Administrator2, Employee2");
    dumpResponse();
  }

}
