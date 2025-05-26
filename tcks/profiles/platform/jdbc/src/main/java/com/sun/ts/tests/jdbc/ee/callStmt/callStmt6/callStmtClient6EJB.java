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
 * @(#)callStmtClient6.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt6;

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
 * The callStmtClient6 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class callStmtClient6EJB extends callStmtClient6 implements Serializable {

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentEJB(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt6_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(callStmtClient6.class, ServiceEETest.class, EETest.class);

		URL resURL = callStmtClient6EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = callStmtClient6EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt6/callStmt6_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt6_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient6.class, ServiceEETest.class, EETest.class);

		resURL = callStmtClient6EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt6/callStmt6_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = callStmtClient6EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt6/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt6_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	private static final String testName = "jdbc.ee.callStmt.callStmt6";

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient6EJB theTests = new callStmtClient6EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetString01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
	 * JDBC:JAVADOC:675; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setChar(int parameterIndex,String x),update the column
	 * minimum value of Char_Tab. Now execute a query to get the minimum value and
	 * retrieve the result of the query using the getString(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetString01() throws Exception {
		super.testSetString01();
	}

	/*
	 * @testName: testSetString02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
	 * JDBC:JAVADOC:675; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setString(int parameterIndex,String x),update the column the
	 * maximum value of Char_Tab. Now execute a query to get the maximum value and
	 * retrieve the result of the query using the getString(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetString02() throws Exception {
		super.testSetString02();
	}

	/*
	 * @testName: testSetString03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
	 * JDBC:JAVADOC:675; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setString(int parameterIndex,String x),update the column the
	 * minimum value of Varchar_Tab. Now execute a query to get the minimum value
	 * and retrieve the result of the query using the getString(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetString03() throws Exception {
		super.testSetString03();
	}

	/*
	 * @testName: testSetString04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
	 * JDBC:JAVADOC:675; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setString(int parameterIndex,String x),update the column the
	 * maximum value of Varchar_Tab. Now execute a query to get the maximum value
	 * and retrieve the result of the query using the getInt(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetString04() throws Exception {
		super.testSetString04();
	}

	/*
	 * @testName: testSetString05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
	 * JDBC:JAVADOC:675; JDBC:JAVADOC:398; JDBC:JAVADOC:399; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setString(int parameterIndex,String x),update the column the
	 * minimum value of Longvarchar_Tab. Now execute a query to get the minimum
	 * value and retrieve the result of the query using the getString(int
	 * columnIndex) method.Compare the returned value, with the minimum value
	 * extracted from the tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetString05() throws Exception {
		super.testSetString05();
	}

	/*
	 * @testName: testSetString06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:674;
	 * JDBC:JAVADOC:675; JDBC:JAVADOC:398; JDBC:JAVADOC:399; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setString(int parameterIndex,String x),update the column the
	 * maximum value of Longvarchar_Tab. Now execute a query to get the maximum
	 * value and retrieve the result of the query using the getInt(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetString06() throws Exception {
		super.testSetString06();
	}

	/*
	 * @testName: testSetBigDecimal01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:672;
	 * JDBC:JAVADOC:673; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database Using the IN parameter of that object,,update
	 * the column Max_Val of Decimal_Tab with the minimum value.Execute a query to
	 * get the minimum value and retrieve the result of the query using the
	 * getBigDecimal(int parameterIndex) method.Compare the returned value with the
	 * minimum value extracted from the tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetBigDecimal01() throws Exception {
		super.testSetBigDecimal01();
	}

	/*
	 * @testName: testSetBigDecimal02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:672;
	 * JDBC:JAVADOC:673; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of Decimal_Tab with the maximum value.Execute a query to
	 * get the maximum value and retrieve the result of the query using the
	 * getBigDecimal(int parameterIndex) method.Compare the returned value with the
	 * maximum value extracted from the tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetBigDecimal02() throws Exception {
		super.testSetBigDecimal02();
	}

	/*
	 * @testName: testSetBigDecimal03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:672;
	 * JDBC:JAVADOC:673; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database Using the IN parameter of that object,,update
	 * the column Max_Val of Numeric_Tab with the minimum value.Execute a query to
	 * get the minimum value and retrieve the result of the query using the
	 * getBigDecimal(int parameterIndex) method.Compare the returned value with the
	 * minimum value extracted from the tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetBigDecimal03() throws Exception {
		super.testSetBigDecimal03();
	}

	/*
	 * @testName: testSetBigDecimal04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:672;
	 * JDBC:JAVADOC:673; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of Numeric_Tab with the maximum value.Execute a query to
	 * get the maximum value and retrieve the result of the query using the
	 * getBigDecimal(int parameterIndex) method.Compare the returned value with the
	 * maximum value extracted from the tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetBigDecimal04() throws Exception {
		super.testSetBigDecimal04();
	}

	/*
	 * @testName: testSetBoolean01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:658;
	 * JDBC:JAVADOC:659; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database Using the IN parameter of that object,update
	 * the column Max_Val of Bit_Tab with the minimum value.Execute a query to get
	 * the minimum value and retrieve the result of the query using the
	 * getBoolean(int parameterIndex) method.Compare the returned value with the
	 * minimum value extracted from the tssql.stmt file.Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetBoolean01() throws Exception {
		super.testSetBoolean01();
	}

	/*
	 * @testName: testSetBoolean02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:658;
	 * JDBC:JAVADOC:659; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database Using the IN parameter of that object,update
	 * the column Null_Val of Bit_Tab with the maximum value.Execute a query to get
	 * the maximum value and retrieve the result of the query using the
	 * getBoolean(int parameterIndex) method.Compare the returned value with the
	 * maximum value extracted from the with tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetBoolean02() throws Exception {
		super.testSetBoolean02();
	}

	/*
	 * @testName: testSetByte01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:660;
	 * JDBC:JAVADOC:661; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database Using the IN parameter of that object,update
	 * the column Max_Val of Tinyint_Tab with the minimum value.Execute a query to
	 * get the minimum value and retrieve the result of the query using the
	 * getByte(int parameterIndex) method.Compare the returned value with the
	 * minimum value extracted from the tssql.stmt file.Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetByte01() throws Exception {
		super.testSetByte01();
	}

	/*
	 * @testName: testSetByte02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:660;
	 * JDBC:JAVADOC:661; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database Using the IN parameter of that object,update
	 * the column Max_Val of Tinyint_Tab with the minimum value.Execute a query to
	 * get the minimum value and retrieve the result of the query using the
	 * getByte(int parameterIndex) method.Compare the returned value with the
	 * minimum value extracted from the tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetByte02() throws Exception {
		super.testSetByte02();
	}

	/*
	 * @testName: testSetShort01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:662;
	 * JDBC:JAVADOC:663; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setShort(int parameterIndex,short x),update the column the
	 * minimum value of Smallint_Tab. Now execute a query to get the minimum value
	 * and retrieve the result of the query using the getShort(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetShort01() throws Exception {
		super.testSetShort01();
	}

	/*
	 * @testName: testSetShort02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:662;
	 * JDBC:JAVADOC:663; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setShort(int parameterIndex,short x),update the column the
	 * maximum value of Smallint_Tab. Now execute a query to get the maximum value
	 * and retrieve the result of the query using the getShort(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetShort02() throws Exception {
		super.testSetShort02();
	}

	/*
	 * @testName: testSetInt01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:664;
	 * JDBC:JAVADOC:665; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setInt(int parameterIndex,int x),update the column the
	 * minimum value of Integer_Tab. Now execute a query to get the minimum value
	 * and retrieve the result of the query using the getInt(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetInt01() throws Exception {
		super.testSetInt01();
	}

	/*
	 * @testName: testSetInt02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:664;
	 * JDBC:JAVADOC:665; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setInt(int parameterIndex,int x),update the column the
	 * maximum value of Integer_Tab. Now execute a query to get the maximum value
	 * and retrieve the result of the query using the getInt(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetInt02() throws Exception {
		super.testSetInt02();
	}

	/*
	 * @testName: testSetLong01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:666;
	 * JDBC:JAVADOC:667; JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setLong(int parameterIndex,long x),update the column the
	 * minimum value of Bigint_Tab. Now execute a query to get the minimum value and
	 * retrieve the result of the query using the getInt(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetLong01() throws Exception {
		super.testSetLong01();
	}

	/*
	 * @testName: testSetLong02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:666;
	 * JDBC:JAVADOC:667; JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. Using setLong(int parameterIndex,long x),update the column the
	 * maximum value of Bigint_Tab. Now execute a query to get the maximum value and
	 * retrieve the result of the query using the getInt(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetLong02() throws Exception {
		super.testSetLong02();
	}

}
