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

package org.jboss.cdi.tck.interceptors.tests.bindings.resolution.ejb;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.BINDING_INT_TO_COMPONENT;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.INT_RESOLUTION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.InterceptionType;
import jakarta.enterprise.util.AnnotationLiteral;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Interceptor resolution test.
 *
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "interceptors", version = "1.2")
public class EnterpriseInterceptorBindingResolutionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EnterpriseInterceptorBindingResolutionTest.class)
                .withBeansXml(new BeansXml().interceptors(ComplicatedInterceptor.class, ComplicatedLifecycleInterceptor.class))
                .build();
    }

    @SuppressWarnings("serial")
    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = BINDING_INT_TO_COMPONENT, id = "e")
    @SpecAssertion(section = INT_RESOLUTION, id = "ca")
    @SpecAssertion(section = INT_RESOLUTION, id = "da")
    @SpecAssertion(section = INT_RESOLUTION, id = "db")
    @SpecAssertion(section = INT_RESOLUTION, id = "dc")
    public void testBusinessMethodInterceptorBindings(MessageService messageService, MonitorService monitorService) {

        // Test interceptor is resolved (note non-binding member of BallBinding)
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.AROUND_INVOKE,
                        new AnnotationLiteral<MessageBinding>() {
                        }, new AnnotationLiteral<LoggedBinding>() {
                        }, new AnnotationLiteral<TransactionalBinding>() {
                        }, new AnnotationLiteral<PingBinding>() {
                        }, new AnnotationLiteral<PongBinding>() {
                        }, new BallBindingLiteral(true, true)).size(), 1);

        // Test the set of interceptor bindings
        assertNotNull(messageService);
        ComplicatedInterceptor.reset();
        messageService.ping();
        assertTrue(ComplicatedInterceptor.intercepted);

        assertNotNull(monitorService);
        ComplicatedInterceptor.reset();
        monitorService.ping();
        assertFalse(ComplicatedInterceptor.intercepted);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INT_RESOLUTION, id = "cb")
    public void testTimeoutMethodInterceptorBindings(MessageService messageService) throws Exception {

        assertNotNull(messageService);
        ComplicatedInterceptor.reset();
        messageService.start();
        // Timeout is async
        StopCondition condition = new StopCondition() {
            public boolean isSatisfied() {
                return ComplicatedInterceptor.intercepted;
            }
        };
        Timer timer = new Timer().setDelay(3, TimeUnit.SECONDS).addStopCondition(condition).start();
        assertTrue(timer.isStopConditionsSatisfiedBeforeTimeout() || condition.isSatisfied());
    }

    @SuppressWarnings("serial")
    @Test
    @SpecAssertion(section = INT_RESOLUTION, id = "b")
    public void testLifecycleInterceptorBindings() throws Exception {

        // Test interceptor is resolved (note non-binding member of BallBinding)
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.POST_CONSTRUCT,
                        new AnnotationLiteral<MessageBinding>() {
                        }, new AnnotationLiteral<LoggedBinding>() {
                        }, new AnnotationLiteral<TransactionalBinding>() {
                        }, new BasketBindingLiteral(true, true)).size(), 1);
        assertEquals(
                getCurrentManager().resolveInterceptors(InterceptionType.PRE_DESTROY, new AnnotationLiteral<MessageBinding>() {
                }, new AnnotationLiteral<LoggedBinding>() {
                }, new AnnotationLiteral<TransactionalBinding>() {
                }, new BasketBindingLiteral(true, true)).size(), 1);

        // Test the set of interceptor bindings
        ComplicatedLifecycleInterceptor.reset();

        Bean<RemoteMessageService> bean = getUniqueBean(RemoteMessageService.class);
        CreationalContext<RemoteMessageService> ctx = getCurrentManager().createCreationalContext(bean);
        RemoteMessageService remoteMessageService = bean.create(ctx);
        remoteMessageService.ping();
        bean.destroy(remoteMessageService, ctx);

        assertTrue(ComplicatedLifecycleInterceptor.postConstructCalled);
        assertTrue(ComplicatedLifecycleInterceptor.preDestroyCalled);
    }

}
