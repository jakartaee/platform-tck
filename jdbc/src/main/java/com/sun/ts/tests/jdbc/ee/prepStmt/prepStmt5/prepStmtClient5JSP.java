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
 * @(#)prepStmtClient5.java	1.18 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt5;

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
 * The prepStmtClient5 class tests methods of PreparedStatement interface using
 * Sun's J2EE Reference Implementation.
 * 
 */

public class prepStmtClient5JSP extends prepStmtClient5 implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt5";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "prepStmt5_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient5JSP.class, prepStmtClient5.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        archive.add(new ByteArrayAsset(clientHtml), "client.html");
        
	       // The jsp descriptor
  URL jspUrl = prepStmtClient5JSP.class.getResource("jsp_vehicle_web.xml");
  if(jspUrl != null) {
  	archive.addAsManifestResource(jspUrl, "web.xml");
  }
  // The sun jsp descriptor
  URL sunJSPUrl = prepStmtClient5JSP.class.getResource("prepStmt5_jsp_vehicle_web.war.sun-web.xml");
  if(sunJSPUrl != null) {
  	archive.addAsManifestResource(sunJSPUrl, "sun-web.xml");
  }
  // Call the archive processor
  archiveProcessor.processWebArchive(archive, prepStmtClient5JSP.class, sunJSPUrl);

        
		archive.addAsWebInfResource(prepStmtClient5JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
		System.out.println(archive.toString(true));
		return archive;
	};



  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient5JSP theTests = new prepStmtClient5JSP();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testSetNull12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:12;JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for Varchar Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetNull12() throws Exception {
		super.testSetNull12();
  }

  /*
   * @testName: testSetNull13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:13; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for LONGVARCHAR Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetNull13() throws Exception {
		super.testSetNull13();
  }

  /*
   * @testName: testSetNull14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:7; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for REAL Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   *
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetNull14() throws Exception {
		super.testSetNull14();
  }

  /*
   * @testName: testSetNull15
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:10; JDBC:JAVADOC:454; JDBC:JAVADOC:455; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for DECIMAL Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetNull15() throws Exception {
		super.testSetNull15();
  }

  /*
   * @testName: testSetNull16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:17; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for BINARY Type and retrieve the same value by executing a query. Call
   * the ResultSet.wasNull() method to check it. It should return a true value.
   *
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetNull16() throws Exception {
		super.testSetNull16();
  }

  /*
   * @testName: testSetNull17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:18; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for VARBINARY Type and retrieve the same value by executing a query.
   * Call the ResultSet.wasNull() method to check it. It should return a true
   * value.
   *
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetNull17() throws Exception {
		super.testSetNull17();
  }

  /*
   * @testName: testSetNull18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:656; JDBC:JAVADOC:657;
   * JDBC:JAVADOC:19; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement to set the value as SQL
   * Null for LONGVARBINARY Type and retrieve the same value by executing a
   * query. Call the ResultSet.wasNull() method to check it. It should return a
   * true value.
   *
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetNull18() throws Exception {
		super.testSetNull18();
  }

  /*
   * @testName: testSetObject30
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
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
  public void testSetObject30() throws Exception {
		super.testSetObject30();
  }

  /*
   * @testName: testSetObject31
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
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
  public void testSetObject31() throws Exception {
		super.testSetObject31();
  }

  /*
   * @testName: testSetObject32
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
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
  public void testSetObject32() throws Exception {
		super.testSetObject32();
  }

  /*
   * @testName: testSetObject33
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
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
  public void testSetObject33() throws Exception {
		super.testSetObject33();
  }

  /*
   * @testName: testSetObject34
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Integer_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject34() throws Exception {
		super.testSetObject34();
  }

  /*
   * @testName: testSetObject35
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Integer_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject35() throws Exception {
		super.testSetObject35();
  }

  /*
   * @testName: testSetObject36
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Bigint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject36() throws Exception {
		super.testSetObject36();
  }

  /*
   * @testName: testSetObject37
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the minimum value of Bigint_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject37() throws Exception {
		super.testSetObject37();
  }

  /*
   * @testName: testSetObject38
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
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
  public void testSetObject38() throws Exception {
		super.testSetObject38();
  }

  /*
   * @testName: testSetObject39
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
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
  public void testSetObject39() throws Exception {
		super.testSetObject39();
  }

  /*
   * @testName: testSetObject40
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_Val with the maximum value of Float_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject40() throws Exception {
		super.testSetObject40();
  }

  /*
   * @testName: testSetObject41
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
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
  public void testSetObject41() throws Exception {
		super.testSetObject41();
  }

  /*
   * @testName: testSetObject42
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:694; JDBC:JAVADOC:695;
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
  public void testSetObject42() throws Exception {
		super.testSetObject42();
  }
}
