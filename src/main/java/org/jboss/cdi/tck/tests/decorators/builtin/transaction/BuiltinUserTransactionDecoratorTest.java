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
package org.jboss.cdi.tck.tests.decorators.builtin.transaction;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECORATOR_BEAN_EE;

import jakarta.inject.Inject;
import jakarta.transaction.Status;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 
 * @author Jozef Hartinger
 *
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class BuiltinUserTransactionDecoratorTest extends AbstractTest {

    @Inject
    private SessionBean sessionBean;

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder()
                .withTestClassPackage(BuiltinUserTransactionDecoratorTest.class)
                .withBeansXml(
                        new BeansXml().decorators(UserTransactionDecorator.class)).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = DECORATOR_BEAN_EE, id = "acn") })
    public void testUserTransactionDecorated() throws Exception {
        Assert.assertEquals(sessionBean.getTransaction().getStatus(), Status.STATUS_UNKNOWN);
    }
}
