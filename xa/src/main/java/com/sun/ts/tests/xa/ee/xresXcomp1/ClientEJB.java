/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.3 03/05/16
 */

/*
 * @(#)Client.java	1.20 02/07/19
 */
package com.sun.ts.tests.xa.ee.xresXcomp1;

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

public class ClientEJB extends Client implements Serializable {
	  
	  @TargetsContainer("tck-appclient")
	  @OverProtocol("appclient")
	  @Deployment(name = "ejb", testable = true)
		public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
			JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "xa_xresXcomp1_ejb_vehicle_client.jar");
			ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle");
			ejbClient.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
			ejbClient.addPackages(true, "com.sun.ts.lib.harness");
			ejbClient.addClasses(ClientEJB.class, Client.class);
			URL resURL = ClientEJB.class.getResource("/com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
			if (resURL != null) {
				ejbClient.addAsManifestResource(resURL, "application-client.xml");
			}

			resURL = ClientEJB.class.getResource(
					"/com/sun/ts/tests/xa/ee/xresXcomp1/xa_xresXcomp1_ejb_vehicle_client.jar.sun-application-client.xml");
			if (resURL != null) {
				ejbClient.addAsManifestResource(resURL, "sun-application-client.xml");
			}

			ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
					"MANIFEST.MF");
			
			
			JavaArchive ejbVehicle = ShrinkWrap.create(JavaArchive.class, "xa_xresXcomp1_ejb_vehicle_ejb.jar");
			ejbVehicle.addPackages(false, "com.sun.ts.tests.common.vehicle");
			ejbVehicle.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
			ejbVehicle.addPackages(true, "com.sun.ts.lib.harness");
			ejbVehicle.addClasses(Ejb1Test.class, Ejb1TestEJB.class);
			ejbVehicle.addClasses(ClientEJB.class, Client.class);
			resURL = ClientEJB.class.getResource("/com/sun/ts/tests/xa/ee/xresXcomp1/ejb_vehicle_ejb.xml");
			if (resURL != null) {
				ejbVehicle.addAsManifestResource(resURL, "ejb-jar.xml");
			}

			resURL = ClientEJB.class.getResource("/com/sun/ts/tests/xa/ee/xresXcomp1/xa_xresXcomp1_ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
			if (resURL != null) {
				ejbVehicle.addAsManifestResource(resURL, "sun-ejb-jar.xml");
			}

			JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "xa_xresXcomp1_ee_txpropagate1_ejb.jar");
			ejb.addPackages(false, "com.sun.ts.tests.common.vehicle");
			ejb.addPackages(false, "com.sun.ts.tests.common.vehicle.ejb");
			ejb.addPackages(false, "com.sun.ts.tests.common.util");
			ejb.addPackages(false, "com.sun.ts.tests.common.whitebox");
			ejb.addPackages(true, "com.sun.ts.lib.harness");
			ejb.addClasses(Ejb1Test.class, Ejb1TestEJB.class);
			ejb.addClasses(ClientEJB.class, Client.class);

			resURL = ClientEJB.class
					.getResource("/com/sun/ts/tests/xa/ee/xresXcomp1/xa_xresXcomp1_ee_txpropagate1_ejb.jar.sun-ejb-jar.xml");

			if (resURL != null) {
				ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
			}

			resURL = ClientEJB.class.getResource("/com/sun/ts/tests/xa/ee/xresXcomp1/xa_xresXcomp1_ee_txpropagate1_ejb.xml");

			if (resURL != null) {
				ejb.addAsManifestResource(resURL, "ejb-jar.xml");
			}

			EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "xa_xresXcomp1_ejb_vehicle.ear");
			ear.addAsModule(ejbClient);
			ear.addAsModule(ejbVehicle);
			ear.addAsModule(ejb);
			return ear;
		};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    ClientEJB theTests = new ClientEJB();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }


  /* Run tests */
  /*
   * @testName: test14
   *
   * @assertion_ids: JavaEE:SPEC:90; JavaEE:SPEC:92; JavaEE:SPEC:79;
   * JavaEE:SPEC:76; JavaEE:SPEC:74; JavaEE:SPEC:70
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_commit
   */
	@Test
	@TargetVehicle("ejb")
  public void test14() throws Exception {
		super.test14();
  }

  /*
   * @testName: test15
   *
   * @assertion_ids: JavaEE:SPEC:90; JavaEE:SPEC:92; JavaEE:SPEC:79;
   * JavaEE:SPEC:76; JavaEE:SPEC:74; JavaEE:SPEC:70
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_commit
   */
	@Test
	@TargetVehicle("ejb")
  public void test15() throws Exception {
		super.test15();
  }

  /*
   * @testName: test16
   *
   * @assertion_ids: JavaEE:SPEC:90; JavaEE:SPEC:92; JavaEE:SPEC:79;
   * JavaEE:SPEC:76; JavaEE:SPEC:74; JavaEE:SPEC:70
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_commit
   */
	@Test
	@TargetVehicle("ejb")
  public void test16() throws Exception {
		super.test16();
  }

}
