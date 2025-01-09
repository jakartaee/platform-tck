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

package com.sun.ts.tests.websocket.platform.jakarta.websocket.server.handshakerequest.authenticatedssl;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
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
 *                     securedWebServicePort;
 *                     ws_wait;
 *                     user;
 *                     password;
 */
@ExtendWith(ArquillianExtension.class)
public class WSCClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = -5851958128006729743L;

	@Deployment(testable = false, name = "https")
    @TargetsContainer("https")
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"wsc_platform_jakarta_websocket_handshakeresponse_ssl_web.war");
		archive.addPackages(false, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.platform.jakarta.websocket.server.handshakerequest.authenticatedssl");
		archive.addClasses(IOUtil.class);
		archive.addAsWebInfResource(WSCClientIT.class.getPackage(), "web.xml", "web.xml");
		return archive;
	};
	
	@ArquillianResource 
	@OperateOnDeployment("https") 
	protected URL urlHttps; 

	String user;

	String password;

	String port;

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_platform_jakarta_websocket_handshakeresponse_ssl_web");
		setRequestProtocol("wss");
	}

	@Override
	@BeforeEach
	public void setup() throws Exception {
		super.setup();
		user = assertProperty(new Properties(), "user");
		password = assertProperty(new Properties(), "password");
	
		String secPortNum = System.getProperty("securedWebServicePort");
		String portNum = isNullOrEmpty(secPortNum) ? Integer.toString(urlHttps.getPort()) : secPortNum ;
		_port = Integer.parseInt(portNum);
	}

	/*
	 * @testName: getUserPrincipalTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:179; WebSocket:JAVADOC:77;
	 * WebSocket:JAVADOC:15; WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-8.3-2;
	 * 
	 * @test_Strategy: HandshakeRequest.getUserPrincipal
	 * HandshakeResponse.getHeaders ClientEndpointConfig.Configurator.afterResponse
	 * ServerEndpointConfig.Configurator.modifyHandshake A transport guarantee of
	 * CONFIDENTIAL
	 * 
	 * Return the authenticated user
	 */
	@Test
	public void getUserPrincipalTest() throws Exception {
		ClientConfigurator configurator = new ClientConfigurator();
		configurator.addToResponse(GetUserPrincipalConfigurator.KEY, String.valueOf(user));
		addClientConfigurator(configurator);
		addAuthorisation();
		invoke("ssl/getuserprincipal", "anything", "anything");
		configurator.assertAfterResponseHasBeenCalled();
	}

	/*
	 * @testName: isUserInRoleTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:180; WebSocket:JAVADOC:77;
	 * WebSocket:JAVADOC:15; WebSocket:JAVADOC:210; WebSocket:SPEC:WSC-8.3-2;
	 * 
	 * @test_Strategy: HandshakeRequest.isUserInRole HandshakeResponse.getHeaders
	 * ClientEndpointConfig.Configurator.afterResponse
	 * ServerEndpointConfig.Configurator.modifyHandshake A transport guarantee of
	 * CONFIDENTIAL
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
		invoke("ssl/isuserinrole", "anything", "anything");
		configurator.assertBeforeRequestHasBeenCalled();
		configurator.assertAfterResponseHasBeenCalled();

		// check the user is in staff role
		configurator = new ClientConfigurator();
		configurator.addToResponse(IsUserInRoleConfigurator.KEY, String.valueOf(true));
		configurator.addToRequest(IsUserInRoleConfigurator.KEY, "staff");
		addAuthorisation();
		addClientConfigurator(configurator);
		invoke("ssl/isuserinrole", "anything", "anything");
		configurator.assertAfterResponseHasBeenCalled();
		configurator.assertBeforeRequestHasBeenCalled();
	}

	// /////////////////////////////////////////////////////////////////////
	void addAuthorisation() {
		setProperty(Property.BASIC_AUTH_USER, user);
		setProperty(Property.BASIC_AUTH_PASSWD, password);
	}
}
