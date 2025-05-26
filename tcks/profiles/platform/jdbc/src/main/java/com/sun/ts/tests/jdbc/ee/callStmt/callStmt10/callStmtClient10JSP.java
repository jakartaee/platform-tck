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

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt10;

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
 * The callStmtClient10 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */
@Tag("tck-javatest")
@Tag("web")
public class callStmtClient10JSP extends callStmtClient10 implements Serializable {

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt10_jsp_vehicle_web.war");
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

		archive.addClasses(callStmtClient10.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = callStmtClient10JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = callStmtClient10JSP.class.getResource("callStmt10_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient10JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(callStmtClient10JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */

	public static void main(String[] args) {
		callStmtClient10JSP theTests = new callStmtClient10JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject41
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:6; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type FLOAT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Double object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject41() throws Exception {
		super.testSetObject41();
	}

	/*
	 * @testName: testSetObject42
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:8; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type DOUBLE and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Double object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject42() throws Exception {
		super.testSetObject42();
	}

	/*
	 * @testName: testSetObject43
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type DOUBLE and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Double object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject43() throws Exception {
		super.testSetObject43();
	}

	/*
	 * @testName: testSetObject44
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JDBC:JAVADOC:10; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type DECIMAL and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a BigDecimal object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject44() throws Exception {
		super.testSetObject44();
	}

	/*
	 * @testName: testSetObject45
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type DECIMAL and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a BigDecimal object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject45() throws Exception {
		super.testSetObject45();
	}

	/*
	 * @testName: testSetObject46
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JDBC:JAVADOC:9; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type NUMERIC and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a BigDecimal object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject46() throws Exception {
		super.testSetObject46();
	}

	/*
	 * @testName: testSetObject47
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type NUMERIC and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a BigDecimal object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject47() throws Exception {
		super.testSetObject47();
	}

	/*
	 * @testName: testSetObject48
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:1; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type BIT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getBoolean(int column). It should return a boolean value that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 * -Description details- This test is actually testing the ability to
	 * successfully call the setObject() method. In order to do that, this test will
	 * get the MAXVAL value from the bit tab, and then it will use the setObject
	 * method to set/change the MINVAL value to be the same as the MAXVAL. After
	 * setting the MINVAL, a query is done to get the newly set MINVAL value and a
	 * comparison is done to make sure it was indeed set to the same value as
	 * MAXVAL.
	 * 
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject48() throws Exception {
		super.testSetObject48();
	}

	/*
	 * @testName: testSetObject49
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:1; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type BIT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getBoolean(int column). It should return a boolean value that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 * -Description details- This test is actually testing the ability to
	 * successfully call the setObject() method. In order to do that, this test will
	 * get the MINVAL value from the bit tab, and then it will use the setObject
	 * method to set/change the MAXVAL value to be the same as the MINVAL. After
	 * setting the MAXVAL, a query is done to get the newly set MAXVAL value and a
	 * comparison is done to make sure it was indeed set to the same value as
	 * MINVAL.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject49() throws Exception {
		super.testSetObject49();
	}

	/*
	 * @testName: testSetObject50
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:11; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type CHAR and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a String object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject50() throws Exception {
		super.testSetObject50();
	}

	/*
	 * @testName: testSetObject51
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:12; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type VARCHAR and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a String object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject51() throws Exception {
		super.testSetObject51();
	}

	/*
	 * @testName: testSetObject52
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:13; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type LONGVARCHAR and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a String object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject52() throws Exception {
		super.testSetObject52();
	}

	/*
	 * @testName: testSetObject56
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:14;JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type DATE and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Date object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject56() throws Exception {
		super.testSetObject56();
	}

	/*
	 * @testName: testSetObject57
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:15; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type TIME and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Time object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject57() throws Exception {
		super.testSetObject57();
	}

	/*
	 * @testName: testSetObject58
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:16; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type TIMESTAMP and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Timestamp object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject58() throws Exception {
		super.testSetObject58();
	}

	/*
	 * @testName: testSetObject59
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:2; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Tinyint_Tab with the maximum value of the
	 * Tinyint_Tab. Execute a query to retrieve the Min_Val from Tinyint_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject59() throws Exception {
		super.testSetObject59();
	}

	/*
	 * @testName: testSetObject60
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JDBC:JAVADOC:2; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database Using the IN parameter of that object,update
	 * the column Null_Val of the Tinyint_Tab with the minimum value of the
	 * Tinyint_Tab. Execute a query to retrieve the Null_Val from Tinyint_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file,Both of them should be equal
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject60() throws Exception {
		super.testSetObject60();
	}
}
