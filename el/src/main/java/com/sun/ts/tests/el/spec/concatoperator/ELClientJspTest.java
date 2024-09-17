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

package com.sun.ts.tests.el.spec.concatoperator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.TestNum;
import com.sun.ts.tests.el.common.util.Validator;

import jakarta.el.ELProcessor;

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

  static final String VEHICLE_ARCHIVE = "concatoperator_jsp_vehicle";

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

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "concatoperator_jsp_vehicle_web.war");
  
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
        com.sun.ts.tests.el.spec.concatoperator.ELClientJspTest.class
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
    
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "concatoperator_jsp_vehicle.ear");
    jsp_vehicle_ear.addAsModule(jsp_vehicle_web);
    return jsp_vehicle_ear;

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
   * @testName: elBigDecimalConcatenationTest
   * 
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * 
   * @test_Strategy: Validate that if one of the operands is BigDecimal that the
   *                 operator is '+=' that both operands are coerced to type
   *                 String and concatenated.
   * 
   *                 Equations tested: BigDecimal += BigDecimal BigDecimal +=
   *                 Double BigDecimal += Float BigDecimal += String
   *                 containing".", "e", or "E" BigDecimal += BigInteger
   *                 BigDecimal += Integer BigDecimal += Long BigDecimal +=
   *                 Short BigDecimal += Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigDecimalConcatenationTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(10.531);
    /*
     * The expected result is actually computed in the testBigDecimal method for
     * the '+=' operator!
     */
    Validator.testBigDecimal(testValue, null, "+=");

  }

  /**
   * @testName: elBigIntegerConcatenationTest
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * @test_Strategy: Validate that if one of the operands is BigInteger that the
   *                 operator is '+=' that both operands are coerced to type
   *                 String and concatenated.
   * 
   *                 Equations tested: BigInteger += BigInteger BigInteger +=
   *                 Integer BigInteger += Long BigInteger += Short BigInteger
   *                 += Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elBigIntegerConcatenationTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    /*
     * The expected result is actually computed in the testBigInteger method for
     * the '+=' operator!
     */
    Validator.testBigInteger(testValue, null, "+=");

  }

  /**
   * @testName: elFloatConcatenationTest
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * @test_Strategy: Validate when the operator is '+=' that the operands are
   *                 coerced to Strings and the result is a Concatenation.
   * 
   *                 Equations tested: Float + Double Float + Float Float +
   *                 String containing ".", "e", or "E" Float + BigInteger Float
   *                 + Integer Float + Long Float + Short Float + Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elFloatConcatenationTest() throws Exception {

    // For each float in this List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      /*
       * The expected result is actually computed in the testFloat method for
       * the '+=' operator!
       */
      Validator.testFloat(testValue, null, "+=");
    }

  }

  /**
   * @testName: elDoubleConcatenationTest
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * @test_Strategy: Validate when the operator is '+=' that the operands are
   *                 coerced to Strings and the result is a Concatenation.
   * 
   *                 Equations tested: Double += Double Double += String
   *                 containing ".", "e", or "E" Double += BigInteger Double +=
   *                 Integer Double += Long Double += Short Double += Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elDoubleConcatenationTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    /*
     * The expected result is actually computed in the testDouble method for the
     * '+=' operator!
     */
    Validator.testDouble(testValue, null, "+=");

  }

  /**
   * @testName: elLongConcatenationTest
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * @test_Strategy: Validate when the operator is '+=' that the operands are
   *                 coerced to Strings and the result is a Concatenation.
   * 
   *                 Equations tested: Long += Integer Long += Long Long +=
   *                 Short Long += Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elLongConcatenationTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    /*
     * The expected result is actually computed in the testLong method for the
     * '+=' operator!
     */
    Validator.testLong(testValue, null, "+=");

  }

  /**
   * @testName: elIntegerConcatenationTest
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * @test_Strategy: Validate when the operator is '+=' that the operands are
   *                 coerced to Strings and the result is a Concatenation.
   * 
   *                 Equations tested: Integer += Integer Integer += Short
   *                 Integer += Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elIntegerConcatenationTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    /*
     * The expected result is actually computed in the testInteger method for
     * the '+=' operator!
     */
    Validator.testInteger(testValue, null, "+=");

  }

  /**
   * @testName: elShortConcatenationTest
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * @test_Strategy: Validate when the operator is '+=' that the operands are
   *                 coerced to Strings and the result is a Concatenation.
   * 
   *                 Equations tested: Short += Short Short += Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elShortConcatenationTest() throws Exception {

    Short testValue = Short.valueOf("2");
    /*
     * The expected result is actually computed in the testShort method for the
     * '+=' operator!
     */
    Validator.testShort(testValue, null, "+=");

  }

  /**
   * @testName: elByteConcatenationTest
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * @test_Strategy: Validate that if the operator is '+=' that both operands
   *                 are coerced to String and the result is a Concatenation of
   *                 the operands.
   * 
   *                 Equations tested: Byte += Byte
   */
  @Test
  @TargetVehicle("jsp")
  public void elByteConcatenationTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    /*
     * The expected result is actually computed in the testByte method for the
     * '+=' operator!
     */
    Validator.testByte(testValue, null, "+=");

  }

  /**
   * @testName: elBooleanConcatenationTest
   * @assertion_ids: EL:SPEC:38; EL:SPEC:47.1; EL:SPEC:47.1.1; EL:SPEC:47.1.2
   * @test_Strategy: Validate when the operator is '+=' that both operands are
   *                 coerced to Strings and that they result is a Concatenation
   *                 of the operands.
   * 
   *                 Equations tested: Boolean += String Boolean += Boolean
   * 
   */
  @Test
  @TargetVehicle("jsp")
  public void elBooleanConcatenationTest() throws Exception {

    /*
     * The expected result is actually computed in the testBoolean method for
     * the '+=' operator!
     */
    Validator.testBoolean(false, "true", null, "+=");
    Validator.testBoolean(false, true, null, "+=");

  }

  // ------------------------------------------------------- private methods
  private void logLine(String s) {
    TestUtil.logTrace(s);
  }

  /**
   * Test a query for the correct value.
   * 
   * @param name
   *          The Name of the test
   * @param query
   *          The EL query string
   * @param expected
   *          The expected result of the query.
   */
  private void testQuery(String name, String query, String expected)
      throws Exception {
    ELProcessor elp = new ELProcessor();

    logLine("=== Testing " + name + " ===");
    logLine(query);

    logLine(" = returns =");
    Object ret = elp.eval(query);

    if (!expected.equals(ret.toString())) {

      throw new Exception(
          ELTestUtil.FAIL + "  Unexpected Value!" + ELTestUtil.NL + "Expected: "
              + expected + ELTestUtil.NL + "Received: " + ret.toString());

    }
  }

}
