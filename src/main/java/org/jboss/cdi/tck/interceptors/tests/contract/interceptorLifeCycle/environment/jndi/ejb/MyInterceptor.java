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
package org.jboss.cdi.tck.interceptors.tests.contract.interceptorLifeCycle.environment.jndi.ejb;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Priority;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@MyBinding
@Priority(1000)
@Interceptor
public class MyInterceptor {

    private static boolean interceptorCalled = false;
    private static String greeting;
    private static Animal animal;

    @Resource(name = "greeting")
    private void setGreeting(String greeting) {
        MyInterceptor.greeting = greeting;
    }

    @Inject
    private void init() throws NamingException {
        MyInterceptor.animal = (Animal) InitialContext.doLookup("java:module/Animal");
    }

    public static void reset() {
        greeting = null;
        animal = null;
        interceptorCalled = false;
    }

    public static boolean isInterceptorCalled() {
        return interceptorCalled;
    }

    public static String getGreeting() {
        return greeting;
    }

    public static Animal getAnimal() {
        return animal;
    }

    @PostConstruct
    private void intercept(InvocationContext ctx) {
        interceptorCalled = true;
        try {
            ctx.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
