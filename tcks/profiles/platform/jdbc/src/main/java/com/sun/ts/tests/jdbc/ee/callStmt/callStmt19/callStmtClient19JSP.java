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
 * @(#)callStmtClient19.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt19;

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
 * The callStmtClient19 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class callStmtClient19JSP extends callStmtClient19 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt19";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt19_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient19.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = callStmtClient19JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = callStmtClient19JSP.class.getResource("callStmt19_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient19JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(callStmtClient19JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	public static void main(String[] args) {
		callStmtClient19JSP theTests = new callStmtClient19JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject221
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Date object for SQL Type
	 * Date and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject221() throws Exception {
		super.testSetObject221();
	}

	/*
	 * @testName: testSetObject223
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Time object for SQL Type
	 * Char and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject223() throws Exception {
		super.testSetObject223();
	}

	/*
	 * @testName: testSetObject224
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Time object for SQL Type
	 * Varchar and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject224() throws Exception {
		super.testSetObject224();
	}

	/*
	 * @testName: testSetObject225
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Time object for SQL Type
	 * Longvarchar and call statement.executeQuery method and call getObject method
	 * of ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject225() throws Exception {
		super.testSetObject225();
	}

	/*
	 * @testName: testSetObject226
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Time object for SQL Type
	 * Time and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject226() throws Exception {
		super.testSetObject226();
	}

	/*
	 * @testName: testSetObject227
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Char and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject227() throws Exception {
		super.testSetObject227();
	}

	/*
	 * @testName: testSetObject228
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Varchar and call statement.executeQuery method and call getObject method
	 * of ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject228() throws Exception {
		super.testSetObject228();
	}

	/*
	 * @testName: testSetObject229
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Longvarchar and call statement.executeQuery method and call getObject
	 * method of ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject229() throws Exception {
		super.testSetObject229();
	}

	/*
	 * @testName: testSetObject231
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Time and call statement.executeQuery method and call getObject method of
	 * ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject231() throws Exception {
		super.testSetObject231();
	}

	/*
	 * @testName: testSetObject232
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject(int
	 * parameterIndex, Object x,int jdbcType) method to set Timestamp object for SQL
	 * Type Timestamp and call statement.executeQuery method and call getObject
	 * method of ResultSet. It should return a String object that is been set.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetObject232() throws Exception {
		super.testSetObject232();
	}

	/*
	 * @testName: testRegisterOutParameter01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBigDecimal(int
	 * parameterIndex, int jdbcType) method to set maximum BigDecimal value in null
	 * column and call registerOutParameter(int parameterIndex,int jdbcType, int
	 * scale) method and call getBigDecimal method. It should return a BigDecimal
	 * object that is been set. (Note: This test case also checks the support for
	 * INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter01() throws Exception {
		super.testRegisterOutParameter01();
	}

	/*
	 * @testName: testRegisterOutParameter02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBigDecimal(int
	 * parameterIndex, int jdbcType) method to set minimum BigDecimal value in
	 * maximum value column and call registerOutParameter(int parameterIndex,int
	 * jdbcType,int scale) method and call getBigDecimal method. It should return a
	 * BigDecimal object that is been set. (Note: This test case also checks the
	 * support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter02() throws Exception {
		super.testRegisterOutParameter02();
	}

	/*
	 * @testName: testRegisterOutParameter03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBigDecimal(int
	 * parameterIndex, int jdbcType) method to set maximum Decimal value in null
	 * column and call registerOutParameter(int parameterIndex,int jdbcType, int
	 * scale) method and call getBigDecimal method. It should return a BigDecimal
	 * object that is been set. (Note: This test case also checks the support for
	 * INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter03() throws Exception {
		super.testRegisterOutParameter03();
	}

	/*
	 * @testName: testRegisterOutParameter04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBigDecimal(int
	 * parameterIndex, int jdbcType) method to set minimum Decimal value in maximum
	 * value column in Decimal table and call registerOutParameter(int
	 * parameterIndex,int jdbcType,int scale) method and call getBigDecimal method.
	 * It should return a BigDecimal object that is been set. (Note: This test case
	 * also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter04() throws Exception {
		super.testRegisterOutParameter04();
	}

	/*
	 * @testName: testRegisterOutParameter05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDouble(int
	 * parameterIndex, int jdbcType) method to set maximum Double value in null
	 * column and call registerOutParameter(int parameterIndex,int jdbcType) method
	 * and call getDouble method. It should return a double value that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter05() throws Exception {
		super.testRegisterOutParameter05();
	}

	/*
	 * @testName: testRegisterOutParameter06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDouble(int
	 * parameterIndex, int jdbcType) method to set minimum double value in maximum
	 * value column in Double table and call registerOutParameter(int
	 * parameterIndex,int jdbcType) method and call getDouble method. It should
	 * return a double value that is been set. (Note: This test case also checks the
	 * support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter06() throws Exception {
		super.testRegisterOutParameter06();
	}

	/*
	 * @testName: testRegisterOutParameter07
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDouble(int
	 * parameterIndex, int jdbcType) method to set maximum Float value in null
	 * column and call registerOutParameter method and call getDouble method. It
	 * should return a double value that is been set. (Note: This test case also
	 * checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter07() throws Exception {
		super.testRegisterOutParameter07();
	}

	/*
	 * @testName: testRegisterOutParameter08
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDouble() method to set
	 * minimum float value in maximum value column in Float table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call
	 * getDouble method. It should return a double value that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter08() throws Exception {
		super.testRegisterOutParameter08();
	}
}
