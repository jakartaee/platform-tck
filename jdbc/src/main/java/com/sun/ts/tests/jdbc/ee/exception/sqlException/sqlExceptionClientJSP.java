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
 * @(#)sqlExceptionClient.java	1.16 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.exception.sqlException;

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
 * The sqlExceptionClient class tests methods of SQLException class using Sun's
 * J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class sqlExceptionClientJSP extends sqlExceptionClient implements Serializable {
  private static final String testName = "jdbc.ee.exception.sqlException";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "sqlException_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(sqlExceptionClientJSP.class, sqlExceptionClient.class);
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        archive.add(new ByteArrayAsset(clientHtml), "client.html");
        
	       // The jsp descriptor
     URL jspUrl = sqlExceptionClientJSP.class.getResource("jsp_vehicle_web.xml");
     if(jspUrl != null) {
     	archive.addAsManifestResource(jspUrl, "web.xml");
     }
     // The sun jsp descriptor
     URL sunJSPUrl = sqlExceptionClientJSP.class.getResource("sqlException_jsp_vehicle_web.war.sun-web.xml");
     if(sunJSPUrl != null) {
     	archive.addAsManifestResource(sunJSPUrl, "sun-web.xml");
     }
     // Call the archive processor
     archiveProcessor.processWebArchive(archive, sqlExceptionClientJSP.class, sunJSPUrl);

        
		archive.addAsWebInfResource(sqlExceptionClientJSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
		System.out.println(archive.toString(true));
		return archive;
	};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    sqlExceptionClientJSP theTests = new sqlExceptionClientJSP();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testSQLException01
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:68;
   * 
   * @test_Strategy: This method constructs a SQLException Object with no
   * arguments and for that object the reason,SQLState and ErrorCode are checked
   * for default values.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSQLException01() throws Exception {
		super.testSQLException01();
  }

  /*
   * @testName: testSQLException02
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:67;
   * 
   * @test_Strategy: This method constructs a SQLException Object with one
   * argument and for that object the SQLState, ErrorCode are checked for
   * default values.The reason is checked for whatever is been assigned while
   * creating the new instance.
   */

	@Test
	@TargetVehicle("jsp")
  public void testSQLException02() throws Exception {
		super.testSQLException02();
  }

  /*
   * @testName: testSQLException03
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:66;
   * 
   * @test_Strategy: This method constructs a SQLException Object with two
   * arguments and for that object ErrorCode is checked for default values.The
   * reason and SQLState are checked for whatever is been assigned while
   * creating the new instance.
   */
	@Test
	@TargetVehicle("jsp")
  public void testSQLException03() throws Exception {
		super.testSQLException03();
  }

  /*
   * @testName: testSQLException04
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:65;
   * 
   * @test_Strategy: This method constructs a SQLException Object with three
   * arguments .The reason,SQLState and Errorcode is checked for whatever is
   * been assigned while creating the new instance.
   */
	@Test
	@TargetVehicle("jsp")
  public void testSQLException04() throws Exception {
		super.testSQLException04();
  }
	

  /*
   * @testName: testGetErrorCode
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:70;
   * 
   * @testStartegy: The SQLException object is generated by executing an
   * incomplete SQL Statement and the getErrorCode() method of that object is
   * checked whether it returns an integer.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetErrorCode() throws Exception {
		super.testGetErrorCode();
  }

  /*
   * @testName: testGetSQLState
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:69;
   * 
   * @testStartegy: The SQLException object is generated by executing an
   * incomplete SQL Statement and the getSQLState() method of that object is
   * checked whether it is an instance of java.lang.String.
   */

	@Test
	@TargetVehicle("jsp")
  public void testGetSQLState() throws Exception {
		super.testGetSQLState();
  }

  /*
   * @testName: testGetNextException
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:71;
   * 
   * @test_Strategy: SQLException object is generated by executing an incomplete
   * SQL Statement and using setNextException method a SQLException object is
   * chained. This is checked using the getNextException method which should
   * return a instanceof SQLException object.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetNextException() throws Exception {
		super.testGetNextException();
  }

  /*
   * @testName: testSetNextException
   * 
   * @assertion_ids: JDBC:SPEC:6; JDBC:JAVADOC:72;
   * 
   * @test_Strategy: SQLException object is obtained by executing a incomplete
   * SQLStatement and setNextException() method on the object will set a chain
   * of SQLException on that object which can be checked by using
   * getNextException() method.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSetNextException() throws Exception {
		super.testSetNextException();
  }

}
