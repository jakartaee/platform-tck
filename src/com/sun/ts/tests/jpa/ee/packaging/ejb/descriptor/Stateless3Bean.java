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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;

@Stateless(name = "Stateless3Bean")
@Remote({ Stateless3IF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Stateless3Bean implements Stateless3IF {

  private EntityManagerFactory emf;

  private EntityManager entityManager;

  public SessionContext sessionContext;

  private static final B bRef[] = new B[5];

  private static final A aRef[] = new A[5];

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  @PostConstruct
  public void prepareEnvironment() {
    try {
      TestUtil.logTrace("In PostContruct");
      if (emf == null) {
        emf = (EntityManagerFactory) sessionContext
            .lookup("persistence/MyPersistenceUnit");
      }
    } catch (Exception e) {
      TestUtil.logErr(" In PostConstruct: Unexpected Exception caught", e);
    }
  }

  public void createTestData() {
    TestUtil.logTrace("createTestData");
    try {

      TestUtil.logTrace("joinTransaction");
      entityManager.joinTransaction();

      TestUtil.logTrace("Create 2 A Entities");
      aRef[0] = new A("3", "herB", 3);
      aRef[1] = new A("4", "hisB", 4);

      TestUtil.logTrace("Create 2 B Entities");
      bRef[0] = new B("1", "myB", 1, aRef[0]);
      bRef[1] = new B("2", "yourB", 2, aRef[1]);

      TestUtil.logTrace("Start to persist Bees ");
      for (B b : bRef) {
        if (b != null) {
          entityManager.persist(b);
          TestUtil.logTrace("persisted B " + b);
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected while creating test data:" + e);
    }
  }

  public void removeTestData() {
    TestUtil.logTrace("stateless3Bean removeTestData");

    try {
      if ((entityManager == null) || (!entityManager.isOpen())) {
        entityManager = emf.createEntityManager();
      }
      entityManager.createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB")
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

  public boolean test5() {

    TestUtil.logTrace("Begin test5");
    boolean pass = false;

    try {

      if (emf != null) {
        TestUtil.logTrace("DEBUG: EMF IS NOT null");
        entityManager = emf.createEntityManager();

        if (null != entityManager) {
          TestUtil.logTrace("ENTITYMANAGER IS NOT NULL");

          createTestData();

          B anotherB = entityManager.find(B.class, "1");

          if (anotherB != null) {
            TestUtil.logTrace("anotherB found");
            pass = true;
          }

        } else {
          TestUtil.logErr("ENTITYMANAGER IS NULL");
        }
      } else {
        TestUtil.logErr("EMF is null");
      }
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    } finally {
      try {
        if (entityManager != null) {
          if (entityManager.isOpen()) {
            entityManager.close();
          }
        }
      } catch (IllegalStateException ise) {
        TestUtil.logErr("Unexpected Exception :", ise);
      }
    }

    return pass;
  }

}
