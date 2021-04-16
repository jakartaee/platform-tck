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
package org.jboss.cdi.tck.tests.context.conversation.event.notattached;

import java.io.IOException;

import jakarta.enterprise.context.Conversation;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/test")
@SuppressWarnings("serial")
public class TestServlet extends HttpServlet {

    @Inject
    private ObservingBean observer;

    @Inject
    private Conversation conversation;

    @Inject
    private ConversationScopedBean bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("begin".equals(action)) {
            // Begin long-running conversation and force the context to be initialized (it's lazy by default)
            conversation.begin(ConversationScopedBean.CID);
            bean.ping();
            observer.reset();
            resp.getWriter().append("cid:" + conversation.getId());
        } else if ("invalidate".equals(action)) {
            // Invalidate session - all long-running conversations will be destroyed after the servlet service() method
            // completes
            req.getSession().invalidate();
        } else if ("info".equals(action)) {
            resp.getWriter().append("destroyed cid:" + observer.getDestroyedConversationId().get());
        }
        resp.setContentType("text/plain");
    }
}
