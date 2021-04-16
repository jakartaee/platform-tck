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
package org.jboss.cdi.tck.interceptors.tests.contract.aroundInvoke.ee;

import jakarta.transaction.UserTransaction;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.TestGroups.SECURITY;
import static org.jboss.cdi.tck.interceptors.InterceptorsSections.BUSINESS_METHOD_INTERCEPTOR_METHODS;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@SpecVersion(spec = "interceptors", version = "1.2")
public class AroundInvokeAccessInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(AroundInvokeAccessInterceptorTest.class).build();
    }


    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = {JAVAEE_FULL, SECURITY})
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "e")
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "fb")
    public void testSecurityContext(Student student) throws Exception {
        student.printArticle();
        assertTrue(PrinterSecurityInterceptor.securityContextOK);
        assertTrue(Toner.calledFromInterceptor);
    }

    @Test(dataProvider = ARQUILLIAN_DATA_PROVIDER, groups = JAVAEE_FULL)
    @SpecAssertion(section = BUSINESS_METHOD_INTERCEPTOR_METHODS, id = "fa")
    public void testTransactionContext(Foo foo, UserTransaction ut) throws Exception {
        ut.begin();

        foo.invoke();
        // checks are done in FooInterceptor and BazInterceptor
        assertTrue(FooInterceptor.called);
        assertTrue(BazInterceptor.called);

        ut.commit();
    }
}
