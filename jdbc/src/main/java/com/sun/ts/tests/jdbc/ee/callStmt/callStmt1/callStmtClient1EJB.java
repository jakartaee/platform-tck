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
 * @(#)callStmtClient1.java	1.20 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt1;

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
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

/**
 * The callStmtClient1 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */
public class callStmtClient1EJB extends callStmtClient1 implements Serializable {
	
    @TargetsContainer("tck-javatest")
    @OverProtocol("appclient")
    @Deployment(name = "ejb", order=2)
	public static EnterpriseArchive createDeploymentEJB(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt_ejb_vehicle.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");

		URL resURL = callStmtClient1EJB.class.getResource("com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(true, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient1EJB.class, callStmtClient1.class);

		ejb.addAsManifestResource(callStmtClient1EJB.class.getPackage(), "ejb_vehicle_ejb.xml");

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "batchUpdate_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		System.out.println(ear.toString(true));
		return ear;
	};

  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient1EJB theTests = new callStmtClient1EJB();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }


  /*
   * @testName: testGetBigDecimal01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
   * JDBC:JAVADOC:1270; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getBigDecimal(int
   * parameterIndex) method to retrieve the maximum value of the Numeric_Tab.
   * Extract the maximum value from the tssql.stmt file. Compare this value with
   * the value returned by the getBigDecimal(int parameterIndex) method.Both the
   * values should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetBigDecimal01() throws Exception {
	  super.testGetBigDecimal01();
  }

  /*
   * @testName: testGetBigDecimal02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
   * JDBC:JAVADOC:1270; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getBigDecimal(int
   * parameterIndex) method to retrieve the minimum value of the Numeric_Tab.
   * Extract the minimum value from the tssql.stmt file.Compare this value with
   * the value returned by the getBigDecimal(int parameterIndex).Both the values
   * should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetBigDecimal02() throws Exception {
	  super.testGetBigDecimal02();
  }

  /*
   * @testName: testGetBigDecimal03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1269;
   * JDBC:JAVADOC:1270; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getBigDecimal(int
   * parameterIndex) method to retrieve the null value from Numeric_Tab. Check
   * if it returns null
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetBigDecimal03() throws Exception {
	  super.testGetBigDecimal03();
   }
  

  /*
   * @testName: testGetDouble01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
   * JDBC:JAVADOC:1256; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getDouble(int
   * parameterIndex) method to retrieve the maximum value of the Float_Tab.
   * Extract the maximum value from the tssql.stmt file.Compare this value with
   * the value returned by the getDouble(int parameterIndex). Both the values
   * should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetDouble01() throws Exception {
	  super.testGetDouble01();
  }

  /*
   * @testName: testGetDouble02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
   * JDBC:JAVADOC:1256; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getDouble(int
   * parameterIndex) method to retrieve the minimum value of the Float_Tab.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getDouble(int parameterIndex). Both the values
   * should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetDouble02() throws Exception {
	  super.testGetDouble02();
  }

  /*
   * @testName: testGetDouble03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1255;
   * JDBC:JAVADOC:1256; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getDouble(int
   * parameterIndex) method to retrieve the null value from Float_Tab. Check if
   * it returns null
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetDouble03() throws Exception {
	  super.testGetDouble03();
   }

  /*
   * @testName: testGetShort01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1247;
   * JDBC:JAVADOC:1248; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:3;
   * JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getShort(int
   * parameterIndex) method to retrieve the maximum value of the Smallint_Tab.
   * Extract the maximum value from the tssql.stmt file.Compare this value with
   * the value returned by the getShort(int parameterIndex). Both the values
   * should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetShort01() throws Exception {
	  super.testGetShort01();
   }

  /*
   * @testName: testGetShort02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1247;
   * JDBC:JAVADOC:1248; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getShort(int
   * parameterIndex) method to retrieve the minimum value of the Smallint_Tab.
   * Extract the minimum value from the tssql.stmt file.Compare this value with
   * the value returned by the getShort(int parameterIndex). Both the values
   * should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetShort02() throws Exception {
	  super.testGetShort02();
  }

  /*
   * @testName: testGetShort03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1247;
   * JDBC:JAVADOC:1248; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getShort(int
   * parameterIndex) method to retrieve the null value from the Smallint_Tab.
   * Check if it returns null
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetShort03() throws Exception {
	  super.testGetShort03();
  }

  /*
   * @testName: testGetString01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
   * JDBC:JAVADOC:1242; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getString(int
   * parameterIndex) method to retrieve a non null String value from Char_Tab.
   * Extract the same String value from the tssql.stmt file.Compare this value
   * with the value returned by the getString(int parameterIndex). Both the
   * values should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetString01() throws Exception {
	  super.testGetString01();
   }

  /*
   * @testName: testGetString02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1241;
   * JDBC:JAVADOC:1242; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getString(int
   * parameterIndex) method to retrieve the null value from Char_Tab. Check if
   * it returns null
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetString02() throws Exception {
	  super.testGetString02();
  }

  /*
   * @testName: testGetInt01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1249;
   * JDBC:JAVADOC:1250; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:4;
   * JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getInt(int
   * parameterIndex) method to retrieve the maximum value from Integer_Tab.
   * Extract the maximum value from the tssql.stmt file.Compare this value with
   * the value returned by the getInt(int parameterIndex). Both the values
   * should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetInt01() throws Exception {
	  super.testGetInt01();
  }

  /*
   * @testName: testGetInt02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1249;
   * JDBC:JAVADOC:1250; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getInt(int
   * parameterIndex) method to retrieve the minimum value from Integer_Tab.
   * Extract the minimum value from the tssql.stmt file. Compare this value with
   * the value returned by the getInt(int parameterIndex). Both the values
   * should be equal.
   */
	@Test
	@TargetVehicle("ejb")
  public void testGetInt02() throws Exception {
	  super.testGetInt02();
  }

  /*
   * @testName: testGetInt03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1249;
   * JDBC:JAVADOC:1250; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getInt(int
   * parameterIndex) method to retrieve the null value. Check if it returns null
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetInt03() throws Exception {
	  super.testGetInt03();
  }

  /*
   * @testName: testGetBoolean01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1243;
   * JDBC:JAVADOC:1244; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:1;
   * JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getBoolean(int
   * parameterIndex) method to retrieve the maximum value of Bit_Tab. Extract
   * the maximum value from the tssql.stmt file.Compare this value with the
   * value returned by the getBoolean(int parameterIndex).Both the values should
   * be equal.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetBoolean01() throws Exception {
	  super.testGetBoolean01();
  }

  /*
   * @testName: testGetBoolean02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1243;
   * JDBC:JAVADOC:1244; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:1;
   * JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getBoolean(int
   * parameterIndex) method.to retrieve the minimum value of Bit_Tab. Extract
   * the minimum value from the tssql.stmt file. Compare this value with the
   * value returned by the getBoolean(int parameterIndex) Both the values should
   * be equal.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetBoolean02() throws Exception {
	  super.testGetBoolean02();
  }

  /*
   * @testName: testGetLong01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1251;
   * JDBC:JAVADOC:1252; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JDBC:JAVADOC:5;
   * JavaEE:SPEC:183; JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getLong(int
   * parameterIndex) method to retrieve the maximum value of Bigint_Tab. Extract
   * the maximum value from the tssql.stmt file.Compare this value with the
   * value returned by the getLong(int parameterIndex). Both the values should
   * be equal.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetLong01() throws Exception {
	  super.testGetLong01();
  }

  /*
   * @testName: testGetLong02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1251;
   * JDBC:JAVADOC:1252; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getLong(int
   * parameterIndex) method to retrieve the minimum value from Bigint_Tab.
   * Extract the minimum value from the tssql.stmt file.Compare this value with
   * the value returned by the getLong(int parameterIndex) Both the values
   * should be equal.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetLong02() throws Exception {
	  super.testGetLong02();
  }

  /*
   * @testName: testGetLong03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1251;
   * JDBC:JAVADOC:1252; JDBC:JAVADOC:1145; JDBC:JAVADOC:1146; JavaEE:SPEC:183;
   * JavaEE:SPEC:185;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database.Execute the stored procedure and call the getLong(int
   * parameterIndex) method to retrieve the null value from Bigint_Tab. Check if
   * it returns null
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetLong03() throws Exception {
	  super.testGetLong03();
  }
}
