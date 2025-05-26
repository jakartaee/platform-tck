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
 * @(#)scalarClient3.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar3;

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
 * The scalarClient3 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class scalarClient3EJB extends scalarClient3 {
	private static final String testName = "jdbc.ee.escapeSyntax";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "scalar3_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(scalarClient3.class, ServiceEETest.class, EETest.class);

		URL resURL = scalarClient3EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = scalarClient3EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/escapeSyntax/scalar3/scalar3_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "scalar3_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(scalarClient3.class, ServiceEETest.class, EETest.class);

		resURL = scalarClient3EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/escapeSyntax/scalar3/scalar3_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = scalarClient3EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/escapeSyntax/scalar3/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "scalar3_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		scalarClient3EJB theTests = new scalarClient3EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testWeek
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function week. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testWeek() throws Exception {
		super.testWeek();
	}

	/*
	 * @testName: testMonth
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function month. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testMonth() throws Exception {
		super.testMonth();
	}

	/*
	 * @testName: testYear
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function year. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testYear() throws Exception {
		super.testYear();
	}

	/*
	 * @testName: testMonthname
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function monthname. It should return a character
	 * string.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testMonthname() throws Exception {
		super.testMonthname();
	}

	/*
	 * @testName: testQuarter
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function quarter. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testQuarter() throws Exception {
		super.testQuarter();
	}

	/*
	 * @testName: testNow
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function now. It should return a timestamp
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testNow() throws Exception {
		super.testNow();
	}

	/*
	 * @testName: testHour
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function hour. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testHour() throws Exception {
		super.testHour();
	}

	/*
	 * @testName: testMinute
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function minute. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testMinute() throws Exception {
		super.testMinute();
	}

	/*
	 * @testName: testSecond
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function second. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSecond() throws Exception {
		super.testSecond();
	}

	/*
	 * @testName: testDatabase
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function database. It should return a string.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testDatabase() throws Exception {
		super.testDatabase();
	}

	/*
	 * @testName: testAcos
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function acos. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testAcos() throws Exception {
		super.testAcos();
	}

	/*
	 * @testName: testAsin
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function asin. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testAsin() throws Exception {
		super.testAsin();
	}

	/*
	 * @testName: testAtan
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function atan. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testAtan() throws Exception {
		super.testAtan();
	}

	/*
	 * @testName: testAtan2
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function atan2. It should return a numeric
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testAtan2() throws Exception {
		super.testAtan2();
	}

	/*
	 * @testName: testDegrees
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function degrees. It should return a numeric
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testDegrees() throws Exception {
		super.testDegrees();
	}

	/*
	 * @testName: testRadians
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function radians. It should return a numeric
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRadians() throws Exception {
		super.testRadians();
	}

	/*
	 * @testName: testPi
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function pi. It should return the constant value
	 * of PI.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testPi() throws Exception {
		super.testPi();
	}

	/*
	 * @testName: testRand
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function rand. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRand() throws Exception {
		super.testRand();
	}

	/*
	 * @testName: testDifference
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function difference. It should return an
	 * integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testDifference() throws Exception {
		super.testDifference();
	}

	/*
	 * @testName: testLocate02
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function locate. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testLocate02() throws Exception {
		super.testLocate02();
	}
}
