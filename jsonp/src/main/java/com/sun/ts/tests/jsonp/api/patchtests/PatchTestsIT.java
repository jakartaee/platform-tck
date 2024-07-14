/*
 * Copyright (c) 2024 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsonp.api.patchtests;

import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.StringReader;
import com.sun.ts.tests.jsonp.api.common.JsonPTest;
import com.sun.ts.tests.jsonp.api.common.TestResult;

import jakarta.json.JsonPatch;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.api.asset.StringAsset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.test.api.ArquillianResource;

import java.lang.System.Logger;

// $Id$
/*
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 */
@ExtendWith(ArquillianExtension.class)
public class PatchTestsIT extends JsonPTest {

  private static final Logger logger = System.getLogger(PatchTestsIT.class.getName());

  private static String packagePath = PatchTestsIT.class.getPackageName().replace(".", "/");

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  @Deployment(testable = false)
  public static WebArchive createServletDeployment() throws IOException {
  
    WebArchive war = ShrinkWrap.create(WebArchive.class, "patchtests_servlet_vehicle_web.war");
    war.addClass(PatchTestsIT.class);
    war.addClass(CommonOperation.class);
    war.addClass(PatchCreate.class);

    war.addClass(com.sun.ts.tests.common.vehicle.servlet.ServletVehicle.class);
    war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class);
    war.addClass(com.sun.ts.tests.common.vehicle.VehicleRunnable.class);
    war.addClass(com.sun.ts.tests.common.vehicle.VehicleClient.class);
    war.addClass(com.sun.ts.lib.harness.EETest.class);
    war.addClass(com.sun.ts.lib.harness.RemoteStatus.class);
    war.addClass(com.sun.javatest.Status.class);
    war.addClass(com.sun.ts.lib.harness.ServiceEETest.class);

    war.addClass(com.sun.ts.tests.jsonp.api.common.ArrayBuilder.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.JsonAssert.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.JsonIO.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.JsonPTest.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.JsonValueType.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.MergeRFCObject.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.ObjectBuilder.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.PointerRFCObject.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.SimpleValues.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.TestFail.class);
    war.addClass(com.sun.ts.tests.jsonp.api.common.TestResult.class);
    war.addClass(com.sun.ts.tests.jsonp.common.JSONP_Util.class);

    war.setWebXML(PatchTestsIT.class.getClassLoader().getResource(packagePath+"/servlet_vehicle_web.xml"));

    return war;

  }

  /**
   * Test {@link JsonPatch} factory methods added in JSON-P 1.1.
   *
   * @throws Exception
   *           when this test failed.
   *
   * @testName: jsonCreatePatch11Test
   *
   * @assertion_ids: JSONP:JAVADOC:574; JSONP:JAVADOC:579; JSONP:JAVADOC:581;
   *                 JSONP:JAVADOC:653; JSONP:JAVADOC:658; JSONP:JAVADOC:660;
   *                 JSONP:JAVADOC:620; JSONP:JAVADOC:621;
   *
   * @test_Strategy: Tests JsonPatch API factory methods added in JSON-P 1.1.
   */
  @Test
  public void jsonCreatePatch11Test() throws Exception {
    PatchCreate createTest = new PatchCreate();
    final TestResult result = createTest.test();
    result.eval();
  }

}
