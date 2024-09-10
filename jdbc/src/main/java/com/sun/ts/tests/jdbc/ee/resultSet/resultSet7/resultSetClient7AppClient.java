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
 * @(#)resultSetClient7.java	1.25 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet7;

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
 * The resultSetClient7 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 9/9/99
 */

public class resultSetClient7AppClient extends resultSetClient7 implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet7";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("appclient")
  @Deployment(name = "appclient", order = 2)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "resultSet7_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(true, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(resultSetClient7AppClient.class, resultSetClient7.class);
		  // The appclient-client descriptor
	     URL appClientUrl = resultSetClient7AppClient.class.getResource("appclient_vehicle_client.xml");
	     if(appClientUrl != null) {
	     	archive.addAsManifestResource(appClientUrl, "application-client.xml");
	     }
	     // The sun appclient-client descriptor
	     URL sunAppClientUrl = resultSetClient7AppClient.class.getResource("resultSet7_appclient_vehicle_client.jar.sun-application-client.xml");
	     if(sunAppClientUrl != null) {
	     	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
	     }
	     // Call the archive processor
	     archiveProcessor.processClientArchive(archive, resultSetClient7AppClient.class, sunAppClientUrl);
		System.out.println(archive.toString(true));
		return archive;
	};

  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient7AppClient theTests = new resultSetClient7AppClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testGetObject61
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(int column index) method with the SQL null column of
   * JDBC datatype SMALLINT. It should return null Integer object.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject61() throws Exception {
		super.testGetObject61();
  }

  /*
   * @testName: testGetObject69
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String column index) method with the column of JDBC
   * datatype SMALLINT. It should return an Integer object that has been set as
   * the maximum value of SMALLINT.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject69() throws Exception {
		super.testGetObject69();
  }

  /*
   * @testName: testGetObject70
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String column index) method with the SQL column of JDBC
   * datatype SMALLINT. It should return an Integer object that has been set as
   * the minimum value of SMALLINT.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject70() throws Exception {
		super.testGetObject70();
  }

  /*
   * @testName: testGetObject62
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JDBC:JAVADOC:366; JDBC:JAVADOC:367; JDBC:JAVADOC:442;
   * JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the column of JDBC
   * datatype SMALLINT. It should return an Integer object that has been set as
   * the maximum value of SMALLINT.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject62() throws Exception {
		super.testGetObject62();
  }

  /*
   * @testName: testGetObject63
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the SQL column of JDBC
   * datatype SMALLINT. It should return an Integer object that has been set as
   * the minimum value of SMALLINT.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject63() throws Exception {
		super.testGetObject63();
  }

  /*
   * @testName: testGetObject64
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the SQL null column of
   * JDBC datatype SMALLINT. It should return null Integer object.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject64() throws Exception {
		super.testGetObject64();
  }

  /*
   * @testName: testGetObject65
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(int columnIndex) method with the SQL column of JDBC
   * datatype VARCHAR. It should return an String object that has been set.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject65() throws Exception {
		super.testGetObject65();
  }

  /*
   * @testName: testGetObject66
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:444;
   * JDBC:JAVADOC:445; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(int column index) method with the SQL null column of
   * JDBC datatype VARCHAR. It should return null String object.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject66() throws Exception {
		super.testGetObject66();
  }

  /*
   * @testName: testGetObject67
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JDBC:JAVADOC:442; JDBC:JAVADOC:443; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the SQL column of JDBC
   * datatype VARCHAR. It should return an String object that has been set.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject67() throws Exception {
		super.testGetObject67();
  }

  /*
   * @testName: testGetObject68
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:446;
   * JDBC:JAVADOC:447; JavaEE:SPEC:191;
   * 
   * @test_Strategy: Get a ResultSet object from the Connection to the database.
   * Call the getObject(String columnName) method with the SQL null column of
   * JDBC datatype VARCHAR. It should return null String object.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetObject68() throws Exception {
		super.testGetObject68();
  }
}
