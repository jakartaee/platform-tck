/*
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

import static org.testng.Assert.assertEquals;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.interceptor.InvocationContext;
import javax.naming.InitialContext;
import jakarta.transaction.TransactionSynchronizationRegistry;

public class CatInterceptor {

    static boolean pcCalled = false;
    static boolean pdCalled = false;

    private TransactionSynchronizationRegistry tsr;

    @PostConstruct
    void intercept2(InvocationContext ic) {
        try {
            ic.proceed();
            tsr = (TransactionSynchronizationRegistry) InitialContext.doLookup("java:comp/TransactionSynchronizationRegistry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(tsr.getTransactionKey(), Cat.getPostConstructContext(),
                "PostConstruct interceptor method invocation did NOT occur within the transaction context determined by the target method");
        pcCalled = true;
    }

    @PreDestroy
    void intercept3(InvocationContext ic) {
        try {
            ic.proceed();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        assertEquals(tsr.getTransactionKey(), Cat.getPreDestroyContext(),
                "PreDestroy interceptor method invocation did NOT occur within the transaction context determined by the target method");
        pdCalled = true;
    }
}
