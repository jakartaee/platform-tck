/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundTimeout;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.SECURITY;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.TIMEOUT_METHOD_INT_METHODS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

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
public class AroundTimeoutInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(AroundTimeoutInterceptorTest.class).build();
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = JAVAEE_FULL)
    @SpecAssertion(section = TIMEOUT_METHOD_INT_METHODS, id = "f")
    @SpecAssertion(section = TIMEOUT_METHOD_INT_METHODS, id = "ea")
    public void testInvocationContextGetTimer(TimingBean timingBean) throws Exception {
        timingBean.createTimer();
        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return TimingBean.timeoutAt != null;
            }
        }).start();

        assertNotNull(TimingBean.timeoutAt);
        assertTrue(TimeoutInterceptor.timerOK);
        assertEquals(TimeoutInterceptor.key, TimingBean.key,
                "Around-timeout method invocation did NOT occur within the same transaction context as the timeout method on which it is interposing");
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = {JAVAEE_FULL, SECURITY})
    @SpecAssertion(section = TIMEOUT_METHOD_INT_METHODS, id = "d")
    @SpecAssertion(section = TIMEOUT_METHOD_INT_METHODS, id = "eb")
    public void testSecurityContext(Student student) throws Exception {
        student.sleep();
        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return Alarm.timeoutAt != null;
            }
        }).start();
        assertNotNull(Alarm.timeoutAt.get());

        new Timer().setDelay(5, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
            @Override
            public boolean isSatisfied() {
                return TestData.preDestroyed.get();
            }
        }).start();

        assertTrue(TestData.securityContextOk.get());
        assertTrue(TestData.calledFromInterceptor.get());
    }
}
