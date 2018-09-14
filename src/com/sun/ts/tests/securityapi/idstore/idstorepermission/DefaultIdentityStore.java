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

package com.sun.ts.tests.securityapi.idstore.idstorepermission;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.CallerPrincipal;

/*
 * 
 */
@ApplicationScoped
public class DefaultIdentityStore implements IdentityStore {

  private Map<String, String> userPwd;

  private Map<String, Set<String>> userGroup;

  @PostConstruct
  public void init() {
    userPwd = new HashMap<>();
    userPwd.put("tom", "secret1");
    userPwd.put("emma", "secret22");
    userPwd.put("bob", "secret33");

    userGroup = new HashMap<>();
    userGroup.put("tom", new HashSet<>(asList("Administrator1", "Manager1")));
    userGroup.put("emma", new HashSet<>(asList("Administrator1", "Employee1")));
    userGroup.put("bob", new HashSet<>(asList("Administrator1")));
  }

  @Override
  public CredentialValidationResult validate(Credential credential) {
    CredentialValidationResult result;

    System.out.println("DefaultIdentityStore:validate()");
    if (credential instanceof UsernamePasswordCredential) {
      UsernamePasswordCredential usernamePassword = (UsernamePasswordCredential) credential;
      String caller = usernamePassword.getCaller();
      String expectedPW = userPwd.get(caller);

      if (expectedPW != null
          && expectedPW.equals(usernamePassword.getPasswordAsString())) {
        result = new CredentialValidationResult(caller, userGroup.get(caller));
      } else {
        result = INVALID_RESULT;
      }

      System.out.println("DefaultIdentityStore:validate():result=" + result);
      System.out.println(
          "DefaultIdentityStore:validate():group=" + result.getCallerGroups());

    } else {
      result = NOT_VALIDATED_RESULT;
    }

    return result;
  }

  @Override
  public Set<String> getCallerGroups(
      CredentialValidationResult validationResult) {
    System.out.println("DefaultIdentityStore:getCallerGroups()");
    Set<String> groups = new HashSet<String>();
    groups.add("getCallerGroups");
    groups
        .addAll(userGroup.get(validationResult.getCallerPrincipal().getName()));
    return groups;
  }

  @Override
  public int priority() {
    System.out.println("DefaultIdentityStore:priority....");
    return 100;
  }

  @Override
  public Set<ValidationType> validationTypes() {
    System.out.println("DefaultIdentityStore:validationType....");
    return DEFAULT_VALIDATION_TYPES;
  }
}
