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

/*
 * $Id$
 */

package com.sun.ts.tests.servlet.spec.security.secbasic;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.common.jspservletsec.SecBasicClient;

import java.util.Properties;

/*
 * This  class uses the SecBasicClient to do most of its actual testing.  
 * The tests within this file will either test security constraints using the 
 * DD file or by using annotations.  The naming scheme in this file is as
 * follows:
 *   test1()      -  tests security constaints using DD file
 *   test1_anno() - tests security constraints using annotations
 * In both cases (testing using DD or annotations) the actual tests are 
 * being done in the secBasicClient.  The real difference regards whether
 * the servlet under test is annotation or DD based.
 *
 */
public class Client extends SecBasicClient {
  // Shared test variables:
  private Properties props = null;

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

  // Note: To share the commoncode between servlet and JSP,
  // the commoncode is kept under
  // <TS_HOME>/src/com/sun/ts/tests/common/jspservletsec/secbasicClient.java
  // This subclass(Client.java) is used to flag the superclass
  // to run servlet related secbasic tests
  //

  /*
   * setup() passes "servlet" as the argument to its parent class setup()
   *
   */

  /*
   * @class.setup_props: webServerHost; webServerPort; user; password; authuser;
   * authpassword; ts_home;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    // create newarguments to pass into superclass setup method.
    String[] newargs = new String[2];

    // "servlet" is the flag passed to superclass
    String argExt = new String("servlet");
    newargs[0] = argExt;
    newargs[1] = argExt; // dummy argument

    // Inform the super class to run servlet related tests
    super.setup(newargs, p);
  }

  /*
   * @testName: test1
   *
   * @assertion_ids:Servlet:SPEC:140; JavaEE:SPEC:21
   *
   * @test_Strategy: 1. Send request to access jspSec.jsp 2. Receive
   * authentication request.
   *
   */

  /*
   * @testName: test1_anno
   *
   * @assertion_ids:Servlet:SPEC:140; JavaEE:SPEC:21; Servlet:SPEC:290;
   * Servlet:SPEC:291; Servlet:SPEC:293; Servlet:SPEC:296; Servlet:SPEC:297;
   *
   * @test_Strategy: This does the same thing as test1() with the difference
   * being that this test is using a servlet w/ security constraints defined
   * thru annotations instead of DD. This test validates the following: 1. Send
   * request to access ServletSecAnnoTest (thru SecBasicClient) as
   * unauthenticated user 2. Receive authentication request.
   *
   */
  public void test1_anno() throws Fault {
    // save off pageSec so that we can reuse it
    String tempPageSec = pageSec;
    pageSec = "/servlet_sec_secbasic_web/ServletSecAnnoTest";

    try {
      super.test1();
    } catch (Fault e) {
      throw e;
    } finally {
      // reset pageSec to orig value
      pageSec = tempPageSec;
    }
  }

  /*
   * @testName: test2
   *
   * @assertion_ids: Servlet:SPEC:140;Servlet:JAVADOC:368; JavaEE:SPEC:281
   *
   *
   * @test_Strategy: 1. Send request with correct authentication. 2. Receive
   * page (ensure principal is correct, and ensure that getRemoteUser() returns
   * the correct name)
   *
   * Note: 1. If user has not been authenticated and user attempts to access a
   * protected web resource, and user enters a valid username and password, the
   * original web resource is returned and user is authenticated.
   * 
   * 2. getRemoteUser() returns the user name that the client authenticated
   * with.
   *
   */

