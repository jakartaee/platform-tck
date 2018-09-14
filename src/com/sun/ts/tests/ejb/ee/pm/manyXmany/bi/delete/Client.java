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

package com.sun.ts.tests.ejb.ee.pm.manyXmany.bi.delete;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;

import com.sun.javatest.Status;

public class Client extends EETest {

  private static final String bean = "java:comp/env/ejb/Bean";

  private Bean bRef = null;

  private BeanHome bHome = null;

  private TSNamingContext nctx = null;

  private Properties props = null;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props: org.omg.CORBA.ORBClass; java.naming.factory.initial;
   * generateSQL;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    try {
      logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      logMsg("Looking up home interface for EJB: " + bean);
      bHome = (BeanHome) nctx.lookup(bean, BeanHome.class);
      logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /* Run test */
  /*
   * @testName: delete_MxN_bi_test1
   * 
   * @assertion_ids: EJB:SPEC:177; EJB:SPEC:181
   * 
   * @test_Strategy: A manyXmany bi-directional relationship between entitybean
   * objects. Create a manyXmany bi-directional relationship between entitybean
   * objects. Perform delete of entitybean object. Deploy it on the J2EE server.
   * Ensure that the Persistence Manager throws javax.ejb.EJBException when
   * trying to invoke an accessor method on a deleted entitybean object.
   *
   */

  public void delete_MxN_bi_test1() throws Fault {
    boolean pass = true;
    Bean bRef = null;
    try {
      TestUtil.logMsg("Create Entity Bean");

      ADVC a1 = new ADVC("1", "a1", 1);
      ADVC a2 = new ADVC("2", "a2", 2);
      ADVC a3 = new ADVC("3", "a3", 3);
      ADVC a4 = new ADVC("4", "a4", 4);
      BDVC b1 = new BDVC("1", "b1", 1);
      BDVC b2 = new BDVC("2", "b2", 2);
      BDVC b3 = new BDVC("3", "b3", 3);
      BDVC b4 = new BDVC("4", "b4", 4);

      bRef = bHome.create("1", "bean1", 1, a1, a2, a3, a4, b1, b2, b3, b4);
      bRef.init(props);

      pass = bRef.test1();
    } catch (Exception e) {
      throw new Fault("delete_MxN_bi_test1 failed", e);
    } finally {
      try {
        if (bRef != null) {
          bRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("delete_MxN_bi_test1 failed");
  }

  /*
   * @testName: delete_MxN_bi_test2
   * 
   * @assertion_ids: EJB:SPEC:175; EJB:SPEC:176.2
   * 
   * @test_Strategy: A manyXmany bi-directional relationship between entitybean
   * objects. Create a manyXmany bi-directional relationship between entitybean
   * objects. Perform delete of entitybean object. Deploy it on the J2EE server.
   * Ensure that the accessor methods for the relationships returns the correct
   * collection.
   *
   */

  public void delete_MxN_bi_test2() throws Fault {
    boolean pass = true;
    Bean bRef = null;
    try {
      TestUtil.logMsg("Create Entity Bean");

      ADVC a1 = new ADVC("1", "a1", 1);
      ADVC a2 = new ADVC("2", "a2", 2);
      ADVC a3 = new ADVC("3", "a3", 3);
      ADVC a4 = new ADVC("4", "a4", 4);
      BDVC b1 = new BDVC("1", "b1", 1);
      BDVC b2 = new BDVC("2", "b2", 2);
      BDVC b3 = new BDVC("3", "b3", 3);
      BDVC b4 = new BDVC("4", "b4", 4);

      bRef = bHome.create("1", "bean1", 1, a1, a2, a3, a4, b1, b2, b3, b4);
      bRef.init(props);

      pass = bRef.test2();
    } catch (Exception e) {
      throw new Fault("delete_MxN_bi_test2 failed", e);
    } finally {
      try {
        if (bRef != null) {
          bRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("delete_MxN_bi_test2 failed");
  }

  /*
   * @testName: delete_MxN_bi_test3
   * 
   * @assertion_ids: EJB:SPEC:179
   * 
   * @test_Strategy: A manyXmany bi-directional relationship between entitybean
   * objects. Create a manyXmany bi-directional relationship between entitybean
   * objects. Perform delete of entitybean object. Deploy it on the J2EE server.
   * Ensure that the Persistence Manager throws
   * java.lang.IllegalArgumentException when trying to assign a deleted object
   * as the value of a cmr-field.
   *
   */

  public void delete_MxN_bi_test3() throws Fault {
    boolean pass = true;
    Bean bRef = null;
    try {
      TestUtil.logMsg("Create Entity Bean");

      ADVC a1 = new ADVC("1", "a1", 1);
      ADVC a2 = new ADVC("2", "a2", 2);
      ADVC a3 = new ADVC("3", "a3", 3);
      ADVC a4 = new ADVC("4", "a4", 4);
      BDVC b1 = new BDVC("1", "b1", 1);
      BDVC b2 = new BDVC("2", "b2", 2);
      BDVC b3 = new BDVC("3", "b3", 3);
      BDVC b4 = new BDVC("4", "b4", 4);

      bRef = bHome.create("1", "bean1", 1, a1, a2, a3, a4, b1, b2, b3, b4);
      bRef.init(props);

      pass = bRef.test3();
    } catch (Exception e) {
      throw new Fault("delete_MxN_bi_test3 failed", e);
    } finally {
      try {
        if (bRef != null) {
          bRef.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("delete_MxN_bi_test3 failed");
  }

}
