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
 * @(#)Client.java	1.12 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.ejbref.casesens;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.refbean.*;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  /* These lookups differ only by case */
  private static final String bean1Lookup = prefix + "Philosopher";

  private static final String bean2Lookup = prefix + "philosopher";

  /* Expected values for the bean name */
  private static final String bean1RefName = "Voltaire";

  private static final String bean2RefName = "Rousseau";

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /**
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *                     generateSQL;
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
      throw new Fault("[Client] Setup failed:", e);
    }
  }

  /**
   * @testName: testCaseSensitivity
   *
   * @assertion_ids: JavaEE:SPEC:279
   *
   * @test_Strategy: Deploy an application client referencing two beans whose
   *                 name differ only by case. Each referenced bean is packaged
   *                 with a different values for a String environment entry
   *                 called 'myName'.
   *
   *                 Check that the application client can lookup the two beans.
   *                 Check that their runtime value for the 'myName' env. entry
   *                 are distinct and match the ones specified in the DD
   *                 (validates that the EJB reference are resolved correctly).
   */
  public void testCaseSensitivity() throws Fault {
    ReferencedBeanHome home1 = null;
    ReferencedBeanHome home2 = null;
    ReferencedBean bean1 = null;
    ReferencedBean bean2 = null;
    String bean1Name;
    String bean2Name;
    boolean pass = false;

    try {
      TestUtil.logTrace("[Client] Looking up '" + bean1Lookup + "'...");
      home1 = (ReferencedBeanHome) nctx.lookup(bean1Lookup,
          ReferencedBeanHome.class);
      bean1 = home1.create();
      bean1.initLogging(props);
      bean1Name = bean1.whoAreYou();
      TestUtil.logTrace(bean1Lookup + "name is '" + bean1Name + "'");
      bean1.remove();

      TestUtil.logTrace("[Client] Looking up '" + bean2Lookup + "'...");
      home2 = (ReferencedBeanHome) nctx.lookup(bean2Lookup,
          ReferencedBeanHome.class);
      bean2 = home2.create();
      bean2.initLogging(props);
      bean2Name = bean2.whoAreYou();
      TestUtil.logTrace(bean2Lookup + " name is '" + bean2Name + "'");
      bean2.remove();

      pass = bean1Name.equals(bean1RefName) && bean2Name.equals(bean2RefName);

      if (!pass) {
        TestUtil.logErr("[Client] " + bean1Lookup + "name is '" + bean1Name
            + "' expected '" + bean1RefName + "'");

        TestUtil.logErr("[Client] " + bean2Lookup + "name is '" + bean2Name
            + "' expected '" + bean2RefName + "'");

        throw new Fault("ejb-ref casesens test failed!");
      }
    } catch (Exception e) {
      throw new Fault("ejb-ref casesens test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
