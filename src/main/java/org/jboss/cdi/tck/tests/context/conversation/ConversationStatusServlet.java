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

import java.io.IOException;
import java.io.ObjectOutputStream;

import jakarta.enterprise.context.Conversation;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.BeanLookupUtils;

public class ConversationStatusServlet extends HttpServlet {

    private static final long serialVersionUID = 2984756941080790899L;

    @Inject
    private BeanManager beanManager;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if ("cid".equals(method)) {
            Conversation conversation = BeanLookupUtils.getContextualReference(beanManager, Conversation.class);
            serializeToResponse(conversation.getId(), resp);
        } else if ("cloudDestroyed".equals(method)) {
            if (Cloud.isDestroyed()) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(208);
            }
        } else if ("conversationContextDestroyed".equals(method)) {
            if (ConversationContextObserver.isDestroyed()) {
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(208);
            }
        } else if ("invalidateSession".equals(method)) {
            req.getSession().invalidate();
        } else if ("resetCloud".equals(method)) {
            Cloud.setDestroyed(false);
        } else if ("resetConversationContextObserver".equals(method)) {
            ConversationContextObserver.reset();
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void serializeToResponse(Object object, HttpServletResponse resp) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(resp.getOutputStream());
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

}
