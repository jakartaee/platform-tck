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
 * @(#)dbMetaClient12.java	1.2 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta12;

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

/**
 * The dbMetaClient class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.0, 16/09/2002
 */

@Tag("tck-javatest")
@Tag("web")
public class dbMetaClient12JSP extends dbMetaClient12 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta12";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "dbMeta12_jsp_vehicle_web.war");
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

		archive.addClasses(dbMetaClient12.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = dbMetaClient12JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = dbMetaClient12JSP.class.getResource("dbMeta12_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, dbMetaClient12JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(dbMetaClient12JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient12JSP theTests = new dbMetaClient12JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetSQLStateType
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1130; JDBC:JAVADOC:1131;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getSQLStateType() method on that object. It should
	 * return an integer value.
	 * 
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetSQLStateType() throws Exception {
		super.testGetSQLStateType();
	}

	/*
	 * @testName: testGetDatabaseMinorVersion
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1124; JDBC:JAVADOC:1125;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getDatabaseMinorVersion() method on that object. It
	 * should return an integer value.
	 * 
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDatabaseMinorVersion() throws Exception {
		super.testGetDatabaseMinorVersion();
	}

	/*
	 * @testName: testGetDatabaseMajorVersion
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1122; JDBC:JAVADOC:1123;
	 * JavaEE:SPEC:193;
	 * 
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getDatabaseMajorVersion() method on that object. It
	 * should return an integer value.
	 * 
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetDatabaseMajorVersion() throws Exception {
		super.testGetDatabaseMajorVersion();
	}

	/*
	 * @testName: testGetJDBCMinorVersion
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1128; JDBC:JAVADOC:1129;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getJDBCMinorVersion() method on that object. It should
	 * return an integer value.
	 * 
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetJDBCMinorVersion() throws Exception {
		super.testGetJDBCMinorVersion();
	}

	/*
	 * @testName: testGetJDBCMajorVersion
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1126; JDBC:JAVADOC:1127;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the getJDBCMajorVersion() method on that object. It should
	 * return an integer value.
	 * 
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetJDBCMajorVersion() throws Exception {
		super.testGetJDBCMajorVersion();
	}

	/*
	 * @testName: testSupportsSavepoints
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1104; JDBC:JAVADOC:1105;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsSavepoints() method. It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsSavepoints() throws Exception {
		super.testSupportsSavepoints();
	}

	/*
	 * @testName: testSupportsNamedParameters
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1106; JDBC:JAVADOC:1107;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsSavepoints() method. It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsNamedParameters() throws Exception {
		super.testSupportsNamedParameters();
	}

	/*
	 * @testName: testSupportsMultipleOpenResults
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1108; JDBC:JAVADOC:1109;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsMultipleOpenResults() method. It should return
	 * a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsMultipleOpenResults() throws Exception {
		super.testSupportsMultipleOpenResults();
	}

	/*
	 * @testName: testSupportsGetGeneratedKeys
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1110; JDBC:JAVADOC:1111;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsGetGeneratedKeys() method. It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsGetGeneratedKeys() throws Exception {
		super.testSupportsGetGeneratedKeys();
	}

	/*
	 * @testName: testSupportsResultSetHoldability01
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1118; JDBC:JAVADOC:1119;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetHoldability(int holdability) method.
	 * It should return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsResultSetHoldability01() throws Exception {
		super.testSupportsResultSetHoldability01();
	}

	/*
	 * @testName: testSupportsResultSetHoldability02
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1118; JDBC:JAVADOC:1119;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetHoldability(int holdability) method.
	 * It should return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsResultSetHoldability02() throws Exception {
		super.testSupportsResultSetHoldability02();
	}

	/*
	 * @testName: testGetResultSetHoldability
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1120; JDBC:JAVADOC:1121;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get a DatabaseMetadata object from the connection to the
	 * database and call the supportsResultSetHoldability(int holdability) method.
	 * It should return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetResultSetHoldability() throws Exception {
		super.testGetResultSetHoldability();
	}

}
