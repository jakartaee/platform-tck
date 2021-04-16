/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.cdi.tck.tests.lookup.modules;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.INTER_MODULE_INJECTION;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test that bean in web module can inject selected alternative managed bean from EJB module.
 * 
 * classloading requirements)!
 * 
 * Also note that we need to enable alternative in web module to have it working according to spec (section 5.1.4 Inter-module
 * injection).
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class SelectedAlternativeManagedBeanInjectionAvailabilityTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder().noDefaultWebModule()
                .withTestClassDefinition(SelectedAlternativeManagedBeanInjectionAvailabilityTest.class)
                .withClasses(AlternativeFoo.class).withBeanLibrary(Foo.class, Bar.class).build();

        enterpriseArchive.addAsModule(new WebArchiveBuilder()
                .notTestArchive()
                .withDefaultEjbModuleDependency()
                .withBeansXml(new BeansXml().alternatives(AlternativeFoo.class))
                .withClasses(SelectedAlternativeManagedBeanInjectionAvailabilityTest.class, WebBar.class)
                .build());

        return enterpriseArchive;
    }

    @Inject
    Bar bar;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = INTER_MODULE_INJECTION, id = "o")
    public void testInjection() throws Exception {
        Assert.assertEquals(bar.ping(), 42);
    }

}
