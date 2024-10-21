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
package org.jboss.cdi.tck.tests.implementation.builtin.metadata.session;

import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Decorated;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.Decorator;
import jakarta.inject.Inject;

@jakarta.decorator.Decorator
@SuppressWarnings({ "unused", "serial" })
public class BakeryProductDecorator implements BakeryProduct {

    @Inject
    @Delegate
    private BakeryProduct delegate;

    @Inject
    private Bean<BakeryProductDecorator> bean;

    @Inject
    private Decorator<BakeryProductDecorator> decorator;

    @Inject
    @Decorated
    private Bean<BakeryProduct> decoratedBean;

    public Bean<BakeryProductDecorator> getBean() {
        return bean;
    }

    public Decorator<BakeryProductDecorator> getDecorator() {
        return decorator;
    }

    public Bean<? extends BakeryProduct> getDecoratedBean() {
        return decoratedBean;
    }

    public BakeryProductDecorator getDecoratorInstance() {
        return this;
    }
}
