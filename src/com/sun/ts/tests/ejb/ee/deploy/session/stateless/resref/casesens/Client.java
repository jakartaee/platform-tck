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

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.resref.casesens;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.javatest.Status;

public class Client extends EETest {

  protected static final String lookupName = "java:comp/env/ejb/TestBean";

  protected TestBeanHome home = null;

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
      logTrace("[Client] Looking up home...");
      home = (TestBeanHome) nctx.lookup(lookupName, TestBeanHome.class);
      logMsg("[Client] Setup OK!");
    } catch (Exception e) {
      throw new Fault("[Client] Setup failed:", e);
    }
  }

  /**
   * @testName: testCaseSensitivity
   *
   * @assertion_ids: EJB:SPEC:872
   *
   * @test_Strategy: Deploy a Stateless Session bean (TestBean) with two
   *                 resource references whose name differ only by case and are
   *                 assigned to two distinct factory types: a
   *                 javax.jms.QueueConnectionFactory and a
   *                 javax.jms.TopicConnectionFactory.
   *
   *                 Check that TestBean can lookup the two factories, cast them
   *                 to their respective Java types, and create a connection
   *                 (corresponding to the factory type). This validates that
   *                 the resource references were resolved correctly.
   */
  public void testCaseSensitivity() throws Fault {
    TestBean bean = null;
    boolean pass;

    try {
      logTrace("[Client] creating TestBean instance...");
      bean = home.create();
      bean.initLogging(props);
      logTrace("[Client] Calling TestBean...");
      pass = bean.testCaseSensitivity(props);

      if (!pass) {
        throw new Fault("res-ref case sensitivity test failed");
      }
    } catch (Exception e) {
      throw new Fault("res-ref case sens test failed: ", e);
    } finally {
      try {
        if (null != bean) {
          TestUtil.logTrace("[Client] Removing TestBean...");
          bean.remove();
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
