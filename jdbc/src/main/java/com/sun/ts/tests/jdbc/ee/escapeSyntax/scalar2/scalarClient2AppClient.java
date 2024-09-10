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
 * @(#)scalarClient2.java	1.21 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.escapeSyntax.scalar2;

import java.io.IOException;
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
 * The scalarClient2 class tests methods of scalar Functions interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class scalarClient2AppClient extends scalarClient2 {
  private static final String testName = "jdbc.ee.escapeSyntax";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("appclient")
	@Deployment(name = "appclient",  order = 2)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "scalar2_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(true, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(scalarClient2AppClient.class, scalarClient2.class);
		  // The appclient-client descriptor
	     URL appClientUrl = scalarClient2AppClient.class.getResource("appclient_vehicle_client.xml");
	     if(appClientUrl != null) {
	     	archive.addAsManifestResource(appClientUrl, "application-client.xml");
	     }
	     // The sun appclient-client descriptor
	     URL sunAppClientUrl = scalarClient2AppClient.class.getResource("scalar2_appclient_vehicle_client.jar.sun-application-client.xml");
	     if(sunAppClientUrl != null) {
	     	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
	     }
	     // Call the archive processor
	     archiveProcessor.processClientArchive(archive, scalarClient2AppClient.class, sunAppClientUrl);
		System.out.println(archive.toString(true));
		return archive;
	};



  /* Run test in standalone mode */
  public static void main(String[] args) {
    scalarClient2AppClient theTests = new scalarClient2AppClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testAbs
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function abs. It should return a numeric
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testAbs() throws Exception {
		super.testAbs();
  }

  /*
   * @testName: testPower
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function power. It should return a numeric
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testPower() throws Exception {
		super.testPower();
  }

  /*
   * @testName: testRound
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function round. It should return a numeric
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testRound() throws Exception {
		super.testRound();
  }

  /*
   * @testName: testSign
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function sign. It should return an
   * integer.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSign() throws Exception {
		super.testSign();
  }

  /*
   * @testName: testSqrt
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function sqrt. It should return a numeric
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testSqrt() throws Exception {
		super.testSqrt();
  }

  /*
   * @testName: testTruncate
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function truncate. It should return a
   * numeric value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testTruncate() throws Exception {
		super.testTruncate();
  }

  /*
   * @testName: testMod
   * 
   * @assertion_ids: JavaEE:SPEC:181; JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function mod. It should return an integer.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testMod() throws Exception {
		super.testMod();
  }

  /*
   * @testName: testFloor
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function floor. It should return an
   * integer.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testFloor() throws Exception {
		super.testFloor();
  }

  /*
   * @testName: testCeiling
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function ceiling. It should return an
   * integer.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testCeiling() throws Exception {
		super.testCeiling();
  }

  /*
   * @testName: testLog10
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function log10. It should return a numeric
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testLog10() throws Exception {
		super.testLog10();
  }

  /*
   * @testName: testLog
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function log. It should return a numeric
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testLog() throws Exception {
		super.testLog();
  }

  /*
   * @testName: testExp
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function exp. It should return a numeric
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testExp() throws Exception {
		super.testExp();
  }

  /*
   * @testName: testCos
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function cos. It should return a numeric
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testCos() throws Exception {
		super.testCos();
  }

  /*
   * @testName: testTan
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function tan. It should return a numeric
   * value.
   * 
   */
	@Test
	@TargetVehicle("appclient")
  public void testTan() throws Exception {
		super.testTan();
  }

  /*
   * @testName: testCot
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function cot. It should return a numeric
   * value.
   *
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testCot() throws Exception {
		super.testCot();
  }

  /*
   * @testName: testCurdate
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function curdate. It should return a date
   * value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testCurdate() throws Exception {
		super.testCurdate();
  }

  /*
   * @testName: testDayname
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function dayname. It should return a
   * character string.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testDayname() throws Exception {
		super.testDayname();
  }

  /*
   * @testName: testDayofmonth
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function dayofmonth. It should return an
   * integer.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testDayofmonth() throws Exception {
		super.testDayofmonth();
  }

  /*
   * @testName: testDayofweek
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function dayofweek. It should return an
   * integer.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testDayofweek() throws Exception {
		super.testDayofweek();
  }

  /*
   * @testName: testDayofyear
   * 
   * @assertion_ids: JDBC:SPEC:4;
   * 
   * @test_Strategy: Get a Statement object and call the method executeQuery.
   * The query contains a call to the function dayofyear. It should return an
   * integer.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testDayofyear() throws Exception {
		super.testDayofyear();
  }
}
