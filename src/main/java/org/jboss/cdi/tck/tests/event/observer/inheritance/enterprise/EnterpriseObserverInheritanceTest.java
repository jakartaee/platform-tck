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
package org.jboss.cdi.tck.tests.event.observer.inheritance.enterprise;

import static org.jboss.cdi.tck.TestGroups.JAVAEE_FULL;
import static org.jboss.cdi.tck.cdi.Sections.MEMBER_LEVEL_INHERITANCE_EE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.EnterpriseArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * Event observer inheritance test with enterprise beans.
 * 
 * @author Shane Bryzak
 * @author Martin Kouba
 */
@Test(groups = JAVAEE_FULL)
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseObserverInheritanceTest extends AbstractTest {

    @Deployment
    public static EnterpriseArchive createTestArchive() {
        return new EnterpriseArchiveBuilder().withTestClassPackage(EnterpriseObserverInheritanceTest.class).build();
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "df")
    public void testNonStaticObserverMethodInherited() throws Exception {
        Egg egg = new Egg();
        getCurrentManager().fireEvent(egg);
        assertEquals(egg.getVisited().size(), 2);
        assertTrue(egg.getVisited().contains(Farmer.class.getSimpleName()));
        assertTrue(egg.getVisited().contains(LazyFarmer.class.getSimpleName()));
    }

    @Test
    @SpecAssertion(section = MEMBER_LEVEL_INHERITANCE_EE, id = "dl")
    public void testNonStaticObserverMethodIndirectlyInherited() throws Exception {
        StockPrice stockPrice = new StockPrice();
        getCurrentManager().fireEvent(stockPrice);
        assertEquals(stockPrice.getVisited().size(), 3);
        assertTrue(stockPrice.getVisited().contains(StockWatcher.class.getSimpleName()));
        assertTrue(stockPrice.getVisited().contains(IntermediateStockWatcher.class.getSimpleName()));
        assertTrue(stockPrice.getVisited().contains(IndirectStockWatcher.class.getSimpleName()));
    }
}
