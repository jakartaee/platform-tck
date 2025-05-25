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

package com.sun.ts.tests.el.api.jakarta_el.elcontext;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import jakarta.el.ELContext;
import jakarta.el.ELManager;
import jakarta.el.EvaluationListener;
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
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.stream.Collectors;

@ExtendWith(ArquillianExtension.class)
@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientJspTest extends ServiceEETest {

  static final String VEHICLE_ARCHIVE = "elcontext_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static WebArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "elcontext_jsp_vehicle_web.war");
  
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
        com.sun.ts.tests.el.api.jakarta_el.elcontext.ELClientJspTest.class,
        com.sun.ts.tests.el.api.jakarta_el.elcontext.ELClientJspTest.TCKEvalListener.class
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
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "elcontext_jsp_vehicle.ear");
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
   * @testName: elContextPutGetContextTest
   * @assertion_ids: EL:JAVADOC:33; EL:JAVADOC:39; EL:JAVADOC:328;
   *                 EL:JAVADOC:326; EL:JAVADOC:321
   * @test_Strategy: Assert that we get back the expected value from
   *                 getContext() that we put in with putContext().
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elContextPutGetContextTest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();
    String testStr = "TCKContext";

    elc.putContext(String.class, testStr);
    String result = elc.getContext(String.class).toString();

    if (!testStr.equals(result)) {
      throw new Exception(ELTestUtil.FAIL + " Unexpected Context Returned!"
          + TestUtil.NEW_LINE + "Expected: " + testStr + TestUtil.NEW_LINE
          + "Received: " + result);
    }

  } // end elContextPutGetContextTest

  /**
   * @testName: elContextGetSetLocaleTest
   * @assertion_ids: EL:JAVADOC:36; EL:JAVADOC:40; EL:JAVADOC:328;
   *                 EL:JAVADOC:329
   * @test_Strategy: Assert that we get back the Locale we set.
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elContextGetSetLocaleTest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    String disName = "english";
    Locale en = new Locale(disName);
    elc.setLocale(en);
    String result = elc.getLocale().getDisplayName();

    if (!result.equalsIgnoreCase(disName)) {
      throw new Exception(ELTestUtil.FAIL + " Unexpected Locale Returned!"
          + TestUtil.NEW_LINE + "Expected: " + disName + TestUtil.NEW_LINE
          + "Received: " + result);
    }

  } // end elContextGetSetLocaleTest

  /**
   * @testName: elContextIsSetPropertyResolvedTest
   * @assertion_ids: EL:JAVADOC:38; EL:JAVADOC:41; EL:JAVADOC:328;
   *                 EL:JAVADOC:329; EL:JAVADOC:322
   * @test_Strategy: Assert that when we call setPropertyResolved that
   *                 isPropertyResolved returns true.
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elContextIsSetPropertyResolvedTest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    boolean isProp = elc.isPropertyResolved();

    if (isProp) {
      throw new Exception(
          ELTestUtil.FAIL + " Unexpected result from isPropertyResolved()!"
              + TestUtil.NEW_LINE + "Should have been false!");
    }

    elc.setPropertyResolved(true);
    isProp = elc.isPropertyResolved();

    if (!isProp) {
      throw new Exception(
          ELTestUtil.FAIL + " Unexpected result from isPropertyResolved()!"
              + TestUtil.NEW_LINE + "Should have been true!");
    }

  } // end elContextIsSetPropertyResolvedTest

  /**
   * @testName: elContextPutContextNPETest
   * @assertion_ids: EL:JAVADOC:33; EL:JAVADOC:198; EL:JAVADOC:328;
   *                 EL:JAVADOC:329; EL:JAVADOC:327
   * @test_Strategy: Validate that a NullPointerException is thrown if Class is
   *                 null or Object is null.
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elContextPutContextNPETest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    TestUtil.logMsg("Testing: ELContext.putContext(String.class, null)");
    ELTestUtil.checkForNPE(elc, "putContext",
        new Class<?>[] { Class.class, Object.class },
        new Object[] { String.class, null });

    TestUtil.logMsg("Testing: ELContext.putContext(null, testStrg)");
    ELTestUtil.checkForNPE(elc, "putContext",
        new Class<?>[] { Class.class, Object.class },
        new Object[] { String.class, null });

  } // end elContextPutContextNPETest

  /**
   * @testName: elContextGetContextNPETest
   * @assertion_ids: EL:JAVADOC:33; EL:JAVADOC:194; EL:JAVADOC:328;
   *                 EL:JAVADOC:329; EL:JAVADOC:321
   * @test_Strategy: Validate that a NullPointerException is thrown if key is
   *                 null.
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elContextGetContextNPETest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    TestUtil.logMsg("Testing: ELContext.getContext(null)");
    ELTestUtil.checkForNPE(elc, "getContext", new Class<?>[] { Class.class },
        new Object[] { null });

  } // end elContextGetContextNPETest

  /**
   * @testName: elContextAddGetListenersTest
   * @assertion_ids: EL:JAVADOC:33; EL:JAVADOC:191; EL:JAVADOC:197;
   *                 EL:JAVADOC:328; EL:JAVADOC:329
   * @test_Strategy: Validate that a NullPointerException is thrown if key is
   *                 null.
   * 
   * @since: 3.0
   */
  @Test
  @TargetVehicle("jsp")
  public void  elContextAddGetListenersTest() throws Exception {
    ELManager elm = new ELManager();
    ELContext elc = elm.getELContext();

    EvaluationListener listenerOne = new TCKEvalListener();
    EvaluationListener listenerTwo = new TCKEvalListener();

    elc.addEvaluationListener(listenerOne);
    elc.addEvaluationListener(listenerTwo);

    List<EvaluationListener> listeners = elc.getEvaluationListeners();

    if (!(listeners.contains(listenerOne) && listeners.contains(listenerTwo))) {
      throw new Exception(
          ELTestUtil.FAIL + " Was unable to find test listeners in List "
              + "returned form " + "ElContext.getListeners()!");
    }

  } // end elContextAddGetListenersTest

  // ---------------------------------------------- private classes

  private static class TCKEvalListener extends EvaluationListener {
    // do nothing this is just for test purposes.
  }
}
