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
 * @author Martin Kouba
 */
@Test(groups = { INTEGRATION, PERSISTENCE })
@SpecVersion(spec = "cdi", version = "2.0")
public class TransactionalObserverTest extends AbstractTest {

    private static final SimpleLogger logger = new SimpleLogger(TransactionalObserverTest.class);

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(TransactionalObserverTest.class).withDefaultPersistenceXml()
                .build();
    }

    @Inject
    AccountService accountService;

    @Test
    @SpecAssertions({ @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "a"), @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "b"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "c"), @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "d"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "e"), @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "gaa"),
            @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "b") })
    public void testSucessfullTransaction() throws Exception {

        logger.log("testSucessfullTransaction");
        ActionSequence.reset();

        // Checkpoint is right before tx commit
        accountService.withdrawSuccesTransaction(1);

        // Test sequence
        // BEFORE_COMPLETION must be fired at the beginning of the commit (after checkpoint)
        // AFTER_SUCCESS and AFTER_COMPLETION must be fired after BEFORE_COMPLETION
        // AFTER_FAILURE is not fired
        List<String> phases = ActionSequence.getSequenceData();
        assertEquals(phases.size(), 5);
        assertEquals(phases.get(0), TransactionPhase.IN_PROGRESS.name());
        assertEquals(phases.get(1), "checkpoint");
        assertEquals(phases.get(2), TransactionPhase.BEFORE_COMPLETION.name());
        assertTrue(phases.contains(TransactionPhase.AFTER_SUCCESS.name()));
        assertTrue(phases.contains(TransactionPhase.AFTER_COMPLETION.name()));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "a"), @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "b"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "c"), @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "d"),
            @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "e"), @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "gaa"),
            @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "b") })
    public void testFailedTransaction() throws Exception {

        logger.log("testFailedTransaction");
        ActionSequence.reset();

        // Checkpoint is right before tx rollback
        accountService.withdrawFailedTransaction(2);

        // AFTER_FAILURE and AFTER_COMPLETION must be fired after checkpoint
        // AFTER_SUCCESS and BEFORE_COMPLETION is not fired
        List<String> phases = ActionSequence.getSequenceData();
        assertEquals(phases.size(), 4);
        assertEquals(phases.get(0), TransactionPhase.IN_PROGRESS.name());
        assertEquals(phases.get(1), "checkpoint");
        assertTrue(phases.contains(TransactionPhase.AFTER_FAILURE.name()));
        assertTrue(phases.contains(TransactionPhase.AFTER_COMPLETION.name()));
    }

    /**
     * No transaction - send all events immediately.
     *
     * @throws Exception
     */
    @Test
    @SpecAssertions({ @SpecAssertion(section = TRANSACTIONAL_OBSERVER_METHODS, id = "a"), @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "b") })
    public void testNoTransaction() throws Exception {

        logger.log("testNoTransaction");
        ActionSequence.reset();

        // Checkpoint is after event send
        accountService.withdrawNoTransaction(3);

        // No TX is active - all events are fired immediately and thus before checkpoint
        List<String> sequence = ActionSequence.getSequenceData();
        assertEquals(sequence.size(), 6);
        assertEquals(sequence.get(sequence.size() - 1), "checkpoint");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_NOTIFICATION, id = "bda") })
    public void testObserverFailedTransaction() throws Exception {

        logger.log("testObserverFailedTransaction");
        ActionSequence.reset();

        accountService.withdrawObserverFailedTransaction(2);

        // IN_PROGRESS is fired twice
        // AFTER_FAILURE and AFTER_COMPLETION must be fired after checkpoint
        // AFTER_SUCCESS and BEFORE_COMPLETION is not fired
        List<String> phases = ActionSequence.getSequenceData();
        assertEquals(phases.size(), 5);
        assertEquals(phases.get(0), TransactionPhase.IN_PROGRESS.name());
        assertEquals(phases.get(1), TransactionPhase.IN_PROGRESS.name());
        assertEquals(phases.get(2), "checkpoint");
        assertTrue(phases.contains(TransactionPhase.AFTER_FAILURE.name()));
        assertTrue(phases.contains(TransactionPhase.AFTER_COMPLETION.name()));
    }

}
