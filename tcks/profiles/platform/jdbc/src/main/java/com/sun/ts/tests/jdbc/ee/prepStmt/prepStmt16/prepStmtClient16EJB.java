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
 * $Id$
 */

/*
 * @(#)prepStmtClient16.java	1.16 02/04/23
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt16;

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

/**
 * The prepStmtClient16 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.0, 10/09/2002
 */

@Tag("tck-appclient")

public class prepStmtClient16EJB extends prepStmtClient16 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt16";

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "prepStmt16_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(prepStmtClient16.class, ServiceEETest.class, EETest.class);

		URL resURL = prepStmtClient16EJB.class
				.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}

		resURL = prepStmtClient16EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt16/prepStmt16_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "prepStmt16_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(prepStmtClient16.class, ServiceEETest.class, EETest.class);

		resURL = prepStmtClient16EJB.class.getResource(
				"/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt16/prepStmt16_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = prepStmtClient16EJB.class
				.getResource("/com/sun/ts/tests/jdbc/ee/prepStmt/prepStmt16/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "prepStmt16_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient16EJB theTests = new prepStmtClient16EJB();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetParameterMetaData
	 * 
	 * @assertion_ids: JavaEE:SPEC:186.3; JDBC:JAVADOC:724; JDBC:JAVADOC:725;
	 * JDBC:SPEC:9; JDBC:SPEC:26;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Execute the method getParameterMetaData on the preparedStatement
	 * object. Get the information about the number of parameters by executing the
	 * method getParameterCount(). It should return the number of parameters.
	 * 
	 */

	@Test
	@TargetVehicle("ejb")
	public void testGetParameterMetaData() throws Exception {
		super.testGetParameterMetaData();
	}

	/*
	 * @testName: testSetAsciiStream
	 * 
	 * @assertion_ids: JavaEE:SPEC:186; JDBC:JAVADOC:684; JDBC:JAVADOC:685;
	 * JDBC:SPEC:9; JDBC:SPEC:26;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Get the InputStream object. Excecute the method
	 * preparedStatement.setAsciiStream to Update the Longvarchar_Tab_Name with the
	 * value extracted from the Char_Tab. Query the Longvarchar_Tab in the database
	 * to retrieve the value that is been set. Compare the value that is inserted
	 * with the value retrieved. These values should be equal.
	 * 
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetAsciiStream() throws Exception {
		super.testSetAsciiStream();
	}

	/*
	 * @testName: testSetBinaryStream
	 * 
	 * @assertion_ids: JavaEE:SPEC:186; JDBC:JAVADOC:688; JDBC:JAVADOC:689;
	 * JDBC:SPEC:9; JDBC:SPEC:26;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Get the InputStream object. Excecute the method
	 * preparedStatement.setBinaryStream to Update the Longvarbinary_Tab_Name with
	 * some byte array value. Query the Longvarbinary_Tab in the database to
	 * retrieve the value that is been set. Compare the byte array value that is
	 * inserted with the value retrieved. These cvalues should be equal.
	 * 
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetBinaryStream() throws Exception {
		super.testSetBinaryStream();
	}

	/*
	 * @testName: testSetCharacterStream
	 * 
	 * @assertion_ids: JavaEE:SPEC:186; JDBC:JAVADOC:702; JDBC:JAVADOC:703;
	 * JDBC:SPEC:9; JDBC:SPEC:26;
	 *
	 * @test_Strategy: Get a PreparedStatement object from the connection to the
	 * database. Get the InputStream object. Get a Reader object from this
	 * InputStream. Excecute the method preparedStatement.setCharacterStream to
	 * Update the Longvarchar_Tab_Name with the value extracted from Char_tab. Query
	 * the Longvarchar_Tab in the database to retrieve the value that is been set.
	 * Compare the byte array value that is inserted with the value retrieved. These
	 * values should be equal.
	 * 
	 */

	@Test
	@TargetVehicle("ejb")
	public void testSetCharacterStream() throws Exception {
		super.testSetCharacterStream();
	}
}
