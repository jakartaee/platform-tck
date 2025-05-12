/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * $Id$
 */

package com.sun.ts.tests.appclient.deploy.compat9_10;

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

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")
public class Client extends EETest {

	private static final String prefix = "java:comp/env/ejb/";

	/** Bean lookup */
	private static final String beanLookup = prefix + "TestBean";

	private TSNamingContext nctx = null;

	private Properties props = null;

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient_dep_ejblink_casesens", testable = true)
	public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "appclient_dep_compat9_10_client.jar");
		ejbClient.addPackages(true, Client.class.getPackage());
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");

		// The appclient-client descriptor
		URL appClientUrl = Client.class
				.getResource("/com/sun/ts/tests/appclient/deploy/compat9_10/META-INF/application-client.xml");
		if (appClientUrl != null) {
			ejbClient.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/compat9_10/appclient_dep_compat9_10_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			ejbClient.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.compat9_10.Client" + "\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "appclient_dep_compat9_10_ejb.jar");
		ejb.addClasses(TestBean.class, TestBeanEJB.class);
		ejb.addPackages(true, "com.sun.ts.tests.common.ejb.wrappers");

		URL resURL = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/compat9_10/appclient_dep_compat9_10_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = Client.class.getResource("/com/sun/ts/tests/appclient/deploy/compat9_10/META-INF/ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}


		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_ejblink_casesens.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);

		return ear;
	};

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/**
	 * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
	 */
	public void setup(String[] args, Properties props) throws Exception {
		try {
			this.props = props;
			nctx = new TSNamingContext();
			logMsg("[Client] Setup succeed (got naming context).");
		} catch (Exception e) {
			throw new Exception("Setup failed:", e);
		}
	}

	/**
	 * @testName: test910DD
	 *
	 * @assertion_ids: JavaEE:SPEC:283; JavaEE:SPEC:284; JavaEE:SPEC:10109;
	 *                 JavaEE:SPEC:10110; JavaEE:SPEC:10111
	 *
	 * @test_Strategy: Package an ejb-jar file using a Jakarta EE 10.0 DD
	 *
	 *                 Package an .ear file (Jakarta EE 10.0 DD's) including this
	 *                 ejb-jar and an application client (Jakarta EE 9.0 DD's). This
	 *                 application client references a bean in this ejb-jar module.
	 *
	 *                 Deploy the .ear file.
	 *
	 *                 Run the client and check we can call a business method on the
	 *                 referenced bean at runtime.
	 */
	@Test
	@TargetVehicle("appclient")
	public void test910DD() throws Exception {
		TestBean bean = null;
		String beanName;
		boolean pass = false;

		try {
			TestUtil.logTrace("[Client] Looking up '" + beanLookup + "'...");
			bean = (TestBean) nctx.lookup(beanLookup, TestBean.class);
			bean.initLogging(props);
			pass = bean.ping();

			if (!pass) {
				throw new Exception("appclient compat9_10 test failed!");
			}
		} catch (Exception e) {
			TestUtil.logErr("appclient compat9_10 test failed: " + e);
			throw new Exception("appclient compat9_10 test failed: ", e);
		}
	}

	public void cleanup() throws Exception {
		logMsg("[Client] cleanup()");
	}

}
