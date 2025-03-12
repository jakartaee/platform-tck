/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package com.sun.ts.tests.websocket.spec.servercontainer.upgradehttptowebsocket;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.server.ServerContainer;
import jakarta.websocket.server.ServerEndpointConfig;

@WebServlet("/TCKTestServlet")
public class WSTestServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServerContainer sc = (ServerContainer) getServletContext()
				.getAttribute("jakarta.websocket.server.ServerContainer");
		ServerEndpointConfig sec = ServerEndpointConfig.Builder.create(WSTestServer.class, "/TCKTestServer").build();
		try {
			sc.upgradeHttpToWebSocket(req, resp, sec, Collections.emptyMap());
		} catch (DeploymentException e) {
			throw new ServletException(e);
		}
	}

	private static final long serialVersionUID = 1L;

}
