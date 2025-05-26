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
 * @(#)stmtClient2.java	1.25 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.stmt.stmt2;

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

//Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The stmtClient2 class tests methods of Statement interface using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")

public class stmtClient2JSP extends stmtClient2 implements Serializable {
	private static final String testName = "jdbc.ee.stmt.stmt2";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "stmt2_jsp_vehicle_web.war");
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

		archive.addClasses(stmtClient2.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = stmtClient2JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = stmtClient2JSP.class.getResource("stmt2_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, stmtClient2JSP.class, sunJSPUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		stmtClient2JSP theTests = new stmtClient2JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetResultSet02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:165; JDBC:JAVADOC:166;
	 * 
	 * @test_Strategy: Get a Statement object and call execute() method for updating
	 * a row.Then call getResultSet() method It should return a Null ResultSet
	 * object
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetResultSet02() throws Exception {
		super.testGetResultSet02();
	}

	/*
	 * @testName: testGetResultSetConcurrency01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:179; JDBC:JAVADOC:180;
	 * JDBC:JAVADOC:362;
	 * 
	 * @test_Strategy: Get a Statement object and call getResultSetConcurrency()
	 * method It should return an int value either CONCUR_READ_ONLY or
	 * CONCUR_UPDATABLE.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetResultSetConcurrency01() throws Exception {
		super.testGetResultSetConcurrency01();
	}

	/*
	 * @testName: testGetResultSetType01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:181; JDBC:JAVADOC:182;
	 * 
	 * @test_Strategy: Get a Statement object and call getResultSetType() method It
	 * should return an int value which should be either TYPE_FORWARD_ONLY or
	 * TYPE_SCROLL_INSENSITIVE or TYPE_SCROLL_SENSITIVE
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetResultSetType01() throws Exception {
		super.testGetResultSetType01();
	}

	/*
	 * @testName: testGetResultSetType02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:181; JDBC:JAVADOC:182;
	 * JDBC:JAVADOC:1179; JDBC:JAVADOC:1180; JDBC:JAVADOC:359;
	 * 
	 * @test_Strategy: Call Connection.createStatement with the Type mode as
	 * TYPE_FORWARD_ONLY and call getResultSetType() method It should return a int
	 * value and the value should be equal to ResultSet.TYPE_FORWARD_ONLY
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetResultSetType02() throws Exception {
		super.testGetResultSetType02();
	}

	/*
	 * @testName: testGetResultSetType03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:181; JDBC:JAVADOC:182;
	 * JDBC:JAVADOC:1179; JDBC:JAVADOC:1180;
	 * 
	 * @test_Strategy: Call Connection.createStatement with the Type mode as
	 * TYPE_SCROLL_INSENSITIVE and call getResultSetType() method It should return a
	 * int value and the value should be equal to ResultSet.TYPE_SCROLL_INSENSITIVE
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetResultSetType03() throws Exception {
		super.testGetResultSetType03();
	}

	/*
	 * @testName: testGetUpdateCount01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:167; JDBC:JAVADOC:168;
	 * 
	 * @test_Strategy: Get a Statement object and call the execute() method for
	 * updating a row and call getUpdateCount() method It should return a int value
	 * and the value should be equal to number of rows with the specified condition
	 * for update
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetUpdateCount01() throws Exception {
		super.testGetUpdateCount01();
	}

	/*
	 * @testName: testGetUpdateCount02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:167; JDBC:JAVADOC:168;
	 * 
	 * @test_Strategy: Get a Statement object and call the execute() method for
	 * selecting a non-existent row and call getUpdateCount() method It should
	 * return a int value and the value should be equal to -1
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetUpdateCount02() throws Exception {
		super.testGetUpdateCount02();
	}

	/*
	 * @testName: testGetWarnings
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:157; JDBC:JAVADOC:158;
	 * 
	 * @test_Strategy: Get a Statement object and call getWarnings() method should
	 * return an SQLWarning object
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetWarnings() throws Exception {
		super.testGetWarnings();
	}

	/*
	 * @testName: testSetFetchDirection04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:171; JDBC:JAVADOC:172;
	 * 
	 * @test_Strategy: Get a Statement object and call the setFetchDirection(int
	 * direction) method with an invalid value and it should throw an SQLException
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetFetchDirection04() throws Exception {
		super.testSetFetchDirection04();
	}

	/*
	 * @testName: testSetFetchSize02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:175; JDBC:JAVADOC:176;
	 * 
	 * @test_Strategy: Get a Statement object and call the setFetchSize(int rows)
	 * method with the value of Statement.getMaxRows and call getFetchSize() method
	 * and it should return a int value that is been set
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetFetchSize02() throws Exception {
		super.testSetFetchSize02();
	}

}
