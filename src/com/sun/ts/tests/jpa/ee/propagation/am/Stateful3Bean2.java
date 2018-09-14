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

package com.sun.ts.tests.jpa.ee.propagation.am;

import com.sun.ts.lib.util.RemoteLoggingInitException;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateful;
import javax.persistence.*;
import java.math.BigInteger;
import java.util.Properties;

@PersistenceUnits({
    @PersistenceUnit(name = "persistence/cau", unitName = "CTS-APPMANAGED-UNIT"),
    @PersistenceUnit(name = "persistence/cau2", unitName = "CTS-APPMANAGED-UNIT2") })
@Stateful(name = "Stateful3Bean2")
@Remote({ Stateful3IF2.class })
public class Stateful3Bean2 implements Stateful3IF2 {

  private static final String EMF_LOOKUP_NAME = "java:comp/env/persistence/cau";

  private static final String EMF_LOOKUP_NAME2 = "java:comp/env/persistence/cau2";

  private EntityManagerFactory entityManagerFactory;

  private EntityManagerFactory entityManagerFactory2;

  public SessionContext sessionContext;

  private EntityManager entityManager;

  private EntityManager entityManager2;

  private static final Member mRef[] = new Member[5];

  private static final Member2 mRef2[] = new Member2[5];

  public Properties p;

  @Resource
  public void setSessionContext(SessionContext sessionContext) {
    this.sessionContext = sessionContext;
  }

