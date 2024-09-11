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
 * @(#)dbMetaClient3.java	1.27 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta3;

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
 * The dbMetaClient3 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient3Servlet extends dbMetaClient3 implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta3";
 
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "dbMeta3_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(dbMetaClient3Servlet.class, dbMetaClient3.class);
	       // The servlet descriptor
URL servletUrl = dbMetaClient3Servlet.class.getResource("servlet_vehicle_web.xml");
if(servletUrl != null) {
	archive.addAsManifestResource(servletUrl, "web.xml");
}
// The sun servlet descriptor
URL sunServletUrl = dbMetaClient3Servlet.class.getResource("dbMeta3_servlet_vehicle_web.war.sun-web.xml");
if(sunServletUrl != null) {
	archive.addAsManifestResource(sunServletUrl, "sun-web.xml");
}
// Call the archive processor
archiveProcessor.processWebArchive(archive, dbMetaClient3Servlet.class, sunServletUrl);
		System.out.println(archive.toString(true));
		return archive;
	};



  /* Run test in standalone mode */
  public static void main(String[] args) {
    dbMetaClient3Servlet theTests = new dbMetaClient3Servlet();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testSupportsConvert03
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BINARY, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert03() throws Exception {
		super.testSupportsConvert03();
  }

  /*
   * @testName: testSupportsConvert04
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   * 
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BIT, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert04() throws Exception {
		super.testSupportsConvert04();
  }

  /*
   * @testName: testSupportsConvert05
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(BLOB, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert05() throws Exception {
		super.testSupportsConvert05();
  }

  /*
   * @testName: testSupportsConvert06
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(CHAR, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert06() throws Exception {
		super.testSupportsConvert06();
  }

  /*
   * @testName: testSupportsConvert07
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(CLOB, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert07() throws Exception {
	  super.testSupportsConvert07();
  }

  /*
   * @testName: testSupportsConvert08
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DATE, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert08() throws Exception {
		super.testSupportsConvert08();
  }

  /*
   * @testName: testSupportsConvert09
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DECIMAL, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
  public void testSupportsConvert09() throws Exception {
	  super.testSupportsConvert09();
  }

  /*
   * @testName: testSupportsConvert10
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DISTINCT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert10() throws Exception {
		super.testSupportsConvert10();
  }

  /*
   * @testName: testSupportsConvert11
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(DOUBLE, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert11() throws Exception {
		super.testSupportsConvert11();
  }

  /*
   * @testName: testSupportsConvert12
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(FLOAT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert12() throws Exception {
		super.testSupportsConvert12();
  }

  /*
   * @testName: testSupportsConvert13
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(INTEGER, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert13() throws Exception {
		super.testSupportsConvert13();
  }

  /*
   * @testName: testSupportsConvert14
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(JAVA_OBJECT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert14() throws Exception {
		super.testSupportsConvert14();
  }

  /*
   * @testName: testSupportsConvert15
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(LONGVARBINARY, VARCHAR) method on
   * that object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert15() throws Exception {
		super.testSupportsConvert15();
  }

  /*
   * @testName: testSupportsConvert16
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(LONGVARCHAR, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert16() throws Exception {
		super.testSupportsConvert16();
  }

  /*
   * @testName: testSupportsConvert17
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(NULL, VARCHAR) on that object. It
   * should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert17() throws Exception {
		super.testSupportsConvert17();
  }

  /*
   * @testName: testSupportsConvert18
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(NUMERIC, VARCHAR) method on that
   * object It should return a boolean value; either true or false
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert18() throws Exception {
		super.testSupportsConvert18();
  }

  /*
   * @testName: testSupportsConvert19
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(OTHER, VARCHAR) method on that object
   * It should return a boolean value; either true or false
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert19() throws Exception {
		super.testSupportsConvert19();
  }

  /*
   * @testName: testSupportsConvert20
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(REAL, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert20() throws Exception {
		super.testSupportsConvert20();
  }

  /*
   * @testName: testSupportsConvert21
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(REF, VARCHAR) method on that object.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert21() throws Exception {
		super.testSupportsConvert21();
  }

  /*
   * @testName: testSupportsConvert22
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
   * DataBase and call the supportsConvert(SMALLINT, VARCHAR) method on that
   * object. It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("servlet")
  public void testSupportsConvert22() throws Exception {
		super.testSupportsConvert22();
  }

}
