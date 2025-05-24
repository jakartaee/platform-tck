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

import java.util.Properties;
import java.net.URL;

import com.sun.ts.lib.harness.Fault;
import com.sun.ts.lib.harness.SetupException;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.jsonp.api.common.TestResult;
import com.sun.ts.tests.common.base.ServiceEETest;
import com.sun.ts.lib.harness.Status;

import jakarta.json.JsonPatch;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.asset.StringAsset;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.jboss.arquillian.test.api.ArquillianResource;
import tck.arquillian.protocol.common.TargetVehicle;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import tck.arquillian.porting.lib.spi.TestArchiveProcessor;


import java.lang.System.Logger;

// $Id$
/*
 * RFC 6902: JavaScript Object Notation (JSON) Patch compatibility tests.<br>
 * {@see <a href="https://tools.ietf.org/html/rfc6902">RFC 6902</a>}.
 */
@Tag("tck-appclient")
@Tag("platform")
@Tag("jsonp")
@ExtendWith(ArquillianExtension.class)
public class PatchEjbTest extends ServiceEETest {

  private static final Logger logger = System.getLogger(PatchEjbTest.class.getName());

  private static String packagePath = PatchEjbTest.class.getPackageName().replace(".", "/");

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "STARTING TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
      logger.log(Logger.Level.INFO, "FINISHED TEST : " + testInfo.getDisplayName());
  }

  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "cleanup ok");
  }


  static final String VEHICLE_ARCHIVE = "patchtests_ejb_vehicle";
  
  @TargetsContainer("tck-appclient")
  @OverProtocol("appclient")
  @Deployment(name = VEHICLE_ARCHIVE, testable = true)
  public static EnterpriseArchive createEjbDeployment(@ArquillianResource TestArchiveProcessor archiveProcessor) throws Exception {

    JavaArchive patchtests_ejb_vehicle_client = ShrinkWrap.create(JavaArchive.class, "patchtests_ejb_vehicle_client.jar");
    patchtests_ejb_vehicle_client.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.EmptyVehicleRunner.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRunner.class,
        EETest.class,
        Fault.class,
        SetupException.class,
        ServiceEETest.class,
        com.sun.ts.tests.jsonp.api.common.ArrayBuilder.class,
        com.sun.ts.tests.jsonp.api.common.JsonAssert.class,
        com.sun.ts.tests.jsonp.api.common.JsonIO.class,
        com.sun.ts.tests.jsonp.api.common.JsonPTest.class,
        com.sun.ts.tests.jsonp.api.common.JsonValueType.class,
        com.sun.ts.tests.jsonp.api.common.MergeRFCObject.class,
        com.sun.ts.tests.jsonp.api.common.ObjectBuilder.class,
        com.sun.ts.tests.jsonp.api.common.PointerRFCObject.class,
        com.sun.ts.tests.jsonp.api.common.SimpleValues.class,
        com.sun.ts.tests.jsonp.api.common.TestFail.class,
        com.sun.ts.tests.jsonp.api.common.TestResult.class,
        CommonOperation.class,
        PatchCreate.class,
        PatchEjbTest.class
    );

    URL resURL = PatchEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_client.xml");
    if(resURL != null) {
      patchtests_ejb_vehicle_client.addAsManifestResource(resURL, "application-client.xml");
    }
    resURL = PatchEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_client.jar.sun-application-client.xml");
    if(resURL != null) {
      patchtests_ejb_vehicle_client.addAsManifestResource(resURL, "sun-application-client.xml");
    }
    patchtests_ejb_vehicle_client.addAsManifestResource(new StringAsset("Main-Class: " + PatchEjbTest.class.getName() + "\n"), "MANIFEST.MF");
    archiveProcessor.processClientArchive(patchtests_ejb_vehicle_client, PatchEjbTest.class, resURL);



    JavaArchive patchtests_ejb_vehicle_ejb = ShrinkWrap.create(JavaArchive.class, "patchtests_ejb_vehicle_ejb.jar");
    patchtests_ejb_vehicle_ejb.addClasses(
        com.sun.ts.tests.common.vehicle.VehicleRunnerFactory.class,
        com.sun.ts.tests.common.vehicle.VehicleClient.class,
        com.sun.ts.tests.common.vehicle.VehicleRunnable.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicle.class,
        com.sun.ts.tests.common.vehicle.ejb.EJBVehicleRemote.class,
        EETest.class,
        Fault.class,
        ServiceEETest.class,
        SetupException.class,
        com.sun.ts.tests.jsonp.api.common.ArrayBuilder.class,
        com.sun.ts.tests.jsonp.api.common.JsonAssert.class,
        com.sun.ts.tests.jsonp.api.common.JsonIO.class,
        com.sun.ts.tests.jsonp.api.common.JsonPTest.class,
        com.sun.ts.tests.jsonp.api.common.JsonValueType.class,
        com.sun.ts.tests.jsonp.api.common.MergeRFCObject.class,
        com.sun.ts.tests.jsonp.api.common.ObjectBuilder.class,
        com.sun.ts.tests.jsonp.api.common.PointerRFCObject.class,
        com.sun.ts.tests.jsonp.api.common.SimpleValues.class,
        com.sun.ts.tests.jsonp.api.common.TestFail.class,
        com.sun.ts.tests.jsonp.api.common.TestResult.class,
        com.sun.ts.tests.jsonp.common.JSONP_Util.class,
        CommonOperation.class,
        PatchCreate.class,
        PatchEjbTest.class
    );
    // The ejb-jar.xml descriptor
    URL ejbResURL = PatchEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.xml");
    if(ejbResURL != null) {
      patchtests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "ejb-jar.xml");
    }
    // patchtests_ejb_vehicle_ejb.addAsManifestResource(new StringAsset("Main-Class: " + PatchEjbTest.class.getName() + "\n"), "MANIFEST.MF");

    ejbResURL = PatchEjbTest.class.getClassLoader().getResource(packagePath+"/ejb_vehicle_ejb.jar.sun-ejb-jar.xml");
    if(ejbResURL != null) {
      patchtests_ejb_vehicle_ejb.addAsManifestResource(ejbResURL, "sun-ejb-jar.xml");
    }
    archiveProcessor.processEjbArchive(patchtests_ejb_vehicle_ejb, PatchEjbTest.class, ejbResURL);


    EnterpriseArchive patchtests_ejb_vehicle_client_ear = ShrinkWrap.create(EnterpriseArchive.class, "patchtests_ejb_vehicle.ear");
    // patchtests_ejb_vehicle_client_ear.addAsManifestResource(new StringAsset("Main-Class: " + PatchEjbTest.class.getName() + "\n"), "MANIFEST.MF");
    patchtests_ejb_vehicle_client_ear.addAsModule(patchtests_ejb_vehicle_client);
    patchtests_ejb_vehicle_client_ear.addAsModule(patchtests_ejb_vehicle_ejb);
    return patchtests_ejb_vehicle_client_ear;

  }

    /*
     * @class.setup_props:
     * This is needed by the vehicle base classes
     */
  public void setup(String[] args, Properties p) throws Exception {

  }

  public static void main(String[] args) {
    PatchEjbTest theTests = new PatchEjbTest();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
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
  @TargetVehicle("ejb")
  public void jsonCreatePatch11Test() throws Exception {
    PatchCreate createTest = new PatchCreate();
    final TestResult result = createTest.test();
    result.eval();
  }

}
