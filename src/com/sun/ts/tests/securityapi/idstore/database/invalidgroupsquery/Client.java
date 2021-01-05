/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.idstore.database.invalidgroupsquery;

import java.io.PrintWriter;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_database_invalidgroupsquery_web";

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
   * @testName: testAnnotationDBIDStore_Invalidgroupsquery
   *
   * @assertion_ids: Security:JAVADOC:114
   * 
   * @test_Strategy:
   * 
   * Test the properties of DB IDStore annotation with invalid groupsQuery
   * request with valid user/pwd, get exception since groupsQuery is invalid
   * 
   */
  public void testAnnotationDBIDStore_Invalidgroupsquery() throws Fault {
    String testName = "idstore/database/testAnnotationDBIDStore_Invalidgroupsquery";
    String pageSec = pageServletBase + "/ServletForDatabaseIDStore";
    String username = "tom";
    String password = "secret1";

    String url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb = new StringBuffer(100);
    sb.append("Exception received.").append("|");

    TEST_PROPS.setProperty(TEST_NAME, testName + "_valid");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    dumpResponse();

  }

}
