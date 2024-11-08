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
package org.jboss.cdi.tck.tests.context.request;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.ActionSequence;

@WebServlet(name = "IntrospectServlet", urlPatterns = "/introspect")
public class IntrospectServlet extends HttpServlet {

    public static final String MODE_COLLECT = "collect";

    public static final String MODE_VERIFY = "verify";

    private static final long serialVersionUID = 1L;

    @Inject
    private SimpleRequestBean simpleBean;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/plain");
        String mode = req.getParameter("mode");

        if (mode == null) {
            resp.getWriter().append(simpleBean.getId());
        } else if (MODE_COLLECT.equals(mode)) {
            ActionSequence.reset();
            ActionSequence.addAction(IntrospectServlet.class.getName());
        } else if (MODE_VERIFY.equals(mode)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(ActionSequence.getSequence().toString());
        } else {
            throw new ServletException("Unknown guard mode");
        }
    }

}
