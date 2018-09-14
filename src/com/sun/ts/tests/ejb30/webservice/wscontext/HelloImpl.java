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

package com.sun.ts.tests.ejb30.webservice.wscontext;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.ejb.Stateless;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.interceptor.Interceptors;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.handler.MessageContext;
import java.security.Principal;

@WebService(name = "Hello", serviceName = "HelloService")
@Stateless
@DeclareRoles("Administrator")
public class HelloImpl {
  @Resource
  private WebServiceContext wsContext;

  @RolesAllowed({ "Administrator" })
  public String sayHelloProtected(String param) throws WebServiceException {
    String output = "";

    Object msgContext = wsContext.getMessageContext();
    if (msgContext instanceof javax.xml.ws.handler.MessageContext) {
      output += " 1. MessageContext is an instance of javax.xml.ws.handler.MessageContext ";
    } else {
      throw new WebServiceException(
          "MessageContext not an instance of javax.xml.ws.handler.MessageContext");
    }

    Principal invocationPrincipal = wsContext.getUserPrincipal();
    String principalName = invocationPrincipal.getName();

    if (invocationPrincipal != null) {
      output += " 2. Web Service invoked by user " + principalName;
    } else {
      throw new WebServiceException("UnExpected user principal");
    }

    if (wsContext.isUserInRole("Administrator")) {
      output += " 3. User j2ee is in role Administrator ";
    } else {
      throw new WebServiceException("User not in role Administrator");
    }

    return "Hello " + param + output;
  }
}
