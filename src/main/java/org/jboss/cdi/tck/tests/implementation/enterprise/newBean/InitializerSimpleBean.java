/*
 * JBoss, Home of Professional Open Source
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
package org.jboss.cdi.tck.tests.implementation.enterprise.newBean;

import java.io.Serializable;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Stateful
@SessionScoped
@Named("Charlie")
@Default
public class InitializerSimpleBean implements Serializable, InitializerSimpleBeanLocal {
    private static final long serialVersionUID = 1L;
    private static int initializerCalls = 0;

    @SuppressWarnings("unused")
    @Inject
    private BeanManager beanManager;

    @Inject
    public void initializer() {
        initializerCalls++;
    }

    public void businessMethod() {

    }

    public int getInitializerCalls() {
        return initializerCalls;
    }

    public void setInitializerCalls(int initializerCalls) {
        InitializerSimpleBean.initializerCalls = initializerCalls;
    }
}
