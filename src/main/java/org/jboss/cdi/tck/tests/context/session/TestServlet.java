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

package org.jboss.cdi.tck.tests.context.session;

import java.io.IOException;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet used just to test context during service method.
 *
 * @author David Allen
 * @author Martin Kouba
 */
@WebServlet(name = "TestServlet", urlPatterns = "/test")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private BeanManager beanManager;

	@Inject
	private SimpleSessionBean simpleBean;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		checkSessionContextActive();
		super.service(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType("text/text");
		resp.getWriter().println("It worked!");
	}

	private void checkSessionContextActive() throws ServletException {
		if (beanManager == null
				|| !beanManager.getContext(SessionScoped.class).isActive()
				|| simpleBean == null) {
			throw new ServletException("Session context is not active");
		}
		// Check bean invocation
		simpleBean.getId();
	}

}
