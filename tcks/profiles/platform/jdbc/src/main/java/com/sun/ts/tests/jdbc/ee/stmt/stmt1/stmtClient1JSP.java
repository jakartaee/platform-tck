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
 * @(#)stmtClient1.java	1.23 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.stmt.stmt1;

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

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The stmtClient1 class tests methods of Statement interface using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")

public class stmtClient1JSP extends stmtClient1 implements Serializable {
	private static final String testName = "jdbc.ee.stmt.stmt1";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "stmt1_jsp_vehicle_web.war");
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

		// The jsp descriptor
		URL jspUrl = stmtClient1JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = stmtClient1JSP.class.getResource("stmt1_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, stmtClient1JSP.class, sunJSPUrl);

		archive.addClasses(stmtClient1.class, ServiceEETest.class, EETest.class);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		stmtClient1JSP theTests = new stmtClient1JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testClose
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:139; JDBC:JAVADOC:140;
	 * 
	 * @test_Strategy: Get a Statement object and call close() method and call
	 * executeQuery() method to check and it should throw SQLException
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testClose() throws Exception {
		super.testClose();
	}

	/*
	 * @testName: testExecute01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:163; JDBC:JAVADOC:164;
	 * 
	 * @test_Strategy: Call execute(String sql) of updating a row It should return a
	 * boolean value and the value should be equal to false
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testExecute01() throws Exception {
		super.testExecute01();
	}

	/*
	 * @testName: testExecute02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:163; JDBC:JAVADOC:164;
	 * 
	 * @test_Strategy: Get a Statement object and call execute(String sql) of
	 * selecting rows from the database It should return a boolean value and the
	 * value should be equal to true
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testExecute02() throws Exception {
		super.testExecute02();
	}

	/*
	 * @testName: testExecuteQuery01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
	 * 
	 * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
	 * select a row from the database It should return a ResultSet object
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testExecuteQuery01() throws Exception {
		super.testExecuteQuery01();
	}

	/*
	 * @testName: testExecuteQuery02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
	 * 
	 * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
	 * select a non-existent row from the database It should return a ResultSet
	 * object which is empty and call ResultSet.next() method to check and it should
	 * return a false
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testExecuteQuery02() throws Exception {
		super.testExecuteQuery02();
	}

	/*
	 * @testName: testExecuteQuery03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:135; JDBC:JAVADOC:136;
	 * 
	 *
	 * @test_Strategy: Get a Statement object and call executeQuery(String sql) to
	 * insert a row to the database It should throw SQLException
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testExecuteQuery03() throws Exception {
		super.testExecuteQuery03();
	}

	/*
	 * @testName: testExecuteUpdate01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:137; JDBC:JAVADOC:138;
	 * 
	 * @test_Strategy: Get a Statement object and call executeUpdate(String sql) It
	 * should return an int value which is equal to row count
	 */
	@Test
	@TargetVehicle("jsp")
	public void testExecuteUpdate01() throws Exception {
		super.testExecuteUpdate01();
	}

	/*
	 * @testName: testExecuteUpdate03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:137; JDBC:JAVADOC:138;
	 * 
	 * @test_Strategy: Get a Statement object and call executeUpdate(String sql) for
	 * selecting row from the table It should throw a SQL Exception
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testExecuteUpdate03() throws Exception {
		super.testExecuteUpdate03();
	}

	/*
	 * @testName: testGetFetchDirection
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:173; JDBC:JAVADOC:174;
	 * JDBC:JAVADOC:356;
	 * 
	 * @test_Strategy: Get a Statement object and call the getFetchDirection()
	 * method It should return a int value and the value should be equal to any of
	 * the values FETCH_FORWARD or FETCH_REVERSE or FETCH_UNKNOWN
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetFetchDirection() throws Exception {
		super.testGetFetchDirection();
	}

	/*
	 * @testName: testGetFetchSize
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:177; JDBC:JAVADOC:178;
	 * 
	 * @test_Strategy: Get a ResultSet object and call the getFetchSize() method It
	 * should return a int value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetFetchSize() throws Exception {
		super.testGetFetchSize();
	}

	/*
	 * @testName: testGetMaxFieldSize
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:141; JDBC:JAVADOC:142;
	 * 
	 * @test_Strategy: Get a Statement object and call the getMaxFieldSize() method
	 * It should return a int value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetMaxFieldSize() throws Exception {
		super.testGetMaxFieldSize();
	}

	/*
	 * @testName: testGetMaxRows
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:145; JDBC:JAVADOC:146;
	 * 
	 * @test_Strategy: Get a Statement object and call the getMaxRows() method It
	 * should return a int value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetMaxRows() throws Exception {
		super.testGetMaxRows();
	}

	/*
	 * @testName: testGetMoreResults01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
	 * 
	 * @test_Strategy: Get a Statement object and call the execute() method for
	 * selecting a row and call getMoreResults() method It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetMoreResults01() throws Exception {
		super.testGetMoreResults01();
	}

	/*
	 * @testName: testGetMoreResults02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
	 * 
	 * @test_Strategy: Get a Statement object and call the execute() method for
	 * selecting a non-existent row and call getMoreResults() method It should
	 * return a boolean value and the value should be equal to false
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetMoreResults02() throws Exception {
		super.testGetMoreResults02();
	}

	/*
	 * @testName: testGetMoreResults03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:169; JDBC:JAVADOC:170;
	 * 
	 * @test_Strategy: Get a Statement object and call the execute() method for
	 * updating a row and call getMoreResults() method It should return a boolean
	 * value and the value should be equal to false
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetMoreResults03() throws Exception {
		super.testGetMoreResults03();
	}

	/*
	 * @testName: testGetQueryTimeout
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:151; JDBC:JAVADOC:152;
	 * 
	 * @test_Strategy: Get a Statement object and call getQueryTimeout() method It
	 * should return a int value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetQueryTimeout() throws Exception {
		super.testGetQueryTimeout();
	}

	/*
	 * @testName: testGetResultSet01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:165; JDBC:JAVADOC:166;
	 * 
	 * @test_Strategy: Get a Statement object and call execute() method for
	 * selecting a row and call getResultSet() method It should return a ResultSet
	 * object
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetResultSet01() throws Exception {
		super.testGetResultSet01();
	}

}
