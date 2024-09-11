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

package com.sun.ts.tests.jdbc.ee.prepStmt.prepStmt3;

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
 * The prepStmtClient3 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.8, 11/24/00
 */

public class prepStmtClient3JSP extends prepStmtClient3 implements Serializable {
  private static final String testName = "jdbc.ee.prepStmt.prepStmt3";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "prepStmt3_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(prepStmtClient3JSP.class, prepStmtClient3.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        archive.add(new ByteArrayAsset(clientHtml), "client.html");
        
	       // The jsp descriptor
  URL jspUrl = prepStmtClient3JSP.class.getResource("jsp_vehicle_web.xml");
  if(jspUrl != null) {
  	archive.addAsManifestResource(jspUrl, "web.xml");
  }
  // The sun jsp descriptor
  URL sunJSPUrl = prepStmtClient3JSP.class.getResource("prepStmt3_jsp_vehicle_web.war.sun-web.xml");
  if(sunJSPUrl != null) {
  	archive.addAsManifestResource(sunJSPUrl, "sun-web.xml");
  }
  // Call the archive processor
  archiveProcessor.processWebArchive(archive, prepStmtClient3JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(prepStmtClient3JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
		System.out.println(archive.toString(true));
		return archive;
	};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    prepStmtClient3JSP theTests = new prepStmtClient3JSP();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }


