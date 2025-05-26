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

public class fetchprofileAppClient_Test extends fetchprofile_Test implements Serializable {

	@TargetsContainer("tck-appclient")
	@OverProtocol("appclient")
	@Deployment(name = "appclient", testable = true)
	public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive appclientArchive = ShrinkWrap.create(JavaArchive.class,
				"fetchprofile_appclient_vehicle_client.jar");

		appclientArchive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		appclientArchive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		appclientArchive.addPackages(true, "com.sun.ts.lib.harness");
		appclientArchive.addClass(com.sun.ts.tests.common.base.EETest.class);
		appclientArchive.addClass(com.sun.ts.tests.common.base.ServiceEETest.class);
		appclientArchive.addClasses(fetchprofileAppClient_Test.class, fetchprofile_Test.class);
		
		// The appclient-client descriptor
		URL appClientUrl = fetchprofileAppClient_Test.class
				.getResource("/com/sun/ts/tests/javamail/ee/fetchprofile/appclient_vehicle_client.xml");
		if (appClientUrl != null) {
			appclientArchive.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		appclientArchive.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.common.vehicle.VehicleClient" + "\n"),
				"MANIFEST.MF");

		// The sun appclient-client descriptor
		URL sunAppClientUrl = fetchprofileAppClient_Test.class.getResource(
				"/com/sun/ts/tests/common/vehicle/appclient/appclient_vehicle_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			appclientArchive.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}
		// Call the archive processor
		archiveProcessor.processClientArchive(appclientArchive, fetchprofileAppClient_Test.class, sunAppClientUrl);

		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "fetchprofile_appclient_vehicle.ear");
		ear.addAsModule(appclientArchive);

		return ear;
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
