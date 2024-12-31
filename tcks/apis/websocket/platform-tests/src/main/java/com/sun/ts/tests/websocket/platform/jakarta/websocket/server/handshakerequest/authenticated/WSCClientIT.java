/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.websocket.platform.jakarta.websocket.server.handshakerequest.authenticated;

import java.io.IOException;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
import com.sun.ts.tests.websocket.common.impl.ClientConfigurator;
import com.sun.ts.tests.websocket.common.util.IOUtil;

/*
 * The tests here are not guaranteed to pass in standalone TCK, hence it is put 
 * into full CTS bundle 
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 *                     user;
 *                     password;
 */
@ExtendWith(ArquillianExtension.class)
public class WSCClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = -5851958128006729743L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"wsc_platform_jakarta_websocket_handshakeresponse_authenticated_web.war");
		archive.addPackages(false, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.platform.jakarta.websocket.server.handshakerequest.authenticated");
		archive.addClasses(IOUtil.class);
		archive.addAsWebInfResource(WSCClientIT.class.getPackage(), "web.xml", "web.xml");
		return archive;
	};

	String user;

	String password;

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_platform_jakarta_websocket_handshakeresponse_authenticated_web");
	}

	@Override
	@BeforeEach
	public void setup() throws Exception {
		super.setup();
		user = assertProperty(new Properties(), "user");
		password = assertProperty(new Properties(), "password");
	}

	/*
	 * @testName: getUserPrincipalTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:179; WebSocket:JAVADOC:77;
	 * WebSocket:JAVADOC:15; WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-8.3-1;
	 * WebSocket:SPEC:WSC-7.2-2;
	 * 
	 * @test_Strategy: HandshakeRequest.getUserPrincipal
	 * HandshakeResponse.getHeaders ClientEndpointConfig.Configurator.afterResponse
	 * ServerEndpointConfig.Configurator.modifyHandshake A transport guarantee of
	 * NONE
	 * 
	 * Similarly, if the opening handshake request is already authenticated with the
	 * server, the opening handshake API allows the developer to query the user
	 * Principal of the request. If the connection is established with the
	 * requesting client, the websocket implementation considers the user Principal
	 * for the associated websocket Session to be the user Principal that was
	 * present on the opening handshake.
	 * 
	 * Return the authenticated user
	 */
	@Test
	public void getUserPrincipalTest() throws Exception {
		ClientConfigurator configurator = new ClientConfigurator();
		configurator.addToResponse(GetUserPrincipalConfigurator.KEY, String.valueOf(user));
		addClientConfigurator(configurator);
		addAuthorisation();
		invoke("auth/getuserprincipal", "anything", "anything");
		configurator.assertAfterResponseHasBeenCalled();
	}

	/*
	 * @testName: isUserInRoleTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:180; WebSocket:JAVADOC:77;
	 * WebSocket:JAVADOC:15; WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-8.3-1;
	 * 
	 * @test_Strategy: HandshakeRequest.isUserInRole HandshakeResponse.getHeaders
	 * ClientEndpointConfig.Configurator.afterResponse
	 * ServerEndpointConfig.Configurator.modifyHandshake A transport guarantee of
	 * NONE
	 */
	@Test
	public void isUserInRoleTest() throws Exception {
		// check DIRECTOR role is not known
		// and set staff role to check
		ClientConfigurator configurator = new ClientConfigurator();
		configurator.addToResponse(IsUserInRoleConfigurator.KEY, String.valueOf(false));
		configurator.addToRequest(IsUserInRoleConfigurator.KEY, "DIRECTOR");
		addClientConfigurator(configurator);
		addAuthorisation();
		invoke("auth/isuserinrole", "anything", "anything");
		configurator.assertBeforeRequestHasBeenCalled();
		configurator.assertAfterResponseHasBeenCalled();

		// check the user is in staff role
		configurator = new ClientConfigurator();
		configurator.addToResponse(IsUserInRoleConfigurator.KEY, String.valueOf(true));
		configurator.addToRequest(IsUserInRoleConfigurator.KEY, "staff");
		addAuthorisation();
		addClientConfigurator(configurator);
		invoke("auth/isuserinrole", "anything", "anything");
		configurator.assertAfterResponseHasBeenCalled();
		configurator.assertBeforeRequestHasBeenCalled();
	}

	/*
	 * @testName: isNotAuthenticatedTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-8.1-1; WebSocket:SPEC:WSC-8.3-1;
	 * 
	 * @test_Strategy: the websocket implementation must return a 401 (Unauthorized)
	 * response to the opening handshake request and may not initiate a websocket
	 * connection A transport guarantee of NONE
	 * 
	 * 401 is not possible to check, but the websocket connection we can check
	 */
	@Test
	public void isNotAuthenticatedTest() throws Exception {
		boolean connection = false;
		try {
			logExceptionOnInvocation(false);
			invoke("auth/isuserinrole", "staff", "staff");
			connection = true;
		} catch (Exception f) {
			logMsg("The connection was not initiated as expected:", getCauseMessage(f));
		}
		assertFalse(connection, "The connection is initiated and 401 response was not returned");
	}

	/*
	 * @testName: authorizationIsNotUsedForUrlTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-8.3-1; WebSocket:SPEC:WSC-8.2-1;
	 * 
	 * @test_Strategy: The <url-pattern> used in the security constraint must be
	 * used by the container to match the request URI of the opening handshake of
	 * the websocket A transport guarantee of NONE
	 */
	@Test
	public void authorizationIsNotUsedForUrlTest() throws Exception {
		invoke("unauth/echo", "echo", "echo");
	}

	/*
	 * @testName: authorizationIsNotAppliedForPOSTTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-8.2-2; WebSocket:SPEC:WSC-8.3-1;
	 * 
	 * @test_Strategy: The implementation must interpret any http-method other than
	 * GET (or the default, missing) as not applying to the websocket. A transport
	 * guarantee of NONE
	 */
	@Test
	public void authorizationIsNotAppliedForPOSTTest() throws Exception {
		invoke("post/echo", "echo", "echo");
	}

	// /////////////////////////////////////////////////////////////////////
	void addAuthorisation() {
		setProperty(Property.BASIC_AUTH_USER, user);
		setProperty(Property.BASIC_AUTH_PASSWD, password);
	}
}
