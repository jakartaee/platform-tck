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
package org.jboss.cdi.tck.tests.lookup.injection.non.contextual;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.TYPE_BEAN_DISCOVERY_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import java.io.Serializable;
import java.util.EventListener;

import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.spi.AnnotatedField;
import jakarta.enterprise.inject.spi.AnnotatedMethod;
import jakarta.enterprise.inject.spi.AnnotatedType;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.GenericServlet;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.jsp.tagext.JspTag;
import jakarta.servlet.jsp.tagext.SimpleTag;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * This test verifies that ProcessAnnotatedType event is fired for various Java EE components and tests the AnnotatedType
 * implementation.
 *
 * It's placed in this package because it works with the same Java EE components as
 * {@link InjectionIntoNonContextualComponentTest} does.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ContainerEventTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(ContainerEventTest.class)
                .withWebXml("web.xml")
                .withClasses(Farm.class, ProcessAnnotatedTypeObserver.class, Sheep.class, TagLibraryListener.class,
                        TestFilter.class, TestListener.class, TestServlet.class, TestTagHandler.class, SessionBean.class)
                .withExtension(ProcessAnnotatedTypeObserver.class)
                .withWebResource("ManagedBeanTestPage.xhtml", "ManagedBeanTestPage.xhtml")
                .withWebResource("TagPage.jsp", "TagPage.jsp").withWebResource("faces-config.xml", "/WEB-INF/faces-config.xml")
                .withWebResource("TestLibrary.tld", "WEB-INF/TestLibrary.tld")
                .withDefaultPersistenceXml()
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "bb")
    public void testProcessAnnotatedTypeEventFiredForSessionBean() {
        AnnotatedType<SessionBean> annotatedType = ProcessAnnotatedTypeObserver.getSessionBeanEvent();
        assertNotNull(annotatedType);
        assertEquals(annotatedType.getBaseType(), SessionBean.class);
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "be")
    public void testProcessAnnotatedTypeEventFiredForServletListener() {
        assertNotNull(ProcessAnnotatedTypeObserver.getListenerEvent());
        validateServletListenerAnnotatedType(ProcessAnnotatedTypeObserver.getListenerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "bf")
    public void testProcessAnnotatedTypeEventFiredForTagHandler() {
        assertNotNull(ProcessAnnotatedTypeObserver.getTagHandlerEvent());
        validateTagHandlerAnnotatedType(ProcessAnnotatedTypeObserver.getTagHandlerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "bg")
    public void testProcessAnnotatedTypeEventFiredForTagLibraryListener() {
        assertNotNull(ProcessAnnotatedTypeObserver.getTagLibraryListenerEvent());
        validateTagLibraryListenerAnnotatedType(ProcessAnnotatedTypeObserver.getTagLibraryListenerEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "bj")
    public void testProcessAnnotatedTypeEventFiredForServlet() {
        assertNotNull(ProcessAnnotatedTypeObserver.getServletEvent());
        validateServletAnnotatedType(ProcessAnnotatedTypeObserver.getServletEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "bk")
    public void testProcessAnnotatedTypeEventFiredForFilter() {
        assertNotNull(ProcessAnnotatedTypeObserver.getFilterEvent());
        validateFilterAnnotatedType(ProcessAnnotatedTypeObserver.getFilterEvent());
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = TYPE_BEAN_DISCOVERY_EE, id = "bd")
    public void testProcessAnnotatedTypeEventFiredForJsfManagedBean() {
        assertNotNull(ProcessAnnotatedTypeObserver.getJsfManagedBeanEvent());
        validateJsfManagedBeanAnnotatedType(ProcessAnnotatedTypeObserver.getJsfManagedBeanEvent());
    }

    private void validateServletListenerAnnotatedType(AnnotatedType<TestListener> type) {
        assertEquals(type.getBaseType(), TestListener.class);
        assertTrue(type.getAnnotations().isEmpty());
        assertEquals(type.getFields().size(), 2);
        assertTrue(type.getMethods().stream().anyMatch(m -> m.getJavaMember().getName().equals("contextInitialized")));

        int initializers = 0;
        for (AnnotatedMethod<?> method : type.getMethods()) {
            assertEquals(method.getParameters().size(), 1);
            assertEquals(method.getBaseType(), void.class);
            if (method.isAnnotationPresent(Inject.class)) {
                initializers++;
            }
        }
        assertEquals(initializers, 1);
    }

    private void validateTagHandlerAnnotatedType(AnnotatedType<TestTagHandler> type) {
        assertEquals(type.getBaseType(), TestTagHandler.class);
        assertTrue(typeSetMatches(type.getTypeClosure(), TestTagHandler.class, SimpleTagSupport.class, SimpleTag.class,
                JspTag.class, Object.class));
        assertEquals(type.getAnnotations().size(), 1);
        assertTrue(type.isAnnotationPresent(Any.class));
    }

    private void validateTagLibraryListenerAnnotatedType(AnnotatedType<TagLibraryListener> type) {
        assertEquals(type.getBaseType(), TagLibraryListener.class);
        assertTrue(typeSetMatches(type.getTypeClosure(), TagLibraryListener.class, ServletContextListener.class,
                EventListener.class, Object.class));
        assertEquals(type.getFields().size(), 2);
        assertEquals(type.getConstructors().size(), 1);
        assertTrue(type.getMethods().stream().anyMatch(m -> m.getJavaMember().getName().equals("contextInitialized")));
    }

    private void validateServletAnnotatedType(AnnotatedType<TestServlet> type) {
        assertEquals(type.getBaseType(), TestServlet.class);
        assertTrue(typeSetMatches(type.getTypeClosure(), TestServlet.class, HttpServlet.class, GenericServlet.class,
                Servlet.class, ServletConfig.class, Serializable.class, Object.class));
        assertTrue(type.getAnnotations().isEmpty());
    }

    private void validateFilterAnnotatedType(AnnotatedType<TestFilter> type) {
        assertEquals(type.getBaseType(), TestFilter.class);
        assertTrue(typeSetMatches(type.getTypeClosure(), TestFilter.class, Filter.class, Object.class));
        assertEquals(type.getFields().size(), 12);
        assertEquals(type.getConstructors().size(), 1);
        assertTrue(type.getConstructors().iterator().next().getParameters().isEmpty());
        assertTrue(type.getMethods().stream().anyMatch(m -> m.getJavaMember().getName().equals("doFilter")));
    }

    private void validateJsfManagedBeanAnnotatedType(AnnotatedType<Farm> type) {
        assert type.getFields().size() == 2;
        for (AnnotatedField<?> field : type.getFields()) {
            if (field.getJavaMember().getName().equals("sheep")) {
                assertTrue(field.isAnnotationPresent(Inject.class));
                assertFalse(field.isStatic());
            } else if (field.getJavaMember().getName().equals("initializerCalled")) {
                assertFalse(field.isStatic());
                assertTrue(field.getBaseType().equals(boolean.class));
            } else {
                fail(); // there is no other field
            }
        }
        assertEquals(type.getMethods().size(), 3);
    }
}
