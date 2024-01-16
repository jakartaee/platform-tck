/*
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
package org.jboss.cdi.tck.tests.event.observer.priority.transactional;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.PERSISTENCE;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_NOTIFICATION;
import static org.jboss.cdi.tck.cdi.Sections.TRANSACTIONAL_OBSERVER_METHODS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import jakarta.enterprise.event.TransactionPhase;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Mark Paluch
 */
@Test(groups = { INTEGRATION, PERSISTENCE })
@SpecVersion(spec = "cdi", version = "2.0")
public class TransactionalPriorityObserverTest extends AbstractTest {

    private static final SimpleLogger logger = new SimpleLogger(TransactionalPriorityObserverTest.class);

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(TransactionalPriorityObserverTest.class)
                .withDefaultPersistenceXml().build();
    }

    @Inject
    OnlineAccountService accountService;

    @Test
    @SpecAssertions({ @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "a"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "b"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "c"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "d"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "e"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "gaa"),
            @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "b") })
    public void testSucessfulTransaction() throws Exception {

        logger.log("testSucessfulTransaction");
        ActionSequence.reset();

        // Checkpoint is right before tx commit
        accountService.withdrawSuccesTransaction(1);

        // Test sequence
        // BEFORE_COMPLETION must be fired at the beginning of the commit (after checkpoint)
        // AFTER_SUCCESS and AFTER_COMPLETION must be fired after BEFORE_COMPLETION
        // AFTER_FAILURE is not fired

        // Order:
        // IN_PROGRESS.SenderAccountTransactionObserver (DEFAULT)
        // IN_PROGRESS.ReceiverAccountTransactionObserver (APPLICATION + 1)
        // IN_PROGRESS.PhisherAccountTransactionObserver (APPLICATION + 500)

        // BEFORE_COMPLETION.ReceiverAccountTransactionObserver (APPLICATION - 1)
        // BEFORE_COMPLETION.SenderAccountTransactionObserver (DEFAULT)
        // BEFORE_COMPLETION.PhisherAccountTransactionObserver (APPLICATION + 200)

        List<String> phases = ActionSequence.getSequenceData();
        assertEquals(phases.size(), 13);

        assertEquals(phases.get(0),
                TransactionPhase.IN_PROGRESS.name() + "." + SenderAccountTransactionObserver.class.getSimpleName());
        assertEquals(phases.get(1),
                TransactionPhase.IN_PROGRESS.name() + "." + ReceiverAccountTransactionObserver.class.getSimpleName());
        assertEquals(phases.get(2),
                TransactionPhase.IN_PROGRESS.name() + "." + PhisherAccountTransactionObserver.class.getSimpleName());

        assertEquals(phases.get(3), "checkpoint");

        assertEquals(phases.get(4),
                TransactionPhase.BEFORE_COMPLETION.name() + "." + ReceiverAccountTransactionObserver.class.getSimpleName());
        assertEquals(phases.get(5),
                TransactionPhase.BEFORE_COMPLETION.name() + "." + SenderAccountTransactionObserver.class.getSimpleName());
        assertEquals(phases.get(6),
                TransactionPhase.BEFORE_COMPLETION.name() + "." + PhisherAccountTransactionObserver.class.getSimpleName());

        assertTrue(phases.contains(TransactionPhase.AFTER_SUCCESS.name() + "."
                + ReceiverAccountTransactionObserver.class.getSimpleName()));
        assertTrue(phases.contains(TransactionPhase.AFTER_SUCCESS.name() + "."
                + SenderAccountTransactionObserver.class.getSimpleName()));
        assertTrue(phases.contains(TransactionPhase.AFTER_SUCCESS.name() + "."
                + PhisherAccountTransactionObserver.class.getSimpleName()));

        assertTrue(phases.contains(TransactionPhase.AFTER_COMPLETION.name() + "."
                + ReceiverAccountTransactionObserver.class.getSimpleName()));
        assertTrue(phases.contains(TransactionPhase.AFTER_COMPLETION.name() + "."
                + SenderAccountTransactionObserver.class.getSimpleName()));
        assertTrue(phases.contains(TransactionPhase.AFTER_COMPLETION.name() + "."
                + PhisherAccountTransactionObserver.class.getSimpleName()));
    }

}
