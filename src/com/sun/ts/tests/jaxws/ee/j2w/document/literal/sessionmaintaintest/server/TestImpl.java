/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 */
package com.sun.ts.tests.jaxws.ee.j2w.document.literal.sessionmaintaintest.server;

import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.Resource;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import jakarta.xml.ws.handler.MessageContext;

import java.lang.reflect.*;

import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.WebServiceContext;

@WebService(name = "Test", serviceName = "TestService", targetNamespace = "http://test.org/wsdl")
public class TestImpl {

  @Resource
  private WebServiceContext wsc;

  public TestImpl() {
  }

  @WebMethod
  public String getSessionId() {
    System.out.println("Entering getSessionId()");
    String id = getClientId();
    System.out.println("** session id: " + id);
    System.out.println("Leaving getSessionId()");
    return id;
  }

  @WebMethod
  public boolean compareSessionId(String sessionId) {
    System.out.println("Enter  doesSessionExist()");
    System.out.println("session id passed in by client: " + sessionId);
    String id = getClientId();
    System.out.println("** current session id: " + id);
    boolean exists = false;
    if (id.equals(sessionId)) {
      exists = true;
    } else {
      exists = false;
    }
    System.out.println("returning result of:" + exists);
    System.out.println("Leave doesSessionExist()");
    return exists;
  }

  private String getClientId() {
    String id = null;

    Object o1 = wsc.getMessageContext().get(MessageContext.SERVLET_REQUEST);

    try {
      if (o1 != null) {
        Class c1 = o1.getClass();
        Method getSession = c1.getMethod("getSession");
        Object o2 = getSession.invoke(o1);
        Class c2 = o2.getClass();
        Method getId = c2.getMethod("getId");
        id = (String) getId.invoke(o2);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return id;
  }

}
