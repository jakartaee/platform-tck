/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.ee.packaging.jar;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;

public class Client extends PMClientBase {

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      super.setup(args, p);
      removeTestData();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  /*
   * @testName: JarFileElementsTest
   * 
   * @assertion_ids: PERSISTENCE:SPEC:957
   * 
   * @test_Strategy: With the above archive (jar-file elements), deploy, create
   * entities persist, then find.
   *
   */

  public void JarFileElementsTest() throws Fault {
    boolean pass = true;
    final int count = 2;
    getEntityTransaction().begin();
    for (int i = 1; i <= count; i++) {
      A a = new A(Integer.toString(i), "name_" + Integer.toString(i), i);
      getEntityManager().persist(a);
      TestUtil.logTrace("persisted order " + a.toString());
    }
    for (int i = 1 + count; i <= count + count; i++) {
      C c = new C(Integer.toString(i), "name_" + Integer.toString(i), i);
      getEntityManager().persist(c);
      TestUtil.logTrace("persisted order " + c.toString());
    }
    for (int i = 1; i <= count; i++) {
      B b = new B(Integer.toString(i), "name_" + Integer.toString(i), i);
      getEntityManager().persist(b);
      TestUtil.logTrace("persisted order " + b.toString());
    }

    getEntityTransaction().commit();

    TestUtil.logTrace("find the previously persisted entities");
    for (int i = 1; i <= count; i++) {
      A a = getEntityManager().find(A.class, Integer.toString(i));
      if (a != null) {
        TestUtil.logTrace("Find returned non-null A entity:" + a.toString());
      } else {
        TestUtil.logErr("persisted A[" + i + "] DOES NOT EXIST");
        pass = false;
      }
    }
    for (int i = 1 + count; i <= count + count; i++) {
      C c = getEntityManager().find(C.class, Integer.toString(i));
      if (c != null) {
        TestUtil.logTrace("Find returned non-null C entity:" + c.toString());
      } else {
        TestUtil.logErr("persisted C[" + i + "] DOES NOT EXIST");
        pass = false;
      }
    }
    for (int i = 1; i <= count; i++) {
      B b = getEntityManager().find(B.class, Integer.toString(i));
      if (b != null) {
        TestUtil.logTrace("Find returned non-null B entity:" + b.toString());
      } else {
        TestUtil.logErr("persisted B[" + i + "] DOES NOT EXIST");
        pass = false;
      }
    }
    if (!pass) {
      throw new Fault("JarFileElementsTest failed");
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
    removeTestData();
    TestUtil.logTrace("cleanup complete, calling super.cleanup");
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB")
          .executeUpdate();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception in removeTestData:", re);
      }
    }
  }

}
