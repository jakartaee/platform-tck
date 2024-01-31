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
package org.jboss.cdi.tck.tests.context.conversation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.Set;

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.spi.Bean;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ConversationScoped
@Named("builtin")
public class BuiltInConversation implements Serializable {
    private static final long serialVersionUID = 1L;
    @Inject
    private BeanManager manager;
    @Inject
    private Conversation conversation;

    public boolean isScopeCorrect() {
        return getConversationBean().getScope().equals(RequestScoped.class);
    }

    public boolean isQualifierCorrect() {
        for (Annotation qualifier : getConversationBean().getQualifiers()) {
            if (qualifier.annotationType().equals(Default.class)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNameCorrect() {
        return getConversationBean().getName().equals("jakarta.enterprise.context.conversation");
    }

    public boolean isDefaultConversationHasNullId() {
        return conversation.isTransient() && conversation.getId() == null;
    }

    private Bean<?> getConversationBean() {
        Set<Bean<?>> beans = manager.getBeans(Conversation.class);
        if (beans.size() != 1) {
            throw new RuntimeException("Multiple beans found for Conversation class.");
        }
        return beans.iterator().next();
    }
}
