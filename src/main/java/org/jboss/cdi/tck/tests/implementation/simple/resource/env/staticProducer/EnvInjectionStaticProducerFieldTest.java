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
package org.jboss.cdi.tck.tests.implementation.simple.resource.env.staticProducer;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_RESOURCE;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.webapp30.WebAppDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EnvInjectionStaticProducerFieldTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EnvInjectionStaticProducerFieldTest.class)
                .withWebXml(
                        Descriptors
                                .create(WebAppDescriptor.class)
                                .createEnvEntry()
                                    .envEntryName("greeting")
                                    .envEntryType("java.lang.String")
                                    .envEntryValue("Hello there my friend").up())
                .build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = DECLARING_RESOURCE, id = "bb")
    public void testInjectionOfEnv() {
        Bean<GreetingBean> greetingBean = getBeans(GreetingBean.class).iterator().next();
        CreationalContext<GreetingBean> greetingBeanCc = getCurrentManager().createCreationalContext(greetingBean);
        GreetingBean instance = greetingBean.create(greetingBeanCc);
        assert instance.greet() != null;
        assert instance.greet().equals("Hello there my friend");
    }
}
