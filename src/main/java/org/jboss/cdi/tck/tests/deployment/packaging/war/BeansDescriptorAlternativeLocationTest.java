/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.deployment.packaging.war;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_EE;
import static org.testng.Assert.assertNotNull;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * The <code>WEB-INF/classes</code> directory of a war is a bean archive if there is a file named beans.xml in the
 * <code>WEB-INF/classes/META-INF</code> directory of the war.
 *
 * @author Martin Kouba
 *
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class BeansDescriptorAlternativeLocationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(BeansDescriptorAlternativeLocationTest.class)
                .withBeansDescriptorTargetBase("classes/META-INF/").build();
    }

    @Test
    @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "fb")
    public void testAlternativeLocation() {
        // Injection works
        assertNotNull(beanManager);
    }

}
