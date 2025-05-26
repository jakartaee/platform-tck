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
import org.jboss.shrinkwrap.api.spec.WebArchive;
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

@Tag("tck-javatest")
@Tag("web")
public class scalarClient3Servlet extends scalarClient3 {
	private static final String testName = "jdbc.ee.escapeSyntax";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "scalar3_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(scalarClient3.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = scalarClient3Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
// The sun servlet descriptor
		URL sunServletUrl = scalarClient3Servlet.class.getResource("scalar3_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
// Call the archive processor
		archiveProcessor.processWebArchive(archive, scalarClient3Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		scalarClient3Servlet theTests = new scalarClient3Servlet();
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
	public void testLocate02() throws Exception {
		super.testLocate02();
	}
}
