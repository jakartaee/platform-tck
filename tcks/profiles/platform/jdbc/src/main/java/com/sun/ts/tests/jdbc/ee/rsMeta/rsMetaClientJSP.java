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
 * @(#)rsMetaClient.java	1.25 03/05/16
 */

package com.sun.ts.tests.jdbc.ee.rsMeta;

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
 * The rsMetaClient class tests methods of ResultSetMetaData interface using
 * Sun's J2EE Reference Implementation.
 * 
 * @author
 * @version 1.7, 06/16/99
 */

@Tag("tck-javatest")
@Tag("web")

public class rsMetaClientJSP extends rsMetaClient implements Serializable {
	private static final String testName = "jdbc.ee.rsMeta";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentjsp(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "rsMeta_jsp_vehicle_web.war");
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

		// The jsp descriptor
		URL jspUrl = rsMetaClientJSP.class.getResource("jsp_vehicle_web.xml");
		if (jspUrl != null) {
			archive.addAsWebInfResource(jspUrl, "web.xml");
		}
		// The sun jsp descriptor
		URL sunJSPUrl = rsMetaClientJSP.class.getResource("rsMeta_jsp_vehicle_web.war.sun-web.xml");
		if (sunJSPUrl != null) {
			archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, rsMetaClientJSP.class, sunJSPUrl);

		archive.addClasses(rsMetaClient.class, ServiceEETest.class, EETest.class);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		rsMetaClientJSP theTests = new rsMetaClientJSP();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: testGetColumnCount
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:314; JDBC:JAVADOC:315;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getColumnCount() method on the ResultSetMetaData object.It should return an
	 * integer value greater than or equal to zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetColumnCount() throws Exception {
		super.testGetColumnCount();
	}

	/*
	 * @testName: testIsAutoIncrement
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:316; JDBC:JAVADOC:317;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * isAutoIncrement(int column) method.It should return a boolean value.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsAutoIncrement() throws Exception {
		super.testIsAutoIncrement();
	}

	/*
	 * /* @testName: testIsCaseSensitive
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:318; JDBC:JAVADOC:319;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * isCaseSensitive(int column) method.It should return a boolean value.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsCaseSensitive() throws Exception {
		super.testIsCaseSensitive();
	}

	/*
	 * @testName: testIsSearchable
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:320; JDBC:JAVADOC:321;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * isSearchable(int column) method.It should return a boolean value.
	 */

	@Test
	@TargetVehicle("jsp")
	public void testIsSearchable() throws Exception {
		super.testIsSearchable();
	}

	/*
	 * @testName: testIsCurrency
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:322; JDBC:JAVADOC:323;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the isCurrency(int
	 * column) method.It should return a boolean value.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsCurrency() throws Exception {
		super.testIsCurrency();
	}

	/*
	 * @testName: testIsNullable
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:324; JDBC:JAVADOC:325;
	 * JDBC:JAVADOC:311; JDBC:JAVADOC:312; JDBC:JAVADOC:313;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the isNullable(int
	 * column) method.It should return an integer value which is one of the
	 * constants columnNoNulls(0),columnNullable(1) and columnNullableUnknown(2).
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsNullable() throws Exception {
		super.testIsNullable();
	}

	/*
	 * @testName: testIsSigned
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:326; JDBC:JAVADOC:327;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the isSigned(int
	 * column) method.It should return a boolean value.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsSigned() throws Exception {
		super.testIsSigned();
	}

	/*
	 * @testName: testGetColumnDisplaySize
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:328; JDBC:JAVADOC:329;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getColumnDisplaySize(int colindex) method.It should return an integer
	 * representing the normal maximum width in characters for column colindex.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetColumnDisplaySize() throws Exception {
		super.testGetColumnDisplaySize();
	}

	/*
	 * @testName: testGetColumnLabel
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:330; JDBC:JAVADOC:331;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getColumnLabel(int colindex) method.It should return a String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetColumnLabel() throws Exception {
		super.testGetColumnLabel();
	}

	/*
	 * @testName: testGetColumnName
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:332; JDBC:JAVADOC:333;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getColumnName(int colindex) method.It should return a String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetColumnName() throws Exception {
		super.testGetColumnName();
	}

	/*
	 * @testName: testGetSchemaName
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:334; JDBC:JAVADOC:335;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getSchemaName(int colindex) method.It should return a String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetSchemaName() throws Exception {
		super.testGetSchemaName();
	}

	/*
	 * @testName: testGetPrecision
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:336; JDBC:JAVADOC:337;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getPrecision(int colindex) method.It should return an integer greater than or
	 * equal to zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetPrecision() throws Exception {
		super.testGetPrecision();
	}

	/*
	 * @testName: testGetScale
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:338; JDBC:JAVADOC:339;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the getScale(int
	 * colindex) method.It should return an integer greater than or equal to zero.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetScale() throws Exception {
		super.testGetScale();
	}

	/*
	 * @testName: testGetTableName
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:340; JDBC:JAVADOC:341;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getTableName(int colindex) method.It should return a String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetTableName() throws Exception {
		super.testGetTableName();
	}

	/*
	 * @testName: testGetCatalogName
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:342; JDBC:JAVADOC:343;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getCatalogName(int colindex) method.It should return a String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetCatalogName() throws Exception {
		super.testGetCatalogName();
	}

	/*
	 * @testName: testGetColumnType
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:344; JDBC:JAVADOC:345;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getColumnType(int colindex) method.Check if an integer value is returned.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetColumnType() throws Exception {
		super.testGetColumnType();
	}

	/*
	 * @testName: testGetColumnTypeName
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:346; JDBC:JAVADOC:347;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getColumnTypeName(int colindex) method.It should return a String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetColumnTypeName() throws Exception {
		super.testGetColumnTypeName();
	}

	/*
	 * /*
	 * 
	 * @testName: testIsReadOnly
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:348; JDBC:JAVADOC:349;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the isReadOnly(int
	 * column) method.It should return a boolean value.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsReadOnly() throws Exception {
		super.testIsReadOnly();
	}

	/*
	 * @testName: testIsWritable
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:350; JDBC:JAVADOC:351;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the isWritable(int
	 * column) method.It should return a boolean value.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsWritable() throws Exception {
		super.testIsWritable();
	}

	/*
	 * /*
	 * 
	 * @testName: testIsDefinitelyWritable
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:352; JDBC:JAVADOC:353;
	 * 
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * isDefinitelyWritable(int column) method.It should return a boolean value.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testIsDefinitelyWritable() throws Exception {
		super.testIsDefinitelyWritable();
	}

	/*
	 * @testName: testGetColumnClassName
	 * 
	 * @assertion_ids: JavaEE:SPEC:194; JDBC:JAVADOC:354; JDBC:JAVADOC:355;
	 *
	 * @test_Strategy: Get the ResultSetMetaData object from the corresponding
	 * ResultSet by using the ResultSet's getMetaData method.Call the
	 * getColumnClassName(int colindex) method.It should return a String object.
	 */
	@Test
	@TargetVehicle("jsp")
	public void testGetColumnClassName() throws Exception {
		super.testGetColumnClassName();
	}
}
