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

package com.sun.ts.tests.securityapi.idstore.multiauthz;

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
public class IdentityStoreAuthz1 implements IdentityStore {

  private Map<String, Set<String>> userGroup;

  @PostConstruct
  public void init() {
    userGroup = new HashMap<>();
    userGroup.put("tom", new HashSet<>(asList("AdminAuthz1", "ManagerAuthz1")));
    userGroup.put("emma",
        new HashSet<>(asList("AdminAuthz1", "EmployeeAuthz1")));
    userGroup.put("bob", new HashSet<>(asList("AdminAuthz1")));
  }

  @Override
  public Set<String> getCallerGroups(
      CredentialValidationResult validationResult) {
    System.out.println("IDStoreAuthz1:getCallerGroups...");
    Set<String> groups = new HashSet<String>();
    groups.add("IDStoreAuthz1:getCallerGroups");
    groups
        .addAll(userGroup.get(validationResult.getCallerPrincipal().getName()));
    return groups;
  }

  @Override
  public int priority() {
    int priority = 200;
    return priority;
  }

  @Override
  public Set<ValidationType> validationTypes() {
    return EnumSet.of(PROVIDE_GROUPS);
  }

}
