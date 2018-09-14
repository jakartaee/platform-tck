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

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.resref.scope;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.javatest.Status;

public class Client extends EETest {

  protected static final String prefix = "java:comp/env/ejb/";

  protected static final String qBeanLookup = prefix + "QueueBean";

  protected static final String tBeanLookup = prefix + "TopicBean";

  protected TSNamingContext nctx = null;

  protected Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      logTrace("[Client] Getting naming context...");
      nctx = new TSNamingContext();
      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("[Client] Setup failed:", e);
    }
  }

  /**
   * @testName: testScope
   *
   * @assertion_ids: EJB:SPEC:757.5
   *
   * @test_Strategy:
   *
   *                 We package in the same jar:
   *
   *                 - Two Stateless Session beans using the same res-ref-name
   *                 ('jms/myFactory') to reference two distinct resource
   *                 manager connection factories (a QueueConnectionFactory and
   *                 a TopicConnectionFactory).
   *
   *                 We check that:
   *
   *                 - We can deploy the application. - We can lookup each bean.
   *                 - Each bean can lookup its resource manager connection
   *                 factory. - We can cast that factory to its expected Java
   *                 type and use it to create a connection. This validates the
   *                 resolution of the resource manager connection factories
   *                 references.
   *
   */
  public void testScope() throws Fault {
    QueueBeanHome qHome = null;
    TopicBeanHome tHome = null;
    QueueBean qBean = null;
    TopicBean tBean = null;
    boolean pass;

    try {
      logTrace("[Client] Looking up " + qBeanLookup);
      qHome = (QueueBeanHome) nctx.lookup(qBeanLookup, QueueBeanHome.class);
      logTrace("[Client] creating QueueBean instance...");
      qBean = qHome.create();
      qBean.initLogging(props);

      logTrace("[Client] Looking up " + qBeanLookup);
      tHome = (TopicBeanHome) nctx.lookup(tBeanLookup, TopicBeanHome.class);
      logTrace("[Client] creating TopicBean instance...");
      tBean = tHome.create();
      tBean.initLogging(props);

      logTrace("[Client] Calling beans...");
      pass = qBean.checkYourQueue();
      pass &= tBean.checkYourTopic();

      if (!pass) {
        throw new Fault("res-ref scope test failed");
      }
    } catch (Exception e) {
      throw new Fault("res-ref scope test failed: ", e);
    } finally {

      try {
        if (null != qBean) {
          TestUtil.logTrace("[Client] Removing " + qBeanLookup);
          qBean.remove();
        }

        if (null != tBean) {
          TestUtil.logTrace("[Client] Removing " + tBeanLookup);
          tBean.remove();
        }
      } catch (Exception e) {
        TestUtil
            .logTrace("[Client] Ignore exception on bean " + "remove: " + e);
      }
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
