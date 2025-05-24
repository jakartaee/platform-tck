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
 * %W %E
 */

package com.sun.ts.tests.assembly.compat.single.compat9_10;

import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.util.TSNamingContext;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;


import java.lang.System.Logger;

@Tag("assembly")
@Tag("platform")
@Tag("tck-appclient")
@ExtendWith(ArquillianExtension.class)
public class Client extends EETest {

  /** JNDI Name we use to lookup the bean */
  public static final String lookupName = "java:comp/env/ejb/TestBean";

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      logMsg("[Client] setup(): getting Naming Context...");
      nctx = new TSNamingContext();

      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("[Client] Setup failed:" + e, e);
    }
  }

  private static final Logger logger = System.getLogger(Client.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  static final String VEHICLE_ARCHIVE = "assembly_compat_single_compat9_10";

  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive assembly_compat_single_compat9_10_client = ShrinkWrap.create(JavaArchive.class,
        "assembly_compat_single_compat9_10_client.jar");
    assembly_compat_single_compat9_10_client.addClasses(
        Fault.class,
        EETest.class,
        SetupException.class,
        com.sun.ts.tests.assembly.compat.single.compat9_10.TestBean.class,
        com.sun.ts.tests.assembly.compat.single.compat9_10.TestBeanEJB.class,
        com.sun.ts.tests.assembly.compat.single.compat9_10.Client.class,
        com.sun.ts.lib.util.RemoteLoggingInitException.class,
        com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class);

    URL resURL = Client.class.getResource("application-client.xml");
    if (resURL != null) {
      assembly_compat_single_compat9_10_client.addAsManifestResource(resURL, "application-client.xml");
    }
    resURL = Client.class.getResource("assembly_compat_single_compat9_10_client.jar.sun-application-client.xml");
    if (resURL != null) {
      assembly_compat_single_compat9_10_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    assembly_compat_single_compat9_10_client
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(assembly_compat_single_compat9_10_client, Client.class, resURL);

    JavaArchive assembly_compat_single_compat9_10_ejb = ShrinkWrap.create(JavaArchive.class,
        "assembly_compat_single_compat9_10_ejb.jar");
    assembly_compat_single_compat9_10_ejb.addClasses(
        com.sun.ts.tests.assembly.compat.single.compat9_10.TestBean.class,
        com.sun.ts.tests.assembly.compat.single.compat9_10.TestBeanEJB.class,
        com.sun.ts.lib.util.RemoteLoggingInitException.class,
        com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class
    );
    // The application-client.xml descriptor
    URL ejbResURL = Client.class.getResource("ejb-jar.xml");
    if (ejbResURL != null) {
      assembly_compat_single_compat9_10_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
    }
    ejbResURL = Client.class.getResource("assembly_compat_single_compat9_10_ejb.jar.sun-ejb-jar.xml");
    if (ejbResURL != null) {
      assembly_compat_single_compat9_10_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
    }
    archiveProcessor.processEjbArchive(assembly_compat_single_compat9_10_ejb, Client.class, ejbResURL);

    EnterpriseArchive assembly_compat_single_compat9_10_ear = ShrinkWrap.create(EnterpriseArchive.class,
        "assembly_compat_single_compat9_10.ear");
    assembly_compat_single_compat9_10_ear.addAsModule(assembly_compat_single_compat9_10_client);
    assembly_compat_single_compat9_10_ear.addAsModule(assembly_compat_single_compat9_10_ejb);

    URL earResURL = Client.class.getResource("application.xml");
    if (earResURL != null) {
      assembly_compat_single_compat9_10_ear.addAsManifestResource(earResURL, "application.xml");
    }
    archiveProcessor.processEarArchive(assembly_compat_single_compat9_10_ear, Client.class, earResURL);
    

    return assembly_compat_single_compat9_10_ear;
  }

  /**
   * @testName: test9Compat
   *
   * @assertion_ids: JavaEE:SPEC:283; JavaEE:SPEC:284
   *
   * @test_Strategy: Package an application with an application client and an
   *                 EJB jar file. Use Jakarta EE 10 DD for the 2 modules, and a
   *                 Jakarta EE 9 DD for the application DD.
   *
   *                 Check that: - we can deploy the application, - the
   *                 application client can lookup a bean - the application
   *                 client can create a bean instance - the application client
   *                 can invoke a business method on that instance
   */
  @Test
  public void test9Compat() throws Fault {
    TestBean bean;
    boolean pass;

    try {
      logTrace("[Client] Looking up bean...");
      bean = (TestBean) nctx.lookup(lookupName, TestBean.class);
      bean.initLogging(props);

      logTrace("[Client] Calling bean...");
      pass = bean.ping();
      if (!pass) {
        throw new Fault("Compat single test failed");
      }
    } catch (Exception e) {
      throw new Fault("Compat single test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] Cleanup()");
  }

}
