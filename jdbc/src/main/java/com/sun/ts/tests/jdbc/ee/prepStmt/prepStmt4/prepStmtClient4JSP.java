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
 * @(#)prepStmtClient4.java	1.15 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt4;

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
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

/**
 * The prepStmtClient4 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

public class prepStmtClient4JSP extends prepStmtClient4 implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt4";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "prepStmt4_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient4JSP.class, prepStmtClient4.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        archive.add(new ByteArrayAsset(clientHtml), "client.html");
        
	       // The jsp descriptor
  URL jspUrl = prepStmtClient4JSP.class.getResource("jsp_vehicle_web.xml");
  if(jspUrl != null) {
  	archive.addAsManifestResource(jspUrl, "web.xml");
  }
  // The sun jsp descriptor
  URL sunJSPUrl = prepStmtClient4JSP.class.getResource("prepStmt4_jsp_vehicle_web.war.sun-web.xml");
  if(sunJSPUrl != null) {
  	archive.addAsManifestResource(sunJSPUrl, "sun-web.xml");
  }
  // Call the archive processor
  archiveProcessor.processWebArchive(archive, prepStmtClient4JSP.class, sunJSPUrl);

        
		archive.addAsWebInfResource(prepStmtClient4JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
		System.out.println(archive.toString(true));
		return archive;
	};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient4JSP theTests = new prepStmtClient4JSP();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testSetObject10
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_val with the maximum value of Integer_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject10() throws Exception {
		super.testSetObject10();
  }

  /*
   * @testName: testSetObject11
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the minimum value of Integer_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject11() throws Exception {
		super.testSetObject11();
  }

  /*
   * @testName: testSetObject12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_val with the maximum value of Bigint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject12() throws Exception {
		super.testSetObject12();
  }

  /*
   * @testName: testSetObject13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the maximum value of Bigint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject13() throws Exception {
		super.testSetObject13();
  }

  /*
   * @testName: testSetObject14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Double_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject14() throws Exception {
		super.testSetObject14();
  }

  /*
   * @testName: testSetObject15
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Double_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject15() throws Exception {
		super.testSetObject15();
  }

  /*
   * @testName: testSetObject16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Float_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject16() throws Exception {
		super.testSetObject16();
  }

  /*
   * @testName: testSetObject17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Float_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject17() throws Exception {
		super.testSetObject17();
  }

  /*
   * @testName: testSetObject18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the Non Null value of Date_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the Non Null
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject18() throws Exception {
		super.testSetObject18();
  }

  /*
   * @testName: testSetObject19
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the Non Null value of Time_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the Non Null
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject19() throws Exception {
		super.testSetObject19();
  }

  /*
   * @testName: testSetObject20
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the Non Null value of Timestamp_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the Non Null
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject20() throws Exception {
		super.testSetObject20();
  }

  /*
   * @testName: testSetObject21
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   * 
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Real_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject21() throws Exception {
		super.testSetObject21();
  }

  /*
   * @testName: testSetObject22
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Real_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject22() throws Exception {
		super.testSetObject22();
  }

  /*
   * @testName: testSetObject23
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Tinyint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject23() throws Exception {
		super.testSetObject23();
  }

  /*
   * @testName: testSetObject24
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Tinyint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject24() throws Exception {
		super.testSetObject24();
  }

  /*
   * @testName: testSetObject25
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Smallint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject25() throws Exception {
		super.testSetObject25();
  }

  /*
   * @testName: testSetObject26
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Smallint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject26() throws Exception {
		super.testSetObject26();
  }

  /*
   * @testName: testSetObject27
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database.Update the column value of the Binary_Tab with a byte array using
   * the PreparedStatement.setBytes(int columnIndex) method. Call the
   * getObject(int parameterIndex) method.to retrieve the byte array. It should
   * return the byte array object that has been set.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject27() throws Exception {
		super.testSetObject27();
  }

  /*
   * @testName: testSetObject28
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database.Update the column value of the Varbinary_Tab with a byte array
   * using the PreparedStatement.setBytes(int columnIndex) method. Call the
   * getObject(int parameterIndex) method.to retrieve the byte array. It should
   * return the byte array object that has been set.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject28() throws Exception {
		super.testSetObject28();
  }

  /*
   * @testName: testSetObject29
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database.Update the column value of the Longvarbinary_Tab with a byte array
   * using the PreparedStatement.setBytes(int columnIndex) method. Call the
   * getObject(int parameterIndex) method to retrieve the byte array. It should
   * return the byte array object that has been set.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject29() throws Exception {
		super.testSetObject29();
  }
}
