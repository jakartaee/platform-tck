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

public class internetMimeMultipartAppClient_Test extends internetMimeMultipart_Test {
	
	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class,
				"internetMimeMultipart_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClass(com.sun.ts.tests.common.base.EETest.class);
		archive.addClass(com.sun.ts.tests.common.base.ServiceEETest.class);
		archive.addClasses(internetMimeMultipartAppClient_Test.class, internetMimeMultipart_Test.class);
		archive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// The appclient-client descriptor
		URL appClientUrl = internetMimeMultipartAppClient_Test.class.getResource("/com/sun/ts/tests/javamail/ee/internetMimeMultipart/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			archive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = internetMimeMultipartAppClient_Test.class
				.getResource("/com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}
		// Call the archive processor
		archiveProcessor.processClientArchive(archive, internetMimeMultipartAppClient_Test.class, sunAppClientUrl);


		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class,
				"internetMimeMultipart_appclient_vehicle.ear");
		ear.addAsModule(archive);

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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
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
	@TargetVehicle("appclient")
  public void mimeMultipartTest() throws Exception {
		super.mimeMultipartTest();
  }

}
