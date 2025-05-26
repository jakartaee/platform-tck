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
 * $Id$
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt10;

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
 * The prepStmtClient10 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/27/00
 */

@Tag("tck-appclient")

public class prepStmtClient10EJB extends prepStmtClient10 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt10";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "prepStmt10_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(prepStmtClient10.class, ServiceEETest.class, EETest.class);

		URL resURL = prepStmtClient10EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = prepStmtClient10EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt10/prepStmt10_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "prepStmt10_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(prepStmtClient10.class, ServiceEETest.class, EETest.class);

		resURL = prepStmtClient10EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt10/prepStmt10_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = prepStmtClient10EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt10/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt10_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient10EJB theTests = new prepStmtClient10EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject123
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Real_Tab with the minimum (false) value
	 * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum (false) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject123() throws Exception {
		super.testSetObject123();
	}

	/*
	 * @testName: testSetObject124
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Float_Tab with the maximum (true) value
	 * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject124() throws Exception {
		super.testSetObject124();
	}

	/*
	 * @testName: testSetObject125
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Float_Tab with the minimum (false) value
	 * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum (false) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject125() throws Exception {
		super.testSetObject125();
	}

	/*
	 * @testName: testSetObject126
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Double_Tab with the maximum (true) value
	 * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject126() throws Exception {
		super.testSetObject126();
	}

	/*
	 * @testName: testSetObject127
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Double_Tab with the minimum (false)
	 * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum (false) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject127() throws Exception {
		super.testSetObject127();
	}

	/*
	 * @testName: testSetObject128
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method,update the column Min_Val of Decimal_Tab with the maximum
	 * (true) value of Integer_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the maximum (true) value from the tssql.stmt
	 * file. Compare this value with the value returned by the getObject(int
	 * columnno) method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject128() throws Exception {
		super.testSetObject128();
	}

	/*
	 * @testName: testSetObject129
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method,update the column Null_Val of Decimal_Tab with the minimum
	 * (false) value of Integer_Tab. Call the getObject(int columnno) method to
	 * retrieve this value. Extract the minimum (false) value from the tssql.stmt
	 * file. Compare this value with the value returned by the getObject(int
	 * columnno) method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject129() throws Exception {
		super.testSetObject129();
	}

	/*
	 * @testName: testSetObject130
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method,update the column Min_Val of Numeric_Tab with the maximum
	 * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the maximum value from the tssql.stmt file. Compare this
	 * value with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject130() throws Exception {
		super.testSetObject130();
	}

	/*
	 * @testName: testSetObject131
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method,update the column Null_Val of Numeric_Tab with the minimum
	 * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum value from the tssql.stmt file. Compare this
	 * value with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject131() throws Exception {
		super.testSetObject131();
	}

	/*
	 * @testName: testSetObject134
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Char_Tab with the maximum value of
	 * Integer_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum (false) value from the tssql.stmt file. Compare this
	 * value with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject134() throws Exception {
		super.testSetObject134();
	}

	/*
	 * @testName: testSetObject135
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Char_Tab with the minimum value of
	 * Integer_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the null value from the tssql.stmt file. Compare this value with the
	 * value returned by the getObject(int columnno) method. Both the values should
	 * be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject135() throws Exception {
		super.testSetObject135();
	}

	/*
	 * @testName: testSetObject136
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Varchar_Tab with the maximum value of Integer_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject136() throws Exception {
		super.testSetObject136();
	}

	/*
	 * @testName: testSetObject137
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Varchar_Tab with the minimum value of
	 * Integer_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the null value from the tssql.stmt file. Compare this value with the
	 * value returned by the getObject(int columnno) method. Both the values should
	 * be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject137() throws Exception {
		super.testSetObject137();
	}

	/*
	 * @testName: testSetObject138
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the maximum value
	 * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject138() throws Exception {
		super.testSetObject138();
	}

	/*
	 * @testName: testSetObject139
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the minimum value
	 * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject139() throws Exception {
		super.testSetObject139();
	}

	/*
	 * @testName: testSetObject140
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Tinyint_Tab with the maximum value of
	 * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject140() throws Exception {
		super.testSetObject140();
	}

	/*
	 * @testName: testSetObject141
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Tinyint_Tab with the minimum value of
	 * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject141() throws Exception {
		super.testSetObject141();
	}

	/*
	 * @testName: testSetObject142
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val of Smallint_Tab with the maximum value of Smallint_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")

	public void testSetObject142() throws Exception {
		super.testSetObject142();
	}
}
