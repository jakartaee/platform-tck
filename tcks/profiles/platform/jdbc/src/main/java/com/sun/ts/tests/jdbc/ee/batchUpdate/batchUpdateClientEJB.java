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
 * @(#)batchUpdateClient.java	1.34 03/05/16
 */
package com.sun.ts.tests.jdbc.ee.batchUpdate;

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
 * The batchUpdateClient class tests methods of Statement, PreparedStatement and
 * CallableStatement interfaces using Sun's J2EE Reference Implementation.
 * 
 */
@Tag("tck-appclient")

public class batchUpdateClientEJB extends batchUpdateClient implements Serializable {
	private static final String testName = "jdbc.ee.batchUpdate";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "batchUpdate_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(batchUpdateClient.class, ServiceEETest.class, EETest.class);

		URL resURL = batchUpdateClientEJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = batchUpdateClientEJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/batchUpdate/batchUpdate_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}
		archiveProcessor.processClientArchive(ejbClient, batchUpdateClientEJB.class, resURL);
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "batchUpdate_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(batchUpdateClient.class);

		resURL = batchUpdateClientEJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/batchUpdate/batchUpdate_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = batchUpdateClientEJB.class.getResource("/com/sun/ts/tests/jdbc/ee/batchUpdate/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "batchUpdate_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);

		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		batchUpdateClientEJB theTests = new batchUpdateClientEJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testAddBatch01
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:700; JDBC:JAVADOC:701;
	 * JDBC:SPEC:23;
	 * 
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * with 3 SQL statements and call the executeBatch() method and it should return
	 * array of Integer values of length 3
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testAddBatch01() throws Exception {
		super.testAddBatch01();
	}

	/*
	 * @testName: testAddBatch02
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:183; JDBC:JAVADOC:184;
	 * JDBC:JAVADOC:187; JDBC:JAVADOC:188; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a Statement object and call the addBatch() method with 3
	 * SQL statements and call the executeBatch() method and it should return an
	 * array of Integer of length 3.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testAddBatch02() throws Exception {
		super.testAddBatch02();
	}

	/*
	 * @testName: testAddBatch03
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:700; JDBC:JAVADOC:701;
	 * JDBC:JAVADOC:187; JDBC:JAVADOC:188; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a CallableStatement object and call the addBatch() method
	 * with 3 SQL statements and call the executeBatch() method and it should return
	 * an array of Integer of length 3.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testAddBatch03() throws Exception {
		super.testAddBatch03();
	}

	/*
	 * @testName: testClearBatch01
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * and call the clearBatch() method and then call executeBatch() to check the
	 * call of clearBatch()method The executeBatch() method should return a zero
	 * value.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testClearBatch01() throws Exception {
		super.testClearBatch01();
	}

	/*
	 * @testName: testClearBatch02
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
	 * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a Statement object and call the addBatch() method and
	 * call the clearBatch() method and then call executeBatch() to check the call
	 * of clearBatch()method.The executeBatch() method should return a zero value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testClearBatch02() throws Exception {
		super.testClearBatch02();
	}

	/*
	 * @testName: testClearBatch03
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a CallableStatement object and call the addBatch() method
	 * and call the clearBatch() method and then call executeBatch() to check the
	 * call of clearBatch()method. The executeBatch() method should return a zero
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testClearBatch03() throws Exception {
		super.testClearBatch03();
	}

	/*
	 * @testName: testExecuteBatch01
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * with a 3 valid SQL statements and call the executeBatch() method It should
	 * return an array of Integer values of length 3.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch01() throws Exception {
		super.testExecuteBatch01();
	}

	/*
	 * @testName: testExecuteBatch02
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the executeBatch()
	 * method without calling addBatch() method.It should return an array of zero
	 * length.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch02() throws Exception {
		super.testExecuteBatch02();
	}

	/*
	 * @testName: testExecuteBatch03
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * and call the executeBatch() method with a select Statement It should throw
	 * BatchUpdateException
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch03() throws Exception {
		super.testExecuteBatch03();
	}

	/*
	 * @testName: testExecuteBatch04
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a Statement object and call the addBatch() method with 3
	 * valid SQL statements and call the executeBatch() method It should return an
	 * array of Integer values of length 3
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch04() throws Exception {
		super.testExecuteBatch04();
	}

	/*
	 * @testName: testExecuteBatch05
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:183; JDBC:JAVADOC:184;
	 * 
	 * @test_Strategy: Get a Statement object and call the executeBatch() method
	 * without adding statements into a batch. It should return an array of Integer
	 * value of zero length
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch05() throws Exception {
		super.testExecuteBatch05();
	}

	/*
	 * @testName: testExecuteBatch06
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a Statement object and call the addBatch() method and
	 * call the executeBatch() method with a violation in SQL constraints.It should
	 * throw an BatchUpdateException
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch06() throws Exception {
		super.testExecuteBatch06();
	}

	/*
	 * @testName: testExecuteBatch07
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a Statement object and call the addBatch() method and
	 * call the executeBatch() method with a select Statement It should throw an
	 * BatchUpdateException
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch07() throws Exception {
		super.testExecuteBatch07();
	}

	/*
	 * @testName: testExecuteBatch08
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a CallableStatement object and call the addBatch() method
	 * with 3 valid SQL statements and call the executeBatch() method It should
	 * return an array of Integer Values of length 3.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch08() throws Exception {
		super.testExecuteBatch08();
	}

	/*
	 * @testName: testExecuteBatch09
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * 
	 * @test_Strategy: Get a CallableStatement object and call the executeBatch()
	 * method without adding the statements into Batch. It should return an array of
	 * Integer Value of zero length
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch09() throws Exception {
		super.testExecuteBatch09();
	}

	/*
	 * @testName: testExecuteBatch12
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a CallableStatement object with different SQL statements
	 * in the stored Procedure and call the addBatch() method with 3 statements and
	 * call the executeBatch() method It should return an array of Integer Values of
	 * length 3.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testExecuteBatch12() throws Exception {
		super.testExecuteBatch12();
	}

	/*
	 * @testName: testContinueBatch01
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * with 3 SQL statements.Among these 3 SQL statements first is valid,second is
	 * invalid and third is again valid. Then call the executeBatch() method and it
	 * should return array of Integer values of length 3, if it supports continued
	 * updates. Then check whether the third command in the batch after the invalid
	 * command executed properly.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testContinueBatch01() throws Exception {
		super.testContinueBatch01();
	}
}
