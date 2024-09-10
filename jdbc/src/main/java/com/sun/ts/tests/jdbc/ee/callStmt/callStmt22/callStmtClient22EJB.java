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
 * @(#)callStmtClient22.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.callStmt.callStmt22;

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

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The callStmtClient22 class tests methods of CallableStatement interface (to
 * check the Support for IN, OUT and INOUT parameters of Stored Procedure) using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class callStmtClient22EJB extends callStmtClient22 implements Serializable {
  private static final String testName = "jdbc.ee.callStmt.callStmt22";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("appclient")
	@Deployment(name = "ejb",   order = 2)
	public static EnterpriseArchive createDeploymentejb(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "callStmt22_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejbClient.addPackages(true, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		URL resURL = callStmtClient22EJB.class.getResource("com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "callStmt22_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(true, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(callStmtClient22EJB.class, callStmtClient22.class);

		ejb.addAsManifestResource(callStmtClient22EJB.class.getPackage(), "ejb_vehicle_ejb.xml");

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "callStmt22_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;

	};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    callStmtClient22EJB theTests = new callStmtClient22EJB();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testRegisterOutParameter49
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Date value in null column of Date table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getDate method. It should return a Date object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testRegisterOutParameter49() throws Exception {
		super.testRegisterOutParameter49();
  }

  /*
   * @testName: testRegisterOutParameter50
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Time Object in null column of Time table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getTime method. It should return a Time object that is been set. (Note:
   * This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testRegisterOutParameter50() throws Exception {
		super.testRegisterOutParameter50();
  }

  /*
   * @testName: testRegisterOutParameter51
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Timestamp value in null column of Timestamp table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Timestamp object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testRegisterOutParameter51() throws Exception {
		super.testRegisterOutParameter51();
  }

  /*
   * @testName: testRegisterOutParameter52
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Byte Array object in Binary table and call registerOutParameter(int
   * parameterIndex, int jdbcType) method and call getObject method. It should
   * return a Byte Array object that is been set. (Note: This test case also
   * checks the support for INOUT parameter in Stored Procedure)
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testRegisterOutParameter52() throws Exception {
		super.testRegisterOutParameter52();
  }

  /*
   * @testName: testRegisterOutParameter53
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Byte Array object in Varbinary table and call registerOutParameter(int
   * parameterIndex, int jdbcType) method and call getObject method. It should
   * return a Byte Array object that is been set. (Note: This test case also
   * checks the support for INOUT parameter in Stored Procedure)
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testRegisterOutParameter53() throws Exception {
		super.testRegisterOutParameter53();
  }

  /*
   * @testName: testRegisterOutParameter54
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:1235;
   * JDBC:JAVADOC:1236; JavaEE:SPEC:186;
   *
   * @test_Strategy: Get a CallableStatement object from the connection to the
   * database. execute the stored procedure and call the setObject() method to
   * set Byte Array object in Longvarbinary table and call
   * registerOutParameter(int parameterIndex, int jdbcType) method and call
   * getObject method. It should return a Byte Array object that is been set.
   * (Note: This test case also checks the support for INOUT parameter in Stored
   * Procedure)
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void testRegisterOutParameter54() throws Exception {
		super.testRegisterOutParameter54();
   }

}
