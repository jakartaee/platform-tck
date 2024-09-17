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

package org.jboss.cdi.tck.tests.implementation.enterprise.definition;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEAN_TYPES;
import static org.jboss.cdi.tck.cdi.Sections.WHAT_CLASSES_ARE_BEANS_EE;

import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.BeanDiscoveryMode;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * These tests are any that involve ejb-jar.xml resources.
 * 
 * @author David Allen
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanViaXmlTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder()
                .withTestClassPackage(EnterpriseBeanViaXmlTest.class)
                .withBeansXml(new BeansXml(BeanDiscoveryMode.ALL))
                .withEjbJarXml("ejb-jar.xml")
                .build();
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertions({ @SpecAssertion(section = WHAT_CLASSES_ARE_BEANS_EE, id = "b"), @SpecAssertion(section = SESSION_BEAN_TYPES, id = "ba") })
    public void testEjbDeclaredInXmlNotSimpleBean() {
        Bean<ElephantLocal> elephantBean = getBeans(ElephantLocal.class).iterator().next();
        // The interface is a known type but the class should not be
        assert elephantBean.getTypes().contains(ElephantLocal.class);
        assert !elephantBean.getTypes().contains(Elephant.class);
    }

}
