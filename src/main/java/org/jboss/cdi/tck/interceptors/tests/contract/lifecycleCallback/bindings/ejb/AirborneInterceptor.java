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
package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback.bindings.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.interceptor.AroundConstruct;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.jboss.cdi.tck.util.ActionSequence;

@Airborne
@Interceptor
public class AirborneInterceptor {

    @PreDestroy
    public void preDestroy(InvocationContext ctx) {
        ActionSequence.addAction("preDestroy", AirborneInterceptor.class.getSimpleName());
        try {
            ctx.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    public void postConstruct(InvocationContext ctx) {
        ActionSequence.addAction("postConstruct", AirborneInterceptor.class.getSimpleName());
        try {
            ctx.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @AroundConstruct
    public void aroundConstruct(InvocationContext ctx) {
        ActionSequence.addAction("aroundConstruct", AirborneInterceptor.class.getSimpleName());
        try {
            ctx.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
