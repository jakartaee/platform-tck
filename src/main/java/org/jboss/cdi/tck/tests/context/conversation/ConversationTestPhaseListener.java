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

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.context.ConversationScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.BeanLookupUtils;

public class ConversationTestPhaseListener implements PhaseListener {

    private static final long serialVersionUID = 1197355854770726526L;

    public static final String ACTIVE_BEFORE_APPLY_REQUEST_VALUES_HEADER_NAME = "org.jboss.cdi.tck.activeBeforeApplyRequestValues";

    private boolean activeBeforeApplyRequestValues;

    public void afterPhase(PhaseEvent event) {
    }

    public void beforePhase(PhaseEvent event) {

        BeanManager beanManager = CDI.current().getBeanManager();

        if (event.getPhaseId().equals(PhaseId.APPLY_REQUEST_VALUES)) {
            try {
                beanManager.getContext(ConversationScoped.class);
                activeBeforeApplyRequestValues = true;
            } catch (ContextNotActiveException e) {
                activeBeforeApplyRequestValues = false;
            }
        }
        if (event.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
            Conversation conversation = CDI.current().select(Conversation.class).get();
            HttpServletResponse response = (HttpServletResponse) event.getFacesContext().getExternalContext().getResponse();
            response.addHeader(AbstractConversationTest.CID_HEADER_NAME,
                    conversation.getId() == null ? " null" : conversation.getId());
            response.addHeader(AbstractConversationTest.LONG_RUNNING_HEADER_NAME, String.valueOf(!conversation.isTransient()));
            response.addHeader(Cloud.RAINED_HEADER_NAME, new Boolean(BeanLookupUtils.getContextualReference(beanManager, Cloud.class)
                    .isRained()).toString());
            response.addHeader(ACTIVE_BEFORE_APPLY_REQUEST_VALUES_HEADER_NAME,
                    new Boolean(activeBeforeApplyRequestValues).toString());
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }

}
