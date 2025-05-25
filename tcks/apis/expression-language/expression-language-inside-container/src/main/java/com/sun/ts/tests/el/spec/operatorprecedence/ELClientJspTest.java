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

package com.sun.ts.tests.el.spec.operatorprecedence;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.el.common.elcontext.FuncMapperELContext;
import com.sun.ts.tests.el.common.util.ExprEval;
import jakarta.el.ELException;
import jakarta.el.ExpressionFactory;
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

  static final String VEHICLE_ARCHIVE = "operatorprecedence_jsp_vehicle";

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

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "operatorprecedence_jsp_vehicle_web.war");
  
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
        com.sun.ts.tests.el.spec.operatorprecedence.ELClientJspTest.class
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
    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "operatorprecedence_jsp_vehicle.ear");
    jsp_vehicle_ear.addAsModule(jsp_vehicle_web);
    return jsp_vehicle_ear;
    */

  }

  Properties testProps;

  private static final String[] MODOPER = { "%", "mod" };

  private static final String[] DIVOPER = { "/", "div" };

  private static final String[] ANDOPER = { "&&", "and" };

  private static final String[] OROPER = { "||", "or" };

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
   * @testName: elMultiPreBinaryTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*" is evaluated before "+" "*" is evaluated before "-"
   */
  @Test
  @TargetVehicle("jsp")
  public void elMultiPreBinaryTest() throws Exception {

    this.testOrderPrecedence("{1 + 5 * 2}", Long.valueOf(11));
    this.testOrderPrecedence("{1 - 5 * 2}", Long.valueOf(-9));

  }

  /*
   * @testName: elDivPreBinaryTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /" is evaluated before "+" "div, /" is evaluated before
   * "-"
   */
  @Test
  @TargetVehicle("jsp")
  public void elDivPreBinaryTest() throws Exception {

    for (String s : DIVOPER) {
      this.testOrderPrecedence("{1 + 4 " + s + " 2}", Double.valueOf(3));
      this.testOrderPrecedence("{1 - 4 " + s + " 2}", Double.valueOf(-1));
    }

  }

  /*
   * @testName: elModPreBinaryTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %" is evaluated before "+" "mod, %" is evaluated before
   * "-"
   */
  @Test
  @TargetVehicle("jsp")
  public void elModPreBinaryTest() throws Exception {

    for (String s : MODOPER) {
      this.testOrderPrecedence("{1 + 7 " + s + " 2}", Long.valueOf(2));
      this.testOrderPrecedence("{1 - 7 " + s + " 2}", Long.valueOf(0));
    }

  }

  /*
   * @testName: elMultiPreRelationalTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*" is evaluated before ">" "*" is evaluated before "<" "*"
   * is evaluated before ">=" "*" is evaluated before "<=" "*" is evaluated
   * before "lt" "*" is evaluated before "gt" "*" is evaluated before "le" "*"
   * is evaluated before "ge"
   *
   * "*" is evaluated before "==" "*" is evaluated before "!=" "*" is evaluated
   * before "eq" "*" is evaluated before "ne"
   */
  @Test
  @TargetVehicle("jsp")
  public void elMultiPreRelationalTest() throws Exception {

    this.testOrderPrecedence("{6 > 5 * 2}", false);
    this.testOrderPrecedence("{3 * 2 < 8}", true);
    this.testOrderPrecedence("{6 >= 5 * 2}", false);
    this.testOrderPrecedence("{6 * 2 <= 12}", true);
    this.testOrderPrecedence("{5 * 1 gt 6}", false);
    this.testOrderPrecedence("{6 lt 5 * 2}", true);
    this.testOrderPrecedence("{5 * 1 ge 6}", false);
    this.testOrderPrecedence("{6 le 5 * 2}", true);

    this.testOrderPrecedence("{5 == 5 * 2}", false);
    this.testOrderPrecedence("{5 * 2 != 10}", false);
    this.testOrderPrecedence("{10 eq 5 * 2}", true);
    this.testOrderPrecedence("{15 * 1 ne 1}", true);

  }

  /*
   * @testName: elDivPreRelationalTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /" is evaluated before ">" "div, /" is evaluated before
   * "<" "div, /" is evaluated before ">=" "div, /" is evaluated before "<="
   * "div, /" is evaluated before "lt" "div, /" is evaluated before "gt"
   * "div, /" is evaluated before "le" "div, /" is evaluated before "ge"
   *
   * "div, /" is evaluated before "==" "div, /" is evaluated before "!="
   * "div, /" is evaluated before "eq" "div, /" is evaluated before "ne"
   */
  @Test
  @TargetVehicle("jsp")
  public void elDivPreRelationalTest() throws Exception {

    for (String s : DIVOPER) {
      this.testOrderPrecedence("{3 > 4  " + s + " 2}", true);
      this.testOrderPrecedence("{12 " + s + " 2 < 5}", false);
      this.testOrderPrecedence("{4 >= 6 " + s + " 24}", true);
      this.testOrderPrecedence("{16 " + s + " 2 <= 5}", false);
      this.testOrderPrecedence("{6 gt 5 " + s + " 2}", true);
      this.testOrderPrecedence("{12 " + s + " 1 lt 5}", false);
      this.testOrderPrecedence("{6 ge 5 " + s + " 2}", true);
      this.testOrderPrecedence("{50 " + s + " 2 le 5}", false);

      this.testOrderPrecedence("{1 == 2 " + s + " 2}", true);
      this.testOrderPrecedence("{10 " + s + " 5 != 5}", true);
      this.testOrderPrecedence("{5 eq 5 " + s + " 2}", false);
      this.testOrderPrecedence("{2 ne 4 " + s + " 2}", false);
    }

  }

  /*
   * @testName: elModPreRelationalTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %" is evaluated before ">" "mod, %" is evaluated before
   * "<" "mod, %" is evaluated before ">=" "mod, %" is evaluated before "<="
   * "mod, %" is evaluated before "lt" "mod, %" is evaluated before "gt"
   * "mod, %" is evaluated before "le" "mod, %" is evaluated before "ge"
   *
   * "mod, %" is evaluated before "==" "mod, %" is evaluated before "!="
   * "mod, %" is evaluated before "eq" "mod, %" is evaluated before "ne"
   */
  @Test
  @TargetVehicle("jsp")
  public void elModPreRelationalTest() throws Exception {

    for (String s : MODOPER) {
      this.testOrderPrecedence("{4 " + s + " 15 > 1}", true);
      this.testOrderPrecedence("{5 < 6 " + s + " 2}", false);
      this.testOrderPrecedence("{6 " + s + " 29 >= 5}", true);
      this.testOrderPrecedence("{6 <= 5 " + s + " 2}", false);
      this.testOrderPrecedence("{3 " + s + " 8 gt 1}", true);
      this.testOrderPrecedence("{6 lt 5 " + s + " 2}", false);
      this.testOrderPrecedence("{8 " + s + " 5 ge 2}", true);
      this.testOrderPrecedence("{6 le 5 " + s + " 2}", false);

      this.testOrderPrecedence("{3 " + s + " 2 == 1}", true);
      this.testOrderPrecedence("{5 != 5 " + s + " 2}", true);
      this.testOrderPrecedence("{6 " + s + " 2 eq 5}", false);
      this.testOrderPrecedence("{2 ne 5 " + s + " 3}", false);
    }

  }

  /*
   * @testName: elMultiEqualPreAndTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*, ==" is evaluated before "&&, and"
   */
  @Test
  @TargetVehicle("jsp")
  public void elMultiEqualPreAndTest() throws Exception {

    for (String a : ANDOPER) {
      this.testOrderPrecedence("{10 == 5 * 2 " + a + " 6 * 2 == 15}", false);
      this.testOrderPrecedence("{10 == 5 * 2 " + a + " 6 * 2 == 12}", true);
    }

  }

  /*
   * @testName: elDivEqualPreAndTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /, ==" is evaluated before "&&, and"
   */
  @Test
  @TargetVehicle("jsp")
  public void elDivEqualPreAndTest() throws Exception {

    for (String d : DIVOPER) {
      for (String a : ANDOPER) {
        this.testOrderPrecedence(
            "{12 " + d + " 2 == 6 " + a + " 10 " + d + " 2  == 5}", true);
        this.testOrderPrecedence(
            "{12 " + d + " 3 == 6 " + a + " 10 " + d + " 2  == 5}", false);
      }
    }

  }

  /*
   * @testName: elModEqualPreAndTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %, ==" is evaluated before "&&, and"
   */
  @Test
  @TargetVehicle("jsp")
  public void elModEqualPreAndTest() throws Exception {

    for (String m : MODOPER) {
      for (String a : ANDOPER) {
        this.testOrderPrecedence(
            "{15 " + m + " 4 == 3 " + a + " 3 " + m + " 3 == 0}", true);
        this.testOrderPrecedence(
            "{15 " + m + " 3 == 3 " + a + " 3 " + m + " 3 == 0}", false);
      }
    }

  }

  /*
   * @testName: elMultiEqualOrCondTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*, ==" is evaluated before "||, or"
   */
  @Test
  @TargetVehicle("jsp")
  public void elMultiEqualOrCondTest() throws Exception {

    for (String o : OROPER) {
      this.testOrderPrecedence("{10 == 5 * 2 " + o + " 6 * 2 == 15}", true);
      this.testOrderPrecedence("{10 == 5 * 5 " + o + " 6 * 2 == 12}", true);
      this.testOrderPrecedence("{10 == 5 * 5 " + o + " 6 * 6 == 12}", false);
    }

  }

  /*
   * @testName: elDivEqualPreOrTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /, ==" is evaluated before "||, or"
   */
  @Test
  @TargetVehicle("jsp")
  public void elDivEqualPreOrTest() throws Exception {

    for (String d : DIVOPER) {
      for (String o : OROPER) {
        this.testOrderPrecedence(
            "{12 " + d + " 2 == 6 " + o + " 10 " + d + " 5  == 5}", true);
        this.testOrderPrecedence(
            "{12 " + d + " 3 == 6 " + o + " 10 " + d + " 2  == 5}", true);
        this.testOrderPrecedence(
            "{12 " + d + " 3 == 6 " + o + " 10 " + d + " 5  == 5}", false);
      }
    }

  }

  /*
   * @testName: elModEqualPreOrTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %, ==" is evaluated before "||, or"
   */
  @Test
  @TargetVehicle("jsp")
  public void elModEqualPreOrTest() throws Exception {

    for (String m : MODOPER) {
      for (String o : OROPER) {
        this.testOrderPrecedence(
            "{15 " + m + " 4 == 3 " + o + " 4 " + m + " 3 == 0}", true);
        this.testOrderPrecedence(
            "{15 " + m + " 3 == 3 " + o + " 3 " + m + " 3 == 0}", true);
        this.testOrderPrecedence(
            "{15 " + m + " 3 == 3 " + o + " 8 " + m + " 3 == 0}", false);
      }
    }
  }

  /*
   * @testName: elMultiEqualPreCondTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*" is evaluated before "? :"
   */
  @Test
  @TargetVehicle("jsp")
  public void elMultiEqualPreCondTest() throws Exception {

    // These tests are designed to return the false if correct.
    this.testOrderPrecedence("{5 * 2 == 10 ? false : true}", false);
    this.testOrderPrecedence("{5 * 5 == 10 ? false : true}", true);

  }

  /*
   * @testName: elDivEqualPreCondTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /, ==" is evaluated before "? :"
   */
  @Test
  @TargetVehicle("jsp")
  public void elDivEqualPreCondTest() throws Exception {

    // These tests are designed to return the false if correct.
    for (String d : DIVOPER) {
      this.testOrderPrecedence("{20 " + d + " 2 == 10 ? false : true}", false);
      this.testOrderPrecedence("{24 " + d + " 2 == 10 ? false : true}", true);
    }
  }

  /*
   * @testName: elModEqualPreCondTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %, ==" is evaluated before "||, or"
   */
  @Test
  @TargetVehicle("jsp")
  public void elModEqualPreCondTest() throws Exception {

    for (String m : MODOPER) {
      this.testOrderPrecedence("{21 " + m + " 2 == 1 ? false : true}", false);
      this.testOrderPrecedence("{15 " + m + " 3 == 3 ? false : true}", true);
    }
  }

  /*
   * @testName: elParenPreBinaryTest
   * 
   * @assertion_ids: EL:SPEC:28; EL:SPEC:27
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "( )" is evaluated before "+" "( )" is evaluated before "-"
   * "( )" is evaluated before "*" "( )" is evaluated before "/" "( )" is
   * evaluated before "%"
   */
  @Test
  @TargetVehicle("jsp")
  public void elParenPreBinaryTest() throws Exception {

    // "+" tests
    this.testOrderPrecedence("{(2 + 3) - 10}", Long.valueOf(-5));
    this.testOrderPrecedence("{10 - (2 + 3)}", Long.valueOf(5));

    // "-" tests
    this.testOrderPrecedence("{(1 - 5) + 2}", Long.valueOf(-2));
    this.testOrderPrecedence("{2 + (5 - 1)}", Long.valueOf(6));

    // "*" tests
    this.testOrderPrecedence("{(1 + 5) * 2}", Long.valueOf(12));
    this.testOrderPrecedence("{2 * (1 + 5)}", Long.valueOf(12));

    // "/" tests
    this.testOrderPrecedence("{(4 + 4) / 2}", Double.valueOf(4));
    this.testOrderPrecedence("{2 / (4 + 4)}", Double.valueOf(0.25));

    // "%" tests
    this.testOrderPrecedence("{(2 + 7) % 2}", Long.valueOf(1));
    this.testOrderPrecedence("{18 % (8 + 7)}", Long.valueOf(3));

  }

  /*
   * @testName: functionPrecedenceTest
   * 
   * @assertion_ids: EL:SPEC:29
   * 
   * @test_Strategy: Validate that qualified functions with a namespace prefix
   * have precedence over the operators by constructing an expression which
   * cannot be parsed due to this rule.
   */
  @Test
  @TargetVehicle("jsp")
  public void functionPrecedenceTest() throws Exception {

    boolean pass = false;

    ExpressionFactory expFactory = ExpressionFactory.newInstance();
    FuncMapperELContext context = new FuncMapperELContext();
    expFactory.createValueExpression(context, "${Int:val(10)}", Object.class);
    try {
      expFactory.createValueExpression(context, "${a?Int:val(10)}",
          Object.class);
    } catch (ELException ex) {
      pass = true;
    }

    if (!pass)
      throw new Exception("function precedence failed");

  }

  // ---------------------------------------------------------- private methods

  private void testOrderPrecedence(String testExpr, Object expectedResult)
      throws Exception {

    boolean pass = false;

    String[] symbol = { "$", "#" };
    String expr;

    try {
      for (String prefix : symbol) {
        expr = prefix + testExpr;

        TestUtil.logTrace("Expression to test: " + expr);

        Object result = ExprEval.evaluateValueExpression(expr, null,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());
        pass = (ExprEval.compareValue(result, expectedResult));

        if (!pass)
          throw new Exception("TEST FAILED: pass = false");
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
  }
}
