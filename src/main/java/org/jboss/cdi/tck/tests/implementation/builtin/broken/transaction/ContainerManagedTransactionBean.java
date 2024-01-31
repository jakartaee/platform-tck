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
package org.jboss.cdi.tck.tests.implementation.builtin.broken.transaction;

import static jakarta.ejb.TransactionManagementType.CONTAINER;

import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionManagement;
import jakarta.inject.Inject;
import jakarta.transaction.UserTransaction;

/**
 * 
 * @author Martin Kouba
 */
@Stateful
@TransactionManagement(CONTAINER)
public class ContainerManagedTransactionBean {

    @Inject
    private UserTransaction userTransaction;

    public UserTransaction getUserTransaction() {
        return userTransaction;
    }

}
