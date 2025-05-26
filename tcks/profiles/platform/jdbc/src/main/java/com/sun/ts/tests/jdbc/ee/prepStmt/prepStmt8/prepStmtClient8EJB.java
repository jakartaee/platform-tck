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
 * %W% %E%
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt8;

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

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The prepStmtClient8 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class prepStmtClient8EJB extends prepStmtClient8 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt8";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "prepStmt8_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(prepStmtClient8.class, ServiceEETest.class, EETest.class);

		URL resURL = prepStmtClient8EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = prepStmtClient8EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt8/prepStmt8_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "prepStmt8_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(prepStmtClient8.class, ServiceEETest.class, EETest.class);

		resURL = prepStmtClient8EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt8/prepStmt8_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = prepStmtClient8EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt8/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt8_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient8EJB theTests = new prepStmtClient8EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject83
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Char_Tab with the minimum value of
	 * Decimal_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject83() throws Exception {
		super.testSetObject83();
	}

	/*
	 * @testName: testSetObject84
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Varchar_Tab with the maximum value of
	 * Decimal_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject84() throws Exception {
		super.testSetObject84();
	}

	/*
	 * @testName: testSetObject85
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Varchar_Tab with the minimum value of
	 * Decimal_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject85() throws Exception {
		super.testSetObject85();
	}

	/*
	 * @testName: testSetObject86
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the maximum value
	 * of Decimal_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject86() throws Exception {
		super.testSetObject86();
	}

	/*
	 * @testName: testSetObject87
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the minimum value
	 * of Decimal_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject87() throws Exception {
		super.testSetObject87();
	}

	/*
	 * @testName: testSetObject88
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Tinyint_Tab with the maximum value of
	 * Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject88() throws Exception {
		super.testSetObject88();
	}

	/*
	 * @testName: testSetObject89
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Tinyint_Tab with the minimum value of
	 * Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject89() throws Exception {
		super.testSetObject89();
	}

	/*
	 * @testName: testSetObject90
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Smallint_Tab with the maximum (true)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject90() throws Exception {
		super.testSetObject90();
	}

	/*
	 * @testName: testSetObject91
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Smallint_Tab with the minimum (false)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum (false) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject91() throws Exception {
		super.testSetObject91();
	}

	/*
	 * @testName: testSetObject92
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Integer_Tab with the maximum (true) value
	 * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (true) value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject92() throws Exception {
		super.testSetObject92();
	}

	/*
	 * @testName: testSetObject93
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Integer_Tab with the minimum (false)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum (false) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject93() throws Exception {
		super.testSetObject93();
	}

	/*
	 * @testName: testSetObject94
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Bigint_Tab with the maximum (true) value
	 * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (true) value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject94() throws Exception {
		super.testSetObject94();
	}

	/*
	 * @testName: testSetObject95
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Bigint_Tab with the minimum (false)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum (false) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject95() throws Exception {
		super.testSetObject95();
	}

	/*
	 * @testName: testSetObject96
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Real_Tab with the maximum (true) value of
	 * Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (true) value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject96() throws Exception {
		super.testSetObject96();
	}

	/*
	 * @testName: testSetObject97
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Real_Tab with the minimum (false) value
	 * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum (false) value from the tssql.stmt file. Compare this
	 * value with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject97() throws Exception {
		super.testSetObject97();
	}

	/*
	 * @testName: testSetObject98
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Float_Tab with the maximum (true) value
	 * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (true) value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject98() throws Exception {
		super.testSetObject98();
	}

	/*
	 * @testName: testSetObject99
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Float_Tab with the minimum (true) value
	 * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum (true) value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject99() throws Exception {
		super.testSetObject99();
	}

	/*
	 * @testName: testSetObject100
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Double_Tab with the maximum (true) value
	 * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (true) value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject100() throws Exception {
		super.testSetObject100();
	}

	/*
	 * @testName: testSetObject101
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Double_Tab with the minimum (false)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum (false) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject101() throws Exception {
		super.testSetObject101();
	}

	/*
	 * @testName: testSetObject102
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Decimal_Tab with the maximum (true)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject102() throws Exception {
		super.testSetObject102();
	}
}
