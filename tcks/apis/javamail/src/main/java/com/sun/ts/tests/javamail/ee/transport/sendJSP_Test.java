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

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@Tag("tck-javatest")

public class sendJSP_Test extends send_Test implements Serializable {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("javatest")
	@Deployment(name = "jsp", testable = true)
	public static WebArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "send_jsp_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClass(com.sun.ts.tests.common.base.EETest.class);
		archive.addClass(com.sun.ts.tests.common.base.ServiceEETest.class);
		archive.addClasses(sendJSP_Test.class, send_Test.class);
    
		URL jspVehicle = sendJSP_Test.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
		if(jspVehicle != null) {
			archive.addAsWebResource(jspVehicle, "/jsp_vehicle.jsp");
		}
		URL clientHtml = sendJSP_Test.class.getResource("/com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
		if(clientHtml != null) {
			archive.addAsWebResource(clientHtml, "/client.html");
		}
        
		// The jsp descriptor
        URL jspUrl = sendJSP_Test.class.getResource("jsp_vehicle_web.xml");
        if(jspUrl != null) {
        	archive.addAsWebInfResource(jspUrl, "web.xml");
        }
        // The sun jsp descriptor
        URL sunJSPUrl = sendJSP_Test.class.getResource("transport_jsp_vehicle_web.war.sun-web.xml");
        if(sunJSPUrl != null) {
        	archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
        }
        // Call the archive processor
        archiveProcessor.processWebArchive(archive, sendJSP_Test.class, sunJSPUrl);

		return archive;
	};



  /* Run test in standalone mode */
  public static void main(String[] args) {
    sendJSP_Test theTests = new sendJSP_Test();
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
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
	@TargetVehicle("jsp")
  public void test4() throws Exception {
	  super.test4();
  }

}
