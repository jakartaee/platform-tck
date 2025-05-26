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
 * @(#)resultSetClient45.java	1.26 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet45;

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
 * The resultSetClient45 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-javatest")
@Tag("web")

public class resultSetClient45JSP extends resultSetClient45 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet45";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet45_jsp_vehicle_web.war");
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

		archive.addClasses(resultSetClient45.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = resultSetClient45JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = resultSetClient45JSP.class.getResource("resultSet45_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, resultSetClient45JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(resultSetClient45JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient45JSP theTests = new resultSetClient45JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetString41
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the minimum value of Smallint_Tab table. Call the getString(String
	 * columnName) method to retrieve this value.Extract the minimum value of
	 * Smallint_Tab table as a String from the tssql.stmt file. Compare this value
	 * with the value returned by the getString method. Both the values should be
	 * equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString41() throws Exception {
		super.testGetString41();
	}

	/*
	 * @testName: testGetString42
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the null column value from Smallint_Tab table. Call the getString(String
	 * columnName) method.Check if it returns null.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString42() throws Exception {
		super.testGetString42();
	}

	/*
	 * @testName: testGetString43
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the maximum value of Integer_Tab table. Call the getString(String columnName)
	 * method to retrieve this value.Extract the maximum value of Integer_Tab table
	 * as a String from the tssql.stmt file. Compare this value with the value
	 * returned by the getString method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString43() throws Exception {
		super.testGetString43();
	}

	/*
	 * @testName: testGetString44
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the minimum value of Integer_Tab table. Call the getString(String columnName)
	 * method to retrieve this value.Extract the minimum value of Integer_Tab table
	 * as a String from the tssql.stmt file. Compare this value with the value
	 * returned by the getString method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString44() throws Exception {
		super.testGetString44();
	}

	/*
	 * @testName: testGetString45
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the null column value from Integer_Tab table. Call the getString(String
	 * columnName) method.Check if it returns null.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString45() throws Exception {
		super.testGetString45();
	}

	/*
	 * @testName: testGetString47
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the minimum value of Real_Tab table. Call the getString(String columnName)
	 * method to retrieve this value.Extract the minimum value of Real_Tab table as
	 * a String from the tssql.stmt file. Compare this value with the value returned
	 * by the getString method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString47() throws Exception {
		super.testGetString47();
	}

	/*
	 * @testName: testGetString48
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the null column value from Real_Tab table. Call the getString(String
	 * columnName) method.Check if it returns null.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString48() throws Exception {
		super.testGetString48();
	}

	/*
	 * @testName: testGetString53
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the minimum value of Float_Tab table. Call the getString(String columnName)
	 * method to retrieve this value.Extract the minimum value of Float_Tab table as
	 * a String from the tssql.stmt file. Compare this value with the value returned
	 * by the getString method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString53() throws Exception {
		super.testGetString53();
	}

	/*
	 * @testName: testGetString54
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the null column value from Float_Tab table. Call the getString(String
	 * columnName) method.Check if it returns null.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString54() throws Exception {
		super.testGetString54();
	}

	/*
	 * @testName: testGetString58
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the maximum value of Decimal_Tab table. Call the getString(String columnName)
	 * method to retrieve this value.Extract the maximum value of Decimal_Tab table
	 * as a String from the tssql.stmt file. Compare this value with the value
	 * returned by the getString method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString58() throws Exception {
		super.testGetString58();
	}

	/*
	 * @testName: testGetString59
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the minimum value of Decimal_Tab table. Call the getString(String columnName)
	 * method to retrieve this value.Extract the minimum value of Decimal_Tab table
	 * as a String from the tssql.stmt file. Compare this value with the value
	 * returned by the getString method. Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString59() throws Exception {
		super.testGetString59();
	}

	/*
	 * @testName: testGetString60
	 *
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * the null column value from Decimal_Tab table. Call the getString(String
	 * columnName) method.Check if it returns null.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString60() throws Exception {
		super.testGetString60();
	}
}
