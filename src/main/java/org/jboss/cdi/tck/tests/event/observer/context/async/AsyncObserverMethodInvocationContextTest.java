/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.event.observer.context.async;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHOD_INVOCATION_CONTEXT;
import static org.jboss.cdi.tck.cdi.Sections.REQUEST_CONTEXT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.transaction.Status;
import jakarta.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class AsyncObserverMethodInvocationContextTest extends AbstractTest {

    @Inject
    UserTransaction userTransaction;

    @Inject
    Event<Message> event;

    @Inject
    Event<String> stringEvent;

    @Inject
    Counter counter;

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AsyncObserverMethodInvocationContextTest.class).build();
    }

    @Test
    @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT, id = "ab")
    public void testAsyncObserverIsCalledInNewTransactionContext() throws Exception {
        userTransaction.begin();
        BlockingQueue<Message> queue = new LinkedBlockingQueue<>();
        event.fireAsync(new Message()).thenAccept(queue::offer);
        Message message = queue.poll(2l, TimeUnit.SECONDS);
        assertNotNull(message);
        assertEquals(Status.STATUS_NO_TRANSACTION, AsyncMessageObserver.status.get());
        userTransaction.commit();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = OBSERVER_METHOD_INVOCATION_CONTEXT, id = "aa"), @SpecAssertion(section = REQUEST_CONTEXT, id = "da") })
    public void testAsyncObserverIsCalledInNewRequestContext() throws Exception {
        counter.increment();
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        stringEvent.fireAsync(new String()).thenAccept(queue::offer);
        String string = queue.poll(2l, TimeUnit.SECONDS);
        assertNotNull(string);
        assertTrue(AsyncMessageObserver.requestScopeActive.get());
        assertTrue(AsyncMessageObserver.counterIsZero.get());
    }

}
