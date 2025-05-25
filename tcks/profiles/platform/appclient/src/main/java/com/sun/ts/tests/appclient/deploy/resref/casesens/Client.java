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
 * @(#)Client.java	1.10 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.resref.casesens;

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
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.assembly.util.shared.resref.casesens.TestCode;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")
public class Client extends EETest {

	private Properties props = null;

	private TSNamingContext nctx = null;

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient_dep_resref_casesens")
	public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "appclient_dep_resref_casesens_client.jar");
		ejbClient.addClasses(Client.class, EETest.class, TestCode.class);
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");

		// The appclient-client descriptor
		URL appClientUrl = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/resref/casesens/appclient_dep_resref_casesens_client.xml");
		if (appClientUrl != null) {
			ejbClient.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/resref/casesens/appclient_dep_resref_casesens_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			ejbClient.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.resref.casesens.Client" + "\n"),
				"MANIFEST.MF");

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_resref_casesens.ear");
		ear.addAsModule(ejbClient);
		return ear;
	};

	/**
	 * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
	 *                     webServerHost; webServerPort; mailuser1;
	 */
	public void setup(String[] args, Properties props) throws Exception {
		this.props = props;

		try {
			nctx = new TSNamingContext();
		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/**
	 * @testName: testCaseSensitivity
	 *
	 * @assertion_ids: JavaEE:SPEC:279
	 *
	 * @test_Strategy: Deploy an application client declaring two resource
	 *                 references whose names differ only by case and are assigned
	 *                 to two distinct factory types: a
	 *                 jakarta.jms.QueueConnectionFactory and a
	 *                 jakarta.jms.TopicConnectionFactory.
	 *
	 *                 Check that the application client can lookup the two
	 *                 factories, cast them to their respective Java types, and
	 *                 create a connection (corresponding to the factory type). This
	 *                 validates that the resource references were resolved
	 *                 correctly.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testCaseSensitivity() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testCaseSensitivity(nctx);
			if (!pass) {
				throw new Exception("casesens res-ref test failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			TestUtil.logErr("[Client] Caught exception: " + e);
			throw new Exception("casesens res-ref test failed!", e);
		}
	}

	public void cleanup() {
		logTrace("[Client] cleanup()");
	}

}
