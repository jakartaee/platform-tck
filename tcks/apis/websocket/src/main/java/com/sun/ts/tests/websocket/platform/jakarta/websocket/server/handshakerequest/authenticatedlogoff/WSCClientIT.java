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

package com.sun.ts.tests.websocket.platform.jakarta.websocket.server.handshakerequest.authenticatedlogoff;

import java.io.IOException;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;
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
@Tag("platform")
@Tag("web")
public class WSCClientIT extends WebSocketCommonClient {

	private static final long serialVersionUID = -7084128651642590169L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class,
				"wsc_platform_jakarta_websocket_handshakeresponse_authenticated_logoff_web.war");
		archive.addPackages(false, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.platform.jakarta.websocket.server.handshakerequest.authenticatedlogoff");
		archive.addClasses(IOUtil.class);
		archive.addAsWebInfResource(WSCClientIT.class.getPackage(), "web.xml", "web.xml");
		return archive;
	};

	String user;

	String password;

	int reason = 0;

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_platform_jakarta_websocket_handshakeresponse_authenticated_logoff_web");
	}

	@Override
	@BeforeEach
	public void setup() throws Exception {
		user = assertProperty(new Properties(), "user");
		password = assertProperty(new Properties(), "password");
		super.setup();
	}

	/*
	 * @testName: connectionHasBeenClosedWithStatus1008AfterInvalidationTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-7.2-3;
	 * 
	 * @test_Strategy: In the case where a websocket endpoint is a protected
	 * resource in the web application (see Chapter 8), that is to say, requires an
	 * authorized user to access it, then the websocket implementation must ensure
	 * that the websocket endpoint does not remain connected to its peer after the
	 * underlying implementation has decided the authenticated identity is no longer
	 * valid.
	 * 
	 * In this situation, the websocket implementation must immediately close the
	 * connection using the websocket close status code 1008.
	 */
	@Test
	public void connectionHasBeenClosedWithStatus1008AfterInvalidationTest() throws Exception {
		connectionHasBeenClosedWithStatus1008After(0);
	}

	// /////////////////////////////////////////////////////////////////////
	void addAuthorisation() {
		setProperty(Property.BASIC_AUTH_USER, user);
		setProperty(Property.BASIC_AUTH_PASSWD, password);
	}

	void connectionHasBeenClosedWithStatus1008After(int opId) throws Exception {
		boolean closed = false;
		String[] messages = WSCCloseHttpSessionServer.MESSAGES;
		logExceptionOnInvocation(false);
		// set properties
		addAuthorisation();
		// do not cleanup
		try {
			invoke("closehttpsession", messages[opId], messages[opId], false);
		} catch (Exception e) {
			// Could have been closed before response returned or it
			// could have taken some time, then the validation fails
		}
		// setMaxIdleTimeout is checked only twice a minute
		try {
			TestUtil.sleepSec(5); // give time to close connection
			invokeAgain("ok", "ok", true); // check for closed
		} catch (Exception e) {
			closed = true;
			logTrace("Connection has been closed as expected:", getCauseMessage(e));
		}
		assertTrue(closed, "The connection has not been immediatelly closed");
		// ask for close code
		addAuthorisation();
		invoke("closehttpsession", messages[2], "1008");
		logMsg("After invalidation of HTTP session, connection has been closed with expected status 1008");
	}
}
