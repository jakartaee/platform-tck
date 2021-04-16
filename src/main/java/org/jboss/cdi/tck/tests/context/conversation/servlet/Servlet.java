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
package org.jboss.cdi.tck.tests.context.conversation.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.enterprise.context.Conversation;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.ActionSequence;

@SuppressWarnings("serial")
@WebServlet("/servlet/*")
public class Servlet extends HttpServlet {

    @Inject
    private Message message;

    @Inject
    private Conversation conversation;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith("/display")) {
            printInfo(resp.getWriter());
        } else if (uri.endsWith("/begin")) {
            conversation.begin();
            printInfo(resp.getWriter());
        } else if (uri.endsWith("/end")) {
            conversation.end();
            printInfo(resp.getWriter());
        } else if (uri.endsWith("/set")) {
            setMessage(req);
            printInfo(resp.getWriter());
        } else if (uri.endsWith("/invalidateSession")) {
            ActionSequence.addAction("beforeInvalidate");
            req.getSession().invalidate();
            ActionSequence.addAction("afterInvalidate");
            printInfo(resp.getWriter());
        } else if (uri.endsWith("/resetSequence")) {
            ActionSequence.reset();
        } else if (uri.endsWith("/getSequence")) {
            resp.getWriter().print(ActionSequence.getSequence().dataToCsv());
        } else {
            resp.setStatus(404);
        }
        resp.setContentType("text/plain");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        if (uri.endsWith("/set")) {
            setMessage(req);
            printInfo(resp.getWriter());
        } else {
            resp.setStatus(404);
        }
        resp.setContentType("text/plain");
    }

    private void printInfo(PrintWriter writer) {
        writer.append("message: " + message.getValue());
        writer.append("\n");
        writer.append("cid: [" + conversation.getId());
        writer.append("]");
        writer.append("\n");
        writer.append("transient: " + conversation.isTransient());
    }

    private void setMessage(HttpServletRequest request) {
        String value = request.getParameter("message");
        if (value == null) {
            throw new IllegalArgumentException("message must be specified");
        }
        message.setValue(value);
    }
}
