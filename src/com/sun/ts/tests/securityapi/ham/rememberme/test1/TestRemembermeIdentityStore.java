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

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.CallerPrincipal;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.RememberMeIdentityStore;
import jakarta.security.enterprise.credential.RememberMeCredential;

@ApplicationScoped
public class TestRemembermeIdentityStore implements RememberMeIdentityStore {

  private final Map<String, CredentialValidationResult> identitiesMap = new ConcurrentHashMap<String, CredentialValidationResult>();

  @Override
  public String generateLoginToken(CallerPrincipal callerPrincipal,
      Set<String> groups) {
    String token = UUID.randomUUID().toString();

    CredentialValidationResult validationResult = new CredentialValidationResult(
        callerPrincipal, groups);
    identitiesMap.put(token, validationResult);

    return token;
  }

  @Override
  public CredentialValidationResult validate(RememberMeCredential credential) {
    if (identitiesMap.containsKey(credential.getToken())) {
      return identitiesMap.get(credential.getToken());
    }

    return CredentialValidationResult.INVALID_RESULT;
  }

  @Override
  public void removeLoginToken(String token) {
    identitiesMap.remove(token);
  }
}
