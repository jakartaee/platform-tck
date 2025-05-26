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
 * @(#)scalarClient4.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar4;

import java.io.IOException;
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
 * The scalarClient4 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class scalarClient4AppClient extends scalarClient4 {
	private static final String testName = "jdbc.ee.escapeSyntax";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "scalar4_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(scalarClient4.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = scalarClient4AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/escapeSyntax/scalar4/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = scalarClient4AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}

		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, scalarClient4AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "scalar4_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		scalarClient4AppClient theTests = new scalarClient4AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testTimestampAdd01
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_FRAC_SECOND. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd01() throws Exception {
		super.testTimestampAdd01();
	}

	/*
	 * @testName: testTimestampAdd02
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_SECOND. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd02() throws Exception {
		super.testTimestampAdd02();
	}

	/*
	 * @testName: testTimestampAdd03
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_MINUTE. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd03() throws Exception {
		super.testTimestampAdd03();
	}

	/*
	 * @testName: testTimestampAdd04
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_HOUR. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd04() throws Exception {
		super.testTimestampAdd04();
	}

	/*
	 * @testName: testTimestampAdd05
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_DAY. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd05() throws Exception {
		super.testTimestampAdd05();
	}

	/*
	 * @testName: testTimestampAdd06
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_WEEK. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd06() throws Exception {
		super.testTimestampAdd06();
	}

	/*
	 * @testName: testTimestampAdd07
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_MONTH. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd07() throws Exception {
		super.testTimestampAdd07();
	}

	/*
	 * @testName: testTimestampAdd08
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_QUARTER. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd08() throws Exception {
		super.testTimestampAdd08();
	}

	/*
	 * @testName: testTimestampAdd09
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampadd with the interval value as
	 * SQL_TSI_YEAR. It should return a timestamp value.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampAdd09() throws Exception {
		super.testTimestampAdd09();
	}

	/*
	 * @testName: testTimestampDiff01
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_FRAC_SECOND. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff01() throws Exception {
		super.testTimestampDiff01();
	}

	/*
	 * @testName: testTimestampDiff02
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_SECOND. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff02() throws Exception {
		super.testTimestampDiff02();
	}

	/*
	 * @testName: testTimestampDiff03
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_MINUTE. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff03() throws Exception {
		super.testTimestampDiff03();
	}

	/*
	 * @testName: testTimestampDiff04
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_HOUR. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff04() throws Exception {
		super.testTimestampDiff04();
	}

	/*
	 * @testName: testTimestampDiff05
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_DAY. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff05() throws Exception {
		super.testTimestampDiff05();
	}

	/*
	 * @testName: testTimestampDiff06
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_WEEK. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff06() throws Exception {
		super.testTimestampDiff06();
	}

	/*
	 * @testName: testTimestampDiff07
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_MONTH. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff07() throws Exception {
		super.testTimestampDiff07();
	}

	/*
	 * @testName: testTimestampDiff08
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_QUARTER. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff08() throws Exception {
		super.testTimestampDiff08();
	}

	/*
	 * @testName: testTimestampDiff09
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a call to the function timestampdiff with the interval value
	 * as SQL_TSI_YEAR. It should return an integer.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testTimestampDiff09() throws Exception {
		super.testTimestampDiff09();
	}

	/*
	 * @testName: testLeftOuterjoin
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a left outer join. It should return a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testLeftOuterjoin() throws Exception {
		super.testLeftOuterjoin();
	}

	/*
	 * @testName: testRightOuterjoin
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a right outer join. It should return a ResultSet object.
	 * 
	 */
	@Test
	@TargetVehicle("appclient")
	public void testRightOuterjoin() throws Exception {
		super.testRightOuterjoin();
	}

	/*
	 * @testName: testFullOuterjoin
	 * 
	 * @assertion_ids: JDBC:SPEC:4;
	 * 
	 * @test_Strategy: Get a Statement object and call the method executeQuery. The
	 * query contains a full outer join. It should return a ResultSet object.
	 *
	 */
	@Test
	@TargetVehicle("appclient")
	public void testFullOuterjoin() throws Exception {
		super.testFullOuterjoin();
	}

}
