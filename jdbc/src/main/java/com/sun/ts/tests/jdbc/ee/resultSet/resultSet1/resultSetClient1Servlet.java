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
 * @(#)resultSetClient1.java	1.32 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.resultSet.resultSet1;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The resultSetClient1 class tests methods of resultSet interface using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class resultSetClient1Servlet extends resultSetClient1 implements Serializable {
  private static final String testName = "jdbc.ee.resultSet.resultSet1";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "resultSet1_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(resultSetClient1Servlet.class, resultSetClient1.class);
	       // The servlet descriptor
URL servletUrl = resultSetClient1Servlet.class.getResource("servlet_vehicle_web.xml");
if(servletUrl != null) {
	archive.addAsManifestResource(servletUrl, "web.xml");
}
// The sun servlet descriptor
URL sunServletUrl = resultSetClient1Servlet.class.getResource("resultSet1_servlet_vehicle_web.war.sun-web.xml");
if(sunServletUrl != null) {
	archive.addAsManifestResource(sunServletUrl, "sun-web.xml");
}
// Call the archive processor
archiveProcessor.processWebArchive(archive, resultSetClient1Servlet.class, sunServletUrl);
		System.out.println(archive.toString(true));
		return archive;
	};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    resultSetClient1Servlet theTests = new resultSetClient1Servlet();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testGetConcurrency
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:492;
   * JDBC:JAVADOC:493; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing the query and call the
   * getConcurrency() method on that resultset object. It should return an
   * Integer value and the value should be equal to any of the values
   * CONCUR_READ_ONLY or CONCUR_UPDATABLE which are defined in the Resutset
   * interface.
   */

	@Test
	@TargetVehicle("servlet")
  public void testGetConcurrency() throws Exception {
		super.testGetConcurrency();
  }

  /*
   * @testName: testGetFetchDirection
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:484;
   * JDBC:JAVADOC:485; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query and call the
   * getFetchDirection() method.It should return an Integer value and the value
   * should be equal to any of the values FETCH_FORWARD or FETCH_REVERSE or
   * FETCH_UNKNOWN which are defined in the Resultset interface.
   *
   */

	@Test
	@TargetVehicle("servlet")
  public void testGetFetchDirection() throws Exception {
		super.testGetFetchDirection();
  }

  /*
   * @testName: testGetFetchSize
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:488;
   * JDBC:JAVADOC:489; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the getFetchSize() method
   * It should return a Integer value which should be greater than or equal to
   * zero.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testGetFetchSize() throws Exception {
		super.testGetFetchSize();
  }

  /*
   * @testName: testGetType
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:490;
   * JDBC:JAVADOC:491; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the getType() method on
   * that object. It should return an Integer value and the value should be
   * equal to any of the values TYPE_FORWARD_ONLY or TYPE_SCROLL_INSENSITIVE or
   * TYPE_SCROLL_SENSITIVE which are defined in the Resultset interface.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testGetType() throws Exception {
		super.testGetType();
  }

  /*
   * @testName: testSetFetchSize01
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:486;
   * JDBC:JAVADOC:487; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object by executing a query and call the
   * setFetchSize(int rows). Set the value of rows to zero. The JDBC driver is
   * free to make its own best guess as to what the fetch size should be. Then
   * call getFetchSize() method.
   * 
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetFetchSize01() throws Exception {
		super.testSetFetchSize01();
  }

  /*
   * @testName: testSetFetchSize02
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:486;
   * JDBC:JAVADOC:487; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the setFetchSize(int rows)
   * on that obect. Call Statement's getMaxRows() before calling setFetchSize()
   * method and pass the returned value from getMaxRows() method as the argument
   * to the setFetchSize() method. Then call getFetchSize() method to check
   * whether the returned value is the same that has been set.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetFetchSize02() throws Exception {
		super.testSetFetchSize02();
  }

  /*
   * @testName: testSetFetchSize03
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:486;
   * JDBC:JAVADOC:487; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the setFetchSize(int rows)
   * on that object with the value greater than the Statement's maximum possible
   * rows as returned by getMaxRows() method and it should throw SQLException.
   * In case if the getMaxRows() method returns 0 which means unlimited rows
   * then appropriate message is displayed and test passes
   */
	@Test
	@TargetVehicle("servlet")
  public void testSetFetchSize03() throws Exception {
		super.testSetFetchSize03();
  }

  /*
   * @testName: testSetFetchSize04
   * 
   * @assertion_ids: JDBC:SPEC:9; JDBC:SPEC:10; JDBC:JAVADOC:486;
   * JDBC:JAVADOC:487; JavaEE:SPEC:191;
   *
   * @test_Strategy: Get a ResultSet object and call the setFetchSize(int rows)
   * on that object. And try to set a negative value .It should throw
   * SQLException.
   */

	@Test
	@TargetVehicle("servlet")
  public void testSetFetchSize04() throws Exception {
		super.testSetFetchSize04();
  }
}
