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
 * @(#)Client.java	1.23 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.resref.single;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.shared.resref.single.appclient.TestCode;
import com.sun.javatest.Status;

public class Client extends EETest {

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     webServerHost; webServerPort;
   */
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      nctx = new TSNamingContext();
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testDatasource
   *
   * @assertion_ids: JavaEE:SPEC:10125
   *
   * @test_Strategy: Package an application client declaring a resource
   *                 reference for a javax.sql.Datasource.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the datasource. - We can use it to open a DB
   *                 connection.
   */
  public void testDatasource() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testDatasource(nctx);
      if (!pass) {
        throw new Fault("Datasource res-ref test failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] Caught exception: " + e);
      throw new Fault("Datasource res-ref test failed!", e);
    }
  }

  /**
   * @testName: testURL
   *
   * @assertion_ids: JavaEE:SPEC:10125
   *
   * @test_Strategy: Package an application client declaring a resource
   *                 reference for a java.net.URL.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the URL. - We can use this URL factory to open a
   *                 connection to a HTML page bundled in the application.
   */
  public void testURL() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testURL(nctx);
      if (!pass) {
        throw new Fault("URL res-ref test failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] Caught exception: " + e);
      throw new Fault("URL res-ref test failed!", e);
    }
  }

  /**
   * @testName: testQueue
   *
   * @assertion_ids: JavaEE:SPEC:10125
   *
   * @test_Strategy: Package an application client declaring a resource
   *                 reference for a javax.jms.QueueConnectionFactory.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Queue Connection Factory.
   */
  public void testQueue() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testQueue(nctx);
      if (!pass) {
        throw new Fault("Queue res-ref test failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] Caught exception: " + e);
      throw new Fault("Queue res-ref test failed!", e);
    }
  }

  /**
   * @testName: testTopic
   *
   * @assertion_ids: JavaEE:SPEC:10125
   *
   * @test_Strategy: Package an application client declaring a resource
   *                 reference for a javax.jms.TopicConnectionFactory.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Topic Connection Factory.
   */
  public void testTopic() throws Fault {
    boolean pass;

    try {
      pass = TestCode.testTopic(nctx);
      if (!pass) {
        throw new Fault("Topic res-ref test failed!");
      }
    } catch (Exception e) {
      TestUtil.logErr("[Client] Caught exception: " + e);
      throw new Fault("Topic res-ref test failed!", e);
    }
  }

  /**
   * @testName: testAll
   *
   * @assertion_ids: JavaEE:SPEC:10125
   *
   * @test_Strategy: Package an application client declaring a resource
   *                 reference for all the standard resource manager connection
   *                 factory types.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup all the declared resource factories.
   */
  public void testAll() throws Fault {
    try {
      testDatasource();
      testURL();
      testQueue();
      testTopic();
    } catch (Exception e) {
      TestUtil.logErr("[Client] Caught exception: " + e);
      throw new Fault("All res-ref test failed!", e);
    }
  }

  public void cleanup() {
    logTrace("[Client] cleanup()");
  }

}
