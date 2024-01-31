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
package org.jboss.cdi.tck.tests.decorators.definition.lifecycle;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

import org.jboss.cdi.tck.util.ActionSequence;

/**
 * Decorator is a managed bean and may use the {@link PostConstruct} and {@link PreDestroy} annotations to identify methods to
 * be called back by the container at the appropriate points in the bean’s lifecycle.
 * 
 * @author Martin Kouba
 */
@SuppressWarnings("serial")
@Decorator
public abstract class ChargeDecorator implements BankAccount, Serializable {

    private static final int WITHDRAWAL_CHARGE = 5;

    public static int charged = 0;

    @Inject
    @Delegate
    private BankAccount account;

    @Override
    public void withdraw(int amount) {
        account.withdraw(amount + WITHDRAWAL_CHARGE);
        charged += WITHDRAWAL_CHARGE;
    }

    public static void reset() {
        charged = 0;
    }

    @PostConstruct
    public void postConstruct() {
        ActionSequence.addAction("postConstructCallers", DurableAccount.class.getName());
    }

    @PreDestroy
    public void preDestroy() {
        ActionSequence.addAction("preDestroyCallers", DurableAccount.class.getName());
    }

}
