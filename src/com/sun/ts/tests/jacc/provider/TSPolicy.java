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

/**
 * $Id$
 *
 * @author Raja Perumal
 *         08/14/02
 */

package com.sun.ts.tests.jacc.provider;

import java.security.*;
import javax.security.jacc.*;
import java.net.URL;
import java.net.URI;
import java.util.logging.*;
import java.io.File;
import java.util.StringTokenizer;
import com.sun.ts.lib.util.sec.security.provider.PolicyFile;

/**
 * This is a delegating Policy Implementation class which delegates the
 * permission evaluation to vendor's policy implentation defined by
 * "vendor.javax.security.jacc.policy.provider"
 *
 * In case the vendor doesn't provide Policy implementation default jdk policy
 * will be used.
 *
 * Note: Since J2EE 1.3 appservers are not required to support JSR115, the 1.3
 * policy implementation defined by "javax.security.auth.policy.provider" will
 * not used for testing in this TCK
 */
public final class TSPolicy extends java.security.Policy {
  private java.security.Policy policy = null;

  public static final String VENDOR_POLICY_PROVIDER = "vendor.javax.security.jacc.policy.provider";

  private static ClassLoader classLoader = null;

  public static boolean POLICY_INSTALLED = false;

  public static TSLogger logger = null;

  boolean firstInvocationForJACCPermissions = true;

  boolean multipleSetPolicyAllowed = false;

  String[] tokenArray = new String[2];

  String methodInterfaceName = null;

  String methodName = null;

  // This constructor will be used by vendor AppServer
  public TSPolicy() {
    initializeTSLogger();
    if (!POLICY_INSTALLED)
      loadPolicy();

    // invoke equals and hashCode method on all JACC Permissions.
    if (firstInvocationForJACCPermissions) {
      jaccPermissionsEquals();
      jaccPermissionsHashCode();
      firstInvocationForJACCPermissions = false;
    }
  }

