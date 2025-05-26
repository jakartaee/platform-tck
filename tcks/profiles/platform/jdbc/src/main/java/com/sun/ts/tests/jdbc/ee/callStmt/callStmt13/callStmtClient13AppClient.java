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

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt13;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient13 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */
@Tag("tck-appclient")

public class callStmtClient13AppClient extends callStmtClient13 implements Serializable {
	private static final String testName = "jdbc.ee.callStmt.callStmt13";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "callStmt13_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient13.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = callStmtClient13AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt13/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = callStmtClient13AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, callStmtClient13AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt13_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient13AppClient theTests = new callStmtClient13AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject101
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Numeric_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Numeric_Tab. Compare the
	 * returned value with the maximum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject101() throws Exception {
		super.testSetObject101();
	}

	/*
	 * @testName: testSetObject102
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Numeric_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Numeric_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject102() throws Exception {
		super.testSetObject102();
	}

	/*
	 * @testName: testSetObject105
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Char_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Char_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject105() throws Exception {
		super.testSetObject105();
	}

	/*
	 * @testName: testSetObject106
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Char_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Char_Tab. Compare the returned
	 * value with the minimum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject106() throws Exception {
		super.testSetObject106();
	}

	/*
	 * @testName: testSetObject107
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Varchar_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Varchar_Tab. Compare the
	 * returned value with the maximum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject107() throws Exception {
		super.testSetObject107();
	}

	/*
	 * @testName: testSetObject108
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Varchar_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Varchar_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject108() throws Exception {
		super.testSetObject108();
	}

	/*
	 * @testName: testSetObject109
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Longvarchar_Tab with the maximum value of the
	 * Bit_Tab. Execute a query to retrieve the Null_Val from Longvarchar_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject109() throws Exception {
		super.testSetObject109();
	}

	/*
	 * @testName: testSetObject110
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Longvarchar_Tab with the minimum value of the
	 * Bit_Tab. Execute a query to retrieve the Null_Val from Longvarchar_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject110() throws Exception {
		super.testSetObject110();
	}

	/*
	 * @testName: testSetObject111
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Tinyint_Tab with the maximum value of the
	 * Tinyint_Tab. Execute a query to retrieve the Min_Val from Tinyint_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject111() throws Exception {
		super.testSetObject111();
	}

	/*
	 * @testName: testSetObject112
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Tinyint_Tab with the minimum value of the
	 * Tinyint_Tab. Execute a query to retrieve the Null_Val from Tinyint_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject112() throws Exception {
		super.testSetObject112();
	}

	/*
	 * @testName: testSetObject113
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Smallint_Tab with the maximum value of the
	 * Smallint_Tab. Execute a query to retrieve the Min_Val from Smallint_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject113() throws Exception {
		super.testSetObject113();
	}

	/*
	 * @testName: testSetObject114
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Smallint_Tab with the minimum value of the
	 * Smallint_Tab. Execute a query to retrieve the Null_Val from Smallint_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject114() throws Exception {
		super.testSetObject114();
	}

	/*
	 * @testName: testSetObject115
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Integer_Tab with the maximum value of the
	 * Integer_Tab. Execute a query to retrieve the Min_Val from Integer_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject115() throws Exception {
		super.testSetObject115();
	}

	/*
	 * @testName: testSetObject116
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Integer_Tab with the minimum value of the
	 * Integer_Tab. Execute a query to retrieve the Null_Val from Integer_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject116() throws Exception {
		super.testSetObject116();
	}

	/*
	 * @testName: testSetObject117
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Bigint_Tab with the maximum value of the
	 * Integer_Tab. Execute a query to retrieve the Min_Val from Bigint_Tab. Compare
	 * the returned value with the maximum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject117() throws Exception {
		super.testSetObject117();
	}

	/*
	 * @testName: testSetObject118
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Bigint_Tab with the minimum value of the
	 * Integer_Tab. Execute a query to retrieve the Null_Val from Bigint_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject118() throws Exception {
		super.testSetObject118();
	}

	/*
	 * @testName: testSetObject119
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Real_Tab with the maximum value of the Integer_Tab.
	 * Execute a query to retrieve the Min_Val from Real_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject119() throws Exception {
		super.testSetObject119();
	}

	/*
	 * @testName: testSetObject120
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Real_Tab with the minimum value of the
	 * Integer_Tab. Execute a query to retrieve the Null_Val from Real_Tab. Compare
	 * the returned value with the minimum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetObject120() throws Exception {
		super.testSetObject120();
	}

}
