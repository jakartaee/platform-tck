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

package com.sun.ts.tests.el.api.jakarta_el.methodinfo;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.common.el.api.expression.ExpressionTest;
import com.sun.ts.tests.el.common.elcontext.SimpleELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;
import com.sun.ts.tests.el.common.util.ResolverType;
import jakarta.el.ELContext;
import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.el.MethodInfo;
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
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.stream.Collectors;

@ExtendWith(ArquillianExtension.class)
@Tag("el")
@Tag("platform")
@Tag("web")
@Tag("tck-javatest")
public class ELClientServletTest extends ServiceEETest {

  static final String VEHICLE_ARCHIVE = "methodinfo_servlet_vehicle";

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

    WebArchive servlet_vehicle_web = ShrinkWrap.create(WebArchive.class, "methodinfo_servlet_vehicle_web.war");
  
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
        com.sun.ts.tests.el.api.jakarta_el.methodinfo.ELClientServletTest.class
    );

    
    InputStream inStream = ELClientServletTest.class.getResourceAsStream("/vehicle/servlet/servlet_vehicle_web.xml");

    // Replace the el_servlet_vehicle in servlet_vehicle_web.xml with the servlet_vehicle name for archive
    String webXml = editWebXmlString(inStream, VEHICLE_ARCHIVE);
    servlet_vehicle_web.setWebXML(new StringAsset(webXml));

    return servlet_vehicle_web;
    /*
    EnterpriseArchive servlet_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "methodinfo_servlet_vehicle.ear");
    servlet_vehicle_ear.addAsModule(servlet_vehicle_web);
    return servlet_vehicle_ear;
    */

  }

  private Properties testProps;

  public static void main(String[] args) {
    ELClientServletTest theTests = new ELClientServletTest();
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
   * @testName: methodInfoTest
   * 
   * @assertion_ids: EL:JAVADOC:411;EL:JAVADOC:412;EL:JAVADOC:413
   * @test_Strategy: Validate the behavior of MethodInfo MethodInfo class
   *                 methods: MethodInfo.getName() MethodInfo.getReturnType()
   *                 MethodInfo.getParamTypes()
   */
  @Test
  @TargetVehicle("servlet")
  public void methodInfoTest() throws Exception {

    StringBuffer buf = new StringBuffer();

    boolean pass1, pass2, pass3, pass4 = false;

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();

      SimpleELContext simpleContext = new SimpleELContext(
          ResolverType.VECT_ELRESOLVER);
      ELContext context = simpleContext.getELContext();

      // case 1: non-null return value
      Class<?>[] paramTypes1 = { Object.class };
      MethodExpression mexp1 = expFactory.createMethodExpression(context,
          "#{vect.add}", boolean.class, paramTypes1);
      MethodInfo minfo1 = mexp1.getMethodInfo(context);
      pass1 = ExpressionTest.testMethodInfo(minfo1, "add", boolean.class, 1,
          paramTypes1, buf);

      // case 2: null return value
      Class<?>[] paramTypes2 = { int.class, Object.class };

      MethodExpression mexp2 = expFactory.createMethodExpression(context,
          "#{vect.add}", null, paramTypes2);
      MethodInfo minfo2 = mexp2.getMethodInfo(context);
      pass2 = ExpressionTest.testMethodInfo(minfo2, "add", void.class, 2,
          paramTypes2, buf);

      // case 3: literal expression returning String
      Class<?>[] paramTypes3 = {};

      MethodExpression mexp3 = expFactory.createMethodExpression(context,
          "true", String.class, paramTypes3);
      MethodInfo minfo3 = mexp3.getMethodInfo(context);
      pass3 = ExpressionTest.testMethodInfo(minfo3, "true", String.class, 0,
          paramTypes3, buf);

      // case 4: literal expression returning non-String value
      Class<?>[] paramTypes4 = {};

      MethodExpression mexp4 = expFactory.createMethodExpression(context,
          "true", Boolean.class, paramTypes4);
      MethodInfo minfo4 = mexp4.getMethodInfo(context);
      pass4 = ExpressionTest.testMethodInfo(minfo4, "true", Boolean.class, 0,
          paramTypes4, buf);

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!(pass1 && pass2 && pass3 && pass4))
      throw new Exception(ELTestUtil.FAIL + buf.toString());
  }
}
