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
import java.io.InputStream;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
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
public class scalarClient3JSP extends scalarClient3 {
	private static final String testName = "jdbc.ee.escapeSyntax";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "scalar3_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		archive.addClasses(scalarClient3.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = scalarClient3JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = scalarClient3JSP.class.getResource("scalar3_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, scalarClient3JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(scalarClient3JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		scalarClient3JSP theTests = new scalarClient3JSP();
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
	public void testLocate02() throws Exception {
		super.testLocate02();
	}
}
