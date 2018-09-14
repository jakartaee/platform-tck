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

package com.sun.ts.tests.securityapi.idstore.customhandler;

import static javax.security.enterprise.identitystore.CredentialValidationResult.Status.VALID;
import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;
import static javax.security.enterprise.identitystore.CredentialValidationResult.NOT_VALIDATED_RESULT;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.PROVIDE_GROUPS;
import static javax.security.enterprise.identitystore.IdentityStore.ValidationType.VALIDATE;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Optional.empty;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static javax.interceptor.Interceptor.Priority.APPLICATION;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.CallerPrincipal;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;

/**
 *
 */
@Alternative
@Priority(APPLICATION)
@ApplicationScoped
public class CustomIdentityStoreHandler implements IdentityStoreHandler {

  private List<IdentityStore> validatingIdentityStores;

  private List<IdentityStore> groupProvidingIdentityStores;

  @PostConstruct
  public void init() {
    List<IdentityStore> identityStores = getBeanReferencesByType(
        IdentityStore.class, false);

    validatingIdentityStores = identityStores.stream()
        .filter(i -> i.validationTypes().contains(VALIDATE))
        .sorted(comparing(IdentityStore::priority)).collect(toList());

    groupProvidingIdentityStores = identityStores.stream()
        .filter(i -> i.validationTypes().contains(PROVIDE_GROUPS))
        .sorted(comparing(IdentityStore::priority)).collect(toList());

    System.out.println("CustomIdentityHandler: init");
  }

  @Override
  public CredentialValidationResult validate(Credential credential) {
    System.out.println("CustomIdentityHandler: validate");
    CredentialValidationResult validationResult = null;
    IdentityStore identityStore = null;

    // Check validations in all stores are VALID, then final result is VALID
    // final result would be invalid if any validation is invalid
    for (IdentityStore authenticationIdentityStore : validatingIdentityStores) {
      CredentialValidationResult temp = authenticationIdentityStore
          .validate(credential);
      switch (temp.getStatus()) {

      case NOT_VALIDATED:
        // Don't do anything
        break;
      case INVALID:
        validationResult = temp;
        break;
      case VALID:
        validationResult = temp;
        break;
      default:
        throw new IllegalArgumentException(
            "Value not supported " + temp.getStatus());
      }
      if (validationResult != null && validationResult
          .getStatus() == CredentialValidationResult.Status.INVALID) {
        break;
      }
    }

    if (validationResult == null) {
      // No authentication store at all
      return INVALID_RESULT;
    }

    if (validationResult.getStatus() != VALID) {
      // No store validated (authenticated), no need to continue
      return validationResult;
    }

    CallerPrincipal callerPrincipal = validationResult.getCallerPrincipal();

    Set<String> groups = new HashSet<>();
    groups.add("customIdentiyStoreHandler");

    // Ask all stores that were configured for authorization to get the groups
    // for the
    // authenticated caller
    for (IdentityStore authorizationIdentityStore : groupProvidingIdentityStores) {
      groups
          .addAll(authorizationIdentityStore.getCallerGroups(validationResult));
    }

    return new CredentialValidationResult(callerPrincipal, groups);

  }

  @SuppressWarnings("unchecked")
  private <T> List<T> getBeanReferencesByType(Class<T> type, boolean optional) {

    BeanManager beanManager = CDI.current().getBeanManager();
    Set<Bean<?>> beans = beanManager.getBeans(type);
    List<T> result = new ArrayList<>(beans.size());

    for (Bean<?> bean : beans) {
      result.add(getContextualReference(type, beanManager,
          Collections.singleton(bean)));
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  private <T> T getContextualReference(Class<T> type, BeanManager beanManager,
      Set<Bean<?>> beans) {

    Object beanReference = null;

    Bean<?> bean = beanManager.resolve(beans);
    if (bean != null) {
      beanReference = beanManager.getReference(bean, type,
          beanManager.createCreationalContext(bean));
    }

    return (T) beanReference;
  }

  private static void close(InitialContext context) {
    try {
      if (context != null) {
        context.close();
      }
    } catch (NamingException e) {
      throw new IllegalStateException(e);
    }
  }

}
