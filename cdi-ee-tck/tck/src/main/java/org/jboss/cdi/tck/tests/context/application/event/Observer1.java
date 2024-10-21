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
package org.jboss.cdi.tck.tests.context.application.event;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.ServletContext;

import org.jboss.cdi.tck.util.SimpleLogger;

@ApplicationScoped
public class Observer1 {

    private static final SimpleLogger logger = new SimpleLogger(Observer1.class);

    private boolean observed;

    void observe(@Observes @Initialized(ApplicationScoped.class) ServletContext event) {
        if (observed) {
            throw new IllegalStateException("ServletContext invoked multiple times.");
        }
        observed = true;
    }

    public boolean isObserved() {
        return observed;
    }

    void observeDestroyedServletContext(@Observes @Destroyed(ApplicationScoped.class) ServletContext event) {
        logger.log("ServletContext destroyed"); // this is tricky to test properly
    }
}
