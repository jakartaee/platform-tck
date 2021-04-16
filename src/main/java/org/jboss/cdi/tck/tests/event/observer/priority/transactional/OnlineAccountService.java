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
package org.jboss.cdi.tck.tests.event.observer.priority.transactional;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Status;
import jakarta.transaction.UserTransaction;

import org.jboss.cdi.tck.tests.event.observer.transactional.Failure;
import org.jboss.cdi.tck.tests.event.observer.transactional.Withdrawal;
import org.jboss.cdi.tck.util.ActionSequence;

@Named
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class OnlineAccountService {

    @Resource
    private UserTransaction userTransaction;

    @Inject
    Event<TxWithdrawal> event;

    @Inject
    Event<TxFailure> eventFailure;

    /**
     * 
     * @param amount
     * @throws Exception
     */
    public void withdrawSuccesTransaction(int amount) throws Exception {
        userTransaction.begin();
        event.fire(new TxWithdrawal(amount));
        ActionSequence.addAction("checkpoint");
        userTransaction.commit();
    }

    /**
     * 
     * @param amount
     * @throws Exception
     */
    public void withdrawFailedTransaction(int amount) throws Exception {
        userTransaction.begin();
        event.fire(new TxWithdrawal(amount));
        ActionSequence.addAction("checkpoint");
        // Failed for any reason
        userTransaction.rollback();
    }

    /**
     * 
     * @param amount
     * @throws Exception
     */
    public void withdrawNoTransaction(int amount) throws Exception {
        event.fire(new TxWithdrawal(amount));
        ActionSequence.addAction("checkpoint");
    }

    /**
     * 
     * @param amount
     * @throws Exception
     */
    public void withdrawObserverFailedTransaction(int amount) throws Exception {
        userTransaction.begin();
        event.fire(new TxWithdrawal(amount));
        eventFailure.fire(new TxFailure());
        ActionSequence.addAction("checkpoint");
        if (userTransaction.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
            userTransaction.rollback();
        } else {
            userTransaction.commit();
        }
    }

}
