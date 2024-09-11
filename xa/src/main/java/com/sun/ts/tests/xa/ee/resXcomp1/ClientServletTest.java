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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;


import jakarta.transaction.UserTransaction;

public class ClientServletTest extends Client implements Serializable {
	
	  @TargetsContainer("tck-javatest")
	  @OverProtocol("javatest")
	  @Deployment(name = "servlet", testable = true)
		public static EnterpriseArchive createDeploymentServlet(@ArquillianResource TestArchiveProcessor archiveProcessor) throws IOException {
		  	WebArchive servletVehicleArchive = ShrinkWrap.create(WebArchive.class, "xa_resXcomp1_servlet_vehicle_web.war");
		  	servletVehicleArchive.addPackages(false, "com.sun.ts.tests.common.vehicle");
		  	servletVehicleArchive.addPackages(false, "com.sun.ts.tests.common.vehicle.servlet");
		  	servletVehicleArchive.addPackages(true, "com.sun.ts.lib.harness");
		  	servletVehicleArchive.addClasses(ClientServletTest.class, Client.class);
		  	servletVehicleArchive.addAsWebInfResource(ClientServletTest.class.getPackage(), "servlet_vehicle_web.xml", "web.xml");
		  	servletVehicleArchive.addAsWebInfResource(ClientServletTest.class.getPackage(), "xa_resXcomp1_servlet_vehicle_web.war.sun-web.xml", "sun-web.xml");
			System.out.println(servletVehicleArchive.toString(true));

		  	JavaArchive javaAchive = ShrinkWrap.create(JavaArchive.class, "xa_resXcomp1_ee_txpropagate2_ejb.jar");
		  	javaAchive.addPackages(false, "com.sun.ts.tests.common.util");
		  	javaAchive.addPackages(false, "com.sun.ts.tests.common.whitebox");
		  	javaAchive.addPackages(true, "com.sun.ts.lib.harness");
		  	javaAchive.addClasses(TxBean.class, TxBeanEJB.class);
		  	javaAchive.addClasses(ClientServletTest.class, Client.class);

		  	javaAchive.addAsManifestResource(ClientServletTest.class.getPackage(), "xa_resXcomp1_ee_txpropagate2_ejb.xml", "ejb-jar.xml");
		  	javaAchive.addAsManifestResource(ClientServletTest.class.getPackage(), "xa_resXcomp1_ee_txpropagate2_ejb.jar.sun-ejb-jar.xml", "sun-ejb-jar.xml");
			System.out.println(javaAchive.toString(true));

		  	
		  	EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "xa_resXcomp1_servlet_vehicle.ear");
		  	ear.addAsModule(servletVehicleArchive);
		  	ear.addAsModule(javaAchive);


			System.out.println(ear.toString(true));
			return ear;
		};

  public static void main(String[] args) {
    ClientServletTest client = new ClientServletTest();
    Status s = client.run(args, System.out, System.err);
    s.exit();
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
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
	@TargetVehicle("servlet")
  public void test4() throws Exception {
	  super.test4();
  }

}
