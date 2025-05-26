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
 * @(#)resultSetClient47.java	1.24 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet47;

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
 * The resultSetClient47 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

@Tag("tck-appclient")

public class resultSetClient47EJB extends resultSetClient47 implements Serializable {
	private static final String testName = "jdbc.ee.resultSet.resultSet47";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "resultSet47_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(resultSetClient47.class, ServiceEETest.class, EETest.class);

		URL resURL = resultSetClient47EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = resultSetClient47EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/resultSet/resultSet47/resultSet47_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "resultSet47_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(resultSetClient47.class, ServiceEETest.class, EETest.class);

		resURL = resultSetClient47EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/resultSet/resultSet47/resultSet47_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = resultSetClient47EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/resultSet/resultSet47/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "resultSet47_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		resultSetClient47EJB theTests = new resultSetClient47EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetString84
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:404;
	 * JDBC:JAVADOC:405; JDBC:JAVADOC:368; JDBC:JAVADOC:369; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getString(String columnName) method with the SQL null column of JDBC
	 * datatype TIMESTAMP.It should return null String object.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetString84() throws Exception {
		super.testGetString84();
	}

	/*
	 * @testName: testGetBytes01
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:390;
	 * JDBC:JAVADOC:391; JDBC:JAVADOC:368; JDBC:JAVADOC:369; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the
	 * database.Update the column value of Binary_Tab table with a byte array using
	 * the PreparedStatement.setBytes(int columnIndex) method.Call the getBytes(int
	 * columnIndex) method with the SQL column of JDBC datatype BINARY.It should
	 * return the byte array object that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes01() throws Exception {
		super.testGetBytes01();
	}

	/*
	 * @testName: testGetBytes02
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:390;
	 * JDBC:JAVADOC:391; JDBC:JAVADOC:370; JDBC:JAVADOC:371; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getBytes(int columnIndex) method with the SQL null column of JDBC
	 * datatype BINARY.It should return null byte array object.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes02() throws Exception {
		super.testGetBytes02();
	}

	/*
	 * @testName: testGetBytes03
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:390;
	 * JDBC:JAVADOC:391; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the
	 * database.Update the column value of Varbinary_Tab table with a byte array
	 * using the PreparedStatement.setBytes(int columnIndex).Call the getBytes(int
	 * columnIndex) method with the SQL column of JDBC datatype VARBINARY.It should
	 * return the byte array object that has been set.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes03() throws Exception {
		super.testGetBytes03();
	}

	/*
	 * @testName: testGetBytes04
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:390;
	 * JDBC:JAVADOC:391; JDBC:JAVADOC:370; JDBC:JAVADOC:371; JavaEE:SPEC:191;
	 * 
	 * @test_Strategy: Get a ResultSet object from the Connection to the database.
	 * Call the getBytes(int columnIndex) method with the SQL null column of JDBC
	 * datatype VARBINARY.It should return null byte array object.
	 */
	@Test
	@TargetVehicle("ejb")
	public void testGetBytes04() throws Exception {
		super.testGetBytes04();
	}
}