  private EntityManagerFactory getEMF() {
    TestUtil.logTrace("In getEMF");

    if (this.entityManagerFactory == null) {
      TestUtil.logTrace("Obtaining EMF");
      try {
        TSNamingContext nctx = new TSNamingContext();
        this.entityManagerFactory = (EntityManagerFactory) nctx
            .lookup(EMF_LOOKUP_NAME);
        if (this.entityManagerFactory == null) {
          TestUtil.logErr("EMF returned by lookup was Null");
        } else {
          TestUtil.logTrace("EMF:" + this.entityManagerFactory.toString());
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught looking up EntityManagerFactory", e);
        System.out.println("Exception caught looking up EntityManagerFactory");
        e.printStackTrace();
      }
    }

    return this.entityManagerFactory;
  }

  private EntityManagerFactory getEMF2() {
    TestUtil.logTrace("In getEMF2");

    if (this.entityManagerFactory2 == null) {
      TestUtil.logTrace("Obtaining EMF");
      try {
        TSNamingContext nctx = new TSNamingContext();
        this.entityManagerFactory2 = (EntityManagerFactory) nctx
            .lookup(EMF_LOOKUP_NAME2);
        if (this.entityManagerFactory2 == null) {
          TestUtil.logErr("EMF returned by lookup was Null");
        } else {
          TestUtil.logTrace("EMF2:" + this.entityManagerFactory2.toString());
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught looking up EntityManagerFactory", e);
        System.out.println("Exception caught looking up EntityManagerFactory");
        e.printStackTrace();
      }
    }
    return this.entityManagerFactory2;
  }

  @PostConstruct
  public void prepareEnvironment() {
    try {
      TestUtil.logTrace("In PostContruct");
      getEMF();
      getEMF2();
      if (entityManagerFactory != null) {
        entityManager = this.entityManagerFactory.createEntityManager();
      } else {
        TestUtil.logErr("EntityManagerFactory is null");
      }
      if (entityManagerFactory2 != null) {
        entityManager2 = this.entityManagerFactory2.createEntityManager();
      } else {
        TestUtil.logErr("EntityManagerFactory is null");
      }
    } catch (Exception e) {
      TestUtil.logErr(
          " In PostConstruct: Exception caught while setting EntityManager", e);
    }
  }

  public void init(final Properties p) {
    TestUtil.logTrace("init");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean test5() {
    boolean pass1 = false;
    boolean pass2 = false;
    boolean pass3 = false;
    boolean pass4 = false;
    try {
      entityManager.joinTransaction();
      removeTestData();
      createTestData();

      entityManager2.joinTransaction();
      createTestData2();

      if (entityManager.contains(mRef[0])) {
        TestUtil.logTrace("Member:" + mRef[0].getMemberId() + " exists");
        pass1 = true;
      } else {
        TestUtil.logErr("Member:" + mRef[0].getMemberId() + " does not exist");
      }
      if (entityManager2.contains(mRef2[0])) {
        TestUtil.logTrace("Member2:" + mRef2[0].getMemberId() + " exists");
        pass2 = true;
      } else {
        TestUtil
            .logErr("Member2:" + mRef2[0].getMemberId() + " does not exist");
      }

      if (!entityManager.contains(mRef2[0])) {
        TestUtil.logTrace("PU " + EMF_LOOKUP_NAME + " does not contain Member2:"
            + mRef2[0].getMemberId() + " as expected");
        pass3 = true;
      } else {
        TestUtil.logErr("PU " + EMF_LOOKUP_NAME
            + " does incorrectly contain Member2:" + mRef2[0].getMemberId());
      }
      if (!entityManager2.contains(mRef[0])) {
        TestUtil.logTrace("PU " + EMF_LOOKUP_NAME2 + " does not contain Member:"
            + mRef[0].getMemberId() + " as expected");
        pass4 = true;
      } else {
        TestUtil.logErr("PU " + EMF_LOOKUP_NAME2
            + " does incorrectly contain Member:" + mRef[0].getMemberId());
      }

    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    } finally {
      TestUtil.logTrace("Finally, removeTestData");
      removeTestData();
      closeEntityManager();
    }
    if (pass1 && pass2 && pass3 && pass4) {
      // test passed
      return true;
    }
    return false;
  }

  public void checkMemberStatus(final Member2 m) {
    System.out.println("checkMemberStatus - joinTransaction");
    int newDonation = 10000;

    if (null == m) {
      TestUtil.logTrace("checkMemberStatus: member is NULL");
    } else {
      if (m.isDuesPaid()) {
        TestUtil.logTrace(
            "checkCustomerStatus: thisMember is not null, setDonation");
        m.setDonation(new BigInteger("10000"));
      } else {
        BigInteger currentDonation = m.getDonation();
        int convertedDonation = 0;
        if (currentDonation != null) {
          convertedDonation = currentDonation.intValue();
        }
        m.setDonation(
            new BigInteger(String.valueOf(convertedDonation + newDonation)));
        m.setDuesPaid(true);
      }

      TestUtil.logTrace("merge thisMember");
      entityManager.flush();
    }
  }

  public Member2 getMember(final int memberId) {
    TestUtil.logTrace("getMember");
    return entityManager.find(Member2.class, memberId);
  }

  public void createTestData() {
    TestUtil.logTrace("createTestData");
    try {

      TestUtil.logTrace("Create Member Entities");
      mRef[0] = new Member(7, "Jane Lam", false);
      mRef[1] = new Member(8, "Vinny Testa", false);
      mRef[2] = new Member(9, "Mario Luigi", true, new BigInteger("25000"));
      mRef[3] = new Member(10, "Sky Blue", false);
      mRef[4] = new Member(11, "Leonardi DaVinci", true,
          new BigInteger("100000"));

      TestUtil.logTrace("Start to persist Members ");
      System.out.println("Persist Member Entities");
      for (Member m : mRef) {
        if (m != null) {
          entityManager.persist(m);
          TestUtil.logTrace("persisted Member " + m);
        }
      }

      entityManager.flush();

    } catch (Exception e) {
      TestUtil.logErr("Unexpected while creating member data:" + e);
    }
  }

  public void createTestData2() {
    TestUtil.logTrace("createTestData2");
    try {

      TestUtil.logTrace("Create Member2 Entities");
      mRef2[0] = new Member2(77, "Jane Lam", false);
      mRef2[1] = new Member2(88, "Vinny Testa", false);
      mRef2[2] = new Member2(99, "Mario Luigi", true, new BigInteger("25000"));
      mRef2[3] = new Member2(100, "Sky Blue", false);
      mRef2[4] = new Member2(111, "Leonardi DaVinci", true,
          new BigInteger("100000"));

      TestUtil.logTrace("Start to persist Members ");
      System.out.println("Persist Member2 Entities");
      for (Member2 m : mRef2) {
        if (m != null) {
          entityManager2.persist(m);
          TestUtil.logTrace("persisted Member2 " + m);
        }
      }

      entityManager2.flush();
    } catch (Exception e) {
      TestUtil.logErr("Unexpected while creating member data:" + e);
    }
  }

  public void removeTestData() {
    TestUtil.logTrace("removeTestData");
    try {
      entityManager.createNativeQuery("DELETE FROM MEMBER").executeUpdate();
    } catch (Exception e) {
      TestUtil.logErr("Exception encountered while removing entities:", e);
    }
    // clear the cache if the provider supports caching otherwise
    // the evictAll is ignored.
    TestUtil.logTrace("Clearing cache");
    entityManagerFactory.getCache().evictAll();
  }

  public void closeEntityManager() {
    try {
      if (entityManager.isOpen()) {
        entityManager.close();
      }
    } catch (Exception e) {
      TestUtil.logErr(
          "closeEntityManager: Unexpected Exception caught while closing "
              + "an Application-Managed EntityManager" + e);
    }
  }
}
