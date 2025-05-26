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

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt1;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The prepStmtClient1 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 */

@Tag("tck-javatest")
@Tag("web")
public class prepStmtClient1Servlet extends prepStmtClient1 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt1";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "prepStmt1_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient1.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = prepStmtClient1Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
// The sun servlet descriptor
		URL sunServletUrl = prepStmtClient1Servlet.class.getResource("prepStmt1_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
// Call the archive processor
		archiveProcessor.processWebArchive(archive, prepStmtClient1Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient1Servlet theTests = new prepStmtClient1Servlet();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetMetaData
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:712;
	 * JDBC:JAVADOC:713; JDBC:JAVADOC:1143; JDBC:JAVADOC:1144; JavaEE:SPEC:186;
	 * JavaEE:SPEC:186.2;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the getMetaData() method and get the number of columns
	 * using getColumnCount() method of ResultSetMetaData.Execute a query using
	 * executeQuery() method and get the number of columns. Both the values should
	 * be equal or it should throw an SQL exception.
	 * 
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetMetaData() throws Exception {
		super.testGetMetaData();
	}

	/*
	 * @testName: testClearParameters
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:690;
	 * JDBC:JAVADOC:691; JDBC:JAVADOC:1143; JDBC:JAVADOC:1144; JavaEE:SPEC:186;
	 * 
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Set the value for the IN parameter of the Prepared Statement
	 * object. Call the clearParameters() method.Call the executeQuery() method to
	 * check if the call to clearParameters() clears the IN parameter set by the
	 * Prepared Statement object.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testClearParameters() throws Exception {
		super.testClearParameters();
	}

	/*
	 * @testName: testExecute01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:698; JDBC:JAVADOC:699;
	 * JDBC:JAVADOC:1143; JDBC:JAVADOC:1144; JavaEE:SPEC:182; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Set the value for the IN parameter of the Prepared Statement
	 * object. Execute the precompiled SQL Statement of deleting a row. It should
	 * return a boolean value and the value should be equal to false. (See JDK 1.2.2
	 * API documentation)
	 *
	 */
	@Test
	@TargetVehicle("servlet")

	public void testExecute01() throws Exception {
		super.testExecute01();
	}

	/*
	 * @testName: testExecute03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:698; JDBC:JAVADOC:699;
	 * JDBC:JAVADOC:1143; JDBC:JAVADOC:1144; JavaEE:SPEC:182; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled SQL Statement by calling execute() method
	 * without setting the parameters.An SQL Exception must be thrown. (See JDK
	 * 1.2.2 API documentation)
	 *
	 */
	@Test
	@TargetVehicle("servlet")

	public void testExecute03() throws Exception {
		super.testExecute03();
	}

	/*
	 * @testName: testExecuteQuery01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:652; JDBC:JAVADOC:653;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Set the value for the IN parameter of the Prepared Statement
	 * object. Execute the precompiled SQL Statement by calling executeQuery()
	 * method. It should return a ResultSet object.
	 * 
	 */
	@Test
	@TargetVehicle("servlet")

	public void testExecuteQuery01() throws Exception {
		super.testExecuteQuery01();
	}

	/*
	 * @testName: testExecuteQuery02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:652; JDBC:JAVADOC:653;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Set the value for the IN parameter of the Prepared Statement
	 * object. Execute the precompiled SQL Statement by calling executeQuery()
	 * method with a non existent row. A call to ResultSet.next() should return a
	 * false value.
	 */
	@Test
	@TargetVehicle("servlet")

	public void testExecuteQuery02() throws Exception {
		super.testExecuteQuery02();
	}

	/*
	 * @testName: testExecuteQuery03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:652; JDBC:JAVADOC:653;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled SQL Statement by calling executeQuery()
	 * method without setting the parameters. It should throw a SQL Exception.
	 */
	@Test
	@TargetVehicle("servlet")

	public void testExecuteQuery03() throws Exception {
		super.testExecuteQuery03();
	}

	/*
	 * @testName: testExecuteUpdate01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:654; JDBC:JAVADOC:655;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Set the value for the IN parameter of the PreparedStatement object.
	 * Execute the precompiled SQL Statement by calling executeUpdate() method. It
	 * should return an integer value indicating the number of rows that were
	 * affected. (The value could be zero if zero rows are affected).
	 */
	@Test
	@TargetVehicle("servlet")

	public void testExecuteUpdate01() throws Exception {
		super.testExecuteUpdate01();
	}

	/*
	 * @testName: testExecuteUpdate02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:654; JDBC:JAVADOC:655;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Set the value for the IN parameter of the Prepared Statement
	 * object. Execute the precompiled SQL Statement by calling executeUpdate()
	 * method with a non existent row. It should return an Integer value.
	 */
	@Test
	@TargetVehicle("servlet")

	public void testExecuteUpdate02() throws Exception {
		super.testExecuteUpdate02();
	}

	/*
	 * @testName: testExecuteUpdate03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:654; JDBC:JAVADOC:655;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled SQL Statement without setting the IN
	 * parameter. It should throw an SQL exception.
	 */
	@Test
	@TargetVehicle("servlet")

	public void testExecuteUpdate03() throws Exception {
		super.testExecuteUpdate03();
	}

	/*
	 * @testName: testSetBigDecimal01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:672; JDBC:JAVADOC:673;
	 * JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled.SQL Statement by calling the
	 * setBigDecimal(int parameterindex, BigDecimal x) method for updating the value
	 * of column MIN_VAL in Numeric_Tab.Check first the return value of
	 * executeUpdate() method used is equal to 1. Call the
	 * ResultSet.getBigDecimal(int columnIndex)method. Check if returns the
	 * BigDecimal Value that has been set.
	 */
	@Test
	@TargetVehicle("servlet")

	public void testSetBigDecimal01() throws Exception {
		super.testSetBigDecimal01();
	}

	/*
	 * @testName: testSetBigDecimal02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:672; JDBC:JAVADOC:673;
	 * JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled SQL Statement by calling the
	 * setBigDecimal(int parameterindex, BigDecimal x) method for updating the value
	 * of column NULL_VAL in Numeric_Tab. Call the ResultSet.getBigDecimal(int
	 * columnIndex) method. Check if returns the BigDecimal Value that has been set.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetBigDecimal02() throws Exception {
		super.testSetBigDecimal02();
	}

	/*
	 * @testName: testSetBoolean01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:658; JDBC:JAVADOC:659;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled SQL Statement by calling the setBoolean(int
	 * parameterIndex, boolean x) to set MAX_VAL column of Bit_tab with the MIN_VAL
	 * of Bit_Tab. Call the ResultSet.getBoolean(int columnIndex) method to check if
	 * it returns the boolean Value that has been set.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetBoolean01() throws Exception {
		super.testSetBoolean01();
	}

	/*
	 * @testName: testSetBoolean02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:658; JDBC:JAVADOC:659;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled SQL Statement by calling the method
	 * setBoolean(int parameterIndex, boolean x) to set NULL_VAL column of Bit_tab
	 * with the Max_Val of Bit_Tab. Call the ResultSet.getBoolean(int columnIndex)
	 * method to check if it returns the boolean Value that has been set.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetBoolean02() throws Exception {
		super.testSetBoolean02();
	}

	/*
	 * @testName: testSetByte01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:660; JDBC:JAVADOC:661;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled SQL Statement by calling the setByte(int
	 * parameterindex, byte x) method for updating MAX_VAL column of Tinyint_Tab.
	 * Call the ResultSet.getByte(int columnIndex) method to check if it returns the
	 * byte Value that has been set.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetByte01() throws Exception {
		super.testSetByte01();
	}

	/*
	 * @testName: testSetByte02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:660; JDBC:JAVADOC:661;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the precompiled SQL Statement by calling the setByte(int
	 * parameterindex, byte x) method for updating NULL_VAL column of Tinyint_Tab.
	 * Call the ResultSet.getByte(int columnIndex) method,to check if it returns the
	 * byte Value that has been set.
	 */
	@Test
	@TargetVehicle("servlet")

	public void testSetByte02() throws Exception {
		super.testSetByte02();
	}

	/*
	 * @testName: testSetFloat01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:668; JDBC:JAVADOC:669;
	 * JavaEE:SPEC:186;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database execute the precompiled SQL Statement by calling the setFloat(int
	 * parameterindex, float x) method for updating the MAX_VAL column of Float_Tab.
	 * Call the ResultSet.getFloat(int columnIndex) method to check if it returns
	 * the float Value that has been set.
	 *
	 */
	@Test
	@TargetVehicle("servlet")

	public void testSetFloat01() throws Exception {
		super.testSetFloat01();
	}

	/*
	 * @testName: testSetFloat02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:668; JDBC:JAVADOC:669;
	 * JavaEE:SPEC:186;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database execute the precompiled SQL Statement by calling the setFloat(int
	 * parameterindex, float x) method for updating the NULL_VAL column of
	 * Float_Tab. Call the ResultSet.getFloat(int columnIndex) method to check if it
	 * returns the float Value that has been set.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetFloat02() throws Exception {
		super.testSetFloat02();
	}

	/*
	 * @testName: testSetInt01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:664; JDBC:JAVADOC:665;
	 * JavaEE:SPEC:186;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database execute the precompiled SQL Statement by calling the setInt(int
	 * parameterindex, int x) method for updating the MAX_VAL column of Integer_Tab.
	 * Call the ResultSet.getInt(int columnIndex) method to check if it returns the
	 * integer Value that has been set.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSetInt01() throws Exception {
		super.testSetInt01();
	}
}
