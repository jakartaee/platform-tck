/*
 * Copyright (c) 2015, 2023 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.websocket.negdep.onmessage.client.pongduplicate;

import java.io.IOException;
import java.lang.System.Logger;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.negdep.EchoServerEndpoint;
import com.sun.ts.tests.websocket.negdep.NegativeDeploymentClient;

import jakarta.websocket.DeploymentException;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 *                     lib.name;
 */
/**
 * @OnMessage can contain one pong message handling method. This test checks two
 *            such methods fail the deployment.
 * @since 1.11
 */
@ExtendWith(ArquillianExtension.class)
public class WSCClientIT extends NegativeDeploymentClient {

	private static final long serialVersionUID = 111L;

	private static final Logger logger = System.getLogger(WSCClientIT.class.getName());

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_negdep_onmessage_client_pongduplicate_web.war");
		archive.addClass(EchoServerEndpoint.class);
		archive.addClass(IOUtil.class);
		return archive;
	};

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_negdep_onmessage_client_pongduplicate_web");
	}

	/*
	 * @testName: duplicateOnMessageTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3;WebSocket:SPEC:WSC-4.7-4;
	 * WebSocket:SPEC:WSC-5.2.1-4;
	 * 
	 * @test_Strategy: In both cases, a deployment error raised during the
	 * deployment process must halt the deployment of the application, any well
	 * formed endpoints deployed prior to the error being raised must be removed
	 * from service and no more websocket endpoints from that application may be
	 * deployed by the container, even if they are valid.
	 * 
	 * Each websocket endpoint may only have one message handling method for each of
	 * the native websocket message formats: text, binary and pong. The websocket
	 * implementation must not deploy such an endpoint and must raise a deployment
	 * error if an attempt is made to deploy such an annotated endpoint. [WSC-4.7-4]
	 * 
	 * If the deployment error occurs under the programmatic control of the
	 * developer, for example, when using the WebSocketContainer API to deploy a
	 * client endpoint, deployment errors must be reported by the container to the
	 * developer by using an instance of the DeploymentException. [WSC-5.2.1-4]
	 * 
	 * To check the test fails when deployment pass, comment out of the
	 * 
	 * @OnMessage of OnMessageServerEndpoint
	 */
	@Test
	public void duplicateOnMessageTest() throws Exception {
		setEntity("anything");
		OnMessageClientEndpoint endpoint = new OnMessageClientEndpoint();
		setAnnotatedClientEndpoint(endpoint);
		setProperty(Property.REQUEST, buildRequest("echo"));
		setProperty(Property.STATUS_CODE, "-1");
		logExceptionOnInvocation(false);
		printClientCall(false);
		boolean thrown = true;
		try {
			invoke(false);
			thrown = false;
		} catch (Exception tfe) {
			// DeploymentException
			assertCause(tfe, DeploymentException.class, "The DeploymentException has not been thrown!");
			tfe.printStackTrace();
			logMsg("--\n\n");
			logMsg("DeploymentException has been thrown as expected");
		}
		if (!thrown)
			super.throwDeploymentDidNotFail();

		boolean isSession = false;
		try {
			getSession();
			isSession = true;
		} catch (Exception f) {
			// did not succeeded
			logger.log(Logger.Level.DEBUG,"The session is closed as expected");
		}
		if (isSession) {
			logger.log(Logger.Level.ERROR,"Session is not null!");
			throw new Exception("Session is not null! Deployment succeeded");
		}
	}
}
