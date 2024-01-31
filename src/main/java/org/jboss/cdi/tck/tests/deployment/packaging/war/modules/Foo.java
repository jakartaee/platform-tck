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

package org.jboss.cdi.tck.tests.deployment.packaging.war.modules;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * @author Martin Kouba
 */
@Secured
@RequestScoped
public class Foo implements Business {

    private Bar bar;

    @Inject
    private Event<BusinessOperationEvent> event;

    public static Boolean legacyObserved;

    public Foo() {
    }

    @Inject
    public Foo(Bar bar) {
        this.bar = bar;
    }

    public Bar getBar() {
        return bar;
    }

    @Override
    public void businessOperation1() {
        event.fire(new BusinessOperationEvent());
    }

    @Override
    public void businessOperation2() {
    }

    public void observeLegacyServiceDisposal(@Observes LegacyService legacyService) {
        legacyObserved = LegacyService.cleanupPerformed;
    }

}
