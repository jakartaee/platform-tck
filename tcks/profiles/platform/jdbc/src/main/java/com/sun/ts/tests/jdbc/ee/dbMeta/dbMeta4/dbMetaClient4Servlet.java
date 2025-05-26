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
 * @(#)dbMetaClient4.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta4;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
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
 * The dbMetaClient4 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class dbMetaClient4Servlet extends dbMetaClient4 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta4";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "dbMeta4_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(dbMetaClient4.class, ServiceEETest.class, EETest.class);
		// The servlet descriptor
		URL servletUrl = dbMetaClient4Servlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
// The sun servlet descriptor
		URL sunServletUrl = dbMetaClient4Servlet.class.getResource("dbMeta4_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
// Call the archive processor
		archiveProcessor.processWebArchive(archive, dbMetaClient4Servlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient4Servlet theTests = new dbMetaClient4Servlet();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSupportsConvert23
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(STRUCT, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert23() throws Exception {
		super.testSupportsConvert23();
	}

	/*
	 * @testName: testSupportsConvert24
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(TIME, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert24() throws Exception {
		super.testSupportsConvert24();
	}

	/*
	 * @testName: testSupportsConvert25
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(TIMESTAMP, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert25() throws Exception {
		super.testSupportsConvert25();
	}

	/*
	 * @testName: testSupportsConvert26
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(TINYINT, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert26() throws Exception {
		super.testSupportsConvert26();
	}

	/*
	 * @testName: testSupportsConvert27
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(VARBINARY, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert27() throws Exception {
		super.testSupportsConvert27();
	}

	/*
	 * @testName: testSupportsConvert28
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(BIGINT, INTEGER) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert28() throws Exception {
		super.testSupportsConvert28();
	}

	/*
	 * @testName: testSupportsConvert29
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(BIT, INTEGER) method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert29() throws Exception {
		super.testSupportsConvert29();
	}

	/*
	 * @testName: testSupportsConvert30
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(DATE, INTEGER) method onn that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert30() throws Exception {
		super.testSupportsConvert30();
	}

	/*
	 * @testName: testSupportsConvert31
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(DECIMAL, INTEGER) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert31() throws Exception {
		super.testSupportsConvert31();
	}

	/*
	 * @testName: testSupportsConvert32
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(DOUBLE, INTEGER) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert32() throws Exception {
		super.testSupportsConvert32();
	}

	/*
	 * @testName: testSupportsConvert33
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(FLOAT, INTEGER) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert33() throws Exception {
		super.testSupportsConvert33();
	}

	/*
	 * @testName: testSupportsConvert34
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(NUMERIC, INTEGER) method on that
	 * object. It should return a boolean value; true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert34() throws Exception {
		super.testSupportsConvert34();
	}

	/*
	 * @testName: testSupportsConvert35
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(REAL, INTEGER) method on that object.
	 * It should return a boolean value; either true false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert35() throws Exception {
		super.testSupportsConvert35();
	}

	/*
	 * @testName: testSupportsConvert36
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(SMALLINT, INTEGER) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert36() throws Exception {
		super.testSupportsConvert36();
	}

	/*
	 * @testName: testSupportsConvert37
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(TINYINT, INTEGER) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsConvert37() throws Exception {
		super.testSupportsConvert37();
	}

	/*
	 * @testName: testSupportsTableCorrelationNames
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:884; JDBC:JAVADOC:885;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsTableCorrelationNames() method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsTableCorrelationNames() throws Exception {
		super.testSupportsTableCorrelationNames();
	}

	/*
	 * @testName: testSupportsDifferentTableCorrelationNames
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:886; JDBC:JAVADOC:887;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsDifferentTableCorrelationNames() method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsDifferentTableCorrelationNames() throws Exception {
		super.testSupportsDifferentTableCorrelationNames();
	}

	/*
	 * @testName: testSupportsExpressionsInOrderBy
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:888; JDBC:JAVADOC:889;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsExpressionsInOrderBy() method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsExpressionsInOrderBy() throws Exception {
		super.testSupportsExpressionsInOrderBy();
	}

	/*
	 * @testName: testSupportsOrderByUnrelated
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:890; JDBC:JAVADOC:891;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsOrderByUnrelated() method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsOrderByUnrelated() throws Exception {
		super.testSupportsOrderByUnrelated();
	}

	/*
	 * @testName: testSupportsGroupBy
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:892; JDBC:JAVADOC:893;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsGroupBy() method on that object. It should
	 * return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testSupportsGroupBy() throws Exception {
		super.testSupportsGroupBy();
	}
}
