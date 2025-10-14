/*
 * Copyright (c) 2007, 2025 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.13 03/05/21
 */

package com.sun.ts.tests.assembly.standalone.jar;

import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@Tag("assembly")
@Tag("platform")
@Tag("tck-appclient")
@ExtendWith(ArquillianExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client extends EETest {
  /** JNDI Name we use to lookup the bean */
  public static final String lookupName = "java:comp/env/ejb/TestBean";

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) throws Exception {
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
      this.nctx = new TSNamingContext();

      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("[Client] Setup failed:" + e, e);
    }
  }


  static final String VEHICLE_ARCHIVE = "assembly_standalone_jar";

  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive assembly_standalone_jar_client = ShrinkWrap.create(JavaArchive.class, "assembly_standalone_jar_client.jar");
        assembly_standalone_jar_client.addClasses(
        Fault.class,
        EETest.class,
        SetupException.class,
        com.sun.ts.tests.assembly.standalone.jar.TestBean.class,
        com.sun.ts.tests.assembly.standalone.jar.Client.class);
    // The application-client.xml descriptor
    URL resURL = Client.class.getResource("assembly_standalone_jar_client.xml");
    if (resURL != null) {
      assembly_standalone_jar_client.addAsManifestResource(resURL, "application-client.xml");
    }
    resURL = Client.class.getResource("assembly_standalone_jar_client.jar.sun-application-client.xml");
    if(resURL != null) {
      assembly_standalone_jar_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    assembly_standalone_jar_client
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(assembly_standalone_jar_client, Client.class, resURL);


    EnterpriseArchive assembly_standalone_jar_ear = ShrinkWrap.create(EnterpriseArchive.class, "assembly_standalone_jar_ear.ear");
        assembly_standalone_jar_ear.addAsModule(assembly_standalone_jar_client);
    
    return assembly_standalone_jar_ear;
  }

  @Deployment(name = "assembly_standalone_jar_component_ejb", order = 1, testable = false)
  public static JavaArchive createEjbDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive assembly_standalone_jar_component_ejb = ShrinkWrap.create(JavaArchive.class, "assembly_standalone_jar_component_ejb.jar");
        assembly_standalone_jar_component_ejb.addClasses(
        com.sun.ts.tests.assembly.standalone.jar.TestBean.class,
        com.sun.ts.tests.assembly.standalone.jar.TestBeanEJB.class,
        com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class,
        com.sun.ts.lib.util.RemoteLoggingInitException.class,
        com.sun.ts.lib.util.TSNamingContext.class,
        com.sun.ts.lib.util.TSNamingContextInterface.class,
        com.sun.ts.lib.util.TestReportInfo.class,
        com.sun.ts.lib.util.TestUtil.class);
    // The ejb-jar.xml descriptor
    URL resURL = Client.class.getResource("assembly_standalone_jar_component_ejb.xml");
    if (resURL != null) {
      assembly_standalone_jar_component_ejb.addAsManifestResource(resURL, "ejb-jar.xml");
    }
    resURL = Client.class.getResource("assembly_standalone_jar_component_ejb.jar.sun-ejb-jar.xml");
    if(resURL != null) {
      assembly_standalone_jar_component_ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
    }
    archiveProcessor.processEjbArchive(assembly_standalone_jar_component_ejb, Client.class, resURL);

    return assembly_standalone_jar_component_ejb;
  }



  /**
   * @testName: testStandaloneJar
   *
   * @assertion_ids: JavaEE:SPEC:261
   *
   * @test_Strategy: Package an ejb-jar file
   *                 (assembly_standalone_jar_component_ejb.jar).
   *
   *                 Package a .ear file containing an application client
   *                 accessing a bean in the stand-alone ejb-jar module (JNDI
   *                 names match in runtime information).
   *
   *                 Deploy the ejb-jar module and the .ear file.
   *
   *                 Run the client and check that we can call a business method
   *                 on the referenced bean at runtime.
   */
  @Test
  @OperateOnDeployment("assembly_standalone_jar")
  public void testStandaloneJar() throws Fault {
    TestBean bean;
    boolean pass = false;

    try {
      logTrace("[Client] Looking up bean...");
      bean = (TestBean) nctx.lookup(lookupName, TestBean.class);
      bean.initLogging(props);

      logTrace("[Client] Calling bean...");
      pass = bean.ping();
      if (!pass) {
        TestUtil.logErr("Classpath test failed - pass = " + pass);
        throw new Fault("classpath test failed");
      }
    } catch (Exception e) {
      TestUtil.logErr("Classpath test failed", e);
      throw new Fault("classpath test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup()");
  }
}
