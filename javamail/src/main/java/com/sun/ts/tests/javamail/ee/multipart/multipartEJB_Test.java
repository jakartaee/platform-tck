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
 * @(#)multipart_Test.java	1.16 03/05/16
 */
package com.sun.ts.tests.javamail.ee.multipart;

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

public class multipartEJB_Test extends multipart_Test implements Serializable {
	
	  @TargetsContainer("tck-appclient")
	  @OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentEJB(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "multipart_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClasses(multipartEJB_Test.class, multipart_Test.class);

		URL resURL = multipartEJB_Test.class.getResource("com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");
		
		resURL = multipartEJB_Test.class
				.getResource("/com/sun/ts/tests/javamail/ee/multipart/multipart_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}


		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "multipart_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(multipartEJB_Test.class, multipart_Test.class);

		resURL = multipartEJB_Test.class.getResource(
				"/com/sun/ts/tests/javamail/ee/multipart/multipart_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = multipartEJB_Test.class.getResource("/com/sun/ts/tests/javamail/ee/multipart/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "multipart_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};

  
  // Harness requirements

  /* Run test in standalone mode */
  public static void main(String[] args) {
    multipartEJB_Test theTests = new multipartEJB_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testAddBodyPart1
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: Call api with part argument, then verify by calling
   * getCount.
   */
  // derived from javamail suite multipart_Test class
	@Test
	@TargetVehicle("ejb")
  public void testAddBodyPart1() throws Exception {
	  super.testAddBodyPart1();
  }

  /*
   * @testName: testAddBodyPart2
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: Call api with part argument and position, then verify by
   * calling getCount.
   */
	@Test
	@TargetVehicle("ejb")
  public void testAddBodyPart2() throws Exception {
	  super.testAddBodyPart2();
  }// end of testAddBodyPart2
   //

}
