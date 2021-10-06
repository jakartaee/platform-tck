/*
 * Copyright (c) 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.idstore.common;

import java.util.Set;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomHamForIDStore implements HttpAuthenticationMechanism {

  @Override
  public AuthenticationStatus validateRequest(HttpServletRequest request,
      HttpServletResponse response, HttpMessageContext httpMessageContext)
      throws AuthenticationException {

    IdentityStoreHandler identityStoreHandler = CDI.current()
        .select(IdentityStoreHandler.class).get();

    System.out.println("CustomHAMForIDStore: validateRequest...");

    String username = request.getParameter("user");
    String password = request.getParameter("pwd");

    System.out.println("CustomHAMFormIDStore:user=" + username);
    System.out.println("CustomHAMFormIDStore:pwd=" + password);

    CredentialValidationResult result;

    try {
      Credential credential = new CustomCredential(username, password);
      result = identityStoreHandler.validate(credential);
      System.out.println("CustomHAMFormIDStore: get Groups");
      Set<String> groups = result.getCallerGroups();
      System.out.println("CustomHAMFormIDStore: Groups=" + groups);

      // response.getWriter().write("Groups=" + groups);
      response.getWriter().append(
          "ValidateResultStatus=" + result.getStatus().toString() + "\n");
      response.getWriter().append("ValidateResultGroups=" + groups + "\n");
      response.setStatus(200);
      return httpMessageContext.notifyContainerAboutLogin(result);

    } catch (Exception ex) {
      System.out.println("Exception met..." + ex.getMessage());
      ex.printStackTrace();
    }

    return httpMessageContext.doNothing();
  }
}
