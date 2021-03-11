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

package com.sun.ts.tests.jaxws.sharedwebservices.hellosecureservice;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import java.security.Principal;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;
import jakarta.annotation.Resource;

@WebService(portName = "HelloPort", serviceName = "HelloService", targetNamespace = "http://helloservice.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSHelloSecureService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.sharedwebservices.hellosecureservice.Hello")

public class HelloImpl implements Hello {

  @Resource
  protected WebServiceContext wsContext;

  public String hello(String s) {
    return "Hello, " + s + "!";
  }

  public boolean getMessageContextTest() {
    if (wsContext == null)
      return false;
    else {
      MessageContext v = wsContext.getMessageContext();
      System.out.println("MessageContext=" + v);
      return true;
    }
  }

  public boolean getUserPrincipalTest() {
    if (wsContext == null)
      return false;
    else {
      java.security.Principal v = wsContext.getUserPrincipal();
      System.out.println("UserPrincipal=" + v);
      return true;
    }
  }

  public boolean isUserInRoleTest(String s) {
    if (wsContext == null)
      return false;
    else
      return wsContext.isUserInRole(s);
  }

  public boolean getEndpointReferenceTest() {
    boolean pass = true;
    if (wsContext == null) {
      pass = false;
    } else {
      EndpointReference epr = wsContext.getEndpointReference();
      TestUtil.logMsg("EndpointReference object=" + epr);
      if (epr == null) {
        TestUtil.logErr("getEndpointReference() returned null");
        pass = false;
      } else {
        TestUtil.logMsg(
            "getEndpointReference() returned EndpointReference object: " + epr);
      }
      if (epr instanceof W3CEndpointReference) {
        TestUtil.logMsg("epr instanceof W3CEndpointReference");
      } else {
        TestUtil.logErr("epr not instanceof W3CEndpointReference");
        pass = false;
      }
    }
    if (!pass) {
      return false;
    } else {
      return true;
    }
  }

  /*
   * <T extends EndpointReference> T getEndpointReference(java.lang.Class<T>
   * clazz, org.w3c.dom.Element... referenceParameters)
   */
  public boolean getEndpointReference2Test() {
    boolean pass = true;
    if (wsContext == null) {
      pass = false;
    } else {
      // this is what the params needs to be java.lang.Class<T> clazz,
      // org.w3c.dom.Element... referenceParameters
      // <T extends EndpointReference> T epr=
      // wsContext.getEndpointReference(W3CEndpointReference.class);
      EndpointReference epr = wsContext
          .getEndpointReference(W3CEndpointReference.class);
      TestUtil.logMsg("EndpointReference object=" + epr);
      if (epr == null) {
        TestUtil.logErr("getEndpointReference() returned null");
        pass = false;
      } else {
        TestUtil.logMsg(
            "getEndpointReference() returned EndpointReference object: " + epr);
      }
      if (epr instanceof W3CEndpointReference) {
        TestUtil.logMsg("epr instanceof W3CEndpointReference");
      } else {
        TestUtil.logErr("epr not instanceof W3CEndpointReference");
        pass = false;
      }
    }
    if (!pass) {
      return false;
    } else {
      return true;
    }
  }
}
