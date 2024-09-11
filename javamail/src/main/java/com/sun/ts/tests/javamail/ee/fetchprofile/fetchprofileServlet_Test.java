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

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;

import jakarta.mail.FetchProfile;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;

public class fetchprofileServlet_Test extends fetchprofile_Test implements Serializable {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("javatest")
	@Deployment(name = "servlet", testable = true)
	public static WebArchive createDeploymentServlet(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "fetchprofile_servlet_vehicle_web.war");
		archive.addPackages(true, "com.sun.ts.tests.javamail.ee.common");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		archive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		archive.addPackages(true, "com.sun.ts.lib.harness");
		archive.addClasses(fetchprofileServlet_Test.class, fetchprofile_Test.class);
		
	       // The servlet descriptor
        URL servletUrl = fetchprofileServlet_Test.class.getResource("servlet_vehicle_web.xml");
        if(servletUrl != null) {
        	archive.addAsManifestResource(servletUrl, "web.xml");
        }
        // The sun servlet descriptor
        URL sunServletUrl = fetchprofileServlet_Test.class.getResource("fetchprofile_servlet_vehicle_web.war.sun-web.xml");
        if(sunServletUrl != null) {
        	archive.addAsManifestResource(sunServletUrl, "sun-web.xml");
        }
        // Call the archive processor
        archiveProcessor.processWebArchive(archive, fetchprofileServlet_Test.class, sunServletUrl);

		
		System.out.println(archive.toString(true));
		return archive;
	};
	
	public static void main(String[] args) {
		fetchprofileServlet_Test theTests = new fetchprofileServlet_Test();
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
	public void getItems() throws Exception {
		super.getItems();
	}

}
