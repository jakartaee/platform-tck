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
 * 
 * @author Raja Perumal
 */

package com.sun.ts.tests.jacc.provider;

import javax.security.jacc.*;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.Date;
import java.security.*;
import javax.security.auth.Subject;
import java.io.*;
import java.lang.StringBuffer;
import java.util.logging.*;
import java.util.Map;
import java.util.Vector;

/**
 * Jacc PolicyConfiguration delegates the policy configuration tasks to vendor's
 * PolicyConfiguration implementation class.
 *
 */
public class TSPolicyConfigurationImpl implements PolicyConfiguration {
  private PolicyConfiguration policyConfiguration = null;

  private PolicyConfigurationFactory pcf = null;

  private static TSLogger logger = null;

  private String applicationContext = null;

  private String appTime = null;

  private Vector applicationLinkTable = new Vector();

  public TSPolicyConfigurationImpl(String contextId, boolean remove,
      TSLogger lgr) throws PolicyContextException {
    Date date = new Date();
    appTime = "" + date.getTime();
    logger = lgr;

    // Add timeStamp to the contextId,
    applicationContext = contextId;

    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "TSPolicyConfigurationImpl");
    }

    // set vendor's PolicyConfigurationFactory
    pcf = TSPolicyConfigurationFactoryImpl.getPolicyConfigurationFactory();

    // **** This covers two assertions JACC:SPEC:33 and JACC:SPEC:56 ****
    // set vendor's PolicyConfiguration
    policyConfiguration = pcf.getPolicyConfiguration(contextId, remove);

    // This(appId record) will be used as an identifier
    // for isolating the logs associated with each test run.
    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO,
          "appId :: " + stuffData(applicationContext) + " , " + appTime);
    }

    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "TSPolicyConfigurationImpl");
    }
  }

  /**
   * This method returns this object's policy context identifier.
   * 
   * @return this object's policy context identifier.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the getContextID method signature. The
   *           exception thrown by the implementation class will be encapsulated
   *           (during construction) in the thrown PolicyContextException.
   */
  public String getContextID() throws PolicyContextException {

    boolean bWasInservice = policyConfiguration.inService();

    String contextId = policyConfiguration.getContextID();

    // if the state was inService for our getContextID call, then it must remain
    // in that state as its next transitional state (per javadoc table)
    if (bWasInservice) {
      assertIsInserviceState("getContextID");
    } else {
      // state was not inService so make sure it is still NOT in the
      // inService state after calling policyConfiguration.getContextID()
      assertStateNotInservice("getContextID");
    }

    if (logger.isLoggable(Level.FINER)) {
      logger.log(Level.FINER, "contextId =" + contextId);
    }
    return contextId;
  }

  /**
   * Used to add permissions to a named role in this PolicyConfiguration. If the
   * named Role does not exist in the PolicyConfiguration, it is created as a
   * result of the call to this function.
   * <P>
   * It is the job of the Policy provider to ensure that all the permissions
   * added to a role are granted to principals "mapped to the role".
   * <P>
   * 
   * @param roleName
   *          the name of the Role to which the permissions are to be added.
   *          <P>
   * @param permissions
   *          the collection of permissions to be added to the role. The
   *          collection may be either a homogenous or heterogenous collection.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the addToRole method signature. The
   *           exception thrown by the implementation class will be encapsulated
   *           (during construction) in the thrown PolicyContextException.
   */
  public void addToRole(String roleName, PermissionCollection permissions)
      throws PolicyContextException {
    String permissionType = null;
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "addToRole");
    }
    if (roleName == null || permissions == null)
      return;

    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);
    policyConfiguration.addToRole(roleName, permissions);
    assertStateNotInservice("addToRole");

    if (logger.isLoggable(Level.INFO)) {
      StringBuffer sbuf = new StringBuffer("");
      int bufLength = 0;
      for (Enumeration en = permissions.elements(); en.hasMoreElements();) {
        sbuf.append("addToRole :: ");
        sbuf.append(applicationContext + " , ");
        sbuf.append(appTime + " , ");
        Permission p = (Permission) en.nextElement();

        // Get the permission type,
        // valid permission types are
        // 1) WebResourcePermission
        // 2) WebUserDataPermission
        // 3) WebRoleRefPermission
        // 4) EJBMethodPermission
        // 5) EJBRoleRefPermission
        permissionType = getPermissionType(p);
        sbuf.append(permissionType + " , ");
        sbuf.append(p.getName() + " , ");
        sbuf.append(p.getActions());
        logger.log(Level.INFO, sbuf.toString());
        // Re-initialize string buffer.
        bufLength = sbuf.length();
        sbuf.delete(0, bufLength);

        if (permissionType.equals("WebResourcePermission")
            || permissionType.equals("WebRoleRefPermission")
            || permissionType.equals("EJBMethodPermission")
            || permissionType.equals("EJBRoleRefPermission")) {
          // logged so we can check roleNames and resourcePerms
          logger.log(Level.INFO, "MSG_TAG :: " + permissionType + " :: "
              + roleName + " :: " + applicationContext + " :: " + p.getName());
        } else {
          // logger.log(Level.INFO, "MSG_TAG :: nothing logged for " +
          // permissionType + " :: " + roleName + " :: " + applicationContext +
          // " :: " + p.getName());
        }

      }
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "addToRole");
    }
  }

  /**
   * Used to add a single permission to a named role in this
   * PolicyConfiguration. If the named Role does not exist in the
   * PolicyConfiguration, it is created as a result of the call to this
   * function.
   * <P>
   * It is the job of the Policy provider to ensure that all the permissions
   * added to a role are granted to principals "mapped to the role".
   * <P>
   * 
   * @param roleName
   *          the name of the Role to which the permission is to be added.
   *          <P>
   * @param permission
   *          the permission to be added to the role.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the addToRole method signature. The
   *           exception thrown by the implementation class will be encapsulated
   *           (during construction) in the thrown PolicyContextException.
   */
  public void addToRole(String roleName, Permission permission)
      throws PolicyContextException {
    String permissionType = null;
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "addToRole");
    }
    if (roleName == null || permission == null)
      return;

    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);

    policyConfiguration.addToRole(roleName, permission);
    assertStateNotInservice("addToRole");

    // Get the permission type,
    // valid permission types are
    // 1) WebResourcePermission
    // 2) WebUserDataPermission
    // 3) WebRoleRefPermission
    // 4) EJBMethodPermission
    // 5) EJBRoleRefPermission
    permissionType = getPermissionType(permission);
    if (logger.isLoggable(Level.INFO)) {
      String sbuf = new String("addToRole :: " + applicationContext + " , "
          + appTime + " , " + permissionType + " , " + permission.getName()
          + " , " + permission.getActions());
      logger.log(Level.INFO, sbuf);
    }

    if (permissionType.equals("WebResourcePermission")
        || permissionType.equals("WebRoleRefPermission")
        || permissionType.equals("EJBMethodPermission")
        || permissionType.equals("EJBRoleRefPermission")) {
      // logged so we can check roleNames and resourcePerms
      logger.log(Level.INFO, "MSG_TAG :: " + permissionType + " :: " + roleName
          + " :: " + applicationContext + " :: " + permission.getName());
    } else {
      // logger.log(Level.INFO, "MSG_TAG :: nothing logged for " +
      // permissionType + " :: " + roleName + " :: " + applicationContext + " ::
      // " + permission.getName());
    }

    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "addToRole");
    }
  }

  /**
   * Used to add unchecked policy statements to this PolicyConfiguration.
   * <P>
   * 
   * @param permissions
   *          the collection of permissions to be added as unchecked policy
   *          statements. The collection may be either a homogenous or
   *          heterogenous collection.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the addToUncheckedPolicy method signature.
   *           The exception thrown by the implementation class will be
   *           encapsulated (during construction) in the thrown
   *           PolicyContextException.
   */
  public void addToUncheckedPolicy(PermissionCollection permissions)
      throws PolicyContextException {
    String permissionType = null;
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "addToUncheckedPolicy");
    }
    if (permissions == null)
      return;
    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);
    policyConfiguration.addToUncheckedPolicy(permissions);
    assertStateNotInservice("addToUncheckedPolicy");

    if (logger.isLoggable(Level.INFO)) {
      StringBuffer sbuf = new StringBuffer("");
      int bufLength = 0;

      for (Enumeration en = permissions.elements(); en.hasMoreElements();) {
        sbuf.append("unchecked :: ");
        sbuf.append(applicationContext + " , ");
        sbuf.append(appTime + " , ");

        Permission p = (Permission) en.nextElement();
        // Get the permission type,
        // valid permission types are
        // 1) WebResourcePermission
        // 2) WebUserDataPermission
        // 3) WebRoleRefPermission
        // 4) EJBMethodPermission
        // 5) EJBRoleRefPermission
        permissionType = getPermissionType(p);

        sbuf.append(permissionType + " , ");
        sbuf.append(p.getName() + " , ");
        sbuf.append(p.getActions());

        logger.log(Level.INFO, sbuf.toString());
        // Re-initialize string buffer
        bufLength = sbuf.length();
        sbuf.delete(0, bufLength);
      }
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "addToUncheckedPolicy");
    }
  }

  /**
   * Used to add a single unchecked policy statement to this
   * PolicyConfiguration.
   * <P>
   * 
   * @param permission
   *          the permission to be added to the unchecked policy statements.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the addToUncheckedPolicy method signature.
   *           The exception thrown by the implementation class will be
   *           encapsulated (during construction) in the thrown
   *           PolicyContextException.
   */
  public void addToUncheckedPolicy(Permission permission)
      throws PolicyContextException {
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "addToUncheckedPolicy");
    }

    if (permission == null)
      return;

    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);
    policyConfiguration.addToUncheckedPolicy(permission);
    assertStateNotInservice("addToUncheckedPolicy");

    // Get the permission type,
    // valid permission types are
    // 1) WebResourcePermission
    // 2) WebUserDataPermission
    // 3) WebRoleRefPermission
    // 4) EJBMethodPermission
    // 5) EJBRoleRefPermission
    String permissionType = getPermissionType(permission);
    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO,
          "unchecked :: " + applicationContext + " , " + appTime + " , "
              + permissionType + " , " + permission.getName() + " , "
              + permission.getActions());
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "addToUncheckedPolicy");
    }
  }

  /**
   * Used to add excluded policy statements to this PolicyConfiguration.
   * <P>
   * 
   * @param permissions
   *          the collection of permissions to be added to the excluded policy
   *          statements. The collection may be either a homogenous or
   *          heterogenous collection.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the addToExcludedPolicy method signature.
   *           The exception thrown by the implementation class will be
   *           encapsulated (during construction) in the thrown
   *           PolicyContextException.
   */
  public void addToExcludedPolicy(PermissionCollection permissions)
      throws PolicyContextException {
    String permissionType = null;
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "addToExcludedPolicy");
    }
    if (permissions == null)
      return;

    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);
    policyConfiguration.addToExcludedPolicy(permissions);
    assertStateNotInservice("addToExcludedPolicy");

    if (logger.isLoggable(Level.INFO)) {
      StringBuffer sbuf = new StringBuffer("");
      int bufLength = 0;
      for (Enumeration en = permissions.elements(); en.hasMoreElements();) {
        sbuf.append("excluded :: ");
        sbuf.append(applicationContext + " , ");
        sbuf.append(appTime + " , ");
        Permission p = (Permission) en.nextElement();

        // Get the permission type,
        // valid permission types are
        // 1) WebResourcePermission
        // 2) WebUserDataPermission
        // 3) WebRoleRefPermission
        // 4) EJBMethodPermission
        // 5) EJBRoleRefPermission
        permissionType = getPermissionType(p);
        sbuf.append(permissionType + " , ");
        sbuf.append(p.getName() + " , ");
        sbuf.append(p.getActions());
        logger.log(Level.INFO, sbuf.toString());
        // Re-initialize string buffer.
        bufLength = sbuf.length();
        sbuf.delete(0, bufLength);
      }
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "addToExcludedPolicy");
    }
  }

  /**
   * Used to add a single excluded policy statement to this PolicyConfiguration.
   * <P>
   * 
   * @param permission
   *          the permission to be added to the excluded policy statements.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the addToExcludedPolicy method signature.
   *           The exception thrown by the implementation class will be
   *           encapsulated (during construction) in the thrown
   *           PolicyContextException.
   */
  public void addToExcludedPolicy(Permission permission)
      throws PolicyContextException {
    String permissionType = null;
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "addToExcludedPolicy");
    }
    if (permission == null)
      return;

    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);
    policyConfiguration.addToExcludedPolicy(permission);
    assertStateNotInservice("addToExcludedPolicy");

    // Get the permission type,
    // valid permission types are
    // 1) WebResourcePermission
    // 2) WebUserDataPermission
    // 3) WebRoleRefPermission
    // 4) EJBMethodPermission
    // 5) EJBRoleRefPermission
    permissionType = getPermissionType(permission);
    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO,
          "excluded :: " + applicationContext + " , " + appTime + " , "
              + permissionType + " , " + permission.getName() + " , "
              + permission.getActions());
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "addToExcludedPolicy");
    }
  }

  /**
   * Used to remove a role and all its permissions from this
   * PolicyConfiguration.
   * <P>
   * 
   * @param roleName
   *          the name of the Role to remove from this PolicyConfiguration.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the removeRole method signature. The
   *           exception thrown by the implementation class will be encapsulated
   *           (during construction) in the thrown PolicyContextException.
   */
  public void removeRole(String roleName) throws PolicyContextException {
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "removeRole");
    }
    if (roleName == null)
      return;

    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);
    policyConfiguration.removeRole(roleName);
    assertStateNotInservice("removeRole");

    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO, "Removed Role :: " + roleName);
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "removeRole");
    }
  }

  /**
   * Used to remove any unchecked policy statements from this
   * PolicyConfiguration.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the removeUncheckedPolicy method signature.
   *           The exception thrown by the implementation class will be
   *           encapsulated (during construction) in the thrown
   *           PolicyContextException.
   */
  public void removeUncheckedPolicy() throws PolicyContextException {
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "removeUncheckedPolicy");
    }

    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);
    policyConfiguration.removeUncheckedPolicy();
    assertStateNotInservice("removeUncheckedPolicy");

    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO, "Removed all unchecked policy statements");
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "removeUncheckedPolicy");
    }
  }

  /**
   * Used to remove any excluded policy statements from this
   * PolicyConfiguration.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the removeExcludedPolicy method signature.
   *           The exception thrown by the implementation class will be
   *           encapsulated (during construction) in the thrown
   *           PolicyContextException.
   */
  public void removeExcludedPolicy() throws PolicyContextException {
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "removeExcludedPolicy");
    }

    policyConfiguration = pcf.getPolicyConfiguration(applicationContext, false);
    policyConfiguration.removeExcludedPolicy();
    assertStateNotInservice("removeExcludedPolicy");

    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO, "Removed all excluded policy statements");
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "removeExcludedPolicy");
    }
  }

  /**
   * This method is used to set to "inService" the state of the policy context
   * whose interface is this PolicyConfiguration Object. Only those policy
   * contexts whose state is "inService" will be included in the policy contexts
   * processed by the Policy.refresh method. A policy context whose state is
   * "inService" may be returned to the "open" state by calling the
   * getPolicyConfiguration method of the PolicyConfiguration factory with the
   * policy context identifier of the policy context.
   * <P>
   * When the state of a policy context is "inService", calling any method other
   * than commit, delete, getContextID, or inService on its PolicyConfiguration
   * Object will cause an UnsupportedOperationException to be thrown.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" when this method is
   *           called.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the commit method signature. The exception
   *           thrown by the implementation class will be encapsulated (during
   *           construction) in the thrown PolicyContextException.
   */
  public void commit() throws PolicyContextException {
    // Date date= new Date();
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "commit");
    }

    boolean bWasInservice = policyConfiguration.inService();
    policyConfiguration.commit();

    assertIsInserviceState("commit");

    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO, "PolicyConfiguration.commit() called");
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "commit");
    }
  }

  /**
   * Creates a relationship between this configuration and another such that
   * they share the same principal-to-role mappings. PolicyConfigurations are
   * linked to apply a common principal-to-role mapping to multiple seperately
   * manageable PolicyConfigurations, as is required when an application is
   * composed of multiple modules.
   * <P>
   * Note that the policy statements which comprise a role, or comprise the
   * excluded or unchecked policy collections in a PolicyConfiguration are
   * unaffected by the configuration being linked to another.
   * <P>
   * 
   * @param link
   *          a reference to a different PolicyConfiguration than this
   *          PolicyConfiguration.
   *          <P>
   *          The relationship formed by this method is symetric, transitive and
   *          idempotent. If the argument PolicyConfiguration does not have a
   *          different Policy context identifier than this PolicyConfiguration
   *          no relationship is formed, and an exception, as described below,
   *          is thrown.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws java.lang.UnsupportedOperationException
   *           if the state of the policy context whose interface is this
   *           PolicyConfiguration Object is "deleted" or "inService" when this
   *           method is called.
   *
   * @throws java.lang.IllegalArgumentException
   *           if called with an argument PolicyConfiguration whose Policy
   *           context is equivalent to that of this PolicyConfiguration.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the linkConfiguration method signature. The
   *           exception thrown by the implementation class will be encapsulated
   *           (during construction) in the thrown PolicyContextException.
   */
  public void linkConfiguration(PolicyConfiguration link)
      throws PolicyContextException {
    String contextId = null;
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "linkConfiguration");
    }

    if (link != null) {
      contextId = link.getContextID();

      // Check if the application name already exists
      if (applicationLinkTable != null) {

        // Now add the contextId that is being linked to applicationLinkTable
        if (!applicationLinkTable.contains(contextId)) {
          applicationLinkTable.add(contextId);
        }

        // Now add this.applicationContextId to applicationLinkTable
        if (!applicationLinkTable.contains(applicationContext)) {
          applicationLinkTable.add(applicationContext);
        }
      }

      if (logger.isLoggable(Level.INFO)) {
        StringBuffer sbuf = new StringBuffer("");
        int bufLength = 0;
        sbuf.append("link :: ");
        for (Enumeration appEnum = applicationLinkTable.elements(); appEnum
            .hasMoreElements();) {
          String stuffedAppName = stuffData((String) appEnum.nextElement());
          sbuf.append(stuffedAppName);
          if (appEnum.hasMoreElements())
            sbuf.append(",");
        }
        sbuf.append(" : ");
        sbuf.append(appTime);

        // Log all the linked application names
        logger.log(Level.INFO, sbuf.toString());
      }
    }

    // policyConfiguration.linkConfiguration(link);
    // Note:
    // The Passed varibale "link" may be an instance of
    // delegating Provider's PolicyConfiguration, so before
    // linking it with Vendor's PolicyConfiguration, first
    // get the vendor's equivalent PolicyConfiguration from "link"

    // get contextId from link
    String vendorContextId = link.getContextID();

    // get vendor's Policy configuration from "link"
    // Note: pcf is vendor's PolicyConfigurationFactory
    PolicyConfiguration vendorPC = pcf.getPolicyConfiguration(vendorContextId,
        false);

    // Now link the vendor's PolicyConfiguration

    boolean bWasInservice = policyConfiguration.inService();
    policyConfiguration.linkConfiguration(vendorPC);

    assertStateNotInservice("linkConfiguration");

    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "linkConfiguration");
    }
  }

  /**
   * Causes all policy statements to be deleted from this PolicyConfiguration
   * and sets its internal state such that calling any method, other than
   * delete, getContextID, or inService on the PolicyConfiguration will be
   * rejected and cause an UnsupportedOperationException to be thrown.
   * <P>
   * This operation has no affect on any linked PolicyConfigurations other than
   * removing any links involving the deleted PolicyConfiguration.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the delete method signature. The exception
   *           thrown by the implementation class will be encapsulated (during
   *           construction) in the thrown PolicyContextException.
   */
  public void delete() throws PolicyContextException {
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "delete");
    }

    policyConfiguration.delete();
    assertStateNotInservice("delete");

    if (logger.isLoggable(Level.INFO)) {
      logger.log(Level.INFO, "Deleted all policy statements");
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "delete");
    }
  }

  /**
   * This method is used to determine if the policy context whose interface is
   * this PolicyConfiguration Object is in the "inService" state.
   *
   * @return true if the state of the associated policy context is "inService";
   *         false otherwise.
   *
   * @throws java.lang.SecurityException
   *           if called by an AccessControlContext that has not been granted
   *           the "setPolicy" SecurityPermission.
   *
   * @throws javax.security.jacc.PolicyContextException
   *           if the implementation throws a checked exception that has not
   *           been accounted for by the inService method signature. The
   *           exception thrown by the implementation class will be encapsulated
   *           (during construction) in the thrown PolicyContextException.
   */
  public boolean inService() throws PolicyContextException {
    if (logger.isLoggable(Level.FINER)) {
      logger.entering("TSPolicyConfigurationImpl", "inService");
    }

    boolean bWasInservice = policyConfiguration.inService();

    boolean ret = policyConfiguration.inService();

    // if the state was inService befor our policyConfiguration.inService()
    // call, then it must remain in that state as its next transitional
    // state (per javadoc table)
    if (bWasInservice) {
      assertIsInserviceState("inService");
    } else {
      // state was not inService so must've been OPEN or DELETED but
      // either way, the next state must remain the same so we know
      // we should NOT trasition to different state
      assertStateNotInservice("inService");
    }

    if (logger.isLoggable(Level.FINE)) {
      logger.log(Level.FINE, "PolicyConfiguration.inService() called");
    }
    if (logger.isLoggable(Level.FINER)) {
      logger.exiting("TSPolicyConfigurationImpl", "inService");
    }
    return ret;
  }

  public String getPermissionType(Permission permission) {
    if (permission instanceof WebResourcePermission)
      return "WebResourcePermission";
    else if (permission instanceof WebUserDataPermission)
      return "WebUserDataPermission";
    else if (permission instanceof WebRoleRefPermission)
      return "WebRoleRefPermission";
    else if (permission instanceof EJBMethodPermission)
      return "EJBMethodPermission";
    else if (permission instanceof EJBRoleRefPermission)
      return "EJBRoleRefPermission";
    else
      return null;
  }

  // This method process the input string and stuffs the character twice if
  // the processed character is not an alphabet
  public static String stuffData(String inputStr) {

    char[] outStr = new char[2048];
    char[] str = inputStr.toCharArray();
    for (int i = 0, j = 0; i < str.length; i++) {
      int a = (new Character(str[i])).getNumericValue(str[i]);

      // Don't stuff extra character if the character is an alphabet
      //
      // Numeric values for alphabets falls in 10 to 35, this includes
      // both upper and lower cases
      if ((a > 9) && (a < 36)) {
        outStr[j++] = str[i];

      } else { // stuff the character
        outStr[j++] = str[i];
        outStr[j++] = str[i];
      }
    }
    return ((new String(outStr)).trim());
  }

  private void assertIsInserviceState(String callingMethod) {

    try {
      if (!policyConfiguration.inService()) {
        String msg1 = "ERROR - our policy config should be in the INSERVICE state.";
        String msg2 = "In the wrong state after having called:  "
            + callingMethod;
        debugOut(msg1);
        debugOut(msg2);
        logger.log(Level.SEVERE, msg1);
      }
    } catch (SecurityException ex) {
      String err = "ERROR - got securityException calling policyConfiguration.inService().";
      err += "  You likely need to have 'setPolicy' grant set";
      debugOut(err);
      debugOut(ex.toString());
    } catch (Exception ex) {
      debugOut("ERROR - Exception calling policyConfiguration.inService():  "
          + ex.toString());
      ex.printStackTrace();
    }
  }

  private void assertStateNotInservice(String callingMethod) {

    try {
      if (policyConfiguration.inService()) {
        String msg1 = "ERROR - our policy config should not be in the INSERVICE state.";
        String msg2 = "In the wrong state after having called:  "
            + callingMethod;
        debugOut(msg1);
        debugOut(msg2);
        logger.log(Level.SEVERE, msg1);
      }
    } catch (SecurityException ex) {
      String err = "ERROR - got securityException calling policyConfiguration.inService().";
      err += "  You likely need to have 'setPolicy' grant set";
      debugOut(err);
      debugOut(ex.toString());
    } catch (Exception ex) {
      debugOut("ERROR - Exception calling policyConfiguration.inService():  "
          + ex.toString());
      ex.printStackTrace();
    }
  }

  private void debugOut(String str) {
    System.out.println(str);
  }

}
