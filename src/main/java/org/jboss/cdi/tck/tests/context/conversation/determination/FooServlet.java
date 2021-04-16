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
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Martin Kouba
 * 
 */
@WebServlet("/foo")
public class FooServlet extends HttpServlet {

    public static final String CID = "foo";

    private static final long serialVersionUID = 1L;

    @Inject
    Conversation conversation;

    @Inject
    TestResult testResult;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        // Force session creation as we don't use any conversation/session scoped beans
        req.getSession(true);

        if ("begin".equals(action)) {
            conversation.begin(CID);
            resp.setContentType("text/plain");
            resp.getWriter().append("cid: [" + conversation.getId() + "]");
            resp.getWriter().append("transient: " + conversation.isTransient());
        } else if ("test".equals(action)) {

            if (FooServlet.CID.equals(conversation.getId())) {
                // The long-running conversation is available
                testResult.setServletOk();
            }
            resp.setContentType("text/plain");
            resp.getWriter().write(testResult.toString());
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

    }

}
