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
 * @(#)resultSetClient17.java	1.22 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet17;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
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
 * The resultSetClient17 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-javatest")
@Tag("web")
public class resultSetClient17JSP extends resultSetClient17 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet17";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet17_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = resultSetClient17JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = resultSetClient17JSP.class.getResource("resultSet17_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, resultSetClient17JSP.class, sunJSPUrl);

		archive.addClasses(resultSetClient17.class, ServiceEETest.class, EETest.class);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient17JSP theTests = new resultSetClient17JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetShort61
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Decimal_Tab with the
	 * maximum value of table Smallint_Tab.Now execute a query to get the maximum
	 * value of Decimal_Tab table and retrieve the result of the query using the
	 * getShort(String columnName) method.Compare the returned value, with the
	 * maximum value of table Smallint_Tab extracted from the tssql.stmt file. Both
	 * of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort61() throws Exception {
		super.testGetShort61();
	}

	/*
	 * @testName: testGetShort63
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Decimal_Tab.Call the getShort(String columnName)
	 * method.Check if the value returned is zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort63() throws Exception {
		super.testGetShort63();
	}

	/*
	 * @testName: testGetShort64
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Numeric_Tab with the
	 * maximum value of table Smallint_Tab.Now execute a query to get the maximum
	 * value of Numeric_Tab table and retrieve the result of the query using the
	 * getShort(String columnName) method.Compare the returned value, with the
	 * maximum value of table Smallint_Tab extracted from the tssql.stmt file. Both
	 * of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort64() throws Exception {
		super.testGetShort64();
	}

	/*
	 * @testName: testGetShort66
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Numeric_Tab.Call the getShort(String columnName)
	 * method.Check if the value returned is zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort66() throws Exception {
		super.testGetShort66();
	}

	/*
	 * @testName: testGetShort70
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the table Char_Tab with the maximum value of
	 * table Smallint_Tab.Now execute a query to get the maximum value of Char_Tab
	 * table and retrieve the result of the query using the getShort(String
	 * columnName) method.Compare the returned value, with the maximum value of
	 * table Smallint_Tab extracted from the tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort70() throws Exception {
		super.testGetShort70();
	}

	/*
	 * @testName: testGetShort71
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the Char_Tab with the minimum value of table
	 * Smallint_Tab.Now execute a query to get the minimum value of Char_Tab table
	 * and retrieve the result of the query using the getShort(String columnName)
	 * method.Compare the returned value, with the minimum value of table
	 * Smallint_Tab extracted from the tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort71() throws Exception {
		super.testGetShort71();
	}

	/*
	 * @testName: testGetShort72
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Char_Tab.Call the getShort(String columnName) method.
	 * Check if the value returned is zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort72() throws Exception {
		super.testGetShort72();
	}

	/*
	 * @testName: testGetShort73
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the Varchar_Tab with the maximum value of table
	 * Smallint_Tab.Now execute a query to value from Varchar_Tab table and retrieve
	 * the result of the query using the getShort(String columnName) method.Compare
	 * the returned value, with the maximum value of table Smallint_Tab extracted
	 * from the tssql.stmt file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort73() throws Exception {
		super.testGetShort73();
	}

	/*
	 * @testName: testGetShort74
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the table Varchar_Tab with the minimum value of
	 * table Smallint_Tab.Now execute a query to value from Varchar_Tab table and
	 * retrieve the result of the query using the getShort(String columnName)
	 * method.Compare the returned value, with the minimum value of table
	 * Smallint_Tab extracted from the tssql.stmt file. Both of them should be
	 * equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort74() throws Exception {
		super.testGetShort74();
	}

	/*
	 * @testName: testGetShort75
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:410;
	 * JDBC:JAVADOC:411; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Varchar_Tab.Call the getShort(String columnName)
	 * method.Check if the value returned is zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetShort75() throws Exception {
		super.testGetShort75();
	}
}
