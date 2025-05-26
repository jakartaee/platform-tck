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
 * @(#)dbMetaClient1.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta1;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
// import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The dbMetaClient1 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class dbMetaClient1JSP extends dbMetaClient1 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta1";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "dbMeta1_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(dbMetaClient1.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = dbMetaClient1JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = dbMetaClient1JSP.class.getResource("dbMeta1_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, dbMetaClient1JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(dbMetaClient1JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient1JSP theTests = new dbMetaClient1JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSupportsStoredProcedures
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:962; JDBC:JAVADOC:963;
	 * JavaEE:SPEC:193; JavaEE:SPEC:180;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsStoredprocedures() method It should return true
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsStoredProcedures() throws Exception {
		super.testSupportsStoredProcedures();
	}

	/*
	 * @testName: testAllProceduresAreCallable
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:808; JDBC:JAVADOC:809;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the allProceduresAreCallable() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testAllProceduresAreCallable() throws Exception {
		super.testAllProceduresAreCallable();
	}

	/*
	 * @testName: testAllTablesAreSelectable
	 * 
	 * @assertion_ids: JDBC:SPEC:8 ; JDBC:JAVADOC:810 ; JDBC:JAVADOC:811;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the allTablesAreSelectable() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testAllTablesAreSelectable() throws Exception {
		super.testAllTablesAreSelectable();
	}

	/*
	 * @testName: testGetURL
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:812; JDBC:JAVADOC:813;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getURL() method It should return a String or null if it
	 * cannot be generated
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetURL() throws Exception {
		super.testGetURL();
	}

	/*
	 * @testName: testGetUserName
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:814; JDBC:JAVADOC:815;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getUserName() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetUserName() throws Exception {
		super.testGetUserName();
	}

	/*
	 * @testName: testIsReadOnly
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:816; JDBC:JAVADOC:817;
	 * JavaEE:SPEC:193;
	 * 
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the isReadOnly() method It should return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsReadOnly() throws Exception {
		super.testIsReadOnly();
	}

	/*
	 * @testName: testNullsAreSortedHigh
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:818; JDBC:JAVADOC:819;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the nullsAreSortedHigh() method It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testNullsAreSortedHigh() throws Exception {
		super.testNullsAreSortedHigh();
	}

	/*
	 * @testName: testNullsAreSortedLow
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:820; JDBC:JAVADOC:821;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the nullsAreSortedLow() method It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testNullsAreSortedLow() throws Exception {
		super.testNullsAreSortedLow();
	}

	/*
	 * @testName: testNullsAreSortedAtStart
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:822; JDBC:JAVADOC:823;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the nullsAreSortedAtStart() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testNullsAreSortedAtStart() throws Exception {
		super.testNullsAreSortedAtStart();
	}

	/*
	 * @testName: testNullsAreSortedAtEnd
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:824; JDBC:JAVADOC:825;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the nullsAreSortedAtEnd() method It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testNullsAreSortedAtEnd() throws Exception {
		super.testNullsAreSortedAtEnd();
	}

	/*
	 * @testName: testGetDatabaseProductName
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:826; JDBC:JAVADOC:827;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getDatabaseProductName() method It should return a
	 * String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDatabaseProductName() throws Exception {
		super.testGetDatabaseProductName();
	}

	/*
	 * @testName: testGetDatabaseProductVersion
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:828; JDBC:JAVADOC:829;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getDatabaseProductVersion() method It should return a
	 * String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDatabaseProductVersion() throws Exception {
		super.testGetDatabaseProductVersion();
	}

	/*
	 * @testName: testGetDriverName
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:830; JDBC:JAVADOC:831;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getDriverName() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDriverName() throws Exception {
		super.testGetDriverName();
	}

	/*
	 * @testName: testGetDriverVersion
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:832; JDBC:JAVADOC:833;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getDriverVersion() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDriverVersion() throws Exception {
		super.testGetDriverVersion();
	}

	/*
	 * @testName: testGetDriverMajorVersion
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:834; JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getDriverMajorVersion() method It should return a
	 * Integer value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDriverMajorVersion() throws Exception {
		super.testGetDriverMajorVersion();
	}

	/*
	 * @testName: testGetDriverMinorVersion
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:835; JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getDriverMinorVersion() method It should return a
	 * Integer value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDriverMinorVersion() throws Exception {
		super.testGetDriverMinorVersion();
	}

	/*
	 * @testName: testUsesLocalFilePerTable
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:838; JDBC:JAVADOC:839;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the usesLocalFilePerTable() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testUsesLocalFilePerTable() throws Exception {
		super.testUsesLocalFilePerTable();
	}

	/*
	 * @testName: testSupportsMixedCaseIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:840; JDBC:JAVADOC:841;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsMixedCaseIdentifiers() method It should return
	 * a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsMixedCaseIdentifiers() throws Exception {
		super.testSupportsMixedCaseIdentifiers();
	}

	/*
	 * @testName: testStoresUpperCaseIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:842; JDBC:JAVADOC:843;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesUpperCaseIdentifiers() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testStoresUpperCaseIdentifiers() throws Exception {
		super.testStoresUpperCaseIdentifiers();
	}

	/*
	 * @testName: testStoresLowerCaseIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:844; JDBC:JAVADOC:845;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesLowerCaseIdentifiers() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testStoresLowerCaseIdentifiers() throws Exception {
		super.testStoresLowerCaseIdentifiers();
	}
}
