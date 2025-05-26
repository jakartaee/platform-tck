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
 * @(#)dbMetaClient5.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta5;

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
 * The dbMetaClient5 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class dbMetaClient5EJB extends dbMetaClient5 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta5";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "dbMeta5_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(dbMetaClient5.class, ServiceEETest.class, EETest.class);

		URL resURL = dbMetaClient5EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = dbMetaClient5EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta5/dbMeta5_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "dbMeta5_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(dbMetaClient5.class, ServiceEETest.class, EETest.class);

		resURL = dbMetaClient5EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta5/dbMeta5_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = dbMetaClient5EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta5/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dbMeta5_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient5EJB theTests = new dbMetaClient5EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSupportsGroupByUnrelated
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:894; JDBC:JAVADOC:895;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsGroupByUnrelated() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsGroupByUnrelated() throws Exception {
		super.testSupportsGroupByUnrelated();
	}

	/*
	 * @testName: testSupportsGroupByBeyondSelect
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:896; JDBC:JAVADOC:897;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsGroupByBeyondSelect() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsGroupByBeyondSelect() throws Exception {
		super.testSupportsGroupByBeyondSelect();
	}

	/*
	 * @testName: testSupportsLikeEscapeClause
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:898; JDBC:JAVADOC:899;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsLikeEscapeClause() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsLikeEscapeClause() throws Exception {
		super.testSupportsLikeEscapeClause();
	}

	/*
	 * @testName: testSupportsMultipleResultSets
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:900; JDBC:JAVADOC:901;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsMultipleResultSets() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsMultipleResultSets() throws Exception {
		super.testSupportsMultipleResultSets();
	}

	/*
	 * @testName: testSupportsMultipleTransactions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:902; JDBC:JAVADOC:903;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsMultipleTransactions() method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsMultipleTransactions() throws Exception {
		super.testSupportsMultipleTransactions();
	}

	/*
	 * @testName: testSupportsNonNullableColumns
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:904; JDBC:JAVADOC:905;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsNonNullableColumns() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsNonNullableColumns() throws Exception {
		super.testSupportsNonNullableColumns();
	}

	/*
	 * @testName: testSupportsMinimumSQLGrammar
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:906; JDBC:JAVADOC:907;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsMinimumSQLGrammar() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsMinimumSQLGrammar() throws Exception {
		super.testSupportsMinimumSQLGrammar();
	}

	/*
	 * @testName: testSupportsCoreSQLGrammar
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:908; JDBC:JAVADOC:909;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsCoreSQLGrammar() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsCoreSQLGrammar() throws Exception {
		super.testSupportsCoreSQLGrammar();
	}

	/*
	 * @testName: testSupportsExtendedSQLGrammar
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:910; JDBC:JAVADOC:911;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsExtendedSQLGrammar() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsExtendedSQLGrammar() throws Exception {
		super.testSupportsExtendedSQLGrammar();
	}

	/*
	 * @testName: testSupportsANSI92EntryLevelSQL
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:912; JDBC:JAVADOC:913;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsANSI92EntryLevelSQL() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsANSI92EntryLevelSQL() throws Exception {
		super.testSupportsANSI92EntryLevelSQL();
	}

	/*
	 * @testName: testSupportsANSI92IntermediateSQL
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:914; JDBC:JAVADOC:915;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsANSI92IntermediateSQL() method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsANSI92IntermediateSQL() throws Exception {
		super.testSupportsANSI92IntermediateSQL();
	}

	/*
	 * @testName: testSupportsANSI92FullSQL
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:916; JDBC:JAVADOC:917;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsANSI92FullSQL() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsANSI92FullSQL() throws Exception {
		super.testSupportsANSI92FullSQL();
	}

	/*
	 * @testName: testSupportsIntegrityEnhancementFacility
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:918; JDBC:JAVADOC:919;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsIntegrityEnhancementFacility() method onn that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsIntegrityEnhancementFacility() throws Exception {
		super.testSupportsIntegrityEnhancementFacility();
	}

	/*
	 * @testName: testSupportsOuterJoins
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:920; JDBC:JAVADOC:921;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsOuterJoins() method on that object. It should
	 * return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsOuterJoins() throws Exception {
		super.testSupportsOuterJoins();
	}

	/*
	 * @testName: testSupportsFullOuterJoins
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:922; JDBC:JAVADOC:923;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsFullOuterJoins() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsFullOuterJoins() throws Exception {
		super.testSupportsFullOuterJoins();
	}

	/*
	 * @testName: testSupportsLimitedOuterJoins
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:924; JDBC:JAVADOC:925;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsLimitedOuterJoins() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsLimitedOuterJoins() throws Exception {
		super.testSupportsLimitedOuterJoins();
	}

	/*
	 * @testName: testGetSchemaTerm
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:926; JDBC:JAVADOC:927;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getSchemaTerm() method on that object. It should return
	 * a String and NULL if it cannot be generated.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetSchemaTerm() throws Exception {
		super.testGetSchemaTerm();
	}

	/*
	 * @testName: testGetProcedureTerm
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:928; JDBC:JAVADOC:929;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getProcedureTerm() method on that object. It should
	 * return a String and NULL if it cannot be generated.;
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetProcedureTerm() throws Exception {
		super.testGetProcedureTerm();
	}

	/*
	 * @testName: testGetCatalogTerm
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:930; JDBC:JAVADOC:931;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getCatalogTerm() method on that object. It should
	 * return a String and NULL if it cannot be returned.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetCatalogTerm() throws Exception {
		super.testGetCatalogTerm();
	}

	/*
	 * @testName: testIsCatalogAtStart
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:932; JDBC:JAVADOC:933;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the isCatalogAtStart() method on that object. It should
	 * return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testIsCatalogAtStart() throws Exception {
		super.testIsCatalogAtStart();
	}

}
