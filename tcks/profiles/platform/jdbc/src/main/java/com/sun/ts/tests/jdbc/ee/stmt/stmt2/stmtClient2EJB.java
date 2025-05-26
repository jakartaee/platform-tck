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

//Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The stmtClient2 class tests methods of Statement interface using Sun's J2EE
 * Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-appclient")

public class stmtClient2EJB extends stmtClient2 implements Serializable {
	private static final String testName = "jdbc.ee.stmt.stmt2";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "stmt2_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(stmtClient2.class, ServiceEETest.class, EETest.class);

		URL resURL = stmtClient2EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = stmtClient2EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/stmt/stmt2/stmt2_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "stmt2_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(stmtClient2.class, ServiceEETest.class, EETest.class);

		resURL = stmtClient2EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/stmt/stmt2/stmt2_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = stmtClient2EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/stmt/stmt2/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "stmt2_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		stmtClient2EJB theTests = new stmtClient2EJB();
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
	public void testSetFetchSize02() throws Exception {
		super.testSetFetchSize02();
	}

}
