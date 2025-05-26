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
 * @(#)stmtClient1.java	1.23 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.stmt.stmt1;

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
 * The stmtClient1 class tests methods of Statement interface using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class stmtClient1AppClient extends stmtClient1 implements Serializable {
	private static final String testName = "jdbc.ee.stmt.stmt1";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "stmt1_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(stmtClient1.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = stmtClient1AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/stmt/stmt1/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = stmtClient1AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, stmtClient1AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "stmt1_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;

	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		stmtClient1AppClient theTests = new stmtClient1AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testClose
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:139; JDBC:JAVADOC:140;
	 * 
	 * @test_Strategy: Get a Statement object and call close() method and call
	 * executeQuery() method to check and it should throw SQLException
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testClose() throws Exception {
		super.testClose();
	}

	/*
	 * @testName: testExecute01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:163; JDBC:JAVADOC:164;
	 * 
	 * @test_Strategy: Call execute(String sql) of updating a row It should return a
	 * boolean value and the value should be equal to false
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testExecute01() throws Exception {
		super.testExecute01();
	}

	/*
	 * @testName: testExecute02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:163; JDBC:JAVADOC:164;
	 * 
	 * @test_Strategy: Get a Statement object and call execute(String sql) of
	 * selecting rows from the database It should return a boolean value and the
	 * value should be equal to true
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testExecute02() throws Exception {
		super.testExecute02();
	}

	/*
	 * @testName: testExecuteQuery01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
	 * 
	 * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
	 * select a row from the database It should return a ResultSet object
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testExecuteQuery01() throws Exception {
		super.testExecuteQuery01();
	}

	/*
	 * @testName: testExecuteQuery02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
	 * 
	 * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
	 * select a non-existent row from the database It should return a ResultSet
	 * object which is empty and call ResultSet.next() method to check and it should
	 * return a false
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testExecuteQuery02() throws Exception {
		super.testExecuteQuery02();
	}

	/*
	 * @testName: testExecuteQuery03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
	 * 
	 *
	 * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
	 * insert a row to the database It should throw SQLException
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testExecuteQuery03() throws Exception {
		super.testExecuteQuery03();
	}

	/*
	 * @testName: testExecuteUpdate01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:137; JDBC:JAVADOC:138;
	 * 
	 * @test_Strategy: Get a Statement object and call executeUpdate(String sql) It
	 * should return an int value which is equal to row count
	 */
	@Test
	@TargetVehicle("appclient")
	public void testExecuteUpdate01() throws Exception {
		super.testExecuteUpdate01();
	}

	/*
	 * @testName: testExecuteUpdate03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:137; JDBC:JAVADOC:138;
	 * 
	 * @test_Strategy: Get a Statement object and call executeUpdate(String sql) for
	 * selecting row from the table It should throw a SQL Exception
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testExecuteUpdate03() throws Exception {
		super.testExecuteUpdate03();
	}

	/*
	 * @testName: testGetFetchDirection
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:173; JDBC:JAVADOC:174;
	 * JDBC:JAVADOC:356;
	 * 
	 * @test_Strategy: Get a Statement object and call the getFetchDirection()
	 * method It should return a int value and the value should be equal to any of
	 * the values FETCH_FORWARD or FETCH_REVERSE or FETCH_UNKNOWN
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetFetchDirection() throws Exception {
		super.testGetFetchDirection();
	}

	/*
	 * @testName: testGetFetchSize
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:177; JDBC:JAVADOC:178;
	 * 
	 * @test_Strategy: Get a ResultSet object and call the getFetchSize() method It
	 * should return a int value
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetFetchSize() throws Exception {
		super.testGetFetchSize();
	}

	/*
	 * @testName: testGetMaxFieldSize
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:141; JDBC:JAVADOC:142;
	 * 
	 * @test_Strategy: Get a Statement object and call the getMaxFieldSize() method
	 * It should return a int value
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetMaxFieldSize() throws Exception {
		super.testGetMaxFieldSize();
	}

	/*
	 * @testName: testGetMaxRows
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:145; JDBC:JAVADOC:146;
	 * 
	 * @test_Strategy: Get a Statement object and call the getMaxRows() method It
	 * should return a int value
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetMaxRows() throws Exception {
		super.testGetMaxRows();
	}

	/*
	 * @testName: testGetMoreResults01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
	 * 
	 * @test_Strategy: Get a Statement object and call the execute() method for
	 * selecting a row and call getMoreResults() method It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetMoreResults01() throws Exception {
		super.testGetMoreResults01();
	}

	/*
	 * @testName: testGetMoreResults02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
	 * 
	 * @test_Strategy: Get a Statement object and call the execute() method for
	 * selecting a non-existent row and call getMoreResults() method It should
	 * return a boolean value and the value should be equal to false
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetMoreResults02() throws Exception {
		super.testGetMoreResults02();
	}

	/*
	 * @testName: testGetMoreResults03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
	 * 
	 * @test_Strategy: Get a Statement object and call the execute() method for
	 * updating a row and call getMoreResults() method It should return a boolean
	 * value and the value should be equal to false
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetMoreResults03() throws Exception {
		super.testGetMoreResults03();
	}

	/*
	 * @testName: testGetQueryTimeout
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:151; JDBC:JAVADOC:152;
	 * 
	 * @test_Strategy: Get a Statement object and call getQueryTimeout() method It
	 * should return a int value
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetQueryTimeout() throws Exception {
		super.testGetQueryTimeout();
	}

	/*
	 * @testName: testGetResultSet01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:165; JDBC:JAVADOC:166;
	 * 
	 * @test_Strategy: Get a Statement object and call execute() method for
	 * selecting a row and call getResultSet() method It should return a ResultSet
	 * object
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testGetResultSet01() throws Exception {
		super.testGetResultSet01();
	}

}
