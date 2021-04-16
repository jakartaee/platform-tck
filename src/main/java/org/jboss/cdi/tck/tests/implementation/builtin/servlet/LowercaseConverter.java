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
package org.jboss.cdi.tck.tests.implementation.builtin.servlet;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@SuppressWarnings("serial")
@SessionScoped
public class LowercaseConverter implements Serializable {

    protected static final String TEXT = "text";

    private long id = System.currentTimeMillis();

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private HttpSession httpSession;

    @Inject
    private ServletContext servletContext;

    /**
     * 
     * @param text
     * @return
     */
    public String convert(String text) {
        if (text == null) {
            // Request
            text = httpServletRequest.getParameter(TEXT);
            if (text == null) {
                // Session
                text = (String) httpSession.getAttribute(TEXT);
                if (text == null) {
                    // Servlet context
                    text = (String) servletContext.getAttribute(TEXT);
                }
            }
        }
        return text != null ? text.toLowerCase() : null;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public long getId() {
        return id;
    }

}
