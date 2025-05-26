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
 * @(#)dbMetaClient3.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta3;

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
 * The dbMetaClient3 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */
@Tag("tck-appclient")

public class dbMetaClient3EJB extends dbMetaClient3 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta3";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "dbMeta3_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(dbMetaClient3.class, ServiceEETest.class, EETest.class);

		URL resURL = dbMetaClient3EJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		resURL = dbMetaClient3EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta3/dbMeta3_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "dbMeta3_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(dbMetaClient3.class, ServiceEETest.class, EETest.class);

		resURL = dbMetaClient3EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta3/dbMeta3_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = dbMetaClient3EJB.class.getResource("/com/sun/ts/tests/jdbc/ee/dbMeta/dbMeta3/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "dbMeta3_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient3EJB theTests = new dbMetaClient3EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testSupportsConvert03
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(BINARY, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert03() throws Exception {
		super.testSupportsConvert03();
	}

	/*
	 * @testName: testSupportsConvert04
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(BIT, VARCHAR) method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert04() throws Exception {
		super.testSupportsConvert04();
	}

	/*
	 * @testName: testSupportsConvert05
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(BLOB, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert05() throws Exception {
		super.testSupportsConvert05();
	}

	/*
	 * @testName: testSupportsConvert06
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(CHAR, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert06() throws Exception {
		super.testSupportsConvert06();
	}

	/*
	 * @testName: testSupportsConvert07
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(CLOB, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert07() throws Exception {
		super.testSupportsConvert07();
	}

	/*
	 * @testName: testSupportsConvert08
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(DATE, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert08() throws Exception {
		super.testSupportsConvert08();
	}

	/*
	 * @testName: testSupportsConvert09
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(DECIMAL, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert09() throws Exception {
		super.testSupportsConvert09();
	}

	/*
	 * @testName: testSupportsConvert10
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(DISTINCT, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert10() throws Exception {
		super.testSupportsConvert10();
	}

	/*
	 * @testName: testSupportsConvert11
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(DOUBLE, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert11() throws Exception {
		super.testSupportsConvert11();
	}

	/*
	 * @testName: testSupportsConvert12
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(FLOAT, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert12() throws Exception {
		super.testSupportsConvert12();
	}

	/*
	 * @testName: testSupportsConvert13
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(INTEGER, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert13() throws Exception {
		super.testSupportsConvert13();
	}

	/*
	 * @testName: testSupportsConvert14
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(JAVA_OBJECT, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert14() throws Exception {
		super.testSupportsConvert14();
	}

	/*
	 * @testName: testSupportsConvert15
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(LONGVARBINARY, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert15() throws Exception {
		super.testSupportsConvert15();
	}

	/*
	 * @testName: testSupportsConvert16
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(LONGVARCHAR, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert16() throws Exception {
		super.testSupportsConvert16();
	}

	/*
	 * @testName: testSupportsConvert17
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(NULL, VARCHAR) on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert17() throws Exception {
		super.testSupportsConvert17();
	}

	/*
	 * @testName: testSupportsConvert18
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(NUMERIC, VARCHAR) method on that object
	 * It should return a boolean value; either true or false
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert18() throws Exception {
		super.testSupportsConvert18();
	}

	/*
	 * @testName: testSupportsConvert19
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(OTHER, VARCHAR) method on that object
	 * It should return a boolean value; either true or false
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert19() throws Exception {
		super.testSupportsConvert19();
	}

	/*
	 * @testName: testSupportsConvert20
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(REAL, VARCHAR) method on that object.
	 * It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert20() throws Exception {
		super.testSupportsConvert20();
	}

	/*
	 * @testName: testSupportsConvert21
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(REF, VARCHAR) method on that object. It
	 * should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert21() throws Exception {
		super.testSupportsConvert21();
	}

	/*
	 * @testName: testSupportsConvert22
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(SMALLINT, VARCHAR) method on that
	 * object. It should return a boolean value; either true or false.
	 *
	 */
	@Test
	@TargetVehicle("ejb")
	public void testSupportsConvert22() throws Exception {
		super.testSupportsConvert22();
	}

}
