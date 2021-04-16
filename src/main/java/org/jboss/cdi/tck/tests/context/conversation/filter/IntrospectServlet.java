/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.context.conversation.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import jakarta.enterprise.context.Conversation;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.SimpleLogger;
import org.jboss.cdi.tck.util.Timer;
import org.jboss.cdi.tck.util.Timer.StopCondition;

@SuppressWarnings("serial")
@WebServlet("/introspect")
public class IntrospectServlet extends HttpServlet {

    private static final SimpleLogger logger = new SimpleLogger(IntrospectServlet.class);

    public static final String MODE_INIT = "init";

    public static final String MODE_INSPECT = "inspect";

    public static final String MODE_LONG_TASK = "long_task";

    public static final String MODE_BUSY_REQUEST = "busy_request";

    @Inject
    Dummy dummy;

    @Inject
    Conversation conversation;

    @Inject
    Tester tester;

    @Inject
    State state;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/plain");
        String mode = req.getParameter("mode");

        if (MODE_INIT.equals(mode)) {
            conversation.begin();
            dummy.ping();
            state.reset();
            resp.getWriter().write(conversation.getId() + "::" + req.getSession().getId());
        } else if (MODE_INSPECT.equals(mode)) {
            // Conversation should be activated after custom filter
            if (conversation.isTransient()) {
                resp.sendError(500, "No long running conversation");
            } else {
                resp.getWriter().write("" + tester.getResult());
            }
        } else if (MODE_LONG_TASK.equals(mode)) {
            if (conversation.isTransient()) {
                resp.sendError(500, "No long running conversation");
            } else {
                long start = System.currentTimeMillis();
                try {
                    // The concurrent access timeout is not defined by the spec
                    new Timer().setSleepInterval(100l).setDelay(10, TimeUnit.SECONDS).addStopCondition(new StopCondition() {
                        @Override
                        public boolean isSatisfied() {
                            return state.isBusyAttemptMade();
                        }
                    }).start();
                } catch (InterruptedException e1) {
                    throw new IllegalStateException();
                }
                logger.log("Long task finished [isBusyAttemptMade: {0}, time: {1} ms]", state.isBusyAttemptMade(),
                        System.currentTimeMillis() - start);
                resp.getWriter().write("OK");
            }
        } else if (MODE_BUSY_REQUEST.equals(mode)) {
            resp.sendError(500, "BusyConversationException should be thrown");
        } else {
            throw new ServletException("Unknown test mode");
        }
    }

}
