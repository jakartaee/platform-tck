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
 * @(#)callStmtClient22.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt22;

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
 * The callStmtClient22 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class callStmtClient22Servlet extends callStmtClient22 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt22";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt22_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient22.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = callStmtClient22Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
// The sun servlet descriptor
		URL sunServletUrl = callStmtClient22Servlet.class.getResource("callStmt22_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient22Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient22Servlet theTests = new callStmtClient22Servlet();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testRegisterOutParameter49
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Date value in null column of Date table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getDate method. It should
	 * return a Date object that is been set. (Note: This test case also checks the
	 * support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter49() throws Exception {
		super.testRegisterOutParameter49();
	}

	/*
	 * @testName: testRegisterOutParameter50
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Time Object in null column of Time table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getTime method. It should
	 * return a Time object that is been set. (Note: This test case also checks the
	 * support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter50() throws Exception {
		super.testRegisterOutParameter50();
	}

	/*
	 * @testName: testRegisterOutParameter51
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Timestamp value in null column of Timestamp table and call
	 * registerOutParameter(int parameterIndex, int jdbcType) method and call
	 * getObject method. It should return a Timestamp object that is been set.
	 * (Note: This test case also checks the support for INOUT parameter in Stored
	 * Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter51() throws Exception {
		super.testRegisterOutParameter51();
	}

	/*
	 * @testName: testRegisterOutParameter52
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Byte Array object in Binary table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getObject method. It should
	 * return a Byte Array object that is been set. (Note: This test case also
	 * checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter52() throws Exception {
		super.testRegisterOutParameter52();
	}

	/*
	 * @testName: testRegisterOutParameter53
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Byte Array object in Varbinary table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getObject method. It should
	 * return a Byte Array object that is been set. (Note: This test case also
	 * checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter53() throws Exception {
		super.testRegisterOutParameter53();
	}

	/*
	 * @testName: testRegisterOutParameter54
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
	 * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database. execute the stored procedure and call the setObject() method to set
	 * Byte Array object in Longvarbinary table and call registerOutParameter(int
	 * parameterIndex, int jdbcType) method and call getObject method. It should
	 * return a Byte Array object that is been set. (Note: This test case also
	 * checks the support for INOUT parameter in Stored Procedure)
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testRegisterOutParameter54() throws Exception {
		super.testRegisterOutParameter54();
	}

}
