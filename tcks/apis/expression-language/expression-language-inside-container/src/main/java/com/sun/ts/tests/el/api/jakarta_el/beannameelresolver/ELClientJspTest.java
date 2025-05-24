/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.beannameelresolver;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.SimpleBean;
import jakarta.el.BeanNameELResolver;
import jakarta.el.BeanNameResolver;
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

  static final String VEHICLE_ARCHIVE = "beannameelresolver_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static WebArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "beannameelresolver_jsp_vehicle_web.war");
  
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
        com.sun.ts.tests.el.api.jakarta_el.beannameelresolver.ELClientJspTest.class,
        com.sun.ts.tests.el.api.jakarta_el.beannameelresolver.ELClientJspTest.TCKBeanNameResolver.class
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
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "beannameelresolver_jsp_vehicle.ear");
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
    TestUtil.logTrace("Cleanup method called");
  }

  /**
   * @testName: beanNameELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:152; EL:JAVADOC:153; EL:JAVADOC:154;
   *                 EL:JAVADOC:155; EL:JAVADOC:158; EL:JAVADOC:161;
   *                 EL:JAVADOC:164
   * 
   * @test_Strategy: Verify the following method calls work as expected:
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  @TargetVehicle("jsp")
  public void  beanNameELResolverTest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    try {
      pass = ResolverTest.testELResolver(context, context.getELResolver(),
          bnr.getBean("simpleBean"), "intention", "GOLDEN", buf, false);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }

    TestUtil.logMsg(buf.toString());
  }

  /**
   * @testName: beanNameELResolverInvokeMNFETest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16;
   *                 EL:JAVADOC:143; EL:JAVADOC:237
   * 
   * @test_Strategy: Verify that the invoke() method throws
   *                 MethodNotFoundException if no suitable method can be found.
   */
  @Test
  @TargetVehicle("jsp")
  public void  beanNameELResolverInvokeMNFETest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class, String.class };
    String[] values = { "Doug", "Donahue" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new SimpleBean(), "bogue_method", types, values, true, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logMsg(buf.toString());

  }// End beanELResolverInvokeMNFETest

  /**
   * @testName: beanNameELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16;
   *                 EL:JAVADOC:142; EL:JAVADOC:199; EL:JAVADOC:200;
   *                 EL:JAVADOC:202; EL:JAVADOC:203; EL:JAVADOC:236
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanNameELResolver.invoke().
   */
  @Test
  @TargetVehicle("jsp")
  public void  beanNameELResolverInvokeTest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    elm.addBeanNameResolver(bnr);
    elm.addELResolver(belr);
    elm.defineBean("simpleBean", new SimpleBean());

    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class, String.class };
    String[] values = { "Doug", "Donahue" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new SimpleBean(), "isName", types, values, false, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logMsg(buf.toString());

  }// End beanELResolverInvokeTest

  /**
   * @testName: beanNameELResolverGetTypeNPETest
   * 
   * @assertion_ids: EL:JAVADOC:156
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  @Test
  @TargetVehicle("jsp")
  public void  beanNameELResolverGetTypeNPETest() throws Exception {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "getType",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverGetTypeNPETest

  /**
   * @testName: beanNameELResolverGetValueNPETest
   * 
   * @assertion_ids: EL:JAVADOC:159
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  @Test
  @TargetVehicle("jsp")
  public void  beanNameELResolverGetValueNPETest() throws Exception {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "getValue",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverGetValueNPETest

  /**
   * @testName: beanNameELResolverIsReadOnlyNPETest
   * 
   * @assertion_ids: EL:JAVADOC:162
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  @Test
  @TargetVehicle("jsp")
  public void  beanNameELResolverIsReadOnlyNPETest() throws Exception {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "isReadOnly",
        new Class<?>[] { ELContext.class, Object.class, Object.class },
        new Object[] { null, "Ender", "Wiggins" });

  }// End beanNameELResolverIsReadOnlyNPETest

  /**
   * @testName: beanNameELResolverSetValueNPETest
   * 
   * @assertion_ids: EL:JAVADOC:165
   * 
   * @test_Strategy: Validate a NullPointerException is thrown if context is
   *                 null.
   */
  @Test
  @TargetVehicle("jsp")
  public void  beanNameELResolverSetValueNPETest() throws Exception {
    BeanNameResolver bnr = new TCKBeanNameResolver();
    BeanNameELResolver belr = new BeanNameELResolver(bnr);

    ELTestUtil.checkForNPE(belr, "setValue",
        new Class<?>[] { ELContext.class, Object.class, Object.class,
            Object.class },
        new Object[] { null, "Ender", "Valintine", "Wiggins" });

  }// End beanNameELResolverSetValueNPETest

  // -------------------------------------------- private classes

  private static class TCKBeanNameResolver extends BeanNameResolver {

  }
}
