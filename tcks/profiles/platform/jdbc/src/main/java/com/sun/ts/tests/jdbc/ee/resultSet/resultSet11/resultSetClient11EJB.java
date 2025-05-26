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
 * @(#)resultSetClient11.java	1.23 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet11;

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
 * The resultSetClient11 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-appclient")

public class resultSetClient11EJB extends resultSetClient11 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet11";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "resultSet11_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(resultSetClient11.class, ServiceEETest.class, EETest.class);

		URL resURL = resultSetClient11EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = resultSetClient11EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/resultSet/resultSet11/resultSet11_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "resultSet11_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(resultSetClient11.class, ServiceEETest.class, EETest.class);

		resURL = resultSetClient11EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/resultSet/resultSet11/resultSet11_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = resultSetClient11EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/resultSet/resultSet11/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "resultSet11_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient11EJB theTests = new resultSetClient11EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetByte22
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Decimal_Tab with the
	 * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
	 * value of Decimal_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the maximum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte22() throws Exception {
		super.testGetByte22();
	}

	/*
	 * @testName: testGetByte23
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the minimum value of table Decimal_Tab with the
	 * minimum value of table Tinyint_Tab.Now execute a query to get the maximum
	 * value of Decimal_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the minimum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte23() throws Exception {
		super.testGetByte23();
	}

	/*
	 * @testName: testGetByte24
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Double_Tab.Call the getByte(int columnIndex)
	 * method.Check if it returns the value zero.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte24() throws Exception {
		super.testGetByte24();
	}

	/*
	 * @testName: testGetByte25
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Numeric_Tab with the
	 * maximum value of table Tinyint_Tab.Now execute a query to get the maximum
	 * value of Numeric_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the maximum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testGetByte25() throws Exception {
		super.testGetByte25();
	}

	/*
	 * @testName: testGetByte26
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the minimum value of table Numeric_Tab with the
	 * minimum value of table Tinyint_Tab.Now execute a query to get the minimum
	 * value of Numeric_Tab table and retrieve the result of the query using the
	 * getByte(int columnIndex) method.Compare the returned value, with the minimum
	 * value of table Tinyint_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte26() throws Exception {
		super.testGetByte26();
	}

	/*
	 * @testName: testGetByte27
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Numeric_Tab.Call the getByte(int columnIndex)
	 * method.Check if it returns the value zero.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte27() throws Exception {
		super.testGetByte27();
	}

	/*
	 * @testName: testGetByte31
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the table Char_Tab with the maximum value of
	 * table Tinyint_Tab.Now execute a query to get that value from Char_Tab table
	 * and retrieve the result of the query using the getByte(int columnIndex)
	 * method.Compare the returned value, with the maximum value of table
	 * Tinyint_Tab extracted from the tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte31() throws Exception {
		super.testGetByte31();
	}

	/*
	 * @testName: testGetByte32
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the table Char_Tab with the minimum value of
	 * table Tinyint_Tab.Now execute a query to get that value from Char_Tab table
	 * and retrieve the result of the query using the getByte(int columnIndex)
	 * method.Compare the returned value, with the minimum value of table
	 * Tinyint_Tab extracted from the tssql.stmt file. Both of them should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testGetByte32() throws Exception {
		super.testGetByte32();
	}

	/*
	 * @testName: testGetByte33
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Char_Tab.Call the getByte(int columnIndex) method.Check
	 * if it returns the value zero.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testGetByte33() throws Exception {
		super.testGetByte33();
	}

	/*
	 * @testName: testGetByte34
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the table Varchar_Tab with the maximum value of
	 * table Tinyint_Tab.Now execute a query to get that value from Varchar_Tab
	 * table and retrieve the result of the query using the getByte(int columnIndex)
	 * method.Compare the returned value, with the maximum value of table
	 * Tinyint_Tab extracted from the tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte34() throws Exception {
		super.testGetByte34();
	}

	/*
	 * @testName: testGetByte35
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the table Varchar_Tab with the minimum value of
	 * table Tinyint_Tab.Now execute a query to get that value from Varchar_Tab
	 * table and retrieve the result of the query using the getByte(int columnIndex)
	 * method.Compare the returned value, with the minimum value of table
	 * Tinyint_Tab extracted from the tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetByte35() throws Exception {
		super.testGetByte35();
	}

	/*
	 * @testName: testGetByte36
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:376;
	 * JDBC:JAVADOC:377; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Varchar_Tab.Call the getByte(int columnIndex)
	 * method.Check if it returns the value zero.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testGetByte36() throws Exception {
		super.testGetByte36();
	}
}
