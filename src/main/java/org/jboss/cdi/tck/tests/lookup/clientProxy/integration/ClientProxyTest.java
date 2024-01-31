/*
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
package org.jboss.cdi.tck.tests.lookup.clientProxy.integration;

import java.io.IOException;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.spi.Context;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.CLIENT_PROXIES;
import static org.jboss.cdi.tck.cdi.Sections.CLIENT_PROXY_INVOCATION;
import static org.jboss.cdi.tck.cdi.Sections.CONTEXTUAL_REFERENCE_VALIDITY;

@SpecVersion(spec = "cdi", version = "2.0")
public class ClientProxyTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(ClientProxyTest.class).build();
    }

    @Test
    @SpecAssertion(section = CLIENT_PROXIES, id = "b")
    public void testClientProxyUsedForNormalScope() {
        Tuna tuna = getContextualReference(Tuna.class);
        assert getCurrentConfiguration().getBeans().isProxy(tuna);
    }

    @Test
    @SpecAssertion(section = CLIENT_PROXIES, id = "c")
    public void testSimpleBeanClientProxyIsSerializable() throws IOException, ClassNotFoundException {
        TunedTuna tuna = getContextualReference(TunedTuna.class);
        assert getCurrentConfiguration().getBeans().isProxy(tuna);
        byte[] bytes = passivate(tuna);
        tuna = (TunedTuna) activate(bytes);
        assert getCurrentConfiguration().getBeans().isProxy(tuna);
        assert tuna.getState().equals("tuned");
    }

    @Test
    @SpecAssertion(section = CLIENT_PROXY_INVOCATION, id = "aa")
    public void testClientProxyInvocation() {
        TunedTuna tuna = getContextualReference(TunedTuna.class);
        assert getCurrentConfiguration().getBeans().isProxy(tuna);
        assert tuna.getState().equals("tuned");
    }

    @Test(groups = INTEGRATION, expectedExceptions = { ContextNotActiveException.class, IllegalStateException.class })
    @SpecAssertions({ @SpecAssertion(section = CLIENT_PROXY_INVOCATION, id = "ab"), @SpecAssertion(section = CONTEXTUAL_REFERENCE_VALIDITY, id = "a") })
    public void testInactiveScope() throws Exception {
        assert getCurrentConfiguration().getContexts().getRequestContext().isActive();
        Context ctx = getCurrentConfiguration().getContexts().getRequestContext();
        setContextInactive(ctx);
        assert !getCurrentConfiguration().getContexts().getRequestContext().isActive();
        try {
            getContextualReference(TunedTuna.class).getState();
        } finally {
            // need to set request scope active again, some other tests will fail otherwise
            setContextActive(ctx);
        }
    }
}
