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
 * @(#)dbMetaClient2.java	1.26 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.dbMeta.dbMeta2;

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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.base.ServiceEETest;import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

// Merant DataSource class
//import com.merant.sequelink.jdbcx.datasource.*;

/**
 * The dbMetaClient2 class tests methods of DatabaseMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")
public class dbMetaClient2JSP extends dbMetaClient2 implements Serializable {
	private static final String testName = "jdbc.ee.dbMeta.dbMeta2";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "dbMeta2_jsp_vehicle_web.war");
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

		archive.addClasses(dbMetaClient2.class, ServiceEETest.class, EETest.class);

		// The jsp descriptor
		URL jspUrl = dbMetaClient2JSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = dbMetaClient2JSP.class.getResource("dbMeta2_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, dbMetaClient2JSP.class, sunJSPUrl);

		archive.addAsWebInfResource(dbMetaClient2JSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		dbMetaClient2JSP theTests = new dbMetaClient2JSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testStoresMixedCaseIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:846; JDBC:JAVADOC:847;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesMixedCaseIdentifiers() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testStoresMixedCaseIdentifiers() throws Exception {
		super.testStoresMixedCaseIdentifiers();
	}

	/*
	 * @testName: testSupportsMixedCaseQuotedIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:848; JDBC:JAVADOC:849;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsMixedCaseQuotedIdentifiers() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsMixedCaseQuotedIdentifiers() throws Exception {
		super.testStoresMixedCaseIdentifiers();
	}

	/*
	 * @testName: testStoresUpperCaseQuotedIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:850; JDBC:JAVADOC:851;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesUpperCaseQuotedIdentifiers() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testStoresUpperCaseQuotedIdentifiers() throws Exception {
		super.testStoresUpperCaseQuotedIdentifiers();
	}

	/*
	 * @testName: testStoresLowerCaseQuotedIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:852; JDBC:JAVADOC:853;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesLowerCaseQuotedIdentifiers() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testStoresLowerCaseQuotedIdentifiers() throws Exception {
		super.testStoresLowerCaseQuotedIdentifiers();
	}

	/*
	 * @testName: testStoresMixedCaseQuotedIdentifiers
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:854; JDBC:JAVADOC:855;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the storesMixedCaseQuotedIdentifiers() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testStoresMixedCaseQuotedIdentifiers() throws Exception {
		super.testStoresMixedCaseQuotedIdentifiers();
	}

	/*
	 * @testName: testGetIdentifierQuoteString
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:856; JDBC:JAVADOC:857;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getIdentifierQuoteString() method It should return a
	 * String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetIdentifierQuoteString() throws Exception {
		super.testGetIdentifierQuoteString();
	}

	/*
	 * @testName: testGetSQLKeywords
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:858; JDBC:JAVADOC:859;
	 * JavaEE:SPEC:193;
	 * 
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getSQLKeywords() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetSQLKeywords() throws Exception {
		super.testGetSQLKeywords();
	}

	/*
	 * @testName: testGetNumericFunctions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:860; JDBC:JAVADOC:861;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getNumericFunctions() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetNumericFunctions() throws Exception {
		super.testGetNumericFunctions();
	}

	/*
	 * @testName: testGetStringFunctions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:862; JDBC:JAVADOC:863;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getStringFunctions() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetStringFunctions() throws Exception {
		super.testGetStringFunctions();
	}

	/*
	 * @testName: testGetSystemFunctions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:864; JDBC:JAVADOC:865;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getSystemFunctions() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetSystemFunctions() throws Exception {
		super.testGetSystemFunctions();
	}

	/*
	 * @testName: testGetTimeDateFunctions
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:866; JDBC:JAVADOC:867;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getTimeDateFunctions() method It should return a String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetTimeDateFunctions() throws Exception {
		super.testGetTimeDateFunctions();
	}

	/*
	 * @testName: testGetSearchStringEscape
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:868; JDBC:JAVADOC:869;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getSearchStringEscape() method It should return a
	 * String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetSearchStringEscape() throws Exception {
		super.testGetSearchStringEscape();
	}

	/*
	 * @testName: testGetExtraNameCharacters
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:870; JDBC:JAVADOC:871;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the getExtraNameCharacters() method It should return a
	 * String
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetExtraNameCharacters() throws Exception {
		super.testGetExtraNameCharacters();
	}

	/*
	 * @testName: testSupportsAlterTableWithAddColumn
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:872; JDBC:JAVADOC:873;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsAlterTableWithAddColumn() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsAlterTableWithAddColumn() throws Exception {
		super.testSupportsAlterTableWithAddColumn();
	}

	/*
	 * @testName: testSupportsAlterTableWithDropColumn
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:874; JDBC:JAVADOC:875;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsAlterTableWithDropColumn() method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsAlterTableWithDropColumn() throws Exception {
		super.testSupportsAlterTableWithDropColumn();
	}

	/*
	 * @testName: testSupportsColumnAliasing
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:876; JDBC:JAVADOC:877;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsColumnAliasing() method It should return a true
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsColumnAliasing() throws Exception {
		super.testSupportsColumnAliasing();
	}

	/*
	 * @testName: testNullPlusNonNullIsNull
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:878; JDBC:JAVADOC:879;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the nullPlusNonNullIsNull() method It should return a
	 * boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testNullPlusNonNullIsNull() throws Exception {
		super.testNullPlusNonNullIsNull();
	}

	/*
	 * @testName: testSupportsConvert
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:880; JDBC:JAVADOC:881;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert() method It should return a boolean
	 * value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsConvert() throws Exception {
		super.testSupportsConvert();
	}

	/*
	 * @testName: testSupportsConvert01
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(ARRAY, VARCHAR) method It should return
	 * a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsConvert01() throws Exception {
		super.testSupportsConvert01();
	}

	/*
	 * @testName: testSupportsConvert02
	 * 
	 * @assertion_ids: JDBC:SPEC:8; JDBC:JAVADOC:882; JDBC:JAVADOC:883;
	 * JavaEE:SPEC:193;
	 *
	 * @test_Strategy: Get the DataBaseMetaData object from the Connection to the
	 * DataBase and call the supportsConvert(BIGINT, VARCHAR) method It should
	 * return a boolean value
	 *
	 */
	@Test
	@TargetVehicle("jsp")
	public void testSupportsConvert02() throws Exception {
		super.testSupportsConvert02();
	}

}
