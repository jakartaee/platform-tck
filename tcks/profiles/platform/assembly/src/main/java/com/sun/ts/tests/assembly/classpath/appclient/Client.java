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
 * @(#)Client.java	1.13 03/05/16
 */

/*
 * @(#)Client.java	1.10 02/03/20
 */

package com.sun.ts.tests.assembly.classpath.appclient;

import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.assembly.classpath.util.ClassPathUtil;

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

@Tag("assembly")
@Tag("platform")
@Tag("tck-appclient")
@ExtendWith(ArquillianExtension.class)
public class Client extends EETest {

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
      nctx = new TSNamingContext();
      logMsg("Client: Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Fault("Client: Setup failed:", e);
    }
  }

  static final String VEHICLE_ARCHIVE = "assembly_classpath_appclient";

  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive direct_classpath_util = ShrinkWrap.create(JavaArchive.class, "direct_classpath_util.jar");
    direct_classpath_util.addClasses(com.sun.ts.tests.assembly.classpath.util.ClassPathUtil.class,
    Client.class);
    URL resURL = Client.class.getResource("/util/META-INF/ejb-jar.xml");
    if (resURL != null) {
      direct_classpath_util.addAsManifestResource(resURL, "ejb-jar.xml");
    }
    direct_classpath_util.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    // archiveProcessor.processEjbArchive(direct_classpath_util, Client.class, resURL);


    JavaArchive indirect_classpath_util = ShrinkWrap.create(JavaArchive.class, "indirect_classpath_util.jar");
    indirect_classpath_util.addClasses(com.sun.ts.tests.assembly.classpath.util.IndirectClassPathUtil.class,
    Client.class);
    resURL = Client.class.getResource("/util/META-INF/ejb-jar.xml");
    if (resURL != null) {
      indirect_classpath_util.addAsManifestResource(resURL, "ejb-jar.xml");
    }
    indirect_classpath_util.addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    // archiveProcessor.processEjbArchive(indirect_classpath_util, Client.class, resURL);

    JavaArchive assembly_classpath_appclient_client = ShrinkWrap.create(JavaArchive.class,
        "assembly_classpath_appclient_client.jar");
    assembly_classpath_appclient_client.addClasses(
        Fault.class,
        EETest.class,
        SetupException.class,
        com.sun.ts.tests.assembly.classpath.appclient.Client.class);
    // The application-client.xml descriptor
    resURL = Client.class.getResource("assembly_classpath_appclient_client.xml");
    if (resURL != null) {
      assembly_classpath_appclient_client.addAsManifestResource(resURL, "application-client.xml");
    }
    assembly_classpath_appclient_client
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n" + "Class-Path: libs/direct_classpath_util.jar"+ "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(assembly_classpath_appclient_client, Client.class, resURL);


    EnterpriseArchive assembly_classpath_appclient_ear = ShrinkWrap.create(EnterpriseArchive.class,
        "assembly_classpath_appclient.ear");
    assembly_classpath_appclient_ear.addAsLibrary(direct_classpath_util);
    assembly_classpath_appclient_ear.addAsLibrary(indirect_classpath_util);
    assembly_classpath_appclient_ear.addAsModule(assembly_classpath_appclient_client);
    assembly_classpath_appclient_ear.addAsModule(indirect_classpath_util);
    assembly_classpath_appclient_ear.addAsModule(direct_classpath_util);

    URL earResURL = Client.class.getResource("application.xml");
    if (earResURL != null) {
      assembly_classpath_appclient_ear.addAsManifestResource(earResURL, "application.xml");
    }
    archiveProcessor.processEarArchive(assembly_classpath_appclient_ear, Client.class, earResURL);

    return assembly_classpath_appclient_ear;
  }

  /**
   * @testName: testDirectLibrary
   *
   * @assertion_ids: JavaEE:SPEC:255
   *
   * @test_Strategy: We package an application with:
   *
   *                 - A utility .jar file containing the class ClassPathUtil.
   *                 This .jar file is not a J2EE module and does not appear in
   *                 the upper level application DD. It includes a "dummy" DD
   *                 though, that must be ignored by the deployment tool.
   *
   *                 - An application client jar file. This jar file includes a
   *                 Class-Path header referencing the utility .jar file in its
   *                 manifest file, and does not contain any definition of
   *                 ClassPathUtil.
   *
   *                 We check that:
   *
   *                 - We can deploy the application
   *
   *                 - The application client can create a ClassPathUtil
   *                 instance at runtime, and invoke a method on that instance.
   *                 This validates that the referenced .jar file appears in the
   *                 logical classpath of the application client.
   *
   */
  @Test
  public void testDirectLibrary() throws Fault {
    ClassPathUtil util = null;

    try {
      logMsg("Client: creating class instance...");
      util = new ClassPathUtil();
      util.testDirectLibrary();
    } catch (Exception e) {
      if (null == util) {
        TestUtil.logErr("Bean: can't create instance" + e);
      } else {
        TestUtil.logErr("Bean: can't call instance" + e);
      }

      throw new Fault("Client: classpath test failed: " + e, e);
    }
  }

  /**
   * @testName: testIndirectLibrary
   *
   * @assertion_ids: JavaEE:SPEC:255
   *
   * @test_Strategy: We package an application with:
   *
   *                 - A utility .jar file containing the class
   *                 IndirectClassPathUtil. This .jar file is not a J2EE module
   *                 and does not appear in the upper level application DD. It
   *                 includes a "dummy" DD though, that must be ignored by the
   *                 deployment tool.
   *
   *                 - A second utility .jar file containing the class
   *                 ClassPathUtil. This .jar file is not a J2EE module and does
   *                 not appear in the upper level application DD. It includes a
   *                 "dummy" DD though, that must be ignored by the deployment
   *                 tool. This jar file includes in its manifest file a
   *                 Class-Path header referencing the utility .jar file
   *                 containing IndirectClassPathUtil. It does not contain any
   *                 definition of IndirectClassPathUtil.
   *
   *                 - An application client jar file. This jar file includes in
   *                 its manifest file a Class-Path header referencing the
   *                 second utility .jar file. It does not contain any
   *                 definition of ClassPathUtil nor IndirectClassPathUtil.
   *
   *                 We check that:
   *
   *                 - We can deploy the application
   *
   *                 - The application client can create a ClassPathUtil
   *                 instance at runtime, and invoke a method on that instance.
   *                 This validates that the second utility .jar file appears in
   *                 the logical classpath of the application client.
   *
   *                 - The application client can create a IndirectClassPathUtil
   *                 instance at runtime, and invoke a method on that instance.
   *                 This validates that the first utility .jar file appears in
   *                 the logical classpath of the application client.
   *
   */
  @Test
  public void testIndirectLibrary() throws Fault {
    ClassPathUtil util = null;

    try {
      logMsg("Client: creating class instance...");
      util = new ClassPathUtil();
      util.testIndirectLibrary();
    } catch (Exception e) {
      if (null == util) {
        TestUtil.logErr("Bean: can't create instance" + e);
      } else {
        TestUtil.logErr("Bean: can't call instance" + e);
      }

      throw new Fault("Client: classpath test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("Client: cleanup");
  }
}
