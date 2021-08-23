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
 *   $Id$
 *   @author Raja Perumal
 */

package com.sun.ts.tests.webservices12.sec.annotations.ejb.basicauthssl;

import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.DenyAll;

import jakarta.ejb.Stateless;
import jakarta.jws.WebService;
import jakarta.jws.WebMethod;
import jakarta.xml.ws.WebServiceContext;
import jakarta.annotation.Resource;

@WebService
@Stateless

@DeclareRoles({ "Administrator", "Manager" })
public class Hello {

  @Resource
  private WebServiceContext context;

  String username = null;

  boolean isUserInRoleAdministrator = false;

  @WebMethod
  @RolesAllowed("Administrator")
  public String sayHelloProtected(String param) {

    if (context.getUserPrincipal() != null)
      username = context.getUserPrincipal().getName();
    isUserInRoleAdministrator = context.isUserInRole("Administrator");

    return "Invoked as user :" + username + " : isUserInRoleAdministrator ="
        + isUserInRoleAdministrator + " : Hello " + param;
  }

  @WebMethod
  @PermitAll
  public String sayHelloPermitAll(String param) {

    if (context.getUserPrincipal() != null)
      username = context.getUserPrincipal().getName();
    isUserInRoleAdministrator = context.isUserInRole("Administrator");

    return "Invoked as user :" + username + " : isUserInRoleAdministrator ="
        + isUserInRoleAdministrator + " : Hello " + param;
  }

  @WebMethod
  @DenyAll
  public String sayHelloDenyAll(String param) {

    return "Hello " + param;
  }

}
