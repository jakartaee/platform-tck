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

package com.sun.ts.tests.assembly.compat.standalone.war.compat9_10;

import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;
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
  public static final String jspPropName = "compat_standalone_war_compat9_10";

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) throws Exception {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
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

  public Client() throws Exception {

  }

  static final String VEHICLE_ARCHIVE = "assembly_compat_standalone_war_compat9_10";

  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    JavaArchive assembly_compat_standalone_war_compat9_10_client = ShrinkWrap.create(JavaArchive.class,
        "assembly_compat_standalone_war_compat9_10_client.jar");
        assembly_compat_standalone_war_compat9_10_client.addClasses(
        Fault.class,
        EETest.class,
        SetupException.class,
        com.sun.ts.tests.assembly.compat.standalone.war.compat9_10.Client.class);
    // The application-client.xml descriptor
    URL resURL = Client.class.getResource("application-client.xml");
    if (resURL != null) {
      assembly_compat_standalone_war_compat9_10_client.addAsManifestResource(resURL, "application-client.xml");
    }
    resURL = Client.class.getResource("assembly_compat_standalone_war_compat9_10_client.jar.sun-application-client.xml");
    if(resURL != null) {
      assembly_compat_standalone_war_compat9_10_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    assembly_compat_standalone_war_compat9_10_client
        .addAsManifestResource(new StringAsset("Main-Class: " + Client.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(assembly_compat_standalone_war_compat9_10_client, Client.class, resURL);


    EnterpriseArchive assembly_compat_standalone_war_compat9_10_ear = ShrinkWrap.create(EnterpriseArchive.class,
        "assembly_compat_standalone_war_compat9_10.ear");
        assembly_compat_standalone_war_compat9_10_ear.addAsModule(assembly_compat_standalone_war_compat9_10_client);
    
        URL earResURL = Client.class.getResource("application.xml");
        if (earResURL != null) {
          assembly_compat_standalone_war_compat9_10_ear.addAsManifestResource(earResURL, "application.xml");
        }
    
    return assembly_compat_standalone_war_compat9_10_ear;
  }

  @Deployment(name = "assembly_compat_standalone_war_compat9_10_component_web", order = 1, testable = false)
  public static WebArchive createWarDeploymentVehicle(@ArquillianResource TestArchiveProcessor archiveProcessor) {

    WebArchive assembly_compat_standalone_war_compat9_10_component_web = ShrinkWrap.create(WebArchive.class,
        "assembly_compat_standalone_war_compat9_10_component_web.war");
    // The application-client.xml descriptor
    URL resURL = Client.class.getResource("web.xml");
    if (resURL != null) {
      assembly_compat_standalone_war_compat9_10_component_web.addAsWebInfResource(resURL, "web.xml");
    }
    URL jspURL = Client.class.getResource("test.jsp");
    if(jspURL != null) {
      assembly_compat_standalone_war_compat9_10_component_web.addAsWebResource(jspURL, "test.jsp");
    }


    resURL = Client.class.getResource("assembly_compat_standalone_war_compat9_10_component_web.war.sun-web.xml");
    if(resURL != null) {
      assembly_compat_standalone_war_compat9_10_component_web.addAsWebInfResource(resURL, "sun-web.xml");
    }
    archiveProcessor.processWebArchive(assembly_compat_standalone_war_compat9_10_component_web, Client.class, resURL);

    return assembly_compat_standalone_war_compat9_10_component_web;
  }

  /**
   * @testName: testStandaloneWar
   *
   * @assertion_ids: JavaEE:SPEC:261; JavaEE:SPEC:283; JavaEE:SPEC:284
   *
   * @test_Strategy: Package a war file containing a JSP using a Jakarta EE 9 DD
   *                 (assembly_compat_standalone_war_compat14_50_component_jsp.war).
   *
   *                 Package a .ear file (Jakarta EE 10 DD's) containing an
   *                 application client accessing the JSP packaged in the
   *                 stand-alone WAR module (URL resource factory).
   *
   *                 Deploy the WAR module and the .ear file.
   *
   *                 Run the client and check that we can access this JSP at
   *                 runtime.
   *
   */
  @Test
  @OperateOnDeployment("assembly_compat_standalone_war_compat9_10")
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
