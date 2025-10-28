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
 * @(#)Client.java	1.11 03/05/16
 */

package com.sun.ts.tests.assembly.classpath.ejb;

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
import org.jboss.shrinkwrap.api.asset.ArchiveAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
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


  static final String VEHICLE_ARCHIVE = "assembly_classpath_ejb";

  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive direct_classpath_util = ShrinkWrap.create(JavaArchive.class, "direct_classpath_util.jar");
    direct_classpath_util.addClasses(com.sun.ts.tests.assembly.classpath.util.ClassPathUtil.class);
    URL resURL = Client.class.getResource("/com/sun/ts/tests/assembly/classpath/util/META-INF/ejb-jar.xml");
    if (resURL != null) {
      direct_classpath_util.addAsManifestResource(resURL, "ejb-jar.xml");
    }
    direct_classpath_util.addAsManifestResource(new StringAsset("Class-Path: indirect_classpath_util.jar" + "\n"), "MANIFEST.MF");


    JavaArchive indirect_classpath_util = ShrinkWrap.create(JavaArchive.class, "indirect_classpath_util.jar");
    indirect_classpath_util.addClasses(com.sun.ts.tests.assembly.classpath.util.IndirectClassPathUtil.class);
    resURL = Client.class.getResource("/com/sun/ts/tests/assembly/classpath/util/META-INF/ejb-jar.xml");
    if (resURL != null) {
      indirect_classpath_util.addAsManifestResource(resURL, "ejb-jar.xml");
    }


    JavaArchive assembly_classpath_ejb_client = ShrinkWrap.create(JavaArchive.class,"assembly_classpath_ejb_client.jar");
    assembly_classpath_ejb_client.addClasses(
        Fault.class,
        EETest.class,
        SetupException.class,
        com.sun.ts.tests.assembly.classpath.ejb.TestBean.class,
        com.sun.ts.tests.assembly.classpath.ejb.Client.class);
    resURL = Client.class.getResource("assembly_classpath_ejb_client.xml");
    if (resURL != null) {
      assembly_classpath_ejb_client.addAsManifestResource(resURL, "application-client.xml");
    }
    resURL = Client.class.getResource("assembly_classpath_ejb_client.jar.sun-application-client.xml");
    if(resURL != null) {
      assembly_classpath_ejb_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    assembly_classpath_ejb_client
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(assembly_classpath_ejb_client, Client.class, resURL);


    JavaArchive assembly_classpath_ejb_ejb = ShrinkWrap.create(JavaArchive.class, "assembly_classpath_ejb_ejb.jar");
        assembly_classpath_ejb_ejb.addClasses(
        com.sun.ts.tests.assembly.classpath.ejb.TestBean.class,
        com.sun.ts.tests.assembly.classpath.ejb.TestBeanEJB.class,
        com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class
        );
    URL ejbResURL = Client.class.getResource("assembly_classpath_ejb_ejb.xml");
    if (ejbResURL != null) {
      assembly_classpath_ejb_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
    }
    ejbResURL = Client.class.getResource("assembly_classpath_ejb_ejb.jar.sun-ejb-jar.xml");
    if(ejbResURL != null) {
      assembly_classpath_ejb_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
    }
    assembly_classpath_ejb_ejb.addAsManifestResource(new StringAsset("Class-Path: libs/direct_classpath_util.jar" + "\n"), "MANIFEST.MF");
    archiveProcessor.processEjbArchive(assembly_classpath_ejb_ejb, Client.class, ejbResURL);

    EnterpriseArchive assembly_classpath_ejb_ear = ShrinkWrap.create(EnterpriseArchive.class, "assembly_classpath_ejb.ear");
    assembly_classpath_ejb_ear.addAsModule(assembly_classpath_ejb_client);
    assembly_classpath_ejb_ear.addAsModule(assembly_classpath_ejb_ejb);
    assembly_classpath_ejb_ear.add(new ArchiveAsset(direct_classpath_util, ZipExporter.class), "libs/direct_classpath_util.jar");
    assembly_classpath_ejb_ear.add(new ArchiveAsset(indirect_classpath_util, ZipExporter.class), "libs/indirect_classpath_util.jar");

    URL earResURL = Client.class.getResource("application.xml");
    if (earResURL != null) {
      assembly_classpath_ejb_ear.addAsManifestResource(earResURL, "application.xml");
    }
    archiveProcessor.processEarArchive(assembly_classpath_ejb_ear, Client.class, earResURL);

    return assembly_classpath_ejb_ear;
  }

  /**
   * @testName: testDirectLibrary
   *
   * @assertion_ids: JavaEE:SPEC:255
   *
   * @test_Strategy: We package an application with:
   *
   *                 - A utility .jar file containing the class ClassPathUtil.
   *                 This .jar file is not a J2EE module and do not appear in
   *                 the upper level application DD. It includes a "dummy" DD
   *                 though, that must be ignored by the deployment tool.
   *
   *                 - An EJB jar file. This jar file includes a Class-Path
   *                 header referencing the utility .jar file in its manifest
   *                 file, and does not contain any definition of ClassPathUtil.
   *
   *                 We check that:
   *
   *                 - We can deploy the application
   *
   *                 - The EJB can create a ClassPathUtil instance at runtime,
   *                 and invoke a method on that instance. This validates that
   *                 the referenced .jar file appears in the logical classpath
   *                 of the EJB.
   *
   */
  @Test
  public void testDirectLibrary() throws Fault {
    TestBean bean;
    boolean pass;

    try {
      logTrace("[Client] Looking up bean...");
      bean = (TestBean) nctx.lookup(lookupName, TestBean.class);
      bean.initLogging(props);

      logTrace("[Client] Calling bean...");
      pass = bean.testDirectLibrary();
      if (!pass) {
        throw new Fault("EJB direct classpath test failed");
      }
    } catch (Exception e) {
      throw new Fault("Caught exception: " + e, e);
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
   *                 and do not appear in the upper level application DD. It
   *                 includes a "dummy" DD though, that must be ignored by the
   *                 deployment tool.
   *
   *                 - A second utility .jar file containing the class
   *                 ClassPathUtil. This .jar file is not a J2EE module and do
   *                 not appear in the upper level application DD. It includes a
   *                 "dummy" DD though, that must be ignored by the deployment
   *                 tool. This jar file includes in its manifest file a
   *                 Class-Path header referencing the utility .jar file
   *                 containing IndirectClassPathUtil. It does not contain any
   *                 definition of IndirectClassPathUtil.
   *
   *                 - An EJB jar file. This jar file includes in its manifest
   *                 file a Class-Path header referencing the second utility
   *                 .jar file. It does not contain any definition of
   *                 ClassPathUtil nor IndirectClassPathUtil.
   *
   *                 We check that:
   *
   *                 - We can deploy the application
   *
   *                 - The EJB can create a ClassPathUtil instance at runtime,
   *                 and invoke a method on that instance. This validates that
   *                 the second utility .jar file appears in the logical
   *                 classpath of the EJB.
   *
   *                 - The EJB can create a IndirectClassPathUtil instance at
   *                 runtime, and invoke a method on that instance. This
   *                 validates that the first utility .jar file appears in the
   *                 logical classpath of the EJB.
   *
   */
  @Test
  public void testIndirectLibrary() throws Fault {
    TestBean bean;
    boolean pass;

    try {
      logTrace("[Client] Looking up bean...");
      bean = (TestBean) nctx.lookup(lookupName, TestBean.class);
      bean.initLogging(props);

      logTrace("[Client] Calling bean...");
      pass = bean.testIndirectLibrary();
      if (!pass) {
        throw new Fault("EJB indirect classpath test failed");
      }
    } catch (Exception e) {
      throw new Fault("Caught exception: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
