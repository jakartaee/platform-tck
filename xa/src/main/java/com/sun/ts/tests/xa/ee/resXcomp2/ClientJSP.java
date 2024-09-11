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
 * @(#)Client.java	1.20 02/07/19
 */
package com.sun.ts.tests.xa.ee.resXcomp2;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;

import jakarta.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;


public class ClientJSP extends Client implements Serializable {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("javatest")
	  @Deployment(name = "jsp", testable = true)
		public static EnterpriseArchive createDeploymentJSP(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		  WebArchive jspVehicleArchive = ShrinkWrap.create(WebArchive.class, "xa_resXcomp2_jsp_vehicle_web.war");
		  	jspVehicleArchive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		  	jspVehicleArchive.addPackages(false, "com.sun.ts.tests.common.vehicle.jsp");
		  	jspVehicleArchive.addPackages(true, "com.sun.ts.lib.harness");
		  	jspVehicleArchive.addClasses(ClientJSP.class, Client.class);
			InputStream jspVehicle = Thread.currentThread().getContextClassLoader()
	                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
			jspVehicleArchive.add(new ByteArrayAsset(jspVehicle), "jsp_vehicle.jsp");
	        InputStream clientHtml = Thread.currentThread().getContextClassLoader()
	                .getResourceAsStream("com/sun/ts/tests/common/vehicle/jsp/contentRoot/client.html");
	        jspVehicleArchive.add(new ByteArrayAsset(clientHtml), "client.html");

		  	jspVehicleArchive.addAsWebInfResource(ClientJSP.class.getPackage(), "jsp_vehicle_web.xml", "web.xml");
		  	jspVehicleArchive.addAsWebInfResource(ClientJSP.class.getPackage(), "xa_resXcomp2_jsp_vehicle_web.war.sun-web.xml", "sun-web.xml");
			System.out.println(jspVehicleArchive.toString(true));

		  	JavaArchive javaAchive = ShrinkWrap.create(JavaArchive.class, "xa_resXcomp2_ee_txpropagate3_ejb.jar");
		  	javaAchive.addPackages(false, "com.sun.ts.tests.common.util");
		  	javaAchive.addPackages(false, "com.sun.ts.tests.common.whitebox");
		  	javaAchive.addPackages(true, "com.sun.ts.lib.harness");
		  	javaAchive.addClasses(ClientServlet.class, Client.class);
		  	javaAchive.addClasses(Ejb2Test.class, Ejb2TestEJB.class, Ejb1Test.class, Ejb1TestEJB.class);
		  	javaAchive.addAsManifestResource(ClientJSP.class.getPackage(), "xa_resXcomp2_ee_txpropagate3_ejb.xml", "ejb-jar.xml");
		  	javaAchive.addAsManifestResource(ClientJSP.class.getPackage(), "xa_resXcomp2_ee_txpropagate3_ejb.jar.sun-ejb-jar.xml", "sun-ejb-jar.xml");
			System.out.println(javaAchive.toString(true));

		  	
		  	EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "xa_resXcomp2_jsp_vehicle.ear");
		  	ear.addAsModule(jspVehicleArchive);
		  	ear.addAsModule(javaAchive);


			System.out.println(ear.toString(true));
			return ear;

		};


  /* Run test in standalone mode */
  public static void main(String[] args) {
    ClientJSP theTests = new ClientJSP();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }


  /* Run tests */
  /*
   * @testName: test5
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:68; JavaEE:SPEC:69;
   * JavaEE:SPEC:84
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
	@TargetVehicle("jsp")
  public void test5() throws Exception {
		super.test5();
  }

  /*
   * @testName: test6
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:68; JavaEE:SPEC:69;
   * JavaEE:SPEC:84
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a rollback to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_rollback
   */
	@Test
	@TargetVehicle("jsp")
  public void test6() throws Exception {
		super.test6();
  }

  /*
   * @testName: test7
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:69; JavaEE:SPEC:84
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
	@TargetVehicle("jsp")
  public void test7() throws Exception {
		super.test7();
  }

  /*
   * @testName: test8
   *
   * @assertion_ids: JavaEE:SPEC:74; JavaEE:SPEC:69; JavaEE:SPEC:84
   *
   * @test_Strategy: Contact a Servlet, EJB or JSP. Obtain the UserTransaction
   * interface. Perform global transactions using the Ejb1Test (deployed as
   * TX_REQUIRED) to a single RDBMS table.
   * 
   * Insert/Delete followed by a rollback to a single table.
   *
   * Database Access is performed from Ejb1Test EJB. CLIENT: tx_start, EJB1:
   * Insert, EJB2: Insert, tx_rollback
   */
	@Test
	@TargetVehicle("jsp")
  public void test8() throws Exception {
		super.test8();
  }

}
