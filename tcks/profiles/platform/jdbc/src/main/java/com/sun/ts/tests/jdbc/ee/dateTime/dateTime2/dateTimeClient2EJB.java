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
 * @(#)dateTimeClient2.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dateTime.dateTime2;

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

/**
 * The dateTimeClient2 class tests methods of Time class using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class dateTimeClient2EJB extends dateTimeClient2 implements Serializable {
	private static final String testName = "jdbc.ee.dateTime.dateTime2";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "dateTime2_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(dateTimeClient2.class, ServiceEETest.class, EETest.class);

		URL resURL = dateTimeClient2EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = dateTimeClient2EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dateTime/dateTime2/dateTime2_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "dateTime2_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(dateTimeClient2.class, ServiceEETest.class, EETest.class);

		resURL = dateTimeClient2EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dateTime/dateTime2/dateTime2_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = dateTimeClient2EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dateTime/dateTime2/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dateTime2_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dateTimeClient2EJB theTests = new dateTimeClient2EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testTime01
	 * 
	 * @assertion_ids: JDBC:JAVADOC:46;
	 * 
	 * @test_Strategy: Create a Time Object with a long value as an argument. Then
	 * get the String representation of that Time object. Check whether it is same
	 * as equivalent String Value in property file.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testTime01() throws Exception {
		super.testTime01();
	}

	/*
	 * @testName: testTime02
	 * 
	 * @assertion_ids: JDBC:JAVADOC:46;
	 * 
	 * @test_Strategy: Create a Time Object with a long value as an argument. Then
	 * get the String representation of that Time object. Check whether it is same
	 * as equivalent String Value in property file.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testTime02() throws Exception {
		super.testTime02();
	}

	/*
	 * @testName: testToString01
	 * 
	 * @assertion_ids: JDBC:JAVADOC:49; JDBC:JAVADOC:46;
	 * 
	 * @test_Strategy: Create a Time Object with a long value as an argument. Then
	 * get the String representation of that Time object. using the toString()
	 * method.Check whether it is same as equivalent String Value in property file.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testToString01() throws Exception {
		super.testToString01();
	}

	/*
	 * @testName: testToString02
	 * 
	 * @assertion_ids: JDBC:JAVADOC:49; JDBC:JAVADOC:46;
	 * 
	 * @test_Strategy: Create a Time Object with a long value as an argument. Then
	 * get the String representation of that Time object. using the toString()
	 * method.Check whether it is same as equivalent String Value in property file.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testToString02() throws Exception {
		super.testToString02();
	}

	/*
	 * @testName: testValueOf01
	 * 
	 * @assertion_ids: JDBC:JAVADOC:48; JDBC:JAVADOC:46;
	 * 
	 * @test_Strategy: Call valueof(String ts) static method in java.sql.Time class
	 * with a String argument to get a Time object Check whether it is same as Time
	 * object obtained from equivalent long value .
	 * 
	 */
	@Test
	@TargetVehicle("ejb")
	public void testValueOf01() throws Exception {
		super.testValueOf01();
	}

	/*
	 * @testName: testValueOf02
	 * 
	 * @assertion_ids: JDBC:JAVADOC:48; JDBC:JAVADOC:46;
	 * 
	 * @test_Strategy: Call valueof(String ts) static method in java.sql.Time class
	 * with a String argument to get a Time object Check whether it is same as Time
	 * object obtained from equivalent long value .
	 */
	@Test
	@TargetVehicle("ejb")
	public void testValueOf02() throws Exception {
		super.testValueOf02();
	}

	/*
	 * @testName: testSetTime01
	 * 
	 * @assertion_ids: JDBC:JAVADOC:47; JDBC:JAVADOC:46;
	 * 
	 * @test_Strategy: Create two Time objects with two different long values. Set
	 * the same long value in the second object as used in the first object using
	 * setTime(long) method Check whether both the Time objects are equal using
	 * equals method
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetTime01() throws Exception {
		super.testSetTime01();
	}

	/*
	 * @testName: testSetTime02
	 * 
	 * @assertion_ids: JDBC:JAVADOC:47; JDBC:JAVADOC:46;
	 * 
	 * @test_Strategy: Create two Time objects with two different long values. Set
	 * the same long value in the second object as used in the first object using
	 * setTime(long) method Check whether both the Time objects are equal using
	 * equals method
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSetTime02() throws Exception {
		super.testSetTime02();
	}

}
