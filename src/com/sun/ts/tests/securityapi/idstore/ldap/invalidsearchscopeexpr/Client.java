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

package com.sun.ts.tests.securityapi.idstore.ldap.invalidsearchscopeexpr;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_ldap_invalidsearchscopeexpr_web";

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
   * @testName: testAnnotationLdapIDStore_invalidsearchScopeExpression
   *
   * @assertion_ids: Security:JAVADOC:180
   *
   * @test_Strategy:
   * 
   * callerSearchBase is : ou=caller,dc=securityapi,dc=net
   * ou=caller,dc=securityapi,dc=net user: tom, bob, emma
   * ou=subcaller,ou=caller,dc=securityapi,dc=net user:subtom,subbob,subemma
   * 
   * groupSearchBase is: ou=group,dc=securityapi,dc=net
   * ou=group,dc=securityapi,dc=net Group: Administrator, Manager, Employmee
   * ou=subgroup,ou=group,dc=securityapi,dc=net Group SubAdministrator,
   * SubManager, SubEmployee
   * 
   * user/group: tom/Administrator, Manager, SubAdministrator, SubManager
   * subtom/SubAdministrator, SubManager
   * 
   * value of callerSearchScope=SUB_TREE && groupSearchScope=SUB_TREE value of
   * callerSearchScopeExpression=INVALID exression &&
   * groupSearchScopeExpression=INVALID expression
   * 
   * Then: request with subtom/secret1, get exception request with tom/secret1,
   * get exception
   */
  public void testAnnotationLdapIDStore_invalidsearchScopeExpression()
      throws Fault {
    String testName = "idstore/ldap/testAnnotationLdapIDStore_invalidsearchScopeExpression";
    String pageSecBase = pageServletBase + "/ServletForLdapIDStore";
    String username = "tom";
    String password = "secret1";

    String pageSec = pageSecBase + "?user=" + username;
    pageSec += "&pwd=" + password;
    StringBuffer sb = new StringBuffer(100);
    sb.append("Exception received.").append("|");
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    dumpResponse();

    pageSec = pageSecBase + "?user=subtom";
    pageSec += "&pwd=" + password;
    StringBuffer sb1 = new StringBuffer(100);
    sb.append("Exception received.").append("|");
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, sb1.toString());
    invoke();
    dumpResponse();
  }

}
