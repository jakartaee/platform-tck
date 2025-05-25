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

package com.sun.ts.tests.el.api.jakarta_el.resourcebundleelresolver;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import jakarta.el.ELContext;
import jakarta.el.ResourceBundleELResolver;
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
import java.io.Serializable;
import java.lang.System.Logger;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

@ExtendWith(ArquillianExtension.class)
@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientServletTest extends ServiceEETest {

  static final String VEHICLE_ARCHIVE = "resourcebundleelresolver_servlet_vehicle";

  private static final Logger logger = System.getLogger(ELClientServletTest.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
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

    WebArchive servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "resourcebundleelresolver_servlet_vehicle_web.war");
  
    servlet_vehicle_web.addClasses(
        Fault.class,
        SetupException.class,
        EETest.class,
        ServiceEETest.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class,
        com.sun.ts.tests.common.el.api.expression.ExpressionTest.class,
        com.sun.ts.tests.common.el.api.resolver.BarELResolver.class,
        com.sun.ts.tests.common.el.api.resolver.ResolverTest.class,
        com.sun.ts.tests.el.common.elcontext.BareBonesELContext.class,
        com.sun.ts.tests.el.common.elcontext.SimpleELContext.class,
        com.sun.ts.tests.el.common.elcontext.SimpleELContext.Resolver.class,
        com.sun.ts.tests.el.common.elcontext.FuncMapperELContext.class,
        com.sun.ts.tests.el.common.elcontext.BarELContext.class,
        com.sun.ts.tests.el.common.util.ELTestUtil.class,
        com.sun.ts.tests.el.common.util.SimpleBean.class,
        com.sun.ts.tests.el.common.util.SimpleInterface.class,
        com.sun.ts.tests.el.common.util.ResolverType.class,
        com.sun.ts.tests.el.common.util.NameValuePair.class,
        com.sun.ts.tests.el.common.util.ExprEval.class,
        com.sun.ts.tests.el.common.functionmapper.TCKFunctionMapper.class,
        com.sun.ts.tests.el.common.elresolver.EmployeeELResolver.class,
        com.sun.ts.tests.el.common.elresolver.VariableELResolver.class,
        com.sun.ts.tests.el.common.elresolver.VectELResolver.class,
        com.sun.ts.tests.el.common.elresolver.FunctionELResolver.class,
        com.sun.ts.tests.el.api.jakarta_el.resourcebundleelresolver.ELClientServletTest.class,
        com.sun.ts.tests.el.api.jakarta_el.resourcebundleelresolver.ELClientServletTest.TckResourceBundle.class
    );

    
    InputStream inStream = ELClientServletTest.class.getResourceAsStream("/vehicle/servlet/servlet_vehicle_web.xml");

    // Replace the el_servlet_vehicle in servlet_vehicle_web.xml with the servlet_vehicle name for archive
    String webXml = editWebXmlString(inStream, VEHICLE_ARCHIVE);
    servlet_vehicle_web.setWebXML(new StringAsset(webXml));

    return servlet_vehicle_web;
    /*
    EnterpriseArchive servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "resourcebundleelresolver_servlet_vehicle.ear");
    servlet_vehicle_ear.addAsModule(servlet_vehicle_web);
    return servlet_vehicle_ear;
    */

  }

  private Properties testProps;

  private TckResourceBundle tckrb;

  public static void main(String[] args) {
    ELClientServletTest theTests = new ELClientServletTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
    tckrb = new TckResourceBundle();
  }

  @AfterEach
  public void cleanup() throws Exception {
  }

  /**
   * @testName: resourceBundleELResolverTest
   *
   * @assertion_ids: EL:JAVADOC:103; EL:JAVADOC:104; EL:JAVADOC:105;
   *                 EL:JAVADOC:106; EL:JAVADOC:107; EL:JAVADOC:108;
   *                 EL:JAVADOC:109
   *
   * @test_Strategy: Verify that API calls work as expected:
   *                 ResourceBundleELResolver() setValue() getValue() getType()
   *                 isReadOnly() getCommonPropertyType()
   *                 getFeatureDescriptors()
   */
  @Test
  @TargetVehicle("servlet")
  public void resourceBundleELResolverTest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    try {
      ResourceBundleELResolver rsbResolver = new ResourceBundleELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, rsbResolver, tckrb, "Dinner",
          "DINNER", buf, true);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: resourceBundleELResolverNPETest
   *
   * @assertion_ids: EL:JAVADOC:106; EL:JAVADOC:105; EL:JAVADOC:107;
   *                 EL:JAVADOC:108; EL:JAVADOC:109; EL:JAVADOC:313;
   *                 EL:JAVADOC:314; EL:JAVADOC:316; EL:JAVADOC:317
   *
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   *
   *                 getValue() getType() setValue() isReadOnly()
   */
  @Test
  @TargetVehicle("servlet")
  public void resourceBundleELResolverNPETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    ResourceBundleELResolver resolver = new ResourceBundleELResolver();

    try {
      pass = ResolverTest.testELResolverNPE(resolver, tckrb, "brunch", "BRUNCH",
          buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: resourceBundleELResolverPNWETest
   *
   * @assertion_ids: EL:JAVADOC:108; EL:JAVADOC:109; EL:JAVADOC:318
   *
   * @test_Strategy: Verify that the following methods throw a
   *                 PropertyNotWritableException, since
   *                 ResourceBundleELResolvers are non-writable.
   *
   *                 setValue()
   */
  @Test
  @TargetVehicle("servlet")
  public void resourceBundleELResolverPNWETest() throws Exception {

    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    BareBonesELContext barebonesContext = new BareBonesELContext();
    ELContext context = barebonesContext.getELContext();
    ResourceBundleELResolver rsbResolver = new ResourceBundleELResolver();

    try {
      pass = ResolverTest.testELResolverPNWE(context, rsbResolver, tckrb,
          "snack", "SNACK", buf);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass)
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    TestUtil.logTrace(buf.toString());
  }

  private static class TckResourceBundle extends ResourceBundle
      implements Serializable {

    private static final String KEYS = "Breakfast Lunch Dinner";

    public Object handleGetObject(String key) {
      if ("Breakfast".equals(key))
        return "BREAKFAST";
      if ("Lunch".equals(key))
        return "LUNCH";
      if ("Dinner".equals(key))
        return "DINNER";

      return null;
    }

    public Enumeration getKeys() {
      StringTokenizer keyTokenizer = new StringTokenizer(KEYS);

      return keyTokenizer;

    }
  }
}
