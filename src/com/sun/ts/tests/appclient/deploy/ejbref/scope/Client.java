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
 * @(#)Client.java	1.11 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.ejbref.scope;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.refbean.*;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  /** Bean lookup */
  private static final String beanLookup = prefix + "Verona";

  /** Expected value for the bean name */
  private static final String beanRefName = "Romeo";

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   *
   * @class.testArgs: -ap tssql.stmt
   *
   */
  public void setup(String[] args, Properties props) throws Fault {
    this.props = props;

    try {
      nctx = new TSNamingContext();
      logMsg("[Client] Setup succeed (got naming context).");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testScope
   *
   * @assertion_ids: JavaEE:SPEC:117
   *
   * @test_Strategy:
   *
   *                 We package in the same .ear file:
   *
   *                 - Two application clients's using the same ejb-ref-name
   *                 ('ejb/Verona') to reference two distinct ReferencedBean's.
   *
   *                 - One EJB jar, containing two distinct beans, whose
   *                 identity is defined by a String environment entry
   *                 ('myName').
   *
   *                 We check that:
   *
   *                 - We can deploy the application. - We can run one of the
   *                 two application clients. - This application client can
   *                 lookup its referenced bean and get the bean identity. -
   *                 Check this runtime identity against the one specified in
   *                 the application client DD (validates that this EJB
   *                 reference is resolved correctly).
   */
  public void testScope() throws Fault {
    ReferencedBeanHome home = null;
    ReferencedBean bean = null;
    String beanName;
    boolean pass = false;

    try {
      TestUtil.logTrace("[Client] Looking up '" + beanLookup + "'...");
      home = (ReferencedBeanHome) nctx.lookup(beanLookup,
          ReferencedBeanHome.class);
      bean = home.create();
      bean.initLogging(props);
      beanName = bean.whoAreYou();
      TestUtil.logTrace(beanLookup + "name is '" + beanName + "'");
      bean.remove();

      pass = beanName.equals(beanRefName);
      if (!pass) {
        TestUtil.logErr("[Client] " + beanLookup + "name is '" + beanName
            + "' expected '" + beanRefName + "'");

        throw new Fault("ejb-ref scope test failed!");
      }
    } catch (Exception e) {
      throw new Fault("ejb-ref scope test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
