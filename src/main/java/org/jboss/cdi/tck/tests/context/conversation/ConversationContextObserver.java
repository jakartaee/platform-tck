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
package org.jboss.cdi.tck.tests.context.conversation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.ServletRequest;

@ApplicationScoped
public class ConversationContextObserver {

    private static boolean initialized = false;
    private static boolean destroyed = false;

    public void observeConversationInitialized(@Observes @Initialized(ConversationScoped.class) ServletRequest event) {
        initialized = true;
    }

    public void observeConversationDestroyed(@Observes @Destroyed(ConversationScoped.class) ServletRequest event) {
        destroyed = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static boolean isDestroyed() {
        return destroyed;
    }

    public static void reset() {
        initialized = false;
        destroyed = false;
    }
}
