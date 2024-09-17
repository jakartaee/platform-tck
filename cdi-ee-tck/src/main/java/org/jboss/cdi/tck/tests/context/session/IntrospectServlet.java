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

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jboss.cdi.tck.util.ActionSequence;

/**
 * Used to process requests to check which session context is in use.
 *
 * @author David Allen
 * @author Martin Kouba
 */
@WebServlet(name = "IntrospectServlet", urlPatterns = "/introspect")
public class IntrospectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public static final String MODE_INVALIDATE = "invalidate";

	public static final String MODE_VERIFY = "verify";

	public static final String MODE_TIMEOUT = "timeout";

	@Inject
	SimpleSessionBean simpleBean;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/text");
		String mode = req.getParameter("mode");

		if (mode == null) {
			resp.getWriter().append(simpleBean.getId());
		} else if (MODE_INVALIDATE.equals(mode)) {
			ActionSequence.reset();
			ActionSequence.addAction(IntrospectServlet.class.getName());
			req.getSession().invalidate();
		} else if (MODE_VERIFY.equals(mode)) {
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("text/plain");
			resp.getWriter().write(ActionSequence.getSequence().toString());
		} else if (MODE_TIMEOUT.equals(mode)) {
			ActionSequence.reset();
			req.getSession().setMaxInactiveInterval(1);
		} else {
			throw new ServletException("Unknown mode");
		}
	}

}
