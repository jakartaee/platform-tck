/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.11 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.resref.scope;

import java.io.IOException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.shared.resref.scope.QueueCode;
import com.sun.ts.tests.assembly.util.shared.resref.scope.TopicCode;

import tck.arquillian.porting.lib.spi.TestArchiveProcessor;

@ExtendWith(ArquillianExtension.class)
public class Client extends EETest {

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }
  
  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")	
 
	@Deployment(testable = false)
	public static EnterpriseArchive createDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor)
			throws IOException {
		JavaArchive ejbClient1 = ShrinkWrap.create(JavaArchive.class, "appclient_dep_resref_scope_another_client.jar");
		ejbClient1.addPackages(true, Client.class.getPackage());
		ejbClient1.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient1.addClasses(Client.class, QueueCode.class, TopicCode.class);

		// The appclient-client descriptor
		URL appClientUrl = Client.class.getResource(
				"com/sun/ts/tests/appclient/deploy/resref/scope/appclient_dep_resref_scope_another_client.xml");
		if (appClientUrl != null) {
			ejbClient1.addAsManifestResource(appClientUrl, "application-client.xml");
		}
		// The sun appclient-client descriptor
		URL sunAppClientUrl = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/resref/scope/appclient_dep_resref_scope_another_client.jar.sun-application-client.xml");
		if (sunAppClientUrl != null) {
			ejbClient1.addAsManifestResource(sunAppClientUrl, "sun-application-client.xml");
		}
		
		ejbClient1.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.resref.scope.Client" + "\n"),
				"MANIFEST.MF");


		JavaArchive ejbClient2 = ShrinkWrap.create(JavaArchive.class, "appclient_dep_resref_scope_client.jar");
		ejbClient2.addPackages(true, "com.sun.ts.lib.harness");
		ejbClient2.addClasses(Client.class, QueueCode.class);

		URL resURL = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/resref/scope/appclient_dep_resref_scope_client.xml");

		if (resURL != null) {
			ejbClient2.addAsManifestResource(resURL, "ejb-jar.xml");
		}

		resURL = Client.class.getResource(
				"/com/sun/ts/tests/appclient/deploy/resref/scope/appclient_dep_resref_scope_client.jar.sun-application-client.xml");

		if (resURL != null) {
			ejbClient2.addAsManifestResource(resURL, "sun-ejb-jar.xml");
		}
		
		ejbClient2.addAsManifestResource(
				new StringAsset("Main-Class: " + "com.sun.ts.tests.appclient.deploy.resref.scope.Client" + "\n"),
				"MANIFEST.MF");


		EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, "appclient_dep_resref_scope.ear");
		ear.addAsModule(ejbClient1);
		ear.addAsModule(ejbClient2);
		return ear;
	};


  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties props) throws Exception {
    this.props = props;

    try {
      nctx = new TSNamingContext();
      logMsg("[Client] Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
    }
  }

  /**
   * @testName: testScope
   *
   * @assertion_ids: JavaEE:SPEC:125
   *
   * @test_Strategy:
   *
   *                 We package in the same .ear file:
   *
   *                 - Two application clients using the same res-ref-name
   *                 ('jms/myFactory') to reference two distinct resource
   *                 manager connection factories (a QueueConnectionFactory and
   *                 a TopicConnectionFactory).
   *
   *                 We check that:
   *
   *                 - We can deploy the application. - We can run one of the
   *                 application clients - This application client can lookup
   *                 its resource manager connection factory. - We can cast that
   *                 factory to its expected Java type and use it to create a
   *                 connection. This validates the resolution of the resource
   *                 manager connection factories reference.
   *
   */
  @Test
  public void testScope() throws Exception {
    boolean pass;

    try {
      pass = QueueCode.checkYourQueue(nctx);
      if (!pass) {
        throw new Exception("res-ref scope test failed!");
      }
    } catch (Exception e) {
      throw new Exception("res-ref scope test failed: " + e, e);
    }
  }

  public void cleanup() throws Exception {
    logMsg("[Client] cleanup()");
  }
}
