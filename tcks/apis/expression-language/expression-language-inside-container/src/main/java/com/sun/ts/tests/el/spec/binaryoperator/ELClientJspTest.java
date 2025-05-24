/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.spec.binaryoperator;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.TestNum;
import com.sun.ts.tests.el.common.util.Validator;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Properties;
import java.util.stream.Collectors;

@ExtendWith(ArquillianExtension.class)
@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientJspTest extends ServiceEETest {

  static final String VEHICLE_ARCHIVE = "binaryoperator_jsp_vehicle";

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
  public static WebArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "binaryoperator_jsp_vehicle_web.war");
  
    jsp_vehicle_web.addClasses(
        Fault.class,
        SetupException.class,
        EETest.class,
        ServiceEETest.class,
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
        com.sun.ts.tests.el.spec.binaryoperator.ELClientJspTest.class
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
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "binaryoperator_jsp_vehicle.ear");
    jsp_vehicle_ear.addAsModule(jsp_vehicle_web);
    return jsp_vehicle_ear;
    */

  }

  Properties testProps;

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
    // does nothing at this point
  }

  /**
   * @testName: elNullOperandAddTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "+"
   *                 (addition) operation are null, the result is (Long) 0.
   */
  @Test
  @TargetVehicle("jsp")
  public void elNullOperandAddTest() throws Exception {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "+");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandSubtractTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "-"
   *                 (subtraction) operation are null, the result is (Long) 0.
   */
  @Test
  @TargetVehicle("jsp")
  public void elNullOperandSubtractTest() throws Exception {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(false, "-");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandMultiplyTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "*"
   *                 (multiplication) operation are null, the result is (Long)
   *                 0.
   */
  @Test
  @TargetVehicle("jsp")
  public void elNullOperandMultiplyTest() throws Exception {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "*");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandDivisionTest
   * @assertion_ids: EL:SPEC:18.1
   * @test_Strategy: Validate that if both of the operands in an EL "/"
   *                 (division) operation are null, the result is (Long) 0.
   * 
   */
  @Test
  @TargetVehicle("jsp")
  public void elNullOperandDivisionTest() throws Exception {

    boolean pass = false;
    Long expectedResult = Long.valueOf("0");

    try {
      String expr = ExprEval.buildElExpr(true, "/");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandModulusTest
   * @assertion_ids: EL:SPEC:19.1
   * @test_Strategy: Validate that if both of the operands in an EL "%" (mod)
   *                 operation are null, the result is (Long) 0.
   * 
   */
  @Test
  @TargetVehicle("jsp")
  public void elNullOperandModulusTest() throws Exception {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "%");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elBigDecimalAddTest
   * @assertion_ids: EL:SPEC:17.2.1
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a BigDecimal, the result is coerced
   *                 to BigDecimal and is the sum of the operands.
   * 
   *                 Equations tested: BigDecimal + BigDecimal BigDecimal +
   *                 Double BigDecimal + Float BigDecimal + String containing
   *                 ".", "e", or "E" BigDecimal + BigInteger BigDecimal +
   *                 Integer BigDecimal + Long BigDecimal + Short BigDecimal +
   *                 Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigDecimalAddTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(10.531);
    BigDecimal expectedResult = BigDecimal.valueOf(11.531);

    Validator.testBigDecimal(testValue, expectedResult, "+");

  }

  /**
   * @testName: elBigDecimalSubtractTest
   * @assertion_ids: EL:SPEC:17.2.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a BigDecimal, the result is
   *                 coerced to BigDecimal and is the difference of the
   *                 operands.
   * 
   *                 Equations tested: BigDecimal - BigDecimal BigDecimal -
   *                 Double BigDecimal - Float BigDecimal - String containing
   *                 ".", "e", or "E" BigDecimal - BigInteger BigDecimal -
   *                 Integer BigDecimal - Long BigDecimal - Short BigDecimal -
   *                 Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigDecimalSubtractTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(10.531);
    BigDecimal expectedResult = BigDecimal.valueOf(9.531);

    Validator.testBigDecimal(testValue, expectedResult, "-");

  }

  /**
   * @testName: elBigDecimalMultiplyTest
   * @assertion_ids: EL:SPEC:17.2.3
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a BigDecimal, the result is
   *                 coerced to BigDecimal and is the product of the operands.
   * 
   *                 Equations tested: BigDecimal * BigDecimal BigDecimal *
   *                 Double BigDecimal * Float BigDecimal * String containing
   *                 ".", "e", or "E" BigDecimal * BigInteger BigDecimal *
   *                 Integer BigDecimal * Long BigDecimal * Short BigDecimal *
   *                 Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigDecimalMultiplyTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(1.5);
    BigDecimal expectedResult = BigDecimal.valueOf(1.5);

    Validator.testBigDecimal(testValue, expectedResult, "*");

  }

  /**
   * @testName: elBigDecimalDivisionTest
   * @assertion_ids: EL:SPEC:18.2
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and is the quotient of the operands.
   * 
   *                 Equations tested: BigDecimal / BigDecimal BigDecimal /
   *                 Double BigDecimal / Float BigDecimal / String containing
   *                 ".", "e", or "E" BigDecimal / BigInteger BigDecimal /
   *                 Integer BigDecimal / Long BigDecimal / Short BigDecimal /
   *                 Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigDecimalDivisionTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(3.0);
    BigDecimal expectedResult = BigDecimal.valueOf(3.0);

    Validator.testBigDecimal(testValue, expectedResult, "/");

  }

  /**
   * @testName: elBigDecimalModulusTest
   * @assertion_ids: EL:SPEC:19.2
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a BigDecimal, the result is coerced to Double
   *                 and is the remainder of the quotient of the operands.
   * 
   *                 Equations tested: BigDecimal % BigDecimal BigDecimal %
   *                 Double BigDecimal % Float BigDecimal % String containing
   *                 ".", "e", or "E" BigDecimal % BigInteger BigDecimal %
   *                 Integer BigDecimal % Long BigDecimal % Short BigDecimal %
   *                 Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigDecimalModulusTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(2.5);
    BigDecimal expectedResult = BigDecimal.valueOf(0.5);

    Validator.testBigDecimal(testValue, expectedResult, "%");

  }

  /**
   * @testName: elBigIntegerAddTest
   * @assertion_ids: EL:SPEC:17.4.1
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a BigInteger, the result is coerced
   *                 to BigInteger and is the sum of the operands.
   * 
   *                 Equations tested: BigInteger + BigInteger BigInteger +
   *                 Integer BigInteger + Long BigInteger + Short BigInteger +
   *                 Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigIntegerAddTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(10532);

    Validator.testBigInteger(testValue, expectedResult, "+");

  }

  /**
   * @testName: elBigIntegerSubtractTest
   * @assertion_ids: EL:SPEC:17.4.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a BigInteger, the result is
   *                 coerced to BigInteger and is the difference of the
   *                 operands.
   * 
   *                 Equations tested: BigInteger - BigInteger BigInteger -
   *                 Integer BigInteger - Long BigInteger - Short BigInteger -
   *                 Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigIntegerSubtractTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(10530);

    Validator.testBigInteger(testValue, expectedResult, "-");

  }

  /**
   * @testName: elBigIntegerMultiplyTest
   * @assertion_ids: EL:SPEC:17.4.3
   * @test_Strategy: Validate that if one of the operands in an EL "*" operation
   *                 is a BigInteger, the result is coerced to BigInteger and is
   *                 the product of the operands.
   * 
   *                 BigInteger * BigInteger BigInteger * Integer BigInteger *
   *                 Long BigInteger * Short BigInteger * Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigIntegerMultiplyTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(10531);

    Validator.testBigInteger(testValue, expectedResult, "*");

  }

  /**
   * @testName: elBigIntegerDivisionTest
   * @assertion_ids: EL:SPEC:18.2
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a BigInteger, the result is coerced to
   *                 BigDecimal and is the quotient of the operands.
   * 
   *                 BigInteger / BigInteger BigInteger / Integer BigInteger /
   *                 Long BigInteger / Short BigInteger / Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigIntegerDivisionTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(10531);

    Validator.testBigInteger(testValue, expectedResult, "/");

  }

  /**
   * @testName: elBigIntegerModulusTest
   * @assertion_ids: EL:SPEC:19.3
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and is the remainder of the quotient of the
   *                 operands.
   * 
   *                 BigInteger % BigInteger BigInteger % Integer BigInteger %
   *                 Long BigInteger % Short BigInteger % Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigIntegerModulusTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(0);

    Validator.testBigInteger(testValue, expectedResult, "%");

  }

  /**
   * @testName: elFloatAddTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Float, the result is coerced to
   *                 Double and is the sum of the operands.
   * 
   *                 Equations tested: Float + Double Float + Float Float +
   *                 String containing ".", "e", or "E" Float + BigInteger Float
   *                 + Integer Float + Long Float + Short Float + Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elFloatAddTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue + Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "+");
    }

  }

  /**
   * @testName: elFloatSubtractTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Float, the result is coerced
   *                 to Double and is the difference of the operands.
   * 
   *                 Equations tested: Float - Double Float - Float Float -
   *                 String containing ".", "e", or "E" Float - BigInteger Float
   *                 - Integer Float - Long Float - Short Float - Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elFloatSubtractTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue - Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "-");
    }

  }

  /**
   * @testName: elFloatMultiplyTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Float, the result is
   *                 coerced to Double and is the product of the operands.
   * 
   *                 Equations tested: Float * Double Float * Float Float *
   *                 String containing ".", "e", or "E" Float * BigInteger Float
   *                 * Integer Float * Long Float * Short Float * Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elFloatMultiplyTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue * Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "*");
    }

  }

  /**
   * @testName: elFloatDivisionTest
   * @assertion_ids: EL:SPEC:18.2; EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Float, the result is coerced to Double and
   *                 is the quotient of the operands.
   * 
   *                 Equations tested: Float / Double Float / Float Float /
   *                 String containing ".", "e", or "E" Float / BigInteger Float
   *                 / Integer Float / Long Float / Short Float / Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elFloatDivisionTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue / Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "/");
    }

  }

  /**
   * @testName: elFloatModulusTest
   * @assertion_ids: EL:SPEC:19.2
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Float, the result is coerced to Double and
   *                 is the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Float % Double Float % Float Float %
   *                 String containing ".", "e", or "E" Float % BigInteger Float
   *                 % Integer Float % Long Float % Short Float % Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elFloatModulusTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue % Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "%");
    }

  }

  /**
   * @testName: elDoubleAddTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Double, the result is coerced to
   *                 Double and is the sum of the operands.
   * 
   *                 Equations tested: Double + Double Double + String
   *                 containing ".", "e", or "E" Double + BigInteger Double +
   *                 Integer Double + Long Double + Short Double + Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elDoubleAddTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(3.5);

    Validator.testDouble(testValue, expectedResult, "+");

  }

  /**
   * @testName: elDoubleSubtractTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Double, the result is coerced
   *                 to Double and is the difference of the operands.
   * 
   *                 Equations tested: Double - Double Double - String
   *                 containing ".", "e", or "E" Double - BigInteger Double -
   *                 Integer Double - Long Double - Short Double - Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elDoubleSubtractTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(1.5);

    Validator.testDouble(testValue, expectedResult, "-");

  }

  /**
   * @testName: elDoubleMultiplyTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Double, the result is
   *                 coerced to Double and is the product of the operands.
   * 
   *                 Equations tested: Double * Double Double * String
   *                 containing ".", "e", or "E" Double * BigInteger Double *
   *                 Integer Double * Long Double * Short Double * Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elDoubleMultiplyTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(2.5);

    Validator.testDouble(testValue, expectedResult, "*");

  }

  /**
   * @testName: elDoubleDivisionTest
   * @assertion_ids: EL:SPEC:18.2; EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Double, the result is coerced to Double and
   *                 is the quotient of the operands.
   * 
   *                 Equations tested: Double / Double Double / String
   *                 containing ".", "e", or "E" Double / BigInteger Double /
   *                 Integer Double / Long Double / Short Double / Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elDoubleDivisionTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(2.5);

    Validator.testDouble(testValue, expectedResult, "/");

  }

  /**
   * @testName: elDoubleModulusTest
   * @assertion_ids: EL:SPEC:19.2
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Double, the result is coerced to Double and
   *                 is the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Double % Double Double % String
   *                 containing ".", "e", or "E" Double % BigInteger Double %
   *                 Integer Double % Long Double % Short Double % Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elDoubleModulusTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(0.5);

    Validator.testDouble(testValue, expectedResult, "%");
  }

  /**
   * @testName: elNumericStringSubtractTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a numeric string, the result is
   *                 coerced to Double and is the difference of the operands.
   * 
   *                 Equations tested: Numeric String - String containing ".",
   *                 "e", or "E" Numeric String - BigInteger Numeric String -
   *                 Integer Numeric String - Long Numeric String - Short
   *                 Numeric String - Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elNumericStringSubtractTest() throws Exception {

    String testValue = "25e-1";
    Double expectedResult = Double.valueOf(1.5);

    Validator.testNumericString(testValue, expectedResult, "-");

  }

  /**
   * @testName: elNumericStringMultiplyTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a numeric string, the result
   *                 is coerced to Double and is the product of the operands.
   * 
   *                 Equations tested: Numeric String * String containing ".",
   *                 "e", or "E" Numeric String * BigInteger Numeric String *
   *                 Integer Numeric String * Long Numeric String * Short
   *                 Numeric String * Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elNumericStringMultiplyTest() throws Exception {

    String testValue = "25E-1";
    Double expectedResult = Double.valueOf(2.5);

    Validator.testNumericString(testValue, expectedResult, "*");

  }

  /**
   * @testName: elNumericStringDivisionTest
   * @assertion_ids: EL:SPEC:18.2; EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a numeric string, the result is coerced to
   *                 Double and is the quotient of the operands.
   * 
   *                 Equations tested: Numeric String / String containing ".",
   *                 "e", or "E" Numeric String / BigInteger Numeric String /
   *                 Integer Numeric String / Long Numeric String / Short
   *                 Numeric String / Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elNumericStringDivisionTest() throws Exception {

    String testValue = "2.5";
    Double expectedResult = Double.valueOf(2.5);

    Validator.testNumericString(testValue, expectedResult, "/");

  }

  /**
   * @testName: elNumericStringModulusTest
   * @assertion_ids: EL:SPEC:19.2
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a numeric string, the result is coerced to
   *                 Double and is the remainder of the quotient of the
   *                 operands.
   * 
   *                 Equations tested: Numeric String % String containing ".",
   *                 "e", or "E" Numeric String % BigInteger Numeric String %
   *                 Integer Numeric String % Long Numeric String % Short
   *                 Numeric String % Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elNumericStringModulusTest() throws Exception {

    String testValue = "2.5e0";
    Double expectedResult = Double.valueOf(0.5);

    Validator.testNumericString(testValue, expectedResult, "%");

  }

  /**
   * @testName: elLongAddTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Long, the result is coerced to
   *                 Long and is the sum of the operands.
   * 
   *                 Equations tested: Long + Integer Long + Long Long + Short
   *                 Long + Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elLongAddTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(25001);

    Validator.testLong(testValue, expectedResult, "+");

  }

  /**
   * @testName: elLongSubtractTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Long, the result is coerced to
   *                 Long and is the difference of the operands.
   * 
   *                 Equations tested: Long - Integer Long - Long Long - Short
   *                 Long - Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elLongSubtractTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(24999);

    Validator.testLong(testValue, expectedResult, "-");

  }

  /**
   * @testName: elLongMultiplyTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Long, the result is coerced
   *                 to Long and is the product of the operands.
   * 
   *                 Equations tested: Long * Integer Long * Long Long * Short
   *                 Long * Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elLongMultiplyTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(25000);

    Validator.testLong(testValue, expectedResult, "*");

  }

  /**
   * @testName: elLongDivisionTest
   * @assertion_ids: EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Long, the result is coerced to Double and is
   *                 the quotient of the operands.
   * 
   *                 Equations tested: Long / Integer Long / Long Long / Short
   *                 Long / Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elLongDivisionTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(25000);

    Validator.testLong(testValue, expectedResult, "/");

  }

  /**
   * @testName: elLongModulusTest
   * @assertion_ids: EL:SPEC:19.4
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Long, the result is coerced to Long and is
   *                 the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Long % Integer Long % Long Long % Short
   *                 Long % Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elLongModulusTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(0);

    Validator.testLong(testValue, expectedResult, "%");

  }

  /**
   * @testName: elIntegerAddTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Integer, the result is coerced to
   *                 Long and is the sum of the operands.
   * 
   *                 Equations tested: Integer + Integer Integer + Short Integer
   *                 + Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elIntegerAddTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(26);

    Validator.testInteger(testValue, expectedResult, "+");

  }

  /**
   * @testName: elIntegerSubtractTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Integer, the result is coerced
   *                 to Long and is the difference of the operands.
   * 
   *                 Equations tested: Long - Integer Long - Short Long - Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elIntegerSubtractTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(24);

    Validator.testInteger(testValue, expectedResult, "-");

  }

  /**
   * @testName: elIntegerMultiplyTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Integer, the result is
   *                 coerced to Long and is the product of the operands.
   * 
   *                 Equations tested: Integer * Integer Integer * Short Integer
   *                 * Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elIntegerMultiplyTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(25);

    Validator.testInteger(testValue, expectedResult, "*");

  }

  /**
   * @testName: elIntegerDivisionTest
   * @assertion_ids: EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Integer, the result is coerced to Double and
   *                 is the quotient of the operands.
   * 
   *                 Equations tested: Integer / Integer Integer / Short Integer
   *                 / Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elIntegerDivisionTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(25);

    Validator.testInteger(testValue, expectedResult, "/");

  }

  /**
   * @testName: elIntegerModulusTest
   * @assertion_ids: EL:SPEC:19.4
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Integer, the result is coerced to Long and
   *                 is the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Integer % Integer Integer % Short Integer
   *                 % Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elIntegerModulusTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(0);

    Validator.testInteger(testValue, expectedResult, "%");

  }

  /**
   * @testName: elShortAddTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Short, the result is coerced to
   *                 Long and is the sum of the operands.
   * 
   *                 Equations tested: Short + Short Short + Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elShortAddTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("3");

    Validator.testShort(testValue, expectedResult, "+");

  }

  /**
   * @testName: elShortSubtractTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Short, the result is coerced
   *                 to Long and is the difference of the operands.
   * 
   *                 Equations tested: Short - Short Short - Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elShortSubtractTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("1");

    Validator.testShort(testValue, expectedResult, "-");

  }

  /**
   * @testName: elShortMultiplyTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Short, the result is
   *                 coerced to Long and is the product of the operands.
   * 
   *                 Equations tested: Short * Short Short * Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elShortMultiplyTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("2");

    Validator.testShort(testValue, expectedResult, "*");

  }

  /**
   * @testName: elShortDivisionTest
   * @assertion_ids: EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Short, the result is coerced to Double and
   *                 is the quotient of the operands.
   * 
   *                 Equations tested: Short / Short Short / Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elShortDivisionTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("2");

    Validator.testShort(testValue, expectedResult, "/");

  }

  /**
   * @testName: elShortModulusTest
   * @assertion_ids: EL:SPEC:19.4
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Short, the result is coerced to Long and is
   *                 the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Short % Short Short % Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elShortModulusTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("0");

    Validator.testShort(testValue, expectedResult, "%");

  }

  /**
   * @testName: elByteAddTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if both operands in an EL "+" (addition)
   *                 operation are Bytes, the result is coerced to Long and is
   *                 the sum of the operands.
   * 
   *                 Equations tested: Byte + Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elByteAddTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("3");

    Validator.testByte(testValue, expectedResult, "+");

  }

  /**
   * @testName: elByteSubtractTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if both operands in an EL "-" (subtraction)
   *                 operation are Bytes, the result is coerced to Long and is
   *                 the difference of the operands.
   * 
   *                 Equations tested: Byte - Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elByteSubtractTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("1");

    Validator.testByte(testValue, expectedResult, "-");

  }

  /**
   * @testName: elByteMultiplyTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if both operands in an EL "*"
   *                 (multiplication) operation are Bytes, the result is coerced
   *                 to Long and is the product of the operands.
   * 
   *                 Equations tested: Byte * Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elByteMultiplyTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("2");

    Validator.testByte(testValue, expectedResult, "*");

  }

  /**
   * @testName: elByteDivisionTest
   * @assertion_ids: EL:SPEC:18.3
   * @test_Strategy: Validate that if both operands in an EL "/" (div) operation
   *                 are Bytes, the result is coerced to Double and is the
   *                 quotient of the operands.
   * 
   *                 Equations tested: Byte / Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elByteDivisionTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("2");

    Validator.testByte(testValue, expectedResult, "/");

  }

  /**
   * @testName: elByteModulusTest
   * @assertion_ids: EL:SPEC:19.4
   * @test_Strategy: Validate that if both operands in an EL "%" (mod) operation
   *                 are Bytes, the result is coerced to Long and is the
   *                 remainder of the quotient of the operands.
   * 
   *                 Equations tested: Byte % Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elByteModulusTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("0");

    Validator.testByte(testValue, expectedResult, "%");

  }

  /**
   * @testName: elBooleanAndTest
   * @assertion_ids: EL:SPEC:23.1; EL:SPEC:24.2.1
   * @test_Strategy: Validate that if one of the operands in an EL "&&", "and"
   *                 operation is a Boolean, the result is coerced to Boolean.
   * 
   *                 Equations tested: Boolean && String Boolean && Boolean
   *                 Boolean and String Boolean and Boolean
   * 
   */
  @Test
  @TargetVehicle("jsp")
  public void elBooleanAndTest() throws Exception {

    Validator.testBoolean(true, "true", true, "&&");
    Validator.testBoolean(true, true, true, "&&");

    Validator.testBoolean(true, "false", false, "and");
    Validator.testBoolean(true, false, false, "and");

  }

  /**
   * @testName: elBooleanOrTest
   * @assertion_ids: EL:SPEC:23.1; EL:SPEC:24.2.1
   * @test_Strategy: Validate that if one of the operands in an EL "||", "or"
   *                 operation is a Boolean, the result is coerced to Boolean.
   * 
   *                 Equations tested: Boolean || String Boolean || Boolean
   *                 Boolean or String Boolean or Boolean
   * 
   */
  @Test
  @TargetVehicle("jsp")
  public void elBooleanOrTest() throws Exception {

    Validator.testBoolean(false, "false", false, "||");
    Validator.testBoolean(true, "false", true, "or");

    Validator.testBoolean(true, false, true, "||");
    Validator.testBoolean(true, true, true, "or");

  }

}
