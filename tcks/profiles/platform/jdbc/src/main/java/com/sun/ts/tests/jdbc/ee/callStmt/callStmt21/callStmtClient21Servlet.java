/*
 * Copyright (c) 2006, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)callStmtClient21.java	1.27 07/10/03
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt21;

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
 * The callStmtClient21 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class callStmtClient21Servlet extends callStmtClient21 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt21";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt21_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient21.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = callStmtClient21Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
//The sun servlet descriptor
		URL sunServletUrl = callStmtClient21Servlet.class.getResource("callStmt21_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
//Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient21Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient21Servlet theTests = new callStmtClient21Servlet();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testRegisterOutParameter29
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setBytes() method to set
	 * Longvarbinary value in Longvarbinary table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getBytes method. It should
	 * return a Byte Array object that is been set. (Note: This test case also
	 * checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter29() throws Exception {
		super.testRegisterOutParameter29();
	}

	/*
	 * @testName: testRegisterOutParameter30
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * maximum Double value in null column and call registerOutParameter method and
	 * call getObject method. It should return a Double object that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter30() throws Exception {
		super.testRegisterOutParameter30();
	}

	/*
	 * @testName: testRegisterOutParameter31
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * minimum double value in maximum value column in Double table and call
	 * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
	 * call getObject method. It should return a Double object that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter31() throws Exception {
		super.testRegisterOutParameter31();
	}

	/*
	 * @testName: testRegisterOutParameter32
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Double value in null column and call registerOutParameter method and call
	 * getObject method. It should return a Double object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter32() throws Exception {
		super.testRegisterOutParameter32();
	}

	/*
	 * @testName: testRegisterOutParameter33
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * minimum float value in maximum value column in Float table and call
	 * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
	 * call getObject method. It should return a Double object that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter33() throws Exception {
		super.testRegisterOutParameter33();
	}

	/*
	 * @testName: testRegisterOutParameter34
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * maximum Real value in null column of Real table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a Float object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter34() throws Exception {
		super.testRegisterOutParameter34();
	}

	/*
	 * @testName: testRegisterOutParameter35
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * minimum real value in maximum value column in Real table and call
	 * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
	 * call getObject method. It should return a Float object that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter35() throws Exception {
		super.testRegisterOutParameter35();
	}

	/*
	 * @testName: testRegisterOutParameter36
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * maximum BIT value in null column of BIT table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a Boolean object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter36() throws Exception {
		super.testRegisterOutParameter36();
	}

	/*
	 * @testName: testRegisterOutParameter37
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * minimum BIT value in maximum value column in BIT table and call
	 * registerOutParameter(int parameterIndex,int jdbcType,int scale) method and
	 * call getBoolen method. It should return a Boolean object that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter37() throws Exception {
		super.testRegisterOutParameter37();
	}

	/*
	 * @testName: testRegisterOutParameter38
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * maximum Smallint value in null column of Smallint table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a Integer object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter38() throws Exception {
		super.testRegisterOutParameter38();
	}

	/*
	 * @testName: testRegisterOutParameter39
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * minimum Smallint value in maximum value column in Smallint table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call
	 * getObject method. It should return a Integer object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter39() throws Exception {
		super.testRegisterOutParameter39();
	}

	/*
	 * @testName: testRegisterOutParameter40
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * maximum Tinyint value in null column of Tinyint table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a Byte object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter40() throws Exception {
		super.testRegisterOutParameter40();
	}

	/*
	 * @testName: testRegisterOutParameter41
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * minimum Tinyint value in maximum value column in Tinyint table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call
	 * getObject method. It should return a Byte object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter41() throws Exception {
		super.testRegisterOutParameter41();
	}

	/*
	 * @testName: testRegisterOutParameter42
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * maximum Integer value in null column of Integer table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a Integer object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter42() throws Exception {
		super.testRegisterOutParameter42();
	}

	/*
	 * @testName: testRegisterOutParameter43
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * minimum Integer value in maximum value column in Integer table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call
	 * getObject method. It should return a Integer object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter43() throws Exception {
		super.testRegisterOutParameter43();
	}

	/*
	 * @testName: testRegisterOutParameter44
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * maximum Bigint value in null column of Bigint table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a Long Object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter44() throws Exception {
		super.testRegisterOutParameter44();
	}

	/*
	 * @testName: testRegisterOutParameter45
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * minimum Bigint value in maximum value column in Bigint table and call
	 * registerOutParameter(int parameterIndex,int jdbcType) method and call
	 * getObject method. It should return a Long object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter45() throws Exception {
		super.testRegisterOutParameter45();
	}

	/*
	 * @testName: testRegisterOutParameter46
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
	@TargetVehicle("servlet")
	public void testRegisterOutParameter46() throws Exception {
		super.testRegisterOutParameter46();
	}

	/*
	 * @testName: testRegisterOutParameter47
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Varchar value in null column of Varchar table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a String object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter47() throws Exception {
		super.testRegisterOutParameter47();
	}

	/*
	 * @testName: testRegisterOutParameter48
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Longvarchar value in null column of Longvarchar table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a String object that is been set. (Note:
	 * This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter48() throws Exception {
		super.testRegisterOutParameter48();
	}

}
