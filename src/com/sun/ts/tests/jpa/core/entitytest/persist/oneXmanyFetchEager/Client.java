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

package com.sun.ts.tests.jpa.core.entitytest.persist.oneXmanyFetchEager;

import com.sun.javatest.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
   * @testName: persist1XMTest1
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:622;
   * PERSISTENCE:JAVADOC:7; PERSISTENCE:JAVADOC:130
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows: The perist
   * operation is cascaded to entities referenced by X, if the relationship from
   * X to these other entities is annotated with cascade=PERSIST annotation
   * member.
   *
   * Invoke persist on a OneToMany relationship from X annotated with
   * cascade=PERSIST and ensure the persist operation is cascaded.
   *
   */
  public void persist1XMTest1() throws Fault {
    TestUtil.logTrace("Begin persist1XMTest1");
    boolean pass = false;
    A aRef;
    List<B> bCol;
    List<B> newCol;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      final B b1 = new B("1", "b1", 1);
      final B b2 = new B("2", "b2", 1);
      final B b3 = new B("3", "b3", 1);
      final B b4 = new B("4", "b4", 1);
      List<B> v1 = new java.util.ArrayList<B>();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);
      aRef = new A("1", "bean1", 1, v1);
      getEntityManager().persist(aRef);

      newCol = aRef.getBCol();

      dumpCollectionDataB(newCol);

      if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3)
          && newCol.contains(b4)) {
        pass = true;
      } else {
        TestUtil.logErr("Test failed");
        pass = false;
      }
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

    if (!pass) {
      throw new Fault("persist1XMTest1 failed");
    }
  }

  /*
   * @testName: persist1XMTest2
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:644
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows: The perist
   * operation is cascaded to entities referenced by X, if the relationship from
   * X to these other entities is annotated with cascade=PERSIST annotation
   * member.
   *
   * For all entities Y referenced by a relationship from X, if the relationship
   * to Y has been annotated with the cascade member value cascade=PERSIST, the
   * persist operation is applied to Y.
   *
   * Invoke persist on a relationship from X annotated where Y IS NOT annotated
   * with cascade=PERSIST and ensure the persist operation is not cascaded. An
   * IllegalStateException should be thrown.
   *
   */
  public void persist1XMTest2() throws Fault {
    TestUtil.logTrace("Begin persist1XMTest2");
    boolean pass = false;
    B bRef;
    A a1;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      a1 = new A("2", "a2", 2);

      bRef = new B("2", "bean2", 2, a1);
      getEntityManager().persist(bRef);

      // During flush, since we only persist B, not A,
      // and the relationship from B to A is not cascade,
      // flush should trigger IllegalStateException.
      getEntityManager().flush();
      getEntityTransaction().rollback();
    } catch (IllegalStateException e) {
      TestUtil.logTrace("IllegalStateException Caught as Expected.");
      pass = true;
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

    if (!pass) {
      throw new Fault("persist1XMTest2 failed");
    }
  }

  /*
   * @testName: persist1XMTest3
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:624
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
  public void persist1XMTest3() throws Fault {
    TestUtil.logTrace("Begin persist1XMTest3");
    boolean pass = false;
    A aRef;
    A newA;
    List<B> newCol;

    try {
      TestUtil.logTrace("New instances");
      final B b1 = new B("1", "b1", 3);
      final B b2 = new B("2", "b2", 3);
      final B b3 = new B("3", "b3", 3);
      final B b4 = new B("4", "b4", 3);
      List<B> v1 = new java.util.ArrayList<B>();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);
      getEntityTransaction().begin();
      aRef = new A("3", "bean3", 3, v1);
      getEntityManager().persist(aRef);
      newCol = aRef.getBCol();

      dumpCollectionDataB(newCol);

      if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3)
          && newCol.contains(b4)) {
        TestUtil.logTrace("Remove aRef ");
        getEntityManager().remove(findA("3"));
        getEntityManager().flush();

        TestUtil.logTrace("Persist a removed entity ");
        newA = findA("3");
        if (null == newA) {
          newA = new A("3", "bean3", 3, v1);
          getEntityManager().persist(newA);
          pass = ((getInstanceStatus(newA)) && (findA("3") != null));
        } else {
          TestUtil.logTrace("entity A not removed");
          pass = false;
        }
      } else {
        TestUtil.logTrace("Instance is not already persisted. Test Fails.");
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

    if (!pass) {
      throw new Fault("persist1XMTest3 failed");
    }
  }

  /*
   * @testName: persist1XMTest4
   * 
   * @assertion_ids: PERSISTENCE:SPEC:628; PERSISTENCE:SPEC:632
   * 
   * @test_Strategy: A managed entity instance becomes removed by invoking the
   * remove method on it or by cascading the remove operation. The semantics of
   * the remove operation, applied to an entity X are as follows:
   *
   * Test the remove semantics of a OneToMany relationship and when the
   * relationship is NOT annotated with REMOVE. Ensure the remove is NOT
   * cascaded.
   *
   */
  public void persist1XMTest4() throws Fault {
    TestUtil.logTrace("Begin persist1XMTest4");
    boolean pass = false;
    A a1;
    A a2;
    List<B> newCol;
    try {
      getEntityTransaction().begin();
      final B b1 = new B("1", "b1", 5);
      final B b2 = new B("2", "b2", 5);
      final B b3 = new B("3", "b3", 5);
      final B b4 = new B("4", "b4", 5);
      List<B> v1 = new java.util.ArrayList<B>();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      TestUtil.logTrace("New A instance");
      a1 = new A("5", "bean5", 5, v1);
      getEntityManager().persist(a1);

      a2 = findA("5");
      newCol = a2.getBCol();
      dumpCollectionDataB(newCol);

      if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3)
          && newCol.contains(b4)) {
        try {
          TestUtil.logTrace("Remove instance a1");
          getEntityManager().remove(a1);
          getEntityManager().flush();

          if ((null != findB("1")) && (null != findB("2"))
              && (null != findB("3")) && (null != findB("4"))) {
            pass = true;
          }
        } catch (Exception fe) {
          TestUtil.logErr(
              "Unexpected exception caught trying to remove entity instance :",
              fe);
        }
      } else {
        TestUtil.logTrace("List<B> not persisted, cannot proceed with test");
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

    if (!pass) {
      throw new Fault("persist1XMTest4 failed");
    }
  }

  /*
   * @testName: persist1XMTest5
   * 
   * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:668
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true:
   *
   * If the entity has been retrieved from the database and has not been removed
   * or detached.
   */
  public void persist1XMTest5() throws Fault {
    TestUtil.logTrace("Begin persist1XMTest5");
    boolean pass = false;
    A a1;
    A a2;
    List<B> newCol;
    try {
      getEntityTransaction().begin();
      final B b1 = new B("1", "b1", 6);
      final B b2 = new B("2", "b2", 6);
      final B b3 = new B("3", "b3", 6);
      final B b4 = new B("4", "b4", 6);
      List<B> v1 = new ArrayList<B>();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      TestUtil.logTrace("New A instance");
      a1 = new A("6", "bean6", 6, v1);
      getEntityManager().persist(a1);
      a2 = findA("6");
      newCol = a2.getBCol();

      dumpCollectionDataB(newCol);

      if (newCol.contains(b1) && newCol.contains(b2) && newCol.contains(b3)
          && newCol.contains(b4)) {
        pass = getInstanceStatus(a1);
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

    if (!pass) {
      throw new Fault("persist1XMTest5 failed");
    }
  }

  /*
   * @testName: persist1XMTest6
   * 
   * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:669;
   * PERSISTENCE:SPEC:677
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true: If the entity instance is
   * new and the persist method has been called on the entity. The effect of
   * cascading persist is immediately visible visible to the contains method.
   */
  public void persist1XMTest6() throws Fault {
    TestUtil.logTrace("Begin persist1XMTest6");
    boolean pass = false;
    A a1;
    List<B> newCol;
    try {
      getEntityTransaction().begin();
      final B b1 = new B("1", "b1", 7);
      final B b2 = new B("2", "b2", 7);
      final B b3 = new B("3", "b3", 7);
      final B b4 = new B("4", "b4", 7);
      List<B> v1 = new ArrayList<B>();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      TestUtil.logTrace("New A instance");
      a1 = new A("7", "bean7", 7, v1);
      getEntityManager().persist(a1);
      newCol = a1.getBCol();

      dumpCollectionDataB(newCol);

      if ((newCol.size() != 0) && (getEntityManager().contains(b1))
          && (getEntityManager().contains(b2))
          && (getEntityManager().contains(b3))
          && (getEntityManager().contains(b4))) {
        pass = getInstanceStatus(a1);
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

    if (!pass) {
      throw new Fault("persist1XMTest6 failed");
    }
  }

  /*
   * @testName: persist1XMTest7
   * 
   * @assertion_ids: PERSISTENCE:SPEC:667; PERSISTENCE:SPEC:669;
   * PERSISTENCE:SPEC:677
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true: If the entity instance is
   * new and the persist operation has been cascaded to it.
   *
   * Create an entity instance where cascade=persist is not used. Verify the
   * contains method returns false.
   */
  public void persist1XMTest7() throws Fault {
    TestUtil.logTrace("Begin persist1XMTest7");
    boolean pass = false;
    A a1;
    B bRef;
    try {
      getEntityTransaction().begin();
      a1 = new A("8", "b8", 8);
      bRef = new B("8", "bean8", 8, a1);
      getEntityManager().persist(bRef);

      pass = ((getEntityManager().contains(bRef))
          && (!getEntityManager().contains(a1)));

      // don't do commit, just do rollback
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

    if (!pass) {
      throw new Fault("persist1XMTest7 failed");
    }
  }

  /*
   * @testName: persist1XMTest7IllegalStateException
   * 
   * @assertion_ids: PERSISTENCE:SPEC:646;
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns true: If the entity instance is
   * new and the persist operation has been cascaded to it.
   *
   * Create an entity instance where cascade=persist is not used. Verify that an
   * illegalStateException is thrown when the flush is executed
   */
  public void persist1XMTest7IllegalStateException() throws Fault {
    TestUtil.logTrace("Begin persist1XMTest7IllegalStateException");
    boolean pass = false;
    A a1;
    B bRef;
    try {
      getEntityTransaction().begin();
      a1 = new A("8", "b8", 8);
      bRef = new B("8", "bean8", 8, a1);

      getEntityManager().persist(bRef);
      getEntityManager().flush();
      TestUtil.logErr("IllegalStateException was not thrown");
      pass = false;
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("Received expected Exception :" + ise);
      pass = true;
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

    if (!pass) {
      throw new Fault("persist1XMTest7IllegalStateException failed");
    }
  }

  /*
   * @testName: persist1XMTest8
   * 
   * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:675
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns false:
   *
   * If the entity instance is new and the persist operation not been called on
   * it.
   *
   */
  public void persist1XMTest8() throws Fault {
    boolean pass = true;
    A a1;
    try {
      final B b1 = new B("1", "b1", 9);
      final B b2 = new B("2", "b2", 9);
      final B b3 = new B("3", "b3", 9);
      final B b4 = new B("4", "b4", 9);
      List<B> v1 = new ArrayList<B>();
      v1.add(b1);
      v1.add(b2);
      v1.add(b3);
      v1.add(b4);

      a1 = new A("9", "bean9", 9, v1);

      getEntityTransaction().begin();
      pass = getInstanceStatus(a1);
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

    if (pass) {
      throw new Fault("persist1XMTest8 failed");
    }
  }

  /*
   * @testName: persist1XMTest9
   * 
   * @assertion_ids: PERSISTENCE:SPEC:671; PERSISTENCE:SPEC:676
   * 
   * @test_Strategy: The contains method [used to determine whether an entity
   * instance is in the managed state] returns false:
   *
   * If the entity instance is new and the persist operation has not been
   * cascaded to it.
   */
  public void persist1XMTest9() throws Fault {
    boolean pass = false;

    A a1;
    B bRef;
    try {
      getEntityTransaction().begin();
      a1 = new A("10", "b10", 10);
      bRef = new B("10", "bean10", 10, a1);

      pass = ((!getEntityManager().contains(bRef))
          && (!getEntityManager().contains(a1)));

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

    if (!pass) {
      throw new Fault("persist1XMTest9 failed");
    }
  }

  /*
   * @testName: persist1XMTest10
   * 
   * @assertion_ids: PERSISTENCE:SPEC:619; PERSISTENCE:SPEC:642
   * 
   * @test_Strategy: Using a 1xmany bi-directional relationship between entity
   * objects. Ensure the proper relationship results are correct.
   *
   */
  public void persist1XMTest10() throws Fault {
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      // Create A and B entity objects
      TestUtil.logTrace("Create A and B Entity Objects");
      final B b11 = new B("19", "b19", 19);
      final B b12 = new B("20", "b29", 29);
      List<B> v1 = new ArrayList<B>();
      v1.add(b11);
      v1.add(b12);

      A aRef = new A("11", "bean11", 11, v1);
      getEntityManager().persist(aRef);

      // Bi-Directional Relationship access
      // Get B info from A
      final B newB = new B("21", "b39", 39);
      final B newB1 = new B("22", "b49", 49);

      TestUtil.logTrace("Getting B info from entity object A");
      List<B> bInfo = aRef.getBInfoFromA();
      bInfo.add(newB);
      bInfo.add(newB1);
      final A newA = findA("11");

      newA.setBCol(bInfo);
      getEntityManager().flush();

      bInfo = aRef.getBInfoFromA();
      TestUtil.logTrace("dumping B info ...");
      dumpCollectionDataB(bInfo);

      if ((bInfo.size() != 0) && (bInfo.contains(b11)) && (bInfo.contains(b12))
          && (bInfo.contains(newB)) && (bInfo.contains(newB1))) {
        pass = true;
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

    if (!pass) {
      throw new Fault("persist1XMTest10 failed");
    }
  }

  /*
   * @testName: persist1XMTest11
   * 
   * @assertion_ids: PERSISTENCE:SPEC:618; PERSISTENCE:SPEC:622
   * 
   * @test_Strategy: The new entity bean instance becomes both managed and
   * persistent by invoking the persist method on it. The semantics of the
   * persist operation as applied to entity X is as follows: The perist
   * operation is cascaded to entities referenced by X, if the relationship from
   * X to these other entities is annotated with cascade=PERSIST annotation
   * member.
   *
   * Invoke persist on a OneToMany relationship from X annotated with
   * cascade=PERSIST and ensure the persist operation is cascaded.
   *
   * If X is a pre-existing managed entity, it is ignored by the persist
   * operation. However, the persist operation is cascaded to entities
   * referenced by X, if the relationships from X to these other entities is
   * annotated with cascade=PERSIST annotation member value.
   *
   */
  public void persist1XMTest11() throws Fault {
    boolean pass = false;
    A aRef;
    A aRef1;
    B b1;

    try {
      getEntityTransaction().begin();
      b1 = new B("12", "b12", 12);
      final List<B> v1 = new ArrayList<B>();
      v1.add(b1);
      aRef = new A("12", "bean12", 12);
      getEntityManager().persist(aRef);

      if (getEntityManager().contains(aRef)) {
        aRef1 = findA("12");
        aRef1.setBCol(v1);
        getEntityManager().persist(aRef1);
        pass = getEntityManager().contains(b1);
        TestUtil.logTrace("try to find B");
        B b2 = findB("12");
        if (null != b2) {
          TestUtil.logTrace("b2 is not null");
        }
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

    if (!pass) {
      throw new Fault("persist1XMTest11 failed");
    }
  }

  /*
   * @testName: persist1XMTest12
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:642
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * If X is a managed entity, it is synchronized to the database.
   *
   */
  public void persist1XMTest12() throws Fault {
    boolean pass = false;
    A aRef;
    A a2;
    B b1;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      b1 = new B("13", "b13", 13);
      final List<B> v1 = new ArrayList<B>();
      v1.add(b1);
      aRef = new A("13", "bean13", 13, v1);
      getEntityManager().persist(aRef);

      a2 = findA("13");
      if ((null != a2) && (getEntityManager().contains(a2))) {
        List<B> result = a2.getBInfoFromA();
        dumpCollectionDataB(result);
        pass = true;
      } else {
        TestUtil.logErr("Entity not managed - test fails.");
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

    if (!pass) {
      throw new Fault("persist1XMTest12 failed");
    }
  }

  /*
   * @testName: persist1XMTest13
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:644
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * For all entities Y referenced by a relationship from X, if the relationship
   * to Y has been annotated with the cascade member value cascade=PERSIST, the
   * persist operation is applied to Y.
   *
   */
  public void persist1XMTest13() throws Fault {
    boolean pass = false;

    A aRef;
    B b1;

    try {
      TestUtil.logTrace("New instances");
      getEntityTransaction().begin();
      b1 = new B("14", "b14", 14);
      final List<B> v1 = new ArrayList<B>();
      v1.add(b1);
      aRef = new A("14", "bean14", 14, v1);

      getEntityManager().persist(aRef);
      getEntityManager().flush();
      pass = getEntityManager().contains(b1);
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

    if (!pass) {
      throw new Fault("persist1XMTest13 failed");
    }
  }

  /*
   * @testName: persist1XMTest14
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:646
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * For any new entity Y referenced by a relationship from X, where the
   * relationship to Y has not been annotated with the cascade member value
   * cascade=PERSIST, an exception will be thrown by the container or the
   * transaction commit will fail.
   *
   */
  public void persist1XMTest14() throws Fault {
    boolean pass = false;
    B bRef;
    A a1;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      a1 = new A("15", "a15", 15);
      bRef = new B("15", "bean15", 15, a1);

      getEntityManager().persist(bRef);
      getEntityManager().flush();
      getEntityTransaction().commit();
    } catch (Exception e) {
      TestUtil.logTrace("Exception Caught as Expected:" + e.getMessage());
      pass = true;
    } finally {
      try {
        if (getEntityTransaction().isActive()) {
          getEntityTransaction().rollback();
        }
      } catch (Exception fe) {
        TestUtil.logErr("Unexpected exception rolling back TX:", fe);
      }
    }

    if (!pass) {
      throw new Fault("persist1XMTest14 failed");
    }
  }

  /*
   * @testName: persist1XMTest15
   * 
   * @assertion_ids: PERSISTENCE:SPEC:641; PERSISTENCE:SPEC:646
   * 
   * @test_Strategy: The flush method can be used for force synchronization. The
   * semantics of the flush operation applied to an entity X is as follows:
   *
   * For any detached entity Y referenced by a relationship from X, where the
   * relationship to Y has not been annotated with the cascade member value
   * cascade=PERSIST the semantics depend upon the ownership of the
   * relationship. If X owns the relationship, any changes to the relationship
   * are synchronized with the database, otherwise, if Y owns the relationship,
   * the behavior is undefined.
   *
   */
  public void persist1XMTest15() throws Fault {
    A a1;
    B bRef;
    B b2;
    boolean pass = false;

    try {
      getEntityTransaction().begin();
      TestUtil.logTrace("New instances");
      a1 = new A("16", "a16", 16);
      getEntityManager().persist(a1);
      bRef = new B("16", "bean16", 16, a1);
      getEntityManager().persist(bRef);
      getEntityTransaction().commit();

      getEntityTransaction().begin();
      b2 = findB("16");
      A newA = b2.getA1();
      newA.setAName("newA");
      getEntityManager().flush();
      if ((b2.isA()) && (newA.getAName().equals("newA"))) {
        pass = true;
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

    if (!pass) {
      throw new Fault("persist1XMTest15 failed");
    }
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

  private void dumpCollectionDataB(final List<B> c) {
    TestUtil.logTrace("List<B> Data");
    TestUtil.logTrace("---------------");
    TestUtil.logTrace("- size=" + c.size());
    Iterator i = c.iterator();
    int elem = 1;
    while (i.hasNext()) {
      B v = (B) i.next();
      TestUtil.logTrace("- Element #" + elem++);
      TestUtil.logTrace("  id=" + v.getBId() + ", name=" + v.getBName()
          + ", value=" + v.getBValue());
    }
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
      clearCache();
      getEntityManager().createNativeQuery("DELETE FROM BEJB_1XM_BI_BTOB")
          .executeUpdate();
      getEntityManager().createNativeQuery("DELETE FROM AEJB_1XM_BI_BTOB")
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
