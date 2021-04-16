/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.observer.context.async;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.ObservesAsync;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

@ApplicationScoped
public class AsyncMessageObserver {

    @Inject
    UserTransaction transaction;

    @Inject
    Counter counter;
    
    @Inject
    BeanManager bm;
    
    public static AtomicBoolean requestScopeActive = new AtomicBoolean(false);

    public static AtomicInteger status = new AtomicInteger();
    public static AtomicBoolean counterIsZero = new AtomicBoolean();

    public void observe(@ObservesAsync Message message) throws SystemException {
        status.set(transaction.getStatus());

    }

    public void observe(@ObservesAsync String text) {
        requestScopeActive.set(bm.getContext(RequestScoped.class).isActive());
        counterIsZero.set(counter.getCount().get() > 0 ? false : true);
    }


}
