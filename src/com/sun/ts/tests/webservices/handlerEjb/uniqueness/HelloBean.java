/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.handlerEjb.uniqueness;

import java.rmi.RemoteException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.xml.rpc.handler.MessageContext;

public class HelloBean implements SessionBean {

  private SessionContext ctx;

  private MessageContext context;

  // public HelloBean() {}
  public void ejbCreate() {
  }

  public void ejbActivate() {
  }

  public void ejbRemove() {
  }

  public void ejbPassivate() {
  }

  public void setSessionContext(SessionContext sc) {
    ctx = sc;
  }

  public String hello() {
    context = ctx.getMessageContext();
    if (context == null)
      return "***Null MessageContext obj!";

    String s1 = (String) context.getProperty("ClientParam1");
    String s2 = (String) context.getProperty("ServerParam1");
    if (s1 == null || s1.equals(""))
      s1 = "***";
    if (s2 == null || s2.equals(""))
      s2 = "***";

    return s1 + s2;
  }
}
