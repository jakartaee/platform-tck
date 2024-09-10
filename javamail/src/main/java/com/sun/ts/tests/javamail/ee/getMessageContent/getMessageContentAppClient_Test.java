/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.javamail.ee.getMessageContent;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TSNamingContextInterface;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;


import tck.arquillian.protocol.common.TargetVehicle;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

public class getMessageContentAppClient_Test extends getMessageContent_Test {
  
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("appclient")
	@Deployment(name = "appclient", order = 2)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "getMessageContent_appclient_vehicle_client.jar");
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.appclient");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(getMessageContentAppClient_Test.class, getMessageContent_Test.class);
		
        // The appclient-client descriptor
        URL appClientUrl = getMessageContentAppClient_Test.class.getResource("appclient_vehicle_client.xml");
        if(appClientUrl != null) {
        	archive.addAsManifestResource(appClientUrl, "application-client.xml");
        }
        // The sun appclient-client descriptor
        URL sunAppClientUrl = getMessageContentAppClient_Test.class.getResource("getMessageContent_appclient_vehicle_client.jar.sun-application-client.xml");
        if(sunAppClientUrl != null) {
        	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
        }
        // Call the archive processor
        archiveProcessor.processClientArchive(archive, getMessageContentAppClient_Test.class, sunAppClientUrl);

		System.out.println(archive.toString(true));
		return archive;
	};
	

  public static void main(String[] args) {
    getMessageContentAppClient_Test theTests = new getMessageContentAppClient_Test();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

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
  } // end of test1()

 
}
