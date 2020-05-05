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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.httpservletmsgctxpropstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.Resource;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;

import java.lang.reflect.*;

import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.WebServiceContext;

@WebService(portName = "HttpTestPort", serviceName = "HttpTestService", targetNamespace = "http://httptestservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JRLHttpServletMsgCtxPropsTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.httpservletmsgctxpropstest.HttpTest")
public class HttpTestImpl implements HttpTest {

  @Resource
  private WebServiceContext wsc;

  public HttpTestImpl() {
    System.out.println("WebServiceContext wsc=" + wsc);
  }

  public void testServletProperties() {
    System.out.println("Enter testServletProperties()");
    MessageContext ctxt = wsc.getMessageContext();
    System.out.println("MessageContext.SERVLET_REQUEST="
        + ctxt.get(MessageContext.SERVLET_REQUEST));
    System.out.println("MessageContext.SERVLET_RESPONSE="
        + ctxt.get(MessageContext.SERVLET_RESPONSE));
    System.out.println("MessageContext.SERVLET_CONTEXT="
        + ctxt.get(MessageContext.SERVLET_CONTEXT));
    if (ctxt.get(MessageContext.SERVLET_REQUEST) == null
        || ctxt.get(MessageContext.SERVLET_RESPONSE) == null
        || ctxt.get(MessageContext.SERVLET_CONTEXT) == null) {
      throw new WebServiceException(
          "testServletProperties(): MessageContext is not populated.");
    }
    System.out.println("Leave testServletProperties()");
  }

  public void testHttpProperties() {
    System.out.println("Enter testHttpProperties()");
    MessageContext ctxt = wsc.getMessageContext();
    System.out.println("MessageContext.HTTP_REQUEST_HEADERS="
        + ctxt.get(MessageContext.HTTP_REQUEST_HEADERS));
    System.out.println("MessageContext.HTTP_REQUEST_METHOD="
        + ctxt.get(MessageContext.HTTP_REQUEST_METHOD));
    if (ctxt.get(MessageContext.HTTP_REQUEST_HEADERS) == null
        || ctxt.get(MessageContext.HTTP_REQUEST_METHOD) == null
        || !ctxt.get(MessageContext.HTTP_REQUEST_METHOD).equals("POST")) {
      throw new WebServiceException(
          "testHttpProperties(): MessageContext is not populated.");
    }
    System.out.println("Leave testHttpProperties()");
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
