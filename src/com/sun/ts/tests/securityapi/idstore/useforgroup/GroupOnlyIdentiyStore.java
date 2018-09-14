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

package com.sun.ts.tests.securityapi.idstore.useforgroup;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;
import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.EnumSet;
import java.util.TreeSet;
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
public class GroupOnlyIdentiyStore implements IdentityStore {

  private Map<String, String> userPwd;

  private Map<String, Set<String>> userGroup;

  @PostConstruct
  public void init() {
    userPwd = new HashMap<>();
    userPwd.put("tom", "welcom1");
    userPwd.put("emma", "welcom2");
    userPwd.put("bob", "welcome3");

    userGroup = new HashMap<>();
    userGroup.put("tom", new TreeSet<>(asList("Oracle", "Oracle_HQ")));
    userGroup.put("emma", new TreeSet<>(asList("Oracle", "Oracle_CDC")));
    userGroup.put("bob", new TreeSet<>(asList("Oracle")));

  }

  @Override
  public CredentialValidationResult validate(Credential credential) {
    CredentialValidationResult result;

    System.out.println("AuthzOnlyIdentityStore:validate()");
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

      System.out.println("AuthzOnlyIdentityStore:validate():result=" + result);
      System.out.println("AuthzOnlyIdentityStore:validate():group="
          + result.getCallerGroups());

    } else {
      result = NOT_VALIDATED_RESULT;
    }

    return result;
  }

  @Override
  public Set<String> getCallerGroups(
      CredentialValidationResult validationResult) {
    System.out.println("AuthzOnlyIdentityStore:getCallerGroups()");
    Set<String> groups = new TreeSet<String>();
    groups.add("useforgroup:getCallerGroups");
    groups
        .addAll(userGroup.get(validationResult.getCallerPrincipal().getName()));
    return groups;
  }

  @Override
  public int priority() {
    int priority = 90;
    System.out.println("AuthzOnlyIdentityStore:priority=" + priority);
    return priority;
  }

  @Override
  public Set<ValidationType> validationTypes() {
    System.out.println("AuthzOnlyIdentityStore:validationType=AUTHORIZATION");
    return EnumSet.of(PROVIDE_GROUPS);
  }
}
