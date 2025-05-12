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
 * @(#)Client.java	1.23 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.resref.single;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.assembly.util.shared.resref.single.appclient.TestCode;

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
	@Deployment(name = "appclient_dep_resref_single")
	public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {

		EnterpriseArchive ear = null;

		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "appclient_dep_resref_single_client.jar");
		ejbClient.addPackages(true, Client.class.getPackage());
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(TestCode.class, Client.class);

		// The appclient-client descriptor
		URL appClientUrl = Client.class
				.getResource("/com/sun/ts/tests/appclient/deploy/resref/single/appclient_dep_resref_single_client.xml");
		if (appClientUrl != null) {
			ejbClient.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/resref/single/appclient_dep_resref_single_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			ejbClient.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.resref.single.Client" + "\n"),
				"MANIFEST.MF");
		WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "appclient_dep_resref_single_jsp_web.war");
		InputStream testJSP = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/appclient/deploy/resref/single/contentRoot/test.jsp");
		webArchive.add(new ByteArrayAsset(testJSP), "test.jsp");

		// The jsp descriptor
		URL jspUrl = Client.class.getResource("appclient_dep_resref_single_jsp_web.xml");
		if (jspUrl != null) {
			webArchive.addAsWebInfResource(jspUrl, "web.xml");
		}

		ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_resref_single.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(webArchive);
		return ear;
	};

	/**
	 * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
	 *                     webServerHost; webServerPort;
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
	 * @testName: testDatasource
	 *
	 * @assertion_ids: JavaEE:SPEC:10125
	 *
	 * @test_Strategy: Package an application client declaring a resource reference
	 *                 for a javax.sql.Datasource.
	 * 
	 *                 Check that: - We can deploy the application. - We can lookup
	 *                 the datasource. - We can use it to open a DB connection.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testDatasource() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testDatasource(nctx);
			if (!pass) {
				throw new Exception("Datasource res-ref test failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			TestUtil.logErr("[Client] Caught exception: " + e);
			throw new Exception("Datasource res-ref test failed!", e);
		}
	}

	/**
	 * @testName: testURL
	 *
	 * @assertion_ids: JavaEE:SPEC:10125
	 *
	 * @test_Strategy: Package an application client declaring a resource reference
	 *                 for a java.net.URL.
	 * 
	 *                 Check that: - We can deploy the application. - We can lookup
	 *                 the URL. - We can use this URL factory to open a connection
	 *                 to a HTML page bundled in the application.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testURL() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testURL(nctx);
			if (!pass) {
				throw new Exception("URL res-ref test failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			TestUtil.logErr("[Client] Caught exception: " + e);
			throw new Exception("URL res-ref test failed!", e);
		}
	}

	/**
	 * @testName: testQueue
	 *
	 * @assertion_ids: JavaEE:SPEC:10125
	 *
	 * @test_Strategy: Package an application client declaring a resource reference
	 *                 for a jakarta.jms.QueueConnectionFactory.
	 * 
	 *                 Check that: - We can deploy the application. - We can lookup
	 *                 the JMS Queue Connection Factory.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testQueue() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testQueue(nctx);
			if (!pass) {
				throw new Exception("Queue res-ref test failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			TestUtil.logErr("[Client] Caught exception: " + e);
			throw new Exception("Queue res-ref test failed!", e);
		}
	}

	/**
	 * @testName: testTopic
	 *
	 * @assertion_ids: JavaEE:SPEC:10125
	 *
	 * @test_Strategy: Package an application client declaring a resource reference
	 *                 for a jakarta.jms.TopicConnectionFactory.
	 * 
	 *                 Check that: - We can deploy the application. - We can lookup
	 *                 the JMS Topic Connection Factory.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTopic() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testTopic(nctx);
			if (!pass) {
				throw new Exception("Topic res-ref test failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			TestUtil.logErr("[Client] Caught exception: " + e);
			throw new Exception("Topic res-ref test failed!", e);
		}
	}

	/**
	 * @testName: testAll
	 *
	 * @assertion_ids: JavaEE:SPEC:10125
	 *
	 * @test_Strategy: Package an application client declaring a resource reference
	 *                 for all the standard resource manager connection factory
	 *                 types.
	 * 
	 *                 Check that: - We can deploy the application. - We can lookup
	 *                 all the declared resource factories.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testAll() throws Exception {
		try {
			testDatasource();
			testURL();
			testQueue();
			testTopic();
		} catch (Exception e) {
			e.printStackTrace();
			TestUtil.logErr("[Client] Caught exception: " + e);
			throw new Exception("All res-ref test failed!", e);
		}
	}

	public void cleanup() {
		logTrace("[Client] cleanup()");
	}

}
