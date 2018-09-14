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
 * @(#)JACCEntityBean.java	1.5 03/05/16
 *
 * @author Raja Perumal
 *         08/22/02
 */

package com.sun.ts.tests.jacc.util;

import javax.ejb.EntityBean;
import javax.ejb.CreateException;
import javax.ejb.EntityContext;
import javax.ejb.EJBException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import java.rmi.RemoteException;
import java.security.Principal;

public abstract class JACCEntityBean implements EntityBean {

  private EntityContext context;

  private static final String jndiName = "java:comp/env/ejb/JACCSession";

  private JACCSessionHome jaccSessionHome = null;

  private JACCSession jaccSession = null;

  private TSNamingContext nctx = null;

  public JACCEntityKey ejbCreate(String arg1, int arg2, long arg3)
      throws CreateException {

    setArg1(arg1);
    setArg2(new Integer(arg2));
    setArg3(new Long(arg3));
    return null;
  }

  public void ejbPostCreate(String arg1, int arg2, long arg3)
      throws CreateException {
  }

  public void setEntityContext(EntityContext ctx) {
    context = ctx;
    try {

      nctx = new TSNamingContext();

      // lookup JACCSessionHome
      jaccSessionHome = (JACCSessionHome) nctx.lookup(jndiName,
          JACCSessionHome.class);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);

      throw new EJBException("Unable to obtain naming context");
    }

  }

  public void unsetEntityContext() {
    this.context = null;
  }

  public void ejbRemove() {
  }

  public void ejbLoad() {
  }

  public void ejbStore() {
  }

  public void ejbPassivate() {
  }

  public void ejbActivate() {
  }

  // Get Arg1
  public abstract String getArg1();

  // Set Arg1
  public abstract void setArg1(String arg1);

  // Get Arg2
  public abstract Integer getArg2();

  // Set Arg2
  public abstract void setArg2(Integer arg2);

  // Get Arg3
  public abstract Long getArg3();

  // Set Arg3
  public abstract void setArg3(Long arg3);

  public boolean accessJACCSession_getCallerName() throws RemoteException {
    boolean result = false;
    String callerName = null;
    Principal principal = context.getCallerPrincipal();

    if (principal != null) {
      callerName = principal.getName();
    }

    try {

      TestUtil.logMsg("User " + callerName + " invoked "
          + "JACCEntity.accessJACCSession_getCallerName");

      // create JACCSession
      jaccSession = jaccSessionHome.create("sample", 2, 2L);

      TestUtil.logMsg("Created JACCSessionBean");

      // access getCallerName()
      String newCallerName = jaccSession.getCallerName();
      TestUtil.logMsg(
          "JACCEnitty accessed JACCSession.getCallerName as " + newCallerName);
      result = true;

    } catch (CreateException ce) {
      throw new RemoteException("couldn't create JACCSession", ce);
    } catch (Exception e) {
      throw new RemoteException("couldn't access JACCSession.getCallerName()",
          e);
    }
    return result;
  }

}
