/*
 * Copyright (c) 2015, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.negdep.onmessage.srv.pongduplicate;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.impl.StringPongMessage;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.negdep.NegativeDeploymentClient;

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

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_negdep_onmessage_srv_pongduplicate_web.war");
		archive.addPackages(false, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.negdep.onmessage.srv.pongduplicate");
		archive.addPackages(false, Filters.exclude(NegativeDeploymentClient.class),
				"com.sun.ts.tests.websocket.negdep");
		archive.addPackages(true, "com.sun.ts.tests.websocket.common.stringbean");
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_negdep_onmessage_srv_pongduplicate_web");
	}

	/*
	 * @testName: duplicateOnMessageUndeployOtherEndpointTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3;WebSocket:SPEC:WSC-4.7-4;
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
	 * To check the test fails when deployment pass, comment out of the
	 * 
	 * @OnMessage of OnMessageServerEndpoint
	 */
	@Test
	public void duplicateOnMessageUndeployOtherEndpointTest() throws Exception {
		setProperty(Property.CONTENT, "anything");
		setProperty(Property.REQUEST, buildRequest("echo"));
		setProperty(Property.STATUS_CODE, "-1");
		// logExceptionOnInvocation(false);
		try {
			invoke(false);
		} catch (Exception tfe) {
			// DeploymentException
		}
		String response = getResponseAsString();
		if ("anything".equals(response))
			throwValidEndpointMustBeRemoved();
	}

	/*
	 * @testName: duplicateOnMessageTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3;WebSocket:SPEC:WSC-4.7-4;
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
	 * To check the test fails when deployment pass, comment out of the
	 * 
	 * @OnMessage of OnMessageServerEndpoint
	 */
	@Test
	public void duplicateOnMessageTest() throws Exception {
		setEntity(new StringPongMessage("anything"));
		setProperty(Property.REQUEST, buildRequest("invalid"));
		setProperty(Property.STATUS_CODE, "-1");
		// logExceptionOnInvocation(false);
		try {
			invoke(false);
		} catch (Exception tfe) {
			// DeploymentException
		}
		String response = getResponseAsString();
		if (response != null && response.contains("anything"))
			throwValidEndpointMustBeRemoved();
	}
}
