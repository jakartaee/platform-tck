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

package com.sun.ts.tests.securityapi.ham.autoapplysession;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;

@RequestScoped
public class TestIdentityStore implements IdentityStore {

  private final Map<String, String> callerToPassword;

  private final Map<String, Set<String>> callerToGroups;

  {
    callerToPassword = new HashMap<String, String>();
    callerToPassword.put("tom", "secret1");
    callerToPassword.put("emma", "secret2");
    callerToPassword.put("bob", "secret3");

    callerToGroups = new HashMap<String, Set<String>>();
    callerToGroups.put("tom",
        new HashSet<String>(Arrays.asList("Administrator", "Manager")));
    callerToGroups.put("emma",
        new HashSet<String>(Arrays.asList("Administrator", "Employee")));
    callerToGroups.put("bob",
        new HashSet<String>(Arrays.asList("Administrator")));
  }

  @Override
  public CredentialValidationResult validate(Credential credential) {
    if (credential instanceof UsernamePasswordCredential) {
      return validate((UsernamePasswordCredential) credential);
    }

    return CredentialValidationResult.NOT_VALIDATED_RESULT;
  }

  private CredentialValidationResult validate(
      UsernamePasswordCredential usernamePasswordCredential) {
    String user = usernamePasswordCredential.getCaller();
    String password = callerToPassword.get(user);

    if (password != null
        && usernamePasswordCredential.getPassword().compareTo(password)) {
      return new CredentialValidationResult(user, callerToGroups.get(user));
    }

    return CredentialValidationResult.INVALID_RESULT;
  }

}
