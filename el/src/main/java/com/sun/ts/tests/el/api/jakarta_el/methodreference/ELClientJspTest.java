/*
 * Copyright (c) 2021 Contributors to the Eclipse Foundation.
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
package com.sun.ts.tests.el.api.jakarta_el.methodreference;

import java.util.Properties;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;


import com.sun.ts.tests.el.common.util.Fault;
import com.sun.ts.tests.common.el.api.expression.ExpressionTest;
import com.sun.ts.tests.el.common.elcontext.VarMapperELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.MethodsBean;

import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;
import jakarta.el.MethodNotFoundException;
import jakarta.el.MethodReference;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.ValueExpression;


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

  static final String VEHICLE_ARCHIVE = "methodreference_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "methodreference_jsp_vehicle_web.war");
  
    jsp_vehicle_web.addClasses(
        com.sun.ts.lib.harness.EETest.Fault.class,
        com.sun.ts.lib.harness.EETest.SetupException.class,
        com.sun.ts.lib.harness.EETest.class,
        com.sun.ts.lib.harness.ServiceEETest.class,
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
        com.sun.ts.tests.el.common.elcontext.VarMapperELContext.class,
        com.sun.ts.tests.el.common.util.ELTestUtil.class,
        com.sun.ts.tests.el.common.util.SimpleBean.class,
        com.sun.ts.tests.el.common.util.SimpleInterface.class,
        com.sun.ts.tests.el.common.util.ResolverType.class,
        com.sun.ts.tests.el.common.util.NameValuePair.class,
        com.sun.ts.tests.el.common.util.ExprEval.class,
        com.sun.ts.tests.el.common.util.MethodsBean.class,
        com.sun.ts.tests.el.common.functionmapper.TCKFunctionMapper.class,
        com.sun.ts.tests.el.common.elresolver.EmployeeELResolver.class,
        com.sun.ts.tests.el.common.elresolver.VariableELResolver.class,
        com.sun.ts.tests.el.common.elresolver.VectELResolver.class,
        com.sun.ts.tests.el.common.elresolver.FunctionELResolver.class,
        com.sun.ts.tests.el.api.jakarta_el.methodreference.ELClientJspTest.class
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

    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "methodreference_jsp_vehicle.ear");
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

  private static final String NL = System.getProperty("line.seperator", "\n");

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
  }


  /**
   * @testName: methodReferenceTest
   * 
   * @assertion_ids: EL:JAVADOC:87; EL:JAVADOC:88; EL:JAVADOC:89
   * @test_Strategy: Validate the behavior of MethodReference class methods:
   *                 MethodReference.getBase() MethodReference.getMethodInfo()
   *                 MethodReference.getAnnotations()
   *                 MethodReference.getEvaluatedParameters
   */
  @Test
  @TargetVehicle("jsp")
  public void  methodReferenceTest() throws Fault {

    StringBuffer buf = new StringBuffer();

    boolean pass1, pass2, pass3, pass4;

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      ELContext context = (new VarMapperELContext(testProps)).getELContext();
      
      MethodsBean bean = new MethodsBean();
      ValueExpression ve = expFactory.createValueExpression(bean, MethodsBean.class);
      context.getVariableMapper().setVariable("bean", ve);

      // case 1: non-null return value
      MethodExpression mexp1 = expFactory.createMethodExpression(
          context, "#{bean.targetF('aaa',1234)}", String.class, null);
      MethodInfo minfo1 = mexp1.getMethodInfo(context);
      MethodReference mref1 = mexp1.getMethodReference(context);
      pass1 = ExpressionTest.testMethodReference(mref1, bean, minfo1,
          new Class<?>[] { Deprecated.class },
          new Object[] { "aaa", Long.valueOf(1234) },
          buf);

      // case 2: NPE
      try {
        mexp1.getMethodReference(null);
        pass2 = false;
        buf.append("Did not get expected NullPointerException for null context." + NL);
      } catch (NullPointerException npe) {
        pass2 = true;
      }

      // case 3: PNFE
      MethodExpression mexp3 = expFactory.createMethodExpression(
          context, "#{noSuchBean.method()}", String.class, null);
      try {
        mexp3.getMethodReference(context);
        pass3 = false;
        buf.append("Did not get expected PropertyNotFoundException return for unknown bean." + NL);
      } catch (PropertyNotFoundException pnfe) {
        pass3 = true;
      }

      // case 4: MNFE
      MethodExpression mexp4 = expFactory.createMethodExpression(
          context, "#{bean.noSuchMethod()}", String.class, null);
      try {
        mexp4.getMethodReference(context);
        pass4 = false;
        buf.append("Did not get expected MethodNotFoundException return for unknown bean." + NL);
      } catch (MethodNotFoundException mnfe) {
        pass4 = true;
      }
      
    } catch (Exception ex) {
      TestUtil.logErr("Test getMethodReference threw an Exception!" + ELTestUtil.NL +
          "Received: " + ex.toString() + ELTestUtil.NL);
      ex.printStackTrace();
      throw new Fault(ex);
    }

    if (!(pass1 && pass2 && pass3 && pass4))
      throw new Fault(ELTestUtil.FAIL + NL + buf.toString());
  }
}
