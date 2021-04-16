/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundTimeout;

import jakarta.ejb.Timer;
import jakarta.interceptor.AroundTimeout;
import jakarta.interceptor.InvocationContext;
import javax.naming.InitialContext;
import jakarta.transaction.TransactionSynchronizationRegistry;

public class TimeoutInterceptor {

    static boolean timerOK = false;
    static Object key;

    @AroundTimeout
    public Object interceptTimeout(InvocationContext ctx) throws Exception {
        if (((String) ((Timer) ctx.getTimer()).getInfo()).equals("some info")) {
            timerOK = true;
        }
        TransactionSynchronizationRegistry tsr;
        try {
            tsr = (TransactionSynchronizationRegistry) InitialContext.doLookup("java:comp/TransactionSynchronizationRegistry");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        key = tsr.getTransactionKey();

        return ctx.proceed();
    }
}
