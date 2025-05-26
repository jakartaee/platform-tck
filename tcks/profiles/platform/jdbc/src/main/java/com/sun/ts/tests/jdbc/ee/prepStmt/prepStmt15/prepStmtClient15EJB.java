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
 * @(#)prepStmtClient15.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt15;

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
 * The prepStmtClient15 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */
@Tag("tck-appclient")

public class prepStmtClient15EJB extends prepStmtClient15 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt15";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "prepStmt15_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(prepStmtClient15.class, ServiceEETest.class, EETest.class);

		URL resURL = prepStmtClient15EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = prepStmtClient15EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt15/prepStmt15_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "prepStmt15_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(prepStmtClient15.class, ServiceEETest.class, EETest.class);

		resURL = prepStmtClient15EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt15/prepStmt15_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = prepStmtClient15EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt15/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt15_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient15EJB theTests = new prepStmtClient15EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetObject223
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Longvarchar_Tab with the maximum (mfg date) value of
	 * Date_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (mfg date) value from the tssql.stmt file. Compare this
	 * value with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject223() throws Exception {
		super.testSetObject223();
	}

	/*
	 * @testName: testSetObject224
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Date_Tab with the maximum (mfg date) value of Date_Tab.
	 * Call the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum (mfg date) value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject224() throws Exception {
		super.testSetObject224();
	}

	/*
	 * @testName: testSetObject225
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Timestamp_Tab with the maximum (mfg timestamp) value of
	 * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (mfg timestamp) value from the tssql.stmt file.
	 * Compare this value with the value returned by the getObject(int columnno)
	 * method. Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject225() throws Exception {
		super.testSetObject225();
	}

	/*
	 * @testName: testSetObject226
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Char_Tab with the maximum (mfg time) value of Time_Tab.
	 * Call the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum (mfg time) value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject226() throws Exception {
		super.testSetObject226();
	}

	/*
	 * @testName: testSetObject227
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Varchar_Tab with the maximum (mfg time)value of Time_Tab.
	 * Call the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum (mfg time) value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject227() throws Exception {
		super.testSetObject228();
	}

	/*
	 * @testName: testSetObject228
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Longvarchar_Tab with the maximum (mfg time) value of
	 * Time_Tab. Call the getObject(int columnno) method to retrieve this value.
	 * Extract the maximum (mfg time) value from the tssql.stmt file. Compare this
	 * value with the value returned by the getObject(int columnno) method. Both the
	 * values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject228() throws Exception {
		super.testSetObject228();
	}

	/*
	 * @testName: testSetObject229
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Time_Tab with the maximum (mfg time) value of Time_Tab.
	 * Call the getObject(int columnno) method to retrieve this value. Extract the
	 * maximum (mfg time) value from the tssql.stmt file. Compare this value with
	 * the value returned by the getObject(int columnno) method. Both the values
	 * should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject229() throws Exception {
		super.testSetObject229();
	}

	/*
	 * @testName: testSetObject230
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Char_Tab with the maximum (brktime) value of
	 * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (brktime) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject230() throws Exception {
		super.testSetObject230();
	}

	/*
	 * @testName: testSetObject231
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Varchar_Tab with the maximum (brktime) value of
	 * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (brktime) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject231() throws Exception {
		super.testSetObject231();
	}

	/*
	 * @testName: testSetObject232
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Longvarchar_Tab with the maximum (brktime) value of
	 * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (brktime) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject232() throws Exception {
		super.testSetObject232();
	}

	/*
	 * @testName: testSetObject233
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Date_Tab with the maximum (brktime) value of
	 * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (brktime) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject233() throws Exception {
		super.testSetObject233();
	}

	/*
	 * @testName: testSetObject234
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Time_Tab with the maximum (brktime) value of
	 * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (brktime) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject234() throws Exception {
		super.testSetObject234();
	}

	/*
	 * @testName: testSetObject235
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:26; JDBC:JAVADOC:694;
	 * JDBC:JAVADOC:695; JavaEE:SPEC:186;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using the setObject(int parameterIndex, Object x) method,update the
	 * column Null_Val of Timestamp_Tab with the maximum (brktime) value of
	 * Timestamp_Tab. Call the getObject(int columnno) method to retrieve this
	 * value. Extract the maximum (brktime) value from the tssql.stmt file. Compare
	 * this value with the value returned by the getObject(int columnno) method.
	 * Both the values should be equal.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetObject235() throws Exception {
		super.testSetObject235();
	}

}
