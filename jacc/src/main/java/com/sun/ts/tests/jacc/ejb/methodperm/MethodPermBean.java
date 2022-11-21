/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.security.AccessControlException;

import com.sun.ts.lib.util.TestUtil;

import jakarta.ejb.Remote;
import jakarta.ejb.Stateful;

@Stateful(name = "jacc_ejb_methodperm_MethodPermBean")
@Remote({ MethodPermInterface.class })
public class MethodPermBean implements MethodPermInterface {

  /*
   *  
   */
  public boolean protectedByRoleManager() {
    boolean rval = false; // assume fail

    debug("Enterred: MethodPermBean.protectedByRoleManager().");

    try {

      // XXXX: this is a placeholder methoid thats not called

      debug("SUCCESS:  protectedByRoleManager passed.");
      rval = true;
    } catch (AccessControlException ex) {
      debug(
          "FAILURE:  protectedByRoleManager throwing AccessControlException.");
      ex.printStackTrace();
      return false;
    } catch (Exception ex) {
      debug(
          "FAILURE:  protectedByRoleManager(), throwing unexpected Exception.");
      ex.printStackTrace();
      return false;
    }

    debug(
        "Leaving MethodPermBean.protectedByRoleManager() with rval = " + rval);

    return rval;
  }

  /*
   *  
   */
  public boolean protectedByRoleAdminAndManager() {
    boolean rval = false; // assume fail

    debug("Enterred: MethodPermBean.protectedByRoleAdminAndManager().");

    try {

      // XXXX: this is a placeholder methoid thats not called

      debug("SUCCESS:  protectedByRoleAdminAndManager passed.");
      rval = true;
    } catch (AccessControlException ex) {
      debug(
          "FAILURE:  protectedByRoleAdminAndManager throwing AccessControlException.");
      ex.printStackTrace();
      return false;
    } catch (Exception ex) {
      debug(
          "FAILURE:  protectedByRoleAdminAndManager(), throwing unexpected Exception.");
      ex.printStackTrace();
      return false;
    }

    debug("Leaving MethodPermBean.protectedByRoleAdminAndManager() with rval = "
        + rval);

    return rval;
  }

  /*
   *  
   */
  public boolean protectedByAnyAuthUser() {
    boolean rval = false; // assume fail

    debug("Enterred: MethodPermBean.protectedByAnyAuthUser().");

    try {

      // XXXX: this is a placeholder methoid thats not called

      debug("SUCCESS:  protectedByAnyAuthUser passed.");
      rval = true;
    } catch (AccessControlException ex) {
      debug(
          "FAILURE:  protectedByAnyAuthUser throwing AccessControlException.");
      ex.printStackTrace();
      return false;
    } catch (Exception ex) {
      debug(
          "FAILURE:  protectedByAnyAuthUser(), throwing unexpected Exception.");
      ex.printStackTrace();
      return false;
    }

    debug(
        "Leaving MethodPermBean.protectedByAnyAuthUser() with rval = " + rval);

    return rval;
  }

  private void debug(String str) {
    System.out.println(str);
    TestUtil.logMsg(str);
  }

}
