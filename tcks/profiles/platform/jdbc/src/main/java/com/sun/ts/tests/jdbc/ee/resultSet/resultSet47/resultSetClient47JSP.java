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
 * @(#)resultSetClient47.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet47;

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
 * The resultSetClient47 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-javatest")
@Tag("web")

public class resultSetClient47JSP extends resultSetClient47 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet47";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet47_jsp_vehicle_web.war");
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
		URL jspUrl = resultSetClient47JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = resultSetClient47JSP.class.getResource("resultSet47_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, resultSetClient47JSP.class, sunJSPUrl);

		archive.addClasses(resultSetClient47.class, ServiceEETest.class, EETest.class);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient47JSP theTests = new resultSetClient47JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetString84
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JDBC:JAVADOC:368; JDBC:JAVADOC:369; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getString(String columnName) method with the SQL null column of JDBC
	 * datatype TIMESTAMP.It should return null String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetString84() throws Exception {
		super.testGetString84();
	}

	/*
	 * @testName: testGetBytes01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:390;
	 * JDBC:JAVADOC:391; JDBC:JAVADOC:368; JDBC:JAVADOC:369; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the
	 * database.Update the column value of Binary_Tab table with a byte array using
	 * the PreparedStatement.setBytes(int columnIndex) method.Call the getBytes(int
	 * columnIndex) method with the SQL column of JDBC datatype BINARY.It should
	 * return the byte array object that has been set.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBytes01() throws Exception {
		super.testGetBytes01();
	}

	/*
	 * @testName: testGetBytes02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:390;
	 * JDBC:JAVADOC:391; JDBC:JAVADOC:370; JDBC:JAVADOC:371; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getBytes(int columnIndex) method with the SQL null column of JDBC
	 * datatype BINARY.It should return null byte array object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBytes02() throws Exception {
		super.testGetBytes02();
	}

	/*
	 * @testName: testGetBytes03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:390;
	 * JDBC:JAVADOC:391; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the
	 * database.Update the column value of Varbinary_Tab table with a byte array
	 * using the PreparedStatement.setBytes(int columnIndex).Call the getBytes(int
	 * columnIndex) method with the SQL column of JDBC datatype VARBINARY.It should
	 * return the byte array object that has been set.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBytes03() throws Exception {
		super.testGetBytes03();
	}

	/*
	 * @testName: testGetBytes04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:390;
	 * JDBC:JAVADOC:391; JDBC:JAVADOC:370; JDBC:JAVADOC:371; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getBytes(int columnIndex) method with the SQL null column of JDBC
	 * datatype VARBINARY.It should return null byte array object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetBytes04() throws Exception {
		super.testGetBytes04();
	}
}
