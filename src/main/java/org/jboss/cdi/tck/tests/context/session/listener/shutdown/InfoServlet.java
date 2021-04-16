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

package org.jboss.cdi.tck.tests.context.session.listener.shutdown;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.SimpleLogger;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/info")
public class InfoServlet extends HttpServlet {

    private static final SimpleLogger logger = new SimpleLogger(InfoServlet.class);

    @Inject
    private TestFlag testFlag;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("set".equals(action)) {
            logger.log("Set test flag");
            testFlag.set();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.getWriter().write("" + testFlag.isSet());
        } else if ("get".equals(action)) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.getWriter().write("" + testFlag.isSet());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
