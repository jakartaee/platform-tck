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
package org.jboss.cdi.tck.tests.implementation.enterprise.definition;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SESSION_BEAN;
import static org.jboss.cdi.tck.cdi.Sections.INSTANTIATION_EE;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEANS;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEAN_NAME;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEAN_TYPES;
import static org.testng.Assert.assertTrue;

import java.lang.annotation.Annotation;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Nicklas Karlsson
 * @author Martin Kouba
 */
@Test(groups = { INTEGRATION })
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseBeanDefinitionTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .build();
    }

    @Test
    @SpecAssertion(section = SESSION_BEANS, id = "b")
    public void testStatelessMustBeDependentScoped() {
        assert getBeans(GiraffeLocal.class).size() == 1;
        assert getBeans(GiraffeLocal.class).iterator().next().getScope().equals(Dependent.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = INSTANTIATION_EE, id = "a"), @SpecAssertion(section = INSTANTIATION_EE, id = "ab") })
    public void testConstructorAnnotatedInjectCalled() {
        ExplicitConstructor bean = getContextualReference(ExplicitConstructor.class);
        assert bean.getConstructorCalls() == 1;
        assert bean.getInjectedSimpleBean() instanceof SimpleBean;
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER)
    @SpecAssertion(section = INSTANTIATION_EE, id = "bb")
    public void testConstructorWithNoParamsUsed(NoParamConstructorSessionBean noParamConstructorSessionBean) {
        noParamConstructorSessionBean.ping();
        assertTrue(NoParamConstructorSessionBean.constructedCorrectly);
    }

    @Test
    @SpecAssertion(section = SESSION_BEANS, id = "c")
    public void testSingletonWithDependentScopeOK() {
        assert getBeans(Labrador.class).size() == 1;
    }

    @Test
    @SpecAssertion(section = SESSION_BEANS, id = "c")
    public void testSingletonWithApplicationScopeOK() {
        assert getBeans(Laika.class).size() == 1;
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SESSION_BEAN_TYPES, id = "aa"), @SpecAssertion(section = DECLARING_SESSION_BEAN, id = "c") })
    public void testBeanTypesAreLocalInterfacesWithoutWildcardTypesOrTypeVariablesWithSuperInterfaces() {
        Bean<DogLocal> dogBean = getBeans(DogLocal.class).iterator().next();
        assert dogBean.getTypes().contains(DogLocal.class);
        assert dogBean.getTypes().contains(PitbullLocal.class);
        assert !dogBean.getTypes().contains(Pitbull.class);
    }

    @Test
    @SpecAssertion(section = SESSION_BEAN_TYPES, id = "ba")
    public void testEnterpriseBeanClassLocalView() {
        Bean<Retriever> dogBean = getUniqueBean(Retriever.class);
        assert dogBean.getTypes().contains(Retriever.class);
        assert dogBean.getTypes().contains(Dog.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = SESSION_BEAN_TYPES, id = "c"), @SpecAssertion(section = DECLARING_SESSION_BEAN, id = "aa") })
    public void testObjectIsInAPITypes() {
        assert getBeans(GiraffeLocal.class).size() == 1;
        assert getBeans(GiraffeLocal.class).iterator().next().getTypes().contains(Object.class);
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SESSION_BEAN, id = "ba"), @SpecAssertion(section = SESSION_BEANS, id = "e") })
    public void testBeanWithScopeAnnotation() {
        Bean<LionLocal> lionBean = getBeans(LionLocal.class).iterator().next();
        assert lionBean.getScope().equals(RequestScoped.class);
    }

    @Test
    @SpecAssertion(section = DECLARING_SESSION_BEAN, id = "bb")
    public void testBeanWithNamedAnnotation() {
        Bean<MonkeyLocal> monkeyBean = getBeans(MonkeyLocal.class).iterator().next();
        assert monkeyBean.getName().equals("Monkey");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECLARING_SESSION_BEAN, id = "bd"), @SpecAssertion(section = SESSION_BEAN_NAME, id = "a") })
    public void testBeanWithStereotype() {
        Bean<PolarBearLocal> polarBearBean = getBeans(PolarBearLocal.class).iterator().next();
        assert polarBearBean.getScope().equals(RequestScoped.class);
        assert polarBearBean.getName().equals("polarBear");
    }

    @Test
    @SpecAssertion(section = DECLARING_SESSION_BEAN, id = "be")
    public void testBeanWithQualifiers() {
        Annotation tame = new Tame.Literal();
        Bean<ApeLocal> apeBean = getBeans(ApeLocal.class, tame).iterator().next();
        assert apeBean.getQualifiers().contains(tame);
    }

    @Test
    @SpecAssertion(section = SESSION_BEAN_NAME, id = "a")
    public void testDefaultName() {
        assert getBeans(PitbullLocal.class).size() == 1;
        assert getBeans(PitbullLocal.class).iterator().next().getName().equals("pitbull");
    }

}
