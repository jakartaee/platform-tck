/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.lookup.dependency.resolution.broken.ambiguous.ear;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.UNSATISFIED_AND_AMBIG_DEPENDENCIES;

import jakarta.enterprise.inject.spi.DeploymentException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Test that ambiguous dependency is detected when multiple beans eligible for injection to the injection point are deployed in
 * different modules. See WELD-1586 for more info.
 * 
 * @author Matus Abaffy
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class MultiModuleSessionBeanAmbiguousDependencyTest extends AbstractTest {

    @ShouldThrowException(DeploymentException.class)
    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = new EnterpriseArchiveBuilder()
                .notTestArchive()
                .withClasses(Animal.class, Dog.class)
                .noDefaultWebModule()
                .build();

        WebArchive barArchive = new WebArchiveBuilder().notTestArchive().withName("test.war")
                .withClasses(Bar.class, Cat.class)
                .withDefaultEjbModuleDependency()
                .build();
        enterpriseArchive.addAsModule(barArchive);

        return enterpriseArchive;
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = UNSATISFIED_AND_AMBIG_DEPENDENCIES, id = "ae")
    public void testAmbiguousDependencyDetected() {
    }
}
