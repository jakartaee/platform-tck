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
 * @(#)stmtClient3.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.stmt.stmt3;

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
 * The stmtClient3 class tests methods of Statement interface using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")

public class stmtClient3JSP extends stmtClient3 implements Serializable {
	private static final String testName = "jdbc.ee.stmt.stmt3";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "stmt3_jsp_vehicle_web.war");
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

		archive.addClasses(stmtClient3.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = stmtClient3JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = stmtClient3JSP.class.getResource("stmt3_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, stmtClient3JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(stmtClient3JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		stmtClient3JSP theTests = new stmtClient3JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSetFetchSize05
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:175; JDBC:JAVADOC:176;
	 *
	 * @test_Strategy: Get a Statement object and call the setFetchSize(int rows)
	 * method with the negative value and it should throw SQLException
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetFetchSize05() throws Exception {
		super.testSetFetchSize05();
	}

	/*
	 * @testName: testSetMaxFieldSize01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:143; JDBC:JAVADOC:144;
	 *
	 * @test_Strategy: Get a Statement object and call the setMaxFieldSize(int max)
	 * method and call getMaxFieldSize() method and it should return an integer
	 * value that is been set
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetMaxFieldSize01() throws Exception {
		super.testSetMaxFieldSize01();
	}

	/*
	 * @testName: testSetMaxFieldSize02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:143; JDBC:JAVADOC:144;
	 *
	 * @test_Strategy: Get a Statement object and call the setMaxFieldSize(int max)
	 * method with an invalid value (negative value) and It should throw a
	 * SQLException
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetMaxFieldSize02() throws Exception {
		super.testSetMaxFieldSize02();
	}

	/*
	 * @testName: testSetMaxRows01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:147; JDBC:JAVADOC:148;
	 *
	 * @test_Strategy: Get a Statement object and call the setMaxRows(int rows)
	 * method and call getMaxRows() method and it should return a integer value that
	 * is been set
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetMaxRows01() throws Exception {
		super.testSetMaxRows01();
	}

	/*
	 * @testName: testSetMaxRows02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:147; JDBC:JAVADOC:148;
	 *
	 * @test_Strategy: Get a Statement object and call the setMaxRows(int rows)
	 * method with an invalid value (negative value) and It should throw an
	 * SQLException
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetMaxRows02() throws Exception {
		super.testSetMaxRows02();
	}

	/*
	 * @testName: testSetQueryTimeout02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:153; JDBC:JAVADOC:154;
	 *
	 * @test_Strategy: Get a Statement object and call the setQueryTimeout(int
	 * secval) method with an invalid value (negative value)and It should throw an
	 * SQLException
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSetQueryTimeout02() throws Exception {
		super.testSetQueryTimeout02();
	}

}
