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

package com.sun.ts.tests.jpa.ee.packaging.ejb.standalone;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jpa.ee.common.B;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import java.util.Properties;

@Stateful(name = "Stateful3Bean")
@Remote({ Stateful3IF.class })
public class Stateful3Bean implements Stateful3IF {

  private EntityManager entityManager;

  @PostConstruct
  public void prepareEnvironment() {
    try {
      TestUtil.logTrace("In PostContruct");
      entityManager = (EntityManager) sessionContext
          .lookup("persistence/MyPersistenceContext");
    } catch (Exception e) {
      TestUtil.logErr(
          " In PostConstruct: Exception caught while looking up EntityManager",
          e);
    }
  }

  public SessionContext sessionContext;

  private static final B bRef[] = new B[5];

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  public void createTestData() {
    TestUtil.logTrace("createTestData");

    try {
      TestUtil.logTrace("Create 5 Bees");
      bRef[0] = new B("1", "b1", 1);
      bRef[1] = new B("2", "b2", 2);
      bRef[2] = new B("3", "b3", 3);
      bRef[3] = new B("4", "b4", 4);
      bRef[4] = new B("5", "b5", 5);

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
    try {
      entityManager.createNativeQuery("DELETE FROM BEJB_1X1_BI_BTOB")
          .executeUpdate();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception caught while cleaning up:", e);
    }
    // clear the cache if the provider supports caching otherwise
    // the evictAll is ignored.
    TestUtil.logTrace("Clearing cache");
    entityManager.getEntityManagerFactory().getCache().evictAll();
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

    try {

      createTestData();
      B anotherB = entityManager.find(B.class, "3");

      if (anotherB != null) {
        TestUtil.logTrace("newB found" + anotherB.getName());
        pass = true;
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception :", e);
    }
    return pass;
  }

}
