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

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt3;

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
 * The prepStmtClient3 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

@Tag("tck-appclient")

public class prepStmtClient3EJB extends prepStmtClient3 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt3";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "prepStmt3_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(prepStmtClient3.class, ServiceEETest.class, EETest.class);

		URL resURL = prepStmtClient3EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = prepStmtClient3EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt3/prepStmt3_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "prepStmt3_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(prepStmtClient3.class, ServiceEETest.class, EETest.class);

		resURL = prepStmtClient3EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt3/prepStmt3_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = prepStmtClient3EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt3/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt3_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient3EJB theTests = new prepStmtClient3EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetString01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
	 * JDBC:JAVADOC:372; JDBC:JAVADOC:373; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setString(int parameterIndex, String x) method,update the
	 * column value with the maximum value of Char_Tab. Call the getString(String
	 * columnName) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getString(String columnName) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetString01() throws Exception {
		super.testSetString01();
	}

	/*
	 * @testName: testSetTime01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:680; JDBC:JAVADOC:681;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setTime(int parameterIndex, Time x) method,update the
	 * column value with the Non-Null Time value. Call the getTime(int columnno)
	 * method to retrieve this value. Extract the Time value from the tssql.stmt
	 * file. Compare this value with the value returned by the getTime(int columnno)
	 * method. Both the values should be equal
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetTime01() throws Exception {
		super.testSetTime01();
	}

	/*
	 * @testName: testSetTime02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:716; JDBC:JAVADOC:717;
	 * JDBC:JAVADOC:616; JDBC:JAVADOC:617; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setTime(int parameterIndex, Time x, Calendar cal)
	 * method,update the column value with the Non-Null Time value using the
	 * Calendar Object. Call the getTime(int columnno) method to retrieve this
	 * value. Extract the Time value from the tssql.stmt file. Compare this value
	 * with the value returned by the getTime(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetTime02() throws Exception {
		super.testSetTime02();
	}

	/*
	 * @testName: testSetTimestamp01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:682; JDBC:JAVADOC:683;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setTimestamp(int parameterIndex, Timestamp x)
	 * method,update the column value with the Non-Null Timestamp value. Call the
	 * getTimestamp(int columnno) method to retrieve this value. Extract the
	 * Timestamp value from the tssql.stmt file. Compare this value with the value
	 * returned by the getTimestamp(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetTimestamp01() throws Exception {
		super.testSetTimestamp01();
	}

	/*
	 * @testName: testSetTimestamp02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:718; JDBC:JAVADOC:719;
	 * JDBC:JAVADOC:620; JDBC:JAVADOC:621; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setTimestamp(int parameterIndex, Time x, Calendar cal)
	 * method,update the column value with the Non-Null Timestamp value using the
	 * Calendar Object. Call the getTimestamp(int columnno) method to retrieve this
	 * value. Extract the Timestamp value from the tssql.stmt file. Compare this
	 * value with the value returned by the getTimestamp(int columnno) method. Both
	 * the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetTimestamp02() throws Exception {
		super.testSetTimestamp02();
	}

	/*
	 * @testName: testSetString02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
	 * JDBC:JAVADOC:372; JDBC:JAVADOC:373; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using setString(int parameterIndex, String x),update the column
	 * with the maximum value which is a SQL VARCHAR. Call the getString(int
	 * ColumnIndex) method to retrieve this value. Extract the maximum value as a
	 * String from the tssql.stmt file. Compare this value with the value returned
	 * by the getString method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetString02() throws Exception {
		super.testSetString02();
	}

	/*
	 * @testName: testSetFloat01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:668; JDBC:JAVADOC:669;
	 * JDBC:JAVADOC:384; JDBC:JAVADOC:385; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using setFloat(int parameterIndex,float x),update the column with
	 * the minimum value of Real_Tab. Now execute a query to get the minimum value
	 * and retrieve the result of the query using the getFloat(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetFloat01() throws Exception {
		super.testSetFloat01();
	}

	/*
	 * @testName: testSetFloat02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
	 * JDBC:JAVADOC:384; JDBC:JAVADOC:385; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using setFloat(int parameterIndex,float x),update the column with
	 * the maximum value of Real_Tab. Now execute a query to get the maximum value
	 * and retrieve the result of the query using the getFloat(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetFloat02() throws Exception {
		super.testSetFloat02();
	}

	/*
	 * @testName: testSetBytes01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement for updating Non-Null value
	 * from Binary_Tab by calling setBytes(int parameterIndex, byte[] x) and call
	 * the getBytes(int) method to check and it should return a Byte Array
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetBytes01() throws Exception {
		super.testSetBytes01();
	}

	/*
	 * @testName: testSetBytes02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement for updating Non-Null value
	 * from Varbinary_Tab by calling setBytes(int parameterIndex, byte[] x) and call
	 * the getBytes(int) method to check and it should return a Byte Array
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetBytes02() throws Exception {
		super.testSetBytes02();
	}

	/*
	 * @testName: testSetBytes03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement for updating Non-Null value
	 * from Longvarbinary_Tab by calling setBytes(int parameterIndex, byte[] x) and
	 * call the getBytes(int) method to check and it should return a Byte Array
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetBytes03() throws Exception {
		super.testSetBytes03();
	}

	/*
	 * @testName: testSetObject01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val with the maximum value of Char_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject01() throws Exception {
		super.testSetObject01();
	}

	/*
	 * @testName: testSetObject02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_val with the maximum value of Varchar_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject02() throws Exception {
		super.testSetObject02();
	}

	/*
	 * @testName: testSetObject03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_val with the maximum value of Longvarchar_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the maximum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject03() throws Exception {
		super.testSetObject03();
	}

	/*
	 * @testName: testSetObject04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_val with the maximum value of Numeric_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject04() throws Exception {
		super.testSetObject04();
	}

	/*
	 * @testName: testSetObject05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_val with the minimum value of Numeric_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the minimum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject05() throws Exception {
		super.testSetObject05();
	}

	/*
	 * @testName: testSetObject06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_val with the maximum value of Decimal_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject06() throws Exception {
		super.testSetObject06();
	}

	/*
	 * @testName: testSetObject07
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_val with the minimum value of Decimal_Tab. Call the getObject(int
	 * columnno) method to retrieve this value. Extract the minimum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getObject(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject07() throws Exception {
		super.testSetObject07();
	}

	/*
	 * @testName: testSetObject08
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JDBC:JAVADOC:374; JDBC:JAVADOC:375; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_val with the maximum value of Bit_Tab. Call the getBoolean(int
	 * columnno) method to retrieve this value. Extract the maximum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getBoolean(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject08() throws Exception {
		super.testSetObject08();
	}

	/*
	 * @testName: testSetObject09
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
	 * JDBC:JAVADOC:374; JDBC:JAVADOC:375; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_val with the minimum value of Bit_Tab. Call the getBoolean(int
	 * columnno) method to retrieve this value. Extract the minimum value from the
	 * tssql.stmt file. Compare this value with the value returned by the
	 * getBoolean(int columnNo) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject09() throws Exception {
		super.testSetObject09();
	}
}
