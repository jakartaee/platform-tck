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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundInvoke.ee;

import static org.testng.Assert.assertEquals;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import javax.naming.InitialContext;
import jakarta.transaction.TransactionSynchronizationRegistry;

public class FooInterceptor {

    static boolean called = false;
    
    @AroundInvoke
    public Object intercept(InvocationContext ic) throws Exception {
        Object obj = ic.proceed();
        TransactionSynchronizationRegistry tsr = (TransactionSynchronizationRegistry) InitialContext
                .doLookup("java:comp/TransactionSynchronizationRegistry");
        assertEquals(tsr.getTransactionKey(), Foo.getKey(),
                "Around-invoke method invocation did NOT occur within the same transaction context as the method on which it is interposing");

        called = true;
        return obj;
    }
}
