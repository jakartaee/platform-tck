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
 * @(#)mimemessage_Test.java	1.17 03/05/16
 */
package com.sun.ts.tests.javamail.ee.mimemessage;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import jakarta.mail.Address;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class mimemessageAppClient_Test extends mimemessage_Test implements Serializable {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("appclient")
	@Deployment(name = "appclient", order = 2)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "mimemessage_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(true, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(mimemessageAppClient_Test.class, mimemessage_Test.class);
	  // The appclient-client descriptor
     URL appClientUrl = mimemessageAppClient_Test.class.getResource("appclient_vehicle_client.xml");
     if(appClientUrl != null) {
     	archive.addAsManifestResource(appClientUrl, "application-client.xml");
     }
     // The sun appclient-client descriptor
     URL sunAppClientUrl = mimemessageAppClient_Test.class.getResource("mimemessage_appclient_vehicle_client.jar.sun-application-client.xml");
     if(sunAppClientUrl != null) {
     	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
     }
     // Call the archive processor
     archiveProcessor.processClientArchive(archive, mimemessageAppClient_Test.class, sunAppClientUrl);
	System.out.println(archive.toString(true));
		return archive;
	};


  // Harness requirements

  /* Run test in standalone mode */
  public static void main(String[] args) {
    mimemessageAppClient_Test theTests = new mimemessageAppClient_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @testName: testSetContent1
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: Call setContent with required arguments for multipart then
   * call getContent() to verify.
   */
  // derived from javamail suite setContent_Test class
	@Test
	@TargetVehicle("appclient")
  public void testSetContent1() throws Exception {
	  super.testSetContent1();
  } // end of testSetContent1()

  /*
   * @testName: testSetContent2
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: Call setContent with required arguments for text/plain then
   * call getContentType() to verify.
   */
  // derived from javamail suite sendMessage_Test class
	@Test
	@TargetVehicle("appclient")
  public void testSetContent2() throws Exception {
	  super.testSetContent2();
   } // end of testSetContent2()

  /*
   * @testName: getSession_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy:
   * 
   * This class tests the <strong>getSession()</strong> API. It does this by
   * invoking the test api and then checking that the returned object is the
   * same object used to create the message.<p>
   * 
   * Get the session of this message. <p> api2test: public String getSession()
   * <p>
   * 
   * how2test: Call this API on given message object, verify that it returns the
   * Session object used to create this message. If this operation is
   * successfull then this testcase passes, otherwise it fails. <p>
   * 
   * Returns the Session object used when the message was created. Returns null
   * if no Session is available. <p>
   */
  // derived from javamail suite getSession_Test class
	@Test
	@TargetVehicle("appclient")
  public void getSession() throws Exception {
	  super.getSession();
  }

  /**
  
   */
  /*
   * @testName: createMimeMessage_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235; JavaEE:SPEC:238;
   * 
   * @test_Strategy: This test tests the <strong>createMessage()</strong> API.
   * It does by passing various valid input values and then checking the type of
   * the returned object. <p>
   * 
   * Create a subclassed MimeMessage object and in reply(boolean) return
   * instance of this new subclassed object. <p> api2test: protected void
   * createMimeMessage() <p>
   * 
   * how2test: Call API with various arguments, then call getRecipients() api,
   * verify that user specified recipient address types have been added. If so
   * then this testcase passes, otherwise it fails. <p>
   */
  // derived from javamail suite createMimeMessage_Test
	@Test
	@TargetVehicle("appclient")
  public void createMimeMessage() throws Exception {
	  super.createMimeMessage();
  }

  /*
   * @testName: reply_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy:
   * 
   * This test tests the <strong>reply()</strong> APIs. <p>
   * 
   * Create a reply MimeMessage object and check that it has the appropriate
   * headers. <p> api2test: public Message reply() <p>
   * 
   * how2test: Call API with various arguments, then verify that the reply
   * message has the required recipients and subject. If so then this testcase
   * passes, otherwise it fails. <p>
   * 
   * derived from javamail suite reply_Test
   */
	@Test
	@TargetVehicle("appclient")
  public void reply() throws Exception {
	  super.reply();
  }

  /*
   * @testName: setFrom_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy:
   * 
   * This test tests the <strong>setFrom(String)</strong> API. It does this by
   * passing various valid input values and then checking the type of the
   * returned object. <p>
   * 
   * Set the "From" attribute in this Message. <p> api2test: public void
   * setFrom(String) <p>
   * 
   * how2test: Call this API with various addresses, then call call getFrom()
   * api, if the setFrom values and getFrom values are the same, then this
   * testcase passes, otherwise it fails. <p>
   * 
   * derived from javamail suite setContent_Test
   */
	@Test
	@TargetVehicle("appclient")
  public void setFrom() throws Exception {
	  super.setFrom();
  }

}
