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
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.javamail.ee.common.MailTestUtil;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import jakarta.mail.FetchProfile;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

public class fetchprofileEJB_Test extends fetchprofile_Test implements Serializable {


	  @TargetsContainer("tck-javatest")
	  @OverProtocol("appclient")
	@Deployment(name = "ejb", order = 2)
	public static EnterpriseArchive createDeploymentEjb(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "fetchprofile_ejb_vehicle_client.jar");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejbClient.addPackages(true, "com.sun.ts.lib.harness");

		URL resURL = fetchprofileEJB_Test.class.getResource("com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
		if (resURL != null) {
			ejbClient.addAsManifestResource(resURL, "application-client.xml");
		}
		ejbClient.addAsManifestResource(fetchprofileEJB_Test.class.getPackage(), "fetchprofile_ejb_vehicle_client.jar.sun-application-client.xml" , "sun-application-client.xml");
		ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
				"MANIFEST.MF");

		JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "fetchprofile_ejb_vehicle_ejb.jar");
		ejb.addPackages(true, "com.sun.ts.tests.jdbc.ee.common");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
		ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
		ejb.addPackages(true, "com.sun.ts.lib.harness");
		ejb.addClasses(fetchprofileEJB_Test.class, fetchprofile_Test.class);

		ejb.addAsManifestResource(fetchprofileEJB_Test.class.getPackage(), "fetchprofile_ejb_vehicle_ejb.jar.sun-ejb-jar.xml", "sun-ejb-jar.xml");
		ejb.addAsManifestResource(fetchprofileEJB_Test.class.getPackage(), "ejb_vehicle_ejb.xml", "ejb-jar.xml");
		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "fetchprofile_ejb_vehicle.ear");
		ear.addAsModule(ejbClient);
		ear.addAsModule(ejb);
		return ear;

	};



	public static void main(String[] args) {
		fetchprofileEJB_Test theTests = new fetchprofileEJB_Test();
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
	@TargetVehicle("ejb")
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
	@TargetVehicle("ejb")
	public void test2() throws Exception {
		super.test2();
	}
	/*
	 * @testName: getItems
	 * 
	 * @assertion_ids: JavaEE:SPEC:235;
	 * 
	 * @test_Strategy: getItems
	 */
	// derived from javamail suite fetchProfile getItems
	@Test
	@TargetVehicle("ejb")
	public void getItems() throws Exception {
		super.getItems();
	}

}
