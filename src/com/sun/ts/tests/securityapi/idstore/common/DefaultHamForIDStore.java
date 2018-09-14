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

package com.sun.ts.tests.securityapi.idstore.common;

import javax.inject.Inject;
import javax.enterprise.inject.spi.CDI;
import javax.security.enterprise.identitystore.IdentityStore.ValidationType;
import javax.security.enterprise.identitystore.CredentialValidationResult.Status;

import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;

import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DefaultHamForIDStore implements HttpAuthenticationMechanism {

  @Override
  public AuthenticationStatus validateRequest(HttpServletRequest request,
      HttpServletResponse response, HttpMessageContext httpMessageContext)
      throws AuthenticationException {

    IdentityStoreHandler identityStoreHandler = CDI.current()
        .select(IdentityStoreHandler.class).get();
    System.out.println("DefaultHAMForIDStore: validateRequest...");

    String username = request.getParameter("user");
    String password = request.getParameter("pwd");

    System.out.println("DefaultHAMForIDStore:user=" + username);
    System.out.println("DefaultHAMForIDStore:pwd=" + password);

    CredentialValidationResult result;

    try {
      Credential credential = new UsernamePasswordCredential(username,
          new Password(password));
      result = identityStoreHandler.validate(credential);
      System.out.println("DefaultHAMForIDStore: get Groups");
      Set<String> groups = result.getCallerGroups();
      System.out.println("DefaultHAMForIDStore: Groups=" + groups);
      String callerDn = result.getCallerDn();
      System.out.println("DefaultHAMForIDStore: callerDn=" + callerDn);

      // response.getWriter().write("Groups=" + groups);
      response.getWriter().append(
          "ValidateResultStatus=" + result.getStatus().toString() + "\n");
      response.getWriter().append("ValidateResultGroups=" + groups + "\n");
      response.getWriter().append("ValidateCallerDN=" + callerDn + "\n");

      // Always return 200 because response body could be returned.
      // If return 401, response body will not be returned and Client can't
      // verify
      // the status and Groups value of CredentialValidationResult
      response.setStatus(200);
      if (result.getStatus() == VALID)
        return httpMessageContext.notifyContainerAboutLogin(result);
      else
        return httpMessageContext.doNothing();

    } catch (Exception ex) {
      try {
        response.getWriter().append("Exception received." + "\n");
        response.getWriter()
            .append("Exception message: " + ex.getMessage() + "\n");
      } catch (IOException e) {
        System.out.println("Exception met..." + e.getMessage());
        e.printStackTrace();
      }

      System.out.println("Exception met..." + ex.getMessage());
      ex.printStackTrace();
    }

    return httpMessageContext.doNothing();
  }
}
