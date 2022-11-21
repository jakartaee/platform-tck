/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.common.web.WebServer;

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
  public void setup(String[] args, Properties props) throws Exception {
    boolean ok;

    try {
      logMsg("[Client] setup()");
      webServer = WebServer.newInstance(props);
    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
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
  public void testCharacterEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testCharacterEntry");
      if (!pass) {
        throw new Exception("testCharacterEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testCharacterEntry failed", e);
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
  public void testStringEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testStringEntry");
      if (!pass) {
        throw new Exception("testStringEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testStringEntry failed", e);
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
  public void testBooleanEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testBooleanEntry");
      if (!pass) {
        throw new Exception("testBooleanEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testBooleanEntry failed", e);
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
  public void testByteEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testByteEntry");
      if (!pass) {
        throw new Exception("testByteEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testByteEntry failed", e);
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
  public void testShortEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testShortEntry");
      if (!pass) {
        throw new Exception("testShortEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testShortEntry failed", e);
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
  public void testIntegerEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testIntegerEntry");
      if (!pass) {
        throw new Exception("testIntegerEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testIntegerEntry failed", e);
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
  public void testLongEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testLongEntry");
      if (!pass) {
        throw new Exception("testLongEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testLongEntry failed", e);
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
  public void testFloatEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testFloatEntry");
      if (!pass) {
        throw new Exception("testFloatEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testFloatEntry failed", e);
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
  public void testDoubleEntry() throws Exception {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testDoubleEntry");
      if (!pass) {
        throw new Exception("testDoubleEntry failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Exception("testDoubleEntry failed", e);
    }
  }

  public void cleanup() throws Exception {
    logMsg("[Client] cleanup()");
  }

}
