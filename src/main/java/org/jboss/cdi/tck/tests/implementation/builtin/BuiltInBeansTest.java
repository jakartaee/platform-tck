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
package org.jboss.cdi.tck.tests.implementation.builtin;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.REWRITE;
import static org.jboss.cdi.tck.cdi.Sections.ADDITIONAL_BUILTIN_BEANS;
import static org.testng.Assert.assertNotNull;

import java.security.Principal;

import javax.security.auth.login.LoginException;
import jakarta.transaction.SystemException;
import jakarta.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Pete Muir
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class BuiltInBeansTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClassPackage(BuiltInBeansTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "a") })
    public void testUserTransactionBean() throws SystemException {
        UserTransaction userTransaction = getContextualReference(UserTransactionInjectedBeanLocal.class).getUserTransaction();
        assertNotNull(userTransaction);
        // Check that the UserTransaction is at least queryable
        userTransaction.getStatus();
    }

    /**
     * 
     * @throws SystemException
     * @throws LoginException
     */
    @Test(groups = REWRITE)
    // PLM We should check the Principal somehow
    @SpecAssertions({ @SpecAssertion(section = ADDITIONAL_BUILTIN_BEANS, id = "b") })
    public void testPrincipalBean() throws SystemException, LoginException {
        PrincipalInjectedBeanLocal instance = getContextualReference(PrincipalInjectedBeanLocal.class);
        instance.login();
        Principal principal = instance.getPrincipal();
        // Not much we can check on the Princiapl easily
    }

}
