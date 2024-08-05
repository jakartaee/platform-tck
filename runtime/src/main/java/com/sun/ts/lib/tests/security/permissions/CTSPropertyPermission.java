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

package com.sun.ts.lib.tests.security.permissions;

import java.security.BasicPermission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Permission;

import java.io.Serializable;
import java.util.Map;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Collections;
import java.util.StringTokenizer;

/**
 * This is a Java SE based Permission class, used for Test purposes. This
 * permission extends the BasicPermission class and is used to perform
 * validations of permissions.xml.
 * 
 * This permission should support actions of "read" or "write" and basically
 * tries to behave similar to the JavaSE PropertyPermission class. The reason
 * for this locally defined class is that we want to reference it in the
 * permissions.xml while ensuring it will not be used or pre-defined within any
 * of the vendors app servers. This means there will ONLY be perms defined
 * inpermissions.xml for READ actions. This will allow a different level of
 * testing to be done.
 * 
 * Sample usage of this permssion: permission
 * com.sun.ts.tests.ejb30.sec.permsxml.CTSPropertyPermission "*" "read.write";
 * 
 */

public final class CTSPropertyPermission extends BasicPermission {

  private transient String methodName;

  private transient String actions;

  private transient int hashCodeValue = 0;

  private transient boolean isReadAction = false;

  private transient boolean isWriteAction = false;

  protected int mask = 0;

  static private int READ = 0x01;

  static private int WRITE = 0x02;

  /**
   * Create a new CTSPropertyPermission with no action
   *
   * @param name
   *          -
   */
  public CTSPropertyPermission(String name) {
    this(name, "READ");
  }

  /**
   * Creates a new CTSPropertyPermission with action
   *
   * @param name
   *          JNDI resource path name
   * @param action
   *          JNDI action (none defined)
   */
  public CTSPropertyPermission(String name, String actions) {
    super(name);
    this.setActions(actions);
    this.mask = parseActions(actions);
  }

  private int parseActions(String action) {

    // we support actions separated by a commona ',' or a space ' '
    StringTokenizer st = new StringTokenizer(action, ", ");

    mask = 0;
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (token.equals("READ")) {
        mask |= READ;
      } else if (token.equals("WRITE")) {
        mask |= WRITE;
      } else {
        // ignore unrecognize actions and assume READ
        mask |= READ;
      }
    }
    return mask;
  }

  public int getMask() {
    return mask;
  }

  public boolean equals(Object o) {
    if (o == null || !(o instanceof CTSPropertyPermission))
      return false;

    CTSPropertyPermission that = (CTSPropertyPermission) o;

    if (!this.getName().equals(that.getName()))
      return false;

    if (this.methodName != null) {
      if (that.methodName == null || !this.methodName.equals(that.methodName)) {
        return false;
      }
    } else if (that.methodName != null) {
      return false;
    }

    if (this.actions != null) {
      if (that.actions == null || !this.actions.equals(that.actions)) {
        return false;
      }
    } else if (that.actions != null) {
      return false;
    }

    if (that.mask != this.mask) {
      return false;
    }

    return true;
  }

  public static String getActions(int mask) {
    if (mask == 0) {
      return "";
    } else if (mask == READ) {
      return "READ";
    } else if (mask == WRITE) {
      return "WRITE";
    } else if (mask == (READ | WRITE)) {
      return "READ,WRITE";
    } else {
      // something went wrong
      System.out.println("in getActions():  bad value for mask = " + mask);
      return "ZZZ";
    }
  }

  public void setActions(String val) {
    this.actions = val;

    this.actions = this.actions.toUpperCase();
    if (this.actions.contains("READ")) {
      isReadAction = true;
    }

    if (this.actions.contains("WRITE")) {
      isWriteAction = true;
    }

  }

  public String getMethodName() {
    return this.methodName;
  }

  public void setMethodName(String val) {
    this.methodName = val;
  }

  public int hashCode() {
    if (hashCodeValue == 0) {

      String hash = this.getName();
      if (this.actions != null) {
        hash += " " + this.actions;
      }
      hashCodeValue = hash.hashCode();
    }
    return this.hashCodeValue;
  }

  public boolean implies(Permission permission) {

    debug("enterred CTSPropertyPermission.implies()");

    CTSPropertyPermission that = (CTSPropertyPermission) permission;

    if ((permission == null)
        || !(permission instanceof CTSPropertyPermission)) {
      debug("in implies():  permission ! instance of CTSPropertyPermission");
      return false;
    }

    if ((!this.getName().equals("*"))
        && (!this.getName().equals(that.getName()))) {
      debug("in implies():  this.getName() != that.getName()");
      return false;
    }

    if ((this.mask & that.mask) != that.mask) {
      debug("in implies():  masks not equal");
      return false;
    }

    if ((this.methodName != null) && (that.methodName == null
        || !this.methodName.equals(that.methodName))) {
      debug("that.methodName = " + that.methodName);
      debug("this.methodName = " + this.methodName);
      return false;
    }

    String strName = that.getName();
    debug("in implies():  strName = " + strName);
    debug("in implies():  this.actions() = " + this.getActions());
    debug("in implies():  that.actions() = " + that.getActions());

    if (this.getIsReadAction() != that.getIsReadAction()) {
      debug("in implies(): this.getIsReadAction() != that.getIsReadAction()");
      return false;
    }

    if (this.getIsWriteAction() != that.getIsWriteAction()) {
      debug("in implies(): this.getIsWriteAction() != that.getIsWriteAction()");
      return false;
    }

    return true;
  }

  public PermissionCollection newPermissionCollection() {
    return new CTSPropertyPermissionCollection();
  }

  public void setIsReadAction(boolean val) {
    isReadAction = val;
  }

  public boolean getIsReadAction() {
    return isReadAction;
  }

  public void setIsWriteAction(boolean val) {
    isWriteAction = val;
  }

  public boolean getIsWriteAction() {
    return isWriteAction;
  }

  private void debug(String str) {
    System.err.println(str);
  }

}

