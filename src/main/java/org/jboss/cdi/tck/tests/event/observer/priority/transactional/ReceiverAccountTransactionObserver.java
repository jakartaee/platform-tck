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

import jakarta.annotation.Priority;
import jakarta.annotation.Resource;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.interceptor.Interceptor;
import jakarta.transaction.UserTransaction;

/**
 * 
 * @author Mark Paluch
 */
public class ReceiverAccountTransactionObserver extends AbstractObserver {

    @Resource
    private UserTransaction userTransaction;

    /**
     * 
     * @param txWithdrawal
     * @throws Exception
     */
    public void withdrawAfterSuccess(
            @Observes(during = TransactionPhase.AFTER_SUCCESS) @Priority(Interceptor.Priority.APPLICATION + 100) TxWithdrawal txWithdrawal)
            throws Exception {
        logEventFired(TransactionPhase.AFTER_SUCCESS);
    }

    /**
     * 
     * @param txWithdrawal
     * @throws Exception
     */
    public void withdrawAfterCompletion(
            @Observes(during = TransactionPhase.AFTER_COMPLETION) @Priority(Interceptor.Priority.APPLICATION + 50) TxWithdrawal txWithdrawal)
            throws Exception {
        logEventFired(TransactionPhase.AFTER_COMPLETION);
    }

    /**
     * 
     * @param txWithdrawal
     * @throws Exception
     */
    public void withdrawBeforeCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) @Priority(Interceptor.Priority.APPLICATION - 1) TxWithdrawal txWithdrawal)
            throws Exception {
        logEventFired(TransactionPhase.BEFORE_COMPLETION);
    }

    /**
     * Always fire immediately.
     * 
     * @param txWithdrawal
     * @throws Exception
     */
    public void withdrawNoTx(
            @Observes(during = TransactionPhase.IN_PROGRESS) @Priority(Interceptor.Priority.APPLICATION + 600) TxWithdrawal txWithdrawal)
            throws Exception {
        logEventFired(TransactionPhase.IN_PROGRESS);
    }

    /**
     * 
     * @param txWithdrawal
     * @throws Exception
     */
    public void withdrawAfterFailure(@Observes(during = TransactionPhase.AFTER_FAILURE) TxWithdrawal txWithdrawal) throws Exception {
        logEventFired(TransactionPhase.AFTER_FAILURE);
    }

    /**
     * 
     * @param txFailure
     * @throws Exception
     */
    public void failBeforeCompletion(@Observes(during = TransactionPhase.IN_PROGRESS) TxFailure txFailure) throws Exception {
        logEventFired(TransactionPhase.IN_PROGRESS);
        userTransaction.setRollbackOnly();
    }

}
