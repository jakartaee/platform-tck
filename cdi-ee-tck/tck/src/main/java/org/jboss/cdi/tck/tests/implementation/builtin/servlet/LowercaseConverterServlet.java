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
package org.jboss.cdi.tck.tests.implementation.builtin.servlet;

import java.io.IOException;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/")
@SuppressWarnings("serial")
public class LowercaseConverterServlet extends HttpServlet {

    @Inject
    LowercaseConverter lowercaseConverter;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getSession().setAttribute(LowercaseConverter.TEXT, null);
        req.getSession().getServletContext().setAttribute(LowercaseConverter.TEXT, "ServletCONTEXT");

        if (req.getRequestURI().contains("/convert-request")) {
            resp.getWriter().append(lowercaseConverter.convert(null));
        } else if (req.getRequestURI().contains("/convert-session")) {
            req.getSession().setAttribute(LowercaseConverter.TEXT, "SesSion");
            resp.getWriter().append(lowercaseConverter.convert(null));
        } else if (req.getRequestURI().contains("/convert-context")) {
            req.getSession().getServletContext().setAttribute(LowercaseConverter.TEXT, "ServletCONTEXT");
            resp.getWriter().append(lowercaseConverter.convert(null));
        }
        resp.setContentType("text/plain");
    }
}
