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
 * @(#)callStmtClient12.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt12;

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

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient12 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class callStmtClient12EJB extends callStmtClient12 implements Serializable {

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt12_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(callStmtClient12EJB.class, callStmtClient12.class, ServiceEETest.class, EETest.class);

		URL resURL = callStmtClient12EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = callStmtClient12EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt12/callStmt12_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt12_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient12EJB.class, callStmtClient12.class);

		resURL = callStmtClient12EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/callStmt/callStmt12/callStmt12_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = callStmtClient12EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/callStmt/callStmt12/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt12_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient12EJB theTests = new callStmtClient12EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject81
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Varchar_Tab with the maximum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Varchar_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject81() throws Exception {
		super.testSetObject81();
	}

	/*
	 * @testName: testSetObject82
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Varchar_Tab with the minimum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Varchar_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject82() throws Exception {
		super.testSetObject82();
	}

	/*
	 * @testName: testSetObject83
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Longvarchar_Tab with the maximum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Longvarchar_Tab.
	 * Compare the returned value with the maximum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject83() throws Exception {
		super.testSetObject83();
	}

	/*
	 * @testName: testSetObject84
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Longvarchar_Tab with the minimum value of the
	 * Decimal_Tab. Execute a query to retrieve the Null_Val from Longvarchar_Tab.
	 * Compare the returned value with the minimum value extracted from tssql.stmt
	 * file. Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject84() throws Exception {
		super.testSetObject84();
	}

	/*
	 * @testName: testSetObject85
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Tinyint_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Min_Val from Tinyint_Tab. Compare the
	 * returned value with the maximum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject85() throws Exception {
		super.testSetObject85();
	}

	/*
	 * @testName: testSetObject86
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Tinyint_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Tinyint_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject86() throws Exception {
		super.testSetObject86();
	}

	/*
	 * @testName: testSetObject87
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Smallint_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Min_Val from Smallint_Tab. Compare the
	 * returned value with the maximum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject87() throws Exception {
		super.testSetObject87();
	}

	/*
	 * @testName: testSetObject88
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Smallint_Tab with the minimum value of the
	 * Bit_Tab. Execute a query to retrieve the Null_Val from Smallint_Tab. Compare
	 * the returned value with the minimum value extracted from tssql.stmt file.
	 * Both of them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject88() throws Exception {
		super.testSetObject88();
	}

	/*
	 * @testName: testSetObject89
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Integer_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Min_Val from Integer_Tab. Compare the
	 * returned value with the maximum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject89() throws Exception {
		super.testSetObject89();
	}

	/*
	 * @testName: testSetObject90
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Integer_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Integer_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject90() throws Exception {
		super.testSetObject90();
	}

	/*
	 * @testName: testSetObject91
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Bigint_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Min_Val from Bigint_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject91() throws Exception {
		super.testSetObject91();
	}

	/*
	 * @testName: testSetObject92
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Bigint_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Bigint_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject92() throws Exception {
		super.testSetObject92();
	}

	/*
	 * @testName: testSetObject93
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Real_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Min_Val from Real_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject93() throws Exception {
		super.testSetObject93();
	}

	/*
	 * @testName: testSetObject94
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Real_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Real_Tab. Compare the returned
	 * value with the minimum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject94() throws Exception {
		super.testSetObject94();
	}

	/*
	 * @testName: testSetObject95
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Float_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Min_Val from Float_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject95() throws Exception {
		super.testSetObject95();
	}

	/*
	 * @testName: testSetObject96
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Float_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Float_Tab. Compare the returned
	 * value with the minimum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject96() throws Exception {
		super.testSetObject96();
	}

	/*
	 * @testName: testSetObject97
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Min_Val of the Double_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Min_Val from Double_Tab. Compare the returned
	 * value with the maximum value extracted from tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject97() throws Exception {
		super.testSetObject97();
	}

	/*
	 * @testName: testSetObject98
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Double_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Double_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject98() throws Exception {
		super.testSetObject98();
	}

	/*
	 * @testName: testSetObject99
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Decimal_Tab with the maximum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Decimal_Tab. Compare the
	 * returned value with the maximum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject99() throws Exception {
		super.testSetObject99();
	}

	/*
	 * @testName: testSetObject100
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
	 * JDBC:JAVADOC:693; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: This test case is meant for checking the support for IN
	 * parameter in CallableStatement Interface. Get a CallableStatement object from
	 * the connection to the database. Using the IN parameter of that object,update
	 * the column Null_Val of the Decimal_Tab with the minimum value of the Bit_Tab.
	 * Execute a query to retrieve the Null_Val from Decimal_Tab. Compare the
	 * returned value with the minimum value extracted from tssql.stmt file. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject100() throws Exception {
		super.testSetObject100();
	}
}
