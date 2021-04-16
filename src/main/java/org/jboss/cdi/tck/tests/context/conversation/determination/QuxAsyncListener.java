/*
 * JBoss, Home of Professional Open Source
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

package org.jboss.cdi.tck.tests.context.conversation.determination;

import java.io.IOException;
import jakarta.enterprise.context.Conversation;
import jakarta.inject.Inject;
import jakarta.servlet.AsyncEvent;
import jakarta.servlet.AsyncListener;

/**
 * @author Martin Kouba
 * 
 */
public class QuxAsyncListener implements AsyncListener {

    @Inject
    Conversation conversation;

    @Inject
    StatusBean statusBean;

    @Override
    public void onComplete(AsyncEvent event) throws IOException {

        if (!statusBean.isOnTimeout() && !statusBean.isOnError()) {
            statusBean.setOnComplete(checkSameConversationActive());
        }
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        statusBean.setOnTimeout(checkSameConversationActive());
        event.getAsyncContext().complete();
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        statusBean.setOnError(checkSameConversationActive());
        event.getAsyncContext().complete();
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {

        statusBean.setOnStartAsync(checkSameConversationActive());
    }

    public boolean checkSameConversationActive(){
        return FooServlet.CID.equals(conversation.getId());
    }

}
