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
package org.jboss.cdi.tck.tests.context.conversation.event.notattached;

import java.util.concurrent.atomic.AtomicReference;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.event.Observes;

@ApplicationScoped
public class ObservingBean {

    private final AtomicReference<String> destroyedConversationId = new AtomicReference<String>();

    public void observeConversationDestroyed(@Observes @Destroyed(ConversationScoped.class) String event) {

        if (destroyedConversationId.get() != null) {
            throw new IllegalStateException("Only one non-attached long-runnnig conversation should be destroyed");
        }
        destroyedConversationId.set(event);
    }

    public AtomicReference<String> getDestroyedConversationId() {
        return destroyedConversationId;
    }

    public void reset() {
        destroyedConversationId.set(null);
    }

}
