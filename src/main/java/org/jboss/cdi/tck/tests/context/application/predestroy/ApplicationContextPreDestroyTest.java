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

package org.jboss.cdi.tck.tests.context.application.predestroy;

import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_EE;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ApplicationContextPreDestroyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ApplicationContextPreDestroyTest.class).build();
    }

    @Inject
    Bar bar;

    @Test
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "dh")
    public void testApplicationContextActivePreDestroy() {
        Bean<Foo> bean = getUniqueBean(Foo.class);
        CreationalContext<Foo> ctx = getCurrentManager().createCreationalContext(bean);
        Foo foo = bean.create(ctx);
        assertFalse(foo.isBarAlone());
        bean.destroy(foo, ctx);
        assertTrue(bar.isAlone());
    }

}
