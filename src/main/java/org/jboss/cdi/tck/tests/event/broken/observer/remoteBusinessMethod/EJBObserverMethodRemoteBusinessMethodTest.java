/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.event.broken.observer.remoteBusinessMethod;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVER_METHODS_EE;
import static org.jboss.cdi.tck.cdi.Sections.OBSERVES_EE;

import jakarta.enterprise.inject.spi.DefinitionException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "2.0")
public class EJBObserverMethodRemoteBusinessMethodTest extends AbstractTest {

    @ShouldThrowException(DefinitionException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withClasses(Terrier.class, TibetanTerrier.class).withTestClass(EJBObserverMethodRemoteBusinessMethodTest.class).build();
    }

    @Test(groups = { INTEGRATION, JAVAEE_FULL })
    @SpecAssertion(section = OBSERVER_METHODS_EE, id = "d")
    @SpecAssertion(section = OBSERVES_EE, id = "g")
    public void testObserverMethodOnRemoteBusinessMethodFails() {
    }
}
