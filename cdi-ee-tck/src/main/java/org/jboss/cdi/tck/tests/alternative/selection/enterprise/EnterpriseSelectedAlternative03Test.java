/*
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
package org.jboss.cdi.tck.tests.alternative.selection.enterprise;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.DECLARING_SELECTED_ALTERNATIVES_APPLICATION;
import static org.jboss.cdi.tck.cdi.Sections.UNSATISFIED_AND_AMBIG_DEPENDENCIES;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import jakarta.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.alternative.selection.Alpha;
import org.jboss.cdi.tck.tests.alternative.selection.Bar;
import org.jboss.cdi.tck.tests.alternative.selection.BarProducer;
import org.jboss.cdi.tck.tests.alternative.selection.Bravo;
import org.jboss.cdi.tck.tests.alternative.selection.Charlie;
import org.jboss.cdi.tck.tests.alternative.selection.Foo;
import org.jboss.cdi.tck.tests.alternative.selection.Tame.TameLiteral;
import org.jboss.cdi.tck.tests.alternative.selection.TestBean;
import org.jboss.cdi.tck.tests.alternative.selection.Wild.WildLiteral;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * EAR deployment with 2 libraries and 1 war:
 * <ul>
 * <li>ear 1.lib - contains {@link Foo} TestBean alternative with priority 1000</li>
 * 
 * <li>war - contains {@link Bar} TestBean alternative with priority 2000, {@link BarProducer} Bar alternative producer method and Bar alternative producer
 * field with priority 1100 with 2 different annotations .</li>
 * </ul>
 * 
 * Expected results:
 * <ul>
 * <li>{@link Foo} is available for injection in all beans</li>
 * <li>{@link Bar} is available for injection in beans in war only</li>
 * <li>when injecting {@link TestBean}, {@link Bar} is preferred if visible</li>
 * <li>{@link BarProducer} alternative producer method and alternative producer field are both selected and visible in war only.</li>
 * </ul>
 * 
 * @author Matej Briskar
 * 
 */

@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseSelectedAlternative03Test extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {

        EnterpriseArchive enterpriseArchive = SelectedAlternativeTestUtil.createEnterpriseBuilderBase()
        // A - default EJB jar
                .withTestClassDefinition(EnterpriseSelectedAlternative03Test.class)
                // C - lib visible to all
                .withBeanLibrary(Foo.class).withBeanLibrary(Bravo.class).noDefaultWebModule().build();

        // E - not visible for AC
        WebArchive bazWebArchive = new WebArchiveBuilder().notTestArchive().withClasses(Bar.class, EnterpriseSelectedAlternative03Test.class)
                .withBeanLibrary(Alpha.class, Charlie.class, BarProducer.class).build();

        enterpriseArchive.addAsModule(bazWebArchive);

        return enterpriseArchive;
    }

    @Inject
    Alpha alpha;

    @Inject
    Bravo bravo;

    @Inject
    Charlie charlie;

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "aa")
    public void testAlternativeManagedBeanAvailable() {
        assertNotNull(alpha);
        assertNotNull(bravo);
        assertNotNull(charlie);

        alpha.assertAvailable(Bar.class);
        alpha.assertAvailable(Foo.class);
        bravo.assertAvailable(Foo.class);
        charlie.assertAvailable(Bar.class);
        charlie.assertAvailable(Foo.class);
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = UNSATISFIED_AND_AMBIG_DEPENDENCIES, id = "cb")
    public void testDependencyResolvable() {

        assertEquals(alpha.assertAvailable(TestBean.class).getId(), Bar.class.getName());
        // Bar is not available for Bravo
        assertEquals(bravo.assertAvailable(TestBean.class).getId(), Foo.class.getName());
        assertEquals(charlie.assertAvailable(TestBean.class).getId(), Bar.class.getName());
    }

    @Test(groups = JAVAEE_FULL)
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "ba")
    @SpecAssertion(section = DECLARING_SELECTED_ALTERNATIVES_APPLICATION, id = "bb")
    public void testAlternativeProducerSelected() {
        // Producer field
        alpha.assertAvailable(Bar.class, WildLiteral.INSTANCE);
        charlie.assertAvailable(Bar.class, WildLiteral.INSTANCE);
        // Producer method
        alpha.assertAvailable(Bar.class, TameLiteral.INSTANCE);
        charlie.assertAvailable(Bar.class, TameLiteral.INSTANCE);
    }

}
