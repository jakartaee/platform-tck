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
 * $Id$
 */

/*
 * @(#)Client.java
 */

package com.sun.ts.tests.xa.ee.resXcomp1;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Properties;
import java.util.Vector;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;


import jakarta.transaction.UserTransaction;

public class ClientEJB extends Client implements Serializable {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("appclient")
	  @Deployment(name = "ejb", order = 2)
		public static EnterpriseArchive createDeploymentAppclient(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
			JavaArchive ejbClient = ShrinkWrap.create(JavaArchive.class, "xa_resXcomp1_ejb_vehicle_client.jar");
			ejbClient.addPackages(true, "com.sun.ts.tests.common.vehicle");
			ejbClient.addPackages(true, "com.sun.ts.lib.harness");
			ejbClient.addClasses(ClientEJB.class, Client.class);
			ejbClient.addAsManifestResource(ClientEJB.class.getPackage(), "ejb_vehicle_ejb.xml");
			URL resURL = ClientEJB.class.getResource("com/sun/ts/tests/common/vehicle/ejb/ejb_vehicle_client.xml");
			if (resURL != null) {
				ejbClient.addAsManifestResource(resURL, "application-client.xml");
			}
			ejbClient.addAsManifestResource(new StringAsset("Main-Class: com.sun.ts.tests.common.vehicle.VehicleClient\n"),
					"MANIFEST.MF");

			JavaArchive ejb = ShrinkWrap.create(JavaArchive.class, "xa_resXcomp1_ejb_vehicle_ejb.jar");
			ejb.addPackages(true, "com.sun.ts.tests.common.vehicle");
			ejb.addPackages(true, "com.sun.ts.lib.harness");
			ejb.addClasses(ClientEJB.class, Client.class);

			ejb.addAsManifestResource(ClientEJB.class.getPackage(), "ejb_vehicle_ejb.xml");

			EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "xa_resXcomp1_ejb_vehicle.ear");
			ear.addAsModule(ejbClient);
			ear.addAsModule(ejb);
			return ear;
		};


  public static void main(String[] args) {
    ClientEJB client = new ClientEJB();
    Status s = client.run(args, System.out, System.err);
    s.exit();
  }
  
  
  public String getTxRef() {
	   return "java:comp/env/ejb/MyEjbReferenceEJB";
 }

  /* Run test */

  /*
   * @testName: test1
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:68
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the TxBean (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from TxBean EJB.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void test1() throws Exception {
	  super.test1();
  }

  /*
   * @testName: test2
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:68
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the TxBean (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a rollback to a single table.
   *
   * Database Access is performed from TxBean EJB.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void test2() throws Exception {
	  super.test2();
  }

  /*
   * @testName: test3
   *
   * @assertion_ids: JavaEE:SPEC:74
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the TxBean (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a commit to a single table.
   *
   * Database Access is performed from TxBean EJB.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void test3() throws Exception {
	  super.test3();
  }

  /*
   * @testName: test4
   *
   * @assertion_ids: JavaEE:SPEC:74
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the TxBean (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a rollback to a single table.
   *
   * Database Access is performed from TxBean EJB.
   *
   */
	@Test
	@TargetVehicle("ejb")
  public void test4() throws Exception {
	  super.test4();
  }

}
