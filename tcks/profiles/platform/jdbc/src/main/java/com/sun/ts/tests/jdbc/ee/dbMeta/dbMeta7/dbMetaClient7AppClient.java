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

public class dbMetaClient7AppClient extends dbMetaClient7 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta7";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "dbMeta7_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(dbMetaClient7.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = dbMetaClient7AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta7/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = dbMetaClient7AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, dbMetaClient7AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dbMeta7_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient7AppClient theTests = new dbMetaClient7AppClient();
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
	public void testGetMaxRowSize() throws Exception {
		super.testGetMaxRowSize();
	}

}
