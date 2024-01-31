/*
 * Copyright 2017, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.deployment.trimmed.enteprise;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessBeanAttributes;

public class TestExtension implements Extension {

    private AtomicInteger vehiclePBAinvocations = new AtomicInteger(0);
    private AtomicInteger bikePBAinvocations = new AtomicInteger(0);

    void observesVehiclePBA(@Observes ProcessBeanAttributes<? extends MotorizedVehicle> event) {
        vehiclePBAinvocations.incrementAndGet();
    }

    void observesBikePBA(@Observes ProcessBeanAttributes<? extends Bike> event) {
        bikePBAinvocations.incrementAndGet();
    }

    public AtomicInteger getVehiclePBAinvocations() {
        return vehiclePBAinvocations;
    }

    public AtomicInteger getBikePBAinvocations() {
        return bikePBAinvocations;
    }

}
