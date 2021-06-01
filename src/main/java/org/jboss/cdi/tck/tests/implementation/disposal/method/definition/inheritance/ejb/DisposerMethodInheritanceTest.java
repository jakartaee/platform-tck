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

package org.jboss.cdi.tck.tests.implementation.disposal.method.definition.inheritance.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.implementation.disposal.method.definition.inheritance.Yummy;
import org.jboss.cdi.tck.util.DependentInstance;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE_EE;
import static org.testng.Assert.assertEquals;

/**
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class DisposerMethodInheritanceTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(DisposerMethodInheritanceTest.class)
                .withClasses(Yummy.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertions({ @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "de"),
            @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "dk") })
    public void testSessionBeanDisposerMethodNotInherited() {

        DependentInstance<Egg> eggInstance = new DependentInstance<Egg>(getCurrentManager(), Egg.class);
        Egg egg = eggInstance.get();
        ;
        // The producer method for @Default Egg is defined on Sumavanka
        assertEquals(egg.getChicken().getOriginClass(), Sumavanka.class);
        eggInstance.destroy();
        // Disposer method is not inherited
        assertEquals(Egg.disposedBy.size(), 0);

        DependentInstance<Code> codeInstance = new DependentInstance<Code>(getCurrentManager(), Code.class);
        Code code = codeInstance.get();
        // The producer method for @Default Code is defined on Guru
        assertEquals(code.getProgrammer().getOriginClass(), Guru.class);
        codeInstance.destroy();
        // Disposer method is not inherited
        assertEquals(Code.disposedBy.size(), 0);
    }

}
