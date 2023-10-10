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

public class Client3IT extends Client {

  public Client3IT() {
  }


  @BeforeAll
  public void setupLongData() throws Exception {
    TestUtil.logTrace("setupLongData");
    try {
      super.setup();

      removeTestData();
      createLongTestData();

    } catch (Exception e) {
      throw new Exception("Setup failed:", e);
    }
  }

  /*
   * @testName: longFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.5
   *
   * @test_Strategy:
   */
  @Test
  public void longFieldTest() throws Exception {

    boolean pass = false;
    try {
      Long_Field a = getEntityManager().find(Long_Field.class, "1");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        long version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Long_Field a1 = getEntityManager().find(Long_Field.class, "1");
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
      throw new Exception("shortFieldTest failed");
    }

  }

  /*
   * @testName: longPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.5
   *
   * @test_Strategy:
   */
  @Test
  public void longPropertyTest() throws Exception {
    boolean pass = false;
    try {
      Long_Property a = getEntityManager().find(Long_Property.class, "2");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicLong());
        // if (a.getVersion() == 1) {
        long version = a.getBasicLong();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        Long_Property a1 = getEntityManager().find(Long_Property.class, "2");
        if (a1 != null) {
          if (a1.getBasicLong() > version) {
            TestUtil.logTrace("version:" + a1.getBasicLong());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicLong());
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
      throw new Exception("shortPropertyTest failed");
    }

  }

  /*
   * @testName: longClassFieldTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.6
   *
   * @test_Strategy:
   */
  @Test
  public void longClassFieldTest() throws Exception {

    boolean pass = false;
    try {
      LongClass_Field a = getEntityManager().find(LongClass_Field.class, "3");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getVersion());
        // if (a.getVersion() == 1) {
        Long version = a.getVersion();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        LongClass_Field a1 = getEntityManager().find(LongClass_Field.class,
            "3");
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
      throw new Exception("shortClassFieldTest failed");
    }

  }

  /*
   * @testName: longClassPropertyTest
   *
   * @assertion_ids: PERSISTENCE:SPEC:2117; PERSISTENCE:SPEC:2117.6
   *
   * @test_Strategy:
   */
  @Test
  public void longClassPropertyTest() throws Exception {
    boolean pass = false;
    try {
      LongClass_Property a = getEntityManager().find(LongClass_Property.class,
          "4");
      if (a != null) {
        TestUtil.logTrace("version:" + a.getBasicLong());
        // if (a.getVersion() == 1) {
        Long version = a.getBasicLong();
        a.setName("two");
        getEntityTransaction().begin();
        getEntityManager().merge(a);
        getEntityManager().flush();
        getEntityTransaction().commit();
        LongClass_Property a1 = getEntityManager()
            .find(LongClass_Property.class, "4");
        if (a1 != null) {
          if (a1.getBasicLong() > version) {
            TestUtil.logTrace("version:" + a1.getBasicLong());
            pass = true;
          } else {
            TestUtil
                .logErr("Did not get a greater version after a modification:"
                    + a1.getBasicLong());
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
      throw new Exception("shortClassPropertyTest failed");
    }

  }

  public void createLongTestData() {
    TestUtil.logTrace("createLongTestData");

    try {
      getEntityTransaction().begin();
      getEntityManager().persist(new Long_Field("1"));
      getEntityManager().persist(new Long_Property("2"));
      getEntityManager().persist(new LongClass_Field("3", new Long(0)));
      getEntityManager().persist(new LongClass_Property("4", new Long(0)));
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception in createLongTestData:", e);
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
