/*
 * Copyright 2021, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.implementation.builtin.metadata.session;

import java.io.Serializable;

import jakarta.enterprise.inject.Intercepted;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Interceptor;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

@SuppressWarnings("serial")
@jakarta.interceptor.Interceptor
@Frozen
public class YoghurtInterceptor implements Serializable {

    @Inject
    private Bean<YoghurtInterceptor> bean;
    @Inject
    private Interceptor<YoghurtInterceptor> interceptor;
    @Inject
    @Intercepted
    private Bean<?> interceptedBean;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        return this;
    }

    public Bean<YoghurtInterceptor> getBean() {
        return bean;
    }

    public Interceptor<YoghurtInterceptor> getInterceptor() {
        return interceptor;
    }

    public Bean<?> getInterceptedBean() {
        return interceptedBean;
    }
}
