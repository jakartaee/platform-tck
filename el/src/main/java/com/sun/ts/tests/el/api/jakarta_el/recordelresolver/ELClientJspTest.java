/*
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package com.sun.ts.tests.el.api.jakarta_el.recordelresolver;

import java.lang.System.Logger;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import java.util.Properties;

import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ELContext;
import jakarta.el.RecordELResolver;

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

  static final String VEHICLE_ARCHIVE = "recordelresolver_jsp_vehicle";

  private static final Logger logger = System.getLogger(ELClientJspTest.class.getName());

  @TargetsContainer("tck-javatest")
  @OverProtocol("javatest")
  @Deployment(name = VEHICLE_ARCHIVE, order = 2)
  public static EnterpriseArchive createDeploymentVehicle() throws IOException {

    WebArchive jsp_vehicle_web = ShrinkWrap.create(WebArchive.class, "recordelresolver_jsp_vehicle_web.war");
  
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
        com.sun.ts.tests.el.api.jakarta_el.recordelresolver.ELClientJspTest.class
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

    EnterpriseArchive jsp_vehicle_ear = ShrinkWrap.create(EnterpriseArchive.class, "recordelresolver_jsp_vehicle.ear");
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
   * @testName: recordELResolverTest
   *
   * @test_Strategy: Verify that API calls work as expected for: arrayELResolver() getValue() getType() setValue()
   * isReadOnly() getCommonPropertyType() getFeatureDescriptors(). Records should behave similarly to read-only beans.
   */
  @Test
  @TargetVehicle("jsp")
  public void  recordELResolverTest() throws Exception {

    boolean pass;
    StringBuffer buf = new StringBuffer();
    TestRecord testRecord = new TestRecord("valueA", "valueB");

    try {
      RecordELResolver recordResolver = new RecordELResolver();
      BareBonesELContext barebonesContext = new BareBonesELContext();
      ELContext context = barebonesContext.getELContext();

      pass = ResolverTest.testELResolver(context, recordResolver, testRecord, "propertyA", "newValue", buf, true);
    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
    TestUtil.logTrace(buf.toString());
  }
  
  
  public record TestRecord(String propertyA, String propertyB) { }
}
