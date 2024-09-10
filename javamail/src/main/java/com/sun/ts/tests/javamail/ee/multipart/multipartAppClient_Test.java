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

import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class multipartAppClient_Test extends multipart_Test implements Serializable {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("appclient")
	@Deployment(name = "appclient", order = 2)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "multipart_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(true, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(multipartAppClient_Test.class, multipart_Test.class);		
		  // The appclient-client descriptor
	     URL appClientUrl = multipartAppClient_Test.class.getResource("appclient_vehicle_client.xml");
	     if(appClientUrl != null) {
	     	archive.addAsManifestResource(appClientUrl, "application-client.xml");
	     }
	     // The sun appclient-client descriptor
	     URL sunAppClientUrl = multipartAppClient_Test.class.getResource("multipart_appclient_vehicle_client.jar.sun-application-client.xml");
	     if(sunAppClientUrl != null) {
	     	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
	     }
	     // Call the archive processor
	     archiveProcessor.processClientArchive(archive, multipartAppClient_Test.class, sunAppClientUrl);

		System.out.println(archive.toString(true));
		return archive;
	};

  
  // Harness requirements

  /* Run test in standalone mode */
  public static void main(String[] args) {
    multipartAppClient_Test theTests = new multipartAppClient_Test();
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
  public void testAddBodyPart2() throws Exception {
	  super.testAddBodyPart2();
  }// end of testAddBodyPart2
   //

}
