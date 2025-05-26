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

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt9;

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
 * The prepStmtClient9 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 24/11/00
 */

@Tag("tck-javatest")
@Tag("web")
public class prepStmtClient9JSP extends prepStmtClient9 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt9";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "prepStmt9_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient9.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");
		// The jsp descriptor
		URL jspUrl = prepStmtClient9JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = prepStmtClient9JSP.class.getResource("prepStmt9_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, prepStmtClient9JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(prepStmtClient9JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient9JSP theTests = new prepStmtClient9JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject103
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method,update the column Null_Val of Decimal_Tab with the minimum
	 * (false) value of Bit_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum (false) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject103() throws Exception {
		super.testSetObject103();
	}

	/*
	 * @testName: testSetObject104
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method, update the column Null_Val of Numeric_Tab with the maximum
	 * (true) value of Bit_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the maximum (true) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject104() throws Exception {
		super.testSetObject104();
	}

	/*
	 * @testName: testSetObject105
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType,
	 * int scale) method,update the column Null_Val of Numeric_Tab with the minimum
	 * (false) value of Bit_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum (false) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject105() throws Exception {
		super.testSetObject105();
	}

	/*
	 * @testName: testSetObject108
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Char_Tab with the maximum (true) value
	 * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (true) value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject108() throws Exception {
		super.testSetObject108();
	}

	/*
	 * @testName: testSetObject109
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Char_Tab with the minimum (false) value
	 * of Bit_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the minimum (false) value from the tssql.stmt file. Compare this
	 * value with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject109() throws Exception {
		super.testSetObject109();
	}

	/*
	 * @testName: testSetObject110
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Varchar_Tab with the maximum (true)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject110() throws Exception {
		super.testSetObject110();
	}

	/*
	 * @testName: testSetObject111
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Varchar_Tab with the minimum (false)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum (false) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject111() throws Exception {
		super.testSetObject111();
	}

	/*
	 * @testName: testSetObject112
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the maximum (true)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject112() throws Exception {
		super.testSetObject112();
	}

	/*
	 * @testName: testSetObject113
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Longvarchar_Tab with the minimum (false)
	 * value of Bit_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the minimum (false) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject113() throws Exception {
		super.testSetObject113();
	}

	/*
	 * @testName: testSetObject114
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Tinyint_Tab with the maximum (true) value
	 * of Tinyint_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject114() throws Exception {
		super.testSetObject114();
	}

	/*
	 * @testName: testSetObject115
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Tinyint_Tab with the minimum (false)
	 * value of Tinyint_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum (false) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject115() throws Exception {
		super.testSetObject115();
	}

	/*
	 * @testName: testSetObject116
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Smallint_Tab with the maximum (true)
	 * value of Smallint_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the maximum (true) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject116() throws Exception {
		super.testSetObject116();
	}

	/*
	 * @testName: testSetObject117
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Smallint_Tab with the minimum (false)
	 * value of Smallint_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum (false) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject117() throws Exception {
		super.testSetObject117();
	}

	/*
	 * @testName: testSetObject118
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Integer_Tab with the maximum (true) value
	 * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject118() throws Exception {
		super.testSetObject118();
	}

	/*
	 * @testName: testSetObject119
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Integer_Tab with the minimum (false)
	 * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum (false) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject119() throws Exception {
		super.testSetObject119();
	}

	/*
	 * @testName: testSetObject120
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Bigint_Tab with the maximum (true) value
	 * of Integer_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (true) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject120() throws Exception {
		super.testSetObject120();
	}

	/*
	 * @testName: testSetObject121
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Null_Val of Bigint_Tab with the minimum (false)
	 * value of Integer_Tab. Call the getObject(int columnno) method to retrieve
	 * this value. Extract the minimum (false) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject121() throws Exception {
		super.testSetObject121();
	}

	/*
	 * @testName: testSetObject122
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x, targetSqlType)
	 * method,update the column Min_Val of Real_Tab with the maximum (true) value of
	 * Integer_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (true) value from the tssql.stmt file. Compare this value
	 * with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testSetObject122() throws Exception {
		super.testSetObject122();
	}
}
