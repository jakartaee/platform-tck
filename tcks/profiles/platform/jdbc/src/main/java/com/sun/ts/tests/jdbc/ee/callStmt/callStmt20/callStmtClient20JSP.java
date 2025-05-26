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

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt20;

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
 * The callStmtClient20 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class callStmtClient20JSP extends callStmtClient20 implements Serializable {

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt20_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient20.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = callStmtClient20JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = callStmtClient20JSP.class.getResource("callStmt20_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient20JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(callStmtClient20JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient20JSP theTests = new callStmtClient20JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testRegisterOutParameter09
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setFloat() method to set
	 * maximum Real value in null column of Real table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getFloat method. It should return a float value that is been set. (Note: This
	 * test case also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter09() throws Exception {
		super.testRegisterOutParameter09();
	}

	/*
	 * @testName: testRegisterOutParameter10
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setFloat() method to set
	 * minimum real value in maximum value column in Real table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call
	 * getFloat method. It should return a float value that is been set. (Note: This
	 * test case also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter10() throws Exception {
		super.testRegisterOutParameter10();
	}

	/*
	 * @testName: testRegisterOutParameter11
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBoolean() method to
	 * set maximum BIT value in null column of BIT table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getBoolean method. It should return a boolean value that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter11() throws Exception {
		super.testRegisterOutParameter11();
	}

	/*
	 * @testName: testRegisterOutParameter12
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBoolean() method to
	 * set minimum BIT value in maximum value column in BIT table and call
	 * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
	 * call getBoolen method. It should return a boolean value that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter12() throws Exception {
		super.testRegisterOutParameter12();
	}

	/*
	 * @testName: testRegisterOutParameter13
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setShort() method to set
	 * maximum Smallint value in null column of Smallint table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getShort method. It should return a byte value that is been set. (Note: This
	 * test case also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter13() throws Exception {
		super.testRegisterOutParameter13();
	}

	/*
	 * @testName: testRegisterOutParameter14
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setShort() method to set
	 * minimum Smallint value in maximum value column in Smallint table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call
	 * getShort method. It should return a byte value that is been set. (Note: This
	 * test case also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter14() throws Exception {
		super.testRegisterOutParameter14();
	}

	/*
	 * @testName: testRegisterOutParameter15
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setByte() method to set
	 * maximum Tinyint value in null column of Tinyint table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getByte method. It should return a short value that is been set. (Note: This
	 * test case also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter15() throws Exception {
		super.testRegisterOutParameter15();
	}

	/*
	 * @testName: testRegisterOutParameter16
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setByte() method to set
	 * minimum Tinyint value in maximum value column in Tinyint table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call getByte
	 * method. It should return a short value that is been set. (Note: This test
	 * case also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter16() throws Exception {
		super.testRegisterOutParameter16();
	}

	/*
	 * @testName: testRegisterOutParameter17
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setInt() method to set
	 * maximum Integer value in null column of Integer table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call getInt
	 * method. It should return a int value that is been set. (Note: This test case
	 * also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter17() throws Exception {
		super.testRegisterOutParameter17();
	}

	/*
	 * @testName: testRegisterOutParameter18
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setInt() method to set
	 * minimum Integer value in maximum value column in Integer table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call getInt
	 * method. It should return a int value that is been set. (Note: This test case
	 * also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter18() throws Exception {
		super.testRegisterOutParameter18();
	}

	/*
	 * @testName: testRegisterOutParameter19
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setLong() method to set
	 * maximum Bigint value in null column of Bigint table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getLong method. It should return a long value that is been set. (Note: This
	 * test case also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter19() throws Exception {
		super.testRegisterOutParameter19();
	}

	/*
	 * @testName: testRegisterOutParameter20
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setLong() method to set
	 * minimum Bigint value in maximum value column in Bigint table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call getLong
	 * method. It should return a long value that is been set. (Note: This test case
	 * also checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter20() throws Exception {
		super.testRegisterOutParameter20();
	}

	/*
	 * @testName: testRegisterOutParameter21
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1237;
	 * JDBC:JAVADOC:1238; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setString() method to set
	 * Char value in null column of Char table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getString method. It should
	 * return a String object that is been set. (Note: This test case also checks
	 * the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter21() throws Exception {
		super.testRegisterOutParameter21();
	}

	/*
	 * @testName: testRegisterOutParameter22
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setString() method to set
	 * Varchar value in null column of Varchar table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getString method. It should return a String object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter22() throws Exception {
		super.testRegisterOutParameter22();
	}

	/*
	 * @testName: testRegisterOutParameter23
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setString() method to set
	 * Longvarchar value in null column of Longvarchar table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getString method. It should return a String object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter23() throws Exception {
		super.testRegisterOutParameter23();
	}

	/*
	 * @testName: testRegisterOutParameter24
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setDate() method to set
	 * Date value in null column of Date table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getDate method. It should
	 * return a Date object that is been set. (Note: This test case also checks the
	 * support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter24() throws Exception {
		super.testRegisterOutParameter24();
	}

	/*
	 * @testName: testRegisterOutParameter25
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setTime() method to set
	 * Time value in null column of Time table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getTime method. It should
	 * return a Time object that is been set. (Note: This test case also checks the
	 * support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter25() throws Exception {
		super.testRegisterOutParameter25();
	}

	/*
	 * @testName: testRegisterOutParameter26
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setTimestamp() method to
	 * set Timestamp value in null column of Timestamp table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getTimestamp method. It should return a Timestamp object that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter26() throws Exception {
		super.testRegisterOutParameter26();
	}

	/*
	 * @testName: testRegisterOutParameter27
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBytes() method to set
	 * Binary value in Binary table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getBytes method. It should
	 * return a Byte Array object that is been set. (Note: This test case also
	 * checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter27() throws Exception {
		super.testRegisterOutParameter27();
	}

	/*
	 * @testName: testRegisterOutParameter28
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBytes() method to set
	 * Varbinary value in Varbinary table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getBytes method. It should
	 * return a Byte Array object that is been set. (Note: This test case also
	 * checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testRegisterOutParameter28() throws Exception {
		super.testRegisterOutParameter28();
	}

}
