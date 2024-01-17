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
package org.jboss.cdi.tck.interceptors.tests.bindings.resolution.ejb;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.AroundTimeout;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

@Interceptor
@TransactionalBinding
@LoggedBinding
@MessageBinding
@PingBinding
@PongBinding
@BallBinding(requiresBall = true)
public class ComplicatedInterceptor {

    public static boolean intercepted = false;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        intercepted = true;
        return ctx.proceed();
    }

    @AroundTimeout
    public Object timeout(InvocationContext ctx) throws Exception {
        intercepted = true;
        return ctx.proceed();
    }

    public static void reset() {
        intercepted = false;
    }
}
