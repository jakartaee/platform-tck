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

/*
 * $Id:$
 */
package com.sun.ts.tests.websocket.api.jakarta.websocket.deploymentException;

import java.lang.System.Logger;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import jakarta.websocket.DeploymentException;

@Tag("websocket")
@Tag("platform")
@Tag("web")

public class WSClientIT {

	private static final Logger logger = System.getLogger(WSClientIT.class.getName());

	/* Run test */
	/*
	 * @testName: constructorTest
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:49;
	 *
	 * @test_Strategy: Test constructor DeploymentException(String)
	 */
	@Test
	public void constructorTest() throws Exception {
		String reason = "TCK: testing the DeploymentException(String)";

		@SuppressWarnings("unused")
		DeploymentException dex = new DeploymentException(reason);

	}

	/*
	 * @testName: constructorTest1
	 * 
	 * @assertion_ids: WebSocket:JAVADOC:50;
	 *
	 * @test_Strategy: Test constructor DeploymentException(String, Throwable)
	 */
	@Test
	public void constructorTest1() throws Exception {
		String reason = "TCK: testing the DeploymentException(String)";

		@SuppressWarnings("unused")
		DeploymentException dex = new DeploymentException(reason, new Throwable("TCK_Test"));
	}

	public void cleanup() {
	}
}
