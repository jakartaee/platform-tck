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

package com.sun.ts.tests.securityapi.idstore.database.hashalgorithmparam;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_database_hashalgorithmparam_web";

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
   * @testName: testAnnotationDBIDStore_HashAlgorithmParam
   *
   * @assertion_ids: Security:JAVADOC:171; Security:JAVADOC:189;
   * Security:JAVADOC:190; Security:JAVADOC:191
   * 
   * @test_Strategy:
   * 
   * user: "tom_hash512_saltsize16", password is secret1, and encoded by below
   * parameter when store in DB
   * param.put("Pbkdf2PasswordHash.Algorithm","PBKDF2WithHmacSHA512");
   * param.put("Pbkdf2PasswordHash.Iterations","1024");
   * param.put("Pbkdf2PasswordHash.SaltSizeBytes","16");
   * param.put("Pbkdf2PasswordHash.KeySizeBytes","16");
   * 
   * user: tom_hash256_saltsize32, password is secret1, and encoded by below
   * parameter when store in DB
   * param.put("Pbkdf2PasswordHash.Algorithm","PBKDF2WithHmacSHA256");
   * param.put("Pbkdf2PasswordHash.Iterations","2048");
   * param.put("Pbkdf2PasswordHash.SaltSizeBytes","32");
   * param.put("Pbkdf2PasswordHash.KeySizeBytes","32");
   * 
   * user: tom_hash512_saltsize32, password is secret1, and encoded by below
   * parameter when store in DB
   * param.put("Pbkdf2PasswordHash.Algorithm","PBKDF2WithHmacSHA512");
   * param.put("Pbkdf2PasswordHash.Iterations","2048");
   * param.put("Pbkdf2PasswordHash.SaltSizeBytes","32");
   * param.put("Pbkdf2PasswordHash.KeySizeBytes","16");
   * 
   * 
   * In this test case, DatabaseIdentityStore use below parameter:
   * hashAlgorithmParameters = {
   * "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
   * "Pbkdf2PasswordHash.Iterations=2048",
   * "Pbkdf2PasswordHash.SaltSizeBytes=16", "Pbkdf2PasswordHash.KeySizeBytes=16"
   * }
   * 
   * 1) request with tom_hash256_saltsize32/secret1, return VALID with related
   * groups 2) request with tom_hash512_saltsize16/secret1, return VALID with
   * related groups 3) request with tom_hash512_saltsize32/secret1, return VALID
   * with related groups
   */
  public void testAnnotationDBIDStore_HashAlgorithmParam() throws Fault {
    String testName = "idstore/database/testAnnotationDBIDStore_HashAlgorithmParam";
    String pageSec = pageServletBase + "/ServletForDatabaseIDStore";
    String username = "tom_hash256_saltsize32";
    String password = "secret1";

    // Step1. request with user and pwd (the password store in DB is using hash
    String url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[Administrator, Manager]").append("|");
    sb.append("web username: ").append(username);
    TEST_PROPS.setProperty(TEST_NAME, testName + "_with_" + username);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    dumpResponse();

    username = "tom_hash512_saltsize16";
    url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=VALID").append("|");
    sb1.append("ValidateResultGroups=[Administrator, Manager]").append("|");
    sb1.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName + "_with_" + username);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb1.toString());
    invoke();
    dumpResponse();

    username = "tom_hash512_saltsize32";
    url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb2 = new StringBuffer(100);
    sb2.append("ValidateResultStatus=VALID").append("|");
    sb2.append("ValidateResultGroups=[Administrator, Manager]").append("|");
    sb2.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName + "_with_" + username);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb2.toString());
    invoke();
    dumpResponse();

  }

}
