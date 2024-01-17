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

package org.jboss.cdi.tck.tests.context.request.postconstruct;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;

/**
 * @author Martin Kouba
 * 
 */
@Dependent
public class SimpleBean {

    private boolean isRequestContextActiveDuringPostConstruct = false;

    @Inject
    Action action;

    @Inject
    BeanManager beanManager;

    @PostConstruct
    public void init() {
        if (beanManager != null && beanManager.getContext(RequestScoped.class).isActive() && action != null && action.ping()) {
            isRequestContextActiveDuringPostConstruct = true;
        }
    }

    public boolean isRequestContextActiveDuringPostConstruct() {
        return isRequestContextActiveDuringPostConstruct;
    }

}
