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
package org.jboss.cdi.tck.tests.event.observer.context.enterprise.staticMethod;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import jakarta.annotation.security.DeclareRoles;
import jakarta.ejb.EJBContext;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.event.TransactionPhase;
import javax.naming.InitialContext;
import jakarta.transaction.TransactionSynchronizationRegistry;

import org.jboss.cdi.tck.util.ActionSequence;

@Stateless
@DeclareRoles({ "student", "printer" })
public class FooObserver {

    private static TransactionSynchronizationRegistry tsr;
    private static EJBContext ejbContext;

    private static Toner toner;
    private static Printer printer;

    public static void observeInProgress(@Observes(during = TransactionPhase.IN_PROGRESS) Foo foo) throws Exception {
        // this observer method is called first -> it is sufficient to lookup the needed resources just once
        if (tsr == null) {
            init();
        }

        assertEquals(tsr.getTransactionKey(), Printer.getKey(),
                "Non-transactional observer method was NOT called in the same transaction context as the invocation of Event.fire()");
        assertClientSecurityContext(TransactionPhase.IN_PROGRESS);
    }

    private static void init() throws Exception {
        tsr = (TransactionSynchronizationRegistry) InitialContext.doLookup("java:comp/TransactionSynchronizationRegistry");
        ejbContext = (EJBContext) InitialContext.doLookup("java:comp/EJBContext");
        toner = (Toner) InitialContext.doLookup("java:module/Toner");
        printer = (Printer) InitialContext.doLookup("java:module/Printer");
    }

    public static void observeBeforeCompletion(@Observes(during = TransactionPhase.BEFORE_COMPLETION) Foo foo) throws Exception {
        assertEquals(tsr.getTransactionKey(), Printer.getKey(),
                "Before completion transactional observer method was NOT called within the context of the transaction that was about to complete.");
        assertClientSecurityContext(TransactionPhase.BEFORE_COMPLETION);
    }

    public static void observeAfterCompletion(@Observes(during = TransactionPhase.AFTER_COMPLETION) Foo foo) throws Exception {
        assertClientSecurityContext(TransactionPhase.AFTER_COMPLETION);
    }

    public static void observeAfterFailure(@Observes(during = TransactionPhase.AFTER_FAILURE) Foo foo) throws Exception {
        assertClientSecurityContext(TransactionPhase.AFTER_FAILURE);
    }

    public static void observeAfterSuccess(@Observes(during = TransactionPhase.AFTER_SUCCESS) Foo foo) throws Exception {
        assertClientSecurityContext(TransactionPhase.AFTER_SUCCESS);
    }

    private static void assertClientSecurityContext(TransactionPhase phase) {
        assertTrue(ejbContext.isCallerInRole("student"));
        assertTrue(!ejbContext.isCallerInRole("printer"));

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
