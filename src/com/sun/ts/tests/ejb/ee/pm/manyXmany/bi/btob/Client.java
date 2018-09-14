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
 * @(#)Client.java	1.13 03/05/16
 */

package com.sun.ts.tests.ejb.ee.pm.manyXmany.bi.btob;

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
   * @testName: btob_MxN_bi_test0
   * 
   * @assertion_ids: EJB:SPEC:232.1; EJB:SPEC:214
   * 
   * @test_Strategy: A manyxmany bi-directional relationship between entitybean
   * objects. Create a manyxmany bi-directional relationship between entitybean
   * objects. Do not set the relationship fields. The results should be set to
   * empty or null. Deploy EAR on the J2EE server. Ensure the entity beans were
   * created and that the persistence manager has null settings for the
   * relationship fields not set.
   *
   * Create 1 entity bean and verify the cmr-fields being empty or null for
   * entitybean objects.
   *
   */

  public void btob_MxN_bi_test0() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // bi-directional relationship with each other (no relations set)
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 0);
      bRef.init(props);

      // Bi-Directional relationship fields should be empty or null for beans
      if (bRef.test0())
        TestUtil.logMsg("relationship fields are empty or null - expected");
      else {
        TestUtil
            .logErr("relationship fields are not empty or null - unexpected");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("btob_MxN_bi_test0 failed", e);
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
      throw new Fault("btob_MxN_bi_test0 failed");
  }

  /*
   * @testName: btob_MxN_bi_test0b
   * 
   * @assertion_ids: EJB:SPEC:232.1
   * 
   * @test_Strategy: A manyxmany bi-directional relationship between entitybean
   * objects. Create a manyxmany bi-directional relationship between entitybean
   * objects. Do set relationship fields to null. The results should be set to
   * empty or null. Deploy EAR on the J2EE server. Ensure the entity beans were
   * created and that the persistence manager has null settings for the
   * relationship fields not set.
   *
   * Create 1 entity bean and verify the cmr-fields being empty or null.
   *
   */

  public void btob_MxN_bi_test0b() throws Fault {
    boolean pass = true;
    try {
      // Create entity bean with two entitybean objects in a 1x1
      // bi-directional relationship with each other (no relations set)
      logMsg("Create Entity Bean");
      ADVC a1 = new ADVC("1", "a1", 1);
      BDVC b1 = new BDVC("1", "b1", 1);
      bRef = bHome.create("1", "bean1", 1, a1, b1, 0);
      bRef.init(props);

      // Bi-Directional relationship fields should be empty or null for beans
      if (bRef.test0())
        TestUtil.logMsg("relationship fields are empty or null - expected");
      else {
        TestUtil
            .logErr("relationship fields are not empty or null - unexpected");
        pass = false;
      }

      pass = bRef.setCmrFieldToNull();

    } catch (Exception e) {
      throw new Fault("btob_MxN_bi_test0b failed", e);
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
      throw new Fault("btob_MxN_bi_test0b failed");
  }

  /*
   * @testName: btob_MxN_bi_test1
   * 
   * @assertion_ids: EJB:SPEC:208; EJB:SPEC:194; EJB:SPEC:196
   * 
   * @test_Strategy: A manyxmany bi-directional relationship between entitybean
   * objects. Create a manyxmany bi-directional relationship between entitybean
   * objects. Deploy EAR on the J2EE server. Perform the relationship assignment
   * per assertion tag. Ensure the proper relationship results are correct after
   * the assignment by the persistence manager.
   *
   */

  public void btob_MxN_bi_test1() throws Fault {
    boolean pass = false;
    try {
      // Create A and B entitybean objects
      logMsg("Create A and B EntityBean Objects");
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

      pass = bRef.doAssignmentTest1();
    } catch (Exception e) {
      throw new Fault("btob_MxN_bi_test1 failed", e);
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
      throw new Fault("btob_MxN_bi_test1 failed");
  }

  /*
   * @testName: btob_MxN_bi_test2
   * 
   * @assertion_ids: EJB:SPEC:209
   * 
   * @test_Strategy: A manyxmany bi-directional relationship between entitybean
   * objects. Create a manyxmany bi-directional relationship between entitybean
   * objects. Deploy EAR on the J2EE server. Perform the relationship assignment
   * per assertion tag. Ensure the proper relationship results are correct after
   * the assignment by the persistence manager.
   *
   */

  public void btob_MxN_bi_test2() throws Fault {
    boolean pass = false;
    try {
      // Create A and B entitybean objects
      logMsg("Create A and B EntityBean Objects");
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

      pass = bRef.doAssignmentTest2();
    } catch (Exception e) {
      throw new Fault("btob_MxN_bi_test2 failed", e);
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
      throw new Fault("btob_MxN_bi_test2 failed");
  }

  /*
   * @testName: btob_MxN_bi_test3
   * 
   * @assertion_ids: EJB:SPEC:210
   * 
   * @test_Strategy: A manyxmany bi-directional relationship between entitybean
   * objects. Create a manyxmany bi-directional relationship between entitybean
   * objects. Deploy EAR on the J2EE server. Perform the relationship assignment
   * per the assertion tag. Ensure the proper relationship results are correct
   * after the assignment by the persistence manager.
   *
   */

  public void btob_MxN_bi_test3() throws Fault {
    boolean pass = false;
    try {
      // Create A and B entitybean objects
      logMsg("Create A and B EntityBean Objects");
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

      pass = bRef.doAssignmentTest3();
    } catch (Exception e) {
      throw new Fault("btob_MxN_bi_test3 failed", e);
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
      throw new Fault("btob_MxN_bi_test3 failed");
  }
}
