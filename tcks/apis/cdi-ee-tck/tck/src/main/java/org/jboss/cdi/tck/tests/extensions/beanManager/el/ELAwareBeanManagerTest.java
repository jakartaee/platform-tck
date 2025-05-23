/*
 * Copyright (c) 2025 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * Apache Software License 2.0 which is available at:
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.cdi.tck.tests.extensions.beanManager.el;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.cdi.Sections;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.enterprise.inject.spi.el.ELAwareBeanManager;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;

@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "4.1")
public class ELAwareBeanManagerTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ELAwareBeanManagerTest.class).build();
    }

    @Test
    @SpecAssertion(section = Sections.EL_INTEGRATION_API, id = "a")
    public void testBeanManagerImplementsELAwareBeanManager() {
        assert getCurrentManager() instanceof ELAwareBeanManager;
    }

    @Test
    @SpecAssertion(section = Sections.EL_INTEGRATION_API, id = "ba")
    public void testContainerProvidesELAwareBeanManagerBean() {
        assert getBeans(ELAwareBeanManager.class).size() > 0;
    }

    @Test
    @SpecAssertion(section = Sections.EL_INTEGRATION_API, id = "bb")
    public void testELAwareBeanManagerBeanIsDependentScoped() {
        Bean<ELAwareBeanManager> beanManager = getBeans(ELAwareBeanManager.class).iterator().next();
        assert beanManager.getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertion(section = Sections.EL_INTEGRATION_API, id = "bc")
    public void testELAwareBeanManagerBeanHasCurrentBinding() {
        Bean<ELAwareBeanManager> beanManager = getBeans(ELAwareBeanManager.class).iterator().next();
        assert beanManager.getQualifiers().contains(Default.Literal.INSTANCE);
    }
    
    @Test
    @SpecAssertion(section = Sections.EL_INTEGRATION_API, id = "c")
    public void testELAwareBeanManagerCastFromCdiCurrent() {
        assert CDI.current().getBeanManager() instanceof ELAwareBeanManager;
    }
}
