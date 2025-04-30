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

package com.sun.ts.tests.assembly.compat.cocktail.compat9_10;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
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
import org.jboss.shrinkwrap.api.asset.UrlAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
import tck.arquillian.protocol.common.TargetVehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;


import java.lang.System.Logger;

@Tag("assembly")
@Tag("platform")
@Tag("tck-appclient")
@ExtendWith(ArquillianExtension.class)
public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  /** Bean lookup */
  private static final String bean1Lookup = prefix + "Vision";

  private static final String bean2Lookup = prefix + "Music";

  /** Expected value for the bean name */
  private static final String bean1RefName = "Rimbaud";

  private static final String bean2RefName = "Verlaine";

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) throws Exception {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      nctx = new TSNamingContext();
      logMsg("[Client] Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
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

  public Client() throws Exception {
  }

  static final String VEHICLE_ARCHIVE = "assembly_classpath_appclient";

  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive assembly_compat_cocktail_compat9_10_jar1_ejb = ShrinkWrap.create(JavaArchive.class, "assembly_compat_cocktail_compat9_10_jar1_ejb.jar");
    assembly_compat_cocktail_compat9_10_jar1_ejb.addClasses(
      com.sun.ts.tests.assembly.compat.cocktail.compat9_10.ReferencedBean.class,
      com.sun.ts.tests.assembly.compat.cocktail.compat9_10.ReferencedBeanEJB.class,   
      com.sun.ts.tests.assembly.util.shared.ejbref.common.ReferencedBeanCode.class,
      com.sun.ts.lib.util.RemoteLoggingInitException.class,
      com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class
      );
    URL resURL = Client.class.getResource("assembly_compat_cocktail_compat9_10_jar1_ejb.xml");
    if (resURL != null) {
      assembly_compat_cocktail_compat9_10_jar1_ejb.addAsManifestResource(resURL, "ejb-jar.xml");
    }

    resURL = Client.class.getResource("assembly_compat_cocktail_compat9_10_jar1_ejb.jar.sun-ejb-jar.xml");
    if (resURL != null) {
      assembly_compat_cocktail_compat9_10_jar1_ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
    }

    archiveProcessor.processEjbArchive(assembly_compat_cocktail_compat9_10_jar1_ejb, Client.class, resURL);
    
    JavaArchive assembly_compat_cocktail_compat9_10_jar2_ejb = ShrinkWrap.create(JavaArchive.class, "assembly_compat_cocktail_compat9_10_jar2_ejb.jar");
    assembly_compat_cocktail_compat9_10_jar2_ejb.addClasses(
      com.sun.ts.tests.assembly.compat.cocktail.compat9_10.ReferencedBean.class,
      com.sun.ts.tests.assembly.compat.cocktail.compat9_10.ReferencedBeanEJB.class,   
      com.sun.ts.tests.assembly.util.shared.ejbref.common.ReferencedBeanCode.class,
      com.sun.ts.lib.util.RemoteLoggingInitException.class,
      com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class
      );
    resURL = Client.class.getResource("assembly_compat_cocktail_compat9_10_jar2_ejb.xml");
    if (resURL != null) {
      assembly_compat_cocktail_compat9_10_jar2_ejb.addAsManifestResource(resURL, "ejb-jar.xml");
    }

    resURL = Client.class.getResource("assembly_compat_cocktail_compat9_10_jar2_ejb.jar.sun-ejb-jar.xml");
    if (resURL != null) {
      assembly_compat_cocktail_compat9_10_jar2_ejb.addAsManifestResource(resURL, "sun-ejb-jar.xml");
    }

    archiveProcessor.processEjbArchive(assembly_compat_cocktail_compat9_10_jar1_ejb, Client.class, resURL);



    JavaArchive assembly_compat_cocktail_compat9_10_client = ShrinkWrap.create(JavaArchive.class,
        "assembly_compat_cocktail_compat9_10_client.jar");
      assembly_compat_cocktail_compat9_10_client.addClasses(
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.tests.assembly.compat.cocktail.compat9_10.Client.class,
        com.sun.ts.tests.assembly.compat.cocktail.compat9_10.ReferencedBean.class,
        com.sun.ts.tests.assembly.compat.cocktail.compat9_10.ReferencedBeanEJB.class,   
        com.sun.ts.tests.assembly.util.shared.ejbref.common.ReferencedBeanCode.class,
        com.sun.ts.lib.util.RemoteLoggingInitException.class,
        com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class
        );
    // The application-client.xml descriptor
    resURL = Client.class.getResource("assembly_compat_cocktail_compat9_10_client.xml");
    if (resURL != null) {
      assembly_compat_cocktail_compat9_10_client.addAsManifestResource(resURL, "application-client.xml");
    }
    resURL = Client.class.getResource("assembly_compat_cocktail_compat9_10_client.jar.sun-application-client.xml");
    if (resURL != null) {
      assembly_compat_cocktail_compat9_10_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    assembly_compat_cocktail_compat9_10_client
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(assembly_compat_cocktail_compat9_10_client, Client.class, resURL);


    JavaArchive assembly_compat_cocktail_compat9_10_another_client = ShrinkWrap.create(JavaArchive.class,
        "assembly_compat_cocktail_compat9_10_another_client.jar");
        assembly_compat_cocktail_compat9_10_another_client.addClasses(
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.tests.assembly.compat.cocktail.compat9_10.Client.class,
        com.sun.ts.tests.assembly.compat.cocktail.compat9_10.ReferencedBean.class,
        com.sun.ts.tests.assembly.compat.cocktail.compat9_10.ReferencedBeanEJB.class,   
        com.sun.ts.tests.assembly.util.shared.ejbref.common.ReferencedBeanCode.class,
        com.sun.ts.lib.util.RemoteLoggingInitException.class,
        com.sun.ts.tests.common.ejb.wrappers.Stateless3xWrapper.class
        );
    // The application-client.xml descriptor
    resURL = Client.class.getResource("assembly_compat_cocktail_compat9_10_another_client.xml");
    if (resURL != null) {
      assembly_compat_cocktail_compat9_10_another_client.addAsManifestResource(resURL, "application-client.xml");
    }
    resURL = Client.class.getResource("assembly_compat_cocktail_compat9_10_another_client.jar.sun-application-client.xml");
    if (resURL != null) {
      assembly_compat_cocktail_compat9_10_another_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    assembly_compat_cocktail_compat9_10_another_client
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(assembly_compat_cocktail_compat9_10_another_client, Client.class, resURL);

    EnterpriseArchive assembly_compat_cocktail_compat9_10 = ShrinkWrap.create(EnterpriseArchive.class,
        "assembly_compat_cocktail_compat9_10.ear");
        assembly_compat_cocktail_compat9_10.addAsModule(assembly_compat_cocktail_compat9_10_jar1_ejb);
        assembly_compat_cocktail_compat9_10.addAsModule(assembly_compat_cocktail_compat9_10_jar2_ejb);
        assembly_compat_cocktail_compat9_10.addAsModule(assembly_compat_cocktail_compat9_10_client);
        assembly_compat_cocktail_compat9_10.addAsModule(assembly_compat_cocktail_compat9_10_another_client);

    URL earResURL = Client.class.getResource("application.xml");
    if (earResURL != null) {
      assembly_compat_cocktail_compat9_10.addAsManifestResource(earResURL, "application.xml");
    }
    archiveProcessor.processEarArchive(assembly_compat_cocktail_compat9_10, Client.class, earResURL);

    return assembly_compat_cocktail_compat9_10;
  }


  /**
   * @testName: test9DD
   *
   * @assertion_ids: JavaEE:SPEC:284
   *
   * @test_Strategy:
   *
   *                 Package an .ear file (Jakarta EE 10 application DD) with: - 1
   *                 ejb-jar file using a Jakarta EE 9 DD - 1 ejb-jar file using a
   *                 Jakarta EE 10 DD - 1 application client jar file using a Jakarta EE
   *                 9 DD - 1 application client jar file using a Jakarta EE 10 DD
   *
   *                 Deploy the .ear file.
   *
   *                 Run the 9 client and check that it can call a bean in
   *                 each ejb-jar at runtime.
   */
  @Test
  public void test9DD() throws Fault {
    ReferencedBean bean1 = null;
    ReferencedBean bean2 = null;
    String bean1Name;
    String bean2Name;
    boolean pass = false;

    try {
      TestUtil.logTrace("[Client] Looking up '" + bean1Lookup);
      bean1 = (ReferencedBean) nctx.lookup(bean1Lookup,
          ReferencedBean.class);
      bean1.createNamingContext();
      bean1.initLogging(props);
      bean1Name = bean1.whoAreYou();
      TestUtil.logTrace(bean1Lookup + "name is '" + bean1Name + "'");

      TestUtil.logTrace("[Client] Looking up '" + bean2Lookup);
      bean2 = (ReferencedBean) nctx.lookup(bean2Lookup,
          ReferencedBean.class);
      bean2.createNamingContext();
      bean2.initLogging(props);
      bean2Name = bean2.whoAreYou();
      TestUtil.logTrace(bean2Lookup + "name is '" + bean2Name + "'");

      pass = bean1Name.equals(bean1RefName) && bean2Name.equals(bean2RefName);
      if (!pass) {
        TestUtil.logErr("[Client] " + bean1Lookup + "name is '" + bean1Name
            + "' expected '" + bean1RefName + "'");

        TestUtil.logErr("[Client] " + bean2Lookup + "name is '" + bean2Name
            + "' expected '" + bean2RefName + "'");

        throw new Fault("compat cocktail test failed!");
      }
    } catch (Exception e) {
      throw new Fault("compat cocktail test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
