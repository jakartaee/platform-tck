/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.security.enterprise.credential.Credential;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sun.ts.tests.securityapi.idstore.common.CustomCredential;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
