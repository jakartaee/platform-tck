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
 * @(#)scalarClient1.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar1;

import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The scalarClient1 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class scalarClient1AppClient extends scalarClient1 {
	private static final String testName = "jdbc.ee.escapeSyntax";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "scalar1_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(scalarClient1.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = scalarClient1AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/escapeSyntax/scalar1/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = scalarClient1AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}
		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, scalarClient1AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "scalar1_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		scalarClient1AppClient theTests = new scalarClient1AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testConcat
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call executeQuery(String) method.
	 * The query contains the concat scalar function. It should return the
	 * concatenated string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testConcat() throws Exception {
		super.testConcat();
	}

	/*
	 * @testName: testAscii
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a statement object and call executeQuery method. The
	 * query contains the ascii function call. It should return an integer.
	 *
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testAscii() throws Exception {
		super.testAscii();
	}

	/*
	 * @testName: testInsert
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function insert. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testInsert() throws Exception {
		super.testInsert();
	}

	/*
	 * @testName: testLcase
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statment object and call the method executeQuery. The
	 * query contains a call to the function lcase. It should return a string.
	 *
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testLcase() throws Exception {
		super.testLcase();
	}

	/*
	 * @testName: testLeft
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function left. It should return a string.
	 *
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testLeft() throws Exception {
		super.testLeft();
	}

	/*
	 * @testName: testLength
	 *
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function length. It should return a number.
	 *
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testLength() throws Exception {
		super.testLength();
	}

	/*
	 * @testName: testLocate01
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function locate. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testLocate01() throws Exception {
		super.testLocate01();
	}

	/*
	 * @testName: testLtrim
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function ltrim. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testLtrim() throws Exception {
		super.testLtrim();
	}

	/*
	 * @testName: testRepeat
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function repeat. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testRepeat() throws Exception {
		super.testRepeat();
	}

	/*
	 * @testName: testRight
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function right. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testRight() throws Exception {
		super.testRight();
	}

	/*
	 * @testName: testRtrim
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function rtrim. It should return a string.
	 *
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testRtrim() throws Exception {
		super.testRtrim();
	}

	/*
	 * @testName: testSoundex
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function soundex. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSoundex() throws Exception {
		super.testSoundex();
	}

	/*
	 * @testName: testSpace
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function space. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSpace() throws Exception {
		super.testSpace();
	}

	/*
	 * @testName: testSubstring
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function substring. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSubstring() throws Exception {
		super.testSubstring();
	}

	/*
	 * @testName: testUcase
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function ucase. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testUcase() throws Exception {
		super.testUcase();
	}

	/*
	 * @testName: testChar
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function char. It should return a character.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testChar() throws Exception {
		super.testChar();
	}

	/*
	 * @testName: testReplace
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function replace. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testReplace() throws Exception {
		super.testReplace();
	}

	/*
	 * @testName: testUser
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function user. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testUser() throws Exception {
		super.testUser();
	}

	/*
	 * @testName: testIfNull
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function ifnull.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testIfNull() throws Exception {
		super.testIfNull();
	}

	/*
	 * @testName: testSin
	 * 
	 * @assertion_ids: JDBC:SPEC:4; JDBC:SPEC:27;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function sin. It should return a numerical
	 * value.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSin() throws Exception {
		super.testSin();
	}

}
