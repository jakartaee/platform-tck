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

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.event.Event;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * @author Martin Kouba
 */
@Dependent
public class Qux {

    @Inject
    private Event<BusinessOperationObservedEvent> observedEvent;

    @Inject
    private Util util;

    public void observeBusinessOperation(@Observes BusinessOperationEvent event) {
        util.utilize();
        observedEvent.fire(new BusinessOperationObservedEvent());
    }

}
