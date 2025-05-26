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
 * @(#)resultSetClient18.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet18;

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
 * The resultSetClient18 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-javatest")
@Tag("web")
public class resultSetClient18JSP extends resultSetClient18 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet18";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet18_jsp_vehicle_web.war");
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

		archive.addClasses(resultSetClient18.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = resultSetClient18JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = resultSetClient18JSP.class.getResource("resultSet18_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, resultSetClient18JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(resultSetClient18JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient18JSP theTests = new resultSetClient18JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetInt04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object by executing the query that gets the
	 * maximum value of table Smallint_Tab.Call the getInt(int columnIndex) method.
	 * Compare the returned result with the value extracted from tssql.stmt
	 * file.Both of them should be equal and the returned result must be equal to
	 * the Maximum Value of JDBC Smallint datatype.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt04() throws Exception {
		super.testGetInt04();
	}

	/*
	 * @testName: testGetInt05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object by executing the query that gets the
	 * minimum value of table Smallint_Tab.Call the getInt(int columnIndex) method.
	 * Compare the returned result with the value extracted from tssql.stmt
	 * file.Both of them should be equal and the returned result must be equal to
	 * the Minimum Value of JDBC Smallint datatype.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt05() throws Exception {
		super.testGetInt05();
	}

	/*
	 * @testName: testGetInt06
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Smallint_Tab.Call the getInt(int columnIndex)
	 * method.Check if the value returned is zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt06() throws Exception {
		super.testGetInt06();
	}

	/*
	 * @testName: testGetInt07
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that gets the
	 * maximum value of table Integer_Tab.Call the getInt(int columnIndex) method.
	 * Compare the returned result with the value extracted from tssql.stmt
	 * file.Both of them should be equal and the returned result must be equal to
	 * the Maximum Value of JDBC Integer datatype.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt07() throws Exception {
		super.testGetInt07();
	}

	/*
	 * @testName: testGetInt08
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that gets the
	 * minimum value of table Integer_Tab.Call the getInt(int columnIndex) method.
	 * Compare the returned result with the value extracted from tssql.stmt
	 * file.Both of them should be equal and the returned result must be equal to
	 * the Minimum Value of JDBC Integer datatype.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt08() throws Exception {
		super.testGetInt08();
	}

	/*
	 * @testName: testGetInt09
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 *
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Integer_Tab.Call the getInt(int columnIndex)
	 * method.Check if the value returned is zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt09() throws Exception {
		super.testGetInt09();
	}

	/*
	 * @testName: testGetInt10
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Real_Tab with an
	 * integer value. Retrieve the value updated in the table by executing a query
	 * in the Real_Tab. Compare the value inserted and the value retrieved. Both of
	 * them should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt10() throws Exception {
		super.testGetInt10();
	}

	/*
	 * @testName: testGetInt12
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Real_Tab.Call the getInt(int columnIndex) method.Check
	 * if the value returned is zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt12() throws Exception {
		super.testGetInt12();
	}

	/*
	 * @testName: testGetInt16
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the maximum value of table Float_Tab with the
	 * maximum value of table Integer_Tab.Now execute a query to get the maximum
	 * value of Float_Tab table and retrieve the result of the query using the
	 * getInt(int columnIndex) method.Compare the returned value, with the maximum
	 * value of table Integer_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt16() throws Exception {
		super.testGetInt16();
	}

	/*
	 * @testName: testGetInt17
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Using this,update the minimum value of table Float_Tab with the
	 * minimum value of table Integer_Tab.Now execute a query to get the minimum
	 * value of Float_Tab table and retrieve the result of the query using the
	 * getInt(int columnIndex) method.Compare the returned value, with the minimum
	 * value of table Integer_Tab extracted from the tssql.stmt file. Both of them
	 * should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt17() throws Exception {
		super.testGetInt17();
	}

	/*
	 * @testName: testGetInt18
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:380;
	 * JDBC:JAVADOC:381; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object by executing the query that returns
	 * null value from table Float_Tab.Call the getInt(int columnIndex) method.Check
	 * if the value returned is zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetInt18() throws Exception {
		super.testGetInt18();
	}
}
