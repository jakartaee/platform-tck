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

package com.sun.ts.tests.jacc.web.toolsContracts;

import java.security.Permissions;
import javax.security.jacc.WebResourcePermission;
import javax.security.jacc.WebRoleRefPermission;
import javax.security.jacc.WebUserDataPermission;

import java.util.Properties;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;

import com.sun.javatest.Status;
import com.sun.ts.tests.jacc.util.LogFileProcessor;
import com.sun.ts.tests.jacc.util.LogRecordEntry;

// CAUTION: *** The expected permissions constructed for various permissions
//           such as WebResourcePermission, WebRoleRefPermission,
//           WebUserDataPermission are based on the application 
//           jacc_toolsContracts. If the application is modified for 
//           any reason then the expected permissions should also be 
//           modified accordingly. ***
//
public class Client extends ServiceEETest {

  private Properties props = null;

  private String contextId = "jacc_ctx";

  LogFileProcessor logProcessor = null;

  private String applicationContext;

  private boolean initialized = false;

  private Permissions unCheckedPermissions = new Permissions();

  private Permissions excludedPermissions = new Permissions();

  private Permissions addToRolePermissions = new Permissions();

  public static void main(String args[]) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: log.file.location; webServerHost; webServerPort;
   *
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    if (!initialized) {
      // create LogFileProcessor
      logProcessor = new LogFileProcessor(props);

      // retrieve logs based on application Name
      logProcessor.fetchLogs("getAppSpecificRecordCollection|appId",
          "toolsContracts");

      // retrieve unchecked permissions as a permission collection
      unCheckedPermissions = logProcessor.getAppSpecificUnCheckedPermissions();

      // retrieve excluded permissions as a permission collection
      excludedPermissions = logProcessor.getAppSpecificExcludedPermissions();

      // retrieve addToRole permissions as a permission collection
      addToRolePermissions = logProcessor.getAppSpecificAddToRolePermissions();

      initialized = true;
    }
  }

  public void cleanup() throws Fault {

  }

  /**
   * @testName: WebResourcePermission
   *
   * @assertion_ids: JACC:SPEC:36; JACC:SPEC:72; JACC:SPEC:27; JACC:SPEC:28;
   *                 JACC:SPEC:52; JACC:SPEC:128;
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy the application.
   *
   *                 3. During deployment, appserver generates permissions for
   *                 the J2EE components based on the given deployment
   *                 descriptor
   *
   *                 4. Retrieve server side logs and verify the generated
   *                 permissions matches the expected permission collection
   *
   */
  public void WebResourcePermission() throws Fault {
    boolean verified = false;
    Permissions expectedUnCheckedPermissions = new Permissions();
    Permissions expectedExcludedPermissions = new Permissions();
    Permissions expectedAddToRolePermissions = new Permissions();

    // ----------UNCHECKED----------//
    // 1) retrieve server generated unchecked policy statements
    // 2) construct expected unchecked policy statements
    // 3) verify expected policy statements with generated policy statements

    // Get "unchecked" WebResourcePermissions
    Permissions uncheckedWebResourcePermissions = logProcessor
        .getSpecificPermissions(unCheckedPermissions, "WebResourcePermission");

    TestUtil.logMsg("Server generated unchecked WebResourcePermissions");
    logProcessor.printPermissionCollection(uncheckedWebResourcePermissions);

    // Construct the expected unchecked WebResourcePermission
    expectedUnCheckedPermissions
        .add(new WebResourcePermission("/unchecked.jsp", (String) null));
    expectedUnCheckedPermissions
        .add(new WebResourcePermission("/sslprotected.jsp", "!GET,POST"));
    expectedUnCheckedPermissions.add(new WebResourcePermission(
        "/:/secured.jsp:/unchecked.jsp:/excluded.jsp:/sslprotected.jsp:/anyauthuser.jsp",
        (String) null));
    expectedUnCheckedPermissions
        .add(new WebResourcePermission("/excluded.jsp", "!GET,POST"));
    expectedUnCheckedPermissions
        .add(new WebResourcePermission("/secured.jsp", "!GET,POST"));
    expectedUnCheckedPermissions
        .add(new WebResourcePermission("/anyauthuser.jsp", "!GET,POST"));

    TestUtil.logMsg("verifying unchecked policy statments:");

    verified = logProcessor.verifyLogImplies(expectedUnCheckedPermissions,
        uncheckedWebResourcePermissions);

    if (!verified) {
      throw new Fault("WebResourcePermission failed: "
          + "unchecked policy statements verification failed");
    } else {
      TestUtil.logMsg("unchecked policy statements verification successful");
    }

    // ---------EXCLUDED----------//
    // 1) retrieve server generated excluded policy statements
    // 2) construct expected excluded policy statements
    // 3) verify expected policy statements with generated policy statements

    // Get "excluded" WebResourcePermissions
    Permissions excludedWebResourcePermissions = logProcessor
        .getSpecificPermissions(excludedPermissions, "WebResourcePermission");

    TestUtil.logMsg("Server generated excluded WebResourcePermissions");
    logProcessor.printPermissionCollection(excludedWebResourcePermissions);

    // Construct the expected excluded WebResourcePermission
    expectedExcludedPermissions
        .add(new WebResourcePermission("/excluded.jsp", "GET,POST"));

    TestUtil.logMsg("verifying excluded policy statments:");

    verified = logProcessor.verifyLogImplies(expectedExcludedPermissions,
        excludedWebResourcePermissions);

    if (!verified) {
      throw new Fault("WebResourcePermission failed: "
          + "excluded policy statements verification failed");
    } else {
      TestUtil.logMsg("excluded policy statements verification successful");
    }

    // ---------ADDTOROLE----------//
    // 1) retrieve server generated addToRole policy statements
    // 2) construct expected addToRole policy statements
    // 3) verify expected policy statements with generated policy statements

    // Get "addToRole" WebResourcePermissions
    Permissions addToRoleWebResourcePermissions = logProcessor
        .getSpecificPermissions(addToRolePermissions, "WebResourcePermission");

    TestUtil.logMsg("Server generated addToRole WebResourcePermissions");
    logProcessor.printPermissionCollection(addToRoleWebResourcePermissions);

    // Construct the expected excluded WebResourcePermission
    expectedAddToRolePermissions
        .add(new WebResourcePermission("/secured.jsp", "GET,POST"));
    expectedAddToRolePermissions
        .add(new WebResourcePermission("/sslprotected.jsp", "GET,POST"));
    expectedAddToRolePermissions
        .add(new WebResourcePermission("/anyauthuser.jsp", "GET,POST"));

    TestUtil.logMsg("verifying addToRole policy statments:");

    verified = logProcessor.verifyLogImplies(expectedAddToRolePermissions,
        addToRoleWebResourcePermissions);

    if (!verified) {
      throw new Fault("WebResourcePermission failed: "
          + "addToRole policy statements verification failed");
    } else {
      TestUtil.logMsg("addToRole policy statements verification successful");
    }
  }

  /**
   * @testName: WebRoleRefPermission
   *
   * @assertion_ids: JACC:SPEC:36; JACC:SPEC:112; JACC:SPEC:38; JACC:SPEC:43;
   *                 JACC:SPEC:44; JACC:JAVADOC:50; JACC:SPEC:27; JACC:SPEC:28;
   *                 JACC:SPEC:45; JACC:SPEC:52; JACC:SPEC:75; JACC:SPEC:128;
   *                 JACC:SPEC:131
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ). 2.
   *                 Deploy the application.
   *
   *                 3. During deployment, appserver generates permissions for
   *                 the J2EE components based on the given deployment
   *                 descriptor
   *
   *                 4. Retrieve server side logs and verify the generated
   *                 permissions matches the expected permission collection
   */
  public void WebRoleRefPermission() throws Fault {
    boolean verified = false;
    Permissions expectedAddToRolePermissions = new Permissions();

    // ---------ADDTOROLE----------//
    // 1) retrieve server generated addToRole policy statements
    // 2) construct expected addToRole policy statements
    // 3) verify expected policy statements with generated policy statements

    // Get "addToRole" WebRoleRefPermissions
    Permissions addToRoleWebRoleRefPermissions = logProcessor
        .getSpecificPermissions(addToRolePermissions, "WebRoleRefPermission");

    TestUtil.logMsg("Server generated addToRole WebRoleRefPermissions");
    logProcessor.printPermissionCollection(addToRoleWebRoleRefPermissions);

    // Construct the expected excluded WebRoleRefPermission
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("secured", "ADM"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("secured", "Administrator"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("secured", "Manager"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("secured", "Employee"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("sslprotected", "MGR"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("sslprotected", "ADM"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("sslprotected", "Administrator"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("sslprotected", "Manager"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("sslprotected", "Employee"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("unchecked", "Manager"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("unchecked", "Administrator"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("unchecked", "Employee"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("excluded", "Manager"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("excluded", "Administrator"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("excluded", "Employee"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("anyauthuser", "Employee"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("anyauthuser", "Manager"));
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("anyauthuser", "Administrator"));

    // JSR115 Maintenance Review changes
    expectedAddToRolePermissions
        .add(new WebRoleRefPermission("", "Administrator"));
    expectedAddToRolePermissions.add(new WebRoleRefPermission("", "Manager"));
    expectedAddToRolePermissions.add(new WebRoleRefPermission("", "Employee"));

    TestUtil.logMsg("verifying addToRole policy statments:");

    verified = logProcessor.verifyLogImplies(expectedAddToRolePermissions,
        addToRoleWebRoleRefPermissions);

    if (!verified) {
      throw new Fault("WebRoleRefPermission failed: "
          + "addToRole policy statements verification failed");
    } else {
      TestUtil.logMsg("addToRole policy statements verification successful");
    }
  }

  /**
   * @testName: AnyAuthUserWebRoleRef
   *
   * @assertion_ids: JACC:SPEC:130; JACC:SPEC:131;
   *
   * @test_Strategy: This is testing that: If the any authenticated user
   *                 role-name, **, does not appear in a security-role-ref
   *                 within the servlet, a WebRoleRefPermission must also be
   *                 added for it. The name of each such WebRoleRefPermission
   *                 must be the servlet-name of the corresponding servlet
   *                 element. steps: 1. We have any-authenticated-user
   *                 referenced in a security-constraint in our DD (for
   *                 anyauthuser.jsp) We have a total of 5 servlets defined in
   *                 our DD also.
   * 
   *                 2. Deploy the application.
   *
   *                 3. During deployment, appserver generates permissions for
   *                 the J2EE components based on the given deployment
   *                 descriptor
   *
   *                 4. Retrieve server side logs and verify the generated
   *                 permissions matches the expected permission collection
   */
  public void AnyAuthUserWebRoleRef() throws Fault {
    boolean verified = false;
    Permissions expectedAddToRolePerms = new Permissions();

    // retrieve server generated addToRole policy statements
    Permissions addToRoleWebRoleRefPermissions = logProcessor
        .getSpecificPermissions(addToRolePermissions, "WebRoleRefPermission");

    // for debug aid, print out server generated addToRole policy statements
    TestUtil.logMsg("Server generated addToRole WebRoleRefPermissions");
    logProcessor.printPermissionCollection(addToRoleWebRoleRefPermissions);

    // according to jacc 1.5 spec (chapter 3, section 3.1.3.3), it states that
    // "a WebRoleRefPermission must also be added for it" (meaning **) and that
    // "The name of each such WebRoleRefPermission must be the servlet-name
    // of the corresponding servlet element."
    // This means for each servlet definition in our web.xml, there will need to
    // exist a WebRoleRefPermission with that servlet name for the ** role.
    //
    expectedAddToRolePerms.add(new WebRoleRefPermission("excluded", "**"));
    expectedAddToRolePerms.add(new WebRoleRefPermission("unchecked", "**"));
    expectedAddToRolePerms.add(new WebRoleRefPermission("sslprotected", "**"));
    expectedAddToRolePerms.add(new WebRoleRefPermission("secured", "**"));
    expectedAddToRolePerms.add(new WebRoleRefPermission("anyauthuser", "**"));

    TestUtil.logMsg("verifying addToRole policy statments:");

    verified = logProcessor.verifyLogImplies(expectedAddToRolePerms,
        addToRoleWebRoleRefPermissions);

    if (!verified) {
      throw new Fault("AnyAuthUserWebRoleRef failed: "
          + "addToRole policy statements for any-authenticated-user (**) failed");
    } else {
      TestUtil.logMsg("addToRole policy statements verification successful");
    }

  }

  /**
   * @testName: WebResourcePermissionExcludedPolicy
   *
   * @assertion_ids: JACC:SPEC:37; JACC:SPEC:114; JACC:SPEC:111; JACC:SPEC:27;
   *                 JACC:SPEC:28; JACC:SPEC:34; JACC:SPEC:52
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy the application.
   *
   *                 3. During deployment, appserver generates permissions for
   *                 the J2EE components based on the given deployment
   *                 descriptor
   *
   *                 4. Retrieve server side logs and verify the generated
   *                 permissions matches the expected permission collection
   *
   */
  public void WebResourcePermissionExcludedPolicy() throws Fault {
    boolean verified = false;
    Permissions expectedExcludedPermissions = new Permissions();

    // ---------EXCLUDED----------//
    // 1) retrieve server generated excluded policy statements
    // 2) construct expected excluded policy statements
    // 3) verify expected policy statements with generated policy statements

    // Get "excluded" WebResourcePermissions
    Permissions excludedWebResourcePermissions = logProcessor
        .getSpecificPermissions(excludedPermissions, "WebResourcePermission");

    TestUtil.logMsg("Server generated excluded WebResourcePermissions");
    logProcessor.printPermissionCollection(excludedWebResourcePermissions);

    // Construct the expected excluded WebResourcePermission
    expectedExcludedPermissions
        .add(new WebResourcePermission("/excluded.jsp", "GET,POST"));

    TestUtil.logMsg("verifying excluded policy statments:");

    verified = logProcessor.verifyLogImplies(expectedExcludedPermissions,
        excludedWebResourcePermissions);

    if (!verified) {
      throw new Fault("WebResourcePermissionExcludedPolicy failed: "
          + "excluded policy statements verification failed");
    } else {
      TestUtil.logMsg("excluded policy statements verification successful");
    }
  }

  /**
   * @testName: WebResourcePermissionUnCheckedPolicy
   *
   * @assertion_ids: JACC:SPEC:36; JACC:SPEC:39; JACC:SPEC:27; JACC:SPEC:28;
   *                 JACC:SPEC:52; JACC:JAVADOC:17
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy the application.
   *
   *                 3. During deployment, appserver generates permissions for
   *                 the J2EE components based on the given deployment
   *                 descriptor
   *
   *                 4. Retrieve server side logs and verify the generated
   *                 unchecked permissions matches the expected permission
   *                 collection
   */
  public void WebResourcePermissionUnCheckedPolicy() throws Fault {
    Permissions expectedPermissions = new Permissions();
    boolean verified = false;

    // Get "unchecked" WebResourcePermissions
    Permissions uncheckedWebResourcePermissions = logProcessor
        .getSpecificPermissions(unCheckedPermissions, "WebResourcePermission");

    TestUtil.logMsg("Server generated unchecked WebResourcePermissions");
    logProcessor.printPermissionCollection(uncheckedWebResourcePermissions);

    // Construct the expected unchecked WebResourcePermission
    expectedPermissions
        .add(new WebResourcePermission("/unchecked.jsp", (String) null));
    expectedPermissions
        .add(new WebResourcePermission("/sslprotected.jsp", "!GET,POST"));
    expectedPermissions.add(new WebResourcePermission(
        "/:/secured.jsp:/unchecked.jsp:/excluded.jsp:/sslprotected.jsp:/anyauthuser.jsp",
        (String) null));
    expectedPermissions
        .add(new WebResourcePermission("/excluded.jsp", "!GET,POST"));
    expectedPermissions
        .add(new WebResourcePermission("/secured.jsp", "!GET,POST"));
    expectedPermissions
        .add(new WebResourcePermission("/anyauthuser.jsp", "!GET,POST"));

    verified = logProcessor.verifyLogImplies(expectedPermissions,
        uncheckedWebResourcePermissions);

    if (!verified) {
      throw new Fault("WebResourcePermissionUnCheckedPolicy failed");
    } else {
      TestUtil.logMsg("WebResourcePermission constructed"
          + " correctly with unchecked policy statements");
    }
  }

  /**
   * @testName: WebUserDataPermission
   *
   * @assertion_ids: JACC:SPEC:41; JACC:SPEC:42; JACC:JAVADOC:54;
   *                 JACC:JAVADOC:56; JACC:JAVADOC:58; JACC:SPEC:27;
   *                 JACC:SPEC:28; JACC:SPEC:34; JACC:SPEC:52
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Deploy the application.
   *
   *                 3. During deployment, appserver generates permissions for
   *                 the J2EE components based on the given deployment
   *                 descriptor
   *
   *                 4. Retrieve server side logs and verify the generated
   *                 unchecked permissions matches the expected permission
   *                 collection
   *
   *
   */
  public void WebUserDataPermission() throws Fault {
    boolean verified = false;
    Permissions expectedUnCheckedPermissions = new Permissions();
    Permissions expectedExcludedPermissions = new Permissions();

    // ----------UNCHECKED----------//
    // 1) retrieve server generated unchecked policy statements
    // 2) construct expected unchecked policy statements
    // 3) verify expected policy statements with generated policy statements

    // Get "unchecked" WebUserDataPermissions
    Permissions uncheckedWebUserDataPermissions = logProcessor
        .getSpecificPermissions(unCheckedPermissions, "WebUserDataPermission");

    TestUtil.logMsg("Server generated unchecked WebUserDataPermissions");
    logProcessor.printPermissionCollection(uncheckedWebUserDataPermissions);

    // Construct the expected unchecked WebUserDataPermission
    expectedUnCheckedPermissions.add(new WebUserDataPermission(
        "/sslprotected.jsp", "GET,POST:CONFIDENTIAL"));
    expectedUnCheckedPermissions
        .add(new WebUserDataPermission("/excluded.jsp", "!GET,POST"));
    expectedUnCheckedPermissions
        .add(new WebUserDataPermission("/sslprotected.jsp", "!GET,POST"));
    expectedUnCheckedPermissions
        .add(new WebUserDataPermission("/secured.jsp", (String) null));
    expectedUnCheckedPermissions
        .add(new WebUserDataPermission("/anyauthuser.jsp", "!GET,POST"));
    expectedUnCheckedPermissions.add(new WebUserDataPermission(
        "/:/unchecked.jsp:/secured.jsp:/sslprotected.jsp:/excluded.jsp:/anyauthuser.jsp",
        (String) null));
    expectedUnCheckedPermissions
        .add(new WebUserDataPermission("/unchecked.jsp", (String) null));

    TestUtil.logMsg("verifying unchecked policy statments:");

    verified = logProcessor.verifyLogImplies(expectedUnCheckedPermissions,
        uncheckedWebUserDataPermissions);

    if (!verified) {
      throw new Fault("WebUserDataPermission failed: "
          + "unchecked policy statements verification failed");
    } else {
      TestUtil.logMsg("unchecked policy statements verification successful");
    }

    // ---------EXCLUDED----------//
    // 1) retrieve server generated excluded policy statements
    // 2) construct expected excluded policy statements
    // 3) verify expected policy statements with generated policy statements

    // Get "excluded" WebUserDataPermission
    Permissions excludedWebUserDataPermissions = logProcessor
        .getSpecificPermissions(excludedPermissions, "WebUserDataPermission");

    TestUtil.logMsg("Server generated excluded WebUserDataPermission");
    logProcessor.printPermissionCollection(excludedWebUserDataPermissions);

    // Construct the expected excluded WebUserDataPermission
    expectedExcludedPermissions
        .add(new WebUserDataPermission("/excluded.jsp", "GET,POST"));

    TestUtil.logMsg("verifying excluded policy statments:");

    verified = logProcessor.verifyLogImplies(expectedExcludedPermissions,
        excludedWebUserDataPermissions);

    if (!verified) {
      throw new Fault("WebUserDataPermission failed: "
          + "excluded policy statements verification failed");
    } else {
      TestUtil.logMsg("excluded policy statements verification successful");
    }
  }

  /**
   * @testName: WebResourcePermissionEquals
   *
   * @assertion_ids: JACC:JAVADOC:40
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of WebResourcePermission.equals()
   *
   *
   */
  public void WebResourcePermissionEquals() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "WebResourcePermission.equals() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("WebResourcePermissionEquals : FAILED");
    } else {
      TestUtil.logMsg("WebResourcePermissionEquals : PASSED");
    }
  }

  /**
   * @testName: WebRoleRefPermissionEquals
   *
   * @assertion_ids: JACC:JAVADOC:47
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of WebRoleRefPermission.equals()
   *
   *
   */
  public void WebRoleRefPermissionEquals() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "WebRoleRefPermission.equals() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("WebRoleRefPermissionEquals : FAILED");
    } else {
      TestUtil.logMsg("WebRoleRefPermissionEquals : PASSED");
    }
  }

  /**
   * @testName: WebUserDataPermissionEquals
   *
   * @assertion_ids: JACC:JAVADOC:53
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of WebUserDataPermission.equals()
   *
   *
   */
  public void WebUserDataPermissionEquals() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "WebUserDataPermission.equals() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("WebUserDataPermissionEquals : FAILED");
    } else {
      TestUtil.logMsg("WebUserDataPermissionEquals : PASSED");
    }
  }

  /**
   * @testName: WebResourcePermissionHashCode
   *
   * @assertion_ids: JACC:JAVADOC:42
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of WebResourcePermission.hashCode()
   *
   *
   */
  public void WebResourcePermissionHashCode() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "WebResourcePermission.hashCode() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("WebResourcePermissionHashCode : FAILED");
    } else {
      TestUtil.logMsg("WebResourcePermissionHashCode : PASSED");
    }
  }

  /**
   * @testName: WebRoleRefPermissionHashCode
   *
   * @assertion_ids: JACC:JAVADOC:49
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of WebRoleRefPermission.hashCode()
   *
   *
   */
  public void WebRoleRefPermissionHashCode() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "WebRoleRefPermission.hashCode() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("WebRoleRefPermissionHashCode : FAILED");
    } else {
      TestUtil.logMsg("WebRoleRefPermissionHashCode : PASSED");
    }
  }

  /**
   * @testName: WebUserDataPermissionHashCode
   *
   * @assertion_ids: JACC:JAVADOC:55
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of WebUserDataPermission.hashCode()
   *
   *
   */
  public void WebUserDataPermissionHashCode() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "WebUserDataPermission.hashCode() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("WebUserDataPermissionHashCode : FAILED");
    } else {
      TestUtil.logMsg("WebUserDataPermissionHashCode : PASSED");
    }
  }

  /**
   * @testName: PolicyConfigurationFactory
   *
   * @assertion_ids: JACC:SPEC:25; JACC:SPEC:15; JACC:SPEC:63
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify PolicyConfigurationFactory is called and
   *                 instantiated in the server.
   *
   *                 Description The getPolicyConfigurationFactory method must
   *                 be used in the containers to which the application or
   *                 module are being deployed to find or instantiate
   *                 PolicyConfigurationFactory objects.
   *
   */
  public void PolicyConfigurationFactory() throws Fault {
    boolean verified = false;
    String args[] = { "PolicyConfigurationFactory instantiated" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("PolicyConfigurationFactory failed : "
          + "PolicyconfigurationFactory not instantiated");
    } else {
      TestUtil.logMsg("PolicyConfigurationFactory() instantiated");
    }
  }

  /**
   * @testName: GetPolicyConfiguration
   *
   * @assertion_ids: JACC:SPEC:26; JACC:JAVADOC:28; JACC:JAVADOC:29
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify PolicyConfigurationFactory is called and
   *                 instantiated in the server.
   *
   *                 Description The getPolicyconfiguration method of the
   *                 factory must be used to find or instantiate
   *                 PolicyConfiguration objects corresponding to the
   *                 application or modules being deployed.
   *
   */
  public void GetPolicyConfiguration() throws Fault {
    boolean verified = false;
    String args[] = {
        "PolicyConfigurationFactory.getPolicyConfiguration() invoked" };

    // verify whether the log contains required messages.
    verified = logProcessor.verifyLogContains(args);
    if (!verified) {
      throw new Fault("GetPolicyConfiguration failed : "
          + "getPolicyconfiguration() was not invoked");
    } else {
      TestUtil.logMsg(
          "PolicyConfigurationFactory.getPolicyConfiguration() invoked");
    }
  }

  /**
   * @testName: validateNoInvalidStates
   *
   * @assertion_ids: JACC:SPEC:60;
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log to
   *                 verify PolicyConfigurationFactory is called and
   *                 instantiated in the server.
   *
   *                 Description This method looks for occurances of error
   *                 message within JACCLog.txt where those error messags would
   *                 only appear in JACCLog.txt if there was a
   *                 policyConfiguration lifecycle state that was in the wrong
   *                 state at the wrong time. This can ONLY test the state for
   *                 being in the 'inService' state or not. So testing is done
   *                 to make sure the PolicyConfigration state is correct wrt
   *                 policyConfiguration.inService() for each of the methods
   *                 defined in the PolicyConfiguration javadoc table. Again,
   *                 this is not a complete validation of all states, but is
   *                 only able to validate if the state is inService or not at
   *                 each of the method calls based on the javadoc table.
   *                 Occurance of an ERROR message below would be a flag for a
   *                 method being in an incorrect state.
   */
  public void validateNoInvalidStates() throws Fault {
    boolean verified = false;
    String args1[] = {
        "ERROR - our policy config should not be in the INSERVICE state." };
    String args2[] = {
        "ERROR - our policy config should be in the INSERVICE state." };

    // verify that the log contains no errors related to the inService state
    verified = logProcessor.verifyLogContains(args1);
    if (verified) {
      // if here, then there was an error message where we were errorneously
      // caught in the inService state when we should not have been
      throw new Fault(
          "validateNoInvalidStates failed : detected error message of: "
              + args1[0]);
    } else {
      TestUtil.logMsg("validateNoInvalidStates() passed.");
    }

    verified = logProcessor.verifyLogContains(args2);
    if (verified) {
      // if here, then there was an error message where we were NOT
      // in the inService state but we should have been
      throw new Fault(
          "validateNoInvalidStates failed : detected error message of: "
              + args2[0]);
    } else {
      TestUtil.logMsg("validateNoInvalidStates() passed.");
    }

  }

  /**
   * @testName: PolicyRefresh
   *
   * @assertion_ids: JACC:SPEC:54; JACC:SPEC:5; JACC:SPEC:23
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify that TSPolicy.refresh() method is called
   *
   *                 (Note: This assertion implicitly tests JACC:SPEC:5,
   *                 JACC:SPEC:23 i.e loading provider specified interfaces by
   *                 the containers)
   *
   */
  public void PolicyRefresh() throws Fault {
    boolean verified = false;
    String tempArgs[] = { "TSPolicy.refresh() invoked" };

    // verify the log contains TSPolicy.refresh().
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("PolicyRefresh() failed");
    } else {
      TestUtil.logMsg("TSPolicy.refresh() invoked");
    }
  }

  /**
   * @testName: Policy
   *
   * @assertion_ids: JACC:SPEC:53; JACC:SPEC:56; JACC:SPEC:67; JACC:SPEC:68;
   *                 JACC:SPEC:105; JACC:SPEC:14; JACC:SPEC:22
   *
   * @test_Strategy: 1. Register TS provider with the AppServer. (See User guide
   *                 for Registering TS Provider with your AppServer ).
   *
   *                 2. Use FetchLog servlet, and verify the server side log
   *                 contains the following string "TSPolicy.refresh() invoked"
   *
   *                 3. The occurance of the above string indicates the server
   *                 used the system property
   *                 javax.security.jacc.policy.provider to instantiate and
   *                 replace the policy object used by the JRE
   */
  public void Policy() throws Fault {

    boolean verified = false;

    String args[] = { "TSPolicy.refresh() invoked" };

    // verify whether the log contains required string.
    verified = logProcessor.verifyLogContains(args);

    if (!verified) {
      throw new Fault("TestName: Policy failed : "
          + "Policy replacement algorithm not used");
    } else {
      TestUtil.logMsg("System Policy loaded based on system properties");
    }

  }
}
