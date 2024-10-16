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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@Tag("tck-javatest")

public class internetMimeMultipartServlet_Test extends internetMimeMultipart_Test {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentServlet(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "internetMimeMultipart_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(internetMimeMultipartServlet_Test.class, internetMimeMultipart_Test.class);
		
		
		// The servlet descriptor
        URL jspUrl = internetMimeMultipartServlet_Test.class.getResource("servlet_vehicle_web.xml");
        if(jspUrl != null) {
        	archive.addAsWebInfResource(jspUrl, "web.xml");
        }
        // The sun servlet descriptor
        URL sunJSPUrl = internetMimeMultipartServlet_Test.class.getResource("internetMimeMultipart_servlet_vehicle_web.war.sun-web.xml");
        if(sunJSPUrl != null) {
        	archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
        }
        // Call the archive processor
        archiveProcessor.processWebArchive(archive, internetMimeMultipartServlet_Test.class, sunJSPUrl);

		return archive;
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
	@TargetVehicle("servlet")
  public void test1() throws Exception {
	  super.test1();
  }// end of test1

  /*
   * @testName: properties_Test
   * 
   * @assertion_ids: JavaEE:SPEC:235;
   * 
   * @test_Strategy: getMessageContent test
   */
  // derived from javamail suite properties_Test
	@Test
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
  public void mimeMultipartTest() throws Exception {
		super.mimeMultipartTest();
  }

}
