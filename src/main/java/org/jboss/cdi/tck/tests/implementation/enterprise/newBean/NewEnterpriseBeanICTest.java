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
package org.jboss.cdi.tck.tests.implementation.enterprise.newBean;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.NEW_EE;

import java.util.Set;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.New;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class NewEnterpriseBeanICTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NewEnterpriseBeanICTest.class)
                .withExcludedClass(NewEnterpriseBeanTest.class.getName()).build();
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "l")
    public void testNewBeanHasSameConstructor() {
        ExplicitConstructor newBean = getContextualReference(ExplicitConstructor.class, New.Literal.of(
                ExplicitConstructorSessionBean.class));
        assert newBean.getConstructorCalls() == 1;
        assert newBean.getInjectedSimpleBean() != null;
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "m")
    public void testNewBeanHasSameInitializers() {
        InitializerSimpleBeanLocal bean = getContextualReference(InitializerSimpleBeanLocal.class);
        InitializerSimpleBeanLocal newBean = getContextualReference(InitializerSimpleBeanLocal.class, New.Literal.of(
                InitializerSimpleBean.class));
        assert bean != newBean;
        assert bean.getInitializerCalls() == 2;
    }

    /**
     * Sets up both the bean and the @New bean with different configurations so that the correct producer method used can be
     * determined.
     * 
     * @throws Exception
     */
    @Test
    @SpecAssertion(section = NEW_EE, id = "v")
    public void testNewBeanHasNoProducerMethods() throws Exception {
        FoxLocal fox = getContextualReference(FoxLocal.class);
        FoxLocal newFox = getContextualReference(FoxLocal.class, New.Literal.of(Fox.class));
        fox.setNextLitterSize(3);
        newFox.setNextLitterSize(5);
        @SuppressWarnings("serial")
        Litter theOnlyLitter = getContextualReference(Litter.class, new AnnotationLiteral<Tame>() {
        });
        assert theOnlyLitter.getQuantity() == fox.getNextLitterSize();
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "x")
    public void testNewBeanHasNoDisposalMethods() throws Exception {
        FoxLocal fox = getContextualReference(FoxLocal.class);
        FoxLocal newFox = getContextualReference(FoxLocal.class, New.Literal.of(Fox.class));
        @SuppressWarnings("serial")
        Set<Bean<Litter>> beans = getBeans(Litter.class, new AnnotationLiteral<Tame>() {
        });
        assert beans.size() == 1;
        Bean<Litter> litterBean = beans.iterator().next();
        CreationalContext<Litter> creationalContext = getCurrentManager().createCreationalContext(litterBean);
        Litter litter = litterBean.create(creationalContext);
        litterBean.destroy(litter, creationalContext);
        assert fox.isLitterDisposed();
        assert !newFox.isLitterDisposed();
    }

}
