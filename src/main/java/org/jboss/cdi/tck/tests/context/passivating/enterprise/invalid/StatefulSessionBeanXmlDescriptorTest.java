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
package org.jboss.cdi.tck.tests.context.passivating.enterprise.invalid;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_EE;

import jakarta.enterprise.inject.spi.DeploymentException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans11.BeansDescriptor;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class StatefulSessionBeanXmlDescriptorTest extends AbstractTest {

    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(StatefulSessionBeanXmlDescriptorTest.class)
                .withClasses(Digital.class, DigitalInterceptor.class, Elephant.class, ElephantLocal.class)
                .withBeansXml(new BeansXml().interceptors(DigitalInterceptor.class))
                .build()
                .addAsWebInfResource(StatefulSessionBeanXmlDescriptorTest.class.getPackage(), "ejb-jar.xml", "ejb-jar.xml");
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = PASSIVATION_CAPABLE_EE, id = "ab")
    public void testDeployment() {
        // only verify deployment
    }
}
