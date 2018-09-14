/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.securitycontext.ejb;

import java.security.Principal;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Stateless
@DeclareRoles({ "Administrator", "Manager", "Employee" })
@PermitAll
public class TestEJB {

  @Inject
  private SecurityContext securityContext;

  public Principal getCallerPrincipal() {
    return securityContext.getCallerPrincipal();
  }

  public boolean isCallerInRole(String role) {
    return securityContext.isCallerInRole(role);
  }

  public AuthenticationStatus authenticate(HttpServletRequest request,
      HttpServletResponse response, AuthenticationParameters parameters) {
    return securityContext.authenticate(request, response, parameters);
  }
}
