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
 * The resultSetClient11 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-javatest")
@Tag("web")
public class resultSetClient11Servlet extends resultSetClient11 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet11";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet11_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(resultSetClient11.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = resultSetClient11Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
// The sun servlet descriptor
		URL sunServletUrl = resultSetClient11Servlet.class
				.getResource("resultSet11_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
// Call the archive processor
		archiveProcessor.processWebArchive(archive, resultSetClient11Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient11Servlet theTests = new resultSetClient11Servlet();
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
	public void testGetByte36() throws Exception {
		super.testGetByte36();
	}
}
