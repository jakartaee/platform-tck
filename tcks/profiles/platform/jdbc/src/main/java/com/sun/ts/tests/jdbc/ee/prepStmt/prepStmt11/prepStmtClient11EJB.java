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
 * @(#)prepStmtClient11.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt11;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

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
 * The prepStmtClient11 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/27/00
 */

@Tag("tck-appclient")

public class prepStmtClient11EJB extends prepStmtClient11 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt11";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "prepStmt11_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(prepStmtClient11.class, ServiceEETest.class, EETest.class);

		URL resURL = prepStmtClient11EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = prepStmtClient11EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt11/prepStmt11_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "prepStmt11_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(prepStmtClient11.class, ServiceEETest.class, EETest.class);

		resURL = prepStmtClient11EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt11/prepStmt11_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = prepStmtClient11EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt11/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt11_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient11EJB theTests = new prepStmtClient11EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject143
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Smallint_Tab with the minimum value of Smallint_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject143() throws Exception {
		super.testSetObject143();
	}

	/*
	 * @testName: testSetObject144
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val of Integer_Tab with the maximum value of Integer_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the maximum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetObject144() throws Exception {
		super.testSetObject144();
	}

	/*
	 * @testName: testSetObject145
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Integer_Tab with the minimum value of Integer_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject145() throws Exception {
		super.testSetObject145();
	}

	/*
	 * @testName: testSetObject146
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val of Bigint_Tab with the maximum value of Bigint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the maximum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject146() throws Exception {
		super.testSetObject146();
	}

	/*
	 * @testName: testSetObject147
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Bigint_Tab with the minimum value of Bigint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the minimum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject147() throws Exception {
		super.testSetObject147();
	}

	/*
	 * @testName: testSetObject148
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val of Real_Tab with the maximum value of Bigint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the maximum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject148() throws Exception {
		super.testSetObject148();
	}

	/*
	 * @testName: testSetObject149
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Real_Tab with the minimum value of Bigint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the minimum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject149() throws Exception {
		super.testSetObject149();
	}

	/*
	 * @testName: testSetObject150
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column min_Val of Float_Tab with the maximum value of Float_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the maximum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject150() throws Exception {
		super.testSetObject150();
	}

	/*
	 * @testName: testSetObject151
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Float_Tab with the minimum value of Float_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the minimum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject151() throws Exception {
		super.testSetObject151();
	}

	/*
	 * @testName: testSetObject152
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method, update
	 * the column Min_Val of Double_Tab with the maximum value of Double_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject152() throws Exception {
		super.testSetObject152();
	}

	/*
	 * @testName: testSetObject153
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Double_Tab with the minimum value of Double_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the minimum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject153() throws Exception {
		super.testSetObject153();
	}

	/*
	 * @testName: testSetObject154
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Decimal_Tab with the maximum value of Integer_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject154() throws Exception {
		super.testSetObject154();
	}

	/*
	 * @testName: testSetObject155
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Decimal_Tab with the minimum value of Integer_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject155() throws Exception {
		super.testSetObject155();
	}

	/*
	 * @testName: testSetObject156
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Min_Val of Numeric_Tab with the maximum value of Integer_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the maximum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject156() throws Exception {
		super.testSetObject156();
	}

	/*
	 * @testName: testSetObject157
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:692; JDBC:JAVADOC:693;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Numeric_Tab with the minimum value of Integer_Tab. Call
	 * the getObject(int columnno) method to retrieve this value. Extract the
	 * minimum value from the tssql.stmt file. Compare this value with the value
	 * returned by the getObject(int columnno) method. Both the values should be
	 * equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject157() throws Exception {
		super.testSetObject157();

	}

	/*
	 * @testName: testSetObject160
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Char_Tab with the maximum value of Bigint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the maximum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject160() throws Exception {
		super.testSetObject160();
	}

	/*
	 * @testName: testSetObject161
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Char_Tab with the minimum value of Bigint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the minimum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject161() throws Exception {
		super.testSetObject161();
	}

	/*
	 * @testName: testSetObject162
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
	 * JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Varchar_Tab with the maximum value of Bigint_Tab. Call the
	 * getObject(int columnno) method to retrieve this value. Extract the maximum
	 * value from the tssql.stmt file. Compare this value with the value returned by
	 * the getObject(int columnno) method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject162() throws Exception {
		super.testSetObject162();
	}
}
