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
 * @(#)dbMetaClient11.java	1.30 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta11;

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
 * The dbMetaClient11 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

public class dbMetaClient11JSP extends dbMetaClient11 implements Serializable {
  private static final String testName = "jdbc.ee.dbMeta.dbMeta11";
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "dbMeta11_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
        archive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
        archive.add(new ByteArrayAsset(clientHtml), "client.html");
        
		archive.addClasses(dbMetaClient11JSP.class, dbMetaClient11.class);
		
	       // The jsp descriptor
     URL jspUrl = dbMetaClient11JSP.class.getResource("jsp_vehicle_web.xml");
     if(jspUrl != null) {
     	archive.addAsManifestResource(jspUrl, "web.xml");
     }
     // The sun jsp descriptor
     URL sunJSPUrl = dbMetaClient11JSP.class.getResource("dbMeta11_jsp_vehicle_web.war.sun-web.xml");
     if(sunJSPUrl != null) {
     	archive.addAsManifestResource(sunJSPUrl, "sun-web.xml");
     }
     // Call the archive processor
     archiveProcessor.processWebArchive(archive, dbMetaClient11JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(dbMetaClient11JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
		System.out.println(archive.toString(true));
		return archive;
	};


 
  /* Run test in standalone mode */
  public static void main(String[] args) {
    dbMetaClient11JSP theTests = new dbMetaClient11JSP();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

 
  /*
   * @testName: testSupportsTransactionIsolationLevel2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with the isolation level TRANSACTION_READ_COMMITTED.
   * It should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSupportsTransactionIsolationLevel2() throws Exception {
		super.testSupportsTransactionIsolationLevel2();
   }

  /*
   * @testName: testSupportsTransactionIsolationLevel3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with the isolation level
   * TRANSACTION_READ_UNCOMMITTED. It should return a boolean value; either true
   * or false.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSupportsTransactionIsolationLevel3() throws Exception {
		super.testSupportsTransactionIsolationLevel3();
   }

  /*
   * @testName: testSupportsTransactionIsolationLevel4
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with isolation level TRANSACTION_REPEATABLE_READ. It
   * should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSupportsTransactionIsolationLevel4() throws Exception {
		super.testSupportsTransactionIsolationLevel4();
			
   }

  /*
   * @testName: testSupportsTransactionIsolationLevel5
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1032; JDBC:JAVADOC:1033;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the supportsTransactionIsolationLevel(int isolevel)
   * method on that object with isolation level TRANSACTION_SERIALIZABLE. It
   * should return a boolean value; either true or false.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testSupportsTransactionIsolationLevel5() throws Exception {
		super.testSupportsTransactionIsolationLevel5();
  }

  /*
   * @testName: testGetColumnPrivileges
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1056; JDBC:JAVADOC:1057;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getColumnPrivileges() method on that object. It
   * should return a ResultSet object. Validate the column names and column
   * ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetColumnPrivileges() throws Exception {
		super.testGetColumnPrivileges();
  }

  /*
   * @testName: testGetTablePrivileges
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1058; JDBC:JAVADOC:1059;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getTablePrivileges() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetTablePrivileges() throws Exception {
		super.testGetTablePrivileges();
  }

  /*
   * @testName: testGetBestRowIdentifier1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetBestRowIdentifier1() throws Exception {
		super.testGetBestRowIdentifier1();
  }

  /*
   * @testName: testGetBestRowIdentifier2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetBestRowIdentifier2() throws Exception {
		super.testGetBestRowIdentifier2();
  }

  /*
   * @testName: testGetBestRowIdentifier3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetBestRowIdentifier3() throws Exception {
		super.testGetBestRowIdentifier3();
  }

  /*
   * @testName: testGetBestRowIdentifier4
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetBestRowIdentifier4() throws Exception {
		super.testGetBestRowIdentifier4();
  }

  /*
   * @testName: testGetBestRowIdentifier5
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetBestRowIdentifier5() throws Exception {
		super.testGetBestRowIdentifier5();
  }

  /*
   * @testName: testGetBestRowIdentifier6
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetBestRowIdentifier6() throws Exception {
		super.testGetBestRowIdentifier6();
  }

  /*
   * @testName: testGetBestRowIdentifier7
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1060; JDBC:JAVADOC:1061;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getBestRowIdentifier() method on that object. It
   * should return a ResultSet object. Validate the column names and column
   * ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetBestRowIdentifier7() throws Exception {
		super.testGetBestRowIdentifier7();
  }

  /*
   * @testName: testGetVersionColumns
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1062; JDBC:JAVADOC:1063;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getVersionColumns() method on that object. It should
   * return a ResultSet object.Compare the column names Validate the column
   * names and column ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetVersionColumns() throws Exception {
		super.testGetVersionColumns();
  }

  /*
   * @testName: testGetPrimaryKeys
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1064; JDBC:JAVADOC:1065;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getPrimaryKeys() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetPrimaryKeys() throws Exception {
		super.testGetPrimaryKeys();
  }

  /*
   * @testName: testGetImportedKeys
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1066; JDBC:JAVADOC:1067;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getImportedKeys() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetImportedKeys() throws Exception {
		super.testGetImportedKeys();
  }

  /*
   * @testName: testGetExportedKeys
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1068; JDBC:JAVADOC:1069;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getExportedKeys() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetExportedKeys() throws Exception {
		super.testGetExportedKeys();
  }

  /*
   * @testName: testGetCrossReference
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1070; JDBC:JAVADOC:1071;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getCrossReference() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetCrossReference() throws Exception {
		super.testGetCrossReference();
  }

  /*
   * @testName: testGetIndexInfo1
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetIndexInfo1() throws Exception {
		super.testGetIndexInfo1();
  }

  /*
   * @testName: testGetIndexInfo2
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetIndexInfo2() throws Exception {
		super.testGetIndexInfo2();
  }

  /*
   * @testName: testGetIndexInfo3
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetIndexInfo3() throws Exception {
		super.testGetIndexInfo3();
  }

  /*
   * @testName: testGetIndexInfo4
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object.
   *
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetIndexInfo4() throws Exception {
		super.testGetIndexInfo4();
  }

  /*
   * @testName: testGetIndexInfo5
   * 
   * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:1074; JDBC:JAVADOC:1075;
   * JavaEE:SPEC:193;
   *
   * @test_Strategy: Get a DatabaseMetadata object from the connection to the
   * database and call the getIndexInfo() method on that object. It should
   * return a ResultSet object. Validate the column names and column ordering.
   */
	@Test
	@TargetVehicle("jsp")
  public void testGetIndexInfo5() throws Exception {
		super.testGetIndexInfo5();
  }

}
