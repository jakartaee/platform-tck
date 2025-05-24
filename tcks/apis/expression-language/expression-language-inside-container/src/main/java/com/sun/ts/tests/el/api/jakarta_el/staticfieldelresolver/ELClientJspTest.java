/*
 * Copyright (c) 2012, 2021 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.staticfieldelresolver;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import jakarta.el.ELClass;
import jakarta.el.ELContext;
import jakarta.el.ELManager;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.PropertyNotWritableException;
import jakarta.el.StaticFieldELResolver;
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

  static final String VEHICLE_ARCHIVE = "staticfieldelresolver_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static WebArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "staticfieldelresolver_jsp_vehicle_web.war");
  
    jsp_vehicle_web.addClasses(
        Fault.class,
        SetupException.class,
        EETest.class,
        ServiceEETest.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
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
        com.sun.ts.tests.el.api.jakarta_el.staticfieldelresolver.TCKELClass.class,
        com.sun.ts.tests.el.api.jakarta_el.staticfieldelresolver.ELClientJspTest.class
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
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "staticfieldelresolver_jsp_vehicle.ear");
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
   * @testName: staticFieldELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:330; EL:JAVADOC:331; EL:JAVADOC:332;
   *                 EL:JAVADOC:335; EL:JAVADOC:338; EL:JAVADOC:341;
   *                 EL:JAVADOC:343; EL:JAVADOC:346; EL:JAVADOC:189;
   *                 EL:JAVADOC:204
   * 
   * 
   * @test_Strategy: Verify the following method calls work as expected:
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType()
   */
  @Test
  @TargetVehicle("jsp")
  public void  staticFieldELResolverTest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = true;

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);
    ELContext context = elm.getELContext();

    Object base = new ELClass(TCKELClass.class);
    Object property = "firstName";
    Object value = "Ender";

    // setValue()
    context.setPropertyResolved(false);
    try {
      resolver.setValue(context, base, property, value);
      buf.append(ELTestUtil.FAIL + TestUtil.NEW_LINE
          + "Expected PropertyNotWritableException to be "
          + "thrown when calling setValue()!" + TestUtil.NEW_LINE
          + "No exception was thown!" + TestUtil.NEW_LINE);
      pass = false;

    } catch (PropertyNotWritableException pnwe) {
      buf.append(ELTestUtil.PASS + TestUtil.NEW_LINE
          + "PropertyNotWritableException Thrown as expected for "
          + "setValue()!" + TestUtil.NEW_LINE);
    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + TestUtil.NEW_LINE
          + "Wrong Exception Thrownfor setValue()!" + TestUtil.NEW_LINE
          + "Expected: PropertyNotWritableException" + TestUtil.NEW_LINE
          + "Received: " + e.getClass().getSimpleName() + TestUtil.NEW_LINE);
    }

    // getValue()
    context.setPropertyResolved(false);
    Object valueRetrieved = resolver.getValue(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("getValue() did not resolve" + TestUtil.NEW_LINE);
      pass = false;
    }

    if (valueRetrieved != value) {
      buf.append("Invalid value from getValue():" + TestUtil.NEW_LINE
          + "Value expected: " + value.toString() + TestUtil.NEW_LINE
          + "Value retrieved: " + valueRetrieved.toString()
          + TestUtil.NEW_LINE);
      pass = false;
    }

    // getType()
    context.setPropertyResolved(false);
    Class<?> type = resolver.getType(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("getType() did not resolve" + TestUtil.NEW_LINE);
      pass = false;
    } else if (type != null) {
      buf.append("getType() returns " + type.getName() + " rather than null" + TestUtil.NEW_LINE);
      pass = false;
    } else {
      buf.append("getType() returns null" + TestUtil.NEW_LINE + "as expected." + TestUtil.NEW_LINE);
    }

    // isReadOnly
    context.setPropertyResolved(false);
    boolean nonWritable = resolver.isReadOnly(context, base, property);
    if (!context.isPropertyResolved()) {
      buf.append("isReadOnly() did not resolve" + TestUtil.NEW_LINE);
      pass = false;

    } else if (!nonWritable) {
      buf.append("isReadOnly() returned unexpected value: " + TestUtil.NEW_LINE
          + "Expected: false" + TestUtil.NEW_LINE + "Received: " + nonWritable
          + TestUtil.NEW_LINE);
      pass = false;

    } else {
      buf.append("isReadOnly() returns false as expected" + TestUtil.NEW_LINE);
    }

    // getCommonPropertyType()
    context.setPropertyResolved(false);
    Class<?> commonPropertyType = (resolver.getCommonPropertyType(context,
        base));
    buf.append("getCommonPropertyType() returns " + commonPropertyType.getName()
        + TestUtil.NEW_LINE);

    // getFeatureDescriptors() commenting below as the method is deprecated in EL 6.0
    // context.setPropertyResolved(false);
    // Iterator<?> i = resolver.getFeatureDescriptors(context, base);

    // if (i == null) {
    //   buf.append("getFeatureDescriptors() returns null" + TestUtil.NEW_LINE);
    // }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + TestUtil.NEW_LINE + buf.toString());
    }

  } // End staticFieldELResolverTest

  /**
   * @testName: staticFieldResolverInvokeMNFETest
   * 
   * @assertion_ids: EL:JAVADOC:339; EL:JAVADOC:189; EL:JAVADOC:204
   * 
   * @test_Strategy: Verify that the invoke() method throws
   *                 MethodNotFoundException if no suitable method can be found.
   */
  @Test
  @TargetVehicle("jsp")
  public void  staticFieldResolverInvokeMNFETest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);
    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class, String.class };
    String[] values = { "Doug", "Donahue" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new ELClass(TCKELClass.class), "bogue_method", types, values, true,
          buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logMsg(buf.toString());

  }// End staticFieldResolverInvokeMNFETest

  /**
   * @testName: staticFieldELResolverInvokeTest
   * 
   * @assertion_ids: EL:JAVADOC:338; EL:JAVADOC:189; EL:JAVADOC:204
   * 
   * @test_Strategy: Verify that API calls work as expected for
   *                 BeanNameELResolver.invoke().
   */
  @Test
  @TargetVehicle("jsp")
  public void  staticFieldELResolverInvokeTest() throws Exception {
    StringBuffer buf = new StringBuffer();
    boolean pass = false;

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);
    ELContext context = elm.getELContext();

    Class<?>[] types = { String.class };
    String[] values = { "Ender" };

    try {
      pass = ResolverTest.testELResolverInvoke(context, context.getELResolver(),
          new ELClass(TCKELClass.class), "isName", types, values, false, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }

    TestUtil.logMsg(buf.toString());

  }// End staticFieldELResolverInvokeTest

  /**
   * @testName: staticFieldELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:333; EL:JAVADOC:336; EL:JAVADOC:342;
   *                 EL:JAVADOC:344; EL:JAVADOC:189
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   * 
   *                 getType() getValue() isReadOnly() setValue()
   */
  @Test
  @TargetVehicle("jsp")
  public void  staticFieldELResolverNPETest() throws Exception {
    boolean pass = false;
    StringBuffer buf = new StringBuffer();

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);

    try {
      pass = ResolverTest.testELResolverNPE(resolver,
          new ELClass(TCKELClass.class), "intention", "billy", buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }

    TestUtil.logTrace(buf.toString());

  } // End staticFieldELResolverNPETest

  /**
   * @testName: staticFieldResolverInvokePNFETest
   * 
   * @assertion_ids: EL:JAVADOC:334; EL:JAVADOC:337
   * 
   * @test_Strategy: Verify that the invoke() method throws
   *                 PropertyNotFoundException the specified class does not
   *                 exist, or if the field is not a public static filed of the
   *                 class, or if the field is inaccessible.
   */
  @Test
  @TargetVehicle("jsp")
  public void  staticFieldResolverInvokePNFETest() throws Exception {

    Object base = new ELClass(TCKELClass.class);

    // Test for non static field
    testForPNFE("notStatic", base);

    // Test for Private static field
    testForPNFE("privStatic", base);

    // Test for non existent Class
    testForPNFE("privStatic", "bogus");

  }// End staticFieldResolverInvokePNFETest

  // ------------------------- private methods

  private void testForPNFE(String property, Object base) {
    StringBuffer buf = new StringBuffer();

    ELManager elm = new ELManager();
    StaticFieldELResolver resolver = new StaticFieldELResolver();
    elm.addELResolver(resolver);
    ELContext context = elm.getELContext();

    // getType()
    try {
      resolver.getType(context, base, property);
      buf.append(ELTestUtil.FAIL + " getType() did not throw any exception."
          + TestUtil.NEW_LINE + "Expected: PropertyNotFoundException "
          + TestUtil.NEW_LINE);

    } catch (PropertyNotFoundException pnfe) {
      buf.append(ELTestUtil.PASS);

    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + "Wrong Exception thrown for getType()!"
          + TestUtil.NEW_LINE + " Expected: PropertyNotFoundException"
          + TestUtil.NEW_LINE + "Received: " + e.getClass().getSimpleName());
    }

    // getValue()
    try {
      resolver.getValue(context, base, property);
      buf.append(ELTestUtil.FAIL + " getValue() did not throw any exception."
          + TestUtil.NEW_LINE + "Expected: PropertyNotFoundException "
          + TestUtil.NEW_LINE);

    } catch (PropertyNotFoundException pnfe) {
      buf.append(ELTestUtil.PASS);

    } catch (Exception e) {
      buf.append(ELTestUtil.FAIL + "Wrong Exception thrown getValue()!"
          + TestUtil.NEW_LINE + " Expected: PropertyNotFoundException"
          + TestUtil.NEW_LINE + "Received: " + e.getClass().getSimpleName());
    }

  }// End testForPNFE

}
