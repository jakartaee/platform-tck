/*
 * Copyright (c) 2008, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jpa.core.annotations.version;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.sun.ts.lib.util.TestUtil;

public class Client1IT extends Client {

  public Client1IT() {
  }


  @BeforeAll
  public void setupIntData() throws Exception {
    TestUtil.logTrace("setupIntData");
    try {
      super.setup();

      removeTestData();
      createIntTestData();

    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: intFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2115; PERSISTENCE:SPEC:2116;
   * PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.1
   *
   * @test_Strategy:
   */
  @Test
  public void intFieldTest() throws Exception {

    boolean pass = false;
    try {
      Int_Field a = getEntityManager().find(Int_Field.class, "1");
      if (a != null) {
        // if (a.getVersion() == 1) {
        TestUtil.logTrace("version:" + a.getVersion());
        int version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Int_Field a1 = getEntityManager().find(Int_Field.class, "1");
        if (a1 != null) {
          if (a1.getVersion() > version) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Exception("intFieldTest failed");
    }

  }

  /*
   * @testName: intPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2115; PERSISTENCE:SPEC:2116;
   * PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.1
   *
   * @test_Strategy:
   */
  @Test
  public void intPropertyTest() throws Exception {
    boolean pass = false;
    try {
      Int_Property a = getEntityManager().find(Int_Property.class, "2");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getValue());
        // if (a.getVersion() == 1) {
        int version = a.getValue();

        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Int_Property a1 = getEntityManager().find(Int_Property.class, "2");
        if (a1 != null) {
          if (a1.getValue() > version) {
            TestUtil.logTrace("version:" + a1.getValue());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getValue());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Exception("intPropertyTest failed");
    }

  }

  /*
   * @testName: integerFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.2
   *
   * @test_Strategy:
   */
  @Test
  public void integerFieldTest() throws Exception {

    boolean pass = false;
    try {
      Integer_Field a = getEntityManager().find(Integer_Field.class, "3");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        Integer version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Integer_Field a1 = getEntityManager().find(Integer_Field.class, "3");
        if (a1 != null) {
          if (a1.getVersion() > version) {
            TestUtil.logTrace("version:" + a1.getVersion());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getVersion());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Exception("integerFieldTest failed");
    }

  }

  /*
   * @testName: integerPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.2
   *
   * @test_Strategy:
   */
  @Test
  public void integerPropertyTest() throws Exception {
    boolean pass = false;
    try {
      Integer_Property a = getEntityManager().find(Integer_Property.class, "4");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicInteger());
        // if (a.getVersion() == 1) {
        Integer version = a.getBasicInteger();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Integer_Property a1 = getEntityManager().find(Integer_Property.class,
            "4");
        if (a1 != null) {
          if (a1.getBasicInteger() > version) {
            TestUtil.logTrace("version:" + a1.getBasicInteger());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicInteger());
          }
        } else {
          TestUtil.logErr("Second find returned null result");
        }
        /*
         * } else { TestUtil.logErr("Did not get a version of 1 after find"); }
         */
      } else {
        TestUtil.logErr("Find returned null result");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass) {
      throw new Exception("integerPropertyTest failed");
    }

  }

  public void createIntTestData() {
    TestUtil.logTrace("createIntTestData");

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(new Int_Field("1"));
      getEntityManager().persist(new Int_Property("2"));
      getEntityManager().persist(new Integer_Field("3", new Integer(0)));
      getEntityManager().persist(new Integer_Property("4", new Integer(0)));

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createIntTestData:", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception re) {
        TestUtil.logErr("Unexpected Exception during Rollback:", re);
      }
    }

  }
}
