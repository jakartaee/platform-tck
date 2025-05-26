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
 * @(#)prepStmtClient7.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt7;

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

/**
 * The prepStmtClient7 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

@Tag("tck-javatest")
@Tag("web")
public class prepStmtClient7Servlet extends prepStmtClient7 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt7";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "prepStmt7_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient7.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = prepStmtClient7Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
// The sun servlet descriptor
		URL sunServletUrl = prepStmtClient7Servlet.class.getResource("prepStmt7_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
// Call the archive processor
		archiveProcessor.processWebArchive(archive, prepStmtClient7Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient7Servlet theTests = new prepStmtClient7Servlet();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject63
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val with the minimum value of Tinyint_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject63() throws Exception {
		super.testSetObject63();
	}

	/*
	 * @testName: testSetObject64
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val with the maximum value of Smallint_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject64() throws Exception {
		super.testSetObject64();
	}

	/*
	 * @testName: testSetObject65
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,targetSqlType)
	 * method,update the column Null_Val with the minimum value of Smallint_Tab.
	 * Call the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject65() throws Exception {
		super.testSetObject65();
	}

	/*
	 * @testName: testSetObject66
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val with the maximum value of Integer_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject66() throws Exception {
		super.testSetObject66();
	}

	/*
	 * @testName: testSetObject67
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val with the minimum value of Integer_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject67() throws Exception {
		super.testSetObject67();
	}

	/*
	 * @testName: testSetObject68
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val with the maximum value of Bigint_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject68() throws Exception {
		super.testSetObject68();
	}

	/*
	 * @testName: testSetObject69
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val with the minimum value of Bigint_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject69() throws Exception {
		super.testSetObject69();
	}

	/*
	 * @testName: testSetObject70
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Real_Tab with the maximum value of
	 * Integer_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject70() throws Exception {
		super.testSetObject70();
	}

	/*
	 * @testName: testSetObject71
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Real_Tab with the minimum value of
	 * Integer_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject71() throws Exception {
		super.testSetObject71();
	}

	/*
	 * @testName: testSetObject72
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Float_Tab with the maximum value of
	 * Decimal_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject72() throws Exception {
		super.testSetObject72();
	}

	/*
	 * @testName: testSetObject73
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Float_Tab with the minimum value of
	 * Decimal_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject73() throws Exception {
		super.testSetObject73();
	}

	/*
	 * @testName: testSetObject74
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val with the maximum value of Double_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject74() throws Exception {
		super.testSetObject74();
	}

	/*
	 * @testName: testSetObject75
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val with the minimum value of Double_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject75() throws Exception {
		super.testSetObject75();
	}

	/*
	 * @testName: testSetObject76
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method,update the column Min_Val with the maximum value of
	 * Decimal_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject76() throws Exception {
		super.testSetObject76();
	}

	/*
	 * @testName: testSetObject77
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,targetSqlType, int
	 * scale) method,update the column Null_Val with the minimum value of
	 * Decimal_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject77() throws Exception {
		super.testSetObject77();
	}

	/*
	 * @testName: testSetObject78
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,targetSqlType, int
	 * scale),update the column Min_Val with the maximum value of NUmeric_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject78() throws Exception {
		super.testSetObject78();
	}

	/*
	 * @testName: testSetObject79
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method,update the column Null_Val with the minimum value of
	 * Numeric_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject79() throws Exception {
		super.testSetObject79();
	}

	/*
	 * @testName: testSetObject82
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val with the maximum value of Decimal_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject82() throws Exception {
		super.testSetObject82();
	}
}
