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
 * @(#)CSIv2SessionBean.java	1.12 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

import javax.ejb.*;
import java.util.*;

/**
 * Session bean implementation for CSIv2 testing.
 */
public class CSIv2SessionBean implements SessionBean {
  private SessionContext context;

  private Properties hProps = null;

  public CSIv2SessionBean() {
  }

  public void ejbCreate(Properties p, boolean recording)
      throws CreateException {
    if (recording) {
      methodEJBCreateInvoke(p);
    } else {
      methodEJBCreate(p);
    }
  }

  private void methodEJBCreate(Properties p) throws CreateException {
    try {
      hProps = p;
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    }
  }

  /**
   * Logs information about a home interface invocation.
   */
  private void methodEJBCreateInvoke(Properties p) throws CreateException {
    try {
      hProps = p;
      TestUtil.init(p);
      TestUtil.logTrace("CSIv2SessionBean.ejbCreateInvoke()");
      CSIv2Log log = CSIv2Log.getLog();
      log.logStartServer();
      log.logInvocationPrincipal(context.getCallerPrincipal().getName());
      TestUtil.logMsg("CSIv2SessionBean caller principal : "
          + context.getCallerPrincipal().getName());
      log.logEndServer();
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    }

    // The calling code will expect a create exception.
    throw new CreateException("(expected result)");
  }

  /**
   * The only "important" method in this Session Bean. This implements the
   * CSIv2TestLogicInterface.
   */
  public void invoke(ArrayList chain, Properties p) {
    TestUtil.logTrace("CSIv2SessionBean.invoke()");
    CSIv2TestLogicImpl csiv2TestLogicImpl = new CSIv2TestLogicImpl();
    CSIv2Log log = CSIv2Log.getLog();
    log.logStartServer();
    log.logInvocationPrincipal(context.getCallerPrincipal().getName());
    csiv2TestLogicImpl.invoke(chain, p);
    log.logEndServer();
  }

  // Required Session Bean interfaces:
  public void ejbActivate() {
  }

  public void ejbPassivate() {
  }

  public void ejbRemove() {
  }

  public void setSessionContext(SessionContext ctx) {
    this.context = ctx;
  }
}
