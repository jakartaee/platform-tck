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

package org.jboss.cdi.tck.tests.context.application.postconstruct;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

/**
 * @author Martin Kouba
 * 
 */
@Startup
@Singleton
@ApplicationScoped
public class EagerSingleton {

    private boolean isApplicationContextActiveDuringPostConstruct = false;

    @Inject
    Service service;

    @Inject
    BeanManager beanManager;

    @PostConstruct
    public void init() {
        if (beanManager != null && beanManager.getContext(ApplicationScoped.class).isActive() && service != null
                && service.ping()) {
            isApplicationContextActiveDuringPostConstruct = true;
        }
    }

    public boolean isApplicationContextActiveDuringPostConstruct() {
        return isApplicationContextActiveDuringPostConstruct;
    }

}
