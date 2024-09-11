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
 * %W% %E%
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt8;

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

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient8 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */
public class callStmtClient8JSP extends callStmtClient8 implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt8";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = "jsp", testable = true)
 	public static WebArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
 		WebArchive archive = ShrinkWrap.create(WebArchive.class, "callStmt8_jsp_vehicle_web.war");
 		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
 		archive.addPackages(true, "com.sun.ts.lib.harness");
 		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        archive.add(new ByteArrayAsset(clientHtml), "client.html");
        
 		archive.addClasses(callStmtClient8JSP.class, callStmtClient8.class);
 		
	       // The jsp descriptor
     URL jspUrl = callStmtClient8JSP.class.getResource("jsp_vehicle_web.xml");
     if(jspUrl != null) {
     	archive.addAsManifestResource(jspUrl, "web.xml");
     }
     // The sun jsp descriptor
     URL sunJSPUrl = callStmtClient8JSP.class.getResource("callStmt8_jsp_vehicle_web.war.sun-web.xml");
     if(sunJSPUrl != null) {
     	archive.addAsManifestResource(sunJSPUrl, "sun-web.xml");
     }
     // Call the archive processor
     archiveProcessor.processWebArchive(archive, callStmtClient8JSP.class, sunJSPUrl);

 		archive.addAsWebInfResource(callStmtClient8JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
 		System.out.println(archive.toString(true));
 		return archive;
 	};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient8JSP theTests = new callStmtClient8JSP();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testSetObject01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set String object for SQL Type CHAR and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject01() throws Exception {
	  super.testSetObject01();
   }

  /*
   * @testName: testSetObject02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set String object for SQL Type VARCHAR
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject02() throws Exception {
	  super.testSetObject02();
  }

  /*
   * @testName: testSetObject03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set String object for SQL Type
   * LONGVARCHAR and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from tssql.stmt file.
   * Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject03() throws Exception {
	  super.testSetObject03();
  }

  /*
   * @testName: testSetObject04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set BigDecimal object for SQL Type
   * Numeric and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object.
   * Compare the returned value with the extracted value from tssql.stmt file.
   * Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject04() throws Exception {
	  super.testSetObject04();
  }

  /*
   * @testName: testSetObject05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set BigDecimal object for SQL Type
   * Numeric and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column) method. It should return a BigDecimal
   * object. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject05() throws Exception {
	  super.testSetObject05();
  }

  /*
   * @testName: testSetObject06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set BigDecimal object for SQL Type
   * Decimal and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object.
   * Compare the returned value with the value extracted from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject06() throws Exception {
	  super.testSetObject06();
  }

  /*
   * @testName: testSetObject07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set BigDecimal object for SQL Type
   * Decimal and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object.
   * Compare the result with the extracted value from the tssql.stmt file. Both
   * the values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject07() throws Exception {
	  super.testSetObject07();
  }

  /*
   * @testName: testSetObject08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Boolean object for SQL Type Bit and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getBoolean(int column). Compare the result with the extracted
   * value from the tssql.stmt file. Both the values should be equal.
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject08() throws Exception {
	  super.testSetObject08();
  }

  /*
   * @testName: testSetObject09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Boolean object for SQL Type Bit and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getBoolen(int column). Compare the result with the extracted
   * value from the tssql.stmt file. Both the values should be equal.
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject09() throws Exception {
	  super.testSetObject09();
  }

  /*
   * @testName: testSetObject10
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Integer object for SQL Type Integer
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject10() throws Exception {
	  super.testSetObject10();
  }

  /*
   * @testName: testSetObject11
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Integer object for SQL Type Integer
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Integer object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject11() throws Exception {
	  super.testSetObject11();
  }

  /*
   * @testName: testSetObject12
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Long object for SQL Type Bigint and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Long object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject12() throws Exception {
	  super.testSetObject12();
  }

  /*
   * @testName: testSetObject13
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Long object for SQL Type Bigint and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Long object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject13() throws Exception {
	  super.testSetObject13();
  }

  /*
   * @testName: testSetObject14
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Double object for SQL Type Double
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject14() throws Exception {
	  super.testSetObject14();
  }

  /*
   * @testName: testSetObject15
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Double object for SQL Type Double
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject15() throws Exception {
	  super.testSetObject15();
  }

  /*
   * @testName: testSetObject16
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Double object for SQL Type Float
   * and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject16() throws Exception {
	  super.testSetObject16();
  }

  /*
   * @testName: testSetObject17
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Float object for SQL Type Float and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Float object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject17() throws Exception {
	  super.testSetObject17();
  }

  /*
   * @testName: testSetObject18
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Date object for SQL Type Date and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Date object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject18() throws Exception {
	  super.testSetObject18();
  }

  /*
   * @testName: testSetObject19
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Time object for SQL Type Time and
   * call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Time object. Compare
   * the result with the extracted value from the tssql.stmt file. Both the
   * values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject19() throws Exception {
	  super.testSetObject19();
  }

  /*
   * @testName: testSetObject20
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:696;
   * JDBC:JAVADOC:697; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject(int
   * parameterIndex, Object x) method to set Timestamp object for SQL Type
   * Timestamp & call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Timestamp object.
   * Compare the result with the extracted value from the tssql.stmt file. Both
   * the values should be equal.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetObject20() throws Exception {
	  super.testSetObject20();
  }

}
