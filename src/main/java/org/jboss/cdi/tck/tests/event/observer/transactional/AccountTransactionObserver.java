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
package org.jboss.cdi.tck.tests.event.observer.transactional;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import jakarta.transaction.UserTransaction;

import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * 
 * @author Martin Kouba
 */
@Dependent
public class AccountTransactionObserver {

    private static final SimpleLogger logger = new SimpleLogger(AccountTransactionObserver.class);

    @Resource
    private UserTransaction userTransaction;

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterSuccess(@Observes(during = TransactionPhase.AFTER_SUCCESS) Withdrawal withdrawal) throws Exception {
        logEventFired(TransactionPhase.AFTER_SUCCESS);
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterCompletion(@Observes(during = TransactionPhase.AFTER_COMPLETION) Withdrawal withdrawal)
            throws Exception {
        logEventFired(TransactionPhase.AFTER_COMPLETION);
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawBeforeCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) Withdrawal withdrawal)
            throws Exception {
        logEventFired(TransactionPhase.BEFORE_COMPLETION);
    }

    /**
     * Always fire immediately.
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawNoTx(@Observes(during = TransactionPhase.IN_PROGRESS) Withdrawal withdrawal) throws Exception {
        logEventFired(TransactionPhase.IN_PROGRESS);
    }

    /**
     * 
     * @param withdrawal
     * @throws Exception
     */
    public void withdrawAfterFailure(@Observes(during = TransactionPhase.AFTER_FAILURE) Withdrawal withdrawal) throws Exception {
        logEventFired(TransactionPhase.AFTER_FAILURE);
    }

    /**
     * 
     * @param failure
     * @throws Exception
     */
    public void failBeforeCompletion(@Observes(during = TransactionPhase.IN_PROGRESS) Failure failure) throws Exception {
        logEventFired(TransactionPhase.IN_PROGRESS);
        userTransaction.setRollbackOnly();
    }

    private void logEventFired(TransactionPhase phase) {
        logger.log(phase.toString());
        ActionSequence.addAction(phase.toString());
    }
}
