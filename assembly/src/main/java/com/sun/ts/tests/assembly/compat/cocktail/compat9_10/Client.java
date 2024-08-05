/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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
 * %W %E
 */

package com.sun.ts.tests.assembly.compat.cocktail.compat9_10;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  /** Bean lookup */
  private static final String bean1Lookup = prefix + "Vision";

  private static final String bean2Lookup = prefix + "Music";

  /** Expected value for the bean name */
  private static final String bean1RefName = "Rimbaud";

  private static final String bean2RefName = "Verlaine";

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
   * @testName: test9DD
   *
   * @assertion_ids: JavaEE:SPEC:284
   *
   * @test_Strategy:
   *
   *                 Package an .ear file (Jakarta EE 10 application DD) with: - 1
   *                 ejb-jar file using a Jakarta EE 9 DD - 1 ejb-jar file using a
   *                 Jakarta EE 10 DD - 1 application client jar file using a Jakarta EE
   *                 9 DD - 1 application client jar file using a Jakarta EE 10 DD
   *
   *                 Deploy the .ear file.
   *
   *                 Run the 9 client and check that it can call a bean in
   *                 each ejb-jar at runtime.
   */
  public void test9DD() throws Fault {
    ReferencedBean bean1 = null;
    ReferencedBean bean2 = null;
    String bean1Name;
    String bean2Name;
    boolean pass = false;

    try {
      TestUtil.logTrace("[Client] Looking up '" + bean1Lookup);
      bean1 = (ReferencedBean) nctx.lookup(bean1Lookup,
          ReferencedBean.class);
      bean1.createNamingContext();
      bean1.initLogging(props);
      bean1Name = bean1.whoAreYou();
      TestUtil.logTrace(bean1Lookup + "name is '" + bean1Name + "'");

      TestUtil.logTrace("[Client] Looking up '" + bean2Lookup);
      bean2 = (ReferencedBean) nctx.lookup(bean2Lookup,
          ReferencedBean.class);
      bean2.createNamingContext();
      bean2.initLogging(props);
      bean2Name = bean2.whoAreYou();
      TestUtil.logTrace(bean2Lookup + "name is '" + bean2Name + "'");

      pass = bean1Name.equals(bean1RefName) && bean2Name.equals(bean2RefName);
      if (!pass) {
        TestUtil.logErr("[Client] " + bean1Lookup + "name is '" + bean1Name
            + "' expected '" + bean1RefName + "'");

        TestUtil.logErr("[Client] " + bean2Lookup + "name is '" + bean2Name
            + "' expected '" + bean2RefName + "'");

        throw new Fault("compat cocktail test failed!");
      }
    } catch (Exception e) {
      throw new Fault("compat cocktail test failed: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup()");
  }

}
