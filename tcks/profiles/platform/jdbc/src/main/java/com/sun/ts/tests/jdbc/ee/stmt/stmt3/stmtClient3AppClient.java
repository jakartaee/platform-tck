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
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
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

@Tag("tck-appclient")

public class stmtClient3AppClient extends stmtClient3 implements Serializable {
	private static final String testName = "jdbc.ee.stmt.stmt3";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "stmt3_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(stmtClient3.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = stmtClient3AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/stmt/stmt3/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = stmtClient3AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, stmtClient3AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "stmt3_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;

	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		stmtClient3AppClient theTests = new stmtClient3AppClient();
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
	public void testSetQueryTimeout02() throws Exception {
		super.testSetQueryTimeout02();
	}

}
