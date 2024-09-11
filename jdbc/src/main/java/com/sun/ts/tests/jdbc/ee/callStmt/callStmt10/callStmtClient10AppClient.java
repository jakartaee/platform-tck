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

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt10;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient10 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient10AppClient extends callStmtClient10 implements Serializable {
	
    @TargetsContainer("tck-javatest")
    @OverProtocol("appclient")
	@Deployment(name = "appclient", order=2)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "callStmt10_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(true, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(callStmtClient10AppClient.class, callStmtClient10.class);
		
		  // The appclient-client descriptor
	     URL appClientUrl = callStmtClient10AppClient.class.getResource("appclient_vehicle_client.xml");
	     if(appClientUrl != null) {
	     	archive.addAsManifestResource(appClientUrl, "application-client.xml");
	     }
	     // The sun appclient-client descriptor
	     URL sunAppClientUrl = callStmtClient10AppClient.class.getResource("callStmt10_appclient_vehicle_client.jar.sun-application-client.xml");
	     if(sunAppClientUrl != null) {
	     	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
	     }
	     // Call the archive processor
	     archiveProcessor.processClientArchive(archive, callStmtClient10AppClient.class, sunAppClientUrl);

		System.out.println(archive.toString(true));
		return archive;
	};

	
  /* Run test in standalone mode */

  public static void main(String[] args) {
    callStmtClient10AppClient theTests = new callStmtClient10AppClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testSetObject41
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:6; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type FLOAT and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject41() throws Exception {
	  super.testSetObject41();
  }

  /*
   * @testName: testSetObject42
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:8; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DOUBLE and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject42() throws Exception {
	  super.testSetObject42();
  }

  /*
   * @testName: testSetObject43
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DOUBLE and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Double object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject43() throws Exception {
	  super.testSetObject43();
  }

  /*
   * @testName: testSetObject44
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JDBC:JAVADOC:10; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DECIMAL and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject44() throws Exception {
	  super.testSetObject44();
  }

  /*
   * @testName: testSetObject45
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DECIMAL and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject45() throws Exception {
	  super.testSetObject45();
  }

  /*
   * @testName: testSetObject46
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JDBC:JAVADOC:9; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type NUMERIC and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject46() throws Exception {
	  super.testSetObject46();
  }

  /*
   * @testName: testSetObject47
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:692;
   * JDBC:JAVADOC:693; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type NUMERIC and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a BigDecimal object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject47() throws Exception {
	  super.testSetObject47();
  }

  /*
   * @testName: testSetObject48
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:1; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type BIT and call statement.executeQuery(String sql) method and call
   * ResultSet.getBoolean(int column). It should return a boolean value that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   * -Description details- This test is actually testing the ability to
   * successfully call the setObject() method. In order to do that, this test
   * will get the MAXVAL value from the bit tab, and then it will use the
   * setObject method to set/change the MINVAL value to be the same as the
   * MAXVAL. After setting the MINVAL, a query is done to get the newly set
   * MINVAL value and a comparison is done to make sure it was indeed set to the
   * same value as MAXVAL.
   * 
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject48() throws Exception {
	  super.testSetObject48();
  }

  /*
   * @testName: testSetObject49
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:1; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type BIT and call statement.executeQuery(String sql) method and call
   * ResultSet.getBoolean(int column). It should return a boolean value that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   * -Description details- This test is actually testing the ability to
   * successfully call the setObject() method. In order to do that, this test
   * will get the MINVAL value from the bit tab, and then it will use the
   * setObject method to set/change the MAXVAL value to be the same as the
   * MINVAL. After setting the MAXVAL, a query is done to get the newly set
   * MAXVAL value and a comparison is done to make sure it was indeed set to the
   * same value as MINVAL.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject49() throws Exception {
	  super.testSetObject49();
  }

  /*
   * @testName: testSetObject50
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:11; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type CHAR and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject50() throws Exception {
	  super.testSetObject50();
  }

  /*
   * @testName: testSetObject51
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:12; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type VARCHAR and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a String object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject51() throws Exception {
	  super.testSetObject51();
  }

  /*
   * @testName: testSetObject52
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:13; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type LONGVARCHAR and call statement.executeQuery(String sql) method and
   * call ResultSet.getObject(int column). It should return a String object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject52() throws Exception {
	  super.testSetObject52();
  }

  /*
   * @testName: testSetObject56
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:14;JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type DATE and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Date object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject56() throws Exception {
	  super.testSetObject56();
  }

  /*
   * @testName: testSetObject57
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:15; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type TIME and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Time object that is
   * been set. Compare the result with the extracted value from the tssql.stmt
   * file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject57() throws Exception {
	  super.testSetObject57();
  }

  /*
   * @testName: testSetObject58
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:16; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database execute the stored procedure and call the setObject(int
   * parameterIndex, Object x,int jdbcType) method to set String object for SQL
   * Type TIMESTAMP and call statement.executeQuery(String sql) method and call
   * ResultSet.getObject(int column). It should return a Timestamp object that
   * is been set. Compare the result with the extracted value from the
   * tssql.stmt file. Both the values should be equal.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject58() throws Exception {
	  super.testSetObject58();
   }

  /*
   * @testName: testSetObject59
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:2; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database. Using the IN parameter of that
   * object,update the column Min_Val of the Tinyint_Tab with the maximum value
   * of the Tinyint_Tab. Execute a query to retrieve the Min_Val from
   * Tinyint_Tab. Compare the returned value with the maximum value extracted
   * from tssql.stmt file. Both of them should be equal
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject59() throws Exception {
	  super.testSetObject59();
  }

  /*
   * @testName: testSetObject60
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:694;
   * JDBC:JAVADOC:695; JDBC:JAVADOC:2; JavaEE:SPEC:186;
   *
   * @test_Strategy: This test case is meant for checking the support for IN
   * parameter in CallableStatement Interface. Get a CallableStatement object
   * from the connection to the database Using the IN parameter of that
   * object,update the column Null_Val of the Tinyint_Tab with the minimum value
   * of the Tinyint_Tab. Execute a query to retrieve the Null_Val from
   * Tinyint_Tab. Compare the returned value with the minimum value extracted
   * from tssql.stmt file,Both of them should be equal
   */
	@Test
	@TargetVehicle("appclient")
  public void testSetObject60() throws Exception {
	  super.testSetObject60();
  } 
}
