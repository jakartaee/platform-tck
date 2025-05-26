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
 * @(#)resultSetClient7.java	1.25 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet7;

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
 * The resultSetClient7 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 9/9/99
 */

@Tag("tck-appclient")

public class resultSetClient7EJB extends resultSetClient7 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet7";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "resultSet7_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(resultSetClient7.class, ServiceEETest.class, EETest.class);

		URL resURL = resultSetClient7EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = resultSetClient7EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/resultSet/resultSet7/resultSet7_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "resultSet7_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(resultSetClient7.class, ServiceEETest.class, EETest.class);

		resURL = resultSetClient7EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/resultSet/resultSet7/resultSet7_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = resultSetClient7EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/resultSet/resultSet7/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "resultSet7_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient7EJB theTests = new resultSetClient7EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetObject61
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(int column index) method with the SQL null column of JDBC
	 * datatype SMALLINT. It should return null Integer object.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject61() throws Exception {
		super.testGetObject61();
	}

	/*
	 * @testName: testGetObject69
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String column index) method with the column of JDBC
	 * datatype SMALLINT. It should return an Integer object that has been set as
	 * the maximum value of SMALLINT.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject69() throws Exception {
		super.testGetObject69();
	}

	/*
	 * @testName: testGetObject70
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String column index) method with the SQL column of JDBC
	 * datatype SMALLINT. It should return an Integer object that has been set as
	 * the minimum value of SMALLINT.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject70() throws Exception {
		super.testGetObject70();
	}

	/*
	 * @testName: testGetObject62
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JDBC:JAVADOC:442;
	 * JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the column of JDBC datatype
	 * SMALLINT. It should return an Integer object that has been set as the maximum
	 * value of SMALLINT.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject62() throws Exception {
		super.testGetObject62();
	}

	/*
	 * @testName: testGetObject63
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the SQL column of JDBC
	 * datatype SMALLINT. It should return an Integer object that has been set as
	 * the minimum value of SMALLINT.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject63() throws Exception {
		super.testGetObject63();
	}

	/*
	 * @testName: testGetObject64
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the SQL null column of JDBC
	 * datatype SMALLINT. It should return null Integer object.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject64() throws Exception {
		super.testGetObject64();
	}

	/*
	 * @testName: testGetObject65
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(int columnIndex) method with the SQL column of JDBC
	 * datatype VARCHAR. It should return an String object that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject65() throws Exception {
		super.testGetObject65();
	}

	/*
	 * @testName: testGetObject66
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(int column index) method with the SQL null column of JDBC
	 * datatype VARCHAR. It should return null String object.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject66() throws Exception {
		super.testGetObject66();
	}

	/*
	 * @testName: testGetObject67
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the SQL column of JDBC
	 * datatype VARCHAR. It should return an String object that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject67() throws Exception {
		super.testGetObject67();
	}

	/*
	 * @testName: testGetObject68
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the SQL null column of JDBC
	 * datatype VARCHAR. It should return null String object.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetObject68() throws Exception {
		super.testGetObject68();
	}
}
