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
package org.jboss.cdi.tck.tests.extensions.interceptors;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

/**
 * @author Stuart Douglas stuart@baileyroberts.com.au
 */
@FullMarathon
@Interceptor
public class LifecycleInterceptor {
    static private boolean preDestroyCalled = false;
    static private boolean postConstructCalled = false;

    @PreDestroy
    public void preDestroy(InvocationContext ctx) {
        preDestroyCalled = true;
    }

    @PostConstruct
    public void postConstruct(InvocationContext ctx) {
        Object marathon = ctx.getTarget();
        if (marathon instanceof Marathon) {
            Marathon m = (Marathon) marathon;
            m.setLength(42);
        }
        postConstructCalled = true;
    }

    static public boolean isPostConstructCalled() {
        return postConstructCalled;
    }

    static public boolean isPreDestroyCalled() {
        return preDestroyCalled;
    }

}
