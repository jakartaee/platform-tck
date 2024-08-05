/*
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
package org.jboss.cdi.tck.tests.implementation.simple.resource.ejb;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.inject.Inject;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class Bean implements BeanRemote {
    private int knocks = 0;

    private @Inject
    Monitor monitor;

    private @Resource
    UserTransaction transaction;

    public String knockKnock() {
        knocks++;
        return "We're home";
    }

    public int getKnocks() {
        return knocks;
    }

    public boolean isUserTransactionInjected() {
        try {
            if (transaction != null) {
                transaction.getStatus();
                return true;
            }
        } catch (SystemException e) {
        }
        return false;
    }

    @PreDestroy
    public void cleanup() {
        monitor.remoteEjbDestroyed();
    }

    @Remove
    public void dispose() {
    }
}
