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
package org.jboss.cdi.tck.tests.context.dependent.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Remove;
import jakarta.ejb.Stateful;
import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Dependent
@Named
@Default
@Stateful
public class Fox implements FoxLocal {

    private static boolean destroyed = false;
    private static int destroyCount = 0;
    private static boolean dependentContextActiveDuringPostConstruct = false;

    @Inject
    private BeanManager beanManager;

    @PostConstruct
    public void construct() {
        try {
            dependentContextActiveDuringPostConstruct = beanManager.getContext(Dependent.class).isActive();
        } catch (ContextNotActiveException e) {
            dependentContextActiveDuringPostConstruct = false;
        }
    }

    public static boolean isDestroyed() {
        return destroyed;
    }

    public static void setDestroyed(boolean destroyed) {
        Fox.destroyed = destroyed;
    }

    public static void setDestroyCount(int destroyCount) {
        Fox.destroyCount = destroyCount;
    }

    public static int getDestroyCount() {
        return destroyCount;
    }

    public static boolean isDependentContextActiveDuringPostConstruct() {
        return dependentContextActiveDuringPostConstruct;
    }

    public static void setDependentContextActiveDuringPostConstruct(boolean dependentContextActiveDuringPostConstruct) {
        Fox.dependentContextActiveDuringPostConstruct = dependentContextActiveDuringPostConstruct;
    }

    @PreDestroy
    public void destroy() {
        destroyed = true;
        destroyCount++;
    }

    public String getName() {
        return "gavin";
    }

    @Remove
    public void remove() {

    }

}
