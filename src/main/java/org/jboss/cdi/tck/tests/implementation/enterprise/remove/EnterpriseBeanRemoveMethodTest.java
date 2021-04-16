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
package org.jboss.cdi.tck.tests.implementation.enterprise.remove;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.jboss.cdi.tck.cdi.Sections.SESSION_BEAN_EJB_REMOVE_METHOD;

import jakarta.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.ee.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Nicklas Karlsson
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "2.0")
public class EnterpriseBeanRemoveMethodTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(EnterpriseBeanRemoveMethodTest.class).build();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = SESSION_BEAN_EJB_REMOVE_METHOD, id = "a")
    public void testApplicationMayCallAnyRemoveMethodOnDependentScopedSessionEnterpriseBeans() throws Exception {
        Bean<?> bean = getCurrentManager().getBeans(StateKeeper.class).iterator().next();
        StateKeeper stateKeeper = (StateKeeper) getCurrentManager().getReference(bean, StateKeeper.class,
                getCurrentManager().createCreationalContext(bean));
        stateKeeper.setRemoveCalled(false);

        DependentSessionInterface sessionBean = getContextualReference(DependentSessionInterface.class);
        sessionBean.remove();
        assert stateKeeper.isRemoveCalled();
    }

    @Test(groups = INTEGRATION)
    @SpecAssertion(section = SESSION_BEAN_EJB_REMOVE_METHOD, id = "da")
    public void testApplicationMayCallRemoveMethodOnDependentScopedSessionEnterpriseBeansButNoParametersArePassed()
            throws Exception {
        DependentSessionInterface sessionBean = getContextualReference(DependentSessionInterface.class);
        sessionBean.anotherRemoveWithParameters("required", null);
        StateKeeper stateKeeper = getContextualReference(StateKeeper.class);
        assert stateKeeper.isRemoveCalled();
    }

    @Test(groups = INTEGRATION, expectedExceptions = UnsupportedOperationException.class)
    @SpecAssertion(section = SESSION_BEAN_EJB_REMOVE_METHOD, id = "b")
    public void testApplicationCannotCallRemoveMethodOnNonDependentScopedSessionEnterpriseBean() {
        SessionScopedSessionInterface sessionBean = getContextualReference(SessionScopedSessionInterface.class);
        sessionBean.remove();
        assert false : "Should never reach this assertion";
    }

}
