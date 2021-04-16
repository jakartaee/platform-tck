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
package org.jboss.cdi.tck.tests.context.session;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;

import org.jboss.cdi.tck.util.ActionSequence;
import org.jboss.cdi.tck.util.SimpleLogger;

/**
 * Just to verify session context is destroyed after Filter is called.
 */
@WebFilter(filterName = "IntrospectFilter", urlPatterns = "/introspect")
public class IntrospectFilter implements Filter {

	private static final SimpleLogger logger = new SimpleLogger(
			IntrospectFilter.class);

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		logger.log("Do filter...");
		chain.doFilter(request, response);
		String mode = request.getParameter("mode");
        if (IntrospectServlet.MODE_INVALIDATE.equals(mode)) {
            ActionSequence.addAction(IntrospectFilter.class.getName());
        }
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
