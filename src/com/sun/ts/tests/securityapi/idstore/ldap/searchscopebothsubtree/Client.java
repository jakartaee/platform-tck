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

package com.sun.ts.tests.securityapi.idstore.ldap.searchscopebothsubtree;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_ldap_searchscopebothsubtree_web";

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
   * @testName: testAnnotationLdapIDStore_searchScopeBothSubTree
   *
   * @assertion_ids: Security:JAVADOC:174;Security:JAVADOC:179
   *
   * @test_Strategy:
   * 
   * ou=caller,dc=securityapi,dc=net user: tom, bob, emma
   * ou=subcaller,ou=caller,dc=securityapi,dc=net user:subtom,subbob,subemma
   * 
   * ou=group,dc=securityapi,dc=net Group: Administrator, Manager, Employmee
   * ou=subgroup,ou=group,dc=securityapi,dc=net Group SubAdministrator,
   * SubManager, SubEmployee
   * 
   * user/group: tom/Administrator, Manager, SubAdministrator, SubManager
   * subtom/SubAdministrator, SubManager
   * 
   * if value of callerSearchScope is SUB_TREE && value of groupSearchScope is
   * SUB_TREE request with subtom/secret1, the response returns group
   * [SubAdministrator, SubManager] request with tom/secret1, the response
   * returns group [Administrator, Manager, SubAdministrator, SubManager]
   * 
   */
  public void testAnnotationLdapIDStore_searchScopeBothSubTree() throws Fault {
    String testName = "idstore/ldap/testAnnotationLdapIDStore_searchScopeBothSubTree";
    String pageSecBase = pageServletBase + "/ServletForLdapIDStore";
    String username = "tom";
    String password = "secret1";

    String pageSec = pageSecBase + "?user=" + username;
    pageSec += "&pwd=" + password;
    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[").append("|");
    sb.append("web username: ").append(username);
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    checkGroupsSet("ValidateResultGroups",
        "Administrator, Manager, SubAdministrator, SubManager");
    dumpResponse();

    pageSec = pageSecBase + "?user=subtom";
    pageSec += "&pwd=" + password;
    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=VALID").append("|");
    sb1.append("ValidateResultGroups=[SubAdministrator, SubManager]")
        .append("|");
    sb1.append("web username: subtom");
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, sb1.toString());
    invoke();
    dumpResponse();
  }

}
