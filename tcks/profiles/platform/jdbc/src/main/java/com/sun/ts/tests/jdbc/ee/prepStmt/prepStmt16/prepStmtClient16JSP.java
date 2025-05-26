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
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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

@Tag("tck-javatest")
@Tag("web")
public class prepStmtClient16JSP extends prepStmtClient16 implements Serializable {
	private static final String testName = "jdbc.ee.prepStmt.prepStmt16";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "prepStmt16_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient16.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = prepStmtClient16JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = prepStmtClient16JSP.class.getResource("prepStmt16_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, prepStmtClient16JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(prepStmtClient16JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		prepStmtClient16JSP theTests = new prepStmtClient16JSP();
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
	public void testSetCharacterStream() throws Exception {
		super.testSetCharacterStream();
	}
}
