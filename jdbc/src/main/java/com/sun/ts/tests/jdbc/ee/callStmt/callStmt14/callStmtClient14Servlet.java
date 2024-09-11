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
 * @(#)callStmtClient14.java	1.19 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt14;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient14 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient14Servlet extends callStmtClient14 implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt14";

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentServlet(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt14_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient14Servlet.class, callStmtClient14.class);
		archive.addAsWebInfResource(callStmtClient14Servlet.class.getPackage(), "servlet_vehicle_web.xml", "web.xml");
		
	       // The servlet descriptor
URL servletUrl = callStmtClient14Servlet.class.getResource("servlet_vehicle_web.xml");
if(servletUrl != null) {
	archive.addAsManifestResource(servletUrl, "web.xml");
}
// The sun servlet descriptor
URL sunServletUrl = callStmtClient14Servlet.class.getResource("callStmt14_servlet_vehicle_web.war.sun-web.xml");
if(sunServletUrl != null) {
	archive.addAsManifestResource(sunServletUrl, "sun-web.xml");
}
// Call the archive processor
archiveProcessor.processWebArchive(archive, callStmtClient14Servlet.class, sunServletUrl);

		System.out.println(archive.toString(true));
		return archive;
	};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient14Servlet theTests = new callStmtClient14Servlet();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }


  /*
   * @testName: testSetObject121
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Float_Tab with the maximum value of
   * the Integer_Tab. Execute a query to retrieve the Min_Val from Float_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject121() throws Exception {
		super.testSetObject121();
  }

  /*
   * @testName: testSetObject122
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Float_Tab with the minimum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Float_Tab. Compare the returned value with the minimum value extracted from
   * tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject122() throws Exception {
		super.testSetObject122();
  }

  /*
   * @testName: testSetObject123
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Double_Tab with the maximum value
   * of the Integer_Tab. Execute a query to retrieve the Min_Val from
   * Double_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject123() throws Exception {
		super.testSetObject123();
  }

  /*
   * @testName: testSetObject124
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Double_Tab with the minimum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Double_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject124() throws Exception {
		super.testSetObject124();
  }

  /*
   * @testName: testSetObject125
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Decimal_Tab with the maximum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Decimal_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject125() throws Exception {
		super.testSetObject125();
  }

  /*
   * @testName: testSetObject126
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Decimal_Tab with the minimum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Decimal_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject126() throws Exception {
		super.testSetObject126();
  }

  /*
   * @testName: testSetObject127
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Numeric_Tab with the maximum value
   * of the Integer_Tab. Execute a query to retrieve the Min_Val from
   * Numeric_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject127() throws Exception {
		super.testSetObject127();
  }

  /*
   * @testName: testSetObject128
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Numeric_Tab with the minimum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Numeric_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject128() throws Exception {
		super.testSetObject128();

  }

  /*
   * @testName: testSetObject131
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Char_Tab with the maximum value of
   * the Integer_Tab. Execute a query to retrieve the Null_Val from Char_Tab.
   * Compare the returned value with the maximum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject131() throws Exception {
		super.testSetObject131();
  }

  /*
   * @testName: testSetObject132
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Char_Tab with the minimum value of
   * the Integer_Tab. Execute a query to retrieve the Null_Val from Char_Tab.
   * Compare the returned value with the minimum value extracted from tssql.stmt
   * file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject132() throws Exception {
		super.testSetObject132();
  }

  /*
   * @testName: testSetObject133
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Varchar_Tab with the maximum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Varchar_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject133() throws Exception {
		super.testSetObject133();
  }

  /*
   * @testName: testSetObject134
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Varchar_Tab with the minimum value
   * of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Varchar_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject134() throws Exception {
		super.testSetObject134();
  }

  /*
   * @testName: testSetObject135
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Longvarchar_Tab with the maximum
   * value of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Longvarchar_Tab. Compare the returned value with the maximum value
   * extracted from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject135() throws Exception {
		super.testSetObject135();
  }

  /*
   * @testName: testSetObject136
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Null_Val of the Longvarchar_Tab with the minimum
   * value of the Integer_Tab. Execute a query to retrieve the Null_Val from
   * Longvarchar_Tab. Compare the returned value with the minimum value
   * extracted from tssql.stmt file. Both of them should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject136() throws Exception {
		super.testSetObject136();
  }

  /*
   * @testName: testSetObject137
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Tinyint_Tab with the
   * maximum value of Tinyint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject137() throws Exception {
		super.testSetObject137();
  }

  /*
   * @testName: testSetObject138
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Tinyint_Tab with the
   * minimum value of Tinyint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject138() throws Exception {
		super.testSetObject138();
  }

  /*
   * @testName: testSetObject139
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x, int
   * targetSqlType) method,update the column Min_Val of Smallint_Tab with the
   * maximum value of Smallint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the maximum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject139() throws Exception {
		super.testSetObject139();
  }

  /*
   * @testName: testSetObject140
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x,int
   * targetSqlType) method,update the column Null_Val of Smallint_Tab with the
   * minimum value of Smallint_Tab. Call the getObject(int columnno) method to
   * retrieve this value. Extract the minimum value from the tssql.stmt file.
   * Compare this value with the value returned by the getObject(int columnno)
   * method. Both the values should be equal.
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetObject140() throws Exception {
		super.testSetObject140();
  }

}
