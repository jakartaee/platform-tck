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
 * @(#)Client.java	1.14 03/05/16
 */

package com.sun.ts.tests.appclient.deploy.ejblink.path;

import java.util.Properties;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.assembly.util.refbean.*;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  /** Bean lookup */
  private static final String bean1Lookup = prefix + "Nikita";

  private static final String bean2Lookup = prefix + "Illusion";

  /** Expected value for the bean name */
  private static final String bean1RefName = "Besson";

  private static final String bean2RefName = "Renoir";

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
   * @assertion_ids: JavaEE:SPEC:118; JavaEE:SPEC:119; JavaEE:SPEC:120
   *
   * @test_Strategy: An application client references two beans packaged in two
   *                 different ejb-jar's. Both referenced bean use the same
   *                 ejb-name in their respective JAR file, and they are
   *                 identified by a String environment entry ('myName').
   *
   *                 The ejb-link for the external bean is in the form
   *                 'ejbjar#EJBName'.
   *
   *                 Check that we can deploy the application, that the client
   *                 can lookup the two beans. Check that referenced beans
   *                 identities (as reported by the String env. entry) match the
   *                 ones specified in the DD.
   */
  public void testScope() throws Fault {
    ReferencedBeanHome home1 = null;
    ReferencedBean2Home home2 = null;
    ReferencedBean bean1 = null;
    ReferencedBean2 bean2 = null;
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
      TestUtil.logTrace(bean1Lookup + " name is '" + bean1Name + "'");
      bean1.remove();

      home2 = (ReferencedBean2Home) nctx.lookup(bean2Lookup,
          ReferencedBean2Home.class);
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
