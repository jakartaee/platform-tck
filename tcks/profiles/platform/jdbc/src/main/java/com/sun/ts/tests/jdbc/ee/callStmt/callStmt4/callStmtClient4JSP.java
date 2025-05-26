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
 * @(#)callStmtClient4.java	1.19 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt4;

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

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;
/**
 * The callStmtClient4 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class callStmtClient4JSP extends callStmtClient4 implements Serializable {

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt4_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient4.class, ServiceEETest.class, EETest.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
		InputStream clientHtml = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		archive.add(new ByteArrayAsset(clientHtml), "client.html");

		// The jsp descriptor
		URL jspUrl = callStmtClient4JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = callStmtClient4JSP.class.getResource("callStmt4_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, callStmtClient4JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(callStmtClient4JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		callStmtClient4JSP theTests = new callStmtClient4JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetObject21
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve a Time value from
	 * Time_Tab.Extract the same Time value from the tssql.stmt file.Compare this
	 * value with the value returned by the getObject(int parameterIndex).Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject21() throws Exception {
		super.testGetObject21();
	}

	/*
	 * @testName: testGetObject22
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Time_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject22() throws Exception {
		super.testGetObject22();
	}

	/*
	 * @testName: testGetObject23
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve a Timestamp value from
	 * Timestamp_Tab.Extract the same Timestamp value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex).Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject23() throws Exception {
		super.testGetObject23();
	}

	/*
	 * @testName: testGetObject24
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Timestamp_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject24() throws Exception {
		super.testGetObject24();
	}

	/*
	 * @testName: testGetObject25
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve a Date value from from
	 * Date_Tab.Extract the Date char value from the tssql.stmt file.Compare this
	 * value with the value returned by the getObject(int parameterIndex).Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject25() throws Exception {
		super.testGetObject25();
	}

	/*
	 * @testName: testGetObject26
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Date_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject26() throws Exception {
		super.testGetObject26();
	}

	/*
	 * @testName: testGetObject27
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the maximum value from
	 * Tinyint_Tab. Extract the maximum value from the tssql.stmt file.Compare this
	 * value with the value returned by the getObject(int parameterIndex) Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject27() throws Exception {
		super.testGetObject27();
	}

	/*
	 * @testName: testGetObject28
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the minimum value from
	 * Tinyint_Tab. Extract the minimum value from the tssql.stmt file.Compare this
	 * value with the value returned by the getObject(int parameterIndex) Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject28() throws Exception {
		super.testGetObject28();
	}

	/*
	 * @testName: testGetObject29
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Tinyint_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject29() throws Exception {
		super.testGetObject29();
	}

	/*
	 * @testName: testGetObject30
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the maximum value from
	 * Double_Tab. Extract the maximum value from the tssql.stmt file.Compare this
	 * value with the value returned by the getObject(int parameterIndex) Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject30() throws Exception {
		super.testGetObject30();
	}

	/*
	 * @testName: testGetObject31
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the minimum value of the
	 * parameter from Double_Tab. Extract the minimum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject31() throws Exception {
		super.testGetObject31();
	}

	/*
	 * @testName: testGetObject32
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Double_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject32() throws Exception {
		super.testGetObject32();

	}

	/*
	 * @testName: testGetObject33
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the maximum value from
	 * Real_Tab. Extract the maximum value from the tssql.stmt file.Compare this
	 * value with the value returned by the getObject(int parameterIndex) Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject33() throws Exception {
		super.testGetObject33();
	}

	/*
	 * @testName: testGetObject34
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the minimum value from
	 * Real_Tab. Extract the minimum value from the tssql.stmt file.Compare this
	 * value with the value returned by the getObject(int parameterIndex) Both the
	 * values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject34() throws Exception {
		super.testGetObject34();
	}

	/*
	 * @testName: testGetObject35
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Real_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject35() throws Exception {
		super.testGetObject35();
	}

	/*
	 * @testName: testGetObject36
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve a Varchar value from
	 * Varchar_Tab.Extract the same Varchar value from the tssql.stmt file.Compare
	 * this value with the value returned by the getObject(int parameterIndex).Both
	 * the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject36() throws Exception {
		super.testGetObject36();
	}

	/*
	 * @testName: testGetObject37
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Varchar_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject37() throws Exception {
		super.testGetObject37();
	}

	/*
	 * @testName: testGetObject38
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve a Longvarchar value from
	 * Longvarchar_Tab.Extract the same Longvarchar value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex).Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject38() throws Exception {
		super.testGetObject38();
	}

	/*
	 * @testName: testGetObject39
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType) method. Execute the stored procedure and call the
	 * getObject(int parameterIndex) method to retrieve the null value from
	 * Longvarchar_Tab.Check if it returns null
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject39() throws Exception {
		super.testGetObject39();
	}

	/*
	 * @testName: testGetObject40
	 * 
	 * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1267;
	 * JDBC:JAVADOC:1268; JavaEE:SPEC:183; JavaEE:SPEC:185;
	 *
	 * @test_Strategy: Get a CallableStatement object from the connection to the
	 * database.Register the parameter using registerOutParameter(int
	 * parameterIndex,int sqlType,int scale) method. Execute the stored procedure
	 * and call the getObject(int parameterIndex) method to retrieve the maximum
	 * value from Decimal_Tab. Extract the maximum value from the tssql.stmt
	 * file.Compare this value with the value returned by the getObject(int
	 * parameterIndex) Both the values should be equal.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetObject40() throws Exception {
		super.testGetObject40();
	}

}
