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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.environment;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INTERCEPTOR_ENVIRONMENT;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Matus Abaffy
 */
@SpecVersion(spec = "interceptors", version = "1.2")
public class InterceptorEnvironmentTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(InterceptorEnvironmentTest.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = JAVAEE_FULL)
    @SpecAssertion(section = INTERCEPTOR_ENVIRONMENT, id = "b")
    public void testSameThread(Bar bar) throws InterruptedException {
        assertEquals(bar.ping(), 2);

        bar.createTimer();
        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return TestEndObserver.toBeDestroyed.get();
            }
        }).start();
        assertNotNull(Bar.timeoutAt);
        assertTrue(AroundTimeoutThreadInterceptor.threadOK.get());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INTERCEPTOR_ENVIRONMENT, id = "c")
    public void testInvocationContext(Foo foo) {
        final int input = 5;
        final int result = ((2 * input) + 3) * 3;
        assertEquals(foo.count(input), result);
        assertFalse(Foo.called);
    }
}
