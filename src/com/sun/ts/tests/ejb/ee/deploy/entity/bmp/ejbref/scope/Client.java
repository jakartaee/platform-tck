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
 * @(#)Client.java	1.18 03/05/16
 */

package com.sun.ts.tests.ejb.ee.deploy.entity.bmp.ejbref.scope;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import java.io.*;
import java.util.*;
import javax.ejb.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.common.dao.DAOFactory;

public class Client extends EETest {

  private static final String prefix = "java:comp/env/ejb/";

  private static final String bean1Lookup = prefix + "Romeo";

  private static final String bean2Lookup = prefix + "Tristan";

  private static final String bean3Lookup = prefix + "Cyrano";

  /* Expected values for partners */
  private static final String bean1RefPartner = "Juliette";

  private static final String bean2RefPartner = "Iseult";

  private static final String bean3RefPartner = "Roxane";

  private ReferencingBeanHome home = null;

  private ReferencingBean bean = null;

  private Properties props = null;

  private TSNamingContext nctx = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * 
   * @class.testArgs: -ap tssql.stmt
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;

    try {
      logTrace("Client: Getting Naming Context...");
      nctx = new TSNamingContext();

      logTrace("Client: Initializing BMP table...");
      DAOFactory.getInstance().getCoffeeDAO().cleanup();

      logTrace("Client:Got Naming Context!");
    } catch (Exception e) {
      logErr("Client: Failed to obtain Naming Context:" + e);
      throw new Fault("Client: Setup failed:" + e, e);
    }
  }

  /**
   * @testName: testScopeInternal
   *
   * @assertion_ids: EJB:SPEC:757.2
   *
   * @test_Strategy:
   *
   *                 We package in the same ejb-jar:
   *
   *                 - Two ReferencingBean's using the same ejb-ref-name
   *                 ('ejb/Partner') to reference two distinct ReferencedBean's.
   *
   *                 - Two ReferencedBean's, whose identity is defined by a
   *                 String environment entry ('myName').
   *
   *                 We check that:
   *
   *                 - We can deploy the application. - We can lookup each of
   *                 the referencing beans. - Each ReferencingBean can lookup
   *                 its ReferencedBean and get the ReferencedBean's identity. -
   *                 Check this runtime identity against the one specified in
   *                 the DD (to check the reference was resolved correctly).
   *
   */
  public void testScopeInternal() throws Fault {
    String bean1Partner;
    String bean2Partner;
    boolean pass = false;

    try {

      logTrace("Client: Looking up " + bean1Lookup);
      home = (ReferencingBeanHome) nctx.lookup(bean1Lookup,
          ReferencingBeanHome.class);
      bean = home.create(props, 1, "expresso", 8);
      logTrace("Client: Checking referenced EJB...");
      bean1Partner = bean.whoIsYourPartner(props, 10);
      bean.remove();

      logTrace("Client: Looking up " + bean2Lookup);
      home = (ReferencingBeanHome) nctx.lookup(bean2Lookup,
          ReferencingBeanHome.class);
      bean = home.create(props, 2, "capuccino", 8);
      logTrace("Client: Checking referenced EJB...");
      bean2Partner = bean.whoIsYourPartner(props, 11);
      bean.remove();

      pass = bean1Partner.equals(bean1RefPartner)
          && bean2Partner.equals(bean2RefPartner);

      if (!pass) {
        logErr("Client: Expected " + bean1Lookup + " partner to be "
            + bean1RefPartner + " and  " + bean2Lookup + " partner to be "
            + bean2RefPartner);

        throw new Fault("Client: EJB reference scope test failed!");
      }
    } catch (Exception e) {
      logErr("Client: Caught exception: " + e);
      throw new Fault("Client: Internal EJB ref scope test failed!" + e, e);
    }
  }

  /**
   * @testName: testScopeExternal
   *
   * @assertion_ids: EJB:SPEC:757.2
   *
   * @test_Strategy:
   *
   *                 We package in the one ejb-jar:
   *
   *                 - ReferencingBean1 using ejb-ref-name ('ejb/Partner') to
   *                 reference ReferencedBean1. - ReferencedBean1 and
   *                 ReferencedBean3, whose identity is defined by a String
   *                 environment entry ('myName').
   *
   *                 We package in another ejb-jar (part of the same EAR file):
   *
   *                 - ReferencingBean3 using ejb-ref-name ('ejb/Partner') to
   *                 reference ReferencedBean3 (packaged in previous jar).
   * 
   *                 We check that:
   *
   *                 - We can deploy the application. - We can lookup each of
   *                 the referencing beans. - Each ReferencingBean can lookup
   *                 its ReferencedBean and get the ReferencedBean's identity. -
   *                 Check this runtime identity against the one specified in
   *                 the DD (to check that the reference was resolved
   *                 correctly).
   *
   */
  public void testScopeExternal() throws Fault {
    String bean1Partner;
    String bean3Partner;
    boolean pass = false;

    try {

      logTrace("Client: Looking up " + bean1Lookup);
      home = (ReferencingBeanHome) nctx.lookup(bean1Lookup,
          ReferencingBeanHome.class);
      bean = home.create(props, 1, "expresso", 8);
      logTrace("Client: Checking referenced EJB...");
      bean1Partner = bean.whoIsYourPartner(props, 12);
      bean.remove();

      logTrace("Client: Looking up " + bean3Lookup);
      home = (ReferencingBeanHome) nctx.lookup(bean3Lookup,
          ReferencingBeanHome.class);
      bean = home.create(props, 2, "cappucino", 8);
      logTrace("Client: Checking referenced EJB...");
      bean3Partner = bean.whoIsYourPartner(props, 13);
      bean.remove();

      pass = bean1Partner.equals(bean1RefPartner)
          && bean3Partner.equals(bean3RefPartner);

      if (!pass) {
        logErr("Client: Expected " + bean1Lookup + " partner to be "
            + bean1RefPartner + " and  " + bean3Lookup + " partner to be "
            + bean3RefPartner);

        throw new Fault("Client: EJB reference scope test failed!");
      }
    } catch (Exception e) {
      logErr("Client: Caught exception: " + e);
      throw new Fault("Client: External EJB ref scope test failed!" + e, e);
    }

  }

  public void cleanup() {
    logTrace("Client: Cleanup.");
  }
}