  /*
   * @testName: test2_anno
   *
   * @assertion_ids: Servlet:SPEC:140; JavaEE:SPEC:21; Servlet:SPEC:290;
   * Servlet:SPEC:291; Servlet:SPEC:293; Servlet:SPEC:296; Servlet:SPEC:297;
   *
   * @test_Strategy: This does the same thing as test2() with the difference
   * being that this test is using a servlet w/ security constraints defined
   * thru annotations instead of DD. This particular test (test2_anno) actually
   * requires the use of the DD to set the <role-name> and <role-link> since
   * these cant be linked using annotations only.
   *
   * This test validates the following: 1. Send request with correct
   * authentication. 2. Receive page (ensure principal is correct, and ensure
   * that getRemoteUser() returns the correct name)
   *
   * Note: 1. If user has not been authenticated and user attempts to access a
   * protected web resource, and user enters a valid username and password, the
   * original web resource is returned and user is authenticated.
   *
   * 2. getRemoteUser() returns the user name that the client authenticated
   * with. NOTE: the test (test2 in secBasicClient) actually does a check of the
   * "ADM" role (as defined in the DD via the role-link element.)
   *
   */
  public void test2_anno() throws Fault {
    // save off pageSec so that we can reuse it
    String tempPageSec = pageSec;
    pageSec = "/servlet_sec_secbasic_web/ServletSecAnnoTest";

    try {
      super.test2();
    } catch (Fault e) {
      throw e;
    } finally {
      // reset pageSec to orig value
      pageSec = tempPageSec;
    }
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: Servlet:SPEC:162; JavaEE:SPEC:30; JavaEE:SPEC:281
   *
   * @test_Strategy: 1. Re-send request with incorrect authentication. 2.
   * Receive authentication request.
   * 
   * Note: 1. If user has not been authenticated and user attempts to access a
   * protected web resource, and user enters an invalid username and password,
   * the container denies access to the web resource.
   *
   */

  /*
   * @testName: test3_anno
   *
   * @assertion_ids: Servlet:SPEC:162; JavaEE:SPEC:30; JavaEE:SPEC:281;
   * Servlet:SPEC:290; Servlet:SPEC:291; Servlet:SPEC:293; Servlet:SPEC:296;
   * Servlet:SPEC:297;
   *
   * @test_Strategy: This does the same thing as test3() with the difference
   * being that this test is using a servlet w/ security constraints defined
   * thru annotations instead of DD. This test validates the following: 1.
   * Re-send request with incorrect authentication. 2. Receive authentication
   * request.
   *
   * Note: 1. If user has not been authenticated and user attempts to access a
   * protected web resource, and user enters an invalid username and password,
   * the container denies access to the web resource.
   *
   */
  public void test3_anno() throws Fault {
    // save off pageSec so that we can reuse it
    String tempPageSec = pageSec;
    pageSec = "/servlet_sec_secbasic_web/ServletSecAnnoTest";

    try {
      super.test3();
    } catch (Fault e) {
      throw e;
    } finally {
      // reset pageSec to orig value
      pageSec = tempPageSec;
    }
  }

  /*
   * @testName: test4
   *
   * @assertion_ids: Servlet:SPEC:162; JavaEE:SPEC:30; JavaEE:SPEC:281
   *
   * @test_Strategy: 1. Send request with correct authentication for user
   * javajoe for a page javajoe is allowed to access. 2. Receive page (this
   * verifies that the javajoe user is set up properly). 3. Send request with
   * correct authentication, but incorrect authorization to access resource 4.
   * Receive error
   *
   * Note: If user has not been authenticated and user attempts to access a
   * protected web resource, and user enters an valid username and password, but
   * for a role that is not authorized to access the resource, the container
   * denies access to the web resource.
   *
   */

  /*
   * @testName: test4_anno
   *
   * @assertion_ids: Servlet:SPEC:162; JavaEE:SPEC:30; JavaEE:SPEC:281;
   * Servlet:SPEC:290; Servlet:SPEC:291; Servlet:SPEC:293; Servlet:SPEC:298;
   *
   * @test_Strategy: This does the same thing as test4() with the difference
   * being that this test is using a servlet w/ security constraints defined
   * thru annotations instead of DD. This test validates the following: 1. Send
   * request with correct authentication for user javajoe for a page javajoe is
   * allowed to access. 2. Receive page (this verifies that the javajoe user is
   * set up properly). 3. Send request with correct authentication, but
   * incorrect authorization to access resource 4. Receive error
   *
   * Note: If user has not been authenticated and user attempts to access a
   * protected web resource, and user enters an valid username and password, but
   * for a role that is not authorized to access the resource, the container
   * denies access to the web resource.
   *
   */
  public void test4_anno() throws Fault {
    // save off pageSec so that we can reuse it
    String tempPageSec = pageSec;
    String tempPageGuest = pageGuest;
    pageSec = "/servlet_sec_secbasic_web/ServletSecAnnoTest";
    pageGuest = "/servlet_sec_secbasic_web/GuestPageAnnoTest";

    try {
      super.test4();
    } catch (Fault e) {
      throw e;
    } finally {
      // reset pageSec to orig value
      pageSec = tempPageSec;
      pageGuest = tempPageGuest;
    }
  }

  /*
   * @testName: test5
   *
   * @assertion_ids: Servlet:JAVADOC:368; Servlet:JAVADOC:369; JavaEE:SPEC:30;
   * JavaEE:SPEC:281
   *
   * @test_Strategy: 1. Send request for unprotected.jsp with no authentication.
   * 2. Receive page 3. Search the returned page for "!true!", which would
   * indicate that at least one call to isUserInRole attempted by
   * unprotected.jsp returned true. 4. check that getRemoteUser() returns null.
   * 
   * Note: 1. If user has not been authenticated and user attempts to access an
   * unprotected web resource, the web resource is returned without need to
   * authenticate. 2. isUserInRole() must return false for any valid or invalid
   * role reference. 3. getRemoteUser() must return false
   *
   */

  /*
   * @testName: test5_anno
   *
   * @assertion_ids: Servlet:JAVADOC:368; Servlet:JAVADOC:369; JavaEE:SPEC:30;
   * JavaEE:SPEC:281; Servlet:SPEC:290; Servlet:SPEC:298;
   *
   * @test_Strategy: This does the same thing as test5() with the difference
   * being that this test is using a servlet w/ security constraints defined
   * thru annotations instead of DD. This test validates the following: 1. Send
   * request for unprotected.jsp with no authentication. 2. Receive page 3.
   * Search the returned page for "!true!", which would indicate that at least
   * one call to isUserInRole attempted by unprotected.jsp returned true. 4.
   * check that getRemoteUser() returns null.
   * 
   * Note: 1. If user has not been authenticated and user attempts to access an
   * unprotected web resource, the web resource is returned without need to
   * authenticate. 2. isUserInRole() must return false for any valid or invalid
   * role reference. 3. getRemoteUser() must return false
   *
   */
  public void test5_anno() throws Fault {
    // save off pageSec so that we can reuse it
    String tempPageUnprotected = pageUnprotected;
    pageUnprotected = "/servlet_sec_secbasic_web/UnProtectedAnnoTest";

    try {
      super.test5();
    } catch (Fault e) {
      throw e;
    } finally {
      // reset pageSec to orig value
      pageUnprotected = tempPageUnprotected;
    }
  }

  /*
   * @testName: test6
   *
   * @assertion_ids: Servlet:SPEC:149
   *
   * @test_Strategy: Given two servlets in the same application, each of which
   * calls isUserInRole(X), and where X is linked to different roles in the
   * scope of each of the servlets (i.e. R1 for servlet 1 and R2 for servlet 2),
   * then a user whose identity is mapped to R1 but not R2, shall get a true
   * return value from isUserInRole( X ) in servlet 1, and a false return value
   * from servlet 2 (a user whose identity is mapped to R2 but not R1 should get
   * the inverse set of return values).
   * 
   * Since test1 already verifies the functionality for isUserInRole returning
   * true, this test needs only verify that it should return false for the other
   * jsp. For this test, MGR and ADM are swapped, so isUserInRole() should
   * return opposite values from test1.
   *
   * 1. Send request to access rolereverse.jsp 2. Receive redirect to login
   * page, extract location and session id cookie. 3. Send request to access new
   * location, send cookie 4. Receive login page 5. Send form response with
   * username and password 6. Receive redirect to resource 7. Request resource
   * 8. Receive resource (check isUserInRole for all known roles)
   *
   */

  /*
   * @testName: test6_anno
   *
   * @assertion_ids: Servlet:SPEC:149; Servlet:SPEC:290; Servlet:SPEC:293;
   * Servlet:SPEC:296; Servlet:SPEC:297;
   *
   * @test_Strategy: This does the same thing as test6() with the difference
   * being that this test is using a servlet w/ security constraints defined
   * thru annotations instead of DD. This test validates the following: Given
   * two servlets in the same application, each of which calls isUserInRole(X),
   * and where X is linked to different roles in the scope of each of the
   * servlets (i.e. R1 for servlet 1 and R2 for servlet 2), then a user whose
   * identity is mapped to R1 but not R2, shall get a true return value from
   * isUserInRole( X ) in servlet 1, and a false return value from servlet 2 (a
   * user whose identity is mapped to R2 but not R1 should get the inverse set
   * of return values).
   * 
   * Since test1 already verifies the functionality for isUserInRole returning
   * true, this test needs only verify that it should return false for the other
   * jsp. For this test, MGR and ADM are swapped, so isUserInRole() should
   * return opposite values from test1.
   *
   * 1. Send request to access rolereverse.jsp 2. Receive redirect to login
   * page, extract location and session id cookie. 3. Send request to access new
   * location, send cookie 4. Receive login page 5. Send form response with
   * username and password 6. Receive redirect to resource 7. Request resource
   * 8. Receive resource (check isUserInRole for all known roles)
   *
   */
  public void test6_anno() throws Fault {
    // save off pageSec so that we can reuse it
    String tempPageRoleReverse = pageRoleReverse;
    pageRoleReverse = "/servlet_sec_secbasic_web/RoleReverseAnnoTest";

    try {
      super.test6();
    } catch (Fault e) {
      throw e;
    } finally {
      // reset pageSec to orig value
      pageRoleReverse = tempPageRoleReverse;
    }
  }

  /*
   * @testName: test7
   *
   * @assertion_ids: Servlet:SPEC:162; JavaEE:SPEC:30; JavaEE:SPEC:281
   *
   * @test_Strategy: 1. Re-send request with incorrect authentication. 2.
   * Receive authentication request.
   *
   * Note: 1. If user has not been authenticated and user attempts to access a
   * protected web resource, and user enters an invalid username and password,
   * the container denies access to the web resource.
   *
   */

  /*
   * @testName: test7_anno
   *
   * @assertion_ids: Servlet:SPEC:162; JavaEE:SPEC:30; JavaEE:SPEC:281;
   * Servlet:SPEC:290;
   *
   * @test_Strategy: This does the same thing as test7() with the difference
   * being that this test is using a servlet w/ security constraints defined
   * thru annotations instead of DD. This test validates the following: 1.
   * Re-send request with incorrect authentication. 2. Receive authentication
   * request.
   *
   * Note: 1. If user has not been authenticated and user attempts to access a
   * protected web resource, and user enters an invalid username and password,
   * the container denies access to the web resource.
   *
   */
  public void test7_anno() throws Fault {
    // save off pageSec so that we can reuse it
    String tempPageSec = pageSec;
    pageSec = "/servlet_sec_secbasic_web/ServletSecAnnoTest";

    try {
      super.test7();
    } catch (Fault e) {
      throw e;
    } finally {
      // reset pageSec to orig value
      pageSec = tempPageSec;
    }
  }

}
