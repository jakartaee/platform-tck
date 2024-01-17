/*
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
package org.jboss.cdi.tck.tests.deployment.packaging.visibility;

import static org.jboss.cdi.tck.cdi.Sections.BEAN_ARCHIVE_EE;
import static org.jboss.cdi.tck.cdi.Sections.PERFORMING_TYPESAFE_RESOLUTION;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Verifies that a bean from a BDA is injectable into another BDA.
 *
 * <p>
 * This test was originally part of Seam Compatibility project.
 * <p>
 *
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class JarToJarAlphaVisibilityTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(JarToJarAlphaVisibilityTest.class).withBeanLibrary("alpha.jar", Foo.class)
                .withBeanLibrary("bravo.jar", Bar.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = BEAN_ARCHIVE_EE, id = "jh"), @SpecAssertion(section = PERFORMING_TYPESAFE_RESOLUTION, id = "n") })
    public void testDeployment() {
        // noop
    }
}
