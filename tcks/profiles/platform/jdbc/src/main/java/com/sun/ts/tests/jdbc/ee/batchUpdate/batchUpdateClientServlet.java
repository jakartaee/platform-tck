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
 * The batchUpdateClient class tests methods of Statement, PreparedStatement and
 * CallableStatement interfaces using Sun's J2EE Reference Implementation.
 * 
 */

@Tag("tck-javatest")
@Tag("web")

public class batchUpdateClientServlet extends batchUpdateClient implements Serializable {
	private static final String testName = "jdbc.ee.batchUpdate";

	@TargetsContainer("tck-javatest")
	@OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentservlet(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "batchUpdate_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(batchUpdateClient.class, ServiceEETest.class, EETest.class);

		// The servlet descriptor
		URL servletUrl = batchUpdateClientServlet.class.getResource("servlet_vehicle_web.xml");
		if (servletUrl != null) {
			archive.addAsWebInfResource(servletUrl, "web.xml");
		}
		// The sun servlet descriptor
		URL sunServletUrl = batchUpdateClientServlet.class
				.getResource("batchUpdate_servlet_vehicle_web.war.sun-web.xml");
		if (sunServletUrl != null) {
			archive.addAsWebInfResource(sunServletUrl, "sun-web.xml");
		}
		// Call the archive processor
		archiveProcessor.processWebArchive(archive, batchUpdateClientServlet.class, sunServletUrl);

		return archive;
	};

	/* Run test in standalone mode */
	public static void main(String[] args) {
		batchUpdateClientServlet theTests = new batchUpdateClientServlet();
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
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * with 3 SQL statements and call the executeBatch() method and it should return
	 * array of Integer values of length 3
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testAddBatch01() throws Exception {
		super.testAddBatch01();
	}

	/*
	 * @testName: testAddBatch02
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:183; JDBC:JAVADOC:184;
	 * JDBC:JAVADOC:187; JDBC:JAVADOC:188; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a Statement object and call the addBatch() method with 3
	 * SQL statements and call the executeBatch() method and it should return an
	 * array of Integer of length 3.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testAddBatch02() throws Exception {
		super.testAddBatch02();
	}

	/*
	 * @testName: testAddBatch03
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:700; JDBC:JAVADOC:701;
	 * JDBC:JAVADOC:187; JDBC:JAVADOC:188; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a CallableStatement object and call the addBatch() method
	 * with 3 SQL statements and call the executeBatch() method and it should return
	 * an array of Integer of length 3.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testAddBatch03() throws Exception {
		super.testAddBatch03();
	}

	/*
	 * @testName: testClearBatch01
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * and call the clearBatch() method and then call executeBatch() to check the
	 * call of clearBatch()method The executeBatch() method should return a zero
	 * value.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testClearBatch01() throws Exception {
		super.testClearBatch01();
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
	@TargetVehicle("servlet")
	public void testClearBatch02() throws Exception {
		super.testClearBatch02();
	}

	/*
	 * @testName: testClearBatch03
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:185; JDBC:JAVADOC:186;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a CallableStatement object and call the addBatch() method
	 * and call the clearBatch() method and then call executeBatch() to check the
	 * call of clearBatch()method. The executeBatch() method should return a zero
	 * value.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testClearBatch03() throws Exception {
		super.testClearBatch03();
	}

	/*
	 * @testName: testExecuteBatch01
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * with a 3 valid SQL statements and call the executeBatch() method It should
	 * return an array of Integer values of length 3.
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testExecuteBatch01() throws Exception {
		super.testExecuteBatch01();
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
	@TargetVehicle("servlet")
	public void testExecuteBatch02() throws Exception {
		super.testExecuteBatch02();
	}

	/*
	 * @testName: testExecuteBatch03
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * and call the executeBatch() method with a select Statement It should throw
	 * BatchUpdateException
	 *
	 */
	@Test
	@TargetVehicle("servlet")
	public void testExecuteBatch03() throws Exception {
		super.testExecuteBatch03();
	}

	/*
	 * @testName: testExecuteBatch04
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a Statement object and call the addBatch() method with 3
	 * valid SQL statements and call the executeBatch() method It should return an
	 * array of Integer values of length 3
	 */
	@Test
	@TargetVehicle("servlet")
	public void testExecuteBatch04() throws Exception {
		super.testExecuteBatch04();
	}

	/*
	 * @testName: testExecuteBatch05
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:183; JDBC:JAVADOC:184;
	 * 
	 * @test_Strategy: Get a Statement object and call the executeBatch() method
	 * without adding statements into a batch. It should return an array of Integer
	 * value of zero length
	 */
	@Test
	@TargetVehicle("servlet")
	public void testExecuteBatch05() throws Exception {
		super.testExecuteBatch05();
	}

	/*
	 * @testName: testExecuteBatch06
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:183; JDBC:JAVADOC:184; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a Statement object and call the addBatch() method and
	 * call the executeBatch() method with a violation in SQL constraints.It should
	 * throw an BatchUpdateException
	 */
	@Test
	@TargetVehicle("servlet")
	public void testExecuteBatch06() throws Exception {
		super.testExecuteBatch06();
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
	@TargetVehicle("servlet")
	public void testExecuteBatch07() throws Exception {
		super.testExecuteBatch07();
	}

	/*
	 * @testName: testExecuteBatch08
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a CallableStatement object and call the addBatch() method
	 * with 3 valid SQL statements and call the executeBatch() method It should
	 * return an array of Integer Values of length 3.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testExecuteBatch08() throws Exception {
		super.testExecuteBatch08();
	}

	/*
	 * @testName: testExecuteBatch09
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * 
	 * @test_Strategy: Get a CallableStatement object and call the executeBatch()
	 * method without adding the statements into Batch. It should return an array of
	 * Integer Value of zero length
	 */
	@Test
	@TargetVehicle("servlet")
	public void testExecuteBatch09() throws Exception {
		super.testExecuteBatch09();
	}

	/*
	 * @testName: testExecuteBatch12
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a CallableStatement object with different SQL statements
	 * in the stored Procedure and call the addBatch() method with 3 statements and
	 * call the executeBatch() method It should return an array of Integer Values of
	 * length 3.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testExecuteBatch12() throws Exception {
		super.testExecuteBatch12();
	}

	/*
	 * @testName: testContinueBatch01
	 * 
	 * @assertion_ids: JavaEE:SPEC:190; JDBC:JAVADOC:187; JDBC:JAVADOC:188;
	 * JDBC:JAVADOC:700; JDBC:JAVADOC:701; JDBC:SPEC:23;
	 * 
	 * @test_Strategy: Get a PreparedStatement object and call the addBatch() method
	 * with 3 SQL statements.Among these 3 SQL statements first is valid,second is
	 * invalid and third is again valid. Then call the executeBatch() method and it
	 * should return array of Integer values of length 3, if it supports continued
	 * updates. Then check whether the third command in the batch after the invalid
	 * command executed properly.
	 */
	@Test
	@TargetVehicle("servlet")
	public void testContinueBatch01() throws Exception {
		super.testContinueBatch01();
	}
}
