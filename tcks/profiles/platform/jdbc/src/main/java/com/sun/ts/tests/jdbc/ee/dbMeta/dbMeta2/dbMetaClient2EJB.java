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
 * @(#)dbMetaClient2.java	1.26 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta2;

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
 * The dbMetaClient2 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class dbMetaClient2EJB extends dbMetaClient2 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta2";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "dbMeta2_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(dbMetaClient2.class, ServiceEETest.class, EETest.class);

		URL resURL = dbMetaClient2EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = dbMetaClient2EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta2/dbMeta2_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "dbMeta2_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(dbMetaClient2.class, ServiceEETest.class, EETest.class);

		resURL = dbMetaClient2EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta2/dbMeta2_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = dbMetaClient2EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta2/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dbMeta2_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient2EJB theTests = new dbMetaClient2EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testStoresMixedCaseIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:846; JDBC:JAVADOC:847;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesMixedCaseIdentifiers() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testStoresMixedCaseIdentifiers() throws Exception {
		super.testStoresMixedCaseIdentifiers();
	}

	/*
	 * @testName: testSupportsMixedCaseQuotedIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:848; JDBC:JAVADOC:849;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsMixedCaseQuotedIdentifiers() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsMixedCaseQuotedIdentifiers() throws Exception {
		super.testStoresMixedCaseIdentifiers();
	}

	/*
	 * @testName: testStoresUpperCaseQuotedIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:850; JDBC:JAVADOC:851;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesUpperCaseQuotedIdentifiers() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testStoresUpperCaseQuotedIdentifiers() throws Exception {
		super.testStoresUpperCaseQuotedIdentifiers();
	}

	/*
	 * @testName: testStoresLowerCaseQuotedIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:852; JDBC:JAVADOC:853;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesLowerCaseQuotedIdentifiers() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testStoresLowerCaseQuotedIdentifiers() throws Exception {
		super.testStoresLowerCaseQuotedIdentifiers();
	}

	/*
	 * @testName: testStoresMixedCaseQuotedIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:854; JDBC:JAVADOC:855;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesMixedCaseQuotedIdentifiers() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testStoresMixedCaseQuotedIdentifiers() throws Exception {
		super.testStoresMixedCaseQuotedIdentifiers();
	}

	/*
	 * @testName: testGetIdentifierQuoteString
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:856; JDBC:JAVADOC:857;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getIdentifierQuoteString() method It should return a
	 * String
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetIdentifierQuoteString() throws Exception {
		super.testGetIdentifierQuoteString();
	}

	/*
	 * @testName: testGetSQLKeywords
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:858; JDBC:JAVADOC:859;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getSQLKeywords() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetSQLKeywords() throws Exception {
		super.testGetSQLKeywords();
	}

	/*
	 * @testName: testGetNumericFunctions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:860; JDBC:JAVADOC:861;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getNumericFunctions() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetNumericFunctions() throws Exception {
		super.testGetNumericFunctions();
	}

	/*
	 * @testName: testGetStringFunctions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:862; JDBC:JAVADOC:863;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getStringFunctions() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetStringFunctions() throws Exception {
		super.testGetStringFunctions();
	}

	/*
	 * @testName: testGetSystemFunctions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:864; JDBC:JAVADOC:865;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getSystemFunctions() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetSystemFunctions() throws Exception {
		super.testGetSystemFunctions();
	}

	/*
	 * @testName: testGetTimeDateFunctions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:866; JDBC:JAVADOC:867;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getTimeDateFunctions() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetTimeDateFunctions() throws Exception {
		super.testGetTimeDateFunctions();
	}

	/*
	 * @testName: testGetSearchStringEscape
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:868; JDBC:JAVADOC:869;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getSearchStringEscape() method It should return a
	 * String
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetSearchStringEscape() throws Exception {
		super.testGetSearchStringEscape();
	}

	/*
	 * @testName: testGetExtraNameCharacters
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:870; JDBC:JAVADOC:871;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getExtraNameCharacters() method It should return a
	 * String
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetExtraNameCharacters() throws Exception {
		super.testGetExtraNameCharacters();
	}

	/*
	 * @testName: testSupportsAlterTableWithAddColumn
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:872; JDBC:JAVADOC:873;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsAlterTableWithAddColumn() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsAlterTableWithAddColumn() throws Exception {
		super.testSupportsAlterTableWithAddColumn();
	}

	/*
	 * @testName: testSupportsAlterTableWithDropColumn
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:874; JDBC:JAVADOC:875;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsAlterTableWithDropColumn() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsAlterTableWithDropColumn() throws Exception {
		super.testSupportsAlterTableWithDropColumn();
	}

	/*
	 * @testName: testSupportsColumnAliasing
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:876; JDBC:JAVADOC:877;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsColumnAliasing() method It should return a true
	 * value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsColumnAliasing() throws Exception {
		super.testSupportsColumnAliasing();
	}

	/*
	 * @testName: testNullPlusNonNullIsNull
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:878; JDBC:JAVADOC:879;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the nullPlusNonNullIsNull() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testNullPlusNonNullIsNull() throws Exception {
		super.testNullPlusNonNullIsNull();
	}

	/*
	 * @testName: testSupportsConvert
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:880; JDBC:JAVADOC:881;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert() method It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert() throws Exception {
		super.testSupportsConvert();
	}

	/*
	 * @testName: testSupportsConvert01
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(ARRAY, VARCHAR) method It should return
	 * a boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert01() throws Exception {
		super.testSupportsConvert01();
	}

	/*
	 * @testName: testSupportsConvert02
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(BIGINT, VARCHAR) method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert02() throws Exception {
		super.testSupportsConvert02();
	}

}
