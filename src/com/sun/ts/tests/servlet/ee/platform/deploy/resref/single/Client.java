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

package com.sun.ts.tests.servlet.ee.platform.deploy.resref.single;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.common.web.WebServer;

import java.util.Properties;

public class Client extends EETest {

  private static final String webAlias = "/servlet_ee_platform_deploy_resref_single_web/testDriver";

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
   *                     web server port; mailuser1, the email recipient;
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
   * @testName: testDatasource
   *
   * @assertion_ids: JavaEE:platform:10125
   *
   * @test_Strategy: Package a Servlet in a WAR file declaring a resource
   *                 reference for a javax.sql.Datasource.
   *
   *                 Check that: - We can deploy the application. - The Servlet
   *                 can lookup the datasource. - We can use it to open a DB
   *                 connection.
   */
  public void testDatasource() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testDatasource");
      if (!pass) {
        throw new Fault("testDatasource failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testDatasource failed", e);
    }
  }

  /**
   * @testName: testSession
   *
   * @assertion_ids: JavaEE:platform:10125
   *
   * @test_Strategy: Package a Servlet in a WAR file declaring a resource
   *                 reference for a javax.mail.Session.
   *
   *                 Check that: - We can deploy the application. - The Servlet
   *                 can lookup the mail session. - We can use this factory to
   *                 send a mail.
   */
  public void testSession() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testSession");
      if (!pass) {
        throw new Fault("testSession failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testSession failed", e);
    }
  }

  /**
   * @testName: testURL
   *
   * @assertion_ids: JavaEE:platform:10125
   *
   * @test_Strategy: Package a Servlet in a WAR file declaring a resource
   *                 reference for a javax.net.URL.
   *
   *                 Check that: - We can deploy the application. - The Servlet
   *                 can lookup URL factory to open a connection to a JSP
   *                 bundled in the application.
   */
  public void testURL() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testURL");
      if (!pass) {
        throw new Fault("testURL failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testURL failed", e);
    }
  }

  /**
   * @testName: testQueue
   *
   * @assertion_ids: JavaEE:platform:10125
   *
   * @test_Strategy: Package a Servlet in a WAR file declaring a resource
   *                 reference for a javax.javax.jms.QueueConnectionFactory
   *
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Queue Connection Factory.
   */
  public void testQueue() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testQueue");
      if (!pass) {
        throw new Fault("testQueue failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testQueue failed", e);
    }
  }

  /**
   * @testName: testTopic
   *
   * @assertion_ids: JavaEE:platform:10125
   *
   * @test_Strategy: Package a Servlet in a WAR file declaring a resource
   *                 reference for a javax.javax.jms.TopicConnectionFactory
   *
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Topic Connection Factory.
   */
  public void testTopic() throws Fault {
    try {
      boolean pass = true;

      pass = webServer.test(webAlias, "testTopic");
      if (!pass) {
        throw new Fault("testTopic failed");
      }
    } catch (Exception e) {
      logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testTopic failed", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
