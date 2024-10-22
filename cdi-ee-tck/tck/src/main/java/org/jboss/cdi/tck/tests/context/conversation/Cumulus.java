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

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ConversationScoped
@Named
public class Cumulus implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int timeout = 15000;
    private static final String customCid = "humilis";
    private static final String customCidAlreadyInUse = "alreadyInUse";

    @Inject
    private Conversation conversation;

    public void beginConversation() {
        conversation.begin();
    }

    public void beginConversationIdentifiedByCustomIdentifier() {
        conversation.begin(customCid);
    }

    public void beginNextConversationIdentifiedByCustomIdentifier() {
        conversation.begin(customCidAlreadyInUse);
    }

    public String beginConversationIdentifiedByCustomIdentifierAndSwallowException() {
        try {
            beginNextConversationIdentifiedByCustomIdentifier();
            return "error";
        } catch (IllegalArgumentException e) {
            return "home";
        }
    }

    public String beginConversationAndSwallowException() {
        try {
            conversation.begin();
            return "error";
        } catch (IllegalStateException e) {
            return "home";
        }
    }

    public void beginConversationAndSetTimeout() {
        conversation.begin();
        conversation.setTimeout(timeout);
    }

    public void endConversation() {
        conversation.end();
    }

    public String endConversationAndSwallowException() {
        try {
            conversation.end();
            return "error";
        } catch (IllegalStateException e) {
            return "home";
        }
    }

    public boolean isConversationIdentifiedByCustomIdentifier() {
        return !conversation.isTransient() && conversation.getId().equals(customCid);
    }

    public boolean isConversationIdentifierNull() {
        return conversation.getId() == null;
    }

    public boolean isConversationTimeoutSetProperly() {
        return conversation.getTimeout() == timeout;
    }

    public boolean isConversationHasDefaultTimeout() {
        return conversation.getTimeout() > 0;
    }
}
