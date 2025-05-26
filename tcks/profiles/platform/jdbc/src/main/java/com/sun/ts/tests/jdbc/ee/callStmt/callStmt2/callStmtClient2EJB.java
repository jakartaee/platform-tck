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
 * @(#)callStmtClient2.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt2;

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
 * The callStmtClient2 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class callStmtClient2EJB extends callStmtClient2 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt2";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt2_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(callStmtClient2.class, ServiceEETest.class, EETest.class);

		URL resURL = callStmtClient2EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = callStmtClient2EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt2/callStmt2_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt2_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient2.class, ServiceEETest.class, EETest.class);

		resURL = callStmtClient2EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt2/callStmt2_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = callStmtClient2EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt2/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt2_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;

	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient2EJB theTests = new callStmtClient2EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetTime01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1263;
	 * JDBC:JAVADOC:1264; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getTime(int
	 * parameterIndex) method to retrieve a Time value from Time_Tab. Extract the
	 * the same Time value from the tssql.stmt file.Compare this value with the
	 * value returned by the getTime(int parameterIndex).Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTime01() throws Exception {
		super.testGetTime01();
	}

	/*
	 * @testName: testGetTime02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1263;
	 * JDBC:JAVADOC:1264; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getTime(int
	 * parameterIndex) method to retrieve the null value from Time_Tab.Check if it
	 * returns null.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTime02() throws Exception {
		super.testGetTime02();
	}

	/*
	 * @testName: testGetTime03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1283;
	 * JDBC:JAVADOC:1284; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getTime(int
	 * parameterIndex,Calander cal) method to retrieve a Time value from Time_tab.
	 * Extract the same Time value from the tssql.stmt file.Compare this value with
	 * the value returned by the getTime(int parameterIndex,Calander cal). Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTime03() throws Exception {
		super.testGetTime03();
	}

	/*
	 * @testName: testGetTime04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1283;
	 * JDBC:JAVADOC:1284; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getTime(int
	 * parameterIndex,Calender cal) method to retrieve the null value from Time_Tab.
	 * Check if it returns null.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTime04() throws Exception {
		super.testGetTime04();
	}

	/*
	 * @testName: testGetTimestamp01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1265;
	 * JDBC:JAVADOC:1266; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getTimestamp(int
	 * parameterIndex) method to retrieve a Timestamp value from Timestamp_Tab.
	 * Extract the the same Timestamp value from the tssql.stmt file.Compare this
	 * value with the value returned by the getTimestamp(int parameterIndex) Both
	 * the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTimestamp01() throws Exception {
		super.testGetTimestamp01();
	}

	/*
	 * @testName: testGetTimestamp02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1265;
	 * JDBC:JAVADOC:1266; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getTimestamp(int
	 * parameterIndex) method to retrieve the null value from Timestamp_Tab. Check
	 * if it returns null.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTimestamp02() throws Exception {
		super.testGetTimestamp02();
	}

	/*
	 * @testName: testGetTimestamp03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1285;
	 * JDBC:JAVADOC:1286; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getTimestamp(int
	 * parameterIndex,Calender cal) method to retrieve a Timestamp value from
	 * Timestamp_Tab. Extract the the same Timestamp value from the tssql.stmt
	 * file.Compare this value with the value returned by the getTimestamp(int
	 * parameterIndex, Calender cal) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTimestamp03() throws Exception {
		super.testGetTimestamp03();
	}

	/*
	 * @testName: testGetTimestamp04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1285;
	 * JDBC:JAVADOC:1286; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getTimestamp(int
	 * parameterIndex,Calender cal) method to retrieve the null value from
	 * Timestamp_Tab. Check if it returns null.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTimestamp04() throws Exception {
		super.testGetTimestamp04();
	}

	/*
	 * @testName: testGetDate01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1261;
	 * JDBC:JAVADOC:1262; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDate(int
	 * parameterIndex) method to retrieve a Date value from Date_Tab. Extract the
	 * the same Date value from the tssql.stmt file.Compare this value with the
	 * value returned by the getDate(int parameterIndex).Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetDate01() throws Exception {
		super.testGetDate01();
	}

	/*
	 * @testName: testGetDate02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1261;
	 * JDBC:JAVADOC:1262; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDate(int
	 * parameterIndex) method to retrieve the null value from Date_Tab. Check if it
	 * returns null.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetDate02() throws Exception {
		super.testGetDate02();
	}

	/*
	 * @testName: testGetDate03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1281;
	 * JDBC:JAVADOC:1282; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDate(int
	 * parameterIndex,Calender cal) method to retrieve a Date value from
	 * Date_Tab.Extract the the same Date value from the tssql.stmt file.Compare
	 * this value with the value returned by the getDate(int parameterIndex,
	 * Calender cal) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetDate03() throws Exception {
		super.testGetDate03();
	}

	/*
	 * @testName: testGetDate04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1281;
	 * JDBC:JAVADOC:1282; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDate(int
	 * parameterIndex, Calender cal) method to retrieve the null value from
	 * Date_Tab. Check if it returns null.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetDate04() throws Exception {
		super.testGetDate04();
	}

	/*
	 * @testName: testGetByte01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1245;
	 * JDBC:JAVADOC:1246; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getByte(int
	 * parameterIndex) method to retrieve the maximum value from Tinyint_Tab.
	 * Extract the maximum value from the tssql.stmt file.Compare this value with
	 * the value returned by the getByte(int parameterIndex).Both the values should
	 * be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte01() throws Exception {
		super.testGetByte01();
	}

	/*
	 * @testName: testGetByte02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1245;
	 * JDBC:JAVADOC:1246; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getByte(int
	 * parameterIndex) method to retrieve the minimum value from Tinyint_Tab.
	 * Extract the minimum value from the tssql.stmt file.Compare this value with
	 * the value returned by the getByte(int parameterIndex).Both the values should
	 * be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte02() throws Exception {
		super.testGetByte02();
	}

	/*
	 * @testName: testGetByte03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1245;
	 * JDBC:JAVADOC:1246; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getByte(int
	 * parameterIndex) method to retrieve the null value from Tinyint_Tab.Check if
	 * it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte03() throws Exception {
		super.testGetByte03();
	}

	/*
	 * @testName: testGetDouble01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
	 * JDBC:JAVADOC:1256; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDouble(int
	 * parameterIndex) method to retrieve the maximum value of Double_Tab. Extract
	 * the maximum value from the tssql.stmt file.Compare this value with the value
	 * returned by the getDouble(int parameterIndex). Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetDouble01() throws Exception {
		super.testGetDouble01();
	}

	/*
	 * @testName: testGetDouble02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
	 * JDBC:JAVADOC:1256; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDouble(int
	 * parameterIndex) method to retrieve the minimum value from Double_Tab. Extract
	 * the minimum value from the tssql.stmt file.Compare this value with the value
	 * returned by the getDouble(int parameterIndex). Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetDouble02() throws Exception {
		super.testGetDouble02();
	}

	/*
	 * @testName: testGetDouble03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
	 * JDBC:JAVADOC:1256; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getDouble(int
	 * parameterIndex) method to retrieve the null value from Double_Tab.Check if it
	 * returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetDouble03() throws Exception {
		super.testGetDouble03();
	}

	/*
	 * @testName: testWasNull
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1239;
	 * JDBC:JAVADOC:1240; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Execute the stored procedure and call the getInt(int parameterIndex)
	 * method to retrieve the null value from Integer_Tab. Check if it returns null
	 * using the method wasNull().
	 */
	@Test
	@TargetVehicle("ejb")
	public void testWasNull() throws Exception {
		super.testWasNull();
	}
}
