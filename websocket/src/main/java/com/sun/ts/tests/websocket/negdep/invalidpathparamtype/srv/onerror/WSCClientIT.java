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

package com.sun.ts.tests.websocket.negdep.invalidpathparamtype.srv.onerror;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.websocket.common.stringbean.StringBean;
import com.sun.ts.tests.websocket.common.stringbean.StringBeanTextDecoder;
import com.sun.ts.tests.websocket.common.util.IOUtil;
import com.sun.ts.tests.websocket.negdep.NegativeDeploymentClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ws_wait;
 *                     lib.name;
 */
/**
 * @since 1.11
 */
@ExtendWith(ArquillianExtension.class)
public class WSCClientIT extends NegativeDeploymentClient {

	private static final long serialVersionUID = 111L;

	@Deployment(testable = false)
	public static WebArchive createDeployment() throws IOException {

		WebArchive archive = ShrinkWrap.create(WebArchive.class, "wsc_negdep_invalidpathparamtype_srv_onerror_web.war");
		archive.addPackages(false, Filters.exclude(WSCClientIT.class),
				"com.sun.ts.tests.websocket.negdep.invalidpathparamtype.srv.onerror");
		archive.addPackages(true, Filters.exclude(NegativeDeploymentClient.class), "com.sun.ts.tests.websocket.negdep");
		archive.addClasses(StringBeanTextDecoder.class, StringBean.class);
		archive.addClasses(IOUtil.class);
		return archive;
	};

	public WSCClientIT() throws Exception {
		setContextRoot("wsc_negdep_invalidpathparamtype_srv_onerror_web");
	}

	/*
	 * @testName: invalidTypeOnErrorUndeployOtherEndpointTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3;WebSocket:SPEC:WSC-4.3-1;
	 * 
	 * @test_Strategy: In both cases, a deployment error raised during the
	 * deployment process must halt the deployment of the application, any well
	 * formed endpoints deployed prior to the error being raised must be removed
	 * from service and no more websocket endpoints from that application may be
	 * deployed by the container, even if they are valid.
	 * 
	 * The allowed types for these parameters are String, any Java primitive type,
	 * or boxed version thereof. Any other type annotated with this annotation is an
	 * error that the implementation must report at deployment time. [WSC-4.3-1]
	 * 
	 * To verify the test fails when the deployment was successful, switch
	 * StringHolder to String as argument of @OnError in
	 * OnErrorStringHolderServerEndpoint
	 */
	@Test
	public void invalidTypeOnErrorUndeployOtherEndpointTest() throws Exception {
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
	 * @testName: invalidTypeOnErrorTest
	 * 
	 * @assertion_ids: WebSocket:SPEC:WSC-5.2.1-3;WebSocket:SPEC:WSC-4.3-1;
	 * 
	 * @test_Strategy: In both cases, a deployment error raised during the
	 * deployment process must halt the deployment of the application, any well
	 * formed endpoints deployed prior to the error being raised must be removed
	 * from service and no more websocket endpoints from that application may be
	 * deployed by the container, even if they are valid.
	 * 
	 * The allowed types for these parameters are String, any Java primitive type,
	 * or boxed version thereof. Any other type annotated with this annotation is an
	 * error that the implementation must report at deployment time. [WSC-4.3-1]
	 * 
	 * To verify the test fails when the deployment was successful, switch
	 * StringHolder to String as argument of @OnError in
	 * OnErrorStringHolderServerEndpoint
	 */
	@Test
	public void invalidTypeOnErrorTest() throws Exception {
		setProperty(Property.CONTENT, "throw");
		setProperty(Property.REQUEST, buildRequest("invalid/pathparam"));
		setProperty(Property.STATUS_CODE, "-1");
		// logExceptionOnInvocation(false);
		try {
			invoke(false);
		} catch (Exception tfe) {
			// DeploymentException
		}
		String response = getResponseAsString();
		if ("throw".equals(response))
			throwValidEndpointMustBeRemoved();
	}
}
