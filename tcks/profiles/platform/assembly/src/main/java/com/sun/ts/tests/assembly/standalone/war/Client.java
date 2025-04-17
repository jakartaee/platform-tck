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

package com.sun.ts.tests.assembly.standalone.war;

import java.net.URL;
import java.net.URLConnection;
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
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.lang.System.Logger;

@Tag("assembly")
@Tag("platform")
@Tag("tck-appclient")
@ExtendWith(ArquillianExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class Client extends EETest {

  /** Name we use to lookup the URL */
  public static final String urlLookup = "java:comp/env/url/myURL";

  /** Name of the property set by the JSP */
  public static final String jspPropName = "standalone_war";

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      TestUtil.logTrace("[Client] Getting Naming Context...");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      throw new Fault("Setup failed:" + e, e);
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

  static final String VEHICLE_ARCHIVE = "assembly_standalone_war";

  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createEarDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive assembly_standalone_war_client = ShrinkWrap.create(JavaArchive.class,
        "assembly_standalone_war_client.jar");
        assembly_standalone_war_client.addClasses(
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.tests.assembly.standalone.war.Client.class);
    // The application-client.xml descriptor
    URL resURL = Client.class.getResource("assembly_standalone_war_client.xml");
    if (resURL != null) {
      assembly_standalone_war_client.addAsManifestResource(resURL, "application-client.xml");
    }
    resURL = Client.class.getResource("assembly_standalone_war_client.jar.sun-application-client.xml");
    if(resURL != null) {
      assembly_standalone_war_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    assembly_standalone_war_client
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(assembly_standalone_war_client, Client.class, resURL);



    EnterpriseArchive assembly_standalone_war_ear = ShrinkWrap.create(EnterpriseArchive.class,
        "assembly_standalone_war.ear");
        assembly_standalone_war_ear.addAsModule(assembly_standalone_war_client);
    // archiveProcessor.processEarArchive(assembly_standalone_war_ear, Client.class, resURL);

    return assembly_standalone_war_ear;
  }

  @Deployment(name = "assembly_standalone_war_component_web", order = 1, testable = false)
  public static WebArchive createWarDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    WebArchive assembly_standalone_war_component_web = ShrinkWrap.create(WebArchive.class,
        "assembly_standalone_war_component_web.war");
        assembly_standalone_war_component_web.addClasses(
        com.sun.ts.tests.common.web.WebUtil.class, 
        com.sun.ts.tests.common.web.JSPBeanWrapper.class);
    // The application-client.xml descriptor
    URL resURL = Client.class.getResource("assembly_standalone_war_component_web.xml");
    if (resURL != null) {
      assembly_standalone_war_component_web.addAsWebInfResource(resURL, "web.xml");
    }
    URL jspURL = Client.class.getResource("test.jsp");
    if(jspURL != null) {
      assembly_standalone_war_component_web.addAsWebResource(jspURL, "test.jsp");
    }

    resURL = Client.class.getResource("assembly_standalone_war_component_web.war.sun-web.xml");
    if(resURL != null) {
      assembly_standalone_war_component_web.addAsWebInfResource(resURL, "sun-web.xml");
    }
    assembly_standalone_war_component_web
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processWebArchive(assembly_standalone_war_component_web, Client.class, resURL);

    return assembly_standalone_war_component_web;
  }

  /**
   * @testName: testStandaloneWar
   *
   * @assertion_ids: JavaEE:SPEC:261
   *
   * @test_Strategy: Package a war file containing a JSP
   *                 (assembly_standalone_war_component_jsp.jar).
   *
   *                 Package a .ear file containing an application client
   *                 accessing the JSP packaged in the stand-alone WAR module
   *                 (URL resource factory).
   *
   *                 Deploy the WAR module and the .ear file.
   *
   *                 Run the client and check that we can access this JSP at
   *                 runtime.
   *
   */
  @Test
  @OperateOnDeployment("assembly_standalone_war")
  public void testStandaloneWar() throws Fault {
    boolean pass = false;
    String value;
    URL myUrl;
    URLConnection urlConnection;
    Properties jspProps;

    try {
      TestUtil.logTrace("[Client] looking up " + urlLookup);
      myUrl = (java.net.URL) nctx.lookup(urlLookup);
      TestUtil.logTrace("[Client] get a new URL connection...");
      urlConnection = myUrl.openConnection();

      jspProps = TestUtil.getResponseProperties(urlConnection);
      value = jspProps.getProperty(jspPropName);
      pass = (null != value) && value.equals("true");

      if (!pass) {
        throw new Fault("Standalone war test failed: " + jspPropName + " = "
            + ((null == value) ? "null" : value) + ", expected 'true'!");
      }
    } catch (Exception e) {
      logErr("[Client] Stand-alone test failed: " + e);
      throw new Fault("Stand-alone test failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
