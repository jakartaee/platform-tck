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

import java.io.Serializable;

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.jboss.cdi.tck.util.SimpleLogger;

@Named
@ConversationScoped
public class Storm implements Serializable {

    private static final long serialVersionUID = -1513633490356967202L;

    private final SimpleLogger logger = new SimpleLogger(Storm.class);

    @Inject
    Conversation conversation;

    private String strength;

    public String thunder() {
        return "thunder";
    }

    public String lightening() {
        return "lightening?faces-redirect=true";
    }

    public void beginConversation() {
        conversation.begin();
        logger.log("Long-running conversation {0}", conversation.getId());
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        logger.log("Set strength {0} in conversation {1}", strength, conversation.getId());
        this.strength = strength;
    }

}
