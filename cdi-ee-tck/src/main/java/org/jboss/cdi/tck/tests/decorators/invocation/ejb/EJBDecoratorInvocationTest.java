/*
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
package org.jboss.cdi.tck.tests.decorators.invocation.ejb;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BIZ_METHOD_EE;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_BEAN_EE;
import static org.testng.Assert.assertTrue;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author pmuir
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EJBDecoratorInvocationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(EJBDecoratorInvocationTest.class)
                .withClasses(PigSty.class, PigStyImpl.class, PigStyDecorator.class, Pig.class)
                .withBeansXml(
                        new BeansXml().decorators(PigStyDecorator.class))
                .build();
    }

    @Inject
    PigSty pigSty;

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = BIZ_METHOD_EE, id = "d"), @SpecAssertion(section = DECORATOR_BEAN_EE, id = "ab") })
    public void testEJBDecoratorInvocation() {
        PigStyDecorator.reset();
        PigStyImpl.reset();
        pigSty.clean();
        assertTrue(PigStyDecorator.isDecoratorCalled());
        assertTrue(PigStyImpl.isBeanCalled());
    }

}
