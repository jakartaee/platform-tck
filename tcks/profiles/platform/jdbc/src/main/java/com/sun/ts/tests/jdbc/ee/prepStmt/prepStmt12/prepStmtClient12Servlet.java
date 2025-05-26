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
 * $Id$
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt12;

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
 * The prepStmtClient12 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/27/00
 */

@Tag("tck-javatest")
@Tag("web")
public class prepStmtClient12Servlet extends prepStmtClient12 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt12";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "prepStmt12_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient12.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = prepStmtClient12Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
// The sun servlet descriptor
		URL sunServletUrl = prepStmtClient12Servlet.class.getResource("prepStmt12_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
// Call the archive processor
		archiveProcessor.processWebArchive(archive, prepStmtClient12Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient12Servlet theTests = new prepStmtClient12Servlet();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject163
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Varchar_Tab with the minimum value of Bigint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the minimum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject163() throws Exception {
		super.testSetObject163();
	}

	/*
	 * @testName: testSetObject164
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the maximum value
	 * of Bigint_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject164() throws Exception {
		super.testSetObject164();
	}

	/*
	 * @testName: testSetObject165
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the minimum value
	 * of Bigint_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject165() throws Exception {
		super.testSetObject165();
	}

	/*
	 * @testName: testSetObject166
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Tinyint_Tab with the maximum value of
	 * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject166() throws Exception {
		super.testSetObject166();
	}

	/*
	 * @testName: testSetObject167
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Tinyint_Tab with the minimum value of
	 * Tinyint_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject167() throws Exception {
		super.testSetObject167();
	}

	/*
	 * @testName: testSetObject168
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Smallint_Tab with the maximum value of
	 * Smallint_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject168() throws Exception {
		super.testSetObject168();
	}

	/*
	 * @testName: testSetObject169
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Smallint_Tab with the minimum value of
	 * Smallint_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject169() throws Exception {
		super.testSetObject169();
	}

	/*
	 * @testName: testSetObject170
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Integer_Tab with the some integer value
	 * after converting it to Float. Call the getObject(int columnno) method to
	 * retrieve this value. Compare this value with the value that is inserted into
	 * the databse using setObject method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject170() throws Exception {
		super.testSetObject170();
	}

	/*
	 * @testName: testSetObject174
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Real_Tab with the maximum value of
	 * Real_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject174() throws Exception {
		super.testSetObject174();
	}

	/*
	 * @testName: testSetObject175
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Real_Tab with the minimum value of
	 * Real_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject175() throws Exception {
		super.testSetObject175();
	}

	/*
	 * @testName: testSetObject176
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Float_Tab with the maximum value of
	 * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject176() throws Exception {
		super.testSetObject176();
	}

	/*
	 * @testName: testSetObject177
	 * 
	 * @assertion_ids: JDBC:SPEC:9;JDBC:SPEC:26; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Float_Tab with the minimum value of
	 * Float_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("servlet")
	public void testSetObject177() throws Exception {
		super.testSetObject177();
	}

}
