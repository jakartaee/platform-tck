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
 * @(#)Client.java	1.23 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.session.stateless.ejblink.single;

import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.lib.util.TSNamingContext;

public class Client extends EETest {

  private static final String testLookup = "java:comp/env/" + "ejb/TestBean";

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
   *                     generateSQL;
   *
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      logTrace("Client: Getting TS Naming Context...");
      nctx = new TSNamingContext();

      logTrace("Client: Looking up the Home...");
      home = (TestBeanHome) nctx.lookup(testLookup, TestBeanHome.class);
      logTrace("Client: Looked up Home!");
    } catch (Exception e) {
      throw new Fault("Client: Setup failed:", e);
    }
  }

  /**
   * @testName: testStatelessInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a Stateless Session bean (TestBean) referencing
   *                 another Stateless Session bean (StatelessInternal) which is
   *                 part of the same JAR file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessInternal beans (to check that the EJB reference
   *                 was resolved consistently with the DD).
   */
  public void testStatelessInternal() throws Fault {
    try {
      logTrace("Client: Creating TestBean...");
      bean = home.create();
      bean.initLogging(props);

      logTrace("Client: Checking Stateless internal references...");
      boolean pass = bean.testStatelessInternal(props);
      bean.remove();

      if (!pass) {
        throw new Fault("Stateless internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: Caught exception: ", e);
      throw new Fault("Stateless internal EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testStatelessExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a Stateless Session bean (TestBean) referencing
   *                 another Stateless Session bean (StatelessExternal) which is
   *                 part of another JAR file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatelessExternal beans (to check that the EJB reference
   *                 was resolved consistently with the DD).
   */
  public void testStatelessExternal() throws Fault {
    try {
      logTrace("Client: Creating TestBean...");
      bean = home.create();
      bean.initLogging(props);

      logTrace("Client: Checking Stateless external references...");
      boolean pass = bean.testStatelessExternal(props);
      bean.remove();

      if (!pass) {
        throw new Fault("Stateless internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: Caught exception: ", e);
      throw new Fault("Stateless internal EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testStatefulInternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a Stateless Session bean (TestBean) referencing a
   *                 Statelful Session bean (StatefulInternal) which is part of
   *                 the same JAR file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulInternal beans (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testStatefulInternal() throws Fault {
    try {
      logTrace("Client: Creating TestBean...");
      bean = home.create();
      bean.initLogging(props);

      logTrace("Client: Checking Stateful internal references...");
      boolean pass = bean.testStatefulInternal(props);
      bean.cleanUpBean();
      bean.remove();

      if (!pass) {
        throw new Fault("Stateful internal EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: Caught exception: ", e);
      throw new Fault("Stateful internal EJB ref test failed!" + e, e);
    }
  }

  /**
   * @testName: testStatefulExternal
   *
   * @assertion_ids: EJB:SPEC:765
   *
   * @test_Strategy: Deploy a Stateless Session bean (TestBean) referencing a
   *                 Stateful Session bean (StatefulExternal), which is part of
   *                 another JAR file.
   *
   *                 Check at runtime that TestBean can do a lookup for the EJB
   *                 reference and use it to create a bean. Then invoke on that
   *                 instance a business method to be found only in
   *                 StatefulExternal beans (to check that the EJB reference was
   *                 resolved consistently with the DD).
   */
  public void testStatefulExternal() throws Fault {
    try {
      logTrace("Client: Creating TestBean...");
      bean = home.create();
      bean.initLogging(props);

      logTrace("Client: Checking Stateful external references...");
      boolean pass = bean.testStatefulExternal(props);
      bean.cleanUpBean();
      bean.remove();

      if (!pass) {
        throw new Fault("Stateful external EJB ref test failed!");
      }
    } catch (Exception e) {
      logErr("Client: Caught exception: ", e);
      throw new Fault("Stateful external EJB ref test failed!" + e, e);
    }
  }

  public void cleanup() {
    logTrace("Client: Cleanup()");
  }

}
