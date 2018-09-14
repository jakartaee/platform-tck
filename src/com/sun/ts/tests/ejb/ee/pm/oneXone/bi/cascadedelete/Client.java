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
 * @(#)Client.java	1.15 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.oneXone.bi.cascadedelete;

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
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + bean);
      bHome = (BeanHome) nctx.lookup(bean, BeanHome.class);

      TestUtil.logMsg("Setup ok");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /* Run test */

  /*
   * @testName: cascadedelete_1x1_bi_test1
   * 
   * @assertion_ids: EJB:SPEC:182; EJB:SPEC:184; EJB:SPEC:185
   * 
   * @test_Strategy: A 1x1 bi-directional relationship between entitybean
   * objects. Create a 1x1 bi-directional relationship between entitybean
   * objects. Perform cascade delete of entitybean object. Deploy it on the J2EE
   * server. Ensure that the cascade delete of entitybean object and all related
   * entitybean objects succeeded.
   *
   */

  public void cascadedelete_1x1_bi_test1() throws Fault {
    boolean pass = true;
    Bean b = null;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // bi-directional relationship with each other (relation set)
      TestUtil.logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      b = bHome.create("1", "bean1", 1, a1, b1, 2);
      b.init(props);

      // Check cascade delete of entitybean object
      if (b.test1())
        TestUtil.logMsg("cascade delete of entitybean object passed");
      else {
        TestUtil.logErr("cascade delete of entitybean object failed");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("cascadedelete_1x1_bi_test1 failed", e);
    } finally {
      try {
        if (b != null) {
          b.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("cascadedelete_1x1_bi_test1 failed");
  }

  /*
   * @testName: cascadedelete_1x1_bi_test2
   * 
   * @assertion_ids: EJB:SPEC:186
   * 
   * @test_Strategy: A 1x1 bi-directional relationship between entitybean
   * objects. Create a 1x1 bi-directional relationship between entitybean
   * objects. Perform cascade delete of entitybean object. Deploy it on the J2EE
   * server. Ensure that the accessor methods for the relationships returns
   * null.
   *
   */

  public void cascadedelete_1x1_bi_test2() throws Fault {
    boolean pass = true;
    Bean b = null;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // bi-directional relationship with each other (relation set)
      TestUtil.logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      b = bHome.create("1", "bean1", 1, a1, b1, 2);
      b.init(props);

      // Check if accessor methods for the relationships returns null
      if (b.test2())
        TestUtil
            .logMsg("accessor methods for relationships returns null passed");
      else {
        TestUtil.logErr(
            "accessor methods for relationships returns not null failed");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("cascadedelete_1x1_bi_test2 failed", e);
    } finally {
      try {
        if (b != null) {
          b.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("cascadedelete_1x1_bi_test2 failed");
  }

  /*
   * @testName: cascadedelete_1x1_bi_test3
   * 
   * @assertion_ids: EJB:SPEC:178
   * 
   * @test_Strategy: A 1x1 bi-directional relationship between entitybean
   * objects. Create a 1x1 bi-directional relationship between entitybean
   * objects. Perform cascade delete of entitybean object. Deploy it on the J2EE
   * server. Ensure that the Persistence Manager throws javax.ejb.EJBException
   * when trying to invoke an accessor methods on a deleted entitybean objects.
   *
   */

  public void cascadedelete_1x1_bi_test3() throws Fault {
    boolean pass = true;
    Bean b = null;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // bi-directional relationship with each other (relation set)
      TestUtil.logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      b = bHome.create("1", "bean1", 1, a1, b1, 2);
      b.init(props);

      // Check that PM throws javax.ejb.EJBException
      if (b.test3())
        TestUtil.logMsg("pm does throw EJBException passed");
      else {
        TestUtil.logErr("pm does not throw EJBException failed");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("test3 failed", e);
    } finally {
      try {
        if (b != null) {
          b.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("cascadedelete_1x1_bi_test3 failed");
  }

  /*
   * @testName: cascadedelete_1x1_bi_test4
   * 
   * @assertion_ids: EJB:SPEC:179
   * 
   * @test_Strategy: A 1x1 bi-directional relationship between entitybean
   * objects. Create a 1x1 bi-directional relationship between entitybean
   * objects. Perform cascade delete of entitybean object. Deploy it on the J2EE
   * server. Ensure that the Persistence Manager throws
   * java.lang.IllegalArgumentException when trying to assign a deleted object
   * as the value of a cmr-field.
   *
   */

  public void cascadedelete_1x1_bi_test4() throws Fault {
    boolean pass = true;
    Bean b = null;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // bi-directional relationship with each other (relation set)
      TestUtil.logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      b = bHome.create("1", "bean1", 1, a1, b1, 2);
      b.init(props);

      // Check that PM throws java.lang.IllegalArgumentException
      if (b.test4())
        TestUtil.logMsg("pm does throw IllegalArgumentException passed");
      else {
        TestUtil.logErr("pm does not throw IllegalArgumentException failed");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("cascadedelete_1x1_bi_test4 failed", e);
    } finally {
      try {
        if (b != null) {
          b.remove();
        }
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("cascadedelete_1x1_bi_test4 failed");
  }
}
