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
 * @(#)resultSetClient10.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet10;

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
 * The resultSetClient10 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-appclient")

public class resultSetClient10AppClient extends resultSetClient10 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet10";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "resultSet10_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(resultSetClient10.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = resultSetClient10AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/resultSet/resultSet10/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = resultSetClient10AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, resultSetClient10AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "resultSet10_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient10AppClient theTests = new resultSetClient10AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetByte01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that gets the
	 * maximum value of table Tinyint_Tab.Call the getByte(int columnIndex) method.
	 * Compare the returned result with the value extracted from tssql.stmt
	 * file.Both of them should be equal and the returned result must be equal to
	 * the Maximum Value of JDBC Tinyint datatype.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte01() throws Exception {
		super.testGetByte01();
	}

	/*
	 * @testName: testGetByte02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that gets the
	 * minimum value of table Tinyint_Tab.Call the getByte(int columnIndex) method.
	 * Compare the returned result with the value extracted from tssql.stmt
	 * file.Both of them should be equal and the returned result must be equal to
	 * the minimum Value of JDBC Tinyint datatype.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte02() throws Exception {
		super.testGetByte02();
	}

	/*
	 * @testName: testGetByte03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Tinyint_Tab.Call the getByte(int columnIndex)
	 * method.Check if it returns the value zero.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte03() throws Exception {
		super.testGetByte03();
	}

	/*
	 * @testName: testGetByte76
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:408;
	 * JDBC:JAVADOC:409; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that gets the
	 * maximum value of table Tinyint_Tab.Call the getByte(String columnName)
	 * method. Compare the returned result with the value extracted from tssql.stmt
	 * file.Both of them should be equal and the returned result must be equal to
	 * the Maximum Value of JDBC Tinyint datatype.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte76() throws Exception {
		super.testGetByte76();
	}

	/*
	 * @testName: testGetByte77
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:408;
	 * JDBC:JAVADOC:409; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that gets the
	 * minimum value of table Tinyint_Tab.Call the getByte(String columnName)
	 * method. Compare the returned result with the value extracted from tssql.stmt
	 * file.Both of them should be equal and the returned result must be equal to
	 * the minimum Value of JDBC Tinyint datatype.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte77() throws Exception {
		super.testGetByte77();
	}

	/*
	 * @testName: testGetByte78
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:408;
	 * JDBC:JAVADOC:409; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Tinyint_Tab.Call the getByte(String columnName)
	 * method.Check if it returns the value zero.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte78() throws Exception {
		super.testGetByte78();
	}

	/*
	 * @testName: testGetByte04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Smallint_Tab with the
	 * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
	 * value of Smallint_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the maximum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte04() throws Exception {
		super.testGetByte04();
	}

	/*
	 * @testName: testGetByte05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the minimum value of table Smallint_Tab with the
	 * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
	 * value of Smallint_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the minimum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testGetByte05() throws Exception {
		super.testGetByte05();
	}

	/*
	 * @testName: testGetByte06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Smallint_Tab.Call the getByte(int columnIndex)
	 * method.Check if it returns the value zero.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte06() throws Exception {
		super.testGetByte06();
	}

	/*
	 * @testName: testGetByte07
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Integer_Tab with the
	 * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
	 * value of Integer_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the maximum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testGetByte07() throws Exception {
		super.testGetByte07();
	}

	/*
	 * @testName: testGetByte08
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the minimum value of table Integer_Tab with the
	 * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
	 * value of Integer_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the minimum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte08() throws Exception {
		super.testGetByte08();
	}

	/*
	 * @testName: testGetByte09
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Integer_Tab.Call the getByte(int columnIndex)
	 * method.Check if it returns the value zero.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte09() throws Exception {
		super.testGetByte09();
	}

	/*
	 * @testName: testGetByte13
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Real_Tab with the
	 * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
	 * value of Real_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the maximum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */

	@Test
	@TargetVehicle("appclient")
	public void testGetByte13() throws Exception {
		super.testGetByte13();
	}

	/*
	 * @testName: testGetByte14
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the minimum value of table Real_Tab with the
	 * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
	 * value of Real_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the minimum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte14() throws Exception {
		super.testGetByte14();
	}

	/*
	 * @testName: testGetByte15
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Real_Tab.Call the getByte(int columnIndex) method.Check
	 * if it returns the value zero.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte15() throws Exception {
		super.testGetByte15();
	}

	/*
	 * @testName: testGetByte16
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Float_Tab with the
	 * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
	 * value of Float_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the maximum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte16() throws Exception {
		super.testGetByte16();
	}

	/*
	 * @testName: testGetByte17
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the minimum value of table Float_Tab with the
	 * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
	 * value of Float_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the minimum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte17() throws Exception {
		super.testGetByte17();
	}

	/*
	 * @testName: testGetByte18
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Float_Tab.Call the getByte(int columnIndex)
	 * method.Check if it returns the value zero.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetByte18() throws Exception {
		super.testGetByte18();
	}
}
