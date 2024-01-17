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
package org.jboss.cdi.tck.tests.context.request.ejb;

import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

@Stateless
public class FooBean implements FooRemote {

    @Inject
    private BeanManager beanManager;

    @Inject
    FooRequestBean fooRequestBean;

    @Override
    public String ping() {

        FooRequestBean.reset();
        Context requestContext = null;

        try {
            requestContext = beanManager.getContext(RequestScoped.class);
        } catch (ContextNotActiveException e) {
            // No-op
        }

        if (requestContext == null || !requestContext.isActive() || fooRequestBean == null) {
            return null;
        }
        double id = fooRequestBean.getId();
        return "pong" + id;
    }

    @Override
    public boolean wasRequestBeanInPreviousCallDestroyed() {
        // We only want to know whether FooRequestBean was destroyed after previous call
        return FooRequestBean.isBeanDestroyed();
    }

}
