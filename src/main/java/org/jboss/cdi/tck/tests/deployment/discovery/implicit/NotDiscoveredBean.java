/*
 * Copyright 2015, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.deployment.discovery.implicit;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

public class NotDiscoveredBean {

    public static boolean disposerCalled = false;

    @Produces
    public ProducedBean producedBean = new ProducedBean();

    @Produces
    public ProducedBean create() {
        return new ProducedBean();
    }

    public void observer(@Observes ProducedBean message) {
    }

    public void dispose(@Disposes ProducedBean bean) {
        disposerCalled = true;
    }

}
