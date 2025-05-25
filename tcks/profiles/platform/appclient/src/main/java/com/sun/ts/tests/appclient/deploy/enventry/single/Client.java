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
 * @(#)Client.java	1.15 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.enventry.single;

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
import com.sun.ts.tests.assembly.util.shared.enventry.single.TestCode;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@ExtendWith(ArquillianExtension.class)
@Tag("platform")
@Tag("tck-appclient")
public class Client extends EETest {

	private TSNamingContext nctx = null;

	private Properties props = null;

	public static void main(String[] args) {
		Client theTests = new Client();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient_dep_enventry_single")
	public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "appclient_dep_enventry_single_client.jar");
		ejbClient.addPackages(true, Client.class.getPackage());
		ejbClient.addClasses(Client.class, EETest.class, TestCode.class);
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");

		// The appclient-client descriptor
		URL appClientUrl = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/enventry/single/appclient_dep_enventry_single_client.xml");
		if (appClientUrl != null) {
			ejbClient.addAsManifestResource(appClientUrl, "application-client.xml");
		}

		ejbClient.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.enventry.single.Client" + "\n"),
				"MANIFEST.MF");

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_enventry_single.ear");
		ear.addAsModule(ejbClient);
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
			throw new Exception("Setup failed: ", e);
		}
	}

	/**
	 * @testName: testString
	 *
	 * @assertion_ids: JavaEE:SPEC:103.1
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares a
	 *                 String environment entry. Lookup this entry and check that
	 *                 its runtime value match the DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testString() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testStringEntry(nctx);
			if (!pass) {
				throw new Exception("String env entry test failed!");
			}
		} catch (Exception e) {
			throw new Exception("String env entry test failed: " + e, e);
		}
	}

	/**
	 * @testName: testBoolean
	 *
	 * @assertion_ids: JavaEE:SPEC:103.7
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares a
	 *                 Boolean environment entry. Lookup this entry and check that
	 *                 its runtime value match the DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testBoolean() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testBooleanEntry(nctx);
			if (!pass) {
				throw new Exception("Boolean env entry test failed!");
			}
		} catch (Exception e) {
			throw new Exception("Boolean env entry test failed: " + e, e);
		}
	}

	/**
	 * @testName: testByte
	 *
	 * @assertion_ids: JavaEE:SPEC:103.3
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares a
	 *                 Byte environment entry. Lookup this entry and check that its
	 *                 runtime value match the DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testByte() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testByteEntry(nctx);
			if (!pass) {
				throw new Exception("Byte env entry test failed!");
			}
		} catch (Exception e) {
			throw new Exception("Byte env entry test failed: " + e, e);
		}
	}

	/**
	 * @testName: testShort
	 *
	 * @assertion_ids: JavaEE:SPEC:103.4
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares a
	 *                 Short environment entry. Lookup this entry and check that its
	 *                 runtime value match the DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testShort() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testShortEntry(nctx);
			if (!pass) {
				throw new Exception("Short env entry test failed!");
			}
		} catch (Exception e) {
			throw new Exception("Short env entry test failed: " + e, e);
		}
	}

	/**
	 * @testName: testInteger
	 *
	 * @assertion_ids: JavaEE:SPEC:103.5
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares a
	 *                 Integer environment entry. Lookup this entry and check that
	 *                 its runtime value match the DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testInteger() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testIntegerEntry(nctx);
			if (!pass) {
				throw new Exception("Integer env entry test failed!");
			}
		} catch (Exception e) {
			throw new Exception("Integer env entry test failed: " + e, e);
		}
	}

	/**
	 * @testName: testLong
	 *
	 * @assertion_ids: JavaEE:SPEC:103.8
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares a
	 *                 Long environment entry. Lookup this entry and check that its
	 *                 runtime value match the DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testLong() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testLongEntry(nctx);
			if (!pass) {
				throw new Exception("Long env entry test failed!");
			}
		} catch (Exception e) {
			throw new Exception("Long env entry test failed: " + e, e);
		}
	}

	/**
	 * @testName: testFloat
	 *
	 * @assertion_ids: JavaEE:SPEC:103.9
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares a
	 *                 Float environment entry. Lookup this entry and check that its
	 *                 runtime value match the DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testFloat() throws Exception {
		boolean pass;

		try {
			pass = TestCode.testFloatEntry(nctx);
			if (!pass) {
				throw new Exception("Float env entry test failed!");
			}
		} catch (Exception e) {
			throw new Exception("Float env entry test failed: " + e, e);
		}
	}

	/**
	 * @testName: testDouble
	 *
	 * @assertion_ids: JavaEE:SPEC:103.6
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares a
	 *                 Double environment entry. Lookup this entry and check that
	 *                 its runtime value match the DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testDouble() throws Exception {
		boolean pass;
		Double value;

		try {
			pass = TestCode.testDoubleEntry(nctx);
			if (!pass) {
				throw new Exception("Double env entry test failed!");
			}
		} catch (Exception e) {
			throw new Exception("Double env entry test failed: ", e);
		}
	}

	/**
	 * @testName: testAll
	 *
	 * @assertion_ids: JavaEE:SPEC:103
	 *
	 * @test_Strategy: Deploy and create an application client whose DD declares an
	 *                 environment entry of each type. Lookup these entries and
	 *                 check that their runtime value match their DD value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testAll() throws Exception {
		try {
			logTrace("[Client] testAll() : starting...");
			testString();
			testBoolean();
			testByte();
			testShort();
			testInteger();
			testLong();
			testFloat();
			testDouble();
			logTrace("[Client] testAll() : done!");
		} catch (Exception e) {
			throw new Exception("env entry test [all types] failed: ", e);
		}
	}

	public void cleanup() throws Exception {
		logMsg("[Client] cleanup()");
	}
}
