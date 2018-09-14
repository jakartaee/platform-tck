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

package com.sun.ts.tests.ejb.ee.deploy.session.stateful.enventry.scope;

import java.util.Properties;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.harness.EETest;
import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String testName = "com.sun.ts.tests.ejb.ee.deploy.session.stateful"
      + ".enventry.scope.Bean";

  /*
   * JNDI Names for the beans.
   */
  private static final String prefix = "java:comp/env/ejb/";

  private static final String bean1Name_SameJar = prefix + "Bean1_SameJar";

  private static final String bean2Name_SameJar = prefix + "Bean2_SameJar";

  private static final String bean1Name_MultJar = prefix + "Bean1_MultJar";

  private static final String bean2Name_MultJar = prefix + "Bean2_MultJar";

  /*
   * String env entry name and its expected values.
   */
  private static final String entryName = "Duende";

  private static final String bean1Value_SameJar = "Paco de Lucia";

  private static final String bean2Value_SameJar = "El Camaron";

  private static final String bean1Value_MultJar = "Vincente Amigo";

  private static final String bean2Value_MultJar = "Tomatito";

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   *
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      logTrace("Client: Getting naming context...");
      nctx = new TSNamingContext();
      logTrace("Client: Setup OK! (got naming context)");
    } catch (Exception e) {
      throw new Fault("Client: Setup failed:", e);
    }
  }

  /**
   * @testName: testScopeInternal
   *
   * @assertion_ids: EJB:SPEC:757.1
   *
   * @test_Strategy: Deploy two Stateful Session beans (part of a same JAR file)
   *                 using the same env-entry-name but declaring different
   *                 values. Check that we can lookup the env. entry from each
   *                 EJB, that runtime values are distinct, and that they
   *                 correspond to the ones in the DD.
   */
  public void testScopeInternal() throws Fault {
    BeanHome home1 = null;
    BeanHome home2 = null;

    Bean bean1 = null;
    Bean bean2 = null;

    boolean pass = false;

    try {
      logTrace("Client: looking up '" + bean1Name_SameJar + "' and '"
          + bean2Name_SameJar + "' ...");
      home1 = (BeanHome) nctx.lookup(bean1Name_SameJar, BeanHome.class);
      home2 = (BeanHome) nctx.lookup(bean2Name_SameJar, BeanHome.class);

      logTrace("Client: creating bean instances...");
      bean1 = home1.create(props);
      bean2 = home2.create(props);

      logTrace("Client: Calling beans...");
      if (!bean1.checkEntry(entryName, bean1Value_SameJar)) {
        throw new Fault("Client: Env entry scope test failed [1st]");
      }
      if (!bean2.checkEntry(entryName, bean2Value_SameJar)) {
        throw new Fault("Client: Env entry scope test failed [2nd]");
      }

      logTrace("Client: Removing beans...");
      bean1.remove();
      bean2.remove();

      pass = true;
    } catch (Exception e) {
      throw new Fault("Client: Env entry scope test failed [same JAR]: " + e,
          e);
    }
  }

  /**
   * @testName: testScopeExternal
   *
   * @assertion_ids: EJB:SPEC:757.1
   *
   * @test_Strategy: Deploy two Stateful Session beans (in 2 distinct JAR files)
   *                 using the same env-entry-name but declaring different
   *                 values. Check that we can lookup the env. entry from each
   *                 EJB, that runtime values are distinct, and that they
   *                 correspond to the ones in the DD.
   */
  public void testScopeExternal() throws Fault {
    BeanHome home1 = null;
    BeanHome home2 = null;

    Bean bean1 = null;
    Bean bean2 = null;

    boolean pass = false;

    try {
      logTrace("Client: looking up '" + bean1Name_MultJar + "' and '"
          + bean2Name_MultJar + "' ...");
      home1 = (BeanHome) nctx.lookup(bean1Name_MultJar, BeanHome.class);
      home2 = (BeanHome) nctx.lookup(bean2Name_MultJar, BeanHome.class);

      logTrace("Client: creating bean instances...");
      bean1 = home1.create(props);
      bean2 = home2.create(props);

      logTrace("Client: Calling beans...");
      if (!bean1.checkEntry(entryName, bean1Value_MultJar)) {
        throw new Fault("Client: Env entry scope test failed [1st]");
      }
      if (!bean2.checkEntry(entryName, bean2Value_MultJar)) {
        throw new Fault("Client: Env entry scope test failed [2nd]");
      }

      logTrace("Client: Removing beans...");
      bean1.remove();
      bean2.remove();

      pass = true;
    } catch (Exception e) {
      throw new Fault("Client: Env entry scope test failed [2 JARs]: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("Client: cleanup");
  }
}
