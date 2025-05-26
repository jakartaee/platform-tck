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
 * %W% %E%
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt13;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
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
 * The callStmtClient13 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 */
@Tag("tck-javatest")
@Tag("web")
public class callStmtClient13JSP extends callStmtClient13 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt13";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt13_jsp_vehicle_web.war");
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

		archive.addClasses(callStmtClient13.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = callStmtClient13JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = callStmtClient13JSP.class.getResource("callStmt13_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient13JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(callStmtClient13JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient13JSP theTests = new callStmtClient13JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject101
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Numeric_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Numeric_Tab. Compare the
	 * returned value with the maximum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject101() throws Exception {
		super.testSetObject101();
	}

	/*
	 * @testName: testSetObject102
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Numeric_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Numeric_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject102() throws Exception {
		super.testSetObject102();
	}

	/*
	 * @testName: testSetObject105
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Char_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Char_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject105() throws Exception {
		super.testSetObject105();
	}

	/*
	 * @testName: testSetObject106
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Char_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Char_Tab. Compare the returned
	 * value with the minimum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject106() throws Exception {
		super.testSetObject106();
	}

	/*
	 * @testName: testSetObject107
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Varchar_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Varchar_Tab. Compare the
	 * returned value with the maximum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject107() throws Exception {
		super.testSetObject107();
	}

	/*
	 * @testName: testSetObject108
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Varchar_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Varchar_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject108() throws Exception {
		super.testSetObject108();
	}

	/*
	 * @testName: testSetObject109
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Longvarchar_Tab with the maximum value of the
	 * Bit_Tab. Execute a query to retrieve the Null_Val from Longvarchar_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject109() throws Exception {
		super.testSetObject109();
	}

	/*
	 * @testName: testSetObject110
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Longvarchar_Tab with the minimum value of the
	 * Bit_Tab. Execute a query to retrieve the Null_Val from Longvarchar_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject110() throws Exception {
		super.testSetObject110();
	}

	/*
	 * @testName: testSetObject111
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Tinyint_Tab with the maximum value of the
	 * Tinyint_Tab. Execute a query to retrieve the Min_Val from Tinyint_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject111() throws Exception {
		super.testSetObject111();
	}

	/*
	 * @testName: testSetObject112
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Tinyint_Tab with the minimum value of the
	 * Tinyint_Tab. Execute a query to retrieve the Null_Val from Tinyint_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject112() throws Exception {
		super.testSetObject112();
	}

	/*
	 * @testName: testSetObject113
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Smallint_Tab with the maximum value of the
	 * Smallint_Tab. Execute a query to retrieve the Min_Val from Smallint_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject113() throws Exception {
		super.testSetObject113();
	}

	/*
	 * @testName: testSetObject114
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Smallint_Tab with the minimum value of the
	 * Smallint_Tab. Execute a query to retrieve the Null_Val from Smallint_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject114() throws Exception {
		super.testSetObject114();
	}

	/*
	 * @testName: testSetObject115
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Integer_Tab with the maximum value of the
	 * Integer_Tab. Execute a query to retrieve the Min_Val from Integer_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject115() throws Exception {
		super.testSetObject115();
	}

	/*
	 * @testName: testSetObject116
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Integer_Tab with the minimum value of the
	 * Integer_Tab. Execute a query to retrieve the Null_Val from Integer_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject116() throws Exception {
		super.testSetObject116();
	}

	/*
	 * @testName: testSetObject117
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Bigint_Tab with the maximum value of the
	 * Integer_Tab. Execute a query to retrieve the Min_Val from Bigint_Tab. Compare
	 * the returned value with the maximum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject117() throws Exception {
		super.testSetObject117();
	}

	/*
	 * @testName: testSetObject118
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Bigint_Tab with the minimum value of the
	 * Integer_Tab. Execute a query to retrieve the Null_Val from Bigint_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject118() throws Exception {
		super.testSetObject118();
	}

	/*
	 * @testName: testSetObject119
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Real_Tab with the maximum value of the Integer_Tab.
	 * Execute a query to retrieve the Min_Val from Real_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject119() throws Exception {
		super.testSetObject119();
	}

	/*
	 * @testName: testSetObject120
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Real_Tab with the minimum value of the
	 * Integer_Tab. Execute a query to retrieve the Null_Val from Real_Tab. Compare
	 * the returned value with the minimum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject120() throws Exception {
		super.testSetObject120();
	}

}
