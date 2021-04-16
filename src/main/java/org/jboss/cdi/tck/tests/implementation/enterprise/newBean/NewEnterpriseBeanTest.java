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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.New;
import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import java.lang.reflect.Type;
import java.util.Set;

@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "2.0")
public class NewEnterpriseBeanTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(NewEnterpriseBeanTest.class)
                .withExcludedClasses(Tiger.class.getName(), Spell.class.getName(), Staff.class.getName(),
                        Dragon.class.getName(), NewEnterpriseBeanICTest.class.getName(), Hat.class.getName(),
                        Fireball.class.getName())
                .withLibrary(Tiger.class, Spell.class, Staff.class, Dragon.class, Hat.class, Fireball.class)
                .withBeansXml(new BeansXml().interceptors(OrderInterceptor.class)).build();
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "p")
    public void testNewBeanIsDependentScoped() {
        Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, New.Literal.of(
                WrappedEnterpriseBean.class));
        assert beans.size() == 1;
        Bean<WrappedEnterpriseBeanLocal> newEnterpriseBean = beans.iterator().next();
        assert Dependent.class.equals(newEnterpriseBean.getScope());
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "r")
    public void testNewBeanHasOnlyOneQualifier() {
        Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, New.Literal.of(
                WrappedEnterpriseBean.class));
        assert beans.size() == 1;
        Bean<WrappedEnterpriseBeanLocal> newEnterpriseBean = beans.iterator().next();
        assert newEnterpriseBean.getQualifiers().size() == 1;
        assert newEnterpriseBean.getQualifiers().iterator().next().annotationType().equals(New.class);
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "s")
    public void testNewBeanHasNoBeanELName() {
        Set<Bean<WrappedEnterpriseBeanLocal>> beans = getBeans(WrappedEnterpriseBeanLocal.class, New.Literal.of(
                WrappedEnterpriseBean.class));
        assert beans.size() == 1;
        Bean<WrappedEnterpriseBeanLocal> newEnterpriseBean = beans.iterator().next();
        assert newEnterpriseBean.getName() == null;
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "t")
    public void testNewBeanHasNoStereotypes() {
        Bean<MonkeyLocal> monkeyBean = getBeans(MonkeyLocal.class).iterator().next();
        Bean<MonkeyLocal> newMonkeyBean = getBeans(MonkeyLocal.class, New.Literal.of(Monkey.class)).iterator().next();
        assert monkeyBean.getScope().equals(RequestScoped.class);
        assert newMonkeyBean.getScope().equals(Dependent.class);
        assert monkeyBean.getName().equals("monkey");
        assert newMonkeyBean.getName() == null;
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "u")
    public void testNewBeanHasNoObservers() {
        // Should just be 1 observer from bean, not new bean
        assert getCurrentManager().resolveObserverMethods("event").size() == 1;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = NEW_EE, id = "j"), @SpecAssertion(section = NEW_EE, id = "k") })
    public void testForEachEnterpriseBeanANewBeanExists() {
        Bean<OrderLocal> orderBean = getBeans(OrderLocal.class).iterator().next();
        Set<Bean<OrderLocal>> newOrderBeans = getBeans(OrderLocal.class, New.Literal.of(Order.class));
        assert newOrderBeans.size() == 1;
        Bean<OrderLocal> newOrderBean = newOrderBeans.iterator().next();
        assert orderBean.getQualifiers().size() == 2;
        assert orderBean.getQualifiers().contains(Default.Literal.INSTANCE);
        assert orderBean.getQualifiers().contains(Any.Literal.INSTANCE);
        assert orderBean.getTypes().equals(newOrderBean.getTypes());
        assert orderBean.getBeanClass().equals(newOrderBean.getBeanClass());
        assert newOrderBean.getQualifiers().size() == 1;
        assert newOrderBean.getQualifiers().iterator().next().annotationType().equals(New.class);

        Set<Bean<LionLocal>> lionBeans = getBeans(LionLocal.class, TameLiteral.INSTANCE);
        Set<Bean<LionLocal>> newLionBeans = getBeans(LionLocal.class, New.Literal.of(Lion.class));
        assert lionBeans.size() == 1;
        assert newLionBeans.size() == 1;
        Bean<LionLocal> lionBean = lionBeans.iterator().next();
        Bean<LionLocal> newLionBean = newLionBeans.iterator().next();
        assert lionBean.getQualifiers().size() == 2;
        assert lionBean.getQualifiers().contains(TameLiteral.INSTANCE);
        assert lionBean.getQualifiers().contains(Any.Literal.INSTANCE);
        assert newLionBean.getQualifiers().size() == 1;
        assert newLionBean.getQualifiers().iterator().next().annotationType().equals(New.class);
        assert lionBean.getTypes().equals(newLionBean.getTypes());
        assert lionBean.getBeanClass().equals(newLionBean.getBeanClass());
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "n")
    public void testNewBeanHasSameInjectedFields() {
        Bean<InitializerSimpleBeanLocal> simpleBean = getBeans(InitializerSimpleBeanLocal.class).iterator().next();
        Bean<InitializerSimpleBeanLocal> newSimpleBean = getBeans(InitializerSimpleBeanLocal.class,
                New.Literal.of(InitializerSimpleBean.class)).iterator().next();
        assert !newSimpleBean.getInjectionPoints().isEmpty();
        assert simpleBean.getInjectionPoints().equals(newSimpleBean.getInjectionPoints());
    }

    @Test
    @SpecAssertion(section = NEW_EE, id = "o")
    public void testNewBeanHasTheSameInterceptorBindings() {
        // Method foo() is intercepted
        assertTrue(getContextualReference(NewSessionBeanConsumer.class).getOrder().ping());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = NEW_EE, id = "yb") })
    public void testNewBeanCreatedForFieldInjectionPoint(Wizard wizard) {
        Bean<Tiger> bean = getUniqueBean(Tiger.class, New.Literal.INSTANCE);
        checkNewQualifiedBean(bean, Object.class, Tiger.class);
        assertTrue(wizard.getTiger().ping());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = NEW_EE, id = "yd") })
    public void testNewBeanCreatedForInitializerInjectionPoint(Wizard wizard) {
        Bean<Staff> bean = getUniqueBean(Staff.class, New.Literal.INSTANCE);
        checkNewQualifiedBean(bean, Object.class, Staff.class);
        assertTrue(wizard.getStaff().ping());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = NEW_EE, id = "yf") })
    public void testNewBeanCreatedForConstructorInjectionPoint(Wizard wizard) {
        Bean<Spell> bean = getUniqueBean(Spell.class, New.Literal.INSTANCE);
        checkNewQualifiedBean(bean, Object.class, Spell.class);
        assertTrue(wizard.getSpell().ping());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = NEW_EE, id = "yh") })
    public void testNewBeanCreatedForProducerMethod(Wizard wizard) {
        Bean<Dragon> bean = getUniqueBean(Dragon.class, New.Literal.INSTANCE);
        checkNewQualifiedBean(bean, Object.class, Dragon.class);
        assertTrue(wizard.getDragon().ping());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = NEW_EE, id = "yj") })
    public void testNewBeanCreatedForObserverMethod(Wizard wizard) {
        Bean<Hat> bean = getUniqueBean(Hat.class, New.Literal.INSTANCE);
        checkNewQualifiedBean(bean, Object.class, Hat.class);
        getCurrentManager().fireEvent(new Wizard());
        assertTrue(wizard.getHat().ping());
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertions({ @SpecAssertion(section = NEW_EE, id = "yl") })
    public void testNewBeanCreatedForDisposerMethod(DragonProducer producer) {

        Bean<Fireball> bean = getUniqueBean(Fireball.class, New.Literal.INSTANCE);
        checkNewQualifiedBean(bean, Object.class, Fireball.class);

        Bean<Dragon> dragonBean = getUniqueBean(Dragon.class, TameLiteral.INSTANCE);
        CreationalContext<Dragon> ctx = getCurrentManager().createCreationalContext(dragonBean);
        Dragon dragon = dragonBean.create(ctx);
        dragonBean.destroy(dragon, ctx);

        assertTrue(producer.isDragonDestroyed());
    }

    private <T> void checkNewQualifiedBean(Bean<T> bean, Type... requiredTypes) {
        assertTrue(typeSetMatches(bean.getTypes(), requiredTypes));
        // Has scope @Dependent,
        assertEquals(bean.getScope(), Dependent.class);
        // Has exactly one qualifier...
        assertTrue(annotationSetMatches(bean.getQualifiers(), New.Literal.of(bean.getBeanClass())));
        // Has no bean EL name
        assertNull(bean.getName());
        // Has no stereotypes
        assertTrue(bean.getStereotypes() == null || bean.getStereotypes().isEmpty());
        // Is not an alternative
        assertFalse(bean.isAlternative());
    }
}
