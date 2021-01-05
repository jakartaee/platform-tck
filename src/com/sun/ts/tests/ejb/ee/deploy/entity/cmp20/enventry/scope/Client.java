/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)Client.java	1.13 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.cmp20.enventry.scope;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;

public class Client extends EETest {

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

  private TSNamingContext ctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   */
  public void setup(String[] args, Properties props) throws Fault {

    try {
      this.props = props;
      logTrace("[Client] Getting naming context...");
      ctx = new TSNamingContext();
      logTrace("[Client] Setup OK! (got naming context)");
    } catch (Exception e) {
      throw new Fault("Setup failed:", e);
    }
  }

  /**
   * @testName: testScopeInternal
   *
   * @assertion_ids: EJB:SPEC:757.1
   *
   * @test_Strategy: Deploy two CMP 2.0 Entity beans (part of a same JAR file)
   *                 using the same env-entry-name but declaring different
   *                 values. Check that we can lookup this env. entry from both
   *                 EJB's, that runtime values are distinct, and that they
   *                 match the ones in the DD.
   */
  public void testScopeInternal() throws Fault {
    TestBeanHome home1 = null;
    TestBeanHome home2 = null;

    TestBean bean1 = null;
    TestBean bean2 = null;

    boolean pass = false;

    try {
      logTrace("[Client] looking up '" + bean1Name_SameJar + "' and '"
          + bean2Name_SameJar + "' ...");
      home1 = (TestBeanHome) ctx.lookup(bean1Name_SameJar, TestBeanHome.class);
      home2 = (TestBeanHome) ctx.lookup(bean2Name_SameJar, TestBeanHome.class);

      logTrace("[Client] creating bean instances...");
      bean1 = home1.create(props, 1, "columbian", 10);
      bean2 = home2.create(props, 2, "french roast", 5);

      logTrace("[Client] Calling beans...");
      if (!bean1.checkEntry(entryName, bean1Value_SameJar)) {
        throw new Fault("Env entry scope test failed [1st]");
      }
      if (!bean2.checkEntry(entryName, bean2Value_SameJar)) {
        throw new Fault("Env entry scope test failed [2nd]");
      }

      logTrace("[Client] Removing beans...");
      bean1.remove();
      bean2.remove();

      pass = true;
    } catch (Exception e) {
      throw new Fault("scope test failed [same JAR]: " + e, e);
    }
  }

  /**
   * @testName: testScopeExternal
   *
   * @assertion_ids: EJB:SPEC:757.1
   *
   * @test_Strategy: Deploy two CMP 2.0 Entity beans (in 2 distinct JAR files)
   *                 using the same env-entry-name but declaring different
   *                 values. Check that we can lookup this env entry from both
   *                 EJB's, that runtime values are distinct, and that they
   *                 match the ones in the DD.
   */
  public void testScopeExternal() throws Fault {
    TestBeanHome home1 = null;
    TestBeanHome home2 = null;

    TestBean bean1 = null;
    TestBean bean2 = null;

    boolean pass = false;

    try {
      logTrace("[Client] looking up '" + bean1Name_MultJar + "' and '"
          + bean2Name_MultJar + "' ...");
      home1 = (TestBeanHome) ctx.lookup(bean1Name_MultJar, TestBeanHome.class);
      home2 = (TestBeanHome) ctx.lookup(bean2Name_MultJar, TestBeanHome.class);

      logTrace("[Client] creating bean instances...");
      bean1 = home1.create(props, 1, "columbian", 10);
      bean2 = home2.create(props, 2, "french roast", 5);

      logTrace("[Client] Calling beans...");
      if (!bean1.checkEntry(entryName, bean1Value_MultJar)) {
        throw new Fault("Env entry scope test failed [1st]");
      }
      if (!bean2.checkEntry(entryName, bean2Value_MultJar)) {
        throw new Fault("Env entry scope test failed [2nd]");
      }

      logTrace("[Client] Removing beans...");
      bean1.remove();
      bean2.remove();

      pass = true;
    } catch (Exception e) {
      throw new Fault("scope test failed [2 JARs]: " + e, e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("[Client] cleanup");
  }

}
