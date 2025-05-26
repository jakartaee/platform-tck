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
 * @(#)dbMetaClient9.java	1.28 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta9;

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
 * The dbMetaClient9 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class dbMetaClient9EJB extends dbMetaClient9 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta9";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "dbMeta9_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(dbMetaClient9.class, ServiceEETest.class, EETest.class);

		URL resURL = dbMetaClient9EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = dbMetaClient9EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta9/dbMeta9_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "dbMeta9_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(dbMetaClient9.class, ServiceEETest.class, EETest.class);

		resURL = dbMetaClient9EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta9/dbMeta9_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = dbMetaClient9EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta9/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dbMeta9_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient9EJB theTests = new dbMetaClient9EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetTypeInfo
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1072; JDBC:JAVADOC:1073;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getTypeInfo() method on that object. It should return a
	 * ResultSet object Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTypeInfo() throws Exception {
		super.testGetTypeInfo();
	}

	/*
	 * @testName: testSupportsResultSetType1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1076; JDBC:JAVADOC:1077;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetType(int resType) method with Type
	 * TYPE_FORWARD_ONLY on that object.It should return a boolean value; either
	 * true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetType1() throws Exception {
		super.testSupportsResultSetType1();
	}

	/*
	 * @testName: testSupportsResultSetType2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1076; JDBC:JAVADOC:1077;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetType() method with Type
	 * TYPE_SCROLL_INSENSITIVE on that object.It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetType2() throws Exception {
		super.testSupportsResultSetType2();
	}

	/*
	 * @testName: testSupportsResultSetType3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1076; JDBC:JAVADOC:1077;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetType() method with Type
	 * TYPE_SCROLL_SENSITIVE on that object.It should return a boolean value; either
	 * true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetType3() throws Exception {
		super.testSupportsResultSetType3();
	}

	/*
	 * @testName: testSupportsResultSetConcurrency1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1078; JDBC:JAVADOC:1079;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetConcurrency(int resType, int rsConcur)
	 * method on that object with TYPE_FORWARD_ONLY and CONCUR_READ_ONLY. It should
	 * return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetConcurrency1() throws Exception {
		super.testSupportsResultSetConcurrency1();
	}

	/*
	 * @testName: testSupportsResultSetConcurrency2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1078; JDBC:JAVADOC:1079;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetConcurrency(int resType, int rsConcur)
	 * method on that object with TYPE_FORWARD_ONLY and CONCUR_UPDATABLE. It should
	 * return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetConcurrency2() throws Exception {
		super.testSupportsResultSetConcurrency2();
	}

	/*
	 * @testName: testSupportsResultSetConcurrency3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1078; JDBC:JAVADOC:1079;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetConcurrency(int resType, int rsConcur)
	 * method on that object with TYPE_SCROLL_INSENSITIVE and CONCUR_READ_ONLY. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetConcurrency3() throws Exception {
		super.testSupportsResultSetConcurrency3();
	}

	/*
	 * @testName: testSupportsResultSetConcurrency4
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1078; JDBC:JAVADOC:1079;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetConcurrency(int resType, int rsConcur)
	 * method on that object with TYPE_SCROLL_INSENSITIVE and CONCUR_UPDATABLE. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetConcurrency4() throws Exception {
		super.testSupportsResultSetConcurrency4();
	}

	/*
	 * @testName: testSupportsResultSetConcurrency5
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1078; JDBC:JAVADOC:1079;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetConcurrency(int resType, int rsConcur)
	 * method on that object with TYPE_SCROLL_SENSITIVE and CONCUR_READ_ONLY. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetConcurrency5() throws Exception {
		super.testSupportsResultSetConcurrency5();
	}

	/*
	 * @testName: testSupportsResultSetConcurrency6
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1078; JDBC:JAVADOC:1079;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetConcurrency(int resType, int rsConcur)
	 * method on that object with TYPE_SCROLL_SENSITIVE and CONCUR_UPDATABLE. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsResultSetConcurrency6() throws Exception {
		super.testSupportsResultSetConcurrency6();
	}

	/*
	 * @testName: testOwnUpdatesAreVisible1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1080; JDBC:JAVADOC:1081;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownUpdatesAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_FORWARD_ONLY. It should return a boolean value; either
	 * true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnUpdatesAreVisible1() throws Exception {
		super.testOwnUpdatesAreVisible1();
	}

	/*
	 * @testName: testOwnUpdatesAreVisible2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1080; JDBC:JAVADOC:1081;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownUpdatesAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnUpdatesAreVisible2() throws Exception {
		super.testOwnUpdatesAreVisible2();
	}

	/*
	 * @testName: testOwnUpdatesAreVisible3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1080; JDBC:JAVADOC:1081;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownUpdatesAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnUpdatesAreVisible3() throws Exception {
		super.testOwnUpdatesAreVisible3();
	}

	/*
	 * @testName: testOwnDeletesAreVisible1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1082; JDBC:JAVADOC:1083;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownDeletesAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_FORWARD_ONLY. It should return a boolean value; either
	 * true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnDeletesAreVisible1() throws Exception {
		super.testOwnDeletesAreVisible1();
	}

	/*
	 * @testName: testOwnDeletesAreVisible2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1082; JDBC:JAVADOC:1083;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownDeletesAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnDeletesAreVisible2() throws Exception {
		super.testOwnDeletesAreVisible2();
	}

	/*
	 * @testName: testOwnDeletesAreVisible3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1082; JDBC:JAVADOC:1083;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownDeletesAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnDeletesAreVisible3() throws Exception {
		super.testOwnDeletesAreVisible3();
	}

	/*
	 * @testName: testOwnInsertsAreVisible1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1084; JDBC:JAVADOC:1085;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownInsertsAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_FORWARD_ONLY. It should return a boolean value; either
	 * true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnInsertsAreVisible1() throws Exception {
		super.testOwnInsertsAreVisible1();
	}

	/*
	 * @testName: testOwnInsertsAreVisible2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1084; JDBC:JAVADOC:1085;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownInsertsAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnInsertsAreVisible2() throws Exception {
		super.testOwnInsertsAreVisible2();
	}

	/*
	 * @testName: testOwnInsertsAreVisible3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1084; JDBC:JAVADOC:1085;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the ownInsertsAreVisible(int resType) method on that object
	 * with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOwnInsertsAreVisible3() throws Exception {
		super.testOwnInsertsAreVisible3();
	}

	/*
	 * @testName: testOthersUpdatesAreVisible1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1086; JDBC:JAVADOC:1087;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherUpdatesAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_FORWARD_ONLY. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testOthersUpdatesAreVisible1() throws Exception {
		super.testOthersUpdatesAreVisible1();
	}

}
