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
package org.jboss.cdi.tck.tests.context.conversation.event;

import java.io.IOException;

import jakarta.enterprise.context.Conversation;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
@SuppressWarnings("serial")
public class Servlet extends HttpServlet {

    @Inject
    Conversation conversation;

    @Inject
    ConversationScopedObserver conversationScopedObserver;

    @Inject
    ApplicationScopedObserver applicationScopedObserver;

    @Inject
    ConversationScopedBean bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith("/begin")) {
            conversation.begin(ConversationScopedBean.CID);
            bean.ping();
        } else if (uri.contains("/end")) {
            req.setAttribute("foo", System.currentTimeMillis());
            conversation.end();
        } else if (uri.contains("/display-transient")) {
            req.setAttribute("foo", System.currentTimeMillis());
        }
        resp.getWriter().append("Initialized:" + conversationScopedObserver.isInitializedObserved());
        resp.getWriter().append("\n");
        resp.getWriter().append("Before Destroyed:" + applicationScopedObserver.isBeforeDestroyedCalled());
        resp.getWriter().append("\n");
        resp.getWriter().append("Destroyed:" + applicationScopedObserver.isDestroyedCalled());
        resp.getWriter().append("\n");
        resp.getWriter().append("cid:" + conversation.getId());
        resp.setContentType("text/plain");
    }
}
