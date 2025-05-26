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
 * The prepStmtClient12 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/27/00
 */

@Tag("tck-appclient")

public class prepStmtClient12EJB extends prepStmtClient12 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt12";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "prepStmt12_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(prepStmtClient12.class, ServiceEETest.class, EETest.class);

		URL resURL = prepStmtClient12EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = prepStmtClient12EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt12/prepStmt12_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "prepStmt12_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(prepStmtClient12.class, ServiceEETest.class, EETest.class);

		resURL = prepStmtClient12EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt12/prepStmt12_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = prepStmtClient12EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt12/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt12_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient12EJB theTests = new prepStmtClient12EJB();
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
	public void testSetObject177() throws Exception {
		super.testSetObject177();
	}

}
