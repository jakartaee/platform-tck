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
 * @(#)batchUpdateClient.java	1.34 03/05/16
 */
package com.sun.ts.tests.jdbc.ee.batchUpdate;

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
 * The batchUpdateClient class tests methods of Statement, PreparedStatement and
 * CallableStatement interfaces using Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class batchUpdateClientAppClient extends batchUpdateClient implements Serializable {
  private static final String testName = "jdbc.ee.batchUpdate";

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = "appclient", testable = true)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "batchUpdate_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(true, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(batchUpdateClientAppClient.class, batchUpdateClient.class);
		
		  // The appclient-client descriptor
	     URL appClientUrl = batchUpdateClientAppClient.class.getResource("appclient_vehicle_client.xml");
	     if(appClientUrl != null) {
	     	archive.addAsManifestResource(appClientUrl, "application-client.xml");
	     }
	     // The sun appclient-client descriptor
	     URL sunAppClientUrl = batchUpdateClientAppClient.class.getResource("batchUpdate_appclient_vehicle_client.jar.sun-application-client.xml");
	     if(sunAppClientUrl != null) {
	     	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
	     }
	     // Call the archive processor
	     archiveProcessor.processClientArchive(archive, batchUpdateClientAppClient.class, sunAppClientUrl);

		System.out.println(archive.toString(true));
		return archive;
	};
	
  /* Run test in standalone mode */
  public static void main(String[] args) {
    batchUpdateClientAppClient theTests = new batchUpdateClientAppClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testAddBatch01
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:700; JDBC:JAVADOC:701;
   * JDBC:SPEC:23;
   * 
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method with 3 SQL statements and call the executeBatch() method and it
   * should return array of Integer values of length 3
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testAddBatch01() throws Exception {
  }

  /*
   * @testName: testAddBatch02
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:183; JDBC:JAVADOC:184;
   * JDBC:JAVADOC:187; JDBC:JAVADOC:188; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method with
   * 3 SQL statements and call the executeBatch() method and it should return an
   * array of Integer of length 3.
   *
   */
  public void testAddBatch02() throws Exception {
  }

  /*
   * @testName: testAddBatch03
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:700; JDBC:JAVADOC:701;
   * JDBC:JAVADOC:187; JDBC:JAVADOC:188; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a CallableStatement object and call the addBatch()
   * method with 3 SQL statements and call the executeBatch() method and it
   * should return an array of Integer of length 3.
   */
	@Test
	@TargetVehicle("appclient")
  public void testAddBatch03() throws Exception {
  }

  /*
   * @testName: testClearBatch01
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method and call the clearBatch() method and then call executeBatch() to
   * check the call of clearBatch()method The executeBatch() method should
   * return a zero value.
   */
	@Test
	@TargetVehicle("appclient")
  public void testClearBatch01() throws Exception {
  }

  /*
   * @testName: testClearBatch02
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method and
   * call the clearBatch() method and then call executeBatch() to check the call
   * of clearBatch()method.The executeBatch() method should return a zero value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testClearBatch02() throws Exception {
  }

  /*
   * @testName: testClearBatch03
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a CallableStatement object and call the addBatch()
   * method and call the clearBatch() method and then call executeBatch() to
   * check the call of clearBatch()method. The executeBatch() method should
   * return a zero value.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testClearBatch03() throws Exception {
  }

  /*
   * @testName: testExecuteBatch01
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method with a 3 valid SQL statements and call the executeBatch() method It
   * should return an array of Integer values of length 3.
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch01() throws Exception {
  }

  /*
   * @testName: testExecuteBatch02
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the executeBatch()
   * method without calling addBatch() method.It should return an array of zero
   * length.
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch02() throws Exception {
  }

  /*
   * @testName: testExecuteBatch03
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method and call the executeBatch() method with a select Statement It should
   * throw BatchUpdateException
   *
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch03() throws Exception {
  }

  /*
   * @testName: testExecuteBatch04
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method with
   * 3 valid SQL statements and call the executeBatch() method It should return
   * an array of Integer values of length 3
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch04() throws Exception {
  }

  /*
   * @testName: testExecuteBatch05
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184;
   * 
   * @test_Strategy: Get a Statement object and call the executeBatch() method
   * without adding statements into a batch. It should return an array of
   * Integer value of zero length
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch05() throws Exception {
  }

  /*
   * @testName: testExecuteBatch06
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method and
   * call the executeBatch() method with a violation in SQL constraints.It
   * should throw an BatchUpdateException
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch06() throws Exception {
  }

  /*
   * @testName: testExecuteBatch07
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a Statement object and call the addBatch() method and
   * call the executeBatch() method with a select Statement It should throw an
   * BatchUpdateException
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch07() throws Exception {
  }

  /*
   * @testName: testExecuteBatch08
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a CallableStatement object and call the addBatch()
   * method with 3 valid SQL statements and call the executeBatch() method It
   * should return an array of Integer Values of length 3.
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch08() throws Exception {
  }

  /*
   * @testName: testExecuteBatch09
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * 
   * @test_Strategy: Get a CallableStatement object and call the executeBatch()
   * method without adding the statements into Batch. It should return an array
   * of Integer Value of zero length
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch09() throws Exception {
  }

  /*
   * @testName: testExecuteBatch12
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a CallableStatement object with different SQL
   * statements in the stored Procedure and call the addBatch() method with 3
   * statements and call the executeBatch() method It should return an array of
   * Integer Values of length 3.
   */
	@Test
	@TargetVehicle("appclient")
  public void testExecuteBatch12() throws Exception {
  }

  /*
   * @testName: testContinueBatch01
   * 
   * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
   * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
   * 
   * @test_Strategy: Get a PreparedStatement object and call the addBatch()
   * method with 3 SQL statements.Among these 3 SQL statements first is
   * valid,second is invalid and third is again valid. Then call the
   * executeBatch() method and it should return array of Integer values of
   * length 3, if it supports continued updates. Then check whether the third
   * command in the batch after the invalid command executed properly.
   */
	@Test
	@TargetVehicle("appclient")
  public void testContinueBatch01() throws Exception {
  }
}
