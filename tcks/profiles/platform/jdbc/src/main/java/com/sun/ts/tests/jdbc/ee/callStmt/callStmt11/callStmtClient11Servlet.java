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
 * $Id$
 */

/*
 * @(#)callStmtClient11.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt11;

import java.io.IOException;
import java.io.Serializable;
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
 * The callStmtClient11 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */
@Tag("tck-javatest")
@Tag("web")
public class callStmtClient11Servlet extends callStmtClient11 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt11";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentServlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt11_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient11.class, ServiceEETest.class, EETest.class);
		archive.addAsWebInfResource(callStmtClient11Servlet.class.getPackage(), "servlet_vehicle_web.xml", "web.xml");

		// The servlet descriptor
		URL servletUrl = callStmtClient11Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
		// The sun servlet descriptor
		URL sunServletUrl = callStmtClient11Servlet.class.getResource("callStmt11_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient11Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient11Servlet theTests = new callStmtClient11Servlet();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject61
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
	 * file. Both of them should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject61() throws Exception {
		super.testSetObject61();
	}

	/*
	 * @testName: testSetObject62
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
	 * file. Both of them should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject62() throws Exception {
		super.testSetObject62();
	}

	/*
	 * @testName: testSetObject63
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
	 * file. Both of them should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject63() throws Exception {
		super.testSetObject63();
	}

	/*
	 * @testName: testSetObject64
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
	 * file. Both of them should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject64() throws Exception {
		super.testSetObject64();
	}

	/*
	 * @testName: testSetObject65
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Bigint_Tab with the maximum value of the
	 * Bigint_Tab. Execute a query to retrieve the Min_Val from Bigint_Tab. Compare
	 * the returned value with the maximum value extracted from tssql.stmt file.
	 * Both of them should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject65() throws Exception {
		super.testSetObject65();
	}

	/*
	 * @testName: testSetObject66
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Bigint_Tab with the minimum value of the
	 * Bigint_Tab. Execute a query to retrieve the Null_Val from Bigint_Tab. Compare
	 * the returned value with the minimum value extracted from tssql.stmt file.
	 * Both of them should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject66() throws Exception {
		super.testSetObject66();
	}

	/*
	 * @testName: testSetObject67
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:7; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Integer_Tab with the maximum value of the Real_Tab.
	 * Execute a query to retrieve the Min_Val from Real_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject67() throws Exception {
		super.testSetObject67();
	}

	/*
	 * @testName: testSetObject68
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
	 * Both of them should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject68() throws Exception {
		super.testSetObject68();
	}

	/*
	 * @testName: testSetObject69
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Float_Tab with the maximum value of the
	 * Decimal_Tab. Execute a query to retrieve the Min_Val from Float_Tab. Compare
	 * the returned value with the maximum value extracted from tssql.stmt file.
	 * Both of them should be equal
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject69() throws Exception {
		super.testSetObject69();
	}

	/*
	 * @testName: testSetObject70
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Float_Tab with the minimum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Decimal_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject70() throws Exception {
		super.testSetObject70();
	}

	/*
	 * @testName: testSetObject71
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Double_Tab with the maximum value of the
	 * Double_Tab. Execute a query to retrieve the Min_Val from Double_Tab. Compare
	 * the returned value with the maximum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject71() throws Exception {
		super.testSetObject71();
	}

	/*
	 * @testName: testSetObject72
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Double_Tab with the minimum value of the
	 * Double_Tab. Execute a query to retrieve the Null_Val from Double_Tab. Compare
	 * the returned value with the minimum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject72() throws Exception {
		super.testSetObject72();
	}

	/*
	 * @testName: testSetObject73
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Decimal_Tab with the maximum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Decimal_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject73() throws Exception {
		super.testSetObject73();
	}

	/*
	 * @testName: testSetObject74
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Decimal_Tab with the minimum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Decimal_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject74() throws Exception {
		super.testSetObject74();
	}

	/*
	 * @testName: testSetObject75
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Numeric_Tab with the maximum value of the
	 * Numeric_Tab. Execute a query to retrieve the Min_Val from Numeric_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject75() throws Exception {
		super.testSetObject75();
	}

	/*
	 * @testName: testSetObject76
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Numeric_Tab with the minimum value of the
	 * Numeric_Tab. Execute a query to retrieve the Null_Val from Numeric_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject76() throws Exception {
		super.testSetObject76();
	}

	/*
	 * @testName: testSetObject79
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Char_Tab with the maximum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Char_Tab. Compare
	 * the returned value with the maximum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject79() throws Exception {
		super.testSetObject79();
	}

	/*
	 * @testName: testSetObject80
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Char_Tab with the minimum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Char_Tab. Compare
	 * the returned value with the minimum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetObject80() throws Exception {
		super.testSetObject80();
	}
}
