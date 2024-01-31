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
package org.jboss.cdi.tck.interceptors.tests.contract.lifecycleCallback.ejb;

import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

import static org.testng.Assert.assertNotNull;

public class AnimalInterceptor {
    private static Set<String> postConstructInterceptorCalledFor = new HashSet<String>();
    private static Set<String> preDestroyInterceptorCalledFor = new HashSet<String>();

    @PostConstruct
    public void postConstruct(InvocationContext ctx) {
        Animal target = (Animal) ctx.getTarget();
        assertNotNull(target.getBar());
        postConstructInterceptorCalledFor.add((target).getAnimalType());
        try {
            ctx.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        if (ctx.getMethod().getName().equals("echo")) {
            return ctx.proceed() + ctx.getParameters()[0].toString();
        } else {
            return ctx.proceed();
        }
    }

    @PreDestroy
    public void preDestroy(InvocationContext ctx) {
        preDestroyInterceptorCalledFor.add(((Animal) ctx.getTarget()).getAnimalType());
        try {
            ctx.proceed();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isPostConstructInterceptorCalled(String animalType) {
        return postConstructInterceptorCalledFor.contains(animalType);
    }

    public static boolean isPreDestroyInterceptorCalled(String animalType) {
        return preDestroyInterceptorCalledFor.contains(animalType);
    }
}
