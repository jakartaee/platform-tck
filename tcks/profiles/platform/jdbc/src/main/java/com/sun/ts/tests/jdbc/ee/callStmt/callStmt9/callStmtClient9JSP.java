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
 * @(#)callStmtClient9.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt9;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient9 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class callStmtClient9JSP extends callStmtClient9 implements Serializable {

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt9_jsp_vehicle_web.war");
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

		archive.addClasses(callStmtClient9.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = callStmtClient9JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = callStmtClient9JSP.class.getResource("callStmt9_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient9JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(callStmtClient9JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	private static final String testName = "jdbc.ee.callStmt.callStmt9";

	/*
	 * @testName: testSetObject21
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Float object for SQL Type REAL and
	 * call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Float object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal. *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject21() throws Exception {
		super.testSetObject21();
	}

	/*
	 * @testName: testSetObject22
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Float object for SQL Type REAL and
	 * call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Float object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal. *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject22() throws Exception {
		super.testSetObject22();
	}

	/*
	 * @testName: testSetObject23
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Integer object for SQL Type TINYINT
	 * and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject23() throws Exception {
		super.testSetObject23();
	}

	/*
	 * @testName: testSetObject24
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Integer object for SQL Type TINYINT
	 * and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject24() throws Exception {
		super.testSetObject24();
	}

	/*
	 * @testName: testSetObject25
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Integer object for SQL Type SMALLINT
	 * and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject25() throws Exception {
		super.testSetObject25();
	}

	/*
	 * @testName: testSetObject26
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Integer object for SQL Type SMALLINT
	 * and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal. *
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject26() throws Exception {
		super.testSetObject26();
	}

	/*
	 * @testName: testSetObject27
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Byte array object for SQL Type Binary
	 * and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a Byte Array object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject27() throws Exception {
		super.testSetObject27();
	}

	/*
	 * @testName: testSetObject28
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Byte array object for SQL Type
	 * Varbinary and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a Varbinary object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject28() throws Exception {
		super.testSetObject28();
	}

	/*
	 * @testName: testSetObject29
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
	 * JDBC:JAVADOC:697; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x) method to set Byte array object for SQL Type
	 * Longvarbinary and call statement.executeQuery method and call getObject
	 * method of ResultSet. It should return a Varbinary object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject29() throws Exception {
		super.testSetObject29();
	}

	/*
	 * @testName: testSetObject30
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type TINYINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject30() throws Exception {
		super.testSetObject30();
	}

	/*
	 * @testName: testSetObject31
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type TINYINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject31() throws Exception {
		super.testSetObject31();
	}

	/*
	 * @testName: testSetObject32
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type SMALLINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject32() throws Exception {
		super.testSetObject32();
	}

	/*
	 * @testName: testSetObject33
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type SMALLINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject33() throws Exception {
		super.testSetObject33();
	}

	/*
	 * @testName: testSetObject34
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type INTEGER and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject34() throws Exception {
		super.testSetObject34();
	}

	/*
	 * @testName: testSetObject35
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type INTEGER and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Integer object that is
	 * been set. Compare the result with the extracted value from the tssql.stmt
	 * file. Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject35() throws Exception {
		super.testSetObject35();
	}

	/*
	 * @testName: testSetObject36
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type BIGINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Long object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject36() throws Exception {
		super.testSetObject36();
	}

	/*
	 * @testName: testSetObject37
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type BIGINT and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Long object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject37() throws Exception {
		super.testSetObject37();
	}

	/*
	 * @testName: testSetObject38
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type REAL and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Float object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject38() throws Exception {
		super.testSetObject38();
	}

	/*
	 * @testName: testSetObject39
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set String object for SQL
	 * Type REAL and call statement.executeQuery(String sql) method and call
	 * ResultSet.getObject(int column). It should return a Float object that is been
	 * set. Compare the result with the extracted value from the tssql.stmt file.
	 * Both the values should be equal.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject39() throws Exception {
		super.testSetObject39();
	}

	/*
	 * @testName: testSetObject40
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
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
	public void testSetObject40() throws Exception {
		super.testSetObject40();
	}
}
