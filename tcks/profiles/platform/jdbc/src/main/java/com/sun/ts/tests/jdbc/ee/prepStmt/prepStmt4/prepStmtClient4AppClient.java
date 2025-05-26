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
 * @(#)prepStmtClient4.java	1.15 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt4;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

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
 * The prepStmtClient4 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

@Tag("tck-appclient")

public class prepStmtClient4AppClient extends prepStmtClient4 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt4";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "prepStmt4_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient4.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = prepStmtClient4AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt4/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = prepStmtClient4AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}
		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, prepStmtClient4AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt4_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient4AppClient theTests = new prepStmtClient4AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject10
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_val with the maximum value of Integer_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject10() throws Exception {
		super.testSetObject10();
	}

	/*
	 * @testName: testSetObject11
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_val with the minimum value of Integer_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the minimum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject11() throws Exception {
		super.testSetObject11();
	}

	/*
	 * @testName: testSetObject12
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_val with the maximum value of Bigint_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject12() throws Exception {
		super.testSetObject12();
	}

	/*
	 * @testName: testSetObject13
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_val with the maximum value of Bigint_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject13() throws Exception {
		super.testSetObject13();
	}

	/*
	 * @testName: testSetObject14
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val with the maximum value of Double_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject14() throws Exception {
		super.testSetObject14();
	}

	/*
	 * @testName: testSetObject15
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the minimum value of Double_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the minimum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject15() throws Exception {
		super.testSetObject15();
	}

	/*
	 * @testName: testSetObject16
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val with the maximum value of Float_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject16() throws Exception {
		super.testSetObject16();
	}

	/*
	 * @testName: testSetObject17
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the minimum value of Float_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the minimum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject17() throws Exception {
		super.testSetObject17();
	}

	/*
	 * @testName: testSetObject18
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the Non Null value of Date_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the Non Null value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject18() throws Exception {
		super.testSetObject18();
	}

	/*
	 * @testName: testSetObject19
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the Non Null value of Time_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the Non Null value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject19() throws Exception {
		super.testSetObject19();
	}

	/*
	 * @testName: testSetObject20
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the Non Null value of Timestamp_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the Non Null
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject20() throws Exception {
		super.testSetObject20();
	}

	/*
	 * @testName: testSetObject21
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val with the maximum value of Real_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject21() throws Exception {
		super.testSetObject21();
	}

	/*
	 * @testName: testSetObject22
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the minimum value of Real_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the minimum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject22() throws Exception {
		super.testSetObject22();
	}

	/*
	 * @testName: testSetObject23
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val with the maximum value of Tinyint_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject23() throws Exception {
		super.testSetObject23();
	}

	/*
	 * @testName: testSetObject24
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the minimum value of Tinyint_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the minimum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject24() throws Exception {
		super.testSetObject24();
	}

	/*
	 * @testName: testSetObject25
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val with the maximum value of Smallint_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject25() throws Exception {
		super.testSetObject25();
	}

	/*
	 * @testName: testSetObject26
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the minimum value of Smallint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the minimum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject26() throws Exception {
		super.testSetObject26();
	}

	/*
	 * @testName: testSetObject27
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database.Update the column value of the Binary_Tab with a byte array using
	 * the PreparedStatement.setBytes(int columnIndex) method. Call the
	 * getObject(int parameterIndex) method.to retrieve the byte array. It should
	 * return the byte array object that has been set.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject27() throws Exception {
		super.testSetObject27();
	}

	/*
	 * @testName: testSetObject28
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database.Update the column value of the Varbinary_Tab with a byte array using
	 * the PreparedStatement.setBytes(int columnIndex) method. Call the
	 * getObject(int parameterIndex) method.to retrieve the byte array. It should
	 * return the byte array object that has been set.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject28() throws Exception {
		super.testSetObject28();
	}

	/*
	 * @testName: testSetObject29
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database.Update the column value of the Longvarbinary_Tab with a byte array
	 * using the PreparedStatement.setBytes(int columnIndex) method. Call the
	 * getObject(int parameterIndex) method to retrieve the byte array. It should
	 * return the byte array object that has been set.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetObject29() throws Exception {
		super.testSetObject29();
	}
}
