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
 * $Id$
 */

package com.sun.ts.tests.jpa.core.types.primarykey.compound;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;

public class Client extends PMClientBase {

  private static final CompoundPK refPK1 = new CompoundPK(1, "cof0001", 1.0F);

  private static final CompoundPK refPK2 = new CompoundPK(2, "cof0002", 2.0F);

  private static final CompoundPK2 refPK3 = new CompoundPK2(3, "cof0003", 3.0F);

  private static final CompoundPK2 refPK4 = new CompoundPK2(4, "cof0004", 4.0F);

  private static final CompoundPK3 refPK5 = new CompoundPK3(5, "cof0005", 5.0F);

  private static final CompoundPK3 refPK6 = new CompoundPK3(6, "cof0006", 6.0F);

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {

    TestUtil.logTrace("Entering setup");
    super.setup(args, p);
    removeTestData();
  }

  /**
   * @testName: testCompoundPK1
   * @assertion_ids: PERSISTENCE:SPEC:1063; PERSISTENCE:SPEC:1064;
   *                 PERSISTENCE:SPEC:533; PERSISTENCE:SPEC:547;
   *                 PERSISTENCE:SPEC:1127; PERSISTENCE:SPEC:535;
   *                 PERSISTENCE:SPEC:544; PERSISTENCE:SPEC:545;
   *                 PERSISTENCE:SPEC:546
   * @test_Strategy: Define a 3.0 Entity bean with a compound primary key
   *                 <p/>
   *                 A composite primary key may either be represented and
   *                 mapped as an embeddable class or may be represented and
   *                 mapped to multiple fields or properties of the entity
   *                 class.
   *                 <p/>
   *                 Using an @Embeddable composite primary key class mapped as
   *                 a @EmbeddedId, Check that you can: - Create bean instances
   *                 - Discover these instances with
   *                 EntityManager.find(EntityClass, primaryKey) - Remove the
   *                 beans using EntityManager.remove(Object entity)
   *                 <p/>
   *                 There should be only one EmbeddedId annotation and no Id
   *                 annotations when the EmbeddedId annotation is used.
   */
  public void testCompoundPK1() throws Fault {

    TestBean bean1;
    TestBean bean2;
    TestBean bean3;
    TestBean bean4;
    CompoundPK valPK1;
    CompoundPK valPK2;
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Creating bean1 and bean2 instance...");
      bean1 = new TestBean(refPK1, "Arabica", 10);
      bean2 = new TestBean(refPK2, "Java", 12);
      getEntityManager().persist(bean1);
      getEntityManager().persist(bean2);
      getEntityManager().flush();

      TestUtil.logTrace("[Client] Locate beans using primary keys...");
      bean3 = getEntityManager().find(TestBean.class, refPK1);
      bean4 = getEntityManager().find(TestBean.class, refPK2);

      TestUtil.logTrace("Check we can call the beans...");
      bean1.ping();
      bean2.ping();
      bean3.ping();
      bean4.ping();

      TestUtil.logTrace("Check beans are identical...");
      if ((bean1 == bean3)) {
        TestUtil.logTrace("bean1 and bean3 are equal");
        pass = true;
      }
      if (!pass) {
        throw new Exception(
            "testCompoundPK1: bean1 and bean3 should be identical!");
      }

      if ((bean2 == bean4)) {
        TestUtil.logTrace("bean2 and bean4 are equal");
        pass = true;
      }
      if (!pass) {
        throw new Exception(
            "[testCompoundPK1] bean2 and bean4 should be identical!");
      }

      TestUtil.logTrace("Comparing primary keys...");
      valPK1 = bean3.getCompoundPK();
      valPK2 = bean4.getCompoundPK();
      if (valPK1.equals(refPK1) && refPK1.equals(valPK1)) {
        TestUtil.logTrace("testCompoundPK1: valPK1 equals refPK1");
        pass = true;
      }
      if (!pass) {
        throw new Exception(
            "testCompoundPK1: bean1 and bean3 PK should match!");
      }
      if (valPK2.equals(refPK2) && refPK2.equals(valPK2)) {
        TestUtil.logTrace("testCompoundPK1: valPK2 equals refPK2");
        pass = true;
      }
      if (!pass) {
        throw new Fault("[testCompoundPK1] bean2 and bean4 PK should match!");
      }

    } catch (Exception e) {
      TestUtil.logErr("[testCompoundPK1] Caught exception: " + e);
      throw new Fault("testCompoundPK1 test failed: " + e, e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "[testCompoundPK1] Exception caught while rolling back TX", e);
      }
    }

    /* testCompoundPK1 pass */
  }

  /**
   * @testName: testCompoundPK2
   * @assertion_ids: PERSISTENCE:SPEC:1065; PERSISTENCE:SPEC:1066;
   *                 PERSISTENCE:JAVADOC:85; PERSISTENCE:SPEC:548;
   *                 PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:535;
   *                 PERSISTENCE:SPEC:544; PERSISTENCE:SPEC:545;
   *                 PERSISTENCE:SPEC:546; PERSISTENCE:SPEC:1102
   * @test_Strategy: Define a 3.0 Entity bean with a compound primary key
   *                 <p/>
   *                 A composite primary key may either be represented and
   *                 mapped as an embeddable class or may be represented and
   *                 mapped to multiple fields or properties of the entity
   *                 class.
   *                 <p/>
   *                 Using the @IdClass annotation to define a composite primary
   *                 key class where the primary keys are mapped to multiple
   *                 properties of the entity class, Check that you can: -
   *                 Create bean instances - Discover these instances with
   *                 EntityManager.find(EntityClass, primaryKey) - Remove the
   *                 beans using EntityManager.remove(Object entity)
   *                 <p/>
   *                 When using the @IdClass annotion, the @Id annotation must
   *                 also be applied to such field or properties. This entity
   *                 uses the @Id annotations on the primary key properties.
   */
  public void testCompoundPK2() throws Fault {

    TestBean2 bean1;
    TestBean2 bean2;
    TestBean2 bean3;
    TestBean2 bean4;
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Creating bean1 and bean2 instance...");
      bean1 = new TestBean2(3, "cof0003", 3.0F, "Vanilla", 10);
      bean2 = new TestBean2(4, "cof0004", 4.0F, "Mocha Java", 12);
      getEntityManager().persist(bean1);
      getEntityManager().persist(bean2);
      getEntityManager().flush();

      TestUtil.logTrace("Locate beans using primary keys...");
      bean3 = getEntityManager().find(TestBean2.class, refPK3);
      bean4 = getEntityManager().find(TestBean2.class, refPK4);

      TestUtil.logTrace("Check we can call the beans...");
      bean1.ping();
      bean2.ping();
      bean3.ping();
      bean4.ping();

      TestUtil.logTrace("Check beans are identical...");
      if ((bean1 == bean3)) {
        TestUtil.logTrace("bean1 and bean3 are equal");
        pass = true;
      }
      if (!pass) {
        throw new Exception(
            "[testCompoundPK2] bean1 and bean3 should be identical!");
      }

      if ((bean2 == bean4)) {
        TestUtil.logTrace("bean2 and bean4 are equal");
        pass = true;
      }
      if (!pass) {
        throw new Fault("[testCompoundPK2] bean2 and bean4 PK should match!");
      }

    } catch (Exception e) {
      TestUtil.logErr("[testCompoundPK2] Caught exception: " + e);
      throw new Fault("testCompoundPK2 test failed: " + e, e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "[testCompoundPK2] Exception caught while rolling back TX", e);
      }
    }

    /* testCompoundPK2 pass */
  }

  /**
   * @testName: testCompoundPK3
   * @assertion_ids: PERSISTENCE:SPEC:1065; PERSISTENCE:SPEC:1066;
   *                 PERSISTENCE:JAVADOC:85; PERSISTENCE:SPEC:548;
   *                 PERSISTENCE:SPEC:549; PERSISTENCE:SPEC:535;
   *                 PERSISTENCE:SPEC:544; PERSISTENCE:SPEC:545
   * @test_Strategy: Define a 3.0 Entity bean with a compound primary key
   *                 <p/>
   *                 A composite primary key may either be represented and
   *                 mapped as an embeddable class or may be represented and
   *                 mapped to multiple fields or properties of the entity
   *                 class.
   *                 <p/>
   *                 Using the @IdClass annotation to define a composite primary
   *                 key class where the primary keys are mapped to multiple
   *                 fields of the entity class, Check that you can: - Create
   *                 bean instances - Discover these instances with
   *                 EntityManager.find(EntityClass, primaryKey) - Remove the
   *                 beans using EntityManager.remove(Object entity)
   *                 <p/>
   *                 When using the @IdClass annotion, the @Id annotation must
   *                 also be applied to such fields.
   */
  public void testCompoundPK3() throws Fault {

    TestBean3 bean1;
    TestBean3 bean2;
    TestBean3 bean3;
    TestBean3 bean4;
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("Creating bean1 and bean2 instance...");
      bean1 = new TestBean3(5, "cof0005", 5.0F, "Cinnamon", 11);
      bean2 = new TestBean3(6, "cof0006", 6.0F, "Hazelnut", 12);
      getEntityManager().persist(bean1);
      getEntityManager().persist(bean2);
      getEntityManager().flush();

      TestUtil.logTrace("Locate beans using primary keys...");
      bean3 = getEntityManager().find(TestBean3.class, refPK5);
      bean4 = getEntityManager().find(TestBean3.class, refPK6);

      TestUtil.logTrace("Check we can call the beans...");
      bean1.ping();
      bean2.ping();
      bean3.ping();
      bean4.ping();

      TestUtil.logTrace("Check beans are identical...");
      if ((bean1 == bean3)) {
        TestUtil.logTrace("bean1 and bean3 are equal");
        pass = true;
      }
      if (!pass) {
        throw new Exception(
            "[testCompoundPK3] bean1 and bean3 should be identical!");
      }

      if ((bean2 == bean4)) {
        TestUtil.logTrace("bean2 and bean4 are equal");
        pass = true;
      }
      if (!pass) {
        throw new Fault("[testCompoundPK3] bean2 and bean4 PK should match!");
      }

    } catch (Exception e) {
      TestUtil.logErr("[testCompoundPK3] Caught exception: " + e);
      throw new Fault("testCompoundPK3 test failed: " + e, e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception e) {
        TestUtil.logErr(
            "[testCompoundPK3] Exception caught while rolling back TX", e);
      }
    }

    /* testCompoundPK3 pass */
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("Cleanup data");
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
      getEntityManager().createNativeQuery("DELETE FROM PKEY").executeUpdate();
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
