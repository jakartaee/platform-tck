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
 * @(#)dbMetaClient11.java	1.30 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta11;

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
 * The dbMetaClient11 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class dbMetaClient11EJB extends dbMetaClient11 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta11";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "dbMeta11_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(dbMetaClient11.class, ServiceEETest.class, EETest.class);

		URL resURL = dbMetaClient11EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = dbMetaClient11EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta11/dbMeta11_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "dbMeta11_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(dbMetaClient11.class, ServiceEETest.class, EETest.class);

		resURL = dbMetaClient11EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta11/dbMeta11_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = dbMetaClient11EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta11/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dbMeta11_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient11EJB theTests = new dbMetaClient11EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSupportsTransactionIsolationLevel2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsTransactionIsolationLevel(int isolevel) method
	 * on that object with the isolation level TRANSACTION_READ_COMMITTED. It should
	 * return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsTransactionIsolationLevel2() throws Exception {
		super.testSupportsTransactionIsolationLevel2();
	}

	/*
	 * @testName: testSupportsTransactionIsolationLevel3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsTransactionIsolationLevel(int isolevel) method
	 * on that object with the isolation level TRANSACTION_READ_UNCOMMITTED. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsTransactionIsolationLevel3() throws Exception {
		super.testSupportsTransactionIsolationLevel3();
	}

	/*
	 * @testName: testSupportsTransactionIsolationLevel4
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsTransactionIsolationLevel(int isolevel) method
	 * on that object with isolation level TRANSACTION_REPEATABLE_READ. It should
	 * return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsTransactionIsolationLevel4() throws Exception {
		super.testSupportsTransactionIsolationLevel4();

	}

	/*
	 * @testName: testSupportsTransactionIsolationLevel5
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsTransactionIsolationLevel(int isolevel) method
	 * on that object with isolation level TRANSACTION_SERIALIZABLE. It should
	 * return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsTransactionIsolationLevel5() throws Exception {
		super.testSupportsTransactionIsolationLevel5();
	}

	/*
	 * @testName: testGetColumnPrivileges
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1056; JDBC:JAVADOC:1057;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getColumnPrivileges() method on that object. It should
	 * return a ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetColumnPrivileges() throws Exception {
		super.testGetColumnPrivileges();
	}

	/*
	 * @testName: testGetTablePrivileges
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1058; JDBC:JAVADOC:1059;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getTablePrivileges() method on that object. It should
	 * return a ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTablePrivileges() throws Exception {
		super.testGetTablePrivileges();
	}

	/*
	 * @testName: testGetBestRowIdentifier1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getBestRowIdentifier() method on that object. It should
	 * return a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBestRowIdentifier1() throws Exception {
		super.testGetBestRowIdentifier1();
	}

	/*
	 * @testName: testGetBestRowIdentifier2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getBestRowIdentifier() method on that object. It should
	 * return a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBestRowIdentifier2() throws Exception {
		super.testGetBestRowIdentifier2();
	}

	/*
	 * @testName: testGetBestRowIdentifier3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getBestRowIdentifier() method on that object. It should
	 * return a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBestRowIdentifier3() throws Exception {
		super.testGetBestRowIdentifier3();
	}

	/*
	 * @testName: testGetBestRowIdentifier4
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getBestRowIdentifier() method on that object. It should
	 * return a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBestRowIdentifier4() throws Exception {
		super.testGetBestRowIdentifier4();
	}

	/*
	 * @testName: testGetBestRowIdentifier5
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getBestRowIdentifier() method on that object. It should
	 * return a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBestRowIdentifier5() throws Exception {
		super.testGetBestRowIdentifier5();
	}

	/*
	 * @testName: testGetBestRowIdentifier6
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getBestRowIdentifier() method on that object. It should
	 * return a ResultSet object
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBestRowIdentifier6() throws Exception {
		super.testGetBestRowIdentifier6();
	}

	/*
	 * @testName: testGetBestRowIdentifier7
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getBestRowIdentifier() method on that object. It should
	 * return a ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBestRowIdentifier7() throws Exception {
		super.testGetBestRowIdentifier7();
	}

	/*
	 * @testName: testGetVersionColumns
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1062; JDBC:JAVADOC:1063;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getVersionColumns() method on that object. It should
	 * return a ResultSet object.Compare the column names Validate the column names
	 * and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetVersionColumns() throws Exception {
		super.testGetVersionColumns();
	}

	/*
	 * @testName: testGetPrimaryKeys
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1064; JDBC:JAVADOC:1065;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getPrimaryKeys() method on that object. It should
	 * return a ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetPrimaryKeys() throws Exception {
		super.testGetPrimaryKeys();
	}

	/*
	 * @testName: testGetImportedKeys
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1066; JDBC:JAVADOC:1067;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getImportedKeys() method on that object. It should
	 * return a ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetImportedKeys() throws Exception {
		super.testGetImportedKeys();
	}

	/*
	 * @testName: testGetExportedKeys
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1068; JDBC:JAVADOC:1069;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getExportedKeys() method on that object. It should
	 * return a ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetExportedKeys() throws Exception {
		super.testGetExportedKeys();
	}

	/*
	 * @testName: testGetCrossReference
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1070; JDBC:JAVADOC:1071;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getCrossReference() method on that object. It should
	 * return a ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetCrossReference() throws Exception {
		super.testGetCrossReference();
	}

	/*
	 * @testName: testGetIndexInfo1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getIndexInfo() method on that object. It should return
	 * a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetIndexInfo1() throws Exception {
		super.testGetIndexInfo1();
	}

	/*
	 * @testName: testGetIndexInfo2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getIndexInfo() method on that object. It should return
	 * a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetIndexInfo2() throws Exception {
		super.testGetIndexInfo2();
	}

	/*
	 * @testName: testGetIndexInfo3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getIndexInfo() method on that object. It should return
	 * a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetIndexInfo3() throws Exception {
		super.testGetIndexInfo3();
	}

	/*
	 * @testName: testGetIndexInfo4
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getIndexInfo() method on that object. It should return
	 * a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetIndexInfo4() throws Exception {
		super.testGetIndexInfo4();
	}

	/*
	 * @testName: testGetIndexInfo5
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getIndexInfo() method on that object. It should return
	 * a ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetIndexInfo5() throws Exception {
		super.testGetIndexInfo5();
	}

}