  /*
   * @testName: testSetString01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
   * JDBC:JAVADOC:372; JDBC:JAVADOC:373; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setString(int parameterIndex, String x) method,update
   * the column value with the maximum value of Char_Tab. Call the
   * getString(String columnName) method to retrieve this value. Extract the
   * maximum value from the tssql.stmt file. Compare this value with the value
   * returned by the getString(String columnName) method. Both the values should
   * be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetString01() throws Exception {
		super.testSetString01();
  }

  /*
   * @testName: testSetTime01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:680; JDBC:JAVADOC:681;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setTime(int parameterIndex, Time x) method,update the
   * column value with the Non-Null Time value. Call the getTime(int columnno)
   * method to retrieve this value. Extract the Time value from the tssql.stmt
   * file. Compare this value with the value returned by the getTime(int
   * columnno) method. Both the values should be equal
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetTime01() throws Exception {
		super.testSetTime01();
  }

  /*
   * @testName: testSetTime02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:716; JDBC:JAVADOC:717;
   * JDBC:JAVADOC:616; JDBC:JAVADOC:617; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setTime(int parameterIndex, Time x, Calendar cal)
   * method,update the column value with the Non-Null Time value using the
   * Calendar Object. Call the getTime(int columnno) method to retrieve this
   * value. Extract the Time value from the tssql.stmt file. Compare this value
   * with the value returned by the getTime(int columnno) method. Both the
   * values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetTime02() throws Exception {
		super.testSetTime02();
  }

  /*
   * @testName: testSetTimestamp01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:682; JDBC:JAVADOC:683;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setTimestamp(int parameterIndex, Timestamp x)
   * method,update the column value with the Non-Null Timestamp value. Call the
   * getTimestamp(int columnno) method to retrieve this value. Extract the
   * Timestamp value from the tssql.stmt file. Compare this value with the value
   * returned by the getTimestamp(int columnno) method. Both the values should
   * be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetTimestamp01() throws Exception {
		super.testSetTimestamp01();
  }

  /*
   * @testName: testSetTimestamp02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:718; JDBC:JAVADOC:719;
   * JDBC:JAVADOC:620; JDBC:JAVADOC:621; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setTimestamp(int parameterIndex, Time x, Calendar cal)
   * method,update the column value with the Non-Null Timestamp value using the
   * Calendar Object. Call the getTimestamp(int columnno) method to retrieve
   * this value. Extract the Timestamp value from the tssql.stmt file. Compare
   * this value with the value returned by the getTimestamp(int columnno)
   * method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetTimestamp02() throws Exception {
		super.testSetTimestamp02();
  }

  /*
   * @testName: testSetString02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
   * JDBC:JAVADOC:372; JDBC:JAVADOC:373; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setString(int parameterIndex, String x),update the column
   * with the maximum value which is a SQL VARCHAR. Call the getString(int
   * ColumnIndex) method to retrieve this value. Extract the maximum value as a
   * String from the tssql.stmt file. Compare this value with the value returned
   * by the getString method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetString02() throws Exception {
		super.testSetString02();
  }

  /*
   * @testName: testSetFloat01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:668; JDBC:JAVADOC:669;
   * JDBC:JAVADOC:384; JDBC:JAVADOC:385; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setFloat(int parameterIndex,float x),update the column with
   * the minimum value of Real_Tab. Now execute a query to get the minimum value
   * and retrieve the result of the query using the getFloat(int columnIndex)
   * method.Compare the returned value, with the minimum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetFloat01() throws Exception {
		super.testSetFloat01();
  }

  /*
   * @testName: testSetFloat02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:674; JDBC:JAVADOC:675;
   * JDBC:JAVADOC:384; JDBC:JAVADOC:385; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using setFloat(int parameterIndex,float x),update the column with
   * the maximum value of Real_Tab. Now execute a query to get the maximum value
   * and retrieve the result of the query using the getFloat(int columnIndex)
   * method.Compare the returned value, with the maximum value extracted from
   * the tssql.stmt file. Both of them should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetFloat02() throws Exception {
		super.testSetFloat02();
  }

  /*
   * @testName: testSetBytes01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement for updating Non-Null value
   * from Binary_Tab by calling setBytes(int parameterIndex, byte[] x) and call
   * the getBytes(int) method to check and it should return a Byte Array
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetBytes01() throws Exception {
		super.testSetBytes01();
  }

  /*
   * @testName: testSetBytes02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement for updating Non-Null value
   * from Varbinary_Tab by calling setBytes(int parameterIndex, byte[] x) and
   * call the getBytes(int) method to check and it should return a Byte Array
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetBytes02() throws Exception {
		super.testSetBytes02();
  }

  /*
   * @testName: testSetBytes03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:676; JDBC:JAVADOC:677;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. execute the precompiled SQL Statement for updating Non-Null value
   * from Longvarbinary_Tab by calling setBytes(int parameterIndex, byte[] x)
   * and call the getBytes(int) method to check and it should return a Byte
   * Array
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetBytes03() throws Exception {
		super.testSetBytes03();
  }

  /*
   * @testName: testSetObject01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_Val with the maximum value of Char_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnno) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject01() throws Exception {
		super.testSetObject01();
  }

  /*
   * @testName: testSetObject02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the maximum value of Varchar_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject02() throws Exception {
		super.testSetObject02();
  }

  /*
   * @testName: testSetObject03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the maximum value of Longvarchar_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject03() throws Exception {
		super.testSetObject03();
  }

  /*
   * @testName: testSetObject04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the maximum value of Numeric_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject04() throws Exception {
		super.testSetObject04();
  }

  /*
   * @testName: testSetObject05
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the minimum value of Numeric_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject05() throws Exception {
		super.testSetObject05();
  }

  /*
   * @testName: testSetObject06
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_val with the maximum value of Decimal_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject06() throws Exception {
		super.testSetObject06();
  }

  /*
   * @testName: testSetObject07
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the minimum value of Decimal_Tab. Call the
   * getObject(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getObject(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject07() throws Exception {
		super.testSetObject07();
  }

  /*
   * @testName: testSetObject08
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JDBC:JAVADOC:374; JDBC:JAVADOC:375; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Min_val with the maximum value of Bit_Tab. Call the
   * getBoolean(int columnno) method to retrieve this value. Extract the maximum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getBoolean(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject08() throws Exception {
		super.testSetObject08();
  }

  /*
   * @testName: testSetObject09
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:JAVADOC:696; JDBC:JAVADOC:697;
   * JDBC:JAVADOC:374; JDBC:JAVADOC:375; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a PreparedStatement object from the connection to the
   * database. Using the setObject(int parameterIndex, Object x) method,update
   * the column Null_val with the minimum value of Bit_Tab. Call the
   * getBoolean(int columnno) method to retrieve this value. Extract the minimum
   * value from the tssql.stmt file. Compare this value with the value returned
   * by the getBoolean(int columnNo) method. Both the values should be equal.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSetObject09() throws Exception {
		super.testSetObject09();
  }
}
