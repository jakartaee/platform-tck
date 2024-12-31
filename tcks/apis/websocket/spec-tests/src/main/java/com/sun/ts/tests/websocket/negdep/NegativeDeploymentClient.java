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

package com.sun.ts.tests.websocket.negdep;

import org.junit.jupiter.api.BeforeEach;

import com.sun.ts.tests.websocket.common.client.WebSocketCommonClient;

/**
 * Can turn off printing of the exception stacktrace on screen with ts.jte
 * property log.exception=false. This feature works just with negative
 * deployment tests
 * 
 * @since 1.11
 */
public class NegativeDeploymentClient extends WebSocketCommonClient {

	private static final long serialVersionUID = 111;

	protected String lib_name;

	@Override
	@BeforeEach
	public void setup() throws Exception {
		super.setup();
		lib_name = System.getProperty("lib.name");
		assertFalse(isNullOrEmpty(lib_name), "'lib.name' was not set in the properties.");
	}

	protected void throwWhenCts() throws Exception {
		if (lib_name.equalsIgnoreCase("cts"))
			throwDeploymentDidNotFail();
	}

	protected void throwValidEndpointMustBeRemoved() throws Exception {
		String msg = "Test Failed - a deployment error raised during the deployment process must halt the deployment of the application, any well formed endpoints deployed prior to the error being raised must be removed from service and no more websocket endpoints from that application may be deployed by the container, even if they are valid";
		throw new Exception(msg);
	}

	protected void throwDeploymentDidNotFail() throws Exception {
		throw new Exception("Test Failed - expected deployment to fail, but it succeeded!");
	}
}
