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
 * @(#)connectionClient1.java	1.22 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.connection.connection1;

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
 * The connectionClient1 class tests methods of Connection interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class connectionClient1EJB extends connectionClient1 implements Serializable {
	private static final String testName = "jdbc.ee.connection.connection1";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "connection1_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(connectionClient1.class, ServiceEETest.class, EETest.class);

		URL resURL = connectionClient1EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = connectionClient1EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/connection/connection1/connection1_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "connection1_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(connectionClient1.class, ServiceEETest.class, EETest.class);

		resURL = connectionClient1EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/connection/connection1/connection1_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = connectionClient1EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/connection/connection1/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "connection1_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		connectionClient1EJB theTests = new connectionClient1EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testClose
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1157; JDBC:JAVADOC:1158;
	 * JDBC:JAVADOC:1159; JDBC:JAVADOC:1160;
	 *
	 * @test_Strategy: Get a Connection object and call close() method and call
	 * isClosed() method and it should return a true value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testClose() throws Exception {
		super.testClose();
	}

	/*
	 * @testName: testCreateStatement01
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1141; JDBC:JAVADOC:1142;
	 *
	 * @test_Strategy: Get a Connection object and call createStatement() method and
	 * call instanceof to check It should return a Statement object
	 */
	@Test
	@TargetVehicle("ejb")
	public void testCreateStatement01() throws Exception {
		super.testCreateStatement01();
	}

	/*
	 * @testName: testGetCatalog
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1169; JDBC:JAVADOC:1170;
	 * 
	 * @test_Strategy: Get a Connection object and call getCatalog() method It
	 * should return a String value The getCatalogs() method in Databasemeta data
	 * object will return a Resultset object that contains the catalog name in the
	 * column TABLE_CAT .The String returned by Connection.getCatalog() method will
	 * be checked against these column values.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetCatalog() throws Exception {
		super.testGetCatalog();
	}

	/*
	 * @testName: testGetMetaData
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1161; JDBC:JAVADOC:1162;
	 *
	 * @test_Strategy: Get a Connection object and call getMetaData() method and
	 * call instanceof method to check It should return a DatabaseMetaData object
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMetaData() throws Exception {
		super.testGetMetaData();
	}

	/*
	 * @testName: testGetTransactionIsolation
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1173; JDBC:JAVADOC:1174;
	 * JDBC:SPEC:16; JDBC:SPEC:15
	 *
	 * @test_Strategy: Get a Connection object and call getTransactionIsolation()
	 * method It should return a Integer value and must be equal to the value of
	 * TRANSACTION_NONE or TRANSACTION_READ_COMMITTED or
	 * TRANSACTION_READ_UNCOMMITTED or TRANSACTION_REPEATABLE_READ or
	 * TRANSACTION_SERIALIZABLE which is default set by the driver
	 * 
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTransactionIsolation() throws Exception {
		super.testGetTransactionIsolation();
	}

	/*
	 * @testName: testIsClosed01
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1159; JDBC:JAVADOC:1160;
	 *
	 * @test_Strategy: Get a Connection object and call isClosed() method It should
	 * return a boolean value and the value should be equal to false
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testIsClosed01() throws Exception {
		super.testIsClosed01();
	}

	/*
	 * @testName: testIsClosed02
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1157; JDBC:JAVADOC:1158;
	 * JDBC:JAVADOC:1159; JDBC:JAVADOC:1160;
	 *
	 * @test_Strategy: Get a Connection object and call close() method and call
	 * isClosed() method It should return a boolean value and the value should be
	 * equal to true
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testIsClosed02() throws Exception {
		super.testIsClosed02();
	}

	/*
	 * @testName: testIsReadOnly
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1165; JDBC:JAVADOC:1166;
	 * JDBC:JAVADOC:1163; JDBC:JAVADOC:1164;
	 *
	 * @test_Strategy: Get a Connection object and call setReadOnly(boolean b)
	 * method and call isReadOnly() method It should return a boolean value that is
	 * been set
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testIsReadOnly() throws Exception {
		super.testIsReadOnly();
	}

	/*
	 * @testName: testNativeSQL
	 * 
	 * @assertion_ids: JavaEE:SPEC:179; JDBC:JAVADOC:1147; JDBC:JAVADOC:1148;
	 *
	 * @test_Strategy: Get a Connection object and call nativeSQL(String sql) method
	 * It should return a String value which represents native SQL grammar
	 * implementation of the SQL statement if the driver supports else it returns
	 * the actual SQL statement as a String.This is checked by using instanceof
	 * method
	 */
	@Test
	@TargetVehicle("ejb")
	public void testNativeSQL() throws Exception {
		super.testNativeSQL();
	}

}
