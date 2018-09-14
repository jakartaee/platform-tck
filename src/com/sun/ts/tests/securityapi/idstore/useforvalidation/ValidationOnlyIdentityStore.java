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

package com.sun.ts.tests.securityapi.idstore.useforvalidation;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.identitystore.IdentityStore.ValidationType;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.CallerPrincipal;

/*
 * 
 */
@RequestScoped
public class ValidationOnlyIdentityStore implements IdentityStore {

  private Map<String, String> userPwd;

  private Map<String, Set<String>> userGroup;

  @PostConstruct
  public void init() {
    userPwd = new HashMap<>();
    userPwd.put("tom", "secret1");
    userPwd.put("emma", "secret2");
    userPwd.put("bob", "secret3");

    userGroup = new HashMap<>();
    userGroup.put("tom", new HashSet<>(asList("Administrator", "Manager")));
    userGroup.put("emma", new HashSet<>(asList("Administrator", "Employee")));
    userGroup.put("bob", new HashSet<>(asList("Administrator")));
  }

  @Override
  public CredentialValidationResult validate(Credential credential) {
    CredentialValidationResult result;

    System.out.println("ValidationOnlyIdentityStore:validate()");
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

      System.out
          .println("ValidationOnlyIdentityStore:validate():result=" + result);
      System.out.println("ValidationOnlyIdentityStore:validate():group="
          + result.getCallerGroups());

    } else {
      result = NOT_VALIDATED_RESULT;
    }

    return result;
  }

  @Override
  public Set<String> getCallerGroups(
      CredentialValidationResult validationResult) {
    System.out.println("ValidationOnlyIdentityStore:getCallerGroups()");
    Set<String> groups = new HashSet<String>();
    groups.add("Authn:getCallerGroups");
    groups
        .addAll(userGroup.get(validationResult.getCallerPrincipal().getName()));
    return groups;
  }

  @Override
  public int priority() {
    int priority = 90;
    System.out.println("ValidationOnlyIdentityStore:priority=" + priority);
    return priority;
  }

  @Override
  public Set<ValidationType> validationTypes() {
    System.out
        .println("ValidationOnlyIdentityStore:validationType=AUTHENTICATION");
    return EnumSet.of(VALIDATE);
  }

}
