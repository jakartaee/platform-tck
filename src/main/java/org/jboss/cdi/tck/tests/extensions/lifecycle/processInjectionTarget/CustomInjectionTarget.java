/*
 * Copyright 2014, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.extensions.lifecycle.processInjectionTarget;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.enterprise.inject.spi.InjectionTarget;
import java.util.Set;

public class CustomInjectionTarget<T> implements InjectionTarget<T> {

    private InjectionTarget<T> wrappedInjectionTarget;
    private Runnable runnable;

    public CustomInjectionTarget(InjectionTarget<T> originalInjectionTarget, Runnable runnable) {
        this.wrappedInjectionTarget = originalInjectionTarget;
        this.runnable = runnable;
    }

    public void inject(T instance, CreationalContext<T> ctx) {
        runnable.run();
        wrappedInjectionTarget.inject(instance, ctx);
    }

    @Override
    public void postConstruct(T instance) {
        wrappedInjectionTarget.postConstruct(instance);
    }

    @Override
    public void preDestroy(T instance) {
        wrappedInjectionTarget.preDestroy(instance);
    }

    @Override
    public Object produce(CreationalContext ctx) {
        return wrappedInjectionTarget.produce(ctx);
    }

    @Override
    public void dispose(T instance) {
        wrappedInjectionTarget.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return wrappedInjectionTarget.getInjectionPoints();
    }

}
