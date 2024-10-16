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
 * @(#)internetaddress_Test.java	1.16 03/05/16
 */
package com.sun.ts.tests.javamail.ee.internetaddress;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@Tag("tck-appclient")

public class internetaddressAppClient_Test extends internetaddress_Test
    implements Serializable {
  
	  @TargetsContainer("tck-appclient")
	  @OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "internetaddress_appclient_vehicle_client.jar");
		archive.addPackages(false, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(internetaddressAppClient_Test.class, internetaddress_Test.class);
		
		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");
		
        // The appclient-client descriptor
        URL appClientUrl = internetaddressAppClient_Test.class.getResource("/com/sun/ts/tests/javamail/ee/internetaddress/appclient_vehicle_client.xml");
        if(appClientUrl != null) {
        	archive.addAsManifestResource(appClientUrl, "application-client.xml");
        }
        // The sun appclient-client descriptor
        URL sunAppClientUrl = internetaddressAppClient_Test.class.getResource("/com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
        if(sunAppClientUrl != null) {
        	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
        }
        // Call the archive processor
        archiveProcessor.processClientArchive(archive, internetaddressAppClient_Test.class, sunAppClientUrl);

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "internetaddress_appclient_vehicle.ear");
		ear.addAsModule(archive);

		return ear;
	};
	
  // Harness requirements

  /* Run test in standalone mode */
  public static void main(String[] args) {
    internetaddressAppClient_Test theTests = new internetaddressAppClient_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testUtilMethods1
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:234; JavaEE:SPEC:233;
   * 
   * @test_Strategy: verify InternetAddress util method toString
   */
  // derived from javamail suite utilmethods_Test class
	@Test
	@TargetVehicle("appclient")
  public void testUtilMethods1() throws Exception {
		super.testUtilMethods1();
  } // end of testUtilMethods1()
    //

  /*
   * @testName: testUtilMethods2
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:234; JavaEE:SPEC:233;
   * 
   * @test_Strategy: verify InternetAddress util method equals
   */
  // derived from javamail suite utilmethods_Test class
	@Test
	@TargetVehicle("appclient")
  public void testUtilMethods2() throws Exception {
   super.testUtilMethods2();
  } // end of testUtilMethods2()
    //

  /*
   * @testName: testgetAddress
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: verify InternetAddress method getAddress
   */
  // derived from javamail suite getAddress_Test class
	@Test
	@TargetVehicle("appclient")
  public void testgetAddress() throws Exception {
		super.testgetAddress();
    } // end of testgetAddress()

  /*
   * @testName: testgetPersonal
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: verify InternetAddress method getPersonal
   */
  // derived from javamail suite getPersonal_Test.java class
	@Test
	@TargetVehicle("appclient")
  public void testgetPersonal() throws Exception {
		super.testgetPersonal();
  } // end of testgetPersonal()

  /*
   * @testName: contentType_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy:
   * 
   * derived from javamail suite setContent_Test
   */
	@Test
	@TargetVehicle("appclient")
  public void contentTypeTest() throws Exception {
		super.contentTypeTest();
  }

  /*
   * @testName: next_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: This tests tests the <strong>next()</strong> APIs. It does
   * by passing various valid input values and then checking the type of the
   * returned object. <p>
   * 
   * Return the next token from the parse stream.<p> api2test: public
   * HeaderTokenizer.Token next() <p>
   * 
   * 
   * derived from javamail suite next_Test
   */
	@Test
	@TargetVehicle("appclient")
  public void nextTest() throws Exception {
		super.nextTest();
  }

  /*
   * @testName: combineSegments_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: * This class tests the
   * <strong>ParameterList.combineSegments()strong> API. <p>
   * 
   * 
   * how2test: Create a ParameterList with a parameter split into several
   * segments and then call combineSegments() and verify that the parameter is
   * returned as a single correct value. If is so then this testcase passes,
   * otherwise it fails. <p>
   * 
   * derived from javamail suite combineSegments_Test
   */
	@Test
	@TargetVehicle("appclient")
  public void combineSegmentsTest() throws Exception {
		super.combineSegmentsTest();
  }
}
