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
 * @(#)callStmtClient9.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt9;

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

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient9 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class callStmtClient9EJB extends callStmtClient9 implements Serializable {

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentEJB(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt9_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(callStmtClient9.class, ServiceEETest.class, EETest.class);

		URL resURL = callStmtClient9EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = callStmtClient9EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt9/callStmt9_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt9_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient9.class, ServiceEETest.class, EETest.class);

		resURL = callStmtClient9EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt9/callStmt9_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = callStmtClient9EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt9/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt9_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	private static final String testName = "jdbc.ee.callStmt.callStmt9";

	/*
	 * @testName: testSetObject21
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Float object for SQL Type REAL and
	 * call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Float object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal. *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject21() throws Exception {
		super.testSetObject21();
	}

	/*
	 * @testName: testSetObject22
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Float object for SQL Type REAL and
	 * call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Float object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal. *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject22() throws Exception {
		super.testSetObject22();
	}

	/*
	 * @testName: testSetObject23
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Integer object for SQL Type TINYINT
	 * and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject23() throws Exception {
		super.testSetObject23();
	}

	/*
	 * @testName: testSetObject24
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Integer object for SQL Type TINYINT
	 * and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject24() throws Exception {
		super.testSetObject24();
	}

	/*
	 * @testName: testSetObject25
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Integer object for SQL Type SMALLINT
	 * and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject25() throws Exception {
		super.testSetObject25();
	}

	/*
	 * @testName: testSetObject26
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Integer object for SQL Type SMALLINT
	 * and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject26() throws Exception {
		super.testSetObject26();
	}

	/*
	 * @testName: testSetObject27
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Byte array object for SQL Type Binary
	 * and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a Byte Array object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject27() throws Exception {
		super.testSetObject27();
	}

	/*
	 * @testName: testSetObject28
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Byte array object for SQL Type
	 * Varbinary and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a Varbinary object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject28() throws Exception {
		super.testSetObject28();
	}

	/*
	 * @testName: testSetObject29
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Byte array object for SQL Type
	 * Longvarbinary and call statement.executeQuery method and call getObject
	 * method of ResultSet. It should return a Varbinary object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject29() throws Exception {
		super.testSetObject29();
	}

	/*
	 * @testName: testSetObject30
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type TINYINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject30() throws Exception {
		super.testSetObject30();
	}

	/*
	 * @testName: testSetObject31
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type TINYINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject31() throws Exception {
		super.testSetObject31();
	}

	/*
	 * @testName: testSetObject32
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type SMALLINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject32() throws Exception {
		super.testSetObject32();
	}

	/*
	 * @testName: testSetObject33
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type SMALLINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject33() throws Exception {
		super.testSetObject33();
	}

	/*
	 * @testName: testSetObject34
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type INTEGER and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject34() throws Exception {
		super.testSetObject34();
	}

	/*
	 * @testName: testSetObject35
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type INTEGER and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject35() throws Exception {
		super.testSetObject35();
	}

	/*
	 * @testName: testSetObject36
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type BIGINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Long object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject36() throws Exception {
		super.testSetObject36();
	}

	/*
	 * @testName: testSetObject37
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type BIGINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Long object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject37() throws Exception {
		super.testSetObject37();
	}

	/*
	 * @testName: testSetObject38
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type REAL and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Float object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject38() throws Exception {
		super.testSetObject38();
	}

	/*
	 * @testName: testSetObject39
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type REAL and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Float object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject39() throws Exception {
		super.testSetObject39();
	}

	/*
	 * @testName: testSetObject40
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type FLOAT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Double object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject40() throws Exception {
		super.testSetObject40();
	}
}
