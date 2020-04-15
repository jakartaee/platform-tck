/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.ham.workflow.validaterequestduringauthen;

import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestScoped
public class TestAuthenticationMechanism
    implements HttpAuthenticationMechanism {

  @Inject
  private IdentityStoreHandler identityStoreHandler;

  @Override
  public AuthenticationStatus validateRequest(HttpServletRequest request,
      HttpServletResponse response, HttpMessageContext httpMessageContext)
      throws AuthenticationException {

    if (request.getAttribute("httpservletrequest_authenticate") == null) {
      return httpMessageContext.doNothing();
    } else {
      request.removeAttribute("httpservletrequest_authenticate");
    }

    try {
      response.getWriter()
          .print("In HttpAuthenticationMechanism validateRequest method.");
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (request.getParameter("name") != null
        && request.getParameter("password") != null) {

      String name = request.getParameter("name");
      Password password = new Password(request.getParameter("password"));

      CredentialValidationResult result = identityStoreHandler
          .validate(new UsernamePasswordCredential(name, password));

      if (result.getStatus() == VALID) {
        return httpMessageContext.notifyContainerAboutLogin(
            result.getCallerPrincipal(), result.getCallerGroups());
      }

      return httpMessageContext.responseUnauthorized();
    }

    if (httpMessageContext.isProtected()) {
      return httpMessageContext.responseUnauthorized();
    }

    return httpMessageContext.doNothing();
  }

}
