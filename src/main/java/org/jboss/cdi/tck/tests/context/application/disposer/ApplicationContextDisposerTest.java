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

package org.jboss.cdi.tck.tests.context.application.disposer;

import static org.jboss.cdi.tck.cdi.Sections.APPLICATION_CONTEXT_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.util.AnnotationLiteral;
import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class ApplicationContextDisposerTest extends AbstractTest {

    private static final SimpleLogger logger = new SimpleLogger(ApplicationContextDisposerTest.class);

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ApplicationContextDisposerTest.class).build();
    }

    @Inject
    Forest forest;

    @Test
    @SpecAssertion(section = APPLICATION_CONTEXT_EE, id = "dg")
    public void testApplicationContextActiveDuringDispose() {
        logger.log("Injected forest: {0}", forest.toString());
        @SuppressWarnings("serial")
        Bean<Mushroom> bean = getUniqueBean(Mushroom.class, new AnnotationLiteral<Edible>() {
        });
        CreationalContext<Mushroom> ctx = getCurrentManager().createCreationalContext(bean);
        Mushroom mushroom = bean.create(ctx);
        assertEquals(mushroom.getName(), "Boletus");
        assertFalse(forest.isEmpty());
        bean.destroy(mushroom, ctx);
        assertTrue(forest.isEmpty());
    }

}
