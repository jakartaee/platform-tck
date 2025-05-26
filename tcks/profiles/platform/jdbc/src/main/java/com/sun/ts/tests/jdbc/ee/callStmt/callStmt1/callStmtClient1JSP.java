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
 * @(#)callStmtClient1.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt1;

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

/**
 * The callStmtClient1 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 */

@Tag("tck-javatest")
@Tag("web")
public class callStmtClient1JSP extends callStmtClient1 implements Serializable {

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt1_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient1.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = callStmtClient1JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = callStmtClient1JSP.class.getResource("callStmt1_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient1JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(callStmtClient1JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient1JSP theTests = new callStmtClient1JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetBigDecimal01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
	 * JDBC:JAVADOC:1270; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getBigDecimal(int
	 * parameterIndex) method to retrieve the maximum value of the Numeric_Tab.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getBigDecimal(int parameterIndex) method.Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBigDecimal01() throws Exception {
		super.testGetBigDecimal01();
	}

	/*
	 * @testName: testGetBigDecimal02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
	 * JDBC:JAVADOC:1270; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getBigDecimal(int
	 * parameterIndex) method to retrieve the minimum value of the Numeric_Tab.
	 * Extract the minimum value from the tssql.stmt file.Compare this value with
	 * the value returned by the getBigDecimal(int parameterIndex).Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBigDecimal02() throws Exception {
		super.testGetBigDecimal02();
	}

	/*
	 * @testName: testGetBigDecimal03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
	 * JDBC:JAVADOC:1270; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getBigDecimal(int
	 * parameterIndex) method to retrieve the null value from Numeric_Tab. Check if
	 * it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBigDecimal03() throws Exception {
		super.testGetBigDecimal03();
	}

	/*
	 * @testName: testGetDouble01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
	 * JDBC:JAVADOC:1256; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDouble(int
	 * parameterIndex) method to retrieve the maximum value of the Float_Tab.
	 * Extract the maximum value from the tssql.stmt file.Compare this value with
	 * the value returned by the getDouble(int parameterIndex). Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDouble01() throws Exception {
		super.testGetDouble01();
	}

	/*
	 * @testName: testGetDouble02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
	 * JDBC:JAVADOC:1256; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDouble(int
	 * parameterIndex) method to retrieve the minimum value of the Float_Tab.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getDouble(int parameterIndex). Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDouble02() throws Exception {
		super.testGetDouble02();
	}

	/*
	 * @testName: testGetDouble03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
	 * JDBC:JAVADOC:1256; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDouble(int
	 * parameterIndex) method to retrieve the null value from Float_Tab. Check if it
	 * returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDouble03() throws Exception {
		super.testGetDouble03();
	}

	/*
	 * @testName: testGetShort01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1247;
	 * JDBC:JAVADOC:1248; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:3;
	 * JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getShort(int
	 * parameterIndex) method to retrieve the maximum value of the Smallint_Tab.
	 * Extract the maximum value from the tssql.stmt file.Compare this value with
	 * the value returned by the getShort(int parameterIndex). Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort01() throws Exception {
		super.testGetShort01();
	}

	/*
	 * @testName: testGetShort02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1247;
	 * JDBC:JAVADOC:1248; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getShort(int
	 * parameterIndex) method to retrieve the minimum value of the Smallint_Tab.
	 * Extract the minimum value from the tssql.stmt file.Compare this value with
	 * the value returned by the getShort(int parameterIndex). Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort02() throws Exception {
		super.testGetShort02();
	}

	/*
	 * @testName: testGetShort03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1247;
	 * JDBC:JAVADOC:1248; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getShort(int
	 * parameterIndex) method to retrieve the null value from the Smallint_Tab.
	 * Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort03() throws Exception {
		super.testGetShort03();
	}

	/*
	 * @testName: testGetString01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
	 * JDBC:JAVADOC:1242; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getString(int
	 * parameterIndex) method to retrieve a non null String value from Char_Tab.
	 * Extract the same String value from the tssql.stmt file.Compare this value
	 * with the value returned by the getString(int parameterIndex). Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString01() throws Exception {
		super.testGetString01();
	}

	/*
	 * @testName: testGetString02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
	 * JDBC:JAVADOC:1242; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getString(int
	 * parameterIndex) method to retrieve the null value from Char_Tab. Check if it
	 * returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString02() throws Exception {
		super.testGetString02();
	}

	/*
	 * @testName: testGetInt01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1249;
	 * JDBC:JAVADOC:1250; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:4;
	 * JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getInt(int parameterIndex)
	 * method to retrieve the maximum value from Integer_Tab. Extract the maximum
	 * value from the tssql.stmt file.Compare this value with the value returned by
	 * the getInt(int parameterIndex). Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt01() throws Exception {
		super.testGetInt01();
	}

	/*
	 * @testName: testGetInt02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1249;
	 * JDBC:JAVADOC:1250; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getInt(int parameterIndex)
	 * method to retrieve the minimum value from Integer_Tab. Extract the minimum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getInt(int parameterIndex). Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt02() throws Exception {
		super.testGetInt02();
	}

	/*
	 * @testName: testGetInt03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1249;
	 * JDBC:JAVADOC:1250; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getInt(int parameterIndex)
	 * method to retrieve the null value. Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt03() throws Exception {
		super.testGetInt03();
	}

	/*
	 * @testName: testGetBoolean01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1243;
	 * JDBC:JAVADOC:1244; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:1;
	 * JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getBoolean(int
	 * parameterIndex) method to retrieve the maximum value of Bit_Tab. Extract the
	 * maximum value from the tssql.stmt file.Compare this value with the value
	 * returned by the getBoolean(int parameterIndex).Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBoolean01() throws Exception {
		super.testGetBoolean01();
	}

	/*
	 * @testName: testGetBoolean02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1243;
	 * JDBC:JAVADOC:1244; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:1;
	 * JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getBoolean(int
	 * parameterIndex) method.to retrieve the minimum value of Bit_Tab. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getBoolean(int parameterIndex) Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBoolean02() throws Exception {
		super.testGetBoolean02();
	}

	/*
	 * @testName: testGetLong01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1251;
	 * JDBC:JAVADOC:1252; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:5;
	 * JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getLong(int
	 * parameterIndex) method to retrieve the maximum value of Bigint_Tab. Extract
	 * the maximum value from the tssql.stmt file.Compare this value with the value
	 * returned by the getLong(int parameterIndex). Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetLong01() throws Exception {
		super.testGetLong01();
	}

	/*
	 * @testName: testGetLong02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1251;
	 * JDBC:JAVADOC:1252; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getLong(int
	 * parameterIndex) method to retrieve the minimum value from Bigint_Tab. Extract
	 * the minimum value from the tssql.stmt file.Compare this value with the value
	 * returned by the getLong(int parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetLong02() throws Exception {
		super.testGetLong02();
	}

	/*
	 * @testName: testGetLong03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1251;
	 * JDBC:JAVADOC:1252; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
	 * JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getLong(int
	 * parameterIndex) method to retrieve the null value from Bigint_Tab. Check if
	 * it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetLong03() throws Exception {
		super.testGetLong03();
	}
}
