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

public class stmtClient3EJB extends stmtClient3 implements Serializable {
	private static final String testName = "jdbc.ee.stmt.stmt3";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "stmt3_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(stmtClient3.class, ServiceEETest.class, EETest.class);

		URL resURL = stmtClient3EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = stmtClient3EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/stmt/stmt3/stmt3_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "stmt3_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(stmtClient3.class, ServiceEETest.class, EETest.class);

		resURL = stmtClient3EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/stmt/stmt3/stmt3_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = stmtClient3EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/stmt/stmt3/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "stmt3_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		stmtClient3EJB theTests = new stmtClient3EJB();
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
	public void testSetQueryTimeout02() throws Exception {
		super.testSetQueryTimeout02();
	}

}
