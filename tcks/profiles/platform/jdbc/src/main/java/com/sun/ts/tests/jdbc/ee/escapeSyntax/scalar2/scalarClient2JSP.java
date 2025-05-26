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
 * The scalarClient2 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class scalarClient2JSP extends scalarClient2 {
	private static final String testName = "jdbc.ee.escapeSyntax";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "scalar2_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(scalarClient2.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = scalarClient2JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = scalarClient2JSP.class.getResource("scalar2_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, scalarClient2JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(scalarClient2JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		scalarClient2JSP theTests = new scalarClient2JSP();
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
	public void testDayofyear() throws Exception {
		super.testDayofyear();
	}
}
