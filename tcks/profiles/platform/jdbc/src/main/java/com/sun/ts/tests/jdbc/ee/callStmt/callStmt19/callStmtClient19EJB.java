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
 * @(#)callStmtClient19.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt19;

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
 * The callStmtClient19 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 */
@Tag("tck-appclient")

public class callStmtClient19EJB extends callStmtClient19 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt19";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt19_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(callStmtClient19.class, ServiceEETest.class, EETest.class);

		URL resURL = callStmtClient19EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = callStmtClient19EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt19/callStmt19_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt19_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient19.class, ServiceEETest.class, EETest.class);

		resURL = callStmtClient19EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt19/callStmt19_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = callStmtClient19EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt19/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt19_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;

	};

	public static void main(String[] args) {
		callStmtClient19EJB theTests = new callStmtClient19EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject221
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Date object for SQL Type
	 * Date and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject221() throws Exception {
		super.testSetObject221();
	}

	/*
	 * @testName: testSetObject223
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Time object for SQL Type
	 * Char and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject223() throws Exception {
		super.testSetObject223();
	}

	/*
	 * @testName: testSetObject224
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Time object for SQL Type
	 * Varchar and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject224() throws Exception {
		super.testSetObject224();
	}

	/*
	 * @testName: testSetObject225
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Time object for SQL Type
	 * Longvarchar and call statement.executeQuery method and call getObject method
	 * of ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject225() throws Exception {
		super.testSetObject225();
	}

	/*
	 * @testName: testSetObject226
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Time object for SQL Type
	 * Time and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject226() throws Exception {
		super.testSetObject226();
	}

	/*
	 * @testName: testSetObject227
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Char and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject227() throws Exception {
		super.testSetObject227();
	}

	/*
	 * @testName: testSetObject228
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Varchar and call statement.executeQuery method and call getObject method
	 * of ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject228() throws Exception {
		super.testSetObject228();
	}

	/*
	 * @testName: testSetObject229
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Longvarchar and call statement.executeQuery method and call getObject
	 * method of ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject229() throws Exception {
		super.testSetObject229();
	}

	/*
	 * @testName: testSetObject231
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Time and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject231() throws Exception {
		super.testSetObject231();
	}

	/*
	 * @testName: testSetObject232
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Timestamp and call statement.executeQuery method and call getObject
	 * method of ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject232() throws Exception {
		super.testSetObject232();
	}

	/*
	 * @testName: testRegisterOutParameter01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBigDecimal(int
	 * parameterIndex, int jdbcType) method to set maximum BigDecimal value in null
	 * column and call registerOutParameter(int parameterIndex,int jdbcType, int
	 * scale) method and call getBigDecimal method. It should return a BigDecimal
	 * object that is been set. (Note: This test case also checks the support for
	 * INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRegisterOutParameter01() throws Exception {
		super.testRegisterOutParameter01();
	}

	/*
	 * @testName: testRegisterOutParameter02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBigDecimal(int
	 * parameterIndex, int jdbcType) method to set minimum BigDecimal value in
	 * maximum value column and call registerOutParameter(int parameterIndex,int
	 * jdbcType,int scale) method and call getBigDecimal method. It should return a
	 * BigDecimal object that is been set. (Note: This test case also checks the
	 * support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRegisterOutParameter02() throws Exception {
		super.testRegisterOutParameter02();
	}

	/*
	 * @testName: testRegisterOutParameter03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBigDecimal(int
	 * parameterIndex, int jdbcType) method to set maximum Decimal value in null
	 * column and call registerOutParameter(int parameterIndex,int jdbcType, int
	 * scale) method and call getBigDecimal method. It should return a BigDecimal
	 * object that is been set. (Note: This test case also checks the support for
	 * INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRegisterOutParameter03() throws Exception {
		super.testRegisterOutParameter03();
	}

	/*
	 * @testName: testRegisterOutParameter04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBigDecimal(int
	 * parameterIndex, int jdbcType) method to set minimum Decimal value in maximum
	 * value column in Decimal table and call registerOutParameter(int
	 * parameterIndex,int jdbcType,int scale) method and call getBigDecimal method.
	 * It should return a BigDecimal object that is been set. (Note: This test case
	 * also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRegisterOutParameter04() throws Exception {
		super.testRegisterOutParameter04();
	}

	/*
	 * @testName: testRegisterOutParameter05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDouble(int
	 * parameterIndex, int jdbcType) method to set maximum Double value in null
	 * column and call registerOutParameter(int parameterIndex,int jdbcType) method
	 * and call getDouble method. It should return a double value that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRegisterOutParameter05() throws Exception {
		super.testRegisterOutParameter05();
	}

	/*
	 * @testName: testRegisterOutParameter06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDouble(int
	 * parameterIndex, int jdbcType) method to set minimum double value in maximum
	 * value column in Double table and call registerOutParameter(int
	 * parameterIndex,int jdbcType) method and call getDouble method. It should
	 * return a double value that is been set. (Note: This test case also checks the
	 * support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRegisterOutParameter06() throws Exception {
		super.testRegisterOutParameter06();
	}

	/*
	 * @testName: testRegisterOutParameter07
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDouble(int
	 * parameterIndex, int jdbcType) method to set maximum Float value in null
	 * column and call registerOutParameter method and call getDouble method. It
	 * should return a double value that is been set. (Note: This test case also
	 * checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRegisterOutParameter07() throws Exception {
		super.testRegisterOutParameter07();
	}

	/*
	 * @testName: testRegisterOutParameter08
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDouble() method to set
	 * minimum float value in maximum value column in Float table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call
	 * getDouble method. It should return a double value that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testRegisterOutParameter08() throws Exception {
		super.testRegisterOutParameter08();
	}
}
