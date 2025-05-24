/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.el.api.jakarta_el.compositeelresolver;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;
import jakarta.el.ArrayELResolver;
import jakarta.el.BeanELResolver;
import jakarta.el.CompositeELResolver;
import jakarta.el.ELContext;
import jakarta.el.ELManager;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.protocol.common.TargetVehicle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

@ExtendWith(ArquillianExtension.class)
@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientJspTest extends ServiceEETest {

  static final String VEHICLE_ARCHIVE = "compositeelresolver_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static WebArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "compositeelresolver_jsp_vehicle_web.war");
  
    jsp_vehicle_web.addClasses(
        Fault.class,
        SetupException.class,
        EETest.class,
        ServiceEETest.class,
        com.sun.ts.tests.common.el.api.resolver.BarELResolver.class,
        com.sun.ts.tests.common.el.api.resolver.ResolverTest.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.el.common.elcontext.BareBonesELContext.class,
        com.sun.ts.tests.el.common.util.ELTestUtil.class,
        com.sun.ts.tests.el.common.util.SimpleBean.class,
        com.sun.ts.tests.el.common.util.SimpleInterface.class,
        com.sun.ts.tests.el.api.jakarta_el.compositeelresolver.ELClientJspTest.class
    );

    InputStream inStream = ELClientJspTest.class.getResourceAsStream("/vehicle/jsp/jsp_vehicle_web.xml");

    // Replace the el_jsp_vehicle in jsp_vehicle_web.xml with the jsp_vehicle name for archive
    String webXml = editWebXmlString(inStream, VEHICLE_ARCHIVE);
    jsp_vehicle_web.setWebXML(new StringAsset(webXml));

    // Web content
    URL warResURL = ELClientJspTest.class.getResource("/vehicle/jsp/contentRoot/client.html");
    jsp_vehicle_web.addAsWebResource(warResURL, "/client.html");
    warResURL = ELClientJspTest.class.getResource("/vehicle/jsp/contentRoot/jsp_vehicle.jsp");
    jsp_vehicle_web.addAsWebResource(warResURL, "/jsp_vehicle.jsp");

    return jsp_vehicle_web;
    /*
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "compositeelresolver_jsp_vehicle.ear");
    jsp_vehicle_ear.addAsModule(jsp_vehicle_web);
    return jsp_vehicle_ear;
    */

  }

  public static String inputStreamToString(InputStream inStream) throws IOException {
    try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))) {
      return bufReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
  }

  public static String editWebXmlString(InputStream inStream, String servlet_vehicle) throws IOException{
    return inputStreamToString(inStream).replaceAll("el_jsp_vehicle", servlet_vehicle);
  }
  
  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  private static final String[] names = { "doug", "nick", "roger", "ryan",
      "ed" };

  private Properties testProps;

  public static void main(String[] args) {
    ELClientJspTest theTests = new ELClientJspTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  @AfterEach
  public void cleanup() throws Exception {
    // does nothing at this point
  }

  /**
   * @testName: compositeELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:26; EL:JAVADOC:27;
   *                 EL:JAVADOC:28; EL:JAVADOC:29; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:34
   * 
   * @test_Strategy: Verify that API calls work as expected:
   *                 CompositeELResolver() add() getValue() getType() setValue()
   *                 isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors() ELContext.getELResolver()
   */
  @Test
  @TargetVehicle("jsp")
  public void  compositeELResolverTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create the CompositeELResolver.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      pass = ResolverTest.testCompositeELResolver(context, compResolver, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: compositeELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:181
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanELResolver.invoke().
   */
  @Test
  @TargetVehicle("jsp")
  public void  compositeELResolverInvokeTest() throws Exception {
    SimpleBean sb = new SimpleBean();
    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      ELManager elm = new ELManager();
      ELContext context = elm.getELContext();

      // Create the CompositeELResolver.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      compResolver.add(beanResolver);

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Doug", "Donahue" };

      pass = ResolverTest.testELResolverInvoke(context, compResolver, sb,
          "isName", types, values, false, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());

  }// End beanELResolverInvokeTest

  /**
   * @testName: compositeELResolverAddNPETest
   * 
   * @assertion_ids: EL:JAVADOC:24; EL:JAVADOC:25; EL:JAVADOC:174
   * 
   * @test_Strategy: Verify that the CompositeELResolver.add method throws a
   *                 NullPointerException if the provided resolver is null.
   */
  @Test
  @TargetVehicle("jsp")
  public void  compositeELResolverAddNPETest() throws Exception {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();

      compResolver.add(null);

      buf.append(TestUtil.NEW_LINE + "Tested Failed, CompositeResolver.add(), "
          + "should have thrown NullPointerException!");
      pass = false;

    } catch (NullPointerException npe) {
      TestUtil.logMsg("Expected Exception thrown, when providing a"
          + " null resolver to Composite.add()");

    } catch (Exception e) {
      buf.append("test failed with: " + TestUtil.NEW_LINE + "EXPECTED: "
          + "NullPointerException to be thrown " + TestUtil.NEW_LINE
          + "RECEIVED: " + e.toString() + "" + TestUtil.NEW_LINE);
      pass = false;
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: compositeELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:26; EL:JAVADOC:27; EL:JAVADOC:28;
   *                 EL:JAVADOC:29; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:175; EL:JAVADOC:178; EL:JAVADOC:175;
   *                 EL:JAVADOC:182; EL:JAVADOC:185
   * 
   * @test_Strategy: Verify that API calls work as expected: getValue()
   *                 getType() setValue() isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */
  @Test
  @TargetVehicle("jsp")
  public void  compositeELResolverNPETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create dummy ELResolvers to populate the CompositeELResolver.
      ArrayELResolver aResolver = new ArrayELResolver();
      BeanELResolver bResolver = new BeanELResolver();

      // Create the CompositeELResolver & add the dummy ELResolvers to it.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();
      compResolver.add(aResolver);
      compResolver.add(bResolver);

      pass = ResolverTest.testELResolverNPE(compResolver, names,
          Integer.valueOf(2), "steve", buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: compositeELResolverPNFETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:28; EL:JAVADOC:30; EL:JAVADOC:31;
   *                 EL:JAVADOC:176; EL:JAVADOC:179; EL:JAVADOC:183;
   *                 EL:JAVADOC:186
   * 
   * @test_Strategy: Verify that API calls throw PropertyNotFoundException as
   *                 expected: getType() setValue() isReadOnly()
   */
  @Test
  @TargetVehicle("jsp")
  public void  compositeELResolverPNFETest() throws Exception {

    boolean pass = true;
    StringBuffer buf = new StringBuffer();

    try {
      BareBonesELContext compContext = new BareBonesELContext();
      ELContext context = compContext.getELContext();

      // Create an ArrayELResolver to populate the CompositeELResolver.
      ArrayELResolver aResolver = new ArrayELResolver();

      // Create the CompositeELResolver & add the ELResolver to it.
      CompositeELResolver compResolver = (CompositeELResolver) context
          .getELResolver();
      compResolver.add(aResolver);

      pass = ResolverTest.testELResolverPNFE(context, compResolver, names,
          Integer.valueOf(10), buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: compositeELResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:25; EL:JAVADOC:187
   * 
   * @test_Strategy: Verify that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 setValue()
   */
  @Test
  @TargetVehicle("jsp")
  public void  compositeELResolverPNWETest() throws Exception {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    SimpleBean sb = new SimpleBean();

    BareBonesELContext compContext = new BareBonesELContext();
    ELContext context = compContext.getELContext();

    // Create an ArrayELResolver to populate the CompositeELResolver.
    BeanELResolver resolver = new BeanELResolver(true);

    // Create the CompositeELResolver & add the ELResolver to it.
    CompositeELResolver compResolver = (CompositeELResolver) context
        .getELResolver();
    compResolver.add(resolver);

    try {
      pass = ResolverTest.testELResolverPNWE(context, resolver, sb, "intention",
          "billy", buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

}
