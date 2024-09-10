/*
 * Copyright (c) 2013, 2021 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.javamail.ee.fetchprofile;

import java.io.IOException;
import java.io.Serializable;
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
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;
import tck.arquillian.protocol.common.TargetVehicle;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;

import jakarta.mail.FetchProfile;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

public class fetchprofileAppClient_Test extends fetchprofile_Test implements Serializable {

	  @TargetsContainer("tck-javatest")
	  @OverProtocol("appclient")
	@Deployment(name = "appclient", order = 2)
	public static JavaArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
			JavaArchive archive = ShrinkWrap.create(JavaArchive.class, "fetchprofile_appclient_vehicle_client.jar");

		  try {
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(fetchprofileAppClient_Test.class, fetchprofile_Test.class);
        // The appclient-client descriptor
        URL appClientUrl = fetchprofileAppClient_Test.class.getResource("appclient_vehicle_client.xml");
        if(appClientUrl != null) {
        	archive.addAsManifestResource(appClientUrl, "application-client.xml");
        }
        // The sun appclient-client descriptor
        URL sunAppClientUrl = fetchprofileAppClient_Test.class.getResource("fetchprofile_appclient_vehicle_client.jar.sun-application-client.xml");
        if(sunAppClientUrl != null) {
        	archive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
        }
        // Call the archive processor
        archiveProcessor.processClientArchive(archive, fetchprofileAppClient_Test.class, sunAppClientUrl);
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
		System.out.println(archive.toString(true));
		return archive;
	};
	
	public static void main(String[] args) {
		fetchprofileAppClient_Test theTests = new fetchprofileAppClient_Test();
		Status s = theTests.run(args, System.out, System.err);
		s.exit();
	}

	/*
	 * @testName: test1
	 * 
	 * @assertion_ids: JavaEE:SPEC:235;
	 * 
	 * @test_Strategy: verify FetchProfile()
	 */
	// derived from javamail suite fetchProfile_Test class
	@Test
	@TargetVehicle("appclient")
	public void test1() throws Exception {
		super.test1();
	} // end of test1()

	/*
	 * @testName: test2
	 * 
	 * @assertion_ids: JavaEE:SPEC:235;
	 * 
	 * @test_Strategy: verify FetchProfile()
	 */
	// derived from javamail suite fetchProfile_Test class
	@Test
	@TargetVehicle("appclient")
	public void test2() throws Exception {
		super.test2();
	}// end of test2()

	/*
	 * @testName: getItems
	 * 
	 * @assertion_ids: JavaEE:SPEC:235;
	 * 
	 * @test_Strategy: getItems
	 */
	// derived from javamail suite fetchProfile getItems
	@Test
	@TargetVehicle("appclient")
	public void getItems() throws Exception {
		super.getItems();
	}
}