/*
 *
 */
final class CTSPropertyPermissionCollection extends PermissionCollection
    implements Serializable {
  private transient Map perms;

  public CTSPropertyPermissionCollection() {
    perms = new HashMap(32); // some default
  }

  public void add(Permission permission) {

    if (permission == null) {
      throw new IllegalArgumentException(
          "failed trying to add null permission");
    }

    if (!(permission instanceof CTSPropertyPermission)) {
      throw new IllegalArgumentException("invalid permission: " + permission);
    }

    if (isReadOnly()) {
      throw new SecurityException(
          "can't add perm to (READONLY) CTSPropertyPermission");
    }

    CTSPropertyPermission propPerm = (CTSPropertyPermission) permission;
    String permName = propPerm.getName();

    // try to get the perrm from our hashmap
    CTSPropertyPermission thePerm = (CTSPropertyPermission) perms.get(permName);
    if (thePerm != null) {
      if (thePerm.getMask() != propPerm.getMask()) {
        String actions = CTSPropertyPermission
            .getActions(thePerm.getMask() | propPerm.getMask());
        perms.put(permName, new CTSPropertyPermission(permName, actions));
      }
    } else {
      perms.put(permName, permission);
    }
  }

  public boolean implies(Permission permission) {

    CTSPropertyPermission that = (CTSPropertyPermission) permission;

    if (!(permission instanceof CTSPropertyPermission)) {
      System.out.println(
          "CTSPropertyPermissionCollection.implies():  permission ! instance of CTSPropertyPermission");
      return false;
    }

    String permName = that.getName();
    int desiredMask = that.getMask();
    int actualMask = 0;

    CTSPropertyPermission tempPerm = (CTSPropertyPermission) perms
        .get(permName);
    if (tempPerm != null) {
      actualMask |= tempPerm.getMask();
      if ((actualMask & desiredMask) == desiredMask) {
        return true;
      }
    }

    return false;
  }

  public Enumeration elements() {
    return Collections.enumeration(perms.values());
  }

}
