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
package org.jboss.cdi.tck.tests.implementation.enterprise.lifecycle;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_EE;
import static org.jboss.cdi.tck.cdi.Sections.CONTEXTUAL_REFERENCE;
import static org.jboss.cdi.tck.cdi.Sections.DEPENDENT_OBJECTS_DESTRUCTION_EE;
import static org.jboss.cdi.tck.cdi.Sections.INITIALIZER_METHODS_EE;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE_EE;
import static org.jboss.cdi.tck.cdi.Sections.PASSIVATION_CAPABLE_DEPENDENCY_EE;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEAN_EJB_REMOVE_METHOD;
import static org.jboss.cdi.tck.cdi.Sections.STATEFUL_LIFECYCLE;
import static org.jboss.cdi.tck.cdi.Sections.STATELESS_LIFECYCLE;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.CreationalContext;
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
 * Sections
 *
 * 6.5. Lifecycle of stateful session beans 6.6. Lifecycle of stateless session and singleton beans 6.11. Lifecycle of EJBs
 *
 * Mostly overlapping with other tests...
 *
 * @author Nicklas Karlsson
 * @author David Allen
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanLifecycleTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(EnterpriseBeanLifecycleTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .build();
    }

    @Test(groups =  INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = STATEFUL_LIFECYCLE, id = "bb"), @SpecAssertion(section = CONTEXTUAL_REFERENCE, id = "b"),
            @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "ja") })
    public void testCreateSFSB() throws Exception {
        GrossStadt frankfurt = getContextualReference(GrossStadt.class);
        Bean<KleinStadt> stadtBean = getBeans(KleinStadt.class).iterator().next();
        assert stadtBean != null : "Expected a bean for stateful session bean Kassel";
        KleinStadt stadtInstance = getContextualReference(KleinStadt.class, new Important.Literal());
        assert stadtInstance != null : "Expected instance to be created by container";
        assert frankfurt.isKleinStadtCreated() : "PostConstruct should be invoked when bean instance is created";
        frankfurt.resetCreatedFlags();

        // Create a second one to make sure create always does create a new session bean
        KleinStadt anotherStadtInstance = getContextualReference(KleinStadt.class, new Important.Literal());
        assert anotherStadtInstance != null : "Expected second instance of session bean";
        assert frankfurt.isKleinStadtCreated();
        assert anotherStadtInstance != stadtInstance : "create() should not return same bean as before";

        stadtInstance.setName("hometown");
        assert "hometown".equals(stadtInstance.getName());

        // Verify that the instance returned is a proxy by checking for all local interfaces
        assert getCurrentConfiguration().getBeans().isProxy(stadtInstance);
        assert stadtInstance instanceof KleinStadt;
        assert stadtInstance instanceof SchoeneStadt;
    }

    @Test(groups =  INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = PASSIVATION_CAPABLE_DEPENDENCY_EE, id = "ac") })
    public void testSerializeSFSB() throws Exception {

        KleinStadt stadtInstance = getContextualReference(KleinStadt.class, new Important.Literal());

        byte[] bytes = passivate(stadtInstance);
        Object object = activate(bytes);
        stadtInstance = (KleinStadt) object;
        assert getCurrentConfiguration().getBeans().isProxy(stadtInstance);

    }

    @Test(groups =  INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = STATEFUL_LIFECYCLE, id = "bc"), @SpecAssertion(section = STATELESS_LIFECYCLE, id = "c") })
    public void testDestroyRemovesSFSB() throws Exception {
        GrossStadt frankfurt = getContextualReference(GrossStadt.class);
        Bean<KleinStadt> stadtBean = getBeans(KleinStadt.class).iterator().next();
        assert stadtBean != null : "Expected a bean for stateful session bean Kassel";
        Context requestContext = getCurrentManager().getContext(RequestScoped.class);
        CreationalContext<KleinStadt> creationalContext = getCurrentManager().createCreationalContext(stadtBean);
        KleinStadt kassel = stadtBean.create(creationalContext);
        kassel.ping();
        stadtBean.destroy(kassel, creationalContext);

        assert frankfurt.isKleinStadtDestroyed() : "Expected SFSB bean to be destroyed";
        kassel = requestContext.get(stadtBean);
        assert kassel == null : "SFSB bean should not exist after being destroyed";
        // frankfurt.dispose();
    }

    @Test(groups =  INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = STATELESS_LIFECYCLE, id = "c"), @SpecAssertion(section = SESSION_BEAN_EJB_REMOVE_METHOD, id = "dba") })
    public void testRemovedEjbIgnored() {
        KleinStadt stadtInstance = getContextualReference(KleinStadt.class, new Important.Literal());
        assert stadtInstance != null : "Expected instance to be created by container";
        stadtInstance.setName("Kassel-Wilhelmshoehe");
        stadtInstance.zustandVergessen();

        // Now make sure that the container does not return this instance again
        KleinStadt newStadtInstance = getContextualReference(KleinStadt.class);
        assert newStadtInstance != null : "Failed to get SFSB instance the second time";
        assert !"Kassel-Wilhelmshoehe".equals(newStadtInstance.getName()) : "The destroyed SFSB was not ignored";
    }

    @Test(groups =  INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = STATELESS_LIFECYCLE, id = "b") })
    public void testCreateSLSB() {
        Bean<NeueStadt> stadtBean = getBeans(NeueStadt.class).iterator().next();
        assert stadtBean != null : "Expected a bean for stateful session bean Kassel";
        CreationalContext<NeueStadt> creationalContext = getCurrentManager().createCreationalContext(stadtBean);
        NeueStadt stadtInstance = stadtBean.create(creationalContext);
        assert stadtInstance != null : "Expected instance to be created by container";

        // Verify that the instance returned is a proxy by checking for all local interfaces
        assert stadtInstance instanceof NeueStadt;
        assert stadtInstance instanceof GeschichtslosStadt;
    }

    @Test(groups =  INTEGRATION)
    @SpecAssertion(section = INITIALIZER_METHODS_EE, id = "f")
    public void testInitializerMethodsCalledWithCurrentParameterValues() {
        AlteStadt alteStadt = getContextualReference(AlteStadt.class);
        assert alteStadt != null : "Could not find the AlteStadt bean";
        assert alteStadt.getAnotherPlaceOfInterest() != null;
    }

    @Test(groups =  INTEGRATION)
    @SpecAssertion(section = DEPENDENT_OBJECTS_DESTRUCTION_EE, id = "a")
    public void testDependentObjectsDestroyed() {
        Bean<UniStadt> uniStadtBean = getBeans(UniStadt.class).iterator().next();
        CreationalContext<UniStadt> creationalContext = getCurrentManager().createCreationalContext(uniStadtBean);
        UniStadt marburg = uniStadtBean.create(creationalContext);
        assert marburg != null : "Couldn't find the main SFSB";
        uniStadtBean.destroy(marburg, creationalContext);
        GrossStadt frankfurt = getContextualReference(GrossStadt.class);
        assert frankfurt.isSchlossDestroyed();
    }

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "bab")
    public void testDirectSubClassInheritsPostConstructOnSuperclass() throws Exception {
        OrderProcessor.postConstructCalled = false;
        assert getBeans(DirectOrderProcessorLocal.class).size() == 1;
        getContextualReference(DirectOrderProcessorLocal.class).order();
        assert OrderProcessor.postConstructCalled;
    }

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "bad")
    public void testIndirectSubClassInheritsPostConstructOnSuperclass() throws Exception {
        OrderProcessor.postConstructCalled = false;
        assert getBeans(OrderProcessorLocal.class).size() == 1;
        getContextualReference(OrderProcessorLocal.class).order();
        assert OrderProcessor.postConstructCalled;
    }

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "bbb")
    public void testSubClassInheritsPreDestroyOnSuperclass() throws Exception {
        OrderProcessor.preDestroyCalled = false;
        assert getBeans(DirectOrderProcessorLocal.class).size() == 1;
        Bean<DirectOrderProcessorLocal> bean = getBeans(DirectOrderProcessorLocal.class).iterator().next();
        CreationalContext<DirectOrderProcessorLocal> creationalContext = getCurrentManager().createCreationalContext(bean);
        DirectOrderProcessorLocal instance = bean.create(creationalContext);
        bean.destroy(instance, creationalContext);
        assert OrderProcessor.preDestroyCalled;
    }

    @Test(groups = { INTEGRATION })
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "bbd")
    public void testIndirectSubClassInheritsPreDestroyOnSuperclass() throws Exception {
        OrderProcessor.preDestroyCalled = false;
        assert getBeans(OrderProcessorLocal.class).size() == 1;
        Bean<OrderProcessorLocal> bean = getBeans(OrderProcessorLocal.class).iterator().next();
        CreationalContext<OrderProcessorLocal> creationalContext = getCurrentManager().createCreationalContext(bean);
        OrderProcessorLocal instance = bean.create(creationalContext);
        bean.destroy(instance, creationalContext);
        assert OrderProcessor.preDestroyCalled;
    }
}
