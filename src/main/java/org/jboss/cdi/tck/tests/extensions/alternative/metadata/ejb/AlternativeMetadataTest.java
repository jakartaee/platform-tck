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
package org.jboss.cdi.tck.tests.extensions.alternative.metadata.ejb;

import jakarta.enterprise.inject.spi.Bean;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.ItalianFood;
import org.jboss.shrinkwrap.api.BeansXmlVersion;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.BeansXml;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.cdi.Sections.ALTERNATIVE_METADATA_SOURCES_EE;
import static org.testng.Assert.assertEquals;


/**
 * This test class contains tests for adding meta data using extensions.
 *
 * @author Jozef Hartinger
 * @author Martin Kouba
 * @author Tomas Remes
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class AlternativeMetadataTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        WebArchive war = new WebArchiveBuilder()
                .withTestClassPackage(AlternativeMetadataTest.class)
                .withClasses(ItalianFood.class)
                .withBeansXml(new BeansXml().setBeansXmlVersion(BeansXmlVersion.v11))
                .withExtension(ProcessAnnotatedTypeObserver.class).build();
        System.out.println(war.toString(true));
        return war;
    }

    @Test
    @SpecAssertion(section = ALTERNATIVE_METADATA_SOURCES_EE, id = "kb")
    public void testGetTypeClosureUsedToDetermineTypeOfSessionBean() {
        Bean<Pasta> pasta = getBeans(Pasta.class).iterator().next();
        assertEquals(pasta.getTypes().size(), 2, ""+pasta.getTypes());
    }


}
