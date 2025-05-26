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
 * @(#)callStmtClient5.java	1.18 03/05/16
 */
package com.sun.ts.tests.jdbc.ee.callStmt.callStmt5;

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
 * The callStmtClient5 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class callStmtClient5EJB extends callStmtClient5 implements Serializable {

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt5_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(callStmtClient5.class, ServiceEETest.class, EETest.class);

		URL resURL = callStmtClient5EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = callStmtClient5EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt5/callStmt5_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt5_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient5.class, ServiceEETest.class, EETest.class);

		resURL = callStmtClient5EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt5/callStmt5_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = callStmtClient5EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt5/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt5_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient5EJB theTests = new callStmtClient5EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetObject41
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
	 * and call the getObject(int parameterIndex) method to retrieve the minimum
	 * value of the parameter from Decimal_Tab. Extract the minimum value from the
	 * tssql.stmt file.Compare this value with the value returned by the
	 * getObject(int parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject41() throws Exception {
		super.testGetObject41();
	}

	/*
	 * @testName: testGetObject42
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
	 * and call the getObject(int parameterIndex) method to retrieve the null value
	 * from Decimal_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject42() throws Exception {
		super.testGetObject42();
	}

	/*
	 * @testName: testGetObject43
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Update the column Binary_Val of the Binary_Tab with a byte array
	 * using the PreparedStatement.setBytes(int columnIndex) method.Register the
	 * parameter using registerOutParameter(int parameterIndex,int sqlType) method.
	 * Execute the stored procedure and call the getObject(int parameterIndex)
	 * method to retrieve the byte array. It should return the byte array object
	 * that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject43() throws Exception {
		super.testGetObject43();
	}

	/*
	 * @testName: testGetObject44
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Binary_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject44() throws Exception {
		super.testGetObject44();
	}

	/*
	 * @testName: testGetObject45
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Update the column Varbinary_Val of the Varbinary_tab with a byte
	 * array using the PreparedStatement.setBytes(int columnIndex) method.Register
	 * the parameter using registerOutParameter(int parameterIndex,int sqlType,int
	 * scale) method. Execute the stored procedure and call the getObject(int
	 * parameterIndex) method to retrieve the byte array. It should return the byte
	 * array object that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject45() throws Exception {
		super.testGetObject45();
	}

	/*
	 * @testName: testGetObject46
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
	 * and call the getObject(int parameterIndex) method to retrieve the null value
	 * from Varbinary_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject46() throws Exception {
		super.testGetObject46();
	}

	/*
	 * @testName: testGetObject47
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Update the column Longvarbinary_Val of the Longvarbinary_Tab with a
	 * byte array using the PreparedStatement.setBytes(int columnIndex)
	 * method.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method.to retrieve the byte array. It should
	 * return the byte array object that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject47() throws Exception {
		super.testGetObject47();
	}

	/*
	 * @testName: testGetObject48
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from from
	 * Longvarbinary_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject48() throws Exception {
		super.testGetObject48();
	}

	/*
	 * @testName: testGetFloat01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1253;
	 * JDBC:JAVADOC:1254; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getFloat(int parameterIndex) method to retrieve the maximum value from
	 * Real_Tab. Extract the maximum value from the tssql.stmt file.Compare this
	 * value with the value returned by the getFloat(int parameterIndex). Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetFloat01() throws Exception {
		super.testGetFloat01();
	}

	/*
	 * @testName: testGetFloat02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1253;
	 * JDBC:JAVADOC:1254; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getFloat(int parameterIndex) method to retrieve the minimum value from
	 * Real_Tab. Extract the minimum value from the tssql.stmt file.Compare this
	 * value with the value returned by the getFloat(int parameterIndex). Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetFloat02() throws Exception {
		super.testGetFloat02();
	}

	/*
	 * @testName: testGetFloat03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1253;
	 * JDBC:JAVADOC:1254; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getFloat(int parameterIndex) method to retrieve the null value from Real_Tab.
	 * Check if it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetFloat03() throws Exception {
		super.testGetFloat03();
	}

	/*
	 * @testName: testGetString03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
	 * JDBC:JAVADOC:1242; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getString(int parameterIndex) method to retrieve a String value from
	 * Varchar_Tab Extract the same String value from the tssql.stmt file.Compare
	 * this value with the value returned by the getString(int parameterIndex). Both
	 * the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetString03() throws Exception {
		super.testGetString03();
	}

	/*
	 * @testName: testGetString04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
	 * JDBC:JAVADOC:1242; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getString(int parameterIndex) method to retrieve the null value from
	 * Varchar_Tab. Check if it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetString04() throws Exception {
		super.testGetString04();
	}

	/*
	 * @testName: testGetString05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
	 * JDBC:JAVADOC:1242; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getString(int parameterIndex) method to retrieve a String value from
	 * Longvarchar_Tab. Extract the same String value from the tssql.stmt
	 * file.Compare this value with the value returned by the getString(int
	 * parameterIndex). Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetString05() throws Exception {
		super.testGetString05();
	}

	/*
	 * @testName: testGetString06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
	 * JDBC:JAVADOC:1242; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getString(int parameterIndex) method to retrieve the null value from
	 * Longvarchar_Tab. Check if it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetString06() throws Exception {
		super.testGetString06();
	}

	/*
	 * @testName: testGetBigDecimal04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
	 * JDBC:JAVADOC:1270; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale). Execute the stored procedure and call
	 * the getBigDecimal(int parameterIndex) method.to retrieve the maximum value
	 * from Decimal_Tab. Extract the maximum value from the tssql.stmt file.Compare
	 * this value with the value returned by the getBigDecimal(int
	 * parameterIndex).Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBigDecimal04() throws Exception {
		super.testGetBigDecimal04();
	}

	/*
	 * @testName: testGetBigDecimal05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
	 * JDBC:JAVADOC:1270; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale). Execute the stored procedure and call
	 * the getBigDecimal(int parameterIndex) method to retrieve the minimum value
	 * from Decimal_Tab. Extract the minimum value from the tssql.stmt file.Compare
	 * this value with the value returned by the getBigDecimal(int
	 * parameterIndex).Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBigDecimal05() throws Exception {
		super.testGetBigDecimal05();
	}

	/*
	 * @testName: testGetBigDecimal06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
	 * JDBC:JAVADOC:1270; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType, int scale). Execute the stored procedure and call
	 * the getBigDecimal(int parameterIndex) method to retrieve the null value from
	 * Decimal_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBigDecimal06() throws Exception {
		super.testGetBigDecimal06();
	}

	/*
	 * @testName: testGetBytes01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
	 * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Update the column Binary_Val of the Binary with
	 * a byte array using the PreparedStatement.setBytes(int columnIndex)
	 * method.Execute the stored procedure and call the getBytes(int parameterIndex)
	 * method to retrieve the byte array. It should return the byte array object
	 * that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes01() throws Exception {
		super.testGetBytes01();
	}

	/*
	 * @testName: testGetBytes02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
	 * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getBytes(int parameterIndex) method to retrieve the null value from
	 * Binary_Tab. Check if it returns null.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes02() throws Exception {
		super.testGetBytes02();
	}

	/*
	 * @testName: testGetBytes03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
	 * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Update the column Varbinary_Val of the
	 * Varbinary_Tab with a byte array using the PreparedStatement.setBytes(int
	 * columnIndex) method.Execute the stored procedure and call the getBytes(int
	 * parameterIndex) method.to retrieve the byte array. It should return the byte
	 * array object that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes03() throws Exception {
		super.testGetBytes03();
	}

	/*
	 * @testName: testGetBytes04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
	 * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getBytes(int parameterIndex) method.to retrieve the null value from
	 * Varbinary_Tab. Check if it returns null.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes04() throws Exception {
		super.testGetBytes04();
	}

	/*
	 * @testName: testGetBytes05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
	 * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Update the column Longvarbinary_Val of the
	 * Longvarbinary_Tab with a byte array using the PreparedStatement.setBytes(int
	 * columnIndex) method.Execute the stored procedure and call the getBytes(int
	 * parameterIndex) method.to retrieve the byte array. It should return the byte
	 * array object that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes05() throws Exception {
		super.testGetBytes05();
	}

	/*
	 * @testName: testGetBytes06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1259;
	 * JDBC:JAVADOC:1260; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType). Execute the stored procedure and call the
	 * getBytes(int parameterIndex) method.to retrieve the null value from
	 * Longvarbinary_Tab.Check if it returns null
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes06() throws Exception {
		super.testGetBytes06();
	}

}