  /**
   * Evaluates the global policy and returns a PermissionCollection object
   * specifying the set of permissions allowed for code from the specified code
   * source.
   *
   * @param codesource
   *          the CodeSource associated with the caller. This encapsulates the
   *          original location of the code (where the code came from) and the
   *          public key(s) of its signer.
   *
   * @return the set of permissions allowed for code from <i>codesource</i>
   *         according to the policy.The returned set of permissions must be a
   *         new mutable instance and it must support heterogeneous Permission
   *         types.
   *
   */
  public PermissionCollection getPermissions(CodeSource codesource) {
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicy", "getPermissions");
    }
    // print permission collection as logger info ?
    return policy.getPermissions(codesource);
  }

  /**
   * Evaluates the global policy and returns a PermissionCollection object
   * specifying the set of permissions allowed given the characteristics of the
   * protection domain.
   *
   * @param domain
   *          the ProtectionDomain associated with the caller.
   *
   * @return the set of permissions allowed for the <i>domain</i> according to
   *         the policy.The returned set of permissions must be a new mutable
   *         instance and it must support heterogeneous Permission types.
   *
   * @see java.security.ProtectionDomain
   * @see java.security.SecureClassLoader
   * @since 1.4
   */
  public PermissionCollection getPermissions(ProtectionDomain domain) {
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicy", "getPermissions");
    }
    // print permission collection as logger info ?
    return policy.getPermissions(domain);
  }

  /**
   * Evaluates the global policy for the permissions granted to the
   * ProtectionDomain and tests whether the permission is granted.
   *
   * @param domain
   *          the ProtectionDomain to test
   * @param permission
   *          the Permission object to be tested for implication.
   *
   * @return true if "permission" is a proper subset of a permission granted to
   *         this ProtectionDomain.
   *
   * @see java.security.ProtectionDomain
   * @since 1.4
   */
  public boolean implies(ProtectionDomain domain, Permission permission) {
    // invoke policyContextKey1() only if
    // permission instanceof WebResourcePermission and
    // permission.getName().equals(/secured.jsp) // for secured.jsp
    if ((permission instanceof WebResourcePermission)
        && (permission.getName().equals("/secured.jsp"))) {
      logger.log(Level.INFO, "Calling policyContextKey1()");
      policyContextKey1();
    }

    if (permission instanceof EJBMethodPermission) {

      // Get method interface name (such as "Remote", "Home", etc..)
      // and method name from permissions.getActions() string.
      tokenArray = getTokensFromString(permission.getActions());

      // Get method interface name and methodName from tokenArray
      methodName = tokenArray[0];
      methodInterfaceName = tokenArray[1];

      // inovke policyContextKey2() and policyContextKey3() only if
      // permission.getName().equals("jacc_providerContracts_JACCEntity") and
      // methodInterfaceName.equals("Remote) and
      // methodName.equals("getArg1")
      if ((permission.getName().equals("jacc_providerContracts_JACCEntity"))
          && methodName.equals("getArg1")
          && methodInterfaceName.equals("Remote"))

      {
        logger.log(Level.INFO, "Calling policyContextKey2()");
        // policyContextKey2();
        logger.log(Level.INFO, "Calling policyContextKey3()");
        policyContextKey3();
      }
    }

    return policy.implies(domain, permission);
  }

  /**
   * Refreshes/reloads the policy configuration. The behavior of this method
   * depends on the implementation. For example, calling <code>refresh</code> on
   * a file-based policy will cause the file to be re-read.
   *
   */
  public void refresh() {
    policy.refresh();
    if (logger != null)
      logger.log(Level.INFO, "TSPolicy.refresh() invoked");
  }

  /**
   * Loads vendor's policy implementation or JDK's default policy Note:
   * LoadPolicy does not set the policy using setPolicy(policy), since setPolicy
   * method will be called by the underlying provider, this task is delegated to
   * the underlying provider.
   */
  private void loadPolicy() {
    String javaPolicy = System.getProperty(VENDOR_POLICY_PROVIDER);
    if (javaPolicy == null) {
      logger.log(Level.FINE, "Loading Default Policy");
      policy = (java.security.Policy) new PolicyFile();
      policy.refresh();
      POLICY_INSTALLED = true;
      logger.log(Level.INFO, "Default policy loaded");
    } else {
      try {
        logger.log(Level.FINE, "Loading Policy = " + javaPolicy);
        // Object obj = Class.forName(javaPolicy).newInstance();
        classLoader = TSPolicy.class.getClassLoader();
        Class clazz = classLoader.loadClass(javaPolicy);
        Object obj = (Object) clazz.newInstance();

        if (obj instanceof java.security.Policy) {
          policy = (java.security.Policy) obj;
          logger.log(Level.INFO, "vendor's policy loaded!");
          POLICY_INSTALLED = true;
        } else {
          logger.log(Level.SEVERE,
              "vendor's policy is not of type java.security.Policy");
          throw new RuntimeException(
              javaPolicy + "is not a type of java.security.Policy");
        }
      } catch (ClassNotFoundException cnfe) {
        // problem with property value or classpath
        logger.log(Level.SEVERE, "vendor's Policy instantiation error", cnfe);
        throw new RuntimeException(cnfe);
      } catch (IllegalAccessException iae) {
        // problem with policy class definition
        logger.log(Level.SEVERE, "vendor's Policy instantiation error", iae);
        throw new RuntimeException(iae);
      } catch (InstantiationException ie) {
        // problem with policy instantiation
        logger.log(Level.SEVERE, "vendor's Policy instantiation error", ie);
        throw new RuntimeException(ie);
      }
    }
  }

  private static void initializeTSLogger() {
    String logFileLocation = null;
    if (logger != null)
      return;
    else {
      try {
        logFileLocation = System.getProperty("log.file.location");
        if (logFileLocation != null) {
          logger = TSLogger.getTSLogger("jacc");
          boolean appendMode = true;

          // clean the content of JACCLog.txt if it exists
          File file = new File(logFileLocation + "/JACCLog.txt");
          if (file.exists()) {
            // delete the file, if it exists
            file.delete();
          }

          // create a new file
          System.out.println(
              "XXXX:  in initializeTSLogger() - about to create JACCLog.txt");
          FileHandler fileHandler = new FileHandler(
              logFileLocation + "/JACCLog.txt", appendMode);
          fileHandler.setFormatter(new TSXMLFormatter());
          logger.addHandler(fileHandler);
          setTSLogger(logger);
        } else {
          // use default logging mechanism
          logger = TSLogger.getTSLogger("jacc");
          setTSLogger(logger);
          logger.log(Level.SEVERE,
              "log.file.location not set: Using default logger");
        }
      } catch (Exception e) {
        throw new RuntimeException("TSLogger Initialization failed", e);
      }
    }
  }

  public static TSLogger getTSLogger() {
    return logger;
  }

  public static void setTSLogger(TSLogger lgr) {
    logger = lgr;
  }

  /**
   * testName: policyContextKey1
   *
   * @assertion_ids: JACC:SPEC:99; JACC:JAVADOC:30
   *
   * @test_Strategy: 1) call
   *                 PolicyContext.getContext("javax.servlet.http.HttpServletRequest")
   *                 2) verify the return value is an instance of
   *                 HttpServletRequest
   *
   */
  private void policyContextKey1() {
    try {
      // Get HttpServletRequest object
      Object o = PolicyContext
          .getContext("javax.servlet.http.HttpServletRequest");
      if (o instanceof javax.servlet.http.HttpServletRequest) {
        logger.log(Level.INFO, "PolicyContext.getContext() " + "test passed for"
            + "javax.servlet.http.HttpServletRequest");
        logger.log(Level.INFO, "PolicyContextKey1: PASSED");
      } else {
        logger.log(Level.INFO,
            "PolicyContext.getContext()" + "returned incorrect value for key "
                + "javax.servlet.http.HttpServletRequest");
        logger.log(Level.INFO, "PolicyContextKey1: FAILED");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "PolicyContextKey1: FAILED");
    }
  }

  /**
   * testName: policyContextKey3
   *
   * @assertion_ids: JACC:SPEC:97; JACC:JAVADOC:30
   *
   * @test_Strategy: 1) call
   *                 PolicyContext.getContext("javax.security.auth.Subject.container)
   *                 2) verify the return value is an instance of
   *                 javax.security.auth.Subject
   *
   */
  private void policyContextKey3() {
    try {
      // Get Subject
      Object o = PolicyContext
          .getContext("javax.security.auth.Subject.container");
      if (o instanceof javax.security.auth.Subject) {
        logger.log(Level.INFO, "PolicyContext.getContext() " + "test passed for"
            + "javax.security.auth.Subject.container");
        logger.log(Level.INFO, "PolicyContextKey3: PASSED");
      } else {
        logger.log(Level.INFO,
            "PolicyContext.getContext()" + "returned incorrect value for key "
                + "javax.security.auth.Subject.container");
        logger.log(Level.INFO, "PolicyContextKey3: FAILED");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "PolicyContextKey3: FAILED");
    }
  }

  /**
   * Gets the method interface name and method Name from the given permission's
   * action string
   */
  public String[] getTokensFromString(String actions) {
    String[] array = new String[2];
    StringTokenizer strtoken;

    // Get first token and the remaining string
    strtoken = new StringTokenizer(actions, ",");
    if (actions.indexOf(",") > 0) {
      // get methodName
      array[0] = strtoken.nextToken();

      if (strtoken.hasMoreTokens()) {
        // get methodInterfaceName
        array[1] = strtoken.nextToken();
      }
    }
    return array;
  }

  /**
   * testName: jaccPermissionsEquals
   *
   * assertion_ids: JACC:JAVADOC:4; JACC:JAVADOC:9; JACC:JAVADOC:43;
   * JACC:JAVADOC:47; JACC:JAVADOC:53
   *
   * test_Strategy: 1) verify EJBMethodPermission.equals() or 2) verify
   * EJBRoleRefPermission.equals() or 3) verify WebResourcePermission.equals()
   * or 4) verify WebRoleRefPermission.equals() or 5) verify
   * WebUserDataPermission.equals()
   *
   */
  private void jaccPermissionsEquals() {
    try {
      logger.log(Level.FINE, "Checking EJBMethodPermission.equals()");

      EJBMethodPermission emp = new EJBMethodPermission("DummyEJB",
          "dummyMethod,Home,String");

      // call equals method onto itself
      boolean result = emp.equals(emp);

      if (result) {
        logger.log(Level.INFO, "EJBMethodPermission.equals() : PASSED");
      } else {
        logger.log(Level.INFO, "EJBMethodPermission.equals() : FAILED");
        logger.log(Level.INFO, "Calling EJBMethodPermission.equals()"
            + " onto itself returned false");
      }

    } catch (Exception e) {
      logger.log(Level.SEVERE, "EJBMethodPermission.equals() : FAILED");
    }

    try {
      EJBRoleRefPermission errp = new EJBRoleRefPermission("DummyEJB",
          "dummyRole");
      // call equals method onto itself
      boolean result = errp.equals(errp);
      if (result) {
        logger.log(Level.INFO, "EJBRoleRefPermission.equals() : PASSED");
      } else {
        logger.log(Level.INFO, "EJBRoleRefPermission.equals() : FAILED");
        logger.log(Level.INFO, "Calling EJBRoleRefPermission.equals()"
            + " onto itself returned false");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "EJBRoleRefPermission.equals() : FAILED");
    }

    try {
      WebResourcePermission wrp = new WebResourcePermission("/dummyEntry",
          "POST");

      // call equals method onto itself
      boolean result = wrp.equals(wrp);
      if (result) {
        logger.log(Level.INFO, "WebResourcePermission.equals() : PASSED");
      } else {
        logger.log(Level.INFO, "WebResourcePermission.equals() : FAILED");
        logger.log(Level.INFO, "Calling WebResourcePermission.equals()"
            + " onto itself returned false");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "WebResourcePermission.equals() : FAILED");
    }

    try {
      WebRoleRefPermission wrrp = new WebRoleRefPermission("dummyReosource",
          "dummyRole");

      // call equals method onto itself
      boolean result = wrrp.equals(wrrp);
      if (result) {
        logger.log(Level.INFO, "WebRoleRefPermission.equals() : PASSED");
      } else {
        logger.log(Level.INFO, "WebRoleRefPermission.equals() : FAILED");
        logger.log(Level.INFO, "Calling WebRoleRefPermission.equals()"
            + " onto itself returned false");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "WebRoleRefPermission.equals() : FAILED");
    }

    try {
      WebUserDataPermission wudp = new WebUserDataPermission(
          "/dummyResource.jsp", "GET,POST:CONFIDENTIAL");

      // call equals method onto itself
      boolean result = wudp.equals(wudp);
      if (result) {
        logger.log(Level.INFO, "WebUserDataPermission.equals() : PASSED");
      } else {
        logger.log(Level.INFO, "WebUserDataPermission.equals() : FAILED");
        logger.log(Level.INFO, "Calling WebUserDataPermission.equals()"
            + " onto itself returned false");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "WebUserDataPermission.equals() : FAILED");
    }

  }

  /**
   * testName: jaccPermissionsHashCode
   *
   * assertion_ids: JACC:JAVADOC:6; JACC:JAVADOC:11; JACC:JAVADOC:42;
   * JACC:JAVADOC:49; JACC:JAVADOC:55
   *
   * test_Strategy: 1) verify EJBMethodPermission.hashCode(); or 2) verify
   * EJBRoleRefPermission.hashCode(); or 3) verify
   * WebResourcePermission.hashCode() or 4) verify
   * WebRoleRefPermission.hashCode() or 5) verify
   * WebUserDataPermission.hashCode()
   */

  private void jaccPermissionsHashCode() {
    try {
      EJBMethodPermission emp = new EJBMethodPermission("DummyEJB",
          "dummyMethod,Home,String");

      // Get EJBMethodPermission's hashcode
      int hashCode1 = emp.hashCode();

      // Get EJBMethodPermission's hashcode again
      int hashCode2 = emp.hashCode();

      if (hashCode1 == hashCode2) {
        logger.log(Level.INFO, "EJBMethodPermission.hashCode() : PASSED");
      } else {
        logger.log(Level.INFO, "EJBMethodPermission.hashCode() : FAILED");
        logger.log(Level.INFO, "EJBMethodPermission.hashCode()"
            + " returned different values within the same application.");

      }

    } catch (Exception e) {
      logger.log(Level.SEVERE, "EJBMethodPermission.hashCode() : FAILED");
    }

    try {
      EJBRoleRefPermission errp = new EJBRoleRefPermission("DummyEJB",
          "dummyRole");

      // Get EJBRoleRefPermission's hashcode
      int hashCode3 = errp.hashCode();

      // Get EJBRoleRefPermission's hashcode again
      int hashCode4 = errp.hashCode();

      if (hashCode3 == hashCode4) {
        logger.log(Level.INFO, "EJBRoleRefPermission.hashCode() : PASSED");
      } else {
        logger.log(Level.INFO, "EJBRoleRefPermission.hashCode() : FAILED");
        logger.log(Level.INFO, "EJBRoleRefPermission.hashCode()"
            + " returned different values within the same application.");
      }

    } catch (Exception e) {
      logger.log(Level.SEVERE, "EJBRoleRefPermission.hashCode() : FAILED");
    }

    try {
      WebResourcePermission wrp = new WebResourcePermission("/dummyEntry",
          "POST");

      // Get WebResourcePermission's hashcode
      int hashCode5 = wrp.hashCode();

      // Get WebResourcePermission's hashcode again
      int hashCode6 = wrp.hashCode();

      if (hashCode5 == hashCode6) {
        logger.log(Level.INFO, "WebResourcePermission.hashCode() : PASSED");
      } else {
        logger.log(Level.INFO, "WebResourcePermission.hashCode() : FAILED");
        logger.log(Level.INFO, "WebResourcePermission.hashCode()"
            + " returned different values within the same application.");
      }

    } catch (Exception e) {
      logger.log(Level.SEVERE, "WebResourcePermission.hashCode() : FAILED");
    }

    try {
      WebRoleRefPermission wrrp = new WebRoleRefPermission("dummyReosource",
          "dummyRole");

      // Get WebRoleRefPermission's hashcode
      int hashCode7 = wrrp.hashCode();

      // Get WebRoleRefPermission's hashcode again
      int hashCode8 = wrrp.hashCode();

      if (hashCode7 == hashCode8) {
        logger.log(Level.INFO, "WebRoleRefPermission.hashCode() : PASSED");
      } else {
        logger.log(Level.INFO, "WebRoleRefPermission.hashCode() : FAILED");
        logger.log(Level.INFO, "WebRoleRefPermission.hashCode()"
            + " returned different values within the same application.");
      }

    } catch (Exception e) {
      logger.log(Level.SEVERE, "WebRoleRefPermission.hashCode() : FAILED");
    }

    try {
      WebUserDataPermission wudp = new WebUserDataPermission(
          "/dummyResource.jsp", "GET,POST:CONFIDENTIAL");

      // Get WebUserDataPermission's hashcode
      int hashCode9 = wudp.hashCode();

      // Get WebUserDataPermission's hashcode again
      int hashCode10 = wudp.hashCode();

      if (hashCode9 == hashCode10) {
        logger.log(Level.INFO, "WebUserDataPermission.hashCode() : PASSED");
      } else {
        logger.log(Level.INFO, "WebUserDataPermission.hashCode() : FAILED");
        logger.log(Level.INFO, "WebUserDataPermission.hashCode()"
            + " returned different values within the same application.");
      }
    } catch (Exception e) {
      logger.log(Level.SEVERE, "WebUserDataPermission.hashCode() : FAILED");
    }
  }
}
