/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.javamail.ee.internetMimeMultipart;

import java.io.IOException;
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

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;


@Tag("tck-appclient")

public class internetMimeMultipartEJB_Test extends internetMimeMultipart_Test {
	
	  @TargetsContainer("tck-appclient")
	  @OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentEjb(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "internetMimeMultipart_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.javamail.ee");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClass(com.sun.ts.tests.common.base.EETest.class);
		ejbClient.addClass(com.sun.ts.tests.common.base.ServiceEETest.class);
		ejbClient.addClasses(internetMimeMultipartEJB_Test.class, internetMimeMultipart_Test.class);

		URL resURL = internetMimeMultipartEJB_Test.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");
		
		resURL = internetMimeMultipartEJB_Test.class
				.getResource("/com/sun/ts/tests/javamail/ee/internetMimeMultipart/internetMimeMultipart_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}


		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "internetMimeMultipart_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.javamail.ee");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClass(com.sun.ts.tests.common.base.EETest.class);
		ejb.addClass(com.sun.ts.tests.common.base.ServiceEETest.class);
		ejb.addClasses(internetMimeMultipartEJB_Test.class, internetMimeMultipart_Test.class);

		
		resURL = internetMimeMultipartEJB_Test.class.getResource(
				"/com/sun/ts/tests/javamail/ee/internetMimeMultipart/internetMimeMultipart_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = internetMimeMultipartEJB_Test.class.getResource("/com/sun/ts/tests/javamail/ee/internetMimeMultipart/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "internetMimeMultipart_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};


  /*
   * @testName: test1
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getMessageContent test
   */
  // derived from javamail suite getMessageContent_Test
	@Test
	@TargetVehicle("ejb")
  public void test1() throws Exception {
	  super.test1();
  }// end of test1

  /*
   * @testName: propertiesTest
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getMessageContent test
   */
  // derived from javamail suite propertiesTest
	@Test
	@TargetVehicle("ejb")
  public void propertiesTest() throws Exception {
		super.propertiesTest();
  }

  /*
   * @testName: mimeMultipartTest
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getMessageContent test
   */
  // derived from javamail suite MimeMultipart test
	@Test
	@TargetVehicle("ejb")
  public void mimeMultipartTest() throws Exception {
		super.mimeMultipartTest();
  }

}
