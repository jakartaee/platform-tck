/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.context.conversation.inactive;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.CONVERSATION;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.TestGroups;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * As "The conversation scope is active during all Servlet requests", this test needs to use some EE functionality (currently
 * EJB Timer service) and thus be placed in {@link TestGroups#JAVAEE_FULL}.
 *
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class InactiveConversationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InactiveConversationTest.class).build();
    }

    @Inject
    FooBean foo;

    @Inject
    Bar bar;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = CONVERSATION, id = "p")
    public void testContextNotActiveExceptionThrown() throws Exception {

        assertNotNull(foo);
        // Conversation scope is not active during EJB timeout method
        foo.createTimer();

        StopCondition condition = new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return bar.isContextNotActiveExceptionThrown();
            }
        };
        Timer timer = new Timer().setDelay(1000).addStopCondition(condition).start();
        assertTrue(timer.isStopConditionsSatisfiedBeforeTimeout() || condition.isSatisfied());
    }
}
