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
 * @(#)callStmtClient18.java	1.19 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt18;

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

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient18 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class callStmtClient18EJB extends callStmtClient18 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt18";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt18_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(callStmtClient18.class, ServiceEETest.class, EETest.class);

		URL resURL = callStmtClient18EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = callStmtClient18EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt18/callStmt18_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt18_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient18.class, ServiceEETest.class, EETest.class);

		resURL = callStmtClient18EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt18/callStmt18_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = callStmtClient18EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt18/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt18_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;

	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient18EJB theTests = new callStmtClient18EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject201
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the column Min_Val of Double_Tab with the
	 * maximum value of Double_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the maximum value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method.Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject201() throws Exception {
		super.testSetObject201();
	}

	/*
	 * @testName: testSetObject202
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Double_Tab with the minimum value of
	 * Double_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject202() throws Exception {
		super.testSetObject202();
	}

	/*
	 * @testName: testSetObject203
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
	@TargetVehicle("ejb")
	public void testSetObject203() throws Exception {
		super.testSetObject203();
	}

	/*
	 * @testName: testSetObject204
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Decimal_Tab with the minimum value of
	 * Decimal_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject204() throws Exception {
		super.testSetObject204();
	}

	/*
	 * @testName: testSetObject205
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
	 * method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject205() throws Exception {
		super.testSetObject205();
	}

	/*
	 * @testName: testSetObject206
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
	@TargetVehicle("ejb")
	public void testSetObject206() throws Exception {
		super.testSetObject206();
	}

	/*
	 * @testName: testSetObject209
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the Name column of Char_Tab with the maximum
	 * value of Double_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject209() throws Exception {
		super.testSetObject209();
	}

	/*
	 * @testName: testSetObject210
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Char_Tab with the minimum value of
	 * Double_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject210() throws Exception {
		super.testSetObject210();
	}

	/*
	 * @testName: testSetObject211
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the Name column of Varchar_Tab with the maximum
	 * value of Double_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject211() throws Exception {
		super.testSetObject211();
	}

	/*
	 * @testName: testSetObject212
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Varchar_Tab with the minimum value of
	 * Double_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject212() throws Exception {
		super.testSetObject212();
	}

	/*
	 * @testName: testSetObject213
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, int
	 * targetSqlType) method,update the Name column of Longvarchar_Tab with the
	 * maximum value of Double_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the maximum value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject213() throws Exception {
		super.testSetObject213();
	}

	/*
	 * @testName: testSetObject214
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x,int targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the minimum value
	 * of Double_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject214() throws Exception {
		super.testSetObject214();
	}

	/*
	 * @testName: testSetObject215
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Byte Array object for
	 * SQL Type BINARY and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Byte array object that is
	 * been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject215() throws Exception {
		super.testSetObject215();
	}

	/*
	 * @testName: testSetObject216
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Byte Array object for
	 * SQL Type VARBINARY and call statement.executeQuery(String sql) method and
	 * call ResultSet.getObject(int column). It should return a Byte array object
	 * that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject216() throws Exception {
		super.testSetObject216();
	}

	/*
	 * @testName: testSetObject217
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Byte Array object for
	 * SQL Type LONGVARBINARY and call statement.executeQuery(String sql) method and
	 * call ResultSet.getObject(int column). It should return a Byte array object
	 * that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject217() throws Exception {
		super.testSetObject217();
	}

	/*
	 * @testName: testSetObject218
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Date object for SQL Type
	 * CHAR and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Date object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject218() throws Exception {
		super.testSetObject218();
	}

	/*
	 * @testName: testSetObject219
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Date object for SQL Type
	 * VARCHAR and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a String object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject219() throws Exception {
		super.testSetObject219();
	}

	/*
	 * @testName: testSetObject220
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Date object for SQL Type
	 * LONGVARCHAR and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a String object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject220() throws Exception {
		super.testSetObject220();
	}
}
