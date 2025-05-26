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
 * @(#)resultSetClient49.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet49;

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
 * The resultSetClient49 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-javatest")
@Tag("web")

public class resultSetClient49Servlet extends resultSetClient49 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet49";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet49_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(resultSetClient49.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = resultSetClient49Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
// The sun servlet descriptor
		URL sunServletUrl = resultSetClient49Servlet.class
				.getResource("resultSet49_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
// Call the archive processor
		archiveProcessor.processWebArchive(archive, resultSetClient49Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient49Servlet theTests = new resultSetClient49Servlet();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetTime13
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:394;
	 * JDBC:JAVADOC:395; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing a query that returns the
	 * non-null column of Time_Tab as a Time object.Call the getTime(int
	 * columnIndex) method to retrieve this value.Extract the non-null value of
	 * Time_Tab from the tssql.stmt file as a String.Convert this value into a Time
	 * object.Compare this object with the object returned by the getTime(int
	 * columnIndex).Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTime13() throws Exception {
		super.testGetTime13();
	}

	/*
	 * @testName: testGetTime14
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:394;
	 * JDBC:JAVADOC:395; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing a query that returns null
	 * value from Time_Tab as a Time object.Call the getTime(int columnIndex)
	 * method. Check if the value returned is null.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTime14() throws Exception {
		super.testGetTime14();
	}

	/*
	 * @testName: testGetTime16
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:394;
	 * JDBC:JAVADOC:395; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing a query that returns null
	 * value from Timestamp_Tab as a Time object.Call the getTime(int columnIndex)
	 * method. Check if the value returned is null.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTime16() throws Exception {
		super.testGetTime16();
	}

	/*
	 * @testName: testGetTime17
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:426;
	 * JDBC:JAVADOC:427; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing a query that returns non
	 * null column of Time_Tab as a Time object.Call the getTime(String columnName)
	 * to retrieve this value.Extract the non-null value ofTime_Tab from the
	 * tssql.stmt file as a String.Convert this value into a Time object.Compare
	 * this object with the object returned by the getTime(String columnName)
	 * method. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTime17() throws Exception {
		super.testGetTime17();
	}

	/*
	 * @testName: testGetTime18
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:426;
	 * JDBC:JAVADOC:427; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing a query that returns null
	 * value from Time_Tab as a Time object.Call the getTime(String columnName)
	 * method. Check if the value returned is null.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTime18() throws Exception {
		super.testGetTime18();
	}

	/*
	 * @testName: testGetTimestamp01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
	 * JDBC:JAVADOC:397; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the non-null column of Char_Tab table with the
	 * non-null value of Timestamp_Tab.Execute a query that returns the non-null
	 * column of Char_Tab. table.Call the getTimeStamp(int columnIndex) to retrieve
	 * this value.Compare the value returned with the non null column value of
	 * Timestamp_Tab table. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTimestamp01() throws Exception {
		super.testGetTimestamp01();
	}

	/*
	 * @testName: testGetTimestamp13
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
	 * JDBC:JAVADOC:397; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing a query that returns null
	 * value from from Char_Tab.Call the getTimestamp(String columnIndex)
	 * method.Check if it returns null.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTimestamp13() throws Exception {
		super.testGetTimestamp13();
	}

	/*
	 * @testName: testGetTimestamp03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
	 * JDBC:JAVADOC:397; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the non-null column of Varchar_Tab table with the
	 * non-null value of Timestamp_Tab.Execute a query that returns the non-null
	 * column of Varchar_Tab. table.Call the getTimeStamp(int columnIndex) to
	 * retrieve this value.Compare the value returned with the non null column value
	 * of Timestamp_Tab table. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTimestamp03() throws Exception {
		super.testGetTimestamp03();
	}

	/*
	 * @testName: testGetTimestamp04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
	 * JDBC:JAVADOC:397; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing a query that returns null
	 * value from from Varchar_Tab.Call the getTimestamp(int columnIndex)
	 * method.Check if it returns null.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTimestamp04() throws Exception {
		super.testGetTimestamp04();
	}

	/*
	 * @testName: testGetTimestamp12
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:396;
	 * JDBC:JAVADOC:397; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the non-null column of Longvarchar_Tab table with
	 * the non-null value of Timestamp_Tab.Execute a query that returns the non-null
	 * column of Longvarchar_Tab. table.Call the getTimeStamp(String columnIndex) to
	 * retrieve this value.Compare the value returned with the non null column value
	 * of Timestamp_Tab table. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testGetTimestamp12() throws Exception {
		super.testGetTimestamp12();
	}

}
