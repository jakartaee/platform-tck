/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback.ejb;

import static org.testng.Assert.assertNotNull;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.interceptor.Interceptors;
import jakarta.transaction.TransactionSynchronizationRegistry;

@Stateful
@Interceptors(CatInterceptor.class)
public class Cat extends Animal {

    @Resource
    private TransactionSynchronizationRegistry tsr;

    private static Object postConstructContext;
    private static Object preDestroyContext;

    public static Object getPostConstructContext() {
        return postConstructContext;
    }

    public static Object getPreDestroyContext() {
        return preDestroyContext;
    }

    @PostConstruct
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void init() {
        postConstructContext = tsr.getTransactionKey();
        assertNotNull(postConstructContext);
    }

    @PreDestroy
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void die() {
        preDestroyContext = tsr.getTransactionKey();
        assertNotNull(preDestroyContext);
    }

    @Override
    public void foo() {
    }

    @Override
    public String getAnimalType() {
        return "Cat";
    }
}
