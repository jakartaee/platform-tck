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
 * @(#)send_Test.java	1.17 03/05/16
 */
package com.sun.ts.tests.javamail.ee.transport;

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

public class sendEJB_Test extends send_Test implements Serializable {
	
	  @TargetsContainer("tck-appclient")
	  @OverProtocol("appclient")
	@Deployment(name = "ejb", testable = true)
	public static EnterpriseArchive createDeploymentEJB(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "send_ejb_vehicle_client.jar");
		ejbClient.addPackages(true, "com.sun.ts.tests.javamail.ee");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient.addClass(com.sun.ts.tests.common.base.EETest.class);
		ejbClient.addClass(com.sun.ts.tests.common.base.ServiceEETest.class);
		ejbClient.addClasses(sendEJB_Test.class, send_Test.class);

		URL resURL = sendEJB_Test.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		
		resURL = sendEJB_Test.class
				.getResource("/com/sun/ts/tests/javamail/ee/transport/transport_ejb_vehicle_client.jar.sun-application-client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
		}

		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "send_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.javamail.ee");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClass(com.sun.ts.tests.common.base.EETest.class);
		ejb.addClass(com.sun.ts.tests.common.base.ServiceEETest.class);
		ejb.addClasses(sendEJB_Test.class, send_Test.class);

		resURL = sendEJB_Test.class.getResource(
				"/com/sun/ts/tests/javamail/ee/transport/transport_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}

		resURL = sendEJB_Test.class.getResource("/com/sun/ts/tests/javamail/ee/transport/ejb_vehicle_ejb.xml");

		if (resURL != null) {
			ejb.addAsManifestResource(resURL, "ejb-jar.xml");
		}
		
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "send_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;
	};



  /* Run test in standalone mode */
  public static void main(String[] args) {
    sendEJB_Test theTests = new sendEJB_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }


  /*
   * @testName: testSend
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: Send this message. If success return void, else throw
   * error.
   */
  // derived from javamail suite send_Test class
	@Test
	@TargetVehicle("ejb")
  public void testSend() throws Exception {
	  super.testSend();
  }

  //
  /*
   * @testName: testSend2
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: Send this message. If success return void, else throw
   * error.
   */
  // derived from javamail suite send_Test class
	@Test
	@TargetVehicle("ejb")
  public void testSend2() throws Exception {
	  super.testSend2();
  }

  /*
   * @testName: testSend3
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: construct the message - check the type verify that addtype
   * returns rfc822
   */
  // derived from javamail suite getType class
	@Test
	@TargetVehicle("ejb")
  public void testSend3() throws Exception {
	  super.testSend3();
   }
  
  /*
   * @testName: testconnect1
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: Send this message. If success return void, else throw
   * error.
   */
  // derived from javamail suite connect_Test class
	@Test
	@TargetVehicle("ejb")
  public void testconnect1() throws Exception {
	  super.testconnect1();
  }// end of testconnect1()

  /*
   * @testName: testSendMessage
   * 
   * @assertion_ids: JavaEE:SPEC:238; JavaEE:SPEC:235;
   * 
   * @test_Strategy: Call this API for given message objects with address list.
   * If this invocation is successful then this testcase passes.
   */
  // derived from javamail suite sendMessage_Test class
	@Test
	@TargetVehicle("ejb")
  public void testSendMessage() throws Exception {
	  super.testSendMessage();
  } // end of testSendMessage()

  /*
   * @testName: test4
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy:
   * 
   * This test tests the <strong>send()</strong> API. It does this by passing
   * various valid input values and then checking the type of the returned
   * object. <p>
   * 
   * Send this message. <p> api2test: public void send(msg) <p> Send this
   * message to the specified addresses. <p> api2test: public void send(Message,
   * Address[]) <p> Send this message using the specified username and password.
   * <p> api2test: public void send(Message, String, String) <p>
   * 
   * how2test: Call these APIs for given message objects with or without address
   * list. If this invocation is successfull then this testcase passes otherwise
   * it fails. <p>
   * 
   * derived from javamail suite send_Test
   */
	@Test
	@TargetVehicle("ejb")
  public void test4() throws Exception {
	  super.test4();
  }

}
