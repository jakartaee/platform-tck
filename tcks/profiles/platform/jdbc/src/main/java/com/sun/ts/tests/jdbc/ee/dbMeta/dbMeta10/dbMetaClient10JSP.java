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
 * @(#)dbMetaClient10.java	1.33 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta10;

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
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The dbMetaClient class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class dbMetaClient10JSP extends dbMetaClient10 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta10";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "dbMeta10_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		archive.addClasses(dbMetaClient10.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = dbMetaClient10JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = dbMetaClient10JSP.class.getResource("dbMeta10_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, dbMetaClient10JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(dbMetaClient10JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient10JSP theTests = new dbMetaClient10JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testOthersUpdatesAreVisible2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1086; JDBC:JAVADOC:1087;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherUpdatesAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testOthersUpdatesAreVisible2() throws Exception {
		super.testOthersUpdatesAreVisible2();
	}

	/*
	 * @testName: testOthersUpdatesAreVisible3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1086; JDBC:JAVADOC:1087;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherUpdatesAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testOthersUpdatesAreVisible3() throws Exception {
		super.testOthersUpdatesAreVisible3();
	}

	/*
	 * @testName: testOthersDeletesAreVisible1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1088; JDBC:JAVADOC:1089;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherDeletesAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_FORWARD_ONLY. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testOthersDeletesAreVisible1() throws Exception {
		super.testOthersDeletesAreVisible1();
	}

	/*
	 * @testName: testOthersDeletesAreVisible2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1088; JDBC:JAVADOC:1089;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherDeletesAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testOthersDeletesAreVisible2() throws Exception {
		super.testOthersDeletesAreVisible2();
	}

	/*
	 * @testName: testOthersDeletesAreVisible3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1088; JDBC:JAVADOC:1089;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherDeletesAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testOthersDeletesAreVisible3() throws Exception {
		super.testOthersDeletesAreVisible3();
	}

	/*
	 * @testName: testOthersInsertsAreVisible1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1090; JDBC:JAVADOC:1091;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherInsertsAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_FORWARD_ONLY. It should return a boolean value;
	 * either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testOthersInsertsAreVisible1() throws Exception {
		super.testOthersInsertsAreVisible1();
	}

	/*
	 * @testName: testOthersInsertsAreVisible2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1090; JDBC:JAVADOC:1091;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherInsertsAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testOthersInsertsAreVisible2() throws Exception {
		super.testOthersInsertsAreVisible2();
	}

	/*
	 * @testName: testOthersInsertsAreVisible3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1090; JDBC:JAVADOC:1091;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the otherInsertsAreVisible(int resType) method on that
	 * object with ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testOthersInsertsAreVisible3() throws Exception {
		super.testOthersInsertsAreVisible3();
	}

	/*
	 * @testName: testUpdatesAreDetected1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1092; JDBC:JAVADOC:1093;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the updatesAreDetected() method on that object with the
	 * ResultSet Type as ResultSet.TYPE_FORWARD_ONLY. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testUpdatesAreDetected1() throws Exception {
		super.testUpdatesAreDetected1();
	}

	/*
	 * @testName: testUpdatesAreDetected2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1092; JDBC:JAVADOC:1093;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the updatesAreDetected() method on that object with the
	 * ResultSet Type as ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a
	 * boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testUpdatesAreDetected2() throws Exception {
		super.testUpdatesAreDetected2();
	}

	/*
	 * @testName: testUpdatesAreDetected3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1092; JDBC:JAVADOC:1093;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the updatesAreDetected() method on that object with the
	 * ResultSet Type as ResultSet.TYPE_SCROLL_SENSITIVE. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testUpdatesAreDetected3() throws Exception {
		super.testUpdatesAreDetected3();
	}

	/*
	 * @testName: testDeletesAreDetected1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1094; JDBC:JAVADOC:1095;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the deletesAreDetected() method on that object with the
	 * result set type as ResultSet.TYPE_FORWARD_ONLY. It should return a boolean
	 * value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testDeletesAreDetected1() throws Exception {
		super.testDeletesAreDetected1();
	}

	/*
	 * @testName: testDeletesAreDetected2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1094; JDBC:JAVADOC:1095;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the deletesAreDetected() method on that object with the
	 * result set type as ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a
	 * boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testDeletesAreDetected2() throws Exception {
		super.testDeletesAreDetected2();
	}

	/*
	 * @testName: testDeletesAreDetected3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1094; JDBC:JAVADOC:1095;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the deletesAreDetected() method on that object with the
	 * result set type as ResultSet.TYPE_SCROLL_SENSITIVE. It should return a
	 * boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testDeletesAreDetected3() throws Exception {
		super.testDeletesAreDetected3();
	}

	/*
	 * @testName: testInsertsAreDetected1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1096; JDBC:JAVADOC:1097;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the insertsAreDetected() method on that object with the
	 * result set type as ResultSet.TYPE_FORWARD_ONLY. It should return a boolean
	 * value; either or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testInsertsAreDetected1() throws Exception {
		super.testInsertsAreDetected1();
	}

	/*
	 * @testName: testInsertsAreDetected2
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1096; JDBC:JAVADOC:1097;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the insertsAreDetected() method on that object with the
	 * result set type as ResultSet.TYPE_SCROLL_INSENSITIVE. It should return a
	 * boolean value; either or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testInsertsAreDetected2() throws Exception {
		super.testInsertsAreDetected2();
	}

	/*
	 * @testName: testInsertsAreDetected3
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1096; JDBC:JAVADOC:1097;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the insertsAreDetected() method on that object with the
	 * result set type as ResultSet.TYPE_SCROLL_SENSITIVE. It should return a
	 * boolean value; either or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testInsertsAreDetected3() throws Exception {
		super.testInsertsAreDetected3();
	}

	/*
	 * @testName: testGetUDTs
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1100; JDBC:JAVADOC:1101;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getUDTs() method on that object. It should return a
	 * ResultSet object. Validate the column names and column ordering.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetUDTs() throws Exception {
		super.testGetUDTs();
	}

	/*
	 * @testName: testGetUDTs01
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1100; JDBC:JAVADOC:1101;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getUDTs() method on that object. It should return a
	 * ResultSet object. Validate the column names and column ordering.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetUDTs01() throws Exception {
		super.testGetUDTs01();
	}

	/*
	 * @testName: testSupportsTransactionIsolationLevel1
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsTransactionIsolationLevel(int isolevel) method
	 * on that object with the isolation level TRANSACTION_NONE. It should return a
	 * boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsTransactionIsolationLevel1() throws Exception {
		super.testSupportsTransactionIsolationLevel1();
	}

}
