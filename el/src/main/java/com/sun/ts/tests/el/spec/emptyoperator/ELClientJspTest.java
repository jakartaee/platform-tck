/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.spec.emptyoperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.NameValuePair;

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

  static final String VEHICLE_ARCHIVE = "emptyoperator_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

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

  public static String editWebXmlString(InputStream inStream, String jsp_vehicle) throws IOException{
    return inputStreamToString(inStream).replaceAll("el_jsp_vehicle", jsp_vehicle);
  }


  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "emptyoperator_jsp_vehicle_web.war");
  
    jsp_vehicle_web.addClasses(
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.el.api.expression.ExpressionTest.class,
        com.sun.ts.tests.el.common.elresolver.EmployeeELResolver.class,
        com.sun.ts.tests.el.common.elresolver.FunctionELResolver.class,
        com.sun.ts.tests.el.common.elresolver.VariableELResolver.class,
        com.sun.ts.tests.el.common.elresolver.VectELResolver.class,
        com.sun.ts.tests.el.common.elcontext.BareBonesELContext.class,
        com.sun.ts.tests.el.common.elcontext.SimpleELContext.class,
        com.sun.ts.tests.el.common.elcontext.SimpleELContext.Resolver.class,
        com.sun.ts.tests.el.common.elcontext.FuncMapperELContext.class,
        com.sun.ts.tests.el.common.elcontext.BarELContext.class,
        com.sun.ts.tests.el.common.elcontext.VRContext.class,
        com.sun.ts.tests.el.common.elcontext.VarMapperELContext.class,
        com.sun.ts.tests.el.common.util.ELTestUtil.class,
        com.sun.ts.tests.el.common.util.SimpleBean.class,
        com.sun.ts.tests.el.common.util.SimpleInterface.class,
        com.sun.ts.tests.el.common.util.ResolverType.class,
        com.sun.ts.tests.el.common.util.NameValuePair.class,
        com.sun.ts.tests.el.common.util.ExprEval.class,
        com.sun.ts.tests.el.common.util.MethodsBean.class,
        com.sun.ts.tests.el.common.util.TestNum.class,
        com.sun.ts.tests.el.common.util.TypesBean.class,
        com.sun.ts.tests.el.common.util.Validator.class,
        com.sun.ts.tests.el.common.functionmapper.TCKFunctionMapper.class,
        com.sun.ts.tests.el.spec.emptyoperator.ELClientJspTest.class
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
    
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "emptyoperator_jsp_vehicle.ear");
    jsp_vehicle_ear.addAsModule(jsp_vehicle_web);
    return jsp_vehicle_ear;

  }

  Properties testProps;

  private final boolean[] deferred = { true, false };

  public static void main(String[] args) {
    ELClientJspTest theTests = new ELClientJspTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Exception {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Exception {
  }

  /*
   * @testName: elEmptyNullTest
   * 
   * @assertion_ids: EL:SPEC:25.1
   * 
   * @test_Strategy: Validate that if "null" is passed with the Empty operator,
   * the correct Boolean result is returned.
   */
  @Test
  @TargetVehicle("jsp")
  public void elEmptyNullTest() throws Exception {

    boolean pass = false;

    String[] symbols = { "$", "#" };
    boolean expectedResult = true;

    try {
      for (String prefix : symbols) {
        String expr = prefix + "{empty null}";
        Object result = ExprEval.evaluateValueExpression(expr, null,
            Object.class);

        if (result == null) {
          TestUtil.logTrace("result is null");
        } else {
          TestUtil.logTrace("result is " + result.toString());
        }

        pass = (ExprEval.compareClass(result, Boolean.class)
            && ExprEval.compareValue((Boolean) result, expectedResult));

        if (!pass)
          throw new Exception("TEST FAILED: pass = false");
      }
    } catch (Exception e) {
      throw new Exception(e);
    }

  }

  /*
   * @testName: elEmptyStringTest
   * 
   * @assertion_ids: EL:SPEC:25.2; EL:SPEC:25.6
   * 
   * @test_Strategy: Validate that if a String is passed with the Empty
   * operator, the correct Boolean result is returned.
   */
  @Test
  @TargetVehicle("jsp")
  public void elEmptyStringTest() throws Exception {

    this.testEmptyOperator("", true);

    this.testEmptyOperator("something", false);

  }

  /*
   * @testName: elEmptyArrayTest
   * 
   * @assertion_ids: EL:SPEC:25.3; EL:SPEC:25.6
   * 
   * @test_Strategy: Validate that if a Array is passed with the Empty operator,
   * the correct Boolean result is returned.
   */
  @Test
  @TargetVehicle("jsp")
  public void elEmptyArrayTest() throws Exception {

    this.testEmptyOperator(new String[0], true);

    String[] testArray = { "Apple" };
    this.testEmptyOperator(testArray, false);

  }

  /*
   * @testName: elEmptyMapTest
   * 
   * @assertion_ids: EL:SPEC:25.4; EL:SPEC:25.6
   * 
   * @test_Strategy: Validate that if a Map is passed with the Empty operator,
   * the correct Boolean result is returned.
   */
  @Test
  @TargetVehicle("jsp")
  public void elEmptyMapTest() throws Exception {

    HashMap testMap = new HashMap();

    this.testEmptyOperator(testMap, true);

    testMap.put("Fruit", "Apple");
    this.testEmptyOperator(testMap, false);

    // Clean out the Map.
    testMap.clear();
  }

  /*
   * @testName: elEmptyCollectionTest
   * 
   * @assertion_ids: EL:SPEC:25.5; EL:SPEC:25.6
   * 
   * @test_Strategy: Validate that if a Collection is passed with the Empty
   * operator, the correct Boolean result is returned.
   */
  @Test
  @TargetVehicle("jsp")
  public void elEmptyCollectionTest() throws Exception {

    ArrayList testCollection = new ArrayList();

    this.testEmptyOperator(testCollection, true);

    testCollection.add("Apple");
    this.testEmptyOperator(testCollection, false);

    // Clean out the Collection.
    testCollection.clear();
  }

  // ---------------------------------------------------------- private methods

  // Test Empty operator.
  private void testEmptyOperator(Object testVal, boolean expectedResult)
      throws Exception {

    boolean pass = false;

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(testVal);

    try {
      for (boolean tf : deferred) {
        String expr = ExprEval.buildElExpr(tf, "empty");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && ExprEval.compareValue((Boolean) result, expectedResult));

        if (!pass)
          throw new Exception("TEST FAILED: pass = false");
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
  }

}
