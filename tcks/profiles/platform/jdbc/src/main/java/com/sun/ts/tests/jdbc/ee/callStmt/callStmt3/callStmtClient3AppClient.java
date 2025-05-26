/*
 * Copyright (c) 2006, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)callStmtClient3.java	1.28 07/10/03
 */
package com.sun.ts.tests.jdbc.ee.callStmt.callStmt3;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
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

/**
 * The callStmtClient3 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class callStmtClient3AppClient extends callStmtClient3 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt3";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "callStmt3_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient3.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = callStmtClient3AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt3/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = callStmtClient3AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, callStmtClient3AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt3_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient3AppClient theTests = new callStmtClient3AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetObject01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
	 * and call the getObject(int parameterIndex) method to retrieve the maximum
	 * value of the parameter from Numeric_Tab. Extract the maximum value from the
	 * tssql.stmt file.Compare this value with the value returned by the
	 * getObject(int parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject01() throws Exception {
		super.testGetObject01();
	}

	/*
	 * @testName: testGetObject02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
	 * and call the getObject(int parameterIndex) method to retrieve the minimum
	 * value of the parameter from Numeric_Tab. Extract the minimum value from the
	 * tssql.stmt file.Compare this value with the value returned by the
	 * getObject(int parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject02() throws Exception {
		super.testGetObject02();
	}

	/*
	 * @testName: testGetObject03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
	 * and call the getObject(int parameterIndex) method to retrieve the null value
	 * from Numeric_Tab. Check if it returns null
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject03() throws Exception {
		super.testGetObject03();
	}

	/*
	 * @testName: testGetObject04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method.to retrieve the maximum value of the
	 * parameter from Float_Tab. Extract the maximum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject04() throws Exception {
		super.testGetObject04();
	}

	/*
	 * @testName: testGetObject05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the minimum value of the
	 * parameter from Float_Tab. Extract the minimum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject05() throws Exception {
		super.testGetObject05();
	}

	/*
	 * @testName: testGetObject06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Float_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject06() throws Exception {
		super.testGetObject06();
	}

	/*
	 * @testName: testGetObject07
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the maximum value of the
	 * parameter from Smallint_Tab. Extract the maximum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject07() throws Exception {
		super.testGetObject07();
	}

	/*
	 * @testName: testGetObject08
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the minimum value of the
	 * parameter from Smallint_Tab. Extract the minimum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject08() throws Exception {
		super.testGetObject08();
	}

	/*
	 * @testName: testGetObject09
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Smallint_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject09() throws Exception {
		super.testGetObject09();
	}

	/*
	 * @testName: testGetObject10
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve a char value from
	 * Char_Tab.Extract the same char value from the tssql.stmt file.Compare this
	 * value with the value returned by the getObject(int parameterIndex).Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject10() throws Exception {
		super.testGetObject10();
	}

	/*
	 * @testName: testGetObject11
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Char_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject11() throws Exception {
		super.testGetObject11();
	}

	/*
	 * @testName: testGetObject12
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the maximum value of the
	 * parameter from Integer_Tab. Extract the maximum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject12() throws Exception {
		super.testGetObject12();
	}

	/*
	 * @testName: testGetObject13
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the minimum value of the
	 * parameter from Integer_Tab. Extract the minimum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject13() throws Exception {
		super.testGetObject13();
	}

	/*
	 * @testName: testGetObject14
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Integer_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject14() throws Exception {
		super.testGetObject14();
	}

	/*
	 * @testName: testGetObject15
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the maximum value of the
	 * parameter from Bit_Tab. Extract the maximum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject15() throws Exception {
		super.testGetObject15();
	}

	/*
	 * @testName: testGetObject16
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the minimum value of the
	 * parameter from Bit_Tab. Extract the minimum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject16() throws Exception {
		super.testGetObject16();
	}

	/*
	 * @testName: testGetObject18
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the maximum value of the
	 * parameter from Bigint_Tab. Extract the maximum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject18() throws Exception {
		super.testGetObject18();
	}

	/*
	 * @testName: testGetObject19
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the minimum value of the
	 * parameter from Bigint_Tab. Extract the minimum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject19() throws Exception {
		super.testGetObject19();
	}

	/*
	 * @testName: testGetObject20
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Bigint_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetObject20() throws Exception {
		super.testGetObject20();
	}

}
