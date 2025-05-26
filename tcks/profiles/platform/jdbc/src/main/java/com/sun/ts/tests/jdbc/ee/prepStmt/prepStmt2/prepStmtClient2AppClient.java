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
 * @(#)prepStmtClient2.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt2;

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
 * The prepStmtClient2 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

@Tag("tck-appclient")

public class prepStmtClient2AppClient extends prepStmtClient2 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt2";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "prepStmt2_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient2.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = prepStmtClient2AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt2/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = prepStmtClient2AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}
		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, prepStmtClient2AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt2_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient2AppClient theTests = new prepStmtClient2AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetInt02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:664; JDBC:JAVADOC:665;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database Using setInt(int parameterIndex,int x),update the column with the
	 * maximum value of Integer_Tab. Now execute a query to get the maximum value
	 * and retrieve the result of the query using the getInt(int columnIndex) method
	 * Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetInt02() throws Exception {
		super.testSetInt02();
	}

	/*
	 * @testName: testSetDate01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:678; JDBC:JAVADOC:679;
	 * JDBC:JAVADOC:392; JDBC:JAVADOC:393; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database execute the precompiled SQL Statement by calling setDate(int
	 * parameterIndex,Date x) method and call the ResultSet.getDate(int) method to
	 * check and it should return a String Value that it is been set
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetDate01() throws Exception {
		super.testSetDate01();
	}

	/*
	 * @testName: testSetDate02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:714; JDBC:JAVADOC:715;
	 * JDBC:JAVADOC:612; JDBC:JAVADOC:613; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database execute the precompiled SQL Statement by calling setDate(int
	 * parameterIndex,Date x,Calendar cal) method and call the
	 * ResultSet.getDate(int) method to check and it should return a String Value
	 * that it is been set
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetDate02() throws Exception {
		super.testSetDate02();
	}

	/*
	 * @testName: testSetDouble01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:670; JDBC:JAVADOC:671;
	 * JDBC:JAVADOC:386; JDBC:JAVADOC:387; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database Using setDouble(int parameterIndex,double x),update the column the
	 * minimum value of Double_Tab. Now execute a query to get the minimum value and
	 * retrieve the result of the query using the getDouble(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetDouble01() throws Exception {
		super.testSetDouble01();
	}

	/*
	 * @testName: testSetDouble02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:678; JDBC:JAVADOC:679;
	 * JDBC:JAVADOC:386; JDBC:JAVADOC:387; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using setDouble(int parameterIndex,double x),update the column the
	 * maximum value of Double_Tab. Now execute a query to get the maximum value and
	 * retrieve the result of the query using the getDouble(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetDouble02() throws Exception {
		super.testSetDouble02();
	}

	/*
	 * @testName: testSetLong01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:666; JDBC:JAVADOC:667;
	 * JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using setLong(int parameterIndex,long x),update the column the
	 * minimum value of Bigint_Tab. Now execute a query to get the minimum value and
	 * retrieve the result of the query using the getLong(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetLong01() throws Exception {
		super.testSetLong01();
	}

	/*
	 * @testName: testSetLong02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:666; JDBC:JAVADOC:667;
	 * JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using setLong(int parameterIndex,long x),update the column the
	 * maximum value of Bigint_Tab. Now execute a query to get the maximum value and
	 * retrieve the result of the query using the getLong(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetLong02() throws Exception {
		super.testSetLong02();
	}

	/*
	 * @testName: testSetShort01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:662; JDBC:JAVADOC:663;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using setShort(int parameterIndex,short x),update the column the
	 * minimum value of Smallint_Tab. Now execute a query to get the minimum value
	 * and retrieve the result of the query using the getShort(int columnIndex)
	 * method.Compare the returned value, with the minimum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetShort01() throws Exception {
		super.testSetShort01();
	}

	/*
	 * @testName: testSetShort02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:662; JDBC:JAVADOC:663;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using setShort(int parameterIndex,short x),update the column the
	 * maximum value of Smallint_Tab. Now execute a query to get the maximum value
	 * and retrieve the result of the query using the getShort(int columnIndex)
	 * method.Compare the returned value, with the maximum value extracted from the
	 * tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetShort02() throws Exception {
		super.testSetShort02();
	}

	/*
	 * @testName: testSetNull01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:4; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for INTEGER Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 *
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull01() throws Exception {
		super.testSetNull01();
	}

	/*
	 * @testName: testSetNull02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:6; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for FLOAT Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull02() throws Exception {
		super.testSetNull02();
	}

	/*
	 * @testName: testSetNull03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:3; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for SMALLINT Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull03() throws Exception {
		super.testSetNull03();
	}

	/*
	 * @testName: testSetNull04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:10; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for CHAR Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull04() throws Exception {
		super.testSetNull04();
	}

	/*
	 * @testName: testSetNull05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:15; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for TIME Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull05() throws Exception {
		super.testSetNull05();
	}

	/*
	 * @testName: testSetNull06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:16; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for TIMESTAMP Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull06() throws Exception {
		super.testSetNull06();
	}

	/*
	 * @testName: testSetNull07
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:14; JDBC:JAVADOC:392; JDBC:JAVADOC:393; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for DATE Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull07() throws Exception {
		super.testSetNull07();
	}

	/*
	 * @testName: testSetNull08
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:9; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for NUMERIC Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull08() throws Exception {
		super.testSetNull08();
	}

	/*
	 * @testName: testSetNull09
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:2; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for TINYINT Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull09() throws Exception {
		super.testSetNull09();
	}

	/*
	 * @testName: testSetNull10
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:8; JDBC:JAVADOC:386; JDBC:JAVADOC:387; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for DOUBLE Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull10() throws Exception {
		super.testSetNull10();
	}

	/*
	 * @testName: testSetNull11
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
	 * JDBC:JAVADOC:5; JDBC:JAVADOC:382; JDBC:JAVADOC:383; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. execute the precompiled SQL Statement to set the value as SQL Null
	 * for BIGINT Type and retrieve the same value by executing a query. Call the
	 * ResultSet.wasNull() method to check it. It should return a true value.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testSetNull11() throws Exception {
		super.testSetNull11();
	}
}
