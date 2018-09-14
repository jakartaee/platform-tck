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

package com.sun.ts.tests.ejb.ee.pm.oneXone.uni.btob;

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
   * @testName: btob_1x1_uni_test0
   * 
   * @assertion_ids: EJB:SPEC:232.1
   * 
   * @test_Strategy: A 1x1 uni-directional relationship between entitybean
   * objects. Create a 1x1 uni-directional relationship between entitybean
   * objects. Do not set relationship fields. The results should be set to null.
   * Deploy EAR on the J2EE server. Ensure the entitybean objects were created
   * and that the persistence manager has null settings for the relationship
   * fields not set.
   *
   */

  public void btob_1x1_uni_test0() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // uni-directional relationship with each other (no relations set)
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 0);
      bRef.init(props);

      // Uni-Directional relationship fields should be null for both
      // entitybean object
      if (bRef.test0())
        TestUtil.logMsg("relationship fields are null - expected");
      else {
        TestUtil.logErr("relationship fields are nonnull - unexpected");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("btob_1x1_uni_test0 failed", e);
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
      throw new Fault("btob_1x1_uni_test0 failed");
  }

  /*
   * @testName: btob_1x1_uni_test1
   * 
   * @assertion_ids: EJB:SPEC:232.1
   * 
   * @test_Strategy: A 1x1 uni-directional relationship between entitybean
   * objects. Create a 1x1 uni-directional relationship between entitybean
   * objects. Do set relationship fields to null. The results should be set to
   * null. Deploy EAR on the J2EE server. Ensure the entitybean objects were
   * created and that the persistence manager has null settings for the
   * relationship fields not set.
   *
   */

  public void btob_1x1_uni_test1() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // uni-directional relationship with each other
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 1);
      bRef.init(props);

      // Uni-Directional relationship fields should be null for
      // entitybean object
      if (bRef.test1())
        TestUtil.logMsg("relationship fields are null - expected");
      else {
        TestUtil.logErr("relationship fields are nonnull - unexpected");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("btob_1x1_uni_test1 failed", e);
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
      throw new Fault("btob_1x1_uni_test1 failed");
  }

  /*
   * @testName: btob_1x1_uni_test2
   * 
   * @assertion_ids: EJB:SPEC:204
   * 
   * @test_Strategy: A 1x1 uni-directional relationship between entitybean
   * objects. Create a 1x1 uni-directional relationship between entitybean
   * objects. Deploy EAR on the J2EE server. Perform the relationship assignment
   * per assertion tag. Ensure the proper relationship results are correct after
   * the assignment by the persistence manager.
   *
   */

  public void btob_1x1_uni_test2() throws Fault {
    boolean pass = false;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // uni-directional relationship with each other
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      ADVC a2 = new ADVC("2", "a2", 2);
      BDVC b2 = new BDVC("2", "b2", 2);
      bRef = bHome.create("1", "bean1", 1, a1, b1, a2, b2);
      bRef.init(props);

      pass = bRef.test2();
    } catch (Exception e) {
      throw new Fault("btob_1x1_uni_test2 failed", e);
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
      throw new Fault("btob_1x1_uni_test2 failed");
  }

  /*
   * @testName: btob_1x1_uni_test3
   * 
   * @assertion_ids: EJB:SPEC:191
   * 
   * @test_Strategy: Create a 1x1 uni-directional relationship between
   * entitybean objects. Deploy EAR on the J2EE server. Ensure the entity beans
   * were created and that the persistence manager is handling getting/setting
   * of data. Ensure uni-directional data access from both entitybean objects.
   *
   */

  public void btob_1x1_uni_test3() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // uni-directional relationship with each other
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 2);
      bRef.init(props);

      // Get relationship info from bean set

      // Uni-Directional Relationship access
      // Get B info from A
      BDVC b = bRef.getBInfoFromA();
      String bId = b.getId();
      String bName = b.getName();

      logMsg("bId=" + bId + ", bName=" + bName);

      if (!bId.equals("1") || !bName.equals("b1")) {
        pass = false;
        logErr("DataMismatch error");
      }
    } catch (Exception e) {
      throw new Fault("btob_1x1_uni_test3 failed", e);
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
      throw new Fault("btob_1x1_uni_test3 failed");
  }
}
