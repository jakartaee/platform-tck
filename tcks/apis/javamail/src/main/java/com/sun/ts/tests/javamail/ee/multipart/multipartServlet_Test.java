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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.harness.Status;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

@Tag("tck-javatest")

public class multipartServlet_Test extends multipart_Test implements Serializable {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentServlet(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "multipart_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClass(com.sun.ts.tests.common.base.EETest.class);
		archive.addClass(com.sun.ts.tests.common.base.ServiceEETest.class);
		archive.addClasses(multipartServlet_Test.class, multipart_Test.class);
		
		// The servlet descriptor
        URL jspUrl = multipartServlet_Test.class.getResource("servlet_vehicle_web.xml");
        if(jspUrl != null) {
        	archive.addAsWebInfResource(jspUrl, "web.xml");
        }
        // The sun servlet descriptor
        URL sunJSPUrl = multipartServlet_Test.class.getResource("multipart_servlet_vehicle_web.war.sun-web.xml");
        if(sunJSPUrl != null) {
        	archive.addAsWebInfResource(sunJSPUrl, "sun-web.xml");
        }
        // Call the archive processor
        archiveProcessor.processWebArchive(archive, multipartServlet_Test.class, sunJSPUrl);

		return archive;
	};

  
  // Harness requirements

  /* Run test in standalone mode */
  public static void main(String[] args) {
    multipartServlet_Test theTests = new multipartServlet_Test();
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
  public void testAddBodyPart2() throws Exception {
	  super.testAddBodyPart2();
  }// end of testAddBodyPart2
   //

}
