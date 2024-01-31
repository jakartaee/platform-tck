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
package org.jboss.cdi.tck.tests.event.observer.context;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import javax.naming.InitialContext;
import jakarta.transaction.TransactionSynchronizationRegistry;

import org.jboss.cdi.tck.util.ActionSequence;

@Dependent
public class FooObserver {

    @EJB
    private Toner toner;

    @EJB
    private Printer printer;

    private static TransactionSynchronizationRegistry tsr;
    
    public void observeInProgress(@Observes(during = TransactionPhase.IN_PROGRESS) Foo foo) throws Exception {
        // this observer method is called first and we do not have to look up TransactionSynchronizationRegistry each time
        if (tsr == null) {
            tsr = (TransactionSynchronizationRegistry) InitialContext.doLookup("java:comp/TransactionSynchronizationRegistry");
        }
        assertEquals(tsr.getTransactionKey(), Printer.getKey(),
                "Non-transactional observer method was NOT called in the same transaction context as the invocation of Event.fire()");
        assertClientSecurityContext(TransactionPhase.IN_PROGRESS);
    }

    public void observeBeforeCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) Foo foo) throws Exception {
        assertEquals(tsr.getTransactionKey(), Printer.getKey(),
                "Before completion transactional observer method was NOT called within the context of the transaction that was about to complete.");
        assertClientSecurityContext(TransactionPhase.BEFORE_COMPLETION);
    }

    public void observeAfterCompletion(@Observes(during = TransactionPhase.AFTER_COMPLETION) Foo foo) throws Exception {
        assertClientSecurityContext(TransactionPhase.AFTER_COMPLETION);
    }

    public void observeAfterFailure(@Observes(during = TransactionPhase.AFTER_FAILURE) Foo foo) throws Exception {
        assertClientSecurityContext(TransactionPhase.AFTER_FAILURE);
    }

    public void observeAfterSuccess(@Observes(during = TransactionPhase.AFTER_SUCCESS) Foo foo) throws Exception {
        assertClientSecurityContext(TransactionPhase.AFTER_SUCCESS);
    }

    private void assertClientSecurityContext(TransactionPhase phase) {

        toner.spill();
        try {
            printer.tryAccess();
            fail("Transactional observer method was NOT called within the same client security context.");
        } catch (jakarta.ejb.EJBAccessException expected) {
            // OK
        }

        ActionSequence.addAction(phase.toString());
    }
}
