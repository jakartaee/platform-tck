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
 * $Id$
 */

package com.sun.ts.tests.jpa.se.descriptor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

public class Client extends PMClientBase {

  private static final B bRef[] = new B[5];


  @BeforeEach
  public void setup() throws Exception {
    try {

      super.setup();
      removeTestData();
      createTestData();
    } catch (Exception e) {
      throw new Exception("Setup Failed!", e);
    }
  }

  /*
   * @testName: test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:900; PERSISTENCE:SPEC:901;
   * PERSISTENCE:SPEC:850; PERSISTENCE:SPEC:852; PERSISTENCE:SPEC:854;
   * PERSISTENCE:SPEC:859; PERSISTENCE:SPEC:952; PERSISTENCE:SPEC:968;
   * PERSISTENCE:SPEC:909; PERSISTENCE:SPEC:910; PERSISTENCE:SPEC:893;
   * PERSISTENCE:SPEC:939; PERSISTENCE:SPEC:953; PERSISTENCE:SPEC:945;
   * PERSISTENCE:SPEC:943; PERSISTENCE:SPEC:969; PERSISTENCE:SPEC:970;
   * PERSISTENCE:JAVADOC:162;
   * 
   * @test_Strategy: With the above archive, deploy bean, create entities,
   * persist, then find.
   *
   */
@Test
  public void test1() throws Exception {
    boolean pass = false;
    try {

      B anotherB = getEntityManager().find(B.class, "1");

      if (anotherB != null) {
        if (anotherB.equals(bRef[0])) {
          TestUtil.logTrace("get expected B");
          pass = true;
        } else {
          TestUtil.logErr("Did not get expected B:" + anotherB.toString());
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Exception("test1 failed");
    }
  }

  public void createTestData() {
    TestUtil.logTrace("createTestData");

    try {

      TestUtil.logTrace("Create 2 B Entities");
      bRef[0] = new B("1", "myB", 1);
      bRef[1] = new B("2", "yourB", 2);

      TestUtil.logTrace("Start to persist Bs ");
      for (B b : bRef) {
        if (b != null) {
          getEntityManager().persist(b);
          TestUtil.logTrace("persisted B " + b);
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception while creating test data:" + e);
    }
  }

  @AfterEach
  public void cleanup() throws Exception {
    TestUtil.logTrace("cleanup");
    removeTestData();
    super.cleanup();
  }

  private void removeTestData() {
    TestUtil.logTrace("removeTestData");
    if (getEntityTransaction().isActive()) {
      getEntityTransaction().rollback();
    }
    try {
      getEntityTransaction().begin();
      getEntityManager().createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB")
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
