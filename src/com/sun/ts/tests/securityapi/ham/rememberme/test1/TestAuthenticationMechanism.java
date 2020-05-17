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

package com.sun.ts.tests.securityapi.ham.rememberme.test1;

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.authentication.mechanism.http.RememberMe;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RememberMe(cookieName = "JSR375COOKIENAME", cookieMaxAgeSeconds = 15, isRememberMe = false, isRememberMeExpression = "#{self.isRememberMe(httpMessageContext)}", cookieSecureOnly = false)
@RequestScoped
public class TestAuthenticationMechanism
    implements HttpAuthenticationMechanism {

  @Inject
  private IdentityStoreHandler identityStoreHandler;

  @Override
  public AuthenticationStatus validateRequest(HttpServletRequest request,
      HttpServletResponse response, HttpMessageContext httpMessageContext)
      throws AuthenticationException {

    request.setAttribute("ham-validateRequest-called", "true");

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

  public Boolean isRememberMe(HttpMessageContext httpMessageContext) {
    return httpMessageContext.getRequest().getParameter("rememberme") != null
        && httpMessageContext.getRequest().getParameter("rememberme")
            .equalsIgnoreCase("true");
  }

  @Override
  public void cleanSubject(HttpServletRequest request,
      HttpServletResponse response, HttpMessageContext httpMessageContext) {
    HttpAuthenticationMechanism.super.cleanSubject(request, response,
        httpMessageContext);
  }

}
