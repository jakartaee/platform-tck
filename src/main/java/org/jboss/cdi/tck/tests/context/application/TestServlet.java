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

package org.jboss.cdi.tck.tests.context.application;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet used to test application context during service method and also for reporting test results of testing the context
 * during a method invocation on ServletContextListener, HttpSessionListener and ServlerRequestListener.
 * 
 * @author David Allen
 * 
 */
public class TestServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Inject
    private BeanManager jsr299Manager;
    @Inject
    private Result result;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("servlet".equals(req.getParameter("test"))) {
            if (jsr299Manager.getContext(ApplicationScoped.class).isActive()) {
                resp.setStatus(200);
            } else {
                resp.setStatus(500);
            }
        } else if ("servletContextListener".equals(req.getParameter("test"))) {
            testServletContextListener(req, resp);
        } else if ("httpSessionListener".equals(req.getParameter("test"))) {
            testHttpSessionListener(req, resp);
        } else if ("servletRequestListener".equals(req.getParameter("test"))) {
            testServletRequestListener(req, resp);
        } else {
            resp.setStatus(404);
        }
    }

    private void testServletContextListener(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus((result.isApplicationScopeActiveForServletContextListener()) ? 200 : 500);
    }

    private void testHttpSessionListener(HttpServletRequest req, HttpServletResponse resp) {
        req.getSession().setAttribute("foo", "bar");
        resp.setStatus((result.isApplicationScopeActiveForHttpSessionListener()) ? 200 : 500);
    }

    private void testServletRequestListener(HttpServletRequest req, HttpServletResponse resp) {
        resp.setStatus((result.isApplicationScopeActiveForServletRequestListener()) ? 200 : 500);
    }
}
