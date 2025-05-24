/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.elprocessor;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import jakarta.el.ELProcessor;
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
import java.lang.reflect.Method;
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

  static final String VEHICLE_ARCHIVE = "elprocessor_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static WebArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "elprocessor_jsp_vehicle_web.war");
  
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
        com.sun.ts.tests.el.api.jakarta_el.elprocessor.ELClientJspTest.class
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
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "elprocessor_jsp_vehicle.ear");
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
    this.testProps = p;
  }

  /**
   * Does nothing...
   * 
   * @throws Exception
   */
  @AfterEach
  public void cleanup() throws Exception {
    // does nothing at this point
  }

  /**
   * @testName: elProcessorDefineFunctionNPETest
   * @assertion_ids: EL:JAVADOC:220; EL:JAVADOC:216; EL:JAVADOC:219
   * @test_Strategy: Assert that a NullPointerException is thrown if any of the
   *                 arguments is null.
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elProcessorDefineFunctionNPETest() throws Exception {
    ELProcessor elp = new ELProcessor();

    Method meth;
    try {
      meth = elp.getClass().getMethod("toString", new Class<?>[] {});

      // Tests for defineFunction(String, String, Method)
      TestUtil.logMsg(
          "Testing: ELProcessor.defineFunction(null, " + "function, meth)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, Method.class },
          new Object[] { null, "function", meth });

      TestUtil.logMsg(
          "Testing: ELProcessor.defineFunction(prefix, " + "null, meth)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, Method.class },
          new Object[] { "prefix", null, meth });

      TestUtil.logMsg(
          "Testing: ELProcessor.defineFunction(prefix, " + "function, null)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, Method.class },
          new Object[] { "prefix", "function", null });

      // Tests for defineFunction(String, String, String, String)
      TestUtil.logMsg("Testing: ELProcessor.defineFunction(prefix, "
          + "function, className, null)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, String.class,
              String.class },
          new Object[] { "prefix", "function", "className", null });

      TestUtil.logMsg("Testing: ELProcessor.defineFunction(prefix, "
          + "function, null, method)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, String.class,
              String.class },
          new Object[] { "prefix", "function", null, "method" });

      TestUtil.logMsg("Testing: ELProcessor.defineFunction(prefix, "
          + "null, className, method)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, String.class,
              String.class },
          new Object[] { "prefix", null, "className", "method" });

      TestUtil.logMsg("Testing: ELProcessor.defineFunction(null, "
          + "function, className, method)");
      ELTestUtil.checkForNPE(elp, "defineFunction",
          new Class<?>[] { String.class, String.class, String.class,
              String.class },
          new Object[] { null, "function", "className", "method" });

    } catch (SecurityException e) {
      e.printStackTrace();

    } catch (NoSuchMethodException nsme) {
      nsme.printStackTrace();
    }

  } // end elProcessorDefineFunctionNPETest

  /**
   * @testName: elProcessorDefineFunctionCNFETest
   * @assertion_ids: EL:JAVADOC:220; EL:JAVADOC:214; EL:JAVADOC:217
   * @test_Strategy: Assert that a ClassNotFoundException if the specified class
   *                 does not exists.
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elProcessorDefineFunctionCNFETest() throws Exception {
    ELProcessor elp = new ELProcessor();

    TestUtil.logMsg("Testing: ELProcessor.defineFunction(null, "
        + "function, className, method)");
    ELTestUtil.checkForCNFE(elp, "defineFunction",
        new Class<?>[] { String.class, String.class, String.class,
            String.class },
        new Object[] { "prefix", "function", "bogus", "method" });

  } // end elProcessorDefineFunctionCNFETest

  /**
   * @testName: elProcessorDefineFunctionNSMETest
   * @assertion_ids: EL:JAVADOC:220; EL:JAVADOC:215
   * @test_Strategy: Assert that a NoSuchMethodException if the method (with or
   *                 without the signature) is not a declared method of the
   *                 class, or if the method signature is not valid.
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elProcessorDefineFunctionNSMETest() throws Exception {
    ELProcessor elp = new ELProcessor();

    TestUtil.logMsg("Testing: ELProcessor.defineFunction(null, "
        + "function, className, method)");
    ELTestUtil.checkForCNFE(elp, "defineFunction",
        new Class<?>[] { String.class, String.class, String.class,
            String.class },
        new Object[] { "prefix", "function", "java.util.String", "bogus" });

  } // end elProcessorDefineFunctionNSMETest
}
