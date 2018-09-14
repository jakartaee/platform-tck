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

package com.sun.ts.tests.webservices.handlerEjb.HandlerFlow;

import java.rmi.RemoteException;
import java.security.Principal;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import javax.xml.rpc.handler.MessageContext;

public class HelloBean implements SessionBean {

  private boolean initCalled = false;

  private SessionContext ctx;

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

  public String hello(String s) {
    return "Hello, " + s + "!";
  }

  public String hi(String s) {
    return "Hi, " + s + "!";
  }

  public String howdy(String s) {
    return "Howdy, " + s + "!";
  }

  public String enventry(String s) {
    return "Found the expected integer value of 8 for string: " + s + "!";
  }

  public boolean wasInitCalled() {
    return initCalled;
  }

  public boolean wasDestroyCalled() {
    // Untestable so always return true;
    return true;
  }

  public boolean getHttpSessionTest() {
    boolean pass = false;
    return pass;
  }

  public boolean getMessageContextTest() {
    boolean pass = true;
    MessageContext mCtx = null;
    System.out.println("getMessageContextTest called ... ");

    try {
      if (ctx != null) {
        mCtx = ctx.getMessageContext();
        System.out.println("*** MessageContext=" + mCtx);
        if (mCtx == null)
          pass = false;
      } else {
        System.out.println("SessionContext is null");
        pass = false;
      }
    } catch (Exception e) {
      System.out.println("Exception: " + e);
      pass = false;
    }
    return pass;
  }

  public boolean getServletContextTest() {
    boolean pass = false;
    return pass;
  }

  public boolean getUserPrincipalTest() {
    boolean pass = false;
    return pass;
  }
}
