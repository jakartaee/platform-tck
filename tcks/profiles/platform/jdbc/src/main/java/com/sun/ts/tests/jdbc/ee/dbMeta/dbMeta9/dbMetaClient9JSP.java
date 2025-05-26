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
 * The dbMetaClient9 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class dbMetaClient9JSP extends dbMetaClient9 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta9";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "dbMeta9_jsp_vehicle_web.war");
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

		archive.addClasses(dbMetaClient9.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = dbMetaClient9JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = dbMetaClient9JSP.class.getResource("dbMeta9_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, dbMetaClient9JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(dbMetaClient9JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient9JSP theTests = new dbMetaClient9JSP();
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
	public void testOthersUpdatesAreVisible1() throws Exception {
		super.testOthersUpdatesAreVisible1();
	}

}
