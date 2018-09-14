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

package com.sun.ts.tests.jpa.ee.packaging.ejb.descriptor;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Stateful3Bean implements Stateful3IF {

  private EntityManager entityManager;

  private EntityManagerFactory emf;

  private Map myMap = new HashMap();

  public SessionContext sessionContext;

  private static final B bRef[] = new B[5];

  private static final C cRef[] = new C[5];

  private EntityManager getEntityManager() {
    TestUtil.logTrace("Look up EntityManagerFactory,get EntityManager");
    try {

      emf = (EntityManagerFactory) sessionContext
          .lookup("persistence/MyPersistenceUnit");

      if (emf != null) {
        entityManager = emf.createEntityManager(myMap);
      } else {
        TestUtil.logErr("EntityManagerFactory is null");
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception caught while setting EntityManager", e);
    }
    return entityManager;
  }

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  public void createTestData() {
    try {

      TestUtil.logTrace("createTestData");

      TestUtil.logTrace("Create 2 B Entities");
      bRef[0] = new B("1", "myB", 1);
      bRef[1] = new B("2", "yourB", 2);

      TestUtil.logTrace("Start to persist Bs ");
      for (B b : bRef) {
        if (b != null) {
          entityManager.persist(b);
          TestUtil.logTrace("persisted B " + b);
        }
      }

      TestUtil.logTrace("Create 2 C Entities");
      cRef[0] = new C("5", "myC", 5);
      cRef[1] = new C("6", "yourC", 6);

      TestUtil.logTrace("Start to persist Cs ");
      for (C c : cRef) {
        if (c != null) {
          entityManager.persist(c);
          TestUtil.logTrace("persisted C " + c);
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected while creating test data:" + e);
    }
  }

  public void removeTestData() {
    TestUtil.logTrace("stateful3Bean removeTestData");

    try {
      if ((entityManager == null) || (!entityManager.isOpen())) {
        entityManager = getEntityManager();
      }
      entityManager.createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB")
          .executeUpdate();
      entityManager.createNativeQuery("DELETE FROM AEJB_1X1_BI_BTOB")
          .executeUpdate();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught while cleaning up:", e);
    } finally {

      if (entityManager.isOpen()) {
        entityManager.close();
      }
    }
    // clear the cache if the provider supports caching otherwise
    // the evictAll is ignored.
    TestUtil.logTrace("Clearing cache");
    emf.getCache().evictAll();
    TestUtil.logTrace("cleanup complete");
  }

  public void init(Properties p) {
    TestUtil.logTrace("init");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean test1() {

    TestUtil.logTrace("Begin test1");
    boolean pass = false;
    EntityManager em = getEntityManager();

    try {
      createTestData();

      B anotherB = em.find(B.class, "1");

      if (anotherB != null) {
        TestUtil.logTrace("anotherB found");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("test1: Unexpected Exception :", e);
    } finally {
      try {
        if (em.isOpen()) {
          em.close();
        }
      } catch (IllegalStateException ise) {
        TestUtil.logErr(
            "Unexpected IllegalStateException caught closing EntityManager",
            ise);
      } catch (Exception e) {
        TestUtil.logErr(
            "Unexpected Exception caught in while closing EntityManager", e);
      }
    }

    return pass;
  }

  public boolean test2() {
    TestUtil.logTrace("Begin test2");
    boolean pass = false;
    EntityManager thisEM = getEntityManager();

    try {
      if (thisEM.isOpen()) {
        TestUtil.logTrace("EntityManager is OPEN, try close");
        thisEM.close();
      }

      if (!thisEM.isOpen()) {
        TestUtil.logTrace("EntityManager isOpen, returns false as expected");
        pass = true;
      } else {
        TestUtil.logErr("EntityManager isOpen, returns false - unexpected");
      }

    } catch (IllegalStateException ise) {
      TestUtil.logErr("Unexpected IllegalStateException caught:", ise);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught:", e);
    }

    return pass;
  }

  public boolean test3() {
    TestUtil.logTrace("Begin test3");
    boolean pass = false;
    EntityManager thatEM = getEntityManager();

    try {
      if (thatEM.isOpen()) {
        thatEM.close();
      }

      if (!thatEM.isOpen()) {
        thatEM.close();
      }
    } catch (IllegalStateException ise) {
      TestUtil.logTrace("IllegalStateException caught as expected");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught in test3", e);
    }

    return pass;
  }

  public boolean test4() {
    TestUtil.logTrace("Begin test4");
    boolean pass = false;
    try {
      EntityManager entityManager = getEntityManager();
      entityManager.getTransaction();

    } catch (IllegalStateException e) {
      TestUtil.logTrace("Caught Expected Exception :" + e);
      pass = true;
    } finally {
      try {
        if (entityManager.isOpen()) {
          entityManager.close();
        }
      } catch (IllegalStateException ise) {
        TestUtil.logErr(
            "Unexpected IllegalStateException caught closing EntityManager",
            ise);
      } catch (Exception e) {
        TestUtil.logErr(
            "Unexpected Exception caught while closing EntityManager", e);
      }
    }

    return pass;
  }

  public boolean test6() {

    TestUtil.logTrace("Begin test6");
    boolean pass = false;
    EntityManager em = getEntityManager();

    try {
      createTestData();

      C c = em.find(C.class, "5");

      if (c != null) {
        TestUtil.logTrace("c found");
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("test1: Unexpected Exception :", e);
    } finally {
      try {
        if (em.isOpen()) {
          em.close();
        }
      } catch (IllegalStateException ise) {
        TestUtil.logErr(
            "Unexpected IllegalStateException caught closing EntityManager",
            ise);
      } catch (Exception e) {
        TestUtil.logErr(
            "Unexpected Exception caught in while closing EntityManager", e);
      }
    }

    return pass;
  }

}
