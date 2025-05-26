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
 * @(#)callStmtClient16.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt16;

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
 * The callStmtClient16 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class callStmtClient16JSP extends callStmtClient16 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt16";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt16_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient16.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = callStmtClient16JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = callStmtClient16JSP.class.getResource("callStmt16_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient16JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(callStmtClient16JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient16JSP theTests = new callStmtClient16JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject161
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the Name column of Longvarchar_Tab with the
	 * maximum value of Bigint_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the maximum value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject161() throws Exception {
		super.testSetObject161();
	}

	/*
	 * @testName: testSetObject162
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the minimum value
	 * of Bigint_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject162() throws Exception {
		super.testSetObject162();
	}

	/*
	 * @testName: testSetObject163
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the column Min_Val of Tinyint_Tab with the
	 * maximum value of Tinyint_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the maximum value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject163() throws Exception {
		super.testSetObject163();
	}

	/*
	 * @testName: testSetObject164
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Tinyint_Tab with the minimum value of
	 * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject164() throws Exception {
		super.testSetObject164();
	}

	/*
	 * @testName: testSetObject165
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the column Min_Val of Smallint_Tab with the
	 * maximum value of Smallint_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the maximum value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method.Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject165() throws Exception {
		super.testSetObject165();
	}

	/*
	 * @testName: testSetObject166
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Smallint_Tab with the minimum value of
	 * Smallint_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject166() throws Exception {
		super.testSetObject166();
	}

	/*
	 * @testName: testSetObject167
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the column Min_Val of Integer_Tab with some
	 * integer value after converting it to Float. Call the getObject(int columnno)
	 * method to retrieve this value. Compare this value with the value that is sent
	 * to database. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject167() throws Exception {
		super.testSetObject167();
	}

	/*
	 * @testName: testSetObject171
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the column Min_Val of Real_Tab with the maximum
	 * value of Real_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject171() throws Exception {
		super.testSetObject171();
	}

	/*
	 * @testName: testSetObject172
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Real_Tab with the minimum value of
	 * Real_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject172() throws Exception {
		super.testSetObject172();
	}

	/*
	 * @testName: testSetObject173
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the column Min_Val of Float_Tab with the maximum
	 * value of Float_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject173() throws Exception {
		super.testSetObject173();
	}

	/*
	 * @testName: testSetObject174
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Float_Tab with the minimum value of
	 * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject174() throws Exception {
		super.testSetObject174();
	}

	/*
	 * @testName: testSetObject177
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the column Min_Val of Decimal_Tab with the
	 * maximum value of Decimal_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the maximum value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject177() throws Exception {
		super.testSetObject177();
	}

	/*
	 * @testName: testSetObject178
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType,
	 * int scale) method,update the column Null_Val of Decimal_Tab with the minimum
	 * value of Decimal_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum value from the tssql.stmt file. Compare this
	 * value with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject178() throws Exception {
		super.testSetObject178();
	}

	/*
	 * @testName: testSetObject179
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the column Min_Val of Numeric_Tab with the
	 * maximum value of Numeric_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the maximum value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method.Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject179() throws Exception {
		super.testSetObject179();
	}

	/*
	 * @testName: testSetObject180
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Numeric_Tab with the minimum value of
	 * Numeric_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject180() throws Exception {
		super.testSetObject180();
	}
}
