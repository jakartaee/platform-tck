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
 * @(#)dbMetaClient7.java	1.26 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta7;

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
 * The dbMetaClient7 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class dbMetaClient7EJB extends dbMetaClient7 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta7";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "dbMeta7_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(dbMetaClient7.class, ServiceEETest.class, EETest.class);

		URL resURL = dbMetaClient7EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = dbMetaClient7EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta7/dbMeta7_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "dbMeta7_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(dbMetaClient7.class, ServiceEETest.class, EETest.class);

		resURL = dbMetaClient7EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta7/dbMeta7_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = dbMetaClient7EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta7/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dbMeta7_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient7EJB theTests = new dbMetaClient7EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSupportsUnionAll
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:976; JDBC:JAVADOC:977;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsUnionAll() method on that object. It should
	 * return a boolean value; eithet true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsUnionAll() throws Exception {
		super.testSupportsUnionAll();
	}

	/*
	 * @testName: testSupportsOpenCursorsAcrossCommit
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:978; JDBC:JAVADOC:979;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsOpenCursorsAcrossCommit() method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsOpenCursorsAcrossCommit() throws Exception {
		super.testSupportsOpenCursorsAcrossCommit();
	}

	/*
	 * @testName: testSupportsOpenCursorsAcrossRollback
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:980; JDBC:JAVADOC:981;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsOpenCursorsAcrossRollback() method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsOpenCursorsAcrossRollback() throws Exception {
		super.testSupportsOpenCursorsAcrossRollback();
	}

	/*
	 * @testName: testSupportsOpenStatementsAcrossCommit
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:982; JDBC:JAVADOC:983;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsOpenStatementsAcrossCommit() method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsOpenStatementsAcrossCommit() throws Exception {
		super.testSupportsOpenStatementsAcrossCommit();
	}

	/*
	 * @testName: testSupportsOpenStatementsAcrossRollback
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:984; JDBC:JAVADOC:985;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsOpenStatementsAcrossRollback() method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsOpenStatementsAcrossRollback() throws Exception {
		super.testSupportsOpenStatementsAcrossRollback();
	}

	/*
	 * @testName: testGetMaxBinaryLiteralLength
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:986; JDBC:JAVADOC:987;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxBinaryLiteralLength() method on that object. It
	 * should return an integer value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxBinaryLiteralLength() throws Exception {
		super.testGetMaxBinaryLiteralLength();
	}

	/*
	 * @testName: testGetMaxCharLiteralLength
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:988; JDBC:JAVADOC:989;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxCharLiteralLength() method on that object. It
	 * should return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxCharLiteralLength() throws Exception {
		super.testGetMaxCharLiteralLength();
	}

	/*
	 * @testName: testGetMaxColumnNameLength
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:990; JDBC:JAVADOC:991;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxColumnNameLength() method on that object. It
	 * should return an integer value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxColumnNameLength() throws Exception {
		super.testGetMaxColumnNameLength();
	}

	/*
	 * @testName: testGetMaxColumnsInGroupBy
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:992; JDBC:JAVADOC:993;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxColumnsInGroupBy() method on that object. It
	 * should return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxColumnsInGroupBy() throws Exception {
		super.testGetMaxColumnsInGroupBy();
	}

	/*
	 * @testName: testGetMaxColumnsInIndex
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:994; JDBC:JAVADOC:995;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxColumnsInIndex() method on that object. It should
	 * return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxColumnsInIndex() throws Exception {
		super.testGetMaxColumnsInIndex();
	}

	/*
	 * @testName: testGetMaxColumnsInOrderBy
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:996; JDBC:JAVADOC:997;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxColumnsInOrderBy() method on that object. It
	 * should return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxColumnsInOrderBy() throws Exception {
		super.testGetMaxColumnsInOrderBy();
	}

	/*
	 * @testName: testGetMaxColumnsInSelect
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:998; JDBC:JAVADOC:999;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxColumnsInSelect() method on that object. It
	 * should return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxColumnsInSelect() throws Exception {
		super.testGetMaxColumnsInSelect();
	}

	/*
	 * @testName: testGetMaxColumnsInTable
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1000; JDBC:JAVADOC:1001;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxColumnsInTable() method on that object. It should
	 * return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxColumnsInTable() throws Exception {
		super.testGetMaxColumnsInTable();
	}

	/*
	 * @testName: testGetMaxConnections
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1002; JDBC:JAVADOC:1003;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxConnections() method on that object. It should
	 * return an integer value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxConnections() throws Exception {
		super.testGetMaxConnections();
	}

	/*
	 * @testName: testGetMaxCursorNameLength
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1004; JDBC:JAVADOC:1005;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxCursorNameLength() method on that object. It
	 * should return an integer value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxCursorNameLength() throws Exception {
		super.testGetMaxCursorNameLength();
	}

	/*
	 * @testName: testGetMaxIndexLength
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1006; JDBC:JAVADOC:1007;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxIndexLength() method on that object. It should
	 * return an integer value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxIndexLength() throws Exception {
		super.testGetMaxIndexLength();
	}

	/*
	 * @testName: testGetMaxSchemaNameLength
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1008; JDBC:JAVADOC:1009;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxSchemaNameLength() method on that object. It
	 * should return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxSchemaNameLength() throws Exception {
		super.testGetMaxSchemaNameLength();
	}

	/*
	 * @testName: testGetMaxProcedureNameLength
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1010; JDBC:JAVADOC:1011;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxProcedureNameLength() method on that object. It
	 * should return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxProcedureNameLength() throws Exception {
		super.testGetMaxProcedureNameLength();
	}

	/*
	 * @testName: testGetMaxCatalogNameLength
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1012; JDBC:JAVADOC:1013;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxCatalogNameLength() method on that object. It
	 * should return an integer value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxCatalogNameLength() throws Exception {
		super.testGetMaxCatalogNameLength();
	}

	/*
	 * @testName: testGetMaxRowSize
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1014; JDBC:JAVADOC:1015;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getMaxRowSize() method on that object. It should return
	 * an integer value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetMaxRowSize() throws Exception {
		super.testGetMaxRowSize();
	}

}
