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

package com.sun.ts.tests.el.api.jakarta_el.arrayelresolver;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ArrayELResolver;
import jakarta.el.ELContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tck.arquillian.protocol.common.TargetVehicle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientJspTest extends ServiceEETest {

  static final String VEHICLE_ARCHIVE = "arrayelresolver_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "arrayelresolver_jsp_vehicle_web.war");
  
    jsp_vehicle_web.addClasses(
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.common.el.api.resolver.BarELResolver.class,
        com.sun.ts.tests.common.el.api.resolver.ResolverTest.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.el.common.elcontext.BareBonesELContext.class,
        com.sun.ts.tests.el.common.util.ELTestUtil.class,
        com.sun.ts.tests.el.api.jakarta_el.arrayelresolver.ELClientJspTest.class
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

    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "arrayelresolver_jsp_vehicle.ear");
    jsp_vehicle_ear.addAsModule(jsp_vehicle_web);
    return jsp_vehicle_ear;

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

  private static final String[] names = { "doug", "nick", "roger", "ryan",
      "ed" };

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
   * @testName: arrayELResolverTest
   * 
   * @assertion_ids: EL:JAVADOC:1; EL:JAVADOC:3; EL:JAVADOC:4; EL:JAVADOC:5;
   *                 EL:JAVADOC:6; EL:JAVADOC:7; EL:JAVADOC:8
   * 
   * @test_Strategy: Verify that API calls work as expected: arrayELResolver()
   *                 getValue() getType() setValue() isReadOnly()
   *                 getCommonPropertyType() getFeatureDescriptors()
   */
  @Test
  @TargetVehicle("jsp")
  public void arrayELResolverTest() throws Exception {

    boolean pass;
    StringBuffer buf = new StringBuffer();
    String[] colors = { "red", "blue", "green" };

    try {
      ArrayELResolver arrayResolver = new ArrayELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, arrayResolver, colors,
          Integer.valueOf(1), "yellow", buf, false);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: arrayELResolverNPETest
   * 
   * @assertion_ids: EL:JAVADOC:1; EL:JAVADOC:5; EL:JAVADOC:6; EL:JAVADOC:7;
   *                 EL:JAVADOC:8; EL:JAVADOC:122; EL:JAVADOC:125;
   *                 EL:JAVADOC:128; EL:JAVADOC:131
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 NullPointerException, if context is null:
   * 
   *                 getValue() getType() setValue() isReadOnly()
   */
  @Test
  @TargetVehicle("jsp")
  public void arrayELResolverNPETest() throws Exception {

    boolean pass;
    StringBuffer buf = new StringBuffer();

    ArrayELResolver resolver = new ArrayELResolver();

    try {
      pass = ResolverTest.testELResolverNPE(resolver, names, Integer.valueOf(1),
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
   * @testName: arrayELResolverPNFETest
   * 
   * @assertion_ids: EL:JAVADOC:1; EL:JAVADOC:5; EL:JAVADOC:7; EL:JAVADOC:8;
   *                 EL:JAVADOC:121; EL:JAVADOC:127; EL:JAVADOC:134
   * 
   * @test_Strategy: Verify that the following methods throw a
   *                 PropertyNotFoundException, if the given index is out of
   *                 bounds for this array :
   * 
   *                 getType() isReadOnly() setValue()
   */
  @Test
  @TargetVehicle("jsp")
  public void arrayELResolverPNFETest() throws Exception {

    boolean pass;
    StringBuffer buf = new StringBuffer();

    ArrayELResolver resolver = new ArrayELResolver();
    BareBonesELContext barebonesContext = new BareBonesELContext();
    ELContext context = barebonesContext.getELContext();

    try {
      pass = ResolverTest.testELResolverPNFE(context, resolver, names,
          Integer.valueOf(10), buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /**
   * @testName: arrayELResolverIAETest
   * 
   * @assertion_ids: EL:JAVADOC:1; EL:JAVADOC:6; EL:JAVADOC:8; EL:JAVADOC:124;
   *                 EL:JAVADOC:132
   * 
   * @test_Strategy: Verify that the following methods throw an
   *                 IllegalArgumentException, if the property could not be
   *                 coerced into an integer:
   * 
   *                 getValue() setValue()
   */
  @Test
  @TargetVehicle("jsp")
  public void arrayELResolverIAETest() throws Exception {

    boolean pass;
    StringBuffer buf = new StringBuffer();

    ArrayELResolver resolver = new ArrayELResolver();
    BareBonesELContext barebonesContext = new BareBonesELContext();
    ELContext context = barebonesContext.getELContext();

    try {
      pass = ResolverTest.testELResolverIAE(context, resolver, names, "GARBAGE",
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
   * @testName: arrayELResolverPNWETest
   * 
   * @assertion_ids: EL:JAVADOC:2; EL:JAVADOC:8; EL:JAVADOC:127; EL:JAVADOC:133
   * 
   * @test_Strategy: Verify that the single-parameter constructor for
   *                 arrayELResolver can be used to construct a read-only
   *                 resolver, and that the setValue() method throws a
   *                 PropertyNotWritableException, if the resolver was
   *                 constructed in read-only mode.
   * 
   *                 ArrayELResolver(boolean) setValue()
   */
  @Test
  @TargetVehicle("jsp")
  public void arrayELResolverPNWETest() throws Exception {

    boolean pass;
    StringBuffer buf = new StringBuffer();

    ArrayELResolver resolver = new ArrayELResolver(true);
    BareBonesELContext barebonesContext = new BareBonesELContext();
    ELContext context = barebonesContext.getELContext();

    try {
      pass = ResolverTest.testELResolverPNWE(context, resolver, names,
          Integer.valueOf(1), "billy", buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

  /*
   * @testName: arrayELResolverOBETest
   * 
   * @assertion_ids: EL:JAVADOC:1; EL:JAVADOC:6
   * 
   * @test_Strategy: Verify that if the index is out of bounds, null is
   * returned.
   * 
   * getValue()
   */
  @Test
  @TargetVehicle("jsp")
  public void arrayELResolverOBETest() throws Exception {

    boolean pass = true;

    StringBuffer buf = new StringBuffer();

    ArrayELResolver resolver = new ArrayELResolver();
    BareBonesELContext barebonesContext = new BareBonesELContext();
    ELContext context = barebonesContext.getELContext();

    try {
      Object value = resolver.getValue(context, names, Integer.valueOf(5));

      if (value != null) {
        pass = false;
        buf.append("Expected Value: 'null'").append(TestUtil.NEW_LINE)
            .append("Received Value: ").append(value.toString());
      }

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
  }

  /*
   * @testName: arrayELResolverCCETest
   * 
   * @assertion_ids: EL:JAVADOC:1; EL:JAVADOC:8; EL:JAVADOC:130
   * 
   * @test_Strategy: Verify that if the class of a specified object prevents it
   * from being added to the array, a ClassCastException is thrown.
   * 
   * setValue()
   */
  @Test
  @TargetVehicle("jsp")
  public void arrayELResolverCCETest() throws Exception {

    boolean pass = false;

    ArrayELResolver resolver = new ArrayELResolver();
    BareBonesELContext barebonesContext = new BareBonesELContext();
    ELContext context = barebonesContext.getELContext();

    try {
      resolver.setValue(context, names, Integer.valueOf(1), Boolean.TRUE);

    } catch (ClassCastException cce) {
      pass = true;

    } catch (Exception e) {
      TestUtil.logErr(
          "Failed: Exception thrown but was not a " + "ClassCastException");
      throw new Exception(e);
    }

    if (!pass) {
      throw new Exception("Failed: No exception thrown.");
    }
  }


  /*
   * @testName: arrayELResolverLengthTest
   * 
   * @test_Strategy: Verify that the length of an array is available as a read-only property.
   */
  @Test
  @TargetVehicle("jsp")
  public void arrayELResolverLengthTest() throws Exception {

    boolean pass;
    StringBuffer buf = new StringBuffer();
    String[] colors = { "red", "blue", "green" };

    try {
      ArrayELResolver arrayResolver = new ArrayELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, arrayResolver, colors,
          "length", "3", buf, true);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }

}
