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
 *   $Id$
 *   @author Raja Perumal
 */

package com.sun.ts.tests.webservices12.sec.annotations.ejb.basicauthssl;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.PermitAll;
import javax.annotation.security.DenyAll;

import javax.ejb.Stateless;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;

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
