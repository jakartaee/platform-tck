/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.servlet.ee.platform.deploy.enventry.single;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.common.web.WebServer;

import java.util.Properties;

public class Client extends EETest {

  private static final String webAlias = "/servlet_ee_platform_deploy_enventry_single_web/testDriver";

  protected WebServer webServer;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * Test setup
   *
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     webServerHost, the web server host; webServerPort, the
   *                     web server port;
   */
  public void setup(String[] args, Properties props) throws Fault {
    boolean ok;

    try {
      logMsg("[Client] setup()");
      webServer = WebServer.newInstance(props);
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testCharacterEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.2
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a Character environment entry. Deploy the WAR and
   *                 have the servlet lookup the environment entry at runtime.
   *                 Check that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   */
  public void testCharacterEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testCharacterEntry");
      if (!pass) {
        throw new Fault("testCharacterEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testCharacterEntry failed", e);
    }
  }

  /**
   * @testName: testStringEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.1
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a String environment entry. Deploy the WAR and
   *                 have the servlet lookup the environment entry at runtime.
   *                 Check that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   */
  public void testStringEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testStringEntry");
      if (!pass) {
        throw new Fault("testStringEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testStringEntry failed", e);
    }
  }

  /**
   * @testName: testBooleanEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.7
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a Boolean environment entry. Deploy the WAR and
   *                 have the servlet lookup the environment entry at runtime.
   *                 Check that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   */
  public void testBooleanEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testBooleanEntry");
      if (!pass) {
        throw new Fault("testBooleanEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testBooleanEntry failed", e);
    }
  }

  /**
   * @testName: testByteEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.3
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a Byte environment entry. Deploy the WAR and have
   *                 the servlet lookup the environment entry at runtime. Check
   *                 that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   */
  public void testByteEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testByteEntry");
      if (!pass) {
        throw new Fault("testByteEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testByteEntry failed", e);
    }
  }

  /**
   * @testName: testShortEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.4
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a Short environment entry. Deploy the WAR and have
   *                 the servlet lookup the environment entry at runtime. Check
   *                 that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   */
  public void testShortEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testShortEntry");
      if (!pass) {
        throw new Fault("testShortEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testShortEntry failed", e);
    }
  }

  /**
   * @testName: testIntegerEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.5
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a Integer environment entry. Deploy the WAR and
   *                 have the servlet lookup the environment entry at runtime.
   *                 Check that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   * @test_Strategy:
   *
   */
  public void testIntegerEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testIntegerEntry");
      if (!pass) {
        throw new Fault("testIntegerEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testIntegerEntry failed", e);
    }
  }

  /**
   * @testName: testLongEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.8
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a Long environment entry. Deploy the WAR and have
   *                 the servlet lookup the environment entry at runtime. Check
   *                 that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   * @test_Strategy:
   *
   */
  public void testLongEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testLongEntry");
      if (!pass) {
        throw new Fault("testLongEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testLongEntry failed", e);
    }
  }

  /**
   * @testName: testFloatEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.9
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a Float environment entry. Deploy the WAR and have
   *                 the servlet lookup the environment entry at runtime. Check
   *                 that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   * @test_Strategy:
   *
   */
  public void testFloatEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testFloatEntry");
      if (!pass) {
        throw new Fault("testFloatEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testFloatEntry failed", e);
    }
  }

  /**
   * @testName: testDoubleEntry
   *
   * @assertion_ids: Servlet:platform:116.1; JavaEE:platform:103.6
   *
   * @test_Strategy: Package a servlet in a WAR file whose Deployment Descriptor
   *                 declares a Double environment entry. Deploy the WAR and
   *                 have the servlet lookup the environment entry at runtime.
   *                 Check that the runtime value match the one declared in the
   *                 Deployment Descriptor.
   */
  public void testDoubleEntry() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testDoubleEntry");
      if (!pass) {
        throw new Fault("testDoubleEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testDoubleEntry failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
