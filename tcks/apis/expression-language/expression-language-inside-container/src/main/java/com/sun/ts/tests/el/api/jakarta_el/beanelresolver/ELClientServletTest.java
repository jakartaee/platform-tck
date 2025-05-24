/*
 * Copyright (c) 2009, 2021 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.beanelresolver;

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
import jakarta.el.BeanELResolver;
import jakarta.el.ELContext;
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
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.TimeZone;
import java.util.stream.Collectors;

@ExtendWith(ArquillianExtension.class)
@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientServletTest extends ServiceEETest {

  static final String VEHICLE_ARCHIVE = "beanelresolver_servlet_vehicle";

  private static final Logger logger = System.getLogger(ELClientServletTest.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
      // this.sb = new SimpleBean();
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }


  public static String inputStreamToString(InputStream inStream) throws IOException {
    try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(inStream, StandardCharsets.UTF_8))) {
      return bufReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }
  }

  public static String editWebXmlString(InputStream inStream, String servlet_vehicle) throws IOException{
    return inputStreamToString(inStream).replaceAll("el_servlet_vehicle", servlet_vehicle);
  }


  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static WebArchive createDeploymentVehicle() throws IOException {

    WebArchive servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "beanelresolver_servlet_vehicle_web.war");
  
    servlet_vehicle_web.addClasses(
        Fault.class,
        SetupException.class,
        EETest.class,
        ServiceEETest.class,
        com.sun.ts.tests.common.el.api.resolver.BarELResolver.class,
        com.sun.ts.tests.common.el.api.resolver.ResolverTest.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
        com.sun.ts.tests.el.api.jakarta_el.beanelresolver.ELClientServletTest.class,
        com.sun.ts.tests.el.common.elcontext.BareBonesELContext.class,
        com.sun.ts.tests.el.common.util.SimpleBean.class,
        com.sun.ts.tests.el.common.util.SimpleInterface.class,
        com.sun.ts.tests.el.common.util.ELTestUtil.class
    );

    
    InputStream inStream = ELClientServletTest.class.getResourceAsStream("/vehicle/servlet/servlet_vehicle_web.xml");

    // Replace the el_servlet_vehicle in servlet_vehicle_web.xml with the servlet_vehicle name for archive
    String webXml = editWebXmlString(inStream, VEHICLE_ARCHIVE);
    servlet_vehicle_web.setWebXML(new StringAsset(webXml));

    return servlet_vehicle_web;
    /*
    EnterpriseArchive servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "beanelresolver_servlet_vehicle.ear");
    servlet_vehicle_ear.addAsModule(servlet_vehicle_web);
    return servlet_vehicle_ear;
    */

  }

  public ELClientServletTest() {
    this.sb = new SimpleBean();
  }

  private SimpleBean sb;

  private Properties testProps;

  public static void main(String[] args) {
    ELClientServletTest theTests = new ELClientServletTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
    sb = new SimpleBean();
  }

  @AfterEach
  public void cleanup() throws Exception {
    TestUtil.logTrace("Cleanup method called");
  }

  /**
   * @testName: beanELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16
   * 
   * @test_Strategy: Verify that API calls work as expected: beanELResolver()
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    sb.setIntention("initial_setting");

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, beanResolver, sb, "intention",
          "ABC123", buf, false);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: beanELResolverDefaultMethodReadOnlyTest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16
   * 
   * @test_Strategy: Verify that API calls work as expected when accessing
   *                 read-only properties defined via a default interface method
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverDefaultMethodReadOnlyTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, beanResolver, sb, "defaultRO",
         "RO", buf, true);
    } catch (Exception ex) {
      pass = false;
      TestUtil.logErr("Test of a valid expression using a Bean with a read-only " +
          "property implemented via default methods threw an Exception!" + TestUtil.NEW_LINE +
          "Received: " + ex.toString() + TestUtil.NEW_LINE);

      ex.printStackTrace();
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  
  /**
   * @testName: beanELResolverDefaultMethodReadWriteTest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16
   * 
   * @test_Strategy: Verify that API calls work as expected when accessing
   *                 writable properties defined via a default interface method
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverDefaultMethodReadWriteTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, beanResolver, sb, "defaultRW",
          "RW", buf, false);
    } catch (Exception ex) {
      pass = false;
      TestUtil.logErr("Test of a valid expression using a Bean with a read-write " +
          "property implemented via default methods threw an Exception!" + TestUtil.NEW_LINE +
          "Received: " + ex.toString() + TestUtil.NEW_LINE);

      ex.printStackTrace();
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: beanELResolverInvokeTest
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16; EL:JAVADOC:142
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanELResolver.invoke().
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverInvokeTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Doug", "Donahue" };

      pass = ResolverTest.testELResolverInvoke(context, beanResolver, sb,
          "isName", types, values, Boolean.FALSE, buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());

  }// End beanELResolverInvokeTest

  /**
   * @testName: beanELResolverInvokeVoidTest
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16
   * @test_Strategy: Verify that the ELResolver.invoke() API calls work as
   *                 expected when calling a method that returns void.
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverInvokeVoidTest() throws Exception {

    boolean pass;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Peter", "Pan" };

      Object result = beanResolver.invoke(context, sb, "setFullName", types,
          values);

      if (null == result) {
        // validate the new values.
        pass = ResolverTest.testELResolverInvoke(context, beanResolver, sb,
            "isName", types, values, Boolean.FALSE, buf);
      } else {
        pass = false;
        buf.append("Unexpected Value returned!" + TestUtil.NEW_LINE
            + "Expected: null" + TestUtil.NEW_LINE + "Recieved: "
            + result.getClass().getName());
      }

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());

  }// End beanELResolverInvokeVoidTest

  /**
   * @testName: beanELResolverInvokeMNFETest
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:11; EL:JAVADOC:12; EL:JAVADOC:13;
   *                 EL:JAVADOC:14; EL:JAVADOC:15; EL:JAVADOC:16; EL:JAVADOC:143
   * @test_Strategy: Verify that the invoke() method throws
   *                 MethodNotFoundException if no suitable method can be found.
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverInvokeMNFETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      Class<?>[] types = { String.class, String.class };
      String[] values = { "Doug", "Donahue" };

      pass = ResolverTest.testELResolverInvoke(context, beanResolver, sb,
          "bogus_Method", types, values, Boolean.TRUE, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());

  }// End beanELResolverInvokeMNFETest

  /**
   * @testName: beanELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:13; EL:JAVADOC:14; EL:JAVADOC:15;
   *                 EL:JAVADOC:16; EL:JAVADOC:136; EL:JAVADOC:139;
   *                 EL:JAVADOC:145; EL:JAVADOC:148
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   * 
   *                 getType() getValue() isReadOnly() setValue()
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverNPETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    sb.setIntention("initial_setting");

    BeanELResolver resolver = new BeanELResolver();

    try {
      pass = ResolverTest.testELResolverNPE(resolver, sb, "intention", "billy",
          buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: beanELResolverPNFETest
   * 
   * @assertion_ids: EL:JAVADOC:9; EL:JAVADOC:13; EL:JAVADOC:14; EL:JAVADOC:15;
   *                 EL:JAVADOC:16; EL:JAVADOC:137; EL:JAVADOC:140;
   *                 EL:JAVADOC:146; EL:JAVADOC:149
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 PropertyNotFoundException, if the base is not null and the
   *                 specified property does not exist.
   * 
   *                 getType() isReadOnly() setValue() getValue()
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverPNFETest() throws Exception {

    boolean pass = true;

    StringBuffer buf = new StringBuffer();
    sb.setIntention("initial_setting");

    BeanELResolver resolver = new BeanELResolver();
    BareBonesELContext barebonesComtext = new BareBonesELContext();
    ELContext context = barebonesComtext.getELContext();

    try {
      pass = ResolverTest.testELResolverPNFE(context, resolver, sb,
          "Bogus_Field", buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: beanELResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:10; EL:JAVADOC:16; EL:JAVADOC:150
   * 
   * @test_Strategy: Verify that the single-parameter constructor for
   *                 beanELResolver can be used to construct a read-only
   *                 resolver, and that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 BeanELResolver(boolean) setValue()
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverPNWETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    BeanELResolver resolver = new BeanELResolver(true);
    BareBonesELContext barebonesComtext = new BareBonesELContext();
    ELContext context = barebonesComtext.getELContext();

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
  
  /**
   * @testName: beanELResolverMethodVisibilityTest
   * 
   * @test_Strategy: Verify that API calls work as expected for a property that is not visible via the implementing
   *                 class (it is in an internal, non-exported class) but is visible via an interface method:
   *                 beanELResolver() getValue() getType() setValue() isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */
  @Test
  @TargetVehicle("servlet")
  public void beanELResolverMethodVisibilityTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();
    TimeZone tz = TimeZone.getDefault();
    
    try {
      BeanELResolver beanResolver = new BeanELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, beanResolver, tz, "rawOffset", Integer.valueOf(0), buf, false);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }
}
