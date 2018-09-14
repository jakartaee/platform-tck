/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jacc.ejb.methodperm;

import java.security.Permissions;
import javax.security.jacc.EJBMethodPermission;
import javax.security.jacc.EJBRoleRefPermission;

import java.util.Properties;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Collection;
import java.util.NoSuchElementException;

import com.sun.ts.tests.jacc.util.LogRecordEntry;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.EETest.Fault;

import com.sun.javatest.Status;
import com.sun.ts.tests.jacc.util.LogFileProcessor;
import com.sun.ts.tests.jacc.util.LogRecordEntry;

// CAUTION: *** The expected permissions constructed for various permissions
//           such as WebResourcePermission, WebRoleRefPermission,
//           WebUserDataPermission are based on the application 
//           jacc_ejb_methodperm. If the application is modified for 
//           any reason then the expected permissions should also be 
//           modified accordingly. ***
//
public class Client extends EETest {

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
          "jacc_ejb_methodperm");

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
   * @testName: EJBMethodPermissionTest
   *
   * @assertion_ids: JACC:SPEC:27; JACC:SPEC:28; JACC:SPEC:48; JACC:SPEC:47;
   *                 JACC:SPEC:81;
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
  public void EJBMethodPermissionTest() throws Fault {
    boolean verified = false;
    Permissions expectedAddToRolePermissions = new Permissions();

    // 1) retrieve server generated policy statements
    // 2) construct our expected policy statements
    // 3) verify expected policy statements with generated policy statements

    // step 1: retrieve server generated unchecked policy statements
    // Get "addToRole" WebResourcePermissions
    Permissions addToRoleEJBMethodPermissions = logProcessor
        .getSpecificPermissions(addToRolePermissions, "EJBMethodPermission");

    TestUtil.logMsg("Server generated addToRole EJBMethodPermissions");
    logProcessor.printPermissionCollection(addToRoleEJBMethodPermissions);

    // step 2: construct our expected addToRole policy statements
    // Construct the expected addToRole EJBMethodPermissions
    String[] params = {};
    expectedAddToRolePermissions
        .add(new EJBMethodPermission("jacc_ejb_methodperm_MethodPermBean",
            "protectedByRoleManager", "Remote", params));
    expectedAddToRolePermissions
        .add(new EJBMethodPermission("jacc_ejb_methodperm_MethodPermBean",
            "protectedByRoleAdminAndManager", "Remote", params));
    expectedAddToRolePermissions
        .add(new EJBMethodPermission("jacc_ejb_methodperm_MethodPermBean",
            "protectedByAnyAuthUser", "Remote", params));

    // step 3: verify expected policy statements with generated policy
    // statements
    TestUtil.logMsg("verifying unchecked policy statments:");
    verified = logProcessor.verifyLogImplies(expectedAddToRolePermissions,
        addToRoleEJBMethodPermissions);

    if (!verified) {
      throw new Fault("EJBMethodPermissionTest failed: "
          + "addToRole policy statements verification failed");
    } else {
      TestUtil.logMsg("addToRole policy statements verification successful");
    }
  }

  /**
   * @testName: EJBMethodPermissionAddToRole
   *
   * @assertion_ids: JACC:SPEC:27; JACC:SPEC:28; JACC:SPEC:48; JACC:SPEC:49;
   *                 JACC:SPEC:134;
   *
   * @test_Strategy: This will test JACC (v1.5) spec section 3.1.5.1 statement
   *                 that addToRole must be called for each role-name that
   *                 exists within method-permission. (addToRole MAY be called
   *                 for the "**" role)
   *
   */
  public void EJBMethodPermissionAddToRole() throws Fault {
    boolean bManagerFound = false;
    boolean bAdminFound = false;

    // 1) retrieve server generated (MSG_TAG) addToRole calls
    // 2) sanitize the calls so we are left with ONLY EJBMethodPermission calls
    // 3) see if we are calling addToRole for Administrator and Manager

    // step 1: retrieve server generated addToRole calls
    Collection<LogRecordEntry> records = logProcessor
        .getMsgTagRecordCollection();

    // step 2: sanitize the calls so we are left with ONLY EJBMethodPermission
    // calls
    Iterator<LogRecordEntry> iterator = records.iterator();
    while (iterator.hasNext()) {
      LogRecordEntry recordEntry = (LogRecordEntry) iterator.next();

      // Get permission type (hint, we want EJBMethodPermission)
      // the format of a MSG_TAG entry in the jacc logfile is as follows:
      // MSG_TAG :: <permission_type> :: <role-name> :: <app-context> ::
      // <permissiion_name>
      String message = recordEntry.getMessage();
      String permType = null;
      String roleName = null;
      String appContext = null;
      String permName = null;

      try {
        StringTokenizer tokens = new StringTokenizer(message, " :: ");
        if (message.indexOf(" :: ") > 0) {
          tokens.nextToken(); // get rid of MSG_TAG
          permType = tokens.nextToken(); // permission type
          TestUtil.logTrace(
              "EJBMethodPermissionAddToRole:  permType = " + permType);

          if ((permType != null) && (permType.equals("EJBMethodPermission"))) {
            roleName = tokens.nextToken(); // get role-name
            appContext = tokens.nextToken(); // app-context
            permName = tokens.nextToken(); // permission name
            TestUtil.logTrace("roleName = " + roleName);
            TestUtil.logTrace("appContext = " + appContext);
            TestUtil.logTrace("permName = " + permName);

            if ((permName != null)
                && (permName.equals("jacc_ejb_methodperm_MethodPermBean"))) {
              if ((roleName != null) && (roleName.equals("Manager"))) {
                bManagerFound = true;
              } else if ((roleName != null)
                  && (roleName.equals("Administrator"))) {
                bAdminFound = true;
              } else if ((roleName != null) && (roleName.equals("**"))) {
                // this is optional for the any-authenticated-role named "**"
                // (per spec) so log for information purposes only
                TestUtil.logMsg(
                    "EJBMethodPermission addToRole called for role '**'");
              }
              if (bManagerFound && bAdminFound) {
                // we are done so bail
                TestUtil.logMsg("bManagerFound && bAdminFound so breaking");
                break;
              }
            }
          }
        }
      } catch (NoSuchElementException ex) {
        // invalid MSG_TAG - try next one
        TestUtil
            .logMsg("Invalid MSG_TAG entry found in jacc log file: " + message);
        TestUtil.logMsg(ex.getMessage());
        iterator.next();
      }

    } // while iterator.hasNext

    // step 3: see if we are calling addToRole for Administrator and Manager
    TestUtil.logMsg("verifying addToRole policy statments:");

    if (bAdminFound && bManagerFound) {
      TestUtil.logMsg(
          "addToRole called for method-permission roles:  Administrator and Manager.");
    } else {
      throw new Fault("EJBMethodPermissionAddToRole failed: "
          + "addToRole policy statements verification failed");
    }

  }

  /**
   * @testName: EJBRoleRefPermission
   *
   * @assertion_ids: JACC:SPEC:135; JACC:SPEC:51; JACC:SPEC:52; JACC:SPEC:27;
   *                 JACC:SPEC:28;
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
  public void EJBRoleRefPermission() throws Fault {
    boolean verified = false;
    Permissions expectedAddToRolePermissions = new Permissions();

    // ---------ADDTOROLE----------//
    // 1) retrieve server generated addToRole policy statements
    // 2) construct expected addToRole policy statements
    // 3) verify expected policy statements with generated policy statements

    // Get "addToRole" WebRoleRefPermissions
    Permissions addToRoleEJBRoleRefPermissions = logProcessor
        .getSpecificPermissions(addToRolePermissions, "EJBRoleRefPermission");

    TestUtil.logMsg("Server generated addToRole EJBRoleRefPermissions");
    logProcessor.printPermissionCollection(addToRoleEJBRoleRefPermissions);

    // Construct the expected excluded EJBRoleRefPermission
    expectedAddToRolePermissions.add(new EJBRoleRefPermission(
        "jacc_ejb_methodperm_MethodPermBean", "ADMIN"));
    expectedAddToRolePermissions.add(new EJBRoleRefPermission(
        "jacc_ejb_methodperm_MethodPermBean", "Administrator"));
    expectedAddToRolePermissions.add(
        new EJBRoleRefPermission("jacc_ejb_methodperm_MethodPermBean", "MGR"));
    expectedAddToRolePermissions.add(new EJBRoleRefPermission(
        "jacc_ejb_methodperm_MethodPermBean", "Manager"));
    expectedAddToRolePermissions.add(
        new EJBRoleRefPermission("jacc_ejb_methodperm_MethodPermBean", "EMP"));
    expectedAddToRolePermissions.add(new EJBRoleRefPermission(
        "jacc_ejb_methodperm_MethodPermBean", "Employee"));
    expectedAddToRolePermissions.add(
        new EJBRoleRefPermission("jacc_ejb_methodperm_MethodPermBean", "**"));

    TestUtil.logMsg("verifying addToRole policy statments:");

    verified = logProcessor.verifyLogImplies(expectedAddToRolePermissions,
        addToRoleEJBRoleRefPermissions);

    if (!verified) {
      throw new Fault("EJBRoleRefPermission failed: "
          + "addToRole policy statements verification failed");
    } else {
      TestUtil.logMsg("addToRole policy statements verification successful");
    }
  }

  /**
   * @testName: EJBMethodPermissionEquals
   *
   * @assertion_ids: JACC:JAVADOC:4;
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of EJBMethodPermission.equals()
   *
   *
   */
  public void EJBMethodPermissionEquals() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "EJBMethodPermission.equals() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("EJBMethodPermissionEquals : FAILED");
    } else {
      TestUtil.logMsg("EJBMethodPermissionEquals : PASSED");
    }
  }

  /**
   * @testName: EJBRoleRefPermissionEquals
   *
   * @assertion_ids: JACC:JAVADOC:9;
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of EJBRoleRefPermission.equals()
   *
   *
   */
  public void EJBRoleRefPermissionEquals() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "EJBRoleRefPermission.equals() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("EJBRoleRefPermissionEquals : FAILED");
    } else {
      TestUtil.logMsg("EJBRoleRefPermissionEquals : PASSED");
    }
  }

  /**
   * @testName: EJBMethodPermissionHashCode
   *
   * @assertion_ids: JACC:JAVADOC:6;
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of EJBMethodPermission.hashCode()
   *
   */
  public void EJBMethodPermissionHashCode() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "EJBMethodPermission.hashCode() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("EJBMethodPermissionHashCode : FAILED");
    } else {
      TestUtil.logMsg("EJBMethodPermissionHashCode : PASSED");
    }
  }

  /**
   * @testName: EJBRoleRefPermissionHashCode
   *
   * @assertion_ids: JACC:JAVADOC:11;
   *
   * @test_Strategy: 1. When we deploy the applications defined in
   *                 toolsContracts, the equals() and hashcode() method will be
   *                 called on all JACC Permission classes. ( i.e
   *                 EJBMethodPermission, EJBRoleRefPermission,
   *                 WebResourcePermission, WebRoleRefPermission,
   *                 WebUserDataPermission)
   *
   *                 2. Use FetchLog servlet to read the server side log and
   *                 verify the result of EJBRoleRefPermission.hashCode()
   *
   *
   */
  public void EJBRoleRefPermissionHashCode() throws Fault {
    boolean verified = false;

    String tempArgs[] = { "EJBRoleRefPermission.hashCode() : PASSED" };

    // verify the log contains the required string.
    verified = logProcessor.verifyLogContains(tempArgs);

    if (!verified) {
      throw new Fault("EJBRoleRefPermissionHashCode : FAILED");
    } else {
      TestUtil.logMsg("EJBRoleRefPermissionHashCode : PASSED");
    }
  }

}
