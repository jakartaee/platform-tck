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
 * @(#)scalarClient2.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar2;

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
 * The scalarClient2 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class scalarClient2EJB extends scalarClient2 {
	private static final String testName = "jdbc.ee.escapeSyntax";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "scalar2_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(scalarClient2.class, ServiceEETest.class, EETest.class);

		URL resURL = scalarClient2EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = scalarClient2EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/escapeSyntax/scalar2/scalar2_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "scalar2_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(scalarClient2.class, ServiceEETest.class, EETest.class);

		resURL = scalarClient2EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/escapeSyntax/scalar2/scalar2_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = scalarClient2EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/escapeSyntax/scalar2/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "scalar2_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		scalarClient2EJB theTests = new scalarClient2EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testAbs
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function abs. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testAbs() throws Exception {
		super.testAbs();
	}

	/*
	 * @testName: testPower
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function power. It should return a numeric
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testPower() throws Exception {
		super.testPower();
	}

	/*
	 * @testName: testRound
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function round. It should return a numeric
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRound() throws Exception {
		super.testRound();
	}

	/*
	 * @testName: testSign
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function sign. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSign() throws Exception {
		super.testSign();
	}

	/*
	 * @testName: testSqrt
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function sqrt. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSqrt() throws Exception {
		super.testSqrt();
	}

	/*
	 * @testName: testTruncate
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function truncate. It should return a numeric
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testTruncate() throws Exception {
		super.testTruncate();
	}

	/*
	 * @testName: testMod
	 * 
	 * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function mod. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testMod() throws Exception {
		super.testMod();
	}

	/*
	 * @testName: testFloor
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function floor. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testFloor() throws Exception {
		super.testFloor();
	}

	/*
	 * @testName: testCeiling
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function ceiling. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testCeiling() throws Exception {
		super.testCeiling();
	}

	/*
	 * @testName: testLog10
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function log10. It should return a numeric
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testLog10() throws Exception {
		super.testLog10();
	}

	/*
	 * @testName: testLog
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function log. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testLog() throws Exception {
		super.testLog();
	}

	/*
	 * @testName: testExp
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function exp. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExp() throws Exception {
		super.testExp();
	}

	/*
	 * @testName: testCos
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function cos. It should return a numeric value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testCos() throws Exception {
		super.testCos();
	}

	/*
	 * @testName: testTan
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function tan. It should return a numeric value.
	 * 
	 */
	@Test
	@TargetVehicle("ejb")
	public void testTan() throws Exception {
		super.testTan();
	}

	/*
	 * @testName: testCot
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function cot. It should return a numeric value.
	 *
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testCot() throws Exception {
		super.testCot();
	}

	/*
	 * @testName: testCurdate
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function curdate. It should return a date value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testCurdate() throws Exception {
		super.testCurdate();
	}

	/*
	 * @testName: testDayname
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function dayname. It should return a character
	 * string.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testDayname() throws Exception {
		super.testDayname();
	}

	/*
	 * @testName: testDayofmonth
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function dayofmonth. It should return an
	 * integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testDayofmonth() throws Exception {
		super.testDayofmonth();
	}

	/*
	 * @testName: testDayofweek
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function dayofweek. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testDayofweek() throws Exception {
		super.testDayofweek();
	}

	/*
	 * @testName: testDayofyear
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function dayofyear. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testDayofyear() throws Exception {
		super.testDayofyear();
	}
}
