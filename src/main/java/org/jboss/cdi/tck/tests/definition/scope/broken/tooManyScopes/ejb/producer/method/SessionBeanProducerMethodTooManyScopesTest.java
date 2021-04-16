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
package org.jboss.cdi.tck.tests.definition.scope.broken.tooManyScopes.ejb.producer.method;

import jakarta.enterprise.inject.spi.DefinitionException;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.ShouldThrowException;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_BEAN_SCOPE;

@SpecVersion(spec = "cdi", version = "2.0")
public class SessionBeanProducerMethodTooManyScopesTest extends AbstractTest {

    @ShouldThrowException(DefinitionException.class)
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClass(SessionBeanProducerMethodTooManyScopesTest.class).withClass(SessionBeanProducerMethodWithTooManyScopeTypes_Broken.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = DECLARING_BEAN_SCOPE, id = "bb")
    public void testTooManyScopesSpecifiedInJava() {
    }

}