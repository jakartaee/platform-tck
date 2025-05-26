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
 * @(#)dateTimeClient3.java	1.17 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dateTime.dateTime3;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;
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

/*
 * The dateTimeClient3 class tests methods of Timestamp class using 
 * Sun's J2EE Reference Implementation.
 * @author  
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class dateTimeClient3AppClient extends dateTimeClient3 implements Serializable {
	private static final String testName = "jdbc.ee.dateTime.dateTime3";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "dateTime3_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(dateTimeClient3.class, ServiceEETest.class, EETest.class);
		// The appclient-client descriptor
		URL appClientUrl = dateTimeClient3AppClient.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dateTime/dateTime3/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = dateTimeClient3AppClient.class.getResource(
				"//com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}
		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// Call the archive processor
		archiveProcessor.processClientArchive(archive, dateTimeClient3AppClient.class, sunAppClientUrl);
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dateTime3_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dateTimeClient3AppClient theTests = new dateTimeClient3AppClient();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testDate01
	 * 
	 * @assertion_ids: JDBC:JAVADOC:100
	 * 
	 * @test_Strategy: Create a Date Object with a long value as an argument. Then
	 * get the String representation of that Date object. Check whether it is same
	 * as equivalent String Value in property file.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testDate01() throws Exception {
		super.testDate01();
	}

	/*
	 * @testName: testDate02
	 * 
	 * @assertion_ids: JDBC:JAVADOC:100
	 * 
	 * @test_Strategy: Create a Date Object with a long value as an argument. Then
	 * get the String representation of that Date object. Check whether it is same
	 * as equivalent String Value in property file.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testDate02() throws Exception {
		super.testDate02();
	}

	/*
	 * @testName: testToString01
	 * 
	 * @assertion_ids: JDBC:JAVADOC:103
	 * 
	 * @test_Strategy: Create a Date Object with a long value as an argument. Then
	 * get the String representation of that Date object. using the toString()
	 * method.Check whether it is same as equivalent String Value in property file.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testToString01() throws Exception {
		super.testToString01();
	}

	/*
	 * @testName: testToString02
	 * 
	 * @assertion_ids: JDBC:JAVADOC:103
	 * 
	 * @test_Strategy: Create a Date Object with a long value as an argument. Then
	 * get the String representation of that Date object. using the toString()
	 * method.Check whether it is same as equivalent String Value in property file.
	 */
	@Test
	@TargetVehicle("appclient")
	public void testToString02() throws Exception {
		super.testToString02();
	}

	/*
	 * @testName: testValueOf01
	 * 
	 * @assertion_ids: JDBC:JAVADOC:102
	 * 
	 * @test_Strategy: Call valueof(String ts) static method in java.sql.Date class
	 * with a String argument to get a Date object Check whether it is same as Date
	 * object obtained from equivalent long value .
	 */
	@Test
	@TargetVehicle("appclient")
	public void testValueOf01() throws Exception {
		super.testValueOf01();
	}

	/*
	 * @testName: testValueOf02
	 * 
	 * @assertion_ids: JDBC:JAVADOC:102
	 * 
	 * @test_Strategy: Call valueof(String ts) static method in java.sql.Date class
	 * with a String argument to get a Date object Check whether it is same as Date
	 * object obtained from equivalent long value .
	 */
	@Test
	@TargetVehicle("appclient")
	public void testValueOf02() throws Exception {
		super.testValueOf02();
	}

	/*
	 * @testName: testSetTime01
	 * 
	 * @assertion_ids: JDBC:JAVADOC:101
	 * 
	 * @test_Strategy: Create two Date objects with two different long values. Set
	 * the same long value in the second object as used in the first object using
	 * setTime(long) method Check whether both the Date objects are equal using
	 * equals method
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetTime01() throws Exception {
		super.testSetTime01();
	}

	/*
	 * @testName: testSetTime02
	 * 
	 * @assertion_ids: JDBC:JAVADOC:101
	 * 
	 * @test_Strategy: Create two Date objects with two different long values. Set
	 * the same long value in the second object as used in the first object using
	 * setTime(long) method Check whether both the Date objects are equal using
	 * equals method
	 */
	@Test
	@TargetVehicle("appclient")
	public void testSetTime02() throws Exception {
		super.testSetTime02();
	}

}
