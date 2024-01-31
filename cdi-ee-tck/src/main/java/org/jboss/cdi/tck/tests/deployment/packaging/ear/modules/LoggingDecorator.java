/*
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

package org.jboss.cdi.tck.tests.deployment.packaging.ear.modules;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;

/**
 * @author Martin Kouba
 * 
 */
@Decorator
public abstract class LoggingDecorator implements Business {

    private static AtomicInteger decorationPerformed = new AtomicInteger(0);

    @Inject
    @Delegate
    @Any
    private Business loggable;

    @Override
    public void businessOperation1() {
        decorationPerformed.incrementAndGet();
        loggable.businessOperation1();
    }

    public static int getNumberOfDecorationsPerformed() {
        return decorationPerformed.get();
    }

    public static void reset() {
        decorationPerformed.set(0);
    }

}
