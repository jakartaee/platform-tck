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
 * @(#)Client.java	1.35 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.bmp.resref.single;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.common.dao.DAOFactory;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private TestBeanHome home = null;

  private TestBean bean = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     mailuser1; webServerHost; webServerPort; mailFrom;
   *                     mailHost;
   *
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;

      TestUtil.logTrace("[Client] Initializing BMP table...");
      DAOFactory.getInstance().getCoffeeDAO().cleanup();

      nctx = new TSNamingContext();
      home = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
    } catch (Exception e) {
      throw new Fault("Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testDatasource
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a BMP Entity Bean declaring a resource reference for
   *                 a javax.sql.Datasource.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the datasource. - We can use it to open a DB
   *                 connection.
   */
  public void testDatasource() throws Fault {
    boolean pass = false;

    try {
      bean = home.create(props, 1, "capuccino", 12);
      pass = bean.testDatasource();
      bean.remove();

      if (!pass) {
        throw new Fault("Datasource res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Datasource res-ref test failed: " + e);
      throw new Fault("Datasource res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testSession
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a BMP Entity Bean declaring a resource reference for
   *                 a javax.mail.Session.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the mail session. - We can use this factory to send
   *                 a mail.
   */
  public void testSession() throws Fault {
    boolean pass = false;

    try {
      bean = home.create(props, 1, "capuccino", 12);
      pass = bean.testSession();
      bean.remove();

      if (!pass) {
        throw new Fault("Session ref-res test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] Session res-ref test failed: " + e);
      throw new Fault("Session res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testURL
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a BMP Entity Bean declaring a resource reference for
   *                 a java.net.URL.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the URL. - We can use this URL factory to open a
   *                 connection to a HTML page bundled in the application.
   */
  public void testURL() throws Fault {
    boolean pass = false;

    try {
      bean = home.create(props, 1, "capuccino", 12);
      pass = bean.testURL();
      bean.remove();

      if (!pass) {
        throw new Fault("URL res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] URL res-ref test failed: " + e);
      throw new Fault("URL res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testQueue
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a BMP Entity Bean declaring a resource reference for
   *                 a javax.jms.QueueConnectionFactory.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Queue Connection Factory.
   */
  public void testQueue() throws Fault {
    boolean pass = false;

    try {
      bean = home.create(props, 1, "capuccino", 12);
      pass = bean.testQueue();
      bean.remove();

      if (!pass) {
        throw new Fault("JMS Queue res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] JMS Queue Factory res-ref test failed: " + e);
      throw new Fault("Queue factory res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testTopic
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a BMP Entity Bean declaring a resource reference for
   *                 a javax.jms.TopicConnectionFactory.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup the JMS Topic Connection Factory.
   */
  public void testTopic() throws Fault {
    boolean pass = false;

    try {
      bean = home.create(props, 1, "capuccino", 12);
      pass = bean.testTopic();
      bean.remove();

      if (!pass) {
        throw new Fault("JMS Topic res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] JMS Topic res-ref test failed: " + e);
      throw new Fault("Topic factory res-ref test failed: ", e);
    }
  }

  /**
   * @testName: testAll
   *
   * @assertion_ids: EJB:SPEC:10766
   *
   * @test_Strategy: Create a BMP Entity Bean declaring a resource reference for
   *                 all the standard resource manager connection factory types.
   * 
   *                 Check that: - We can deploy the application. - We can
   *                 lookup all the declared resource factories.
   */
  public void testAll() throws Fault {
    boolean pass = false;

    try {
      bean = home.create(props, 1, "capuccino", 12);
      pass = bean.testAll();
      bean.remove();

      if (!pass) {
        throw new Fault("All res-ref test failed!");
      }
    } catch (Exception e) {
      logErr("[Client] All res-ref test failed: " + e);
      throw new Fault("All res-ref test failed: ", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
