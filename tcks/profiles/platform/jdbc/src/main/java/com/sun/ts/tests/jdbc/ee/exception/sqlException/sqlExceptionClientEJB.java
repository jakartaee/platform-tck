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
 * @(#)sqlExceptionClient.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.exception.sqlException;

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
 * The sqlExceptionClient class tests methods of SQLException class using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class sqlExceptionClientEJB extends sqlExceptionClient implements Serializable {
	private static final String testName = "jdbc.ee.exception.sqlException";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "sqlException_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(sqlExceptionClient.class, ServiceEETest.class, EETest.class);

		URL resURL = sqlExceptionClientEJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = sqlExceptionClientEJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/exception/sqlException/sqlException_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "sqlException_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(sqlExceptionClient.class, ServiceEETest.class, EETest.class);

		resURL = sqlExceptionClientEJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/exception/sqlException/sqlException_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = sqlExceptionClientEJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/exception/sqlException/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "sqlException_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		sqlExceptionClientEJB theTests = new sqlExceptionClientEJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSQLException01
	 * 
	 * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:68;
	 * 
	 * @test_Strategy: This method constructs a SQLException Object with no
	 * arguments and for that object the reason,SQLState and ErrorCode are checked
	 * for default values.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSQLException01() throws Exception {
		super.testSQLException01();
	}

	/*
	 * @testName: testSQLException02
	 * 
	 * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:67;
	 * 
	 * @test_Strategy: This method constructs a SQLException Object with one
	 * argument and for that object the SQLState, ErrorCode are checked for default
	 * values.The reason is checked for whatever is been assigned while creating the
	 * new instance.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSQLException02() throws Exception {
		super.testSQLException02();
	}

	/*
	 * @testName: testSQLException03
	 * 
	 * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:66;
	 * 
	 * @test_Strategy: This method constructs a SQLException Object with two
	 * arguments and for that object ErrorCode is checked for default values.The
	 * reason and SQLState are checked for whatever is been assigned while creating
	 * the new instance.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSQLException03() throws Exception {
		super.testSQLException03();
	}

	/*
	 * @testName: testSQLException04
	 * 
	 * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:65;
	 * 
	 * @test_Strategy: This method constructs a SQLException Object with three
	 * arguments .The reason,SQLState and Errorcode is checked for whatever is been
	 * assigned while creating the new instance.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSQLException04() throws Exception {
		super.testSQLException04();
	}

	/*
	 * @testName: testGetErrorCode
	 * 
	 * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:70;
	 * 
	 * @testStartegy: The SQLException object is generated by executing an
	 * incomplete SQL Statement and the getErrorCode() method of that object is
	 * checked whether it returns an integer.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetErrorCode() throws Exception {
		super.testGetErrorCode();
	}

	/*
	 * @testName: testGetSQLState
	 * 
	 * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:69;
	 * 
	 * @testStartegy: The SQLException object is generated by executing an
	 * incomplete SQL Statement and the getSQLState() method of that object is
	 * checked whether it is an instance of java.lang.String.
	 */

	@Test
	@TargetVehicle("ejb")
	public void testGetSQLState() throws Exception {
		super.testGetSQLState();
	}

	/*
	 * @testName: testGetNextException
	 * 
	 * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:71;
	 * 
	 * @test_Strategy: SQLException object is generated by executing an incomplete
	 * SQL Statement and using setNextException method a SQLException object is
	 * chained. This is checked using the getNextException method which should
	 * return a instanceof SQLException object.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetNextException() throws Exception {
		super.testGetNextException();
	}

	/*
	 * @testName: testSetNextException
	 * 
	 * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:72;
	 * 
	 * @test_Strategy: SQLException object is obtained by executing a incomplete
	 * SQLStatement and setNextException() method on the object will set a chain of
	 * SQLException on that object which can be checked by using getNextException()
	 * method.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetNextException() throws Exception {
		super.testSetNextException();
	}

}
