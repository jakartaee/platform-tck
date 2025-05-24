/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.12 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.enventry.scope;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.shared.enventry.scope.TestCode;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")
public class Client extends EETest {

	/** Env. entry name for JNDI lookup */
	private static final String entryName = "Duende";

	/** Reference value for the env. entry (as specified in DD). */
	private static final String entryRef = "Paco de Lucia";

	private TSNamingContext nctx = null;

	private Properties props = null;

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient_dep_enventry_scope")
	public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient1 = ShrinkWrap.create(JavaArchive.class,
				"appclient_dep_enventry_scope_another_client.jar");
		ejbClient1.addClasses(Client.class, TestCode.class);
		ejbClient1.addPackages(true, "com.sun.ts.lib.harness");

		// The appclient-client descriptor
		URL appClientUrl = Client.class.getResource(
				"com/sun/ts/tests/appclient/deploy/enventry/scope/appclient_dep_enventry_scope_another_client.xml");
		if (appClientUrl != null) {
			ejbClient1.addAsManifestResource(appClientUrl, "application-client.xml");
		}

		ejbClient1.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.enventry.scope.Client" + "\n"),
				"MANIFEST.MF");

		JavaArchive ejbClient2 = ShrinkWrap.create(JavaArchive.class, "appclient_dep_enventry_scope_client.jar");
		ejbClient2.addClasses(Client.class, TestCode.class);
		ejbClient2.addPackages(true, "com.sun.ts.lib.harness");

		URL resURL = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/enventry/scope/appclient_dep_enventry_scope_client.xml");

		if (resURL != null) {
			ejbClient2.addAsManifestResource(resURL, "application-client.xml");
		}

		ejbClient2.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.enventry.scope.Client" + "\n"),
				"MANIFEST.MF");

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_enventry_scope.ear");
		ear.addAsModule(ejbClient1);
		ear.addAsModule(ejbClient2);
		return ear;
	};

	/*
	 * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
	 *
	 */
	public void setup(String[] args, Properties props) throws Exception {
		this.props = props;

		try {
			nctx = new TSNamingContext();
			logMsg("[Client] Setup succeed (got naming context).");
		} catch (Exception e) {
			throw new Exception("[Client] Setup failed:", e);
		}
	}

	/**
	 * @testName: testScope
	 *
	 * @assertion_ids: JavaEE:SPEC:102
	 *
	 * @test_Strategy: We package in the same .ear file 2 application clients
	 *                 (_client and _another_client). Both use the same
	 *                 env-entry-name to declare two distinct String environment
	 *                 entry values.
	 *
	 *                 We check that: - We can deploy the application. - One of the
	 *                 application clients (_client) can be run and can lookup its
	 *                 String environment entry. - The runtime value of this entry
	 *                 correspond to the one declared in the Deployment Descriptor.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testScope() throws Exception {
		boolean pass;

		try {
			pass = TestCode.checkEntry(nctx, entryName, entryRef);
			if (!pass) {
				throw new Exception("Env entry scope test failed!");
			}
		} catch (Exception e) {
			throw new Exception("Env entry scope test failed: " + e, e);
		}
	}

	public void cleanup() throws Exception {
		logMsg("[Client] cleanup()");
	}
}
