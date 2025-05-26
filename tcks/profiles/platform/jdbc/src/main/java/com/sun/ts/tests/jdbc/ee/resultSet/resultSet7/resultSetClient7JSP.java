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
 * @(#)resultSetClient7.java	1.25 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet7;

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
 * The resultSetClient7 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 9/9/99
 */

@Tag("tck-javatest")
@Tag("web")

public class resultSetClient7JSP extends resultSetClient7 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet7";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet7_jsp_vehicle_web.war");
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
		URL jspUrl = resultSetClient7JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = resultSetClient7JSP.class.getResource("resultSet7_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, resultSetClient7JSP.class, sunJSPUrl);

		archive.addClasses(resultSetClient7.class, ServiceEETest.class, EETest.class);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient7JSP theTests = new resultSetClient7JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetObject61
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(int column index) method with the SQL null column of JDBC
	 * datatype SMALLINT. It should return null Integer object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject61() throws Exception {
		super.testGetObject61();
	}

	/*
	 * @testName: testGetObject69
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String column index) method with the column of JDBC
	 * datatype SMALLINT. It should return an Integer object that has been set as
	 * the maximum value of SMALLINT.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject69() throws Exception {
		super.testGetObject69();
	}

	/*
	 * @testName: testGetObject70
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String column index) method with the SQL column of JDBC
	 * datatype SMALLINT. It should return an Integer object that has been set as
	 * the minimum value of SMALLINT.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject70() throws Exception {
		super.testGetObject70();
	}

	/*
	 * @testName: testGetObject62
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JDBC:JAVADOC:442;
	 * JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the column of JDBC datatype
	 * SMALLINT. It should return an Integer object that has been set as the maximum
	 * value of SMALLINT.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject62() throws Exception {
		super.testGetObject62();
	}

	/*
	 * @testName: testGetObject63
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the SQL column of JDBC
	 * datatype SMALLINT. It should return an Integer object that has been set as
	 * the minimum value of SMALLINT.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject63() throws Exception {
		super.testGetObject63();
	}

	/*
	 * @testName: testGetObject64
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the SQL null column of JDBC
	 * datatype SMALLINT. It should return null Integer object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject64() throws Exception {
		super.testGetObject64();
	}

	/*
	 * @testName: testGetObject65
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(int columnIndex) method with the SQL column of JDBC
	 * datatype VARCHAR. It should return an String object that has been set.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject65() throws Exception {
		super.testGetObject65();
	}

	/*
	 * @testName: testGetObject66
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
	 * JDBC:JAVADOC:445; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(int column index) method with the SQL null column of JDBC
	 * datatype VARCHAR. It should return null String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject66() throws Exception {
		super.testGetObject66();
	}

	/*
	 * @testName: testGetObject67
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the SQL column of JDBC
	 * datatype VARCHAR. It should return an String object that has been set.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject67() throws Exception {
		super.testGetObject67();
	}

	/*
	 * @testName: testGetObject68
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
	 * JDBC:JAVADOC:447; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getObject(String columnName) method with the SQL null column of JDBC
	 * datatype VARCHAR. It should return null String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject68() throws Exception {
		super.testGetObject68();
	}
}
