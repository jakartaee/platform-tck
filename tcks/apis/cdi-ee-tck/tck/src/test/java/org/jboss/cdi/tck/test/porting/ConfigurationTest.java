/*
 * Copyright 2024, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.test.porting;

import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.impl.ConfigurationFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Validate that the CDI {@link Configuration} can be loaded and has the expected values
 * as defined in the pom.xml surefire plugin configuration and META-INF/cdi-tck.properties.
 */
public class ConfigurationTest {
    @Test
    public void testCDIConfigurtion() {
        Configuration config = ConfigurationFactory.get();
        Assert.assertEquals(config.getCoreMode(), Boolean.FALSE);
        Assert.assertEquals(config.getLibraryDirectory(), "target/dependency/lib");
        Assert.assertEquals(config.getTestDataSource(), "java:jboss/datasources/ExampleDS");
        Assert.assertEquals(config.getTestTimeoutFactor(), 100);
        Assert.assertEquals(config.getTestJmsConnectionFactory(), "java:/ConnectionFactory");
        Assert.assertEquals(config.getTestJmsQueue(), "java:/queue/test");
        Assert.assertEquals(config.getTestJmsTopic(), "java:/topic/test");

        Assert.assertTrue(config.getBeans() instanceof org.jboss.cdi.tck.test.porting.DummyBeans);
        Assert.assertTrue(config.getContexts() instanceof org.jboss.cdi.tck.test.porting.DummyContexts);
        Assert.assertTrue(config.getContextuals() instanceof org.jboss.cdi.tck.test.porting.DummyContextuals);
        Assert.assertTrue(config.getCreationalContexts() instanceof org.jboss.cdi.tck.test.porting.DummyCreationalContexts);
        Assert.assertTrue(config.getEl() instanceof org.jboss.cdi.tck.test.porting.DummyEL);
    }
}
