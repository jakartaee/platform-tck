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

package com.sun.ts.tests.jpa.core.entitytest.cascadeall.manyXone;

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
   * BEGIN Test Cases
   */

  /*
   * @testName: cascadeAllMX1Test1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:623;
   * PERSISTENCE:JAVADOC:112; PERSISTENCE:JAVADOC:113
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows: The perist
   * operation is cascaded to entities referenced by X, if the relationship from
   * X to these other entities is annotated with cascade=ALL annotation member.
   *
   * Invoke persist on a ManyToOne relationship from X annotated with
   * cascade=ALL and ensure the persist operation is cascaded.
   *
   */

  public void cascadeAllMX1Test1() throws Fault {
    TestUtil.logTrace("Begin cascadeAllMX1Test1");
    boolean pass = false;
    A aRef;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      aRef = new A("1", "bean1", 1);
      final B b1 = new B("1", "b1", 1, aRef);
      getEntityManager().persist(b1);
      final B b2 = new B("2", "b2", 1, aRef);
      getEntityManager().persist(b2);
      final B b3 = new B("3", "b3", 1, aRef);
      getEntityManager().persist(b3);
      final B b4 = new B("4", "b4", 1, aRef);
      getEntityManager().persist(b4);
      getEntityManager().flush();

      final A newA1 = b1.getA1Info();
      final A newA2 = b2.getA1Info();
      final A newA3 = b3.getA1Info();
      final A newA4 = b4.getA1Info();

      if ((newA1 != null) && (newA2 != null) && (newA3 != null)
          && (newA4 != null)) {
        pass = true;
      } else {
        TestUtil.logErr("Wrong results received");
        pass = false;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }

    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test1 failed");
  }

  /*
   * @testName: cascadeAllMX1Test2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:624;
   * PERSISTENCE:JAVADOC:111
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows:
   *
   * If X is a removed entity, it becomes managed.
   *
   * Create an entity, persist it, remove it, and invoke persist again. Check
   * that it is managed and is accessible.
   *
   */

  public void cascadeAllMX1Test2() throws Fault {
    TestUtil.logTrace("Begin cascadeAllMX1Test2");
    boolean pass = false;
    A aRef;

    try {
      getEntityTransaction().begin();
      aRef = new A("2", "bean2", 2);
      final B b1 = new B("2", "b2", 2, aRef);
      getEntityManager().persist(b1);
      getEntityManager().flush();

      final A newA1 = b1.getA1Info();

      if (newA1 != null) {
        try {
          TestUtil.logTrace("Remove b1 ");
          getEntityManager().remove(newA1);
          getEntityManager().remove(b1);
          getEntityManager().flush();

          TestUtil.logTrace("Persist a removed entity ");
          final B newB = findB("2");
          if (null == newB) {
            getEntityManager().persist(b1);
            getEntityManager().flush();
            pass = ((getEntityManager().contains(b1)) && (b1.getA1() != null));
          } else {
            TestUtil.logTrace("entity B not removed");
          }
          getEntityTransaction().commit();
        } catch (Exception ee) {
          TestUtil.logErr(
              "Unexpected exception trying to persist a removed entity", ee);
        }
      } else {
        TestUtil.logTrace("Instance is not already persisted. Test Fails.");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test2 failed");
  }

  /*
   * @testName: cascadeAllMX1Test3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:668
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true:
   *
   * If the entity has been retrieved from the database and has not been removed
   * or detached.
   */

  public void cascadeAllMX1Test3() throws Fault {
    TestUtil.logTrace("Begin cascadeAllMX1Test3");
    boolean pass = false;
    A aRef;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      aRef = new A("4", "bean4", 4);
      getEntityManager().persist(aRef);
      final B b1 = new B("1", "b1", 4);
      getEntityManager().persist(b1);
      final B b2 = new B("2", "b2", 4);
      getEntityManager().persist(b2);
      final B b3 = new B("3", "b3", 4);
      getEntityManager().persist(b3);
      final B b4 = new B("4", "b4", 4);
      getEntityManager().persist(b4);

      b1.setA1(aRef);
      b2.setA1(aRef);
      b3.setA1(aRef);
      b4.setA1(aRef);

      getEntityManager().flush();

      final A newA1 = b1.getA1Info();
      final A newA2 = b2.getA1Info();
      final A newA3 = b3.getA1Info();
      final A newA4 = b4.getA1Info();

      if (((newA1 != null) && (getEntityManager().contains(newA1)))
          && ((newA2 != null) && (getEntityManager().contains(newA2)))
          && ((newA3 != null) && (getEntityManager().contains(newA3)))
          && ((newA4 != null) && (getEntityManager().contains(newA4)))) {
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Unexpected exception caught: " + e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test3 failed");
  }

  /*
   * @testName: cascadeAllMX1Test4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:669;
   * PERSISTENCE:SPEC:677
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true: If the entity instance is
   * new and the persist method has been called on the entity. The effect of
   * cascading persist is immediately visible visible to the contains method.
   */

  public void cascadeAllMX1Test4() throws Fault {
    TestUtil.logTrace("Begin cascadeAllMX1Test4");
    boolean pass = false;
    A aRef;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      aRef = new A("5", "bean5", 5);
      final B b1 = new B("1", "b1", 5, aRef);
      getEntityManager().persist(b1);
      final B b2 = new B("2", "b2", 5, aRef);
      getEntityManager().persist(b2);
      final B b3 = new B("3", "b3", 5, aRef);
      getEntityManager().persist(b3);
      final B b4 = new B("4", "b4", 5, aRef);
      getEntityManager().persist(b4);

      pass = getInstanceStatus(aRef);

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test4 failed");
  }

  /*
   * @testName: cascadeAllMX1Test5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:676
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns false:
   *
   * If the entity instance is new and the persist operation has not been
   * cascaded to it.
   *
   */
  public void cascadeAllMX1Test5() throws Fault {
    boolean pass = false;
    A aRef;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      aRef = new A("6", "bean6", 6);
      final B b1 = new B("1", "b1", 6, aRef);
      final B b2 = new B("2", "b2", 6, aRef);
      final B b3 = new B("3", "b3", 6, aRef);
      final B b4 = new B("4", "b4", 6, aRef);

      pass = ((!getInstanceStatus(aRef)) && (!getInstanceStatus(b1))
          && (!getInstanceStatus(b2)) && (!getInstanceStatus(b3))
          && (!getInstanceStatus(b4)));

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test5 failed");
  }

  /*
   * @testName: cascadeAllMX1Test6
   * 
   * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:675
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns false:
   *
   * If the entity instance is new and the persist operation not been called on
   * it.
   */

  public void cascadeAllMX1Test6() throws Fault {
    boolean pass = false;

    A a1;
    B bRef;
    try {
      getEntityTransaction().begin();
      a1 = new A("7", "b7", 7);
      bRef = new B("7", "bean7", 7, a1);

      pass = ((!getEntityManager().contains(bRef))
          && (!getEntityManager().contains(a1)));

      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test6 failed");
  }

  /*
   * @testName: cascadeAllMX1Test7
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:622
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows: The perist
   * operation is cascaded to entities referenced by X, if the relationship from
   * X to these other entities is annotated with cascade=ALL annotation member.
   *
   * Invoke persist on a ManyToOne relationship from X annotated with
   * cascade=ALL and ensure the persist operation is cascaded.
   *
   * If X is a pre-existing managed entity, it is ignored by the persist
   * operation. However, the persist operation is cascaded to entities
   * referenced by X, if the relationships from X to these other entities is
   * annotated with cascade=ALL annotation member value.
   *
   */

  public void cascadeAllMX1Test7() throws Fault {
    boolean pass = false;
    A a1;
    A a2;
    B bRef;
    B bRef1;

    try {
      getEntityTransaction().begin();
      a1 = new A("8", "a8", 8);
      bRef = new B("8", "bean8", 8);
      getEntityManager().persist(bRef);

      if (getEntityManager().contains(bRef)) {
        bRef1 = findB("8");
        bRef1.setA1(a1);
        getEntityManager().persist(bRef1);
        pass = getEntityManager().contains(a1);
        TestUtil.logTrace("try to find A");
        a2 = findA("8");
        if (null != a2) {
          TestUtil.logTrace("b2 is not null");
        }
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test7 failed");
  }

  /*
   * @testName: cascadeAllMX1Test8
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:642
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * If X is a managed entity, it is synchronized to the database.
   *
   */

  public void cascadeAllMX1Test8() throws Fault {
    boolean pass = false;
    B bRef;
    A a1;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      a1 = new A("9", "A9", 9);
      bRef = new B("9", "bean9", 9);
      getEntityManager().persist(bRef);

      if (null == bRef.getA1()) {
        bRef.setA1(a1);
        getEntityManager().flush();
      }

      if ((null != a1) && (getEntityManager().contains(a1))) {
        A result = bRef.getA1Info();
        pass = true;
      } else {
        TestUtil.logErr("Entity not managed - test fails.");
      }
      getEntityTransaction().commit();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test8 failed");
  }

  /*
   * @testName: cascadeAllMX1Test9
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:644
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * For all entities Y referenced by a relationship from X, if the relationship
   * to Y has been annotated with the cascade member value cascade=ALL, the
   * persist operation is applied to Y.
   *
   */

  public void cascadeAllMX1Test9() throws Fault {
    boolean pass = false;

    B bRef;
    A a1;

    try {
      TestUtil.logTrace("New instances");
      getEntityTransaction().begin();
      a1 = new A("10", "a10", 10);
      bRef = new B("10", "bean10", 10, a1);

      getEntityManager().persist(bRef);
      getEntityManager().flush();
      pass = getEntityManager().contains(a1);
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
      pass = false;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test9 failed");
  }

  /*
   * @testName: cascadeAllMX1Test10
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:646
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * For any detached entity Y referenced by a relationship from X, where the
   * relationship to Y has not been annotated with the cascade member value
   * cascade=ALL the semantics depend upon the ownership of the relationship. If
   * X owns the relationship, any changes to the relationship are synchronized
   * with the database, otherwise, if Y owns the relationship, the behavior is
   * undefined.
   *
   */

  public void cascadeAllMX1Test10() throws Fault {
    A a1;
    B bRef;
    B b2;
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      a1 = new A("11", "a11", 11);
      bRef = new B("11", "bean11", 11, a1);
      getEntityManager().persist(bRef);
      getEntityTransaction().commit();

      getEntityTransaction().begin();
      b2 = findB("11");
      A newA = b2.getA1();
      newA.setAName("newA");
      getEntityManager().flush();
      if ((b2.isA()) && (newA.getAName().equals("newA"))) {
        pass = true;
      }
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred", e);
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass)
      throw new Fault("cascadeAllMX1Test10 failed");
  }

  /*
   *
   * Business Methods to set up data for Test Cases
   *
   */

  private A findA(final String id) {
    TestUtil.logTrace("Entered findA method");
    return getEntityManager().find(A.class, id);
  }

  private B findB(final String id) {
    TestUtil.logTrace("Entered findB method");
    return getEntityManager().find(B.class, id);
  }

  private boolean getInstanceStatus(final Object o) {
    TestUtil.logTrace("Entered getInstanceStatus method");
    return getEntityManager().contains(o);
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
      getEntityManager().createNativeQuery("DELETE FROM BEJB_MX1_UNI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM AEJB_MX1_UNI_BTOB")
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
