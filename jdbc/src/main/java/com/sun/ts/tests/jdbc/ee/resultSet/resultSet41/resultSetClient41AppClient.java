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

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet41;

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
 * The resultSetClient41 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 99/10/12
 */

public class resultSetClient41AppClient extends resultSetClient41 implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet41";

  @TargetsContainer("tck-javatest")
  @OverProtocol("appclient")
  @Deployment(name = "appclient", order = 2)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "resultSet41_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(true, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(resultSetClient41AppClient.class, resultSetClient41.class);
		  // The appclient-client descriptor
	     URL appClientUrl = resultSetClient41AppClient.class.getResource("appclient_vehicle_client.xml");
	     if(appClientUrl != null) {
	     	archive.addAsManifestResource(appClientUrl, "application-client.xml");
	     }
	     // The sun appclient-client descriptor
	     URL sunAppClientUrl = resultSetClient41AppClient.class.getResource("resultSet41_appclient_vehicle_client.jar.sun-application-client.xml");
	     if(sunAppClientUrl != null) {
	     	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
	     }
	     // Call the archive processor
	     archiveProcessor.processClientArchive(archive, resultSetClient41AppClient.class, sunAppClientUrl);
		System.out.println(archive.toString(true));
		return archive;
	};
  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient41AppClient theTests = new resultSetClient41AppClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }


  /*
   * @testName: testGetBoolean67
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:406;
   * JDBC:JAVADOC:407; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * maximum value of table Bit_Tab.Call the getBoolean(String columnName)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Maximum Value of JDBC Bit datatype.
   */

	@Test
	@TargetVehicle("appclient")
  public void testGetBoolean67() throws Exception {
		super.testGetBoolean67();
  }

  /*
   * @testName: testGetBoolean68
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:406;
   * JDBC:JAVADOC:407; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that gets the
   * minimum value of table Bit_Tab.Call the getBoolean(String columnName)
   * method. Compare the returned result with the value extracted from
   * tssql.stmt file.Both of them should be equal and the returned result must
   * be equal to the Minimum Value of JDBC Bit datatype.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetBoolean68() throws Exception {
		super.testGetBoolean68();
  }

  /*
   * @testName: testGetBoolean69
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:406;
   * JDBC:JAVADOC:407; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query that returns
   * null value from table Bit_Tab.Call the getBoolean(String columnName)
   * method.Check if the value returned is boolean value false.
   */
	@Test
	@TargetVehicle("appclient")
  public void testGetBoolean69() throws Exception {
		super.testGetBoolean69();
  }
}
